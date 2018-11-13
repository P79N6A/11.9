/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.entity;

import com.yeepay.g3.utils.common.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类名称: CombPayRecord <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/14 上午11:59
 * @version: 1.0.0
 */

public class CombPayRecord {

    /**
     * 主键
     */
    private long id;

    /**
     * 支付订单号索引
     */
    private String recordNo;

    /**
     * 第二支付类型
     */
    private String payOrderType;

    /**
     * 营销系统活动号
     */
    private String marketingNo;

    /**
     * 第二支付订单号
     */
    private String payOrderNo;

    /**
     * 第二银行订单号
     */
    private String bankOrderNo;

    /**
     * 第二支付金额
     */
    private BigDecimal amount;

    /**
     * 产品类型
     */
    private String payProduct;

    /**
     * 第二支付订单状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 通道编码
     */
    private String frpCode;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }

    public String getMarketingNo() {
        return marketingNo;
    }

    public void setMarketingNo(String marketingNo) {
        this.marketingNo = marketingNo;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayProduct() {
        return payProduct;
    }

    public void setPayProduct(String payProduct) {
        this.payProduct = payProduct;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getFrpCode() {
        return frpCode;
    }

    public void setFrpCode(String frpCode) {
        this.frpCode = frpCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        if(StringUtils.isNotBlank(errorMsg) && errorMsg.length() > 256) {
            errorMsg = errorCode.substring(0, 255);
        }
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "CombPayRecord{" +
                "id=" + id +
                ", recordNo='" + recordNo + '\'' +
                ", payOrderType='" + payOrderType + '\'' +
                ", marketingNo='" + marketingNo + '\'' +
                ", payOrderNo='" + payOrderNo + '\'' +
                ", bankOrderNo='" + bankOrderNo + '\'' +
                ", amount=" + amount +
                ", payProduct='" + payProduct + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", payTime=" + payTime +
                ", frpCode='" + frpCode + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}