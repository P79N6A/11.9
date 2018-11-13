package com.yeepay.g3.app.nccashier.wap.service.impl;

import com.yeepay.g3.app.nccashier.wap.vo.ResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.*;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.*;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.CflEasyService;
import com.yeepay.g3.app.nccashier.wap.vo.CardBinInfoResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardItemNecessary;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentAmountInfoVO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.service.ClfEasyFacade;
import com.yeepay.g3.util.ncmock.MockRemoteServiceFactory;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

import static com.yeepay.g3.app.nccashier.wap.utils.ConstantUtil.AJAX_FAILED;

@Service("cflEasyService")
public class CflEasyServiceImpl implements CflEasyService {


	private static final Logger logger = LoggerFactory.getLogger(CflEasyServiceImpl.class);


	private static final String CAHSHIER_URL = null;
	
	private ClfEasyFacade clfEasyFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, ClfEasyFacade.class);
	
	@Override
	public void getSupportCflEasyBankInfo(CflEasyBankInfoVO cflEasyBankInfoVO, String token, Long requestId) {
		CflEasySupportBankRequestDTO requestDTO = new CflEasySupportBankRequestDTO();
		requestDTO.setToken(token);
		requestDTO.setRequestId(requestId);
		CflEasyBankReponseDTO bankInfoDTO = clfEasyFacade.getSupportCflEasyBankInfo(requestDTO);
		transferCflBank(bankInfoDTO, cflEasyBankInfoVO);
	}

	@Override
	public void cflEasyPreRoute(CflEasyPreRouteRequestVO cflEasyPreRouteRequestVO,
			CflEasyOrderReponseVO cflEasyOrderReponseVO, Long requestId) {
		CflEasyPreRouterRequestDTO requestDTO = new CflEasyPreRouterRequestDTO();
		requestDTO.setToken(cflEasyPreRouteRequestVO.getToken());
		requestDTO.setRequestId(requestId);
		requestDTO.setBankCode(cflEasyPreRouteRequestVO.getBankCode());
		requestDTO.setCardNo(cflEasyPreRouteRequestVO.getCardno());
		requestDTO.setPeriod(cflEasyPreRouteRequestVO.getPeriod());
		CflEasyOrderResponseDTO orderResponseDTO = clfEasyFacade.preRouter(requestDTO);
		buildCflEasyOrderReponseVO(orderResponseDTO, cflEasyOrderReponseVO);
	}

	@Override
	public void cflEasyOrder(CflEasyOrderRequestVO cflEasyOrderRequestVO, CflEasyOrderReponseVO cflEasyOrderReponseVO,
			Long requestId) {
		CflEasyOrderRequestDTO requestDTO = new CflEasyOrderRequestDTO();
		requestDTO.setToken(cflEasyOrderRequestVO.getToken());
		requestDTO.setRequestId(requestId);
		CardInfoDTO cardInfo = new CardInfoDTO();
		cardInfo.setBankCode(cflEasyOrderRequestVO.getBankCode());
		cardInfo.setCardno(cflEasyOrderRequestVO.getCardno());
		cardInfo.setName(cflEasyOrderRequestVO.getOwner());
		cardInfo.setIdno(cflEasyOrderRequestVO.getIdno());
		cardInfo.setPhone(cflEasyOrderRequestVO.getPhoneNo());
		cardInfo.setCvv2(cflEasyOrderRequestVO.getCvv());
		cardInfo.setValid(cflEasyOrderRequestVO.getAvlidDate());
		requestDTO.setCardInfo(cardInfo);
		requestDTO.setPeriod(cflEasyOrderRequestVO.getPeriod());
		CflEasyOrderResponseDTO orderResponseDTO = clfEasyFacade.order(requestDTO);
		buildCflEasyOrderReponseVO(orderResponseDTO, cflEasyOrderReponseVO);
	}


	@Override
	public void clfEasySmsSend(CflEasySmsSendRequestVo cflEasySmsSendRequestVo, ResponseVO resVo) {

		BasicResponseDTO response = null;
		try {
			CflEasySmsSendRequestDTO requestDTO = new CflEasySmsSendRequestDTO();
			BeanUtils.copyProperties(cflEasySmsSendRequestVo,requestDTO);
			response = clfEasyFacade.sendSms(requestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-分期易发短验异常", t);
		}
		handleResponse(response);
		supplyResponseVo(response,resVo);
	}

	@Override
	public void clfEasyConfirmPay(CflEasyConfirmPayRequestVo cflEasyConfirmPayRequestDTO,ResponseVO resVo) {
		BasicResponseDTO response = null;
		try {
			CflEasyConfirmPayRequestDTO requestDTO = new CflEasyConfirmPayRequestDTO();
			BeanUtils.copyProperties(cflEasyConfirmPayRequestDTO,requestDTO);
			response = clfEasyFacade.confirmPay(requestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-分期易确认支付异常", t);
		}
		handleResponse(response);
		supplyResponseVo(response,resVo);
	}



	private void buildCflEasyOrderReponseVO(CflEasyOrderResponseDTO orderResponseDTO,
			CflEasyOrderReponseVO cflEasyOrderReponseVO) {
		if (orderResponseDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		if (orderResponseDTO.getProcessStatusEnum() == ProcessStatusEnum.FAILED) {
			throw new CashierBusinessException(orderResponseDTO.getReturnCode(), orderResponseDTO.getReturnMsg());
		}
		CardBinInfoResponseVO cardBin = new CardBinInfoResponseVO();
		cardBin.setBankCode(orderResponseDTO.getCardBin().getBankCode());
		cardBin.setBankName(orderResponseDTO.getCardBin().getBankName());
		cardBin.setCardLater4(orderResponseDTO.getCardBin().getCardlater4());
		cardBin.setCardType(orderResponseDTO.getCardBin().getCardType());
		cflEasyOrderReponseVO.setCardBin(cardBin);
		CardItemNecessary needItem = new CardItemNecessary();
		if(orderResponseDTO.getNeedItem()!=null){
			needItem.setNeedAvlidDate(orderResponseDTO.getNeedItem().getAvlidDateIsNeed());
			needItem.setNeedBankPWD(orderResponseDTO.getNeedItem().getBankPWDIsNeed());
			needItem.setNeedCvv(orderResponseDTO.getNeedItem().getCvvIsNeed());
			needItem.setNeedIdNo(orderResponseDTO.getNeedItem().getIdnoIsNeed());
			needItem.setNeedOwner(orderResponseDTO.getNeedItem().getOwnerIsNeed());
			needItem.setNeedPhoneNo(orderResponseDTO.getNeedItem().getPhoneNoIsNeed());
		}
		cflEasyOrderReponseVO.setNeedItem(needItem);
		cflEasyOrderReponseVO.setSmsType(orderResponseDTO.getSmsType());
	}

	private void transferCflBank(CflEasyBankReponseDTO bankInfoDTO, CflEasyBankInfoVO cflEasyBankInfo) {
		if (bankInfoDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		if (bankInfoDTO.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			if (CollectionUtils.isEmpty(bankInfoDTO.getCflEasyBankList())) {
				return;
			}
			for (CflEasyBankDTO cflEasyBankDTO : bankInfoDTO.getCflEasyBankList()) {
				if (CollectionUtils.isEmpty(cflEasyBankDTO.getPeriodAndPaymentInfos())) {
					continue;
				}
				cflEasyBankInfo.appendBankInfo(cflEasyBankDTO.getBankCode(), cflEasyBankDTO.getBankName());
				cflEasyBankInfo.appendAmountInfo(bankInfoDTO.getOrderAmount());
				for (InstallmentPeriodAndPaymentInfo periodAndPaymentInfo : cflEasyBankDTO.getPeriodAndPaymentInfos()) {
					InstallmentAmountInfoVO cflPeriodInfo = new InstallmentAmountInfoVO();
					cflPeriodInfo.setFirstPayment(periodAndPaymentInfo.getFirstPayment() == null ? null
							: periodAndPaymentInfo.getFirstPayment().toString());
					cflPeriodInfo.setTerminalPayment(periodAndPaymentInfo.getTerminalPayment() == null ? null
							: periodAndPaymentInfo.getTerminalPayment().toString());
					cflPeriodInfo.setPeriod(periodAndPaymentInfo.getPeriod());
					cflEasyBankInfo.appendPeriodInfo(cflEasyBankDTO.getBankCode(), cflPeriodInfo);
				}
			}
			return;
		}
		if (bankInfoDTO.getProcessStatusEnum() == ProcessStatusEnum.FAILED) {
			throw new CashierBusinessException(bankInfoDTO.getReturnCode(), bankInfoDTO.getReturnMsg());
		}

	}


	private void handleResponse(BasicResponseDTO response) {
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.FAILED) {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}


	private void supplyResponseVo(BasicResponseDTO response,ResponseVO resVo){
		if(ProcessStatusEnum.FAILED.equals(response.getProcessStatusEnum()) || StringUtils.isNotBlank(response.getReturnCode())){
			resVo.setBizStatus(AJAX_FAILED);
			resVo.setErrorMsg(response.getReturnMsg());
			resVo.setErrorCode(response.getReturnCode());
		}
	}



}
