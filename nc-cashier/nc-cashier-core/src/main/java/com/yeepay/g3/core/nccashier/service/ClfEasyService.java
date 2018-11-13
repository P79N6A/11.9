package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.enumtype.SynTypeEnum;
import com.yeepay.g3.core.nccashier.vo.CardInfo;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasyConfirmResponseDTO;

/**
 * @program: nc-cashier-parent
 * @description: ${description}
 * @author: jimin.zhou
 * @create: 2018-10-18 11:42
 **/
public interface ClfEasyService  {

    PaymentRecord getPaymentRecord(PaymentRequest paymentRequest, String recordId,PayTool payTool,OrderAction orderAction,String recordPayTypes[]);

    PaymentRecord getRecordToOrder(CardInfo cardInfo, String period, String token, PaymentRequest paymentRequest);
    
    NeedSurportDTO order(PaymentRequest paymentRequest, PaymentRecord paymentRecord, CardInfoDTO cardInfo);

    NcCflEasyConfirmResponseDTO confirmPay(CardInfoDTO cardInfoDTO, String verifyCode, PaymentRequest paymentRequest, PaymentRecord paymentRecord, SynTypeEnum synTypeEnum);

}
