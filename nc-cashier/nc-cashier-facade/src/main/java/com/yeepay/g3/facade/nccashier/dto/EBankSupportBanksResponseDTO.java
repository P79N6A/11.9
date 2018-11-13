package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * pc网银 请求支持的收银台模板返回实体类
 * @author duangduang
 * @since  2016-11-08
 */
public class EBankSupportBanksResponseDTO extends BasicResponseDTO{

	private static final long serialVersionUID = 1L;
	
	/**
	 * b2b银行
	 */
	private List<BaseBankInfo> b2bBanks;
	
	/**
	 * b2c银行
	 */
	private List<BaseBankInfo> b2cBanks;



	/**
	 * b2b支付场景
	 */
	private String b2bPayScene;

	/**
	 * b2c支付场景
	 */
	private String b2cPayScene;

	public String getB2bPayScene() {
		return b2bPayScene;
	}

	public void setB2bPayScene(String b2bPayScene) {
		this.b2bPayScene = b2bPayScene;
	}

	public String getB2cPayScene() {
		return b2cPayScene;
	}

	public void setB2cPayScene(String b2cPayScene) {
		this.b2cPayScene = b2cPayScene;
	}

	public EBankSupportBanksResponseDTO(){
		
	}

	public List<BaseBankInfo> getB2cBanks() {
		return b2cBanks;
	}

	public void setB2cBanks(List<BaseBankInfo> b2cBanks) {
		this.b2cBanks = b2cBanks;
	}

	public List<BaseBankInfo> getB2bBanks() {
		return b2bBanks;
	}

	public void setB2bBanks(List<BaseBankInfo> b2bBanks) {
		this.b2bBanks = b2bBanks;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	

}
