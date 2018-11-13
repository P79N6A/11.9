package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * 签约关系列表
 * 
 * @author duangduang
 *
 */
public class SignRelationVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 签约关系ID
	 */
	private String signRelationId;

	/**
	 * 银行信息
	 */
	private BankInfoVO bank;

	/**
	 * 卡号
	 */
	private String cardNo;

	/**
	 * 卡类型
	 */
	private String cardType;

	/**
	 * 是否可用
	 */
	private String status;

	/**
	 * 用来标识不可用的原因
	 */
	private String remark;

	public SignRelationVO() {

	}

	public String getSignRelationId() {
		return signRelationId;
	}

	public void setSignRelationId(String signRelationId) {
		this.signRelationId = signRelationId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public BankInfoVO getBank() {
		return bank;
	}

	public void setBank(BankInfoVO bank) {
		this.bank = bank;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "SignRelationVO [signRelationId=" + signRelationId + ", bank=" + bank + ", cardNo="
				+ HiddenCode.hiddenBankCardNO(cardNo) + ", cardType=" + cardType + ", status=" + status + ", remark="
				+ remark + "]";
	}

}
