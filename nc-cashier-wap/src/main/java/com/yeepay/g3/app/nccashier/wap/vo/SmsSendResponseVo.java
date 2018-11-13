package com.yeepay.g3.app.nccashier.wap.vo;/**
 * @program: nc-cashier-parent
 * @description: 发短验返回参数
 * @author: jimin.zhou
 * @create: 2018-10-22 17:37
 **/

import com.yeepay.g3.app.nccashier.wap.vo.BasicResponseVO;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;

/**
 *
 * @description: 发短验返回参数
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-22 17:37
 **/


public class SmsSendResponseVo extends ResponseVO {

    private ReqSmsSendTypeEnum smsType;

    private String token;

    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public ReqSmsSendTypeEnum getSmsType() {
        return smsType;
    }

    public void setSmsType(ReqSmsSendTypeEnum smsType) {
        this.smsType = smsType;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
