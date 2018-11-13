/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.core.payprocessor.service.PreAuthReverseRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 类名称: PreAuthReverseRecordServiceImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/23 下午7:34
 * @version: 1.0.0
 */
@Service
public class PreAuthReverseRecordServiceImpl extends AbstractService implements PreAuthReverseRecordService {


    @Override
    public void add(PreAuthReverseRecord preAuthReverseRecord) {
        preAuthReverseRecordDao.insert(preAuthReverseRecord);
    }

    @Override
    public PreAuthReverseRecord queryByRecordNo(String recordNo) {
        return preAuthReverseRecordDao.queryByRecordNo(recordNo);
    }

    @Override
    public PreAuthReverseRecord queryByReverseNo(String reverseNo) {
        return preAuthReverseRecordDao.queryByReverseNo(reverseNo);
    }

    @Override
    public void updateRecord(PreAuthReverseRecord preAuthReverseRecord) {
        preAuthReverseRecordDao.updateByPrimaryKey(preAuthReverseRecord);
    }

}