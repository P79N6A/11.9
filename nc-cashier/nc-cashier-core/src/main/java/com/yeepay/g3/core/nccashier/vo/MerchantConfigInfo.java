package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;

public class MerchantConfigInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String DirectPayType;
	
	private String PayType;
	
	private String payTool;
	
	public MerchantConfigInfo(){
		
	}

	public String getDirectPayType() {
		return DirectPayType;
	}

	public void setDirectPayType(String directPayType) {
		DirectPayType = directPayType;
	}

	public String getPayType() {
		return PayType;
	}

	public void setPayType(String payType) {
		PayType = payType;
	}

	public String getPayTool() {
		return payTool;
	}

	public void setPayTool(String payTool) {
		this.payTool = payTool;
	}
	
	/**
	 * 解析json串，反序列化为ExtendInfoFromPayRequest对象
	 * 
	 * @param json
	 * @return
	 */
	public static MerchantConfigInfo getFromJson(String json) {
		JSONObject jsonObject = CommonUtil.parseJson(json);
		if(jsonObject==null){
			return null;
		}
		return jsonObject.toJavaObject(MerchantConfigInfo.class);
	}
	
}
