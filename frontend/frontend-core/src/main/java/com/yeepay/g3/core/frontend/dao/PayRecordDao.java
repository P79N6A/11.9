/**
 * 
 */
package com.yeepay.g3.core.frontend.dao;

import java.util.List;

import com.yeepay.g3.core.frontend.entity.PayRecord;
import com.yeepay.g3.utils.persistence.GenericDao;

/**
 * 支付记录
 * @author TML
 */
public interface PayRecordDao extends GenericDao<PayRecord> {
	
	/**
	 * 查询支付记录，通过支付记录流水号
	 * @param recordNo
	 * @return
	 */
//	List<PayRecord> queryByRecordNo(String recordNo);
	
	/**
	 * 修改支付记录，通过OrderNo
	 * @param payRecord
	 */
	void update(PayRecord payRecord);
	
	/**
	 * 获取sequence信息
	 */
	long getSeqValue(String platformType);

	/**
	 * 查询支付记录，通过支付订单号
	 * @param orderNO
	 * @return
	 */
	List<PayRecord> queryByOrderNo(String orderNO, String platformType);

	/**
	 * 根据订单号查支付次数
	 * @param orderNo
	 * @return
	 */
	int countRecordByOrderNo(String orderNo, String platformType);
}
