package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UnbindRecord;

public interface UnbindCardService {

	UnbindRecord create(Long bindId, PaymentRequest paymentRequest, String cause);

	void update(UnbindRecord unbindRecord);

}
