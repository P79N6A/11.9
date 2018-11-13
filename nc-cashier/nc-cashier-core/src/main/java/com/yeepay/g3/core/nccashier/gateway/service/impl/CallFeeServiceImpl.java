package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.gateway.service.CallFeeService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.fee.front.dto.CalFeeByRoleRequestDTO;
import com.yeepay.g3.facade.fee.front.dto.CalFeeByRoleResultDTO;
import com.yeepay.g3.facade.fee.front.dto.CalcuateFeeRequestDTO;
import com.yeepay.g3.facade.fee.front.dto.CalcuateFeeResultDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;


import org.springframework.stereotype.Service;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.gateway.service.impl
 *
 * @author pengfei.chen
 * @since 17/3/8 14:56
 */
@Service("callFeeService")
public class CallFeeServiceImpl extends NcCashierBaseService implements CallFeeService {
    Logger logger = LoggerFactory.getLogger(CallFeeServiceImpl.class);

    public CalcuateFeeResultDTO getUserFee(CalcuateFeeRequestDTO calcuateFeeRequestDTO){
        CalcuateFeeResultDTO calFeeResultDTO;
        try {
            calFeeResultDTO = calFeeTransactionFacade.calFee(calcuateFeeRequestDTO);
        }catch (Throwable e){
            logger.error("获取用户手续费异常,merchantOrderId:{}",calcuateFeeRequestDTO.getOrderNumber());
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        if(calFeeResultDTO == null){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }else {
            return calFeeResultDTO;
        }
    }

	@Override
	public CalFeeByRoleResultDTO callFeeByRole(CalFeeByRoleRequestDTO requestDTO) {
		CalFeeByRoleResultDTO response = null;
		try {
			 response = calFeeTransactionFacade.calFeeByRole(requestDTO);
		} catch (Throwable t) {
			logger.error("交易订单号=" + requestDTO.getOrderNumber() + ",商编=" + requestDTO.getCustomerNumber()
					+ ",调用三代计费系统calFeeByRole接口异常,", t);
			if(t instanceof RuntimeException){
				RuntimeException runtimeException = (RuntimeException) t;
				if(StringUtils.isNotBlank(runtimeException.getMessage()) && runtimeException.getMessage().contains("费率模型不存在")){
					return null;
				}
			}
		}
		if (response == null) { 
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		return response;
	}
}