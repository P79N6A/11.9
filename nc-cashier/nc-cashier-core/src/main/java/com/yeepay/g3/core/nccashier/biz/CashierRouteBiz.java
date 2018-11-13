package com.yeepay.g3.core.nccashier.biz;

import com.ibm.db2.jcc.am.SqlIntegrityConstraintViolationException;
import com.yeepay.g3.core.nccashier.entity.MerchantProductInfo;
import com.yeepay.g3.facade.nccashier.dto.BussinessTypeResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierResultDTO;
import com.yeepay.g3.facade.nccashier.dto.NewOrderRequestResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

import java.util.List;
import java.util.Map;

public interface CashierRouteBiz {

	/**
	 * 支付请求
	 * @param cashierRequestDTO
	 * @return
	 * @throws SqlIntegrityConstraintViolationException 
	 */
	public CashierResultDTO receiptRequest(CashierRequestDTO cashierRequestDTO) throws CashierBusinessException;
	
	/**
	 * 解密
	 * @param encId
	 * @return
	 */
	public String routeDecrypt(String merchantNo,String encRequestId);
	/**
	 * 获取跳转url
	 */
	public String getURL(String cashierVer, String merchantNo, long id);
	
	/**
	 * 获取支付类型
	 * @param requestId
	 * @return
	 */
	public BussinessTypeResponseDTO routerPayWay(long requestId,String cusType);
	
	/**
	 * 获取支付扩展信息
	 * @param requestId
	 * @param tokenId
	 * @return
	 */
	public PayExtendInfo getPayExtendInfo(long requestId,String tokenId);


	/**
	 * 获取配置中心 新配置信息
	 * @param tokenId
	 * @return
     */
	public List<MerchantProductDTO> getNewMerchantInNetConfig(long requestId, String tokenId);
	
	/**
	 * paymentRequest下单处理(订单信息来自反查交易系统)
	 * @param orderNo
	 * @return
	 */
	NewOrderRequestResponseDTO newOrderRequest(String orderNo,int orderSysNo);

	/**
	 * paymentRequest下单处理(订单信息来自反查交易系统+前端URL)
	 * @param orderProcessorRequestDTO
	 * @return
	 */
	OrderProcessResponseDTO orderProcessRequest(OrderProcessorRequestDTO orderProcessorRequestDTO);
}
