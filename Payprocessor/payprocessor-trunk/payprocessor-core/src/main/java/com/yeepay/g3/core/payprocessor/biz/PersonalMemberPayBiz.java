package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayResponseDTO;

public interface PersonalMemberPayBiz {
    
    PersonalMemberSyncPayResponseDTO personalMemberSyncPay(PersonalMemberSyncPayRequestDTO requestDTO);

}
