package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CflEasyBankReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyConfirmPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyPreRouterRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySupportBankRequestDTO;

/**
 * @program: nc-cashier-parent
 * @description: 分期易Facade
 * @author: jimin.zhou
 * @create: 2018-10-17 15:27
 **/
public interface ClfEasyFacade {

	CflEasyBankReponseDTO getSupportCflEasyBankInfo(CflEasySupportBankRequestDTO requestDTO);
	
	/**
	 * 预路由，获取必填项 TODO 如果是绑卡的场景当如何？
	 * 
	 * @param requestDTO
	 * @return
	 */
	CflEasyOrderResponseDTO preRouter(CflEasyPreRouterRequestDTO requestDTO);
	
	/**
	 * 下单
	 * 
	 * @param requestDTO
	 * @return
	 */
	CflEasyOrderResponseDTO order(CflEasyOrderRequestDTO requestDTO);
	
    BasicResponseDTO sendSms(CflEasySmsSendRequestDTO requestDTO);

    BasicResponseDTO confirmPay(CflEasyConfirmPayRequestDTO requestDTO);

}
