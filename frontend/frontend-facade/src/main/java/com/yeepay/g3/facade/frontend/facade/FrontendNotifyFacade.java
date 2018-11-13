package com.yeepay.g3.facade.frontend.facade;

import com.yeepay.g3.facade.frontend.dto.BankNotifyRequestDTO;
import com.yeepay.g3.facade.frontend.dto.BankNotifyResponseDTO;

/**
 * 接收银行子系统回调
 * @author songscorpio
 *
 */
public interface FrontendNotifyFacade {
	/**
	 * 接收回调
	 * 查询订单
	 * 核实关键参数是否匹配（是否存在被篡改的风险）
	 * 更新支付订单
	 * 通知交易系统
	 * 
	 * @param dto
	 * @return
	 */
	public BankNotifyResponseDTO receiveBankNotify(BankNotifyRequestDTO bankNotifyRequestDTO);
}
