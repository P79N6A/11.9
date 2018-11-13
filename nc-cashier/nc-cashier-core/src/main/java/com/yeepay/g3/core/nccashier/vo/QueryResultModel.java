/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.facade.nccashier.enumtype.TradeStateEnum;

/**
 * @author zhen.tan
 * 查询结果model
 *
 */
public class QueryResultModel {
	
	private TradeStateEnum  state;
	private String errorCode;
	private String errorMsg;
	private String paySystem;
	public TradeStateEnum getState() {
		return state;
	}
	public void setState(TradeStateEnum state) {
		this.state = state;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getPaySystem() {
		return paySystem;
	}
	public void setPaySystem(String paySystem) {
		this.paySystem = paySystem;
	}
	
}
