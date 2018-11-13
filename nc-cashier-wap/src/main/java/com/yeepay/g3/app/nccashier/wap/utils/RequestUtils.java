package com.yeepay.g3.app.nccashier.wap.utils;

import javax.servlet.http.HttpServletRequest;

import com.yeepay.g3.app.nccashier.wap.enumtype.BrowserType;
import com.yeepay.g3.app.nccashier.wap.vo.UserIpVO;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * Request工具类
 *
 * @author da.zhang
 * @date 2013-6-27
 */
public class RequestUtils {
	
	private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);
	
	/**
	 * 获取用户IP
	 * 
	 * @param req
	 * 			HttpServletRequest
	 * @return IP
	 */
	public static final String getUserIP(HttpServletRequest req) {
		String ip = req.getHeader("x-forwarded-for");   
		logger.info("x-forwarded-for={}",ip);
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
			ip = req.getHeader("Proxy-Client-IP"); 
			logger.info("Proxy-Client-IP={}",ip);
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
			ip = req.getHeader("WL-Proxy-Client-IP");   
			logger.info("WL-Proxy-Client-IP={}",ip);
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
			ip = req.getRemoteAddr();  
			logger.info("RemoteAddr={}",ip);
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP地址，多个IP由","分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}
	
	public static String getReferer(HttpServletRequest req) {
		return req.getHeader("referer");
	}
	
	/**
	 * 获取用户终端UA
	 * 
	 * @param req
	 * 			HttpServletRequest
	 * @return UA
	 */
	public static final String getUserUA(HttpServletRequest req) {
		return req.getHeader("User-Agent");
	}
	
	public static final BrowserType getBrowserType(HttpServletRequest request) {
		String ua = RequestUtils.getUserUA(request);
		if (isWxBrowser(ua)) {
			return BrowserType.WECHAT;
		} else if (isAlipayBrowser(ua)) {
			return BrowserType.ALIPAY;
		}

		return BrowserType.OTHER;
	}

	private static final boolean isWxBrowser(String ua) {
		boolean isWeChatBrowser = ua.indexOf("MicroMessenger") > -1;
		return isWeChatBrowser;
	}

	private static final boolean isAlipayBrowser(String ua) {
		boolean isAlipayBrower = ua.indexOf("Alipay") > -1;
		return isAlipayBrower;
	}
	
}
