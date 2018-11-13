package com.yeepay.g3.core.nccashier.entity;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;

import java.io.Serializable;
import java.util.Date;

public class RealPersonChooseTimes implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5482835414006445815L;
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
	 * 状态 VALIDE/INVALIDE
	 */
	private String status;
	/**
	 * 展示次数
	 */
	private int showCount;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 最近一笔交易订单号
	 */
	private long payRequestId;
	
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
	public int getShowCount() {
		return showCount;
	}
	public void setShowCount(int showCount) {
		this.showCount = showCount;
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
	
	/**
	 * @return the payRequestId
	 */
	public long getPayRequestId() {
		return payRequestId;
	}
	/**
	 * @param payRequestId the payRequestId to set
	 */
	public void setPayRequestId(long payRequestId) {
		this.payRequestId = payRequestId;
	}
	@Override
	public String toString(){
		final StringBuffer sb = new StringBuffer("RealPersonChooseTimes{");
		sb.append("id=").append(id);
		sb.append(", merchantNo=").append(merchantNo);
		sb.append(", identityId=").append(HiddenCode.hiddenIdentityId(identityId));
		sb.append(", identityType=").append(identityType);
		sb.append(", status=").append(status);
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append("}");
		
		return sb.toString();
	}
	

}
