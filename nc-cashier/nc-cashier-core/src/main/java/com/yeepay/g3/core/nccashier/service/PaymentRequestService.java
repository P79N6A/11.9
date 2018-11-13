package com.yeepay.g3.core.nccashier.service;

import java.sql.SQLException;
import java.util.List;

import com.ibm.db2.jcc.am.SqlIntegrityConstraintViolationException;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
/**
 * @date 2016.05.24
 * @author xueping.ni
 *paymentRequest服务接口
 */
public interface PaymentRequestService {
	/**
	 * 通过支付请求号查询支付请求信息
	 * @param requestId
	 * @return
	 */
	public PaymentRequest findPayRequestById(long requestId) throws CashierBusinessException;
	/**
	 * 保存支付请求表信息
	 * @param paymentRequest
	 * @throws SqlIntegrityConstraintViolationException
	 * @throws SQLException
	 */
	public long savePaymentRequest(PaymentRequest paymentRequest) throws  CashierBusinessException;
	/**
	 * 更新支付请求表状态
	 * @param paymentRequest
	 */
	public void updatePayRequestState(PaymentRequest paymentRequest);
	/**
	 * 通过订单方订单号和订单方编码查询支付请求信息
	 * @return
	 */
	public PaymentRequest findPayRequestByOrderOrderId(String orderOrderId,String orderSysNo);
	/**
	 * 通过交易系统订单号和交易系统编码查询支付请求信息
	 * @return
	 */
	public PaymentRequest findPayRequestByTradeSysOrderId(String tradeSysOrderId,String tradeSysNo);
	/**
	 * 通过支付请求号查询在有效期内的支付请求信息
	 * @param requestId
	 * @return
	 */
	public PaymentRequest findPaymentRequestByRequestId(long requestId);

	/**
	 * 判断支付请求订单是否已经过期
	 * 
	 * @param paymentRequest
	 * @return
	 */
	public boolean isRequestExpired(PaymentRequest paymentRequest);


	/**
	 * 根据请求订单的状态，更新请求订单
	 * @param paymentRequest
	 * @param toState
	 * @param statusList
	 * @return
	 */
	public void updateRequestBaseOnStatus(PaymentRequest paymentRequest, List<String> statusList);

	/**
	 * 更新requestPayment的扩展信息
	 * @param paymentRequest
	 * @return
	 */
	public int updatePayRequestExtendInfoById(PaymentRequest paymentRequest);

	/**
	 * 更新用户请求参数
	 * @param paymentRequest
	 * @return
	 */
	public int updateUserRequestInfo(PaymentRequest paymentRequest);
	
	/**
	 * 获取过期截止日期
	 * 
	 * @param paymentRequest
	 * @return
	 */
	long getExpiredTime(PaymentRequest paymentRequest);
	
	public List<PaymentRequest> findRequestListByMerchantNoAndMerchantOrderId(String merchantNo,String merchantOrderId);

	boolean openPayType(PaymentRequest paymentRequest, String token, String payTool);
	
	/**
	 * @title 根据交易系统订单号+交易系统编码查询paymentRequest，不存在则创建
	 * @param orderInfo
	 * @param merchantInNetConfig
	 * @param productLevel
	 * @param paymentRequestExtInfo
	 * @return
	 */
	PaymentRequest createRequestWhenUnexsit(OrderDetailInfoModel orderInfo, MerchantInNetConfigResult merchantInNetConfig,
											ProductLevel productLevel, PaymentRequestExtInfo paymentRequestExtInfo);

	/**
	 * @title 根据交易订单号和交易系统编码获取非空paymentRequest
	 * @param tradeSysOrderId
	 * @param tradeSysNo
	 * @return
	 */
	PaymentRequest findNonNullPayRequestByTradeSysOrder(String tradeSysOrderId, String tradeSysNo, boolean checkExpiredTime);
	
	/**
	 * 根据交易订单号和交易系统编码获取paymentRequest，包含空
	 * 
	 * @param tradeSysOrderId
	 *            交易系统订单号
	 * @param tradeSysNo
	 *            交易系统编码
	 * @param checkExpiredTime
	 *            是否校验过期时间
	 * @return
	 */
	PaymentRequest findPayRequestByTradeSysOrder(String tradeSysOrderId, String tradeSysNo, boolean checkExpiredTime);
}
