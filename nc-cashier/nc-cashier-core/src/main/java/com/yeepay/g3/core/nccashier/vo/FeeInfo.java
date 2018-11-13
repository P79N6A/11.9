package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收银台内部费率信息
 * 
 * @author duangduang
 *
 */
public class FeeInfo implements Serializable {

	private static final long serialVersionUID = -8174234230937235674L;

	/**
	 * 计费类型
	 */
	private String chargeType;

	/**
	 * 收款方手续费
	 */
	private BigDecimal payeeFee = BigDecimal.ZERO;
	/**
	 * 付款方手续费
	 */
	private BigDecimal payerFee = BigDecimal.ZERO;

	/**
	 * 计费角色
	 */
	private String feeRole;

	private String payeeCalPolicy;

	private String payerCalPolicy;

	/**
	 * 收款方手续费值，该值目前用于银行卡分期的补贴手续费
	 */
	private BigDecimal payeeCalPolicyVal;

	/**
	 * 付款方手续费值
	 */
	private BigDecimal payerCalPolicyVal;

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public BigDecimal getPayeeFee() {
		return payeeFee;
	}

	public void setPayeeFee(BigDecimal payeeFee) {
		this.payeeFee = payeeFee;
	}

	public BigDecimal getPayerFee() {
		return payerFee;
	}

	public void setPayerFee(BigDecimal payerFee) {
		this.payerFee = payerFee;
	}

	public String getFeeRole() {
		return feeRole;
	}

	public void setFeeRole(String feeRole) {
		this.feeRole = feeRole;
	}

	public String getPayeeCalPolicy() {
		return payeeCalPolicy;
	}

	public void setPayeeCalPolicy(String payeeCalPolicy) {
		this.payeeCalPolicy = payeeCalPolicy;
	}

	public String getPayerCalPolicy() {
		return payerCalPolicy;
	}

	public void setPayerCalPolicy(String payerCalPolicy) {
		this.payerCalPolicy = payerCalPolicy;
	}

	public BigDecimal getPayeeCalPolicyVal() {
		return payeeCalPolicyVal;
	}

	public void setPayeeCalPolicyVal(BigDecimal payeeCalPolicyVal) {
		this.payeeCalPolicyVal = payeeCalPolicyVal;
	}

	public BigDecimal getPayerCalPolicyVal() {
		return payerCalPolicyVal;
	}

	public void setPayerCalPolicyVal(BigDecimal payerCalPolicyVal) {
		this.payerCalPolicyVal = payerCalPolicyVal;
	}

	@Override
	public String toString() {
		return "FeeInfo [chargeType=" + chargeType + ", payeeFee=" + payeeFee + ", payerFee=" + payerFee + ", feeRole="
				+ feeRole + ", payeeCalPolicy=" + payeeCalPolicy + ", payerCalPolicy=" + payerCalPolicy
				+ ", payeeCalPolicyVal=" + payeeCalPolicyVal + ", payerCalPolicyVal=" + payerCalPolicyVal + "]";
	}
}
