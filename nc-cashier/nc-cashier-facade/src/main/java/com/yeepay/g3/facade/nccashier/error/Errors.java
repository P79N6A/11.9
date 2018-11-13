package com.yeepay.g3.facade.nccashier.error;

import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误
 * 
 * @author yang.liu
 * @date 2014-5-13
 */
public enum Errors {
	
	/**
	 * 3100000打头
	 * 系统类* /
	 */
	SYSTEM_EXCEPTION("3100001", "系统异常，请稍后重试"),
	
	PAY_REQUEST_NULL("3100002", "支付请求不存在"),
	
	PAY_RECORD_NULL("3100003", "支付记录不存在"),
	
	THRANS_FINISHED("3100004", "此交易已完成"),
	
	THRANS_EXP_DATE("3100005", "此交易已过有效期，请重新下单"),

	SUPPORT_BANKLIST_FAILED("3100006", "暂时无法获取支持列表，请稍后重试"),
	
	SMS_SEND_FRILED("3100007", "短信发送失败,请重试"),
	
	CANT_REVERSE("3100008", "非成功的交易不能发起冲正"),
	
	REPEAT_ORDER("3100009", "已下单，请勿重复支付"),
	
	BUSINESS_NOT_SUPPORT("3100010", "业务方暂不支持"),

	SYSTEM_TOKEN_MISS("3100011", "TOKEN无效"),
	
	ORDER_SUCCESS("3100012", "支付成功，请勿重新支付"),

	ENCODE_ERROR("3100013", "加密失败"),

	DECODE_ERROR("3100014", "解密失败"),

	SECURITY_ERROR("3100015", "验签失败"),
	
	SYSTEM_INPUT_EXCEPTION("3100016","业务参数有误"),
	
	CASHIER_CONFIG_NULL("3100017","收银台模板不存在"),
	
	G3_BANK_CONVERT_MAP_NULL("3100018","三代统一银行转码配置不存在"),
	REQUEST_NOT_SAME("3100019","支付请求ID不一致"),
	NOT_OPEN_ORDER_ORIGIN("3100020","未开通微信相应支付来源"),
	
	NOT_PAY_TYPE_ERROR("3100021","请根据页面提示选择正确的扫码工具"),

	NOT_OPEN_PRODUCT_ERROR("3100022","商户未开通此支付方式，请返回或联系客服"),
	
	ORDER_CANCEL_ERROR("3100023","支付超时，请返回商户重新支付，若产生重复扣款，会第一时间退回您的银行账户"),
	//只有快捷支付时候，金额超出全部限额
	AMOUNT_OVER_LIMIT("3100024","当前订单金额较大，已超出银行单笔支付限额，建议返回商户重新下单"),
	
	ORDER_RISK_ERROR("3100025","交易存在风险，请联系商户重新下单"),
	//区分反查哪个订单系统
	ORDER_SYS_NO("3100026","订单方编号错误"),
	
	OPR_CANCELED_ERROR("3100027","订单已撤销，请返回商户重新支付"),
	
	SIGN_ERROR("3100028", "验签失败"),
	
	TIMESTAMP_OUT_OF_EXPIRE_DATE("3100029", "时间戳过期"),
	
	/**账户支付 商户权限校验失败**/
	MERCHANT_PERMISSION_INVALID("3100030","此账户无权限"),
	
	MERCHANT_TRADE_PASSWORD_INVALID("3100031","交易密码错误"),

	// 无可用的支付方式
	NO_PAYTYPE_AVAILABLE_ERROR("3100032","收银台异常，工作人员正在抢修，请联系客服"),
	
	/**收银台定制化，商户未选择定制的收银台**/
	MERCHANT_CASHIER_NOT_CUSTOMIZED("3100033", "商户未选择定制化收银台"),
	
	NOT_OPEN_BIND_CARD("3100034", "商户未开通绑卡功能"),
	
	PERIOR_NOT_SUPPPORT("3100035", "不支持此期数"),
	
	ORDER_STATUS_INVALID("3100036", "订单状态不合法"),
	
	LESS_THAN_MIN_LIMIT("3100037", "订单金额低于银行最低限额"),

	OAUTH_URI_NOT_CONFIG_EXCEPTION("3100038", "商户未向易宝报备微信授权信息"),
	
	BROWSER_NOT_SUPPORT("3100039", "当前浏览器不支持"),
	NOT_GET_LOCK("3100040", "当前系统繁忙，请稍后重试"),

	NOT_OPEN_AUTH_PAY_ERROR("3100041","商户未开通授权支付，请返回或联系客服"),

	ORDER_TYPE_IS_PREAUTH("3100042", "该类订单只能做预授权"),


	QUERY_BANK_LIST_ERROR("3100043","查询支持的银行列表失败"),

