/**
 * 
 */
package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.facade.frontend.enumtype.PlatformType;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 请求的基类DTO
 * @author TML
 */
public abstract class BasicRequestDTO implements Serializable{

	protected static final long serialVersionUID = 397813017249933028L;

	/**
	 * 业务方
	 */
	@NotBlank(message = "requestSystem不能为空")
	protected String requestSystem;

	/**
	 * 业务方订单号
	 */
	@NotBlank(message = "requestId不能为空")
    protected String requestId;
	
	/**
	 * 商户订单号
	 */
	@NotBlank(message = "outTradeNo不能为空")
	protected String outTradeNo;

	/**
	 * 支付金额
	 */
	@DecimalMin(value="0.01",message = "totalAmount最小值为0.01")
	protected BigDecimal totalAmount;

	/**
	 * 微信被扫设备信息 无用字段
	 */
	@Deprecated
	protected String deviceInfo;

	/**
     * 商户编号
     */
	@NotBlank(message = "customerNumber不能为空")
	protected String customerNumber;
	
	/**
	 * 平台类型
	 */
	@NotNull(message = "platformType不能为空")
    protected PlatformType platformType;

    /**
     * 商品描述
     */
	@NotBlank(message = "goodsDescription不能为空")
    protected String goodsDescription;
    
    /**
	 * 客户端ip
	 */
    @NotBlank(message = "payerIp不能为空")
	protected String payerIp;
    
    /**
	 * 商户名称
	 */
    @NotBlank(message = "customerName不能为空")
	protected String customerName;
	
	/**
	 * 商户等级
	 */
	protected String customerLevel;
	
	/**
	 * 行业编码
	 */
	protected String industryCode;
	
	/**
	 * 交易系统订单方
	 */
	@NotBlank(message = "orderSystem不能为空")
	protected String orderSystem;

	/**
	 * 商品信息 json字符串
	 */
	protected String goodsInfo;
	/**
	 * 工具信息 json字符串
	 */
	protected String toolsInfo;
	
	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}

	public String getGoodsDescription() {
		return goodsDescription;
	}

	public void setGoodsDescription(String goodsDescription) {
		this.goodsDescription = goodsDescription;
	}

	public String getPayerIp() {
		return payerIp;
	}

	public void setPayerIp(String payerIp) {
		this.payerIp = payerIp;
	}

	public String getOrderSystem() {
		return orderSystem;
	}

	public void setOrderSystem(String orderSystem) {
		this.orderSystem = orderSystem;
	}

	public String getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(String goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public String getToolsInfo() {
		return toolsInfo;
	}

	public void setToolsInfo(String toolsInfo) {
		this.toolsInfo = toolsInfo;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	
}
