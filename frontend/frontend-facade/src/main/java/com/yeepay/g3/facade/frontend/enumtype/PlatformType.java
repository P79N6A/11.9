package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * Created by gengyu.bi on 2015/1/23.
 */
public enum PlatformType {

    /**
     * 微信
     */
    WECHAT(0),
    /**
     * 支付宝
     */
    ALIPAY(1),
    
    /**
     * 银联钱包
     */
    OPEN_UPOP(2),
    
    /**
     * 京东钱包
     */
    JD(3),
    /**
     * QQ钱包
     */
    QQ(4);

    private int num;

    PlatformType(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public static PlatformType getPlatformType(String platformTypeStr){
    	PlatformType platformType = null;
    	if(StringUtils.isNotBlank(platformTypeStr)){
    		try{
    			platformType = PlatformType.valueOf(platformTypeStr);
    		}catch(Exception e){
    			return platformType;
    		}
    	}
    	return platformType;
    }
    
    
}
