package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.List;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * 对外签约关系实体
 * 
 * @author duangduang
 *
 */
public class SignRelationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 签约关系ID
	 */
	private String signRelationId;

	/**
	 * 签约卡类型
	 */
	private String cardType;

	/**
	 * 卡号
	 */
	private String cardNo;

	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * 银行名称
	 */
	private String bankName;

	/**
	 * 最低限额
	 */
	private String minLimit;
	
	private String remark;
	
	private String rateWay;
	
	private List<InstallmentPeriodAndRateInfoDTO> numsAndRates;

	public SignRelationDTO() {

	}

	public String getSignRelationId() {
		return signRelationId;
	}

	public void setSignRelationId(String signRelationId) {
		this.signRelationId = signRelationId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(String minLimit) {
		this.minLimit = minLimit;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public List<InstallmentPeriodAndRateInfoDTO> getNumsAndRates() {
		return numsAndRates;
	}

	public void setNumsAndRates(List<InstallmentPeriodAndRateInfoDTO> numsAndRates) {
		this.numsAndRates = numsAndRates;
	}
	
	public String getRateWay() {
		return rateWay;
	}

	public void setRateWay(String rateWay) {
		this.rateWay = rateWay;
	}

	@Override
	public String toString() {
		return "SignRelationDTO [signRelationId=" + signRelationId + ", cardType=" + cardType + ", cardNo=" + HiddenCode.hiddenBankCardNO(cardNo)
				+ ", bankCode=" + bankCode + ", bankName=" + bankName + ", minLimit=" + minLimit + ", remark=" + remark
				+ ", rateWay=" + rateWay + ", numsAndRates=" + numsAndRates + "]";
	}

}
