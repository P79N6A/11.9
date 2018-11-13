/**
 * 
 */
package com.yeepay.g3.core.frontend.service;

import java.util.List;

import com.yeepay.g3.core.frontend.entity.PayRecord;

/**
 * @author TML
 *
 */
public interface PayRecordService {

	/**
	 * 创建订单
	 * @param payRecord
	 */
	public void createPayRecord(PayRecord payRecord);
	
	/**
	 * 更新订单表
	 * @param payRecord
	 */
	public void updatePayRecord(PayRecord payRecord);
	
	
	/**
	 * 查询支付记录，通过支付记录流水号
	 * @param recordNo
	 * @return
	 */
//	public PayRecord queryByRecordNo(String recordNo);
	
	/**
	 * 修改支付记录，通过支付记录流水号
	 * @param payRecord
	 */
//	public void updateByRecordNo(PayRecord payRecord);
	
	/**
	 * 生成支付记录流水ID
	 * @return
	 */
	public String generateId(String platformType);

    /**
     * 查询支付记录，通过支付订单号
     * @param orderNo
     * @return
     */
	public List<PayRecord> queryByOrderNo(String orderNo, String platformType);

	/**
	 * 根据订单号查支付次数
	 * @param orderNo
	 * @return
	 */
	int countRecordByOrderNo(String orderNo, String platformType);
}
