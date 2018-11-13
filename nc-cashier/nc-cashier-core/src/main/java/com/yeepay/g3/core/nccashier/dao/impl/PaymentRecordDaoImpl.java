package com.yeepay.g3.core.nccashier.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.yeepay.g3.core.nccashier.dao.PaymentRecordDao;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.utils.common.log.Logger;

@Repository
public class PaymentRecordDaoImpl extends SqlSessionDaoSupport implements PaymentRecordDao {

	private static final Logger logger =
			NcCashierLoggerFactory.getLogger(PaymentRecordDaoImpl.class);

	protected String getStatementId(String name) {
		return "com.yeepay.g3.core.nccashier.entity.PaymentRecord." + name;
	}

	@Override
	public long savePaymentRecord(PaymentRecord paymentRecord) {
		getSqlSession().insert(getStatementId("insert"), paymentRecord);
		long id = paymentRecord.getId();
		return id;
	}

	@Override
	public int updateRecordState(PaymentRecord paymentRecord) {
		return getSqlSession().update(getStatementId("updateRecordState"), paymentRecord);

	}

	@Override
	public List<PaymentRecord> findRecordList(Map map) {
		List<PaymentRecord> recordList =
				getSqlSession().selectList(getStatementId("findRecordList"), map);
		return recordList;
	}

	@Override
	public int updateRecordNo(PaymentRecord paymentRecord) {
		return getSqlSession().update(getStatementId("updateRecordNoById"), paymentRecord);

	}

	@Override
	public PaymentRecord findRecordByPayOrderNo(Map map) {
		PaymentRecord paymentRecord = (PaymentRecord) getSqlSession()
				.selectOne(getStatementId("findRecordByPayOrderNo"), map);
		return paymentRecord;
	}

	@Override
	public PaymentRecord findRecordByPaymentRecordId(Map map) {
		PaymentRecord paymentRecord = (PaymentRecord) getSqlSession()
				.selectOne(getStatementId("findRecordByPaymentRecordId"), map);
		return paymentRecord;
	}

	@Override
	public int updateRecordStateBaseOnOriginalStatus(Long id, PayRecordStatusEnum paying,
			List<PayRecordStatusEnum> statusList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("toState", paying);
		param.put("originalStatus", statusList);
		param.put("id", id);
		param.put("updateTime", new Date());
		return getSqlSession().update(this.getStatementId("updateRecordStateBaseOnOriginalStatus"),
				param);
	}

	@Override
	public int updateRecordBaseOnOriginalStatus(PaymentRecord paymentRecord,
			List<PayRecordStatusEnum> statusList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("paymentRecord", paymentRecord);
		param.put("originalStatus", statusList);
		return getSqlSession().update(this.getStatementId("updateRecordBaseOnOriginalStatus"),
				param);
	}

	@Override
	public List<PaymentRecord> findRecordListByOrderOrderId(String orderOrderId,
			String orderSysNo) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderOrderId", orderOrderId);
		map.put("orderSysNo", orderSysNo);
		List<PaymentRecord> recordList =
				getSqlSession().selectList(getStatementId("findRecordListByOrderOrderId"), map);
		return recordList;
	}
	
	@Override
	public List<PaymentRecord> findRecordsByMerchantOrderId(String merchantOrderId,String merchantNo) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchantOrderId", merchantOrderId);
		map.put("merchantNo", merchantNo);
		List<PaymentRecord> recordList =
				getSqlSession().selectList(getStatementId("findRecordsByMerchantOrderId"), map);
		return recordList;
	}

	@Override
	public int updateRecordStatusAndPaymentExt(PaymentRecord paymentRecord) {
        return getSqlSession().update(getStatementId("updateRecordStatusAndPaymentExt"), paymentRecord);
    }
}
