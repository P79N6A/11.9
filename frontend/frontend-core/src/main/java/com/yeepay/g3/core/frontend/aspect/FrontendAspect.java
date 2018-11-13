package com.yeepay.g3.core.frontend.aspect;

import com.alibaba.dubbo.common.utils.Assert;
import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.util.CommonUtils;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.BasicRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.ResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Minglong
 * @Description:
 * @date 2016年8月17日 下午2:20:49
 */
@Aspect
public class FrontendAspect {

    private static final Logger logger = FeLoggerFactory.getLogger(FrontendAspect.class);

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Pointcut("execution(* com.yeepay.g3.facade.frontend.facade.*Facade.*(..)) ")
    private void doFrontendAspect() {
    }

    @Around("doFrontendAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object result = method.getReturnType().newInstance();
        try {
            Object[] arguments = joinPoint.getArgs();
            validate(arguments);
            result = joinPoint.proceed();
        } catch (Throwable e) {
            handelException(methodName, (ResponseDTO) result, e);
        }
        return result;
    }

    private void handelException(String methodName, ResponseDTO result, Throwable e) {
        if (result != null && e != null) {
            logger.error(" - [ " + methodName + " ] - [异常] ", e);
            FrontendBizException frontendBizException = null;
            if (e instanceof FrontendBizException) {
                frontendBizException = (FrontendBizException) e;
            } else {
                frontendBizException = new FrontendBizException(ErrorCode.F0001000);
            }
            result.setResponseCode(frontendBizException.getDefineCode());
            result.setResponseMsg(frontendBizException.getMessage());
        }
    }


    /**
     * 根据Bean中的注解配置验证Bean的参数合法性
     *
     * @param <E>
     * @param obj 待验证对象
     */
    @SuppressWarnings("unchecked")
    private <E> void validate(Object obj) {
        Assert.notNull(obj, "request parameter can't be null");
        Object[] parameters;
        if (obj.getClass().isArray()) {
            parameters = (Object[]) obj;
        } else {
            parameters = new Object[]{obj};
        }
        for (Object param : parameters) {
            if (param instanceof BasicRequestDTO) {
                BasicRequestDTO requestDTO = (BasicRequestDTO) param;
                if (requestDTO.getRequestSystem() != null && !CommonUtils.containsRequestSystem(requestDTO.getRequestSystem())) {
                    throw new FrontendBizException(ErrorCode.F0001001, "参数不合法["
                            + requestDTO.getRequestSystem()
                            + "非法]");
                }
            }
            if (param instanceof PayRequestDTO) {
                PayRequestDTO payRequestDTO = (PayRequestDTO) param;
                if (OrderType.JSAPI.equals(payRequestDTO.getOrderType())
                        && StringUtils.isBlank(payRequestDTO.getPayInterface())) { //StringUtils.isBlank(payRequestDTO.getOpenId()) || StringUtils.isBlank(payRequestDTO.getAppId()) ||
                    throw new FrontendBizException(ErrorCode.F0001001, "公众号支付需要payInterface");
                } else if (OrderType.PASSIVESCAN.equals(payRequestDTO.getOrderType())
                        && (StringUtils.isBlank(payRequestDTO.getPayEmpowerNo())
                        || StringUtils.isBlank(payRequestDTO.getMerchantStoreNo())
                        || StringUtils.isBlank(payRequestDTO.getMerchantTerminalId()))) {
                    throw new FrontendBizException(ErrorCode.F0001001, "被扫支付需要传payEmpowerNo,merchantStoreNo,merchantTerminalId");
                } else if (OrderType.LN.equals(payRequestDTO.getOrderType())
                        && (StringUtils.isBlank(payRequestDTO.getPayerAccountNo())
                        && StringUtils.isBlank(payRequestDTO.getOpenId()))) {
                    throw new FrontendBizException(ErrorCode.F0001001, "支付宝生活号需要payerAccountNo或openId");
                }
            }
            Set<ConstraintViolation<E>> set = validator.validate((E) param);
            if (set.size() != 0) {
                throw new FrontendBizException(ErrorCode.F0001001, "参数不合法["
                        + getMergedMessage(set)
                        + "]");
            }
        }

    }


    private String getMergedMessage(Set set) {
        StringBuilder sb = new StringBuilder("");
        for (Object obj : set) {
            if (obj instanceof ConstraintViolation) {
                ConstraintViolation constraintViolation = (ConstraintViolation) obj;
                sb.append(constraintViolation.getPropertyPath());
                sb.append(" ");
                sb.append(constraintViolation.getMessage());
                sb.append(" ");
            }
        }
        return sb.toString();
    }


}
