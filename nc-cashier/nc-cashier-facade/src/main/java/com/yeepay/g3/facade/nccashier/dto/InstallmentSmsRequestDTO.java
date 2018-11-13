package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

/**
 * 银行卡分期发短验请求入参
 * 
 * @author duangduang
 *
 */
public class InstallmentSmsRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * paymentRequestId
	 */
	private Long requestId;

	/**
	 * paymentRecordId
	 */
	private Long recordId;

	public InstallmentSmsRequestDTO() {

	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public void validate() {
		if (requestId == null || requestId <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
		if (recordId == null || recordId <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "recordId未传");
		}
	}

	@Override
	public String toString() {
		return "InstallmentSmsRequestDTO [requestId=" + requestId + ", recordId=" + recordId + "]";
	}

}
