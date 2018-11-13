/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.SynTypeEnum;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.vo.CashierUserInfo;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * 类名称: APICflEasyService <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/4 下午3:54
 * @version: 1.0.0
 */
public interface APICflEasyService {

    /**
     * 如需校验，校验商户的产品开通配置
     *
     * @param merchantNo
     * @return
     */
    MerchantInNetConfigResult verifyProductOpen(String merchantNo, TransactionTypeEnum transactionType);

    /**
     * 调pp首次支付
     * @param requestDTO
     * @param paymentRequest
     * @param user
     * @return
     */
    APICflEasyPaymentResponseDTO firstPayRequest(APICflEasyFirstRequestDTO requestDTO, PaymentRequest paymentRequest, CashierUserInfo user);


    /**
     * 调pp绑卡支付
     * @param requestDTO
     * @param paymentRequest
     * @param user
     * @return
     */
    APICflEasyPaymentResponseDTO bindPayRequest(APICflEasyBindRequestDTO requestDTO, PaymentRequest paymentRequest, CashierUserInfo user);

    APIBasicResponseDTO smsSend(CardInfoDTO cardInfoDTO, PaymentRequest paymentRequest, PaymentRecord paymentRecord);

    APICflEasyConfirmPayResponseDTO confirmPay(CardInfoDTO cardInfoDTO,String verifyCode, PaymentRequest paymentRequest, PaymentRecord paymentRecord);

    PaymentRecord queryPaymentRecord(OrderDetailInfoModel orderInfo, String recordId, int paymentProcess);

    boolean paymentProcessInfoCheck(CardInfoDTO suppliedCardInfo, PaymentRecord paymentRecord, int paymentProcess, String verifyCode);
}
