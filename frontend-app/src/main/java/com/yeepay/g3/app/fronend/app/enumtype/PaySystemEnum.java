package com.yeepay.g3.app.fronend.app.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 支付系统
 * @author chronos.
 * @createDate 2016/12/27.
 */
public enum  PaySystemEnum {
    /**
     * FE
     */
    FE,
    /**
     * 分期
     */
    CFL,
    /**
     * 网银
     */
    NET;

    public static PaySystemEnum getPaySystem(String sys){
        PaySystemEnum paySystem = null;
        if(StringUtils.isNotBlank(sys)){
            try{
                if (PlatformType.WECHAT.name().equals(sys)
                        || PlatformType.ALIPAY.name().equals(sys)
                        || PlatformType.OPEN_UPOP.name().equals(sys)) {
                    return PaySystemEnum.FE;
                } else {
                    paySystem = PaySystemEnum.valueOf(sys);
                }
            }catch(Exception e){
                return paySystem;
            }
        }
        return paySystem;
    }
}
