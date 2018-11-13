/**
 * 
 */
package com.yeepay.g3.core.payprocessor.util;

import org.junit.Test;

import com.yeepay.g3.core.payprocessor.BaseTest;

/**
 * Description:
 * 
 * @author peile.fan
 * @since:2017年1月10日 下午6:13:10
 */
public class ConstantUtilsTest extends BaseTest {
	
	private ConstantUtils constantUtils;

	@Test
	public void testGetCallBackServiceUrl() throws Exception {
		String callBckUrl = constantUtils.getCallBackServiceUrl("NetPay");
		System.out.println(callBckUrl);
	}

	@Test
	public void testMergeKey() {
		ConstantUtils.mergeSensitiveKey();

		System.out.println(ConstantUtils.getDB_AES_KEY());
	}

}
