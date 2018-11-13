package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayRequestStatusEnum;

public class CashierPayResponseDTO extends BasicResponseDTO implements Serializable {


	private static final long serialVersionUID = -2434868935760561027L;
	// 支付订单的状态
	private PayRecordStatusEnum recordStatus;

	// 请求订单的状态
	private PayRequestStatusEnum requestStatus;


	public PayRecordStatusEnum getRecordStatus() {
		return recordStatus;
	}


	public void setRecordStatus(PayRecordStatusEnum recordStatus) {
		this.recordStatus = recordStatus;
	}



	public PayRequestStatusEnum getRequestStatus() {
		return requestStatus;
	}


	public void setRequestStatus(PayRequestStatusEnum requestStatus) {
		this.requestStatus = requestStatus;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierPayResponseDTO{");
		sb.append("recordStatus='").append(this.recordStatus).append('\'');
		sb.append("requestStatus='").append(this.requestStatus).append('\'');
		sb.append("," + super.toString());
		sb.append('}');
		return sb.toString();
	}
}
