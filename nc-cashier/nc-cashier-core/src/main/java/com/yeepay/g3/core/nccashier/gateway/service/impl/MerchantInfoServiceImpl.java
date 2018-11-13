/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.gateway.service.MerchantInfoService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.merchant_platform.dto.LevelRespDTO;
import com.yeepay.g3.facade.merchant_platform.dto.MerchantReqDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author zhen.tan
 *
 */
@Service
public class MerchantInfoServiceImpl extends NcCashierBaseService implements MerchantInfoService {

	@Override
	public LevelRespDTO getMerchantLevel(String merchantAccountCode) {
		MerchantReqDTO reqDTO = new MerchantReqDTO();
		reqDTO.setMerchantNo(merchantAccountCode);
		Map<String,String> authMap = CommonUtil.getMerchantPlatformAuth();
		reqDTO.setSystem(authMap.get(CommonUtil.AUTH_SYSTEM_KEY));
		reqDTO.setUid(authMap.get(CommonUtil.AUTH_UUID_KEY));
		reqDTO.setCharSet("UTF-8");
		reqDTO.setReqTime(new Timestamp(System.currentTimeMillis()));
		LevelRespDTO respDTO = null;

		// 缓存
		LevelRespDTO levelRespDTO = RedisTemplate.getTargetFromRedis(
				Constant.NCCASHIER_MERCHANT_LEVEL_REDIS_KEY + merchantAccountCode, LevelRespDTO.class);
		if (levelRespDTO != null) {
			return levelRespDTO;
		}

		try {
			respDTO = merchantFacade.getLevel(reqDTO);
			if(respDTO == null || !"0000".equals(respDTO.getRetCode())){
				respDTO = new LevelRespDTO();
			}
		} catch (Throwable e) {
			respDTO = new LevelRespDTO();
		}

		if ("0000".equals(respDTO.getRetCode())) {
			// 放入缓存 - 设置超时时间
			try {
				RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_MERCHANT_LEVEL_REDIS_KEY + merchantAccountCode,
						respDTO, CommonUtil.merchantLevelRedisTime());
			} catch (Exception e) {
				//DO NOTHING
			}
		}
		return respDTO;
	}
}
