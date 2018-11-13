package com.yeepay.g3.core.frontend.biz;

import com.yeepay.g3.facade.frontend.dto.FanRouteAddRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddResponseDTO;

public interface FanRouteBiz {

    /**
     * 新增粉丝路由
     * @param requestDTO
     * @return
     */
    FanRouteAddResponseDTO addFanRouteInfo(FanRouteAddRequestDTO requestDTO);
}
