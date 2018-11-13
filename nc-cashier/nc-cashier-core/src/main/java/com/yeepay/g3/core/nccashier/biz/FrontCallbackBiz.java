package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackDTO;
import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackResDTO;

public interface FrontCallbackBiz {
	
	IntelligentNetResultFrontCallbackResDTO receiveIntelligentNetResultFrontCallback(
			IntelligentNetResultFrontCallbackDTO intelligentNetResultFrontCallbackDTO);

}
