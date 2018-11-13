/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.entity;

import java.util.Date;

/**
 * 类名称: PreAuthReverseRecord <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/23 下午6:55
 * @version: 1.0.0
 */

public class PreAuthReverseRecord {

    /**
     * 主键
     */
    private Long id;

    /**
     * paymentRequest主键
     */
    private Long requestId;

    /**
     * 支付订单号,Record主键,唯一索引
     */
    private String recordNo;

    /**
     * 预授权冲正流水号
     */
    private String reverseNo;

    /**
     * ncpay原支付订单号
     */
    private String orgPaymentNo;

    /**
     * ncpay支付订单号
     */
    private String paymentNo;

    /**
     * ncpay的业务方
     */
    private long biz;

    /**
     * 原订单类型
     */
    private String orgPayOrderType;

    /**
     * 订单类型
     */
    private String payOrderType;

    /**
     * 支付状态
     */
    private String cancelStatus;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 成功时间
     */
    private Date successTime;

    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getReverseNo() {
        return reverseNo;
    }

    public void setReverseNo(String reverseNo) {
        this.reverseNo = reverseNo;
    }

    public String getOrgPaymentNo() {
        return orgPaymentNo;
    }

    public void setOrgPaymentNo(String orgPaymentNo) {
        this.orgPaymentNo = orgPaymentNo;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public long getBiz() {
        return biz;
    }

    public void setBiz(long biz) {
        this.biz = biz;
    }

    public String getOrgPayOrderType() {
        return orgPayOrderType;
    }

    public void setOrgPayOrderType(String orgPayOrderType) {
        this.orgPayOrderType = orgPayOrderType;
    }

    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PreAuthReverseRecord{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", recordNo='" + recordNo + '\'' +
                ", reverseNo='" + reverseNo + '\'' +
                ", orgPaymentNo='" + orgPaymentNo + '\'' +
                ", paymentNo='" + paymentNo + '\'' +
                ", biz=" + biz +
                ", orgPayOrderType='" + orgPayOrderType + '\'' +
                ", payOrderType='" + payOrderType + '\'' +
                ", cancelStatus='" + cancelStatus + '\'' +
                ", requestTime=" + requestTime +
                ", modifyTime=" + modifyTime +
                ", successTime=" + successTime +
                ", remark='" + remark + '\'' +
                '}';
    }
}