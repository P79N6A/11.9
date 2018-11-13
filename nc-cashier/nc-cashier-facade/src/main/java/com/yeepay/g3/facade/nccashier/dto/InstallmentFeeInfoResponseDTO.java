package com.yeepay.g3.facade.nccashier.dto;

import java.math.BigDecimal;

/**
 * 银行卡分期手续费等金额信息返回实体
 * 
 * @author duangduang
 *
 */
public class InstallmentFeeInfoResponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;

	/**
	 * 银行卡分期对应银行和期数对应的分期利息金额，后期可以下掉
	 */
	@Deprecated
	private BigDecimal feeAmount;

	/**
	 * 补贴后的手续费金额，即用户真正承担的手续费
	 */
	private BigDecimal feeAmountAfterSubsidy;

	/**
	 * 首期还款额
	 */
	private BigDecimal firstPayment;

	/**
	 * 每期还款额
	 */
	private BigDecimal terminalPayment;

	public InstallmentFeeInfoResponseDTO() {

	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public BigDecimal getFeeAmountAfterSubsidy() {
		return feeAmountAfterSubsidy;
	}

	public void setFeeAmountAfterSubsidy(BigDecimal feeAmountAfterSubsidy) {
		this.feeAmountAfterSubsidy = feeAmountAfterSubsidy;
	}

	public BigDecimal getFirstPayment() {
		return firstPayment;
	}

	public void setFirstPayment(BigDecimal firstPayment) {
		this.firstPayment = firstPayment;
	}

	public BigDecimal getTerminalPayment() {
		return terminalPayment;
	}

	public void setTerminalPayment(BigDecimal terminalPayment) {
		this.terminalPayment = terminalPayment;
	}

	@Override
	public String toString() {
		return "InstallmentFeeInfoResponseDTO [orderAmount=" + orderAmount + ", feeAmount=" + feeAmount
				+ ", feeAmountAfterSubsidy=" + feeAmountAfterSubsidy + ", firstPayment=" + firstPayment
				+ ", terminalPayment=" + terminalPayment + ", returnCode=" + getReturnCode() + ", returnMsg="
				+ getReturnMsg() + ", processStatusEnum=" + getProcessStatusEnum() + "]";
	}

}
