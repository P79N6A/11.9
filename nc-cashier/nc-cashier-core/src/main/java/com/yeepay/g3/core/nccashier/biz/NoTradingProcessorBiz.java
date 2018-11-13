package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryBindCardOpenStatusResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderResponseDTO;

/**
 * 
 * @Description 无交易订单处理器facade
 * @author yangmin.peng
 * @since 2017年8月22日下午6:18:40
 */
public interface NoTradingProcessorBiz {
	
	NOPCardBinResponseDTO getNopCardBinInfo(NOPCardBinRequestDTO cardBinRequestDTO);

	NOPAuthBindResponseDTO authBindCardRequest(NOPAuthBindRequestDTO authBindRequestDTO);

	NOPAuthBindConfirmResponseDTO authBindCardConfirm(NOPAuthBindConfirmRequestDTO authBindConfirmRequest);
	
	NOPAuthBindSMSResponseDTO authBindCardReSendSMS(NOPAuthBindSMSRequestDTO authBindSMSRequest);
	
	NOPQueryOrderResponseDTO queryNopOrderStatus(NOPQueryOrderRequestDTO queryOrderRequest);
	
	NOPQueryBindCardOpenStatusResponseDTO getNopBindCardOpenStatus(String merchantNo);
	
}
