package com.yeepay.g3.core.nccashier.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.service.MarketInfoManageService;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.ConstantUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.ActivityInfoOfPayProduct;
import com.yeepay.g3.core.nccashier.vo.MarketReductionActivitiesRequestParam;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.utils.jdbc.dal.utils.StringUtils;

import static com.yeepay.g3.core.nccashier.utils.ConfigCenterUtils.isLoadSystem;

@Service("marketInfoManageService")
public class MarketInfoManageServiceImpl extends NcCashierBaseService implements MarketInfoManageService {

	private static Logger logger = LoggerFactory.getLogger(MarketInfoManageService.class);
	
	@Resource
	private MerchantVerificationService merchantVerificationService;

	@SuppressWarnings("unchecked")
	private Map<String, ActivityInfoOfPayProduct> filterMarketActivityInfo(PayExtendInfo payExtendInfo,
			MarketReductionActivitiesRequestParam requestParam) {
		Map<String, ActivityInfoOfPayProduct> activities = marketInfoService
				.queryMarketingReductionActivities(requestParam);
		if(MapUtils.isEmpty(activities)){
			return null;
		}
		Iterator<?> it = activities.entrySet().iterator();
		while (it.hasNext()) {// ALL EBANK NCPAY，那如果PAY_TOOL有EBANK，则活动保留ALL和EBANK
			boolean exist = false;
			Map.Entry<String, ActivityInfoOfPayProduct> entry = (Map.Entry<String, ActivityInfoOfPayProduct>) it.next();
			if(ConstantUtil.ALL.equals(entry.getKey())){
				// 若当前活动配置条件为ALL，那么适用于所有的支付产品
				continue;
			}
			for (String payTool : payExtendInfo.getPayTool()) {
				// 过滤掉不可用的支付产品 如果是ALL的话，表示所有活动都支持
				if (entry.getKey().indexOf(payTool) != -1) {
					// 只要当前的key包含当前支付方式，就认为支持，进入下一个循环 ——
					// TODO：目前不过滤银行，也就是就算当前商户不支持使用贷记卡，但是有贷记卡的活动也会展示
					if (PayTool.SCCANPAY.name().equals(payTool) || PayTool.EWALLETH5.name().equals(payTool)
							|| PayTool.EWALLET.name().equals(payTool) || PayTool.WECHAT_OPENID.name().equals(payTool)
							|| PayTool.ZFB_SHH.name().equals(payTool)) {
						// 聚合类的或扫码
						String[] composit = entry.getKey().split(ConstantUtil.LINE);
						if (composit.length == 2 &&  payExtendInfo.getPayTypes().indexOf(PayTypeEnum.valueOf(composit[1]).value() + "") != -1) {
							exist = true;
						}
					} else {
						exist = true;
					}
					break;
				}
			}
			if (!exist) {
				it.remove();
			}
		}
		return activities;
	}

	@Override
	public Map<String, ActivityInfoOfPayProduct> getMarketActivity(String token, PaymentRequest paymentRequest) {
		if(!CommonUtil.merchantAttendMarketActivity(paymentRequest.getMerchantNo(), paymentRequest.getOrderSysNo()) || !"PAY_PROCCESOR".equals(paymentRequest.getPaySysCode())
		 || isLoadSystem(paymentRequest.getTradeSysNo())){
			return null;
		}
		String activitiesStr = RedisTemplate.getTargetFromRedis(
				Constant.NCCASHIER_MARKET_ACTIVITY_REDIS_KEY + paymentRequest.getMerchantNo() + "_" + paymentRequest.getOrderAmount(), String.class);
		logger.info("从缓存中获取到的营销活动信息为：" + activitiesStr + "," + ConstantUtil.NO_ACTIVITY.equals(activitiesStr));

		if (ConstantUtil.NO_ACTIVITY.equals(activitiesStr)) {
			logger.info("从缓存中获取得值为{},不参与营销", activitiesStr);
			return null;
		}
		Map<String, ActivityInfoOfPayProduct> activities = null;
		if (StringUtils.isBlank(activitiesStr)) {
			// 获取开通的支付产品
			PayExtendInfo payExtendInfo = merchantVerificationService.getMerchantInNetConfig(token, paymentRequest);
			// 构造请求营销系统的入参
			MarketReductionActivitiesRequestParam requestParam = buildMarketReductionActivitiesRequestParamByPayRequest(
					paymentRequest);
			// 请求营销系统获取营销信息，并根据当前商户开通的支付产品过滤营销活动
			activities = filterMarketActivityInfo(payExtendInfo, requestParam);
			// 将过滤得到的营销活动保存到缓存中
			RedisTemplate.setCacheObjectSumValue(
					Constant.NCCASHIER_MARKET_ACTIVITY_REDIS_KEY + paymentRequest.getMerchantNo() + "_" + paymentRequest.getOrderAmount(),
					MapUtils.isEmpty(activities) ? "NO_ACTIVITY": JSON.toJSONString(activities), CommonUtil.marketActivityRedisTime());
		} else {
			activities = (Map<String, ActivityInfoOfPayProduct>) JSON.parseObject(activitiesStr, new TypeReference<HashMap<String,ActivityInfoOfPayProduct>>(){});
			logger.info("从缓存中获取到的营销活动信息为：" + activities);
		}
		return activities;
	}

