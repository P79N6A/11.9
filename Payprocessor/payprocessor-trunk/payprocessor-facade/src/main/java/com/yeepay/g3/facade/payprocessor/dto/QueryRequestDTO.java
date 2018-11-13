package com.yeepay.g3.facade.payprocessor.dto;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 查单请求参数---订单处理器使用
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class QueryRequestDTO implements Serializable {
    

	private static final long serialVersionUID = -1827024484417201904L;


	/**
     * 订单方
     */
    @NotBlank(message = "orderSystem不能为空")
    private String orderSystem;

    /**
     * 订单方订单号
     */
    @NotBlank(message = "orderNo不能为空")
    private String orderNo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

    public String getOrderSystem() {
        return orderSystem;
    }

    public void setOrderSystem(String orderSystem) {
        this.orderSystem = orderSystem;
    }
}
