/**
 * 
 */
package com.yeepay.g3.facade.payprocessor.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;

import java.math.BigDecimal;

/**
 * 返回对象的基类
 * 
 * @author TML
 */
public abstract class BasicResponseDTO extends ResponseStatusDTO {

	private static final long serialVersionUID = 3374969889222656864L;
	/**
	 * 支付订单号，子表主键
	 */
	protected String recordNo;

	/**
	 * 商户订单号
	 */
	protected String outTradeNo;

	/**
	 * 订单方订单号
	 */
	protected String orderNo;

	/**
	 * 系统唯一码
	 */
	protected String dealUniqueSerialNo;

	/**
	 * 订单类型
	 */
	protected PayOrderType payOrderType;

	/**
	 * 商户编号
	 */
	protected String customerNumber;

	/**
	 * 组合支付返回实体
	 */
	protected CombResponseDTO combResponseDTO;

	/**
	 * 如果是组合支付，有此参数，第一支付金额
	 * 如果是预授权，则此参数为预授权完成金额
	 */
	protected BigDecimal firstPayAmount;


	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public PayOrderType getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(PayOrderType payOrderType) {
		this.payOrderType = payOrderType;
	}

	public CombResponseDTO getCombResponseDTO() {
		return combResponseDTO;
	}

	public void setCombResponseDTO(CombResponseDTO combResponseDTO) {
		this.combResponseDTO = combResponseDTO;
	}

	public BigDecimal getFirstPayAmount() {
		return firstPayAmount;
	}

	public void setFirstPayAmount(BigDecimal firstPayAmount) {
		this.firstPayAmount = firstPayAmount;
	}

	@Override
    public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
