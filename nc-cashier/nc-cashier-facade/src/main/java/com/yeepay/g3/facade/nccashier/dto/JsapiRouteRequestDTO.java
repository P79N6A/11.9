package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 16/12/12 17:27
 */
public class JsapiRouteRequestDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    private String  requestId;
    
    /**
     * 支付方式
     */
    private String payType;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
    
}
