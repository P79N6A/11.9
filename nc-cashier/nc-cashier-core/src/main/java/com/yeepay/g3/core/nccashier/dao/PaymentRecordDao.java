package com.yeepay.g3.core.nccashier.dao;

import java.util.List;
import java.util.Map;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;

public interface PaymentRecordDao {

	public long savePaymentRecord(PaymentRecord paymentRecord);

	public int updateRecordState(PaymentRecord paymentRecord);

	public List<PaymentRecord> findRecordList(Map map);

	public int updateRecordNo(PaymentRecord paymentRecord);

	public PaymentRecord findRecordByPayOrderNo(Map map);

	public PaymentRecord findRecordByPaymentRecordId(Map map);

	public int updateRecordStateBaseOnOriginalStatus(Long id, PayRecordStatusEnum toState,
			List<PayRecordStatusEnum> statusList);

	public int updateRecordBaseOnOriginalStatus(PaymentRecord paymentRecord,
			List<PayRecordStatusEnum> statusList);

	public List<PaymentRecord> findRecordListByOrderOrderId(String orderOrderId, String orderSysNo);
	
	public List<PaymentRecord> findRecordsByMerchantOrderId(String merchantOrderId,String merchantNo);

	/**
	 * 更新payment_record的支付订单号、状态、paymentExt
	 * @param paymentRecord
	 * @return
	 */
	int updateRecordStatusAndPaymentExt(PaymentRecord paymentRecord);
}
