package com.yeepay.g3.app.fronend.app.utils;

import com.yeepay.g3.app.fronend.app.enumtype.OrderStatus;
import com.yeepay.g3.app.fronend.app.enumtype.OrderType;
import com.yeepay.g3.app.fronend.app.enumtype.Source;
import com.yeepay.g3.facade.frontend.enumtype.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询时会使用到的
 */
public class MemberQueryUtil {
	private static Map cacheMap = new HashMap<String,Object>();

	private static final String CARD_TYPE = "CARD_TYPE";
	public static final String REQUEST_SYSTEM_MAP = "REQUEST_SYSTEM_MAP";
	public static final String BANK_LIST = "BANK_LIST";

	private static final String PAY_STATUS = "PAY_STATUS";
	private static final String ORDER_TYPE = "ORDER_TYPE";
	private static final String NOTIFY_STATUS = "NOTIFY_STATUS";
	private static final String REFUND_STATUS = "REFUND_STATUS";
	private static final String REFUND_TYPE = "REFUND_TYPE";
	private static final String PLATFORM = "PLATFORM";
	private static final String PAY_PRODUCT = "PAY_PRODUCT";
	private static final String ORDER_SYSTEM = "ORDER_SYSTEM";

	 /**
     * 获取鉴权订单状态
     * @return
     */
	public static Map<String, String> getMpayOrderStatus() {
		Map<String, String> map = null;
		map = new LinkedHashMap<String, String>();
		map.put(OrderStatus.INIT.name(), "初始化");
		map.put(OrderStatus.CANCEL.name(), "取消");
		map.put(OrderStatus.CLOSE.name(), "关闭");
		map.put(OrderStatus.REFUND.name(), "退款");
		map.put(OrderStatus.SUCCESS.name(), "成功");
		map.put(OrderStatus.TOSETTLE.name(), "结账");
		return map;
	}
	
	public static Map<String, String> getMpayOrderType() {
		Map<String, String> map = null;
		map = new LinkedHashMap<String, String>();
		map.put(OrderType.AUTHCODEPAY.name(), "商户扫码");
		map.put(OrderType.SCANCODEPAY.name(), "用户扫码或者公众号支付");
		return map;
	}
	
	public static Map<String, Source> getMpaySource() {
		Map<String, Source> map = null;
		map = new LinkedHashMap<String, Source>();
		for (Source source : Source.values()) {
			source.setSelect(false);
		}
		map.put(Source.GATEWAY.name(), Source.GATEWAY);
		map.put(Source.GENERAL.name(), Source.GENERAL);
		map.put(Source.MPPAY.name(), Source.MPPAY);
		map.put(Source.ONLINE.name(), Source.ONLINE);
		map.put(Source.POS.name(), Source.POS);
		map.put(Source.SKB.name(), Source.SKB);
		map.put(Source.ZGT.name(), Source.ZGT);
		return map;
	}
	public static Map<String, String> getMpaySourceDesc() {
		Map<String, String> map = null;
		map = new LinkedHashMap<String, String>();
		map.put(Source.GATEWAY.name(), Source.GATEWAY.getDesc());
		map.put(Source.GENERAL.name(), Source.GENERAL.getDesc());
		map.put(Source.MPPAY.name(), Source.MPPAY.getDesc());
		map.put(Source.ONLINE.name(), Source.ONLINE.getDesc());
		map.put(Source.POS.name(), Source.POS.getDesc());
		map.put(Source.SKB.name(), Source.SKB.getDesc());
		map.put(Source.ZGT.name(), Source.ZGT.getDesc());
		return map;
	}

	/**
	 * 支付状态
	 * @return
	 */
	public static Map<String, String> getFrontPayStatus(){
		Map<String, String> map = (Map<String, String>) cacheMap.get(PAY_STATUS);
		if (map!=null)
			return map;
		map = new HashMap<String, String>();
		map.put(PayStatusEnum.INIT.name(),"未支付");
		map.put(PayStatusEnum.SUCCESS.name(),"支付成功");
		map.put(PayStatusEnum.FAILURE.name(),"支付失败");
		cacheMap.put(PAY_STATUS,map);
		return map;
	}

