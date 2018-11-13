package com.yeepay.g3.facade.nccashier.error;

import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.facade.nccashier.dto.ErrorCodeDTO;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * UPP系统的错误码，和错误描述信息
 * ErrorCode.java
 * @author litishi
 * @date 2013-5-9
 */
public class ErrorCode {

	private static final String NCPAYSYSCODE ="NCPAY";
	/**
	* 成功(内部使用，非对外错误码)
	*/
	public static final String SUCCESS="0";
	/**
	 * 系统错误
	 */
	public static final String E400000 = "400000";
	/**
	 * 平台代码无效
	 */
	public static final String E400001 = "400001";
	/**
	 * 请求参数为空
	 */
	public static final String E400002 = "400002";
	/**
	 * 无效的渠道号
	 */
	public static final String E400003 = "400003";
	/**
	 * 无效，或不支持的卡号
	 */
	public static final String E400004 = "400004";
	/**
	 * 重复的请求
	 */
	public static final String E400010 = "400010";
	/**
	 * 原交易订单不存在
	 */
	public static final String E400020 = "400020";
	/**
	 * 原交易订单状态不支持此请求
	 */
	public static final String E400021 = "400021";
	/**
	 * 原交易订单数据与当前请求不匹配
	 */
	public static final String E400022 = "400022";
	/**
	 * 生成请求报文失败
	 */
	public static final String E400030 = "400030";
	/**
	 * 解析应答报文失败
	 */
	public static final String E400031 = "400031";
	/**
	 * 与银行通讯失败
	 */
	public static final String E400032 = "400032";
	/**
	 * 渠道已关闭
	 */
	public static final String E400033 = "400033";
	/**
	 * 提交退款中心失败
	 */
	public static final String E400034 = "400034";
	/**
	 * 渠道路由规则关系为空
	 */
	public static final String E400035 = "400035";
	/**
	 * 路由规则为空
	 */
	public static final String E400036 = "400036";
	/**
	 * 该卡号对应的银行在卡表中不存在
	 */
	public static final String E400037 = "400037";
	/**
	 * 无效的卡类型
	 */
	public static final String E400038 = "400038";
	/**
	 * 路由银行编号映射错误
	 */
	public static final String E400039 = "400039";
	/**
	 * 取不到该渠道及银行对应的路由
	 */
	public static final String E400040 = "400040";
	
	/**
	 * 一分钟内同一手机号校验过频繁，请稍后再试
	 */
	public static final String E400080 = "400080";
	
	/**
	 * 支付记录不存在异常
	 */
	public static final String E400081 = "400081";
	/**
	 * 银行规则记录不存在异常
	 */
	public static final String E400082 = "400082";
	/**
	 * 风控处理结果不存在异常
	 */
	public static final String E400083 = "400083";
	/**
	 * 支付记录状态异常
	 */
	public static final String E400084 = "400084";
	/**
	 * 卡信息输入异常
	 */
	public static final String E400085 = "400085";
	/**
	 * 卡信息认证未通过异常
	 */
	public static final String E400086 = "400086";
	/**
	 * 卡种不支持异常
	 */
	public static final String E400087 = "400087";
	/**
	 * 支付订单不需要短信验证异常
	 */
	public static final String E400088 = "400088";
	/**
	 * 手机号码无记录异常
	 */
	public static final String E400089 = "400089";
	/**
	 * 无法获得短信内容异常
	 */
	public static final String E400090 = "400090";
	/**
	 * 短信验证码过期异常
	 */
	public static final String E400091 = "400091";
	/**
	 * 短信验证码发送失败，请稍后重试
	 */
	public static final String E400092 = "400092";
	/**
	 * 短信验证码发送次数超限异常
	 */
	public static final String E400093 = "400093";
	/**
	 * 短信验证码错误异常
	 */
	public static final String E400094 = "400094";
	/**
	 * 风控阻断异常
	 */
	public static final String E400095 = "400095";
	/**
	 * 创建支付订单异常
	 */
	public static final String E400096 = "400096";
	/**
	 * 退款原交易状态异常
	 */
	public static final String E400097 = "400097";
	/**
	 * 退款原交易不存在
	 */
	public static final String E400098 = "400098";
	/**
	 * 退款已经存在不允许退款
	 */
	public static final String E400099 = "400099";
	/**
	 * 退款可退金额超限
	 */
	public static final String E400100 = "400100";
	/**
	 * 退款类型错误
	 */
	public static final String E400101 = "400101";
	/**
	 * 退款金额错误
	 */
	public static final String E400102 = "400102";
	/**
	 * 退款请求和原交易业务方不匹配
	 */
	public static final String E400103 = "400103";
	/**
	 * 创建退款请求异常
	 */
	public static final String E400104 = "400104";
	/**
	 * 退款请求异常
	 */
	public static final String E400105 = "400105";
	/**
	 * 冲正请求失败
	 */
	public static final String E400106 = "400106";
	/**
	 * 创建冲正请求记录失败
	 */
	public static final String E400107 = "400107";
	/**
	 * 更新冲正请求记录失败
	 */
	public static final String E400108 = "400108";
	/**
	 * 冲正请求记录不存在
	 */
	public static final String E400109 = "400109";
	/**
	 * 冲正异常
	 */
	public static final String E400110 = "400110";
	/**
	 * 原交易无指定银行规则
	 */
	public static final String E400111 = "400111";
	/**
	 * 冲正请求号重复
	 */
	public static final String E400112 = "400112";
	/**
	 * 无法找到商品类别码
	 */
	public static final String E400113 = "400113";
	
