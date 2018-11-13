package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 限制的支付方式
* @Description:
* @author Minglong
* @date 2016年8月18日 下午1:27:31
 */
public enum PayLimitType {

	NO_CREDIT("非贷记卡"),
	
	NO_DEBIT("非借记卡");
	
	private final String descriptioin;
	
	PayLimitType(String descriptioin){
		this.descriptioin = descriptioin;
	}
	
	public String getDescriptioin() {
		return descriptioin;
	}
	
	public static PayLimitType getPayStatusEnum(String payLimitStr){
		PayLimitType payLimitType = null;
    	if(StringUtils.isNotBlank(payLimitStr)){
    		try{
    			payLimitType = PayLimitType.valueOf(payLimitStr);
    		}catch(Exception e){
    			return payLimitType;
    		}
    	}
    	return payLimitType;
    }
	
}
