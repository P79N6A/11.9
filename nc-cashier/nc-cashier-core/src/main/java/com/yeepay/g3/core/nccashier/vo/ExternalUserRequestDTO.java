/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * 外部用户请求DTO
 * @author zhen.tan
 * @since：2016年5月24日 下午6:26:38 
 */
public class ExternalUserRequestDTO {

	private String merchantAccount;
	private String identityId;
	private IdentityType identityType;
	private String userName;
	private String userIdentityNo;
	public String getMerchantAccount() {
		return merchantAccount;
	}
	public void setMerchantAccount(String merchantAccount) {
		this.merchantAccount = merchantAccount;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public IdentityType getIdentityType() {
		return identityType;
	}
	public void setIdentityType(IdentityType identityType) {
		this.identityType = identityType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserIdentityNo() {
		return userIdentityNo;
	}
	public void setUserIdentityNo(String userIdentityNo) {
		this.userIdentityNo = userIdentityNo;
	}

	public ExternalUserRequestDTO() {
	}
	public ExternalUserRequestDTO(PaymentRequest payRequest) {
		this.merchantAccount = payRequest.getMerchantNo();
		this.identityId = payRequest.getIdentityId();
		this.identityType = IdentityType.valueOf(payRequest.getIdentityType());
	}

	@Override
	public String toString() {
		return "ExternalUserRequestDTO{" +
				"merchantAccount='" + merchantAccount + '\'' +
				", identityId='" + HiddenCode.hiddenIdentityId(identityId) + '\'' +
				", identityType=" + identityType +
				", userName='" + userName + '\'' +
				", userIdentityNo='" + userIdentityNo + '\'' +
				'}';
	}
}
