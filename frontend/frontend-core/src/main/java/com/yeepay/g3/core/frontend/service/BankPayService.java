package com.yeepay.g3.core.frontend.service;

import com.yeepay.g3.core.frontend.dto.BankQueryRequestDTO;
import com.yeepay.g3.core.frontend.dto.BankQueryResponseDTO;

/**
 * 请求银行子系统微信支付接口
 * @author TML
 */
public interface BankPayService{
	
	/**
	 * 用户主扫、公众号支付
	 */
//	public void H5appPay(PayOrder payOrder,PayRecord payRecord, BasicRequestDTO basicRequestDTO);
	
	/**
	 * 开放支付查单接口
	 * @param bankQueryRequestDTO
	 * @return
	 */
	public BankQueryResponseDTO queryOpenPayOrder(BankQueryRequestDTO bankQueryRequestDTO);
	
//	/**
//	 * 网银查单接口
//	 * @param bankQueryRequestDTO
//	 * @return
//	 */
//	public BankQueryResponseDTO queryOnlinePayOrder(BankQueryRequestDTO bankQueryRequestDTO);
	
//	/**
//	 * 获取支付订单号
//	 * @return
//	 * @param payOrder
//	 */
//	public String getPayOrderNo(PayOrder payOrder);

//	/**
//	 * 主扫接口--支付宝
//	 * @param payOrder
//	 * @param payRecord
//	 */
//	void activeScanMPay(PayOrder payOrder, PayRecord payRecord, BasicRequestDTO basicRequestDTOs);
}
