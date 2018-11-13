package com.yeepay.g3.app.fronend.app.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chronos.
 * @createDate 2016/10/17.
 */
public class QueryParams implements Serializable {

    private String platformType;
    private String customerNumber;
    private Date orderDate;
    private String orderSystem;

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderSystem() {
        return orderSystem;
    }

    public void setOrderSystem(String orderSystem) {
        this.orderSystem = orderSystem;
    }
}
