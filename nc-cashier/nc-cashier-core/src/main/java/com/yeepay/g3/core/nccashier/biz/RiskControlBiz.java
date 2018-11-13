/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.CheckRefferReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferRequestDTO;

/**
 * @author zhen.tan
 *
 */
public interface RiskControlBiz {

	public CheckRefferReponseDTO checkReffer(CheckRefferRequestDTO requestDTO);
}
