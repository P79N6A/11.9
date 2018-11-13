package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;

import com.yeepay.g3.app.nccashier.wap.utils.DataUtil;
import com.yeepay.g3.facade.cwh.param.HiddenCode;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

public class CardInfoVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String cardNo;// 卡号
	
	private String owner;// 持卡人姓名
	
	private String idNo;// 证件号
	
	private String phoneNo;// 银行预留手机号
	
	private String avlidDate;// 有效期
	
	private String cvv;// cvv

	private String bankPWD; //取款密码

	public CardInfoVO(){
		
	}

	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}


	public String getIdNo() {
		return idNo;
	}


	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAvlidDate() {
		return avlidDate;
	}

	public void setAvlidDate(String avlidDate) {
		this.avlidDate = avlidDate;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getBankPWD() {
		return bankPWD;
	}

	public void setBankPWD(String bankPWD) {
		this.bankPWD = bankPWD;
	}

	public void validate() {
		if(StringUtils.isNotEmpty(getCardNo()) && !getCardNo().contains("*")
				&& !DataUtil.isBankCardNum(getCardNo())){
			throw new CashierBusinessException(Errors.INVALID_BANK_CARD_NO.getCode(), Errors.INVALID_BANK_CARD_NO.getMsg());
		}
		if (StringUtils.isNotEmpty(getOwner()) && !getOwner().contains("*") 
				&& !DataUtil.isChinese(getOwner())) {
			throw new CashierBusinessException(Errors.INVALID_NAME.getCode(), Errors.INVALID_NAME.getMsg());
		}
		if (StringUtils.isNotBlank(getPhoneNo()) && !getPhoneNo().contains("*")
				&& !DataUtil.isMobile(getPhoneNo())) {
			throw new CashierBusinessException(Errors.INVALID_PHONE.getCode(), Errors.INVALID_PHONE.getMsg());
		}
		try {
			if (StringUtils.isNotBlank(getIdNo()) && !getIdNo().contains("*")
					&& !"".equals(DataUtil.IDCardValidate(getIdNo().toLowerCase()))) {
				throw new CashierBusinessException(Errors.INVALID_IDNO.getCode(), Errors.INVALID_IDNO.getMsg());
			}
		} catch (ParseException e) {
			throw new CashierBusinessException(Errors.INVALID_IDNO.getCode(), Errors.INVALID_IDNO.getMsg());
		}
	}
	@Override
	public String toString() {
		return "CardInfoVO [cardno=" + HiddenCode.hiddenBankCardNO(cardNo) 
		+ ", owner=" + HiddenCode.hiddenName(owner) 
		+ ", idno=" + HiddenCode.hiddenIdentityCode(idNo) 
		+ ", phoneNo=" + HiddenCode.hiddenMobile(phoneNo)
		+ ", avlidDate=" + HiddenCode.hiddenAbliddate(avlidDate)
		+ ", cvv=" + HiddenCode.hiddenCvv(cvv) + "]"
		+ ", bankPWD=" + HiddenCode.HiddenBankPwd(bankPWD) + "]";
	}
	
}
