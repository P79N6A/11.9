package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.frontend.dto.InstallmentOrderResponseDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.facade.frontend.dto.InstallmentReverseRequestDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentReverseResponseDTO;
import com.yeepay.g3.facade.merchant_platform.dto.LevelRespDTO;

/**
 * 调用FE的分期子系统的服务
 * 
 * @author duangduang
 * @since 2016-12-23
 */
public interface FrontedInstallmentService {

	/**
	 * 调用FE的分期子系统进行退款（冲正||重复退款）
	 * 
	 * @param request
	 * @return
	 */
	InstallmentReverseResponseDTO callInstallmentRefund(InstallmentReverseRequestDTO request);
	
	/**
	 * 调用分期支付下单
	 * @param request
	 * @param paymentRecord
	 * @param respDTO
	 * @return
	 */
	public InstallmentOrderResponseDTO callInstallmentCreateOrder(PaymentRequest request, PaymentRecord paymentRecord, LevelRespDTO respDTO);
	/**
	 * 查询分期付款订单
	 * @param paymentRecord
	 * @return
	 */
	public InstallmentResultMessage queryInstallmentOrder(
			PaymentRecord paymentRecord);
	
}
