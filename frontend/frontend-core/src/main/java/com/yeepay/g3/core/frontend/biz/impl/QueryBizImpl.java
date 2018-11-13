package com.yeepay.g3.core.frontend.biz.impl;

import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.biz.QueryBiz;
import com.yeepay.g3.core.frontend.dto.BankQueryRequestDTO;
import com.yeepay.g3.core.frontend.dto.BankQueryResponseDTO;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.*;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.facade.FrontendNotifyFacade;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("queryBiz")
public class QueryBizImpl extends AbstractBiz implements QueryBiz{
	
	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(QueryBizImpl.class);

	@Override
	public void queryBankOrder(Date start, Date end, String platformType) {
		List<PayOrder> unSuccessList = payOrderService.queryUnSuccessByDate(start, end, platformType);
		if(unSuccessList == null || unSuccessList.size() < 1){
			return;
		}
        logger.info("[需要补单的订单数量]" + " [" + unSuccessList.size() + "]" );
        for(PayOrder payOrder: unSuccessList){
            try {
                repairByOrder(payOrder);
            } catch (FrontendBizException e) {
                logger.warn("[" + payOrder.getOrderNo() + "] - [补单失败]" + e.getMessage());
            } catch (Throwable th) {
                logger.error("[" + payOrder.getOrderNo() + "] - [补单失败]", th);
                continue;
            }
        }
	}

	@Override
	public FeOperationResponseDTO repairOrders(FeOperationRequestDTO requestDTO) {
		logger.info("[size = " + requestDTO.getOrderNos().size() + "]");
		FeOperationResponseDTO responseDTO = new FeOperationResponseDTO();
		for (BasicOperationDTO operate : requestDTO.getOrderNos()){
			try {
				logger.info("[" + operate.getRequestId() + "] - [START]");
				PayOrder payOrder = queryBySystemAndRequestId(operate.getRequestSystem(),
						operate.getRequestId(), operate.getPlatformType());
				if (PayStatusEnum.SUCCESS.name().equals(payOrder.getPayStatus())){
					responseDTO.setIgnore(responseDTO.getIgnore() + 1);
					continue;
				}
				repairByOrder(payOrder);
				responseDTO.setSuccess(responseDTO.getSuccess() + 1);
				logger.info("[" + operate.getRequestId() + "] - [SUCCESS]");
			} catch (FrontendBizException e) {
				responseDTO.getErrorList().add("[" + operate.getRequestId() + "]" + e.getMessage());
			} catch (Throwable th) {
				logger.error("[" + operate.getRequestId() +"] - [FAILURE]", th);
				responseDTO.getErrorList().add("[" + operate.getRequestId() + "]" + th.getMessage());
			}
		}
		return responseDTO;
	}

	private void repairByOrder(PayOrder payOrder){
		BankQueryResponseDTO bankQueryResponseDTO = bankPayService.queryOpenPayOrder(buildBankQueryRequestDTO(payOrder));
		if (bankQueryResponseDTO == null){
			throw new FrontendBizException(ErrorCode.F0002004, "未查询到银行订单");
		}
		if (StringUtils.isNotBlank(bankQueryResponseDTO.getResponseCode())){
            throw new FrontendBizException(bankQueryResponseDTO.getResponseCode(), bankQueryResponseDTO.getResponseMsg());
		}
		if (!PayStatusEnum.SUCCESS.equals(bankQueryResponseDTO.getPayStatus())){
            throw new FrontendBizException(ErrorCode.F0002005, "订单未支付");
        }
		FrontendNotifyFacade frontendNotifyFacade = RemoteServiceFactory.getService(FrontendNotifyFacade.class);
        BankNotifyResponseDTO responseDTO = frontendNotifyFacade.receiveBankNotify(buildBankNotifyRequestDTO(bankQueryResponseDTO));
        if (StringUtils.isNotBlank(responseDTO.getResponseCode())) {
            throw new FrontendBizException(responseDTO.getResponseCode(), responseDTO.getResponseMsg());
        }
	}

	private BankQueryRequestDTO buildBankQueryRequestDTO(PayOrder payOrder){
		BankQueryRequestDTO bankQueryRequestDTO = new BankQueryRequestDTO();
		bankQueryRequestDTO.setCustomerNumber(payOrder.getCustomerNumber());
		bankQueryRequestDTO.setOrderNo(payOrder.getOrderNo());
		bankQueryRequestDTO.setPayInterface(payOrder.getPayInterface());
		return bankQueryRequestDTO;
	}

	private BankNotifyRequestDTO buildBankNotifyRequestDTO(BankQueryResponseDTO bankQueryResponseDTO){
		BankNotifyRequestDTO bankNotifyRequestDTO = new BankNotifyRequestDTO();
		bankNotifyRequestDTO.setCustomerNumber(bankQueryResponseDTO.getCustomerNumber());
		bankNotifyRequestDTO.setOrderNo(bankQueryResponseDTO.getOrderNo());
		bankNotifyRequestDTO.setBankSuccessTime(bankQueryResponseDTO.getBankSuccessTime());
		bankNotifyRequestDTO.setBankTotalCost(bankQueryResponseDTO.getBankTotalCost());
		bankNotifyRequestDTO.setBankTradeId(bankQueryResponseDTO.getBankTradeId());
		bankNotifyRequestDTO.setOutTradeNo(bankQueryResponseDTO.getOutTradeNo());
		bankNotifyRequestDTO.setPayBank(bankQueryResponseDTO.getPayBank());
		bankNotifyRequestDTO.setPayBankcardType(bankQueryResponseDTO.getPayBankcardType());
		bankNotifyRequestDTO.setPayStatus(bankQueryResponseDTO.getPayStatus());
		bankNotifyRequestDTO.setPaySuccessTime(bankQueryResponseDTO.getPaySuccessTime());
		bankNotifyRequestDTO.setRequestId(bankQueryResponseDTO.getRequestId());
		bankNotifyRequestDTO.setTransactionId(bankQueryResponseDTO.getTransactionId());
        bankNotifyRequestDTO.setPayInterface(bankQueryResponseDTO.getPayInterface());
        bankNotifyRequestDTO.setTotalAmount(bankQueryResponseDTO.getTotalAmount());
        bankNotifyRequestDTO.setOpenId(bankQueryResponseDTO.getOpenId());// added by zhijun.wang 2017-07-26
		bankNotifyRequestDTO.setExtParam(bankQueryResponseDTO.getExtParam());
		return bankNotifyRequestDTO;
	}

	@Override
	public FrontendQueryResponseDTO queryOrder(
			FrontendQueryRequestDTO frontendQueryRequestDTO) {
		FrontendQueryResponseDTO frontendQueryResponseDTO = new FrontendQueryResponseDTO();
		PayOrder payOrder = null;
		try {
			//查询订单
			payOrder = payOrderService.queryBySystemAndRequestId(frontendQueryRequestDTO.getRequestSystem(), frontendQueryRequestDTO.getRequestId(), 
					frontendQueryRequestDTO.getPlatformType().name());
            if(payOrder == null){
				throw new FrontendBizException(ErrorCode.F0002004);
			} 
			// 组装返回值结果
			updateResponse(null,frontendQueryResponseDTO, payOrder, null);
		} catch (FrontendBizException e) {
			handleException(e, frontendQueryResponseDTO, payOrder, null);
		} catch (Throwable e) {
			handleException(e, frontendQueryResponseDTO,payOrder, null);
		} 
		return frontendQueryResponseDTO;
	}
}
