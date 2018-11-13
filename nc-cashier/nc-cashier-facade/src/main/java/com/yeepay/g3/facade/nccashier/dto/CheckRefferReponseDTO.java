package com.yeepay.g3.facade.nccashier.dto;


import java.io.Serializable;

/**
 *  分控reffer校验返回
 */
public class CheckRefferReponseDTO extends BasicResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1157503021843597775L;
	
	/**
	 * 分控返回码
	 * SAFE("0000", "建议放行"), 
	 * DUBIOUS_TRADE("9001", "可疑交易"), 
	 * RISK_TRADE_INGRAY("9002", "交易风险，灰名单监控中"), 
	 * RISK_TRADE_REFER("9003", "交易风险，与入网报备地址不一致"), 
	 * RISK_TRADE_BLACK("9004", "交易风险，黑名单拦截");
	 */
	private String resultCode;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CheckRefferReponseDTO{");
		sb.append("resultCode='").append(this.resultCode).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}

}
