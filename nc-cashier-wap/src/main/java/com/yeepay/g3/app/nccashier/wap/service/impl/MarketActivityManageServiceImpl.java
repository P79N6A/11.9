package com.yeepay.g3.app.nccashier.wap.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.MarketActivityManageService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.utils.ConstantUtil;
import com.yeepay.g3.app.nccashier.wap.vo.MarketingInfoVO;
import com.yeepay.g3.facade.nccashier.dto.ActivityInfoOfPayProductDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;


@Service("marketActivityManageService")
public class MarketActivityManageServiceImpl implements MarketActivityManageService {

	@Resource
	private NcCashierService ncCashierService;

	@Override
	public MarketingInfoVO getMarketActivityInfo(long requestId, String tokenId) {
		MarketActivityRequestDTO marketActivityRequestDTO = buildMarketActivityRequestDTO(requestId, tokenId);
		MarketActivityResponseDTO marketActivityResponse = ncCashierService
				.getMarketActivityInfo(marketActivityRequestDTO);
		return buildMarketingInfoVO(marketActivityResponse);
	}

	@SuppressWarnings("unchecked")
	private MarketingInfoVO buildMarketingInfoVO(MarketActivityResponseDTO marketActivityResponse) {
		MarketingInfoVO marketInfoRes = new MarketingInfoVO();
		if (marketActivityResponse == null || MapUtils.isEmpty(marketActivityResponse.getActivities())) {
			// 为空的话，不参与营销活动
			marketInfoRes.setDoMarketActivity(ConstantUtil.NO);
			return marketInfoRes;
		}

		// 做营销活动
		marketInfoRes.setDoMarketActivity(ConstantUtil.YES);
		marketInfoRes.setActivityCopyWrites(new HashMap<String, MarketingInfoVO>());
		
		Map<String, ActivityInfoOfPayProductDTO> activityInfoMap = marketActivityResponse.getActivities();
		Iterator<?> it = activityInfoMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ActivityInfoOfPayProductDTO> entry = (Map.Entry<String, ActivityInfoOfPayProductDTO>) it
					.next();
			String[] keys = entry.getKey().split(ConstantUtil.MIDDLE_LINE);

			String name = keys[0];
			//转换展示信息
			if (keys.length == 2 && PayTool.SCCANPAY.name().equals(keys[0])){
				name = keys[0] + ConstantUtil.MIDDLE_LINE + transferPayType(keys[1]);
			}

			if (keys.length == 2 && (PayTool.EWALLET.name().equals(keys[0])
					|| PayTool.WECHAT_OPENID.name().equals(keys[0])
					|| PayTool.EWALLETH5.name().equals(keys[0])
					|| PayTool.ZFB_SHH.name().equals(keys[0]))){
				name = keys[1];
			}

			MarketingInfoVO firstLevel = marketInfoRes.getChildMarketInfoByKey(name);// 第一级key目前可能取值：ALL/NCPAY/EANK……
			if (keys.length == 1 || (PayTool.NCPAY.name().equals(keys[0])
					|| (PayTool.EANK.name().equals(keys[0]) && keys.length == 2))
					|| PayTool.SCCANPAY.name().equals(keys[0])
					|| PayTool.EWALLET.name().equals(keys[0])
					|| PayTool.WECHAT_OPENID.name().equals(keys[0])
					|| PayTool.EWALLETH5.name().equals(keys[0])
					|| PayTool.ZFB_SHH.name().equals(keys[0])
					|| PayTool.BK_ZF.name().equals(keys[0])) {
				// 如果全部支持工具支持或者在第二层级支付产品上支持，第一级有文案说明
				// 其他产品先不管，到时候如果需要的话，按照产品新给的原型，重新开发
				List<String> currentCopyWrites = activityInfoMap.get(entry.getKey()).getCopyWrites();
				if(CollectionUtils.isEmpty(currentCopyWrites)){
					continue;
				}
				for(String currentCopyWrite : currentCopyWrites){
					if(StringUtils.isBlank(currentCopyWrite)){
						continue;
					}
					if(StringUtils.isBlank(firstLevel.getCopyWrite())){
						firstLevel.setCopyWrite(currentCopyWrite);
					}else if(!firstLevel.getCopyWrite().contains(currentCopyWrite)){
						firstLevel.setCopyWrite(firstLevel.getCopyWrite() + "_" + currentCopyWrite);
					}
				}
			} else if (PayTool.EANK.name().equals(keys[0]) && keys.length == 3) {
				// 网银有三级的要特殊处理，要求
				MarketingInfoVO secondLevel = firstLevel.getChildMarketInfoByKey(keys[1]); // 对公对私级别
				MarketingInfoVO thirdLevel = secondLevel.getChildMarketInfoByKey(keys[2]); // 银行级别
				// 因为网银无需返回文案，为了减少传输的数据量，统一返回“立减”二字
				thirdLevel.setCopyWrite(ConstantUtil.MARKET_SUBTRACTION);
			}
			marketInfoRes.getActivityCopyWrites().put(name, firstLevel);
		}
		return marketInfoRes;
	}
	

	/**
	 * 构造请求nc-cashier-hessian获取营销活动信息的入参
	 * 
	 * @param requestId
	 * @param tokenId
	 * @return
	 */
	private MarketActivityRequestDTO buildMarketActivityRequestDTO(long requestId, String tokenId) {
		MarketActivityRequestDTO request = new MarketActivityRequestDTO();
		request.setRequestId(requestId);
		request.setTokenId(tokenId);
		return request;
	}

	/**
	 * 判断支付方式
	 * @param payType
	 * @return
	 */
	private String transferPayType(String payType){
		if (PayTypeEnum.ALIPAY.name().equals(payType) ){
			return "ALIPAY";

		};
		if (PayTypeEnum.WECHAT_ATIVE_SCAN.name().equals(payType)){
			return "WECHAT";
		}
		if (PayTypeEnum.UPOP_ATIVE_SCAN.name().equals(payType)){
			return "UNIONPAY";
		}
		if (PayTypeEnum.JD_ATIVE_SCAN.name().equals(payType)){
			return "JD";
		}
		if (PayTypeEnum.QQ_ATIVE_SCAN.name().equals(payType)){
			return "QQ";
		}
		return "";
	}
}
