package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.nccashier.dto.*;

public interface APICashierYJZFService {

    /**
     * 首次支付下单，参数校验及商编格式转换
     *
     * @param requestDTO
     */
    void validateFirstPaymentParam(APIYJZFFirstPaymentRequestDTO requestDTO);

    /**
     * 二次支付下单，参数校验及商编格式转换
     *
     * @param requestDTO
     */
    void validateBindPaymentParam(APIYJZFBindPaymentRequestDTO requestDTO);

    /**
     * 请求发短验，参数校验及商编格式转换
     *
     * @param requestDTO
     */
    void validateSendSMSParam(APIYJZFSendSmsRequestDTO requestDTO);

    /**
     * 确认支付，参数校验及商编格式转换
     *
     * @param requestDTO
     */
    void validateConfirmPayParam(APIYJZFConfirmPayRequestDTO requestDTO);

    /**
     * 如需校验，校验商户的产品开通配置
     *
     * @param merchantNo
     * @param checkProduct
     * @return
     */
    MerchantInNetConfigResult verifyProductOpen(String merchantNo, boolean checkProduct, TransactionTypeEnum transactionType);

    /**
     * 校验同人限制信息，包括同人身份校验、同人绑卡数超限校验
     *
     * @param requestDTO
     * @param orderInfo
     */
    void validateSamePersionLimit(ValidateSamePersionLimitDTO requestDTO, OrderDetailInfoModel orderInfo);

    /**
     * 保存pay_record等，并进行首次支付下单
     *
     * @param requestDTO
     * @param paymentRequest
     * @param user
     * @return
     */
    APIYJZFFirstPaymentResponseDTO firstPayRequest(APIYJZFFirstPaymentRequestDTO requestDTO, PaymentRequest paymentRequest, CashierUserInfo user);

    /**
     * 保存pay_record等，并进行二次支付下单
     *
     * @param requestDTO
     * @param paymentRequest
     * @param user
     * @return
     */
    APIYJZFBindPaymentResponseDTO bindPayRequest(APIYJZFBindPaymentRequestDTO requestDTO, PaymentRequest paymentRequest, CashierUserInfo user);


    /**
     * 保存临时卡，并请求发短验
     *
     * @param requestDTO
     * @param paymentRequest
     * @param paymentRecord
     * @return
     */
    APIBasicResponseDTO sendSMS(APIYJZFSendSmsRequestDTO requestDTO, PaymentRequest paymentRequest, PaymentRecord paymentRecord);

    /**
     * 保存临时卡，并请求确认支付
     *
     * @param requestDTO
     * @param paymentRequest
     * @param paymentRecord
     * @return
     */
    APIYJZFConfirmPayResponseDTO confirmPay(APIYJZFConfirmPayRequestDTO requestDTO, PaymentRequest paymentRequest, PaymentRecord paymentRecord);

    /**
     * 获取外部用户
     *
     * @param userNo
     * @param userType
     * @param merchantNo
     * @return
     */
    CashierUserInfo buildMemberUser(String userNo, String userType, String merchantNo);


    /**
     * 获取符合要求的PaymentRecord
     *
     * @param orderInfo
     * @param recordId
     * @param paymentProcess 支付过程步骤，2=发送短验，3=确认支付
     * @return
     */
    PaymentRecord queryPaymentRecord(OrderDetailInfoModel orderInfo, String recordId, int paymentProcess);

    /**
     * 检查支付流程，包括：1、提交的补充项，是否与下单时获取的所需补充项及提交场景符合；2、要求发短验的，确认支付时是否已发短验；3、不应在此步骤提交补充项的，返回标识需清空提交的补充信息
     *
     * @param suppliedCardInfo 入参中提交的卡信息
     * @param paymentRecord
     * @param paymentProcess   支付过程步骤，2=发送短验，3=确认支付
     * @param verifyCode       验证码，确认支付入参中包含时，传入
     * @return true = 此步骤不需提交补充项，调用PP前需清空
     */
    boolean paymentProcessInfoCheck(CardInfoDTO suppliedCardInfo, PaymentRecord paymentRecord, int paymentProcess, String verifyCode);

}
