package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class UrlExtInfoVO implements Serializable{

	private static Logger logger = LoggerFactory.getLogger(UrlExtInfoVO.class);
	
	private static final long serialVersionUID = 1L;
	
	private String openId;
	private String appId;
	private String clientId;
	private String aliUserId;
	private String aliAppId;
	private String platForm;
	private String appName;
	private String appStatement;
	private String reportFee;


	public UrlExtInfoVO(){
		
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAliUserId() {
		return aliUserId;
	}

	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
	}

	public String getAliAppId() {
		return aliAppId;
	}

	public void setAliAppId(String aliAppId) {
		this.aliAppId = aliAppId;
	}

	public String getPlatForm() {
		return platForm;
	}

	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppStatement() {
		return appStatement;
	}

	public void setAppStatement(String appStatement) {
		this.appStatement = appStatement;
	}

	public static UrlExtInfoVO parseExtJson(String json){
		if(StringUtils.isBlank(json)){
			return null;
		}
		
		try {
			JSONObject jsonObject = JSON.parseObject(json);
			if(jsonObject!=null){
				return jsonObject.toJavaObject(UrlExtInfoVO.class);
			}
		}catch (Throwable e){
			logger.warn("解析标准版链接扩展参数json=" + json + "异常, e=", e);
		}
		
		return null;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}

	public String getReportFee() {
		return reportFee;
	}

	public void setReportFee(String reportFee) {
		this.reportFee = reportFee;
	}
}
