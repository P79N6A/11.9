package com.yeepay.g3.core.nccashier.utils;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-09-04 10:37
 **/

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @description: 配置中心帮助类
 *
 * @author: jimin.zhou
 *
 * @create: 2018-09-04 10:37
 **/


public class ConfigCenterUtils {

    public static final String BAC_LOAD_BIZ_SYS = "BAC_LOAD";


    /**
     * 请求配置中心新接口 系统名称
     */
    public static final String NC_CASHIER_SYS_NAME = "NCCASHIER";


    public static boolean isLoadSystem(String system){
        return BAC_LOAD_BIZ_SYS.equals(system);
    }


    /**
     * 请求配置中心新接口 对应收银台的 UID
     */
    public static final String BAC_LOAD_MERCHANT_UID = "8703672e79c74600826555f2c04264bb";


    public static final Map<String,String> bizSysUidMap = new HashMap<String, String>();
    static {
        bizSysUidMap.put(NC_CASHIER_SYS_NAME,BAC_LOAD_MERCHANT_UID);
    }


    public static String getUidByBizSystem(String bizSys){
        return  bizSysUidMap.get(bizSys);
    }
}
