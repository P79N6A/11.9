package com.yeepay.g3.app.nccashier.wap.vo;

import java.util.List;

/**
 * 银行分期信息
 * @author duangduang
 *
 */
public class InstallmentBankNumberInfoVO extends BasicResponseVO{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 可用银行列表
	 */
	private List<BankInfoVO> usableBankList;
	
	/**
	 * 不可用银行列表
	 */
	private List<BankInfoVO> unusableBankList;
	
	/**
	 * 可用签约列表
	 */
	private List<SignRelationVO> usableSignRelationList;
	
	/**
	 * 不可用签约列表
	 */
	private List<SignRelationVO> unusableSignRelationList;
	
	/**
	 * 模式，表明当前是否是有签约列表的模式
	 */
	private String mode;

	public InstallmentBankNumberInfoVO(){
		
	}

	public List<BankInfoVO> getUsableBankList() {
		return usableBankList;
	}

	public void setUsableBankList(List<BankInfoVO> usableBankList) {
		this.usableBankList = usableBankList;
	}

	public List<BankInfoVO> getUnusableBankList() {
		return unusableBankList;
	}

	public void setUnusableBankList(List<BankInfoVO> unusableBankList) {
		this.unusableBankList = unusableBankList;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return "InstallmentBankNumberInfoVO [usableBankList=" + usableBankList + ", unusableBankList="
				+ unusableBankList + ", usableSignRelationList=" + usableSignRelationList
				+ ", unusableSignRelationList=" + unusableSignRelationList + ", mode=" + mode + "]";
	}
}
