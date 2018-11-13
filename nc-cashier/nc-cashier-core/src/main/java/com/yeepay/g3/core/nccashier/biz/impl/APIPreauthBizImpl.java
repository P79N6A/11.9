package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.APIPreauthBiz;
import com.yeepay.g3.core.nccashier.enumtype.OrderSystemPreauthStatusEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.service.PreauthService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteResDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 预授权biz层实现
 * 
 * @author duangduang
 *
 */
@Service("apiPreauthBiz")
public class APIPreauthBizImpl extends APIBaseBiz implements APIPreauthBiz {

	@Resource
	private NewOrderHandleService newOrderHandleService;

	@Resource
	private PreauthService preauthService;

	@Override
	public APIPreauthResponseDTO preauthCancel(APIBasicRequestDTO requestDTO) {
		APIPreauthResponseDTO responseDTO = new APIPreauthResponseDTO();
		try {
			APIPreauthCancelRequestDTO preauthCancelRequestDTO = (APIPreauthCancelRequestDTO) requestDTO;
			// 参数校验
			validateParam(requestDTO);
			// 反查订单
			OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(),
					requestDTO.getBizType(), null);
			// 预授权撤销操作
			preauthService.preauthCancel(preauthCancelRequestDTO, responseDTO, orderInfo);
		} catch (Throwable t) {
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, requestDTO);
		return responseDTO;
	}

	private void validateParam(APIBasicRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
		// 商编的转化 —— 将OPR：等前缀去掉
		requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
		NcCashierLoggerFactory.TAG_LOCAL.set("[queryResult],token=" + requestDTO.getToken() + "]");
	}

	@Override
	public APIPreauthCompleteResponseDTO preauthComplete(APIBasicRequestDTO requestDTO) {
		APIPreauthCompleteResponseDTO responseDTO = new APIPreauthCompleteResponseDTO();
		try {
			APIPreauthCompleteRequestDTO completeRequestDTO = (APIPreauthCompleteRequestDTO) requestDTO;
			// 参数校验
			validateParam(requestDTO);
			// 反查订单
			OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(),
					requestDTO.getBizType(), null);
			// 预授权完成操作
			preauthService.preauthComplete(completeRequestDTO, responseDTO, orderInfo);
		} catch (Throwable t) {
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, requestDTO);
		return responseDTO;
	}

	@Override
	public APIPreauthResponseDTO preauthCompleteCancel(APIBasicRequestDTO requestDTO) {
		APIPreauthResponseDTO responseDTO = new APIPreauthResponseDTO();
		try {
			APIPreauthCompleteCancelRequestDTO preauthCancelRequestDTO = (APIPreauthCompleteCancelRequestDTO) requestDTO;
			// 参数校验
			validateParam(requestDTO);
			// 反查订单
			OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(),
					requestDTO.getBizType(), null);
			// 预授权完成撤销操作
			preauthService.preauthCompleteCancel(preauthCancelRequestDTO, responseDTO, orderInfo);
		} catch (Throwable t) {
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, requestDTO);
		return responseDTO;
	}

	// 以上为第一版本，为电信行业线提供的预授权完成、撤销、完成撤销接口；以下是给商户提供的前置收银台预授权相关接口
	
	private void firstValidateParam(APIPreauthFirstRequestDTO requestDTO){
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
		// 商编的转化 —— 将OPR：等前缀去掉
		requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
		NcCashierLoggerFactory.TAG_LOCAL.set("[preAuthFirstRequest],token=" + requestDTO.getToken() + "]");
	}

	private void bindValidateParam(APIPreauthBindRequestDTO requestDTO){
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
		// 商编的转化 —— 将OPR：等前缀去掉
		requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
		NcCashierLoggerFactory.TAG_LOCAL.set("[preAuthBindRequest],token=" + requestDTO.getToken() + "]");
	}

	private void smsSendValidateParam(APIPreauthSmsSendRequestDTO requestDTO){
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
		requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
		NcCashierLoggerFactory.TAG_LOCAL.set("[preauthSmsSend],token=" + requestDTO.getToken() + "]");
	}

	private void confirmValidateParam(APIPreauthConfirmRequestDTO requestDTO){
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
		requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
		NcCashierLoggerFactory.TAG_LOCAL.set("[preAuthOrderConfirm],token=" + requestDTO.getToken() + "]");
	}

	@Override
	public APIPreauthPaymentResponseDTO preAuthFirstRequest(APIPreauthFirstRequestDTO requestDTO) {
		APIPreauthPaymentResponseDTO responseDTO = new APIPreauthPaymentResponseDTO();
		try{
			// 参数校验
			firstValidateParam(requestDTO);
			// 反查订单并校验订单是否为预授权订单，状态是否合法
			OrderDetailInfoModel orderInfo = preauthService.queryOrder(requestDTO, new OrderSystemPreauthStatusEnum[]{OrderSystemPreauthStatusEnum.WAITPREAUTH});
			//预授权下单
			preauthService.preAuthFirstRequestAPI(requestDTO,responseDTO,orderInfo);
		} catch (Throwable t){
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}

		supplyResponse(responseDTO,requestDTO);

		return responseDTO;
	}


	@Override
	public APIPreauthPaymentResponseDTO preAuthBindRequest(APIPreauthBindRequestDTO requestDTO) {
		APIPreauthPaymentResponseDTO responseDTO = new APIPreauthPaymentResponseDTO();
		try {
			// 参数校验
			bindValidateParam(requestDTO);
			// 反查订单并校验订单是否为预授权订单，状态是否合法
			OrderDetailInfoModel orderInfo = preauthService.queryOrder(requestDTO, new OrderSystemPreauthStatusEnum[]{OrderSystemPreauthStatusEnum.WAITPREAUTH});
			// 预授权下单
			preauthService.preAuthBindRequestAPI(requestDTO,responseDTO,orderInfo);
		} catch (Throwable t){
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO,requestDTO);
		return responseDTO;
	}


	@Override
	public APIBasicResponseDTO preauthSmsSend(APIPreauthSmsSendRequestDTO requestDTO) {
		APIBasicResponseDTO responseDTO = new APIBasicResponseDTO();
		try {
			// 参数校验
			smsSendValidateParam(requestDTO);
			// 反查订单并校验订单是否为预授权订单，状态是否合法
			OrderDetailInfoModel orderInfo = preauthService.queryOrder(requestDTO, new OrderSystemPreauthStatusEnum[]{OrderSystemPreauthStatusEnum.WAITPREAUTH});
			// 预授权发送短验
			preauthService.preauthSmsSendAPI(requestDTO,responseDTO,orderInfo);
		} catch (Throwable t) {
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, requestDTO);
		return responseDTO;
	}

	@Override
	public APIPreauthConfirmResponseDTO preAuthOrderConfirm(APIPreauthConfirmRequestDTO requestDTO) {

		APIPreauthConfirmResponseDTO responseDTO = new APIPreauthConfirmResponseDTO();
		try{
			// 参数校验
			confirmValidateParam(requestDTO);
			// 反查订单并校验订单是否为预授权订单，状态是否合法
			OrderDetailInfoModel orderInfo = preauthService.queryOrder(requestDTO, new OrderSystemPreauthStatusEnum[]{OrderSystemPreauthStatusEnum.WAITPREAUTH});
			// 确认支付 支付成后绑卡
			preauthService.preAuthOrderConfirmAPI(requestDTO, responseDTO, orderInfo);
		}catch (Throwable t){
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, requestDTO);
		return responseDTO;
		
	}

	@Override
	public APIPreauthCompleteResDTO complete(APIPreauthCompleteReqDTO reqDTO) {
		APIPreauthCompleteResDTO responseDTO = new APIPreauthCompleteResDTO();
		try {
			// 参数校验
			validatePreauthRequestParam(reqDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[complete],token=" + reqDTO.getToken() + "]");
			// 反查订单并校验预授权状态：只有处于预授权发起成功状态的订单才能进行预授权完成
			OrderDetailInfoModel orderInfo = preauthService.queryOrder(reqDTO, new OrderSystemPreauthStatusEnum[]{OrderSystemPreauthStatusEnum.PREAUTH});
			// 备注：讨论过这个步骤不关心产品开通配置
			// 预授权完成，能走到这一步，说明还没有预授权撤销、预授权完成成功
			preauthService.preauthComplete(reqDTO, responseDTO, orderInfo);
		} catch (Throwable t) {
			String bizType = (reqDTO == null ? null : reqDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, reqDTO);
		return responseDTO;
	}
	
	public void supplyResponse(APIPreauthCompleteResDTO responseDTO, APIPreauthCompleteReqDTO requestDTO) {
		String completeRecordId = responseDTO.getRecordId();
		super.supplyResponse(responseDTO, requestDTO);
		responseDTO.setRecordId(completeRecordId);
	}
	
	private void validatePreauthRequestParam(APIBasicRequestDTO basicRequestDTO){
		if (basicRequestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		basicRequestDTO.validate();
		basicRequestDTO.setMerchantNo(CommonUtil.formatMerchantNo(basicRequestDTO.getMerchantNo()));
	}


	@Override
	public APIPreauthCancelResDTO cancle(APIPreauthCancelReqDTO reqDTO) {
		APIPreauthCancelResDTO responseDTO = new APIPreauthCancelResDTO();
		try {
			// 参数校验
			validatePreauthRequestParam(reqDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[cancle],token=" + reqDTO.getToken() + "]");
			// 反查订单并校验预授权状态：只有处于预授权发起成功状态的订单才能进行预授权完成
			OrderDetailInfoModel orderInfo = preauthService.queryOrder(reqDTO, new OrderSystemPreauthStatusEnum[]{OrderSystemPreauthStatusEnum.PREAUTH});
			// 备注：讨论过这个步骤不关心产品开通配置
			// 预授权撤销，能走到这一步，说明还没有预授权撤销、预授权完成成功
			preauthService.preauthCancel(reqDTO, responseDTO, orderInfo);
		} catch (Throwable t) {
			String bizType = (reqDTO == null ? null : reqDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, reqDTO);
		return responseDTO;
	}


	@Override
	public APIPreauthCompleteCancelResDTO completeCancle(APIPreauthCompleteCancelReqDTO reqDTO) {
		APIPreauthCompleteCancelResDTO responseDTO = new APIPreauthCompleteCancelResDTO();
		try {
			// 参数校验
			validatePreauthRequestParam(reqDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[completeCancle],token=" + reqDTO.getToken() + "]");
			// 反查订单并校验预授权状态：只有处于预授权发起成功状态的订单才能进行预授权完成
			OrderDetailInfoModel orderInfo = preauthService.queryOrder(reqDTO, new OrderSystemPreauthStatusEnum[]{OrderSystemPreauthStatusEnum.PREAUTHCOMPLETE,OrderSystemPreauthStatusEnum.PREAUTHCOMPLETEPAUSE});
			// 备注：讨论过这个步骤不关心产品开通配置
			// 预授权撤销，能走到这一步，说明还没有预授9权撤销、预授权完成成功
			preauthService.preauthCompleteCancel(reqDTO, responseDTO, orderInfo);
		} catch (Throwable t) {
			String bizType = (reqDTO == null ? null : reqDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, reqDTO);
		return responseDTO;
	}
}
