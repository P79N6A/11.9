package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.facade.nccashier.dto.APIPayQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPayResultQueryResponseDTO;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIResultQueryBiz;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIResultQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;

@Service("apiResultQueryBiz")
public class APIResultQueryBizImpl extends APIBaseBiz implements APIResultQueryBiz {

//	private static Logger logger = LoggerFactory.getLogger(APIResultQueryBiz.class);

	@Resource
	private NewOrderHandleService newOrderHandleService;


	@Resource
	private QueryResultService queryResultService;

	@Resource
	private APIPayResultQueryService apiPayResultQueryService;



	@Override
	public APIResultQueryResponseDTO queryResult(APIBasicRequestDTO requestDTO) {
		APIResultQueryResponseDTO response = new APIResultQueryResponseDTO();
		try {
			validateParam(requestDTO);
			OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(),
					requestDTO.getBizType(), null);
			queryResultService.queryOrderResult(orderInfo, response);
		} catch (Throwable t) {
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, response, t);
		}
		supplyResponse(response, requestDTO);
		return response;
	}

	@Override
	public APIPayResultQueryResponseDTO queryPayResultByRecordId(APIPayQueryRequestDTO requestDTO) {
		APIPayResultQueryResponseDTO response = new APIPayResultQueryResponseDTO();
		try{
			validateQueryPayResultParam(requestDTO);
			PaymentRecord record = apiPayResultQueryService.getRecordByIdUseRedis(requestDTO.getRecordId());
			response = apiPayResultQueryService.gerPayResultByQueryPP(record);
			apiPayResultQueryService.saveRedisWhenQuerySucess(record);
		} catch (Throwable t) {
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, response, t);
		}
		supplyResponse(response, requestDTO);
		return response;
	}

	private void supplyResponse(APIResultQueryResponseDTO response, APIBasicRequestDTO requestDTO) {
		if (requestDTO != null) {
			BeanUtils.copyProperties(requestDTO, response);
		}
	}

	/**
	 * 入参校验
	 * 
	 * @param requestDTO
	 */
	private void validateParam(APIBasicRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		if (StringUtils.isBlank(requestDTO.getToken())) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", token不能为空");
		}
		requestDTO.validate();
		requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
		NcCashierLoggerFactory.TAG_LOCAL.set("[queryResult],token=" + requestDTO.getToken() + "]");
	}


	private void validateQueryPayResultParam(APIPayQueryRequestDTO requestDTO){
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
	}

}
