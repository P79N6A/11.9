/**
 * 
 */
package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.BindNeedItemInfo;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderResponseDTO;

/**
 * 创建支付订单相关service
 * @author zhen.tan
 * @since：2016年5月25日 下午6:27:39
 */
public interface OrderPaymentService {

	/**
	 * 校验绑卡支付业务信息
	 * @param requestDto
	 * @return
	 */
	public CombinedPaymentDTO validateBindPayBusinInfo(CashierPaymentRequestDTO requestDto) throws CashierBusinessException;
	
	/**
	 * 校验首次支付业务信息
	 * @param requestDto
	 * @return
	 */
	public CombinedPaymentDTO validateFirstPayBusinInfo(CashierPaymentRequestDTO requestDto) throws CashierBusinessException;
	
	/**
	 * 创建绑卡支付订单
	 * @param requestDto
	 * @param combinedPaymentDto
	 */
	public void doBindtPayCreatePayment(CashierPaymentRequestDTO requestDto,CombinedPaymentDTO combinedPaymentDto) throws CashierBusinessException;
	
	/**
	 * 创建首次支付订单
	 * @param requestDto
	 * @param combinedPaymentDto
	 */
	void doFirsttPayCreatePayment(CashierPaymentRequestDTO requestDto, CombinedPaymentDTO combinedPaymentDto)throws CashierBusinessException ;
	
	/**
	 * 调用ncpay下单
	 * @param requestDto
	 * @param combinedPaymentDto
	 * @param cardInfoType
	 * @return
	 */
	PaymentResponseDTO callNcPayOrder(CashierPaymentRequestDTO requestDto,CombinedPaymentDTO combinedPaymentDto,CardInfoTypeEnum cardInfoType);
	
	/**
	 * 补充首次下单结果
	 * @param paymentResponseDTO
	 * @param response
	 * 
	 */
	public void supplyFirstOrderResult(PaymentResponseDTO paymentResponseDTO,CashierPaymentResponseDTO response,CashierPaymentRequestDTO paymentRequestDto);
	
	/**
	 * 补充绑卡下单结果
	 * @param requestDto
	 * @param paymentResponseDTO
	 * @param response
	 * @param paymentRequest
	 */
	public void supplyBindOrderResult(CashierPaymentRequestDTO requestDto, PaymentResponseDTO paymentResponseDTO, CashierPaymentResponseDTO response, PaymentRequest paymentRequest);

	/**
	 * 获取卡信息，并处理补充项
	 * @param bindId
	 * @param needItem
	 * @param paymentRequest
	 * @return
	 */
	NeedBankCardDTO getBindCardAndNeedSupplement(long bindId, int needItem, PaymentRequest paymentRequest);

	/**
	 * 调用ncpay发送短验
	 * @param payorderId
	 * @param smsRequest
	 * @param tradeSysNo
	 */
	void verifyAndSendSms(String payorderId, CashierSmsSendRequestDTO smsRequest, CardInfoDTO cardInfoDTO, String tradeSysNo, String paymentSysCode);
	
	/**
	 * 校验发短验业务信息
	 * @param smsRequest
	 * @return
	 */
	public String validateSmsBusinInfo(CashierSmsSendRequestDTO smsRequest);
	
	void supplyBindPayOrderResult(CashierPaymentRequestDTO paymentRequestDto, PaymentResponseDTO paymentResponseDTO, FirstBindCardPayResponseDTO response, PaymentRequest paymentRequest, NeedBankCardDTO needItemByUserInput);
	
	BindNeedItemInfo getNeedItemAndSmsType(CashierPaymentRequestDTO requestDto, PaymentResponseDTO paymentResponseDTO, CashierPaymentResponseDTO response, PaymentRequest paymentRequest);
	
	CombinedPaymentDTO buildCombinedPaymentDTO(PaymentRequest paymentRequest, PaymentRecord paymentRecord);

	/**
	 * 保存临时卡信息
	 * @param cardInfoDTO
	 * @param payRequest
	 * @param bindLimitInfoResDTO
	 * @return
	 */
	long addPayTmpCard(CardInfoDTO cardInfoDTO, PaymentRequest payRequest, BindLimitInfoResDTO bindLimitInfoResDTO);

	/**
	 * 保存临时卡信息
	 * 
	 * @param cardInfoDTO
	 * @return
	 */
	long addPayTmpCard(CardInfoDTO cardInfoDTO);
	
	/**
	 * 封装参数，并调用pp一键支付下单接口，目前供API收银台一键支付使用
	 * @param paymentRequest
	 * @param payRecord
	 * @param cardInfoType
	 */
	NcPayOrderResponseDTO payProcessorRequestOrder(PaymentRequest paymentRequest, PaymentRecord payRecord, CardInfoTypeEnum cardInfoType);
}
