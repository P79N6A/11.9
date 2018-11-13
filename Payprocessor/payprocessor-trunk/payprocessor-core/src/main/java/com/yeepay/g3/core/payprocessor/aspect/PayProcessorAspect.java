package com.yeepay.g3.core.payprocessor.aspect;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.alibaba.dubbo.common.utils.Assert;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.util.log.LogInfoEncryptUtil;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.ResponseStatusDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

/**
* @Description:切面
 * 处特定接口外,内部接口调用都会经过这里,进行参数校验、日志打印等操作
* @author Minglong
* @date 2016年8月17日 下午2:20:49
 */
@Aspect
public class PayProcessorAspect {

	private static final Logger logger = PayLoggerFactory.getLogger(PayProcessorAspect.class);
	
	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
    @Pointcut("execution(* com.yeepay.g3.facade.payprocessor.facade.*Facade.*(..)) ")
    private void doPayProcessorAspect() {
    }

    @Around("doPayProcessorAspect()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    	String methodName = joinPoint.getSignature().getName();
    	MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String name = joinPoint.getTarget().getClass().getSimpleName();
		// method = joinPoint.getSignature().getName();
		Object result = method.getReturnType().newInstance();
		try {
			Object[] arguments = joinPoint.getArgs();
			logger.info("[" + name + "入参] - [" + methodName + "] - "
					+ LogInfoEncryptUtil.getLogString(arguments) + "]");
			validate(arguments);
			result = joinPoint.proceed();
		} catch (Throwable e) {
			handelException(methodName, (ResponseStatusDTO)result, e);
		} 
		logger.info("[" + name + "返回结果] - [" + methodName + "] - " + LogInfoEncryptUtil.getLogString(result));
		return result;
	}
    
    private void handelException(String methodName, ResponseStatusDTO result, Throwable e) {
		if(result != null && e != null){
			logger.error(" - [ " + methodName + " ] - [异常] ", e);
			PayBizException payBizException = null;
			if(e instanceof PayBizException){
				payBizException = (PayBizException) e;
			}else{
				payBizException = new PayBizException(ErrorCode.P9001000);
			}
			result.setResponseCode(payBizException.getDefineCode());
			result.setResponseMsg(payBizException.getMessage());
			result.setProcessStatus(ProcessStatus.FAILED);
		}
	}
    
    
    /**
	 * 根据Bean中的注解配置验证Bean的参数合法性
	 * 
	 * @param <E>
	 * @param obj
	 *            待验证对象
	 */
	@SuppressWarnings("unchecked")
	private <E> void validate(Object obj) {
		Assert.notNull(obj, "request parameter can't be null");
		Object[] parameters;
		if(obj.getClass().isArray()) {
			parameters = (Object[]) obj;
		}else {
			parameters = new Object[] {obj};
		}
		for(Object param : parameters) {
			if(param instanceof OpenPayRequestDTO){
				OpenPayRequestDTO openPayRequestDTO = (OpenPayRequestDTO) param;
				if(StringUtils.isBlank(openPayRequestDTO.getPayerIp())){
					throw new PayBizException(ErrorCode.P9001001, "开发支付需要传payerIp");
				}
				//payerIp
			} else if(param instanceof NcPayOrderRequestDTO){
				NcPayOrderRequestDTO ncPayRequestDTO = (NcPayOrderRequestDTO) param;
				if (StringUtils.isBlank(ncPayRequestDTO.getPayScene())) {
					throw new PayBizException(ErrorCode.P9001001, "一键支付需要传payScene");
				}
				if (StringUtils.isBlank(ncPayRequestDTO.getIndustryCode())) {
					throw new PayBizException(ErrorCode.P9001001, "一键支付需要传industryCode");
				}	
				
			} else if(param instanceof PersonalMemberSyncPayRequestDTO) {
				PersonalMemberSyncPayRequestDTO personalMemberSyncPayRequestDTO = (PersonalMemberSyncPayRequestDTO)param;
				if (StringUtils.isBlank(personalMemberSyncPayRequestDTO.getMemberNo())) {
					throw new PayBizException(ErrorCode.P9001001, "会员支付需要传memberNo");
				}	
			}  
			
			Set<ConstraintViolation<E>> set = validator.validate((E) param);
			if (set.size() != 0) {
				throw new PayBizException(ErrorCode.P9001001, "参数不合法["
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
