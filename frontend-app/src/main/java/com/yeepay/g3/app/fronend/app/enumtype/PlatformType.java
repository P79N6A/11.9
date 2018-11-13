package com.yeepay.g3.app.fronend.app.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * Created by gengyu.bi on 2015/1/23.
 */
public enum PlatformType {
    /**
     * WX
     */
    WECHAT,
    /**
     * 支付宝
     */
    ALIPAY,
    /**
     * 网银
     */
    NET,
    /**
     * mpay
     */
    MPAY,
    /**
     * 分期支付
     */
    CFL,
    
    OPEN_UPOP;

    public static com.yeepay.g3.app.fronend.app.enumtype.PlatformType getPlatformType(String platformTypeStr){
        com.yeepay.g3.app.fronend.app.enumtype.PlatformType platformType = null;
        if(StringUtils.isNotBlank(platformTypeStr)){
            try{
                platformType = com.yeepay.g3.app.fronend.app.enumtype.PlatformType.valueOf(platformTypeStr);
            }catch(Exception e){
                return platformType;
            }
        }
        return platformType;
    }

    public static PlatformType[] fusionpayPlatforms(){
        PlatformType[] platformTypes = {WECHAT,ALIPAY,OPEN_UPOP};
        return platformTypes;
    }
}
