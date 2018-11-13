package com.yeepay.g3.core.nccashier.utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.google.common.collect.Lists;
import com.yeepay.g3.core.nccashier.enumtype.NotifyProtocolEnum;
import com.yeepay.g3.utils.common.CommonUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import com.yeepay.g3.utils.rmi.soa.SoaSupportUtils;
import com.yeepay.g3.utils.rmi.utils.ConfigUtils;

/**
 * 远程任务调用(支持HESSIAN、HTTPINVOKER、DUBBO)
 * 
 * @author haojie.yuan
 *
 */
public class RemoteServiceCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteServiceCaller.class);

    /**
     * classLoader 缓存，提高重复利用率，避免不断创建classLoader导致内存不足
     */
    private static final List<ClassLoader> classLoaderCache = Lists.newArrayList();

    /**
     * class 缓存
     * <p/>
     * key : 包+类名 value: key:方法名 value:value:class
     */
    private static final ConcurrentMap<String, ConcurrentMap<String, Class<?>>> localOptClassCache = new ConcurrentHashMap<String, ConcurrentMap<String, Class<?>>>();

    private static String namespace;

    // 统一配置type值
    // private static final String CONFIG_TYPE = "config_remoteservice";

    // map: namespace -> 1强制走Dubbo, 0强制走RMI
    private static final String RMI_ROUTE_CONFIG_KEY = "RMI_DUBBO_ROUTE";

    public static Object executeRemote(String callbackUrl,Object param, NotifyProtocolEnum remoteServiceProtocol) {
    	return executeRemote(callbackUrl,param,remoteServiceProtocol,null);
    }
    
    /**
     * 描述： 远程任务调用
     *
     * @param callbackUrl
     * @param methodParams
     * @param remoteServiceProtocol
     *            远程服务协议 (HESSIAN/HTTPINVOKER/GET/POST/MQ)
     */
    public static Object executeRemote(String callbackUrl,Object param, NotifyProtocolEnum remoteServiceProtocol,String remoteServiceUrl) {
        LOGGER.info("executeCallback info callbackUrl:{}, methodParams:{}, remoteServiceProtocol:{}", callbackUrl, param, remoteServiceProtocol);

        if (StringUtils.isEmpty(callbackUrl)) {
            throw new RuntimeException("remote callbackUrl is null.");
        }

        // 非 Hessian 和 HttpInvoke 调用走 HttpRequest
        String className;
        String method;
        try {
        	int leftQuto = callbackUrl.indexOf("(");
        	String classMethod = leftQuto > 0 ? callbackUrl.substring(0, leftQuto).trim() : callbackUrl;
        	String paramType = leftQuto > 0 ? callbackUrl.substring(leftQuto).trim() : "";
            className = classMethod.substring(0, classMethod.lastIndexOf(".")).trim();
            method = classMethod.substring(classMethod.lastIndexOf(".") + 1).trim() + paramType;
        } catch (Exception e) {
            throw new RuntimeException("callbackUrl is not right :" + callbackUrl, e);
        }
        return execute(className, method ,param, remoteServiceProtocol, remoteServiceUrl);

    }

    /**
     * 描述： 远程任务调用(支持HESSIAN、HTTPINVOKER)
     *
     * @param className
     * @param method
     * @param methodParams
     * @param remoteServiceProtocol
     *            远程服务协议
     */
    public static void executeRemote(String className, String method,Object param, NotifyProtocolEnum remoteServiceProtocol) {
        execute(className, method,param, remoteServiceProtocol, null);
    }

    /**
     * 描述： 远程任务调用(支持HESSIAN、HTTPINVOKER)
     *
     * @param className
     * @param method
     * @param methodParams
     * @param remoteServiceProtocol
     *            远程服务协议
     * @param remoteServiceUrl
     *            远程服务地址(为空时从统一配置中获取)
     */
    public static void executeRemote(String className, String method,Object param, NotifyProtocolEnum remoteServiceProtocol,
            String remoteServiceUrl) {
        execute(className, method,param, remoteServiceProtocol, remoteServiceUrl);
    }

    /**
     * 描述： 任务执行
     *
     * @param className
     * @param method
     * @param methodParams
     * @param remoteServiceProtocol
     *            远程服务协议
     * @param remoteServiceUrl
     *            远程服务地址
     */
    private static Object execute(String className, String method,Object param, NotifyProtocolEnum remoteServiceProtocol,
            String remoteServiceUrl) {
        LOGGER.info("remote service info className:{}, method:{}, methodParams:{}, remoteServiceProtocol:{}, remoteServiceUrl:{}", className, method,
        		param, remoteServiceProtocol, remoteServiceUrl);

        // 默认为 Hessian 请求，兼容老数据
        if (null == remoteServiceProtocol) {
            remoteServiceProtocol = NotifyProtocolEnum.HESSIAN;
        }

        if (!NotifyProtocolEnum.HESSIAN.equals(remoteServiceProtocol) && !NotifyProtocolEnum.HTTPINVOKER.equals(remoteServiceProtocol)) {
            throw new RuntimeException("remote ServiceProtocol not support.");
        }

        if (StringUtils.isEmpty(className)) {
            throw new RuntimeException("remote interface is null.");
        }

        if (StringUtils.isEmpty(method)) {
            throw new RuntimeException("remote method is null.");
        }

        // 获取任务对应的服务
        Object service = null;
        LOGGER.info("rmi调用获得的远程地址remoteServiceUrl:[{}]", remoteServiceUrl);

        service = getRemoteService(className, method,param,remoteServiceProtocol,remoteServiceUrl);

        if (service == null) {
            throw new RuntimeException("remote service is not found by interface [" + className + "].");
        }

        Object rtn = executeMethod(className, service, method,param);
        LOGGER.info("任务调用成功 {}.{} ", className, method);
        return rtn;
    }

    /**
     * 判断方法是否带参数
     *
     * @param methodName
     * @return
     */
    private static boolean isNoneParamMethod(String methodName) {
        int start = methodName.indexOf("(");
        int end = methodName.indexOf(")");
        return start == -1 || end == -1 || end - start == 1 || methodName.substring(start + 1, end).trim().length() == 0;
    }

    /**
     * 描述： 获取远程服务
     * 策略: 如果任务配置了远程服务的地址,则使用此地址构造出一个hessian远程服务对象
     * 如果任务未配置,则默认使用 RemoteServiceFactory.getService(ReflectUtil.getClass(interface))
     *
     * @param className
     * @param methodDetail
     * @return
     */
    private static Object getRemoteService(String className, String methodDetail,Object param, NotifyProtocolEnum remoteServiceProtocol,String serviceUrl) {
        try {
            Class<?> targetClass = getOrMakeInterface(className, methodDetail, Object.class.getName(), null,param);

            // 远程服务每次都重新获取服务，目的是每次都重新初始化代理，以应对动态增加方法的场景
//            RemoteServiceFactory.deleteService(RemotingProtocol.valueOf(remoteServiceProtocol.getValue()), targetClass);
            if(StringUtils.isNotEmpty(serviceUrl)){
            	return RemoteServiceFactory.getService(serviceUrl,RemotingProtocol.valueOf(remoteServiceProtocol.getValue()), targetClass);
            }else{
            	return RemoteServiceFactory.getService(RemotingProtocol.valueOf(remoteServiceProtocol.getValue()), targetClass);
            }
            
        } catch (Exception e) {
            LOGGER.error("invoke remote service error, className:" + className + ", methodDetail:" + methodDetail, e);
            throw new RuntimeException(e);
        }
    }

    private static CtClass[] getParamsCtClass(String[] params) {
        CtClass[] paramsClass = new CtClass[params.length];
        ClassPool cp = getClassPool();
        try {
            for (int i = 0; i < params.length; i++) {
                paramsClass[i] = cp.getCtClass(params[i].trim());
            }
        } catch (NotFoundException e) {
            throw new RuntimeException("param class " + params + " is not found.");
        }
        return paramsClass;
    }

    /**
     * 动态创建接口
     * <p/>
     * 如果已经创建过且没有目标方法则在新的 classLoader 中生成新的 class，但该 class 只包含目标方法，不拷贝在其他 classLoader 中已存在的方法
     *
     * @param className
     *            类全名
     * @param methodDetail
     *            方法全名
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static Class<?> getOrMakeInterface(String className, String methodDetail, String returnValueClassName, String[] exceptionClassNames,Object param)
            throws NotFoundException, CannotCompileException {
        // 先尝试在缓存中查找
        Class<?> cla = getClassFromCache(className, methodDetail);
        if (null != cla) {
            LOGGER.info("Load targetClass from cache, className:{}, methodDetail:{}", className, methodDetail);
            return cla;
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassPool pool = getClassPool(classLoader);

        // 如果缓存中不存在要找的 ctClass 则在当前线程的 classPool 中创建一个新的
        CtClass ctClass = null;
        boolean newCtClass = false;
        try {
            ctClass = pool.get(className);
        } catch (NotFoundException e) {
            LOGGER.info("Not found in classPool, create new ctClass:{}", className);
            pool.makePackage(classLoader, className.substring(0, className.lastIndexOf(".")));
            ctClass = pool.makeInterface(className);
            newCtClass = true;
        }

        // 方法参数设定
        String methodName = null;
        String params = null;
        if (isNoneParamMethod(methodDetail)) {
        	methodName = methodDetail.replace("(", "").replace(")", "").trim();
        	if(methodDetail.contains("(")){
        	}
        	else{
        		if(null!=param){
        			if(param.getClass().isArray()){
        				Object[] pp = (Object[])param;
        				String dot = "";
        				params = "";
        				for(int i=0; i!=pp.length; i++){
        					params = params + dot + pp[i].getClass().getName();
        					dot = ",";
        				}
        			}
        			else{
        				params = param.getClass().getName();
        			}
        		}
        	}
        } else {
            methodName = methodDetail.substring(0, methodDetail.indexOf("("));
            params = methodDetail.substring(methodDetail.indexOf("(") + 1, methodDetail.indexOf(")"));
        }

        if (checkMethodExistsInCtClass(ctClass, methodName, params)) {
            // 不应该请求到这里的, 但也不排除奇迹的发生
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                LOGGER.error("Much sleep last night, can't sleep", e1);
            }
            cla = getClassFromCache(className, methodDetail);
            if (null != cla) {
                LOGGER.error("Oh! It's fantastic. Load targetClass from cache, className:{}, methodDetail:{}", className, methodDetail);
                return cla;
            }
        } else {
            LOGGER.info("Add a new method to ctClass:{}, method:{}, params:{}, return:{}, exception:{}", className, methodName, params, returnValueClassName,
                    exceptionClassNames);
            CtClass returnValueClass = returnValueClassName != null ? pool.get(returnValueClassName) : CtClass.voidType;
            // 至今未解决的蛋疼问题，这个方法应该也是好的？
            CtClass[] paramClass = params != null ? pool.get(params.split(",")) : null;
            CtClass[] exceptionClass = exceptionClassNames != null ? pool.get(exceptionClassNames) : null;

            // 修改 ctClass 前需求保证其可被修改
            ctClass.defrost();
            CtMethod ctMethod = CtNewMethod.abstractMethod(returnValueClass, methodName, paramClass, exceptionClass, ctClass);
            ctClass.addMethod(ctMethod);

            if (!newCtClass) {
                // 更换 classLoader
                int index = localOptClassCache.get(className).size();
                if (index < classLoaderCache.size()) {
                    classLoader = classLoaderCache.get(index);
                } else {
                    classLoader = new RemoteClassLoader();
                    classLoaderCache.add(classLoader);
                }
            }
        }

        // 同一个ClassLoader是不允许多次加载一个类的，否则会报java.lang.LinkageError。attempted duplicate class definition for
        try {
            cla = ctClass.toClass(classLoader, classLoader.getClass().getProtectionDomain());
        } catch (CannotCompileException e) {
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(className.trim());
            } catch (ClassNotFoundException cnfe) {
                // do nothing
            }
        }

        LOGGER.info("Add class to cache:{}, method:{}", className, methodDetail);
        localOptClassCache.get(className).putIfAbsent(methodDetail, cla);
        return cla;
    }

    /**
     * 从缓存中获取 class
     *
     * @param className
     * @param methodDetail
     * @return
     */
    private static Class<?> getClassFromCache(String className, String methodDetail) {
        Class<?> targetClass = null;
        ConcurrentMap<String, Class<?>> classMap = localOptClassCache.get(className);
        if (null == classMap) {
            localOptClassCache.putIfAbsent(className, new ConcurrentHashMap<String, Class<?>>(4));
        } else {
            targetClass = classMap.get(methodDetail);
        }
        return targetClass;
    }

    private static Object executeMethod(String className, Object target, String methodFullName,Object param) {
        String methodName = methodFullName;
        
        Class<?>[] paramTypes = null;

        if (methodFullName.contains("(")) {
            methodName = methodName.substring(0, methodName.indexOf("(")).trim();
            String quto = methodFullName.substring(methodFullName.indexOf("(")+1, methodFullName.indexOf(")")).trim();
            if(null != quto && !quto.isEmpty() ){
            	String[] types = quto.split(",");
	            paramTypes = new Class<?>[types.length];
	            for(int i=0; i!=types.length; i++){
	            	try {
						paramTypes[i] = Class.forName(types[i].trim());
					} catch (ClassNotFoundException e) {
						LOGGER.error("找不到类："+types[i], e);
					}
	            }
            }
        }
        else if(null != param){
        	if(param.getClass().isArray()){
        		Object[] pp = (Object[])param;
        		paramTypes = new Class<?>[pp.length];
        		for(int i=0; i!=pp.length; i++){
        			paramTypes[i] = pp[i].getClass();
        		}
        	}
        	else{
        		paramTypes = new Class<?>[] {param.getClass()};
        	}
        }

        try {
            // 从远程调用缓存中获取该方法
            Method method = getMethod(target, methodName, paramTypes);
            if (method == null) {
                // 尝试从本地缓存获取该方法并刷新到远程调用缓存
                Class<?> targetClass = getOrMakeInterface(className, methodFullName, null, null,param);
                RemoteServiceFactory.deleteService(RemotingProtocol.HESSIAN, targetClass);
                RemoteServiceFactory.deleteService(RemotingProtocol.HTTPINVOKER, targetClass);
                target = RemoteServiceFactory.getService(targetClass);
                method = getMethod(target, methodName, paramTypes);
                if (method == null) {
                    // 实在没救了，走好，不送
                    throw new RuntimeException("method [" + methodName + "] with param + [" + paramTypes + "] is not found on target "
                            + target.getClass().getName());
                }
            }
            if(null == paramTypes || paramTypes.length == 0){
            	return method.invoke(target);
            }
            else if(paramTypes.length == 1){
            	return method.invoke(target, param);
            }
            else if(paramTypes.length == 2){
            	return method.invoke(target, ((Object[])param)[0], ((Object[])param)[1]);
            }
            else if(paramTypes.length == 3){
            	return method.invoke(target, ((Object[])param)[0], ((Object[])param)[1], ((Object[])param)[2]);
            }
            else if(paramTypes.length == 4){
            	return method.invoke(target, ((Object[])param)[0], ((Object[])param)[1], ((Object[])param)[2], ((Object[])param)[3]);
            }
            else if(paramTypes.length == 5){
            	return method.invoke(target, ((Object[])param)[0], ((Object[])param)[1], ((Object[])param)[2], ((Object[])param)[3], ((Object[])param)[4]);
            }
            else{
            	throw new Exception("method param size is begger than 5 !");
            }
        } catch (Exception e) {
            LOGGER.error("执行remote时服务调用报错", e);
            throw new IllegalArgumentException("invoke [" + className + "." + methodName + "] error.", e);
        }
    }

    private static boolean checkMethodExistsInCtClass(CtClass ctClass, String methodName, String params) {
        LOGGER.info("To found method:{} with params:{} in ctClass:{}", methodName, params, ctClass.getName());
        CtClass[] paramTypes = params != null ? getParamsCtClass(params.split(",")) : null;

        try {
            ctClass.getDeclaredMethod(methodName, paramTypes);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private static Method getMethod(Object target, String methodName, Class<?>[] paramTypes) {
        try {
            return target.getClass().getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static void invokeDubboService(String className, String methodFullName, Map<String, Object> methodParams, NotifyProtocolEnum remoteServiceProtocol) {
        LOGGER.info("调用dubbo服务：className:[{}],method:[{}]", className, methodFullName);
        // 引用远程服务
        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>(); // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
        reference.setInterface(className); // 弱类型接口名
        reference.setCheck(false);
        reference.setGeneric(true); // 声明为泛化接口
        reference.setProtocol(remoteServiceProtocol.getValue().toLowerCase());

        String methodName = methodFullName;
        if (methodFullName.contains("(")) {
            methodName = methodName.substring(0, methodName.indexOf("(")).trim();
        }

        String[] paramTypes = methodParams == null ? null : new String[] { "java.util.Map" };
        Object[] paramValues = methodParams == null ? null : new Object[] { methodParams };

        GenericService genericService = reference.get(); // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用

        // 基本类型以及Date,List,Map等不需要转换，直接调用
        genericService.$invoke(methodName, paramTypes, paramValues);

        LOGGER.info("调用DUBBO成功DUBBO:url:[{}]:client:[{}],", reference.getUrl(), reference.getClient());
    }

    /**
     * 从统一配置获取 Dubbo 路由规则（默认不走Dubbo）
     *
     * @param className
     *            服务类
     * @return
     */
    private static boolean isSoaService(String classPackage) {
        boolean isSoaService = false;
        if (StringUtils.isBlank(namespace)) {
            synchronized (SoaSupportUtils.class) {
                if (StringUtils.isBlank(namespace)) {
                    LOGGER.debug("加载namespace");
                    try {
                        Map<String, String> props = CommonUtils.loadProps("runtimecfg/yeepay-config.properties");
                        namespace = props.get("namespace");
                    } catch (Exception e) {
                        LOGGER.warn("获取命名空间异常", e);
                    }
                    if (StringUtils.isBlank(namespace)) {
                        namespace = "default";
                    }
                }
            }
        }

        if ("default".equals(namespace)) {
            Map<String, String> routeMap = ConfigUtils.getSysConfigMap(RMI_ROUTE_CONFIG_KEY);
            if (null != routeMap && routeMap.size() > 0) {
                String value = routeMap.get(classPackage);
                if (null != value && "1".equals(value)) {
                    isSoaService = true;
                }
            }
        }
        return isSoaService;
    }

    private static ClassPool getClassPool() {
        return getClassPool(Thread.currentThread().getContextClassLoader());
    }

    private static ClassPool getClassPool(ClassLoader classLoader) {
        ClassPool pool;
        try {
            Class<?> classGenerator = Class.forName("com.alibaba.dubbo.common.bytecode.ClassGenerator");
            Method m = classGenerator.getDeclaredMethod("getClassPool", ClassLoader.class);
            pool = (ClassPool) m.invoke(null, classLoader);
        } catch (Throwable t) {
            pool = new ClassPool(true);
            if (classLoader != null) {
                pool.appendClassPath(new LoaderClassPath(classLoader));
            }
        }
        return pool;
    }

}
