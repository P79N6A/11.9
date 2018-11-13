
package com.yeepay.g3.core.payprocessor.biz.impl;

import java.math.BigDecimal;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.biz.CancelBiz;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.enumtype.CancelStatusEnum;
import com.yeepay.g3.core.payprocessor.param.CancelRequestParam;
import com.yeepay.g3.core.payprocessor.param.CancelResponse;
import com.yeepay.g3.facade.payprocessor.enumtype.PaymentStatusEnum;

/**
 * Description:
 * 
 * @author peile.fan
 * @since:2017年1月5日 下午3:23:19
 */
@Service
public class CancelBizImpl extends BaseBizImpl implements CancelBiz {

	@Override
	public CancelResponse cancelRequest(CancelRequestParam requestDTO) {
		CancelResponse response = new CancelResponse();
		cancelProcess(requestDTO, response);
		return response;
	}

	private void cancelProcess(CancelRequestParam requestDTO, CancelResponse response) {
		PaymentRequest oldRequest = paymentRequestService.queryBySystemAndOrderNo(requestDTO.getOrderSystem(),
				requestDTO.getOrderNo());
		if (oldRequest != null) {
			cancelRequest(response, oldRequest);
		} else {
			try {
				insertCancelRequest(requestDTO, response);
			} catch (DuplicateKeyException e) {
				// 获取刚保存的请求订单
				PaymentRequest justexistsRequest = paymentRequestService
						.queryBySystemAndOrderNo(requestDTO.getOrderSystem(), requestDTO.getOrderNo());
				cancelRequest(response, justexistsRequest);
			}
		}
	}

	private void insertCancelRequest(CancelRequestParam requestDTO, CancelResponse response) {
		PaymentRequest payment = buildPaymentRequest(requestDTO);
		paymentRequestService.createPaymentRequest(payment);
		dealResponseStatus(PaymentStatusEnum.REVERSE, CancelStatusEnum.SUCCESS, response);
	}

	private void cancelRequest(CancelResponse response, PaymentRequest oldRequest) {
		// 已经存在，判断状态
		// 支付成功，撤销业务不处理，由回调逻辑处理，返回撤销失败
		if (PaymentStatusEnum.SUCCESS.name().equals(oldRequest.getPayStatus())) {
			dealResponseStatus(PaymentStatusEnum.SUCCESS, CancelStatusEnum.FAILURE, response);
		} else if (PaymentStatusEnum.REVERSE.name().equals(oldRequest.getPayStatus())) {
			dealResponseStatus(PaymentStatusEnum.REVERSE, CancelStatusEnum.SUCCESS, response);
		} else {
			int row = paymentRequestService.updateRequestToReverse(oldRequest.getId(), PaymentStatusEnum.DOING.name());
			if (row == 1) {
				dealResponseStatus(PaymentStatusEnum.REVERSE, CancelStatusEnum.SUCCESS, response);
			} else {
				dealResponseStatus(PaymentStatusEnum.SUCCESS, CancelStatusEnum.FAILURE, response);
			}
		}
	}

	private void dealResponseStatus(PaymentStatusEnum payStatus, CancelStatusEnum cancelStatus,
			CancelResponse responseDTO) {
		responseDTO.setPayStatus(payStatus);
		responseDTO.setCancelStatus(cancelStatus);
	}

	protected PaymentRequest buildPaymentRequest(CancelRequestParam requestDTO) {
		PaymentRequest payment = new PaymentRequest();
		payment.setOrderSystem(requestDTO.getOrderSystem());
		payment.setAmount(new BigDecimal(requestDTO.getAmount()));
		payment.setCustomerNo(requestDTO.getCustomerNumber());
		payment.setCustomerName(requestDTO.getCustomerName());
		payment.setDealUniqueSerialNo(requestDTO.getDealUniqueSerialNo());
		payment.setOrderNo(requestDTO.getOrderNo());
		payment.setPayStatus(PaymentStatusEnum.REVERSE.name());
		payment.setOutTradeNo(requestDTO.getOutTradeNo());
		return payment;
	}

}
