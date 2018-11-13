package com.yeepay.g3.core.nccashier.validator;

import java.lang.reflect.Field;

import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;

public class BeanValidator {

	/**
	 * 根据Bean中的注解配置验证Bean的参数合法性
	 * 
	 * @param <E>
	 * @param obj 待验证对象
	 */
	public static <E> void validate(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException("validate object must not be null");
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		Field[] parentFields = obj.getClass().getSuperclass().getDeclaredFields();
		Field[] allFields = new Field[fields.length + parentFields.length];
		System.arraycopy(fields, 0, allFields, 0, fields.length);
		System.arraycopy(parentFields, 0, allFields, fields.length, parentFields.length);
		for (Field field : allFields) {
			NotEmptyValidate notEmpty =
					(NotEmptyValidate) field.getAnnotation(NotEmptyValidate.class);
			if (notEmpty != null) {
				notEmptyValidate(obj, field);
			}
			NumberValidate numValidate = (NumberValidate) field.getAnnotation(NumberValidate.class);
			if (numValidate != null) {
				Object value = BeanUtils.getProperty(obj, field.getName());
				if (value == null) {
					throwException(obj, field);
				}
				if (field.getType() == java.lang.Long.class
						|| "long".equals(field.getType().getName())) {
					Long l = (Long) value;
					if (l == 0) {
						throwException(obj, field);
					}
				} else if (field.getType() == java.lang.Integer.class
						|| "int".equals(field.getType().getName())) {
					Integer i = (Integer) value;
					if (i == 0) {
						throwException(obj, field);
					}
				}
			}

		}

	}

	private static void notEmptyValidate(Object obj, Field field) {
		boolean isEmpty = false;
		Object value = BeanUtils.getProperty(obj, field.getName());
		if (field.getType() == java.lang.String.class) {
			isEmpty = StringUtils.isBlank((String) value);
		} else {
			isEmpty = (value == null) ? true : false;
		}
		if (isEmpty) {
			throwException(obj, field);
		}
	}

	private static void throwException(Object obj, Field field) {
		ErrorDesc errorDesc = (ErrorDesc) field.getAnnotation(ErrorDesc.class);
		if (errorDesc != null && errorDesc.error() != null) {
			Errors errorEnum = errorDesc.error();
			String errorMsg = 
					StringUtils.isBlank(errorDesc.errorMsg()) ? CommonUtil.handleException(errorEnum).getMessage()
					: errorDesc.errorMsg();
			throw new CashierBusinessException(errorEnum.getCode(), errorMsg);
		} else {
			throw new IllegalArgumentException(
					"验证" + obj.getClass().getSimpleName() + " " + field.getName() + "  is null");
		}
	}

}
