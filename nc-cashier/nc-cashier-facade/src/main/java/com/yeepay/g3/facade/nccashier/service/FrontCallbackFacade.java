package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackDTO;
import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackResDTO;

public interface FrontCallbackFacade {

	/**
	 * 获取智能网银的前端回调地址
	 * 
	 * @param intelligentNetResultFrontCallbackDTO
	 * @return
	 */
	IntelligentNetResultFrontCallbackResDTO receiveIntelligentNetResultFrontCallback(
			IntelligentNetResultFrontCallbackDTO intelligentNetResultFrontCallbackDTO);

}
