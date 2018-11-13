package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * API收银台返回状态枚举
 * Created by ruiyang.du on 2017/7/17.
 */
public enum APICashierPayResultEnum {

    SUCCESS("CAS00000","调用成功"),
    SYSTEM_ERROR("CAS10000","系统内部异常");
    /* 其他错误状态，需在错误码管理系统，配置NCCASHIER错误码到NCCASHIER_API错误码的映射 */

    private String code;
    private String message;

    private APICashierPayResultEnum(String code,String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
