package com.yeepay.g3.core.payprocessor.biz.impl;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.biz.PersonalMemberPayBiz;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;

import java.math.BigDecimal;

@Service
public class PersonalMemberPayBizImpl extends BaseBizImpl implements
		PersonalMemberPayBiz {

	public static final PayLogger logger = (PayLogger) PayLoggerFactory
			.getLogger(PersonalMemberPayBizImpl.class);

	@Override
	public PersonalMemberSyncPayResponseDTO personalMemberSyncPay(
			PersonalMemberSyncPayRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[个人会员支付请求] - [DealUniqueSerialNo = "
				+ requestDTO.getDealUniqueSerialNo() + "]");
		PersonalMemberSyncPayResponseDTO response = new PersonalMemberSyncPayResponseDTO();
		PayRecord record = null;
		try {
			PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			if(record.isCombinedPay()) {
				createCombPayRecord(requestDTO, record, payment);
			}
			String flowId = personalMemberPayService.syncPay(requestDTO, record,response);
			handlePayResult(flowId,record,response);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record == null ? null : record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			response.setBasicProductCode(requestDTO.getBasicProductCode());
			response.setRetailProductCode(requestDTO.getRetailProductCode());
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}

	private void handlePayResult(String flowId,
			PayRecord record, PersonalMemberSyncPayResponseDTO response) {
		if(TrxStatusEnum.FAILUER.equals(response.getTrxStatus())) {
			return;
		}
		
		// 返回成功
		TrxStatusEnum recordStatus = null;
		// 组合支付
		if(record.isCombinedPay()) {
			recordStatus = personalMemberPayResultProccess.processForSyncPaySuccessComb(record.getRecordNo(), flowId);
			CombResponseDTO combResponseDTO = combAbstractService.bulidCombResponse(record.getRecordNo());
			response.setCombResponseDTO(combResponseDTO);
			response.setFirstPayAmount(record.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		}else {
			recordStatus = personalMemberPayResultProccess.processForSyncPaySuccess(record.getRecordNo(),flowId);
		}
		response.setProcessStatus(ProcessStatus.SUCCESS);
		response.setTrxStatus(recordStatus);
		response.setPaymentFlowNo(flowId);
		response.setRecordNo(record.getRecordNo());

	}

}
