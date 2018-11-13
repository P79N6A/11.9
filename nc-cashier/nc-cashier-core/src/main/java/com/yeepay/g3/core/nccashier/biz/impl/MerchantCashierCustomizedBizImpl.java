package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.MerchantCashierCustomizedBiz;
import com.yeepay.g3.core.nccashier.service.MerchantCashierCustomizedService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedFileDTO;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedLayoutSelectDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.StringUtils;
/**
 * 
 * @Description 收银台定制化服务biz
 * @author yangmin.peng
 * @since 2017年6月16日下午12:03:05
 */
@Service("merchantCashierCustomizedBiz")
public class MerchantCashierCustomizedBizImpl extends NcCashierBaseBizImpl implements MerchantCashierCustomizedBiz {

	@Resource
	private MerchantCashierCustomizedService merchantCashierCustomizedService;

	@Override
	public MerchantCashierCustomizedLayoutSelectDTO queryMerchantCashierCustomizedLayoutSelectInfo(String merchantNo) {
		if (StringUtils.isBlank(merchantNo)) {
			throw CommonUtil.handleException(Errors.MERCHANT_CASHIER_NOT_CUSTOMIZED);
		}
		MerchantCashierCustomizedLayoutSelectDTO response = new MerchantCashierCustomizedLayoutSelectDTO();
		try{
			response =merchantCashierCustomizedService.queryMerchantCashierCustomizedLayoutSelectInfo(merchantNo);
		}catch(Throwable t){
			handleException(response, t);
		}
		return response;
	}

	@Override
	public MerchantCashierCustomizedFileDTO queryMerchantCashierCustomizedFile(String fileId, String fileType) {
		if (StringUtils.isBlank(fileId) || StringUtils.isBlank(fileType)) {
			throw CommonUtil.handleException(Errors.MERCHANT_CASHIER_NOT_CUSTOMIZED);
		}
		MerchantCashierCustomizedFileDTO response = new MerchantCashierCustomizedFileDTO();
		try{
			response = merchantCashierCustomizedService.queryMerchantCashierCustomizedFile(fileId, fileType);
		}catch(Throwable t){
			handleException(response, t);
		}
		return response;
		
	}

}
