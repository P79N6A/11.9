package com.yeepay.g3.core.nccashier.dao;

import java.util.Map;

import com.yeepay.g3.core.nccashier.entity.UserAccount;

/**
 * Created by xiewei on 15-10-21.
 */
public interface UserProcessDao {
	public long saveUserAccount(UserAccount userAccount);

	public UserAccount getUserAccountBytoken(Map map);

	public void updateUserAccountRecordId(UserAccount userAccount);
}
