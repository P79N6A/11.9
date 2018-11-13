package com.yeepay.g3.core.nccashier.biz;

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
 * 银行卡分期服务BIZ层，主要服务于H5收银台和PC收银台
 * 
 * @author duangduang
 *
 */
public interface BankInstallmentBiz {

	/**
	 * 银行分期路由；若有签约列表，则返回签约列表（区分可用和不可用）；
	 * 
	 * @param requestId
	 * @return
	 */
	InstallmentRouteResponseDTO routePayWay(long requestId);

	/**
	 * 获取银行卡分期的手续费、补贴后的手续费、首期还款额、每期还款额及订单金额
	 * 
	 * @param requestDTO
	 * @return
	 */
	InstallmentFeeInfoResponseDTO getInstallmentFeeInfo(InstallmentFeeInfoRequestDTO requestDTO);

	/**
	 * 卡号下单接口
	 * 
	 * @param cardNoOrderRequestDTO
	 * @return
	 */
	CardNoOrderResponseDTO orderByCardNo(CardNoOrderRequestDTO cardNoOrderRequestDTO);

	/**
	 * 签约关系ID下单接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	BasicResponseDTO orderBySignRelationId(SignRelationIdOrderRequestDTO requestDTO);

	/**
	 * 发短验接口
	 * 
	 * @param requestId
	 * @param recordId
	 * @return
	 */
	BasicResponseDTO sendSms(InstallmentSmsRequestDTO requestDTO);

	/**
	 * 确认支付接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	BasicResponseDTO confirmPay(InstallmentConfirmRequestDTO requestDTO);
}
