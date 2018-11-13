package com.yeepay.g3.app.nccashier.wap.utils;

import javax.servlet.http.HttpServletRequest;

import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;


public class PayerIpUtils {
	
	private static Logger logger = LoggerFactory.getLogger(PayerIpUtils.class);
	
	/**
	 * 根据银行编码和对公对私属性获取payerIp
	 * 
	 * @param request
	 * @param bankCode
	 * @param bankAccountCode
	 * @return
	 */
	public static String getNetPayerIp(HttpServletRequest request, String bankCode, String bankAccountCode){
		String[] netPayerIpConfig = CommonUtil.getNetPayerIpConfig(bankCode);
		if(netPayerIpConfig!=null && netPayerIpConfig.length != 0){
			String ip = getPayerIpByConfig(request, netPayerIpConfig);
			if(StringUtils.isNotBlank(ip)){
				return ip;
			}
		}
		
		if("ICBC".equals(bankCode)){
			return getXforwardThenRemoteIp(request);
		}
		if("CMBCHINA".equals(bankCode)){
			return xforwardThenXRealThenRemote(request);
		}
		
		return null;
	}

	private static String getXforwardThenRemoteIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");     
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
			ip = request.getRemoteAddr();     
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP地址，多个IP由","分割
		return substringIp(ip);
	}

//	private static String getRemoteAddrIp(HttpServletRequest request){
//		String ip =  request.getRemoteAddr();          
//		// 对于通过多个代理的情况，第一个IP为客户端真实IP地址，多个IP由","分割
//		return substringIp(ip);
//	}
	
	private static String getPayerIpByConfig(HttpServletRequest request, String[] netPayerIpConfig) {
		if (netPayerIpConfig == null || netPayerIpConfig.length == 0) {
			return null;
		}
		for (String netPayerIpConf : netPayerIpConfig) {
			if (StringUtils.isBlank(netPayerIpConf)) {
				continue;
			}
			String ip = "remoteAddr".equals(netPayerIpConf) ? request.getRemoteAddr()
					: request.getHeader(netPayerIpConf);
			if (ip != null && ip.length() != 0 && "unknown".equalsIgnoreCase(ip)) {
				logger.info(netPayerIpConf + ":{}", ip);
				return substringIp(ip);
			}
		}
		return null;
	}
	
	private static String xforwardThenXRealThenRemote(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		logger.info("x-forwarded-for:{}", ip);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
			logger.info("X-Real-IP:{}", ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			logger.info("remoteAddr:{}", ip);
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP地址，多个IP由","分割
		return substringIp(ip);
	}
	
	private static String substringIp(String ip){
		// 对于通过多个代理的情况，第一个IP为客户端真实IP地址，多个IP由","分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}
}
