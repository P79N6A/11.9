package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yeepay.g3.core.nccashier.biz.CashierBusinessBiz;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.core.nccashier.gateway.service.NcPayService;
import com.yeepay.g3.core.nccashier.gateway.service.RiskControlService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcPayResultProccess;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.QueryResultService;
import com.yeepay.g3.core.nccashier.service.UserProceeService;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.dto.PayResultTaskRequestDTO;

/**
 * Created by xiewei on 15-10-19.
 */
@Component("cashierBusinessBiz")
public class CashierBusinessBizImpl extends NcCashierBaseBizImpl implements CashierBusinessBiz {

	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private PaymentRequestService paymentRequestService;
	@Resource
	private UserProceeService userProceeService;
	@Resource
	private NcPayService ncPayService;
	@Resource
	private RiskControlService riskControlService;

	@Resource
	private NcPayResultProccess ncPayResultProccess;
	@Resource
	private CwhService cwhService;

	@Resource
	private NcConfigService ncConfigService;

	@Resource
	private QueryResultService queryResultService;

	
	/**
	 * 冲正
	 */
	@Override
	public ReverseResponseDTO reversePayOrder(ReverseRequestDTO reverseRequestDTO)
			throws CashierBusinessException {
		ReverseResponseDTO reverseResponseDTO = new ReverseResponseDTO();
		BeanValidator.validate(reverseRequestDTO);
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[reversePayOrder],订单方订单号=" + reverseRequestDTO.getBizOrderNum());
		paymentProcessService.reversePayOrder(reverseRequestDTO, reverseResponseDTO);
		return reverseResponseDTO;
	}


	/**
	 * 补单接口
	 * 
	 * @throws CashierBusinessException
	 * @throws Exception
	 */
	@Override
	public OrderNoticeDTO supplementPaymentOrder(String tradeSysOrderId, String tradeSysNo)
			throws CashierBusinessException {
		return queryResultService.supplyPaymentOrder(tradeSysOrderId, tradeSysNo);
	}

	@Override
	public boolean callBackNcpayMq(PayResultRequestDTO payResultRequestDTO) {
		PayResultTaskRequestDTO payResultTaskRequestDTO = new PayResultTaskRequestDTO();
		payResultTaskRequestDTO.setBizOrderNum(payResultRequestDTO.getTradeSysOrderId());
		payResultTaskRequestDTO.setBussType(payResultRequestDTO.getTradeSysNo());
		payResultTaskRequestDTO.setPayOrderNum(payResultRequestDTO.getPaymentOrderNo());
		return ncPayService.updateTaskStatus(payResultTaskRequestDTO);
	}
}
