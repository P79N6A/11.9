/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.nccashier.utils;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.facade.nccashier.dto.CflEasyBankInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称: OthersTest <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/6 上午11:44
 * @version: 1.0.0
 */

public class OthersTest {

    public static void main(String[] args) {
        CflEasyBankInfo cflEasyBankInfo1 = new CflEasyBankInfo("CCB", "建设银行", "3,6,12", "600,20000");
        CflEasyBankInfo cflEasyBankInfo2 = new CflEasyBankInfo("BOC", "中国银行", "3,6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo3 = new CflEasyBankInfo("ABC", "农业银行", "3,6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo4 = new CflEasyBankInfo("CITIC", "中信银行", "3,6,12", "600,20000");
        CflEasyBankInfo cflEasyBankInfo5 = new CflEasyBankInfo("GDB", "广发银行", "6,12", "600,20000");
        CflEasyBankInfo cflEasyBankInfo6 = new CflEasyBankInfo("SPDB", "浦发银行", "3,6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo7 = new CflEasyBankInfo("CEB", "光大银行", "3,6,12", "600,20000");
        CflEasyBankInfo cflEasyBankInfo8 = new CflEasyBankInfo("CMBC", "民生银行", "3,6,12", "600,20000");
        CflEasyBankInfo cflEasyBankInfo9 = new CflEasyBankInfo("HXB", "华夏银行", "6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo10 = new CflEasyBankInfo("CIB", "兴业银行", "3,6,12", "600,20000");
        CflEasyBankInfo cflEasyBankInfo11 = new CflEasyBankInfo("BOB", "北京银行", "3,6,12", "600,20000");
        CflEasyBankInfo cflEasyBankInfo12 = new CflEasyBankInfo("BOS", "上海银行", "3,6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo13 = new CflEasyBankInfo("JSBK", "江苏银行", "3,6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo14 = new CflEasyBankInfo("NBCB", "宁波银行", "3,6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo15 = new CflEasyBankInfo("BJRCB", "北京农商", "3,6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo16 = new CflEasyBankInfo("GRCB", "广州农商", "3,6,12", "600,20000");
        CflEasyBankInfo cflEasyBankInfo17 = new CflEasyBankInfo("JJCCB", "九江银行", "3,6,12,24", "600,20000");
        CflEasyBankInfo cflEasyBankInfo18 = new CflEasyBankInfo("ICBC", "工商银行", "3,6,12", "600,20000");

        List<CflEasyBankInfo> list = new ArrayList<CflEasyBankInfo>();
        list.add(cflEasyBankInfo1);
        list.add(cflEasyBankInfo2);
        list.add(cflEasyBankInfo3);
        list.add(cflEasyBankInfo4);
        list.add(cflEasyBankInfo5);
        list.add(cflEasyBankInfo6);
        list.add(cflEasyBankInfo7);
        list.add(cflEasyBankInfo8);
        list.add(cflEasyBankInfo9);
        list.add(cflEasyBankInfo10);
        list.add(cflEasyBankInfo11);
        list.add(cflEasyBankInfo12);
        list.add(cflEasyBankInfo13);
        list.add(cflEasyBankInfo14);
        list.add(cflEasyBankInfo15);
        list.add(cflEasyBankInfo16);
        list.add(cflEasyBankInfo17);
        list.add(cflEasyBankInfo18);

        for(CflEasyBankInfo cflEasyBankInfo : list) {
            System.out.println(JSONObject.toJSON(cflEasyBankInfo).toString());
        }


    }
}