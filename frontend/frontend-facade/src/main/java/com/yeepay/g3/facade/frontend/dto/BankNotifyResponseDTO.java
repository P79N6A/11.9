package com.yeepay.g3.facade.frontend.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.frontend.enumtype.NotifyStatusEnum;

/**
 * 回到通知结果
 * @author TML
 *
 */
public class BankNotifyResponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = -2327972817827412406L;
	
	/**
	 * 回调结果
	 */
	private NotifyStatusEnum notifyStatus;
	
	/**
	 * 网银支付返回银行子系统页面回调
	 */
	private String pageCallBack;

	public NotifyStatusEnum getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(NotifyStatusEnum notifyStatus) {
		this.notifyStatus = notifyStatus;
	}


	public String getPageCallBack() {
		return pageCallBack;
	}

	public void setPageCallBack(String pageCallBack) {
		this.pageCallBack = pageCallBack;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);				
	}
	
	
	
}
