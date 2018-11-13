package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 支付业务类型
 * @author wangmeimei
 *
 */
public enum PayBusinessType {
	
	/**
	 * 仅借
	 */
    OD,
	
    /**
     * 仅贷
     */
	OC,
	
	/**
	 * 借贷
	 */
    DC,
	
    /**
     * 大额
     */
	DE,
	
	/**
	 * 公益
	 */
	GY;

	public static PayBusinessType getPayBusinessType(String payBusinessTypeStr){
		PayBusinessType payBusinessType = null;
    	if(StringUtils.isNotBlank(payBusinessTypeStr)){
    		try{
    			payBusinessType = PayBusinessType.valueOf(payBusinessTypeStr);
    		}catch(Exception e){
    			return payBusinessType;
    		}
    	}
    	return payBusinessType;
    }
}
