package com.yeepay.g3.facade.nccashier.enumtype;



/**
 * 支付方式枚举类
 *
 */
public enum PayTypeEnum {

	 /**
     * api微信H5支付
     */
    WECHAT_H5_API(19),
    
	 /**
     * wap微信H5支付
     */
    WECHAT_H5_WAP(20),
    
    /**
     * 微信公众号支付
     */
    WECHAT_OPENID(21),
    
    /**
     * 微信主扫支付
     */
    WECHAT_ATIVE_SCAN(22),
    
    /**
	 * 银行卡WAP支付
	 */
	 BANK_PAY_WAP(23),
	
	/**
	 * 无卡收银台首次支付
	 */
	YJ_WAP_FIRST(24),
	
	/**
	 * 无卡收银台绑卡支付
	 */
	YJ_WAP_BIND(25),
	
	/**
	 * 移动收银台首次支付
	 */
	WALLET_WAP_FIRST(26),
	
	/**
	 * 移动收银台绑卡支付
	 */
	WALLET_WAP_BIND(27),
    
    /**
	 * 支付宝支付
	 */
	ALIPAY(28),
    
    /**
	 * 支付宝_API支付
	 */
	ALIPAY_API(29),
	
	/**
     * 微信主扫支付API
     */
    WECHAT_ATIVE_SCAN_API(30),
    
    /**
     * 微信公众号支付API
     */
    WECHAT_OPENID_API(31),
    
    /**
     * 网银支付
     */
    NET_BANK(32),
    
    /**
     * 分期付款
     */
    CFL(33),
    
    /**
     * 微信被扫
     */
    WECHAT_SCAN(34),
    
    /**
     * 支付宝被扫
     */
    ALIPAY_SCAN(35),

    /**
     * 分期支付3期
     */
    CFL_3(36),
    
    /**
     * 分期支付6期
     */
    CFL_6(37),
    
    /**
     * 分期支付9期
     */
    CFL_9(38),
    
    /**
     * 	分期支付12期
     */
    CFL_12(39),
    
    /**
     * 分期支付24期
     */
    CFL_24(40),
    
    /**
     * 白条
     */
    CFL_BT(41),
    
    /**
     * 支付宝SDK
     */
    ALIPAY_SDK(42),
    
    /**
     * 微信SDK
     */
    WECHAT_SDK(43), 
    
    /**
     * 银联钱包被扫
     */
    UPOP_PASSIVE_SCAN(44),
    
    /**
     * 银联钱包主扫
     */
    UPOP_ATIVE_SCAN(45),

	/**
	 * 会员支付
	 */
	ZF_ZHZF(46),

	/**
	 * 京东主扫
	 */
	JD_ATIVE_SCAN(47),

	/**
	 * 京东被扫
	 */
	JD_PASSIVE_SCAN(48),

	/**
	 * 京东h5
	 */
	JD_H5(49),

	/**
	 * 支付宝H5
	 */
	ALIPAY_H5(50),
	
	/**
	 * 非银行卡支付
	 */
	FYHK_FYHKZF(51),
	
	/**
	 * 支付宝生活号
	 */
	ZFB_SHH(52),

	/**
	 * 绑卡支付
	 */
	BK_ZF(53),

	/**
	 * wap微信H5支付，低配版
	 */
	WECHAT_H5_LOW(54),
	
	/**
	 * 银行卡分期
	 */
	YHKFQ_ZF(55),

	/**
	 * qq钱包主扫
	 */
	QQ_ATIVE_SCAN(56),

	/**
	 * qq钱包被扫
	 */
	QQ_PASSIVE_SCAN(57),
	
	/**
	 * 预授权支付
	 */
	YSQ(58),
	
	/**
	 * 支付宝H5标准版
	 */
	ALIPAY_H5_STANDARD(59),
	
	/**
	 * 个人会员支付
	 */
	GRHYZF(60),

    /**
     * 担保分期
     */
    DBFQ_TL(61),

	/**
	 * 一键扫码，该产品能用的前提：开通了PC或者H5快捷支付？
	 */
	NC_ATIVE_SCAN(62),

	/**
	 * 微信小程序支付
	 */
	XCX_OFFLINE_ZF(63),

	/**
	 * 分期易API
	 */
	ZF_FQY_API(64),

	/**
	 * 分期易标准
	 */
	ZF_FQY_NORMAL(65);

	private final int value;
	
	PayTypeEnum(int value){
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public static PayTypeEnum valueOfName(String name){
		PayTypeEnum[] types = values();
		for(PayTypeEnum type : types){
			if(type.name().equals(name)){
				return type;
			}
		}
		return BANK_PAY_WAP;
	}

	/**
	 * 判断payType是否为主扫支付
	 * @param payType
	 * @return
	 */
	public static boolean isActiveScanPay(String payType){
		return PayTypeEnum.ALIPAY.name().equals(payType) || PayTypeEnum.WECHAT_ATIVE_SCAN.name().equals(payType)
				|| PayTypeEnum.UPOP_ATIVE_SCAN.name().equals(payType) || PayTypeEnum.JD_ATIVE_SCAN.name().equals(payType)
				|| PayTypeEnum.ALIPAY_H5.name().equals(payType) || PayTypeEnum.QQ_ATIVE_SCAN.name().equals(payType);
	}

}
