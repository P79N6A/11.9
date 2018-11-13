package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.CombPayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付单查询结果
 * 
 * @author peile.fan
 *
 */
public class PayRecordResponseDTO extends ResponseStatusDTO {

	private static final long serialVersionUID = -4183071141883872374L;


	/**
	 * 支付记录支付状态
	 */
	private TrxStatusEnum trxStatus;

	/**
	 * 订单方状态
	 */
	private OrderSystemStatusEnum orderSystemStatus;

	/**
	 * 短验状态
	 */
	private SmsCheckResultEnum smsState;

	/**
	 * 银行通道编码
	 */
	private String frpCode;

	/**
	 * 银行订单号
	 */
	private String bankOrderNO;

	/**
	 * 银行交易流水号
	 */
	private String bankTrxId;

	/**
	 * 成本
	 */
	private BigDecimal cost;

	/**
	 * 支付订单号
	 */
	private String recordNo;
	
	/**
	 * 卡账户id
	 */
	private String cardId;

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
	 * 商户编号
	 */
	private String customerNo;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;

	/**
	 * 组合支付实体
	 */
	private CombResponseDTO combResponseDTO;

	/**
	 * 组合支付，第一支付金额 + 第二支付金额 = 订单金额
	 */
	private BigDecimal firstPayAmount;

	/**
	 * 支付子单类型
	 */
	private String payOrderType;
	
	
	public TrxStatusEnum getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(TrxStatusEnum trxStatus) {
		this.trxStatus = trxStatus;
	}

	public OrderSystemStatusEnum getOrderSystemStatus() {
		return orderSystemStatus;
	}

	public void setOrderSystemStatus(OrderSystemStatusEnum orderSystemStatus) {
		this.orderSystemStatus = orderSystemStatus;
	}

	public SmsCheckResultEnum getSmsState() {
		return smsState;
	}

	public void setSmsState(SmsCheckResultEnum smsState) {
		this.smsState = smsState;
	}

	public String getFrpCode() {
		return frpCode;
	}

	public void setFrpCode(String frpCode) {
		this.frpCode = frpCode;
	}

	public String getBankOrderNO() {
		return bankOrderNO;
	}

	public void setBankOrderNO(String bankOrderNO) {
		this.bankOrderNO = bankOrderNO;
	}

	public String getBankTrxId() {
		return bankTrxId;
	}

	public void setBankTrxId(String bankTrxId) {
		this.bankTrxId = bankTrxId;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
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

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
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

	public String getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(String payOrderType) {
		this.payOrderType = payOrderType;
	}

	@Override
	public String toString() {
		return "PayRecordResponseDTO{" +
				"trxStatus=" + trxStatus +
				", orderSystemStatus=" + orderSystemStatus +
				", smsState=" + smsState +
				", frpCode='" + frpCode + '\'' +
				", bankOrderNO='" + bankOrderNO + '\'' +
				", bankTrxId='" + bankTrxId + '\'' +
				", cost=" + cost +
				", recordNo='" + recordNo + '\'' +
				", cardId='" + cardId + '\'' +
				", cflCount=" + cflCount +
				", cflRate=" + cflRate +
				", merchantFeeSubsidy=" + merchantFeeSubsidy +
				", merchantAmountSubsidy=" + merchantAmountSubsidy +
				", customerNo='" + customerNo + '\'' +
				", outTradeNo='" + outTradeNo + '\'' +
				", combResponseDTO=" + combResponseDTO +
				", firstPayAmount=" + firstPayAmount +
				", payOrderType=" + payOrderType +
				", responseCode='" + responseCode + '\'' +
				", responseMsg='" + responseMsg + '\'' +
				", processStatus=" + processStatus +
				'}';
	}
}
