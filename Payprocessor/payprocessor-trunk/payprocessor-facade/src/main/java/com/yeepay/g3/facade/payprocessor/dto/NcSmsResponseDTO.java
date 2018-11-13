package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class NcSmsResponseDTO extends ResponseStatusDTO{

	/**
	 * 支付订单号，子表主键
	 */
	protected String recordNo;

	/**
	 * 支付验证码信息
	 */
	private String smsCode;

	/**
	 * 验证码实际发送方式
	 */
	private SmsSendTypeEnum smsSendType;

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public SmsSendTypeEnum getSmsSendType() {
		return smsSendType;
	}

	public void setSmsSendType(SmsSendTypeEnum smsSendType) {
		this.smsSendType = smsSendType;
	}

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NcSmsResponseDTO [recordNo=");
		builder.append(recordNo);
		builder.append(", smsCode=");
		builder.append(HiddenCode.hiddenVerifyCode(smsCode));
		builder.append(", smsSendType=");
		builder.append(smsSendType);
		builder.append(", responseCode=");
		builder.append(responseCode);
		builder.append(", responseMsg=");
		builder.append(responseMsg);
		builder.append(", processStatus=");
		builder.append(processStatus);
		builder.append("]");
		return builder.toString();
	}

	

	
}
