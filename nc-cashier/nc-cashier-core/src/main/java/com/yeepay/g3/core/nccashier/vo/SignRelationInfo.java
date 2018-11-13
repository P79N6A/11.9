package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.core.nccashier.enumtype.InstallmentPayTypeEnum;
import com.yeepay.g3.facade.nccashier.dto.SignRelationDTO;
import com.yeepay.g3.utils.common.StringUtils;

public class SignRelationInfo {
	
	/**
	 * 签约关系ID
	 */
	private String signRelationId;

	/**
	 * 签约关系状态
	 */
	private String signRelationStatus;

	private SignCardInfo signCardInfo;
	
	/**
	 * 外部用户
	 */
	private CashierUserInfo externalUser;
	
	private String remark;

	public SignRelationInfo() {

	}

	public String getSignRelationId() {
		return signRelationId;
	}

	public void setSignRelationId(String signRelationId) {
		this.signRelationId = signRelationId;
	}

	public String getSignRelationStatus() {
		return signRelationStatus;
	}

	public void setSignRelationStatus(String signRelationStatus) {
		this.signRelationStatus = signRelationStatus;
	}

	public SignCardInfo getSignCardInfo() {
		return signCardInfo;
	}

	public void setSignCardInfo(SignCardInfo signCardInfo) {
		this.signCardInfo = signCardInfo;
	}


	public CashierUserInfo getExternalUser() {
		return externalUser;
	}

	public void setExternalUser(CashierUserInfo externalUser) {
		this.externalUser = externalUser;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @title 校验签约信息是否合法
	 * 
	 * @return
	 */
	public boolean isCardSigned() {
		if (signCardInfo == null || signCardInfo.getCardInfo() == null || signCardInfo.getCardInfo().getBank() == null
				|| StringUtils.isBlank(signCardInfo.getCardInfo().getBank().getBankCode())) {
			return false;
		}
		return true;
	}
	
	/**
	 * @title 校验签约关系是否合法
	 * @return
	 */
	public boolean isSignRelationIllegal() {
		if (StringUtils.isBlank(signRelationId)) {
			return false;
		}
		if (externalUser==null || StringUtils.isBlank(externalUser.getExternalUserId())) {
			return false;
		}
		if (!isCardSigned()) {
			return false;
		}
		return true;
	}
	
	public boolean bankMatch(String bankCode){
		return signCardInfo.bankMatch(bankCode);
	}

	public SignRelationDTO transferToSignRelationDTO() {
		SignRelationDTO signRelationDTO = new SignRelationDTO();
		signRelationDTO.setSignRelationId(signRelationId);
		if (signCardInfo != null && signCardInfo.getCardInfo() != null) {
			signCardInfo.getCardInfo().setSignRelationDTO(signRelationDTO);
		}
		return signRelationDTO;
	}
	
	public InstallmentPayTypeEnum getPayType(){
		if(StringUtils.isNotBlank(signRelationId)){
			return InstallmentPayTypeEnum.SIGN_RELATION;
		}
		if(signCardInfo!=null && StringUtils.isNotBlank(signCardInfo.getSignInfoId())){
			return InstallmentPayTypeEnum.SIGN_CARD;
		}
		return null;
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder().
				append("SignRelationInfo={").
				append("signRelationId=").append(signRelationId).append(",").
				append("externalUser=").append(externalUser).append(",").
				append("signRelationStatus=").append(signRelationStatus).append(",").
				append("signCardInfo=").append(signCardInfo).
				append("}");
		return str.toString();
	}
	
}
