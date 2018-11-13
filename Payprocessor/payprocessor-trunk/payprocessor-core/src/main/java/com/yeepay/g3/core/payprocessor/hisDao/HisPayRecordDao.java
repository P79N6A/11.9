package com.yeepay.g3.core.payprocessor.hisDao;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;

public interface HisPayRecordDao {

	PayRecord selectByPrimaryKey(String recordNo);

}