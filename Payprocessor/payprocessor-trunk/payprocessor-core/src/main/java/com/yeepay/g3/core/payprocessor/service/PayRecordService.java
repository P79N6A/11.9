
package com.yeepay.g3.core.payprocessor.service;

import java.util.Date;
import java.util.List;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @author peile.fan
 *
 */
public interface PayRecordService {

	/**
	 * @param recordNo
	 */
	PayRecord queryRecordById(String recordNo);

	PayRecord queryRecordByRecord(String recordNo);

	/**
	 * 创建支付记录
	 * 
	 * @param payRecord
	 */
	void createPayRecord(PayRecord payRecord);


	/**
	 * 更新支付记录表的paymentNo
	 * 
	 * @param recordNo
	 * @param paymentNo
	 * @param originalStatus
	 */
	void updatePaymentNo(String recordNo, String paymentNo, String originalStatus);

	/**
	 * 更新支付订单的状态
     * @param recordNo
     * @param newStatus
     * @param originalStatus ：允许的原始状态集合
     */
	int updatePaymentStatus(String recordNo, String newStatus, List<String> originalStatus);

	/**
	 * @param recordNo
	 * @param name
	 */
	void updateNcPaymentSmsState(String recordNo, String name);

	/**
	 * @param recordNo
	 * @param errorCode
	 * @param errorMsg
	 */
	void updateNcPaymentToFail(String recordNo, String errorCode, String errorMsg);

	/**
	 * 查询未支付订单
	 * @param start
	 * @param end
	 * @return
	 */
	List<PayRecord> queryDoingRecord(Date start, Date end , int maxRowCount);

	/**
	 * 查询支付次数
	 * @param requestId
	 * @return
	 */
	int queryPayTimes(Long requestId);
	
	/**
	 * 判断支付订单是否存在
	 * @param recordNo
	 * @return 
	 */
	String queryNcPaymentNo(String recordNo);

	/**
	 *
	 * @param recordNo
	 * @param errorCode
	 * @param errorMsg
	 */
	void updateRecordErrorInfo(String recordNo,String errorCode, String errorMsg);

	/**
	 * 更新支付订单为失败
	 * @param recordNo
	 * @param errorCode
	 * @param errorMsg
	 */
	void updatePaymentToFail(String recordNo,String errorCode,String errorMsg);

	/**
	 * 判断预授权完成撤销成功的订单
	 * @param requestId
	 * @return
	 */
	int queryCompleteCancelCount(Long requestId);
}
