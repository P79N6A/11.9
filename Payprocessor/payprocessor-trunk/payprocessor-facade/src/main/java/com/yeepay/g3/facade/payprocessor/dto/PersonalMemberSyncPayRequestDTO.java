package com.yeepay.g3.facade.payprocessor.dto;

import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

public class PersonalMemberSyncPayRequestDTO extends BasicRequestDTO {

    private static final long serialVersionUID = 4532300341351324583L;

    /**
     * 会员编号
     */
    @NotEmpty
    private String memberNo;
    /**
     * 扩展信息
     */
    private Map<String, String> extParam;
    
    /**
     * 零售产品码
     */
    private String retailProductCode;
    /**
     * 基础产品码
     */
    private String basicProductCode;
    
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public Map<String, String> getExtParam() {
		return extParam;
	}
	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}
	public String getRetailProductCode() {
		return retailProductCode;
	}
	public void setRetailProductCode(String retailProductCode) {
		this.retailProductCode = retailProductCode;
	}
	public String getBasicProductCode() {
		return basicProductCode;
	}
	public void setBasicProductCode(String basicProductCode) {
		this.basicProductCode = basicProductCode;
	}
	
	@Override
	public String toString() {
		 StringBuilder builder = new StringBuilder();
		 builder.append("PersonalMemberSyncPayRequestDTO [" );
		 builder.append(super.toString());
		 builder.append("]");
		 return builder.toString();
	}
    
}
