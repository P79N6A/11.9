package com.yeepay.g3.core.frontend.service.impl;

import com.yeepay.g3.core.frontend.dto.BankQueryRequestDTO;
import com.yeepay.g3.core.frontend.dto.BankQueryResponseDTO;
import com.yeepay.g3.core.frontend.service.BankPayService;
import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.trade.bankinterface.dto.OpenBankOrderDTO;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author TML
 */
@Service
public class BankPayServiceImpl extends AbstractService implements BankPayService{

//	@Override
//	public void H5appPay(PayOrder payOrder, PayRecord payRecord, BasicRequestDTO basicRequestDTO) {
//		OpenPreparePayParamDTO openPreparePayParamDTO = this.buildOpenPreparePayParamDTO(payOrder
//				,OpenWayEnum.WECHAT_WAP_H5, ConstantUtils.getH5PayInterface(), basicRequestDTO);
//		basePay(payOrder,payRecord,openPreparePayParamDTO);
//	}
	
//	/**
//	 * 获取银行子系统流水号（支付订单号）
//	 * @return
//	 * @param platformType
//	 */
//	public String getPayOrderNo(PayOrder payOrder){
//		String orderNo = null;
//		try{
//			if (OrderType.ALIPAYSCAN.name().equals(payOrder.getOrderType())){
//				orderNo = openInterfaceFacade.generateOrderNo(ConstantUtils.ALIPAY_SCAN_PAYINTERFACE);
//			} else if(OrderType.H5APP.name().equals(payOrder.getOrderType())){
//				orderNo = openInterfaceFacade.generateOrderNo(ConstantUtils.getH5PayInterface());
//			} else {
//				orderNo = openInterfaceFacade.generateOpenOrderNo();
//			}
//			if(StringUtils.isBlank(orderNo)){
//				throw new FrontendBizException(ErrorCode.F0003002);
//			}
//			return orderNo;
//		}catch(Exception e){
//			throw new FrontendBizException(ErrorCode.F0003002);
//		}
//	}

//	@Override
//	public void activeScanMPay(PayOrder payOrder, PayRecord payRecord, BasicRequestDTO basicRequestDTO) {
//		OpenPreparePayParamDTO openPreparePayParamDTO = this.buildOpenPreparePayParamDTO(payOrder
//				,OpenWayEnum.ACTIVE, ConstantUtils.ALIPAY_SCAN_PAYINTERFACE, basicRequestDTO);
//		basePay(payOrder,payRecord,openPreparePayParamDTO);
//	}

//	private void basePay(PayOrder payOrder, PayRecord payRecord,OpenPreparePayParamDTO openPreparePayParamDTO){
//		OpenPreparePayResultDTO openPreparePayResultDTO = openInterfaceFacade.preparePay(openPreparePayParamDTO);
//		if (openPreparePayResultDTO == null) {
//			throw new FrontendBizException(ErrorCode.F0003002);
//		}
//		payRecord.setFrontValue(openPreparePayResultDTO.getPayUrl());
//		payOrder.setPayInterface(openPreparePayParamDTO.getPayInterface());
//		if (!openPreparePayResultDTO.isSuccess() && StringUtils.isNotBlank(openPreparePayResultDTO.getBizCode())) {
//			payRecord.setNocardCode(openPreparePayResultDTO.getBizCode());
//			payRecord.setNocardMsg(openPreparePayResultDTO.getBizMsg());
//			throw new FrontendBizException(errorCodeTranslator.translateCode(ErrorCodeSource.BANKINTERFACE, openPreparePayResultDTO.getBizCode(), openPreparePayResultDTO.getBizMsg()));
//		} else {
//			payOrder.setPayStatus(PayStatusEnum.INIT.name());
//		}
//	}

//	private OpenPreparePayParamDTO buildOpenPreparePayParamDTO(PayOrder payOrder,OpenWayEnum wayEnum,String payInterFace, BasicRequestDTO basicRequestDTO){
//		OpenPreparePayParamDTO openPreparePayParamDTO = new OpenPreparePayParamDTO();
//		openPreparePayParamDTO.setMerchantOrderNo(payOrder.getOutTradeNo());
//		openPreparePayParamDTO.setOpenOrderNo(payOrder.getOrderNo());
//		openPreparePayParamDTO.setOpenWay(wayEnum);
//		openPreparePayParamDTO.setPayInterface(payInterFace);
//		openPreparePayParamDTO.setOutOrderNo(payOrder.getRequestId());
//		openPreparePayParamDTO.setPageCallBack(payOrder.getPageCallBack());
//		openPreparePayParamDTO.setPayAmount(new Amount(payOrder.getTotalAmount()));
//		openPreparePayParamDTO.setYeepayCommodityName(payOrder.getGoodsDescription());
//		openPreparePayParamDTO.setYeepayMerchantNo(payOrder.getCustomerNumber());
//		openPreparePayParamDTO.setPayerIp(payOrder.getPayerIp());
//		openPreparePayParamDTO.setYeepayMerchantName(basicRequestDTO.getCustomerName());
//		if(PayLimitType.NO_CREDIT.name().equals(payOrder.getPayLimitType())){
//			openPreparePayParamDTO.setLimitCredit(true);
//		}
//		return openPreparePayParamDTO;
//	}