	/**
	 * 签名较验失败或未知错误
	 */
	public static final String E401001 = "401001";
	/**
	 * 卡密成功处理过或者提交卡号过于频繁
	 */
	public static final String E401002 = "401002";
	/**
	 * 卡数量过多,目前最多支持10张卡
	 */
	public static final String E401003 = "401003";
	/**
	 * 订单号重复
	 */
	public static final String E401004 = "401004";
	/**
	 * 支付金额有误
	 */
	public static final String E401005 = "401005";
	/**
	 * 支付方式未开通
	 */
	public static final String E401006 = "401006";
	/**
	 * 业务状态不可用,未开通此类卡业务
	 */
	public static final String E401007 = "401007";
	/**
	 * 卡面额组填写错误
	 */
	public static final String E401008 = "401008";
	/**
	 * 卡号密码为空或者数量不相等
	 */
	public static final String E401009 = "401009";
	/**
	 * 销卡成功，订单失败
	 */
	public static final String E401010 = "401010";
	/**
	 * 卡号卡密或卡面额不符合规则
	 */
	public static final String E401011 = "401011";
	/**
	 * 本张卡密您提交过于频繁，请您稍后再试
	 */
	public static final String E401012 = "401012";
	/**
	 * 不支持的卡类型（比如电信地方卡）
	 */
	public static final String E401013 = "401013";
	/**
	 * 密码错误或充值卡无效
	 */
	public static final String E401014 = "401014";
	/**
	 * 充值卡无效
	 */
	public static final String E401015 = "401015";
	/**
	 * 卡内余额不足
	 */
	public static final String E401016 = "401016";
	/**
	 * 余额卡过期（有效期1个月）
	 */
	public static final String E401017 = "401017";
	/**
	 * 此卡正在处理中
	 */
	public static final String E401018= "401018";
	/**
	 * 未知错误
	 */
	public static final String E401019 = "401019";
	/**
	 * 此卡已使用
	 */
	public static final String E401020 = "401020";
	/**
	 * 卡密在系统处理中
	 */
	public static final String E401021 = "401021";
	/**
	 * 该卡为假卡
	 */
	public static final String E401022 = "401022";
	/**
	 * 该卡种正在维护
	 */
	public static final String E401023 = "401023";
	/**
	 * 浙江省移动维护
	 */
	public static final String E401024 = "401024";
	/**
	 * 江苏省移动维护
	 */
	public static final String E401025 = "401025";
	/**
	 * 福建省移动维护
	 */
	public static final String E401026 = "401026";
	/**
	 * 辽宁省移动维护
	 */
	public static final String E401027 = "401027";
	/**
	 * 该卡已被锁定
	 */
	public static final String E401028 = "401028";
	/**
	 * 系统繁忙，请稍后再试
	 */
	public static final String E401029 = "401029";
	/**
	 * 验证码发送频率限制，请稍后重试
	 */
	public static final String E400140 ="400140";
	/**
	 * 查发卡方失败，请联系发卡银行
	 */
	public static final String E411101 = "411101";
	/**
	 * 本卡在该商户不允许此交易，请联系收单机构
	 */
	public static final String E411103 = "411103";
	/**
	 * 本卡被发卡方没收，请联系发卡银行
	 */
	public static final String E411104 = "411104";
	/**
	 * 持卡人认证失败，请重新核对信息
	 */
	public static final String E411105 = "411105";
	/**
	 * 请求正在处理中
	 */
	public static final String E411106 = "411106";
	/**
	 * 无效应答
	 */
	public static final String E411107 = "411107";
	/**
	 * 不作任何处理
	 */
	public static final String E411108 = "411108";
	/**
	 * 支付失败，请联系发卡银行
	 */
	public static final String E411110 = "411110";
	/**
	 * 支付失败，请稍候重试
	 */
	public static final String E411112 = "411112";
	/**
	 * 交易超限，请联系发卡银行
	 */
	public static final String E411113 = "411113";
	/**
	 * 无效卡号，请核对重新输入
	 */
	public static final String E411114 = "411114";
	/**
	 * 本卡未初始化，请联系发卡银行
	 */
	public static final String E411121 = "411121";
	/**
	 * 报文格式错误，请联系收单机构
	 */
	public static final String E411130 = "411130";
	/**
	 * 该卡有作弊嫌疑或有相关限制，请联系发卡银行
	 */
	public static final String E411134 = "411134";
	/**
	 * CVN验证失败或有作弊嫌疑
	 */
	public static final String E411135 = "411135";
	/**
	 * 密码错误次数超限，请联系发卡银行
	 */
	public static final String E411138 = "411138";
	/**
	 * 请求失败，请联系收单机构
	 */
	public static final String E411140 = "411140";
	/**
	 * 可用余额不足，请联系发卡银行
	 */
	public static final String E411151 = "411151";
	/**
	 * 过期的卡
	 */
	public static final String E411152 = "411152";
	/**
	 * 冲正成功
	 */
	public static final String E411153 = "411153";
	/**
	 * 该卡已过期，请联系发卡银行
	 */
	public static final String E411154 = "411154";
	/**
	 * 密码验证失败，请重新输入
	 */
	public static final String E411155 = "411155";
	/**
	 * 消费金额超限，请联系发卡银行
	 */
	public static final String E411161 = "411161";
	/**
	 * 单日消费次数超限，请联系发卡银行
	 */
	public static final String E411165 = "411165";
	/**
	 * 异地控制限额调整为500元，帮付通错误代码调整为J5-- 预留手机号与开户行归属地不一致
	 */
	public static final String E411199 = "411199";
	/**
	 * 无此权限，请联系收单机构
	 */
	public static final String E411001 = "411001";
	/**
	 * 该卡超过商户限额
	 */
	public static final String E411003 = "411003";
	/**
	 * 该卡不支持无卡支付，请联系发卡方开通
	 */
	public static final String E411022 = "411022";
	/**
	 * 商户单笔金额超限
	 */
	public static final String E411041 = "411041";
	/**
	 * 商户月累计金额超限
	 */
	public static final String E411042 = "411042";
	/**
	 * 没有找到相应交易记录
	 */
	public static final String E411043 = "411043";
	/**
	 * 银联异步新增：交易失败，详情请咨询95516
	 */
	public static final String E411044 = "411044";
	/**
	 * 身份证件号不正确
	 */
	public static final String E411045 = "411045";
	/**
	 * 交易受限
	 */
	public static final String E411046 = "411046";
	/**
	 * 身份证与姓名不匹配
	 */
	public static final String E411047 = "411047";
	/**
	 * 证件号码为空
	 */
	public static final String E411048 = "411048";
	/**
	 * 手机号为空
	 */
	public static final String E411049 = "411049";
	/**
	 * 姓名为空
	 */
	public static final String E411050 = "411050";
	/**
	 * 超过商户日交易限额
	 */
	public static final String E411051 = "411051";
	/**
	 * 超过商户日交易笔数
	 */
	public static final String E411052 = "411052";
	/**
	 * 对公账户代扣支付失败
	 */
	public static final String E411053 = "411053";
	/**
	 * 输入姓名有误
	 */
	public static final String E411054 = "411054";
	/**
	 * 未知错误
	 */
	public static final String E411055 = "411055";
	/**
	 * 持卡人鉴权失败，请重新核对信息
	 */
	public static final String E411056 = "411056";
	/**
	 * 银行卡异常，请联系发卡银行
	 */
	public static final String E411057 = "411057";
	/**
	 * 平台代码无效
	 */
	public static final String E411058 = "411058";
	/**
	 * 请求参数错误
	 */
	public static final String E411059 = "411059";
	/**
	 * 不支持的银行
	 */
	public static final String E411060 = "411060";
	/**
	 * 没有可用的通道
	 */
	public static final String E411061 = "411061";
	/**
	 * 银行状态异常
	 */
	public static final String E411062 = "411062";
	/**
	 * 不支持的交易类型
	 */
	public static final String E411063 = "411063";
	/**
	 * 银行编码有误
	 */
	public static final String E411064 = "411064";
	/**
	 * 重复的请求
	 */
	public static final String E411065 = "411065";
	/**
	 * 原交易订单不存在
	 */
	public static final String E411066 = "411066";
	/**
	 * 原交易订单状态不支持此请求
	 */
	public static final String E411067 = "411067";
	/**
	 * 原交易订单数据与当前请求不匹配
	 */
	public static final String E411068 = "411068";
	/**
	 * 非法请求
	 */
	public static final String E411069 = "411069";
	/**
	 * 银行处理中，请勿重复操作
	 */
	public static final String E411070 = "411070";
	/**
	 * 银行路由失败
	 */
	public static final String E411071 = "411071";
	/**
	 * 原交易未找到或者已经处理
	 */
	public static final String E411072 = "411072";
	
