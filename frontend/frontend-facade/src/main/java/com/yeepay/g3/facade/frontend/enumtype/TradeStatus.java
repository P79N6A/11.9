package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

public enum TradeStatus {
	/**
	 * 支付成功 
	 */
	SUCCESS,
	/**
	 * 转入退款 
	 */
	REFUND,
	
	/**
	 * 已关闭
	 */
	CLOSED,
	
	/**
	 * 已撤销 
	 */
	REVOKED,
	
	/**
	 * 用户支付中 
	 */
	USERPAYING,
	
	/**
	 * 未支付(输入密码或确认支付超时)
	 */
	NOPAY,
	
	/**
	 * 支付失败(其他原因,如银行返回失败)
	 */
	PAYERROR;
	
	public static TradeStatus getTradeStatus(String tradeStatus){
		TradeStatus tradeStatusEnum = null;
    	if(StringUtils.isNotBlank(tradeStatus)){
    		try{
    			tradeStatusEnum = TradeStatus.valueOf(tradeStatus);
    		}catch(Exception e){
    			return tradeStatusEnum;
    		}
    	}
    	return tradeStatusEnum;
    }

}
