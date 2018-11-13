/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

/**
 * @author zhen.tan
 * 微信支付下单结果
 *
 */
public class WeChatPayResponseDTO extends BasicResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6969534953680425970L;

	private String tokenId;
	
	/**
	 * 微信返回下单结果
	 */
	private String result;
	

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	

}
