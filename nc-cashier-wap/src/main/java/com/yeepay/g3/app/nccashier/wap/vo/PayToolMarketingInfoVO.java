package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

public class PayToolMarketingInfoVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 支付类型：ALL/PART/NONE
	 */
	private String supportType;
	
	private String describ;
	
	public PayToolMarketingInfoVO(){
		
	}

	public String getSupportType() {
		return supportType;
	}

	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}

	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ;
	}

	public String toString(){
		return com.alibaba.fastjson.JSON.toJSONString(this);
	}
}
