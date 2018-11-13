package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MarketingInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否做营销活动:Y（做），N：不做
	 */
	private String doMarketActivity;

	/**
	 * 文案
	 */
	private String copyWrite;

	/**
	 * 当做营销活动的时候，有文案信息 ALL NCPAY EBANK
	 */
	private Map<String, MarketingInfoVO> activityCopyWrites;
	
	/**
	 * 
	 */
//	private MarketActivityCopyWriteInfoVO marketActivityCopyWriteInfoVO;

	public MarketingInfoVO() {

	}

	public MarketingInfoVO(String doMarketActivity) {
		setDoMarketActivity(doMarketActivity);
	}

	public String getDoMarketActivity() {
		return doMarketActivity;
	}

	public void setDoMarketActivity(String doMarketActivity) {
		this.doMarketActivity = doMarketActivity;
	}

	public String getCopyWrite() {
		return copyWrite;
	}

	public void setCopyWrite(String copyWrite) {
		this.copyWrite = copyWrite;
	}

	public Map<String, MarketingInfoVO> getActivityCopyWrites() {
		return activityCopyWrites;
	}

	public void setActivityCopyWrites(Map<String, MarketingInfoVO> activityCopyWrites) {
		this.activityCopyWrites = activityCopyWrites;
	}
	

//	public MarketActivityCopyWriteInfoVO getMarketActivityCopyWriteInfoVO() {
//		return marketActivityCopyWriteInfoVO;
//	}
//
//	public void setMarketActivityCopyWriteInfoVO(MarketActivityCopyWriteInfoVO marketActivityCopyWriteInfoVO) {
//		this.marketActivityCopyWriteInfoVO = marketActivityCopyWriteInfoVO;
//	}
	
	
	public MarketingInfoVO getChildMarketInfoByKey(String childKey) {
		if (activityCopyWrites == null) {
			// 如果上一级的孩子节点活动信息为空的话，初始化其孩子节点活动实体
			activityCopyWrites = new HashMap<String, MarketingInfoVO>();
		}
		if(activityCopyWrites.get(childKey) == null){
			MarketingInfoVO curLevel = new MarketingInfoVO();
			activityCopyWrites.put(childKey, curLevel);
		}
		return activityCopyWrites.get(childKey);
	}

	public String toString() {
		return com.alibaba.fastjson.JSON.toJSONString(this);
	}

	public static void main(String[] args) {
		MarketingInfoVO noActivity = new MarketingInfoVO();
		noActivity.setDoMarketActivity("N");
		System.out.println(noActivity);

		MarketingInfoVO hasActivity = new MarketingInfoVO();
		hasActivity.setDoMarketActivity("Y");
		Map<String, MarketingInfoVO> activityCopyWrites = new HashMap<String, MarketingInfoVO>();
		hasActivity.setActivityCopyWrites(activityCopyWrites);
		
		MarketingInfoVO allActivityLevel = new MarketingInfoVO();
		allActivityLevel.setCopyWrite("全部支付方式做营销立减");
		activityCopyWrites.put("ALL", allActivityLevel);
		MarketingInfoVO yjzfActivityLevel = new MarketingInfoVO();
		yjzfActivityLevel.setCopyWrite("一键支付做营销立减");
		activityCopyWrites.put("NCPAY", yjzfActivityLevel);
		MarketingInfoVO ebankActivityLevel = new MarketingInfoVO();
		ebankActivityLevel.setActivityCopyWrites(new HashMap<String, MarketingInfoVO>());
//		ebankActivityLevel.setCopyWrite("网银支付做营销立减");
		activityCopyWrites.put("EANK", ebankActivityLevel);
		MarketingInfoVO B2BActivityLevel = new MarketingInfoVO();
		B2BActivityLevel.setActivityCopyWrites(new HashMap<String, MarketingInfoVO>());
		ebankActivityLevel.getActivityCopyWrites().put("B2B", B2BActivityLevel);
		MarketingInfoVO ICBC = new MarketingInfoVO();
		ICBC.setCopyWrite("招行对私立减");
		MarketingInfoVO ABC = new MarketingInfoVO();
		ABC.setCopyWrite("农行对私立减");
		B2BActivityLevel.getActivityCopyWrites().put("ICBC", ICBC);
		B2BActivityLevel.getActivityCopyWrites().put("ABC", ABC);
		System.out.println(hasActivity);
	}

}
