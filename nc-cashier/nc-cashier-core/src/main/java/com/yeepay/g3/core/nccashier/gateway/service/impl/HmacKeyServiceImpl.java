package com.yeepay.g3.core.nccashier.gateway.service.impl;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.HmacKeyService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.merchant_platform.dto.HmacKeyRespDTO;
import com.yeepay.g3.facade.merchant_platform.dto.MerchantReqDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;

@Service("hmacKeyService")
public class HmacKeyServiceImpl extends NcCashierBaseService implements HmacKeyService {
	@Override
	public HmacKeyRespDTO getHmacKey(String merchantNo) {
		MerchantReqDTO reqDTO = new MerchantReqDTO();
		reqDTO.setMerchantNo(merchantNo);
		Map<String, String> authMap = CommonUtil.getMerchantPlatformAuth();
		reqDTO.setSystem(authMap.get(CommonUtil.AUTH_SYSTEM_KEY));
		reqDTO.setUid(authMap.get(CommonUtil.AUTH_UUID_KEY));
		reqDTO.setCharSet("UTF-8");
		reqDTO.setReqTime(new Timestamp(System.currentTimeMillis()));
		HmacKeyRespDTO resDTO = null;
		try {
			resDTO = hmacKeyFacade.getHmacKey(reqDTO);
		} catch (Exception e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (resDTO == null) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		} else {
			return resDTO;
		}
	}
}
