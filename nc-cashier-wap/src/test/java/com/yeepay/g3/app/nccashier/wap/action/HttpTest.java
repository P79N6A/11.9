package com.yeepay.g3.app.nccashier.wap.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.service.CashierRouteFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class HttpTest extends DefaultHttpClient {

//	private static final Logger logger = LoggerFactory.getLogger(HttpTest.class);
//
//	public static final String LOCAL_WAP_URL_PREFIX = "http://localhost:8080/nc-cashier-wap/";
//	private static final String REMOTE_ACTION_URL = "http://10.151.30.8:8008/nc-cashier-wap/cashier/pqr";
//	private static final String REMOTE83_ACTION_URL = "http://172.21.0.83:8008/nc-cashier-wap/cashier/pqr";
//	private final static String REMOTE_CASHIER_HESSIAN_URL = "http://10.151.30.8:8009/nc-cashier-hessian/hessian/";
//	private static final String LOCAL_ACTION_URL = "http://localhost:8080/nc-cashier-wap/cashier/pqr";
//	private final static String LOCAL_CASHIER_HESSIAN_URL = "http://localhost:8081/nc-cashier-hessian/hessian/";
//	// 被扫
//	private static final String MSCANPAY_ACTION_URL = "http://172.21.0.83:8008/nc-cashier-wap/cashier/pqr";
//
//	// 公众号
//	private static final String MX_ACTION_URL = "http://172.21.0.83:8008/nc-cashier-wap/cashier/mx";
//
//	// 标准版
//	private static final String STD_ACTION_URL = "http://172.21.0.83:8008/nc-cashier-wap/cashier/std";
//
//	// 扫码支付
//	private static final String SCANPAY_ACTION_URL = "http://172.21.0.83:8008/nc-cashier-wap/cashier/aqr";
//
//	private final static String CASHIER_HESSIAN_URL = "http://172.21.0.83:8009/nc-cashier-hessian/hessian/";
//
//	public static String post(String url, Map<String, String> params) {
//		logger.info("create httppost:{} ", url);
//
//		HttpTest httpclient = new HttpTest();
//		String body = null;
//		HttpPost post = postForm(url, params);
//		body = invoke(httpclient, post);
//		httpclient.getConnectionManager().shutdown();
//		return body;
//	}
//
//	private static HttpPost postForm(String url, Map<String, String> params) {
//
//		HttpPost httpost = new HttpPost(url);
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		Set<String> keySet = params.keySet();
//		for (String key : keySet) {
//			nvps.add(new BasicNameValuePair(key, params.get(key)));
//		}
//
//		try {
//			System.out.println("set utf-8 form entity to httppost");
//			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//
//		return httpost;
//	}
//
//	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {
//
//		HttpResponse response = sendRequest(httpclient, httpost);
//		String body = paseResponse(response);
//		return body;
//	}
//
//	private static String paseResponse(HttpResponse response) {
//		System.out.println("get response from http server..");
//		HttpEntity entity = response.getEntity();
//		System.out.println("response status: " + response.getStatusLine());
//		String charset = EntityUtils.getContentCharSet(entity);
//		System.out.println(charset);
//
//		String body = null;
//		try {
//			body = EntityUtils.toString(entity);
//			System.out.println(body);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return body;
//	}
//
//	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
//		System.out.println("execute post...");
//		HttpResponse response = null;
//
//		try {
//			response = httpclient.execute(httpost);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	public static void main(String[] args) {
//		// 标准版
//		// System.out.println(makeStdParams(LOCAL_WAP_URL_PREFIX));
//		// mscan();
//		String plainText = "merchantNo=10040007707&token=C7D747BA0BFDA60165ED78C08FAA0E796FA4510302EEB90B25ABE43BB52BAEF8&timestamp=1488189565&cardType=&userNo=1488189565924&userType=USER_ID&bizType=G2NET";
//		String sign = sign(plainText);
//		System.out.println(sign);
//	}
//
//	/**
//	 * 商家扫码请求入口
//	 */
//	private static void mscan() {
//		try {
//			HttpTest test = new HttpTest();
//			Map<String, String> params = MScanTestCase.liuyanParams();
//			String plainText = MScanTestCase.getSignPlaintext(params);
//			System.out.println("签名明文是=" + plainText);
//			String sign = "";
//			if (CommonUtil.checkBizType(params.get("bizType"))) {
//				sign = sign(plainText);
//				System.out.println("模拟易宝签名:" + sign);
//			} else {
//				sign = GenerateSignUtil.generateSignInfo(plainText, params.get("merchantNo"),
//						getPriKey(params.get("merchantNo")));
//				System.out.println("模拟商户签名:" + sign);
//			}
//			params.put("sign", sign);
//			// test.mscanGet(params);
//			System.out.println(test.post(REMOTE_ACTION_URL, params));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 验证签名
//	 * 
//	 * @param content
//	 * @param sign
//	 */
//	private static boolean signVerify(String appKey, String content, String sign) {
//		HessianProxyFactory bean = new HessianProxyFactory();
//		try {
//			CashierRouteFacade routeFacade = (CashierRouteFacade) bean.create(CashierRouteFacade.class,
//					CASHIER_HESSIAN_URL + "CashierRouteFacade");
//			return routeFacade.yopVerify(null, content, sign);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	/**
//	 * 易宝私钥签名
//	 * 
//	 * @param content
//	 * @return
//	 */
//	private static String sign(String content) {
//		HessianProxyFactory bean = new HessianProxyFactory();
//		try {
//			CashierRouteFacade routeFacade = (CashierRouteFacade) bean.create(CashierRouteFacade.class,
//					CASHIER_HESSIAN_URL + "CashierRouteFacade");
//			return routeFacade.yopSign(content);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 生成公众号url
//	 * @param map
//	 * @return
//	 */
//	public static String getWxUrl(Map<String, String> map) {
//		
//		// 生成签名明文
//		StringBuilder plaintext = new StringBuilder();
//		plaintext
//		.append("merchantNo=").append(map.get("merchantNo"))
//		.append("&token=").append(map.get("token"))
//		.append("&timestamp=").append(map.get("timestamp"))
//		.append("&appId=").append(map.get("appId"))
//		.append("&openId=").append(map.get("openId"));
//		String bizType = map.get("bizType");
//		if (!CommonUtil.checkBizType(bizType)) {
//			bizType = "";
//		} else {
//			bizType = "&bizType=" + bizType;
//		}
//		plaintext.append(bizType);
//		System.out.println("签名明文：" + plaintext);
//		
//		//模拟商户/业务方签名
//		String sign = null;
//		if (!StringUtils.isBlank(bizType)) {
//			sign = sign(plaintext.toString());
//			System.out.println("易宝签名:" + sign);
//		} else {
//			sign = GenerateSignUtil.generateSignInfo(plaintext.toString(), map.get("merchantNo"),
//					getPriKey(map.get("merchantNo")));
//			System.out.println("模拟商户签名:" + sign);
//		}
//		
//		// 生成url
//		String url = "http://10.151.30.8:8008/nc-cashier-wap/cashier/wx?merchantNo=" + map.get("merchantNo") 
//			+ "&token=" + map.get("token") 
//			+ "&appId=" + map.get("appId") 
//			+ "&openId=" + map.get("openId")
//			+ "&timestamp=" + map.get("timestamp") 
//			+ "&sign=" + sign + "" 
//			+ bizType;
//		return url;
//	}
//
////	/**
////	 * 返回标准版的URL
////	 * 
////	 * @param urlPrefix
////	 * @return
////	 */
////	public static String makeStdParams(String urlPrefix) {
////		Map<String, String> map = new HashMap<>();
////		map.put("merchantNo", "10040007800");
////		map.put("token", "C7D747BA0BFDA60165ED78C08FAA0E794BBF09EA6A19C3CD598DC6DD077D6221");
////		map.put("timestamp", "1488096507");
////		map.put("userNo", "wangxinDS0001");
////		map.put("userType", "USER_ID");
////		StringBuilder str = new StringBuilder();
////		str.append(map.get("merchantNo"));
////		str.append(map.get("token"));
////		str.append(map.get("timestamp"));
////		str.append(map.get("userNo"));
////		str.append(map.get("userType"));
////		System.out.println("请求入参：" + str.toString());
////		String priKey7800 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrVHL17lfZ+2nZ3eHuO0soTOHU a4X7wuVPrl1hy9DLQuLqdbwfg10gltRfBnPx3ZkrRdR4m5LY7T6yxdFY5Kej3hNwbrch/pvZwoQo 8F2eHigvrCRjxudmHvQHLdtFRPtimPs4VG3GYl11/e1lPDcnOoOLlPF4lyKmsEKPwK/DK+I+jeCP o2jB+pAxFPVWXY6mWrs91sgpdoOmSCijPDsxfhfmVvFQvLUdXr/SuKjvH75keAZ9oUVNjPC7QJHR o3JYoU9iUfiu5TJwwx7XKCjqMuXvb+iV34/L1MHfZdjo1QU+hvQSTonP1+DSVTOTE3uP6ufiC4nw wyXpGAPMY8YRAgMBAAECggEAMGhy9uO59MAhf0o+7MXaDW/zmsYqnCDME8BraBdjThr+7EoJtkmy hWO4a4TyO5NmFKDtUIp8akhWH8LezKQGbblweqL9oWBD/roEB2EqwmM47YdQ3NQ1S1hRkLm3K5I7 CPe6e4b3YUcnqw/tBF6IItBYnJafx3fEdZ51oBJMVvNW10Eu/ZCdVn+wpMIlGzH8qn3pNrkpP2EW 19OSissnjVJ1E7v5TDGZBQVSHkcbQ7xr9oAjOscZcm6TtTPWKlsNvXayyZBVSnsszRPgmVaIZL+L CJGbcQV6HtOwqIFqmUfWNg0Adf4++K5SJWnEzdMP+SaaJyJnYGjklEkf9mQNxQKBgQDvc8YHreju RfGLNmaCUdr0G5iTSi7KQCymMmSRqOd1upD1v3zGCM4a+zVlGu1aQCmba+qWxXIeuygcwn01g01G ukrq4dP85/mnqisI4OG607NCRILbuupphk/tdYWbdI7o/Qave2slaczLlRpAgc/gYqcwCitljmnf KPvX/KU7gwKBgQC3K4A+UfrrMwKvgYxtcP6lCuG8mswBuDRkPC6LZrJc22S3HWw7LXVWwUi8SZdG oCUwDPFzfrHZsl7965SLKB7zk/1+VmSWt8ZueOMwOg7UPOfbMBg52PC1rOtu5JY4Bm1aZsHAIYDK tKKYUaazFyC23ZidNLQhMXINGN8aIscf2wKBgQCEh86WV4IxxxKem5h3DrkiHNgAxbFKDeTog8G4 AQVC2uT6r2Zu8VaqBloSQKoYJqUgucUYd+Xm7m2QJXFJmge+WsO2ZxF+zCIY042IF3e4gQ2ZYvQO i9DMYSOB6WbumL+0Yr89hxDRn1JTZ44lH/QfXFrusuI8Dmu4sSVa8SG+4wKBgAiVSkIhV0+0KTkO KgVq2RPkyaUr38lo11OnGks/+bWuNi76evre63OwRPdFv4f4syVoRdwyoKTh3d+qLWDD9YdWdPd5 lucVH4BHu+WjotRBMmAsBcaYKtdojfO5VGy1qGQnEoctSrq08jWPBe+4crj+80rSkGpJxd1lP/ca kBgnAoGBAMTq2u3BJ3tpT8YSlOA1ojtFOpKVzdYrKi+1piFjyNgEcTpf1f37x8SxYzVYz9gGor5A oAp8DzyJz/9RsSLJJmAV0lliiNbgBDjFOJvJaUy2Gx8KWD9AHW6vtMNnsssCyrrRHHk1HcojT6Uk fw/rSIouLd9rSkID/5AYUAwnNUai";
////		String sign = GenerateSignUtil.generateSignInfo(str.toString(), map.get("merchantNo"), priKey7800);
////		// System.out.println(sign);
////		String url = urlPrefix + "cashier/std?merchantNo=" + map.get("merchantNo") + "" + "&token=" + map.get("token")
////				+ "&userNo=" + map.get("userNo") + "&timestamp=" + map.get("timestamp") + "&userType="
////				+ map.get("userType") + "&sign=" + sign;
////		return url;
////	}
//	
//	public static String getPriKey(String merchantNo){
//		String priKey7800 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrVHL17lfZ+2nZ3eHuO0soTOHU a4X7wuVPrl1hy9DLQuLqdbwfg10gltRfBnPx3ZkrRdR4m5LY7T6yxdFY5Kej3hNwbrch/pvZwoQo 8F2eHigvrCRjxudmHvQHLdtFRPtimPs4VG3GYl11/e1lPDcnOoOLlPF4lyKmsEKPwK/DK+I+jeCP o2jB+pAxFPVWXY6mWrs91sgpdoOmSCijPDsxfhfmVvFQvLUdXr/SuKjvH75keAZ9oUVNjPC7QJHR o3JYoU9iUfiu5TJwwx7XKCjqMuXvb+iV34/L1MHfZdjo1QU+hvQSTonP1+DSVTOTE3uP6ufiC4nw wyXpGAPMY8YRAgMBAAECggEAMGhy9uO59MAhf0o+7MXaDW/zmsYqnCDME8BraBdjThr+7EoJtkmy hWO4a4TyO5NmFKDtUIp8akhWH8LezKQGbblweqL9oWBD/roEB2EqwmM47YdQ3NQ1S1hRkLm3K5I7 CPe6e4b3YUcnqw/tBF6IItBYnJafx3fEdZ51oBJMVvNW10Eu/ZCdVn+wpMIlGzH8qn3pNrkpP2EW 19OSissnjVJ1E7v5TDGZBQVSHkcbQ7xr9oAjOscZcm6TtTPWKlsNvXayyZBVSnsszRPgmVaIZL+L CJGbcQV6HtOwqIFqmUfWNg0Adf4++K5SJWnEzdMP+SaaJyJnYGjklEkf9mQNxQKBgQDvc8YHreju RfGLNmaCUdr0G5iTSi7KQCymMmSRqOd1upD1v3zGCM4a+zVlGu1aQCmba+qWxXIeuygcwn01g01G ukrq4dP85/mnqisI4OG607NCRILbuupphk/tdYWbdI7o/Qave2slaczLlRpAgc/gYqcwCitljmnf KPvX/KU7gwKBgQC3K4A+UfrrMwKvgYxtcP6lCuG8mswBuDRkPC6LZrJc22S3HWw7LXVWwUi8SZdG oCUwDPFzfrHZsl7965SLKB7zk/1+VmSWt8ZueOMwOg7UPOfbMBg52PC1rOtu5JY4Bm1aZsHAIYDK tKKYUaazFyC23ZidNLQhMXINGN8aIscf2wKBgQCEh86WV4IxxxKem5h3DrkiHNgAxbFKDeTog8G4 AQVC2uT6r2Zu8VaqBloSQKoYJqUgucUYd+Xm7m2QJXFJmge+WsO2ZxF+zCIY042IF3e4gQ2ZYvQO i9DMYSOB6WbumL+0Yr89hxDRn1JTZ44lH/QfXFrusuI8Dmu4sSVa8SG+4wKBgAiVSkIhV0+0KTkO KgVq2RPkyaUr38lo11OnGks/+bWuNi76evre63OwRPdFv4f4syVoRdwyoKTh3d+qLWDD9YdWdPd5 lucVH4BHu+WjotRBMmAsBcaYKtdojfO5VGy1qGQnEoctSrq08jWPBe+4crj+80rSkGpJxd1lP/ca kBgnAoGBAMTq2u3BJ3tpT8YSlOA1ojtFOpKVzdYrKi+1piFjyNgEcTpf1f37x8SxYzVYz9gGor5A oAp8DzyJz/9RsSLJJmAV0lliiNbgBDjFOJvJaUy2Gx8KWD9AHW6vtMNnsssCyrrRHHk1HcojT6Uk fw/rSIouLd9rSkID/5AYUAwnNUai";
//		String priKey287 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQcp9lZjnjLrfxKMUeM3M9uhRs enziyPS3rTa1CBNrhdFSFJW2OLlqemFGD7toowuT15R53ccK921d5Xsi8ikhy4OUiYivJoA9Zztm r+uYRoN7WCFbuZzqlC8W0xjKZaCmAlEkMqARdbgrUrgewr3nAAMa6gNh40Rpq2Qplf9cuXwHfW16 ZMcm01RSMvWPa/QtMusKSgW6WZo3CBekV/o9CVSh9qYurbnlG1POtERS6YtcGAtcjrD3uuPamYFQ xYK2w+eg8q6lkert+2TlTPqTtmnaCAtlGnAC1RFAHRGoT+4Yvw4alDM3G1DUUbeiMmzazEQ4h1uH 3CVypeDMwX0fAgMBAAECggEAXR8oF2zB4O4bc4M/IOs2XhL6W7zTijjXWxp17FtaebT5bxsKMUF8 d2KSF2LJBPon2pXeiHoreaxte10X9z16uujC2R2ZWqFNh0hoCRlcnvzGgtwcFVAiEzCY8vQARWsj GCLiQJ3Kh7cGlhdrz5joaGWfmthGefLUBfOSTSUATbvd96mFPX/RDnvpQ1YR53caz6mPPm+Y/SkY vxKIRrW4KJrnWUc4XGEPtwlMSFhqwzTaLfIFps85Q6kJMvT7iD3oao8sb2QXXWz8jO3yMbXoFYuz F7uF5QVedCNC4lCbaXrQlm2vtz3fodg8iXw5WFXPsUOVgaVPFkP0ky5VAoKYEQKBgQD51REDagTf +HBBXvmxU8kazEg55nDfsvZxKoWnH3sRlYB3n3T6CJhKeLiuRGyWVWE6Bos7a4BNzbaLtXbGUiUd GsHplrEw56pVNIJp7i5+go9fJ9xOcBu1jn3Fok9PZxeX/SmGhiWSrysY+YKjyI28dFgd29ds7Vw0 PVoCOr8CNQKBgQCUA4dS3P+hrkD/r72dlZWgWFKW0assz389Nne05TEQwIdAed5nef6gwa4izCnG 6G/RccjZVyXfTJCGK6qWEyJZnAKg61a3yqanqQAwo7hUCAQ/A6m5AM2BvlyPh7oqRBc6h+67WxpN ZqA158VahV2HrRJZo7Ie+4Zoh9dNSmFsgwKBgEmZjnCODC+bNh7cBv1JrKY7Zk/AZqJQS6/dEhDB AnWUsIsNK935KDxQQj/8omzLbGA2y0/PBLZnEw8nf30/d/WSC2xwW3UH2rNNS9o/M+1LM0eaK5nk BxW+i6jsfybqPRYmm9Qosur6tdyerPDpRXAuakMpn9ZUOuSc6mZbvie9AoGAL/aYwnRU7hqee3yC cG9JpkUYtkDJPGMc80QUNG1Uof4RlgYn2NZCeetpN2N7YjZuDavWjv9biWmxJ5k81RbsOaeBfo3k cvhbWtc79YcIM0rJvXW9aPLmpVV/fw9Xd1zLPi9QYCwccNqBrhYl8Lho349o891E2h9dpb/nN+eT fGsCgYEA6Vf17+MTBko2U0sZFZ/quBORRNN+MVQz5F+iCai6knbvMC36h4T3LlXPiAwOVM3y2EHU dal5mEmgi0mmneJswiLa0Pg3O1Qftbr3IXzCb801Cco4XNlJaw30rEygRrqFcWyoaU07XJWZAcro azt2M8/2T53JecfxwiI8wBCbQ4U=";
//		if ("10040007800".equals(merchantNo)) {
//			return priKey7800;
//		}
//		return priKey287;
//	}
//
//	/**
//	 * 返回标准版的url
//	 * @param map
//	 * @return
//	 */
//	public static String getStdUrl(Map<String, String> map) {
//		// 生成签名明文
//		String directPayType = StringUtils.isBlank(map.get("directPayType")) ? "" : map.get("directPayType");
//		String cardType = StringUtils.isBlank(map.get("cardType")) ? "" : map.get("cardType");
//		String userNo = StringUtils.isBlank(map.get("userNo")) ? "" : map.get("userNo");
//		String userType = StringUtils.isBlank(map.get("userType")) ? "" : map.get("userType");
//		String extInfo = StringUtils.isBlank(map.get("ext")) ? "" : map.get("ext");
//		StringBuilder plaintext = new StringBuilder();
//		plaintext
//		.append("merchantNo=").append(map.get("merchantNo"))
//		.append("&token=").append(map.get("token"))
//		.append("&timestamp=").append(map.get("timestamp"))
//		.append("&directPayType=").append(directPayType)
//		.append("&cardType=").append(cardType)
//		.append("&userNo=").append(userNo)
//		.append("&userType=").append(userType);
//		String bizType = map.get("bizType");
//		if (CommonUtil.checkBizType(bizType)) {
//			bizType = "";
//		} else {
//			bizType = "&bizType=" + bizType;
//		}
//		plaintext.append(bizType);
//		plaintext.append("&ext=").append(extInfo);
//		System.out.println("签名明文：" + plaintext);
//		
//		// 模拟商户/业务方签名
//		String sign = null;
//		if (!StringUtils.isBlank(bizType)) {
//			sign = sign(plaintext.toString());
//			System.out.println("易宝签名:" + sign);
//		}
//		else{
//			sign = GenerateSignUtil.generateSignInfo(plaintext.toString(), map.get("merchantNo"),
//					getPriKey(map.get("merchantNo")));
//			System.out.println("模拟商户签名:" + sign);
//		}
//		
//		// 生成url
//		String url = "http://10.151.30.8:8008/nc-cashier-wap/cashier/std?merchantNo=" + map.get("merchantNo") 
//				+ "&token=" + map.get("token") 
//				+ "&cardType=" + cardType
//				+ "&userNo=" + userNo
//				+ "&timestamp=" + map.get("timestamp") 
//				+ "&userType=" + userType 
//				+ "&directPayType=" + directPayType 
//				+ "&ext=" + extInfo
//				+ "&sign=" + sign  
//				+ bizType;
//		return url;
//	}
//
//	/**
//	 * 返回扫码版URL
//	 * @param map
//	 * @return
//	 */
//	public String getAqrUrl(Map<String, String> map) {
//		// 生成签名明文
//		String userNo = StringUtils.isBlank(map.get("userNo")) ? "" : map.get("userNo");
//		String userType = StringUtils.isBlank(map.get("userType")) ? "" : map.get("userType");
//		String extInfo = StringUtils.isBlank(map.get("ext")) ? "" : map.get("ext");
//		
//		String bizType = map.get("bizType");
//		if (StringUtils.isBlank(bizType)) {
//			bizType = "";
//		} else {
//			bizType = "&bizType=" + bizType;
//		}
//		
//		StringBuilder plaintext = new StringBuilder();
//		plaintext
//		.append("merchantNo=").append(map.get("merchantNo"))
//		.append("&token=").append(map.get("token"))
//		.append("&timestamp=").append(map.get("timestamp"))
//		.append("&userNo=").append(userNo)
//		.append("&userType=").append(userType)
//		.append(bizType)
//		.append("&ext=").append(extInfo);
//		System.out.println("请求入参：" + plaintext);
//		
//		// 模拟商户/业务方签名
//		String sign = null;
//		System.out.println("模拟商户签名:" + sign);
//		if (!StringUtils.isBlank(bizType)) {
//			sign = sign(plaintext.toString());
//			System.out.println("易宝签名:" + sign);
//		}
//		else{
//			sign = GenerateSignUtil.generateSignInfo(plaintext.toString(), map.get("merchantNo"),
//					getPriKey(map.get("merchantNo")));
//			System.out.println("模拟商户签名:" + sign);
//		}
//		String url = "http://10.151.30.8:8008/nc-cashier-wap/cashier/aqr?merchantNo=" + map.get("merchantNo")
//				+ "&token=" + map.get("token") 
//				+ "&userNo=" + userNo 
//				+ "&timestamp=" + map.get("timestamp")
//				+ "&userType=" + userType 
//				+ "&sign=" + sign  
//				+ bizType
//				+ "&ext=" + extInfo;
//		return url;
//	}
//
//	public void mscanGet(Map<String, String> params) {
//		// 用HttpClient发送请求，分为五步
//		// 第一步：创建HttpClient对象
//		HttpTest httpCient = new HttpTest();
//		// 第二步：创建代表请求的对象,参数是访问的服务器地址
//		String url = REMOTE_ACTION_URL + "?" + getGetParams(params);
//		System.out.println(url);
//		HttpGet httpGet = new HttpGet(url);
//		try {
//			// 第三步：执行请求，获取服务器发还的相应对象
//			HttpResponse httpResponse = httpCient.execute(httpGet);
//			// 第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
//			if (httpResponse.getStatusLine().getStatusCode() == 200) {
//				// 第五步：从相应对象当中取出数据，放到entity当中
//				HttpEntity entity = httpResponse.getEntity();
//				String response = EntityUtils.toString(entity, "utf-8");// 将entity当中的数据转换为字符串
//				System.out.println(response);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	private static String getGetParams(Map<String, String> params) {
//		StringBuilder result = new StringBuilder();
//		for (String key : params.keySet()) {
//			result.append("&" + key + "=" + params.get(key));
//		}
//		return result.substring(1, result.length());
//	}

}
