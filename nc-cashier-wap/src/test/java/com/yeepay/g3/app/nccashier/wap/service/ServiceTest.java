package com.yeepay.g3.app.nccashier.wap.service;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.yeepay.g3.app.nccashier.wap.base.BaseTest;
import com.yeepay.g3.app.nccashier.wap.utils.WaChatPayUtils;

public class ServiceTest extends BaseTest {

	@Test
	public void testService(){

		System.out.println("test");
	}
	
	@Test
	public void testGetJumpUrl() throws UnsupportedEncodingException{
		System.out.println(WaChatPayUtils.buildWechatH5JumpUrl("https://shouyin.yeepay.com/nc-cashier-wap/newwap/request?token=d8041064-1da4-4ede-991b-34c3dc60658e&requestId=5FQbznt7bGQX79MD8I6VKA==&merchantNo=10000470992",
				"1233333","qwer12333"));
	}
	
	@Test
	public void getLock(){
		System.out.println(WaChatPayUtils.getReentrantLock("12333321","1234"));
		System.out.println(WaChatPayUtils.getReentrantLock("12333321","1235"));
		WaChatPayUtils.releaseLock("12333321");
		System.out.println(WaChatPayUtils.getReentrantLock("12333321","1235"));
		System.out.println(WaChatPayUtils.getReentrantLock("12333321","1234"));

	}
	
	@Test
	public void relesaeLock(){
		WaChatPayUtils.releaseLock("1233333");

	}
	
}
