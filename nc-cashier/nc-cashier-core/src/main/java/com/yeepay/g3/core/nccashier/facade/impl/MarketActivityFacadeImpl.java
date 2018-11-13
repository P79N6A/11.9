package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.MarketActivityBiz;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityResponseDTO;
import com.yeepay.g3.facade.nccashier.service.MarketActivityFacade;

@Service("marketActivityFacade")
public class MarketActivityFacadeImpl implements MarketActivityFacade {

	@Resource
	private MarketActivityBiz marketActivityBiz;

	@Override
	public MarketActivityResponseDTO getMarketActivityInfo(MarketActivityRequestDTO requestDTO) {
		return marketActivityBiz.getMarketActivityInfo(requestDTO);
	}

}
