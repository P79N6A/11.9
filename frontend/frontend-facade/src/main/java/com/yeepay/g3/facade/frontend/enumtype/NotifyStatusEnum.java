/**
 * 
 */
package com.yeepay.g3.facade.frontend.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 回调结果枚举类
 * @author TML
 */
public enum NotifyStatusEnum {

	INIT("初始化"),

	SUCCESS("成功"),

	FAILURE("失败");

	private final String descriptioin;

	NotifyStatusEnum(String description) {
		this.descriptioin = description;
	}

	public String getDescriptioin() {
		return descriptioin;
	}
	
	
	/**
     * 转化枚举类型
     * @param ordertypeStr
     * @return
     */
    public static NotifyStatusEnum getNotifyStatusEnum(String notifyStatusStr){
    	NotifyStatusEnum notifyStatusEnum = null;
    	if(StringUtils.isNotBlank(notifyStatusStr)){
    		try{
    			notifyStatusEnum = NotifyStatusEnum.valueOf(notifyStatusStr);
    		}catch(Exception e){
    			return notifyStatusEnum;
    		}
    	}
    	return notifyStatusEnum;
    }
}
