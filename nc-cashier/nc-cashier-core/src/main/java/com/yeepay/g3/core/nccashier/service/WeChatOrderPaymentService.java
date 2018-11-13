/**
 * 
 */
package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayRequestDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

/**
 * @author zhen.tan
 * 微信支付服务
 *
 */
public interface WeChatOrderPaymentService {

	/**
	 * 微信支付下单业务校验
	 * @param requestDto
	 * @return
	 * @throws CashierBusinessException
	 */
	public CombinedPaymentDTO validatePayBusinInfo(WeChatPayRequestDTO requestDto)
			throws CashierBusinessException;
	
	/**
	 * 微信支付创建record
	 * @param requestDto
	 * @param combinedPaymentDto
	 */
	public void weChatCreateRecord(WeChatPayRequestDTO requestDto,CombinedPaymentDTO combinedPaymentDto);
	
	/**
	 * 调取FE下单（微信、支付宝、分期）
	 * 
	 * @param requestDto
	 * @param combinedPaymentDto
	 * @return
	 */
	public String callFEPay(WeChatPayRequestDTO requestDto,CombinedPaymentDTO combinedPaymentDto);


	/**
	 * 微信预路由接口
	 * @return
	 */
	public JsapiRouteResponseDTO weChatPreRoute(PaymentRequest paymentRequest, String type);
}
