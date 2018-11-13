package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.facade.novalidate.FrontendDsDaemonFacade;
import com.yeepay.g3.utils.common.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 16/9/9.
 */
@Service("frontendDsDaemonFacade")
public class FrontendDsDaemonFacadeImpl implements FrontendDsDaemonFacade {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendDsDaemonFacadeImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource(name = "dataSourceOrg")
    private DataSource dataSource;

    private static String DAILY_STATISTIC_SQL = "SELECT\n" +
            "O.CUSTOMER_NUMBER,O.PLATFORM_TYPE ,O.REQUEST_SYSTEM,O.ORDER_SYSTEM,O.ORDER_TYPE,O.PAY_STATUS,\n" +
            "COUNT(O.ID) TRX_COUNT,SUM(O.TOTAL_AMOUNT) TRX_AMOUNT,SUM(O.BANK_TOTAL_COST) BANK_COST,\n" +
            "TO_CHAR(O.CREATE_TIME,'YYYY-MM-DD') ORDERDATE\n" +
            "FROM FRONTEND.%s O\n" +
            "WHERE \n" +
            "O.CREATE_TIME >= '%s'\n" +
            "AND O.CREATE_TIME < '%s'\n" +
            "GROUP BY O.CUSTOMER_NUMBER,O.PAY_STATUS, O.PLATFORM_TYPE\n" +
            ",O.REQUEST_SYSTEM,ORDER_SYSTEM,O.ORDER_TYPE,TO_CHAR(O.CREATE_TIME,'YYYY-MM-DD')\n" +
            "WITH UR";

    private static String MONTHLY_STATIC_SQL = "SELECT\n" +
            "O.CUSTOMER_NUMBER,O.PLATFORM_TYPE ,O.REQUEST_SYSTEM,O.ORDER_SYSTEM,O.ORDER_TYPE,O.PAY_STATUS,\n" +
            "COUNT(O.ID) TRX_COUNT,SUM(O.TOTAL_AMOUNT) TRX_AMOUNT,SUM(O.BANK_TOTAL_COST) BANK_COST,\n" +
            "TO_CHAR(O.CREATE_TIME,'YYYY-MM') ORDERDATE\n" +
            "FROM FRONTEND.%s O\n" +
            "WHERE \n" +
            "O.CREATE_TIME >= '%s'\n" +
            "AND O.CREATE_TIME < '%s'\n" +
            "GROUP BY O.CUSTOMER_NUMBER,O.PAY_STATUS, O.PLATFORM_TYPE\n" +
            ",O.REQUEST_SYSTEM,ORDER_SYSTEM,O.ORDER_TYPE,TO_CHAR(O.CREATE_TIME,'YYYY-MM')\n" +
            "WITH UR";

    private static String INSERT_DAILY_SQL = "INSERT INTO FRONTEND.TBL_FRONTEND_DAILY_DATA\n" +
            "(CUSTOMER_NUMBER, PLATFORM_TYPE, REQUEST_SYSTEM,ORDER_SYSTEM,ORDER_TYPE, PAY_STATUS, TRX_COUNT, TRX_AMOUNT, BANK_COST,ORDER_DATE)\n" +
            "VALUES(?, ?, ?,?, ?, ?, ?, ?, ?, ?)";

    private static String INSERT_MONTHLY_SQL = "INSERT INTO FRONTEND.TBL_FRONTEND_MONTHLY_DATA\n" +
            "(CUSTOMER_NUMBER, PLATFORM_TYPE, REQUEST_SYSTEM,ORDER_SYSTEM,ORDER_TYPE, PAY_STATUS, TRX_COUNT, TRX_AMOUNT, BANK_COST,ORDER_DATE)\n" +
            "VALUES(?, ?, ?,?, ?, ?, ?, ?, ?, ?)";

    private static String DELETE_DAILY_SQL = "DELETE FROM FRONTEND.TBL_FRONTEND_DAILY_DATA  WHERE ORDER_DATE = '%s'";
    private static String DELETE_MONTHLY_SQL = "DELETE FROM FRONTEND.TBL_FRONTEND_MONTHLY_DATA  WHERE ORDER_DATE = '%s'";

    private static Map TABLE_MAP;
    private static final SimpleDateFormat monthSdf = new SimpleDateFormat("yyyy-MM");

    static {
        TABLE_MAP = new HashMap();
        TABLE_MAP.put(PlatformType.WECHAT.name(),"TBL_FRONTEND_PAY_ORDER");
        TABLE_MAP.put(PlatformType.ALIPAY.name(),"TBL_FRONTEND_PAY_ORDER_ZFB");
        TABLE_MAP.put("NET","TBL_FRONTEND_PAY_ORDER_NET");
        TABLE_MAP.put("CFL","TBL_FRONTEND_PAY_ORDER_CFL");
    }

