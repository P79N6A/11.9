package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

public class EwalletH5WechatOpenInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private boolean openWechatH5;
	
	private boolean openWechatH5Low;
	
	public EwalletH5WechatOpenInfo(){
		
	}

	public boolean isOpenWechatH5() {
		return openWechatH5;
	}

	public void setOpenWechatH5(boolean openWechatH5) {
		this.openWechatH5 = openWechatH5;
	}

	public boolean isOpenWechatH5Low() {
		return openWechatH5Low;
	}

	public void setOpenWechatH5Low(boolean openWechatH5Low) {
		this.openWechatH5Low = openWechatH5Low;
	}
	
	public boolean doubleOpen(){
		if(openWechatH5 && openWechatH5Low){
			return true;
		}
		return false;
	}
	
}
