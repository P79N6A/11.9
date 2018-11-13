/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 类名称: APICflEasyBindRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/4 下午2:02
 * @version: 1.0.0
 */

public class APICflEasyBindRequestDTO extends APIBasicRequestDTO {

    private static final long serialVersionUID = 1388230015985646639L;

    /**
     * 绑卡ID：绑卡下单时必填
     */
    private String bindId;

    /** 用户标识号 */
    private String userNo;

    /** 用户标识类型 */
    private String userType;

    /** 用户IP */
    private String userIp;

    /** 分期期数 */
    private String period;

    public APICflEasyBindRequestDTO() {
    }

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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "APICflEasyBindRequestDTO{" +
                "bindId='" + bindId + '\'' +
                ", userNo='" + HiddenCode.hiddenIdentityId(userNo) + '\'' +
                ", userType='" + userType + '\'' +
                ", userIp='" + userIp + '\'' +
                ", period='" + period + '\'' +
                ", merchantNo='" + getMerchantNo() + '\'' +
                ", token='" + getToken() + '\'' +
                ", bizType='" + getBizType() + '\'' +
                ", version='" + getVersion() + '\'' +
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
        if (StringUtils.isBlank(period)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",period不能为空");
        }
    }
}