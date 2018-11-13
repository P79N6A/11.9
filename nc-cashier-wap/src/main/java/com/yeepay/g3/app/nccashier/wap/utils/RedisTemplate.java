package com.yeepay.g3.app.nccashier.wap.utils;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.utils.lock.impl.RedisLock;
import com.yeepay.utils.lock.utils.RedisCall;
import com.yeepay.utils.lock.utils.RedisClientUtils;

import redis.clients.jedis.Jedis;

/**
 * @company:YeePay
 * @author: N.L
 * @since: 15/12/9 下午6:33
 * @version:1.0
 */
public class RedisTemplate {
	private static Logger logger = LoggerFactory.getLogger(RedisTemplate.class);

	public static <T> T getTargetFromRedis(final String key, final Class<T> t) {
		try {
			return RedisClientUtils.call(new RedisCall<T>() {
				@Override
				public T run(Jedis jedis) {
					String jsonStr = jedis.get(key);
					if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
						return null;
					}
					return (T) JSON.parseObject(jsonStr, t);
				}
			});
		} catch (Throwable e) {
			logger.error("获取redis缓存失败，key = " + key + ", e=", e);
		}
		return null;
	}

	public static <T> T getTargetFromRedisWithoutCatch(final String key, final Class<T> t) throws Exception{
		return RedisClientUtils.call(new RedisCall<T>() {
			@Override
			public T run(Jedis jedis) {
				String jsonStr = jedis.get(key);
				if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
					return null;
				}
				return (T) JSON.parseObject(jsonStr, t);
			}
		});
	}

	/**
	 * 设置缓存，覆盖旧值
	 * 
	 * @param key
	 * @param o
	 * @param expireTime
	 * @return
	 */
	public static Boolean setCacheCoverOldValue(final String key, Object o, final int expireTime) {
		if (null == o) {
			return false;
		}

		final String val = JSON.toJSONString(o);
		return RedisClientUtils.call(new RedisCall<Boolean>() {
			@Override
			public Boolean run(Jedis jedis) {
				try {
					String response = jedis.setex(key, expireTime / 1000, val);
					logger.info("set redis state:{}, key:{}", response, key);
					return RedisLock.SET_SUCCESS.equals(response);
				} catch (Throwable e) {
					logger.error("redis set error,key:" + key, e);
					return false;
				}
			}
		});

	}
	

	public static boolean setCacheObjectSumValue(final String key, Object o, final int expireTime) {
		if (null == o) {
			return false;
		}
		try {
			final String val = JSON.toJSONString(o);
			return RedisClientUtils.call(new RedisCall<Boolean>() {
				@Override
				public Boolean run(Jedis jedis) {
					if (RedisClientUtils.isNewVersion()) {
						String response = jedis.set(key, val, "NX", "PX", expireTime);
						return RedisLock.SET_SUCCESS.equals(response);
					} else {
						if (jedis.exists(key)) {
							jedis.del(key);
						}
						if (jedis.setnx(key, val) == 1) {
							jedis.expire(key, expireTime / 1000);
							return true;
						}
						return false;
					}
				}
			});
		} catch (Throwable e) {
			logger.error("保存redis缓存失败，key = " + key + ", e=", e);
		}
		return false;

	}

	public static boolean delCacheObject(final String key) {
		try {
			return RedisClientUtils.call(new RedisCall<Boolean>() {
				@Override
				public Boolean run(Jedis jedis) {
					String value = jedis.get(key);
					if (StringUtils.isBlank(value))
						return true;
					long pipline = jedis.del(key);
					if (pipline > 0)
						return true;
					return false;
				}
			});
		} catch (Throwable e) {
			logger.error("删除redis缓存失败，key = " + key + ", e=", e);
		}
		return false;
	}
}
