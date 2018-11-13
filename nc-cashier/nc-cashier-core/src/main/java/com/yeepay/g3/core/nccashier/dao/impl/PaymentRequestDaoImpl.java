package com.yeepay.g3.core.nccashier.dao.impl;

import com.yeepay.g3.core.nccashier.dao.PaymentRequestDao;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.utils.common.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentRequestDaoImpl extends SqlSessionDaoSupport implements PaymentRequestDao {

	@Autowired
	SqlSessionFactory sqlSessionFactory;

	protected String getStatementId(String name) {
		return "com.yeepay.g3.core.nccashier.entity.PaymentRequest." + name;
	}
	
	@Override
	public long savePaymentRequest(PaymentRequest paymentRequest) {
		getSqlSession().insert(getStatementId("insert"), paymentRequest);
		long id = paymentRequest.getId();
		return id;
	}

	@Override
	public PaymentRequest findPayRequestById(long id) {
		PaymentRequest pr= (PaymentRequest) this.getSqlSession().selectOne(getStatementId("findPayRequestById"), id);
		return pr;
	}

	@Override
	public int updatePayRequest(PaymentRequest paymentRequest) {
		return getSqlSession().update(getStatementId("updatePayRequestState"), paymentRequest);
    }

	@Override
	public PaymentRequest findPayRequestByTradeSysOrderId(Map map) {
		PaymentRequest pr = (PaymentRequest)getSqlSession().selectOne(getStatementId("findRequestByTradeSysOrderId"), map);
		return pr;
	}

	@Override
	public PaymentRequest findPayRequestByOrderOrderId(Map map) {
		PaymentRequest pr =  null;
		List<PaymentRequest> paymentRequestList = getSqlSession().selectList(getStatementId("findRequestByOrderOrderId"), map);
		if(CollectionUtils.isNotEmpty(paymentRequestList)){
			pr = paymentRequestList.get(0);
		}
		return pr;
	}

	@Override
	public int updatePayRequestExtendInfoById(PaymentRequest paymentRequest) {
		return getSqlSession().update(getStatementId("updatePayRequestExtendInfoById"),paymentRequest);
	}

	@Override
	public int updateUserRequestInfo(PaymentRequest paymentRequest) {
		return getSqlSession().update(getStatementId("updateUserRequestInfo"),paymentRequest);
	}

	@Override
	public int updateRequestBaseOnStatus(PaymentRequest paymentRequest, List<String> statusList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("paymentRequest", paymentRequest);
		param.put("originalStatus", statusList);
		return getSqlSession().update(getStatementId("updateRequestBaseOnStatus"), param);

	}

	@Override
	public List<PaymentRequest> findRequestListByMerchantNoAndMerchantOrderId(Map map) {
		return getSqlSession().selectList(getStatementId("findRequestsByMerchantOrderId"), map);
	}

    @Override
    public int countPaymentRequest(Map pageParam) {
		return (Integer) getSqlSession().selectOne(getStatementId("countPaymentRequest"), pageParam);
	}

    @Override
	public List<PaymentRequest> listPaymentRequestNotEnctypt(Map pageParam) {
		return getSqlSession().selectList(getStatementId("listPaymentRequestNotEnctypt"), pageParam);
	}

	@Override
	public int updateForEncrypt(PaymentRequest paymentRequest) {
		return getSqlSession().update(getStatementId("updateForEncrypt"), paymentRequest);
	}

	@Override
	public int updateForBatchEncrypt(List<PaymentRequest> paymentRequests) {
		int rows = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		try {
			for(PaymentRequest paymentRequest:paymentRequests) {
				int updateRow = sqlSession.update(getStatementId("updateForEncrypt"), paymentRequest);
				rows = rows + updateRow;
			}
			sqlSession.commit();
		}finally {
			sqlSession.close();
		}
		return rows;
	}
}
