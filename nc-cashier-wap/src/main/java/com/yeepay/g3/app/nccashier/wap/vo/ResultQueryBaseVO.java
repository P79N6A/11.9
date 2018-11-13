package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

/**
 * 支付结果查询返回基类
 * @author duangduang
 * @since  2016-11-11
 */
public class ResultQueryBaseVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 查询到的支付结果状态
	 */
	protected String resultState;
	
	public ResultQueryBaseVO(){
		
	}

	public String getResultState() {
		return resultState;
	}

	public void setResultState(String resultState) {
		this.resultState = resultState;
	}
	
	

}
