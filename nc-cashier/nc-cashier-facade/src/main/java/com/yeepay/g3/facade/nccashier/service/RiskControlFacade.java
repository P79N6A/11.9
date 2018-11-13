/**
 * 
 */
package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.CheckRefferReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferRequestDTO;

/**
 * @author zhen.tan
 *
 */
public interface RiskControlFacade {

	/**
	 * 风控reffer校验
	 * @param requestDTO
	 * @return 
	 */
	public CheckRefferReponseDTO checkReffer(CheckRefferRequestDTO requestDTO);
		
}
