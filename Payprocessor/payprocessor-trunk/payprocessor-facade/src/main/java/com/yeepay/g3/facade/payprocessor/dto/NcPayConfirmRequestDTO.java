package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class NcPayConfirmRequestDTO implements Serializable {
	/**
	 * 支付订单号
	 */
	@NotNull(message = "recordNo不能为空")
	private String recordNo;

	/**
	 * 需要补充的临时卡信息ID
	 */
	private Long tmpCardId;

	private String smsCode;
	
	private Map<String,String> extParam;

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

	public Long getTmpCardId() {
		return tmpCardId;
	}

	public void setTmpCardId(Long tmpCardId) {
		this.tmpCardId = tmpCardId;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	/**
	 * @return the extParam
	 */
	public Map<String,String> getExtParam() {
		return extParam;
	}

	/**
	 * @param extParam the extParam to set
	 */
	public void setExtParam(Map<String,String> extParam) {
		this.extParam = extParam;
	}

	public BankCardInfoDTO getBankCardInfoDTO() {
		return bankCardInfoDTO;
	}

	public void setBankCardInfoDTO(BankCardInfoDTO bankCardInfoDTO) {
		this.bankCardInfoDTO = bankCardInfoDTO;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NcPayConfirmRequestDTO [recordNo=");
		builder.append(recordNo);
		builder.append(", tmpCardId=");
		builder.append(tmpCardId);
		builder.append(", smsCode=");
		builder.append(HiddenCode.hiddenVerifyCode(smsCode));
		builder.append(", extParam=");
		builder.append(extParam);
		builder.append(", bankCardInfoDTO=");
		builder.append(bankCardInfoDTO);
		builder.append("]");
		return builder.toString();
	}

}
