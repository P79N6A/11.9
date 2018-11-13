package com.yeepay.g3.facade.nccashier.dto.clfeasy;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-10-18 10:21
 **/

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
 * @create: 2018-10-18 10:21
 **/


public class CflEasyConfirmPayRequestDTO  extends ClfEasyBaseRequestDTO implements Serializable {
    private String token;

    private String verifycode;

    private Long requestId;

    private Long recordId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
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


    public void validate() {
        if (StringUtils.isBlank(verifycode)) {
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "verifyCode未传");
        }
        if (requestId==null || requestId <= 0) {
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
        }
        if (recordId==null || recordId <= 0) {
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "recordId未传");
        }
    }


    @Override
    public String toString() {
        return "CflEasyConfirmPayRequestDTO [requestId=" + requestId + ", recordId=" + recordId + ", verifyCode="
                + verifycode + "]";
    }
}
