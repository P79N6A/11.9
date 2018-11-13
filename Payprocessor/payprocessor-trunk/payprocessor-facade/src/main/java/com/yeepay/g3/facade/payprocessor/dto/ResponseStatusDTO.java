package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;

import java.io.Serializable;

/**
 * 返回信息基类
 * 只包含错误码和错误信息
 * @author chronos.
 * @createDate 2016/11/23.
 */
public abstract class ResponseStatusDTO implements Serializable {

    private static final long serialVersionUID = 7311532413207705259L;
    /**
     * 错误码
     */
    protected String responseCode;

    /**
     * 错误码描述
     */
    protected String responseMsg;

    /**
     * 接口处理状态
     */
    protected ProcessStatus processStatus = ProcessStatus.SUCCESS;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }
}
