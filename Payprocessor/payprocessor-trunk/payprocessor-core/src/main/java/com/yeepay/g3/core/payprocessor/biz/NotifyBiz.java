package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.facade.payprocessor.dto.OperationRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OperationResponseDTO;

/**
 * 通知业务处理
 * 
 * @author chronos.
 * @createDate 2016/11/16.
 */
public interface NotifyBiz {

	/**
	 * 定时补偿7天内成功但未通知订单处理器的订单
	 */
	void reNotify();

	/**
	 * 批量补发通知
	 * @param requestDTO
	 * @return
	 */
	OperationResponseDTO batchReNotify(OperationRequestDTO requestDTO);

}
