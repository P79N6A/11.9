package com.yeepay.g3.core.payprocessor.external.service.impl;

import com.yeepay.g3.core.payprocessor.external.service.BankCardService;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.cwh.param.BankCardDetailDTO;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

/**
 * @author chronos.
 * @createDate 2016/11/11.
 */
@Service
public class BankCardServiceImpl extends AbstractService implements BankCardService {

    public static final Logger logger = PayLoggerFactory.getLogger(BankCardServiceImpl.class);

    @Override
    public BankCardDetailDTO getBankCardDetail(String cardId) {
        BankCardDetailDTO cardDetailDTO = null;
        try {
            cardDetailDTO = bankCardCwhFacade.getBankCardDetail(cardId);
        } catch (Throwable th){
            logger.error("[获取绑卡信息异常] - [cardId=" + cardId + "]", th);
        }
        return cardDetailDTO;
    }
}
