package com.yeepay.g3.core.nccashier.gateway.service;

import java.util.List;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.nccashier.dto.BankSupportDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.param.*;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;
import com.yeepay.g3.facade.ncconfig.result.ConfigCashierBankRuleDTO;
import com.yeepay.g3.facade.ncconfig.result.ConfigCashierDTO;
import com.yeepay.g3.facade.ncconfig.result.MerchantCashierTemplateFileDTO;
import com.yeepay.g3.facade.ncconfig.result.MerchantLayoutSelectDTO;
import com.yeepay.g3.facade.ncconfig.result.OnlineBankTemplateInfo;

/**
 * 
 * @author：peile.fan
 * @since：2016年5月19日 下午6:27:09
 * @version:
 */
public interface NcConfigService {

	/**
	 * 根据卡号获取卡bin
	 * @param cardNo
	 * @return
	 */
	public CardBinDTO getCardBinDTOByCardNo(String cardNo);
	
	/**
	 * 获取收银台模板支持的银行列表
	 * @param paymentRequest
	 * @return
	 */
	public List<BankSupportDTO> getSupportBanks(PaymentRequest paymentRequest, String cusType);
	
	/**
	 * 获取收银台模板
	 * @param param
	 * @return
	 * @throws CashierBusinessException
	 */
	public ConfigCashierDTO queryConfigCashier(ConfigCashierParam param) throws CashierBusinessException;
	
	/**
	 * 获取银行规则
	 * @param param
	 * @return
	 * @throws CashierBusinessException
	 */
	public ConfigCashierBankRuleDTO getCashierBankRule(ConfigCashierBankRuleParam param) throws CashierBusinessException;
	
	/**
	 * 获取网银 支持的银行列表
	 * @param param
	 * @return
	 */
	OnlineBankTemplateInfo getEbankSupportBanks(QueryOnlineBankRulesByTemplateParam param);
	/**
	 * 收银台定制化-查询模版文件内容
	 * 
	 * @param 
	 * @return
	 */
	MerchantCashierTemplateFileDTO queryMerchantCashierTemplateFileInfo(String fileId,String fileType);
	/**
	 * 收银台定制化-根据商编查询商户已选择的模板布局
	 * 
	 * @param merchantLayoutQueryParam
	 * @return
	 */
	MerchantLayoutSelectDTO queryMerchantCashierSelectLayout(BasicMerchantLayoutParam merchantLayoutQueryParam);

	/**
	 * 是否授权支付
	 *
	 * @param configAuthPayParam
	 */
	void hasAuthPay(ConfigAuthPayParam configAuthPayParam);
}
