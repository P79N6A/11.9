package com.yeepay.g3.core.nccashier.service;


import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierResponseDTO;

/**
 * API收银台-聚合及被扫支付-业务逻辑接口
 * Created by ruiyang.du on 2017/6/28.
 */
public interface APICashierOpenAndPassivePayService {


    /**
     * 聚合及被扫支付-支付请求
     * @param apiCashierRequestDTO
     * @return
     */
    UnifiedAPICashierResponseDTO pay(UnifiedAPICashierRequestDTO apiCashierRequestDTO);

}
