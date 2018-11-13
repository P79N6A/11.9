package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;

/**
 * Description PackageName: com.yeepay.g3.core.nccashier.adapterservice
 * 交易订单信息接入服务
 * 
 * @author pengfei.chen
 * @since 17/4/12 15:57
 */
public interface OrderInfoAccessService {

	/**
	 * 获取订单详情，并对返回值的判空进行处理了
	 * 
	 * @param token
	 * @param orderSysConfigDTO
	 * @return
	 */
	public OrderDetailInfoModel getOrderDetailInfoModel(String token, OrderSysConfigDTO orderSysConfigDTO);
}
