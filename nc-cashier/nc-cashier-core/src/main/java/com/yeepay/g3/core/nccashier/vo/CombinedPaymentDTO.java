/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;

/**
 * 订单组合对象
 * @author zhen.tan
 *
 */
public class CombinedPaymentDTO {
	
	private PaymentRequest paymentRequest;

	private PaymentRecord paymentRecord;
	
	/**
	 * 是否需要下单
	 */
	private boolean isNeedOrderRecord;

	public CombinedPaymentDTO(){
		
	}
	
	public CombinedPaymentDTO(PaymentRequest paymentRequest, PaymentRecord paymentRecord){
		setPaymentRecord(paymentRecord);
		setPaymentRequest(paymentRequest);
	}

	public PaymentRequest getPaymentRequest() {
		return paymentRequest;
	}

	public void setPaymentRequest(PaymentRequest paymentRequest) {
		this.paymentRequest = paymentRequest;
	}

	public PaymentRecord getPaymentRecord() {
		return paymentRecord;
	}

	public void setPaymentRecord(PaymentRecord paymentRecord) {
		this.paymentRecord = paymentRecord;
	}

	public boolean isNeedOrderRecord() {
		return isNeedOrderRecord;
	}

	public void setNeedOrderRecord(boolean isNeedOrderRecord) {
		this.isNeedOrderRecord = isNeedOrderRecord;
	}

}
