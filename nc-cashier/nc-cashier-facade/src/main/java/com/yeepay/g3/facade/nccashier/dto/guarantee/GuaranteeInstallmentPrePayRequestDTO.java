package com.yeepay.g3.facade.nccashier.dto.guarantee;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

import java.io.Serializable;

/**
 * 担保分期-预支付-请求DTO
 */
public class GuaranteeInstallmentPrePayRequestDTO implements Serializable {
    private static final long serialVersionUID = -8121426905565280556L;
    /**
     * paymentRequestId
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "requestId为空")
    private long requestId;

    /**
     * token
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "token为空")
    private String token;
    /**
     * 银行编码
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "bankCode为空")
    private String bankCode;

    /**
     * 期数
     */
    @NumberValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "period为空或非数字")
    private int period;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "GuaranteeInstallmentPrePayRequestDTO{" +
                "requestId=" + requestId +
                "token=" + token +
                ", bankCode='" + bankCode + '\'' +
                ", period=" + period +
                '}';
    }
}