	CFL_EASY_CARD_TYPE_ERROR("3100044","分期易只支持贷记卡"),

	CFL_EASY_BANK_CODE_NOT_SUPPORT("3100045","分期易不支持的银行"),

	CFL_EASY_PERIOD_NOT_SUPPORT("3100046","分期易不支持的期数"),

	CFL_EASY_AMOUNT_NOT_SUPPORT("3100047","分期易不支持的金额"),

	CFL_EASY_BIND_ID_NOT_SUPPORT("3100048","分期易绑卡信息有误"),
	
	LARGER_THAN_MAX_LIMIT("3100049", "订单金额大于银行最低限额"),

	REDIRECT_URL_INVALID("3100049","跳转地址有误"),

	ONLYCAEDNO_NOT_SUPPORT("3100050","不支持仅卡号支付"),

	/**
	 * 3200000打头
	 * 卡信息错误类
	 */
	INPUT_PARAM_NULL("3200001", "输入参数错误"),

	CARD_INFO_ERROR("3200002", "卡信息有误，请核对"),
	
	SYSTEM_BINDID_NULL("3200003", "绑卡无效，请换卡支付"),
	
	SUPPORT_BANK_FAILED("3200004", "该卡暂不支持，请查看支持银行列表"),
	
	INVALID_BANK_CARD_NO("3200005", "卡号不合法，请修改卡号"),
	
	NAME_NOT_NULL("3200006", "姓名不能为空"),

	ID_NOT_NULL("3200007", "身份证不能为空"),

	PASSWORD_NOT_NULL("3200008", "密码不能为空"),

	CVV2_NOT_NULL("3200009", "cvv2不能为空"),

	VALIDATE_NOT_NULL("3200010", "有效期不能为空"),

	PHONE_NOT_NULL("3200011", "手机号不能为空"),

	CARD_NOT_NULL("3200012", "卡号不能为空"),

	VERIFYCODE_MISS("3200013", "请输入验证码"),

	INVALID_NAME("3200014", "姓名不合法，请重新输入"),

	INVALID_IDNO("3200015", "身份证号不合法，请重新输入"),

	INVALID_PASSEORD("3200016", "密码不合法，请重新输入"),

	INVALID_CVV2("3200017", "cvv2不合法，请重新输入"),

	INVALID_VALIDATE("3200018", "有效期不合法，请重新输入"),

	INVALID_PHONE("3200019", "手机号不合法，请重新输入"),
	
	INPUT_SAMEPERSONINFOERROR("3200020", "证件号或姓名未传，请联系商家"),

	ID_TYPE_NOT_NULL("3200021", "证件类型不能为空"),
	
	SYSTEM_SIGNRID_NULL("3200022", "签约关系无效，请换卡支付"),

	PASS_BINDID_ERROR("3200023", "绑卡id格式错误"),

	PASS_BINDID_NOT_MATCH("3200024", "绑卡ID与外部用户信息不符"),


	/**
	 * 3300000打头
	 * 操作错误类
	 */
	GET_SMS_FIRST("3300001", "请先获取短验"),

	GET_SMS_AGAIN("3300002", "信息变更,请重新获取短验"),
	
	PLZ_DEBIT_PAY("3300003", "请使用储蓄卡支付"),

	PLZ_CREDIT_PAY("3300004", "请使用信用卡支付"),
	
	GET_SMS_FREQUENT("3300005", "短验获取频繁，请稍后再试"),
	
	CARD_INFO_NOT_SAME("3300006","非本人支付，请联系客服"),
	
	OUT_BIND_CARD_LIMIT("3300007","绑卡超限，请联系客服"),

	EBANK_DIRECT_PAY_INFO_NULL("3300008","请选择银行"), //网银支付未输入银行编号或账户类型
	EBANK_B2B_CLIENT_ID_NULL("3300009","当前银行使用企业支付需要提供客户ID"), //网银B2B未输入客户号

	ACCOUNT_PAY_USER_ACCOUNT_NULL("3300010","请输入账户名"),
	ACCOUNT_PAY_TRADE_PASSWORD_NULL("3300011","请输入交易密码"),
	ACCOUNT_PAY_CAPTCHA_NULL("3300012","请输入验证码"),
	ACCOUNT_PAY_CAPTCHA_ERROR("3300013","请输入正确的验证码"),
	
	DIRECT_PAY_TYPE_ERROR("3300014","直连类型错误"),
	SMS_VERIFY_FAILED("3300015", "短验确认失败，请重发短验"),
	SMS_INPUT_ERROR("3300016", "短验输入错误，请确认后重新输入"),
	DO_NOT_PERMIT_SMS("3300017", "短验输入错误，请确认后重新输入"),
	
