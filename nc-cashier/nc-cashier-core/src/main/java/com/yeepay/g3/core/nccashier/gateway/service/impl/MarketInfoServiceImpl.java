package com.yeepay.g3.core.nccashier.gateway.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.MarketInfoService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.ConstantUtil;
import com.yeepay.g3.core.nccashier.vo.ActivityInfoOfPayProduct;
import com.yeepay.g3.core.nccashier.vo.MarketReductionActivitiesRequestParam;
import com.yeepay.g3.facade.mktg.dto.MarketingRequestDTO;
import com.yeepay.g3.facade.mktg.dto.MarketingResponseDTO;
import com.yeepay.g3.facade.mktg.dto.MarketingResponseDTO.Activity;
import com.yeepay.g3.facade.mktg.dto.scene.PaymentBank;
import com.yeepay.g3.facade.mktg.dto.scene.PaymentScene;
import com.yeepay.g3.facade.mktg.dto.scene.PaymentType;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Service("marketInfoService")
public class MarketInfoServiceImpl extends NcCashierBaseService implements MarketInfoService {

    private static Logger logger = LoggerFactory.getLogger(MarketInfoService.class);

    private MarketingRequestDTO buildCashierRequestDTO(MarketReductionActivitiesRequestParam requestParam) {
        MarketingRequestDTO requestDTO = new MarketingRequestDTO();
        requestDTO.setMerchantNo(requestParam.getMerchantNo());
        requestDTO.setMerchantOrderNo(requestParam.getMerchantOrderNo());
        requestDTO.setOrderAmount(requestParam.getOrderAmount());
        requestDTO.setParentMerchantNo(requestParam.getParentMerchantNo());
        return requestDTO;
    }

    @Override
    public Map<String, ActivityInfoOfPayProduct> queryMarketingReductionActivities(
            MarketReductionActivitiesRequestParam requestParam) {
        MarketingRequestDTO cashierRequestDTO = buildCashierRequestDTO(requestParam);
        MarketingResponseDTO cashierResponse = null;
        try{
            cashierResponse = marketingFacade.queryPromotion(cashierRequestDTO);
        }catch(Throwable t){
            logger.error("调用营销系统查询营销规则异常", t);
        }
        //cashierResponse = mockOneActivityOfMarketingResponseDTO();// TODO 测试
        return buildMarketingReductionActivites(cashierResponse);
    }

    public MarketingResponseDTO mockOneActivityOfMarketingResponseDTO() {
        MarketingResponseDTO responseDTO = new MarketingResponseDTO();
        responseDTO.setCode("MK000000");
        List<Activity> activityList = new ArrayList<MarketingResponseDTO.Activity>();
        Activity activity1 = new Activity();
        activity1.setCopyWriter("促销"); // 活动文案
        activity1.setMarketingNo("122334455");// 活动号
        activityList.add(activity1);
        List<PaymentScene> paymentScenes1 = new ArrayList<PaymentScene>();
        PaymentScene paymentScene = new PaymentScene(); // 支持的支付产品

        paymentScene.setPaymentProduct("EWALLET");
        ArrayList<PaymentType> paymentTypes = new ArrayList<PaymentType>();
        PaymentType paymentType = new PaymentType();
        paymentType.setPaymentBank(null);
        paymentType.setType("ALIPAY");
        paymentTypes.add(paymentType);
        paymentScene.setPaymentType(paymentTypes);

        PaymentScene paymentScene2 = new PaymentScene(); // 支持的支付产品
        paymentScene2.setPaymentProduct("WECHAT_OPENID");
        ArrayList<PaymentType> wechatTypeList = new ArrayList<PaymentType>();
        PaymentType wechatType = new PaymentType();
        wechatType.setPaymentBank(null);
        wechatType.setType("WECHAT_OPENID");
        wechatTypeList.add(wechatType);
        paymentScene2.setPaymentType(wechatTypeList);

        paymentScenes1.add(paymentScene);
        paymentScenes1.add(paymentScene2);
        activity1.setActivityScale(paymentScenes1);
        responseDTO.setActivityList(activityList);
        return responseDTO;
    }

