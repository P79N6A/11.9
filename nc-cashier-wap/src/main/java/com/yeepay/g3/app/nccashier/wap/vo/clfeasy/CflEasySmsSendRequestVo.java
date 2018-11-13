package com.yeepay.g3.app.nccashier.wap.vo.clfeasy;/**
 * @program: nc-cashier-parent
 * @description: 分期易发送短验请求VO
 * @author: jimin.zhou
 * @create: 2018-10-17 14:49
 **/

import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;

/**
 *
 * @description: 分期易发送短验请求VO
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-17 14:49
 **/


public class CflEasySmsSendRequestVo extends ClfEasyBaseRequestVo{

    private String token;

    private Long requestId;

    private Long recordId;

    private ReqSmsSendTypeEnum smsType;

    public ReqSmsSendTypeEnum getSmsType() {
        return smsType;
    }

    public void setSmsType(ReqSmsSendTypeEnum smsType) {
        this.smsType = smsType;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
