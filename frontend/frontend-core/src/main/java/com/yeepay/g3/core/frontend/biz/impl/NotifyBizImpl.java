/**
 * 
 */
package com.yeepay.g3.core.frontend.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.biz.NotifyBiz;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.entity.Promotion;
import com.yeepay.g3.core.frontend.log.FrontEndTracer;
import com.yeepay.g3.core.frontend.util.ConstantUtils;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.*;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TML
 */
@Service
public class NotifyBizImpl extends AbstractBiz implements NotifyBiz{

	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(NotifyBizImpl.class);

	public BankNotifyResponseDTO receiveBankNotify(BankNotifyRequestDTO bankNotifyRequestDTO) {
		PayOrder payOrder = null;
		boolean needUpdatePayOrder = true;
		BankNotifyResponseDTO bankNotifyResponseDTO = new BankNotifyResponseDTO();
		Map<String, String> extParam = new HashMap<String, String>();// 银联返回扩展信息，例如交易流水号(交易凭证号)、报备商户号(银行商户号)、付款方账户号(卡号)、优惠信息 added by zhijun.wang 2017-05-31
		List<Promotion> promotions = null;
		try {
			//检查订单 注：优先根据requestId(也就是payOrderNo)获取订单，获取不到再根据orderNo获取(兼容历史数据)
			payOrder = payOrderService.queryByPayOrderNo(bankNotifyRequestDTO.getRequestId(), ConstantUtils.getPlatformType(bankNotifyRequestDTO.getPayInterface()));
			if(payOrder == null){
				payOrder = payOrderService.queryByOrderNo(bankNotifyRequestDTO.getOrderNo(), ConstantUtils.getPlatformType(bankNotifyRequestDTO.getPayInterface()));
			}
			needUpdatePayOrder = isUpdatePayOrder(payOrder);
			//检查订单相关信息
			checkNotifyPayOrder(payOrder);
			//接收到回调的有效订单，增加埋点日志
			if(ConstantUtils.enableSpanLog()) {
                FrontEndTracer.frontendOrderCompleteSpan(payOrder.getCustomerNumber(), payOrder.getOutTradeNo(),
                        payOrder.getPlatformType(), payOrder.getOrderType(),
                        bankNotifyRequestDTO.getPayStatus() == null ? "" : bankNotifyRequestDTO.getPayStatus().name(),
                        "", "");
            }
			//更新订单信息
			updateNotifyOrder(payOrder, bankNotifyRequestDTO, extParam);
			// 组装返回值结果
			updateResponse(null, bankNotifyResponseDTO,payOrder,null);
			//组装优惠券信息
			promotions = buildPromotion(bankNotifyRequestDTO,payOrder);
		} catch (FrontendBizException e) {
			handleException(e,bankNotifyResponseDTO, payOrder, null);
		} catch (Throwable e) {
			handleException(e, bankNotifyResponseDTO,payOrder, null);
		} finally {
			updatePayOrder(needUpdatePayOrder, payOrder);
			asyncProcess(payOrder, extParam,bankNotifyRequestDTO.getPromotionInfoDTOS());
		}
		return bankNotifyResponseDTO;
	}

	@Override
	public FeOperationResponseDTO notifyOrders(FeOperationRequestDTO requestDTO) {
		logger.info("[size = " + requestDTO.getOrderNos().size() + "]");
		FeOperationResponseDTO responseDTO = new FeOperationResponseDTO();
		for (BasicOperationDTO operate : requestDTO.getOrderNos()) {
			try {
				logger.info("[" + operate.getRequestId() + "] - [START]");
				PayOrder order = queryBySystemAndRequestId(operate.getRequestSystem(),
						operate.getRequestId(), operate.getPlatformType());
				if (!PayStatusEnum.SUCCESS.name().equals(order.getPayStatus())){
					responseDTO.setIgnore(responseDTO.getIgnore() + 1);
					continue;
				}
				//发送MQ消息
				String extParamString = order.getExtParam();
				Map<String, String> extParm = (Map<String, String>)JSONObject.parse(extParamString);
				notifyAndUpdate(order, extParm,null);// changed by zhijun.wang 2017-05-31
				responseDTO.setSuccess(responseDTO.getSuccess() + 1);
				logger.info("[" + operate.getRequestId() + "] - [SUCCESS]");
			}  catch (FrontendBizException e) {
				logger.error("[" + operate.getRequestId() +"] - [FAILURE]", e);
				responseDTO.getErrorList().add("[" + operate.getRequestId() + "]" + e.getMessage());
			} catch (Throwable th) {
				logger.error("[" + operate.getRequestId() +"] - [FAILURE]", th);
				responseDTO.getErrorList().add("[" + operate.getRequestId() + "]" + th.getMessage());
			}
		}
		return responseDTO;
	}

	@Override
	public void notifyMqByDate(Date start, Date end, String platformType) {
		List<PayOrder> orderList = payOrderService.queryUnNotifyByDate(start, end, platformType);
		if (orderList == null || orderList.size()<1){
			return;
		}
		for (PayOrder order : orderList){
			notifyAndUpdate(order, null,null);// changed by zhijun.wang 2017-05-31
			asyncRiskControl(order);
		}
	}

}
