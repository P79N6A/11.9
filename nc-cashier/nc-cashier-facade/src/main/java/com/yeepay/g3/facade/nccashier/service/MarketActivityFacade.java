package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.MarketActivityRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityResponseDTO;

public interface MarketActivityFacade{
	
	MarketActivityResponseDTO getMarketActivityInfo(MarketActivityRequestDTO requestDTO);

}
