package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

/**
 * 签约关系列表查询接口返回实体
 * 
 * @author duangduang
 *
 */
public class SignRelationQueryResponseDTO extends APIBasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号 不允许为空
	 */
	private String userNo;

	/**
	 * 用户编号类型 不允许为空
	 */
	private String userType;

	/**
	 * 可用签约关系列表
	 */
	private List<SignRelationDTO> usableSignRelationList;

	/**
	 * 不可用签约关系列表：卡账户存在的签约关系，其对应银行未在配置中心配置
	 */
	private List<SignRelationDTO> unusableSignRelationList;

	public SignRelationQueryResponseDTO() {

	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<SignRelationDTO> getUsableSignRelationList() {
		return usableSignRelationList;
	}

	public void setUsableSignRelationList(List<SignRelationDTO> usableSignRelationList) {
		this.usableSignRelationList = usableSignRelationList;
	}

	public List<SignRelationDTO> getUnusableSignRelationList() {
		return unusableSignRelationList;
	}

	public void setUnusableSignRelationList(List<SignRelationDTO> unusableSignRelationList) {
		this.unusableSignRelationList = unusableSignRelationList;
	}

	@Override
	public String toString() {
		return "SignRelationQueryResponseDTO [userNo=" + userNo + ", userType=" + userType + ", usableSignRelationList="
				+ usableSignRelationList + ", unusableSignRelationList=" + unusableSignRelationList + "],"
				+ super.toString();
	}

}
