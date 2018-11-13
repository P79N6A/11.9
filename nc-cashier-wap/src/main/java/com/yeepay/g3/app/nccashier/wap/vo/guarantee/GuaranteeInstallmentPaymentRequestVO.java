package com.yeepay.g3.app.nccashier.wap.vo.guarantee;

import com.yeepay.g3.app.nccashier.wap.utils.DataUtil;
import com.yeepay.g3.app.nccashier.wap.vo.CardInfoVO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 担保分期-支付请求-入参vo
 */
public class GuaranteeInstallmentPaymentRequestVO extends CardInfoVO implements Serializable {
    private static final long serialVersionUID = -3727346339939267419L;

    String token;

    /** 银行编码 */
    String bankCode;

    /** 期数 */
    String period;

    /** 预路由ID */
    String preRouteId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPreRouteId() {
        return preRouteId;
    }

    public void setPreRouteId(String preRouteId) {
        this.preRouteId = preRouteId;
    }

    @Override
    public String toString() {
        return "GuaranteeInstallmentPaymentRequestVO{" +
                "{" + super.toString() + '}' +
                "token='" + token + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", period='" + period + '\'' +
                ", preRouteId='" + preRouteId + '\'' +
                '}';
    }

    @Override
    public void validate() {
        if(StringUtils.isEmpty(getToken()) || StringUtils.isEmpty(getBankCode()) ||
                StringUtils.isEmpty(getPeriod()) || StringUtils.isEmpty(getPreRouteId())){
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
        }
        super.validate();
    }
}
