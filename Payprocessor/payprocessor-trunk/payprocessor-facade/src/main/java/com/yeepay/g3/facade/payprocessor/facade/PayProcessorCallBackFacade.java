package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.PayCallBackRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayCallBackResponseDTO;

/**
 * @author 支付处理器回调接口
 */
public interface PayProcessorCallBackFacade {
	/**
	 * 支付完成回调接口
	 * @param ConfrimPayCallBackDTO
	 * @return
	 */
	public PayCallBackResponseDTO payCallBack(PayCallBackRequestDTO PayCallBackDTO);
	
}
