
package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;

import java.util.Date;
import java.util.List;


/**
 * @author peile.fan
 *
 */
public interface CombPayRecordService {

	/**
	 * @param recordNo
	 */
	CombPayRecord queryByRecordNo(String recordNo);

	/**
	 * 创建支付记录
	 */
	void add(CombPayRecord combPayRecord);

	/**
	 * 查询非终态订单
	 * @param start
	 * @param end
	 * @param maxRowCount
	 * @return
	 */
	List<CombPayRecord> selectDepositPayRecords (Date start, Date end , int maxRowCount);

}
