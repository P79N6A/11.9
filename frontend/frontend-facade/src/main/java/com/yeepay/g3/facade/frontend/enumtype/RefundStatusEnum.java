package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos.
 * @createDate 16/8/4.
 */
public enum  RefundStatusEnum {
    /**
     * 未退款
     */
    NONE,
    /**
     * 退款已接收
     */
    INIT,
    /**
     * 已发送到退款中心
     */
    SEND,
    /**
     * 退款成功
     */
    SUCCESS,
    /**
     * 退款中心处理失败
     */
    FAIL;
    
    
    public static RefundStatusEnum getRefundStatusEnum(String refundStatus){
    	RefundStatusEnum refundStatusEnum = null;
    	if(StringUtils.isNotBlank(refundStatus)){
    		try{
    			refundStatusEnum = RefundStatusEnum.valueOf(refundStatus);
    		}catch(Exception e){
    			return refundStatusEnum;
    		}
    	}
    	return refundStatusEnum;
    }
}
