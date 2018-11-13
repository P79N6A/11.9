package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.base.BaseTest;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.vo.CarnivalVO;
import com.yeepay.g3.facade.nccashier.dto.TradeNoticeDTO;
import com.yeepay.g3.facade.nccashier.enumtype.TradeSysCodeEnum;
import com.yeepay.g3.utils.common.json.JSONUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;

public class NewWapPayServiceTest extends BaseTest {

	@Resource
	private NewWapPayService newWapPayService;

	@Test
	public void testQueryQualification4Carnival(){
//		System.out.println(CommonUtil.judgeMerchantShowPcYeepayInfoOrNot("10040007800"));
//		TradeNoticeDTO tradeNoticeDTO = new TradeNoticeDTO();
//
//		//系统
//		tradeNoticeDTO.setTradeSysNo(TradeSysCodeEnum.DS.toString());
////		tradeNoticeDTO.setTradeSysNo(TradeSysCodeEnum.G2NET.toString());
////		tradeNoticeDTO.setTradeSysNo(TradeSysCodeEnum.NCTRADE.toString());
//
//		//金额
//		tradeNoticeDTO.setPaymentAmount(new BigDecimal(2));
////		tradeNoticeDTO.setPaymentAmount(new BigDecimal(0.1));
//
//
////		tradeNoticeDTO.setMerchantNo("10040007800");
//		tradeNoticeDTO.setMerchantNo("10040008079");
//		tradeNoticeDTO.setMerchantOrderId("merchanNoId");
//		CarnivalVO carnivalVO = newWapPayService.queryQualification4Carnival(tradeNoticeDTO);
//		System.out.println(JSONUtils.toJsonString(carnivalVO));


	}
}
