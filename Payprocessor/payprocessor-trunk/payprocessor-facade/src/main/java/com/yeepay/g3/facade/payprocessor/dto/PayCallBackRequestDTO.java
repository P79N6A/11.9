package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.yeepay.g3.facade.payprocessor.enumtype.CombPayOrderType;
import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;

/**
 * @author peile.fan
 *
 */
public class PayCallBackRequestDTO implements Serializable {

	private static final long serialVersionUID = -8263069149560653598L;

	/**
	 * 系统唯一码
	 */
	private String dealUniqueSerialNo;

	/**
	 * 订单方
	 */
	private String orderSystem;

	/**
	 * 订单方订单号+订单方为唯一约束
	 */
	private String orderNo;

	/**
	 * 请求系统
	 * 
	 */
	protected String requestSystem;

	/**
	 * 请求系统主键
	 */
	protected String requestSysId;

	/**
	 * 商户编号
	 */
	private String customerNo;

	/**
	 * 商户名称
	 */
	protected String customerName;

	/**
	 * 支付金额
	 */
	private BigDecimal payAmount;
	
	/**
	 * 用户手续费
	 */
	private BigDecimal userFee;
	

	/**
	 * 成本
	 */
	private BigDecimal cost;

	
	/**
	 * 收款场景
	 */
	private String cashierVersion;

	/**
	 * 支付产品
	 */
	private String payProduct;
	
	/**
	 * 支付类型
	 */
	private String payOrderType;
	
	
	/**
	 * 支付状态
	 */
	private String payStatus;
	/**
	 * 平台类型
	 */
	private String platformType;

	/**************************************** 银行卡信息 *******************************************/
	/**
	 * 持卡人
	 */
	private String owner;
	/**
	 * 身份证号
	 */
	private String idCardNo;
	/**
	 * 手机号
	 */
	private String mobilePhoneNo;
	/**
	 * 卡号
	 */
	private String cardNo;
	/**
	 * 银行名称
	 */
	private String bankName;

	/**
	 * 支付银行编码
	 */
	private String bankId;
	/**
	 * 
	 * 卡类型
	 */
	private String cardType;

	/**************************************** 支付通道信息start *******************************************/

	/**
	 * 银行订单号
	 */
	private String bankOrderNo;

	/**
	 * 授权号
	 */
	private String bankSeq;

	/**
	 * 银行交易流水号
	 */
	private String bankTrxId;

	/**
	 * 支付时间
	 */
	private Date payTime;

	/**
	 * 通道编码
	 */
	private String channelId;

	/**
	 * 用户支付客户端IP
	 */
	private String payerIp;

	/**
	 * 支付订单号,主键,唯一索引
	 */
	private String recordNo;

	/**
	 * 网银支付类型
	 */
	private String netPayType;

	/**
	 * 分期支付公司
	 */
	private String loanCompany;
	/**
	 * 分期支付期数
	 */
	private String loanTerm;

	/**
	 * 扩展信息
	 */
	private String extendMsg;
	
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 用户标识
	 */
	private String userType;
	
	/**
	 * 付款商编
	 */
	private String debitCustomerNo;
	
	/**
	 * 付款账号
	 */
	private String debitAccountNo;

	/**
	 * 基础产品码
	 */
	private String basicProductCode;

	private String openId;

	/**
	 * 钱包高低配
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
	 * 第二支付实体
	 */
	private CombResponseDTO combResponseDTO;

	/**
	 * 如果是组合支付，有此参数，第一支付金额
	 */
	private BigDecimal firstPayAmount;

	/**
	 * 预授权完成金额
	 */
	private BigDecimal preAuthAmount;

	/**
	 * 返回码
	 * 返回码和返回信息，目前只有循环预授权需要这两个字段，返回子单错误码和错误信息
	 */
	private String returnCode;

	/**
	 * 返回信息
	 */
	private String returnMsg;
	

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(String cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public String getPayProduct() {
		return payProduct;
	}

	public void setPayProduct(String payProduct) {
		this.payProduct = payProduct;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}

	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}

	public String getBankSeq() {
		return bankSeq;
	}

	public void setBankSeq(String bankSeq) {
		this.bankSeq = bankSeq;
	}

	public String getBankTrxId() {
		return bankTrxId;
	}

