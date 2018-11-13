package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessResponseDTO;

public interface NcCashierUserAccessBiz {

	RequestInfoDTO requestBaseInfo(String tokenId);

	UserAccessResponseDTO saveUserAccount(UserAccessDTO userAccessDTO);

	BasicResponseDTO clearRecordId(String tokenId);

}
