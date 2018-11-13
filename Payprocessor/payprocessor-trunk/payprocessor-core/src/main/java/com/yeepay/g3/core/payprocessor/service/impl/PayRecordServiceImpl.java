
package com.yeepay.g3.core.payprocessor.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.service.PayRecordService;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author peile.fan
 *
 */
@Service
public class PayRecordServiceImpl extends AbstractService implements PayRecordService {

	@Override
	public PayRecord queryRecordById(String recordNo) {
		PayRecord record = payRecordDao.selectByPrimaryKey(recordNo);
		if (record == null) {
			throw new PayBizException(ErrorCode.P9002006);
		}
		return record;

	}

	@Override
	public PayRecord queryRecordByRecord(String recordNo) {
		return payRecordDao.selectByPrimaryKey(recordNo);
	}

	@Override
	public void createPayRecord(PayRecord payRecord) {
		if (payRecord != null) {
			Timestamp createTime = new Timestamp(System.currentTimeMillis());
			payRecord.setCreateTime(createTime);
			payRecord.setUpdateTime(createTime);
			payRecordDao.insert(payRecord);
		}
	}

	@Override
	public void updatePaymentNo(String recordNo, String paymentNo, String originalStatus) {
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
		int rows = payRecordDao.updatePaymentNo(recordNo, paymentNo, originalStatus, updateTime);
	}

	@Override
	public int updatePaymentStatus(String recordNo, String newStatus, List<String> originalStatus) {
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
		int rows = payRecordDao.updatePaymentStatus(recordNo, newStatus, originalStatus, updateTime);
		return rows;
	}

	@Override
	public void updateNcPaymentSmsState(String recordNo, String smsState) {
		payRecordDao.updateNcPaymentSmsState(recordNo, smsState);
	}

	@Override
	public void updateNcPaymentToFail(String recordNo, String errorCode, String errorMsg) {
		payRecordDao.updatePaymentToFail(recordNo, errorCode, errorMsg);
	}

	@Override
	public List<PayRecord> queryDoingRecord(Date start, Date end , int maxRowCount) {
		return payRecordDao.queryDoingRecord(start, end , maxRowCount);
	}

	@Override
	public int queryPayTimes(Long requestId) {
		return payRecordDao.queryPayTimes(requestId);
	}

	@Override
	public String queryNcPaymentNo(String recordNo) {
		String ncpayPaymentNo = payRecordDao.queryNcPaymentNo(recordNo);
		if (StringUtils.isBlank(ncpayPaymentNo)) {
			throw new PayBizException(ErrorCode.P9002006);
		}
		return ncpayPaymentNo;
	}
	@Override
	public void updateRecordErrorInfo(String recordNo,String errorCode, String errorMsg) {
		payRecordDao.updateRecordErrorInfo(recordNo, errorCode, errorMsg,new Timestamp(System.currentTimeMillis()));
	}
	
	@Override
	public void updatePaymentToFail(String recordNo,String errorCode,String errorMsg) {
		payRecordDao.updatePaymentToFail(recordNo, errorCode, errorMsg);
	}

	@Override
	public int queryCompleteCancelCount(Long requestId) {
		return payRecordDao.queryCompleteCancelCount(requestId);
	}

}