	public void setBankTrxId(String bankTrxId) {
		this.bankTrxId = bankTrxId;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPayerIp() {
		return payerIp;
	}

	public void setPayerIp(String payerIp) {
		this.payerIp = payerIp;
	}

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	public String getNetPayType() {
		return netPayType;
	}

	public void setNetPayType(String netPayType) {
		this.netPayType = netPayType;
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

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getExtendMsg() {
		return extendMsg;
	}

	public void setExtendMsg(String extendMsg) {
		this.extendMsg = extendMsg;
	}

	public String getOrderSystem() {
		return orderSystem;
	}

	public void setOrderSystem(String orderSystem) {
		this.orderSystem = orderSystem;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestSysId() {
		return requestSysId;
	}

	public void setRequestSysId(String requestSysId) {
		this.requestSysId = requestSysId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	

	public String getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(String payOrderType) {
		this.payOrderType = payOrderType;
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
	
	public BigDecimal getUserFee() {
		return userFee;
	}

	public void setUserFee(BigDecimal userFee) {
		this.userFee = userFee;
	}
	
    public String getDebitCustomerNo() {
        return debitCustomerNo;
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

	public String getWalletLevel() {
		return walletLevel;
	}

	public void setWalletLevel(String walletLevel) {
		this.walletLevel = walletLevel;
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

	public CombResponseDTO getCombResponseDTO() {
		return combResponseDTO;
	}

	public void setCombResponseDTO(CombResponseDTO combResponseDTO) {
		this.combResponseDTO = combResponseDTO;
	}

	public BigDecimal getFirstPayAmount() {
		return firstPayAmount;
	}

	public void setFirstPayAmount(BigDecimal firstPayAmount) {
		this.firstPayAmount = firstPayAmount;
	}

	public BigDecimal getPreAuthAmount() {
		return preAuthAmount;
	}

	public void setPreAuthAmount(BigDecimal preAuthAmount) {
		this.preAuthAmount = preAuthAmount;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PayCallBackRequestDTO [dealUniqueSerialNo=");
		builder.append(dealUniqueSerialNo);
		builder.append(", orderSystem=");
		builder.append(orderSystem);
		builder.append(", orderNo=");
		builder.append(orderNo);
		builder.append(", requestSystem=");
		builder.append(requestSystem);
		builder.append(", requestSysId=");
		builder.append(requestSysId);
		builder.append(", customerNo=");
		builder.append(customerNo);
		builder.append(", customerName=");
		builder.append(customerName);
		builder.append(", payAmount=");
		builder.append(payAmount);
		builder.append(", userFee=");
		builder.append(userFee);
		builder.append(", cost=");
		builder.append(cost);
		builder.append(", cashierVersion=");
		builder.append(cashierVersion);
		builder.append(", payProduct=");
		builder.append(payProduct);
		builder.append(", payOrderType=");
		builder.append(payOrderType);
		builder.append(", payStatus=");
		builder.append(payStatus);
		builder.append(", owner=");
		builder.append(HiddenCode.hiddenName(owner));
		builder.append(", idCardNo=");
		builder.append(HiddenCode.hiddenIdentityCode(idCardNo));
		builder.append(", mobilePhoneNo=");
		builder.append(HiddenCode.hiddenMobile(mobilePhoneNo));
		builder.append(", cardNo=");
		builder.append(HiddenCode.hiddenBankCardNO(cardNo));
		builder.append(", bankName=");
		builder.append(bankName);
		builder.append(", bankId=");
		builder.append(bankId);
		builder.append(", cardType=");
		builder.append(cardType);
		builder.append(", bankOrderNo=");
		builder.append(bankOrderNo);
		builder.append(", bankSeq=");
		builder.append(bankSeq);
		builder.append(", bankTrxId=");
		builder.append(bankTrxId);
		builder.append(", payTime=");
		builder.append(payTime);
		builder.append(", channelId=");
		builder.append(channelId);
		builder.append(", payerIp=");
		builder.append(payerIp);
		builder.append(", recordNo=");
		builder.append(recordNo);
		builder.append(", netPayType=");
		builder.append(netPayType);
		builder.append(", loanCompany=");
		builder.append(loanCompany);
		builder.append(", loanTerm=");
		builder.append(loanTerm);
		builder.append(", platformType=");
		builder.append(platformType);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", extendMsg=");
		builder.append(HiddenCode.hiddenExtInfo(extendMsg));
		builder.append(",debitCustomerNo=");
		builder.append(debitCustomerNo);
		builder.append(",debitAccountNo=");
		builder.append(debitAccountNo);
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
		builder.append(",combResponseDTO=");
		builder.append(combResponseDTO);
		builder.append(",firstPayAmount=");
		builder.append(firstPayAmount);
		builder.append(",preAuthAmount=");
		builder.append(preAuthAmount);
		builder.append(",returnCode=");
		builder.append(returnCode);
		builder.append(",returnMsg=");
		builder.append(returnMsg);
		builder.append("]");
		return builder.toString();
	}

}
