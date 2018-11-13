package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.frontend.enumtype.OrderType;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.frontend.enumtype.PlatformType;

public class OpenPayResponseDTO extends BasicResponseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 支付链接
	 */
	private String prepayCode;

	/**
	 * 平台类型
	 */
	private PlatformType platformType;

	/**
	 * 订单类型
	 */
	private OrderType orderType;
	
	/**
	 * 路由处理状态（1：成功；0：失败）与sceneTypeExt组合使用
	 * 若状态为1，同时场景类型为jsapiH5，此时预路由不返appId也表示有通道可用；
	 * 若状态为1，同时场景类型为normal，此时与原来保持一致。
	 */
	private Integer dealStatus;
	/**
	 * 场景类型扩展（jsapiH5：微信内部H5通道；normal：正常通道）
	 */
	private String sceneTypeExt;

	public String getPrepayCode() {
		return prepayCode;
	}

	public void setPrepayCode(String prepayCode) {
		this.prepayCode = prepayCode;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Integer getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getSceneTypeExt() {
		return sceneTypeExt;
	}

	public void setSceneTypeExt(String sceneTypeExt) {
		this.sceneTypeExt = sceneTypeExt;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

}
