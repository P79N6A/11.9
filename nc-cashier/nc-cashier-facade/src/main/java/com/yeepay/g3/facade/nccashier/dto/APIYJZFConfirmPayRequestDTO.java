package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;

import java.io.Serializable;

/**
 * API收银台一键支付，确认支付
 */
public class APIYJZFConfirmPayRequestDTO extends APIBasicRequestDTO implements Serializable {

    private static final long serialVersionUID = -6864850718642862364L;
    /** 支付记录号 */
    private String recordId;
    /** 验证码 */
    private String verifyCode;

    /** 补充项：持卡人姓名 */
    private String owner;
    /** 补充项：身份证号 */
    private String idNo;
    /** 补充项：银行预留手机号 */
    private String phoneNo;
    /** 补充项：CVV */
    private String cvv;
    /** 补充项：有效期 */
    private String avlidDate;
    /** 补充项：取款密码 */
    private String bankPWD;
    /** 扩展信息，格式为Map<'String,String>序列化为json字符串 */
    private String paymentExt;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
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

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getAvlidDate() {
        return avlidDate;
    }

    public void setAvlidDate(String avlidDate) {
        this.avlidDate = avlidDate;
    }

    public String getBankPWD() {
        return bankPWD;
    }

    public void setBankPWD(String bankPWD) {
        this.bankPWD = bankPWD;
    }

    public String getPaymentExt() {
        return paymentExt;
    }

    public void setPaymentExt(String paymentExt) {
        this.paymentExt = paymentExt;
    }

    @Override
    public String toString() {
        return "APIYJZFConfirmPayRequestDTO{" +
                "'" + super.toString() + '\'' +
                ", recordId='" + recordId + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", owner='" + HiddenCode.hiddenName(owner) + '\'' +
                ", idNo='" + HiddenCode.hiddenIdentityCode(idNo) + '\'' +
                ", phoneNo='" + HiddenCode.hiddenMobile(phoneNo) + '\'' +
                ", cvv='" + HiddenCode.hiddenCvv(cvv) + '\'' +
                ", avlidDate='" + HiddenCode.hiddenAbliddate(avlidDate) + '\'' +
                ", bankPWD='" + HiddenCode.HiddenBankPwd(bankPWD) + '\'' +
                ", paymentExt='" + paymentExt + '\'' +
                '}';
    }

    @Override
    public void validate(){
        super.validate();
        if (StringUtils.isBlank(getToken())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",token不能为空");
        }
        if (StringUtils.isBlank(recordId)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",recordId不能为空");
        }
    }

    /**
     * 将入参中的卡信息六项，转换成CardInfoDTO返回
     * @return
     */
    public CardInfoDTO getCardInfoDTO(){
        if(StringUtils.isBlank(getIdNo()) && StringUtils.isBlank(getOwner()) && StringUtils.isBlank(getPhoneNo()) &&
                StringUtils.isBlank(getCvv()) && StringUtils.isBlank(getAvlidDate()) && StringUtils.isBlank(getBankPWD())){
            return null;
        }
        CardInfoDTO cardInfoDTO = new CardInfoDTO();
        cardInfoDTO.setIdno(getIdNo());
        cardInfoDTO.setName(getOwner());
        cardInfoDTO.setPhone(getPhoneNo());
        cardInfoDTO.setCvv2(getCvv());
        cardInfoDTO.setValid(getAvlidDate());
        cardInfoDTO.setPass(getBankPWD());
        return cardInfoDTO;
    }

    /**
     * 清空入参中的卡信息，用于不需要提交补充项而调用方传入了补充项时
     */
    public void cleanCardInfo(){
        setIdNo(null);
        setOwner(null);
        setPhoneNo(null);
        setCvv(null);
        setAvlidDate(null);
        setBankPWD(null);
    }
}
