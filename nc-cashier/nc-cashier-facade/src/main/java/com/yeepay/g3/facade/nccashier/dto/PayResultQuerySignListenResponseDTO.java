package com.yeepay.g3.facade.nccashier.dto;

/**
 * 支付处理器是否可查标识监听处理返参
 * @author duangduang
 * @since  2016-11-10
 *
 */
public class PayResultQuerySignListenResponseDTO extends BasicResponseDTO{

	private static final long serialVersionUID = 1L;
	
	private boolean queryState = false;
	
	private String token;
	
	public PayResultQuerySignListenResponseDTO(){
		
	}

	public boolean isQueryState() {
		return queryState;
	}

	public void setQueryState(boolean queryState) {
		this.queryState = queryState;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	

}
