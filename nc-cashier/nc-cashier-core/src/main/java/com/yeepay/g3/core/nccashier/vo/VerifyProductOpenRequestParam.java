package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;

public class VerifyProductOpenRequestParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String merchantNo;
	
	private ProductLevel productLevel;
	
	private TransactionTypeEnum transactionType;
	
	public VerifyProductOpenRequestParam(){
		
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public ProductLevel getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(ProductLevel productLevel) {
		this.productLevel = productLevel;
	}

	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}
	
}
