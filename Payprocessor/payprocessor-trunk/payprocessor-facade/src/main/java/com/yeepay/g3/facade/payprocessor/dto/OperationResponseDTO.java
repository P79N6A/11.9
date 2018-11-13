package com.yeepay.g3.facade.payprocessor.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chronos.
 * @createDate 2016/11/24.
 */
public class OperationResponseDTO extends ResponseStatusDTO {

    /**
     * 成功数
     */
    private int success = 0;
    /**
     * 忽略数
     */
    private int ignore = 0;
    /**
     * 错误数
     */
    private List<String> errorList = new ArrayList<String>();

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getIgnore() {
        return ignore;
    }

    public void setIgnore(int ignore) {
        this.ignore = ignore;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
