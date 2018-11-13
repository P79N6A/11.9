package com.yeepay.g3.core.nccashier.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.NcCashierUserAccessBiz;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessResponseDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.service.NcCashierUserAccessFacade;

@Service
public class NcCashierUserAccessFacadeImpl implements NcCashierUserAccessFacade {
	@Autowired
	private NcCashierUserAccessBiz ncCashierUserAccessBiz;

	@Override
	public RequestInfoDTO requestBaseInfo(String tokenId) {
		return ncCashierUserAccessBiz.requestBaseInfo(tokenId);
	}

	@Override
	public UserAccessResponseDTO saveUserAccount(UserAccessDTO userAccessDTO)
			throws CashierBusinessException {
		return ncCashierUserAccessBiz.saveUserAccount(userAccessDTO);
	}

	@Override
	public BasicResponseDTO clearRecordId(String tokenId) {
		return ncCashierUserAccessBiz.clearRecordId(tokenId);
	}

}
