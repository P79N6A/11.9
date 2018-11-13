package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;

import java.io.Serializable;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 17/2/17 15:51
 */
public class OrderProcessorRequestDTO implements Serializable {
	
    private static final long serialVersionUID = -6130270266159095850L;
    
    private String token;
    private String appId;
    private String openId;
    private String cardType;
    private String userType;
    private String userNo;
    private CashierVersionEnum CashierVersion;
    private String directPayType;
    private String merchantNo;
    private String userIp;
    private String bizType;
    
    /**
     * 支付宝生活号 商户透传
     */
    private String aliUserId;
    
    /**
     * 支付宝生活号 商户透传
     */
    private String aliAppId;

    /**
     * 客户号，部分B2B网银支付所需
     */
    private String clientId;
    
    /** 以下三个字段 微信H5支付所需 **/
    private String platForm; 
	private String appName;
	private String appStatement;

    /**
     * 报备费率，用于区分聚合支付线上线下
     */
    private String reportFee;
   
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public CashierVersionEnum getCashierVersion() {
        return CashierVersion;
    }

    public void setCashierVersion(CashierVersionEnum cashierVersion) {
        CashierVersion = cashierVersion;
    }

    public String getDirectPayType() {
        return directPayType;
    }

    public void setDirectPayType(String directPayType) {
        this.directPayType = directPayType;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

	public String getAliUserId() {
		return aliUserId;
	}

	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
	}

	public String getAliAppId() {
		return aliAppId;
	}

	public void setAliAppId(String aliAppId) {
		this.aliAppId = aliAppId;
	}

	public String getPlatForm() {
		return platForm;
	}

	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppStatement() {
		return appStatement;
	}

	public void setAppStatement(String appStatement) {
		this.appStatement = appStatement;
	}

    public String getReportFee() {
        return reportFee;
    }

    public void setReportFee(String reportFee) {
        this.reportFee = reportFee;
    }
}
