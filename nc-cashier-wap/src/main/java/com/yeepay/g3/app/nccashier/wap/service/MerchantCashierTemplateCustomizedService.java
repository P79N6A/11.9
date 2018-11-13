package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.vo.MerchantCashierCustomizedLayoutSelectVO;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedFileDTO;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedLayoutSelectDTO;

/**
 * 
 * @Description 收银台定制化service
 * @author yangmin.peng
 * @since 2017年6月16日上午11:20:50
 */
public interface MerchantCashierTemplateCustomizedService {
	/**
	 * 获取商户定制收银台信息(支付工具排序，颜色等)
	 * 
	 * @param merchantNo
	 * @return
	 */
	MerchantCashierCustomizedLayoutSelectVO queryMerchantCashierCustomizedLayoutSelectInfo(String merchantNo);

	/**
	 * 获取收银台定制化模版文件(或者商户上传的logo文件)并生成文件
	 * 
	 * @param fileId
	 * @param fileType
	 * @return
	 */
	void queryMerchantCashierCustomizedFile(MerchantCashierCustomizedLayoutSelectVO merchantCashierCustomizedLayoutSelect);
}
