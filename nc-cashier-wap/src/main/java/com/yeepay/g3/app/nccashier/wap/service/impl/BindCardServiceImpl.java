package com.yeepay.g3.app.nccashier.wap.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.BindCardService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardMerchantRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.NopBindCardOrderInfoVO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayRequestDTO;
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
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;


/**
 * @Description 绑卡支付wap端服务层
 * @author yangmin.peng
 * @since 2017年8月25日下午2:42:44
 */
@Service("bindCardService")
public class BindCardServiceImpl implements BindCardService {

	private static Logger LOGGER = LoggerFactory.getLogger(BindCardServiceImpl.class);
	
	@Autowired
	private NcCashierService ncCashierService;

	@Override
	public NOPCardBinResponseDTO getNopCardBinInfo(String token, String cardNo, BindCardMerchantRequestVO bindCardVO) {
		NOPCardBinRequestDTO requestDTO = new NOPCardBinRequestDTO();
		FirstBindCardPayRequestDTO bindCardPayRequest = new FirstBindCardPayRequestDTO();
		RequestInfoDTO info = null;
		String cardType = null;
		if(StringUtils.isNotBlank(token)){
			info = ncCashierService.requestBaseInfo(token);
			bindCardPayRequest.setTokenId(token);
			bindCardPayRequest.setRequestId(info.getPaymentRequestId());
			requestDTO.setBindPayRequest(bindCardPayRequest);
			cardType = info.getUrlParamInfo().getCardType();
			if(CardTypeEnum.DEBIT.name().equals(cardType)){
				cardType = "OD";
			}else if(CardTypeEnum.CREDIT.name().equals(cardType)){
				cardType = "OC";
			}else{
				cardType = "DC";
			}
		}else{
			cardType = bindCardVO.getCardType();
		}
		requestDTO.setBizType(bindCardVO.getBizType());
		requestDTO.setCardNo(cardNo);
		requestDTO.setCardType(cardType);
		requestDTO.setMerchantNo(bindCardVO.getMerchantNo());
		return ncCashierService.getNopCardBinInfo(requestDTO);
	}

	@Override
	public NOPAuthBindResponseDTO authBindCardRequest(String token, BindCardInfoVO cardInfo,BindCardMerchantRequestVO bindCardRequestVO) {
		NOPAuthBindRequestDTO requestDTO = new NOPAuthBindRequestDTO();
		//设置卡、用户信息
		requestDTO.setBankCardNo(cardInfo.getCardno());
		requestDTO.setUserName(cardInfo.getOwner());
		requestDTO.setIdCardNo(cardInfo.getIdno());
		requestDTO.setPhone(cardInfo.getPhoneNo());
		requestDTO.setCvv2(cardInfo.getCvv());
		requestDTO.setValidthru(cardInfo.getAvlidDate());
		
		requestDTO.setMerchantNo(bindCardRequestVO.getMerchantNo());
		requestDTO.setMerchantFlowId(bindCardRequestVO.getMerchantFlowId());
		requestDTO.setUserNo(bindCardRequestVO.getUserNo());
		requestDTO.setUserType(bindCardRequestVO.getUserType());
		requestDTO.setBindCallBackUrl(bindCardRequestVO.getBindCallBackUrl());
		requestDTO.setCardType(bindCardRequestVO.getCardType());
		requestDTO.setBizType(bindCardRequestVO.getBizType());
		requestDTO.setBizFlowId(bindCardRequestVO.getBizFlowId());
		FirstBindCardPayRequestDTO bindCardPayRequest = new FirstBindCardPayRequestDTO();
		RequestInfoDTO info = null;
		if(StringUtils.isNotBlank(token)){
			info = ncCashierService.requestBaseInfo(token);
			requestDTO.setBizType(info.getUrlParamInfo().getBizType());
			bindCardPayRequest.setTokenId(token);
			bindCardPayRequest.setRequestId(info.getPaymentRequestId());
		}
		requestDTO.setBindPayRequest(bindCardPayRequest);
		return ncCashierService.authBindCardRequest(requestDTO);
	}

	

