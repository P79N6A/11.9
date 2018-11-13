/**
 * 
 */
package com.yeepay.g3.core.payprocessor.param;

import com.yeepay.g3.core.payprocessor.enumtype.CancelStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.PaymentStatusEnum;

/**
 * Description:
 * 
 * @author peile.fan
 * @since:2017年2月6日 上午11:46:23
 */
public class CancelResponse {
	/**
	 * 撤销状态
	 */
	private CancelStatusEnum cancelStatus;
	/**
	 * 订单状态
	 */
	private PaymentStatusEnum payStatus;

	public CancelStatusEnum getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(CancelStatusEnum cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public PaymentStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PaymentStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CancelResponseDTO [cancelStatus=");
		builder.append(cancelStatus);
		builder.append(", payStatus=");
		builder.append(payStatus);
		builder.append("]");
		return builder.toString();
	}

}
