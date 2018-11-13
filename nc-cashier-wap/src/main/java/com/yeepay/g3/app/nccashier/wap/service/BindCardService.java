package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.vo.BindCardInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardMerchantRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.NopBindCardOrderInfoVO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderResponseDTO;

/**
 * 
 * @Description 绑卡支付wap端服务层
 * @author yangmin.peng
 * @since 2017年8月25日下午2:42:44
 */
public interface BindCardService {
	
	NOPCardBinResponseDTO getNopCardBinInfo(String token, String cardNo, BindCardMerchantRequestVO bindCardVO);

	NOPAuthBindResponseDTO authBindCardRequest(String token, BindCardInfoVO cardInfo,BindCardMerchantRequestVO bindCardRequestVO);

	NOPAuthBindConfirmResponseDTO authBindCardConfirm(String token, String smsCode,BindCardInfoVO cardInfo,NopBindCardOrderInfoVO nopOrderInfo,BindCardMerchantRequestVO bindCardVO);

	NOPAuthBindSMSResponseDTO authBindCardSMS(String token, NopBindCardOrderInfoVO nopOrderInfo,BindCardMerchantRequestVO bindCardRequestVO);

	NOPQueryOrderResponseDTO queryNopOrderStatus(NopBindCardOrderInfoVO nopOrderInfo,BindCardMerchantRequestVO bindCardRequestVO);

	boolean getNopBindCardOpenStatus(String merchantNo);
	
}
