package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class CashierSmsSendResponseDTO extends BasicResponseDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1921817676241659990L;

	private String tokenId;


	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierSmsSendResponseDTO{");
		sb.append("tokenId='").append(this.tokenId).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
	

}
