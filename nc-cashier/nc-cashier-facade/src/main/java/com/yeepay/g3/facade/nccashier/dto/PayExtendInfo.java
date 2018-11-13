/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;

import java.io.Serializable;
import java.util.Arrays;


/**
 * @author zhen.tan
 *
 */
public class PayExtendInfo implements Serializable{
	
	private static final long serialVersionUID = 5230616965013160L;

	private String payTypes;

	private String identityType;
	
	private String identityId;
	
	private String directPayType;
	// 收银台版本
	private String cashierVersion;

	// 商户请求的产品类型,根据产品类型，显示收银台列表
	private String[] payTool;
	
	/**
	 * 原始appid
	 */
	private String origAppId;
	
	/**
	 * 路由得到的appid
	 */
	private String targetAppId;
	
	/**
	 * 预路由得到的支付宝生活号的appId
	 */
	private String targetAliAppId;
	
	/**
	 * add by meiling.zhuang:商户（业务方）对接标准版，传入的入参openId字段的值
	 */
	private String origOpenId;
	
	/**
	 * 商户透传的支付宝生活号的userId
	 */
	private String origAlipayUserId;

	/**
	 * 商户透传的支付宝生活的appId
	 */
	private String oriAlipayAppId;
	
	/**
	 * 基础产品码
	 */
	private String accountProductCode;
	
	/**
	 * 零售产品码
	 */
	private String saleProductCode;

	//公众号应用密钥
	private String appSecert;
	
	/**
	 * 支付宝生活号预路由返回
	 */
	private String aliAppSecret;
	
	/**
	 * 过期时间
	 */
	private String expireTime;

	/**
	 * 公众号预路由场景类型扩展（jsapiH5/normal）
	 */
	private String sceneTypeExt;

	/**
	 * 钱包支付h5优先级-微信h5
	 */
	private String ewalletLevelWechat;
	
	/**
	 * 支付宝H5优先级
	 */
	private String alipayH5Priority;

	public String getAccountProductCode() {
		return accountProductCode;
	}

	public void setAccountProductCode(String accountProductCode) {
		this.accountProductCode = accountProductCode;
	}

	public String getSaleProductCode() {
		return saleProductCode;
	}

	public void setSaleProductCode(String saleProductCode) {
		this.saleProductCode = saleProductCode;
	}

	public String getOrigAppId() {
		return origAppId;
	}

	public void setOrigAppId(String origAppId) {
		this.origAppId = origAppId;
	}

	public String getTargetAppId() {
		return targetAppId;
	}

	public void setTargetAppId(String targetAppId) {
		this.targetAppId = targetAppId;
	}

	public String getDirectPayType() {
		return directPayType;
	}

