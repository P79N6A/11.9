/**
 * 
 */
package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;

import java.math.BigDecimal;

/**
 * @author zhen.tan
 * 新下单处理服务
 *
 */
public interface NewOrderHandleService {

	/**
	 *  过滤商户入网配置  
	 *  @param detailInfo
	 * @return MerchantInNetConfigResult
	 */
	MerchantInNetConfigResult filterMerchantInNetConfig(OrderDetailInfoModel detailInfo);

	/**
	 * 补充配置中心信息到PaymentRequest
	 * @param paymentRequest
	 * @param orderInfo
	 * @return
	 */
	public String suppleConfigCenterInfoToPaymentRequest(PaymentRequest paymentRequest,OrderDetailInfoModel orderInfo);

	/**
	 *
	 * @return
	 */
	public BigDecimal queryUserFee(PaymentRequest paymentRequest,String callFeeItem);

	/**
	 * 订单信息reffer检查
	 * @param orderSysConfigDTO
	 * @param orderDetailInfoModel
	 */
	public void orderReferCheck(OrderSysConfigDTO orderSysConfigDTO,OrderDetailInfoModel orderDetailInfoModel);
		
	/**
	 * 反查订单，并校验签名商编
	 * 
	 * @param token
	 * @param bizType
	 * @param merchantNo
	 * @return
	 */
	OrderDetailInfoModel queryOrder(String requestMerchantNo, String token, String bizType, TransactionTypeEnum unsupportTransactionType);
}
