package com.yeepay.g3.core.payprocessor.entity;

import java.util.Date;

/**
 * 冲正信息表
 */
public class ReverseRecord {
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
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款请求时间
     */
    private Date requestTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 退款成功时间
     */
    private Date successTime;

    /**
     * 退款标识
     */
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

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
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
        return "ReverseRecord{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", recordNo='" + recordNo + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", requestTime=" + requestTime +
                ", modifyTime=" + modifyTime +
                ", successTime=" + successTime +
                ", remark='" + remark + '\'' +
                '}';
    }
}