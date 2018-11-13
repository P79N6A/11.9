package com.yeepay.g3.facade.payprocessor.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yeepay.g3.facade.payprocessor.enumtype.CashierVersion;
import com.yeepay.g3.facade.payprocessor.enumtype.PayCardType;
import com.yeepay.g3.facade.payprocessor.enumtype.PaymentStatusEnum;
import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;

/**
 * 查单返回参数
 * 
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class QueryResponseDTO extends BasicResponseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -133180639663394507L;
	/**
	 * 唯一流水号
	 */
	private String uniqueOrderNo;
	/**
	 * 订单金额
	 */
	private BigDecimal payAmount;
	/**
	 * 订单状态
	 */
	private PaymentStatusEnum paymentStatus;
	/**
	 * 成本
	 */
	private BigDecimal cost;
	/**
	 * 账户名称
	 */
	private String cardName;
	/**
	 * 身份证号
	 */
	private String idCardNo;
	/**
	 * 卡号
	 */
	private String bankCardNo;
	/**
	 * 手机号
	 */
	private String mobilePhoneNo;
	/**
	 * 收款场景
	 */
	@Deprecated
	private CashierVersion cashierVersion;

	private String cashierType;
	/**
	 * 支付产品
	 */
	private String payProduct;
	/**
	 * 银行子系统订单号
	 */
	private String bankOrderId;
	/**
	 * 银行成功时间
	 */
	private Date bankPaySuccDate;
	/**
	 * 支付成功时间
	 */
	private Date paySuccDate;
	/**
	 * 支付订单号
	 */
	private String paymentSysNo;
	/**
	 * 银行编码
	 */
	private String bankId;
	/**
	 * 支付IP地址
	 */
	private String paymentIp;
	/**
	 * 卡种
	 */
	private PayCardType cardType;
	/**
	 * 通道编码
	 */
	private String channelId;

	/**
	 * 用户手续费
	 */
	private BigDecimal userFee;

	/**
	 * 订单方
	 */
	private String orderSystem;

	/**
	 * 卡种 B2C/B2B
	 */
	private String netPayType;

	/**
	 * 银行子系统交易流水号
	 */
	private String bankTrxId;

	/**
	 * 分期公司
	 */
	private String loanCompany;
	/**
	 * 分期期数
	 */
	private String loanTerm;

	private String bankName;

	private String userId;

	private String userType;
	
	/**
	 * 平台类型
	 */
	private String platformType;

	/**
	 * 支付扩展信息
	 */
	private String extendMsg;
	
	 /**
     * 付款方商户编号
     */
    private String debitCustomerNo;

    /**
     * 付款方账号
     */
    private String debitAccountNo;

	/**
	 * 零售产品码
	 */
	private String retailProductCode;
	/**
	 * 基础产品码
	 */
	private String basicProductCode;

	private String openId;
	/**
	 * 钱包高低配标记
	 */
	private String walletLevel;

	/**
	 * 分期付款期数
	 */
	private Integer cflCount;

	/**
	 * 分期付款手续费率
	 */
	private BigDecimal cflRate = new BigDecimal(0);

	/**
	 * 商户补贴手续费率
	 */
	private BigDecimal merchantFeeSubsidy = new BigDecimal(0);

	/**
	 * 商户补贴手续费
	 */
	private BigDecimal merchantAmountSubsidy = new BigDecimal(0);

	/**
	 * 付费角色
	 */
	private String payFeeRole;

	private String feeFlowNo;

	/**
	 * 支付类型
	 */
	private String payType;

	public String getUniqueOrderNo() {
		return uniqueOrderNo;
	}

	public void setUniqueOrderNo(String uniqueOrderNo) {
		this.uniqueOrderNo = uniqueOrderNo;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public PaymentStatusEnum getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}

	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}

	@Deprecated
	public CashierVersion getCashierVersion() {
		return cashierVersion;
	}

	@Deprecated
	public void setCashierVersion(CashierVersion cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public String getBankOrderId() {
		return bankOrderId;
	}

	public void setBankOrderId(String bankOrderId) {
		this.bankOrderId = bankOrderId;
	}

	public Date getBankPaySuccDate() {
		return bankPaySuccDate;
	}

	public void setBankPaySuccDate(Date bankPaySuccDate) {
		this.bankPaySuccDate = bankPaySuccDate;
	}

	public Date getPaySuccDate() {
		return paySuccDate;
	}

	public void setPaySuccDate(Date paySuccDate) {
		this.paySuccDate = paySuccDate;
	}

	public String getPaymentSysNo() {
		return paymentSysNo;
	}

	public void setPaymentSysNo(String paymentSysNo) {
		this.paymentSysNo = paymentSysNo;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getPaymentIp() {
		return paymentIp;
	}

	public void setPaymentIp(String paymentIp) {
		this.paymentIp = paymentIp;
	}

	public PayCardType getCardType() {
		return cardType;
	}

	public void setCardType(PayCardType cardType) {
		this.cardType = cardType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public BigDecimal getUserFee() {
		return userFee;
	}

	public void setUserFee(BigDecimal userFee) {
		this.userFee = userFee;
	}

	public String getPayProduct() {
		return payProduct;
	}

	public void setPayProduct(String payProduct) {
		this.payProduct = payProduct;
	}

	public String getOrderSystem() {
		return orderSystem;
	}

	public void setOrderSystem(String orderSystem) {
		this.orderSystem = orderSystem;
	}

	public String getNetPayType() {
		return netPayType;
	}

	public void setNetPayType(String netPayType) {
		this.netPayType = netPayType;
	}

	public String getBankTrxId() {
		return bankTrxId;
	}

	public void setBankTrxId(String bankTrxId) {
		this.bankTrxId = bankTrxId;
	}

	public String getLoanCompany() {
		return loanCompany;
	}

	public void setLoanCompany(String loanCompany) {
		this.loanCompany = loanCompany;
	}

	public String getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(String loanTerm) {
		this.loanTerm = loanTerm;
	}

	public String getCashierType() {
		return cashierType;
	}

	public void setCashierType(String cashierType) {
		this.cashierType = cashierType;
	}

	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	
    public String getDebitCustomerNo() {
        return debitCustomerNo;
    }

	public String getExtendMsg() {
		return extendMsg;
	}

	public void setExtendMsg(String extendMsg) {
		this.extendMsg = extendMsg;
	}
    
    public void setDebitCustomerNo(String debitCustomerNo) {
        this.debitCustomerNo = debitCustomerNo;
    }

    public String getDebitAccountNo() {
        return debitAccountNo;
    }

    public void setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
    }

	public String getRetailProductCode() {
		return retailProductCode;
	}

	public void setRetailProductCode(String retailProductCode) {
		this.retailProductCode = retailProductCode;
	}

	public String getBasicProductCode() {
		return basicProductCode;
	}

	public void setBasicProductCode(String basicProductCode) {
		this.basicProductCode = basicProductCode;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getCflCount() {
		return cflCount;
	}

	public void setCflCount(Integer cflCount) {
		this.cflCount = cflCount;
	}

	public BigDecimal getCflRate() {
		return cflRate;
	}

	public void setCflRate(BigDecimal cflRate) {
		this.cflRate = cflRate;
	}

	public BigDecimal getMerchantFeeSubsidy() {
		return merchantFeeSubsidy;
	}

	public void setMerchantFeeSubsidy(BigDecimal merchantFeeSubsidy) {
		this.merchantFeeSubsidy = merchantFeeSubsidy;
	}

	public BigDecimal getMerchantAmountSubsidy() {
		return merchantAmountSubsidy;
	}

	public void setMerchantAmountSubsidy(BigDecimal merchantAmountSubsidy) {
		this.merchantAmountSubsidy = merchantAmountSubsidy;
	}

	/**
	 * @return the walletLevel
	 */
	public String getWalletLevel() {
		return walletLevel;
	}

	/**
	 * @param walletLevel the walletLevel to set
	 */
	public void setWalletLevel(String walletLevel) {
		this.walletLevel = walletLevel;
	}

	public String getPayFeeRole() {
		return payFeeRole;
	}

	public void setPayFeeRole(String payFeeRole) {
		this.payFeeRole = payFeeRole;
	}

	public String getFeeFlowNo() {
		return feeFlowNo;
	}

	public void setFeeFlowNo(String feeFlowNo) {
		this.feeFlowNo = feeFlowNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryResponseDTO [uniqueOrderNo=");
		builder.append(uniqueOrderNo);
		builder.append(", payAmount=");
		builder.append(payAmount);
		builder.append(", paymentStatus=");
		builder.append(paymentStatus);
		builder.append(", cost=");
		builder.append(cost);
		builder.append(", bankName=");
		builder.append(bankName);
		builder.append(", cardName=");
		builder.append(HiddenCode.hiddenName(cardName));
		builder.append(", idCardNo=");
		builder.append(HiddenCode.hiddenIdentityCode(idCardNo));
		builder.append(", bankCardNo=");
		builder.append(HiddenCode.hiddenBankCardNO(bankCardNo));
		builder.append(", mobilePhoneNo=");
		builder.append(HiddenCode.hiddenMobile(mobilePhoneNo));
		builder.append(", cashierType=");
		builder.append(cashierType);
		builder.append(", payProduct=");
		builder.append(payProduct);
		builder.append(", bankOrderId=");
		builder.append(bankOrderId);
		builder.append(", bankTrxId=");
		builder.append(bankTrxId);
		builder.append(", bankPaySuccDate=");
		builder.append(bankPaySuccDate);
		builder.append(", paySuccDate=");
		builder.append(paySuccDate);
		builder.append(", paymentSysNo=");
		builder.append(paymentSysNo);
		builder.append(", bankId=");
		builder.append(bankId);
		builder.append(", paymentIp=");
		builder.append(paymentIp);
		builder.append(", cardType=");
		builder.append(cardType);
		builder.append(", channelId=");
		builder.append(channelId);
		builder.append(", userFee=");
		builder.append(userFee);
		builder.append(", orderSystem=");
		builder.append(orderSystem);
		builder.append(", netPayType=");
		builder.append(netPayType);
		builder.append(", recordNo=");
		builder.append(recordNo);
		builder.append(", outTradeNo=");
		builder.append(outTradeNo);
		builder.append(", orderNo=");
		builder.append(orderNo);
		builder.append(", dealUniqueSerialNo=");
		builder.append(dealUniqueSerialNo);
		builder.append(", payOrderType=");
		builder.append(payOrderType);
		builder.append(", customerNumber=");
		builder.append(customerNumber);
		builder.append(", responseCode=");
		builder.append(responseCode);
		builder.append(", responseMsg=");
		builder.append(responseMsg);
		builder.append(", processStatus=");
		builder.append(processStatus);
		builder.append(", loanCompany=");
		builder.append(loanCompany);
		builder.append(", loanTerm=");
		builder.append(loanTerm);
		builder.append(", platformType=");
		builder.append(platformType);
		builder.append(", extendMsg=");
		builder.append(HiddenCode.hiddenExtInfo(extendMsg));
		builder.append(", debitCustomerNo=");
        builder.append(debitCustomerNo);
        builder.append(", debitAccountNo=");
        builder.append(debitAccountNo);
		builder.append(",retailProductCode=");
		builder.append(retailProductCode);
		builder.append(",basicProductCode=");
		builder.append(basicProductCode);
		builder.append(",openId=");
		builder.append(openId);
		builder.append(",walletLevel=");
		builder.append(walletLevel);
		builder.append(",cflCount=");
		builder.append(cflCount);
		builder.append(",cflRate=");
		builder.append(cflRate);
		builder.append(",merchantFeeSubsidy=");
		builder.append(merchantFeeSubsidy);
		builder.append(",merchantAmountSubsidy=");
		builder.append(merchantAmountSubsidy);
		builder.append(",payFeeRole=");
		builder.append(payFeeRole);
		builder.append(",feeFlowNo=");
		builder.append(feeFlowNo);
		builder.append(",payType=");
		builder.append(payType);
		builder.append(",combResponseDTO=");
		builder.append(combResponseDTO);
		builder.append("]");
		return builder.toString();
	}
}
