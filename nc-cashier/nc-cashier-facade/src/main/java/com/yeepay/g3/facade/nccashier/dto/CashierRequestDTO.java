package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CashierRequestDTO implements Serializable {
	private static final long serialVersionUID = -1162339690047598850L;
	/**
	 * 交易系统订单号
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="交易系统订单号tradeSysOrderId未传")
	private String tradeSysOrderId;
	/**
	 * 交易系统编码
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="交易系统编码tradeSysNo未传")
	private String tradeSysNo;
	/**
	 * 订单方订单号 收款宝未传
	 */
	private String orderOrderId;
	/**
	 * 订单方编码 收款宝未传
	 */
	private String orderSysNo;
	/**
	 * 业务模式编码
	 */
	private String bizModeCode;
	/**
	 * 商户编号
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="商户编号merchantNo未传")
	private String merchantNo;
	/**
	 * 商户订单号
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="商户订单号merchantOrderId未传")
	private String merchantOrderId;
	/**
	 * 商户名称
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="商户名称merchantName未传")
	private String merchantName;
	/**
	 * 商品名称
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="商品名称productName未传")
	private String productName;
	/**
	 * 订单有效期 单位：分钟
	 */
	private int orderExpDate;
	/**
	 * 交易发生时间
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="交易发生时间orderCreateDate未传")
	private Date orderCreateDate;
	/**
	 * 订单金额
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="订单金额orderAmount未传")
	private BigDecimal orderAmount;
	/**
	 * 交易币种
	 */
	private CurrencyEnum currency;
	/**
	 * 指定收银台
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.INPUT_PARAM_NULL,errorMsg="指定收银台cashierVersion未传")
	private CashierVersionEnum cashierVersion;
	/**
	 * 主题自定义编码
	 */
	private String themeCustomCode;
	/**
	 * 商品类别码（旧）
	 */
	private IndustryCatalogEnum industryCatalog;
	/**
	 * 商品类别码（新）
	 */
	private String goodsCategoryCode;
	/**
	 * 地区
	 */
	private String areaInfo;
	/**
	 * 交易风控参数
	 */
	private String tradeRiskInfo;
	/**
	 * 查询风控参数
	 */
	private String queryRiskInfo; 
	/**
	 * 用户标识
	 */
	private String identityId;
	/**
	 * 用户类型
	 */
	private IdentityType identityType;
	/**
	 * 请求参数错误返回商户地址
	 */
	private String merchantOrderUrl;
	/**
	 * 支付成功返回商户地址
	 */
	private String frontCallBackUrl;
	/**
	 * UA信息
	 */
	private String userUA;
	/**
	 * 用户ip
	 */
	private String userIp;
	/**
	 * 银行编码
	 */
	private String bankCode;
	/**
	 * 持卡人姓名
	 */
	private String owner;

	/**
	 * 卡号
	 */
	private String cardNo;
	/**
	 * 卡类型
	 */
	private CardTypeEnum cardType;
	/**
	 * 证件类型
	 */
	private IdCardTypeEnum idcardType;
	/**
	 * 证件号
	 */
	private String idcard;
	/**
	 * 手机号
	 */
	private String phoneNo;
	/**
	 * 终端信息
	 */
	private String terminalInfo;
	/**
	 * app信息
	 */
	private String appInfo;

    /**
	 * 备注 ,remark是个json字符串
	 */
    private String remark;

	/**
	 * md5加签域
	 */
	private String hmac;
	/**
	 * 商户ID
	 */
	private String memberNo;
	/**
	 * 商户类型
	 */
	private String memberType;
	
	/**
	 * 扩展信息
	 */
	private String extendInfo;

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getFrontCallBackUrl() {
		return frontCallBackUrl;
	}

	public void setFrontCallBackUrl(String frontCallBackUrl) {
		this.frontCallBackUrl = frontCallBackUrl;
	}

	public String getTradeSysOrderId() {
		return tradeSysOrderId;
	}

	public void setTradeSysOrderId(String tradeSysOrderId) {
		this.tradeSysOrderId = tradeSysOrderId;
	}

	public String getTradeSysNo() {
		return tradeSysNo;
	}

	public void setTradeSysNo(String tradeSysNo) {
		this.tradeSysNo = tradeSysNo;
	}
	
	public String getOrderOrderId() {
		return orderOrderId;
	}

	public void setOrderOrderId(String orderOrderId) {
		this.orderOrderId = orderOrderId;
	}

	public String getOrderSysNo() {
		return orderSysNo;
	}

	public void setOrderSysNo(String orderSysNo) {
		this.orderSysNo = orderSysNo;
	}

	public String getBizModeCode() {
		return bizModeCode;
	}

	public void setBizModeCode(String bizModeCode) {
		this.bizModeCode = bizModeCode;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getOrderExpDate() {
		return orderExpDate;
	}

	public void setOrderExpDate(int orderExpDate) {
		this.orderExpDate = orderExpDate;
	}

	public Date getOrderCreateDate() {
		return orderCreateDate;
	}

	public void setOrderCreateDate(Date orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public CurrencyEnum getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyEnum currency) {
		this.currency = currency;
	}

	public CashierVersionEnum getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(CashierVersionEnum cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public String getThemeCustomCode() {
		return themeCustomCode;
	}

	public void setThemeCustomCode(String themeCustomCode) {
		this.themeCustomCode = themeCustomCode;
	}

	public IndustryCatalogEnum getIndustryCatalog() {
		return industryCatalog;
	}

	public void setIndustryCatalog(IndustryCatalogEnum industryCatalog) {
		this.industryCatalog = industryCatalog;
	}

	public String getGoodsCategoryCode() {
		return goodsCategoryCode;
	}

	public void setGoodsCategoryCode(String goodsCategoryCode) {
		this.goodsCategoryCode = goodsCategoryCode;
	}

	public String getAreaInfo() {
		return areaInfo;
	}

	public void setAreaInfo(String areaInfo) {
		this.areaInfo = areaInfo;
	}

	public String getTradeRiskInfo() {
		return tradeRiskInfo;
	}

	public void setTradeRiskInfo(String tradeRiskInfo) {
		this.tradeRiskInfo = tradeRiskInfo;
	}

	public String getQueryRiskInfo() {
		return queryRiskInfo;
	}

	public void setQueryRiskInfo(String queryRiskInfo) {
		this.queryRiskInfo = queryRiskInfo;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public IdentityType getIdentityType() {
		return identityType;
	}

	public void setIdentityType(IdentityType identityType) {
		this.identityType = identityType;
	}

	public String getMerchantOrderUrl() {
		return merchantOrderUrl;
	}

	public void setMerchantOrderUrl(String merchantOrderUrl) {
		this.merchantOrderUrl = merchantOrderUrl;
	}

	public String getUserUA() {
		return userUA;
	}

	public void setUserUA(String userUA) {
		this.userUA = userUA;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public CardTypeEnum getCardType() {
		return cardType;
	}

	public void setCardType(CardTypeEnum cardType) {
		this.cardType = cardType;
	}

	public IdCardTypeEnum getIdcardType() {
		return idcardType;
	}

	public void setIdcardType(IdCardTypeEnum idcardType) {
		this.idcardType = idcardType;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getTerminalInfo() {
		return terminalInfo;
	}

	public void setTerminalInfo(String terminalInfo) {
		this.terminalInfo = terminalInfo;
	}

	public String getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(String appInfo) {
		this.appInfo = appInfo;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierRequestDTO{");
		sb.append("tradeSysOrderId='").append(tradeSysOrderId);
		sb.append(", tradeSysNo='").append(tradeSysNo);
		sb.append(", orderOrderId='").append(orderOrderId);
		sb.append(", orderSysNo='").append(orderSysNo);
		sb.append(", bizModeCode='").append(bizModeCode);
		sb.append(", merchantNo='").append(merchantNo);
		sb.append(", merchantOrderId='").append(merchantOrderId);
		sb.append(", productName='").append(productName);
		sb.append(", orderExpDate=").append(orderExpDate);
		sb.append(", orderCreateDate=").append(orderCreateDate);
		sb.append(", orderAmount=").append(orderAmount);
		sb.append(", currency=").append(currency);
		sb.append(", cashierVersion=").append(cashierVersion);
		sb.append(", themeCustomCode='").append(themeCustomCode);
		sb.append(", industryCatalog=").append(industryCatalog);
		sb.append(", goodsCategoryCode=").append(goodsCategoryCode);
		sb.append(", areaInfo='").append(areaInfo);
		sb.append(", tradeRiskInfo='").append(tradeRiskInfo);
		sb.append(", identityId='").append(HiddenCode.hiddenIdentityId(identityId));
		sb.append(", identityType='").append(identityType);
		sb.append(", merchantOrderUrl='").append(merchantOrderUrl);
		sb.append(", userUA='").append(userUA);
		sb.append(", userIp='").append(userIp);
		sb.append(", bankCode='").append(bankCode);
		sb.append(", owner='").append(HiddenCode.hiddenName(owner));
		sb.append(", cardNo='").append(HiddenCode.hiddenBankCardNO(cardNo));
		sb.append(", cardType=").append(cardType);
		sb.append(", idcardType=").append(idcardType);
		sb.append(", idcard='").append(HiddenCode.hiddenIdentityCode(idcard));
		sb.append(", phoneNo='").append(HiddenCode.hiddenMobile(phoneNo));
		sb.append(", terminalInfo='").append(terminalInfo);
		sb.append(", appInfo='").append(appInfo);
        sb.append(", remark='").append(remark);
		sb.append(", hMac='").append(hmac);
		sb.append(", memberNo='").append(memberNo);
		sb.append(", memberType='").append(memberType);
		sb.append(", extendInfo='").append(extendInfo);
		sb.append('}');
		return sb.toString();
	}
}
