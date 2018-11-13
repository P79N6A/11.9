package com.yeepay.g3.app.nccashier.wap.utils;

import java.lang.reflect.Field;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class BeanValidate {
	
	private static Logger LOGGER = LoggerFactory.getLogger(BeanValidate.class);

	public static void validate(Object o, BindingResult bindingResult) {
		if (o == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			String fieldName = fieldError.getField();
			Field field;
			try {
				field = o.getClass().getDeclaredField(fieldName);
			} catch(Throwable t){
				LOGGER.error("参数校验异常", t);
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
			}
			ErrorDesc errorDesc = (ErrorDesc) field.getAnnotation(ErrorDesc.class);
			if (errorDesc != null && errorDesc.error() != null) {
				Errors errorEnum = errorDesc.error();
				String errorMsg = StringUtils.isBlank(errorDesc.errorMsg()) ? errorEnum.getMsg() : errorDesc.errorMsg();
				LOGGER.warn("参数校验异常,errorCode={}, errorMsg={}", errorEnum.getCode(), errorMsg);
				throw new CashierBusinessException(errorEnum);
			} else {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
			}
		}
	}
	
	
	public static <T> void validate(T object) {
        //获得验证器
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //执行验证
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        //如果有验证信息，则将第一个取出来包装成异常返回
        ConstraintViolation<T> constraintViolation = constraintViolations.iterator().next();
        if (constraintViolation != null) {
           System.out.println(constraintViolation.getInvalidValue());
        }
    }
	
	
}
