package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.payprocessor.dto.BankCardInfoDTO;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * Created by xiewei on 15-10-16.
 */
public class CardInfoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1876490309001633248L;
	private String name;
	private String phone;
	private String idno;
	private String cardno;
	private String cvv2;
	private String valid;
	private String pass;
	private String bankCode;
	private String bankName;
	private String cardType;
	private String idType;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CardInfoDTO{");
		sb.append("name='").append(HiddenCode.hiddenName(name)).append('\'');
		sb.append(", idno='").append(HiddenCode.hiddenIdentityCode(idno)).append('\'');
		sb.append(", cardno='").append(HiddenCode.hiddenBankCardNO(cardno)).append('\'');
		sb.append(", bankCode='").append(bankCode).append('\'');
		sb.append(", bankName='").append(bankName).append('\'');
		sb.append(", cardType='").append(cardType).append('\'');
		sb.append(", idType='").append(idType).append('\'');
		sb.append(", phone='").append(HiddenCode.hiddenMobile(phone)).append('\'');
		sb.append('}');
		return sb.toString();
	}


	public com.yeepay.g3.facade.ncpay.dto.CardInfoDTO transferNcPayCardInfoDTO(){
		com.yeepay.g3.facade.ncpay.dto.CardInfoDTO cardInfoDTO = new com.yeepay.g3.facade.ncpay.dto.CardInfoDTO();
		cardInfoDTO.setCardType(this.getCardType());
		cardInfoDTO.setIdCard(this.getIdno());
		cardInfoDTO.setIdCardType(this.getIdType());
		cardInfoDTO.setOwner(this.getName());
		cardInfoDTO.setBankCode(this.getBankCode());
		cardInfoDTO.setBankName(this.getBankName());
		cardInfoDTO.setBankMobile(this.getPhone());
		cardInfoDTO.setCardNo(this.getCardno());
		cardInfoDTO.setCvv2(this.getCvv2());
		cardInfoDTO.setPin(this.getPass());
		cardInfoDTO.setExpireDate(this.getValid());

		return cardInfoDTO;
	}

	public BankCardInfoDTO transferPayProcessBankCardInfoDTO(){
		BankCardInfoDTO bankCardInfoDTO = new BankCardInfoDTO();
		bankCardInfoDTO.setCardType(this.getCardType());
		bankCardInfoDTO.setIdCard(this.getIdno());
		bankCardInfoDTO.setIdCardType(this.getIdType());
		bankCardInfoDTO.setOwner(this.getName());
		bankCardInfoDTO.setBankCode(this.getBankCode());
		bankCardInfoDTO.setBankName(this.getBankName());
		bankCardInfoDTO.setBankMobile(this.getPhone());
		bankCardInfoDTO.setCardNo(this.getCardno());
		bankCardInfoDTO.setCvv2(this.getCvv2());
		bankCardInfoDTO.setPin(this.getPass());
		bankCardInfoDTO.setExpireDate(this.getValid());

		return bankCardInfoDTO;
	}
	
	public void basicValidate() {
		if (StringUtils.isBlank(getBankCode())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "bankCode未传");
		}
		if (StringUtils.isBlank(getCardno())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "cardno未传");
		}
	}
	
	public void cardItemIsFull(){
		if(CardTypeEnum.CREDIT.name().equals(getCardType())){
			fiveItemIsFull();
		}else if(CardTypeEnum.DEBIT.name().equals(getCardType())){
			threeItemIsFull();
		}
	}
	
	private void threeItemIsFull(){
		if (StringUtils.isBlank(getName())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "name未传");
		}
		if (StringUtils.isBlank(getIdno())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "idno未传");
		}
		if (StringUtils.isBlank(getPhone())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "idno未传");
		}
	}
	
	private void fiveItemIsFull(){
		threeItemIsFull();
		if (StringUtils.isBlank(getCvv2())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "cv2未传");
		}
		if (StringUtils.isBlank(getValid())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "validDate未传");
		}
	}
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}
}
