/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

/**
 * 类名称: APICflEasyBankInfoResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/5 下午6:27
 * @version: 1.0.0
 */

public class APICflEasyBankInfoResponseDTO extends APIBasicResponseDTO {

    private static final long serialVersionUID = 4337963703419788976L;

    private List<CflEasyBankInfo> cflEasyBankInfoList;

    public APICflEasyBankInfoResponseDTO() {
    }

    public List<CflEasyBankInfo> getCflEasyBankInfoList() {
        return cflEasyBankInfoList;
    }

    public void setCflEasyBankInfoList(List<CflEasyBankInfo> cflEasyBankInfoList) {
        this.cflEasyBankInfoList = cflEasyBankInfoList;
    }

    @Override
    public String toString() {
        return "APICflEasyBankInfoResponseDTO{" +
                "cflEasyBankInfoList=" + cflEasyBankInfoList +
                '}';
    }
}