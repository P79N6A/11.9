package com.yeepay.g3.app.nccashier.wap.vo.guarantee;

import com.yeepay.g3.app.nccashier.wap.vo.BasicResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardItemNecessary;

import java.io.Serializable;

/**
 * 担保分期-预下单-返回信息
 */
public class GuaranteeInstallmentPrePayResponseVO extends BasicResponseVO implements Serializable {
    private static final long serialVersionUID = -8936378763881514671L;

    /**
     * 银行编码
     */
    private String bankCode;
    /**
     * 期数
     */
    private String period;
    /**
     * 订单金额
     */
    private String orderAmount;
    /**
     * 手续费率
     */
    private String serviceChargeRate;
    /**
     * 手续费
     */
    private String serviceCharge;
    /**
     * 每期金额
     */
    private String amountPerPeriod;
    /**
     * 需补充卡信息，Y=有需补充项；N=全部无需补充
     */
    private String needSupply;
    /**
     * 补充项
     */
    private CardItemNecessary cardItemNecessary;
    /**
     * 预路由id
     */
    private String preRouteId;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getServiceChargeRate() {
        return serviceChargeRate;
    }

    public void setServiceChargeRate(String serviceChargeRate) {
        this.serviceChargeRate = serviceChargeRate;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getAmountPerPeriod() {
        return amountPerPeriod;
    }

    public void setAmountPerPeriod(String amountPerPeriod) {
        this.amountPerPeriod = amountPerPeriod;
    }

    public CardItemNecessary getCardItemNecessary() {
        return cardItemNecessary;
    }

    public void setCardItemNecessary(CardItemNecessary cardItemNecessary) {
        this.cardItemNecessary = cardItemNecessary;
    }

    public String getPreRouteId() {
        return preRouteId;
    }

    public void setPreRouteId(String preRouteId) {
        this.preRouteId = preRouteId;
    }

    public String getNeedSupply() {
        return needSupply;
    }

    public void setNeedSupply(String needSupply) {
        this.needSupply = needSupply;
    }

    @Override
    public String toString() {
        return "GuaranteeInstallmentPrePayResponseVO{" +
                "bankCode='" + bankCode + '\'' +
                ", period='" + period + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", serviceChargeRate='" + serviceChargeRate + '\'' +
                ", serviceCharge='" + serviceCharge + '\'' +
                ", amountPerPeriod='" + amountPerPeriod + '\'' +
                ", cardItemNecessary=" + cardItemNecessary +
                ", errorcode='" + errorcode + '\'' +
                ", errormsg='" + errormsg + '\'' +
                ", bizStatus='" + bizStatus + '\'' +
                ", token='" + token + '\'' +
                ", preRouteId='" + preRouteId + '\'' +
                ", needSupply='" + needSupply + '\'' +
                '}';
    }

}
