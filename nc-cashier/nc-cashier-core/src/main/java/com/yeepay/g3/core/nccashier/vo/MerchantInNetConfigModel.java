/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.FeeTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;


/**
 * @author zhen.tan
 * 商户入网配置信息
 *
 */
public class MerchantInNetConfigModel {
	
	// 商编
	private String merchantAccountCode;
	
	private String mcc;
	
	/**
	 * 支付产品Map
	 */
	public Map<CashierVersionEnum,List<Product>> productMap;

	private String saleProductCode;

	private FeeTypeEnum feeTypeEnum;

	/**
	 * 商户属性map，目前用于存储钱包支付h5的高低配优先级
	 */
	private Map<String,String> merchantAttributes;

	public Map<String, String> getMerchantAttributes() {
		return merchantAttributes;
	}

	public void setMerchantAttributes(Map<String, String> merchantAttributes) {
		this.merchantAttributes = merchantAttributes;
	}

	public static class Product{
		
		//支付工具
		private PayTool payTool;

		//支付方式
		private List<String> payTypes;
		
		//支付场景
		private String payScene;
		
		private Map<String, List<String>> bankStaging;

		public PayTool getPayTool() {
			return payTool;
		}

		public void setPayTool(PayTool payTool) {
			this.payTool = payTool;
		}

		public List<String> getPayTypes() {
			return payTypes;
		}

		public void setPayTypes(List<String> payTypes) {
			this.payTypes = payTypes;
		}

		public String getPayScene() {
			return payScene;
		}

		public void setPayScene(String payScene) {
			this.payScene = payScene;
		}

		public Map<String, List<String>> getBankStaging() {
			return bankStaging;
		}

		public void setBankStaging(Map<String, List<String>> bankStaging) {
			this.bankStaging = bankStaging;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
		
		/**
		 * 构造该支付工具对应的银行属性（配置返回的）
		 * 
		 * @return
		 */
		public BankStagingInfo buildBankStagingInfo() {
			BankStagingInfo bankStagingInfo = new BankStagingInfo();
			bankStagingInfo.setBankStagings(bankStaging);
			bankStagingInfo.setPayTool(payTool);
			return bankStagingInfo;
		}
		
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMerchantAccountCode() {
		return merchantAccountCode;
	}

	public void setMerchantAccountCode(String merchantAccountCode) {
		this.merchantAccountCode = merchantAccountCode;
	}

	public Map<CashierVersionEnum, List<Product>> getProductMap() {
		return productMap;
	}

	public void setProductMap(Map<CashierVersionEnum, List<Product>> productMap) {
		this.productMap = productMap;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getSaleProductCode() {
		return saleProductCode;
	}

	public void setSaleProductCode(String saleProductCode) {
		this.saleProductCode = saleProductCode;
	}

	public FeeTypeEnum getFeeTypeEnum() {
		return feeTypeEnum;
	}

	public void setFeeTypeEnum(FeeTypeEnum feeTypeEnum) {
		this.feeTypeEnum = feeTypeEnum;
	}
}
