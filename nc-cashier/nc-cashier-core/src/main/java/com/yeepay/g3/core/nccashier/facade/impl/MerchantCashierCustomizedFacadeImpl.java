package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.MerchantCashierCustomizedBiz;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedFileDTO;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedLayoutSelectDTO;
import com.yeepay.g3.facade.nccashier.service.MerchantCashierCustomizedFacade;
/**
 * 
 * @Description 收银台定制化服务facade实现
 * @author yangmin.peng
 * @since 2017年6月16日上午11:59:58
 */
@Service("merchantCashierCustomizedFacade")
public class MerchantCashierCustomizedFacadeImpl implements MerchantCashierCustomizedFacade {
	@Resource
	private MerchantCashierCustomizedBiz merchantCashierCustomizedBiz;
	@Override
	public MerchantCashierCustomizedLayoutSelectDTO queryMerchantCashierCustomizedLayoutSelectInfo(String merchantNo) {	
		return merchantCashierCustomizedBiz.queryMerchantCashierCustomizedLayoutSelectInfo(merchantNo);
	}

	@Override
	public MerchantCashierCustomizedFileDTO queryMerchantCashierCustomizedFile(String fileId, String fileType) {
		return merchantCashierCustomizedBiz.queryMerchantCashierCustomizedFile(fileId, fileType);
	}

}
