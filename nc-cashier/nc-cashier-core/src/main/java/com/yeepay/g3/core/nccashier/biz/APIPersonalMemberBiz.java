package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMemberBalancePayRequestDTO;

public interface APIPersonalMemberBiz {
	
	APIBasicResponseDTO balancePay(APIMemberBalancePayRequestDTO requestDTO);

}
