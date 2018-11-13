package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;
/**
 * 人
 * @author xueping.ni
 *
 */
public class Person implements Serializable{
	private static final long serialVersionUID = -9125309947695827717L;
	private String realName;
	private String idCardNo;
	private long bindId; 
	
	public Person(){
		
	}
	
	public Person(String realName, String idCardNo){
		setRealName(realName);
		setIdCardNo(idCardNo);
	}
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	/**
	 * @return the bindId
	 */
	public long getBindId() {
		return bindId;
	}
	/**
	 * @param bindId the bindId to set
	 */
	public void setBindId(long bindId) {
		this.bindId = bindId;
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer("Person{");
		sb.append("realName='").append(HiddenCode.hiddenName(realName)).append('\'');
		sb.append(", idCardNo='").append(HiddenCode.hiddenIdentityCode(idCardNo)).append('\'');
		sb.append(", bindId='").append(bindId).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
