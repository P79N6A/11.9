/**
 * 
 */
package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.payprocessor.util.RedisUtil;

import java.math.BigDecimal;

/**
 * @author peile.fan
 *
 */
public class TestRedisUtil {

	public static void main(String[] args) {
//		RedisUtil.pushObjectToRedis("PAYRESULT", "12312313", "READY", 60);

		System.out.println(new BigDecimal(0.09).setScale(2, 4));

		Amount amount = new Amount(2);

	}

}
