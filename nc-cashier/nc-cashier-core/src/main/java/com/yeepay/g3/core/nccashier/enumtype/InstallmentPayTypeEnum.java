package com.yeepay.g3.core.nccashier.enumtype;

public enum InstallmentPayTypeEnum {
	
	FIRST("首次未签约"),
	
	SIGN_CARD("签约卡"),
	
	SIGN_RELATION("签约关系");
	
	private String description;
	
	private InstallmentPayTypeEnum(String description){
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
