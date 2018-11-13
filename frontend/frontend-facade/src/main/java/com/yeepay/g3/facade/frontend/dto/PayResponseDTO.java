package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PayResponseDTO extends BasicResponseDTO{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 支付链接
	 */
	private String prepayCode;

	/**
	 * 支付状态
	 */
	private PayStatusEnum payStatus;

	/**
	 * 支付成本
	 */
	private BigDecimal bankTotalCost;

	/**
	 * 支付银行
	 */
	private String payBank;

	/**
	 * 支付银行卡类型
	 */
	private PayBankcardType payBankcardType;

	/**
	 * 银行子系统支付接口
	 */
	private String payInterface;

	/**
	 * 第三方(微信/支付宝)订单号
	 */
	private String transactionId;

	/**
	 * added by zhijun.wang 20170725
	 */
	private String openId;

	/**
	 * added by zhijun.wang 20171214
	 * 通道traceId，返回便于业务方查日志
	 */
	private String traceId;


	/**
	 * added by zengzhi.han 20181016
	 * 一些非重要的扩展参数，比如 被扫是否需要密码(isNeedPassword)
	 */
	private Map<String, String> extParam;

	/**
	 *
	 * added by zengzhi.han 20181024
	 * 现金支付金额
	 */
	private BigDecimal cashFee;

	/**
	 * added by zengzhi.han 20181024
	 * 应结算金额
	 */
	private BigDecimal settlementFee;

	/**
	 * added by zengzhi.han 20181024
	 * 优惠券信息
	 */
	private List<PromotionInfoDTO> promotionInfoDTOS;


	public String getPrepayCode() {
		return prepayCode;
	}

	public void setPrepayCode(String prepayCode) {
		this.prepayCode = prepayCode;
	}

	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	public BigDecimal getBankTotalCost() {
		return bankTotalCost;
	}

	public void setBankTotalCost(BigDecimal bankTotalCost) {
		this.bankTotalCost = bankTotalCost;
	}

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}

	public PayBankcardType getPayBankcardType() {
		return payBankcardType;
	}

	public void setPayBankcardType(PayBankcardType payBankcardType) {
		this.payBankcardType = payBankcardType;
	}

	public String getPayInterface() {
		return payInterface;
	}

	public void setPayInterface(String payInterface) {
		this.payInterface = payInterface;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}


	public BigDecimal getCashFee() {
		return cashFee;
	}

	public void setCashFee(BigDecimal cashFee) {
		this.cashFee = cashFee;
	}

	public BigDecimal getSettlementFee() {
		return settlementFee;
	}

	public void setSettlementFee(BigDecimal settlementFee) {
		this.settlementFee = settlementFee;
	}

	public List<PromotionInfoDTO> getPromotionInfoDTOS() {
		return promotionInfoDTOS;
	}

	public void setPromotionInfoDTOS(List<PromotionInfoDTO> promotionInfoDTOS) {
		this.promotionInfoDTOS = promotionInfoDTOS;
	}

}
