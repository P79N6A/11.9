package com.yeepay.g3.core.nccashier.vo;


import com.yeepay.g3.facade.nccashier.dto.SignRelationDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * 卡信息实体
 * @author duangduang
 *
 */
public class CardInfo {
	
	/**
	 * 该卡对应的银行信息
	 */
	private BankInfo bank;

	/**
	 * 卡类型
	 */
	private CardTypeEnum cardType;

	/**
	 * 卡号
	 */
	private String cardNo;
	
	/**
	 * 卡名称（卡bin接口会返回该字段，含义未知）
	 */
	private String cardName;
	
	/**
	 * 临时卡ID
	 */
	private String tmpId;

	public BankInfo getBank() {
		return bank;
	}

	public void setBank(BankInfo bank) {
		this.bank = bank;
	}

	public CardTypeEnum getCardType() {
		return cardType;
	}

	public void setCardType(CardTypeEnum cardType) {
		this.cardType = cardType;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public String getTmpId() {
		return tmpId;
	}

	public void setTmpId(String tmpId) {
		this.tmpId = tmpId;
	}

	public void setSignRelationDTO(SignRelationDTO signRelationDTO) {
		signRelationDTO.setCardNo(HiddenCode.hiddenBankCardNO(cardNo));
		signRelationDTO.setCardType(cardType == null ? null : cardType.name());
		signRelationDTO.setBankCode(getBank().getBankCode());
		signRelationDTO.setBankName(getBank().getBankName());
		signRelationDTO.setMinLimit(getBank().getMinLimit());
	}

	public boolean bankMatch(String bankCode) {
		return bank.bankMatch(bankCode);
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder().
				append("CardInfo={").
				append("bank=").append(bank).append(",").
				append("cardType=").append(cardType).append(",").
				append("cardNo=").append(HiddenCode.hiddenBankCardNO(cardNo)).
				append("}");
		return str.toString();
	}
}
