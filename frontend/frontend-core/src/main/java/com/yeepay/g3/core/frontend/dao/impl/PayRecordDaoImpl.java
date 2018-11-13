/**
 * 
 */
package com.yeepay.g3.core.frontend.dao.impl;

import java.util.List;

import com.yeepay.g3.core.frontend.dao.PayRecordDao;
import com.yeepay.g3.core.frontend.entity.PayRecord;
import com.yeepay.g3.utils.persistence.mybatis.GenericDaoDefault;
import com.yeepay.utils.jdbc.dal.analyzer.sql.OperatorTypeEnum;
import com.yeepay.utils.jdbc.dal.routing.DALCondition;

import org.springframework.stereotype.Repository;

/**
 * @author TML
 *
 */
@Repository
public class PayRecordDaoImpl extends GenericDaoDefault<PayRecord> implements PayRecordDao{

//	@Override
//	public List<PayRecord> queryByRecordNo(String recordNo) {
//		if(StringUtils.isNotBlank(recordNo)){
//			return (List<PayRecord>)this.query("queryByRecordNo", recordNo);
//		}else{
//			return null;
//		}
//	}

	@Override
	public void update(PayRecord payRecord) {
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, payRecord.getPlatformType());
		this.update("update", payRecord);
	}
	
	/**
	 * 获取sequence值
	 * @return
	 */
	public long getSeqValue(String platformType){
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
		Long seqId = (Long)this.queryOne("nextValueSeq");
		return seqId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PayRecord> queryByOrderNo(String orderNo, String platformType) {
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
		return (List<PayRecord>)this.query("queryByOrderNo", orderNo);
	}

	@Override
	public int countRecordByOrderNo(String orderNo, String platformType) {
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
		Integer count = (Integer) this.queryOne("countRecordByOrderNo",orderNo);
		return count;
	}
	
}
