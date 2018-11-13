package com.yeepay.g3.facade.nccashier.dto;

/**
 * 银行卡分期卡号下单返回实体
 * 
 * @author duangduang
 *
 */
public class CardNoOrderResponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 银行卡分期开通并支付返回的url信息
	 */
	private UrlInfoDTO urlInfo;

	public CardNoOrderResponseDTO() {

	}

	public UrlInfoDTO getUrlInfo() {
		return urlInfo;
	}

	public void setUrlInfo(UrlInfoDTO urlInfo) {
		this.urlInfo = urlInfo;
	}

	@Override
	public String toString() {
		return "CardNoOrderResponseDTO [urlInfo=" + urlInfo + ", returnCode=" + getReturnCode() + ", returnMsg="
				+ getReturnMsg() + ", processStatusEnum=" + getProcessStatusEnum() + "]";
	}

}
