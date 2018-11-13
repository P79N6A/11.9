/**
 * 
 */
package com.yeepay.g3.core.frontend.biz.impl;

import com.yeepay.g3.core.frontend.biz.FrontMPayBiz;

/**
 * 微信支付biz接口
 * @author TML
 */
//@Service
@Deprecated
public class FrontMPayBizImpl extends AbstractBiz implements FrontMPayBiz{

	
//	@Override
//	public ActiveScanResponseDTO activeScan(ActiveScanRequestDTO activeScanRequestDTO) {
//		
//		PayOrder payOrder = null;
//		PayRecord payRecord = null;
//		boolean needUpdatePayOrder = true;
//		ActiveScanResponseDTO activeScanResponseDTO = new ActiveScanResponseDTO();
//		try {
//			//检查订单
//			PayOrder payOrderOld = payOrderService.queryBySystemAndRequestId(activeScanRequestDTO.getRequestSystem(), 
//					activeScanRequestDTO.getRequestId(), activeScanRequestDTO.getPlatformType().name());
//			//检查订单相关信息
//			checkOrderInfo(activeScanRequestDTO,payOrderOld,
//					ConstantUtils.getActiveScanType(activeScanRequestDTO.getPlatformType()));
//			payRecord = reOrderValidate(payOrderOld);
//			if (payRecord == null) {
//				// 转化订单
//				payOrder = PayOrderAdapter.bulidPayOrder(activeScanRequestDTO, payOrderOld);
//				createPayOrder(payOrder);
//				payRecord = buildPayRecord(payOrder);
//				//调用银行子系统支付宝接口
//				activeScanMPay(payOrder,payRecord, activeScanRequestDTO);
//				//缓存支付信息
//				RedisUtil.pushPayRecordToRedis(payOrder,payRecord);
//				// 组装返回值结果
//				updateResponse(null,activeScanResponseDTO, payOrder, payRecord);
//			} else {
//				updateResponse(null,activeScanResponseDTO, payOrderOld, payRecord);
//				payRecord = null;
//				needUpdatePayOrder = false;
//			}
//		} catch (FrontendBizException e) {
//			handleException(e,activeScanResponseDTO, payOrder, payRecord);
//		} catch (Throwable e) {
//			handleException(e, activeScanResponseDTO,payOrder, payRecord);
//		} finally {
//			updatePayOrder(needUpdatePayOrder, payOrder);
//			//创建流水记录
//			createPayRecord(payRecord);
//		}
//		return activeScanResponseDTO;
//	}
//	
//	
//
//	@Override
//	public ActiveScanResponseDTO activeScanJSAPI(ActiveScanJsapiRequestDTO activeScanJsapiRequestDTO) {
//
//		PayOrder payOrder = null;
//		PayRecord payRecord = null;
//		boolean needUpdatePayOrder = true;
//		ActiveScanResponseDTO activeScanResponseDTO = new ActiveScanResponseDTO();
//		try {
//			//检查订单
//			PayOrder payOrderOld = payOrderService.queryBySystemAndRequestId(activeScanJsapiRequestDTO.getRequestSystem(), 
//					activeScanJsapiRequestDTO.getRequestId(), activeScanJsapiRequestDTO.getPlatformType().name());
//			//检查订单相关信息
//			checkOrderInfo(activeScanJsapiRequestDTO,payOrderOld,OrderType.JSAPI);
//			//判断是否需要重新下单
//			payRecord = reOrderValidate(payOrderOld);
//			if (payRecord == null) {
//				// 转化订单
//				payOrder = PayOrderAdapter.bulidPayOrder(activeScanJsapiRequestDTO, payOrderOld);
//				//生成银行订单号
//				createPayOrder(payOrder);
//				payRecord = buildPayRecord(payOrder);
//				//调用公众号支付接口
//				activeScanMPay(payOrder, payRecord, activeScanJsapiRequestDTO);
//				//缓存支付信息
//				RedisUtil.pushPayRecordToRedis(payOrder,payRecord);
//				// 组装返回值结果
//				updateResponse(null, activeScanResponseDTO, payOrder, payRecord);
//			} else {
//				updateResponse(null,activeScanResponseDTO, payOrderOld, payRecord);
//				payRecord = null;
//				needUpdatePayOrder = false;
//			}
//		} catch (FrontendBizException e) {
//			handleException(e,activeScanResponseDTO, payOrder, payRecord);
//		} catch (Throwable e) {
//			handleException(e, activeScanResponseDTO,payOrder, payRecord);
//		} finally {
//			updatePayOrder(needUpdatePayOrder, payOrder);
//			//创建流水记录
//			createPayRecord(payRecord);
//		}
//		return activeScanResponseDTO;
//	}
//
//
//	@Override
//	public AppPayResponseDTO H5appPay(AppPayRequestDTO appPayRequestDTO) {
//		
//		PayOrder payOrder = null;
//		PayRecord payRecord = null;
//		boolean needUpdatePayOrder = true;
//		AppPayResponseDTO appPayResponseDTO = new AppPayResponseDTO();
//		try {
//			//检查订单
//			PayOrder payOrderOld = payOrderService.queryBySystemAndRequestId(appPayRequestDTO.getRequestSystem(), 
//					appPayRequestDTO.getRequestId(), appPayRequestDTO.getPlatformType().name());
//			//检查订单相关信息
//			checkOrderInfo(appPayRequestDTO,payOrderOld,OrderType.H5APP);
////			payRecord = reOrderValidate(payOrderOld);
////			if (payRecord == null) {
//				// 转化订单
//				payOrder = PayOrderAdapter.bulidPayOrder(appPayRequestDTO, payOrderOld);
//				createPayOrder(payOrder);
//				payRecord = buildPayRecord(payOrder);
//				H5appPayMPay(payOrder, payRecord, appPayRequestDTO);
////				//调用银行路由支付接口
////				pay(payOrder, payRecord, appPayRequestDTO);
//				//缓存支付信息
////				RedisUtil.pushPayRecordToRedis(payOrder.getOrderNo(),payRecord);
//				// 组装返回值结果
//				updateResponse(null, appPayResponseDTO, payOrder, payRecord);
////			} else {
////				updateResponse(null,appPayResponseDTO, payOrderOld, payRecord);
////				payRecord = null;
////				needUpdatePayOrder = false;
////			}
//		} catch (FrontendBizException e) {
//			handleException(e,appPayResponseDTO, payOrder, payRecord);
//		} catch (Throwable e) {
//			handleException(e, appPayResponseDTO,payOrder, payRecord);
//		} finally {
//			updatePayOrder(needUpdatePayOrder, payOrder);
//			//创建流水记录
//			createPayRecord(payRecord);
//		}
//		return appPayResponseDTO;
//	}
//
//	
//	@Override
//	public FrontendQueryResponseDTO queryMPayOrder(
//			FrontendQueryRequestDTO frontendQueryRequestDTO) {
//		FrontendQueryResponseDTO frontendQueryResponseDTO = new FrontendQueryResponseDTO();
//		PayOrder payOrder = null;
//		try {
//			//查询订单
//			payOrder = payOrderService.queryBySystemAndRequestId(frontendQueryRequestDTO.getRequestSystem(), frontendQueryRequestDTO.getRequestId(), 
//					frontendQueryRequestDTO.getPlatformType().name());
//            if(payOrder == null){
//				throw new FrontendBizException(ErrorCode.F0002004);
//			} 
//			// 组装返回值结果
//			updateResponse(null,frontendQueryResponseDTO, payOrder, null);
//		} catch (FrontendBizException e) {
//			handleException(e, frontendQueryResponseDTO, payOrder, null);
//		} catch (Throwable e) {
//			handleException(e, frontendQueryResponseDTO,payOrder, null);
//		} 
//		return frontendQueryResponseDTO;
//	}
//	
	
	
//	/**
//	 * APP支付
//	 */
//	private void H5appPayMPay(PayOrder payOrder,PayRecord payRecord, BasicRequestDTO basicRequestDTO){
//		bankPayService.H5appPay(payOrder,payRecord,basicRequestDTO);
//	}
	
	
//	/**
//	 * 主扫调用Mpay下单、公众号支付
//	 */
//	private void activeScanMPay(PayOrder payOrder,PayRecord payRecord, BasicRequestDTO basicRequestDTO){
//		if (PlatformType.WECHAT.name().equals(payOrder.getPlatformType())) {
//			mPayService.activeScanMPay(payOrder, payRecord);
//		} else {
//			bankPayService.activeScanMPay(payOrder,payRecord,basicRequestDTO);
//		}
//	}

}
