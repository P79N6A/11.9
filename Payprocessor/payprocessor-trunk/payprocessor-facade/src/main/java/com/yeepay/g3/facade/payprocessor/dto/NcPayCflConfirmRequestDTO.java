package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class NcPayCflConfirmRequestDTO implements Serializable {
	/**
	 * 支付订单号
	 */
	@NotNull(message = "recordNo不能为空")
	private String recordNo;

	/**
	 * 短信验证码
	 */
	@NotNull(message = "短信验证码不能为空")
	private String smsCode;

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	@Override
	public String toString() {
		return "NcPayCflConfirmRequestDTO{" +
				"recordNo='" + recordNo + '\'' +
				", smsCode='" + smsCode + '\'' +
				'}';
	}
}
