package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

/**
 * 页面请求参数/返回值基类
 * @author duangduang
 * @since  2016-11-03
 */
public class BaseVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 一笔请求的唯一标识 类sessionId
	 */
	protected String token;

	public BaseVO() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "BaseVO [token=" + token + "]";
	}
}
