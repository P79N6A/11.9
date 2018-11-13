package com.yeepay.g3.app.fronend.app.controller.display;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.app.fronend.app.controller.BaseController;
import com.yeepay.g3.app.fronend.app.dto.PaymentDisplayDataResponse;
import com.yeepay.g3.app.fronend.app.service.OrderService;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 聚合支付日订单数据展板
 */
@Controller
@RequestMapping("datadisplay")
public class DisplayBoardController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DisplayBoardController.class);

    @Autowired
    private OrderService orderService;
    private static final String LINE_SEPA = System.lineSeparator();

    @RequestMapping(value = "uptonow", produces = "text/event-stream;charset=utf-8")
    @ResponseBody
    public String uptonow(HttpServletRequest request, HttpServletResponse response) {
        String jsonData = "";
        try {
            jsonData = getJsonData();
        } catch (Exception e) {
            logger.error("uptonow() error : ", e);
        }
        return "retry:60000" + LINE_SEPA + "data:" + jsonData + LINE_SEPA + LINE_SEPA;
    }

    private String getJsonData() throws ParseException {
        Date now = DateUtils.parseDate(DateUtils.getLongDateStr(), DateUtils.DATE_FORMAT_DATETIME);
        Date oneDaysEarly = DateUtils.addDay(now, -1);
        Date today = DateUtils.parseDate(DateUtils.getShortDateStr(), DateUtils.DATE_FORMAT_DATEONLY);
        Date yesterday = DateUtils.addDay(today, -1);

        BigDecimal tradeAmountToday = orderService.queryTradeAmountByDate(today, now);
        Long orderCountToday = orderService.queryOrderCountByDate(today, now);
        Long activeMerchantToday = orderService.queryActiveMerchantByDate(today, now);
        BigDecimal tradeAmountYesterday = orderService.queryTradeAmountByDate(yesterday, oneDaysEarly);
        Long orderCountYesterday = orderService.queryOrderCountByDate(yesterday, oneDaysEarly);
        Long activeMerchantYesterday = orderService.queryActiveMerchantByDate(yesterday, oneDaysEarly);

        PaymentDisplayDataResponse responseData = new PaymentDisplayDataResponse();
        responseData.setDailyTradeAmount(tradeAmountToday.toString());
        responseData.setDailyOrderCount(Long.toString(orderCountToday));
        responseData.setDailyActiveMerchant(Long.toString(activeMerchantToday));
        responseData.setYesterdayTradeAmount(tradeAmountYesterday.toString());
        responseData.setYesterdayOrderCount(Long.toString(orderCountYesterday));
        responseData.setYesterdayActiveMerchant(Long.toString(activeMerchantYesterday));

        return JSONObject.toJSONString(responseData);
    }

}
