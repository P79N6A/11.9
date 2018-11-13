package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

/**
 * 银行卡分期路由返回实体
 * 
 * @author duangduang
 *
 */
public class InstallmentRouteResponseDTO extends InstallmentBanksResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 可用签约关系列表
	 */
	private List<SignRelationDTO> usableSignRelationList;

	/**
	 * 1，不可用签约关系列表；
	 * 2，目前不可用的场景有两种：① 银行最低限额>订单金额；② 签约记录银行在配置中心未开通；
	 */
	private List<SignRelationDTO> unusableSignRelationList;

	public InstallmentRouteResponseDTO() {

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
		return "InstallmentRouteResponseDTO [usableSignRelationList=" + usableSignRelationList
				+ ", unusableSignRelationList=" + unusableSignRelationList 
				+ ", usableBankList=" + getUsableBankList()
				+ ", returnCode=" + getReturnCode()
				+ ", returnMsg=" + getReturnMsg() 
				+ ", processStatusEnum=" + getProcessStatusEnum()
				+ "]";
	}
}
