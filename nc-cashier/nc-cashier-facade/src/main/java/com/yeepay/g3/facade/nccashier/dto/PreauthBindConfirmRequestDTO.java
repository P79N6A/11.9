package com.yeepay.g3.facade.nccashier.dto;

public class PreauthBindConfirmRequestDTO extends PayConfirmBaseRequestDTO {

	private static final long serialVersionUID = 1L;

	private CardInfoDTO cardInfoDTO;

	public CardInfoDTO getCardInfoDTO() {
		return cardInfoDTO;
	}

	public void setCardInfoDTO(CardInfoDTO cardInfoDTO) {
		this.cardInfoDTO = cardInfoDTO;
	}

	@Override
	public String toString() {
		return "PreauthBindConfirmRequestDTO [needBankCardDTO=" + cardInfoDTO + ", super()=" + super.toString() + "]";
	}
}
