/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.utils;

import com.yeepay.g3.facade.ncpay.util.IsInStringArrayPermitNull;
import com.yeepay.g3.utils.common.ArrayUtils;
import com.yeepay.g3.utils.common.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 类名称: IsInStringArrayValidator <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/14 下午3:30
 * @version: 1.0.0
 */

public class IsInStringArrayPermintNullValidator implements ConstraintValidator<IsInStringArrayPermitNull, String> {

    private String[] permitValue;

    @Override
    public void initialize(IsInStringArrayPermitNull constraintAnnotation) {
        this.permitValue = constraintAnnotation.permitValue();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(ArrayUtils.isEmpty(permitValue) || StringUtils.isBlank(value)) {
            return true;
        }
        for(int i=0; i<permitValue.length; i++) {
            if(value.equals(permitValue[i])) {
                return true;
            }
        }
        return false;
    }
}