package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.APICashierPayResultEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class APIBaseBiz {

	private static Logger logger = LoggerFactory.getLogger(APIBaseBiz.class);
	
	protected void handleException(String bizType, APIBasicResponseDTO response, Throwable t) {
		logger.warn("订单异常，exception=", t);
		boolean yeepayBizException = (t instanceof YeepayBizException);
		String errorCode = yeepayBizException ? ((YeepayBizException) t).getDefineCode()
				: Errors.SYSTEM_EXCEPTION.getCode();
		String errorMsg = yeepayBizException ? ((YeepayBizException) t).getMessage() : Errors.SYSTEM_EXCEPTION.getMsg();
		response.setCode(errorCode);
		response.setMessage(errorMsg);
		if (StringUtils.isBlank(bizType)) {
			CashierBusinessException outerException = CommonUtil.handleApiCashierException(errorCode, errorMsg);
			response.setCode(outerException.getDefineCode());
			response.setMessage(outerException.getMessage());
		}
	}
	
	/**
	 * 将请求参数中的信息复制到返回值
	 * 
	 * @param responseDTO
	 * @param requestDTO
	 */
	protected void supplyResponse(APIBasicResponseDTO responseDTO, APIBasicRequestDTO requestDTO) {
		if (requestDTO != null) {
			BeanUtils.copyProperties(requestDTO, responseDTO);
		}
		// 来自商户的请求，成功的情况下，要置入成功码CAS00000
		if(StringUtils.isBlank(requestDTO.getBizType()) && StringUtils.isBlank(responseDTO.getCode())){
			responseDTO.setCode(APICashierPayResultEnum.SUCCESS.getCode());
			responseDTO.setMessage(APICashierPayResultEnum.SUCCESS.getMessage());
		}
	}

}
