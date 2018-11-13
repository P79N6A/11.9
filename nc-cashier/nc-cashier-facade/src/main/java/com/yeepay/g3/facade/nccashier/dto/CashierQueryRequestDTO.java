package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

public class CashierQueryRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 853131486488028993L;
	
	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="recordId未传")
	private long recordId = 0;
	
	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="requestId未传")
	private long requestId = 0;
	
	/**
	 * 是否是重新查询
	 */
	private boolean isRepeatQuery = false;
	
	/**
	 * 是否需要查询redis标识
	 */
	private boolean needQueryRedis = false;

	public boolean isRepeatQuery() {
		return isRepeatQuery;
	}

	public void setRepeatQuery(boolean isRepeatQuery) {
		this.isRepeatQuery = isRepeatQuery;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public boolean isNeedQueryRedis() {
		return needQueryRedis;
	}

	public void setNeedQueryRedis(boolean needQueryRedis) {
		this.needQueryRedis = needQueryRedis;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierQueryRequestDTO{");
		sb.append(", requestId='").append(this.requestId).append('\'');
		sb.append(", recordId='").append(this.recordId).append('\'');
		sb.append(", isRepeatQuery='").append(this.isRepeatQuery).append('\'');
		sb.append(", needQueryRedis='").append(this.needQueryRedis).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
