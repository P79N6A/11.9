package com.yeepay.g3.core.nccashier.dao.impl;

import com.yeepay.g3.core.nccashier.dao.UserRequestInfoDao;
import com.yeepay.g3.core.nccashier.entity.UserRequestInfo;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Created by xiewei on 15-10-21.
 */
@Repository
public class UserRequestInfoDaoImpl extends SqlSessionDaoSupport implements UserRequestInfoDao {

	protected String getStatementId(String name) {
		return "com.yeepay.g3.core.nccashier.entity.UserRequestInfo." + name;
	}

	@Override
	public long saveUserRequestInfo(UserRequestInfo userRequestInfo) {
		this.getSqlSession().insert(getStatementId("insert"), userRequestInfo);
		return 0;
	}

	@Override
	public UserRequestInfo getUserRequestInfoBytoken(String tokenId) {
		UserRequestInfo userRequestInfo= (UserRequestInfo) this.getSqlSession().selectOne(getStatementId("queryByTokenId"), tokenId);
		return userRequestInfo;
	}
}
