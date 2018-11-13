/**
 * 
 */
package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.biz.NcCashierBankBiz;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.NcCashierBankFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tanzhen
 *
 */
@Service("ncCashierBankFacade")
public class NcCashierBankFacadeImpl implements NcCashierBankFacade {

	@Resource
	private NcCashierBankBiz ncCashierBankBiz;

	@Override
	public SupportBanksResponseDTO supportBankList(long requestId,String cusType) {
		return ncCashierBankBiz.supportBankList(requestId,cusType);
	}
	@Override
	public BankCardReponseDTO getBankCardInfo4Preauth(long requestId,String cusType) {
		return ncCashierBankBiz.getBankCardInfo4Preauth(requestId, cusType);
	}
	@Override
	public BankCardReponseDTO getBankCardInfo(long requestId,String cusType) {
		return ncCashierBankBiz.getBankCardInfo(requestId,cusType);
	}

	@Override
	public BasicResponseDTO validatePassBindId(long requestId) {
		return ncCashierBankBiz.validatePassBindId(requestId);
	}

	@Override
	public PassBindIdDTO isPassBindId(long requestId) {
		return ncCashierBankBiz.isPassBindId(requestId);
	}

	@Override
	public CardValidateResponseDTO getCardValidates(CardValidateRequestDTO requestDto) {
		return ncCashierBankBiz.getCardValidates(requestDto);
	}

	@Override
	public CardBinInfoDTO getCardBinInfo(String cardno) {
		return ncCashierBankBiz.getCardBinInfo(cardno);
	}

	@Override
	public InstallmentBanksResponseDTO getInstallmentBankList(long requestId) {
		return ncCashierBankBiz.getBankList(requestId);
	}

}
