package com.yeepay.g3.core.nccashier.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.dao.RealPersonChooseTimesDao;
import com.yeepay.g3.core.nccashier.entity.RealPersonChooseTimes;
/**
 * 持卡人信息选择页展示次数服务
 * @since 2016-11-22
 * @author xueping.ni
 *
 */

@Repository
public class RealPersonChooseTimesDaoImpl extends SqlSessionDaoSupport
		implements RealPersonChooseTimesDao {

	@Autowired
	SqlSessionFactory sqlSessionFactory;

	protected String getStatementId(String name) {
		return "com.yeepay.g3.core.nccashier.entity.RealPersonChooseTimes."
				+ name;
	}

	@Override
	public RealPersonChooseTimes getShowTimesByUserInfo(Map map) {
		RealPersonChooseTimes showTimes = (RealPersonChooseTimes) getSqlSession()
				.selectOne(getStatementId("getShowTimesByUserInfo"), map);
		return showTimes;
	}

	@Override
	public void updateShowTimes(RealPersonChooseTimes showTimes) {
		getSqlSession().update(getStatementId("update"), showTimes);
	}

	@Override
	public void create(RealPersonChooseTimes showTimes) {
		getSqlSession().insert(getStatementId("insert"), showTimes);
	}
	@Override
	public List<RealPersonChooseTimes> getUnChooseShowTimesInfo(Map map) {
		List<RealPersonChooseTimes> showTimes = getSqlSession().selectList(getStatementId("getUnChooseShowTimesInfo"),map);
		return showTimes;
	}

	@Override
	public int countRealPersonChooseTimes(Map pageParam) {
		return (Integer) getSqlSession().selectOne(getStatementId("countRealPersonChooseTimes"), pageParam);
	}

	@Override
	public List<RealPersonChooseTimes> listRealPersonChooseTimesNotEnctypt(Map pageParam) {
		return getSqlSession().selectList(getStatementId("listRealPersonChooseTimesNotEnctypt"), pageParam);	}

	@Override
	public int updateForEncrypt(RealPersonChooseTimes paymentRequest) {
		return getSqlSession().update(getStatementId("updateForEncrypt"), paymentRequest);
	}
	@Override
	public int updateForBatchEncrypt(List<RealPersonChooseTimes> list) {
		int rows = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		try {
			for (RealPersonChooseTimes realPersonChooseTimes : list) {
				int updateRow = sqlSession.update(getStatementId("updateForEncrypt"), realPersonChooseTimes);
				rows = rows + updateRow;
			}
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
		return rows;
	}
}