	public void setDirectPayType(String directPayType) {
		this.directPayType = directPayType;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(String payTypes) {
		this.payTypes = payTypes;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	

	public String[] getPayTool() {
		return payTool;
	}

	public void setPayTool(String[] payTool) {
		this.payTool = payTool;
	}

	public String getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(String cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public String getAppSecert() {
		return appSecert;
	}

	public void setAppSecert(String appSecert) {
		this.appSecert = appSecert;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	

	public String getOrigOpenId() {
		return origOpenId;
	}

	public void setOrigOpenId(String origOpenId) {
		this.origOpenId = origOpenId;
	}

	public String getSceneTypeExt() {
		return sceneTypeExt;
	}

	public void setSceneTypeExt(String sceneTypeExt) {
		this.sceneTypeExt = sceneTypeExt;
	}
	
	public String getOrigAlipayUserId() {
		return origAlipayUserId;
	}

	public void setOrigAlipayUserId(String origAlipayUserId) {
		this.origAlipayUserId = origAlipayUserId;
	}

	public String getOriAlipayAppId() {
		return oriAlipayAppId;
	}

	public void setOriAlipayAppId(String oriAlipayAppId) {
		this.oriAlipayAppId = oriAlipayAppId;
	}
	
	public String getTargetAliAppId() {
		return targetAliAppId;
	}

	public void setTargetAliAppId(String targetAliAppId) {
		this.targetAliAppId = targetAliAppId;
	}
	
	public String getAliAppSecret() {
		return aliAppSecret;
	}

	public void setAliAppSecret(String aliAppSecret) {
		this.aliAppSecret = aliAppSecret;
	}
	

	public String getAlipayH5Priority() {
		return alipayH5Priority;
	}

	public void setAlipayH5Priority(String alipayH5Priority) {
		this.alipayH5Priority = alipayH5Priority;
	}
	
	/**
	 * 判断当前开通的支付宝版本是什么，没有开通支付宝是允许的
	 * @return
	 */
	public String toDecideAlipayVersion() {
		// 优先级不为空，说明双开了
		if (StringUtils.isNotBlank(alipayH5Priority)) {
			return alipayH5Priority;
		}

		// 走到这儿，就说明一定没有双开
		if (containsPayType(PayTypeEnum.ALIPAY_H5_STANDARD)) {
			return PayTypeEnum.ALIPAY_H5_STANDARD.name();
		}

		// 支付宝和支付宝H5双开的情况，优先走支付宝
		boolean openAlipaySDK = containsPayType(PayTypeEnum.ALIPAY);
		boolean openAlipayH5 = containsPayType(PayTypeEnum.ALIPAY_H5);
		if (openAlipaySDK || openAlipayH5) {
			return openAlipaySDK ? PayTypeEnum.ALIPAY.name() : PayTypeEnum.ALIPAY_H5.name();
		}
		
//		throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		return null;
	}

	/**
	 * 判断是否支持底层的支付工具
	 * @param payType
	 * @return
	 */
	public boolean containsPayType(PayTypeEnum payType) {
		return this.getPayTypes().indexOf(payType.value() + "") > -1;
	}

	// 是否是PC扫码支付
	public boolean isSccanPay() {
		return ((CashierVersionEnum.PC.name().equals(this.getCashierVersion()))
				&& (containsPayTool(PayTool.SCCANPAY)));
	}

	public boolean containsPayTool(PayTool type) {
		String[] strArray = this.getPayTool();
		if (strArray != null && strArray.length > 0) {
			for (String str : strArray) {
				if (str.equals(type.name())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是分期支付方式
	 * 
	 * @return
	 */
	public boolean isInstallment() {
		return ((CashierVersionEnum.PC.name().equals(this.getCashierVersion())) && (containsPayTool(PayTool.CFL)));
	}

	/**
	 * 判断paytypes是否有分期
	 * @return
	 */
	public boolean containsCflPayTypes(){
		if(this.getPayTypes().indexOf(PayTypeEnum.CFL.value() + "") > -1
				|| this.getPayTypes().indexOf(PayTypeEnum.CFL_3.value() + "") > -1
				|| this.getPayTypes().indexOf(PayTypeEnum.CFL_6.value() + "") > -1
				|| this.getPayTypes().indexOf(PayTypeEnum.CFL_9.value() + "") > -1
				|| this.getPayTypes().indexOf(PayTypeEnum.CFL_12.value() + "") > -1
				|| this.getPayTypes().indexOf(PayTypeEnum.CFL_24.value() + "") > -1
				|| this.getPayTypes().indexOf(PayTypeEnum.CFL_BT.value() + "") > -1){

			return true;
		}else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "PayExtendInfo{" +
				"payTypes='" + payTypes + '\'' +
				", identityType='" + identityType + '\'' +
				", identityId='" + HiddenCode.hiddenIdentityId(identityId) + '\'' +
				", directPayType='" + directPayType + '\'' +
				", cashierVersion='" + cashierVersion + '\'' +
				", payTool=" + Arrays.toString(payTool) +
				", origAppId='" + origAppId + '\'' +
				", targetAppId='" + targetAppId + '\'' +
				", origOpenId='" + origOpenId + '\'' +
				", accountProductCode='" + accountProductCode + '\'' +
				", saleProductCode='" + saleProductCode + '\'' +
				", appSecert='" + appSecert + '\'' +
				", expireTime='" + expireTime + '\'' +
				", sceneTypeExt='" + sceneTypeExt + '\'' +
				", ewalletLevelWechat='" + ewalletLevelWechat + '\''
				+ ", alipayH5Priority='" + alipayH5Priority + '\''
				+ 
				'}';
	}

	public String getEwalletLevelWechat() {
		return ewalletLevelWechat;
	}

	public void setEwalletLevelWechat(String ewalletLevelWechat) {
		this.ewalletLevelWechat = ewalletLevelWechat;
	}
	
	
}

