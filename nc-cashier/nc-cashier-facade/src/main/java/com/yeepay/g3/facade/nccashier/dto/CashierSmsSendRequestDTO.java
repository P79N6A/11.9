package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

public class CashierSmsSendRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 281323404327143643L;
	
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="tokenId未传")
	private String tokenId;
	
	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="recordId未传")
	private long recordId = 0;
	
	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="requestId未传")
	private long requestId = 0;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="reqSmsSendTypeEnum未传")
	private ReqSmsSendTypeEnum reqSmsSendTypeEnum;
	
	
	private NeedBankCardDTO needBankCardDTO;
	
	public long getRequestId() {
		return requestId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public ReqSmsSendTypeEnum getReqSmsSendTypeEnum() {
		return reqSmsSendTypeEnum;
	}

	public void setReqSmsSendTypeEnum(ReqSmsSendTypeEnum reqSmsSendTypeEnum) {
		this.reqSmsSendTypeEnum = reqSmsSendTypeEnum;
	}
	
	/**
	 * @return the needBankCardDTO
	 */
	public NeedBankCardDTO getNeedBankCardDTO() {
		return needBankCardDTO;
	}

	/**
	 * @param needBankCardDTO the needBankCardDTO to set
	 */
	public void setNeedBankCardDTO(NeedBankCardDTO needBankCardDTO) {
		this.needBankCardDTO = needBankCardDTO;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierSmsSendRequestDTO{");
		sb.append("tokenId='").append(this.tokenId).append('\'');
		sb.append(", requestId='").append(this.requestId).append('\'');
		sb.append(", recordId='").append(this.recordId).append('\'');
		sb.append(", reqSmsSendTypeEnum='").append(this.reqSmsSendTypeEnum==null?"null":this.reqSmsSendTypeEnum.name()).append('\'');
		sb.append(", needBankCardDTO='").append(needBankCardDTO).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
