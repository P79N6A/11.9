package com.yeepay.g3.app.nccashier.wap.enumtype;

public enum BrowserType {

	ALIPAY,

	WECHAT,

	UC,

	OTHER;

	public static boolean containsBrowserTypes(BrowserType[] browserTypesAllowed, BrowserType browserTypeCompared) {
		if (browserTypesAllowed == null || browserTypesAllowed.length <= 0 || browserTypeCompared == null) {
			// 传个空的，就不用比了，是异常情况
			return false;
		}
		for (BrowserType browserType : browserTypesAllowed) {
			if (browserType.equals(browserTypeCompared)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是微信或支付宝浏览器
	 * 
	 * @param browserType
	 * @return
	 */
	public static boolean isWxOrAlipayBrowser(BrowserType browserType) {
		BrowserType[] browserTypesAllowed = { BrowserType.ALIPAY, BrowserType.WECHAT };
		return containsBrowserTypes(browserTypesAllowed, browserType);
	}
}
