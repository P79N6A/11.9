package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.nccashier.dto.TradeNoticeDTO;

public class NcCashierRedisUtil {
	public static void setTradeNoticeDTOToRedis(TradeNoticeDTO tradeNoticeDTO,
			String paymentOrderNo) {
		RedisTemplate.setCacheObjectSumValue(
				CommonUtil.PREFIX_NCCASHIER + paymentOrderNo + "_" + PaymentSysCode.NCPAY,
				tradeNoticeDTO, 5 * 60 * 1000);
	}

	public static TradeNoticeDTO getTradeNoticeDTOFromRedis(String paymentOrderNo) {
		TradeNoticeDTO tradeNoticeDTO = RedisTemplate.getTargetFromRedis(
				CommonUtil.PREFIX_NCCASHIER + paymentOrderNo + "_" + PaymentSysCode.NCPAY,
				TradeNoticeDTO.class);
		return tradeNoticeDTO;
	}
}
