/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.core.nccashier.entity.MerchantProductInfo;
import com.yeepay.g3.facade.configcenter.dto.MerchantProductRespDto;
import com.yeepay.g3.facade.configcenter.dto.MerchantSettingRespDto;

import java.util.List;
import java.util.Map;

/**
 * 对接商户配置中心新服务
 */
public interface NewMerchantConfigCenterService {

	/**
	 * 获取商户入网配置信息
	 *
	 * @return
	 */
	MerchantProductRespDto getMerchantInNetConfig(String merchantNo, String bizSystem);


	/**
	 * 获取支付场景
	 * @param merchantNo
	 * @param bizSystem
	 * @param baseproductCode
	 * @param marketingproductCode
     * @return
     */
	MerchantSettingRespDto getPayScene(String merchantNo, String baseproductCode,String marketingproductCode);


	List<MerchantProductInfo> getAllMerchantInNetConfig(String merchantNo, String bizSystem,String token);










	
}
