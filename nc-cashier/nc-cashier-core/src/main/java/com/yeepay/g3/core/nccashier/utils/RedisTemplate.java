package com.yeepay.g3.core.nccashier.utils;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.utils.lock.impl.RedisLock;
import com.yeepay.utils.lock.utils.RedisCall;
import com.yeepay.utils.lock.utils.RedisClientUtils;
import java.util.Map;

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
		long start = System.currentTimeMillis();
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
			logger.error("getTargetFromRedis失败，key = " + key, e);
			return null;
		} finally {
			long time = System.currentTimeMillis() - start;
			if(redisExecuteTimeLogEnable(time)){
				// 查询缓存耗时超过阈值时，打日志
				logger.info("getTargetFromRedis() 获取redis缓存，key={}，耗时={}ms",key,time);
			}
		}
	}


	public static Boolean setCacheObjectSumValue(final String key, Object o, final int expireTime) {
		if (null == o) {
			return false;
		}
		long start = System.currentTimeMillis();
		try {
			final String val = JSON.toJSONString(o);
			return RedisClientUtils.call(new RedisCall<Boolean>() {
				@Override
				public Boolean run(Jedis jedis) {
					String response = jedis.setex(key, expireTime / 1000, val);
					logger.info("set redis state:{}, key:{}", response, key);
					return RedisLock.SET_SUCCESS.equals(response);
				}
			});
		} catch (Throwable e) {
			logger.error("redis set error,key:" + key, e);
			return false;
		} finally {
			long time = System.currentTimeMillis() - start;
			if(redisExecuteTimeLogEnable(time)){
				logger.info("setCacheObjectSumValue() 保存redis缓存，key={}，耗时={}ms",key,time);
			}
		}
	}

	public static boolean delCacheObject(final String key) {
		long start = System.currentTimeMillis();
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
			logger.error("delCacheObject() 删除redis缓存失败，key = " + key + ", e=", e);
			return false;
		} finally {
			long time = System.currentTimeMillis() - start;
			if(redisExecuteTimeLogEnable(time)){
				logger.info("delCacheObject() 删除redis缓存，key={}，耗时={}ms",key,time);
			}
		}
	}

	public static String getTargetFromRedisToString(final String key) {
		long start = System.currentTimeMillis();
		try {
			return RedisClientUtils.call(new RedisCall<String>() {
				@Override
				public String run(Jedis jedis) {
					String jsonStr = jedis.get(key);
					if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
						return null;
					}
					return jsonStr;
				}
			});
		} catch (Throwable e) {
			logger.error("getTargetFromRedisToString() 获取redis缓存失败，key = " + key + ", e=", e);
			return null;
		} finally {
			long time = System.currentTimeMillis() - start;
			if(redisExecuteTimeLogEnable(time)){
				logger.info("getTargetFromRedisToString() 获取redis缓存，key={}，耗时={}ms",key,time);
			}
		}
	}

	/**
	 * 将key中的域field的值设为value；如果key不存在，一个新的哈希表被创建并进行HSET操作；如果域field已经存在于哈希表中，旧值将被覆盖。
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public static Boolean hset(final String key, final String field, final String value) {
		long start = System.currentTimeMillis();
		try {
			return RedisClientUtils.call(new RedisCall<Boolean>() {
				@Override
				public Boolean run(Jedis jedis) {
					Long response = jedis.hset(key, field, value);
					return 1 == response;
				}
			});
		} catch (Throwable t) {
			logger.error("hset() 保存redis缓存失败，key = " + key + ",field = " + field + ", 异常=", t);
			return false;
		} finally {
			long time = System.currentTimeMillis() - start;
			if (redisExecuteTimeLogEnable(time)) {
				logger.info("hset() 保存redis缓存，key={}，field={}，耗时={}ms", key, field, time);
			}
		}
	}

	/**
	 * 同时将多个field-value(域-值)对设置到哈希表key中；此命令会覆盖哈希表中已存在的域；如果key不存在，一个空哈希表被创建并执行HMSET操作。
	 * @param key
	 * @param hashmap
	 * @return
	 */
	public static Boolean hmset(final String key, final Map<String,String> hashmap) {
		long start = System.currentTimeMillis();
		try {
			return RedisClientUtils.call(new RedisCall<Boolean>() {
				@Override
				public Boolean run(Jedis jedis) {
					String response = jedis.hmset(key, hashmap);
					return RedisLock.SET_SUCCESS.equals(response);
				}
			});
		} catch (Throwable t) {
			logger.error("hmset() 保存redis缓存失败，key = " + key + ", 异常=", t);
			return false;
		} finally {
			long time = System.currentTimeMillis() - start;
			if (redisExecuteTimeLogEnable(time)) {
				logger.info("hmset() 保存redis缓存，key={}，耗时={}ms", key, time);
			}
		}
	}

	/**
	 * 获取key中的域field的值
	 * @param key
	 * @param field
	 * @return value
	 */
	public static String hget(final String key, final String field) {
		long start = System.currentTimeMillis();
		try {
			return RedisClientUtils.call(new RedisCall<String>() {
				@Override
				public String run(Jedis jedis) {
					return jedis.hget(key, field);
				}
			});
		} catch (Throwable t) {
			logger.error("hget() 获取redis缓存失败，key = " + key + ",field = " + field + ", 异常=", t);
			return null;
		} finally {
			long time = System.currentTimeMillis() - start;
			if (redisExecuteTimeLogEnable(time)) {
				logger.info("hget() 获取redis缓存，key={}，field={}，耗时={}ms", key, field, time);
			}
		}
	}

	/**
	 * 给key设置过期时间expire，入参单位：毫秒
	 * @param key
	 * @param expireMillis
	 * @return
	 */
	public static Boolean expire(final String key, final int expireMillis) {
		long start = System.currentTimeMillis();
		try {
			return RedisClientUtils.call(new RedisCall<Boolean>() {
				@Override
				public Boolean run(Jedis jedis) {
					Long response = jedis.expire(key, expireMillis / 1000);
					return 1 == response;
				}
			});
		} catch (Throwable t) {
			logger.error("expire() 设置redis缓存过期时间失败，key = " + key + ", 异常=", t);
			return false;
		} finally {
			long time = System.currentTimeMillis() - start;
			if (redisExecuteTimeLogEnable(time)) {
				logger.info("expire() 设置redis缓存过期时间，key={}，耗时={}ms", key, time);
			}
		}
	}

	/**
	 * 判断redis读写执行时间日志是否需打印
	 * @param executeMillis 执行毫秒数
	 * @return
	 */
	private static boolean redisExecuteTimeLogEnable(long executeMillis) {
		Long excuteTimeThreshold = CommonUtil.getSysConfigFrom3G(Constant.NCCASHIER_REDIS_TIME_THRESHOLD_KEY);
		if(excuteTimeThreshold==null){
			return false;
		}
		return executeMillis > excuteTimeThreshold;
	}
}
