package com.yeepay.g3.core.nccashier.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.CashierBindCardService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.NcCashierNoticeService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.service.NcCashierCallBackFacade;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

@Service
public class NcCashierNoticeServiceImpl extends NcCashierBaseService
		implements NcCashierNoticeService {
	@Resource
	private PaymentProcessService paymentProcessService;
	@Resource
	private CashierBindCardService cashierBindCardService;

	protected static final Logger logger =
			NcCashierLoggerFactory.getLogger(NcCashierNoticeServiceImpl.class);

	@Override
	public void sendNoticeToTradeSys(PaymentRequest paymentRequest) {
		List<PaymentRecord> recordList = paymentProcessService.findRecordList(
				paymentRequest.getTradeSysOrderId(), paymentRequest.getTradeSysNo());
		if (CollectionUtils.isEmpty(recordList)) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}

		OrderNoticeDTO orderNoticeDTO = orderNoticeTransfer(paymentRequest, recordList);
		Map<String, String> businessInfoMap =
				CommonUtil.getSysConfigFrom3G(Constant.TRADE_CALLBACK_WAY+paymentRequest.getTradeSysNo());

		if (null == businessInfoMap) {
			throw CommonUtil.handleException(Errors.BUSINESS_NOT_SUPPORT);
		}
		String backNotifyWay = businessInfoMap.get(CommonUtil.BACK_NOTIFY_WAY);
		String backNotifyAdress = businessInfoMap.get(CommonUtil.BACK_NOTIFY_ADRESS);
		if(StringUtils.isBlank(backNotifyWay)||StringUtils.isBlank(backNotifyAdress)) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (CommonUtil.MQ.equals(backNotifyWay)) {
			send(backNotifyAdress, orderNoticeDTO);
		} else if (CommonUtil.HESSIAN.equals(backNotifyWay)) {
			sendTradePayResult(backNotifyAdress, orderNoticeDTO);
		}
	}
	@Override
	public void sendNoticeTradeByHessian(PaymentRequest paymentRequest) {
		List<PaymentRecord> recordList = paymentProcessService.findRecordList(
				paymentRequest.getTradeSysOrderId(), paymentRequest.getTradeSysNo());
		if (CollectionUtils.isEmpty(recordList)) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}

		OrderNoticeDTO orderNoticeDTO = orderNoticeTransfer(paymentRequest, recordList);
		Map<String, String> businessInfoMap =
				CommonUtil.getSysConfigFrom3G(Constant.TRADE_CALLBACK_WAY+paymentRequest.getTradeSysNo());

		if (null == businessInfoMap) {
			throw CommonUtil.handleException(Errors.BUSINESS_NOT_SUPPORT);
		}
		String backNotifyWay = businessInfoMap.get(CommonUtil.BACK_NOTIFY_WAY);
		String backNotifyAdress = businessInfoMap.get(CommonUtil.BACK_NOTIFY_ADRESS);
		if(StringUtils.isBlank(backNotifyWay)||StringUtils.isBlank(backNotifyAdress)) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		 if (CommonUtil.HESSIAN.equals(backNotifyWay)) {
			sendTradePayResult(backNotifyAdress, orderNoticeDTO);
		}
	}
	private void send(String target, OrderNoticeDTO o) {
		try {
			amqpTemplate.convertAndSend(target, o);
			logger.info("给业务系统发送MQ消息OK, tradeSysNo:{},tradeSysOrderId:{}", o.getTradeSysNo(),
					o.getTradeSysOrderId());
		} catch (Exception e) {
			logger.error("发给业务系统发送MQ消息失败 tradeSysNo:"+ o.getTradeSysNo()+",tradeSysOrderId:"+o.getTradeSysOrderId(),
					e);
		}
	}

	private boolean sendTradePayResult(String backNotifyAdress, OrderNoticeDTO o) {
		boolean callBackResult = false;
		try {
			logger.info("[通知业务方系统|Hessian] tradeSysNo:{},tradeSysOrderId:{},orderNoticeDTO:{}",
					o.getTradeSysNo(), o.getTradeSysOrderId(), o);
			NcCashierCallBackFacade ncCashierCallBackFacade =
					getNcCashierCallBackFacade(backNotifyAdress);
			if (null != ncCashierCallBackFacade) {
				callBackResult = ncCashierCallBackFacade.comfirmCallBack(o);
			}
		} catch (Exception e) {
			logger.error("[通知业务方系统|Hessian] tradeSysNo:{},tradeSysOrderId:{}", o.getTradeSysNo(),
					o.getTradeSysOrderId(), e);
		}
		return callBackResult;
	}

	private NcCashierCallBackFacade getNcCashierCallBackFacade(String backNotifyAdress) {
		NcCashierCallBackFacade ncCashierCallBackFacade = null;
		try {
			if (StringUtils.isNotEmpty(backNotifyAdress)) {
				ncCashierCallBackFacade = RemoteServiceFactory.getService(backNotifyAdress,
						RemotingProtocol.HESSIAN, NcCashierCallBackFacade.class);
			}
		} catch (Exception e) {
			logger.error("获取NcCashierCallBackFacade服务异常:{}", e);
		}
		return ncCashierCallBackFacade;
	}

}
