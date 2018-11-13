package com.yeepay.g3.core.frontend.entity;

import com.yeepay.g3.utils.persistence.Entity;

import java.util.Date;

/**
 * 粉丝路由
 */
public class FanRoute implements Entity<Long> {
    private static final long serialVersionUID = -5371156161535973011L;
    private Long id;
    private String agentNum;
    private String customerNum;

    private String subCustomerNum;

    private String reportId;

    private Date createTime;

    private Date modifyTime;

    private Integer enabled;

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum == null ? null : customerNum.trim();
    }

    public String getSubCustomerNum() {
        return subCustomerNum;
    }

    public void setSubCustomerNum(String subCustomerNum) {
        this.subCustomerNum = subCustomerNum == null ? null : subCustomerNum.trim();
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId == null ? null : reportId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long aLong) {
        this.id=aLong;
    }

    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }
}