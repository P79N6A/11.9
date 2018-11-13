package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 被扫支付下单结果返回值
 * 
 * @author duangduang
 * @since 2017-02-17
 */
public class APIMerchantScanPayReponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 返回码 **/
	private String code;

	/** 返回信息 **/
	private String message;

	/** 签名 **/
	private String sign;

	public APIMerchantScanPayReponseVO() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	/**
	 * 获取签名入参串
	 * 
	 * @return
	 */
	public String generateSignContent() {
		StringBuilder str = new StringBuilder().append("code=");
		if(StringUtils.isNotBlank(this.code)){
			str.append(this.code);
		}
		str.append("&message=");
		if(StringUtils.isNotBlank(this.message)){
			str.append(this.message);
		}
		return str.toString();
	}
	
	public static void main(String[] args){
		APIMerchantScanPayReponseVO reponseVO = new APIMerchantScanPayReponseVO();
		System.out.println(reponseVO.generateSignContent());
	}
	
}
