package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.CflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.CflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflConfirmResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflOpenRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflOpenResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflOrderRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflOrderResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflSmsRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflSmsResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayConfirmResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcSmsRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcSmsResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordQueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCancelRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCancelResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCompleteRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCompleteResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.ResponseStatusDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * @author zhen.tan
 *
 */
@Service
public class PayProcessorServiceImpl extends NcCashierBaseService implements PayProcessorService {

	private static Logger logger = LoggerFactory.getLogger(PayProcessorService.class);
	
	@Override
	public NcPayOrderResponseDTO ncPayRequest(NcPayOrderRequestDTO requesDTO){
		NcPayOrderResponseDTO responseDTO = null;
		try {
			responseDTO = payOrderFacade.ncRequest(requesDTO);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public NcSmsResponseDTO verifyAndSendSms(NcSmsRequestDTO requestDTO) {
		
		NcSmsResponseDTO responseDTO = null;	
		try {
			responseDTO = payManageFacade.sendSms(requestDTO);	
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public NcPayConfirmResponseDTO confirmPay(NcPayConfirmRequestDTO confirmDTO) {
		NcPayConfirmResponseDTO responseDTO = null;
		try {
			responseDTO = payManageFacade.confirmPay(confirmDTO);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public OpenPayResponseDTO openPayRequest(OpenPayRequestDTO requestDTO) {
		OpenPayResponseDTO responseDTO = null;
		try{
			responseDTO = payOrderFacade.openRequest(requestDTO);
		} catch (Throwable e) {
			
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public NetPayResponseDTO onlinePayRequest(NetPayRequestDTO requestDTO) {
		NetPayResponseDTO responseDTO = null;
		try {
			responseDTO = payOrderFacade.onlineRequest(requestDTO);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		
		handleException(responseDTO);
		return responseDTO;
	}
	
	public PayRecordResponseDTO query(PayRecordQueryRequestDTO requestDTO){
		PayRecordResponseDTO responseDTO = null;
		try {
			responseDTO = payRecordQueryFacade.query(requestDTO);
		} catch (Exception e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		
		return responseDTO;
	}

	@Override
	public OpenPrePayResponseDTO payOrderPrePay(OpenPrePayRequestDTO openPrePayRequestDTO) {
		OpenPrePayResponseDTO responseDTO;
		try {
			responseDTO = payOrderFacade.openPrePay(openPrePayRequestDTO);
		} catch (Exception e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}
	

	private void handleException(ResponseStatusDTO responseDTO){
		if(responseDTO== null ){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}else if(responseDTO.getProcessStatus() == ProcessStatus.FAILED){
			throw CommonUtil.handleException(SysCodeEnum.PP.name(), responseDTO.getResponseCode(), 
				responseDTO.getResponseMsg());
		}
	}
	
	/**
	 * 当PP的确认支付接口返回实体为PayRecordResponseDTO，对其进行处理。
	 * 目前有快捷、银行卡分期和预授权的确认支付接口使用到该方法
	 * 
	 * @param responseDTO
	 */
	private void handleExceptionForSynConfirm(PayRecordResponseDTO responseDTO) {
		if (responseDTO == null) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleExceptionForSyncConfirm(responseDTO, responseDTO.getTrxStatus());
	}
	
	private void handleExceptionForSyncConfirm(ResponseStatusDTO responseStatusDTO, TrxStatusEnum trxStatus) {
		if (responseStatusDTO.getProcessStatus() == ProcessStatus.SUCCESS) {
			// 受理成功，且…
			if (trxStatus == TrxStatusEnum.SUCCESS) {
				// 支付成功
				return;
			} else if (trxStatus == TrxStatusEnum.DOING) {
				// 支付结果未知
				throw CommonUtil.handleException(Errors.ORDER_STATUS_UNKNOWN);
			} else if (trxStatus == TrxStatusEnum.REVERSE) {
				// 支付结果为冲正
				throw CommonUtil.handleException(Errors.ORDER_STATUS_REVERSE);
			}
		}
		// 受理失败，或支付失败
		throw CommonUtil.handleException(SysCodeEnum.PP.name(), responseStatusDTO.getResponseCode(),
				responseStatusDTO.getResponseMsg());
	}

	@Override
	public CflPayResponseDTO cflRequest(CflPayRequestDTO requestDTO) {
		CflPayResponseDTO cflPayResponseDTO =null;
		try{
			cflPayResponseDTO = payOrderFacade.cflRequest(requestDTO);
		}catch(Throwable e){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(cflPayResponseDTO);
		return cflPayResponseDTO;
	}

	@Override
	public PassiveScanPayResponseDTO merchantScanPay(PassiveScanPayRequestDTO request) {
		PassiveScanPayResponseDTO response = null;
		try{
			response = payOrderFacade.passiveScanPay(request);
		}catch(Throwable t){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public AccountSyncPayResponseDTO accountSyncPay(AccountPayRequestDTO ppAccountPayRequestDTO) {
		AccountSyncPayResponseDTO response = null;
		try {
			response = payOrderFacade.accountSyncPay(ppAccountPayRequestDTO);
		} catch (Throwable t) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}
	
	@Override
	public AccountPayResponseDTO accountPay(AccountPayRequestDTO ppAccountPayRequestDTO) {
		AccountPayResponseDTO response = null;
		try{
			response = payOrderFacade.accountPay(ppAccountPayRequestDTO);
		}catch (Throwable t) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public NcPayCflOrderResponseDTO bankInstallmentRequest(NcPayCflOrderRequestDTO requestDTO) {
		NcPayCflOrderResponseDTO response = null;
		try {
			response = payManageFacade.ncpayCflRequest(requestDTO);
		} catch (Throwable t) {
			logger.error("call payprocessor_ncpayCflRequest error, e=", t);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public NcPayCflSmsResponseDTO bankInstallmentSendSms(NcPayCflSmsRequestDTO requestDTO) {
		NcPayCflSmsResponseDTO response = null;
		try{
			response = payManageFacade.ncpayCflSendSms(requestDTO);
		}catch(Throwable t){
			logger.error("call payprocessor_ncpayCflSendSms error, e=", t);
			throw CommonUtil.handleException(Errors.SMS_SEND_FRILED); 
		}
		handleException(response);
		return response;
	}

	@Override
	public NcPayCflOpenResponseDTO bankInstallmentOpenAndPay(NcPayCflOpenRequestDTO requestDTO) {
		NcPayCflOpenResponseDTO response = null;
		try {
			response = payManageFacade.ncpayCflOpenAndPay(requestDTO);
		} catch (Throwable t) {
			logger.error("call payprocessor_ncpayCflOpenAndPay error, e=", t);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		if (StringUtils.isBlank(response.getPayUrl())) {
			logger.warn("tradeSysOrderId={}, merchantNo={}, pp_开通并支付接口返回的payUrl为空", requestDTO.getDealUniqueSerialNo(),
					requestDTO.getCustomerNumber());
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		return response;
	}

	@Override
	public NcPayCflConfirmResponseDTO bankInstallmentConfirmPay(NcPayCflConfirmRequestDTO requestDTO) {
		NcPayCflConfirmResponseDTO response = null;
		try{
			response = payManageFacade.ncpayCflConfirmPay(requestDTO);
		}catch(Throwable t){
			logger.error("call payprocessor_ncpayCflConfirmPay error, e=", t);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public PayRecordResponseDTO bankInstallmentSyncConfirmPay(NcPayCflSynConfirmRequestDTO requestDTO) {
		PayRecordResponseDTO response = null;
		try{
			response = payManageFacade.ncpayCflSynConfirmPay(requestDTO);
		}catch(Throwable t){
			logger.error("call payprocessor_ncpayCflSynConfirmPay error, e=", t);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleExceptionForSynConfirm(response);
		return response;
	}

	@Override
	public PayRecordResponseDTO synConfirmPay(NcPayConfirmRequestDTO confirmDTO) {
		PayRecordResponseDTO responseDTO = null;
		try {
			responseDTO = payManageFacade.synConfirmPay(confirmDTO);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleExceptionForSynConfirm(responseDTO);
		return responseDTO;
	}
	
	@Override
	public NcPayOrderResponseDTO preauthRequest(NcPayOrderRequestDTO requestDTO) {
		NcPayOrderResponseDTO responseDTO = null;
		try {
			responseDTO = payPreAuthFacade.ncPreAuthRequest(requestDTO);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public PayRecordResponseDTO preauthConfirm(NcPayConfirmRequestDTO preauthResDTO) {
		PayRecordResponseDTO responseDTO = null;
		try {
			responseDTO = payPreAuthFacade.ncPreAuthComfirm(preauthResDTO);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
//		handleExceptionForSynConfirm(responseDTO);
		return responseDTO;
	}
	
	@Override
	public PreAuthCompleteResponseDTO preauthComplete(PreAuthCompleteRequestDTO completeRequestDTO) {
		PreAuthCompleteResponseDTO response = null;
		try {
			response = payPreAuthFacade.ncPreAuthComplete(completeRequestDTO);
		} catch (Throwable t) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}
	
	@Override
	public PreAuthCancelResponseDTO preauthCancel(PreAuthCancelRequestDTO cancelRequestDTO) {
		PreAuthCancelResponseDTO response = null;
		try {
			response = payPreAuthFacade.ncPreAuthCancel(cancelRequestDTO);
		} catch (Throwable t) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public QueryResponseDTO queryOrderResult(QueryRequestDTO queryRequestDTO) {
		QueryResponseDTO response = null;
		try {
			response = payProcessorQueryFacade.query(queryRequestDTO);
		} catch (Throwable t) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}
	
	@Override
	public PersonalMemberSyncPayResponseDTO memberBalancePay(PersonalMemberSyncPayRequestDTO requestDTO) {
		PersonalMemberSyncPayResponseDTO responseDTO = null;
		try {
			responseDTO = payOrderFacade.personalMemberSyncPay(requestDTO);
		} catch (Throwable t) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public NcGuaranteeCflPrePayResponseDTO guaranteeCflPrePay(NcGuaranteeCflPrePayRequestDTO ncGuaranteeCflPrePayRequestDTO) {
		NcGuaranteeCflPrePayResponseDTO responseDTO = null;
		try {
			responseDTO = payOrderFacade.guaranteeCflPrePay(ncGuaranteeCflPrePayRequestDTO);
		}catch (Throwable t){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public NcGuaranteeCflPayResponseDTO guaranteeCflRequest(NcGuaranteeCflPayRequestDTO ncGuaranteeCflPayRequestDTO) {
		NcGuaranteeCflPayResponseDTO responseDTO = null;
		try {
			responseDTO = payOrderFacade.guaranteeCflRequest(ncGuaranteeCflPayRequestDTO);
		}catch (Throwable t){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public NcCflEasyResponseDTO cflEasyCreatePayment(NcCflEasyRequestDTO ncCflEasyRequestDTO) {
		NcCflEasyResponseDTO responseDTO = null;
		try {
			responseDTO = payCflEasyFacade.createPayment(ncCflEasyRequestDTO);
		}catch (Throwable t){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public NcCflEasySmsResponseDTO cflEasySendSms(NcCflEasySmsRequestDTO ncCflEasySmsRequestDTO) {
		NcCflEasySmsResponseDTO responseDTO = null;
		try {
			responseDTO = payCflEasyFacade.sendSms(ncCflEasySmsRequestDTO);
		}catch (Throwable t){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}

	@Override
	public PayRecordResponseDTO cflEasySynConfirmPay(NcCflEasyConfirmRequestDTO ncCflEasyConfirmRequestDTO) {
		PayRecordResponseDTO responseDTO = null;
		try {
			responseDTO = payCflEasyFacade.synConfirmPay(ncCflEasyConfirmRequestDTO);
		}catch (Throwable t){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleExceptionForSynConfirm(responseDTO);
		return responseDTO;
	}

	@Override
	public NcCflEasyConfirmResponseDTO clfEasyConfirmPay(NcCflEasyConfirmRequestDTO ncCflEasyConfirmRequestDTO) {
		NcCflEasyConfirmResponseDTO responseDTO = null;
		try {
			responseDTO = payCflEasyFacade.confirmPay(ncCflEasyConfirmRequestDTO);
		}catch (Throwable t){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(responseDTO);
		return responseDTO;
	}
}
