package com.yeepay.g3.core.nccashier.vo;

import java.util.List;

import com.yeepay.g3.facade.mktg.dto.scene.PaymentScene;

/**
 * 营销立减活动信息 —— 一个活动只会包含一种支付方式
 * 
 * @author duangduang
 *
 */
public class MarketingReductionActivity {

	/**
	 * 营销活动号
	 */
	private String marketingNo;

	/**
	 * 优惠类型
	 * 
	 * @see com.yeepay.g3.facade.mktg.enums.MarketingTypeEnum
	 */
	private String discountType;

	/**
	 * 优惠金额或优惠金额范围
	 */
	private String discountAmount;

	/**
	 * 活动文案
	 */
	private String copyWriter;

	/**
	 * 支付场景信息
	 */
	private List<PaymentScene> activityScale;

	public MarketingReductionActivity() {

	}

	public String getMarketingNo() {
		return marketingNo;
	}

	public void setMarketingNo(String marketingNo) {
		this.marketingNo = marketingNo;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getCopyWriter() {
		return copyWriter;
	}

	public void setCopyWriter(String copyWriter) {
		this.copyWriter = copyWriter;
	}

	public List<PaymentScene> getActivityScale() {
		return activityScale;
	}

	public void setActivityScale(List<PaymentScene> activityScale) {
		this.activityScale = activityScale;
	}

}
