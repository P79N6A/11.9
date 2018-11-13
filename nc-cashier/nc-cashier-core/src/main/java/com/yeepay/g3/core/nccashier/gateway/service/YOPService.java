package com.yeepay.g3.core.nccashier.gateway.service;

/**
 * YOP服务类
 * @author:zhen.tan
 * @since：2017年1月07日 下午4:27:04
 * @version:
 */
public interface YOPService {
	
	/**
	 * 签名(使用易宝私钥签名)
	 * 
	 * @param plaintext
	 * @return
	 */
	public String sign(String plaintext);
	
	
	
	/**
	 * 验签
	 * 
	 * @param appKey
	 *            验证商户请求的签名 ：商户编号 
	 *            验证业务方请求的签名：null
	 * @param plaintext
	 * @param signature
	 * @return
	 */
	public boolean verify(String appKey, String plaintext, String signature);
}
