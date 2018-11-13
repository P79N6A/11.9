package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分期银行相关信息
 * 
 * @author duangduang
 *
 */
public class InstallmentBankInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 银行信息
	 */
	private BankInfoDTO bank;

	/**
	 * 当前银行对应的期数和费率
	 */
	private List<InstallmentPeriodAndRateInfoDTO> numsAndRates;

	/**
	 * 手续费收取方式
	 */
	private String rateWay;
	
	private String remark;
	
	private String status;

	public InstallmentBankInfoDTO() {

	}

	public void init() {
		bank = new BankInfoDTO();
		numsAndRates = new ArrayList<InstallmentPeriodAndRateInfoDTO>();
	}

	public BankInfoDTO getBank() {
		return bank;
	}

	public void setBank(BankInfoDTO bank) {
		this.bank = bank;
	}

	public List<InstallmentPeriodAndRateInfoDTO> getNumsAndRates() {
		return numsAndRates;
	}

	public void setNumsAndRates(List<InstallmentPeriodAndRateInfoDTO> numsAndRates) {
		this.numsAndRates = numsAndRates;
	}

	public String getRateWay() {
		return rateWay;
	}

	public void setRateWay(String rateWay) {
		this.rateWay = rateWay;
	}

	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "InstallmentBankInfoDTO [bank=" + bank + ", numsAndRates=" + numsAndRates + ", rateWay=" + rateWay + "]";
	}
}
