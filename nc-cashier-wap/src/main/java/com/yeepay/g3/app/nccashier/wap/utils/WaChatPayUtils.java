package com.yeepay.g3.app.nccashier.wap.utils;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.app.nccashier.wap.enumtype.AlipayLifeNoPropertyEnum;
import com.yeepay.g3.app.nccashier.wap.utils.signature.SignatureUtil;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.httpclient.SimpleHttpUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.utils.lock.Lock;
import com.yeepay.utils.lock.impl.RedisLock;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.utils
 *
 * @author pengfei.chen
 * @since 16/12/8 15:15
 */
public class WaChatPayUtils {

    private static final Logger logger = LoggerFactory.getLogger(WaChatPayUtils.class);

	/** 微信h5，未开通低配版或高配版 */
	public static final int WECHAT_H5_CONFIG_NONE = 0;
	/** 微信h5优先使用低配版 */
	public static final int WECHAT_H5_PREFER_LOW = 1;
	/** 微信h5优先使用高配版 */
	public static final int WECHAT_H5_PREFER_HIGH = 2;


    /**
     * 获取微信认证的访问地址：微信公众号使用
     * @param appId
     * @param token
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toWechatAuth(String appId,String token) throws UnsupportedEncodingException {
        // 微信授权地址
        String weAuthUrl = CommonUtil.getWeAuthUrl();
        String auth2CallUrlConfig = CommonUtil.getAuth2CallBack();
        // 微信回调地址
        String auth2CallUrl = URLEncoder.encode(auth2CallUrlConfig, "UTF-8");
        // 固定替换
		weAuthUrl = weAuthUrl.replace("AppID", appId).replace("Auth2CallUrl", auth2CallUrl).replace("token", token);
		logger.info("获取微信的认证接口url,authUrl={},token={},appId={},redirectUrl={}", weAuthUrl, token, appId,auth2CallUrlConfig);
		return weAuthUrl;
    }


	/**
	 * 获取微信认证的访问地址：微信h5低配版使用
	 * @param appId
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String toWechatAuthForH5Low(String appId,String token) throws UnsupportedEncodingException {
		// 微信授权地址
		String weAuthUrl = CommonUtil.getWeAuthUrl();
		String auth2CallUrlConfig = CommonUtil.getAuth2CallBackForH5Low();
		// 微信回调地址
		String auth2CallUrl = URLEncoder.encode(auth2CallUrlConfig, "UTF-8");
		// 固定替换
		weAuthUrl = weAuthUrl.replace("AppID", appId).replace("Auth2CallUrl", auth2CallUrl).replace("token", token);
		logger.info("获取微信的认证接口url,authUrl={},token={},appId={},redirectUrl={}", weAuthUrl, token, appId,auth2CallUrlConfig);
		return weAuthUrl;
	}

	/**
	 * 获取微信认证的访问地址 ：微信h5低配版使用，且使用商户回调
	 * @param appId
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String toWechatAuthForH5Low(String appId,String token, String merchantNo) throws UnsupportedEncodingException {
		// 微信授权地址
		String weAuthUrl = CommonUtil.getWeAuthUrl();
		// 微信回调地址
		String auth2CallUrlConfig = CommonUtil.getWechatH5LowMerchantRedirect(merchantNo);
		String auth2CallUrl = URLEncoder.encode(auth2CallUrlConfig, "UTF-8");
		// 固定替换
		weAuthUrl = weAuthUrl.replace("AppID", appId).replace("Auth2CallUrl", auth2CallUrl).replace("token", token);
		logger.info("获取微信的认证接口url,authUrl={},token={},appId={},merchantNo={},redirectUrl={}", weAuthUrl, token, appId, merchantNo,auth2CallUrlConfig);
		return weAuthUrl;
	}

	/**
	 * 获取支付宝生活号的授权地址
	 * 
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String parseAlipayLifeNoAuth2Url(String token, PayTypeEnum payType) throws UnsupportedEncodingException {
		String auth2Url = CommonUtil.getAlipayLifeNoConfig(AlipayLifeNoPropertyEnum.AUTH2_URL.getDescription());
		String auth2CallbackUrl = null;
		if (PayTypeEnum.ZFB_SHH == payType) {
			auth2CallbackUrl = CommonUtil.getAlipayLifeNoConfig(AlipayLifeNoPropertyEnum.CALLBACK_URL.getDescription());
		} else if (PayTypeEnum.ALIPAY_H5_STANDARD == payType) {
			auth2CallbackUrl = CommonUtil
					.getAlipayLifeNoConfig(AlipayLifeNoPropertyEnum.STANDARD_CALLBACK_URL.getDescription());
		}
		String appId = CommonUtil.getAlipayLifeNoConfig(AlipayLifeNoPropertyEnum.ALIPAY_APPID.getDescription());
		if(StringUtils.isBlank(auth2Url) || StringUtils.isBlank(auth2CallbackUrl) || StringUtils.isBlank(appId)){
			return null;
		}
		auth2CallbackUrl = URLEncoder.encode(auth2CallbackUrl, "UTF-8");
		auth2Url = auth2Url.replace("{AppID}", appId).replace("{Auth2CallUrl}", auth2CallbackUrl).replace("{token}",
				token);
		logger.info("获取支付宝生活授权地址url={}, token={}, appId={}", auth2Url, token, appId);
		return auth2Url;
	}

	/**
	 * 获取支付宝生活号的userId
	 * 
	 * @param code
	 * @param token
	 * @return
	 */
	public static String getAlipayUserId(String appId, String code, String token) {
		String appSecret = CommonUtil.getAlipayLifeNoConfig(AlipayLifeNoPropertyEnum.ALIPAY_APPSECRET.getDescription());
		String alipayUserIdGetUrl = CommonUtil.getAlipayLifeNoConfig(AlipayLifeNoPropertyEnum.USERID_GET_URL.getDescription());
		if (StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret) || StringUtils.isBlank(code)
				|| StringUtils.isBlank(alipayUserIdGetUrl)) {
			return "none";
		}
		Map<String, String> alipayUserIdGetParams = new HashMap<String, String>();
		alipayUserIdGetParams.put("app_id", appId);
		alipayUserIdGetParams.put("charset", "UTF8");
		alipayUserIdGetParams.put("code", code);
		alipayUserIdGetParams.put("grant_type", "authorization_code");
		alipayUserIdGetParams.put("method", "alipay.system.oauth.token");
		alipayUserIdGetParams.put("sign_type", "RSA2");
		String timestamp = DataUtil.getFormatTimeData(new Date(), DataUtil.SYS_DEFAULT_TIME_FORMAT);
		alipayUserIdGetParams.put("timestamp", timestamp);
		alipayUserIdGetParams.put("version", "1.0");

