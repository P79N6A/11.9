package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 支付订单状态
 */
public enum PayStatusEnum {
	
	INIT("未支付"), 
	
	SUCCESS("支付成功"), 
	
	FAILURE("失败");
	
	private final String descriptioin;

	PayStatusEnum(String description) {
		this.descriptioin = description;
	}

	public String getDescriptioin() {
		return descriptioin;
	}
	
	
	public static PayStatusEnum getPayStatusEnum(String payStatusStr){
		PayStatusEnum payStatusEnum = null;
    	if(StringUtils.isNotBlank(payStatusStr)){
    		try{
    			payStatusEnum = PayStatusEnum.valueOf(payStatusStr);
    		}catch(Exception e){
    			return payStatusEnum;
    		}
    	}
    	return payStatusEnum;
    }
	
	public static PayStatusEnum getPayStatusEnum(TradeStatus tradeStatus){
    	if(TradeStatus.SUCCESS.equals(tradeStatus)
    			|| TradeStatus.REFUND.equals(tradeStatus)){
    		return PayStatusEnum.SUCCESS;
    	}
    	if(TradeStatus.NOPAY.equals(tradeStatus)
    			|| TradeStatus.USERPAYING.equals(tradeStatus)){
    		return PayStatusEnum.INIT;
    	}
    	if(TradeStatus.CLOSED.equals(tradeStatus)
    			|| TradeStatus.PAYERROR.equals(tradeStatus)
    			|| TradeStatus.REVOKED.equals(tradeStatus)){
    		return PayStatusEnum.FAILURE;
    	}
    	return null;
    }
	
	public static PayStatusEnum getPayStatusEnumFromBank(String bankOrderStatus){
		if(bankOrderStatus.equals("PAY")){
    		return PayStatusEnum.SUCCESS;
    	}
    	if(bankOrderStatus.equals("PROCESSING")
    			|| bankOrderStatus.equals("INIT")){
    		return PayStatusEnum.INIT;
    	}
    	return null;
	}
}
