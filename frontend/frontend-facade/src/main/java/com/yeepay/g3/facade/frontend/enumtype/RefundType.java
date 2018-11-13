package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos.
 * @createDate 16/8/4.
 */
public enum RefundType {
	/**
	 * 人工操作退款
	 */
	MANUAL,
    /**
     * 过期订单退款
     */
    TIMEOUT,
    /**
     * 重复支付退款
     */
    REPEATPAY;
    
    public static RefundType getRefundType(String refundType){
    	RefundType refundTypeEnum = null;
    	if(StringUtils.isNotBlank(refundType)){
    		try{
    			refundTypeEnum = RefundType.valueOf(refundType);
    		}catch(Exception e){
    			return refundTypeEnum;
    		}
    	}
    	return refundTypeEnum;
    }
}
