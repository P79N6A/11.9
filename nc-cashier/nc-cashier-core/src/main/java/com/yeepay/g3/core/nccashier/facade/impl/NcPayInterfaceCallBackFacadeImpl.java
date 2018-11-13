package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcPayResultProccess;
import com.yeepay.g3.facade.ncpay.dto.ConfrimPayCallBackDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.facade.ncpay.facade.NcPayInterfaceCallBackFacade;
import com.yeepay.g3.utils.common.log.Logger;

@Service
public class NcPayInterfaceCallBackFacadeImpl implements NcPayInterfaceCallBackFacade {

	private static final Logger logger =
			NcCashierLoggerFactory.getLogger(NcPayInterfaceCallBackFacadeImpl.class);

	@Resource
	private NcPayResultProccess ncPayResultProccess;

	@Override
	public Boolean confirmPayCallBack(ConfrimPayCallBackDTO confrimPayCallBackDTO) {
		if (confrimPayCallBackDTO == null) {
			return false;
		}
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[ncpay接口回调|confirmPayCallBack] - [orderSysId="
					+ confrimPayCallBackDTO.getBizOrderNum() + " orderSysNo="
					+ confrimPayCallBackDTO.getBizType() + "]");
			PaymentResultMessage resultMessage = null;
			String jsonMessage = "";
			jsonMessage = confrimPayCallBackDTO.getMessage();
			logger.info("hessian接收到消息:{}", jsonMessage);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			resultMessage = objectMapper.readValue(jsonMessage, PaymentResultMessage.class);
			return ncPayResultProccess.processForCallBack(resultMessage);
		} catch (Throwable e) {
			logger.error("处理ncpay回调失败", e);
			return false;
		} finally {
			NcCashierLoggerFactory.TAG_LOCAL.set(null);
		}
	}

}
