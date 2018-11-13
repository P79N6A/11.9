package com.yeepay.g3.facade.frontend.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FanRouteAddRequestDTO implements Serializable {
    private static final long serialVersionUID = 7096770343372075696L;
    //代理商编号
    private String agentNum;
    //商户编号
    @NotNull(message = "customerNum不能为空")
    private String customerNum;
    //虚拟商户号(子商户号)
    @NotNull(message = "subCustomerNum不能为空")
    private String subCustomerNum;
    /*和subCustomerNum 一一对应的关系 */
    @NotNull(message = "reportId不能为空")
    private String reportId;
    public String getCustomerNum() {
        return customerNum;
    }
    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    public String getSubCustomerNum() {
        return subCustomerNum;
    }

    public void setSubCustomerNum(String subCustomerNum) {
        this.subCustomerNum = subCustomerNum;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }
}
