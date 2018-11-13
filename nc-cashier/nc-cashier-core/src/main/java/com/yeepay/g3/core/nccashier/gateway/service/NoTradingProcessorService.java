
package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.facade.nop.dto.AuthBindCardConfirmRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardConfirmResponseDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardResponseDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardSmsRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardSmsResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryCardBinRequestDTO;
import com.yeepay.g3.facade.nop.dto.QueryCardbinResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryOrderRequestDTO;
import com.yeepay.g3.facade.nop.dto.QueryOrderResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryProductStatusReponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryProductStatusRequestDTO;

/**
 * 
 * @Description 调用无交易订单处理器service
 * @author yangmin.peng
 * @since 2017年8月25日上午11:17:47
 */
public interface NoTradingProcessorService {
	/**
	 * NOP-查询卡bin接口
	 */
	QueryCardbinResponseDTO queryCardBin(QueryCardBinRequestDTO queryCardBinRequestDTO);

	/**
	 * NOP-查询绑卡产品开通接口
	 */
	QueryProductStatusReponseDTO queryProductStatus(QueryProductStatusRequestDTO queryProductStatusRequestDTO);
	/**
	 * NOP-鉴权绑卡请求接口
	 */
	AuthBindCardResponseDTO authBindCardRequest(AuthBindCardRequestDTO authBindCardRequestDTO);
	/**
	 * NOP-短验确认接口
	 */
	AuthBindCardConfirmResponseDTO authBindCardConfirm(AuthBindCardConfirmRequestDTO authBindCardConfirmRequestDTO);
	/**
	 * NOP-短验重发接口
	 */
	AuthBindCardSmsResponseDTO authBindCardSms(AuthBindCardSmsRequestDTO authBindCardSmsRequestDTO);
	/**
	 * NOP-查询无交易订单接口
	 */
	QueryOrderResponseDTO queryOrder(QueryOrderRequestDTO queryOrderRequestDTO);	
}
