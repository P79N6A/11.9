package com.yeepay.g3.core.nccashier.facade.impl;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-10-17 15:29
 **/

import com.yeepay.g3.core.nccashier.biz.ClfEasyBiz;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CflEasyBankReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyConfirmPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyPreRouterRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySupportBankRequestDTO;
import com.yeepay.g3.facade.nccashier.service.ClfEasyFacade;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-17 15:29
 **/

@Service("clfEasyFacade")
public class ClfEasyFacadeImpl implements ClfEasyFacade {

    @Resource
    private ClfEasyBiz clfEasyBiz;

    @Override
    public BasicResponseDTO sendSms(CflEasySmsSendRequestDTO requestDTO) {
        return clfEasyBiz.smsSend(requestDTO);
    }

    @Override
    public BasicResponseDTO confirmPay(CflEasyConfirmPayRequestDTO requestDTO) {
        return clfEasyBiz.confirmPay(requestDTO);
    }

	@Override
	public CflEasyBankReponseDTO getSupportCflEasyBankInfo(CflEasySupportBankRequestDTO requestDTO) {
		return clfEasyBiz.getSupportCflEasyBankInfo(requestDTO);
	}

	@Override
	public CflEasyOrderResponseDTO order(CflEasyOrderRequestDTO requestDTO) {
		return clfEasyBiz.order(requestDTO);
	}

	@Override
	public CflEasyOrderResponseDTO preRouter(CflEasyPreRouterRequestDTO requestDTO) {
		return clfEasyBiz.preRouter(requestDTO);
	}
}
