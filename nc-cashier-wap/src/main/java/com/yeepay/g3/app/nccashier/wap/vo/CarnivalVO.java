package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * Created by yp-pos-m-7118 on 17/10/24.
 */
public class CarnivalVO {

    /**
     * 是否显示嘉年华
     */
    private boolean showCarnival;

    /**
     * 嘉年华URL
     */
    private String carnivalUrl;

    public boolean isShowCarnival() {
        return showCarnival;
    }

    public void setShowCarnival(boolean showCarnival) {
        this.showCarnival = showCarnival;
    }

    public String getCarnivalUrl() {
        return carnivalUrl;
    }

    public void setCarnivalUrl(String carnivalUrl) {
        this.carnivalUrl = carnivalUrl;
    }
}
