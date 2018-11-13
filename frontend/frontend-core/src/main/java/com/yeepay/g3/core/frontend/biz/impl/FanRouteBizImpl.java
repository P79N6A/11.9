package com.yeepay.g3.core.frontend.biz.impl;

import com.yeepay.g3.core.frontend.biz.FanRouteBiz;
import com.yeepay.g3.core.frontend.entity.FanRoute;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.service.FanRouteService;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddResponseDTO;
import com.yeepay.g3.utils.common.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FanRouteBizImpl implements FanRouteBiz {

    @Resource
    private FanRouteService fanRouteService;

    @Override
    public FanRouteAddResponseDTO addFanRouteInfo(FanRouteAddRequestDTO requestDTO) {
        FanRouteAddResponseDTO responseDTO = new FanRouteAddResponseDTO();
        FanRoute fanRoute = new FanRoute();
        BeanUtils.copyProperties(requestDTO, fanRoute);
        //查询看看有没有
        FanRoute oldFanRoute = fanRouteService.queryFanRouteByReportId(requestDTO.getReportId());
        if (oldFanRoute == null) {
            fanRouteService.addFanRoute(fanRoute);
        } else if (!(requestDTO.getCustomerNum().equals(oldFanRoute.getCustomerNum()) &&
                requestDTO.getSubCustomerNum().equals(oldFanRoute.getSubCustomerNum()))) {
            responseDTO.setResponseCode(ErrorCode.F0004001);
            responseDTO.setResponseMsg("reportId 已存在,但是 customerNum 或者 subCustomerNum 和数据库不一致");
            return responseDTO;
        }
        responseDTO.setResponseCode(ErrorCode.F0004000);
        responseDTO.setResponseMsg("成功");
        return responseDTO;
    }
}
