package com.yeepay.g3.core.nccashier.vo;

import java.util.Map;

public class PersonalMemberBanlancePayRequestInfo {
	
	private Map<String, String> extParam;
	
	private String userIp;
	
	private String memberNo;

	private String payMerchantNo;
	public PersonalMemberBanlancePayRequestInfo(){
		
	}

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getPayMerchantNo() {
		return payMerchantNo;
	}

	public void setPayMerchantNo(String payMerchantNo) {
		this.payMerchantNo = payMerchantNo;
	}

	@Override
	public String toString() {
		return "PersonalMemberBanlancePayRequestInfo [extParam=" + extParam 
				+ ", userIp=" + userIp 
				+ ", memberNo=" + memberNo
				+ ", payMerchantNo=" + payMerchantNo
				+ "]";
	}
	
	
	
}
