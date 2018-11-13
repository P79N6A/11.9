package com.yeepay.g3.facade.payprocessor.dto;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class PayRecordQueryRequestDTO implements Serializable {

    @NotBlank(message = "recordNo不能为空")
    private String recordNo;

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    @Override
    public String toString() {
        return "PayRecordQueryRequestDTO{" +
                "recordNo='" + recordNo + '\'' +
                '}';
    }
}
