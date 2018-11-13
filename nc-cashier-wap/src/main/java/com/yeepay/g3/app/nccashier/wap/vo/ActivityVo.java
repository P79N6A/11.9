package com.yeepay.g3.app.nccashier.wap.vo;

/**
 *  参与活动的数据结构
 * Created by jimin.zhou on 18/4/11.
 */
public class ActivityVo {

    /**
     * 是否参加/展示活动
     */
    private boolean showActivity;

    /**
     * 活动链接
     */
    private String activityUrl;

    /**
     * 活动资源
     */
    private String activityRec;

    /**
     * 银行信息
     */
    private BankInfoVO bankInfoVO;

    public boolean isShowActivity() {
        return showActivity;
    }

    public void setShowActivity(boolean showActivity) {
        this.showActivity = showActivity;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public String getActivityRec() {
        return activityRec;
    }

    public void setActivityRec(String activityRec) {
        this.activityRec = activityRec;
    }

    public BankInfoVO getBankInfoVO() {
        return bankInfoVO;
    }

    public void setBankInfoVO(BankInfoVO bankInfoVO) {
        this.bankInfoVO = bankInfoVO;
    }
}
