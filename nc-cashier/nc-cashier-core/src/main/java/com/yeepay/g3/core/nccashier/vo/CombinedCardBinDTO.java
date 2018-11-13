/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;

/**
 * 卡bin组合对象
 * @author zhen.tan
 *
 */
public class CombinedCardBinDTO {
	
	private PaymentRequest paymentRequest;

	private CardBinDTO cardBinDTO;


	public PaymentRequest getPaymentRequest() {
		return paymentRequest;
	}

	public void setPaymentRequest(PaymentRequest paymentRequest) {
		this.paymentRequest = paymentRequest;
	}

	public CardBinDTO getCardBinDTO() {
		return cardBinDTO;
	}

	public void setCardBinDTO(CardBinDTO cardBinDTO) {
		this.cardBinDTO = cardBinDTO;
	}

}
