package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.CashierCardOwnerBiz;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.facade.nccashier.dto.CardOwnerConfirmResDTO;
import com.yeepay.g3.facade.nccashier.service.CashierCardOwnerFacade;
@Service("cashierCardOwnerFacade")
public class CashierCardOwnerFacadeImpl extends NcCashierBaseService implements
		CashierCardOwnerFacade {

	@Resource
	private CashierCardOwnerBiz cashierCardOwnerBiz;


	@Override
	public void setCardOwner(String bindId, String name, String idCardNo,long paymentRequestId) {
		cashierCardOwnerBiz.setCardOwner(bindId,name,idCardNo,paymentRequestId);
	}

	@Override
	public void unbindCard(long paymentRequestId, String paymentOrderNo) {
		cashierCardOwnerBiz.unbindCard(paymentRequestId,paymentOrderNo);
	}

	@Override
	public void unbindCardForInner(long paymentRequestId, long bindId) {
		cashierCardOwnerBiz.unbindCardForInner(paymentRequestId,bindId);
	}

	@Override
	public CardOwnerConfirmResDTO getCardOwners(long paymentRequestId,
			long paymentRecordId) {
		return cashierCardOwnerBiz.getCardOwners(paymentRequestId,paymentRecordId);
	}

	@Override
	public void unbindCard(long paymentRequestId, long paymentRecordId) {
		cashierCardOwnerBiz.unbindCard(paymentRequestId,paymentRecordId);
	}
	
	@Override
	public void autosetSamePeronInfo() {
		cashierCardOwnerBiz.autosetSamePeronInfo();
	}

	@Override
	public void unbindCardActive(String paymentRequestId, String bindId) {
		cashierCardOwnerBiz.unbindCardActive(paymentRequestId,bindId);
	}


}
