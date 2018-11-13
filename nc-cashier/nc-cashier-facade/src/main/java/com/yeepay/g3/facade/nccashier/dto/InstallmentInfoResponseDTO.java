package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

/**
 * 银行卡分期支持的银行及期数查询接口返回实体
 * 
 * @author duangduang
 *
 */
public class InstallmentInfoResponseDTO extends APIBasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 支持的银行和费率信息
	 */
	private List<InstallmentBankInfoDTO> installmentBankList;

	public InstallmentInfoResponseDTO() {
		super();
	}

	public List<InstallmentBankInfoDTO> getInstallmentBankList() {
		return installmentBankList;
	}

	public void setInstallmentBankList(List<InstallmentBankInfoDTO> installmentBankList) {
		this.installmentBankList = installmentBankList;
	}

	@Override
	public String toString() {
		return "InstallmentInfoResponseDTO [merchantNo=" + getMerchantNo() + ", installmentBankList="
				+ installmentBankList + "]";
	}

}
