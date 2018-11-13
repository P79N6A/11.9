/**
 * 
 */
package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 支付类型、银行卡类型
 * @author TML
 */
public enum PayBankcardType {

	CFT("零钱"),
	
	DEBIT("借记卡"),
	
	CREDIT("贷记卡");
	
	private final String descriptioin;

	
	public static PayBankcardType getPayBankcardType(String payBankcardTypeStr){
		PayBankcardType payBankcardType = null;
    	if(StringUtils.isNotBlank(payBankcardTypeStr)){
    		try{
    			payBankcardType = PayBankcardType.valueOf(payBankcardTypeStr);
    		}catch(Exception e){
    			return payBankcardType;
    		}
    	}
    	return payBankcardType;
    }
	
	
	PayBankcardType(String description) {
		this.descriptioin = description;
	}

	public String getDescriptioin() {
		return descriptioin;
	}
}
