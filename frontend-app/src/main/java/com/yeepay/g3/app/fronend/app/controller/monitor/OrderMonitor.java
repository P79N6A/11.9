package com.yeepay.g3.app.fronend.app.controller.monitor;

import com.yeepay.g3.app.fronend.app.controller.BaseController;
import com.yeepay.g3.app.fronend.app.dto.QueryResultGroup;
import com.yeepay.g3.app.fronend.app.dto.RealTimeResult;
import com.yeepay.g3.app.fronend.app.dto.Series;
import com.yeepay.g3.app.fronend.app.enumtype.PlatformType;
import com.yeepay.g3.app.fronend.app.service.DataService;
import com.yeepay.g3.app.fronend.app.service.OrderService;
import com.yeepay.g3.app.fronend.app.utils.ConstantUtil;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.json.JSONObject;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.utils.jdbc.dal.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/10/17.
 */
@Controller
@RequestMapping("monitor")
public class OrderMonitor extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(OrderMonitor.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private DataService dataService;

    @RequestMapping("query")
    public String query(HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        Date end = DateUtils.parseDate(DateUtils.getLongDateStr(),DateUtils.DATE_FORMAT_DATETIME);
        Date start = DateUtils.addMinute(end,-30);

        QueryResultGroup group = orderService.queryWechatOrderByDate(start,end);
        JSONObject object = (JSONObject) JSONObject.wrap(group);
        model.addAttribute("chartData", object.toString());
        return "monitor/trxCountMonitor";
    }

    @RequestMapping("refresh")
    public String autoRefresh(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ParseException {
        Date nowDate = DateUtils.parseDate(DateUtils.getLongDateStr(),DateUtils.DATE_FORMAT_DATETIME);
        Date startDate = DateUtils.addMinute(nowDate, -1);
        long queryStart = System.currentTimeMillis();
        QueryResultGroup group = orderService.queryWechatOrderByDate(startDate,nowDate);
        long queryEnd = System.currentTimeMillis();
        logger.info("[下单量统计] - [查询耗时] - " + (queryEnd - queryStart) + "ms");
        return outPutJson(group,response);
    }

    @RequestMapping("realtime")
    public String realtime(HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        Date nowData = new Date();
        Date end = DateUtils.parseDate(ConstantUtil.MIN_SDF.format(nowData),ConstantUtil.DATE_FORMAT_MIN);
        Date start = DateUtils.addMinute(end,-30);
        for (PlatformType type : PlatformType.values()){
            JSONObject obj = realtimeDataQuery(start, end, type.name());
            model.addAttribute(type.name().toLowerCase() + "_total", obj.toString());
            if (!type.equals(PlatformType.MPAY)) {
                JSONObject success = realTimeSuccessData(start, end, type.name());
                model.addAttribute(type.name().toLowerCase() + "_success", success.toString());
            }
        }
        return "monitor/realTimeCount";
    }

    @RequestMapping("realtimeRefresh")
    public String realTimeRefresh(String type, HttpServletResponse response) throws ParseException, IOException {
        String[] params = type.split("-");
        if (params == null || params.length < 2)
            return null;
        PlatformType platformType = PlatformType.getPlatformType(params[0]);
        if (platformType == null){
            return null;
        }
        Date nowData = new Date();
        Date end = DateUtils.parseDate(ConstantUtil.MIN_SDF.format(nowData),ConstantUtil.DATE_FORMAT_MIN);
        Date start = DateUtils.addMinute(end,-1);
        JSONObject object;
        if ("SUCCESS".equals(params[1])){
            object = realTimeSuccessData(start, end, platformType.name());
        } else {
            object = realtimeDataQuery(start, end, platformType.name());
        }
        return outPutJson(object,response);
    }

    private JSONObject realtimeDataQuery(Date start, Date end, String type) throws ParseException {
        List<Map<String,Object>> result = orderService.queryMinCountByDate(start, end, type);
        RealTimeResult realTimeResult = new RealTimeResult(type + "-TOTAL", result);
        JSONObject object = (JSONObject) JSONObject.wrap(realTimeResult);
        return object;
    }

    private JSONObject realTimeSuccessData(Date start, Date end, String type) throws ParseException {
        List<Map<String,Object>> result = orderService.queryMinSuccessCountByDate(start, end, type);
        RealTimeResult realTimeResult = new RealTimeResult(type + "-SUCCESS", result);
        JSONObject object = (JSONObject) JSONObject.wrap(realTimeResult);
        return object;
    }

    @RequestMapping("customer")
    public String customer(HttpServletRequest request, HttpServletResponse response, Model model){
        Integer step = -7;
        Date nowDate = new Date();
        List queryResult = dataService.statisticByCustomer(nowDate,step);
        model.addAttribute("result", queryResult);
        model.addAttribute("start", DateUtils.getShortDateStr(DateUtils.addDay(nowDate, step)));
        model.addAttribute("end", DateUtils.getShortDateStr(nowDate));
        initUser(request, model);
        return "monitor/customer";
    }

    @RequestMapping("dayCompare")
    public String dayCompare(HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        //1.多图数据查询
        Date nowData = DateUtils.addHour(new Date(), 1);
        Date end = DateUtils.parseDate(ConstantUtil.HOUR_SDF.format(nowData),ConstantUtil.DATE_FORMAT_HOUR);
        Date start = DateUtils.addDay(end, -1);
        Date pass = DateUtils.addDay(start,-1);
        for (PlatformType type : PlatformType.values()){
            List<Map<String, Object>> today = orderService.queryHourCountByDate(start, end, type.name());
            List<Map<String, Object>> yesterday = orderService.queryHourCountByDate(pass, start, type.name());
            QueryResultGroup group = mergeData(type.name(), today, yesterday);
            JSONObject object = (JSONObject) JSONObject.wrap(group);
            model.addAttribute(type.name().toLowerCase(), object.toString());
        }
        return "monitor/dayCompare";
    }

    /**
     * 分割数据
     * @param key
     * @param orgData
     * @return
     */
    private QueryResultGroup splitData(String key, List<Map<String, Object>> orgData){
        QueryResultGroup group = new QueryResultGroup();

        return group;
    }

    /**
     * 合并数据
     * @param key
     * @param today
     * @param yesterday
     * @return
     */
    private QueryResultGroup mergeData(String key, List<Map<String, Object>> today,
                                       List<Map<String, Object>> yesterday){
        int len = today.size();
        if (len != yesterday.size()){
            //确保数据大小相同
            throw new IllegalArgumentException("数据存在问题,不予解析");
        }
        QueryResultGroup group = new QueryResultGroup();
        group.setKey(key);
        Series s_t = new Series();
        Series s_y = new Series();
        Number[] data_t = new Number[len];
        Number[] data_y = new Number[len];
        String[] categories = new String[len];
        for (int i = 0; i< len; i++){
            String[] keys =  ((String) today.get(i).get("ORDER_DATE")).split(" ");
            categories[i] = keys[1];
            data_t[i] = (Number) today.get(i).get("TRX_COUNT");
            data_y[i] = (Number) yesterday.get(i).get("TRX_COUNT");
            if (i == len -1){
                s_t.setData(data_t);
                s_y.setData(data_y);
            }
        }
        Date yesDate = DateUtils.addDay(new Date(), -1);
        s_t.setName(DateUtils.getShortDateStr());
        s_y.setName(DateUtils.getShortDateStr(yesDate));
        Series[] series = {s_t,s_y};
        group.setCategories(categories);
        group.setSeries(series);
        return group;
    }

    @RequestMapping("advancedQuery")
    public String advancedQuery(String customerNumber, Date end, Model model){
        if(StringUtils.isBlank(customerNumber))
            throw new IllegalArgumentException("参数异常!");
        //实时交易

        //成功交易
        //24小时交易对比
        return "monitor/advancedQuery";
    }
}
