package com.yeepay.g3.app.nccashier.wap.vo.guarantee;

import com.yeepay.g3.app.nccashier.wap.vo.BasicResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.UrlInfoVO;

import java.io.Serializable;

/**
 * 担保分期-卡号校验-返回信息
 */
public class GuaranteeInstallmentCardNoCheckResponseVO extends BasicResponseVO implements Serializable {
    private static final long serialVersionUID = 8558219247238971080L;

    /**
     * 卡类型（中文），当卡号存在时返回
     */
    private String cardType;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "GuaranteeInstallmentCardNoCheckResponseVO{" +
                "cardType='" + cardType + '\'' +
                ", errorcode='" + errorcode + '\'' +
                ", errormsg='" + errormsg + '\'' +
                ", bizStatus='" + bizStatus + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
