package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.facade.fee.front.enumtype.CalFeeCalItemTypeEnum;
import com.yeepay.g3.facade.fee.front.enumtype.CalFeeChargeTypeEnum;
import com.yeepay.g3.facade.fee.front.enumtype.CalFeeOwnerSourceTypeEnum;
import com.yeepay.g3.facade.fee.front.enumtype.CalFeeRoleTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;

public class FeeParam implements Serializable{

	private static final long serialVersionUID = 1L;

	private String merchantNo;
	
	private String calFeeItem;
	
	private String payProduct;
	
	private String payWay;
	
	private String version;
	
	private String belongSys;
	
	private String feeRole;
	
	private String bankInterNumber;
	
	public FeeParam(){
		
	}
	
	public FeeParam(String calFeeItem, String payProduct) {
		setCalFeeItem(calFeeItem);
		setPayProduct(payProduct);
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getCalFeeItem() {
		return calFeeItem;
	}

	public void setCalFeeItem(String calFeeItem) {
		this.calFeeItem = calFeeItem;
	}

	public String getPayProduct() {
		return payProduct;
	}

	public void setPayProduct(String payProduct) {
		this.payProduct = payProduct;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBelongSys() {
		return belongSys;
	}

	public void setBelongSys(String belongSys) {
		this.belongSys = belongSys;
	}
	
	
	public String getFeeRole() {
		return feeRole;
	}

	public void setFeeRole(String feeRole) {
		this.feeRole = feeRole;
	}
	

	public String getBankInterNumber() {
		return bankInterNumber;
	}

	public void setBankInterNumber(String bankInterNumber) {
		this.bankInterNumber = bankInterNumber;
	}

	public boolean compare(String calFeeItemToCompare, String payProductToCompare, String payWayToCompare, String versionToCompare){
		if(StringUtils.isNotBlank(calFeeItem) && !calFeeItem.equals(calFeeItemToCompare)){
			return false;
		}
		if(StringUtils.isNotBlank(payProduct) && !payProduct.equals(payProductToCompare)){
			return false;
		}
		if(StringUtils.isNotBlank(payWay) && !payWay.equals(payWayToCompare)){
			return false;
		}
		if(StringUtils.isNotBlank(version) && !version.equals(versionToCompare)){
			return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
