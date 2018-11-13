/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.NcPayBiz;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.facade.PayPreAuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类名称: PayPreAuthFacadeImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/18 上午11:41
 * @version: 1.0.0
 */
@Service("PayPreAuthFacade")
public class PayPreAuthFacadeImpl implements PayPreAuthFacade {

    @Autowired
    private NcPayBiz ncPayBiz;

    @Override
    public NcPayOrderResponseDTO ncPreAuthRequest(NcPayOrderRequestDTO requestDTO) {
        return ncPayBiz.ncPreAuthRequest(requestDTO);
    }

    @Override
    public PayRecordResponseDTO ncPreAuthComfirm(NcPayConfirmRequestDTO requestDTO) {
        return ncPayBiz.ncPreAuthComfirm(requestDTO);
    }

    @Override
    public PreAuthCancelResponseDTO ncPreAuthCancel(PreAuthCancelRequestDTO requestDTO) {
        return ncPayBiz.ncPreAuthCancel(requestDTO);
    }

    @Override
    public PreAuthCompleteResponseDTO ncPreAuthComplete(PreAuthCompleteRequestDTO requestDTO) {
        return ncPayBiz.ncPreAuthComplete(requestDTO);
    }
}