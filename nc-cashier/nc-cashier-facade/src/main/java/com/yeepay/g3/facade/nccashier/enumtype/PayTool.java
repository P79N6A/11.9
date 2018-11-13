package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * 产品类型
 * 
 * @author：peile.fan
 * @since：2016年10月18日 下午6:12:45
 * @version:
 */
public enum PayTool {

	NCPAY("1", "一键支付"),

	SCCANPAY("2", "扫码支付"),

	EANK("3", "网银支付"),

	/**
	 * 钱包支付（SDK）
	 */
	EWALLET("4", "钱包支付"),
	
	CFL("5","分期付款"),
	
	MSCANPAY("6","商家扫码"),

	WECHAT_OPENID("7","公众号支付"),
	
	/**
	 * 会员支付
	 */
	ZF_ZHZF("8", "会员支付"),

	/**
	 * 钱包支付（H5）
	 */
	EWALLETH5("9", "钱包支付H5"),
	
	/**
	 * 非银行卡支付
	 */
	FYHK_FYHKZF("10", "非银行支付"),
	
	/**
	 * 支付宝生活号
	 */
	ZFB_SHH("11", "支付宝生活号"),
	
	/**
	 * 绑卡支付
	 */
	BK_ZF("12", "绑卡支付"),
	
	YHKFQ_ZF("13", "银行卡分期"),
	/**
	 * 预授权支付
	 */
	YSQ("14","预授权"),
	
	/**
	 * 会员支付
	 */
	GRHYZF("15", "个人会员支付"),

    /**
     * 担保分期
     */
    DBFQ("16","担保分期"),

	/**
	 * 微信小程序支付(命名有误，包括分线上和线下)
	 */
	XCX_OFFLINE_ZF("17","微信小程序支付"),

	/**
	 * 分期易
	 */
	ZF_FQY("18","分期易"),

	/**
	 * 充值网银
	 */
	LOAD_NET("19","充值网银"),


	/**
	 * 充值代付汇入
	 */
	LOAD_REMIT("20","充值代付汇入");



	private String value;

	private String description;

	private PayTool(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static PayTool of(final String value) {
		for (PayTool sce : PayTool.values()) {
			if (sce.value != null && sce.value.equals(value)) {
				return sce;
			}
		}
		return null;
	}

	/**
	 * 判断是否无可用的payTool
	 * @param stringBuilder 将可用的payTool进行拼接，一键除外(需单独判断限额)
	 * @return true if no payTool is available
	 */
	public static boolean isPayToolUnavailable(StringBuilder stringBuilder) {
		return stringBuilder.indexOf(PayTool.EANK.name()) < 0 &&
				stringBuilder.indexOf(PayTool.SCCANPAY.name()) < 0 &&
				stringBuilder.indexOf(PayTool.CFL.name()) < 0 &&
				stringBuilder.indexOf(PayTool.ZF_ZHZF.name()) < 0 &&
				stringBuilder.indexOf(PayTool.FYHK_FYHKZF.name()) < 0 &&
				stringBuilder.indexOf(PayTool.ZF_FQY.name()) < 0;
	}

}
