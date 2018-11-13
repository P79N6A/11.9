package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class NcSmsRequestDTO implements Serializable {

	/**
	 * 支付订单号
	 */
	@NotNull(message = "recordNo不能为空")
	private String recordNo;

	/**
	 * 验证码类型
	 */
	private ReqSmsSendTypeEnum smsSendType;

	/**
	 * 需要补充的临时卡信息ID
	 */
	private Long tmpCardId;

	/**
	 * 卡信息
	 */
	private BankCardInfoDTO bankCardInfoDTO;

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	public ReqSmsSendTypeEnum getSmsSendType() {
		return smsSendType;
	}

	public void setSmsSendType(ReqSmsSendTypeEnum smsSendType) {
		this.smsSendType = smsSendType;
	}

	public Long getTmpCardId() {
		return tmpCardId;
	}

	public void setTmpCardId(Long tmpCardId) {
		this.tmpCardId = tmpCardId;
	}

	public BankCardInfoDTO getBankCardInfoDTO() {
		return bankCardInfoDTO;
	}

	public void setBankCardInfoDTO(BankCardInfoDTO bankCardInfoDTO) {
		this.bankCardInfoDTO = bankCardInfoDTO;
	}

	@Override
	public String toString() {
		return "NcSmsRequestDTO{" +
				"recordNo='" + recordNo + '\'' +
				", smsSendType=" + smsSendType +
				", tmpCardId=" + tmpCardId +
				", bankCardInfoDTO=" + bankCardInfoDTO +
				'}';
	}
}
