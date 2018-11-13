package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.FrontCallbackBiz;
import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackDTO;
import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackResDTO;
import com.yeepay.g3.facade.nccashier.service.FrontCallbackFacade;

@Service
public class FrontCallbackFacadeImpl implements FrontCallbackFacade {

	@Resource
	private FrontCallbackBiz frontCallbackBiz;

	@Override
	public IntelligentNetResultFrontCallbackResDTO receiveIntelligentNetResultFrontCallback(
			IntelligentNetResultFrontCallbackDTO intelligentNetResultFrontCallbackDTO) {
		return frontCallbackBiz.receiveIntelligentNetResultFrontCallback(intelligentNetResultFrontCallbackDTO);
	}

}
