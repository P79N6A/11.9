package com.yeepay.g3.facade.frontend.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * 运营功能基础DTO
 * 唯一确认一笔订单
 * @author chronos.
 * @createDate 2016/12/27.
 */
public class BasicOperationDTO implements Serializable {

    /**
     * 请求系统订单号
     */
    @NotEmpty(message = "requestId不能为空")
    private String requestId;

    /**
     * 请求系统
     */
    @NotEmpty(message = "requestSystem不能为空")
    private String requestSystem;

    private String platformType;

    public BasicOperationDTO() {
    }

    public BasicOperationDTO(String requestId, String requestSystem, String platformType) {
        this.requestId = requestId;
        this.requestSystem = requestSystem;
        this.platformType = platformType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestSystem() {
        return requestSystem;
    }

    public void setRequestSystem(String requestSystem) {
        this.requestSystem = requestSystem;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
