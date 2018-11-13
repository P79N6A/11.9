package com.yeepay.g3.core.nccashier.dao;

import java.util.List;
import java.util.Map;

import com.ibm.db2.jcc.am.SqlIntegrityConstraintViolationException;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;

public interface PaymentRequestDao {

	/**
	 * 保存支付请求信息
	 * @param paymentRequest
	 * @throws SqlIntegrityConstraintViolationException 
	 */
	public long savePaymentRequest(PaymentRequest paymentRequest) ;

	/**
	 * 通过ID获得请求信息
	 * @param requestId
	 * @return
	 */
	public PaymentRequest findPayRequestById(long id);
	
	/**
	 * 更新支付请求表状态
	 * @param paymentRequest
	 */
	public int updatePayRequest(PaymentRequest paymentRequest);
	/**
	 * 通过交易系统订单号和交易系统编码查询支付请求信息
	 * @return
	 */
	public PaymentRequest findPayRequestByTradeSysOrderId(Map map);
	/**
	 * 通过订单方订单号和订单方编码查询支付请求信息
	 */
	public PaymentRequest findPayRequestByOrderOrderId(Map map);

	public int updateRequestBaseOnStatus(PaymentRequest paymentRequest, List<String> statusList);
	
	public int updatePayRequestExtendInfoById(PaymentRequest paymentRequest);

	public int updateUserRequestInfo(PaymentRequest paymentRequest);
	
	/**
	 * 通过商编和商户订单号获取支付请求列表
	 * @param map
	 * @return
	 */
	public List<PaymentRequest> findRequestListByMerchantNoAndMerchantOrderId(Map map);

	/**
	 * 查询ID范围内的记录数量
	 * @param pageParam
	 * @return
	 */
	int countPaymentRequest(Map pageParam);

	/**
	 * 批量查询identity_id_encrypt字段为空的记录
	 * @return ID和IDENTITY_ID 集合
	 */
	List<PaymentRequest> listPaymentRequestNotEnctypt(Map pageParam);

	/**
	 * 更新记录，将IDENTITY_ID的加密值写入IDENTITY_ID_ENCRYPT
	 * @param paymentRequest
	 * @return
	 */
	int updateForEncrypt(PaymentRequest paymentRequest);

	int updateForBatchEncrypt(List<PaymentRequest> paymentRequests);
}
