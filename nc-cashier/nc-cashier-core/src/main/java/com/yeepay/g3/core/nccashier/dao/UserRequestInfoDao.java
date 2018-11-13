package com.yeepay.g3.core.nccashier.dao;

import com.yeepay.g3.core.nccashier.entity.UserRequestInfo;

/**
 * Created by xiewei on 15-10-21.
 */
public interface UserRequestInfoDao {
	public long saveUserRequestInfo(UserRequestInfo userRequestInfo);

	public UserRequestInfo getUserRequestInfoBytoken(String tokenId);
}
