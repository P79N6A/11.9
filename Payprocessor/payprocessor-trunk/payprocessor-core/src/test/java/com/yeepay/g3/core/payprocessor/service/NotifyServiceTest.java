package com.yeepay.g3.core.payprocessor.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;

/**
 * Description:
 * @author peile.fan
 * @since:2017年1月11日 下午2:00:42
 */
public class NotifyServiceTest extends BaseTest {
	
	@Autowired
	private NotifyService notifyService;

	@Autowired
	private PayRecordService payRecordService;

	@Test
	public void testNotify() {
		String recordNo = "";
		PayRecord payRecord = payRecordService.queryRecordById(recordNo);
		notifyService.notify(payRecord);
	}

}
