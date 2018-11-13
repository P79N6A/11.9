package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * Description银行限额列表
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 16/11/9 18:53
 */
public class BankLimiAmountRequestDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -9010397375743774173L;
	private String merchantNo;//商户编号
    private String requestId;//paymentRequest查询字段

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
