
package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.facade.payprocessor.enumtype.RefundStatusEnum;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.service.ReverseRecordService;

import java.util.Date;
import java.util.List;

/**
 * @author peile.fan
 *
 */
@Service
public class ReverseRecordServiceImpl extends AbstractService implements ReverseRecordService {

    @Override
    public List<ReverseRecord> queryUnCsRecordByDate(Date start, Date end) {
        return reverseRecordDao.queryRecordByDate(start, end, RefundStatusEnum.INIT.name());
    }

    @Override
    public List<ReverseRecord> queryCsDoingRecordByDate(Date start, Date end) {
        return reverseRecordDao.queryRecordByDate(start, end, RefundStatusEnum.CSDOING.name());
    }

    @Override
    public List<ReverseRecord> queryUnRefundRecordByDate(Date start, Date end) {
        return reverseRecordDao.queryRecordByDate(start, end, RefundStatusEnum.CSDONE.name());
    }

    @Override
    public ReverseRecord queryByRecordNo(String recordNo) {
        ReverseRecord record = reverseRecordDao.queryByRecordNo(recordNo);
        if (record == null)
            throw new PayBizException(ErrorCode.P9002008);
        return record;
    }

    @Override
    public void updateRecord(ReverseRecord reverseRecord) {
        reverseRecordDao.updateByPrimaryKey(reverseRecord);
    }

    @Override
    public int updateRecord(Long id, String orgStatus, String expectStatus) {
        return reverseRecordDao.updateRecordStatus(id, orgStatus, expectStatus);
    }

    @Override
    public int updateRecordToSuccess(Long id) {
        return reverseRecordDao.updateRecordToSuccess(id);
    }
}
