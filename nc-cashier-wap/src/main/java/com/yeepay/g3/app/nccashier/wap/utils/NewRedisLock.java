/** 
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay) 
 */
package com.yeepay.g3.app.nccashier.wap.utils;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.utils.lock.Lock;
import com.yeepay.utils.lock.utils.RedisCall;
import com.yeepay.utils.lock.utils.RedisClientUtils;

/**
 * @author：zhen.tan
 * @since：2018-04-23 上午10:23:21
 * @version:
 */
public class NewRedisLock implements Lock {
	public static final int DEFAULT_EXPIRE_TIME = 10000;
	public static final long DEFAULT_TRY_INTERVAL = 500;
	public static final String SET_SUCCESS = "OK";
	private int expire;
	private String key;
	private String val;
	private long tryInterval;
	private static final Log LOG = LogFactory.getLog(RedisClientUtils.class);

	

	public NewRedisLock(String key, int expire,String val) {
		this.key = key;
		this.tryInterval = DEFAULT_TRY_INTERVAL;

		if(StringUtils.isBlank(val)){
			this.val = UUID.randomUUID().toString();
		}else{
			this.val = val;
		}
		
		if (expire > 0) {
			this.expire = expire * 1000;
		} else {
			this.expire = DEFAULT_EXPIRE_TIME;
		}
	}

//	public NewRedisLock(String key, int expire, long tryInterval) {
//		this.key = key;
//		this.val = UUID.randomUUID().toString();
//		if (expire > 0) {
//			this.expire = expire * 1000;
//		} else {
//			this.expire = DEFAULT_EXPIRE_TIME;
//		}
//		if (tryInterval > 0) {
//			this.tryInterval = DEFAULT_TRY_INTERVAL;
//		} else {
//			this.tryInterval = DEFAULT_TRY_INTERVAL;
//		}
//	}

	/**
	 * setNx 且设置超时时间
	 * 
	 * @param key
	 * @param val
	 * @param expireTime
	 * @return
	 */
	private boolean setNx(final String key, final String val,
			final int expireTime) {
		return RedisClientUtils.call(new RedisCall<Boolean>() {
			@Override
			public Boolean run(Jedis jedis) {
				if (RedisClientUtils.isNewVersion()) {
					String response = jedis.set(key, val, "NX", "PX",
							expireTime);
					return SET_SUCCESS.equals(response);
				} else {
					if (jedis.setnx(key, val) == 1) {
						jedis.expire(key, expireTime / 1000);
						return true;
					}
					return false;
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yeepay.utils.lock.DistributeLock#lock()
	 */
	@Override
	public boolean lock() {
		return RedisClientUtils.call(new RedisCall<Boolean>() {
			@Override
			public Boolean run(Jedis jedis) {
				if (setNx(key, val, expire)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("get lock success ,key=" + key
								+ ", expire seconds=" + expire);
					}
					return true;
				} else {
					LOG.debug("get lock fail, key=" + key);
					return false;
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yeepay.utils.lock.DistributeLock#tryLock(int)
	 */
	@Override
	public boolean tryLock(final int timeout) {
		long tryTime = System.currentTimeMillis() + timeout * 1000L;
		while (System.currentTimeMillis() < tryTime) {
			if (setNx(key, val, expire)) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("get lock success ,key=" + key
							+ ", expire seconds=" + expire);
				}
				return true;
			} else {
				LOG.debug("get lock fail, key=" + key);
			}
			try {
				TimeUnit.MILLISECONDS.sleep(tryInterval);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yeepay.utils.lock.DistributeLock#unlock()
	 */
	@Override
	public void unlock() {
		RedisClientUtils.call(new RedisCall<Boolean>() {
			@Override
			public Boolean run(Jedis jedis) {
				jedis.eval(
						"if redis.call('get',KEYS[1]) == ARGV[1] then \n return redis.call('del',KEYS[1]) \n else return 0 \n end",
						Arrays.asList(key), Arrays.asList(val));
				return true;
			}
		});
	}
}
