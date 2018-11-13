
package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author peile.fan
 *
 */
public interface PreAuthReverseRecordService {

    void add(PreAuthReverseRecord preAuthReverseRecord);

    PreAuthReverseRecord queryByRecordNo(String recordNo);

    PreAuthReverseRecord queryByReverseNo(String reverseNo);

    void updateRecord(PreAuthReverseRecord preAuthReverseRecord);


}
