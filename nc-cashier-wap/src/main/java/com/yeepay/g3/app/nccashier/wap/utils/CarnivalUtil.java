package com.yeepay.g3.app.nccashier.wap.utils;

import com.yeepay.g3.facade.nccashier.enumtype.TradeSysCodeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yp-pos-m-7118 on 17/10/24.
 */
public class CarnivalUtil {

    public final static Map<String,String> systemMap = new HashMap<String, String>(){{
        put(TradeSysCodeEnum.DS.toString(),"ds");
        put(TradeSysCodeEnum.G2NET.toString(),"g2");
        put(TradeSysCodeEnum.NCTRADE.toString(),"yj");
    }};




    public final static List<String> subBizCatCodeBlackList = new ArrayList<String>() {{
        add("040001");
        add("040002");
        add("040003");
        add("103001");
        add("103002");
        add("103003");
        add("103004");
        add("104001");
        add("104002");
        add("106001");
        add("106002");
        add("106003");
        add("106004");
        add("106005");
        add("106006");
        add("106007");
        add("106008");
        add("106009");
        add("106010");
        add("106011");
        add("106012");
        add("106013");
        add("106014");
        add("106015");
        add("053001");
        add("053002");
        add("053003");
        add("108002");
        add("109011");
        add("110010");
        add("039002");
        add("036001");
        add("036002");
        add("036003");
        add("036004");
        add("036005");
        add("036006");
        add("036007");
    }};

}
