package com.yeepay.g3.app.nccashier.wap.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LogInfoEncryptUtil {

	public static Map<String,String> sensitiveInfoConvert(Map<String,Object> inputParamMap){
		if(inputParamMap == null){
			return null;
		}
		
		Map<String,String> outParamMap = new HashMap<String,String>();
		for (Entry<String,Object> entry : inputParamMap.entrySet()) {
			String key = entry.getKey();
			String value = LogInfoEncryptUtil.getLogString(entry.getValue());
			if(StringUtils.isNotEmpty(value) && value.indexOf("*")>-1){
				outParamMap.put(key,value);
			}else if ("cardno".equalsIgnoreCase(key)) {
				outParamMap.put(key,HiddenCode.hiddenBankCardNO(value));
			}else if("phone".equalsIgnoreCase(key) || "phoneNo".equalsIgnoreCase(key) || "ypMobile".equalsIgnoreCase(key)) {
				outParamMap.put(key,HiddenCode.hiddenMobile(value));
			}else if ("idno".equalsIgnoreCase(key)) {
				outParamMap.put(key,HiddenCode.hiddenIdentityCode(value));
			}else if ("name".equalsIgnoreCase(key) || "owner".equalsIgnoreCase(key) || "username".equalsIgnoreCase(key) ) {
				outParamMap.put(key,HiddenCode.hiddenName(value));
			}else if (key!=null &&(key.contains("cvv") || key.contains("pass") || key.contains("valid")
				|| key.contains("verifycode"))) {
				continue;
			}else if ("userno".equalsIgnoreCase(key)) {
				outParamMap.put(key,HiddenCode.hiddenIdentityId(value));
			}else{
				outParamMap.put(key,value);
			}
		}
		return outParamMap;
	}
	
	public static <T> String getLogString(T object) {
		if (object == null) {
			return null;
		}
		if (object.getClass().isArray()) {
			return getArrayLogString((Object[]) object);
		} else {
			return getObjectString(object);
		}

	}

	private static String getArrayLogString(Object[] object) {
		StringBuffer sb = new StringBuffer();
		Object[] arguments = object;
		for (Object arg : arguments) {
			sb.append(getObjectString(arg));
		}
		return sb.toString();
	}

//	private static <T> String getSingleObjectLogString(T object) {
//		if (object == null) {
//			return null;
//		}
//		if (object instanceof HttpServletRequest) {
//			HttpServletRequest request = (HttpServletRequest) object;
//			Set<Entry> set = request.getParameterMap().entrySet();
//			StringBuffer sb = new StringBuffer();
//			for (Entry entry : set) {
//				sb.append(entry.getKey().toString() + ":"
//						+ request.getParameter(entry.getKey().toString()) + " ");
//			}
//			return sb.toString();
//		}
		
//		return getObjectString(object);
//	}


	/**
	 * 获取对象的显示信息.（对敏感信息进行掩码)
	 * 
	 * @param o
	 * @return
	 */
	private static <T> String getObjectString(T o) {
		if (null == o)
			return null;
		if (isOverrideToString(o)) {
			return o.toString();
		}
		return ToStringBuilder.reflectionToString(o);

	}

	private static boolean isOverrideToString(Object o) {
		if (o == null) {
			return false;
		}
		try {
			o.getClass().getDeclaredMethod("toString", new Class[] {});
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}
}
