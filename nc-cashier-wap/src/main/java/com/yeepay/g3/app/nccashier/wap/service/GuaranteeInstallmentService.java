package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.vo.CardInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentCardNoCheckResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPaymentRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPaymentResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPrePayResponseVO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;

/**
 * 担保分期service
 */
public interface GuaranteeInstallmentService {

    /**
     * 预支付
     * @param info
     * @param bankCode
     * @param period
     * @param token
     * @return
     */
    GuaranteeInstallmentPrePayResponseVO prePay(RequestInfoDTO info, String bankCode, String period, String token);

    /**
     * 请求支付
     * @param info
     * @param requestVO
     * @return
     */
    GuaranteeInstallmentPaymentResponseVO requestPayment(RequestInfoDTO info, GuaranteeInstallmentPaymentRequestVO requestVO);


    /**
     * 获取银行卡和期数
     * @param requestId
     * @return
     */
    InstallmentBankResponseVO getSupportBankAndPeriods(long requestId);

    /**
     * 校验卡号，返回卡类型
     * @param info
     * @param cardNo
     * @param bankCode
     * @return
     */
    GuaranteeInstallmentCardNoCheckResponseVO checkCardNo(RequestInfoDTO info, String cardNo, String bankCode);
}
