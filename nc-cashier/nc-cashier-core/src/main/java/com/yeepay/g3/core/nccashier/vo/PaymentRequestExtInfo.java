package com.yeepay.g3.core.nccashier.vo;

/**
 * 构建paymentRequest时，需要从接口下单入参中传入的补充信息
 */
public class PaymentRequestExtInfo {
    /** 用户请求IP */
    private String userIp;
    /** appId，用于公众号 */
    private String appId;
    /** 绑卡id，用于一键支付的二次支付 */
    private String bindId;
    /** 支付场景，用于一键支付API的二次支付 */
    private String payScene;
    /** 商品类别吗，用于一键支付API的二次支付 */
    private String mcc;
    /** 用户信息 */
    private CashierUserInfo cashierUser;

    public PaymentRequestExtInfo() {
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getPayScene() {
        return payScene;
    }

    public void setPayScene(String payScene) {
        this.payScene = payScene;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public CashierUserInfo getCashierUser() {
        return cashierUser;
    }

    public void setCashierUser(CashierUserInfo cashierUser) {
        this.cashierUser = cashierUser;
    }
}
