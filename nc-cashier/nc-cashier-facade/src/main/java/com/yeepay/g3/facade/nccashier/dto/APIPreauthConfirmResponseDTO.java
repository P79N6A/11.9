package com.yeepay.g3.facade.nccashier.dto;

/**
 * 预授权确认支付
 *
 */
public class APIPreauthConfirmResponseDTO extends APIBasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
     * 绑卡ID
	 */
	private String bindId;

	public APIPreauthConfirmResponseDTO() {

	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	@Override
	public String toString() {
		return "APIPreauthCancelResponseDTO [code=" + getCode() 
				+ ", message=" + getMessage() 
				+ ", merchantNo=" + getMerchantNo()
				+ ", token=" + getToken()
				+ ", bindId=" + bindId
				+ "]";
	}
}
