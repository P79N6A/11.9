package com.yeepay.g3.core.nccashier.vo;


/**
 * 在卡账户存储的签约卡信息
 * @author duangduang
 *
 */
public class SignCardInfo {

	/**
	 * 银行卡签约信息ID
	 */
	private String signInfoId;
	
	/**
	 * 签约方
	 */
	private String signOrganization;

	/**
	 * 签约信息状态
	 */
	private String signInfoStatus;
	
	/**
	 * 手机号
	 */
	private String phoneNo;
	
	/**
	 * 卡信息
	 */
	private CardInfo cardInfo;

	public SignCardInfo(){
		
	}

	public String getSignInfoId() {
		return signInfoId;
	}

	public void setSignInfoId(String signInfoId) {
		this.signInfoId = signInfoId;
	}

	public String getSignOrganization() {
		return signOrganization;
	}

	public void setSignOrganization(String signOrganization) {
		this.signOrganization = signOrganization;
	}

	public String getSignInfoStatus() {
		return signInfoStatus;
	}

	public void setSignInfoStatus(String signInfoStatus) {
		this.signInfoStatus = signInfoStatus;
	}

	public CardInfo getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(CardInfo cardInfo) {
		this.cardInfo = cardInfo;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public boolean bankMatch(String bankCode){
		return cardInfo.bankMatch(bankCode);
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder().
				append("SignCardInfo={").
				append("signInfoId=").append(signInfoId).append(",").
				append("signOrganization=").append(signOrganization).append(",").
				append("signInfoStatus=").append(signInfoStatus).append(",").
				append("phoneNo=").append(phoneNo).append(",").
				append("cardInfo=").append(cardInfo).append(",").
				append("}");
		return str.toString();
	}
	
}
