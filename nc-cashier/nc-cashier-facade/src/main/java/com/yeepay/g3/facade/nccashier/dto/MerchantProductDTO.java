package com.yeepay.g3.facade.nccashier.dto;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-09-04 14:22
 **/

import java.io.Serializable;

/**
 *
 * @description: 配置中心新接口查询配置信息DTO
 *
 * @author: jimin.zhou
 *
 * @create: 2018-09-04 14:22
 **/


public class MerchantProductDTO  implements Serializable {
    private static final long serialVersionUID = -5040206700297817254L;

    /**
     *
     */
    private String biz;
    /**
     * 基础产品码
     */
    private String base;
    /**
     * 营销产品码
     */
    private String mar;
    /**
     * 支付场景
     */
    private String sce;



    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getMar() {
        return mar;
    }

    public void setMar(String mar) {
        this.mar = mar;
    }

    public String getSce() {
        return sce;
    }

    public void setSce(String sce) {
        this.sce = sce;
    }
}
