package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;

public class MerchantConfigRequestParam implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String merchantNo;
	
	private TransactionTypeEnum transactionType;
	
	public MerchantConfigRequestParam(){
		
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}
	
}
