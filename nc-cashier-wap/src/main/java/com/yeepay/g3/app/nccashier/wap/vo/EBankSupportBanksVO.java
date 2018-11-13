package com.yeepay.g3.app.nccashier.wap.vo;

import java.util.List;

/**
 * 商户支持的网银支付的网银列表
 * 
 * @author duangduang
 * @since 2016-11-10
 */
public class EBankSupportBanksVO extends ResponseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 对公银行列表
	 */
	private List<EBankBaseVO> b2bBanks;

	/**
	 * 对私银行列表
	 */
	private List<EBankBaseVO> b2cBanks;


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

	public EBankSupportBanksVO() {

	}

	public List<EBankBaseVO> getB2bBanks() {
		return b2bBanks;
	}

	public void setB2bBanks(List<EBankBaseVO> b2bBanks) {
		this.b2bBanks = b2bBanks;
	}

	public List<EBankBaseVO> getB2cBanks() {
		return b2cBanks;
	}

	public void setB2cBanks(List<EBankBaseVO> b2cBanks) {
		this.b2cBanks = b2cBanks;
	}

}
