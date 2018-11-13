package com.yeepay.g3.core.nccashier.gateway.service;


import com.yeepay.g3.facade.fee.front.dto.CalFeeByRoleRequestDTO;
import com.yeepay.g3.facade.fee.front.dto.CalFeeByRoleResultDTO;
import com.yeepay.g3.facade.fee.front.dto.CalcuateFeeRequestDTO;
import com.yeepay.g3.facade.fee.front.dto.CalcuateFeeResultDTO;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.gateway.service
 *
 * @author pengfei.chen
 * @since 17/3/8 14:56
 */
public interface CallFeeService {

    /**
     * 获取用户手续费
     * @param calFeeDTO
     * @return
     */
    public CalcuateFeeResultDTO getUserFee(CalcuateFeeRequestDTO calFeeDTO);
    
    /**
	 * 调用三代计费系统获取手续费
	 * @param requestDTO
	 * @return
	 */
	CalFeeByRoleResultDTO callFeeByRole(CalFeeByRoleRequestDTO requestDTO);
	
}
