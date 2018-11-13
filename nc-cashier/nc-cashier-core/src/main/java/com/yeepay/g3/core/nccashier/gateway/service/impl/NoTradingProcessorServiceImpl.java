
package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.gateway.service.NoTradingProcessorService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
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
import com.yeepay.g3.facade.nop.dto.ResponseStatusDTO;
import org.springframework.stereotype.Service;


/**
 * 
 * @Description 调用无交易订单处理器service
 * @author yangmin.peng
 * @since 2017年8月25日上午11:17:47
 */
@Service
public class NoTradingProcessorServiceImpl extends NcCashierBaseService implements NoTradingProcessorService {

	@Override
	public QueryCardbinResponseDTO queryCardBin(QueryCardBinRequestDTO request) {
		QueryCardbinResponseDTO response = null;
		try {
			response = queryCardInfoFacade.queryCardBin(request);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public QueryProductStatusReponseDTO queryProductStatus(QueryProductStatusRequestDTO request) {
		QueryProductStatusReponseDTO response = null;
		try {
			response = queryProductStatusFacade.queryProductStatus(request);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public AuthBindCardResponseDTO authBindCardRequest(AuthBindCardRequestDTO request) {
		AuthBindCardResponseDTO response = null;
		try {
			response = authBindCardFacade.authBindCardRequest(request);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public AuthBindCardConfirmResponseDTO authBindCardConfirm(
			AuthBindCardConfirmRequestDTO request) {
		AuthBindCardConfirmResponseDTO response = null;
		try {
			response = authBindCardFacade.authBindCardConfirm(request);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public AuthBindCardSmsResponseDTO authBindCardSms(AuthBindCardSmsRequestDTO request) {
		AuthBindCardSmsResponseDTO response = null;
		try {
			response = authBindCardFacade.authBindCardSms(request);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}

	@Override
	public QueryOrderResponseDTO queryOrder(QueryOrderRequestDTO request) {
		QueryOrderResponseDTO response = null;
		try {
			response = queryCardInfoFacade.queryOrder(request);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		handleException(response);
		return response;
	}
	private void handleException(ResponseStatusDTO responseDTO){
		if(responseDTO== null ){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}else if(!"NOP00000".equals(responseDTO.getCode())){//NOP00000代表调用NOP交易成功
			if("NOP04004".equals(responseDTO.getCode())){//鉴权绑卡请求，发送短信失败，可重新发送短信
				throw new CashierBusinessException(Errors.SMS_SEND_FRILED.getCode(), Errors.SMS_SEND_FRILED.getMsg());
			}else if("NOP04001".equals(responseDTO.getCode())){//绑卡确认，短验确认失败，可重发短验
				throw new CashierBusinessException(Errors.SMS_VERIFY_FAILED.getCode(), Errors.SMS_VERIFY_FAILED.getMsg());
			}else if("NOP04002".equals(responseDTO.getCode())){//绑卡确认，短验输入错误，可重新输入
				throw new CashierBusinessException(Errors.SMS_INPUT_ERROR.getCode(), Errors.SMS_INPUT_ERROR.getMsg());
			}else{
				throw CommonUtil.handleException(SysCodeEnum.NOP.name(), responseDTO.getCode(), 
					responseDTO.getMessage());
				//throw new CashierBusinessException(responseDTO.getCode(), responseDTO.getMessage());
			}
		}
	}
	
}
