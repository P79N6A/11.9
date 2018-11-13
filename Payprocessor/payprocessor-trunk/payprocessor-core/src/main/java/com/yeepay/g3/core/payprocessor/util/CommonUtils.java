package com.yeepay.g3.core.payprocessor.util;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.utils.common.encrypt.AES;

import java.net.MalformedURLException;
import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;


/**
 * 工具类
 */
public class CommonUtils {

	
	/**
	 * 获取指定的facadeService
	 * @param  c
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getFacadeService(Class c,String url){
		HessianProxyFactory factory = new HessianProxyFactory();  
        try {  
        	Object obj= factory.create(c, url);
        	return obj;
        } catch (Exception e) {  
        	return null;
        }  	
	}

	/**
	 * 获取远程服务,
	 * @param facade
	 * @param <T>
	 * @return
	 */
	public static <T> T getRemoteFacade(Class<T> facade, String url, Integer connectTimeout, Integer readeTimeOut){
		HessianProxyFactory factory=new HessianProxyFactory();
		factory.setOverloadEnabled(true);
		factory.setConnectTimeout(connectTimeout);
		factory.setReadTimeout(readeTimeOut);
		try {
			return (T)factory.create(facade, url);
		} catch (MalformedURLException e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
	}
	
	/**
	 * 产生指定长度的随机数字
	 * @param length
	 * @return
	 */
	public static String makeOrderNumber(int length) {
		//产生一个四位数的随机数  
		String lastNum = "";  
		int num;  
		SecureRandom ran = new SecureRandom();  
		for(int i = 0; i<length;i++){  
		    num = ran.nextInt(10);  
		    lastNum += new Integer (num).toString();   
		}
		return lastNum;
	}
	
	/**
	 * 使用统一密钥进行加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String plainText) {
	    if(StringUtils.isBlank(plainText)) {
	        return plainText;
	    }
	    
	    return AES.encryptToBase64(plainText, ConstantUtils.getDB_AES_KEY());
	}
	
	/**
	 * 使用统一密钥进行解密
	 * 
	 * @param cipherText
	 * @return
	 */
	public static String decrypt(String cipherText) {
	    if(StringUtils.isBlank(cipherText)) {
	        return cipherText;
	    }
	    
	    try {
	        return AES.decryptFromBase64(cipherText, ConstantUtils.getDB_AES_KEY());
	    } catch(Throwable t) {
	        //考虑到兼容性，解密失败，使用原文
	        return cipherText;
	    }
	}
}
