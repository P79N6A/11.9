package com.yeepay.g3.app.fronend.app.controller.statistics;

import com.yeepay.g3.app.fronend.app.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chronos.
 * @createDate 2016/10/17.
 */
@Controller
@RequestMapping("statistics")
public class StatisticsController extends BaseController {

    @RequestMapping("dailyData")
    public String queryDailyData(HttpServletRequest request, HttpServletResponse response, Model model){
        initQueryParams(model);
        initUser(request, model);
        return "statistics/queryDailyData";
    }

    @RequestMapping("monthlyData")
    public String queryMonthlyData(HttpServletRequest request, HttpServletResponse response, Model model){
        initQueryParams(model);
        initUser(request, model);
        return "statistics/queryMonthlyData";
    }
}
