package com.yeepay.g3.core.nccashier.enumtype;

/**
 * API收银台，返回值resultType枚举
 * Created by ruiyang.du on 2017/7/26.
 */
public enum ApiResultTypeEnum {
	
    JSON("json","json格式"),
    URL("url","url格式");

    private String type;
    private String description;

    ApiResultTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
