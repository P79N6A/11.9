package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 冲正请求参数
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class ReverseRequestDTO implements Serializable {
	
 
	private static final long serialVersionUID = 3342185588583671081L;


	/**
     * 请求系统
     */
    @NotBlank(message = "requestSystem不能为空")
    private String requestSystem;

    /**
     * 订单方订单号
     */
    private String orderNo;

    /**
     * 支付订单号
     */
    @NotBlank(message = "recordNo不能为空")
    private String recordNo;

    /**
     * 冲正说明
     */
    private String remark;

    public String getRequestSystem() {
        return requestSystem;
    }

    public void setRequestSystem(String requestSystem) {
        this.requestSystem = requestSystem;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }
}
