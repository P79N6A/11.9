package com.yeepay.g3.app.nccashier.wap.action;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.service.MarketActivityManageService;
import com.yeepay.g3.app.nccashier.wap.vo.MarketingInfoVO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Controller 
@RequestMapping(value = "/market")
public class MarketActivityAction extends WapBaseAction {

	@Resource
	private MarketActivityManageService marketActivityManageService;

	private static Logger logger = LoggerFactory.getLogger(MarketActivityAction.class);

	@RequestMapping("/info") 
	@ResponseBody
	public String getMarketingInfo(String token) {
		logger.info("getMarketingInfo token={}", token);
		MarketingInfoVO marketInfo = null;
		try {
			RequestInfoDTO requestInfo = newWapPayService.validateRequestInfoDTO(token);
			marketInfo = marketActivityManageService.getMarketActivityInfo(
					requestInfo.getPaymentRequestId() == null ? 0 : requestInfo.getPaymentRequestId(), token);
//			marketInfo = mock();
		} catch (Throwable t) {
			logger.error("getMarketingInfo未知异常 token=" + token, t);
		}
		return JSON.toJSONString(marketInfo); // TODO 空值是否会报错
	}

	
//	private MarketingInfoVO mock(){
//		MarketingInfoVO hasActivity = new MarketingInfoVO();
//		hasActivity.setDoMarketActivity("Y");
//		Map<String, MarketingInfoVO> activityCopyWrites = new HashMap<String, MarketingInfoVO>();
//		hasActivity.setActivityCopyWrites(activityCopyWrites);
//
//		MarketingInfoVO allActivityLevel = new MarketingInfoVO();
//		allActivityLevel.setCopyWrite("全部支付方式做营销立减");
//		activityCopyWrites.put("ALL", allActivityLevel);
//		MarketingInfoVO yjzfActivityLevel = new MarketingInfoVO();
//		yjzfActivityLevel.setCopyWrite("一键支付做营销立减");
//		activityCopyWrites.put("NCPAY", yjzfActivityLevel);
//		MarketingInfoVO ebankActivityLevel = new MarketingInfoVO();
//		ebankActivityLevel.setActivityCopyWrites(new HashMap<String, MarketingInfoVO>());
////		ebankActivityLevel.setCopyWrite("网银支付做营销立减");
//		activityCopyWrites.put("EANK", ebankActivityLevel);
//		MarketingInfoVO B2BActivityLevel = new MarketingInfoVO();
//		B2BActivityLevel.setActivityCopyWrites(new HashMap<String, MarketingInfoVO>());
//		ebankActivityLevel.getActivityCopyWrites().put("B2B", B2BActivityLevel);
//		MarketingInfoVO ICBC = new MarketingInfoVO();
//		ICBC.setCopyWrite("招行对私立减");
//		MarketingInfoVO ABC = new MarketingInfoVO();
//		ABC.setCopyWrite("农行对私立减");
//		B2BActivityLevel.getActivityCopyWrites().put("ICBC", ICBC);
//		B2BActivityLevel.getActivityCopyWrites().put("ABC", ABC);
//		return hasActivity;
//	}
}
