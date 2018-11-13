package com.yeepay.g3.core.payprocessor.biz.impl;

import com.yeepay.g3.core.payprocessor.biz.FePayBiz;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PPTracer;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.dto.*;

import org.springframework.stereotype.Service;

/**
 * @author chronos.
 * @createDate 2016/11/9.
 */
@Service("fePayBiz")
public class FePayBizImpl extends BaseBizImpl implements FePayBiz {

	public static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(FePayBizImpl.class);

	@Override
	public OpenPayResponseDTO openPay(OpenPayRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[开放支付请求] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		OpenPayResponseDTO response = new OpenPayResponseDTO();
		PayRecord record=null;
		try {
			PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			if(record.isCombinedPay()) {
				createCombPayRecord(requestDTO, record, payment);
			}
			response = frontEndService.openPay(requestDTO, record);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record==null? null:record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}

	@Override
	public NetPayResponseDTO netPay(NetPayRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[网银支付请求] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		NetPayResponseDTO response = new NetPayResponseDTO();
		PayRecord record=null;
		try {
			PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			if(record.isCombinedPay()) {
				createCombPayRecord(requestDTO, record, payment);
			}
			response = frontEndService.onlinePay(requestDTO, record);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record==null? null:record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}

	@Override
	public OpenPrePayResponseDTO openPrePay(OpenPrePayRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[开放支付预路由] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		OpenPrePayResponseDTO response = new OpenPrePayResponseDTO();
		try {
			response = frontEndService.openPrePay(requestDTO);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}
	
	@Override
	public CflPayResponseDTO cflPay(CflPayRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[分期支付] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		CflPayResponseDTO response = new CflPayResponseDTO();
		PayRecord record=null;
		try {
			PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			if(record.isCombinedPay()) {
				createCombPayRecord(requestDTO, record, payment);
			}
			response = frontEndService.cflPay(requestDTO, record);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record==null? null:record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}
	
	@Override
	public PassiveScanPayResponseDTO passiveScanPay(PassiveScanPayRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[被扫请求] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		PassiveScanPayResponseDTO response = new PassiveScanPayResponseDTO();
		PayRecord record=null;
		try {
			PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			if(record.isCombinedPay()) {
				createCombPayRecord(requestDTO, record, payment);
			}
			response = frontEndService.passiveScanPay(requestDTO, record);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record==null? null:record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	
	}
}
