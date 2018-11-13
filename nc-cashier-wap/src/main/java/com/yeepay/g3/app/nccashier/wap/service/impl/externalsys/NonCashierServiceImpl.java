package com.yeepay.g3.app.nccashier.wap.service.impl.externalsys;

import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.externalsys.NonCashierService;
import com.yeepay.g3.app.nccashier.wap.vo.externalsys.NcauthReceiveBankRequestParam;
import com.yeepay.g3.facade.ncauth.dto.ReceiveBankRequestDTO;
import com.yeepay.g3.facade.ncauth.facade.ReceiveBankNotifyFacade;
import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackDTO;
import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackResDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.service.FrontCallbackFacade;
import com.yeepay.g3.util.ncmock.MockRemoteServiceFactory;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

@Service("nonCashierService")
public class NonCashierServiceImpl implements NonCashierService {

	private static Logger logger = LoggerFactory.getLogger(NonCashierService.class);

	private static final String CAHSHIER_URL = null;

	private ReceiveBankNotifyFacade receiveBankNotifyFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, ReceiveBankNotifyFacade.class);
	
	private FrontCallbackFacade FrontCallbackFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, FrontCallbackFacade.class);

	@Override
	public String receiveIntelligentNetResultFrontCallback(String paymentNo) {
		IntelligentNetResultFrontCallbackDTO callbackDTO = new IntelligentNetResultFrontCallbackDTO();
		callbackDTO.setPaymentNo(paymentNo);
		IntelligentNetResultFrontCallbackResDTO response = null;
		try {
			response = FrontCallbackFacade.receiveIntelligentNetResultFrontCallback(callbackDTO);
		} catch (Throwable t) {
			logger.error("receiveIntelligentNetResultFrontCallback, paymentNo=" + paymentNo, t);
		}
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response.getFrontCallbackUrl();
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}
	
	
	 
	@Override
	public String receiveFrontendNotify(NcauthReceiveBankRequestParam ncauthReceiveBankRequestParam) {
		ReceiveBankRequestDTO requestDTO = new ReceiveBankRequestDTO();
		requestDTO.setErrorCode(ncauthReceiveBankRequestParam.getErrorCode());
		requestDTO.setErrorMsg(ncauthReceiveBankRequestParam.getErrorMsg());
		requestDTO.setRequestId(ncauthReceiveBankRequestParam.getRequestId());
		requestDTO.setSuccess(ncauthReceiveBankRequestParam.isSuccess());
		try {
			logger.info("receiveFrontendNotify ncauthReceiveBankRequestParam:{}", ncauthReceiveBankRequestParam);
			String url =  receiveBankNotifyFacade.receiveFrontendNotify(requestDTO);
			logger.info("receiveFrontendNotify url:{}", url);
			return url;
		} catch (Throwable t) {
			logger.error("receiveFrontendNotify 调用鉴权中心获取鉴权前端回调地址异常, requestId="
					+ ncauthReceiveBankRequestParam.getRequestId(), t);
			return null;
		}
	}

}
