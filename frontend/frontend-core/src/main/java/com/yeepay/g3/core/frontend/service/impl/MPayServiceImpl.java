/**
 * 
 */
package com.yeepay.g3.core.frontend.service.impl;


import com.yeepay.g3.core.frontend.service.MPayService;

/**
 * 原微信子系统接口
 * @author TML
 */
//@Service
@Deprecated
public class MPayServiceImpl extends AbstractService implements MPayService{
	
//	private static final Logger logger = FeLoggerFactory.getLogger(MPayServiceImpl.class);
	
    /**
     * mpay 老系统接口
     */
//	private FrontEndPayFacade frontEndPayFacade = (FrontEndPayFacade)CommonUtils.getFacadeService(FrontEndPayFacade.class, ConstantUtils.getMpayUrl());

//	@Override
//	public void activeScanMPay(PayOrder payOrder, PayRecord payRecord) {
//
//		FrontEndRequestDTO frontEndRequestDTO = this.buildFrontEndRequestDTO(payOrder);
//		logger.info("[Mpay] - [入参] - [activeScanPay] - [" + frontEndRequestDTO.toString() + "]");
//		FrontEndResponseDTO frontEndResponseDTO = frontEndPayFacade.activeScanPay(frontEndRequestDTO);
//		if (frontEndResponseDTO == null) {
//			throw new NullPointerException("mpay activeScanPay result is null ");
//		}
//		logger.info("[Mpay] - [出参] - [activeScanPay] - [" + frontEndResponseDTO.toString() + "]");
//		payRecord.setFrontValue(frontEndResponseDTO.getCodeUrl());
//		payOrder.setPayInterface(ConstantUtils.MPAY_PAY_INTERFACE);
//		if (StringUtils.isNotBlank(frontEndResponseDTO.getResultCode())) {
//			payRecord.setNocardCode(frontEndResponseDTO.getResultCode());
//			payRecord.setNocardMsg(frontEndResponseDTO.getResultMsg());
//			throw new FrontendBizException(errorCodeTranslator.translateCode(ErrorCodeSource.MPAY, frontEndResponseDTO.getResultCode(), frontEndResponseDTO.getResultMsg()));
//		} else {
//			payOrder.setPayStatus(PayStatusEnum.INIT.name());
//		}
//	}	
	

//	private FrontEndRequestDTO buildFrontEndRequestDTO(PayOrder payOrder){
//		FrontEndRequestDTO frontEndRequestDTO = new FrontEndRequestDTO();
//		frontEndRequestDTO.setAmount(payOrder.getTotalAmount());
////		frontEndRequestDTO.setAuthCode(authCode);
//		frontEndRequestDTO.setCustomerNo(payOrder.getCustomerNumber());
//		frontEndRequestDTO.setDescription(payOrder.getGoodsDescription());
////		frontEndRequestDTO.setDeviceInfo();
//		frontEndRequestDTO.setOpenId(payOrder.getOpenId());
//		// 请求微信订单号
//		frontEndRequestDTO.setOutTradeNo(payOrder.getOrderNo());
//		OrderType orderType = payOrder.getOrderType() != null ? OrderType.valueOf(payOrder.getOrderType()) : null;
//		TradeType tradeType = TradeType.getTradeType(orderType);
//		frontEndRequestDTO.setTradeType(tradeType != null ? tradeType.name() : null);
//		return frontEndRequestDTO;
//	}

//	@Override
//	public BankQueryResponseDTO queryMPayOrder(
//			BankQueryRequestDTO bankQueryRequestDTO) {
//		FrontEndRequestDTO frontEndRequestDTO = buildFrontEndRequestDTO(bankQueryRequestDTO);
//		FrontEndResponseDTO frontEndResponseDTO = frontEndPayFacade.queryOrder(frontEndRequestDTO);
//		if (frontEndResponseDTO == null) {
//			return null;
//		}
//		if(StringUtils.isNotBlank(frontEndResponseDTO.getResultCode())){
//			BankQueryResponseDTO bankQueryResponseDTO = new BankQueryResponseDTO();
//			bankQueryResponseDTO.setResponseCode(frontEndResponseDTO.getResultCode());
//			bankQueryResponseDTO.setResponseMsg(frontEndResponseDTO.getResultMsg());
//			return bankQueryResponseDTO;
//		}
//		return buildBankQueryResponseDTO(frontEndResponseDTO);
//	}
//	
//	private FrontEndRequestDTO buildFrontEndRequestDTO(BankQueryRequestDTO bankQueryRequestDTO){
//		FrontEndRequestDTO frontEndRequestDTO = new FrontEndRequestDTO();
//		frontEndRequestDTO.setCustomerNo(bankQueryRequestDTO.getCustomerNumber());
//		frontEndRequestDTO.setOutTradeNo(bankQueryRequestDTO.getOrderNo());
//		return frontEndRequestDTO;
//	}
//	
//	private BankQueryResponseDTO buildBankQueryResponseDTO(FrontEndResponseDTO frontEndResponseDTO){
//		BankQueryResponseDTO bankQueryResponseDTO = new BankQueryResponseDTO();
//		bankQueryResponseDTO.setCustomerNumber(frontEndResponseDTO.getCustomerNo());
//		bankQueryResponseDTO.setOrderNo(frontEndResponseDTO.getOutTradeNo());
//		bankQueryResponseDTO.setPayBank(frontEndResponseDTO.getBankCode());
//		bankQueryResponseDTO.setPayBankcardType(PayBankcardType.getPayBankcardType(frontEndResponseDTO.getBankCardType()));
//		bankQueryResponseDTO.setPayStatus(PayStatusEnum.getPayStatusEnum(
//				TradeStatus.getTradeStatus(frontEndResponseDTO.getTradeStatus())));
//		bankQueryResponseDTO.setTransactionId(frontEndResponseDTO.getTransactionId());
//		bankQueryResponseDTO.setTotalAmount(frontEndResponseDTO.getAmount());
//		bankQueryResponseDTO.setBankTotalCost(frontEndResponseDTO.getChannelCost());
//		bankQueryResponseDTO.setBankSuccessTime(frontEndResponseDTO.getTimeEnd());
//		bankQueryResponseDTO.setPaySuccessTime(frontEndResponseDTO.getTimeEnd());
//		bankQueryResponseDTO.setPayInterface(ConstantUtils.MPAY_PAY_INTERFACE);
//		return bankQueryResponseDTO;
//	}
}
