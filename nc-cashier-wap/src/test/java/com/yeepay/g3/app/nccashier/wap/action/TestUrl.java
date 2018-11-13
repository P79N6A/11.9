package com.yeepay.g3.app.nccashier.wap.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.utils
 *
 * @author pengfei.chen
 * @since 17/2/21 18:13
 */
public class TestUrl {

    public static void main(String[] args){
    	//ICBC_B2C
    	
    	String[] strArry = "ICBC_B2C".split("_");
    	System.out.println(strArry.length);
//        try {
//            HttpTest test = new HttpTest();
//            long timestamp= new Date().getTime()/1000l;
//            Map<String,String> map = new HashMap<String, String>();
//            map.put("token","97C3E51DAC89E5450204ACAC2A03804129FB057AF820CEAED497BF93FF6349CD");
//            map.put("merchantNo","10040007800");
//            map.put("timestamp",timestamp+"");
////            map.put("userNo","zmlWechatOpenId");
////            map.put("userType","WECHAT");
//            map.put("userNo","zml");
//            map.put("userType","USER_ID");
//            map.put("directPayType","");
//            map.put("cardType","");
////            map.put("appId","123213123");
////            map.put("openId","qweqweqwewq");
//            map.put("bizType","");
//            Map<String, String> extInfo = new HashMap<String, String>();
//            extInfo.put("openId", "zml_wechat");
//            extInfo.put("appId", "wx9e13bd68a8f1921e");
//            map.put("ext", JSON.toJSONString(extInfo));
//            String url = test.getStdUrl(map);
//            String aqrUrl = test.getAqrUrl(map);
//            System.out.println(url);
//            System.out.println(aqrUrl);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
