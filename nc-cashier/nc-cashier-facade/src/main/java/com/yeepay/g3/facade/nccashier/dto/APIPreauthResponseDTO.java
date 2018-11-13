package com.yeepay.g3.facade.nccashier.dto;

/**
 * 预授权完成、撤销、完成撤销三个接口的返回实体
 * 
 * @author duangduang
 *
 */
public class APIPreauthResponseDTO extends APIBasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 商编
	 */
	private String merchantNo;

	/**
	 * 订单token
	 */
	private String token;

	/**
	 * 支付处理器的支付子订单号，查询接口不返回该字段
	 */
	private String payOrderId;

	/**
	 * payOrderId子弹的状态（支付结果）
	 */
	private String status;

	public APIPreauthResponseDTO() {

	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "APIPreauthCancelResponseDTO [code=" + getCode() 
				+ ", message=" + getMessage() 
				+ ", merchantNo=" + merchantNo 
				+ ", token=" + token 
				+ ", payOrderId=" + payOrderId 
				+ ", status=" + status 
				+ "]";
	}
}
