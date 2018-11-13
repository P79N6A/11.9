package com.yeepay.g3.core.nccashier.biz.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.MarketActivityBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.ActivityInfoOfPayProduct;
import com.yeepay.g3.facade.nccashier.dto.ActivityInfoOfPayProductDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityResponseDTO;

@Service
public class MarketActivityBizImpl extends NcCashierBaseBizImpl implements MarketActivityBiz {

	@Resource
	private MerchantVerificationService merchantVerificationService;

	@Override
	public MarketActivityResponseDTO getMarketActivityInfo(MarketActivityRequestDTO requestDTO) {
		MarketActivityResponseDTO responseDTO = new MarketActivityResponseDTO();
		try {
			// 校验入参
			validateMarketActivityRequestDTO(requestDTO);
			// 校验支付请求是否存在
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			// 获取营销活动
			filterMarketActivityInfo(requestDTO.getTokenId(), paymentRequest, responseDTO);
		} catch (Throwable t) {
			handleException(responseDTO, t);
		}
		return responseDTO;
	}

	@SuppressWarnings("unchecked")
	private void filterMarketActivityInfo(String token, PaymentRequest paymentRequest,
			MarketActivityResponseDTO responseDTO) {
		Map<String, ActivityInfoOfPayProduct> activities = marketInfoManageService.getMarketActivity(token,
				paymentRequest);
		if (MapUtils.isEmpty(activities)) {
			return;
		}
		Map<String, ActivityInfoOfPayProductDTO> activitiesRes = new HashMap<String, ActivityInfoOfPayProductDTO>();
		Iterator<?> it = activities.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, ActivityInfoOfPayProduct> entry = (Entry<String, ActivityInfoOfPayProduct>) it.next();
			ActivityInfoOfPayProductDTO productDTO = buildActivityInfoOfPayProductDTO(entry.getValue());
			activitiesRes.put(entry.getKey(), productDTO);
		}
		responseDTO.setActivities(activitiesRes);
	}
	
	private ActivityInfoOfPayProductDTO buildActivityInfoOfPayProductDTO(
			ActivityInfoOfPayProduct activityInfoOfPayProduct) {
		ActivityInfoOfPayProductDTO productDTO = new ActivityInfoOfPayProductDTO();
		productDTO.setActivityIds(activityInfoOfPayProduct.getActivityIds());
		productDTO.setCopyWrites(activityInfoOfPayProduct.getCopyWrites());
		return productDTO;
	}

	private void validateMarketActivityRequestDTO(MarketActivityRequestDTO requestDTO) {
		BeanValidator.validate(requestDTO);
		NcCashierLoggerFactory.TAG_LOCAL.set("[getMarketActivityInfo],支付请求ID=" + requestDTO.getRequestId() +", token=" + requestDTO.getTokenId());
	}

}
