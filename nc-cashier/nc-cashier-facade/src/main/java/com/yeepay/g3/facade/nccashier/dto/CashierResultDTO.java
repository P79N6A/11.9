package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class CashierResultDTO  implements Serializable {

	private static final long serialVersionUID = 8571329956097329994L;

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
	 * 收银台地址
	 */
	private String cashierCallURL;

	/**
	 * 支付系统编码 当走支付处理器时有值
	 */
	private String paySysCode;

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

	public String getCashierCallURL() {
		return cashierCallURL;
	}

	public String getPaySysCode() {
		return paySysCode;
	}

	public void setPaySysCode(String paySysCode) {
		this.paySysCode = paySysCode;
	}

	public void setCashierCallURL(String cashierCallURL) {
		this.cashierCallURL = cashierCallURL;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierResultDTO{");
		sb.append("tradeSysNo='").append(tradeSysNo).append('\'');
		sb.append("tradeSysOrderId='").append(tradeSysOrderId).append('\'');
		sb.append("orderSysNo='").append(orderSysNo).append('\'');
		sb.append("orderOrderId='").append(orderOrderId).append('\'');
		sb.append("cashierCallURL='").append(cashierCallURL).append('\'');
		sb.append("paySysCode='").append(paySysCode).append('\'');
		sb.append("," + super.toString());
		sb.append('}');
		return sb.toString();
		
	}
}
