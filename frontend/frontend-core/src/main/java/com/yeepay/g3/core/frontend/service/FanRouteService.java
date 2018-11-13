package com.yeepay.g3.core.frontend.service;

import com.yeepay.g3.core.frontend.entity.FanRoute;

import java.util.Map;

/**
 * 粉丝路由
 */
public interface FanRouteService {
    FanRoute queryFanRouteByReportId(String reportId);
    void addFanRoute(FanRoute fanRoute);
    //哆啦宝粉丝路由获得交易的商户号
    String getFanRouteTradeCustomerNum(Map<String, String> map);
}
