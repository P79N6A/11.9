package com.yeepay.g3.facade.frontend.facade;

import com.yeepay.g3.facade.frontend.dto.FanRouteAddRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddResponseDTO;

/**
 * 粉丝路由接口
 */
public interface FrontendFanRouteFacade {

    /**
     * 粉丝路由:新零售同步数据到FE
     * @param requestDTO
     * @return
     */
    FanRouteAddResponseDTO addFanRouteInfo(FanRouteAddRequestDTO requestDTO);
}