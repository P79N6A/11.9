package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * 成功页面显示活动页
 */
public class SuccessActivateVO {

    /**
     * 是否展示成功页活动
     */
    private boolean showSuccessActivate;

    /**
     * 活动URL
     */
    private String successActUrl;

    public boolean isShowSuccessActivate() {
        return showSuccessActivate;
    }

    public void setShowSuccessActivate(boolean showSuccessActivate) {
        this.showSuccessActivate = showSuccessActivate;
    }

    public String getSuccessActUrl() {
        return successActUrl;
    }

    public void setSuccessActUrl(String successActUrl) {
        this.successActUrl = successActUrl;
    }
}
