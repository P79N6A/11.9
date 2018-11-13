package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;

public interface NcCashierCallBackFacade {

	//同步回调交易系统
	public boolean comfirmCallBack(OrderNoticeDTO orderNoticeDTO);
	
}
