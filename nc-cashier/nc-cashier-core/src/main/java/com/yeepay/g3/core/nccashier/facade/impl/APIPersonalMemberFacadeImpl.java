package com.yeepay.g3.core.nccashier.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIPersonalMemberBiz;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMemberBalancePayRequestDTO;
import com.yeepay.g3.facade.nccashier.service.APIPersonalMemberFacade;

@Service("apiPersonalMemberFacade")
public class APIPersonalMemberFacadeImpl implements APIPersonalMemberFacade {

	@Autowired
	private APIPersonalMemberBiz apiPersonalMemberBiz;

	@Override
	public APIBasicResponseDTO balancePay(APIMemberBalancePayRequestDTO requestDTO) {
		return apiPersonalMemberBiz.balancePay(requestDTO);
	}

}
