package com.yeepay.g3.core.payprocessor.dao;

import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PreAuthReverseRecordDao {

    int insert(PreAuthReverseRecord record);

    PreAuthReverseRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKey(PreAuthReverseRecord record);

    PreAuthReverseRecord queryByRecordNo(@Param("recordNo")String recordNo);

    PreAuthReverseRecord queryByReverseNo(@Param("reverseNo")String recordNo);

}