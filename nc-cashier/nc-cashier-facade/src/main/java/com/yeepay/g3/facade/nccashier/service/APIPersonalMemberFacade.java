package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMemberBalancePayRequestDTO;

/**
 * 个人会员相关支付业务，目前只有余额支付
 * 
 * @author duangduang
 *
 */
public interface APIPersonalMemberFacade {

	/**
	 * 个人会员余额支付接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO balancePay(APIMemberBalancePayRequestDTO requestDTO);

}
