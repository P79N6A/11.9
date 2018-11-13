package com.yeepay.g3.core.payprocessor.hisDao;

import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import org.apache.ibatis.annotations.Param;


public interface HisPaymentRequestDao {

	PaymentRequest selectByPrimaryKey(@Param("id") Long id);

	PaymentRequest queryBySystemAndOrderNo(@Param("orderSystem") String orderSystem,
										   @Param("orderNo") String orderNo);

}