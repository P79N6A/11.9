package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class ValidateSamePersionLimitDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户标识号 */
    private String userNo;

    /** 用户标识类型 */
    private String userType;

    /** 持卡人姓名 */
    private String owner;

    /** 身份证号 */
    private String idNo;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
}
