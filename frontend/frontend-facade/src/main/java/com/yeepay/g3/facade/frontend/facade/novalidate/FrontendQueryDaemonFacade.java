package com.yeepay.g3.facade.frontend.facade.novalidate;

/**
 * 定时查询通道订单并补单
 * @author wangmeimei
 *
 */
public interface FrontendQueryDaemonFacade {
	
	/**
	 * 定时查单并补单
	 * @param minutes
	 */
	void queryBankOrderWX(int minutes);
	
	/**
	 * 定时查单并补单
	 * @param minutes
	 */
	void queryBankOrderZFB(int minutes);
	
	/**
	 * 定时查单并补单
	 * @param minutes
	 */
	void queryBankOrderUPOP(int minutes);
	
	/**
	 * 定时查单并补单
	 * @param minutes
	 */
	void queryBankOrderJD(int minutes);

	/**
	 * 定时查单并补单
	 * @param minutes
	 */
	void queryBankOrderQQ(int minutes);

	/**
	 * 定时查单并补单
	 * @param start
	 * @param end
	 */
	void queryBankOrderWX(String start,String end);

	/**
	 * 定时查单并补单
	 * @param start
	 * @param end
	 */
	void queryBankOrderZFB(String start,String end);
}
