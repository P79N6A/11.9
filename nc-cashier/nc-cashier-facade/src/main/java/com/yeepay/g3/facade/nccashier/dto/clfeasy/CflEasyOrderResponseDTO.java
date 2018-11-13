package com.yeepay.g3.facade.nccashier.dto.clfeasy;

import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardBinResDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;

public class CflEasyOrderResponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 1L;

	private CardBinResDTO cardBin;

	/**
	 * 所需补充项
	 */
	private NeedSurportDTO needItem;
	
	private String smsType;

	public CflEasyOrderResponseDTO() {

	}

	public CardBinResDTO getCardBin() {
		return cardBin;
	}

	public void setCardBin(CardBinResDTO cardBin) {
		this.cardBin = cardBin;
	}

	public NeedSurportDTO getNeedItem() {
		return needItem;
	}

	public void setNeedItem(NeedSurportDTO needItem) {
		this.needItem = needItem;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	
	

}
