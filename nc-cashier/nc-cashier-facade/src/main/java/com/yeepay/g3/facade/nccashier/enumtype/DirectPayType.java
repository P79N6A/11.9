package com.yeepay.g3.facade.nccashier.enumtype;

import org.apache.commons.lang.StringUtils;

/**
 * 直连支付方式
 *
 * @author eric
 * @date 2013-3-19
 *
 */
public enum DirectPayType {
	/**
	 * 不直连
	 */
	NONE(0),
	
	/**
	 * 直连微信
	 */
	WECHAT(1),
	
	/**
	 * 直连支付宝
	 */
	ALIPAY(2),
	
	/**
	 * 直连一键支付
	 */
	YJZF(3),
	
	/**
	 * 工商银行B2C
	 */
	ICBC_B2C(4),
	
	/**
	 * 工商银行B2B
	 */
	ICBC_B2B(5),

	/**
	 * 招商银行B2C
	 */
	CMBCHINA_B2C(6),
	
	/**
	 * 招商银行B2B
	 */
	CMBCHINA_B2B(7),
	
	/**
	 * 建设银行B2C
	 */
	CCB_B2C(8),

	/**
	 * 建设银行B2B
	 */
	CCB_B2B(9),
	/**
	 * 交通银行B2C
	 */
	BOCO_B2C(10),
	/**
	 * 交通银行B2B
	 */
	BOCO_B2B(11),
	
	/**
	 * 兴业银行B2C
	 */
	CIB_B2C(12),
	/**
	 * 兴业银行B2B
	 */
	CIB_B2B(13),
	
	/**
	 * 中国民生银行B2C
	 */
	CMBC_B2C(14),

	/**
	 * 中国民生银行B2B
	 */
	CMBC_B2B(15),
	
	/**
	 * 光大银行B2C
	 */
	CEB_B2C(16),
	/**
	 * 光大银行B2B
	 */
	CEB_B2B(17),

	/**
	 * 中国银行B2C
	 */
	BOC_B2C(18),

	/**
	 * 中国银行B2B
	 */
	BOC_B2B(19),

	/**
	 * 平安银行B2C
	 */
	SZPA_B2C(20),
	/**
	 * 平安银行B2B
	 */
	SZPA_B2B(21),

	/**
	 * 中信银行B2C
	 */
	ECITIC_B2C(22),

	/**
	 * 中信银行B2B
	 */
	ECITIC_B2B(23),
	
	/**
	 * 深圳发展银行B2C
	 */
	SDB_B2C(24),
	/**
	 * 深圳发展银行B2B
	 */
	SDB_B2B(25),

	/**
	 * 广发银行B2C
	 */
	GDB_B2C(26),
	/**
	 * 广发银行B2b
	 */
	GDB_B2B(27),

	/**
	 * 上海银行B2C
	 */
	SHB_B2C(28),
	/**
	 * 上海银行B2B
	 */
	SHB_B2B(29),

	/**
	 * 上海浦东发展银行B2C
	 */
	SPDB_B2C(30),
	/**
	 * 上海浦东发展银行B2B
	 */
	SPDB_B2B(31),
	
	/**
	 * 华夏银行B2C
	 */
	HXB_B2C(32),
	
	/**
	 * 华夏银行B2B
	 */
	HXB_B2B(33),

	/**
	 * 北京银行B2C
	 */
	BCCB_B2C(34),

	/**
	 * 北京银行B2B
	 */
	BCCB_B2B(35),

	/**
	 * 中国农业银行B2C
	 */
	ABC_B2C(36),
	
	/**
	 * 中国农业银行B2B
	 */
	ABC_B2B(37),

	/**
	 * 中国邮政储蓄银行B2C
	 */
	PSBC_B2C(38),
	/**
	 * 中国邮政储蓄银行B2B
	 */
	PSBC_B2B(39),
	/**
	 * 北京农商银行B2C
	 */
	BJRCB_B2C(40),
	/**
	 * 北京农商银行B2B
	 */
	BJRCB_B2B(41),
	/**
	 * 上海农村商业银行B2C
	 */
	SRCB_B2C(42),
	/**
	 * 上海农村商业银行B2B
	 */
	SRCB_B2B(43),
	/**
	 * 杭州银行B2C
	 */
	HZBANK_B2C(44),
	/**
	 * 杭州银行B2B
	 */
	HZBANK_B2B(45),
	/**
	 * 宁波银行B2C
	 */
	NBCB_B2C(46),
	/**
	 * 宁波银行B2B
	 */
	NBCB_B2B(47),
	
	/**
	 * 直连分期付款
	 */
	CFL(48),

	/**
	 * 直连银联钱包
	 */
	UPOP(49),
	
	/**
	 * 直连账户支付
	 */
	ZHZF(50),

	/**
	 * 直连京东支付
	 */
	JD(51),
	
	/**
	 * 直连绑卡支付
	 */
	BK_ZF(52),

	/**
	 * 直连QQ钱包
	 */
	QQ(53),
	
	/**
	 * 预授权支付
	 */
	YSQ(54),

	/**
	 * 担保分期
	 */
	DBFQ(55),
	
	/**
	 * 扫码一键直连
	 */
	NC_ATIVE_SCAN(56);
	
	private final int value;
	
	DirectPayType(int value){
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public static DirectPayType getDirectPayType(int value){
		DirectPayType[] types = values();
		for(DirectPayType type : types){
			if(type.value == value){
				return type;
			}
		}
		return NONE;
	}



	public static boolean isDirectEbank(String directPayName){
		if(StringUtils.isEmpty(directPayName)){
			return false;
		}
		if(DirectPayType.NONE == changeDirectTypeEnum(directPayName)){
			return false;
		}
		DirectPayType directPayType = DirectPayType.valueOf(directPayName);
		if(directPayType != null && directPayType.value()>=4 && directPayType.value()<= 47){
			return true;
		}else{
			return false;
		}
		
	}
	public static boolean notCorrectDirectType(String directPayName){
		if(DirectPayType.NONE == changeDirectTypeEnum(directPayName)){
			return true;
		}
		return false;
	}
	public static DirectPayType changeDirectTypeEnum(String directPayName){
		try{
			return DirectPayType.valueOf(directPayName);
		}catch (Throwable e){
			return DirectPayType.NONE;
		}
	}

	/**
	 * 判断网银直连的银行账户类型
	 * @param directPayName
	 * @return
	 */
	public static BankAccountTypeEnum getEbankDirectAccountType(String directPayName) {
		if (!isDirectEbank(directPayName)) {
			return null;
		}
		if (directPayName.endsWith("_B2B")) {
			return BankAccountTypeEnum.B2B;
		} else if (directPayName.endsWith("_B2C")) {
			return BankAccountTypeEnum.B2C;
		} else {
			return null;
		}

	}

}
