/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.dao;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.facade.payprocessor.enumtype.CombTrxStatusEnum;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 类名称: CombPayRecordDao <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/14 下午12:32
 * @version: 1.0.0
 */
public interface CombPayRecordDao {

    int insert(CombPayRecord combPayRecord);

    CombPayRecord selectByRecordNo(String recordNo);

    int updateToFail(CombPayRecord combPayRecord);

    int updateToDeposit(CombPayRecord combPayRecord);

    int updateToSuccess(CombPayRecord combPayRecord);

    int updateToReverse(CombPayRecord combPayRecord);

    int updateToFailByRecordNo(String recordNo);

    List<CombPayRecord> selectByDeposit(@Param("startDate") Date start, @Param("endDate") Date end , @Param("maxRowCount") int maxRowCount);
}
