package com.yeepay.g3.core.nccashier.biz;


import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CflEasyBankReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyConfirmPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyPreRouterRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySupportBankRequestDTO;

/**
 * @program: nc-cashier-parent
 * @description: 分期易非Api版本Biz
 * @author: jimin.zhou
 * @create: 2018-10-18 10:35
 **/
public interface ClfEasyBiz {

	/**
	 * 获取支持的分期易银行列表
	 * 
	 * @param requestDTO
	 * @return
	 */
	CflEasyBankReponseDTO getSupportCflEasyBankInfo(CflEasySupportBankRequestDTO requestDTO);

	/**
	 * 预路由
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
    
    /**
     * 分期易发短信
     * @param requestDTO
     * @return
     */
    BasicResponseDTO smsSend(CflEasySmsSendRequestDTO requestDTO);

    /**
     * 分期易确认支付
     * @param requestDTO
     * @return
     */
    BasicResponseDTO confirmPay(CflEasyConfirmPayRequestDTO requestDTO);

}
