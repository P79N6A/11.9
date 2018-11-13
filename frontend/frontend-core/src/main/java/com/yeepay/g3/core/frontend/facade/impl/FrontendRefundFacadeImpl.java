package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.biz.RefundManageBiz;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.FrontendRefundRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendRefundResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendRefundFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付系统订单退款
 *
 * @author songscorpio
 */
@Service("frontendRefundFacade")
public class FrontendRefundFacadeImpl implements FrontendRefundFacade {

    private final static FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendRefundFacadeImpl.class);

    @Autowired
    private RefundManageBiz refundManageBiz;

    @Override
    public FrontendRefundResponseDTO refund(FrontendRefundRequestDTO dto) {
        FrontendRefundResponseDTO responseDTO = new FrontendRefundResponseDTO();
        try {
            logger.info("front end error refund,request dto is :" + dto.toString());
            responseDTO = refundManageBiz.refundRequest(dto);
        } catch (IllegalArgumentException ie) {
            logger.warn("[业务异常] ", ie);
            responseDTO.setResponseCode(ErrorCode.F0001001);
            responseDTO.setResponseMsg(ie.getMessage());
        } catch (FrontendBizException fe) {
            logger.warn("[业务异常] " + dto.toString(), fe);
            responseDTO.setResponseCode(fe.getDefineCode());
            responseDTO.setResponseMsg(fe.getMessage());
        } catch (Throwable th) {
            logger.warn("[系统异常]" + dto.toString(), th);
            responseDTO.setResponseCode(ErrorCode.F0001000);
            responseDTO.setResponseMsg(th.getMessage());
        }
        logger.info("response dto is:" + responseDTO.toString());
        return responseDTO;
    }
}
