package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.CashierBusinessBiz;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.UserProceeService;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.service.CashierBusinessFacade;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * Created by xiewei on 15-10-19.
 */
@Service("cashierBusinessFacade")
public class CashierBusinessFacadeImpl implements CashierBusinessFacade {

	private static Logger logger =
			NcCashierLoggerFactory.getLogger(CashierBusinessFacadeImpl.class);

	@Resource
	private CashierBusinessBiz cashierBusinessBiz;
	@Resource
	private UserProceeService userProceeService;


	@Override
	public ReverseResponseDTO reversePayOrder(ReverseRequestDTO reverseRequestDTO)
			throws CashierBusinessException {
		return cashierBusinessBiz.reversePayOrder(reverseRequestDTO);
	}

	@Override
	public OrderNoticeDTO supplementPaymentOrder(String tradeSysOrderId, String tradeSysNo)
			throws CashierBusinessException {
		return cashierBusinessBiz.supplementPaymentOrder(tradeSysOrderId, tradeSysNo);
	}

	@Override
	public void callBackNccashierMq(PayResultRequestDTO payResultRequestDTO) {
		BeanValidator.validate(payResultRequestDTO);
		NcCashierLoggerFactory.TAG_LOCAL.set("业务方回调nccashier的mq消息通知,订单方订单号 tradeSysOrderId="
				+ payResultRequestDTO.getTradeSysOrderId() + "支付订单号paymentOrderNo="
				+ payResultRequestDTO.getPaymentOrderNo());
		cashierBusinessBiz.callBackNcpayMq(payResultRequestDTO);
	}
}
