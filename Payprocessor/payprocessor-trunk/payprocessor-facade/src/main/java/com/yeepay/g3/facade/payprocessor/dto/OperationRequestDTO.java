package com.yeepay.g3.facade.payprocessor.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author chronos.
 * @createDate 2016/11/24.
 */
public class OperationRequestDTO implements Serializable {

    @NotNull(message = "recordList不能为空")
    private List<String> recordList;

    public List<String> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<String> recordList) {
        this.recordList = recordList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
