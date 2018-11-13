/**
 * 
 */
package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.RiskControlBiz;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferRequestDTO;
import com.yeepay.g3.facade.nccashier.service.RiskControlFacade;

/**
 * @author zhen.tan
 *
 */
@Service("riskControlFacade")
public class RiskControlFacadeImpl implements RiskControlFacade {

	@Resource
	private RiskControlBiz riskControlBiz;
	
	/* (non-Javadoc)
	 * @see com.yeepay.g3.facade.nccashier.service.RiskControlFacade#checkReffer(com.yeepay.g3.facade.nccashier.dto.CheckRefferRequestDTO)
	 */
	@Override
	public CheckRefferReponseDTO checkReffer(CheckRefferRequestDTO requestDTO) {
		return riskControlBiz.checkReffer(requestDTO);
	}

}
