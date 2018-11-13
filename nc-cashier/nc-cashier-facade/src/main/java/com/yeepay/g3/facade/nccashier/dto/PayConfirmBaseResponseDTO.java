package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class PayConfirmBaseResponseDTO extends BasicResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tokenId;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Override
	public String toString() {
		return "PayConfirmBaseResponseDTO [tokenId=" + tokenId + ", super()=" + super.toString() + "]";
	}

}
