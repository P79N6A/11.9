package com.yeepay.g3.core.nccashier.vo;


import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.dto.BankInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationDTO;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 银行实体
 * @author duangduang
 *
 */
public class BankInfo {

	/**
	 * 银行名称
	 */
	private String bankName;

	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * 最低限额（分期中的最低限额是以银行为区分的）
	 */
	private String minLimit; 

	public BankInfo() {

	}
	
	public BankInfo(String bankCode){
		setBankCode(bankCode);
	}
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(String minLimit) {
		this.minLimit = minLimit;
	}

	public BankInfoDTO transferToBankInfoDTO() {
		BankInfoDTO bankInfoDTO = new BankInfoDTO();
		bankInfoDTO.setBankCode(bankCode);
		bankInfoDTO.setBankName(bankName);
		bankInfoDTO.setMinLimit(minLimit);
		return bankInfoDTO;
	}

	public void setSignRelationDTO(SignRelationDTO signRelationDTO) {
		signRelationDTO.setBankCode(bankCode);
		signRelationDTO.setBankName(bankName);
		signRelationDTO.setMinLimit(minLimit);
	}

	public boolean bankMatch(String bankCode) {
		if (StringUtils.isBlank(bankCode)) {
			return false;
		}
		return this.bankCode.equals(bankCode);
	}

	public void standardBankCode() {
		bankCode = CommonUtil.standardBankCode(bankCode);
	}

	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder().
				append("BankInfo={").
				append("bankCode=").append(bankCode).append(",").
				append("bankName=").append(bankName).append(",").
				append("minLimit=").append(minLimit).append("}");
		return str.toString();
	}
}
