package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;

import java.io.Serializable;

/**
 * API收银台一键支付，首次支付，请求下单入参
 * Created by ruiyang.du on 2018/3/6.
 */
public class APIYJZFFirstPaymentRequestDTO extends APIBasicRequestDTO implements Serializable{

    private static final long serialVersionUID = 2791583524341132272L;

    /** 用户标识号 */
    private String userNo;

    /** 用户标识类型 */
    private String userType;

    /** 用户IP */
    private String userIp;

    /** 是否校验产品开通，不通过YOP暴露 */
    private boolean checkProductOpen = true;

    /** 支付场景，当checkProductOpen为false时此项必填 */
    private String payScene;
    /** 商品类别码，当checkProductOpen为false时此项必填 */
    private String mcc;

    /*卡信息如下 */
    /** 卡号 */
    private String cardNo;

    /** 持卡人姓名 */
    private String owner;

    /** 身份证号 */
    private String idNo;

    /** 银行预留手机号 */
    private String phoneNo;

    /** CVV */
    private String cvv;

    /** 有效期 */
    private String avlidDate;

    /** 扩展信息，格式为Map<'String,String>序列化为json字符串 */
    private String paymentExt;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public boolean isCheckProductOpen() {
        return checkProductOpen;
    }

    public void setCheckProductOpen(boolean checkProductOpen) {
        this.checkProductOpen = checkProductOpen;
    }

    public String getPayScene() {
        return payScene;
    }

    public void setPayScene(String payScene) {
        this.payScene = payScene;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
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

    public String getPaymentExt() {
        return paymentExt;
    }

    public void setPaymentExt(String paymentExt) {
        this.paymentExt = paymentExt;
    }

    @Override
    public String toString() {
        return "APIYJZFFirstPaymentRequestDTO{" +
                "'" + super.toString() + '\'' +
                ", cardNo='" + HiddenCode.hiddenBankCardNO(cardNo) + '\'' +
                ", owner='" + HiddenCode.hiddenName(owner) + '\'' +
                ", idNo='" + HiddenCode.hiddenIdentityCode(idNo) + '\'' +
                ", phoneNo='" + HiddenCode.hiddenMobile(phoneNo) + '\'' +
                ", cvv='" + HiddenCode.hiddenCvv(cvv) + '\'' +
                ", avlidDate='" + HiddenCode.hiddenAbliddate(avlidDate) + '\'' +
                ", userNo='" + HiddenCode.hiddenIdentityId(userNo) + '\'' +
                ", userType='" + userType + '\'' +
                ", userIp='" + userIp + '\'' +
                ", checkProductOpen='" + checkProductOpen + '\'' +
                ", payScene='" + payScene + '\'' +
                ", mcc='" + mcc + '\'' +
                ", paymentExt='" + paymentExt + '\'' +
                '}';
    }

    @Override
    public void validate(){
        super.validate();
        if (StringUtils.isBlank(getToken())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",token不能为空");
        }
        if (StringUtils.isBlank(userIp)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userIp不能为空");
        }
        //卡四项为必填，cvv、有效期为信用卡必填
        if (StringUtils.isBlank(cardNo)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",cardNo不能为空");
        }
        if (StringUtils.isBlank(owner)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",owner不能为空");
        }
        if (StringUtils.isBlank(idNo)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",idNo不能为空");
        }
        if (StringUtils.isBlank(phoneNo)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",phoneNo不能为空");
        }
        if(StringUtils.isNotBlank(userType) && !IdentityType.getIdentityTypeMap().containsKey(userType)){
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userType错误");
        }
    }

    /**
     * 将入参中的卡信息六项，转换成CardInfoDTO返回
     * @return
     */
    public CardInfoDTO getCardInfoDTO(){
        CardInfoDTO cardInfoDTO = new CardInfoDTO();
        cardInfoDTO.setCardno(getCardNo());
        cardInfoDTO.setIdno(getIdNo());
        cardInfoDTO.setName(getOwner());
        cardInfoDTO.setPhone(getPhoneNo());
        cardInfoDTO.setCvv2(getCvv());
        cardInfoDTO.setValid(getAvlidDate());
        return cardInfoDTO;
    }
}
