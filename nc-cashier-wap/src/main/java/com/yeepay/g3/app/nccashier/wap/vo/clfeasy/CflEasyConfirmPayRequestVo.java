package com.yeepay.g3.app.nccashier.wap.vo.clfeasy;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-10-17 15:00
 **/

/**
 *
 * @description: 分期易确认支付请求参数
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-17 15:00
 **/


public class CflEasyConfirmPayRequestVo extends ClfEasyBaseRequestVo {

    private String token;

    private String verifycode;

    private Long requestId;

    private Long recordId;


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

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }
}
