package com.yeepay.g3.facade.nccashier.service;
import com.yeepay.g3.facade.nccashier.dto.TradeNoticeDTO;

public interface QueryPaymentResultFacade {
	
	// 以交易系统结果显示页面的查询接口
	TradeNoticeDTO queryPayResult(String tradeSysOrderId, String tradeSysNo);

	/**
	 * 获取前端回调地址
	 * @param tradeSysOrderId	交易订单号
	 * @param tradeSysNo		交易系统编码
	 * @return					前端回调地址
	 */
	String getFrontCallbackUrl(String tradeSysOrderId, String tradeSysNo);
	
	
}
