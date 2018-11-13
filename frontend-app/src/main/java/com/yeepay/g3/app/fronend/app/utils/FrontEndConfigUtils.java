package com.yeepay.g3.app.fronend.app.utils;

import com.yeepay.g3.app.fronend.app.enumtype.PlatformType;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 16/8/11.
 */
public class FrontEndConfigUtils {
    private static final String SYS_PREFIX = "FRONT_END_";
    private static final String SPLIT_EQUAL = "=";
    private static final String SPLIT_SEMICOLONS = ";";
    public static final String FE_BANK_COST_USER = "FE_BANK_COST_USER";
    public static final String ORDER_TABLE_LIST = "ORDER_TABLE_LIST";

    /**
     * 获取系统配置
     * @param key
     * @return
     */
    public static Object getSysConfig(String key){
        ConfigParam param = ConfigurationUtils.getSysConfigParam(SYS_PREFIX + key);
        if (param==null || param.getValue()==null)
            return null;
        return param.getValue();
    }

    /**
     * 获取键值对系统配置
     * key=value;
     * @param key
     * @return
     */
    public static Map getSysConfigMap(String key){
        String config = (String) getSysConfig(key);
        Map configMap = new HashMap();
        if (config==null)
            return configMap;
        String[] valueList = config.split(SPLIT_EQUAL);
        for (String keyValue : valueList){
            String[] con = keyValue.split(SPLIT_SEMICOLONS);
            if (con==null||con.length<2)
                continue;
            configMap.put(con[0],con[1]);
        }
        return configMap;
    }

    /**
     * 获取前端支付订单表
     * @return
     */
    public static Map<String,String> getTableMap(){
        Map<String,String> tableMap = (Map<String, String>) getSysConfig(ORDER_TABLE_LIST);
        if (tableMap == null || tableMap.size() < 1){
            tableMap = new HashedMap();
            tableMap.put(PlatformType.WECHAT.name(), "TBL_FRONTEND_PAY_ORDER");
            tableMap.put(PlatformType.ALIPAY.name(), "TBL_FRONTEND_PAY_ORDER_ZFB");
            tableMap.put(PlatformType.NET.name(), "TBL_FRONTEND_PAY_ORDER_NET");
            tableMap.put(PlatformType.MPAY.name(), "TBL_MPAY_ORDER");
            tableMap.put(PlatformType.OPEN_UPOP.name(), "TBL_FRONTEND_PAY_ORDER_UPOP");
        }
        return tableMap;
    }
}
