package com.yeepay.g3.app.nccashier.wap.vo;


/**
 * 支付结果可查标识监听VO
 * @author duangduang
 * @since  2016-11-10
 */
public class PayResultQueryStateListenVO extends ResponseVO{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 结果查询url
	 */
	private String resultQueryUrl;
	
	/**
	 * 支付结果是否可查标识
	 */
	private boolean queryState;
	
	
	public PayResultQueryStateListenVO(){
		
	}

	public String getResultQueryUrl() {
		return resultQueryUrl;
	}

	public void setResultQueryUrl(String resultQueryUrl) {
		this.resultQueryUrl = resultQueryUrl;
	}

	public boolean isQueryState() {
		return queryState;
	}

	public void setQueryState(boolean queryState) {
		this.queryState = queryState;
	}

}
