
package com.yeepay.g3.facade.payprocessor.service;

import com.yeepay.g3.core.payprocessor.dao.PaymentRequestDao;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.facade.member.bha.param.PaymentRecordDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.dao.PayRecordDao;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.service.ResultProcessService;

/**
 * @author peile.fan
 *
 */
public class ResultProcessServiceTest extends BaseTest {
	@Autowired
	private ResultProcessService resultProcessService;

	@Autowired
	private PaymentRequestDao paymentRequestDao;

	@Autowired
	private PayRecordDao payRecordDao;
	

	@Test
	public void testUpdatePaymentToSuccess() {
		PayRecord payRecord = payRecordDao.selectByPrimaryKey("SALE14804024222987213");
		resultProcessService.updatePaymentToSuccess(payRecord);
	}

	@Test
	public void testUpdatePaymentAndRecord() {
		PayRecord payRecord = payRecordDao.selectByPrimaryKey("PREAUTH_RE1801091817566705342");
		resultProcessService.updatePaymentAndRecord(payRecord);
	}

	@Test
	public void testUpdatePaymentToReverse() {
		PayRecord payRecord = payRecordDao.selectByPrimaryKey("SALE1809071635542825119");
		PaymentRequest paymentRequest = paymentRequestDao.selectByPrimaryKey(payRecord.getRequestId());
		resultProcessService.updatePaymentToReverse(paymentRequest, payRecord, "业务方冲正");
	}
}
