/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;

/**
 * @author zhen.tan
 * 新下单返回结果
 */
public class NewOrderRequestResponseDTO extends BasicResponseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1270816078270755916L;

	private String orderNo;
	
	private String merchantAccountCode;
	
	private CashierVersionEnum version;
	
	private String encodeRequestId;
	
	private long requestId;

	private String uniqueOrderNo;

	public String getMerchantAccountCode() {
		return merchantAccountCode;
	}

	public void setMerchantAccountCode(String merchantAccountCode) {
		this.merchantAccountCode = merchantAccountCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public CashierVersionEnum getVersion() {
		return version;
	}

	public void setVersion(CashierVersionEnum version) {
		this.version = version;
	}

	public String getEncodeRequestId() {
		return encodeRequestId;
	}

	public void setEncodeRequestId(String encodeRequestId) {
		this.encodeRequestId = encodeRequestId;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String getUniqueOrderNo() {
		return uniqueOrderNo;
	}

	public void setUniqueOrderNo(String uniqueOrderNo) {
		this.uniqueOrderNo = uniqueOrderNo;
	}

	@Override
	public String toString() {
		return super.toString();
	}


}
