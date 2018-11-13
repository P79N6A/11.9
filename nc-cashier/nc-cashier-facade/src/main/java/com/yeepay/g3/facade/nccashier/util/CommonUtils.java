/**
 * 
 */
package com.yeepay.g3.facade.nccashier.util;

import com.yeepay.g3.facade.nccashier.enumtype.TradeSysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Stack;

/**
 * @author xueping.ni
 * @date 2017年3月15日
 *
 */
public class CommonUtils {
	private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);


	/**
	 * 商家扫码URL时间戳有效期（秒）
	 */
	public static final String API_MSCAN_TIMESTAMP_RANGE_WITCH = "OL_NCCASHIER_API_MSCAN_URL_EXPDATE";


	
	
	
	/**
	 * 字符串反转
	 * 
	 * @param s
	 * @return
	 */
	public static String reverse(String s) {
		char[] str = s.toCharArray();
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < str.length; i++)
			stack.push(str[i]);

		String reversed = "";
		for (int i = 0; i < str.length; i++)
			reversed += stack.pop();

		return reversed;
	}

	/**
	 * 获取字符串后七位
	 * 
	 * @param str
	 * @return
	 */
	public static String getLastSevenNo(String str) {
		String s = "";
		if (str.length() >= 7) {
			s = str.substring(str.length() - 7);
		} else {
			s = str;
			for (int i = 0; i < 7 - str.length(); i++) {
				s = s + "0";
			}
		}
		return s;
	}
	
	
	/**
	 * 获取三代统一配置信息
	 * 
	 * @param key
	 * @return Map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getConfig(String key) {

		ConfigParam<Map<String, String>> businessConfig = ConfigurationUtils.getSysConfigParam(key);
		Map<String, String> businessInfoMap = null;
		if (null != businessConfig) {
			businessInfoMap = businessConfig.getValue();
		}
		return businessInfoMap;

	}
	
	/**
	 * 返回商家扫码URL的有效期（以秒为单位）
	 * 
	 * @return
	 */
	public static long getMscanUrlExpDate() {
		long expDate = 120; // 秒为单位
		try {
			String expDateStr = getSysConfigFrom3G(API_MSCAN_TIMESTAMP_RANGE_WITCH);
			expDate = Long.parseLong(expDateStr);
		} catch (Throwable t) {
			expDate = 120;
			logger.error("获取统一配置信息失败:{}", t);
		}
		return expDate;
	}



	
	/**
	 * 获得3代统一配置数据
	 * 
	 * @param key
	 * @return
	 */
	public static <T> T getSysConfigFrom3G(String key) {

		T config = null;
		try {
			ConfigParam p = ConfigurationUtils.getSysConfigParam(key);
			if (p == null || p.getValue() == null) {
				return null;
			} else {
				config = (T) p.getValue();
			}
		} catch (Exception e) {
			logger.error("获取出错", e);
			return null;
		}
		return config;
	}
	
	/**
	 * 商户url是否过期
	 * 
	 * @param timestampInput （秒为单位）
	 * @return
	 */
	public static void checkUrlOutOfExpDate(long timestampInput) {
		long distance = System.currentTimeMillis() - (timestampInput*1000);
		long expdate = CommonUtils.getMscanUrlExpDate() * 1000;
		if(distance > expdate){
			throw new CashierBusinessException(Errors.TIMESTAMP_OUT_OF_EXPIRE_DATE.getCode(), Errors.TIMESTAMP_OUT_OF_EXPIRE_DATE.getMsg());
		}
//		if(distance < 0){
//			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "时间戳不能大于当前日期");
//		}
	}

	/**
	 *
	 * @param paySysCode 支付系统号
	 * @param tradeSysNo 交易系统号
	 * @return
	 */
	public static boolean isPayProcess(String paySysCode,String tradeSysNo){
		//为了兼容老的数据，所以需要判断交易系统编码
		if(TradeSysCodeEnum.G2NET.name().equals(tradeSysNo) || TradeSysCodeEnum.DS.name().equals(tradeSysNo)){
			return true;
		}else {
			return StringUtils.isNotBlank(paySysCode) && paySysCode.equals("PAY_PROCCESOR");
		}
	}


}
