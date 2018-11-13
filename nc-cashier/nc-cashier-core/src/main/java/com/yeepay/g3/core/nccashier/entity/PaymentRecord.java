package com.yeepay.g3.core.nccashier.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.vo.StatusFlow;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * Created by xiewei on --.
 */
public class PaymentRecord {
	
	/**
	 * 主键
	 */
	private long id;
	/**
	 * 支付请求ID
	 */
	private long paymentRequestId;
	/**
	 * 交易系统订单号
	 */
	private String tradeSysOrderId;
	/**
	 * 交易系统编码
	 */
	private String tradeSysNo;
	/**
	 * 订单方订单号
	 */
	private String orderOrderId;
	/**
	 * 订单方编码
	 */
	private String orderSysNo;
	/**
	 * 业务模式编码
	 */
	private String bizModeCode;
	/**
	 * TOKENID
	 */
	private String tokenId;
	/**
	 * 支付金额
	 */
	private BigDecimal paymentAmount;
	/**
	 * 成本
	 */
	private BigDecimal cost;
	/**
	 * 支付方编码
	 */
	private String paymentSysNo;
	/**
	 * 支付方流水号
	 */
	private String paymentOrderNo;
	/**
	 * 支付工具编码
	 */
	private String payProductCode;
	/**
	 * 银行订单号
	 */
	private String bankOrderNo;
	/**
	 * 银行通道
	 */
	private String bankChannelNo;
	/**
	 * 商户编号
	 */
	private String merchantNo;
	/**
	 * 商户名称
	 */
	private String merchantName;
	/**
	 * 商户系统订单号
	 */
	private String merchantOrderId;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 支付类型
	 */
	private String payType;
	/**
	 * 交易风控参数
	 */
	private String tradeRiskInfo;
	/**
	 * 地区
	 */
	private String areaInfo;
	/**
	 * MCC商户类别码
	 */
	private String mcc;
	/**
	 * 短验类型
	 */
	private String smsVerifyType;
	/**
	 * 卡信息ID 
	 */
	private String cardInfoId;
	/**
	 * 卡账户id，首次支付临时卡id，绑卡支付绑卡id、签约关系ID
	 */
	private String bindId;
	/**
	 * 持卡人姓名
	 */

	private String owner;
	/**
	 * 卡类型
	 */
	private String cardType;
	/**
	 * 银行编码
	 */
	private String bankCode;
	/**
	 * 卡号
	 */

	private String cardNo;
	/**
	 * 证件类型
	 */
	private String idCardType;
	/**
	 * 手机号
	 */

	private String phoneNo;
	/**
	 * 证件号
	 */

	private String idCard;
	/**
	 * 短验码
	 */
	private String verifyCode;
	/**
	 * 状态
	 */
	private PayRecordStatusEnum state;
	/**
	 * 会员编号
	 */
	private String memberNo;

	/**
	 * 会员类型
	 */
	private String memberType;
	/**
	 * 错误编码
	 */
	private String errorCode;
	/**
	 * 错误原因
	 */
	private String errorMsg;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 版本
	 */
	private int version;

	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 银行子系统订单号
	 */
	private String tradeSerialNo;
	/**
	 * 补充项
	 */
	private int needItem;

	private String payTool;
	
	/**
	 * 目前唯一含义：分期期数
	 */
	private int period;

	/**
	 * 支付扩展参数
	 */
	private String paymentExt;
	
	/**
	 * 实际支付金额
	 */
	private BigDecimal actualAmount;

	/**
	 * 付款商编
	 */
	private String payMerchantNo;

