package com.yeepay.g3.facade.nccashier.dto;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-11-02 16:17
 **/

import java.io.Serializable;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-11-02 16:17
 **/


public class PassBindIdDTO  extends BasicResponseDTO implements Serializable {

    private boolean isPassBindId;

    public boolean isPassBindId() {
        return isPassBindId;
    }

    public void setPassBindId(boolean passBindId) {
        isPassBindId = passBindId;
    }
}
