/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

/**
 * @author zhen.tan
 * 商户信息model
 *
 */
public class MerchantInfoModel {

	private String merchantAccountCode;
	
	/**
     * 易宝PrivateKey
     */
    private String yibaoPrivateKey;

    /**
     * 易宝PublicKey
     */
    private String yibaoPublicKey;

    /**
     * 商户PublicKey
     */
    private String merchantPublicKey; 
    
    /**
     * 商户名称
     */
    private String merchantName;
    
    /**
     * 商户等级
     */
    private String mechantLevel;

	public String getMerchantAccountCode() {
		return merchantAccountCode;
	}

	public void setMerchantAccountCode(String merchantAccountCode) {
		this.merchantAccountCode = merchantAccountCode;
	}

	public String getYibaoPrivateKey() {
		return yibaoPrivateKey;
	}

	public void setYibaoPrivateKey(String yibaoPrivateKey) {
		this.yibaoPrivateKey = yibaoPrivateKey;
	}

	public String getYibaoPublicKey() {
		return yibaoPublicKey;
	}

	public void setYibaoPublicKey(String yibaoPublicKey) {
		this.yibaoPublicKey = yibaoPublicKey;
	}

	public String getMerchantPublicKey() {
		return merchantPublicKey;
	}

	public void setMerchantPublicKey(String merchantPublicKey) {
		this.merchantPublicKey = merchantPublicKey;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMechantLevel() {
		return mechantLevel;
	}

	public void setMechantLevel(String mechantLevel) {
		this.mechantLevel = mechantLevel;
	}
    
    
}
