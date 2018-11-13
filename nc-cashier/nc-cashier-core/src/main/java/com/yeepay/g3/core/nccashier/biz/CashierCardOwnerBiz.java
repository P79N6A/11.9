package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.CardOwnerConfirmResDTO;
import com.yeepay.g3.facade.nccashier.dto.Person;

public interface CashierCardOwnerBiz {

	public void setCardOwner(String bindId, String name, String idCardNo,long paymentRequestId);

	public void unbindCard(long paymentRequestId, String paymentOrderNo);

	public void unbindCard(long paymentRequestId, long paymentRecordId);

	public CardOwnerConfirmResDTO getCardOwners(long paymentRequestId,
			long paymentRecordId);

	public void unbindCardForInner(long paymentRequestId, long bindId);
	
	public void autosetSamePeronInfo();

	/**
	 * 绑卡支付，在已绑卡列表，主动解绑选中的卡
	 * @param paymentRequestId
	 * @param bindId
	 */
	void unbindCardActive(String paymentRequestId, String bindId);
}