    private Map<String, ActivityInfoOfPayProduct> buildMarketingReductionActivites(
            MarketingResponseDTO cashierResponse) {
        if (cashierResponse == null || !ConstantUtil.MARKET_ACTIVITY_SYS_SUCCESS_CODE.equals(cashierResponse.getCode())
                || CollectionUtils.isEmpty(cashierResponse.getActivityList())) {
            return null;
        }
        Map<String, ActivityInfoOfPayProduct> activities = new HashMap<String, ActivityInfoOfPayProduct>();
        for (Activity activity : cashierResponse.getActivityList()) {
            if (CollectionUtils.isEmpty(activity.getActivityScale())) {
                continue;
            }
            // 每个活动只有一个活动ID，但是可能支持多个支付产品,如果支持全部，就认为不会再支持部分
            for (PaymentScene paymentScene : activity.getActivityScale()) {
                parsePaymentSceneOfActivity(activity, paymentScene, activities);
            }
        }
        return activities;
    }

    private void parsePaymentSceneOfActivity(Activity activity, PaymentScene paymentScene,
                                             Map<String, ActivityInfoOfPayProduct> activities) {
        if (CollectionUtils.isEmpty(paymentScene.getPaymentType())) {
            // 只在二级-支付产品级别上做活动
            buildActivityInfoOfPayProduct(ConstantUtil.getMarketingPaymentProductKey(paymentScene.getPaymentProduct()),
                    activity, activities);
            return;
        }

        for (PaymentType paymentType : paymentScene.getPaymentType()) {
            // 到三级粒度做活动
            if (StringUtils.isBlank(paymentType.getType())) {
                // type为空：不限制借贷/对公对私
                if (CollectionUtils.isEmpty(paymentType.getPaymentBank()) || (paymentType.getPaymentBank().size() == 1
                        && ConstantUtil.ALL_TAG.equals(paymentType.getPaymentBank().get(0)))) {
                    // 相当于在二级做活动
                    buildActivityInfoOfPayProduct(
                            ConstantUtil.getMarketingPaymentProductKey(paymentScene.getPaymentProduct()), activity,
                            activities);
                    continue;
                } else {
                    // 在二级+bankCode级别上做活动
                    for (PaymentBank paymentBank : paymentType.getPaymentBank()) {
                        String key = ConstantUtil.getMarketingPaymentProductKey(paymentScene.getPaymentProduct()) + ConstantUtil.LINE
                                + ConstantUtil.ALL + ConstantUtil.LINE + paymentBank.getBankCode();
                        buildActivityInfoOfPayProduct(key, activity, activities);
                    }
                }

            } else {
                // type不为空：限制借贷/对公对私
                if (CollectionUtils.isEmpty(paymentType.getPaymentBank()) || (paymentType.getPaymentBank().size() == 1
                        && ConstantUtil.ALL_TAG.equals(paymentType.getPaymentBank().get(0).getBankCode()))) {
                    // 在二级+type的级别上做活动
                    String key = ConstantUtil.getMarketingPaymentProductKey(paymentScene.getPaymentProduct()) + ConstantUtil.LINE
                            + paymentType.getType();
                    if (ConstantUtil.ALL.equals(ConstantUtil.getMarketingPaymentProductKey(paymentScene.getPaymentProduct()))){
                        key = ConstantUtil.ALL;
                    }
                    buildActivityInfoOfPayProduct(key, activity, activities);
                    continue;
                } else {
                    // 在二级+type+bankCode级别上做活动
                    // 在二级+type的级别上做活动
                    for (PaymentBank paymentBank : paymentType.getPaymentBank()) {
                        String key = ConstantUtil.getMarketingPaymentProductKey(paymentScene.getPaymentProduct()) + ConstantUtil.LINE
                                + paymentType.getType() + ConstantUtil.LINE + paymentBank.getBankCode();
                        if (StringUtils.isBlank(paymentBank.getBankCode())){
                            key = ConstantUtil.getMarketingPaymentProductKey(paymentScene.getPaymentProduct()) + ConstantUtil.LINE
                                    + paymentType.getType();
                        }
                        buildActivityInfoOfPayProduct(key, activity, activities);
                    }
                }
            }
        }
    }

    private void buildActivityInfoOfPayProduct(String key, Activity activity,
                                               Map<String, ActivityInfoOfPayProduct> activities) {
        ActivityInfoOfPayProduct activityInfo = activities.get(key);
        if (activityInfo == null) {
            activityInfo = new ActivityInfoOfPayProduct();
            activityInfo.init();
        }
        activityInfo.addActivityId(activity.getMarketingNo());
        activityInfo.addCopyWrites(activity.getCopyWriter());
        activities.put(key, activityInfo);
    }
}
