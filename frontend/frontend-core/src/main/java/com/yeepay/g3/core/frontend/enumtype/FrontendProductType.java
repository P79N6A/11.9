package com.yeepay.g3.core.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * FE产品类型
 * @author chronos.
 * @createDate 2016/12/30.
 */
public enum  FrontendProductType {
    /**
     * 微信
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
     * 分期
     */
    CFL;

    public static FrontendProductType getProductType(String productType){
        FrontendProductType feType = null;
        if(StringUtils.isNotBlank(productType)){
            try{
                feType = FrontendProductType.valueOf(productType);
            }catch(Exception e){
                return feType;
            }
        }
        return feType;
    }
}
