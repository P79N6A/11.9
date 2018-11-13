package com.yeepay.g3.app.nccashier.wap.utils;

import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpsHelper extends DefaultHttpClient {

	private static final Logger logger = LoggerFactory.getLogger(HttpsHelper.class);

	public HttpsHelper() throws Exception {
		super();
		SSLContext ctx = SSLContext.getInstance("TLS");
		X509TrustManager tm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = this.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, ssf));
	}

	public static String doGet(String userAgent, String url, String charset) {
		if (null == charset) {
			charset = "utf-8";
		}
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;

		try {
			httpClient = new HttpsHelper();
			httpGet = new HttpGet(url);
			httpGet.addHeader("User-Agent", userAgent);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
			return result;
		} catch (Exception e) {
			logger.error("调用http get请求异常，url = " + url + ", 异常 = ", e);
			return null;
		} finally {
			logger.info("doGet() 调用http接口url={},返回结果={}", url, result);
		}
	}
	
	/**
	 * 缩短链接
	 * @param url
	 * {"url":"http://suo.im/baidu","err":""}
	 * @return shortUrl
	 */
	public static String transformShortUrl(String url) {
		try {
			url = "http://suo.im/api.php?format=json&url="+URLEncoder.encode(url, "UTF-8");
		
			String result = HttpsHelper.doGet("", url, "UTF-8");
			Map<String,String> resultMap = JSONObject.parseObject(result, Map.class);
			if(MapUtils.isNotEmpty(resultMap) && StringUtils.isBlank(resultMap.get("err"))){
				return resultMap.get("url");
			}
		} catch (Throwable e) {
			logger.error("transformShortUrl异常，url = " + url + ", 异常 = ", e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(HttpsHelper.transformShortUrl("https://cash.yeepay.com/newwap/request?token=18448b07-47fd-42e3-a6f4-245b531e7560&requestId=rL9*uqwgipBJVlB-F12sOw==&merchantNo=10000470992"));
	}

}
