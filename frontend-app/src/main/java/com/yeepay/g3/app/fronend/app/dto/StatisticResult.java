package com.yeepay.g3.app.fronend.app.dto;

import com.yeepay.g3.app.fronend.app.utils.FrontendMathUtils;

import java.math.BigDecimal;

/**
 * @author chronos.
 * @createDate 2016/10/20.
 */
public class StatisticResult {

    private String customerNumber;
    private BigDecimal totalCost;
    private BigDecimal totalAmount;
    private Long totalCount;
    private BigDecimal lastTotalCost;
    private BigDecimal lastTotalAmount;
    private Long lastTotalCount;
    private String chain;//环比
    private String isUp;

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getChain() {
        if (lastTotalAmount == null || lastTotalAmount.compareTo(new BigDecimal("0")) < 1){
            return null;
        }
        if (totalAmount == null){
            totalAmount = new BigDecimal("0");
        }
        chain = FrontendMathUtils.divide(totalAmount.subtract(lastTotalAmount), lastTotalAmount);
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public BigDecimal getLastTotalCost() {
        return lastTotalCost;
    }

    public void setLastTotalCost(BigDecimal lastTotalCost) {
        this.lastTotalCost = lastTotalCost;
    }

    public BigDecimal getLastTotalAmount() {
        return lastTotalAmount;
    }

    public void setLastTotalAmount(BigDecimal lastTotalAmount) {
        this.lastTotalAmount = lastTotalAmount;
    }

    public String getIsUp() {
        BigDecimal tmp1 = totalAmount == null ? new BigDecimal("0") : totalAmount;
        BigDecimal tmp2 = lastTotalAmount == null ? new BigDecimal("0") : lastTotalAmount;
        int result = tmp1.compareTo(tmp2);
        switch (result){
            case 1 : return "up";
            case 0 : return "eq";
            case -1 : return "dw";
            default:return null;
        }
    }

    public void setIsUp(String isUp) {
        this.isUp = isUp;
    }

    public Long getLastTotalCount() {
        return lastTotalCount;
    }

    public void setLastTotalCount(Long lastTotalCount) {
        this.lastTotalCount = lastTotalCount;
    }
}
