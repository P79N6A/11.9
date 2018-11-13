package com.yeepay.g3.app.fronend.app.dto;

import java.math.BigDecimal;

/**
 * @author chronos.
 * @createDate 2016/10/17.
 */
public class QueryResult {

    private String dim;
    private BigDecimal trxCount;
    private BigDecimal trxAmount;

    public String getDim() {
        return dim;
    }

    public void setDim(String dim) {
        this.dim = dim;
    }

    public BigDecimal getTrxCount() {
        return trxCount;
    }

    public void setTrxCount(BigDecimal trxCount) {
        this.trxCount = trxCount;
    }

    public BigDecimal getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(BigDecimal trxAmount) {
        this.trxAmount = trxAmount;
    }
}
