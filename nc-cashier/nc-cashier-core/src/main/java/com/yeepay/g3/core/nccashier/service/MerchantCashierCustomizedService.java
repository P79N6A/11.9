package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedFileDTO;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedLayoutSelectDTO;

public interface MerchantCashierCustomizedService {
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
