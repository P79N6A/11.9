package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

/**
 * 无卡收银台业务流程组装服务 Created by xiewei on 15-10-19.
 */
public interface CashierBusinessBiz {


	/**
	 * 业务系统发起冲正
	 * 
	 * @param reverseRequestDTO
	 * @return
	 * @throws CashierBusinessException
	 */
	public ReverseResponseDTO reversePayOrder(ReverseRequestDTO reverseRequestDTO)
			throws CashierBusinessException;

	/**
	 * 补单接口
	 * 
	 * @param tradeSysOrderId
	 * @param tradeSysNo
	 * @return
	 */
	public OrderNoticeDTO supplementPaymentOrder(String tradeSysOrderId, String tradeSysNo)
			throws CashierBusinessException;

	/**
	 * 回调ncpay的mq通知
	 * 
	 * @return
	 */
	public boolean callBackNcpayMq(PayResultRequestDTO payResultRequestDTO);
}
