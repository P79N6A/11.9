package com.yeepay.g3.app.fronend.app.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chronos.
 * @createDate 2016/12/27.
 */
public class OperationResponse {

    private Integer success = 0;
    private Integer ignore = 0;
    private List<String> errorList = new ArrayList<String>();

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getIgnore() {
        return ignore;
    }

    public void setIgnore(Integer ignore) {
        this.ignore = ignore;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}
