package com.yeepay.g3.core.frontend.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.frontend.biz.NotifyBiz;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.core.frontend.util.log.LogInfoEncryptUtil;
import com.yeepay.g3.facade.frontend.dto.BankNotifyRequestDTO;
import com.yeepay.g3.facade.frontend.dto.BankNotifyResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendNotifyFacade;

@Service
public class FrontendNotifyFacadeImpl implements FrontendNotifyFacade {
	
	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendNotifyFacadeImpl.class);

	@Autowired
	private NotifyBiz notifyBiz;
	
	@Override
	public BankNotifyResponseDTO receiveBankNotify(BankNotifyRequestDTO bankNotifyRequestDTO) {
		try{
            FeLoggerFactory.TAG_LOCAL.set("[银行子系统回调]");
            logger.info(LogInfoEncryptUtil.getLogStringforBankNotify(bankNotifyRequestDTO));
            logger.info(String.format("[monitor],event:%s,merchantno:%s,requestno:%s,payno:%s,status:%s", "FE_Bank_Result", bankNotifyRequestDTO.getCustomerNumber(), bankNotifyRequestDTO.getOutTradeNo(), 
            		bankNotifyRequestDTO.getRequestId(), bankNotifyRequestDTO.getPayStatus()));
            BankNotifyResponseDTO bankNotifyResponseDTO = notifyBiz.receiveBankNotify(bankNotifyRequestDTO);
            logger.info(bankNotifyResponseDTO.toString());
            return bankNotifyResponseDTO;
        }finally {
        	FeLoggerFactory.TAG_LOCAL.set(null);
        }
	}

}
