package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;

import java.io.Serializable;

/**
 * API收银台一键支付，二次支付，请求下单入参
 * Created by ruiyang.du on 2018/3/6.
 */
public class APIYJZFBindPaymentRequestDTO extends APIBasicRequestDTO implements Serializable{

    private static final long serialVersionUID = 2143521726729433498L;

    /** 绑卡ID */
    private String bindId;
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
    /** 扩展信息，格式为Map<'String,String>序列化为json字符串 */
    private String paymentExt;

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

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

    public String getPaymentExt() {
        return paymentExt;
    }

    public void setPaymentExt(String paymentExt) {
        this.paymentExt = paymentExt;
    }

    @Override
    public String toString() {
        return "APIYJZFBindPaymentRequestDTO{" +
                "'" + super.toString() + '\'' +
                "bindId='" + bindId + '\'' +
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
        if (StringUtils.isBlank(userNo)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userNo不能为空");
        }
        if(StringUtils.isBlank(userType) ||
                (StringUtils.isNotBlank(userType) && !IdentityType.getIdentityTypeMap().containsKey(userType) && !MemberTypeEnum.YIBAO.name().equals(userType))){
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userType为空或错误");
        }
        if (StringUtils.isBlank(bindId)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",bindId不能为空");
        }
        if (StringUtils.isBlank(userIp)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userIp不能为空");
        }
    }
}
