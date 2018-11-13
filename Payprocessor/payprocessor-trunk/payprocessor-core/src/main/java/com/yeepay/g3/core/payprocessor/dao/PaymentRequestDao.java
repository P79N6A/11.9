package com.yeepay.g3.core.payprocessor.dao;

import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PaymentRequestDao {

	int insert(PaymentRequest paymentRequest);

	PaymentRequest selectByPrimaryKey(@Param("id") Long id);

	int updateByPrimaryKey(PaymentRequest paymentRequest);

	PaymentRequest queryBySystemAndOrderNo(@Param("orderSystem") String orderSystem,
			@Param("orderNo") String orderNo);

	int updateRequestToReverse(@Param("requestId") Long requestId, @Param("orgStatus") String orgStatus);

	/**
	 * 更新支付成功状态，支付成功子表ID，支付成功时间
	 * @return
	 */
	int updateRequestToSuccess(Map map);

	/**
	 * 更新支付成功状态，支付成功子表ID，支付成功时间
	 * @return
	 */
	int updateRequestToNotifySuccess(@Param("id") Long id);

	/**
	 * 查询成功但未通知的订单
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<PaymentRequest> queryUnNotifyPayment(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 更新订单类型和订单状态
	 * @param map
	 * @return
	 */
	int updatePayTypeAndStatus(Map map);

	/**
	 * 预授权结果更新订单类型和订单状态
	 * @param map
	 * @return
	 */
	int updatePaymentToSuccessForPreAuth(Map map);

	/**
	 * 更新支付失败状态，支付成功子表ID，支付成功时间
	 * @return
	 */
	int updateRequestToFail(Map map);

	/**
	 * 强制更新主单状态
	 */
	int updateRequestFromFailToSuccess(Map map);

	/**
	 * 更新主单通知业务方状态为处理中
	 */
	int updatePaymentOrderSystemStatus(Map map);

}