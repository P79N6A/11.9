package com.yeepay.g3.facade.nccashier.dto;


import java.io.Serializable;
import java.util.List;

import com.yeepay.g3.facade.nccashier.enumtype.BusinessTypeEnum;

/**
 * 卡信息
 */
public class BankCardReponseDTO extends BasicResponseDTO implements Serializable {
	
	private static final long serialVersionUID = -6827917981712558732L;
	
	/**
	 * 支付请求ID
	 */
	private long requestId;
	
	/**
	 * 业务类型
	 */
	private BusinessTypeEnum  businessTypeEnum;
	
	/**
	 * 绑卡信息
	 */
	private  BankCardDTO bankCardDTO ;
	
	/**
	 * 不可用的绑卡信息
	 */
	private List<BankCardDTO> unuseBankCardDTO;
	
	/**
	 * 透传卡信息
	 */
	private PassCardInfoDTO passCardInfoDTO;
	
	/**
	 * 是否显示换其他卡支付
	 */
	private boolean showChangeCard =true;
	
	/**
	 * 是否授权
	 */
	private boolean authorized;
	
	/**
	 * 自动绑卡提示
	 * modify 为某些商户的用户定制的
	 */
	private String autoBindTipText;
	
	
	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public BusinessTypeEnum getBusinessTypeEnum() {
		return businessTypeEnum;
	}

	public void setBusinessTypeEnum(BusinessTypeEnum businessTypeEnum) {
		this.businessTypeEnum = businessTypeEnum;
	}

	public BankCardDTO getBankCardDTO() {
		return bankCardDTO;
	}

	public void setBankCardDTO(BankCardDTO bankCardDTO) {
		this.bankCardDTO = bankCardDTO;
	}

	public PassCardInfoDTO getPassCardInfoDTO() {
		return passCardInfoDTO;
	}

	public void setPassCardInfoDTO(PassCardInfoDTO passCardInfoDTO) {
		this.passCardInfoDTO = passCardInfoDTO;
	}

	

	/**
	 * @return the showChangeCard
	 */
	public boolean getShowChangeCard() {
		return showChangeCard;
	}

	/**
	 * @param showChangeCard the showChangeCard to set
	 */
	public void setShowChangeCard(boolean showChangeCard) {
		this.showChangeCard = showChangeCard;
	}
	
	public List<BankCardDTO> getUnuseBankCardDTO() {
		return unuseBankCardDTO;
	}

	public void setUnuseBankCardDTO(List<BankCardDTO> unuseBankCardDTO) {
		this.unuseBankCardDTO = unuseBankCardDTO;
	}

	public boolean isAuthorized() {
		return authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
	
	public String getAutoBindTipText() {
		return autoBindTipText;
	}

	public void setAutoBindTipText(String autoBindTipText) {
		this.autoBindTipText = autoBindTipText;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("BankCardReponseDTO{");
		sb.append("requestId='").append(requestId).append('\'');
		sb.append(", businessTypeEnum='").append(businessTypeEnum).append('\'');
		sb.append(", showChangeCard='").append(showChangeCard).append('\'');
		sb.append(", passCardInfoDTO='").append(passCardInfoDTO).append('\'');
			sb.append(", bankCardDTO='").append(bankCardDTO).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
