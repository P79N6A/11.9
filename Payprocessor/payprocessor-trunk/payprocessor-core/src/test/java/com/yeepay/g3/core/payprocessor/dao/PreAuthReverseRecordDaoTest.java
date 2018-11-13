/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.dao;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 类名称: PreAuthReverseRecordDaoTest <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/27 上午10:46
 * @version: 1.0.0
 */

public class PreAuthReverseRecordDaoTest extends BaseTest {


    @Autowired
    private PreAuthReverseRecordDao preAuthReverseRecordDao;

    @Test
    public void selectByPrimaryKey() throws Exception {
        PreAuthReverseRecord record = preAuthReverseRecordDao.selectByPrimaryKey(1l);
        System.out.println(ToStringBuilder.reflectionToString(record));
    }

    @Test
    public void insert() {
        PreAuthReverseRecord record = new PreAuthReverseRecord();
        record.setRequestId(1234L);
        record.setRecordNo("record1112");
        record.setReverseNo("1");
        record.setRequestTime(new Date());
        record.setModifyTime(new Date());
        record.setCancelStatus("DOING");
        preAuthReverseRecordDao.insert(record);
        System.out.println(preAuthReverseRecordDao.queryByRecordNo("record1112"));
    }


    @Test
    public void updateByPrimeKey() {
        PreAuthReverseRecord preAuthReverseRecord = preAuthReverseRecordDao.queryByReverseNo("REVERSE1808231625387396142");
        preAuthReverseRecord.setPaymentNo("101801025147502473");
        preAuthReverseRecord.setSuccessTime(new Date());
        preAuthReverseRecord.setCancelStatus(TrxStatusEnum.SUCCESS.name());
//        preAuthReverseRecordDao.updateByPrimaryKey(preAuthReverseRecord);
        System.out.println(preAuthReverseRecord);
    }
}