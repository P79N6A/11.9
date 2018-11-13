package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;
import java.io.Serializable;

/**
 * 
 * <p>冲正请求参数</p>
 * @Title: ReverseDTO.java
 * @Projectname: nc-pay-facade
 * @Package: com.yeepay.g3.facade.ncpay.dto 
 * @Copyright: Copyright (c)2015
 * @Company: YeePay
 * @author: SHJ
 * @version: 1.0
 * @Create: 2015年3月25日
 */
public class ReverseRequestDTO implements Serializable {
	private static final long serialVersionUID = -2562631086799415959L;
	/**
	 * 商户编号
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="商户编号merchantNo未传")
	private String merchantNo;
	/**
	 * 业务方
	 */
	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="业务方bizType未传")
	private Long bizType;
	/**
	 * 业务订单号
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="业务方订单号bizOrderNum未传")
	private String bizOrderNum;
	/**
	 * 业务撤销请求号
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="业务撤销请求号bizReverseNum未传")
	private String bizReverseNum;
	/**
	 * 冲正说明
	 */
	private String remark;

	/**
	 * 冲正调用的接口类型(NCPAY,FE)
	 */
	private String reverseInterfaceType;
	
	private String platformType;
	
	public String getPlatformType() {
		return platformType;
	}
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReverseInterfaceType() {
		return reverseInterfaceType;
	}

	public void setReverseInterfaceType(String reverseInterfaceType) {
		this.reverseInterfaceType = reverseInterfaceType;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer("ReverseRequestDTO{");
		sb.append(", merchantNo='").append(this.merchantNo).append('\'');
		sb.append(", bizType='").append(this.bizType).append('\'');
		sb.append(", bizOrderNum='").append(this.bizOrderNum).append('\'');
		sb.append(", bizReverseNum='").append(this.bizReverseNum).append('\'');
		sb.append(", remark='").append(this.remark).append('\'');
		sb.append('}');
		
		return sb.toString();
	}
}