	/**
	 * 超过商户月交易笔数
	 */
	public static final String E411073 = "411073";
	/**
	 * 原交易失败
	 */
	public static final String E400073 = "400073";
	/**
	 * 交易清算日期限定
	 */
	public static final String E400074 = "400074";
	/**
	 * 超过协议签约/撤销次数
	 */
	public static final String E400075 = "400075";
	/**
	 * 该客户号已关闭快速支付
	 */
	public static final String E400076 = "400076";
	/**
	 * 对账问题
	 */
	public static final String E400077 = "400077";
	/**
	 * 原始金额不正确
	 */
	public static final String E400078 = "400078";
	/**
	 * 账户状态无效
	 */
	public static final String E499001 = "499001";
	/**
	 * 该订单不支持退款
	 */
	public static final String E499002 = "499002";
	/**
	 * 退款金额超限
	 */
	public static final String E499003 = "499003";
	/**
	 * 余额不足
	 */
	public static final String E499004 = "499004";
	/**
	 * 订单不存在
	 */
	public static final String E499005 = "499005";
	/**
	 * 历史退款未开通
	 */
	public static final String E499006 = "499006";
	/**
	 *  IP 限制
	 */
	public static final String E499007 = "499007";
	/**
	 *  银行退款失败
	 */
	public static final String E499008 = "499008";
	/**
	 * 未开通电子支付或身份证号、姓名、手机号有误
	 */
	public static final String E400079 = "400079";
	
