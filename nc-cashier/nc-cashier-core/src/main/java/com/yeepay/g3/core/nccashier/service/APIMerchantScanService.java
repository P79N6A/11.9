package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantScanPayDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayResponseDTO;

/**
 * 
 * @author xueping.ni
 *
 */
public interface APIMerchantScanService {

	void callPP2CreateOrder(APIMerchantScanPayDTO request, PaymentRequest paymentRequest, PaymentRecord record);

}
