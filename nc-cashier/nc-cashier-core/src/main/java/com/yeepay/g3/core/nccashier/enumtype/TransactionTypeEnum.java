package com.yeepay.g3.core.nccashier.enumtype;

import com.yeepay.g3.facade.nccashier.error.Errors;

public enum TransactionTypeEnum {
	
	/**
	 * 消费
	 */
	SALE("", "",""),
	
	/**
	 * 个人会员余额支付
	 */
	MEMBER_RECHARGE_2C("GRHYCZ","GRHYCZ_ZFFS","GRHYCZ_ZFFS_STRING"),
	
	/**
	 * 预授权
	 */
	PREAUTH("", "", ""),

	/**
	 * 充值
	 */
	LOAD("", "", "");
	
	private String firstAttribute;
	
	private String secondAttribute;
	
	private String thirdAttribute;
	
	private TransactionTypeEnum(String firstAttribute, String secondAttribute, String thirdAttribute){
		this.firstAttribute = firstAttribute;
		this.secondAttribute = secondAttribute;
		this.thirdAttribute = thirdAttribute;
	}

	public String getFirstAttribute() {
		return firstAttribute;
	}

	public String getSecondAttribute() {
		return secondAttribute;
	}



	public String getThirdAttribute() {
		return thirdAttribute;
	}
	
	public static TransactionTypeEnum valueByName(String transactionTypeName){
		if(TransactionTypeEnum.MEMBER_RECHARGE_2C.name().equals(transactionTypeName)){
			return TransactionTypeEnum.MEMBER_RECHARGE_2C;
		}
		if(TransactionTypeEnum.PREAUTH.name().equals(transactionTypeName)){
			return TransactionTypeEnum.PREAUTH;
		}
		if(TransactionTypeEnum.LOAD.name().equals(transactionTypeName)){
			return TransactionTypeEnum.LOAD;
		}
		return TransactionTypeEnum.SALE;
	}
	
	public static Errors getErrors(TransactionTypeEnum transactionType){
		if(TransactionTypeEnum.PREAUTH == transactionType){
			return Errors.ORDER_TYPE_IS_PREAUTH;
		}
		return Errors.SYSTEM_EXCEPTION;
	}

}
