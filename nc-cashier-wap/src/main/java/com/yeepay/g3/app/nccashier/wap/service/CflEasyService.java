package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.vo.ResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.*;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;

public interface CflEasyService {
	
	/**
	 * 获取可用的分期易银行及期数信息
	 * 
	 * @param cflEasyBankInfoVO
	 * @param token
	 * @param requestId
	 */
	void getSupportCflEasyBankInfo(CflEasyBankInfoVO cflEasyBankInfoVO, String token, Long requestId);
	
	/**
	 * 分期易预路由
	 * 
	 * @param cflEasyPreRouteRequestVO
	 * @param cflEasyOrderReponseVO
	 * @param requestId
	 */
	void cflEasyPreRoute(CflEasyPreRouteRequestVO cflEasyPreRouteRequestVO, CflEasyOrderReponseVO cflEasyOrderReponseVO, Long requestId);
	
	void cflEasyOrder(CflEasyOrderRequestVO cflEasyOrderRequestVO, CflEasyOrderReponseVO cflEasyOrderReponseVO, Long requestId);

	void clfEasySmsSend(CflEasySmsSendRequestVo cflEasySmsSendRequestVo , ResponseVO resVo);

	void clfEasyConfirmPay(CflEasyConfirmPayRequestVo cflEasyConfirmPayRequestDTO,ResponseVO resVo);


}
