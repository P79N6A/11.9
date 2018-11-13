package com.yeepay.g3.core.payprocessor.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.yeepay.g3.facade.account.pay.enums.FeeTypeEnum;
import com.yeepay.g3.facade.account.pay.enums.RequestStatusEnum;

/**
 * 支付处理器发送账户支付的支付结果，并处理该MQ消息
 * 
 * 
 * @author：zhangxh
 * @since：2017年5月23日 下午7:16:01
 * @version:
 */
public class AccountPayResultMessage implements Serializable {

    private static final long serialVersionUID = 1681745364489384085L;

    /**
     * 请求状态
     */
    private RequestStatusEnum status;

    /**
     * 订单交易时间
     */
    private Date trxTime;

    /**
     * 异常码
     */
    private String exceptionCode;

    /**
     * 失败原因
     */
    private String failureReason;

    /**
     * 调用方
     */
    private String operator;

    /**
     * 支付接口编码
     */
    private String payInterfaceCode;

    /**
     * 支付订单号
     */
    private String OrderNo;

    /**
     * 订单接收时间
     */
    private Date createTime;

    /**
     * 收款方商编
     */
    private String creditCustomerNo;

    /**
     * 付款方商编
     */
    private String debitCustomerNo;

    /**
     * 付款方账号
     */
    private String debitAccountNo;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易手续费
     */
    private BigDecimal fee;

    /**
     * 手续费类型(内扣或外扣)
     */
    private FeeTypeEnum feeType;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 基础产品码
     *
     */
    private String basicProductCode;

    /**
     * 商户账户支付系统内部订单号
     */
    private String accountPayOrderNo;

    public RequestStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RequestStatusEnum status) {
        this.status = status;
    }

    public Date getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(Date trxTime) {
        this.trxTime = trxTime;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPayInterfaceCode() {
        return payInterfaceCode;
    }

    public void setPayInterfaceCode(String payInterfaceCode) {
        this.payInterfaceCode = payInterfaceCode;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreditCustomerNo() {
        return creditCustomerNo;
    }

    public void setCreditCustomerNo(String creditCustomerNo) {
        this.creditCustomerNo = creditCustomerNo;
    }

    public String getDebitCustomerNo() {
        return debitCustomerNo;
    }

    public void setDebitCustomerNo(String debitCustomerNo) {
        this.debitCustomerNo = debitCustomerNo;
    }

    public String getDebitAccountNo() {
        return debitAccountNo;
    }

    public void setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public FeeTypeEnum getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeTypeEnum feeType) {
        this.feeType = feeType;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getBasicProductCode() {
        return basicProductCode;
    }

    public void setBasicProductCode(String basicProductCode) {
        this.basicProductCode = basicProductCode;
    }

    public String getAccountPayOrderNo() {
        return accountPayOrderNo;
    }

    public void setAccountPayOrderNo(String accountPayOrderNo) {
        this.accountPayOrderNo = accountPayOrderNo;
    }

    @Override
    public String toString() {
        return "AccountPayResultMessage{" +
                "status=" + status +
                ", trxTime=" + trxTime +
                ", exceptionCode='" + exceptionCode + '\'' +
                ", failureReason='" + failureReason + '\'' +
                ", operator='" + operator + '\'' +
                ", payInterfaceCode='" + payInterfaceCode + '\'' +
                ", OrderNo='" + OrderNo + '\'' +
                ", createTime=" + createTime +
                ", creditCustomerNo='" + creditCustomerNo + '\'' +
                ", debitCustomerNo='" + debitCustomerNo + '\'' +
                ", debitAccountNo='" + debitAccountNo + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", feeType=" + feeType +
                ", extInfo='" + extInfo + '\'' +
                ", basicProductCode='" + basicProductCode + '\'' +
                ", accountPayOrderNo='" + accountPayOrderNo + '\'' +
                '}';
    }
}
