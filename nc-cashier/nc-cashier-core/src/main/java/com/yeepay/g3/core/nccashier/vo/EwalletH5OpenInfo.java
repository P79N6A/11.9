package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

public class EwalletH5OpenInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private EwalletAlipayOpenInfo alipayOpenInfo;
	
	private EwalletH5WechatOpenInfo wechatOpenInfo;
	
	public EwalletH5OpenInfo(){
		
	}


	public EwalletAlipayOpenInfo getAlipayOpenInfo() {
		return alipayOpenInfo;
	}


	public void setAlipayOpenInfo(EwalletAlipayOpenInfo alipayOpenInfo) {
		this.alipayOpenInfo = alipayOpenInfo;
	}


	public EwalletH5WechatOpenInfo getWechatOpenInfo() {
		return wechatOpenInfo;
	}


	public void setWechatOpenInfo(EwalletH5WechatOpenInfo wechatOpenInfo) {
		this.wechatOpenInfo = wechatOpenInfo;
	}

	/**
	 * 微信H5是否双开：开通了EWALLETH5下的wechat_h5和wechat_h5_low
	 * 
	 * @return
	 */
	public boolean wxH5DoubleOpen() {
		if (wechatOpenInfo == null) {
			return false;
		}
		return wechatOpenInfo.doubleOpen();
	}
	
	public boolean alipayDoubleOpen(){
		if(alipayOpenInfo==null){
			return false;
		}
		return alipayOpenInfo.doubleOpen();
	}
	
}
