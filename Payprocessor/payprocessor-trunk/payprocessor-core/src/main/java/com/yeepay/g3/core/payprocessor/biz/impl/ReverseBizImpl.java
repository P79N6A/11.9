package com.yeepay.g3.core.payprocessor.biz.impl;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.biz.ReverseBiz;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.cwh.param.BankCardDetailDTO;
import com.yeepay.g3.facade.payprocessor.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.PayCardType;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.PaymentStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.ReversalStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;

import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author chronos.
 * @createDate 2016/11/11.
 */
@Service
public class ReverseBizImpl extends BaseBizImpl implements ReverseBiz {

	public static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(ReverseBizImpl.class);

	@Override
	public ReverseResponseDTO reverseRequest(ReverseRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[冲正请求] - [recordNo = " + requestDTO.getRecordNo() + "]");
		ReverseResponseDTO responseDTO = new ReverseResponseDTO();
		try {
			PayRecord payRecord = payRecordService.queryRecordById(requestDTO.getRecordNo());
			PaymentRequest payment = paymentRequestService.selectByPrimaryKey(payRecord.getRequestId());
			reverseProcess(payment, payRecord, requestDTO, responseDTO);
			makeUpResponse(responseDTO, payment, payRecord);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(responseDTO, th);
		} finally {
			responseDTO.setRecordNo(requestDTO.getRecordNo());
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}

	/**
	 * 联合校验
	 * 
	 * @param payment
	 * @param record
	 */
	private void reverseProcess(PaymentRequest payment, PayRecord record, ReverseRequestDTO requestDTO,
								ReverseResponseDTO responseDTO) {
		if (!requestDTO.getRequestSystem().equals(record.getRequestSystem())) {
			throw new PayBizException(ErrorCode.P9001003, "不能跨系统冲正");
		}
		// 判断是否需要发起冲正
		if (PaymentStatusEnum.SUCCESS.name().equals(payment.getPayStatus())
				|| TrxStatusEnum.SUCCESS.name().equals(record.getStatus())){
			// 订单已经成功,不允许冲正,冲正失败
			dealResponseStatus(PaymentStatusEnum.SUCCESS, ReversalStatusEnum.FAILURE, responseDTO);
			return;
		}
		if (PaymentStatusEnum.REVERSE.name().equals(payment.getPayStatus())
				|| TrxStatusEnum.REVERSE.name().equals(record.getStatus())){
			// 订单已经冲正,返回冲正成功
			dealResponseStatus(PaymentStatusEnum.getPayStatus(payment.getPayStatus()),
					ReversalStatusEnum.SUCCESS, responseDTO);
			return;
		}
		int rows = payRecordService.updatePaymentStatus(record.getRecordNo(), TrxStatusEnum.REVERSE.name(),
				Arrays.asList(new String[]{TrxStatusEnum.DOING.name(), TrxStatusEnum.FAILUER.name(),
						TrxStatusEnum.REVERSE.name()}));
		if (rows == 1) {
			dealResponseStatus(PaymentStatusEnum.REVERSE, ReversalStatusEnum.SUCCESS, responseDTO);
		} else {
			dealResponseStatus(PaymentStatusEnum.SUCCESS, ReversalStatusEnum.FAILURE, responseDTO);
		}
	}

	private void makeUpResponse(ReverseResponseDTO responseDTO, PaymentRequest payment, PayRecord record) {
		responseDTO.setOutTradeNo(payment.getOutTradeNo());
		responseDTO.setCustomerNumber(payment.getCustomerNo());
		responseDTO.setRecordNo(record.getRecordNo());
		responseDTO.setOrderNo(payment.getOrderNo());
		responseDTO.setDealUniqueSerialNo(payment.getDealUniqueSerialNo());
		responseDTO.setBankId(record.getBankId());
		responseDTO.setBankOrderNo(record.getBankOrderNo());
		responseDTO.setBankSeq(record.getBankSeq());
		responseDTO.setBankTrxId(record.getBankTrxId());
		responseDTO.setConfirmTime(record.getPayTime());
		responseDTO.setCost(record.getCost());
		responseDTO.setFrpCode(record.getFrpCode());
		responseDTO.setOrderStatus(OrderSystemStatusEnum.valueOf(payment.getOrderSystemStatus()));
		responseDTO.setPayCardType(PayCardType.getPayCardType(record.getCardType()));
		responseDTO.setPayOrderType(PayOrderType.getOrderType(record.getPayOrderType()));
		responseDTO.setUserFee(record.getUserFee());
		// 卡账户信息
		if (StringUtils.isNotBlank(record.getCardId())) {
			BankCardDetailDTO bankCardDetailDTO = bankCardService.getBankCardDetail(record.getCardId());
			if (bankCardDetailDTO != null && bankCardDetailDTO.getBaseInfo() != null) {
				responseDTO.setAccountNo(bankCardDetailDTO.getBaseInfo().getCardNo());
				responseDTO.setAccountName(bankCardDetailDTO.getBaseInfo().getOwner());

				responseDTO.setMobile(StringUtils.isNotBlank(bankCardDetailDTO.getBaseInfo().getBankMobile())
						? bankCardDetailDTO.getBaseInfo().getBankMobile()
						: bankCardDetailDTO.getBaseInfo().getYbMobile());
			}
		}
	}

	private void dealResponseStatus(PaymentStatusEnum payStatus, ReversalStatusEnum reversalStatus,
			ReverseResponseDTO responseDTO) {
		responseDTO.setPayStatus(payStatus);
		responseDTO.setReverseStatus(reversalStatus);
	}
}
