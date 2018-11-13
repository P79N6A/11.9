/**
 * 
 */
package com.yeepay.g3.core.payprocessor.external.service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.facade.frontend.dto.NetPayResultMessage;
import com.yeepay.g3.facade.payprocessor.dto.CflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.CflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayResponseDTO;

/**
 * @author zhen.tan 微信pay服务
 *
 */
public interface FrontEndService {

	/**
	 * FE开放支付接口
	 * @param requestDTO
	 * @return
	 */
	OpenPayResponseDTO openPay(OpenPayRequestDTO requestDTO, PayRecord payRecord);

	/**
	 * FE网银支付接口
	 * @param requestDTO
	 * @return
	 */
	NetPayResponseDTO onlinePay(NetPayRequestDTO requestDTO, PayRecord payRecord);

	/**
	 * 查询FE支付结果
	 * @param recordNo
	 * @param platformType
	 * @return
	 */
	FrontendQueryResponseDTO queryPaymentOrder(String recordNo, String platformType);

	/**调用FE预路由接口
	 * @param requestDTO
	 * @return
	 */
	OpenPrePayResponseDTO openPrePay(OpenPrePayRequestDTO requestDTO);

	/**
	 * 调用FE分期支付接口
	 * @return
	 */
	CflPayResponseDTO cflPay(CflPayRequestDTO requestDTO, PayRecord payRecord);
	
	/**
	 * 查询FE 网银支付结果
	 * @param recordNo
	 * @return
	 */
	NetPayResultMessage queryNetPayPaymentOrder(String recordNo);

	/**
	 * 查询FE 分期支付结果
	 * @param recordNo
	 * @return
	 */
	InstallmentResultMessage queryCflPayPaymentOrder(String recordNo);

	/**
	 * 调用FE分期主扫接口
	 */
	PassiveScanPayResponseDTO passiveScanPay(PassiveScanPayRequestDTO requestDTO, PayRecord payRecord);


}
