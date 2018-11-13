package com.yeepay.g3.core.frontend.util;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.entity.PayRecord;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.encrypt.DESede;
import com.yeepay.g3.utils.common.json.JSONException;
import com.yeepay.g3.utils.common.json.JSONObject;
import com.yeepay.utils.lock.utils.RedisCall;
import com.yeepay.utils.lock.utils.RedisClientUtils;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class RedisUtil {
	
	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(RedisUtil.class);
	
	private static final String confusionkey = "q1w2e3r4t5y6u7i8o9p0zaxs";

	/***
	 * 将信息存入Redis,使用json格式存储数据
	 * @param bizType:操作类型
	 * @param key:唯一标识
	 * @param value:值
	 * @param timeOut:缓存有效期 单元秒
	 */
	private static void pushObjectToRedis(String bizType , final String key , final Object value , final int timeOut){
	    final String redisKey = generateRedisKey(bizType ,key);
		long start = System.currentTimeMillis();
		try{
		    final String json = JSONObject.wrap(value).toString();
			RedisClientUtils.call(new RedisCall<Boolean>() {
				@Override
				public Boolean run(Jedis jedis) {
                    jedis.setex(redisKey,timeOut,json);
					return true;
				}
			});
			logger.info("[推送到Redis] - [" + key +"] - [OK] - [" + redisKey +"] " + json + "-[timeout]-" + timeOut);
		} catch (Throwable th){
			logger.error("[推送到Redis] - [" + key +"] - [失败] - [" + redisKey +"]", th);
		}finally {
			long time = System.currentTimeMillis() - start;
			if (redisExecuteTimeLogEnable(time)) {
				logger.info("pushObjectToRedis() 设置redis缓存超时，key=" + key + "，耗时=" + time + "ms");
			}
		}
	}

	/**
	 * 从Redis里面取值
	 * @param bizType:操作类型
	 * @param key
	 * @return
	 */
	private static Object getObjectFromRedis(String bizType ,String key){
		final String redisKey = generateRedisKey(bizType,key);
		long start = System.currentTimeMillis();
		try {
			Object object = RedisClientUtils.call(new RedisCall<Object>() {
				@Override
				public Object run(Jedis jedis) {
					String value = jedis.get(redisKey);
					if (value == null) {
						logger.info("[获取Redis信息] - [失败] - [" + redisKey + "] [值不存在]");
						return null;
					}
					logger.info("[获取Redis信息] - [OK] - [" + redisKey + "] " + value);
					try {
						return new JSONObject(value);
					} catch (JSONException e) {
						logger.error("[转换为JSON数据失败] - [" + redisKey + "] value is : " + value);
						return value;
					}
				}
			});
			return object;
		} catch (Throwable th){
			logger.error("[获取Redis信息] - [" + key +"] - [失败] - [" +  redisKey + "]" ,th);
			return null;
		}finally {
			long time = System.currentTimeMillis() - start;
			if (redisExecuteTimeLogEnable(time)) {
				logger.info("getObjectFromRedis() 读取redis缓存超时，key=" + key + "，耗时=" + time + "ms");
			}
		}
	}

	/**
	 * 删除redis缓存
	 * @param bizType
	 * @param key
	 * @return
	 */
	public static Long delFromRedis(String bizType ,String key){
		final String redisKey = generateRedisKey(bizType,key);
		try {
			Long object  = RedisClientUtils.call(new RedisCall<Long>() {
				@Override
				public Long run(Jedis jedis) {
					Long value = jedis.del(redisKey);
					logger.info("[delFromRedis] - [OK] - [" +  redisKey + "] " + value);
					return value;
				}
			});
			return object;
		} catch (Throwable th){
			logger.error("[delFromRedis] - [" + key +"] - [失败] - [" +  redisKey + "]" ,th);
			return 0L;
		}
	}
	
//	/**
//	 * 将支付记录存入缓存
//	 * 有效期为1个小时,1小时后再次请求,则需要重新下单
//	 * @param payOrder
//	 * @param record
//	 */
//	public static void pushPayRecordToRedis(PayOrder payOrder,PayRecord record){
//		if (payOrder == null || record == null)
//			return;
//		Map params = new HashMap();
//		params.put("frontValue",record.getFrontValue());
//		pushObjectToRedis("ORDER_CHECK", payOrder.getOrderNo()+payOrder.getPayInterface(), params,ConstantUtils.getCacheTime());//有效期为1小时
//	}

	/**
	 * 获取缓存里的支付记录信息
	 * @param payOrder
	 * @return
	 */
	public static PayRecord getRecord(PayOrder payOrder){
		JSONObject jsonObject = (JSONObject) getObjectFromRedis("ORDER_CHECK",
				payOrder.getOrderNo()+payOrder.getPayInterface());
		if (jsonObject == null)
			return null;
        try {
            PayRecord record = new PayRecord();
            record.setFrontValue((String) jsonObject.get("frontValue"));
			logger.info("[FE下单缓存]从redis中获取下单缓存，写入缓存，payOrderNo="+payOrder.getPayOrderNo()+"，orderNo="+payOrder.getOrderNo());
			return record;
        } catch (JSONException e) {
            return null;
        }
	}

	public static String generateRedisKey(String bizType ,String key){
		String rediskey = ConstantUtils.REDIS_KEY_PREFIX + bizType + "_" + key;
		return DESede.encryptToBase64(rediskey, confusionkey);
	}
	
	/**
	 * 缓存路由支付链接
	 * @param payOrder
	 * @param payUrl
	 */
	public static void pushPayUrlToRedis(PayOrder payOrder, String payUrl){
		if (payOrder == null
				|| StringUtils.isBlank(payOrder.getOrderNo())
				|| StringUtils.isBlank(payUrl)){
			return;
		}
		//根据payInterface来判断是否缓存支付链接
		if(ConstantUtils.isDisablePayCache(payOrder.getPayInterface())){
			logger.warn("[跳过支付链接缓存] payInterface:"+payOrder.getPayInterface());
			return;
		}
		Map params = new HashMap();
		params.put("frontValue", payUrl);
		pushObjectToRedis("ORDER_CHECK", payOrder.getOrderNo()+payOrder.getPayInterface(), params, ConstantUtils.getPayUrlCacheTime());
		logger.info("[FE下单缓存]调用路由下单成功，写入缓存，payOrderNo="+payOrder.getPayOrderNo()+"，orderNo="+payOrder.getOrderNo()+"，缓存有效期="+ConstantUtils.getPayUrlCacheTime());
	}
	
	/**
	 * 缓存微信公众号壳账户号
	 * @param maskMerchantNo
	 * @param dealUniqueSerialNo
	 */
	public static void pushMaskMerchantNo(String maskMerchantNo, String dealUniqueSerialNo){
		if(StringUtils.isNotBlank(maskMerchantNo)){
			pushObjectToRedis(OrderType.JSAPI.name(), dealUniqueSerialNo, maskMerchantNo, ConstantUtils.getMaskMerchantNoCacheTime());
		}
	}
	
	/**
	 * 获取微信公众号壳账户号
	 * @param dealUniqueSerialNo
	 */
	public static String getMaskMerchantNo(String dealUniqueSerialNo){
		return (String) getObjectFromRedis(OrderType.JSAPI.name(), dealUniqueSerialNo);
	}


	/**
	 * 判断redis读写执行时间日志是否需打印
	 * @param executeMillis 执行毫秒数
	 * @return
	 */
	private static boolean redisExecuteTimeLogEnable(long executeMillis) {
		String excuteTimeThreshold = ConstantUtils.getRedisTimeThreshold();
		if(StringUtils.isBlank(excuteTimeThreshold)){
			return false;
		}
		return executeMillis > Long.parseLong(excuteTimeThreshold);
	}
	
}
