package com.yeepay.g3.app.nccashier.wap.vo;

import java.math.BigDecimal;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.json.JSONUtils;

/**
 * 某银行某期数对应的手续费、首期还款、每期还款、补贴后的手续费等相关金额信息
 * 
 * @author duangduang
 *
 */
public class InstallmentAmountInfoVO extends BasicResponseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * 期数
	 */
	private String period;

	/**
	 * 订单金额
	 */
	private String orderAmount;

	/**
	 * 手续费
	 */
	private String fee;

	/**
	 * 补贴后的手续费
	 */
	private String feeAfterSubsidy;

	/**
	 * 首期还款额
	 */
	private String firstPayment;

	/**
	 * 每期还款额
	 */
	private String terminalPayment;
	
	private String repayurl;
	
	public InstallmentAmountInfoVO() {

	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public String getRepayurl() {
		return repayurl;
	}

	public void setRepayurl(String repayurl) {
		this.repayurl = repayurl;
	}
	
	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getFeeAfterSubsidy() {
		return feeAfterSubsidy;
	}

	public void setFeeAfterSubsidy(String feeAfterSubsidy) {
		this.feeAfterSubsidy = feeAfterSubsidy;
	}

	public String getFirstPayment() {
		return firstPayment;
	}

	public void setFirstPayment(String firstPayment) {
		this.firstPayment = firstPayment;
	}

	public String getTerminalPayment() {
		return terminalPayment;
	}

	public void setTerminalPayment(String terminalPayment) {
		this.terminalPayment = terminalPayment;
	}

	@Override
	public String toString() {
		return "InstallmentAmountInfoVO [bankCode=" + bankCode + ", period=" + period + ", orderAmount=" + orderAmount
				+ ", fee=" + fee + ", feeAfterSubsidy=" + feeAfterSubsidy + ", firstPayment=" + firstPayment
				+ ", terminalPayment=" + terminalPayment + ", " + super.toString() + "]";
	}
	
	public static void main(String[] args){
//		DecimalFormat df = new DecimalFormat("#.##");
//		System.out.println(Double.ParseDouble(df.format(result_value)));
		InstallmentAmountInfoVO resVO = new InstallmentAmountInfoVO();
		java.text.DecimalFormat myformat=new java.text.DecimalFormat("#.00");
		resVO.setFee(myformat.format(new BigDecimal(4.19)));
//		new BigDecimal(4.19).setScale(2);
//		resVO.setFeeAfterSubsidy(+"");
		resVO.setBizStatus("failed");
		resVO.setErrorcode(Errors.SYSTEM_EXCEPTION.getCode());
		resVO.setErrormsg(Errors.SYSTEM_EXCEPTION.getMsg());
		resVO.setToken("12345666666");
		System.out.println(JSONUtils.toJsonString(resVO));
	}
}
