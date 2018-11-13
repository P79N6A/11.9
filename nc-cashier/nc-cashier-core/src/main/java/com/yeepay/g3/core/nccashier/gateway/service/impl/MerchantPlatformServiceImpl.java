package com.yeepay.g3.core.nccashier.gateway.service.impl;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.MerchantPlatformService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.mp.dto.TradePasswordValidateDTO;
import com.yeepay.g3.facade.mp.exception.UserBusinessException;
import com.yeepay.g3.facade.mp.merchant.enums.FunctionEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

/**
 * 商户平台服务获取实现类
 * 
 * @author duangduang
 * @date 2017-06-06
 */
@Service
public class MerchantPlatformServiceImpl extends NcCashierBaseService implements MerchantPlatformService {

	@Override
	public boolean validatePermission(String merchantLoginName) {
		try {
			return userFacade.validatePermission(merchantLoginName, FunctionEnum.HUIYUANZHIFU_FUNCTION);
		} catch (Throwable t) {
			if (t instanceof UserBusinessException) {// 用户不存在
				throw new CashierBusinessException(Errors.MERCHANT_PLATFORM_ACCOUNT_NAME_NOEXSIT);
			}
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public TradePasswordValidateDTO validateTradePassword(String merchantLoginName, String tradePassword) {
		TradePasswordValidateDTO validateRes = null;
		try{
			validateRes = userFacade.queryTradePasswordValidateResult(merchantLoginName, tradePassword);
		}catch(Throwable t){
			if(t instanceof UserBusinessException){ // 用户不存在
				throw new CashierBusinessException(Errors.MERCHANT_PLATFORM_ACCOUNT_NAME_NOEXSIT);
			}
			// 参数异常IllegalArgumentException&其他异常
			throw CommonUtil.handleException(Errors.MERCHANT_PLATFORM_EXCEPTION);
		}
		
		if(validateRes==null){
			throw CommonUtil.handleException(Errors.MERCHANT_PLATFORM_EXCEPTION);
		}
		
		return validateRes;
		
	}

}
