/**
 * 
 */
package com.yeepay.g3.core.frontend.service.impl;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.frontend.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.frontend.dao.PayOrderDao;
import com.yeepay.g3.core.frontend.dao.PayRecordDao;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.enumtype.ExternalSystem;
import com.yeepay.g3.core.frontend.errorcode.ErrorCodeTranslator;
import com.yeepay.g3.core.frontend.errorcode.SystemErrorCodeTranslator;
import com.yeepay.g3.core.frontend.service.FanRouteService;
import com.yeepay.g3.core.frontend.util.ConstantUtils;
import com.yeepay.g3.core.frontend.util.RedisUtil;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.bankchannel.OpenPayFacade;
import com.yeepay.g3.facade.bankchannel.dto.OpenPayPrepareRouterRequestDTO;
import com.yeepay.g3.facade.bankchannel.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayRequestDTO;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.trade.bankinterface.BankInterfaceFacade;
import com.yeepay.g3.facade.trade.bankinterface.OpenInterfaceFacade;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * 抽象Service， 将Service公用的属性或方法进行封装
 * @author TML
 */

public abstract class AbstractService {
	
	/**
	 * 错误码转换处理器
	 */
	protected ErrorCodeTranslator errorCodeTranslator = SystemErrorCodeTranslator.getInstance();

	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(AbstractService.class);
	
	
	@Resource(name = "amqpTemplate")
	protected AmqpTemplate amqpTemplate;
	
	@Resource(name = "delayAmqpTemplate")
	protected AmqpTemplate delayAmqpTemplate;
	
	@Resource
	protected PayOrderDao payOrderDao;

	@Resource
	protected FanRouteService fanRouteService;
	
	@Resource
	protected PayRecordDao payRecordDao;
	
	protected OpenPayFacade openPayFacade = RemoteFacadeProxyFactory.getService(
			OpenPayFacade.class, ExternalSystem.BANKROUTER);
	protected BankInterfaceFacade bankInterfaceFacade = RemoteFacadeProxyFactory.getService(
			BankInterfaceFacade.class, ExternalSystem.BANKINTERFACE);
	protected OpenInterfaceFacade openInterfaceFacade = RemoteFacadeProxyFactory.getService(
			OpenInterfaceFacade.class, ExternalSystem.BANKINTERFACE);
	
	
	protected OpenPayPrepareRouterRequestDTO buildOpenPayPrepareRouterRequestDTO(PrePayRequestDTO prePayRequestDTO){
		OpenPayPrepareRouterRequestDTO openPayPrepareRouterRequestDTO = new OpenPayPrepareRouterRequestDTO();
		openPayPrepareRouterRequestDTO.setProductCode(ConstantUtils.BANKROUTER_PRODUCTCODE);
		openPayPrepareRouterRequestDTO.setAppId(prePayRequestDTO.getAppId());
		openPayPrepareRouterRequestDTO.setBankCode(PlatformType.ALIPAY.name().equals(prePayRequestDTO.getPlatformType())?
				prePayRequestDTO.getPlatformType():PlatformType.WECHAT.name());

		openPayPrepareRouterRequestDTO.setSceneType(ConstantUtils.getSceneTypeByBasicProductCode(prePayRequestDTO.getPlatformType(), prePayRequestDTO.getBasicProductCode()));
		openPayPrepareRouterRequestDTO.setBasicProductCode(prePayRequestDTO.getBasicProductCode());
		openPayPrepareRouterRequestDTO.setRetailProductCode(prePayRequestDTO.getRetailProductCode());
		openPayPrepareRouterRequestDTO.setBusinessType(prePayRequestDTO.getPayBusinessType().name());
		openPayPrepareRouterRequestDTO.setIndustryCode(prePayRequestDTO.getIndustryCode());
		openPayPrepareRouterRequestDTO.setMerchantNo(prePayRequestDTO.getCustomerNumber());
		openPayPrepareRouterRequestDTO.setMerchantRiskLevel(prePayRequestDTO.getCustomerLevel());
		openPayPrepareRouterRequestDTO.setTradeAmount(new Amount(prePayRequestDTO.getTotalAmount()));
		openPayPrepareRouterRequestDTO.setUniSerialNo(prePayRequestDTO.getDealUniqueSerialNo());
		//根据统一配置,给路由传递禁用信用卡参数 dongbo.jiao 20170905 start
		boolean isLimitCredit = ConstantUtils.isLimitCredit(prePayRequestDTO.getCustomerNumber());
		if(isLimitCredit){
			openPayPrepareRouterRequestDTO.setBusinessType(PayBusinessType.OD.name());
		}
		//等待路由下线该接口
		openPayPrepareRouterRequestDTO.setLimitCredit(isLimitCredit);
		//根据统一配置,给路由传递禁用信用卡参数 dongbo.jiao 20170905 end
		// 哆啦宝业务通道编码 added by zhijun.wang 20171212

		Map<String, String> extParam = prePayRequestDTO.getExtParam();
		// 报备费率  start
		openPayPrepareRouterRequestDTO.setReportFee(extParam.get("reportFee"));
		extParam.remove("reportFee");
		// 报备费率 end
		openPayPrepareRouterRequestDTO.setExternalParams(extParam);
		return openPayPrepareRouterRequestDTO;
	}

