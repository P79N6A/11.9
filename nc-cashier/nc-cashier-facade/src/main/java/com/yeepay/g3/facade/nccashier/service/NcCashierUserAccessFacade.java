package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessResponseDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

/**
 * nccashier操作用户访问记录facade
 * 
 * @author：peile.fan
 * @since：2016年5月23日 下午4:17:32
 * @version:
 */
public interface NcCashierUserAccessFacade {

	/**
	 * 获取请求基本信息(页面需要元素)
	 * 
	 * @param requestId
	 * @return
	 */
	public RequestInfoDTO requestBaseInfo(String tokenId);

	/**
	 * 保存用户请求信息
	 * 
	 * @param requestId
	 * @param userAccessDTO
	 * @return
	 * @throws CashierBusinessException
	 */
	public UserAccessResponseDTO saveUserAccount(UserAccessDTO userAccessDTO);

	/**
	 * 绑卡切换首次支付时清空支付记录表主键
	 */
	public BasicResponseDTO clearRecordId(String tokenId);

}
