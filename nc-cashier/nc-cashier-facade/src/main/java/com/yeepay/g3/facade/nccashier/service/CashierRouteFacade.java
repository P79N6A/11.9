package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

import java.util.List;
import java.util.Map;

/**
 * 无卡收银台路由服务
 * @author xueping.ni
 */
public interface CashierRouteFacade {
	/**
	 * 收单请求接口
	 * 
	 * @param cashierRequestDTO
	 * @return
	 * @throws CashierBusinessException
	 * @throws SQLException
	 */
	public CashierResultDTO receiptRequest(CashierRequestDTO cashierRequestDTO)
			throws CashierBusinessException;

	
	/**
	 * 解密requestId
	 * @param encId
	 * @return
	 */
	public String routeDecrypt(String merchantNo,String encRequestId);
	
	/**
	 * 获取跳转url
	 */
	public String getURL(String cashierVer,String merchantNo,long id);
	
	/**
	 * 路由首次或者绑卡支付方式
	 * 
	 * @param requestId
	 * @return
	 */
	public BussinessTypeResponseDTO routerPayWay(long requestId,String cusType) throws CashierBusinessException;
	
	/**
	 * 获取支付扩展信息
	 * @param requestId
	 * @param tokenId
	 * @return
	 */
	public PayExtendInfo getPayExtendInfo(long requestId,String tokenId);

	/**
	 * 获取配置中心  新配置信息
	 * @param requestId
	 * @param tokenId
     * @return
     */
	public List<MerchantProductDTO> getNewMerchantInNetConfig(long requestId, String tokenId);
	
	/**
	 * 新request下单处理
	 * @param orderNo
	 * @return
	 */
	NewOrderRequestResponseDTO newOrderRequest(String orderNo,int selectOrderSysNo);
	
	/**
	 * YOP验签
	 * @param plaintext
	 * @param signature
	 * @return
	 */
	public boolean yopVerify(String appKey, String plaintext, String signature);
	
	/**
	 * YOP签名
	 * 
	 * @param signContent
	 * @return
	 */
	String yopSign(String signContent);

	/**
	 * 订单处理器下单处理
	 * @param orderProcessorRequestDTO
	 * @return
	 */
	public OrderProcessResponseDTO orderProcessorRequest(OrderProcessorRequestDTO orderProcessorRequestDTO);

}
