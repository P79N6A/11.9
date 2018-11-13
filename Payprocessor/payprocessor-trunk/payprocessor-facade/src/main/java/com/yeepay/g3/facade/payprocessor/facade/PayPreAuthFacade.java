/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.*;

/**
 * 类名称: PayPreAuthFacade <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/18 上午10:55
 * @version: 1.0.0
 */
public interface PayPreAuthFacade {


    /**
     * 预授权下单接口
     *
     * @param requestDTO
     * @return
     */
    NcPayOrderResponseDTO ncPreAuthRequest(NcPayOrderRequestDTO requestDTO);

    /**
     * 预授权确认接口
     * @param requestDTO
     * @return
     */
    PayRecordResponseDTO ncPreAuthComfirm(NcPayConfirmRequestDTO requestDTO);

    /**
     * 预授权撤销、预授权完成撤销接口
     * @param requestDTO
     * @return
     */
    PreAuthCancelResponseDTO ncPreAuthCancel(PreAuthCancelRequestDTO requestDTO);

    /**
     * 预授权完成接口
     * @param requestDTO
     * @return
     */
    PreAuthCompleteResponseDTO ncPreAuthComplete(PreAuthCompleteRequestDTO requestDTO);

}
