package com.yeepay.g3.core.payprocessor.util;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.utils.lock.utils.RedisCall;
import com.yeepay.utils.lock.utils.RedisClientUtils;

import redis.clients.jedis.Jedis;

public class RedisUtil {

	private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(RedisUtil.class);

	/***
	 * 将信息存入Redis,使用json格式存储数据
	 * 
	 * @param bizType:操作类型
	 * @param key:唯一标识
	 * @param value:值
	 * @param timeOut:缓存有效期
	 *            单元秒
	 */
	public static void pushObjectToRedis(String bizType, final String key, final Object value, final int timeOut) {
		final String redisKey = generateRedisKey(bizType, key);
		try {
			final String json = JSON.toJSONString(value);
			RedisClientUtils.call(new RedisCall<Boolean>() {
				@Override
				public Boolean run(Jedis jedis) {
					jedis.setex(redisKey, timeOut, json);
					return true;
				}
			});
			logger.info("[推送到Redis] - [" + key + "] - [OK] - [" + redisKey + "] " + json);
		} catch (Throwable th) {
			logger.error("[推送到Redis] - [" + key + "] - [失败] - [" + redisKey + "]", th);
		}
	}

	/**
	 * 从Redis里面取值
	 * 
	 * @param bizType:操作类型
	 * @param key
	 * @return
	 */
	public static <T> T getObjectFromRedis(String bizType, String key, final Class<T> clazz) {
		if (null == bizType || null == key || null == clazz) {
			return null;
		}
		final String redisKey = generateRedisKey(bizType, key);
		try {
			T t = RedisClientUtils.call(new RedisCall<T>() {
				@Override
				public T run(Jedis jedis) {
					String value = jedis.get(redisKey);
					if (value == null) {
						logger.info("[获取Redis信息] - [失败] - [" + redisKey + "] [值不存在]");
						return null;
					}
					logger.info("[获取Redis信息] - [OK] - [" + redisKey + "] " + value);
					return JSON.parseObject(value, clazz);
				}
			});
			return t;
		} catch (Throwable th) {
			logger.error("[获取Redis信息] - [" + key + "] - [失败] - [" + redisKey + "]", th);
			return null;
		}
	}

	public static String generateRedisKey(String bizType, String key) {
		return ConstantUtils.REDIS_KEY_PREFIX + bizType + "_" + key;
	}

}
