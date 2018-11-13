package com.yeepay.g3.core.payprocessor.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.biz.CancelBiz;
import com.yeepay.g3.core.payprocessor.param.CancelRequestParam;
import com.yeepay.g3.core.payprocessor.param.CancelResponse;

/**
 * Description:
 * 
 * @author peile.fan
 * @since:2017年2月7日 下午2:16:51
 */
public class CancelRequestMessageReceiverTest extends BaseTest {
	@Autowired
	private CancelBiz cancelBiz;

	@Test
	public void onMessageTest() throws IllegalAccessException, InvocationTargetException {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("outTradeNo", "14791938442717700001");
		map.put("customerNumber", "10040007800");
		map.put("customerName", "撤销状态测试");
		map.put("orderNo", "14791938442717700001");
		map.put("dealUniqueSerialNo", "14791938442717700001");
		map.put("orderSystem", "DS");
		map.put("amount", "100");
		System.out.println(JSON.toJSONString(map));
		CancelRequestParam param = new CancelRequestParam();
		BeanUtils.populate(param, map);
		CancelResponse response = cancelBiz.cancelRequest(param);
		System.out.println(response);
		
	}

}
