package com.yeepay.g3.facade.nccashier.constant;

import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	// 5min
	public static final int NCCASHIER_REQUEST_REDIS_TIME_LIMIT = 5 * 60 * 1000;
	public static final int NCCASHIER_NEEDBANKCARDKEY_REDIS_TIME_LIMIT = 5 * 60 * 1000;
	public static final int NCCASHIER_NEEDBANKNEEDITEM_REDIS_TIME_LIMIT = 5 * 60 * 1000;
	public static final int NCCASHIER_USERACCOUNT_REDIS_TIME_LIMIT = 5 * 60 * 1000;
	public static final int NCCASHIER_SMS_SEND_TYPE_LIMIT = 5 * 60 * 1000;
	public static final int NCCASHIER_SMS_SEND_TIME_LIMIT = 5 * 60 * 1000;
	public static final int NCCASHIER_CARD_LIMIT_TIME = 5 * 60 * 1000;
	public static final int NCCASHIER_EBANK_SUPPORT_BANKS_REDIS_TIME_LIMIT = 5 * 60 * 1000;
	public static final int NCCASHIER_CREATEPAYMENT_FAILUREMAP_REDIS_TIME_LIMIT = 5 * 60 * 1000;
	public static final int CASHIER_CUSTOMIZED_LAYOUT_SELECT_INFO_TIME = 5 * 60 * 1000;
    public static final int NCCASHIER_BANK_LIMIT_TIME = 5 * 60 * 1000;
	// 1min
	public static final int ONE_MINUTE = 60 * 1000;
	// 5min
	public static final int FIVE_MINUTE = 5 * 60 * 1000;
	// 30min
	public static final int HALF_HOUR = 30 * 60 * 1000;

	//绑卡下单失败map
	public static final String NCCASHIER_CREATEPAYMENT_FAILUREMAP = "NCCASHIER_CREATEPAYMENT_FAILUREMAP_";
	public static final String NCCASHIER_CONFIRMPAY_TIME_KEY = "NCCASHIER_CONFIRMPAY_TIME_";
	public static final String NCCASHIER_BINDCARDSLIST = "NCCASHIER_BINDCARDSLIST_";
	public static final String NEEDBANKCARDDTOKEY = "NCCASHIER_CARDSUPPORT_";// 缓存补充项NeedBankCardDTO的key

	public static final String NCCASHIER_USERACCOUNT_REDIS_KEY = "NCCASHIER_USERACCOUNT_";
	public static final String NCCASHIER_SMS_TYPE_KEY = "NCCASHIER_SMS_TYPE_";
	public static final String NCCASHIER_BIND_NEEDITEM_KEY = "NCCASHIER_BIND_NEEDITEM_";
	public static final String NCCASHIER_SMS_SEND_TIME_KEY = "NCCASHIER_SMS_TIME_";
	
	// 获取支付宝主扫二维码的时间控制
	public static final String NCCASHIER_ALIPAY_QRCODE_REQUEST_KEY = "NCCASHIER_ALIPAY_QRCODE_REQUEST_";
	// 获取微信主扫二维码的时间控制
	public static final String NCCASHIER_WECHAT_QRCODE_REQUEST_KEY = "NCCASHIER_WECHAT_QRCODE_REQUEST_";
	// 银联二维码的缓存KEY
	public static final String NCCASHIER_UNION_QRCODE_REQUEST_KEY = "NCCASHIER_UNION_QRCODE_REQUEST_";

	public static final Map<PayTypeEnum, String> QRCODE_REQUEST_REDIS_PAYTYPE_KEY_MAP = new HashMap<PayTypeEnum, String>();
	static{
		QRCODE_REQUEST_REDIS_PAYTYPE_KEY_MAP.put(PayTypeEnum.WECHAT_ATIVE_SCAN, NCCASHIER_WECHAT_QRCODE_REQUEST_KEY);
		QRCODE_REQUEST_REDIS_PAYTYPE_KEY_MAP.put(PayTypeEnum.ALIPAY, NCCASHIER_ALIPAY_QRCODE_REQUEST_KEY);
		QRCODE_REQUEST_REDIS_PAYTYPE_KEY_MAP.put(PayTypeEnum.UPOP_ATIVE_SCAN, NCCASHIER_UNION_QRCODE_REQUEST_KEY);
	}
	
	// 微信主扫二维码/支付宝主扫二维码有效期1Min
	public static final int NCCASHIER_QRCODE_EXPIRE_TIME = 20; //20s
	
	//pc收银台二维码url的redis缓存key【避免重复下单】
	public static final String NCCASHIER_QRCODE_REDIS_KEY = "NCCASHIER_QRCODE_REDIS_KEY_";
	//pc收银台二维码url的redis缓存时间【缓存时间可配置，此处为默认值600s】
	public static final int NCCASHIER_QRCODE_REDIS_EXPIRE = 600;
	//pc收银台二维码缓存时间的配置【配置中心的配置key】
	public static final String NCCASHIER_QRCODE_CONFIG_KEY = "NCCASHIER_QRCODE_EXPIRE_CONFIG";

	//网银支持的银行规则缓存
	public static final String NCCASHIER_EBANK_SUPPORT_BANKS_REDIS_KEY = "NCCASHIER_EBANK_SUPPORT_BANKS_";

	public static final String BINDCARDNULL = "NULL";// 绑卡列表为空时放入redis的值
	public static final String STANDARD_BANKCODE = "OL_NCCONFIG_BANK_CODE_TRANSFER";
	public static final String NCCASHIER_MQ_QUEUE_NAME = "nccashier";
	public static final String JOINLY = "JOINLY";//联名商户账户——商户类型
	public static final String YIBAO = "YIBAO";//易宝三代会员——商户类型
	public static final String CARDINFO_LIMIT_BIZ = "OL_NCCASHIER_SAME_PERSON_";//同人限制开关
	public static final String ALLOW_FIRST_PAY_BIZ = "OL_NCCASHIER_ALLOW_FIRST_PAY_BIZ_";//达到绑卡上限是否允许新增卡
	public static final String ON = "ON";
	public static final String OFF = "OFF";
	public static final String SMSSEND_NO = "OL_NCCASHIER_SENDSMS_NO";//发送语音验证码的电话号码
	public static final String CARD_LIMIT_INFO = "OL_NCCASHIER_CARD_LIMIT_INFO";//卡账户限制信息redis缓存的key值
	public static final String CASHIER_CUSTOMIZED_LAYOUT_SELECT_INFO = "OL_NCCASHIER_CUSTOMIZED_LAYOUT_SELECT_INFO";//收银台定制化查询商户选择模版信息redis缓存的key值
	public static final String CARD_BIND_LIMIT_INFO = "OL_NCCASHIER_BINDCARD_LIMIT_";//卡账户限制信息redis缓存的key值
	public static final String SAME_PERSON_SWITCH = "SAME_PERSON_SWITCH";
	public static final String CHANGE_CARD_SWITCH = "CHANGE_CARD_SWITCH";
	public static final String TRADE_CALLBACK_WAY = "OL_NCCASHIER_CALLBACK_WAY_";
    public static final String OL_NCCASHIER_BANK_LIMIT = "OL_NCCASHIER_BANK_LIMIT";//收银台PC限额信息缓存
	/**
	 * 银行卡分期，商户支持的银行及期数缓存KEY
	 */
	public static final String BANK_CARD_INSTALLMENT_SUPPORT_BANK_KEY = "OL_NCCASHIER_BANK_INSTALLMENT_BANKS";

	/**
	 * 获取商户等级信息,缓存key
	 */
	public static final String NCCASHIER_MERCHANT_LEVEL_REDIS_KEY = "NCCASHIER_MERCHANT_LEVEL_";

	public static final String CONFIRM_PAY_FLAG= "CONFIRM_PAY_";
	
	public static final String FRONT_NOTIFY_KEY="FRONT_NOTIFY_";
	public static final String MERCHANT_SAME_PERSON_SET = "OL_CASHIER_MERCHANT_SAMEPERSON_";
	public static final String BINDCARDS = "OL_NCCASHIER_BINDCARDS_";
	public static final String MISS_NEEDITEM_BIND = "OL_NCCASHIER_MISS_CARDINFO_";
	public static final String VALIDE = "VALIDE";
	public static final String INVALIDE = "INVALIDE";
	public static final String SUCCESS = "SUCCESS";
	public static final String BIND = "BIND";
	public static final String MERCHANT_LIMIT_TYPE = "NONE";//商户限制类型为普通
	public static final String PAY_FAIL_OWNER_REASONS = "OL_NCCASHIER_PAY_FAIL_OWNER_REASONS";//因支付身份导致交易失败的错误码
	public static final String NCCASHIER_USERREQUEST_REDIS_KEY = "NCCASHIER_USERREQUEST_";//获取用户请求信息的key
	public static final String FAIL = "FAIL";

	/**
	 * pc扫码标识 redis
	 */
	public static final String SCAN_SIGN_KEY = "PC_SCAN_SIGN_";
	
	public static final int SCAN_SIGN_TIMEOUT = 60 * 1000;
	
	/**
	 * PC端二维码已扫标识
	 */
	public static final String PC_QR_CODE_BE_SCANNED_SIGN = "SUCCESS";
	
	public static final String PC_PAY_RESULT_QUERY_STATE_SIGN = "SUCCESS";

	
	public static final String FINAL_PAY_RESULT = "FINAL_PAY_RESULT";
	
	public static final int FINAL_PAY_RESULT_TIMEOUT  = 60 * 1000;
	
	/**
	 * 支付处理器redis监听 服务器端超时时间【扫码标识、支付结果可查标识】
	 */
	public final static String LISTENER_SERVER_TIMEOUT = "OL_NCCASHIER_LISTENER_SERVER_TIMEOUT";
	public static final String INSTALLMENT_DIRECT = "installmentDirect";//分期扫码支付
	public static final Object INSTALLMENT_QR_CODE_BE_SCANNED_SIGN = "INSTALLMENT_SCAN_SUCCESS";//成功扫描分期支付二维码
	public static final String INSTALLMENT_DIRECT_SCAN = "OL_NCCASHIER_INSTALLMENT_SCAN_";
	
	public static final String VALIDATE_CODE_REDIS_KEY = "OL_NCCASHIER_VALIDATE_CODE";

	/**
	 * 绑卡记录（防止重复调用绑卡）
	 */
	public static final String BINDCARD_RECORD = "OL_NCCASHIER_LISTENER_SERVER_TIMEOUT";
	public static final int BINDCARD_RECORD_TIMEOUT = 10*60*1000;
	
	/**
	 * 设置同人限制值定时任务配置开关
	 */
	public static final String AUTO_SETSAMEPERSON_SETS = "OL_NCCASHIER_AUTO_SETSAMEPERSON_SETS";
	/**
	 * 设置同人限制值定时任务配置的时间间隔，单位为分钟
	 */
	public static final String AUTO_SETSAMEPERSON_TIME_LENGTH = "time_length";
	/**
	 * 设置同人限制值定时任务配置每次处理的任务数
	 */
	public static final String AUTO_SETSAMEPERSON_RECORDS = "deal_record_number";

	/**
	 * 该商户支持的开通信息查询来自USER_REQUEST表
	 */
	public static final String MERCHANT_CONFIG_FROM_USER_REQUEST="USER_REQUEST";

	/**
	 * 该商户支持的开通信息查询来自PAYMENT_REQUEST表
	 */
	public static final String MERCHANT_CONFIG_FROM_PAYMENT_REQUEST="PAYMENT_REQUEST";
	
	
	public static final String CUSTOMER_LEVEL_V = "V";
	/**
	 * 获取用户手续费传给计费中心的支付方式
	 */
	public static final String PAY_WAY_TO_CALL_FEE ="ALL";
	/**
	 * 获取用户手续费传给计费中心的银行接口编码
	 */
	public static final String BANK_INTER_NUMBER_TO_CALL_FEE ="CASHIER";
	/**
	 * 获取用户手续费传给计费中心的支付产品
	 */
	public static final String PAY_PRODUCT_TO_CALL_FEE ="ALL";
	
	public static final String DS_USER_FEE_ITEM = "DSBZB";
	/**
	 * SDK收银台支付工具选择的地址
	 */
	public static final String SDK_PAY_URL = "https://cash.yeepay.com/sdk/payToolsChoose";

	

	/**
	 * 查大算的接口
	 */
	public static final String DS_SYS_INTERFACE = "DS";

	/**
	 * 查2代的接口
	 */
	public static final String G2NET_SYS_INTERFACE = "G2NET";

	/**
	 * 查标准接口
	 */
	public static final String STANDARD_SYS_INTERFACE = "STANDARD";

	/**
	 * 不校验
	 */
	public static final String ORDER_REFFER_NONE = "NONE";

	/**
	 * 校验但是不走白名单
	 */
	public static final String ORDER_REFFER_NOT_NONE = "NOT_NONE";

	/**
	 * 校验并且走白名单
	 */
	public static final String ORDER_REFFER_NOT_NONE_AND_WHITER = "NOT_NONE_AND_WHITE";

	/**
	 * 预路由结果成功状态
	 */
	public static final String PRE_ROUTE_STATUS_SUCCESS = "1";
	/**
	 * 场景类型扩展（jsapiH5：微信内部H5通道）
	 */
	public static final String PRE_ROUTE_SCENE_TYPE_EXT_JSAPIH5 = "jsapiH5";
	/**
	 * 场景类型扩展（normal：正常通道）
	 */
	public static final String PRE_ROUTE_SCENE_TYPE_EXT_NORMAL = "normal";
	
	/**
	 * 图片文件后缀
	 */
	public static final String DEFAULT_IMG_FILE_FORMAT = "png";
	
	/**
	 * 定制在wap支付成功页展示广告的商编
	 */
	public static final String SHOW_ADVETISEMENT_MERCHANT_NO = "10013605587";
	/**
	 * 商户收银台模版定制化，获取商户模版文件，redis缓存的key值
	 */
	public static final String MERCHANT_CASHIER_CUSTOMIZED_LESS_FILE = "MERCHANT_CASHIER_CUSTOMIZED_LESS_FILE_";
	/**
	 * 商户收银台模版定制化，获取商户logo文件，redis缓存的key值
	 */
	public static final String MERCHANT_CASHIER_CUSTOMIZED_LOGO_FILE = "MERCHANT_CASHIER_CUSTOMIZED_LOGO_FILE_";
	/**
	 * 商户收银台模版定制化文件模版类型
	 */
	public static final String MERCHANT_CASHIER_CUSTOMIZED_LESS_FILE_TYPE = "less";
	public static final String MERCHANT_CASHIER_CUSTOMIZED_JS_FILE_TYPE = "js";
	public static final String MERCHANT_CASHIER_CUSTOMIZED_LOGO_FILE_TYPE = "logo";
	/**
	 * 商户收银台模版定制化，模版文件存储默认路径
	 */
	public static final String MERCHANT_CASHIER_CUSTOMIZED_FILE_UPLOAD_ROOT_PATH = "/apps/data/resources";
	/**
	 * 商户收银台定制化，默认模版id
	 */
	public static final String MERCHANT_CASHIER_CUSTOMIZED_DEFAULT_LAYOUT_ID = "10000000000";
	//API收银台支付接口，接口返回结果的redis缓存key
	public static final String NCCASHIER_API_PAY_RESULT_KEY = "NCCASHIER_API_PAY_RESULT_KEY_";
	//API收银台支付接口，接口返回结果的redis缓存有效期
	public static final int NCCASHIER_API_PAY_RESULT_EXPIRE = 600;
	//API收银台支付接口，缓存时长统一配置key
	public static final String NCCASHIER_API_PAY_EXPIRE_CONFIG_KEY = "0L_NCCASHIER_API_PAY_EXPIRE_CONFIG";

	public static final int NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY_TIMEOUT  = 5 * 60 * 1000;

	public static final String NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY = "NCCASHIER_QUERY_PAY_RESUL_RECORD_INFO_REDIS_KEY_";

	public static final String NCCASHIER_MARKET_ACTIVITY_REDIS_KEY = "NCCASHIER_MARKET_ACTIVITY_REDIS_KEY_";
	
	public static final String DEFAULT_PP_PAY_RESULT_QUERY_INTERVAL_TIME = "1000";


	/**
	 * 敏感信息合规改造-数据库需双写字段-加密字段的后缀
	 */
	public static final String ENCRYPT_DATABASE_COLUMU_SUFFIX = "_ENCRYPT";
	/**
	 * 敏感信息加密存储的key，加密后左半部分
	 */
	public static final String AES_KEY_LEFT_PART = "Zu9F11i28SGqeZLlI/CRqE";

	/**
	 * 敏感信息合规改造-nccashier项目-配置集合的统一配置key
	 */
	public static final String NCCASHIER_ENCRYPT_CONFIG_MAP_KEY = "OL_NCCASHIER_ENCRYPT_CONFIG_MAP";

	/**
	 * 敏感信息合规改造-AES加密key-密文右半部分-统一配置field
	 */
	public static final String ENCRYPT_KEY_RIGHT_PART = "ENCRYPT_KEY_RIGHT_PART";

	/**
	 * 敏感信息合规改造-拆分存储加密key所使用的key（key的key）-统一配置field
	 */
	public static final String ENCRYPT_KEY_FOR_KEY = "ENCRYPT_KEY_FOR_KEY";

	/**
	 * 敏感信息合规改造-双写字段读取加密字段(新)的开关-统一配置field
	 * <p>off=读明文；on=读密文
	 */
	public static final String ENCRYPT_DB_COLUMN_QUERY_SWITCH = "ENCRYPT_DB_COLUMN_QUERY_SWITCH";

	/**
	 * 敏感信息合规改造-双写字段改为单写新字段开关-统一配置field
	 * <p>off=明文密文双写；on=只写密文
	 */
	public static final String ENCRYPT_DB_COLUMN_WRITE_SWITCH = "ENCRYPT_DB_COLUMN_WRITE_SWITCH";
	/**
	 * 绑卡支付基础产品码key
	 */
	public static final String BIND_CARD_BASIC_PRODUCT_CODE_KEY = "BK_BIND_CARD";
	/**
	 * NOP绑卡支付已开通
	 */
	public static final String BIND_CARD_PRODUCT_ENABLE = "ENABLE";
	/**
	 * 收银他请求NOP系统RequestSystem
	 */
	public static final String NOP_REQUEST_SYSTEM_NC_CASHIER = "NCCASHIER";
	/**
	 * 调用nop短信错误，前端需要进行错误提示，引导用户进行下一步操作
	 */
	public static final String SMS_SEND_ERROR = "smsSendError";
	public static final String SMS_VERIFY_ERROR = "smsVerifyError";
	public static final String SMS_INPUT_ERROR = "smsInputError";
	public static final String SMS_NOT_PERMIT = "smsNotPermitError";
	public static final String SMS_NOT_GET = "smsNotGetError";

	/**
	 * 收银台读写redis时间的日志阈值，统一配置key，单位：毫秒
	 */
	public static final String NCCASHIER_REDIS_TIME_THRESHOLD_KEY = "OL_NCCASHIER_REDIS_TIME_THRESHOLD_KEY";
	/**
	 * 一键支付首次和绑卡支付首次标识
	 */
	public static final String YJZF_FIRST_PAY = "YJZF_FIRST_PAY";
	public static final String BKZF_FIRST_PAY= "BKZF_FIRST_PAY";
	public static final String BKZF_FIRST_REDIS_KEY = "OL_NCCASHIER_BKZF_FIRST_REDIS_KEY_";
    /**
     * 微信h5高低配第四维度参数透传PP
     */
	public static final String WALLET_LEVEL_HIGH = "HIGH";
	public static final String WALLET_LEVEL_LOW = "LOW";
	public static final String WALLET_LEVEL_STANDARD = "STANDARD"; 
	
	public static final String ALL = "ALL";
	public static final String PAYER = "PAYER"; // 付款方
	public static final String PAYEE = "PAYEE"; // 收款方
	public static final String MERCHANT = "MERCHANT"; // 商户
	
	/**
	 * 银行卡分期补贴手续费模型的payProduct
	 */
	public static final String INSTALLMENT_ALLOWANCE_PAY_PRODUCT = "ZF_YHKFQ_ALLOWANCE";

	/** 定制了不需要展示易宝微信二维码的部分大商户商编 */
	public static final String MERCHANT_NO_NOT_NEED_QRCODE = "OL_NCCASHIER_MERCHANT_NO_NOT_NEED_QRCODE";
	public static final String ORDER_SYS_NO_NOT_NEED_QRCODE = "OL_NCCASHIER_ORDER_SYS_NO_NOT_NEED_QRCODE";

	/** 业务通道编码字段名（用于前置收银台，微信支付宝的开放支付及被扫支付） */
	public static final String SPECIFY_CHANNEL_CODES = "specifyChannelCodes";



	/** 报备费率（用于聚合支付，区分线上线下费率） */
	public static final String REPORT_FEE = "reportFee";

	/*订单父编号***可以理解为渠道编号(比如哆啦宝)编号 */
	public static final String PARENT_MERCHANT_NO = "parentMerchantNo";
	/**哆啦宝粉丝路由功能,只有微信公众号用到*/
	public static final String WX_REPORT_ID = "reportId";
	public static final String WX_FOCUS_APP_ID = "focusAppId";

	/** 区域编码字段名（用于银联二维码被扫） */
	public static final String AREA_CODE = "areaCode";
	
	/**
	* 消费类型:SALE消费、PREAUTH预授权，获取银行规则使用
	*/
	public static final String BNAK_RULE_CUSTYPE_SALE = "SALE";
	public static final String BNAK_RULE_CUSTYPE_PREAUTH = "PREAUTH";

	/** 验证码类型-短信 */
	public static final String VERIFY_CODE_TYPE_SMS="SMS";
	/** 验证码类型-语音 */
	public static final String VERIFY_CODE_TYPE_VOICE="VOICE";
	/** 验证码类型-无需验证 */
	public static final String VERIFY_CODE_TYPE_NONE="NONE";
	/** 提交补充项场景-请求验证码时 */
	public static final String SUPPLY_NEEDITEM_SCENE_VERITY="REQUEST_VERIFY";
	/** 提交补充项场景-确认支付时 */
	public static final String SUPPLY_NEEDITEM_SCENE_CONFIRM="CONFIRM_PAY";
	/** 提交补充项场景-无需补充项 */
	public static final String SUPPLY_NEEDITEM_SCENE_NONE="NONE";


	//担保分期 银行编码 以及对应期数
	public static final String OL_NCCASHIER_DBFQ_SUPPORT_BANK_PERIOD = "OL_NCCASHIER_DBFQ_SUPPORT_BANK_PERIOD";
	/** 担保分期 银行限额 */
	public static final String OL_NCCASHIER_DBFQ_BANK_LIMIT = "OL_NCCASHIER_DBFQ_BANK_LIMIT";
	/** 担保分期下单后，需跳转到的，通道提供的确认支付地址 */
	public static final String GUARANTEE_INSTALLMENT_PAY_URL ="payUrl";
	/** 担保分期下单后，确认支付地址的请求方法 */
	public static final String GUARANTEE_INSTALLMENT_PAY_METHOD ="method";
	/** 担保分期下单后，确认支付地址的请求编码 */
	public static final String GUARANTEE_INSTALLMENT_PAY_ENCODING ="encoding";
	/** 担保分期下单后，确认支付地址的请求参数 */
	public static final String GUARANTEE_INSTALLMENT_PAY_PARAM ="params";
	/** 担保分期，下单时的分期费率 */
	public static final String GUARANTEE_INSTALLMENT_SERVICE_CHARGE_RATE ="serviceChargeRate";
	/** 担保分期，下单获取的跳转信息，缓存key前缀 */
	public static final String GUARANTEE_INSTALLMENT_PAYMENT_CACHE ="OL_NCCASHIER_GUARANTEE_INS_CACHE_";

	//银行编码对应银行名称
	public static final String OL_NCCASHIER_BANKCODE_BANKNAME = "OL_NCCASHIER_BANKCODE_BANKNAME";

	public static final String TO_BANK = "TO_BANK";
	public static final String TO_PCCASHIER = "TO_PCC";
}