		StringBuilder builder = new StringBuilder().append("app_id=").append(appId).append("&charset=UTF8")
				.append("&code=").append(code).append("&grant_type=authorization_code")
				.append("&method=alipay.system.oauth.token").append("&sign_type=RSA2").append("&timestamp=")
				.append(timestamp).append("&version=1.0");
		logger.info("调用支付宝授权 token={}, 入参签名明文={}", token, builder.toString());

		try {

			String sign = SignatureUtil.rsaSign(builder.toString(), appSecret, "UTF-8", "RSA2");
			logger.info("调用支付宝授权 token={}, 生成签名={}", token, sign);
			alipayUserIdGetParams.put("sign", sign);
			String responseData = SimpleHttpUtils.httpPost(alipayUserIdGetUrl, alipayUserIdGetParams);
			if (StringUtils.isNotBlank(responseData)) {
				JSONObject jsonObject = JSONObject.parseObject(responseData);
				if (jsonObject != null) {
					JSONObject response = (JSONObject) jsonObject.get("alipay_system_oauth_token_response");
					if (response != null) {
						String userId = (String) response.get("user_id");
						logger.debug("####user_id=" + userId);
						return userId;
					}
				}
			}
		} catch (Throwable t) {
			logger.error("获取支付宝生活号userId失败, token=" + token, t);
		}

