package com.yeepay.g3.app.fronend.app.service.impl;

import com.yeepay.g3.app.fronend.app.dto.QueryResult;
import com.yeepay.g3.app.fronend.app.dto.QueryResultGroup;
import com.yeepay.g3.app.fronend.app.dto.Series;
import com.yeepay.g3.app.fronend.app.enumtype.PlatformType;
import com.yeepay.g3.app.fronend.app.service.OrderService;
import com.yeepay.g3.app.fronend.app.utils.ConstantUtil;
import com.yeepay.g3.app.fronend.app.utils.FrontEndConfigUtils;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chronos.
 * @createDate 2016/10/17.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JdbcTemplate feJdbcTemplate;

    @Autowired
    private JdbcTemplate mpayJdbcTemplate;

    private static final String QUERY_SQL = "SELECT COUNT(ID) TRX_COUNT,SUM(TOTAL_AMOUNT) TRX_AMOUNT " +
            "FROM %s WHERE ORDER_DATE >= '%s' AND ORDER_DATE <= '%s' WITH UR ";
    private static final String QUERY_MIN_COUNT_SQL = "SELECT COUNT(ID) TRX_COUNT," +
            "TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI') ORDER_DATE " +
            "FROM %s " +
            "WHERE CREATE_TIME >= '%s' " +
            "AND CREATE_TIME < '%s'  " +
            "GROUP BY TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI') " +
            "ORDER BY ORDER_DATE ASC " +
            "WITH UR";
    private static final String QUERY_HOUR_COUNT_SQL = "SELECT COUNT(ID) TRX_COUNT," +
            "TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24') ORDER_DATE " +
            "FROM %s " +
            "WHERE CREATE_TIME >= '%s' " +
            "AND CREATE_TIME < '%s'  " +
            "GROUP BY TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24') " +
            "ORDER BY ORDER_DATE ASC " +
            "WITH UR";

    private static final String QUERY_MIN_SUCCESS_COUNT_SQL = "SELECT COUNT(ID) TRX_COUNT," +
            "TO_CHAR(MODIFY_TIME,'YYYY-MM-DD HH24:MI') ORDER_DATE " +
            "FROM %s " +
            "WHERE MODIFY_TIME >= '%s' " +
            "AND MODIFY_TIME < '%s'  " +
            "AND PAY_STATUS = 'SUCCESS'" +
            "GROUP BY TO_CHAR(MODIFY_TIME,'YYYY-MM-DD HH24:MI') " +
            "ORDER BY ORDER_DATE ASC " +
            "WITH UR";

    /**
     * 查询每日成功订单数
     */
    private static final String QUERY_DAILY_SUCCESS_COUNT_SQL =
            "SELECT COUNT(*) "+
            "FROM %s "+
            "WHERE PAY_STATUS = 'SUCCESS' " +
            "AND CREATE_TIME >  '%s' "+
            "AND CREATE_TIME <  '%s' ";

    /**
     * 查询每日成功交易金额
     */
    private static final String QUERY_DAILY_SUCCESS_AMOUNT_SQL =
            "SELECT SUM(TOTAL_AMOUNT) "+
            "FROM %s "+
            "WHERE PAY_STATUS = 'SUCCESS' " +
            "AND CREATE_TIME > '%s' "+
            "AND CREATE_TIME < '%s' ";
    /**
     * 查询每日活跃商户数
     */
    private static final String QUERY_DAILY_ACTIVE_MERCHANT_SQL =
            "SELECT COUNT(DISTINCT (CUSTOMER_NUMBER)) "+
            "FROM %s "+
            "WHERE PAY_STATUS = 'SUCCESS' " +
            "AND CREATE_TIME > '%s' "+
            "AND CREATE_TIME < '%s' ";

    private static final PayStatusEnum[] status = PayStatusEnum.values();


    @Override
    public List queryOrderByDate(Date start, Date end) {
        List<QueryResult> resultList = new ArrayList<QueryResult>();

        return resultList;
    }

    @Override
    public QueryResultGroup queryWechatOrderByDate(Date start, Date end) {
        String querySql = String.format(QUERY_MIN_COUNT_SQL, "TBL_FRONTEND_PAY_ORDER", new Timestamp(start.getTime()), new Timestamp(end.getTime()));
        List<Map<String, Object>> queryResult = feJdbcTemplate.queryForList(querySql);
        if (queryResult == null || queryResult.size() < 1)
            throw new IllegalArgumentException("查询结果为空");

        int resultSize = queryResult.size();
        int statusSize = status.length;
        String[] categories = new String[resultSize];
        Series[] series = new Series[statusSize + 1];
        for (int i = statusSize ;i >= 0; i--){
            if (i == 0){
                series[i] = new Series("TOTAL", new Number[resultSize]);
            } else {
                series[i] = new Series(status[i-1].name(), new Number[resultSize]);
            }
        }

        for (int i = 0; i < resultSize; i++) {
            String orderDate = (String) queryResult.get(i).get("");
            Number trxCount = (Number) queryResult.get(i).get("TRX_COUNT");
            String payStatus = (String) queryResult.get(i).get("PAY_STATUS");
            if (StringUtils.isBlank(orderDate))
                continue;
            categories[i] = orderDate.split(" ")[1];//取分钟
            for (Series se : series){
                if (se.getName().equals(payStatus)){
                    se.getData()[i] = trxCount != null ? trxCount : 0;
                } else {
                    se.getData()[i] = 0;
                }
            }
            series[0].getData()[i] = trxCount != null ? trxCount : 0;
        }

        return new QueryResultGroup(categories, series);
    }

    @Override
    public List queryMinCountByDate(Date start, Date end, String type) throws ParseException {
        return query(QUERY_MIN_COUNT_SQL, type, 1, start, end, "");
    }

    public List queryMinSuccessCountByDate(Date start, Date end, String type) throws ParseException{
        return query(QUERY_MIN_SUCCESS_COUNT_SQL, type, 1, start, end, "");
    }

    @Override
    public List queryHourCountByDate(Date start, Date end, String type) throws ParseException {
        return query(QUERY_HOUR_COUNT_SQL, type, 2, start, end, "");
    }

    @Override
    public BigDecimal queryTradeAmountByDate(Date start, Date end) {
        BigDecimal orderAmount = new BigDecimal(0);
        for (PlatformType type : PlatformType.fusionpayPlatforms()){
            BigDecimal amount = querySum(QUERY_DAILY_SUCCESS_AMOUNT_SQL, type.name(), start, end, "");
            if (amount != null) {
                orderAmount = orderAmount.add(amount);
            }
        }
        return orderAmount.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Long queryOrderCountByDate(Date start, Date end) {
        Long orderCount = 0L;
        for (PlatformType type : PlatformType.fusionpayPlatforms()){
            Long count = queryCount(QUERY_DAILY_SUCCESS_COUNT_SQL, type.name(), start, end, "");
            if (count != null && count != 0) {
                orderCount = orderCount + count;
            }
        }
        return orderCount;
    }

    @Override
    public Long queryActiveMerchantByDate(Date start, Date end) {
        Long activeMerchants = 0L;
        for (PlatformType type : PlatformType.fusionpayPlatforms()){
            Long merchants = queryCount(QUERY_DAILY_ACTIVE_MERCHANT_SQL, type.name(), start, end, "");
            if(merchants!=null){
                activeMerchants = activeMerchants + merchants;
            }
        }
        return activeMerchants;
    }

    private List query(String orgSql, String type, int step,Date start, Date end, String conditions) throws ParseException {
        String table = FrontEndConfigUtils.getTableMap().get(type);
        String querySql = String.format(orgSql, table,
                new Timestamp(start.getTime()),new Timestamp(end.getTime()), conditions);
        List<Map<String,Object>> orgData = queryOrgData(querySql, type);
        return fixedData(orgData, start, end, step);
    }

    private List<Map<String,Object>> queryOrgData(String querySql,String type){
        if(PlatformType.MPAY.name().equals(type)){
            return mpayJdbcTemplate.queryForList(querySql);
        }else {
            return feJdbcTemplate.queryForList(querySql);
        }
    }

    /**
     * 数据补0操作
     */
    private List fixedData(List<Map<String, Object>> orgData, Date start, Date end, int type) throws ParseException {
        int step;
        SimpleDateFormat sdf ;
        switch (type){
            case 1 : step = (int) ((end.getTime() - start.getTime())/(1000*60));
                sdf = ConstantUtil.MIN_SDF;
                break;
            case 2 : step = (int) ((end.getTime() - start.getTime())/(1000*60*60));
                sdf = ConstantUtil.HOUR_SDF;
                break;
            default:
                throw new IllegalArgumentException("时间类型错误");
        }
        List<Map<String,Object>> returnData = new LinkedList<Map<String, Object>>();
        if (orgData == null || orgData.size() < 1){
            addZeroMap(returnData, start, end, type, sdf);
        } else if (orgData.size() < step){
            Date tmp = start;
            int i = 0;
            while (tmp.getTime() < end.getTime()){
                if (i < orgData.size()) {
                    String order_date = (String) orgData.get(i).get("ORDER_DATE");
                    Date toCompare = sdf.parse(order_date);
                    tmp = addZeroMap(returnData, tmp, toCompare, type, sdf);
                    returnData.add(orgData.get(i));
                } else {
                    tmp = addZeroMap(returnData, tmp, end, type, sdf);
                }
                i++;
            }
        }else {
            returnData = orgData;
        }
        return returnData;
    }

    private Date addZeroMap(List result, Date start, Date end, int type, SimpleDateFormat sdf){
        Date tmp = start;
        while (tmp.getTime() < end.getTime()){
            Map<String,Object> map = new HashedMap();//补0
            map.put("ORDER_DATE", sdf.format(tmp));
            map.put("TRX_COUNT", 0);
            result.add(map);
            tmp = addTimeSeries(tmp, type);
        }
        if (tmp.getTime() == end.getTime()){
            tmp = addTimeSeries(tmp, type);
        }
        return tmp;
    }

    private Date addTimeSeries(Date start, int type){
        switch (type){
            case 1 : return DateUtils.addMinute(start, 1);
            case 2 : return DateUtils.addHour(start, 1);
            default:
                return start;
        }
    }

    /**
     * 执行select count 操作
     * @param orgSql
     * @param type
     * @param start
     * @param end
     * @param conditions
     * @return
     */
    private Long queryCount(String orgSql, String type, Date start, Date end, String conditions){
        String table = FrontEndConfigUtils.getTableMap().get(type);
        String querySql = String.format(orgSql, table,
                new Timestamp(start.getTime()),new Timestamp(end.getTime()), conditions);
        return feJdbcTemplate.queryForObject(querySql, Long.class);

    }

    /**
     * 执行select sum 操作
     * @param orgSql
     * @param type
     * @param start
     * @param end
     * @param conditions
     * @return
     */
    private BigDecimal querySum(String orgSql, String type, Date start, Date end, String conditions){
        String table = FrontEndConfigUtils.getTableMap().get(type);
        String querySql = String.format(orgSql, table,
                new Timestamp(start.getTime()),new Timestamp(end.getTime()), conditions);
        return feJdbcTemplate.queryForObject(querySql, BigDecimal.class);

    }
}
