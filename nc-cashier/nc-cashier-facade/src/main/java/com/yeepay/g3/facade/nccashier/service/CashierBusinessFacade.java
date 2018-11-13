package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

/**
 * 无卡收银台核心处理服务
 * @author chengjunchen
 *
 */
public interface CashierBusinessFacade {

	/**
	 * 冲正接口
	 * @param reverseRequestDTO
	 * @return
	 */
	public ReverseResponseDTO reversePayOrder(ReverseRequestDTO reverseRequestDTO)throws CashierBusinessException;
	/**
	 * 补单接口
	 * @return
	 */
	public OrderNoticeDTO supplementPaymentOrder(String tradeSysOrderId,String tradeSysNo) throws CashierBusinessException;

	
	/**
	 * 回调ncpay mq确认接口，支付成功和支付失败都要回调。
	 */
	public void callBackNccashierMq(PayResultRequestDTO payResultRequestDTO);
}
