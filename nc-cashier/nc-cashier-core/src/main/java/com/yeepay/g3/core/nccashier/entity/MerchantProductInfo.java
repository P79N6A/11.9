package com.yeepay.g3.core.nccashier.entity;/**
 * @program: nc-cashier-parent
 * @description: 配置中心 新配置信息
 * @author: jimin.zhou
 * @create: 2018-09-03 16:59
 **/

/**
 *
 * @description: 配置中心 新配置信息
 *
 * @author: jimin.zhou
 *
 * @create: 2018-09-03 16:59
 **/


public class MerchantProductInfo {

    /**
     * 用短名字方便数据库存储
     */
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
