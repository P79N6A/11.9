package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class InstallmentBankResponseVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private List<BankInfoVO> usableBankList;
	
	/**
	 * key：银行编码
	 */
	private Map<String, List<String>> periodListOfBank;
	
	private List<SignRelationVO> usableSignRelationList;
	
	private List<SignRelationVO> unusableSignRelationList;
	
	private String mode;
	
	public InstallmentBankResponseVO(){
		
	}

	public List<BankInfoVO> getUsableBankList() {
		return usableBankList;
	}

	public void setUsableBankList(List<BankInfoVO> usableBankList) {
		this.usableBankList = usableBankList;
	}

	public Map<String, List<String>> getPeriodListOfBank() {
		return periodListOfBank;
	}

	public void setPeriodListOfBank(Map<String, List<String>> periodListOfBank) {
		this.periodListOfBank = periodListOfBank;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<SignRelationVO> getUsableSignRelationList() {
		return usableSignRelationList;
	}

	public void setUsableSignRelationList(List<SignRelationVO> usableSignRelationList) {
		this.usableSignRelationList = usableSignRelationList;
	}

	public List<SignRelationVO> getUnusableSignRelationList() {
		return unusableSignRelationList;
	}

	public void setUnusableSignRelationList(List<SignRelationVO> unusableSignRelationList) {
		this.unusableSignRelationList = unusableSignRelationList;
	}

	@Override
	public String toString() {
		return "InstallmentBankResponseVO [usableBankList=" + usableBankList + ", periodListOfBank=" + periodListOfBank
				+ ", usableSignRelationList=" + usableSignRelationList + ", unusableSignRelationList="
				+ unusableSignRelationList + ", mode=" + mode + "]";
	}
}
