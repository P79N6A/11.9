package com.yeepay.g3.core.nccashier.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.ParamShowInfo;
import com.yeepay.g3.core.nccashier.entity.UserAccount;

public class UserProcessDaoTest extends BaseTest {
	@Autowired
	private UserProcessDao userProcessDao;

	@Test
	public void testGetUserAccountBytoken() {
		Map map = new HashMap();
		map.put("tokenId", "563b8fb8-301a-43fc-9766-b1b1390ad10b");
		UserAccount userAccount = userProcessDao.getUserAccountBytoken(map);
		System.out.println(JSON.toJSONString(userAccount));
	}
	
	@Test
	public void testUpdateUserAccountBytoken() {
		UserAccount userAccount = new UserAccount();
		userAccount.setTokenId("0d0ee087-db9b-49aa-8460-56e83b215ffe");
		userAccount.setPaymentRecordId("10002");
		userAccount.setUpdateTime(new Date());
		userProcessDao.updateUserAccountRecordId(userAccount);
		System.out.println(JSON.toJSONString(userAccount));
	}

	@Test
	public void testAdd() {
		UserAccount uaccount = new UserAccount();
		uaccount.setTokenId(UUID.randomUUID().toString());
		uaccount.setPaymentRequestId(10001);
		uaccount.setParamShowInfo(new ParamShowInfo());
		uaccount.setCreateTime(new Date());
		uaccount.setUpdateTime(new Date());
		logger.info("要保存的userInfo:" + uaccount);
		userProcessDao.saveUserAccount(uaccount);
	}
}
