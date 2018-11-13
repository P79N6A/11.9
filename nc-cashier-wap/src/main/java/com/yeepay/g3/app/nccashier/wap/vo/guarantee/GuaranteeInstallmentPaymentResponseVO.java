package com.yeepay.g3.app.nccashier.wap.vo.guarantee;

import com.yeepay.g3.app.nccashier.wap.vo.BasicResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.UrlInfoVO;

import java.io.Serializable;

/**
 * 担保分期-支付请求-返回信息
 */
public class GuaranteeInstallmentPaymentResponseVO extends BasicResponseVO implements Serializable {
    private static final long serialVersionUID = 8558219247238971080L;

    /**
     * url信息，当需要跳转到通联商务接口进行确认支付时返回
     */
    private UrlInfoVO urlInfo;

    public UrlInfoVO getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(UrlInfoVO urlInfo) {
        this.urlInfo = urlInfo;
    }

    @Override
    public String toString() {
        return "GuaranteeInstallmentPaymentResponseVO{" +
                "urlInfo=" + urlInfo +
                ", errorcode='" + errorcode + '\'' +
                ", errormsg='" + errormsg + '\'' +
                ", bizStatus='" + bizStatus + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
