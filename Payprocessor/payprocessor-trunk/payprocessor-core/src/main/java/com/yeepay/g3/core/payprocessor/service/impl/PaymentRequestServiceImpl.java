
package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.service.PaymentRequestService;
import com.yeepay.g3.utils.persistence.OptimisticLockingException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peile.fan
 *
 */
@Service
public class PaymentRequestServiceImpl extends AbstractService implements PaymentRequestService {

	@Override
	public PaymentRequest queryBySystemAndOrderNo(String orderSystem, String orderNo) {
		return paymentRequestDao.queryBySystemAndOrderNo(orderSystem, orderNo);
	}

	@Override
	public void createPaymentRequest(PaymentRequest payment) {
		Timestamp createTime = new Timestamp(System.currentTimeMillis());
		payment.setCreateTime(createTime);
		payment.setUpdateTime(createTime);
		paymentRequestDao.insert(payment);
	}

	@Override
	public PaymentRequest selectByPrimaryKey(Long requestId) {
		PaymentRequest payment = paymentRequestDao.selectByPrimaryKey(requestId);
		if (payment == null){
			throw new PayBizException(ErrorCode.P9002004);
		}
		return payment;
	}

	@Override
	public int updateRequestToReverse(Long requestId, String orgStatus) {
		 return paymentRequestDao.updateRequestToReverse(requestId, orgStatus);
	}

	@Override
	public List<PaymentRequest> queryUnNotifyPayment(@Param("startDate") Date startDate, @Param("endDate") Date endDate) {
		return paymentRequestDao.queryUnNotifyPayment(startDate, endDate);
	}

	@Override
	public int updatePayTypeAndStatus(Long requestId, String payType, String status, String prePayType, String preStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("requestId", requestId);
		map.put("payType", payType);
		map.put("status", status);
		map.put("prePayType", prePayType);
		map.put("preStatus", preStatus);
		return paymentRequestDao.updatePayTypeAndStatus(map);
	}

	@Override
	public int updatePaymentOrderSystemStatus(Long requestId, String orderSystemStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", requestId);
		map.put("orderSystemStatus", orderSystemStatus);
		return paymentRequestDao.updatePaymentOrderSystemStatus(map);
	}


}
