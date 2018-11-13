package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

/**
 * ewallet/ewalletH5下，alipay/alipayH5/alipay_H5_standard开通信息
 * 
 * @author duangduang
 *
 */
public class EwalletAlipayOpenInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean openAlipay;

	private boolean openAlipayH5;

	private boolean openAlipayStandard;

	public EwalletAlipayOpenInfo() {

	}

	public boolean isOpenAlipay() {
		return openAlipay;
	}

	public void setOpenAlipay(boolean openAlipay) {
		this.openAlipay = openAlipay;
	}

	public boolean isOpenAlipayH5() {
		return openAlipayH5;
	}

	public void setOpenAlipayH5(boolean openAlipayH5) {
		this.openAlipayH5 = openAlipayH5;
	}

	public boolean isOpenAlipayStandard() {
		return openAlipayStandard;
	}

	public void setOpenAlipayStandard(boolean openAlipayStandard) {
		this.openAlipayStandard = openAlipayStandard;
	}

	public boolean doubleOpen() {
		if (openAlipay && openAlipayStandard) {
			return true;
		}

		if (openAlipayH5 && openAlipayStandard) {
			return true;
		}

		return false;
	}
}