	BANK_AND_CARD_INFO_MATCH("3300018", "输入卡号与所选银行不匹配"),
	
	INSTALLMENT_PAY_EXCEPTION("3300019", "支付异常，请重新下单"),
	
	PREAUTH_COMPLETE_STATUS_ERROR("3300020", "无法进行当前预授权操作"),
	
	NON_PREAUTH_ORDER("3300021", "非预授权类型的订单无法进行预授权相关的操作"),
	
	/**
	 * 3400000
	 * 银行类
	 */
	
	AMOUNT_OUT_RANGE("3400001", "订单金额超限，请换卡支付"),
	CASHIER_CONFIG_BANKS_NULL("3400002","收银台模板支持的银行为空"),
	ORDER_STATUS_UNKNOWN("3400003", "暂未获取到支付结果，请查询或稍后再试"),
	ORDER_STATUS_REVERSE("3400004", "支付失败，如有扣款系统将自动退款"),
	AMOUNT_HIGHER_MAX("3400005", "订单金额超限，请使用其他支付方式"),
	AMOUNT_LOWER_MIN("3400006", "订单金额过低，请使用其他支付方式"),


	/**
	 * 3500000
	 * FE错误码
	 */
     FE_EXCEPITON_1("3500003", "系统异常，请更换其他支付方式或稍后再试"),
     FE_EXCEPITON_2("3500004", "请求参数不合法，请联系客服"),
	 FE_EXCEPITON_3("3500005", "重复请求，请重新发起支付请求"),
	 FE_EXCEPITON_4("3500006", "请求异常，请重新发起支付请求"),
	 FE_EXCEPITON_5("3500007", "请求超时，请重新发起支付请求或稍后再试"),
	 FE_EXCEPITON_7("3500009", "订单已经失败，请重新发起支付请求"),
	 FE_EXCEPITON_8("3500010", "订单已经成功"),
	 FE_EXCEPITON_9("3500011", "订单正在支付中，请稍后再试或联系客服"),
	 FE_EXCEPITON_10("3500012", "订单已经失效，请重新发起支付请求"),
	 FE_EXCEPITON_11("3500013", "订单不存在，请重新发起支付请求"),
	 FE_EXCEPITON_12("3500014", "订单状态异常，请重新发起支付请求"),
	 FE_EXCEPITON_13("3500015", "订单已过期，请更换其他支付方式或重新下单"),
	
	/**
	 * 用户中心
	 */
	USER_CENTER_EXCEPTION("3600001", "不能请求用户中心授权"),

	/**
	 * 3700000对接订单处理器的三个url的参数
	 */
	REQUEST_TERMINAL_NO_SUPPORT("3700001", "请使用微信浏览器打开"),

	REQUEST_INFO_IS_ERROR("3700002", "请求信息被篡改,请重新下单"),

	REQUEST_MERCHANT_NO_ERROR("3700003", "请求商户编号有误"),

	BIZ_SYS_CONFIG_PARAM("3700004","反查订单系统的业务方配置获取异常"),
	
	/**
	 * 商户平台
	 */
	MERCHANT_PLATFORM_EXCEPTION("3800001", "系统异常，请稍后再试"),
	MERCHANT_PLATFORM_ACCOUNT_NAME_NOEXSIT("3800002", "账户名不存在，请输入正确的账户名"),

	/** 三代会员子系统 */
	MEMBER_INVALID_EXCEPTION("3900001", "会员状态不合法");

	private final String code;

	private final String msg;

	Errors(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 获取错误编码
	 * 
	 * @return 错误编码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 获取错误描述信息
	 * 
	 * @return 描述信息
	 */
	public String getMsg() {
		return msg;
	}
	
	public static Errors getErrorsByCode(String code){
		for(Errors o : Errors.values()){
			if(code.equals(o.getCode())){
				return o;
			}
		}
		return SYSTEM_EXCEPTION;
	}
	
	
	public static final Map<String, Errors> DEFAULT_SYSERROR_MAPPING = new HashMap<String, Errors>();
	
	static {

		DEFAULT_SYSERROR_MAPPING.put(SysCodeEnum.FRONTEND.name(),Errors.FE_EXCEPITON_1);
		DEFAULT_SYSERROR_MAPPING.put(SysCodeEnum.NCPAY.name(), Errors.SYSTEM_EXCEPTION);
		
	}
	
	/**
	 * 获取默认的错误码
	 * @param sysCode
	 * @return
	 */
	public static Errors getDefaultErrorCode(String sysCode){
		Errors defaultErrorCode = DEFAULT_SYSERROR_MAPPING.get(sysCode);
		if(defaultErrorCode==null){
			return Errors.SYSTEM_EXCEPTION;
		}
		return defaultErrorCode;
	}
}
