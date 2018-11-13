package com.yeepay.g3.facade.nccashier.enumtype;


public enum CashierVersionEnum {

	/**
	 * pc端
	 */
	PC,
	/**
	 * wap端
	 */
	WAP,
	/**
	 * API收银台
	 */
	API,

	/**
	 * 微信公众号
	 */
	WXGZH,
	/**
	 * SDK收银台
	 */
	SDK;



	public static int getVersionValue(CashierVersionEnum version) {
		if (version == CashierVersionEnum.WAP) {
			return 0;
		} else if (version == CashierVersionEnum.PC) {
			return 1;
		} else if (version == CashierVersionEnum.API) {
			return 2;
		} else if (version == CashierVersionEnum.WXGZH){
			return 3;
		}else {
			return 0;
		}
	}
	
	public static int getVersionValue(String version) {
		if (version == CashierVersionEnum.WAP.name()) {
			return 0;
		} else if (version == CashierVersionEnum.PC.name()) {
			return 1;
		} else if (version == CashierVersionEnum.API.name()) {
			return 2;
		} else if (version == CashierVersionEnum.WXGZH.name()){
			return 3;
		}else if (version == CashierVersionEnum.SDK.name()){
			return 4;
		}else {
			return 0;
		}
	}
	
	/**
	 * 订单处理器版本类型转换成收银台版本类型
	 * PC("PC收银台"), -----WEB(WEB收银台)
	 * WAP("移动收银台"), ——H5(H5收银台)
	 * SDK("SDK收银台")——SDK(SDK收银台)
	 * @param oprVersion
	 * @return
	 */
	public static CashierVersionEnum transformVersionFromOPRVersion(String oprVersion){
		if("H5".equals(oprVersion)){
			return WAP;
		}else if("WEB".equals(oprVersion)){
			return PC;
		}else if ("API".equals(oprVersion)){
			return API;
		}else if ("WXGZH".equals(oprVersion)){
			return WXGZH;
		}else if("SDK".equals(oprVersion)){
			return SDK;
		}else {
			return null;
		}
	}
}
