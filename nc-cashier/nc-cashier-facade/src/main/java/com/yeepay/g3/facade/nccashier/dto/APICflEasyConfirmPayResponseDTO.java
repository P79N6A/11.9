/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.nccashier.dto;

/**
 * 类名称: APICflEasyConfirmPayResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/4 下午2:15
 * @version: 1.0.0
 */

public class APICflEasyConfirmPayResponseDTO extends APIBasicResponseDTO {

    private static final long serialVersionUID = -1417055202480963945L;

    /** 绑卡id，首次支付支付成功且绑卡，则返回 */
    private String bindId;

    public APICflEasyConfirmPayResponseDTO() {
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    @Override
    public String toString() {
        return "APIYJZFConfirmPayResponseDTO{" +
                "'" + super.toString() + '\'' +
                ", bindId='" + bindId + '\'' +
                '}';
    }
}