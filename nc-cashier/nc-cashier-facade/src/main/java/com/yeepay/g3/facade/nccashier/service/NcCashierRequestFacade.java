package com.yeepay.g3.facade.nccashier.service;

import java.sql.SQLException;

import com.yeepay.g3.facade.nccashier.dto.CashierRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierResultDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

/**
 * 定义与收单请求相关的facade
 * 
 * @author：xueping.ni
 * @since：2016年5月20日 下午4:29:59
 * @version:
 */
public interface NcCashierRequestFacade {
	/**
	 * 收单请求接口
	 * @param cashierRequestDTO
	 * @return
	 * @throws CashierBusinessException
	 * @throws SQLException
	 */
	public CashierResultDTO receiptRequest(CashierRequestDTO cashierRequestDTO);

}
