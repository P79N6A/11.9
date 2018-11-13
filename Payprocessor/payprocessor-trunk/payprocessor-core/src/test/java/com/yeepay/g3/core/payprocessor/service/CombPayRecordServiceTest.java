/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 类名称: CombPayRecordServiceTest <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/14 下午2:29
 * @version: 1.0.0
 */

public class CombPayRecordServiceTest extends BaseTest {

    @Autowired
    private CombPayRecordService combPayRecordService;




    @Test
    public void testAdd() {
        CombPayRecord combPayRecord = new CombPayRecord();
        combPayRecord.setRecordNo("recordNo" + System.currentTimeMillis());
        combPayRecord.setCreateTime(new Date());
        combPayRecordService.add(combPayRecord);
    }

    @Test
    public void testQueryByRecordNo() {
        CombPayRecord combPayRecord = combPayRecordService.queryByRecordNo("recordNo1528960164758");
        System.out.println(combPayRecord);
    }

}