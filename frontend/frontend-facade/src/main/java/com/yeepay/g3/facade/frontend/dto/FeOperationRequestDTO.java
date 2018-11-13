package com.yeepay.g3.facade.frontend.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author chronos.
 * @createDate 2016/12/23.
 */
public class FeOperationRequestDTO implements Serializable {

    @NotNull(message = "orderNos不能为空")
    private List<BasicOperationDTO> orderNos;

    public List<BasicOperationDTO> getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(List<BasicOperationDTO> orderNos) {
        this.orderNos = orderNos;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