	protected OpenPayRequestDTO buildOpenPayRequestDTO(PayOrder payOrder, PayRequestDTO payRequestDTO){
		OpenPayRequestDTO openPayRequestDTO = new OpenPayRequestDTO();
		openPayRequestDTO.setBankCode(payOrder.getPlatformType());  //银行编码WECHAT/ALIPAY
		openPayRequestDTO.setMerchantOrderNo(payOrder.getOutTradeNo());   //商户订单号
		openPayRequestDTO.setTradeOrderNo(payOrder.getPayOrderNo());    //FE支付订单号
		openPayRequestDTO.setBankOrderNo(payOrder.getOrderNo());//重复支付请求传原银行订单号。
		openPayRequestDTO.setCallBackUrl(payOrder.getPageCallBack());     
		openPayRequestDTO.setCommodityName(payOrder.getGoodsDescription());
		openPayRequestDTO.setMerchantNo(payOrder.getCustomerNumber());
		openPayRequestDTO.setPayerIp(payOrder.getPayerIp());
		openPayRequestDTO.setProductCode(ConstantUtils.BANKROUTER_PRODUCTCODE);                //支付产品编码
		openPayRequestDTO.setSceneType(ConstantUtils.getSceneType(payOrder.getOrderType()));     //场景类型
		openPayRequestDTO.setTradeAmount(new Amount(payOrder.getTotalAmount()));
		openPayRequestDTO.setBusinessType(payOrder.getPayLimitType());    //业务类型OD/DC/OC/DE
		openPayRequestDTO.setBusinessSource(payRequestDTO.getOrderSystem());  //业务来源
		openPayRequestDTO.setIndustryCode(payRequestDTO.getIndustryCode());     //行业编码
		openPayRequestDTO.setMerchantRiskLevel(payRequestDTO.getCustomerLevel());    //商户等级
		openPayRequestDTO.setMerchantName(payRequestDTO.getCustomerName());     //商户名称
	    openPayRequestDTO.setUniSerialNo(payOrder.getDealUniqueSerialNo());
	    openPayRequestDTO.setRetailProductCode(payOrder.getRetailProductCode());
	    openPayRequestDTO.setBasicProductCode(payOrder.getBasicProductCode());
	    openPayRequestDTO.setOpenId(payRequestDTO.getOpenId());              //公众号支付有效
	    openPayRequestDTO.setAppId(payRequestDTO.getAppId());            //公众号ID
	    openPayRequestDTO.setChannelId(payOrder.getPayInterface());      //公众号支付有效
	    openPayRequestDTO.setReportMerchantNo(payRequestDTO.getReportMerchantNo()); //预路由返回必填
	    openPayRequestDTO.setPayEmpowerNo(payRequestDTO.getPayEmpowerNo());  //支付授权号,场景类型为被扫时，必填
	    openPayRequestDTO.setMerchantStoreNo(payRequestDTO.getMerchantStoreNo());  //商户门店编号,被扫时，必填，长度不超过60
		String merchantTerminalId = payRequestDTO.getMerchantTerminalId(); //商户机具终端号,被扫时，必填，长度不超过60
		if(StringUtils.isBlank(merchantTerminalId)) {
			//应银联要求，merchantTerminalId参数改为必填：被扫保持不变；主扫和公众号传WEB；其余方式传随机数--18.5.23
			if (OrderType.ACTIVESCAN.equals(payRequestDTO.getOrderType()) || OrderType.JSAPI.equals(payRequestDTO.getOrderType())) {
				merchantTerminalId = "WEB";
			} else if (!OrderType.PASSIVESCAN.equals(payRequestDTO.getOrderType())) {
				merchantTerminalId = UUID.randomUUID().toString().substring(0, 8);
			}
		}
		openPayRequestDTO.setMerchantTerminalId(merchantTerminalId);
	    openPayRequestDTO.setOrderExpireMin(payRequestDTO.getOrderExpireMin());
	    openPayRequestDTO.setPayerAccountNo(payRequestDTO.getPayerAccountNo());  //支付宝生活号与openId二选一
	    Map<String, String> extParam = payRequestDTO.getExtParam();
	    if(extParam != null) {
	    	openPayRequestDTO.setAppName(extParam.get("appName"));// 平台类型取值范围为（IOS、Android、WAP）
	    	openPayRequestDTO.setPlatForm(extParam.get("platForm"));// 应用名称（应用在App Store中唯一应用名/应用在安卓分发市场中的应用名/WAP网站名）
	    	openPayRequestDTO.setAppStatement(extParam.get("appStatement"));// 应用标示、应用声明
			//保险行业特殊险种需求,微信支付时上送身份证号和姓名 dongbo.jiao 20170319 start
			openPayRequestDTO.setUserIdCard(extParam.get("IDCardNo"));
			openPayRequestDTO.setUserTrueName(extParam.get("payerName"));
			//移除扩展参数中该字段
			extParam.remove("IDCardNo");
			extParam.remove("payerName");
			//保险行业特殊险种需求,微信支付时上送身份证号和姓名 dongbo.jiao 20170319 end

			// 银联被扫必传区域编码 bing.xiao 20180627 start
			openPayRequestDTO.setAreaInfo(extParam.get("areaCode"));
			//移除扩展参数中该字段
			extParam.remove("areaCode");
			// 银联被扫必传区域编码 bing.xiao 20180627 end

			// 报备费率  start
			openPayRequestDTO.setReportFee(extParam.get("reportFee"));
			extParam.remove("reportFee");
			// 报备费率 end
	    }
		//根据统一配置,给路由传递禁用信用卡参数 dongbo.jiao 20170905 start
		boolean isLimitCredit = ConstantUtils.isLimitCredit(payRequestDTO.getCustomerNumber());
		if(isLimitCredit){
			openPayRequestDTO.setBusinessType(PayBusinessType.OD.name());
		}
		openPayRequestDTO.setLimitCredit(isLimitCredit);
		if(OrderType.PASSIVESCAN.name().equals(payRequestDTO.getOrderType().name())){
			//added by zengzhi.han 20181016 哆啦宝被扫支付走粉丝路由功能(被扫支付没有走预路由,需要重新从数据库中获取)
			openPayRequestDTO.setMaskMerchantNo(fanRouteService.getFanRouteTradeCustomerNum(payRequestDTO.getExtParam()));
		}else{
			//根据统一配置,给路由传递禁用信用卡参数 dongbo.jiao 20170905 end
			//增加壳账户号字段 从redis缓存取出 20170918
			//added by zengzhi.han 20181016 哆啦宝公众号支付公众号支付-微信小程序也走粉丝路由,需要的交易的商户号已经在预路由放到redis中了,此处直接取就从redis中取就可以
			openPayRequestDTO.setMaskMerchantNo(RedisUtil.getMaskMerchantNo(payRequestDTO.getDealUniqueSerialNo()));
		}
		// 哆啦宝业务通道编码 added by zhijun.wang 20171212
		openPayRequestDTO.setExternalParams(payRequestDTO.getExtParam());
		return openPayRequestDTO;
	}
}
