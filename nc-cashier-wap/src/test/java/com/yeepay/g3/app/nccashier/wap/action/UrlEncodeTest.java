package com.yeepay.g3.app.nccashier.wap.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlEncodeTest {
	
	public static void main(String[] args){
		  String urlStr;
		try {
			

			String prefix = "http://172.18.166.227:8080/nc-cashier-wap/cashier/std?sign=c57kMvAX81UmS0Mrpe6pDH_uhZQzRk-dIERXR32hUfUC4rOSLTrSL8wli-BPBjbsfWoqQxJojljtOR8vokTwgxHHUmCVwbkEU_6Srtq72xQl1qhFHCrmUXDgh4yHKm-iSeDXMiFu-B1y7j5kiRjfmEIy1I_IXMFFJokRW84Z-s11BthzJ9OhAfD8KJkKO_O9ev5W8VEAcg-VHX-puQQtn1EMx49MvSVudNOLi-K8navxIW6HQL6asJ2Z5UezJ5xxJoU213uup3_vHT7kJ0005vKJvHd3kb6iRncGJlxvvSMbesuh0gX0iWFxwoXvl-qN-h5OtJTPDUfR0Yf_rkEnZg$SHA256&timestamp=1514277532&token=AD4659E4074919A9892F76D725F633DF5A1B059211413959988D85D130F7A664&merchantNo=10040007800";
			urlStr = prefix  
					+ URLEncoder.encode("{\"", "utf8") + "aliUserId" + URLEncoder.encode("\":\"2088002364008751\"", "utf8")
					+ URLEncoder.encode(",\"", "utf8") + "aliAppId" + URLEncoder.encode("\":\"2017080207997311\"", "utf8")
//					+ URLEncoder.encode(",\"", "utf8") + "appStatement" + URLEncoder.encode("\":\"https://www.baidu.com\"", "utf8")
					+ URLEncoder.encode("}", "utf8");
		    System.out.println(urlStr);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
