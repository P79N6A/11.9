package com.yeepay.g3.facade.nccashier.dto;


public class IntelligentNetResultFrontCallbackResDTO extends BasicResponseDTO{

	private static final long serialVersionUID = -537966478960340964L;
	
	/**
	 * 前端回调地址（已携带参数）
	 */
	private String frontCallbackUrl;

	public IntelligentNetResultFrontCallbackResDTO(){
		
	}

	public String getFrontCallbackUrl() {
		return frontCallbackUrl;
	}

	public void setFrontCallbackUrl(String frontCallbackUrl) {
		this.frontCallbackUrl = frontCallbackUrl;
	}
	

}
