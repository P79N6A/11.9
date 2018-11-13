/**
 * 
 */
package com.yeepay.g3.core.frontend.enumtype;

import com.yeepay.g3.facade.frontend.enumtype.OrderType;

/**
 * mpay 支付类型枚举
 * @author TML
 */
@Deprecated
public enum TradeType {

	/**
	 * 公众号支付
	 */
	JSAPI,
	
	/**
	 * 主扫支付
	 */
	NATIVE;
	
	/**
	 * 转换获取类型
	 * @param orderType
	 * @return
	 */
	public static TradeType getTradeType(OrderType orderType) {
		if (OrderType.ACTIVESCAN.equals(orderType)) {
			return TradeType.NATIVE;
		} else if (OrderType.JSAPI.equals(orderType)) {
			return TradeType.JSAPI;
		}
		return null;
	}
	
}
