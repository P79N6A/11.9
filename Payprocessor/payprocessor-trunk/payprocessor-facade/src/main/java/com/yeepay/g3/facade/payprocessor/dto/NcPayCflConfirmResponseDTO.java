package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class NcPayCflConfirmResponseDTO extends BasicResponseDTO {

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
