package com.yeepay.g3.core.payprocessor.dao;

import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ReverseRecordDao {

    int insert(ReverseRecord record);

    ReverseRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ReverseRecord record);

    /**
     * 根据退款状态和修改时间查询退款订单
     * @param start
     * @param end
     * @return
     */
    List<ReverseRecord> queryRecordByDate(@Param("startDate") Date start,
                                          @Param("endDate") Date end, @Param("refundStatus") String refundStatus);

    ReverseRecord queryByRecordNo(@Param("recordNo")String recordNo);

    /**
     * 更新冲正记录的状态
     * @param id 退款记录表主键
     * @param orgStatus 原始状态
     * @param expectStatus 更新后状态
     * @return
     */
    int updateRecordStatus(@Param("id") Long id, @Param("orgStatus") String orgStatus, @Param("expectStatus") String expectStatus);

    /**
     * 将冲正记录更新为退款成功
     * @param id
     * @return
     */
    int updateRecordToSuccess(@Param("id") Long id);

    /**
     * 将冲正记录更新为退款成功
     * @param id
     * @return
     */
    int updateRecordToSuccessFromCSDONE(@Param("id") Long id);
}