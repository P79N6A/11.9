/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * 类名称: PageRedirectDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/20 上午10:22
 * @version: 1.0.0
 */

public class PageRedirectDTO implements Serializable {

    private static final long serialVersionUID = -9207993135562553134L;

    /**
     * 跳页场景
     */
    private String redirectSceneType;


    /**
     * 跳转请求url
     */
    private String redirectUrl;

    /**
     * 跳转请求方法
     */
    private String method;

    /**
     * 请求的参数编码
     */
    private String encoding;

    /**
     * 跳转其他参数
     */
    private Map<String, Object> extMap;

    public String getRedirectSceneType() {
        return redirectSceneType;
    }

    public void setRedirectSceneType(String redirectSceneType) {
        this.redirectSceneType = redirectSceneType;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
    }

    @Override
    public String toString() {
        return "PageRedirectDTO{" +
                "redirectSceneType='" + redirectSceneType + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                ", method='" + method + '\'' +
                ", encoding='" + encoding + '\'' +
                ", extMap=" + extMap +
                '}';
    }
}