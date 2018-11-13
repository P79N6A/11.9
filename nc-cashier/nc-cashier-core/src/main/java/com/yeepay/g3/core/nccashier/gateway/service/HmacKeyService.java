package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.facade.merchant_platform.dto.HmacKeyRespDTO;

public interface HmacKeyService {

	HmacKeyRespDTO getHmacKey(String merchantNo);
}
