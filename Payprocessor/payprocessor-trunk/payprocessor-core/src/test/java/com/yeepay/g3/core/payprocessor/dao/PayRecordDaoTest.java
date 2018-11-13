
package com.yeepay.g3.core.payprocessor.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.yeepay.g3.core.payprocessor.hisDao.HisPayRecordDao;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.facade.ncconfig.enumtype.CardType;
import com.yeepay.g3.facade.payprocessor.enumtype.CashierVersion;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.DateUtils;

/**
 * @author peile.fan
 *
 */
public class PayRecordDaoTest extends BaseTest {

	@Autowired
	private PayRecordDao payRecordDao;

	@Autowired
	private HisPayRecordDao hisPayRecordDao;

	@Test
	public void testSelectByPrimaryKey() {
		String recordNo = "SALE1806261009331742647";
		PayRecord record = payRecordDao.selectByPrimaryKey(recordNo);
//		record.setRecordNo("SALE1806241410432498834");
//		hisPayRecordDao.insert(record);
		PayRecord record1 = hisPayRecordDao.selectByPrimaryKey("SALE1806241410432498834");
		System.out.println("现库：" + record);
		System.out.println("历史库：" + record1);
	}
	@Test
	public void insert() throws Exception {
		PayRecord payRecord = new PayRecord();
		payRecord.setAmount(new BigDecimal("0.1"));
		payRecord.setBankId("ICBC");
		payRecord.setBankOrderNo("16110416000204789");
		payRecord.setBankSeq("161104160002047892038371");
		payRecord.setBankTrxId("16110415004929443");
		payRecord.setBindCardInfoId("2018716");
		payRecord.setCardId("11819116030216205482");
		payRecord.setCardType(CardType.DEBIT.name());
		payRecord.setCashierVersion(CashierVersion.WEB.name());
		payRecord.setCost(new BigDecimal("0.00"));
		payRecord.setCreateTime(new Date());
		payRecord.setUpdateTime(new Date());
		payRecord.setFrpCode("ICBC_KUAIPAY_JD_GHDS8015");
		payRecord.setPaymentNo("101611045734338644");
		payRecord.setPayOrderType(PayOrderType.SALE.name());
		payRecord.setPayProduct("NCPAY");
		payRecord.setPayScene("TZTSCENE01A020");
		payRecord.setPayTime(new Timestamp(1478246145291L));
		String recordNO = "SALE" + System.currentTimeMillis() + RandomUtils.nextInt(10000);
		payRecord.setRecordNo(recordNO);
		payRecord.setRequestId(76l);
		payRecord.setStatus(TrxStatusEnum.SUCCESS.name());
		payRecord.setUserFee(new BigDecimal("0.01"));
		payRecord.setLoanCompany("马上理财");
		payRecord.setLoanTerm("MSXF01");
		payRecordDao.insert(payRecord);
	}

	@Test
	public void testUpdatePaymentStatus() {
		String recordNo = "SALE14791788091581188";
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
		int rows = payRecordDao.updatePaymentStatus(recordNo, TrxStatusEnum.DOING.name(),
				Arrays.asList(new String[] { TrxStatusEnum.DOING.name() }), updateTime);
		System.out.println("[更新行数] - [rows=" + rows + "]");
	}

	@Test
	public void testUpdatePaymentBankInfo() {
		String recordNo = "SALE14792874982466435";
		PayRecord record = payRecordDao.selectByPrimaryKey(recordNo);
		record.setFrpCode("FRP_CODE");
		record.setBankId("bank_id");
		record.setBankOrderNo("bankOrderNo");
		record.setBankSeq("bank_seq"); // TODO 退款需要
		record.setBankTrxId("bankTrxId");
		record.setCost(new BigDecimal("0.99"));
		record.setPayTime(new Date());
		record.setStatus(TrxStatusEnum.SUCCESS.name());
		record.setCardId("cardId");
		record.setCardType("SALE");
		record.setUpdateTime(new Date());
		payRecordDao.updatePaymentBankInfo(record, new Timestamp(System.currentTimeMillis()));
	}

	@Test
	public void testQueryDoingRecord() {
		Date end = new Date();
		Date start = DateUtils.addDay(end, -7);
		List<PayRecord> recordList = payRecordDao.queryDoingRecord(start, end , 1000);
		System.out.println("大小:" + recordList.size());
		for (PayRecord record : recordList) {
			System.out.println(ToStringBuilder.reflectionToString(record));
		}
	}

	@Test
	public void testUpdateNcPaymentToFail() {
		String recordNo = "SALE14791788091581188";
		payRecordDao.updatePaymentToFail(recordNo, "400015", "系统异常");
	}

	@Test
	public void testQueryTimes() {
		int i = payRecordDao.queryPayTimes(99124l);
		System.out.println(i);
	}

	@Test
	public void queryStatus() {
		String recordNo = "SALE14809227854467179";
		String status = payRecordDao.queryStatus(recordNo);
		System.out.println(status);
	}

	@Test
	public void testUpdateBindId() {
		String recordNo = "SALE14809227854467179";
		String bindId = "123456";
		payRecordDao.updateBindId(bindId, recordNo);
	}
	
	@Test
	public void testQueryNcPaymentNo() {
		String recordNo = "SALE14809227854467179";
		payRecordDao.queryNcPaymentNo(recordNo);
	}
}
