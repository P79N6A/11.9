/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

/**
 * @author zhen.tan
 * 差错退款返回结果
 *
 */
public class FaultRefundResponseDTO extends BasicResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6969534953680425970L;

	private String bizReverseNum;

	public String getBizReverseNum() {
		return bizReverseNum;
	}

	public void setBizReverseNum(String bizReverseNum) {
		this.bizReverseNum = bizReverseNum;
	}
	
	
}
