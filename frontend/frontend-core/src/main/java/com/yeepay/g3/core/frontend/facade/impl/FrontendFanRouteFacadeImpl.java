package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.biz.FanRouteBiz;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendFanRouteFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FrontendFanRouteFacadeImpl implements FrontendFanRouteFacade {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendFanRouteFacadeImpl.class);

    @Resource
    private FanRouteBiz fanRouteBiz;

    @Override
    public FanRouteAddResponseDTO addFanRouteInfo(FanRouteAddRequestDTO requestDTO) {
        FanRouteAddResponseDTO responseDTO = null;
        try {
            responseDTO = fanRouteBiz.addFanRouteInfo(requestDTO);
        }catch (Exception ie){
            logger.warn("[粉丝路由新增业务异常] ", ie);
            responseDTO.setResponseCode(ErrorCode.F0004002);
            responseDTO.setResponseMsg(ie.getMessage());
        }
        return responseDTO;
    }
}
