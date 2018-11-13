package com.yeepay.g3.facade.nccashier.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yeepay.g3.facade.nccashier.error.Errors;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface ErrorDesc {

	Errors error() default Errors.SYSTEM_EXCEPTION;

	String errorMsg() default "";
}
