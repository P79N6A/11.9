package com.yeepay.g3.facade.frontend.enumtype;


import com.yeepay.g3.utils.common.StringUtils;

/**
 * 
 */
public enum OrderType {
    /**
     * 被扫支付
     */
    PASSIVESCAN(0),
    /**
     * 主扫支付
     */
    ACTIVESCAN(1),
    /**
	 * 微信公众号支付
	 */
    JSAPI(2),
    /**
     * h5支付
     */
    H5APP(3),
    /**
     * APP支付
     */
    APP(4),
    /**
     * 支付宝主扫支付
     */
    @Deprecated
    ALIPAYSCAN(5),
    
    /**
     * SDK支付
     */
    SDK(6),
    /**
     * 支付宝生活号
     */
    LN(7),

    /**
     * 微信小程序
     */
    MINI_PROGRAM(8);

    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    OrderType(int num) {
        this.num = num;
    }

    /**
     * 转化枚举类型
     * @param ordertypeStr
     * @return
     */
    public static OrderType getOrderType(String ordertypeStr){
    	OrderType orderType = null;
    	if(StringUtils.isNotBlank(ordertypeStr)){
    		try{
    			orderType = OrderType.valueOf(ordertypeStr);
    		}catch(Exception e){
    			return orderType;
    		}
    	}
    	return orderType;
    }
    
}
