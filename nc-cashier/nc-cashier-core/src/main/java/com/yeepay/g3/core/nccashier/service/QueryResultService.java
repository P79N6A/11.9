/**
 * 
 */
package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.facade.nccashier.dto.APIResultQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultQuerySignListenRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultQuerySignListenResponseDTO;

/**
 * 支付结果查询service
 * 
 * @author zhen.tan
 * @since：2016年5月26日 上午12:27:39
 *
 */
public interface QueryResultService {

	/**
	 * 校验支付结果查询业务信息
	 * 
	 * @param queryRequest
	 * @return
	 */
	public CombinedPaymentDTO validateQueryBusinInfo(CashierQueryRequestDTO queryRequest);

	/**
	 * 补充支付结果
	 * 
	 * @param combinedPaymentDto
	 * @param queryRequest
	 * @param response
	 */
	public void supplyQureyResult(CombinedPaymentDTO combinedPaymentDto, CashierQueryRequestDTO queryRequest,
			CashierQueryResponseDTO response);

	/**
	 * 调用ncpay查询来补单
	 * 
	 * @param tradeSysOrderId
	 * @param tradeSysNo
	 * @return
	 */
	public OrderNoticeDTO supplyPaymentOrder(String tradeSysOrderId, String tradeSysNo);

	/**
	 * 支付处理器是否可查标识监听处理
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	void listenCanPayResultQuery(PayResultQuerySignListenRequestDTO request,
			PayResultQuerySignListenResponseDTO response) throws InterruptedException;

	/**
	 * 组装商户支付成功回调地址
	 * 
	 * @param paymentRequest
	 * @return
	 */
	String buildFrontCallbackUrl(PaymentRequest paymentRequest);

	/**
	 * 查询交易结果
	 * 
	 * @param paymentRequest
	 * @param response
	 * @return
	 */
	void queryOrderResult(OrderDetailInfoModel orderInfo, APIResultQueryResponseDTO response);

}