	/**
	 * 订单类型
	 * @return
	 */
	public static Map<String,String> getFrontOrderType(){
		Map<String, String> map = (Map<String, String>) cacheMap.get(ORDER_TYPE);
		if (map!=null)
			return map;
		map = new HashMap<String, String>();
		map.put(com.yeepay.g3.facade.frontend.enumtype.OrderType.APP.name(),"APP支付");
		map.put(com.yeepay.g3.facade.frontend.enumtype.OrderType.H5APP.name(),"H5支付");
		map.put(com.yeepay.g3.facade.frontend.enumtype.OrderType.ACTIVESCAN.name(),"主扫支付");
		map.put(com.yeepay.g3.facade.frontend.enumtype.OrderType.JSAPI.name(),"公众号支付");
		map.put(com.yeepay.g3.facade.frontend.enumtype.OrderType.PASSIVESCAN.name(),"被扫支付");
		map.put(com.yeepay.g3.facade.frontend.enumtype.OrderType.ALIPAYSCAN.name(),"ZFB主扫");
		map.put(com.yeepay.g3.facade.frontend.enumtype.OrderType.SDK.name(),"SDK");
		map.put(NetOrderType.SALE.name(),"网银");
		map.put("ONLINE","分期线上支付");
		map.put("OFFLINE", "分期线下支付");
		cacheMap.put(ORDER_TYPE,map);
		return map;
	}

	/**
	 * 通知状态
	 * @return
	 */
	public static Map<String, String> getFrontNotifyStatus(){
		Map<String, String> map = (Map<String, String>) cacheMap.get(NOTIFY_STATUS);
		if (map!=null)
			return map;
		map = new HashMap<String, String>();
		map.put(NotifyStatusEnum.INIT.name(),"待通知");
		map.put(NotifyStatusEnum.SUCCESS.name(),"通知成功");
		map.put(NotifyStatusEnum.FAILURE.name(),"通知失败");
		cacheMap.put(NOTIFY_STATUS,map);
		return map;
	}

	/**
	 * 退款状态
	 * @return
	 */
	public static Map<String, String> getFrontRefundStatus(){
		Map<String, String> map = (Map<String, String>) cacheMap.get(REFUND_STATUS);
		if (map!=null)
			return map;
		map = new HashMap<String, String>();
		map.put(RefundStatusEnum.NONE.name(),"未退款");
		map.put(RefundStatusEnum.INIT.name(),"已受理");
		map.put(RefundStatusEnum.SEND.name(),"已发送");
		map.put(RefundStatusEnum.SUCCESS.name(),"退款成功");
		map.put(RefundStatusEnum.FAIL.name(),"退款失败");
		cacheMap.put(REFUND_STATUS,map);
		return map;
	}
	public static Map<String, String> getFrontRefundType(){
		Map<String, String> map = (Map<String, String>) cacheMap.get(REFUND_TYPE);
		if (map!=null)
			return map;
		map = new HashMap<String, String>();
		map.put(RefundType.MANUAL.name(),"人工退款");
		map.put(RefundType.TIMEOUT.name(),"过期退款");
		map.put(RefundType.REPEATPAY.name(),"重复支付退款");
		cacheMap.put(REFUND_TYPE,map);
		return map;
	}

	public static Map<String, String> getFrontPlatform(){
		Map<String, String> map = (Map<String, String>) cacheMap.get(PLATFORM);
		if (map!=null)
			return map;
		map = new HashMap<String, String>();
		map.put(PlatformType.WECHAT.name(),"WX");
		map.put(PlatformType.ALIPAY.name(),"ZFB");
		map.put("NET", "NET");
		map.put("CFL", "CFL");
		map.put(PlatformType.OPEN_UPOP.name(), "UPOP");// 银联钱包added by zhijun.wang
		map.put(PlatformType.JD.name(), "JD");// 京东钱包added by zhijun.wang 2017-06-13
		map.put(PlatformType.QQ.name(), "QQ");// QQ钱包 added by zhijun.wang 2017-12-05
		cacheMap.put(PLATFORM,map);
		return map;
	}

	public static Map<String,String> getFrontBankList(){
		return (Map<String, String>) FrontEndConfigUtils.getSysConfig(BANK_LIST);
	}

	public static Map<String,String> getFrontCardType(){
		return (Map<String, String>) FrontEndConfigUtils.getSysConfig(CARD_TYPE);
	}

	public static Map<String,String> getFrontRequestSystem(){
		return (Map<String, String>) FrontEndConfigUtils.getSysConfig(REQUEST_SYSTEM_MAP);
	}

	public static Map<String, String> getFrontPayProduct(){
		return (Map<String, String>) FrontEndConfigUtils.getSysConfig(PAY_PRODUCT);
    }

    public static Map<String, String> getFrontOrderSystem() {
		return (Map<String, String>) FrontEndConfigUtils.getSysConfig(ORDER_SYSTEM);
	}
}
