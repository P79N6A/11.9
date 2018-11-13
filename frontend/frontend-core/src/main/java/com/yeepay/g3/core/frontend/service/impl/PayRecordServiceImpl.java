/**
 * 
 */
package com.yeepay.g3.core.frontend.service.impl;

import com.yeepay.g3.core.frontend.entity.PayRecord;
import com.yeepay.g3.core.frontend.service.PayRecordService;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author TML
 *
 */
@Service
public class PayRecordServiceImpl extends AbstractService implements PayRecordService{
	
	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(PayRecordServiceImpl.class);

	@Override
	public void createPayRecord(PayRecord payRecord) {
		if(payRecord != null){
			Timestamp createTime = new Timestamp(System.currentTimeMillis());
			payRecord.setCreateTime(createTime);
			payRecordDao.add(payRecord);
		}
	}

	@Override
	public void updatePayRecord(PayRecord payRecord) {
		if(payRecord != null){
			payRecordDao.update(payRecord);
		}
	}

//	@Override
//	public PayRecord queryByRecordNo(String recordNo) {
//		List<PayRecord> list = payRecordDao.queryByRecordNo(recordNo);
//		if(list.isEmpty()){
//			return null;
//		}
//		return list.get(0);
//	}

//	@Override
//	public void updateByRecordNo(PayRecord payRecord) {
//		if(payRecord != null){
//			payRecordDao.updateByRecordNo(payRecord);
//		}
//	}
	
	/**
	 * 生成18位的 支付记录流失ID
	 * 两位业务规则码+6位日期年月日+5位当天的秒数+SEQUENCE的末位5位
	 */
	@Override
	public String generateId(String platformType){
		// 6位日期年月日
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		String year = Integer.toString(today.get(Calendar.YEAR));
		year = year.substring(year.length()-2);
		String date = sdf.format(today.getTime());
		// 5位当天的秒数
		Calendar base = (Calendar) today.clone();
		base.set(Calendar.HOUR_OF_DAY, 0);
		base.set(Calendar.MINUTE, 0);
		base.set(Calendar.SECOND, 0);
		Long l = (today.getTimeInMillis()-base.getTimeInMillis())/1000;
		DecimalFormat dig = new DecimalFormat("00000");
		String seconds = dig.format(l);
		// SEQUENCE的末位5位
		Long seqL = payRecordDao.getSeqValue(platformType);
		String sequence = dig.format(seqL);
		
		StringBuffer gId = new StringBuffer("10").append(year).append(date).append(seconds).append(sequence.substring(sequence.length()-5));
		logger.info("recordNo:" + gId.toString());
		return gId.toString();
	}

	@Override
	public List<PayRecord> queryByOrderNo(String orderNo, String platformType) {
		return payRecordDao.queryByOrderNo(orderNo, platformType);
	}

	@Override
	public int countRecordByOrderNo(String orderNo, String platformType) {
		return payRecordDao.countRecordByOrderNo(orderNo, platformType);
	}

}
