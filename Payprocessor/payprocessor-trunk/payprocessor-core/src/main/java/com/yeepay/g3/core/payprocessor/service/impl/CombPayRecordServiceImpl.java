/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.core.payprocessor.dao.CombPayRecordDao;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.service.CombPayRecordService;
import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 类名称: CombPayRecordServiceImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/14 下午2:25
 * @version: 1.0.0
 */
@Service
public class CombPayRecordServiceImpl extends AbstractService implements CombPayRecordService {


    @Override
    public CombPayRecord queryByRecordNo(String recordNo) {
        return combPayRecordDao.selectByRecordNo(recordNo);
    }

    @Override
    public void add(CombPayRecord combPayRecord) {
        combPayRecordDao.insert(combPayRecord);
    }

    @Override
    public List<CombPayRecord> selectDepositPayRecords(Date start, Date end, int maxRowCount) {
        return combPayRecordDao.selectByDeposit(start, end, maxRowCount);
    }

}