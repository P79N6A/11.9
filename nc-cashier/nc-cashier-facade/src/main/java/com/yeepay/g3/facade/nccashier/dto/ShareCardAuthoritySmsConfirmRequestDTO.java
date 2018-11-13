package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 17/1/4 11:36
 */
public class ShareCardAuthoritySmsConfirmRequestDTO implements Serializable {

    private Long requestId;

    private String validateCode;
    
    private String cusType;
    
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

	public String getCusType() {
		return cusType;
	}

	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
    
}
