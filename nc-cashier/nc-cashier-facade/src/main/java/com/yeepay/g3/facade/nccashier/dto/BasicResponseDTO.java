package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;


public class BasicResponseDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 返回码
	 */
	private String returnCode;

	/**
	 * 返回信息
	 */

	private String returnMsg;

	/**
	 * 返回处理状态
	 */

	private ProcessStatusEnum processStatusEnum = ProcessStatusEnum.SUCCESS;
	
	
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public ProcessStatusEnum getProcessStatusEnum() {
		return processStatusEnum;
	}

	public void setProcessStatusEnum(ProcessStatusEnum processStatusEnum) {
		this.processStatusEnum = processStatusEnum;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("BasicResponseDTO{");
		sb.append("returnCode='").append(this.returnCode).append('\'');
		sb.append(", returnMsg='").append(this.returnMsg).append('\'');
		sb.append(", processStatusEnum='").append(this.processStatusEnum.getValue()).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
