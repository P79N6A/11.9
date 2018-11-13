package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

/**
 * 银行卡分期获取支持银行及期数的返回实体
 * 
 * @author duangduang
 *
 */
public class InstallmentBanksResponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 可用的分期银行列表
	 */
	private List<InstallmentBankInfoDTO> usableBankList;
	
	/**
	 * 不可用的分期银行列表，这里的不可用目前只有最低限额大于订单金额一种
	 */
	private List<InstallmentBankInfoDTO> UnusableBankList;

	public InstallmentBanksResponseDTO() {

	}

	public List<InstallmentBankInfoDTO> getUsableBankList() {
		return usableBankList;
	}

	public void setUsableBankList(List<InstallmentBankInfoDTO> usableBankList) {
		this.usableBankList = usableBankList;
	}
	
	public List<InstallmentBankInfoDTO> getUnusableBankList() {
		return UnusableBankList;
	}

	public void setUnusableBankList(List<InstallmentBankInfoDTO> unusableBankList) {
		UnusableBankList = unusableBankList;
	}

	@Override
	public String toString() {
		return "InstallmentBanksResponseDTO [usableBankList=" + usableBankList 
				+ ", unusableBankList=" + UnusableBankList
				+ ", returnCode=" + getReturnCode()
				+ ", returnMsg=" + getReturnMsg() 
				+ ", processStatusEnum=" + getProcessStatusEnum() 
				+ "]";
	}

}
