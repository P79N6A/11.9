/**
 * 
 */
package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.facade.frontend.enumtype.RefundStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.RefundType;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 差错退款返回参数
 */
public class FrontendRefundResponseDTO extends BasicResponseDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 退款状态
     */
    private RefundStatusEnum refundStatus;

    /**
     * 退款类型
     */
    private RefundType refundType;

    public RefundStatusEnum getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatusEnum refundStatus) {
        this.refundStatus = refundStatus;
    }

    public RefundType getRefundType() {
        return refundType;
    }

    public void setRefundType(RefundType refundType) {
        this.refundType = refundType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
