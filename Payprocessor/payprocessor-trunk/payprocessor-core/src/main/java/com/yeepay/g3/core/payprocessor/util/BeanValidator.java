package com.yeepay.g3.core.payprocessor.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import com.alibaba.dubbo.common.utils.Assert;

public class BeanValidator {

	private static Validator validator;
	static {
		ValidatorFactory validatorFactory = Validation
				.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	public static String getMergedMessage(Set set) {
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

	/**
	 * 根据Bean中的注解配置验证Bean的参数合法性
	 * 
	 * @param <E>
	 * @param obj
	 *            待验证对象
	 */
	@SuppressWarnings("unchecked")
	public static <E> void validate(Object obj) {
		Assert.notNull(obj, "request parameter can't be null");
		Object[] parameters;
		if(obj.getClass().isArray()) {
			parameters = (Object[]) obj;
		}else {
			parameters = new Object[] {obj};
		}
		for(Object param : parameters) {
			Set<ConstraintViolation<E>> set = validator.validate((E) param);
			if (set.size() != 0) {
				throw new IllegalArgumentException("参数不合法["
						+ getMergedMessage(set)
						+ "]");
			}
		}

	}

}
