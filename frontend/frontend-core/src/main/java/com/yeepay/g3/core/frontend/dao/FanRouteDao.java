package com.yeepay.g3.core.frontend.dao;

import com.yeepay.g3.core.frontend.entity.FanRoute;
import com.yeepay.g3.utils.persistence.GenericDao;

/**
 * 粉丝路由
 */
public interface FanRouteDao extends GenericDao<FanRoute> {
    /**
     * 根据 reportId 获取粉丝路由信息
     * @param reportId
     * @return
     */
   FanRoute queryFanRouteByReportId(String reportId);
}