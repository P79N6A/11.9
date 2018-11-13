/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.frontend.dto.*;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.enumtype.RefundType;
import com.yeepay.g3.facade.merchant_platform.dto.LevelRespDTO;

/**
 * @author zhen.tan
 * 微信pay服务
 *
 */
public interface FrontEndService {
	
	/**
	 * 调用FE退款
	 * @param merchantAccountNo
	 * @param refundType
	 * @param requestId
	 * @param platformType
	 * @return
	 */
	public FrontendRefundResponseDTO callRefund(String merchantAccountNo,RefundType refundType,String requestId,PlatformType platformType);
	
	/**
	 * 查询FE支付结果
	 * @param tradeSysOrderId
	 * @param payType
	 * @return
	 */
	public FrontendQueryResponseDTO queryPaymentOrder(String tradeSysOrderId,String payType);


	/**
	 * 调取fe 的统一下单接口
	 * @param payRequestDTO
	 * @return
	 */
	public PayResponseDTO frontendOpenPay(PayRequestDTO payRequestDTO);

}
