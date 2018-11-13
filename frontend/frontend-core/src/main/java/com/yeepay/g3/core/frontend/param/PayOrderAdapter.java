/**
 * 
 */
package com.yeepay.g3.core.frontend.param;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.util.ConstantUtils;
import com.yeepay.g3.facade.frontend.dto.ActiveScanJsapiRequestDTO;
import com.yeepay.g3.facade.frontend.dto.ActiveScanRequestDTO;
import com.yeepay.g3.facade.frontend.dto.AppPayRequestDTO;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.RefundStatusEnum;

import java.math.RoundingMode;

/**
 * 工具转换类
 * @author TML
 */
public class PayOrderAdapter {
	
	
//	public static PayOrder bulidPayOrder(ActiveScanRequestDTO activeScanRequestDTO,PayOrder payOrderOld){
//		
//		PayOrder payOrder = null;
//		if(payOrderOld != null){
//			payOrder = payOrderOld;
//		}else{
//			payOrder = new PayOrder();
//			payOrder.setRefundStatus(RefundStatusEnum.NONE.name());
//		}
//		
//		payOrder.setRequestSystem(activeScanRequestDTO.getRequestSystem());
//		payOrder.setRequestId(activeScanRequestDTO.getRequestId());
////		payOrder.setOrderNo();
//		payOrder.setOrderType(ConstantUtils.getActiveScanType(activeScanRequestDTO.getPlatformType()).name());
//		payOrder.setPlatformType(activeScanRequestDTO.getPlatformType().name());
//		payOrder.setCustomerNumber(activeScanRequestDTO.getCustomerNumber());
//		payOrder.setOutTradeNo(activeScanRequestDTO.getOutTradeNo());
//		payOrder.setTotalAmount(activeScanRequestDTO.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
////		payOrder.setTransactionId();
//		payOrder.setGoodsDescription(activeScanRequestDTO.getGoodsDescription());
//		payOrder.setPayStatus(PayStatusEnum.INIT.name());
//		payOrder.setPayerIp(activeScanRequestDTO.getPayerIp());
////		payOrder.setNotifyStatus();
////		payOrder.setOpenId();
////		payOrder.setPayBank();
////		payOrder.setPayBankcardType();
////		payOrder.setBankTotalCost();
//		return payOrder;
//	}
	
//	public static PayOrder bulidPayOrder(ActiveScanJsapiRequestDTO activeScanRequestDTO,PayOrder payOrderOld){
//		
//		
//		PayOrder payOrder = null;
//		if(payOrderOld != null){
//			payOrder = payOrderOld;
//		}else{
//			payOrder = new PayOrder();
//			payOrder.setRefundStatus(RefundStatusEnum.NONE.name());
//		}
//		
//		payOrder.setRequestSystem(activeScanRequestDTO.getRequestSystem());
//		payOrder.setRequestId(activeScanRequestDTO.getRequestId());
////		payOrder.setOrderNo();
//		payOrder.setOrderType(OrderType.JSAPI.name());
//		payOrder.setPlatformType(activeScanRequestDTO.getPlatformType().name());
//		payOrder.setCustomerNumber(activeScanRequestDTO.getCustomerNumber());
//		payOrder.setOutTradeNo(activeScanRequestDTO.getOutTradeNo());
//		payOrder.setTotalAmount(activeScanRequestDTO.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
////		payOrder.setTransactionId();
//		payOrder.setGoodsDescription(activeScanRequestDTO.getGoodsDescription());
//		payOrder.setPayStatus(PayStatusEnum.INIT.name());
////		payOrder.setNotifyStatus();
//		payOrder.setOpenId(activeScanRequestDTO.getOpenId());
//		payOrder.setPayerIp(activeScanRequestDTO.getPayerIp());
////		payOrder.setPayBank();
////		payOrder.setPayBankcardType();
////		payOrder.setBankTotalCost();
//		
//		return payOrder;
//	}

//	public static PayOrder bulidPayOrder(AppPayRequestDTO appPayRequestDTO,PayOrder payOrderOld) {
//
//		PayOrder payOrder = null;
//		if(payOrderOld != null){
//			payOrder = payOrderOld;
//		}else{
//			payOrder = new PayOrder();
//			payOrder.setRefundStatus(RefundStatusEnum.NONE.name());
//		}
//		
//		payOrder.setRequestSystem(appPayRequestDTO.getRequestSystem());
//		payOrder.setRequestId(appPayRequestDTO.getRequestId());
//		// payOrder.setOrderNo();
//		payOrder.setOrderType(OrderType.H5APP.name());
//		payOrder.setPlatformType(appPayRequestDTO.getPlatformType().name());
//		payOrder.setCustomerNumber(appPayRequestDTO.getCustomerNumber());
//		payOrder.setOutTradeNo(appPayRequestDTO.getOutTradeNo());
//		payOrder.setTotalAmount(appPayRequestDTO.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
//		// payOrder.setTransactionId();
//		payOrder.setGoodsDescription(appPayRequestDTO.getGoodsDescription());
//		payOrder.setPayStatus(PayStatusEnum.INIT.name());
//		payOrder.setPayerIp(appPayRequestDTO.getPayerIp());
//		// payOrder.setNotifyStatus();
//        // payOrder.setOpenId();
//		// payOrder.setPayBank();
//		// payOrder.setPayBankcardType();
//		// payOrder.setBankTotalCost();
//		payOrder.setPageCallBack(appPayRequestDTO.getPageCallBack());
//		payOrder.setPayLimitType(appPayRequestDTO.getPayLimitType() != null ?
//				appPayRequestDTO.getPayLimitType().name() : null);
//		return payOrder;
//	}

}