	/**
	 * 跳转类型
	 */
	private String redirectType;

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("PaymentRecord{");
		sb.append("id=").append(id);
		sb.append(", paymentRequestId=").append(paymentRequestId);
		sb.append(", tradeSysOrderId='").append(tradeSysOrderId).append('\'');
		sb.append(", tradeSysNo='").append(tradeSysNo).append('\'');
		sb.append(", businCode='").append(bizModeCode).append('\'');
		sb.append(", tokenId='").append(tokenId).append('\'');
		sb.append(", paymentAmount=").append(paymentAmount);
		sb.append(", paymentSysNo='").append(paymentSysNo).append('\'');
		sb.append(", paymentOrderNo='").append(paymentOrderNo).append('\'');
		sb.append(", payProductCode='").append(payProductCode).append('\'');
		sb.append(", bankOrderNo='").append(bankOrderNo).append('\'');
		sb.append(", bankChannelNo='").append(bankChannelNo).append('\'');
		sb.append(", merchantNo='").append(merchantNo).append('\'');
		sb.append(", merchantName='").append(merchantName).append('\'');
		sb.append(", merchantOrderId='").append(merchantOrderId).append('\'');
		sb.append(", productName='").append(productName).append('\'');
		sb.append(", payType='").append(payType).append('\'');
		sb.append(", tradeRiskInfo='").append(tradeRiskInfo).append('\'');
		sb.append(", areaInfo='").append(areaInfo).append('\'');
		sb.append(", mcc='").append(mcc).append('\'');
		sb.append(", smsVerifyType='").append(smsVerifyType).append('\'');
		sb.append(", needItem='").append(needItem).append('\'');
		sb.append(", bindId=").append(bindId);
		sb.append(", cardInfoId=").append(cardInfoId);
		sb.append(", owner='").append(HiddenCode.hiddenName(owner)).append('\'');
		sb.append(", cardType='").append(cardType).append('\'');
		sb.append(", bankCode='").append(bankCode).append('\'');
		sb.append(", cardNo='").append(HiddenCode.hiddenBankCardNO(cardNo)).append('\'');
		sb.append(", idCardType='").append(idCardType).append('\'');
		sb.append(", phoneNo='").append(HiddenCode.hiddenMobile(phoneNo)).append('\'');
		sb.append(", idCard='").append(HiddenCode.hiddenIdentityCode(idCard)).append('\'');
		sb.append(", verifyCode='").append(verifyCode).append('\'');
		sb.append(", state='").append(state).append('\'');
		sb.append(", memberNo='").append(memberNo).append('\'');
		sb.append(", memberType='").append(memberType).append('\'');
		sb.append(", errorCode='").append(errorCode).append('\'');
		sb.append(", errorMsg='").append(errorMsg).append('\'');
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", version=").append(version);
		sb.append(", tradeSerialNo=").append(tradeSerialNo);
		sb.append(", payTool=").append(payTool);
		sb.append(", period=").append(period);
		sb.append(", paymentExt=").append(paymentExt);
		sb.append(", payMerchantNo=").append(payMerchantNo);
		sb.append(", redirectType=").append(redirectType);
		sb.append('}');
		return sb.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
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

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getPaymentSysNo() {
		return paymentSysNo;
	}

	public void setPaymentSysNo(String paymentSysNo) {
		this.paymentSysNo = paymentSysNo;
	}

	public String getPaymentOrderNo() {
		return paymentOrderNo;
	}

	public void setPaymentOrderNo(String paymentOrderNo) {
		this.paymentOrderNo = paymentOrderNo;
	}

	public String getPayProductCode() {
		return payProductCode;
	}

	public void setPayProductCode(String payProductCode) {
		this.payProductCode = payProductCode;
	}

	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}

	public String getBankChannelNo() {
		return bankChannelNo;
	}

	public void setBankChannelNo(String bankChannelNo) {
		this.bankChannelNo = bankChannelNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getTradeRiskInfo() {
		return tradeRiskInfo;
	}

	public void setTradeRiskInfo(String tradeRiskInfo) {
		this.tradeRiskInfo = tradeRiskInfo;
	}

	public String getAreaInfo() {
		return areaInfo;
	}

	public void setAreaInfo(String areaInfo) {
		this.areaInfo = areaInfo;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getSmsVerifyType() {
		return smsVerifyType;
	}

	public void setSmsVerifyType(String smsVerifyType) {
		this.smsVerifyType = smsVerifyType;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getCardInfoId() {
		return cardInfoId;
	}

	public void setCardInfoId(String cardInfoId) {
		this.cardInfoId = cardInfoId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}



	public PayRecordStatusEnum getState() {
		return state;
	}

	public void setState(PayRecordStatusEnum state) {
		this.state = state;
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

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTradeSerialNo() {
		return tradeSerialNo;
	}

	public void setTradeSerialNo(String tradeSerialNo) {
		this.tradeSerialNo = tradeSerialNo;
	}

	/**
	 * @return the needItem
	 */
	public int getNeedItem() {
		return needItem;
	}

	/**
	 * @param needItem the needItem to set
	 */
	public void setNeedItem(int needItem) {
		this.needItem = needItem;
	}

	public String getPayTool() {
		return payTool;
	}

	public void setPayTool(String payTool) {
		this.payTool = payTool;
	}
	
	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getPaymentExt() {
		return paymentExt;
	}

	public void setPaymentExt(String paymentExt) {
		this.paymentExt = paymentExt;
	}
	
	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getPayMerchantNo() {
		return payMerchantNo;
	}

	public void setPayMerchantNo(String payMerchantNo) {
		this.payMerchantNo = payMerchantNo;
	}

	/**
	 * 更新record的paymentRecordNo和状态
	 * 
	 * @param recordNo
	 * @param status
	 */
	public void updateRecordNoAndState(String recordNo, PayRecordStatusEnum status) {
		setPaymentOrderNo(recordNo);
		setState(status);
	}

	public boolean checkStatusEnd(OrderAction orderAction) {
		boolean legal = StatusFlow.legalStatus(state, orderAction);
		return legal;
	}

	public String getRedirectType() {
		return redirectType;
	}

	public void setRedirectType(String redirectType) {
		this.redirectType = redirectType;
	}
}
