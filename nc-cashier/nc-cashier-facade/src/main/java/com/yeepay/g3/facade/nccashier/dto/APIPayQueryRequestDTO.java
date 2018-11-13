package com.yeepay.g3.facade.nccashier.dto;/**
 * @program: nc-cashier-parent
 * @description: 前置收银台支付结果查询请求入参DTO
 * @author: jimin.zhou
 * @create: 2018-07-17 16:56
 **/

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

import java.io.Serializable;

/**
 *
 * @description: 前置收银台支付结果查询请求入参DTO
 *
 * @author: jimin.zhou
 *
 * @create: 2018-07-17 16:56
 **/


public class APIPayQueryRequestDTO extends APIBasicRequestDTO implements Serializable{
    private static final long serialVersionUID = -7742312673077628481L;

    /**
     * 收银台recordId
     */
    private long recordId;


    @Override
    public String toString() {
        return "APIPayQueryRequestDTO [recordId=" + recordId
                + "]" + super.toString();
    }


    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
}
