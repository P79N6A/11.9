package com.yeepay.g3.core.nccashier.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.dao.UnbindCardDao;
import com.yeepay.g3.core.nccashier.entity.UnbindRecord;

import java.util.List;
import java.util.Map;

@Repository
public class UnbindCardDaoImpl extends SqlSessionDaoSupport implements
		UnbindCardDao {

	@Autowired
	SqlSessionFactory sqlSessionFactory;

	protected String getStatementId(String name) {
		return "com.yeepay.g3.core.nccashier.entity.UnbindRecord." + name;
	}
	@Override
	public UnbindRecord getUnbindRecordByBindId(Long bindId) {
		UnbindRecord unbindRecord = (UnbindRecord) getSqlSession().selectOne(this.getStatementId("getUnbindRecord"), bindId);
		return unbindRecord;
	}

	@Override
	public void create(UnbindRecord unbindRecord) {
		getSqlSession().insert(this.getStatementId("insert"), unbindRecord);
	}

	@Override
	public void update(UnbindRecord unbindRecord) {
		getSqlSession().insert(this.getStatementId("update"), unbindRecord);
	}

	@Override
	public int countUnbindRecord(Map pageParam) {
		return (Integer) getSqlSession().selectOne(getStatementId("countUnbindRecord"), pageParam);
	}

	@Override
	public List<UnbindRecord> listUnbindRecordNotEnctypt(Map pageParam) {
		return getSqlSession().selectList(getStatementId("listUnbindRecordNotEnctypt"), pageParam);
	}

	@Override
	public int updateForEncrypt(UnbindRecord paymentRequest) {
		return getSqlSession().update(getStatementId("updateForEncrypt"), paymentRequest);
	}

	@Override
	public int updateForBatchEncrypt(List<UnbindRecord> list) {
		int rows = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		try {
			for (UnbindRecord unbindRecord : list) {
				int updateRow = sqlSession.update(getStatementId("updateForEncrypt"), unbindRecord);
				rows = rows + updateRow;
			}
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
		return rows;
	}
}
