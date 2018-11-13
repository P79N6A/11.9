package com.yeepay.g3.facade.nccashier.dto.clfeasy;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-10-17 15:55
 **/

import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

import java.io.Serializable;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-17 15:55
 **/


public class CflEasySmsSendRequestDTO extends ClfEasyBaseRequestDTO implements Serializable {
  
	private static final long serialVersionUID = 1L;

	private String token;

    private Long requestId;

    private Long recordId;


    @Override
    public String toString() {
        return "CflEasySmsSendRequestDTO [requestId=" + requestId + ", recordId=" + recordId + "]";
    }

    public void validate() {
        if (requestId == null || requestId <= 0) {
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
        }
        if (recordId == null || recordId <= 0) {
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "recordId未传");
        }
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
}
