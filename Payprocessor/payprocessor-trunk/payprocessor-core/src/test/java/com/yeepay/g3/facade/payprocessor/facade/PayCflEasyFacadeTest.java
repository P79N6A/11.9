/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasySmsRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasySmsResponseDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类名称: PayCflEasyFacadeTest <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/9 下午3:10
 * @version: 1.0.0
 */

public class PayCflEasyFacadeTest extends BaseTest {


    @Autowired
    private PayCflEasyFacade payCflEasyFacade;

    @Test
    public void testSmsSend() {
        NcCflEasySmsRequestDTO requestDTO = new NcCflEasySmsRequestDTO();
        requestDTO.setRecordNo("CFL_EASY1809091459448358129");
        requestDTO.setTmpCardId(2024320L);
        requestDTO.setSmsSendType(ReqSmsSendTypeEnum.YEEPAY);
        NcCflEasySmsResponseDTO responseDTO = payCflEasyFacade.sendSms(requestDTO);
    }
}