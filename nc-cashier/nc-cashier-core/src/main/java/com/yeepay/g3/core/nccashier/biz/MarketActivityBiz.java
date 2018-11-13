package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.MarketActivityRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityResponseDTO;

public interface MarketActivityBiz {
	
	MarketActivityResponseDTO getMarketActivityInfo(MarketActivityRequestDTO requestDTO);

}
