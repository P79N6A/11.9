/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

/**
 * @author zhen.tan
 * 查询结果model
 *
 */
public class SimpleRecodeInfoModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2261857606985854011L;
	
	private long recordId ;
	private String payOrderNo;
	
	public SimpleRecodeInfoModel(){}
	
	public SimpleRecodeInfoModel(long recordId,String payOrderNo){
		this.recordId = recordId;
		this.payOrderNo = payOrderNo;
	}
	
	public long getRecordId() {
		return recordId;
	}
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	public String getPayOrderNo() {
		return payOrderNo;
	}
	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
	
		SimpleRecodeInfoModel other = (SimpleRecodeInfoModel) obj;
		if (recordId == other.getRecordId()) {
			return true;
		} else {
			return false;	
		}
	}

}
