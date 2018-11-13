package com.yeepay.g3.app.nccashier.wap.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.APIMerchantScanService;
import com.yeepay.g3.app.nccashier.wap.vo.APIMerchantScanPayRequestVO;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantScanPayDTO;
import com.yeepay.g3.utils.common.BeanUtils;

/**
 * 商家扫码Service实现
 * 
 * @author duangduang
 * @since 2017-02-20
 */
@Service("apiMerchantScanService")
public class APIMerchantScanServiceImpl implements APIMerchantScanService {

	@Resource
	private NcCashierService ncCashierService;

	@Override
	public void pay(APIMerchantScanPayRequestVO param, String userIp) {
		// 构造入参
		APIMerchantScanPayDTO requestParam = new APIMerchantScanPayDTO();
		BeanUtils.copyProperties(param, requestParam);
		requestParam.setUserIp(userIp);
		// 请求core进行支付下单
		ncCashierService.apiMerchantScanPay(requestParam);
	}

}
