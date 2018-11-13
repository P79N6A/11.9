package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class NcPayConfirmResponseDTO extends BasicResponseDTO {
	/**
	 * 短信校验结果
	 */
	private SmsCheckResultEnum smsStatus;

	public SmsCheckResultEnum getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(SmsCheckResultEnum smsStatus) {
		this.smsStatus = smsStatus;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
