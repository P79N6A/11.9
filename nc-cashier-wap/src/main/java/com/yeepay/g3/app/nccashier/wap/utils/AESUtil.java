package com.yeepay.g3.app.nccashier.wap.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.encrypt.AES;

public class AESUtil {

	/**
	 * aes密钥
	 */
	public static final String AES_CHANGE_KEY = "NCCASHIER";

	public static final String AES_FIXED_KEY = "qwa7kjuh4gbcv58t";
	
	/**
	 * 对数据进行aes加密
	 * @param data
	 * @return
	 */
	public static String aesEncrypt(String data,String aesKey) {
		if (StringUtils.isBlank(data)) {
			return data;
		} else {
			return AES.encryptToBase64(data, aesKey);
		}
	}

	/**
	 * (固定key)对数据进行aes加密
	 * 
	 * @param data
	 * @return
	 */
	public static String aesEncrypt(String data) {
		if (StringUtils.isBlank(data)) {
			return data;
		} else {
			return AES.encryptToBase64(data, AES_FIXED_KEY);
		}
	}
	
	/**
	 * 解密aes加密的数据
	 * 
	 * @param data
	 * @return
	 */
	public static String aesDecrypt(String data,String aesKey) {
		if (StringUtils.isBlank(data)) {
			return data;
		} else {
			return AES.decryptFromBase64(data, aesKey);
		}
	}

	/**
	 * (固定key)解密aes加密的数据
	 * 
	 * @param data
	 * @return
	 */
	public static String aesDecrypt(String data) {
		if (StringUtils.isBlank(data)) {
			return data;
		} else {
			return AES.decryptFromBase64(data, AES_FIXED_KEY);
		}
	}
	/**
	 * 获取key
	 * @param merchantNo
	 * @return
	 */
	public static String getKey(String merchantNo){
		if(StringUtils.isBlank(merchantNo)){
			return merchantNo;
		}else{
			//字符串反转
			String reversMerchantNo = CommonUtil.reverse(merchantNo);
			//获取字符串后7位
			String keyNo = CommonUtil.getLastSevenNo(reversMerchantNo);
			return keyNo+AES_CHANGE_KEY;
		}
	}
	/**
	 * 解密requestID
	 * @param merchantNo
	 * @param encRequestID
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String routeDecrypt(String merchantNo,String encRequestID) throws UnsupportedEncodingException{
		String aesKey = getKey(merchantNo);
		encRequestID= URLDecoder.decode(encRequestID,"UTF-8");
		encRequestID=encRequestID.replaceAll("-", "+");
		encRequestID=encRequestID.replaceAll("\\*","/");
		String decRequestId =aesDecrypt(encRequestID, aesKey);
		return decRequestId;
	}
	/**
	 * 加密requestID
	 * @param merchantNo
	 * @param requestId
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String routeEncrypt(String merchantNo,String requestId) throws UnsupportedEncodingException{
		String aesKey = getKey(merchantNo);
		String encRequestId =aesEncrypt(requestId, aesKey);
		encRequestId=encRequestId.replaceAll("\\+", "-");
		encRequestId=encRequestId.replaceAll("/","*");
		encRequestId = URLEncoder.encode(encRequestId, "UTF-8");
		return encRequestId;
	}

}
