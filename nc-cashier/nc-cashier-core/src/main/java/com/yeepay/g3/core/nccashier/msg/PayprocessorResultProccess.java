package com.yeepay.g3.core.nccashier.msg;


import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.responseDto.PpPayResultResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class PayprocessorResultProccess extends NcCashierBaseService {
	protected static final Logger logger =
			NcCashierLoggerFactory.getLogger(NcCashierBaseService.class);

	@Resource
	private PaymentProcessService paymentProcessService;

	public Boolean processForCallBack(PpPayResultResponseDTO resultMessage) {
		//先存储数据库
		updateRecordByPpMqResult(resultMessage);
		//更新自己的Redis  超时时间60s
		RedisTemplate.setCacheObjectSumValue(CommonUtil.PP_HAS_PAY_RESULT_KEY + resultMessage.getRecordNo(),"READY",60*1000);
		return true;
	}


	private void updateRecordByPpMqResult(PpPayResultResponseDTO resultMessage){
		//根据返回的DTO查出record
		PaymentRecord paymentRecord = paymentProcessService.findRecordByMerchantOrderId(resultMessage.getOutTradeNo(),resultMessage.getCustomerNo(),resultMessage.getRecordNo());
		if (OrderSystemStatusEnum.SUCCESS.name().equals(resultMessage.getOrderSystemStatus()) ) {
			paymentRecord.setState(PayRecordStatusEnum.SUCCESS);
			paymentRecord.setCardInfoId(resultMessage.getCardId());
			paymentRecord.setActualAmount(resultMessage.getFirstPayAmount());
		} else if (OrderSystemStatusEnum.REVERSE.name().equals(resultMessage.getOrderSystemStatus())) {
			paymentRecord.setState(PayRecordStatusEnum.TRADEREVERSE);
		} else if (TrxStatusEnum.FAILUER.name().equals(resultMessage.getTrxStatus())) {
			paymentRecord.setState(PayRecordStatusEnum.FAILED);
			CashierBusinessException error = CommonUtil.handleException(SysCodeEnum.PP.name(), resultMessage.getResponseCode(),
					resultMessage.getResponseMsg());
			paymentRecord.setErrorCode(error.getDefineCode());
			paymentRecord.setErrorMsg(error.getMessage());
		}
		paymentProcessService.updateRecord(paymentRecord);
	}


}
