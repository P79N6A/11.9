package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.Date;

import com.yeepay.g3.facade.nccashier.enumtype.ReverseStatusEnum;

/**
 * 
 * <p>冲正返回参数</p>
 * @Title: ReverseResponseDTO.java
 * @Projectname: nc-pay-facade
 * @Package: com.yeepay.g3.facade.ncpay.dto 
 * @Copyright: Copyright (c)2015
 * @Company: YeePay
 * @author: SHJ
 * @version: 1.0
 * @Create: 2015年3月25日
 */
public class ReverseResponseDTO  implements Serializable {
	private static final long serialVersionUID = 1600167928357843180L;
	/**
	 * 业务方
	 */
	private Long bizType;
	/**
	 * 业务订单号
	 */
	private String bizOrderNum;
	/**
	 * 业务撤销请求号
	 */
	private String bizReverseNum;
	/**
	 * 业务订单创建日期
	 */
	private Date reverseCompleteDate;
	/**
	 * 充正状态
	 */
	private ReverseStatusEnum reverseStatus;
	/**
	 * 错误码
	 */
	private String errorCode;
	/**
	 * 错误信息
	 */
	private String errorMsg;
	
	public ReverseResponseDTO(){}
	
	public ReverseResponseDTO(Long bizType, String bizOrderNum,String bizReverseNum){
		this.bizType = bizType;
		this.bizOrderNum = bizOrderNum;
		this.bizReverseNum = bizReverseNum;
		reverseStatusInit();
	}
	
	public void reverseStatusInit(){
		if(this.reverseStatus == null){
			this.reverseStatus = ReverseStatusEnum.INIT;
		}else {
			throw new IllegalArgumentException("reverseStatus is not null");
		}
	}

	public void reverseStatusSuccess(){
		if(this.reverseStatus.equals(ReverseStatusEnum.INIT)){
			this.reverseStatus = ReverseStatusEnum.SUCCESS;
		}else{
			throw new IllegalArgumentException("reverseStatus is not INIT");
		}
	}
	
	public void reverseStatusFailure(){
		if(this.reverseStatus.equals(ReverseStatusEnum.INIT)){
			this.reverseStatus = ReverseStatusEnum.FAILURE;
		}else {
			throw new IllegalArgumentException("reverseStatus is not INIT");
		}
	}
	
	public Long getBizType() {
		return bizType;
	}
	public void setBizType(Long bizType) {
		this.bizType = bizType;
	}
	public String getBizOrderNum() {
		return bizOrderNum;
	}
	public void setBizOrderNum(String bizOrderNum) {
		this.bizOrderNum = bizOrderNum;
	}
	public String getBizReverseNum() {
		return bizReverseNum;
	}
	public void setBizReverseNum(String bizReverseNum) {
		this.bizReverseNum = bizReverseNum;
	}
	public Date getReverseCompleteDate() {
		return reverseCompleteDate;
	}
	public void setReverseCompleteDate(Date reverseCompleteDate) {
		this.reverseCompleteDate = reverseCompleteDate;
	}
	public ReverseStatusEnum getReverseStatus() {
		return reverseStatus;
	}
	public void setReverseStatus(ReverseStatusEnum reverseStatus) {
		this.reverseStatus = reverseStatus;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReverseResponseDTO [bizType=");
		builder.append(bizType);
		builder.append(", bizOrderNum=");
		builder.append(bizOrderNum);
		builder.append(", bizReverseNum=");
		builder.append(bizReverseNum);
		builder.append(", reverseCompleteDate=");
		builder.append(reverseCompleteDate);
		builder.append(", reverseStatus=");
		builder.append(reverseStatus);
		builder.append(", errorCode=");
		builder.append(errorCode);
		builder.append(", errorMsg=");
		builder.append(errorMsg);
		builder.append("]");
		return builder.toString();
	}
	
}
