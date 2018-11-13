package com.yeepay.g3.core.nccashier.vo.responseDto;


import java.io.Serializable;

/**
 * Created by jimin.zhou on 18/1/3.
 */
public abstract class ResponseStatusDTO implements Serializable {
   
	private static final long serialVersionUID = 1L;
	
	protected String responseCode;
    protected String responseMsg;

    public ResponseStatusDTO() {
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return this.responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