	/**
	 * 需要手机短验
	 */
	public static final String E400114 = "400114";
	/**
	 * 指定绑卡信息不存在
	 */
	public static final String E400115 = "400115";
	/**
	 * 支付银行卡鉴权失败
	 */
	public static final String E400116 = "400116";
	/**
	 *  银行预留手机号有误
	 */
	public static final String E400117 = "400117";
	/**
	 * 身份证或姓名有误
	 */
	public static final String E400118 = "400118";
	
	/**
	 * CVV2或有效期有误
	 */
	public static final String E400119 = "400119";
	
	/**
	 * 绑卡关系失效
	 */
	public static final String E400120 = "400120";
	
	/**
	 * 身份证号必须为18位
	 */
	public static final String E400121 = "400121";
	
	/**
	 * 	银行卡未开通银联无卡业务
	 */
	public static final String E400122 = "400122";
	/**
	 * 该卡已经签约
	 */
	public static final String E400123 = "400123";

	
	/**
	 * 您的账号尚未签约
	 */
	public static final String E400124 = "400124";
	
	/**
	 *此银行不支持查询签约
	 */
	public static final String E400125 = "400125";
	
	/**
	 * 不存在签约请求记录
	 */
	public static final String E400126 = "400126";

	/**
	 * 金额不能低于1元
	 */
	public static final String E400127 = "400127";
	
	/**
	 * 绑卡与用户信息不匹配
	 */
	public static final String E400128 = "400128";

	/**
	 * 订单已成功支付或者冲正请勿重复支付
	 */
	public static final String E400129 = "400129";


	
	private static final Map<String, ErrorCodeDTO> errorMessage = new HashMap<String, ErrorCodeDTO>();

