/**
 * 
 */
package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.yeepay.g3.facade.payprocessor.enumtype.CashierVersion;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;

/**
 * 请求的基类DTO
 * 
 * @author TML
 */
public abstract class BasicRequestDTO implements Serializable {

	private static final long serialVersionUID = 2942130695741733336L;
	/**
	 * 商户订单号
	 */
	@NotBlank(message = "outTradeNo不能为空")
	@Length(min = 1, max = 64, message = "outTradeNo最大长度为64")
	protected String outTradeNo;

	/**
	 * 系统唯一码
	 */
	@NotBlank(message = "dealUniqueSerialNo不能为空")
	@Length(min = 1, max = 50, message = "dealUniqueSerialNo最大长度为50")
	protected String dealUniqueSerialNo;

	/**
	 * 收款场景，废弃
	 */
	@Deprecated
	private CashierVersion cashierVersion;

	/**
	 * 新收款场景
	 */
	private String cashierType;

	/**
	 * 支付产品
	 */
	@NotNull(message = "payProduct不能为空")
	private String payProduct;

	@NotNull(message = "payOrderType不能为空")
	protected PayOrderType payOrderType;

	/**
	 * 交易系统订单方 DS
	 */
	@NotBlank(message = "orderSystem不能为空")
	@Length(max = 20, message = "orderSystem最大长度为20")
	protected String orderSystem;

	/**
	 * 请求系统SOPAY
	 */
	@NotBlank(message = "requestSystem不能为空")
	@Length(max = 20, message = "requestSystem最大长度为20")
	protected String requestSystem;

	/**
	 * 请求系统主键
	 */
	@NotBlank(message = "requestSysId不能为空")
	@Length(max = 60, message = "requestSysId最大长度为20")
	protected String requestSysId;

	/**
	 * 订单方订单号
	 */
	@NotBlank(message = "订单方订单号")
	@Length(max = 60, message = "orderNo最大长度为20")
	protected String orderNo;

	/**
	 * 支付金额
	 */
	@NotNull(message = "amount不能为空")
	@DecimalMin(value = "0.01", message = "totalAmount最小值为0.01")
	protected BigDecimal amount;

	/**
	 * 用户手续费
	 */
	protected BigDecimal userFee;

	/**
	 * 商户编号
	 */
	@NotBlank(message = "customerNumber不能为空")
	@Length(max = 20, message = "customerNumber最大长度为20")
	protected String customerNumber;

	/**
	 * 商户名称
	 */
	@NotBlank(message = "customerName不能为空")
	protected String customerName;

	/**
	 * 商品描述
	 */
	@NotBlank(message = "productName不能为空")
	protected String productName;

	/**
	 * 客户端ip
	 */
	@Length(max = 20, message = "payerIp")
	protected String payerIp;

	/**
	 * 行业编码
	 */
	protected String industryCode;

	/**
	 * 风控标识
	 */
	protected String riskProduction;

	/**
	 * 商品信息 json字符串 透传到风控
	 */
	protected String goodsInfo;

	/**
	 * 工具信息 json字符串 透传到风控
	 */
	protected String toolsInfo;

	/**
	 * 过期时间
	 */
	private Date expireDate;

	/**
	 * 用户id，商户的传值，透传给订单处理器
	 */
	private String userId;

	/**
	 * 用户类型，商户的传值，透传给订单处理器
	 */
	private String userType;

	/**
	 * 组合支付实体
	 */
	protected CombRequestDTO combRequestDTO;


	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestSysId() {
		return requestSysId;
	}

	public void setRequestSysId(String requestSysId) {
		this.requestSysId = requestSysId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPayerIp() {
		return payerIp;
	}

	public void setPayerIp(String payerIp) {
		this.payerIp = payerIp;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getUserFee() {
		return userFee;
	}

	public void setUserFee(BigDecimal userFee) {
		this.userFee = userFee.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public CashierVersion getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(CashierVersion cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public String getPayProduct() {
		return payProduct;
	}

	public void setPayProduct(String payProduct) {
		this.payProduct = payProduct;
	}

	public PayOrderType getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(PayOrderType payOrderType) {
		this.payOrderType = payOrderType;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getRiskProduction() {
		return riskProduction;
	}

	public void setRiskProduction(String riskProduction) {
		this.riskProduction = riskProduction;
	}

	public String getCashierType() {
		return cashierType;
	}

	public void setCashierType(String cashierType) {
		this.cashierType = cashierType;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public CombRequestDTO getCombRequestDTO() {
		return combRequestDTO;
	}

	public void setCombRequestDTO(CombRequestDTO combRequestDTO) {
		this.combRequestDTO = combRequestDTO;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this , "goodsInfo");
	}
}
