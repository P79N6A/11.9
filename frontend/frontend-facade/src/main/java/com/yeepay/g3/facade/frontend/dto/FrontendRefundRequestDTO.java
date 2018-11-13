/**
 * 
 */
package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.enumtype.RefundType;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

/**
 *
 */
public class FrontendRefundRequestDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 商户编号
     */
    @NotNull(message = "商户编号不能为空")
    private String customerNumber;

    /**
     * 退款类型
     */
    @NotNull(message = "退款类型不能为空")
    private RefundType refundType;
    
    /**
     * 支付平台类型
     */
    @NotNull(message = "platformType不能为空")
    private PlatformType platformType;
    
    /**
	 * 业务方
	 */
	@NotNull(message = "requestSystem不能为空")
	protected String requestSystem;

	/**
	 * 业务方订单号
	 */
	@NotNull(message = "requestId不能为空")
    protected String requestId;
	
    
	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public RefundType getRefundType() {
        return refundType;
    }

    public void setRefundType(RefundType refundType) {
        this.refundType = refundType;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
