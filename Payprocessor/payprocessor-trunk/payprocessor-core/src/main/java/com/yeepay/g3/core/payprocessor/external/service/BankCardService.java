package com.yeepay.g3.core.payprocessor.external.service;

import com.yeepay.g3.facade.cwh.param.BankCardDetailDTO;

/**
 * @author chronos.
 * @createDate 2016/11/11.
 */
public interface BankCardService {

    /**
     * 获取卡账户详细信息
     * @param cardId
     * @return
     */
    BankCardDetailDTO getBankCardDetail(String cardId);
}
