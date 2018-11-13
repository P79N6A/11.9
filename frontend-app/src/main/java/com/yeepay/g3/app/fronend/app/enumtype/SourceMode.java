package com.yeepay.g3.app.fronend.app.enumtype;

public class SourceMode extends BinaryMode {

	private static final long serialVersionUID = 1L;

	public SourceMode(int aValue) {
		super(aValue);
	}
	
	private static final int BASE=1;
	/**
	 * 线下pos
	 */
	public static final int POS = BASE<<0;
	/**
	 * 线上
	 */
	public static final int ONLINE = BASE<<1;
	/**
	 * 网关
	 */
	public static final int GATEWAY = BASE<<2;
	/**
	 * 公众号支付
	 */
	public static final int MPPAY = BASE<<3;
	/**
	 * 收款宝
	 */
	public static final int SKB = BASE<<4;
	/**
	 * 掌柜通
	 */
	public static final int ZGT = BASE<<5;
	/**
	 * 线上直销商户
	 */
	public static final int GENERAL = BASE<<6;
	
	/**
	 * 来源是不是POS
	 * @return
	 */
	public boolean isPOS(){
		return isAllow(POS);
	}
	/**
	 * 来源是不是ONLINE
	 * @return
	 */
	public boolean isONLINE(){
		return isAllow(ONLINE);
	}
	/**
	 * 来源是不是GATEWAY
	 * @return
	 */
	public boolean isGATEWAY(){
		return isAllow(GATEWAY);
	}
	/**
	 * 来源是不是MPPAY
	 * @return
	 */
	public boolean isMPPAY(){
		return isAllow(MPPAY);
	}
	/**
	 * 来源是不是收款宝
	 * @return
	 */
	public boolean isSKB(){
		return isAllow(SKB);
	}
	/**
	 * 来源是不是掌柜通
	 * @return
	 */
	public boolean isZGT(){
		return isAllow(ZGT);
	}
	/**
	 * 来源是不是线上直销商户  
	 * @return
	 */
	public boolean isGENERAL(){
		return isAllow(GENERAL);
	}
}
