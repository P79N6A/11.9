package com.yeepay.g3.core.payprocessor.util;

import com.yeepay.g3.facade.cwh.enumtype.BankCardType;
import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.payprocessor.enumtype.PayCardType;

/**
 * 转换类
 * @author chronos.
 * @createDate 2016/11/11.
 */
public class ConvertUtil {

    /**
     * 将FE的卡类型转换为支付处理器卡类型
     * @param type
     * @return
     */
    public static PayCardType convertPayCardType(PayBankcardType type){
        if (type == null)
            return null;
        try {
            return PayCardType.valueOf(type.name());
        } catch (Throwable th){
            return null;
        }
    }

    /**
     * 将卡账户卡类型转为支付处理器卡类型
     * @param type
     * @return
     */
    public static PayCardType convertPayCardType(BankCardType type){
        if (type == null)
            return null;
        if (BankCardType.CREDITCARD.equals(type))
            return PayCardType.CREDIT;
        if (BankCardType.DEBITCARD.equals(type))
            return PayCardType.DEBIT;
        return null;
    }
}