    @Override
    public void dailyDataStatistic() {
        String endStr = DateUtils.getShortDateStr();
        try {
            Date end = DateUtils.parseDate(endStr,DateUtils.DATE_FORMAT_DATEONLY);
            Date start = DateUtils.addDay(end,-1);
            dailyDataStatisticByDate(start,end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据时间统计交易数据
     * @important 时间间隔必须是一天
     * @param start
     * @param end
     */
    private void dailyDataStatisticByDate(Date start, Date end){
        clearData(DateUtils.getShortDateStr(start), DELETE_DAILY_SQL);

        for (Object table : TABLE_MAP.values()){
            String querySql = String.format(DAILY_STATISTIC_SQL, table.toString(),
                    DateUtils.getTimeStampStr(start), DateUtils.getTimeStampStr(end));
            logger.info(querySql);
            processData(querySql, INSERT_DAILY_SQL);
        }
    }

    @Override
    public void monthlyDataStatistic() {
        String endStr = DateUtils.getShortDateStr();
        try {
            Date endTmp = DateUtils.parseDate(endStr,DateUtils.DATE_FORMAT_DATEONLY);
            Date end = DateUtils.getFirstDateOfMonth(endTmp);
            Date start = DateUtils.addMonths(end,-1);

            monthlyDataStatisticByDate(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void monthlyDataStatisticByDate(Date start, Date end){
        clearData(monthSdf.format(start), DELETE_MONTHLY_SQL);

        for (Object table : TABLE_MAP.values()){
            String querySql = String.format(MONTHLY_STATIC_SQL, table.toString(),
                    DateUtils.getTimeStampStr(start), DateUtils.getTimeStampStr(end));
            logger.info(querySql);
            processData(querySql, INSERT_MONTHLY_SQL);
        }
    }

    @Override
    public void adjustDailyData(String day) {
        try {
            Date start = DateUtils.parseDate(day, DateUtils.DATE_FORMAT_DATEONLY);
            Date end = DateUtils.addDay(start, 1);
            dailyDataStatisticByDate(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void adjustMonthlyData(String month) {
        try {
            Date dateTmp = monthSdf.parse(month);
            Date start = DateUtils.getFirstDateOfMonth(dateTmp);
            Date end = DateUtils.addMonths(start,1);
            monthlyDataStatisticByDate(start,end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询并保存统计数据
     * @param querySql
     * @param insertSql
     */
    private void processData(String querySql, String insertSql){
        Connection conn_query = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn_insert = null;
        PreparedStatement preparedStatement = null;
        int insertCount = 0;
        try {
            conn_query = dataSource.getConnection();
            statement = conn_query.createStatement();
            statement.execute(querySql);
            resultSet = statement.getResultSet();
            conn_insert = dataSource.getConnection();
            preparedStatement = conn_insert.prepareStatement(insertSql);
            while(resultSet.next()){
                preparedStatement.setString(1, resultSet.getString(1));
                preparedStatement.setString(2, resultSet.getString(2));
                preparedStatement.setString(3, resultSet.getString(3));
                preparedStatement.setString(4, resultSet.getString(4));
                preparedStatement.setString(5, resultSet.getString(5));
                preparedStatement.setString(6, resultSet.getString(6));
                preparedStatement.setLong(7, resultSet.getLong(7));
                preparedStatement.setBigDecimal(8,resultSet.getBigDecimal(8));
                preparedStatement.setBigDecimal(9,resultSet.getBigDecimal(9));
                preparedStatement.setString(10, resultSet.getString(10));
                preparedStatement.addBatch();
                insertCount++;
                try{
                    if(insertCount%5000 == 0){
                        int[] batch = preparedStatement.executeBatch();
                        conn_insert.commit();
                        logger.info("insert data success, " + batch.length);
                        preparedStatement.clearBatch();
                        insertCount = 0;
                        logger.info("clear preparedStatement success");
                    }
                }catch(Exception e){
                    logger.error("insert data or clear preparedStatement error, " + e);
                }
            }
            if(insertCount > 0){
                int[] batch = preparedStatement.executeBatch();
                conn_insert.commit();
                logger.info("insert data success, " + batch.length);
            }
        } catch (SQLException e) {
            logger.error("process data error, " + e);
        }  finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(conn_query);
            JdbcUtils.closeStatement(preparedStatement);
            JdbcUtils.closeConnection(conn_insert);
        }
    }

    /**
     * 删除现有数据,防重复
     * @param date
     * @param deleteSql
     */
    private void clearData(String date ,String deleteSql){
        String sql = String.format(deleteSql,date);
        jdbcTemplate.execute(sql);
    }
}
