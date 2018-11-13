
package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;

import java.util.Date;
import java.util.List;

/**
 * @author peile.fan
 *
 */
public interface ReverseRecordService {

    /**
     * 查询未发送到清算中心的订单
     * @param start
     * @param end
     * @return
     */
    List<ReverseRecord> queryUnCsRecordByDate(Date start, Date end);

    /**
     * 查询清算中心处理中的订单
     * @param start
     * @param end
     * @return
     */
    List<ReverseRecord> queryCsDoingRecordByDate(Date start, Date end);

    /**
     * 查询未发送到退款中心的定单
     * @param start
     * @param end
     * @return
     */
    List<ReverseRecord> queryUnRefundRecordByDate(Date start, Date end);

    ReverseRecord queryByRecordNo(String recordNo);

    void updateRecord(ReverseRecord reverseRecord);

    /**
     * 将冲正记录更新为指定状态
     * @param id
     * @param orgStatus
     * @param expectStatus
     * @return
     */
    int updateRecord(Long id, String orgStatus, String expectStatus);

    /**
     * 将冲正记录更新为退款成功
     * @param id
     * @return
     */
    int updateRecordToSuccess(Long id);
}
