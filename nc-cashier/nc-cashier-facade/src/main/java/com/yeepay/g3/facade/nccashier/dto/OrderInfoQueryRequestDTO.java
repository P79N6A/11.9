package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 17/4/7 14:39
 */
public class OrderInfoQueryRequestDTO implements Serializable {


    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
