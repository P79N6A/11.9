package com.yeepay.g3.core.payprocessor.dao;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface PayRecordDao {

	int insert(PayRecord record);

	PayRecord selectByPrimaryKey(String recordNo);


	/**
	 * 将DOING状态的订单修改为成功
	 *
	 * @param record
	 * @return
	 */
	int updateRecordToSuccess(PayRecord record);

	/**
	 * @param recordNo
	 * @param paymentNo
	 * @param originalStatus
	 * @param updateTime
	 * @return
	 */
	int updatePaymentNo(@Param("recordNo") String recordNo, @Param("paymentNo") String paymentNo,
			@Param("originalStatus") String originalStatus, @Param("updateTime") Timestamp updateTime);

	int updateRecordToReverse(PayRecord record);

	/**
	 * @param recordNo
	 * @param newStatus
	 * @param originalStatus
	 * @param updateTime
	 * @return
	 */
	int updatePaymentStatus(@Param("recordNo") String recordNo, @Param("newStatus") String newStatus,
			@Param("originalStatus") List<String> originalStatus, @Param("updateTime") Timestamp updateTime);


	/**
	 * @param recordNo
	 * @param smsState
	 */
	void updateNcPaymentSmsState(@Param("recordNo") String recordNo, @Param("smsState") String smsState);

	/**
	 *
	 * @param recordNo
	 * @param errorCode
	 * @param errorMsg
	 */
	int updatePaymentToFail(@Param("recordNo") String recordNo, @Param("errorCode") String errorCode,
			@Param("errorMsg") String errorMsg);

	/**
	 * 订单成功之后，更新一些银行信息
	 */
	void updatePaymentBankInfo(@Param("payRecord") PayRecord payRecord, @Param("updateTime") Timestamp timestamp);

	/**
	 * 查询未支付订单
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	List<PayRecord> queryDoingRecord(@Param("startDate") Date start, @Param("endDate") Date end , @Param("maxRowCount") int maxRowCount);

	/**
	 * 查询支付次数
	 * 
	 * @param requestId
	 * @return
	 */
	int queryPayTimes(@Param("requestId") Long requestId);

	String queryStatus(@Param("recordNo") String recordNo);

	/**
	 * @param bindId
	 * @param recordNo
	 */
	int updateBindId(@Param("bindId") String bindId, @Param("recordNo") String recordNo);

	/**
	 * 查询ncpay支付订单号
	 * @param recordNo
	 * @return
	 */
	String queryNcPaymentNo(@Param("recordNo") String recordNo);

	/**
	 * @param recordNo
	 * @param errorCode
	 * @param errorMsg
	 */
	int updateRecordErrorInfo(@Param("recordNo") String recordNo, @Param("errorCode")String errorCode, @Param("errorMsg")String errorMsg,@Param("updateTime") Timestamp timestamp);


	/**
	 * 预授权 更新子表为成功或失败状态
	 * @param payRecord
	 * @return
	 */
	int updateRecordForPreAuth(PayRecord payRecord);

	/**
	 * 预授权 更新子表为冲正状态
	 * @param record
	 * @return
	 */
	int updateRecordToReverseForPreAuth(PayRecord record);

	/**
	 * 预授权 强制更新子表状态
	 * @param payRecord
	 * @return
	 */
	int updateRecordFromFailToSuccess(PayRecord payRecord);

	/**
	 * 更新第一支付金额
	 * @param payRecord
	 * @return
	 */
	int updateFirstAmount(PayRecord payRecord);

	/**
	 * 第一支付单直接更新失败
	 * @param payRecord
	 * @return
	 */
	int updateFailWithComb(PayRecord payRecord);

	/**
	 * 判断预授权完成撤销成功的订单
	 * @param requestId
	 * @return
	 */
	int queryCompleteCancelCount(@Param("requestId") Long requestId);
}