	@Override
	public NOPAuthBindConfirmResponseDTO authBindCardConfirm(String token,String smsCode,BindCardInfoVO cardInfo,NopBindCardOrderInfoVO nopOrderInfo,BindCardMerchantRequestVO bindCardVO) {
		NOPAuthBindConfirmRequestDTO requestDTO = new NOPAuthBindConfirmRequestDTO();
		requestDTO.setMerchantNo(bindCardVO.getMerchantNo());
		requestDTO.setSmsCode(smsCode);
		requestDTO.setMerchantFlowId(bindCardVO.getMerchantFlowId());
		requestDTO.setRequestFlowId(nopOrderInfo.getRequestFlowId());
		
		//设置卡、用户信息
		requestDTO.setBankCardNo(cardInfo.getCardno());
		requestDTO.setUserName(cardInfo.getOwner());
		requestDTO.setIdCardNo(cardInfo.getIdno());
		requestDTO.setPhone(cardInfo.getPhoneNo());
		requestDTO.setCvv2(cardInfo.getCvv());
		requestDTO.setValidthru(cardInfo.getAvlidDate());	
		
		FirstBindCardPayRequestDTO bindCardPayRequest = new FirstBindCardPayRequestDTO();
		RequestInfoDTO info = null;
		if(StringUtils.isNotBlank(token)){
			info = ncCashierService.requestBaseInfo(token);
			bindCardPayRequest.setTokenId(token);
			bindCardPayRequest.setRequestId(info.getPaymentRequestId());
		}
		requestDTO.setBindPayRequest(bindCardPayRequest);
		return ncCashierService.authBindCardConfirm(requestDTO);
	}

	@Override
	public NOPAuthBindSMSResponseDTO authBindCardSMS(String token, NopBindCardOrderInfoVO nopOrderInfo,BindCardMerchantRequestVO bindCardRequestVO) {
		NOPAuthBindSMSRequestDTO requestDTO = new NOPAuthBindSMSRequestDTO();
		requestDTO.setMerchantNo(bindCardRequestVO.getMerchantNo());
		requestDTO.setMerchantFlowId(bindCardRequestVO.getMerchantFlowId());
		requestDTO.setRequestFlowId(nopOrderInfo.getRequestFlowId());
		FirstBindCardPayRequestDTO bindCardPayRequest = new FirstBindCardPayRequestDTO();
		RequestInfoDTO info = null;
		if(StringUtils.isNotBlank(token)){
			info = ncCashierService.requestBaseInfo(token);
			bindCardPayRequest.setTokenId(token);
			bindCardPayRequest.setRequestId(info.getPaymentRequestId());
		}
		requestDTO.setBindPayRequest(bindCardPayRequest);
		return ncCashierService.authBindCardSMS(requestDTO);
	}

	@Override
	public NOPQueryOrderResponseDTO queryNopOrderStatus(NopBindCardOrderInfoVO nopOrderInfo,BindCardMerchantRequestVO bindCardRequestVO) {
		NOPQueryOrderRequestDTO requestDTO = new NOPQueryOrderRequestDTO();
		requestDTO.setMerchantNo(bindCardRequestVO.getMerchantNo());
		requestDTO.setMerchantFlowId(bindCardRequestVO.getMerchantFlowId());
		requestDTO.setRequestFlowId(nopOrderInfo.getRequestFlowId());
		requestDTO.setNopOrderId(nopOrderInfo.getNopOrderId());
		return ncCashierService.queryNopOrderStatus(requestDTO);
	}

	
	@Override
	public boolean getNopBindCardOpenStatus(String merchantNo) {
		NOPQueryBindCardOpenStatusResponseDTO response = null;
		try{
			response = ncCashierService.getNopBindCardOpenStatus(merchantNo);
		}catch(Throwable t){
			LOGGER.error("商户"+merchantNo+"未开通绑卡支付",t);
		}
		if(response != null && Constant.BIND_CARD_PRODUCT_ENABLE.equals(response.getStatus())){
			return true;
		}
		return false;
	}

	

}
