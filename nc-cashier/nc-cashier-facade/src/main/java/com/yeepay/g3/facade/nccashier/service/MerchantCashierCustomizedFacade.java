package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedFileDTO;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedLayoutSelectDTO;

/**
 * 
 * @Description 收银台定制化服务facade
 * @author yangmin.peng
 * @since 2017年6月16日上午11:44:03
 */
public interface MerchantCashierCustomizedFacade {
	/**
	 * 获取商户定制收银台信息
	 * @param merchantNo
	 * @return
	 */
	MerchantCashierCustomizedLayoutSelectDTO queryMerchantCashierCustomizedLayoutSelectInfo(String merchantNo);
	/**
	 * 获取收银台定制化模版文件内容
	 * @param fileId
	 * @param fileType
	 * @return
	 */
	MerchantCashierCustomizedFileDTO queryMerchantCashierCustomizedFile(String fileId,String fileType);

}
