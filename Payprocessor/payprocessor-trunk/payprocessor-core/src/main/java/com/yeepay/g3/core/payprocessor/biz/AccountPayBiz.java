package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.facade.payprocessor.dto.AccountPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountSyncPayResponseDTO;

public interface AccountPayBiz {
    
    /**
     * 账户支付异步接口
     * 
     * @param requestDTO
     * @return {@link AccountPayResponseDTO}
     */
    AccountPayResponseDTO accountPay(AccountPayRequestDTO requestDTO);
    
    /**
     * 账户支付同步接口
     * 
     * @param requestDTO
     * @return {@link AccountSyncPayResponseDTO}
     */
    AccountSyncPayResponseDTO accountSyncPay(AccountPayRequestDTO requestDTO);

}
