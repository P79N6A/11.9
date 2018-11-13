package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;

public class ProductLevel {
	
	private CashierVersionEnum version;
	
	private PayTool payTool;
	
	private PayTypeEnum payType;
	
	public ProductLevel(){
		
	}
	
	public ProductLevel(CashierVersionEnum version, PayTool payTool, PayTypeEnum payType){
		setVersion(version);
		setPayTool(payTool);
		setPayType(payType);
	}

	public CashierVersionEnum getVersion() {
		return version;
	}

	public void setVersion(CashierVersionEnum version) {
		this.version = version;
	}

	public PayTool getPayTool() {
		return payTool;
	}

	public void setPayTool(PayTool payTool) {
		this.payTool = payTool;
	}

	public PayTypeEnum getPayType() {
		return payType;
	}

	public void setPayType(PayTypeEnum payType) {
		this.payType = payType;
	}

	public void supplyOrderInfo(OrderDetailInfoModel orderInfo){
		orderInfo.setCashierVersion(version);
	}
	
}
