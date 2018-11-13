package com.yeepay.g3.core.nccashier.entity;

import java.util.Date;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.entity
 *
 * @author pengfei.chen
 * @since 17/2/21 11:08
 */
public class UserRequestInfo {

    private String tokenId;

    private String merchantConfigInfo;

    private String urlParamInfo;

    private String cashierVersion;

    public String getTokenId() {
        return tokenId;
    }

    public Date createTime;

    public Date updateTime;

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getMerchantConfigInfo() {
        return merchantConfigInfo;
    }

    public void setMerchantConfigInfo(String merchantConfigInfo) {
        this.merchantConfigInfo = merchantConfigInfo;
    }

    public String getUrlParamInfo() {
        return urlParamInfo;
    }

    public void setUrlParamInfo(String urlParamInfo) {
        this.urlParamInfo = urlParamInfo;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCashierVersion() {
        return cashierVersion;
    }

    public void setCashierVersion(String cashierVersion) {
        this.cashierVersion = cashierVersion;
    }
}
