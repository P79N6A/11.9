package com.yeepay.g3.app.fronend.app.utils;

import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayRuntimeException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstantUtil {
	private static final String CONFIG_PREFIX = "OL_NCCONFIG_";
	
	public static final String MPAYURLKEY= "WECHAT_SERVICE_URL";

	public static final String DATE_FORMAT_MIN = "yyyy-MM-dd HH:mm";
	public static final String DATE_FORMAT_HOUR = "yyyy-MM-dd HH";
	public static final SimpleDateFormat MIN_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat HOUR_SDF = new SimpleDateFormat("yyyy-MM-dd HH");

	private static final Logger logger = LoggerFactory.getLogger(ConstantUtil.class);
	@SuppressWarnings("unchecked")
	private static <T> T getSysConfig(String key){
		ConfigParam<T> p = ConfigurationUtils.getSysConfigParam(CONFIG_PREFIX+key);
		if(p==null){
			logger.error("无法从统一配置种获取"+key);
			throw new YeepayRuntimeException("无法从统一配置种获取"+key);
		}
		return p.getValue();
	}
	
	public static String getSysConfigValue(String key) {
		if (StringUtils.isBlank(key)) {
			throw new NullPointerException("URLKey is null");
		}
		String wechat_url =null;
		try {
			 wechat_url = getSysConfig(key);
			 if(wechat_url==null){
				 throw new YeepayRuntimeException("未在统一配置下配置WX服务地址");
			 }
		} catch (Exception e) {
			logger.error("获取wechat_url出错",e);
			throw new YeepayRuntimeException("无法从统一配置种获取WX服务地址");
		}
		return wechat_url;
	}

	public static String getDefaultStartDate(){
		long timestamp = System.currentTimeMillis();
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp)) + " 00:00:00";
	}

	public static String getDefaultEndDate(){
		long timestamp = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp)) + " 00:00:00";
	}

}
