package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentSmsRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationIdOrderRequestDTO;

/**
 * 银行卡分期facade，主要是为了H5收银台和PC收银台服务
 * 
 * @author duangduang
 *
 */
public interface BankInstallmentFacade {

	/**
	 * 银行卡分期路由接口：若有签约关系列表，返回签约关系列表（区分可用与否）
	 * 
	 * @param requestId
	 * @return
	 */
	InstallmentRouteResponseDTO routePayWay(long requestId);

	/**
	 * 获取某银行某期的手续费等金额信息
	 * 
	 * @param requestDTO
	 * @return
	 */
	InstallmentFeeInfoResponseDTO getInstallmentFeeInfo(InstallmentFeeInfoRequestDTO requestDTO);

	/**
	 * 签约场景，卡号下单接口（卡号未签约，走首次开通并支付）
	 * 
	 * @param cardNoOrderRequestDTO
	 * @return 首次开通并支付场景，返回url信息；签约场景，不返回url信息；
	 */
	CardNoOrderResponseDTO orderByCardNo(CardNoOrderRequestDTO cardNoOrderRequestDTO);

	/**
	 * 签约场景，签约关系ID下单接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	BasicResponseDTO orderBySignRelationId(SignRelationIdOrderRequestDTO requestDTO);

	/**
	 * 签约场景，发短验接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	BasicResponseDTO sendSms(InstallmentSmsRequestDTO requestDTO);

	/**
	 * 签约场景，确认支付接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	BasicResponseDTO confirmPay(InstallmentConfirmRequestDTO requestDTO);
}
