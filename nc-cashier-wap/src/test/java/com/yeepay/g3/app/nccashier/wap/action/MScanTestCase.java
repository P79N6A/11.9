package com.yeepay.g3.app.nccashier.wap.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.utils.common.DateUtils;

public class MScanTestCase {

	/**
	 * 正常的商户请求的入参
	 * 
	 * @return
	 */
	public static Map<String, String> getNormalMerchantParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("merchantNo", "10040007800");
		params.put("token", "0FB5183137B4B17E9D52A1F9E7F6BDC74CA5709702572EDEAC2CE7121908163D");
		params.put("timestamp", System.currentTimeMillis() / 1000 + "");
		params.put("codeType", "ZFB");
		params.put("code", "284526033752971810");
		params.put("storeCode", "12344");
		params.put("deviceSn", "USER_ID");
		return params;
	}

	/**
	 * 获取签名明文
	 * 
	 * @param params
	 * @return
	 */
	public static String getSignPlaintext(Map<String, String> params) {
		StringBuilder str = new StringBuilder();
		str.append("merchantNo=").append(params.get("merchantNo")).append("&token=").append(params.get("token"))
				.append("&timestamp=").append(params.get("timestamp")).append("&codeType=")
				.append(params.get("codeType")).append("&code=").append(params.get("code")).append("&storeCode=")
				.append(params.get("storeCode")).append("&deviceSn=").append(params.get("deviceSn"));
		if (CommonUtil.checkBizType(params.get("bizType"))) {
			str.append("&bizType=").append(params.get("bizType"));
		}
		return str.toString();
	}

	/**
	 * 正常的业务方（G2NET）请求的入参
	 * 
	 * @return
	 */
	public static Map<String, String> getNormalBizParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("merchantNo", "10040007800");
		params.put("token", "0FB5183137B4B17E9D52A1F9E7F6BDC74CA5709702572EDEAC2CE7121908163D");
		params.put("timestamp", System.currentTimeMillis() / 1000 + "");
		params.put("codeType", "ZFB");
		params.put("code", "284526033752971810");
		params.put("storeCode", "12344");
		params.put("deviceSn", "USER_ID");
		params.put("bizType", "G2NET");
		return params;
	}

	/**
	 * 时间戳过有效期
	 * 
	 * @return
	 */
	public static Map<String, String> getOutOfTimestampParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("merchantNo", "10040007800");
		params.put("token", "0FB5183137B4B17E9D52A1F9E7F6BDC74CA5709702572EDEAC2CE7121908163D");
		long seconds = 1487659807329l / 1000;
		params.put("timestamp", seconds + "");
		System.out.println(("当前时间：" + System.currentTimeMillis() / 1000));
		System.out.println(("时间戳入参：" + params.get("timestamp")));
		params.put("codeType", "ZFB");
		params.put("code", "284526033752971810");
		params.put("storeCode", "12344");
		params.put("deviceSn", "USER_ID");
		return params;
	}

	/**
	 * 时间戳为未来时间
	 * 
	 * @return
	 */
	public static Map<String, String> getFutureTimeStampParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("merchantNo", "10040007800");
		params.put("token", "0FB5183137B4B17E9D52A1F9E7F6BDC74CA5709702572EDEAC2CE7121908163D");
		Date tomorrow = DateUtils.addDay(new Date(), 1);
		params.put("timestamp", tomorrow.getTime() / 1000 + "");
		System.out.println(("当前时间：" + System.currentTimeMillis() / 1000));
		System.out.println(("时间戳入参：" + params.get("timestamp")));
		params.put("codeType", "ZFB");
		params.put("code", "284526033752971810");
		params.put("storeCode", "12344");
		params.put("deviceSn", "USER_ID");
		return params;
	}

	/**
	 * 商编为空
	 * 
	 * @return
	 */
	public static Map<String, String> getOneNullParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("merchantNo", null);
		params.put("token", "0FB5183137B4B17E9D52A1F9E7F6BDC74CA5709702572EDEAC2CE7121908163D");
		params.put("timestamp", System.currentTimeMillis() / 1000 + "");
		params.put("codeType", "ZFB");
		params.put("code", "284526033752971810");
		params.put("storeCode", "12344");
		params.put("deviceSn", "USER_ID");
		return params;
	}

	public static Map<String, String> liuyanParams() {
		Map<String, String> params = new HashMap<String,String>();
		params.put("merchantNo", "10040007800");
		params.put("token", "C7D747BA0BFDA60165ED78C08FAA0E79FB24FCB681F66AAB82455092D9CA96CA");
		long seconds = 1487659807329l / 1000;
		System.out.println(("当前时间：" + System.currentTimeMillis() / 1000));
		params.put("timestamp", System.currentTimeMillis() / 1000 + ""); // 1487659807329
		params.put("codeType", "WX");
		params.put("code", "130370146807545527");
		params.put("storeCode", "14344");
		params.put("deviceSn", "USER_ID");
		return params;
	}

}
