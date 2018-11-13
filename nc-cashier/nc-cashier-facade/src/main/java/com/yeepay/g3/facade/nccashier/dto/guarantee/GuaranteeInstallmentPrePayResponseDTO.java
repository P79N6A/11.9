package com.yeepay.g3.facade.nccashier.dto.guarantee;

import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 担保分期-预支付-返回DTO
 */
public class GuaranteeInstallmentPrePayResponseDTO extends BasicResponseDTO implements Serializable {
    private static final long serialVersionUID = 1153721324182485676L;
    /**
     * paymentRequestId
     */
    private long requestId;
    /**
     * 银行编码
     */
    private String bankCode;
    /**
     * 期数
     */
    private int period;
    /**
     * 订单金额
     */
    private BigDecimal orderAmount;
    /**
     * 手续费率
     */
    private BigDecimal serviceChargeRate;
    /**
     * 手续费
     */
    private BigDecimal serviceCharge;
    /**
     * 每期金额
     */
    private BigDecimal amountPerPeriod;

    /**
     * 需补充卡信息，Y=有需补充项；N=全部无需补充
     */
    private String needSupply;

    /** 需补充-姓名*/
    private boolean needOwner;
    /** 需补充-身份证*/
    private boolean needIdNo;
    /** 需补充-有效期*/
    private boolean needAvlidDate;
    /** 需补充-CVV2*/
    private boolean needCvv;
    /** 需补充-手机号*/
    private boolean needPhoneNo;
    /** 需补充-取款密码*/
    private boolean needBankPWD;
    /**
     * 预路由id
     */
    private String preRouteId;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getServiceChargeRate() {
        return serviceChargeRate;
    }

    public void setServiceChargeRate(BigDecimal serviceChargeRate) {
        this.serviceChargeRate = serviceChargeRate;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getAmountPerPeriod() {
        return amountPerPeriod;
    }

    public void setAmountPerPeriod(BigDecimal amountPerPeriod) {
        this.amountPerPeriod = amountPerPeriod;
    }

    public boolean isNeedOwner() {
        return needOwner;
    }

    public void setNeedOwner(boolean needOwner) {
        this.needOwner = needOwner;
    }

    public boolean isNeedIdNo() {
        return needIdNo;
    }

    public void setNeedIdNo(boolean needIdNo) {
        this.needIdNo = needIdNo;
    }

    public boolean isNeedAvlidDate() {
        return needAvlidDate;
    }

    public void setNeedAvlidDate(boolean needAvlidDate) {
        this.needAvlidDate = needAvlidDate;
    }

    public boolean isNeedCvv() {
        return needCvv;
    }

    public void setNeedCvv(boolean needCvv) {
        this.needCvv = needCvv;
    }

    public boolean isNeedPhoneNo() {
        return needPhoneNo;
    }

    public void setNeedPhoneNo(boolean needPhoneNo) {
        this.needPhoneNo = needPhoneNo;
    }

    public String getPreRouteId() {
        return preRouteId;
    }

    public void setPreRouteId(String preRouteId) {
        this.preRouteId = preRouteId;
    }

    public boolean isNeedBankPWD() {
        return needBankPWD;
    }

    public void setNeedBankPWD(boolean needBankPWD) {
        this.needBankPWD = needBankPWD;
    }

    public String getNeedSupply() {
        return needSupply;
    }

    public void setNeedSupply(String needSupply) {
        this.needSupply = needSupply;
    }

    @Override
    public String toString() {
        return "GuaranteeInstallmentPrePayResponseDTO{" +
                super.toString()+
                ", requestId=" + requestId +
                ", bankCode='" + bankCode + '\'' +
                ", period=" + period +
                ", orderAmount=" + orderAmount +
                ", serviceChargeRate=" + serviceChargeRate +
                ", serviceCharge=" + serviceCharge +
                ", amountPerPeriod=" + amountPerPeriod +
                ", needSupply=" + needSupply +
                ", needOwner=" + needOwner +
                ", needIdNo=" + needIdNo +
                ", needAvlidDate=" + needAvlidDate +
                ", needCvv=" + needCvv +
                ", needPhoneNo=" + needPhoneNo +
                ", needBankPWD=" + needBankPWD +
                ", preRouteId=" + preRouteId +
                '}';
    }

}
