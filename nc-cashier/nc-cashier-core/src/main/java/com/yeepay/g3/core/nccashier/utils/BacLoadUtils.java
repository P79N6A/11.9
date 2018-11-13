package com.yeepay.g3.core.nccashier.utils;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-09-04 16:48
 **/

import com.yeepay.g3.facade.nccashier.dto.MerchantProductDTO;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-09-04 16:48
 **/


public class BacLoadUtils {

    /**
     * 充值收银台 业务方名称
     */
    public static final String BAC_LOAD_BIZ_SYS = "BAC_LOAD";

    /**
     * 营销产品标示
     */
    public static final String BAC_LOAD_SALE_CODE = "RECHARGE";


    public static final String BAC_LOAD_B2B_NET_PAY_TOOL = "B2B_NET";

    public static final String BAC_LOAD_B2C_NET_PAY_TOOL = "B2C_NET";

    public static final String BAC_LOAD_B2B_REMIT_PAY_TOOL = "B2B_REMIT";

    public static final String BAC_LOAD_B2C_REMIT_PAY_TOOL = "B2C_REMIT";

    public static final String BAC_LOAD_ACCOUNT_PAY_TOOL = "ACCOUNT";


    public static boolean isLoadSystem(String system){
        return BAC_LOAD_BIZ_SYS.equals(system);
    }
}
