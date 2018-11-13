package com.yeepay.g3.app.fronend.app.service.impl;

import com.yeepay.g3.app.fronend.app.dto.StatisticResult;
import com.yeepay.g3.app.fronend.app.service.DataService;
import com.yeepay.g3.utils.common.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/10/17.
 */
@Service("dataService")
public class DataServiceImpl implements DataService {

    private static final String STATIC_TOP_TEN_SQL = "SELECT D.CUSTOMER_NUMBER,SUM(D.TRX_AMOUNT) TOTALAOUMNT," +
            "SUM(D.BANK_COST) TOTALCOST ,SUM(TRX_COUNT) TOTALCOUNT " +
            "FROM FRONTEND.TBL_FRONTEND_DAILY_DATA D " +
            "WHERE D.ORDER_DATE >= '%s' " +
            "AND D.ORDER_DATE < '%s' " +
            "AND D.PAY_STATUS = 'SUCCESS' " +
            "GROUP BY D.CUSTOMER_NUMBER " +
            "ORDER BY TOTALAOUMNT  DESC " +
            "FETCH FIRST 10 ROWS ONLY " +
            "WITH UR";
    private static final String STATIC_SINGLE_CUSTOMER_SQL = "SELECT D.CUSTOMER_NUMBER,SUM(D.TRX_AMOUNT) TOTALAOUMNT," +
            "SUM(D.BANK_COST) TOTALCOST ,SUM(TRX_COUNT) TOTALCOUNT " +
            "FROM FRONTEND.TBL_FRONTEND_DAILY_DATA D " +
            "WHERE D.ORDER_DATE >= '%s' " +
            "AND D.ORDER_DATE < '%s' " +
            "AND D.CUSTOMER_NUMBER = '%s'" +
            "AND D.PAY_STATUS = 'SUCCESS' " +
            "GROUP BY D.CUSTOMER_NUMBER " +
            "ORDER BY TOTALAOUMNT  DESC " +
            "FETCH FIRST 10 ROWS ONLY " +
            "WITH UR";

    @Autowired
    private JdbcTemplate feJdbcTemplate;

    @Override
    public List statisticByCustomer(Date end, Integer step) {
        List<StatisticResult> resultList = new LinkedList<StatisticResult>();

        Date tmp = DateUtils.addDay(end, step);
        String querySql = String.format(STATIC_TOP_TEN_SQL,
                DateUtils.getShortDateStr(tmp), DateUtils.getShortDateStr(end));

        List<Map<String,Object>> queryResult = feJdbcTemplate.queryForList(querySql);
        if (queryResult == null || queryResult.size() < 1){
            return resultList;
        }

        for (Map<String,Object> map : queryResult){
            StatisticResult result = mapToDto(map, tmp, step);
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 将map转换为DTO
     * @param current
     * @param end
     * @param step
     * @return
     */
    private StatisticResult mapToDto(Map<String, Object> current, Date end, Integer step){
        StatisticResult result = new StatisticResult();
        String customerNumber = (String) current.get("customer_number");
        result.setCustomerNumber(customerNumber);
        result.setTotalAmount((BigDecimal) current.get("TOTALAOUMNT"));
        result.setTotalCost((BigDecimal) current.get("TOTALCOST"));
        result.setTotalCount((Long) current.get("TOTALCOUNT"));

        Date start = DateUtils.addDay(end, step);
        String querySql = String.format(STATIC_SINGLE_CUSTOMER_SQL, DateUtils.getShortDateStr(start),
                DateUtils.getShortDateStr(end), customerNumber);

        List<Map<String, Object>> queryResult = feJdbcTemplate.queryForList(querySql);
        if (queryResult != null && queryResult.size() > 0){
            Map<String, Object> map = queryResult.get(0);
            result.setLastTotalAmount((BigDecimal) map.get("TOTALAOUMNT"));
            result.setLastTotalCost((BigDecimal) map.get("TOTALCOST"));
            result.setLastTotalCount((Long) map.get("TOTALCOUNT"));
        }
        return result;
    }
}
