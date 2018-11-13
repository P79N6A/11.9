/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.hisDao.HisPayRecordDao;
import com.yeepay.g3.core.payprocessor.service.HisPayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类名称: HisPayRecordServiceImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/24 下午4:41
 * @version: 1.0.0
 */
@Service
public class HisPayRecordServiceImpl implements HisPayRecordService {

    @Autowired
    private HisPayRecordDao hisPayRecordDao;

    @Override
    public PayRecord queryRecordById(String recordNo) {
        return hisPayRecordDao.selectByPrimaryKey(recordNo);
    }
}