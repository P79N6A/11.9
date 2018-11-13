/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.DirectPayType;
import com.yeepay.g3.facade.nccashier.enumtype.FeeTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;


/**
 * @author zhen.tan
 * 商户入网配置筛选结果
 *
 */
public class MerchantInNetConfigResult {
	
	private static Logger logger = LoggerFactory.getLogger(MerchantInNetConfigResult.class);
	
	/**
	 *  商编
	 */
	private String merchantAccountCode;
	
	private String mcc;
	
	/**
	 * 版本
	 */
	private CashierVersionEnum version;
	
	/**
	 * 支付方式
	 */
	private List<String> payTypes;
	
	/**
	 * 支付工具下的支付方式map
	 */
	private Map<String,List<String>> payToolAndPayTypeMap;
	
	/**
	 * 支付场景
	 */
	private Map<PayTool,String> paymentSceneMap;
	
	/**
	 * 直连方式
	 */
	private DirectPayType directPayType;
	/**
	 * 零售产品码
	 */
	private String saleProductCode;

	private FeeTypeEnum feeType;

	/**
	 * 商户属性map，目前用于存储钱包支付h5的高低配优先级
	 */
	private Map<String,String> merchantAttributes;
	
	/**
	 * 目前只有银行卡分期产品会用到
	 */
	private List<BankStagingInfo> bankStagingOfPayTool;

	public Map<PayTool, String> getPaymentSceneMap() {
		return paymentSceneMap;
	}

	public void setPaymentSceneMap(Map<PayTool, String> paymentSceneMap) {
		this.paymentSceneMap = paymentSceneMap;
	}

	public List<String> getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(List<String> payTypes) {
		this.payTypes = payTypes;
	}

	public DirectPayType getDirectPayType() {
		return directPayType;
	}

	public void setDirectPayType(DirectPayType directPayType) {
		this.directPayType = directPayType;
	}

	public String getMerchantAccountCode() {
		return merchantAccountCode;
	}

	public void setMerchantAccountCode(String merchantAccountCode) {
		this.merchantAccountCode = merchantAccountCode;
	}

	public CashierVersionEnum getVersion() {
		return version;
	}

	public void setVersion(CashierVersionEnum version) {
		this.version = version;
	}

	public Map<String, List<String>> getPayToolAndPayTypeMap() {
		return payToolAndPayTypeMap;
	}

	public void setPayToolAndPayTypeMap(
			Map<String, List<String>> payToolAndPayTypeMap) {
		this.payToolAndPayTypeMap = payToolAndPayTypeMap;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the saleProductCode
	 */
	public String getSaleProductCode() {
		return saleProductCode;
	}

	/**
	 * @param saleProductCode the saleProductCode to set
	 */
	public void setSaleProductCode(String saleProductCode) {
		this.saleProductCode = saleProductCode;
	}

	public FeeTypeEnum getFeeType() {
		return feeType;
	}

	public void setFeeType(FeeTypeEnum feeType) {
		this.feeType = feeType;
	}

	public Map<String, String> getMerchantAttributes() {
		return merchantAttributes;
	}

	public void setMerchantAttributes(Map<String, String> merchantAttributes) {
		this.merchantAttributes = merchantAttributes;
	}

	public List<BankStagingInfo> getBankStagingOfPayTool() {
		return bankStagingOfPayTool;
	}

	public void setBankStagingOfPayTool(List<BankStagingInfo> bankStagingOfPayTool) {
		this.bankStagingOfPayTool = bankStagingOfPayTool;
	}

	/**
	 * @title 封装payType和payTool信息，保存在remark字段
	 * @return
	 */
	public String buildRemark() {
		List<String> objPayTypes = new ArrayList<String>();
		for (String payType : payTypes) {
			try{
				PayTypeEnum payTypeEnum = PayTypeEnum.valueOf(payType);
				objPayTypes.add(payTypeEnum.value() + "");
			}catch (Exception e) {
				logger.error("支付类型转换失败", e);
			}

		}
		JSONObject configJson = new JSONObject();
		configJson.put("PayType", objPayTypes.toString());
		configJson.put("payTool", payToolAndPayTypeMap.keySet());
		return configJson.toJSONString();
	}
	
	/**
	 * 获取银行卡分期支持的银行列表
	 * 
	 * @return
	 */
	public Map<String, List<String>> getInstallmentCashierTemplate() {
		if (CollectionUtils.isEmpty(bankStagingOfPayTool)) {
			return null;
		}
		Map<String, List<String>> supportBankList = null;
		for (BankStagingInfo bankStaging : bankStagingOfPayTool) {
			if (PayTool.YHKFQ_ZF.equals(bankStaging.getPayTool())) {
				supportBankList = bankStaging.getBankStagings();
				break;
			}
		}
		if (MapUtils.isEmpty(supportBankList)) {
			return null;
		}
		return supportBankList;
	}
	
	public List<InstallmentBankInfo> listInstallmentSupportBank(){
		// 获取配置中心返回的银行卡分期支持的银行及期数信息
		Map<String, List<String>> banksOpened = getInstallmentCashierTemplate();
		// 获取统一配置配置的银行分期的信息
		Map<String, InstallmentBankInfo> allBankRateInfos = CommonUtil.getInstallmentInfo();
		// 配置中心支持的银行列表为空或者统一配置配置的所有银行列表及费率信息为空
		if (MapUtils.isEmpty(banksOpened) || MapUtils.isEmpty(allBankRateInfos)) {
			return null;
		}
		List<InstallmentBankInfo> supportBanks = new ArrayList<InstallmentBankInfo>();
		// TODO entrySet  三层循环看是否能优化（性能、代码可读性）
		for(String key : banksOpened.keySet()){
			List<String> periods = banksOpened.get(key);
			String bankCode  = CommonUtil.standardBankCode(key); // 将配置中心返回的银行编码标准化
			InstallmentBankInfo installmentBankInfo = allBankRateInfos.get(bankCode);
			// 其中一个银行支持的期数为空或者在统一配置不存在该银行，则跳过该银行
			if (CollectionUtils.isEmpty(periods) || installmentBankInfo == null) {
				logger.warn("商编={}，当前银行={}，配置中心返回的期数={}，该银行在统一配置的配置信息={}，期数为空或者统一配置为空", merchantAccountCode, bankCode,
						periods, installmentBankInfo);
				continue;
			}
			List<InstallmentRateInfo> objRates = installmentBankInfo.getSupportRateList(periods);
			if (CollectionUtils.isEmpty(objRates)) {
				continue;
			}
			
			installmentBankInfo.setInstallmentRateInfos(objRates);
			supportBanks.add(installmentBankInfo);
		
			
		}
		return supportBanks;
	}
}