	static {
		errorMessage.put(NCPAYSYSCODE+"_"+E400081,newErrorCodeDTO(NCPAYSYSCODE,"400081","支付记录不存在异常","N400081","订单不存在，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400082,newErrorCodeDTO(NCPAYSYSCODE,"400082","银行规则记录不存在异常","N400082","该卡暂不支持，请查看支持银行列表"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400083,newErrorCodeDTO(NCPAYSYSCODE,"400083","风控处理结果不存在异常","N400083","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400084,newErrorCodeDTO(NCPAYSYSCODE,"400084","支付记录状态异常","N400084","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400085,newErrorCodeDTO(NCPAYSYSCODE,"400085","卡信息输入异常","N400085","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400086,newErrorCodeDTO(NCPAYSYSCODE,"400086","卡信息验证未通过异常","N400086","卡信息或银行预留手机号有误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400087,newErrorCodeDTO(NCPAYSYSCODE,"400087","卡种不支持异常","N400087","该卡暂不支持，请查看支持银行列表"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400089,newErrorCodeDTO(NCPAYSYSCODE,"400089","手机号码无记录异常","N400089","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400090,newErrorCodeDTO(NCPAYSYSCODE,"400090","无法获得短信内容异常","N400090","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400091,newErrorCodeDTO(NCPAYSYSCODE,"400091","短信验证码已失效请重新获取","N400091","短信校验码已过期，请重新获取"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400092,newErrorCodeDTO(NCPAYSYSCODE,"400092","短信验证码发送失败，请稍后重试","N400092","短信验证码发送失败，请稍后重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400093,newErrorCodeDTO(NCPAYSYSCODE,"400093","短信验证码发送次数超限异常","N400093","短验发送次数超限，请重新下单"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400094,newErrorCodeDTO(NCPAYSYSCODE,"400094","短信验证码错误异常","N400094","短信验证码错误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400095,newErrorCodeDTO(NCPAYSYSCODE,"400095","风控阻断异常","N400095","交易存在限制，无法完成支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400096,newErrorCodeDTO(NCPAYSYSCODE,"400096","创建支付订单异常","N400096","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400097,newErrorCodeDTO(NCPAYSYSCODE,"400097","退款原交易状态异常","N400097","退款原交易状态异常"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400098,newErrorCodeDTO(NCPAYSYSCODE,"400098","退款原交易不存在","N400098","退款原交易不存在"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400099,newErrorCodeDTO(NCPAYSYSCODE,"400099","退款已经存在不允许退款","N400099","退款已经存在不允许退款"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400100,newErrorCodeDTO(NCPAYSYSCODE,"400100","退款可退金额超限","N400100","退款可退金额超限"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400101,newErrorCodeDTO(NCPAYSYSCODE,"400101","退款类型错误","N400101","退款类型错误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400102,newErrorCodeDTO(NCPAYSYSCODE,"400102","退款金额错误","N400102","退款金额错误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400103,newErrorCodeDTO(NCPAYSYSCODE,"400103","退款请求和原交易业务方不匹配","N400103","退款请求和原交易业务方不匹配"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400104,newErrorCodeDTO(NCPAYSYSCODE,"400104","创建退款请求异常","N400104","创建退款请求异常"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400105,newErrorCodeDTO(NCPAYSYSCODE,"400105","退款请求异常","N400105","退款请求异常"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400106,newErrorCodeDTO(NCPAYSYSCODE,"400106","冲正请求失败","N400106","冲正请求失败"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400107,newErrorCodeDTO(NCPAYSYSCODE,"400107","创建冲正请求记录失败","N400107","创建冲正请求记录失败"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400108,newErrorCodeDTO(NCPAYSYSCODE,"400108","更新冲正请求记录失败","N400108","更新冲正请求记录失败"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400109,newErrorCodeDTO(NCPAYSYSCODE,"400109","冲正请求记录不存在","N400109","冲正请求记录不存在"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400110,newErrorCodeDTO(NCPAYSYSCODE,"400110","冲正异常","N400110","冲正异常"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400111,newErrorCodeDTO(NCPAYSYSCODE,"400111","原交易无指定银行规则","N400111","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400112,newErrorCodeDTO(NCPAYSYSCODE,"400112","冲正请求号重复","N400112","冲正请求号重复"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400113,newErrorCodeDTO(NCPAYSYSCODE,"400113","无法找到商品类别码","N400113","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400114,newErrorCodeDTO(NCPAYSYSCODE,"400114","需要手机短验","N400114","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400115,newErrorCodeDTO(NCPAYSYSCODE,"400115","指定绑卡信息不存在（绑卡id没有找到绑卡信息）","N400115","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400116,newErrorCodeDTO(NCPAYSYSCODE,"400116","支付银行卡鉴权失败","N400116","卡信息或银行预留手机号有误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400117,newErrorCodeDTO(NCPAYSYSCODE,"400117","银行预留手机号有误","N400117","银行预留手机号有误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400118,newErrorCodeDTO(NCPAYSYSCODE,"400118","身份证或姓名有误","N400118","身份证或姓名有误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400119,newErrorCodeDTO(NCPAYSYSCODE,"400119","CVV2或有效期有误","N400119","CVV2或有效期有误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400120,newErrorCodeDTO(NCPAYSYSCODE,"400120","绑卡关系已经失","N400120","无效的绑卡，请换卡或重新绑定"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400121,newErrorCodeDTO(NCPAYSYSCODE,"400121","身份证号必须为18位","N400121","身份证号必须为18位"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400122,newErrorCodeDTO(NCPAYSYSCODE,"400122","银行卡未开通银联无卡业务","N400122","未开通银联无卡支付，请联系发卡行"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400123,newErrorCodeDTO(NCPAYSYSCODE,"400123","该卡已经签约","N400123","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400124,newErrorCodeDTO(NCPAYSYSCODE,"400124","您的账号尚未签约","N400124","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400125,newErrorCodeDTO(NCPAYSYSCODE,"400125","此银行不支持查询签约","N400125","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400126,newErrorCodeDTO(NCPAYSYSCODE,"400126","不存在签约请求记录","N400126","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400127,newErrorCodeDTO(NCPAYSYSCODE,"400127","金额不能低于1元","N400127","金额不能低于1元"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400128,newErrorCodeDTO(NCPAYSYSCODE,"400128","绑卡与用户信息不匹配（卡账户中userid与传的userid不一致）；userid是identityid+merchantaccount","N400128","系统异常，请联系商户"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400129,newErrorCodeDTO(NCPAYSYSCODE,"400129","订单已成功支付或者冲正请勿重复支付","N400129","已支付成功，请勿重新支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400140,newErrorCodeDTO(NCPAYSYSCODE,"400140","验证码发送频率限制，请稍后重试","N400140","验证码发送频率限制，请稍后重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400073,newErrorCodeDTO(NCPAYSYSCODE,"400073","原交易失败","N400073","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400074,newErrorCodeDTO(NCPAYSYSCODE,"400074","交易清算日期限定","N400074","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400075,newErrorCodeDTO(NCPAYSYSCODE,"400075","超过协议签约/撤销次数","N400075","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400076,newErrorCodeDTO(NCPAYSYSCODE,"400076","该客户号已关闭快速支付","N400076","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400077,newErrorCodeDTO(NCPAYSYSCODE,"400077","对账问题","N400077","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400078,newErrorCodeDTO(NCPAYSYSCODE,"400078","原始金额不正确","N400078","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400079,newErrorCodeDTO(NCPAYSYSCODE,"400079","未开通电子支付或身份证号、姓名、手机号有误","N400079","未开通电子支付或身份证号、姓名、手机号有误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E400080,newErrorCodeDTO(NCPAYSYSCODE,"400080","一分钟内同一手机号校验过频繁请稍后再试","N400080","系统繁忙，请稍后再试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401001,newErrorCodeDTO(NCPAYSYSCODE,"401001","签名较验失败或未知错误","N401001","系统异常，请稍后重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401002,newErrorCodeDTO(NCPAYSYSCODE,"401002","卡密成功处理过或者提交卡号过于频繁","N401002","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401003,newErrorCodeDTO(NCPAYSYSCODE,"401003","卡数量过多目前最多支持10张卡","N401003","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401004,newErrorCodeDTO(NCPAYSYSCODE,"401004","订单号重复","N401004","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401005,newErrorCodeDTO(NCPAYSYSCODE,"401005","支付金额有误","N401005","支付金额有误，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401006,newErrorCodeDTO(NCPAYSYSCODE,"401006","支付方式未开通","N401006","支付方式未开通，请联系商户"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401007,newErrorCodeDTO(NCPAYSYSCODE,"401007","业务状态不可用未开通此类卡业务","N401007","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401008,newErrorCodeDTO(NCPAYSYSCODE,"401008","卡面额组填写错误","N401008","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401009,newErrorCodeDTO(NCPAYSYSCODE,"401009","卡号密码为空或者数量不相等","N401009","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401010,newErrorCodeDTO(NCPAYSYSCODE,"401010","销卡成功订单失败","N401010","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401011,newErrorCodeDTO(NCPAYSYSCODE,"401011","卡号卡密或卡面额不符合规则","N401011","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401012,newErrorCodeDTO(NCPAYSYSCODE,"401012","本张卡密您提交过于频繁请您稍后再试","N401012","系统异常，请稍后重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401013,newErrorCodeDTO(NCPAYSYSCODE,"401013","不支持的卡类型（比如电信地方卡）","N401013","请使用借记卡或储蓄卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401014,newErrorCodeDTO(NCPAYSYSCODE,"401014","密码错误或充值卡无效","N401014","密码错误，请核实后重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401015,newErrorCodeDTO(NCPAYSYSCODE,"401015","充值卡无效","N401015","系统异常，请稍后重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401016,newErrorCodeDTO(NCPAYSYSCODE,"401016","卡内余额不足","N401016","余额不足，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401017,newErrorCodeDTO(NCPAYSYSCODE,"401017","余额卡过期（有效期1个月）","N401017","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401018,newErrorCodeDTO(NCPAYSYSCODE,"401018","此卡正在处理中","N401018","系统异常，请稍后重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401019,newErrorCodeDTO(NCPAYSYSCODE,"401019","未知错误","N401019","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401020,newErrorCodeDTO(NCPAYSYSCODE,"401020","此卡已使用","N401020","系统异常，请稍后重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401021,newErrorCodeDTO(NCPAYSYSCODE,"401021","卡密在系统处理中","N401021","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401022,newErrorCodeDTO(NCPAYSYSCODE,"401022","该卡为假卡","N401022","此卡为假卡，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401023,newErrorCodeDTO(NCPAYSYSCODE,"401023","该卡种正在维护","N401023","此卡暂不可用，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401024,newErrorCodeDTO(NCPAYSYSCODE,"401024","浙江省移动维护","N401024","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401025,newErrorCodeDTO(NCPAYSYSCODE,"401025","江苏省移动维护","N401025","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401026,newErrorCodeDTO(NCPAYSYSCODE,"401026","福建省移动维护","N401026","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401027,newErrorCodeDTO(NCPAYSYSCODE,"401027","辽宁省移动维护","N401027","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401028,newErrorCodeDTO(NCPAYSYSCODE,"401028","该卡已被锁定","N401028","此卡暂不可用，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E401029,newErrorCodeDTO(NCPAYSYSCODE,"401029","系统繁忙请稍后再试","N401029","系统繁忙，请稍后再试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411003,newErrorCodeDTO(NCPAYSYSCODE,"411003","该卡超过商户限额","N411003","消费金额超限，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411041,newErrorCodeDTO(NCPAYSYSCODE,"411041","商户单笔金额超限","N411041","超过单笔支付限额，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411042,newErrorCodeDTO(NCPAYSYSCODE,"411042","商户月累计金额超限","N411042","超过单月累计支付限额，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411045,newErrorCodeDTO(NCPAYSYSCODE,"411045","请您确认身份证件号是否填写正确","N411045","请确认身份证号是否正确"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411051,newErrorCodeDTO(NCPAYSYSCODE,"411051","超过商户日交易限额","N411051","超过单日支付限额，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411052,newErrorCodeDTO(NCPAYSYSCODE,"411052","超过商户日交易笔数","N411052","超过单日支付次数上限，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411054,newErrorCodeDTO(NCPAYSYSCODE,"411054","输入姓名有误","N411054","银行卡未开通银联无卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411055,newErrorCodeDTO(NCPAYSYSCODE,"411055","未知错误","N411055","支付失败，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411056,newErrorCodeDTO(NCPAYSYSCODE,"411056","卡信息或银行预留手机号有误","N411056","卡信息或银行预留手机号有误"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411057,newErrorCodeDTO(NCPAYSYSCODE,"411057","银行卡异常请联系发卡银行","N411057","该卡有作弊嫌疑或有相关限制，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411058,newErrorCodeDTO(NCPAYSYSCODE,"411058","平台代码无效","N411058","银行系统异常"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411059,newErrorCodeDTO(NCPAYSYSCODE,"411059","请求参数错误","N411059","系统异常，请联系商户"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411060,newErrorCodeDTO(NCPAYSYSCODE,"411060","不支持的银行","N411060","此卡不支持，请查看支持的银行列表"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411061,newErrorCodeDTO(NCPAYSYSCODE,"411061","没有可用的通道","N411061","系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411062,newErrorCodeDTO(NCPAYSYSCODE,"411062","银行状态异常","N411062","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411063,newErrorCodeDTO(NCPAYSYSCODE,"411063","不支持的交易类型","N411063","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411064,newErrorCodeDTO(NCPAYSYSCODE,"411064","银行编码有误","N411064","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411065,newErrorCodeDTO(NCPAYSYSCODE,"411065","重复的请求","N411065","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411066,newErrorCodeDTO(NCPAYSYSCODE,"411066","原交易订单不存在","N411066","原交易订单不存在，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411067,newErrorCodeDTO(NCPAYSYSCODE,"411067","原交易订单状态不支持此请求","N411067","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411068,newErrorCodeDTO(NCPAYSYSCODE,"411068","原交易订单数据与当前请求不匹配","N411068","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411069,newErrorCodeDTO(NCPAYSYSCODE,"411069","非法请求","N411069","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411070,newErrorCodeDTO(NCPAYSYSCODE,"411070","银行处理中请勿重复操作","N411070","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411071,newErrorCodeDTO(NCPAYSYSCODE,"411071","银行路由失败","N411071","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411072,newErrorCodeDTO(NCPAYSYSCODE,"411072","原交易未找到或者已经处理","N411072","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411073,newErrorCodeDTO(NCPAYSYSCODE,"411073","超过商户月交易笔数","N411073","超过单月支付次数上限，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411103,newErrorCodeDTO(NCPAYSYSCODE,"411103","本卡在该商户不允许此交易请联系收单机构","N411103","该卡暂不支持，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411104,newErrorCodeDTO(NCPAYSYSCODE,"411104","本卡被发卡方没收请联系发卡银行","N411104","本卡被发卡方没收，请联系发卡银行 "));
		errorMessage.put(NCPAYSYSCODE+"_"+E411110,newErrorCodeDTO(NCPAYSYSCODE,"411110","支付失败请联系发卡银行","N411110","支付失败，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411112,newErrorCodeDTO(NCPAYSYSCODE,"411112","支付失败请稍候重试","N411112","支付失败，请稍候重试"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411113,newErrorCodeDTO(NCPAYSYSCODE,"411113","交易超限请联系发卡银行","N411113","交易超限，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411114,newErrorCodeDTO(NCPAYSYSCODE,"411114","无效卡号请核对重新输入","N411114","无效卡号，请核实后重新支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411121,newErrorCodeDTO(NCPAYSYSCODE,"411121","本卡未初始化请联系发卡银行","N411121","本卡未激活或睡眠卡，请联系发卡银行 "));
		errorMessage.put(NCPAYSYSCODE+"_"+E411130,newErrorCodeDTO(NCPAYSYSCODE,"411130","报文格式错误请联系收单机构","N411130","银行系统异常，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411138,newErrorCodeDTO(NCPAYSYSCODE,"411138","密码错误次数超限请联系发卡银行","N411138","密码错误次数超限，请联系发卡银行 "));
		errorMessage.put(NCPAYSYSCODE+"_"+E411151,newErrorCodeDTO(NCPAYSYSCODE,"411151","可用余额不足请联系发卡银行","N411151","可用余额不足，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411152,newErrorCodeDTO(NCPAYSYSCODE,"411152","过期的卡","N411152","卡已过期，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411154,newErrorCodeDTO(NCPAYSYSCODE,"411154","该卡已过期请联系发卡银行","N411154","卡已过期，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411155,newErrorCodeDTO(NCPAYSYSCODE,"411155","密码验证失败请重新输入","N411155","密码验证失败，请重新输入 "));
		errorMessage.put(NCPAYSYSCODE+"_"+E411161,newErrorCodeDTO(NCPAYSYSCODE,"411161","消费金额超限请联系发卡银行","N411161","交易超限，请联系客服"));
		errorMessage.put(NCPAYSYSCODE+"_"+E411199,newErrorCodeDTO(NCPAYSYSCODE,"411199","预留手机号与开户行归属地不一致","N411199","风险较高拒绝交易，请换卡支付"));
		errorMessage.put(NCPAYSYSCODE+"_"+E499008,newErrorCodeDTO(NCPAYSYSCODE,"499008","银行退款失败","N499008","银行系统异常"));
	}
	
	public static ErrorCodeDTO getErrorInfo(String code,String msg){
		if(StringUtils.isBlank(code)){
			return newErrorCodeDTO("", "", msg, "", msg);
		}
		if(errorMessage.containsKey(NCPAYSYSCODE+"_"+code)){
			return errorMessage.get(NCPAYSYSCODE+"_"+code);
		}else{
			return newErrorCodeDTO("", code, msg, code, msg);
		}
	}
	
	private static ErrorCodeDTO newErrorCodeDTO(String paySysCode,String originErrorCode,String originErrorMsg,String externalErrorCode,String externalErrorMsg){
		ErrorCodeDTO errorCodeDTO = new ErrorCodeDTO();
		errorCodeDTO.setPaySysCode(paySysCode);
		errorCodeDTO.setOriginErrorCode(originErrorCode);
		errorCodeDTO.setExternalErrorCode(externalErrorCode);
		errorCodeDTO.setOriginErrorMsg(originErrorMsg);
		errorCodeDTO.setExternalErrorMsg(externalErrorMsg);
		return errorCodeDTO;
	}
	
	public static void main(String[] args) {
		
		ErrorCodeDTO errorInfo = getErrorInfo("499008","银行退款失败");
		System.err.println(errorInfo.getExternalErrorCode()+"_"+errorInfo.getExternalErrorMsg());
	}
}