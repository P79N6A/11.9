
package com.yeepay.g3.core.payprocessor.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Description:
 * 
 * @author peile.fan
 * @since:2017年2月17日 下午4:18:41
 */
public class ExtendedInfo implements Serializable {
	/**
	 *银行流水号（付款凭证号）
	 */
	private String bankTradeId;
	/**
	 * 付款方账号(卡号)
	 */
	private String payerBankAccountNo;
	/**
	 * 银行商户号（报备银行商户号）
	 */
	private String reportMerchantNo;
	/**
	 * 优惠信息
	 */
	private String couponInfo;
	
	

    /**
     * 字符串，风控返回的是否短验
     */
    private String userId;

    /**
     * 布尔，是否要发送扣款通知短信
     */
    private String userType;

    private Map<String, String> accountPayExtInfo;
    
    private String walletLevel;

	/**
	 * 扩展字段，不关心内部的key值
	 */
	private Map<String, String> extParam;

	public String getBankTradeId() {
		return bankTradeId;
	}

	public void setBankTradeId(String bankTradeId) {
		this.bankTradeId = bankTradeId;
	}

	public String getPayerBankAccountNo() {
		return payerBankAccountNo;
	}

	public void setPayerBankAccountNo(String payerBankAccountNo) {
		this.payerBankAccountNo = payerBankAccountNo;
	}

	public String getReportMerchantNo() {
		return reportMerchantNo;
	}

	public void setReportMerchantNo(String reportMerchantNo) {
		this.reportMerchantNo = reportMerchantNo;
	}

	public String getCouponInfo() {
		return couponInfo;
	}

	public void setCouponInfo(String couponInfo) {
		this.couponInfo = couponInfo;
	}
	
    public String getUserId() {
        return userId;
    }

    public ExtendedInfo setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public ExtendedInfo setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public Map<String, String> getAccountPayExtInfo() {
        return accountPayExtInfo;
    }

    public void setAccountPayExtInfo(Map<String, String> accountPayExtInfo) {
        this.accountPayExtInfo = accountPayExtInfo;
    }

	public String getWalletLevel() {
		return walletLevel;
	}

	public ExtendedInfo setWalletLevel(String walletLevel) {
		this.walletLevel = walletLevel;
		return this;
	}

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}
}
