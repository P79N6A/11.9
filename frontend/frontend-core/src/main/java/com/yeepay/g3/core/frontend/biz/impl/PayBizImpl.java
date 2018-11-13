package com.yeepay.g3.core.frontend.biz.impl;

import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.biz.PayBiz;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.entity.PayRecord;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.util.FrontEndIdGenerator;
import com.yeepay.g3.core.frontend.util.RedisUtil;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayResponseDTO;

import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PayBizImpl extends AbstractBiz implements PayBiz{

	@Override
	public PayResponseDTO openPay(PayRequestDTO payRequestDTO) {
		PayOrder payOrder = null;
		PayRecord payRecord = null;
		PayResponseDTO payResponseDTO = new PayResponseDTO();
		try {
			//检查订单
			PayOrder payOrderOld = payOrderService.queryBySystemAndRequestId(payRequestDTO.getRequestSystem(), 
					payRequestDTO.getRequestId(), payRequestDTO.getPlatformType().name());
			//检查订单相关信息
			checkOrderInfo(payRequestDTO, payOrderOld);
			//构造订单
			payOrder = buildPayOrder(payRequestDTO, payOrderOld);
			payRecord = reOrderValidate(payOrder);
			if (payRecord == null) {
				//风控校验
				syncRiskControl(payRequestDTO,payOrder);
				// 转化订单
				payRecord = buildPayRecord(payOrder);
				if(payOrderOld==null){
					createPayOrder(payOrder);
				}
				//调用银行路由支付接口
				bankRouterPayService.openPay(payOrder, payRecord, payRequestDTO, payResponseDTO);
				updatePayOrder(payOrder);
				//缓存支付信息
				RedisUtil.pushPayUrlToRedis(payOrder, payRecord.getFrontValue());
				createPayRecord(payRecord);
				// 组装返回值结果
				buildResponse(null,payResponseDTO, payOrder, payRecord);
			} else {
				buildResponse(null,payResponseDTO, payOrder, payRecord);
			}
		} catch (FrontendBizException e) {
			processException(e, payResponseDTO, payOrder, payRecord);
		} catch (Throwable e) {
			processException(e, payResponseDTO,payOrder, payRecord);
		}
		return payResponseDTO;
	}

	@Override
	public PrePayResponseDTO prePayJsapi(PrePayRequestDTO prePayRequestDTO) {
		PrePayResponseDTO prePayResponseDTO = new PrePayResponseDTO();
		try {
			bankRouterPayService.prePayJsapi(prePayRequestDTO, prePayResponseDTO);
		} catch (FrontendBizException e) {
			processException(e, prePayResponseDTO, null, null);
		} catch (Throwable e) {
			processException(e, prePayResponseDTO,null, null);
		} finally{
			prePayResponseDTO.setCustomerNumber(prePayRequestDTO.getCustomerNumber());
			prePayResponseDTO.setTotalAmount(prePayRequestDTO.getTotalAmount());
			prePayResponseDTO.setDealUniqueSerialNo(prePayRequestDTO.getDealUniqueSerialNo());
		}
		return prePayResponseDTO;
	}

}
