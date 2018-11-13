/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;

/**
 * @author zhen.tan
 * 对接订单处理器服务
 */
public interface OrderProcessorService {

	/**
	 * 获取订单详情
	 * @param orderNo
	 * @return
	 */
	OrderDetailInfoModel getOrderDetailInfo(String orderNo);
}
