/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.MerchantConfigRequestParam;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel;
import com.yeepay.g3.facade.configcenter.dto.MerchantPayBankConfigDto;
import com.yeepay.g3.facade.foundation.dto.MerchantLimitRequestDto;
import com.yeepay.g3.facade.foundation.dto.MerchantLimitResponseDto;

import java.util.List;

/**
 * @author zhen.tan
 * 对接商户配置中心服务
 *
 */
public interface MerchantConfigCenterService {
	
	/**
	 * 获取商户入网配置信息
	 * 
	 * @param merchantConfigRequestParam
	 * @return
	 */
	MerchantInNetConfigModel getMerchantInNetConfig(MerchantConfigRequestParam merchantConfigRequestParam);

	/**
	 * 查询商户的银行卡限额
	 * @param requestDto
	 * @return
	 */
	public List<MerchantLimitResponseDto> queryMerchantLimit(MerchantLimitRequestDto requestDto);
	
	/**
	 * 构造商户限额查询接口的入参
	 * @param paymentRequest
	 * @return
	 */
	MerchantLimitRequestDto buildMerchantLimitRequestDto(PaymentRequest paymentRequest);
	
	/**
	 * 获取符合交易的商户限额列表
	 * 
	 * @param paymentRequest
	 * @return
	 */
	List<MerchantLimitResponseDto> filterValidMerchantLimits(PaymentRequest paymentRequest);
	
	MerchantPayBankConfigDto getBankInfo(String merchantNo);




	
}
