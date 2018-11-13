package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.Map;

public class PageRedirectDTO implements Serializable {

    private static final long serialVersionUID = 3804740744709251537L;
    private String redirectSceneType;
    private String redirectUrl;
    private String method;
    private String encoding;
    private Map<String, Object> extMap;

    public PageRedirectDTO() {
    }

    public String getRedirectSceneType() {
        return this.redirectSceneType;
    }

    public void setRedirectSceneType(String redirectSceneType) {
        this.redirectSceneType = redirectSceneType;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getExtMap() {
        return this.extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String toString() {
        return "PageRedirectDTO{redirectSceneType='" + this.redirectSceneType + '\'' + ", redirectUrl='" + this.redirectUrl + '\'' + ", method='" + this.method + '\'' + ", encoding='" + this.encoding + '\'' + ", extMap=" + this.extMap + '}';
    }
}
