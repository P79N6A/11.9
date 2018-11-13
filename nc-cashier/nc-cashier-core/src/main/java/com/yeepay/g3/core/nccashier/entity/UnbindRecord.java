package com.yeepay.g3.core.nccashier.entity;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;

import java.io.Serializable;
import java.util.Date;

public class UnbindRecord  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -493979861749896321L;
	/**
	 * 主键
	 */
	private long id;
	/**
	 * 商户编号
	 */
	private String merchantNo;
	/**
	 * 用户ID
	 */
	private String identityId;
	/**
	 * 用户类型
	 */
	private String identityType;
	/**
	 * 外部用户ID
	 */
	private String mermberNo;
	/**
	 * 状态 SUCCESS/FAILED/INIT
	 */
	private String status;
	public long getBindId() {
		return bindId;
	}
	public void setBindId(long bindId) {
		this.bindId = bindId;
	}
	/**
	 * 绑卡ID
	 */
	private long bindId;
	/**
	 * 失败错误码
	 */
	private String errorCode;
	/**
	 * 失败原因
	 */
	private String errorMsg;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 解绑原因
	 */
	private String cause;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getMermberNo() {
		return mermberNo;
	}
	public void setMermberNo(String mermberNo) {
		this.mermberNo = mermberNo;
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
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	@Override
	public String toString(){
		final StringBuffer sb = new StringBuffer("UnbindRecord{");
		sb.append("id=").append(id);
		sb.append(", merchantNo=").append(merchantNo);
		sb.append(", identityId=").append(HiddenCode.hiddenIdentityId(identityId));
		sb.append(", identityType=").append(identityType);
		sb.append(", status=").append(status);
		sb.append(", bindId=").append(bindId);
		sb.append(", errorCode=").append(errorCode);
		sb.append(", errorMsg=").append(errorMsg);
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", cause=").append(cause);
		sb.append("}");
		
		return sb.toString();
	}

}