	private MarketReductionActivitiesRequestParam buildMarketReductionActivitiesRequestParamByPayRequest(
			PaymentRequest paymentRequest) {
		MarketReductionActivitiesRequestParam requestParam = new MarketReductionActivitiesRequestParam();
		requestParam.setMerchantNo(paymentRequest.getMerchantNo());
		requestParam.setMerchantOrderNo(paymentRequest.getMerchantOrderId());
		requestParam.setOrderAmount(paymentRequest.getOrderAmount());
		return requestParam;
	}

	@Override
	public Set<String> getMarketActivityInfoByPayInfo(String token, PaymentRequest paymentRequest, String payProduct,
			String type, String bankCode) {
		Map<String, ActivityInfoOfPayProduct> activities = getMarketActivity(token, paymentRequest);
		if (MapUtils.isEmpty(activities)) {
			return null;
		}
		Set<String> activityIds = new HashSet<String>();
		List<String> potentialConditions = buildPotentialConditionOfMarketActivity(payProduct, type, bankCode);

		logger.info("查询营销活动 payProduct:{},type:{},bankCode:{},activities:{}", payProduct,type,bankCode,activities);

		for (String potentialCondition : potentialConditions) {
			ActivityInfoOfPayProduct activityInfoOfPayProduct = activities.get(potentialCondition);
			if (activityInfoOfPayProduct == null) {
				continue;
			}
			activityIds.addAll(activityInfoOfPayProduct.getActivityIds());
		}
		return activityIds;
	}

	private List<String> buildPotentialConditionOfMarketActivity(String payProduct, String type, String bankCode) {
		List<String> potentialConditions = new ArrayList<String>();
		potentialConditions.add(ConstantUtil.ALL);
		potentialConditions.add(ConstantUtil.getMarketingPaymentProductKey(payProduct));
		if (StringUtils.isBlank(type)) {
			type = ConstantUtil.ALL;
		}
		potentialConditions.add(ConstantUtil.getMarketingPaymentProductKey(payProduct) + ConstantUtil.LINE + type);
		potentialConditions
				.add(ConstantUtil.getMarketingPaymentProductKey(payProduct) + ConstantUtil.LINE + ConstantUtil.ALL + ConstantUtil.LINE + bankCode);
		potentialConditions.add(ConstantUtil.getMarketingPaymentProductKey(payProduct) + ConstantUtil.LINE + type + ConstantUtil.LINE + bankCode);
		potentialConditions
				.add(ConstantUtil.getMarketingPaymentProductKey(payProduct) + ConstantUtil.LINE + ConstantUtil.ALL + ConstantUtil.LINE + StringUtils.trimToEmpty(bankCode));
		potentialConditions.add(ConstantUtil.getMarketingPaymentProductKey(payProduct) + ConstantUtil.LINE + type + ConstantUtil.LINE + StringUtils.trimToEmpty(bankCode));
		return potentialConditions;
	}
}
