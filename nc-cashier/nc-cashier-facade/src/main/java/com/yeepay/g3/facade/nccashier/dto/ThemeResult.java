package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * Created by xiewei on 15-10-26.
 */
public class ThemeResult implements Serializable{
	private static final long serialVersionUID = 2748337182751858414L;
	private boolean showMerchantName=true;
	private boolean showBottomInfo=true;

	public boolean isShowMerchantName() {
		return showMerchantName;
	}

	public void setShowMerchantName(boolean showMerchantName) {
		this.showMerchantName = showMerchantName;
	}

	public boolean isShowBottomInfo() {
		return showBottomInfo;
	}

	public void setShowBottomInfo(boolean showBottomInfo) {
		this.showBottomInfo = showBottomInfo;
	}
}
