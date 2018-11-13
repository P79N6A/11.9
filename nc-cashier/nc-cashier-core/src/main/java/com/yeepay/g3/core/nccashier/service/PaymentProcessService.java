package com.yeepay.g3.core.nccashier.service;

import java.util.List;

import com.yeepay.g3.core.nccashier.entity.ParamShowInfo;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.PersonHoldCard;
import com.yeepay.g3.core.nccashier.vo.RecordCondition;
import com.yeepay.g3.facade.nccashier.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.TradeNoticeDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;

/**
 * Created by xiewei on 15-10-19.
 */
public interface PaymentProcessService {
	/**
	 * 保存支付记录信息
	 * 
	 * @param paymentRecord
	 * @return
	 */
	public long savePaymentRecord(PaymentRecord paymentRecord);

	// 更改支付请求记录表状态
	public void updateRecord(PaymentRecord paymentRecord);

	public List<PaymentRecord> findRecordList(String tradeSysOrderId, String tradeSysNo);

	/**
	 * 通过支付状态查询支付记录表
	 * 
	 * @param tradeSysOrderId
	 * @param tradeSysNo
	 * @param payState
	 * @return
	 */
	public List<PaymentRecord> findRecordList(String tradeSysOrderId, String tradeSysNo, String payState);

	public void updateRecordNo(long orderId, String smstype, String payOrderId, PayRecordStatusEnum status);

	/**
	 * 通过支付订单号和支付系统编码查询支付记录
	 */
	public PaymentRecord findRecordByPayOrderNo(String payOrderNo, String paymentSysNo);

	/**
	 * 通过支付记录表主键ID查询支付记录
	 * 
	 * @param paymentRecordId
	 * @return
	 */
	public PaymentRecord findRecordByPaymentRecordId(String paymentRecordId);

	/**
	 * 通过token查询支付记录
	 * 
	 * @param token
	 * @return
	 */
	PaymentRecord findRecordBytoken(String token);

	/**
	 * 验证支付请求信息合法性(支付成功和超时)
	 * 
	 * @param paymentRequest
	 */
	public void verifyPayRequest(PaymentRequest paymentRequest);

	/**
	 * 组装个接口信息
	 * 
	 * @param payRequest
	 * @return
	 */
	public ParamShowInfo makeRequestInfoDTO(PaymentRequest payRequest);

	/**
	 * 查询交易结果
	 * 
	 * @param queryTradeResultUrl
	 * @param tradeSysOrderId
	 * @param tradeSysNo
	 * @return
	 */
	public TradeNoticeDTO queryTradeResult(String queryTradeResultUrl, String tradeSysOrderId, String tradeSysNo);

	/**
	 * 补单
	 * 
	 * @param reverseRequestDTO
	 * @param reverseResponseDTO
	 */
	public void reversePayOrder(ReverseRequestDTO reverseRequestDTO, ReverseResponseDTO reverseResponseDTO);

	/**
	 * 根据ncpay处理结果更新支付请求,支付纪录状态
	 * 
	 * @param paymentRequest
	 * @param paymentRecord
	 */
	public boolean updateStateByNcPayCallBack(PaymentRequest paymentRequest, PaymentRecord paymentRecord);

	public void updateRecordStateBaseOnOriginalStatus(Long id, PayRecordStatusEnum paying,
			List<PayRecordStatusEnum> statusList);
	
	void updateRecordStatusBaseOnOriginalStatus(Long paymentRecordId, PayRecordStatusEnum objStatus,
			PayRecordStatusEnum preStatus);

	List<PaymentRecord> findRecordListByOrderOrderId(String orderOrderId, String orderSysNo);

	public void updateRecordNo(Long id, String name, String payOrderId, PayRecordStatusEnum ordered, int needItem, String redirectType);

	/**
	 * 更新payment_record的支付订单号、状态、paymentExt
	 * @param id
	 * @param payOrderId
	 * @param status
	 * @param paymentExt
	 */
	void updateRecordStatusAndPaymentExt(Long id, String payOrderId, PayRecordStatusEnum status, String paymentExt);

	public List<PaymentRecord> findRecordsByMerchantOrderId(String merchantOrderId, String merchantNo);

	public PaymentRecord findRecordByMerchantOrderId(String merchantOrderId, String merchantNo,String paymentOrderNo);

	/**
	 * 根据交易系统订单号和交易系统编码查询满足条件的支付记录，不存在则创建
	 * 
	 * @param paymentRequest
	 * @param compareCondition
	 * @param person
	 * @param recordPayType
	 * @param externalUserId
	 * @param token
	 * @param tmpCardId
	 * @param payMerchantNo
	 * @return
	 */
	PaymentRecord createRecordWhenUnexsit(PaymentRequest paymentRequest, RecordCondition compareCondition,
			PersonHoldCard person, String recordPayType, String externalUserId, String token, String tmpCardId,String payMerchantNo);

	PaymentRecord createPaymentRecord(PaymentRequest paymentRequest, RecordCondition compareCondition,
			PersonHoldCard person, String recordPayType, String externalUserId, String token, String tmpCardId,String payMerchantNo);
	
	/**
	 * 根据交易系统订单号和交易系统编码查询并根据比较条件过滤paymentRecord
	 * 
	 * @param tradeSysOrderId
	 * @param tradeSysN0
	 * @param compareCondition
	 * @return
	 */
	PaymentRecord filterPaymentRecordList(String tradeSysOrderId, String tradeSysN0, RecordCondition compareCondition);

	/**
	 * 根据record查询并根据比较条件过滤paymentRecord，获取不到则抛异常
	 * 
	 * @param recordId
	 * @param compareCondition
	 * @return
	 */
	PaymentRecord getNonNullPaymentRecord(String recordId, RecordCondition compareCondition);

	/**
	 * 根据交易系统订单号和交易系统编码获取paymentRecord，并根据compareCondition过滤，获取不到抛异常
	 * 
	 * @param tradeSysOrderId
	 * @param tradeSysNo
	 * @param compareCondition
	 * @return
	 */
	PaymentRecord getNonNullPaymentRecord(String tradeSysOrderId, String tradeSysNo, RecordCondition compareCondition);
	
	/**
	 * 将符合前置状态为preStatus的paymentRecord的状态回滚至目标状态objStatus
	 * 
	 * @param paymentRecordId
	 * @param objStatus
	 * @param preStatus
	 */
	void recoverRecordToObjStatus(Long paymentRecordId, PayRecordStatusEnum objStatus, PayRecordStatusEnum preStatus);
	
	/**
	 * 确认支付时，更新record为PAYING状态（主要是为了防重）
	 * 
	 * @param paymentRecordId
	 * @param preRecordStatusList
	 *            允许改成PAYING状态的前置状态列表
	 * @return
	 */
	boolean updateRecordToPaying(Long paymentRecordId, PayRecordStatusEnum[] preRecordStatusList);
	
	/**
	 * 确认支付时，处理防重，若发生并发，则抛异常
	 * 
	 * @param paymentRecord
	 * @param preRecordStatusList
	 *            允许改成PAYING状态的前置状态列表
	 */
	void avoidRepeatPayWithException(PaymentRecord paymentRecord, PayRecordStatusEnum[] preRecordStatusList);
}
