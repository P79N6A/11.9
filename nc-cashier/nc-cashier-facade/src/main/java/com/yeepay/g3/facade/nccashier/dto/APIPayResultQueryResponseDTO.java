package com.yeepay.g3.facade.nccashier.dto;/**
 * @program: nc-cashier-parent
 * @description: 前置收银台支付结果查询返回实体
 * @author: jimin.zhou
 * @create: 2018-07-16 11:18
 **/

import java.io.Serializable;

/**
 *
 * @description: 前置收银台支付结果查询返回实体
 *
 * @author: jimin.zhou
 *
 * @create: 2018-07-16 11:18
 **/


public class APIPayResultQueryResponseDTO extends APIBasicResponseDTO implements Serializable {

    private static final long serialVersionUID = -4871627510228697828L;


    /**
     * 支付产品类型,对应payprocess的PayOrderType
     */
    private String payOrderType;


    /**
     * 支付记录状态
     */
    private String trxStatus;


    public void init(String payOrderType,String trxStatus){
        this.payOrderType = payOrderType;
        this.trxStatus = trxStatus;
    }


    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }

    public String getTrxStatus() {
        return trxStatus;
    }

    public void setTrxStatus(String trxStatus) {
        this.trxStatus = trxStatus;
    }
}