		return "none";
	}

    /**
     * 调用微信接口，获取openId
     * @param appId
     * @param code
     * @return
     */
    public static String getWechatOpenId(String appId ,String code,String appSecert) throws Exception {
        try {
            logger.info("获取openid的参数:appid="+appId+",code="+code);
            // 获取微信获取openID的API接口
            String openIdUrl = CommonUtil.getWeOpenIdUrl();
            openIdUrl = openIdUrl.replace("AppID", appId).replace("SECRET", appSecert).replace("CODE",
                    code);
            logger.info("获取微信openId地址=" + openIdUrl);

            String jsonString = SimpleHttpUtils.httpGet(openIdUrl, null);

            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            logger.info("获取微信openId地址返回结果" + jsonObject.toJSONString());
            String openId = jsonObject.getString("openid");
            logger.info("####openId=" + openId);
            return openId;
        }catch (Throwable e){
            logger.error("获取openid失败",e);
        }
        return "none";
    }
    
    /**
     * 获取锁，若requetId已拿到锁，可重入
     * @param lockKey
     * @param timeOut
     * @param requestId
     * @return
     */
    public static boolean getReentrantLock(String lockKey,String requestId) {
    	String lockOn = CommonUtil.getWechatH5LowAPIConfig("LOCK_ON");
    	if("ON".equals(lockOn)){
    		//锁开关打开时才去拿锁，没开启时不需要获取锁
	    	String timeOutString = CommonUtil.getWechatH5LowAPIConfig("REDIS_TIME_OUT");
			int timeOut = StringUtils.isNotBlank(timeOutString)?Integer.parseInt(timeOutString):60;
	
	    	Lock lock = new NewRedisLock("ol.nccashier.lock."+lockKey, timeOut,lockKey);
			try{
				if(lock.tryLock(2)){
					logger.info("key={},requestId={}获得锁",lockKey,requestId);
					RedisTemplate.setCacheObjectSumValue("NCCASHIER_"+lockKey, requestId, timeOut*1000);
					return true;
				} else {
					String requestIdFromReids = RedisTemplate.getTargetFromRedis("NCCASHIER_"+lockKey, String.class);
					if(StringUtils.isNotBlank(requestIdFromReids) && requestIdFromReids.equals(requestId)){
						logger.info("key={},requestId={}可重入",lockKey,requestId);
						return true;
					}else{
						logger.warn("key={},requestId={}未获得锁",lockKey,requestId);
						return false;
					}
				}
			}catch(Throwable e){
				logger.error("key="+lockKey+"获取锁异常",e);
				return false;
			}
    	}else{
    		return true;
    	}
    }
    
    public static void releaseLock(String lockKey) {
    	
    	String lockOn = CommonUtil.getWechatH5LowAPIConfig("LOCK_ON");
    	if("ON".equals(lockOn)){
			try{
				RedisTemplate.delCacheObject("NCCASHIER_"+lockKey);
				Lock lock = new NewRedisLock("ol.nccashier.lock."+lockKey, 10,lockKey);
				lock.unlock();
				logger.warn("key={}释放锁",lockKey);
			}catch(Throwable e){
				logger.error("key="+lockKey+"释放锁异常",e);
			}
    	}
    }
    
    
	/**
	 * 微信h5低配版，获取从wap页跳转微信的链接
	 * @param targetUrl 要在微信中打开的目标url
	 * @param lockKey 
	 * @param requestId
	 * @return
	 */
	public static String buildWechatH5JumpUrl(String targetUrl,String lockKey,String requestId) throws UnsupportedEncodingException {
		
		if(WaChatPayUtils.getReentrantLock(lockKey,requestId)){
			String switchValue = CommonUtil.getWechatH5LowAPIConfig("JUMP_SWITCH");
			if (StringUtils.isBlank(targetUrl)) {
				return null;
			}
			targetUrl = URLEncoder.encode(targetUrl, "UTF-8");
			if("V1".equals(switchValue)){
				String jumpHost = CommonUtil.getWechatH5LowAPIConfig(ConstantUtil.WECHAT_H5_LOW_INTERFACE_JUMP_URL_V1);
				String key = CommonUtil.getWechatH5LowAPIConfig(ConstantUtil.WECHAT_H5_LOW_INTERFACE_PARAM_KEY);
				String f = "json";
				StringBuilder jumpUrl = new StringBuilder();
				jumpUrl.append(jumpHost).append("?key=").append(key).append("&f=").append(f).append("&url=").append(targetUrl).append("&qrpay=yes");
				//httpclient 调用远程接口 ，获取weixin:// 格式的ticket_url
				String result = HttpsHelper.doGet("", jumpUrl.toString(), "UTF-8");
				Map map = JSONObject.parseObject(result, Map.class);
				if (map == null) {
					return null;
				}
				Object status = map.get("status");
				Object ticketUrl = map.get("ticket_url");
				if (status == null || ticketUrl == null || !"ok".equalsIgnoreCase(status.toString())) {
					return null;
				}
				return ticketUrl.toString();
			}else{
				String jumpHost = CommonUtil.getWechatH5LowAPIConfig(ConstantUtil.WECHAT_H5_LOW_INTERFACE_JUMP_URL_V2);
				return jumpHost + targetUrl;
			}
		}else{
			return "LOCK";
		}
	}

	/**
	 * 检查商户微信h5支付的开通和优先级配置
	 * @param info
	 * @param extendInfo
	 * @return 0=不能使用微信h5； 1= 使用微信h5低配版； 2= 使用微信h5高配版
	 */
	public static int checkWechatH5Preference(String merchantNo, PayExtendInfo extendInfo) {
		//都开通时的优先级，根据商编来配置开关(默认走低)
		int result = 0;
		if (StringUtils.isBlank(merchantNo) || extendInfo == null) {
			return result;
		}
		if (extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_LOW)) {
			//开通微信h5低配版
            result = result + 1;
		}
		if (extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP)) {
			//开通微信h5高配版
			result = result + 2;
		}
		if (result != 3) {
			return result;
		}
		//如果同时开通，则取配置中心的商户微信h5优先级配置
		String ewalletLevelWechat = extendInfo.getEwalletLevelWechat();
		if(PayTypeEnum.WECHAT_H5_WAP.name().equals(ewalletLevelWechat)){
			return 2;
		}
		if(PayTypeEnum.WECHAT_H5_LOW.name().equals(ewalletLevelWechat)){
			return 1;
		}
		//无配置，按低配走（在保存优先级时，如发现未配置会报错，故实际上不会走到这里）
		logger.warn("checkWechatH5Preference() 商户同时开通了微信h5标配与低配版，但获取到优先级配置，请检查配置中心，商编={}",merchantNo);
		return 1;
	}


	/**
	 * 钱包H5包装版操作标识校验：
	 * 检查当前请求是否已写入支付宝标准版/微信h5低配版的操作标识，用于区分当前请求，应走公众号还是微信h5低配版/应走生活号还是支付宝标准版
	 * 
	 * @param token
	 * @param payType
	 * @return
	 */
	public static boolean checkEwalletH5PackingSiginal(String token, String payType){
        String key = ConstantUtil.EWALLET_H5_OPERATE_SIGNAL_REDIS_KEY + token + payType;
		String siginal = null;
		try {
			siginal = RedisTemplate.getTargetFromRedisWithoutCatch(key, String.class);
		}catch (Exception e){
        	logger.warn("checkWechatH5LowSiginal() 读取redis失败，按读取到标识处理");
        	return true;
		}
        if(StringUtils.isNotBlank(siginal) && "ok".equals(siginal)){
            return true;
        }
        return false;
	}
	

	/**
	 * 钱包H5包装版操作标识校验记录：记录当前请求的微信h5低配版/支付宝标准版的操作标识
	 * 
	 * @param token
	 * @param payType
	 * @return
	 */
	public static void recordEwalletH5PackingSiginal(String token, String payType) {
        String key = ConstantUtil.EWALLET_H5_OPERATE_SIGNAL_REDIS_KEY + token + payType;
        int expire = ConstantUtil.EWALLET_H5_OPERATE_SIGNAL_REDIS_EXPIRE;
        RedisTemplate.setCacheObjectSumValue(key,"ok",expire);
    }
	
	/**
	 * 从UA获取手机型号
	 * @param userAgent
	 * @return
	 */
	public static String getPhone(String userAgent){
		try{
			Pattern patternAndroid = Pattern.compile(";\\s?(\\S*\\s?\\S*\\s?\\S*)\\s?(Build)/");
			Pattern patternIphone1 = Pattern.compile("(Device/Apple)(\\S*\\s?\\S*)");
			Pattern patternIphone2 = Pattern.compile("(\\(\\S*\\s?\\S*)(; CPU)");  
			
	        Matcher matcherAndroid = patternAndroid.matcher(userAgent); 
	        Matcher matcherIphone1 = patternIphone1.matcher(userAgent);
	        Matcher matcherIphone2 = patternIphone2.matcher(userAgent);
	
	        if (matcherAndroid.find()) {  
	           String model = matcherAndroid.group(1).trim().toUpperCase();  
	           String[] models = model.split(";");
	           if(models.length>1){
	        	   return models[1].trim();
	           }
	           return model;
	        }else if(matcherIphone1.find()){
	        	String model = matcherIphone1.group(2).trim().toUpperCase();  
	        	return model.replace("(", "").replace(")", "");
	        }else if(matcherIphone2.find()){
	        	String model = matcherIphone2.group(1).trim().toUpperCase();  
	        	return model.replace("(", "").replace(")", "");
	        }else{
	        	return "";
	        }
		}catch(Throwable t){
			logger.error("userAgent="+userAgent+"获取手机型号异常",t);
			return "";
		}
	}
	
	
	public static void main(String[] args) {
		String phone =WaChatPayUtils.getPhone("Mozilla/5.0 (Linux; Android 6.0; vivo Y67A Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36");
		System.out.println(phone);
		
	}
}
