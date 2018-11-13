package com.yeepay.g3.facade.nccashier.dto.guarantee;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

import java.io.Serializable;

/**
 * 担保分期-支付下单-请求DTO
 */
public class GuaranteeInstallmentPaymentRequestDTO implements Serializable {
    private static final long serialVersionUID = -1971892505742652083L;
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
    /**
     * 卡号
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "cardNo为空")
    private String cardNo;
    /**
     * 补充项：持卡人姓名
     */
    private String owner;
    /**
     * 补充项：身份证号
     */
    private String idNo;
    /**
     * 补充项：手机号
     */
    private String phoneNo;
    /**
     * 补充项：有效期
     */
    private String avlidDate;
    /**
     * 补充项：cvv
     */
    private String cvv;
    /**
     * 补充项：取款密码
     */
    private String bankPWD;
    /**
     * 预路由Id
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "预路由Id为空")
    private String preRouteId;

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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAvlidDate() {
        return avlidDate;
    }

    public void setAvlidDate(String avlidDate) {
        this.avlidDate = avlidDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBankPWD() {
        return bankPWD;
    }

    public void setBankPWD(String bankPWD) {
        this.bankPWD = bankPWD;
    }

    public String getPreRouteId() {
        return preRouteId;
    }

    public void setPreRouteId(String preRouteId) {
        this.preRouteId = preRouteId;
    }

    @Override
    public String toString() {
        return "GuaranteeInstallmentPaymentRequestDTO{" +
                "requestId='" + requestId + '\'' +
                "token='" + token + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", period=" + period +
                ", cardNo='" + HiddenCode.hiddenBankCardNO(cardNo) + '\'' +
                ", owner='" + HiddenCode.hiddenName(owner) + '\'' +
                ", idNo='" + HiddenCode.hiddenIdentityCode(idNo) + '\'' +
                ", phoneNo='" + HiddenCode.hiddenMobile(phoneNo) + '\'' +
                ", avlidDate='" + HiddenCode.hiddenAbliddate(avlidDate) + '\'' +
                ", cvv='" + HiddenCode.hiddenCvv(cvv) + '\'' +
                ", bankPWD='" + HiddenCode.HiddenBankPwd(bankPWD) + '\'' +
                ", preRouteId=" + preRouteId +
                '}';
    }
}
