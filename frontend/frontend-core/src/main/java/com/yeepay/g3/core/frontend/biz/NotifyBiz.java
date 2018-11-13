/**
 * 
 */
package com.yeepay.g3.core.frontend.biz;

import com.yeepay.g3.facade.frontend.dto.BankNotifyRequestDTO;
import com.yeepay.g3.facade.frontend.dto.BankNotifyResponseDTO;
import com.yeepay.g3.facade.frontend.dto.FeOperationRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FeOperationResponseDTO;

import java.util.Date;

/**
 * 回调通知biz
 * @author TML
 */
public interface NotifyBiz {

	/**
	 * 通知回调处理
	 * @param bankNotifyRequestDTO
	 * @return
	 */
	BankNotifyResponseDTO receiveBankNotify(BankNotifyRequestDTO bankNotifyRequestDTO);

	/**
	 * 通知业务方
	 * @param requestDTO
	 *
	 */
	FeOperationResponseDTO notifyOrders(FeOperationRequestDTO requestDTO);

	/**
	 * 通知业务方
	 */
	void notifyMqByDate(Date start,Date end, String platformType);
}
