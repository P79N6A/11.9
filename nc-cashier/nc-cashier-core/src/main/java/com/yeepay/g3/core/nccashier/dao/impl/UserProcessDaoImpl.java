package com.yeepay.g3.core.nccashier.dao.impl;

import java.util.Date;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.yeepay.g3.core.nccashier.dao.UserProcessDao;
import com.yeepay.g3.core.nccashier.entity.UserAccount;
import com.yeepay.utils.jdbc.dal.analyzer.sql.OperatorTypeEnum;
import com.yeepay.utils.jdbc.dal.routing.DALCondition;

/**
 * Created by xiewei on 15-10-21.
 */
@Repository
public class UserProcessDaoImpl extends SqlSessionDaoSupport implements UserProcessDao {

	protected String getStatementId(String name) {
		return "com.yeepay.g3.core.nccashier.entity.UserAccount." + name;
	}
	@Override
	public long saveUserAccount(UserAccount userAccount) {
		this.getSqlSession().insert(getStatementId("insert"), userAccount);
		long id = userAccount.getId();
		return id;
	}

	@Override
	public UserAccount getUserAccountBytoken(Map map) {
		//用于分表后routing
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, new Date());
		UserAccount userAccount= (UserAccount) this.getSqlSession().selectOne(getStatementId("findUserAccountByToken"), map);
		return userAccount;
	}

	@Override
	public void updateUserAccountRecordId(UserAccount userAccount) {
		//用于分表后routing
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, new Date());
		this.getSqlSession().update(getStatementId("updateRecordId"),userAccount);
	}
}