	@Override
	public BankQueryResponseDTO queryOpenPayOrder(
			BankQueryRequestDTO bankQueryRequestDTO) {
		OpenBankOrderDTO openBankOrderDTO = openInterfaceFacade.getOrder(bankQueryRequestDTO.getPayInterface(),
                    bankQueryRequestDTO.getOrderNo());
		if(openBankOrderDTO == null){
			return null;
		}
		return buildBankQueryResponseDTO(openBankOrderDTO);
	}
	
//	@Override
//	public BankQueryResponseDTO queryOnlinePayOrder(
//			BankQueryRequestDTO bankQueryRequestDTO) {
//		BankOrderDTO bankOrderDTO = bankInterfaceFacade.getBankOrder(bankQueryRequestDTO.getPayInterface(),
//                    bankQueryRequestDTO.getOrderNo());
//		if(bankOrderDTO == null){
//			return null;
//		}
//		return buildBankQueryResponseDTO(bankOrderDTO);
//	}
	
	private BankQueryResponseDTO buildBankQueryResponseDTO(OpenBankOrderDTO openBankOrderDTO){
		BankQueryResponseDTO bankQueryResponseDTO = new BankQueryResponseDTO();
		bankQueryResponseDTO.setOrderNo(openBankOrderDTO.getOpenOrderNo());
		bankQueryResponseDTO.setOutTradeNo(openBankOrderDTO.getMerchantOrderNo());
		bankQueryResponseDTO.setPayBank(openBankOrderDTO.getBankCode());
		bankQueryResponseDTO.setPayBankcardType(openBankOrderDTO.getCardType()==null ? 
				null : PayBankcardType.getPayBankcardType(openBankOrderDTO.getCardType().name()));
		bankQueryResponseDTO.setPayStatus(PayStatusEnum.getPayStatusEnumFromBank(openBankOrderDTO.getStatus().name()));
		bankQueryResponseDTO.setRequestId(openBankOrderDTO.getOutOrderNo());
		bankQueryResponseDTO.setTransactionId(openBankOrderDTO.getOpenTradeId());
		if(openBankOrderDTO.getPayAmount() != null 
				&& openBankOrderDTO.getPayAmount().getValue() != null){
			bankQueryResponseDTO.setTotalAmount(openBankOrderDTO.getPayAmount().getValue());
		}
		bankQueryResponseDTO.setBankTotalCost(new BigDecimal(openBankOrderDTO.getSingleCost()));
		bankQueryResponseDTO.setBankTradeId(openBankOrderDTO.getBankTradeId());
		bankQueryResponseDTO.setBankSuccessTime(openBankOrderDTO.getBankSuccessTime());
		bankQueryResponseDTO.setPaySuccessTime(openBankOrderDTO.getPaySuccessTime());
		bankQueryResponseDTO.setPayInterface(openBankOrderDTO.getPayInterface());
		bankQueryResponseDTO.setExtParam(openBankOrderDTO.getExtendResultMap());
		return bankQueryResponseDTO;
	}
	
//	private BankQueryResponseDTO buildBankQueryResponseDTO(BankOrderDTO bankOrderDTO){
//		BankQueryResponseDTO bankQueryResponseDTO = new BankQueryResponseDTO();
//		bankQueryResponseDTO.setOrderNo(bankOrderDTO.getBankOrderNo());
//		bankQueryResponseDTO.setOutTradeNo(bankOrderDTO.getMerchantOrderNo());
//		bankQueryResponseDTO.setPayBankcardType(bankOrderDTO.getCardType()==null ? 
//				null : PayBankcardType.getPayBankcardType(bankOrderDTO.getCardType().name()));
//		bankQueryResponseDTO.setPayStatus(PayStatusEnum.getPayStatusEnumFromBank(bankOrderDTO.getStatus().name()));
//		bankQueryResponseDTO.setTransactionId(bankOrderDTO.getBankTradeId());
//		if(bankOrderDTO.getPayAmount() != null 
//				&& bankOrderDTO.getPayAmount().getValue() != null){
//			bankQueryResponseDTO.setTotalAmount(bankOrderDTO.getPayAmount().getValue());
//		}
//		bankQueryResponseDTO.setBankTotalCost(null);
//		bankQueryResponseDTO.setBankSuccessTime(bankOrderDTO.getBankSuccessTime());
//		bankQueryResponseDTO.setPaySuccessTime(bankOrderDTO.getPaySuccessTime());
//		bankQueryResponseDTO.setPayInterface(bankOrderDTO.getPayInterface());
//		return bankQueryResponseDTO;
//	}
}
