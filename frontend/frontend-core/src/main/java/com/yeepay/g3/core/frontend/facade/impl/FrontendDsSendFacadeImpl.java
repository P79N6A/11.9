package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.biz.impl.AnalyseBiz;
import com.yeepay.g3.core.frontend.entity.StatisticsDTO;
import com.yeepay.g3.core.frontend.enumtype.FrontendProductType;
import com.yeepay.g3.core.frontend.enumtype.ResultType;
import com.yeepay.g3.core.frontend.util.ChartData;
import com.yeepay.g3.core.frontend.util.FrontendMathUtils;
import com.yeepay.g3.core.frontend.util.JFreeChartHelper;
import com.yeepay.g3.core.frontend.util.MailSendHelper;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.facade.novalidate.FrontendDsSendFacade;
import com.yeepay.g3.facade.notifier.dto.NotifyFeature;
import com.yeepay.g3.utils.common.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chronos.
 * @createDate 2016/10/8.
 */
@Service("frontendDsSendFacade")
public class FrontendDsSendFacadeImpl implements FrontendDsSendFacade {

    private static FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendDsSendFacadeImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static String QUERY_DAILY_SQL = "SELECT * FROM FRONTEND.TBL_FRONTEND_DAILY_DATA where ORDER_DATE = '%s' WITH UR";
    private static String QUERY_MONTHLY_SQL = "SELECT * FROM FRONTEND.TBL_FRONTEND_MONTHLY_DATA where ORDER_DATE = '%s' WITH UR";

    private static String QUERY_CUSTOMER_DAILY_SQL = "SELECT ROWNUMBER() OVER(ORDER BY B.TOTAL_COUNT DESC) ROWNUM,B.* FROM (\n" +
            "SELECT  SUM(TRX_COUNT) TOTAL_COUNT,SUM(TRX_AMOUNT) TOTAL_AMOUNT,SUM(BANK_COST) TOTAL_COST,CUSTOMER_NUMBER \n" +
            "FROM FRONTEND.TBL_FRONTEND_DAILY_DATA " +
            "WHERE  ORDER_DATE = '%s' " +
            "GROUP BY CUSTOMER_NUMBER " +
            "ORDER BY SUM(TRX_COUNT) DESC " +
            "FETCH FIRST 10 ROWS ONLY" +
            "WITH UR" +
            ") B" +
            "WITH UR";

    private static String SPLIT = "_";

    @Autowired
    private AnalyseBiz analyseBiz;

    @Override
    public void sendDailyData() {
        String queryDate = DateUtils.getShortDateStr(DateUtils.addDay(new Date(),-1));
        String querySql = String.format(QUERY_DAILY_SQL,queryDate);
        List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(querySql);
        Map<String, Object> analysedData = analyseData(queryResult);
        analysedData.put("title", queryDate + "日交易数据统计");

        String $startDate = DateUtils.getShortDateStr(DateUtils.addDay(new Date(), -7));

        List<ChartData> chartDataList = createChartData($startDate, queryDate, "TBL_FRONTEND_DAILY_DATA");

        NotifyFeature feature = createNotifyFeature(chartDataList);

        sendMail(analysedData, MailSendHelper.DAILY_DATA_RULE, feature);
    }

    @Override
    public void sendMonthlyData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String queryDate = sdf.format(DateUtils.addMonths(new Date(),-1));
        String querySql = String.format(QUERY_MONTHLY_SQL,queryDate);
        List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(querySql);
        Map<String, Object> analysedData = analyseData(queryResult);
        analysedData.put("title", queryDate + "月交易数据统计");

        String startDate = sdf.format(DateUtils.addMonths(new Date(),-6));
        List<ChartData> chartDataList = createChartData(startDate, queryDate, "TBL_FRONTEND_MONTHLY_DATA");

        NotifyFeature feature = createNotifyFeature(chartDataList);

        sendMail(analysedData, MailSendHelper.MONTHLY_DATA_RULE, feature);
    }

    /**
     * 发送邮件
     * @param message
     * @param rule
     * @param feature
     */
    private void sendMail(Map message, String rule, NotifyFeature feature){
        if(feature.getAttatchments() != null && feature.getAttatchments().size() > 0){
            MailSendHelper.sendEmail(message, rule,feature);
        } else {
            MailSendHelper.sendEmail(message, rule ,MailSendHelper.recipients);
        }
    }

    /**
     * 生成报表数据
     * @param start
     * @param end
     * @param table
     * @return
     */
    private List<ChartData> createChartData(String start, String end, String table){
        List<ChartData> chartDataList = new ArrayList<ChartData>();
        chartDataList.addAll(analyseBiz.queryData(generateAmountDto(start,end,table)));
        chartDataList.addAll(analyseBiz.queryData(generateCountDto(start,end,table)));
        return chartDataList;
    }

    /**
     * 生成通知附件
     * @param chartDataList
     * @return
     */
    private NotifyFeature createNotifyFeature(List<ChartData> chartDataList){
        NotifyFeature feature = new NotifyFeature();
        for (ChartData chartData : chartDataList) {
            byte[] chart = null;
            try {
                    chart = JFreeChartHelper.createTimeSeriesChart(chartData);
            } catch (Throwable th){
                logger.error("生成图表失败", th);
            }
            if (chart == null)
                continue;
            feature.addAttatchment(chartData.getTitle() + ".png",chart);
        }
        return feature;
    }

    /**
     * 交易量数据统计
     * @param start
     * @param end
     * @param table
     * @return
     */
    private StatisticsDTO generateCountDto(String start,String end,String table){
        StatisticsDTO dto = new StatisticsDTO();
        dto.setResultColumn("TRX_COUNT");
        dto.setResultType(ResultType.SUM);
        dto.setTable("FRONTEND." + table);
        dto.getDimensions().put("PLATFORM_TYPE","平台类型");
        dto.getConditions().put("总交易量","ORDER_DATE >= '"+ start +"' AND ORDER_DATE <= '" + end + "'");
        dto.getConditions().put("成功交易量","ORDER_DATE >= '"+ start +"' AND ORDER_DATE <= '" + end
                + "' AND  PAY_STATUS = 'SUCCESS'");
        dto.setGroupBy("ORDER_DATE");
        dto.setOrderBy("ORDER_DATE ASC");
        return dto;
    }

    /**
     * 交易金额统计
     * @param start
     * @param end
     * @param table
     * @return
     */
    private StatisticsDTO generateAmountDto(String start,String end,String table){
        StatisticsDTO dto = new StatisticsDTO();
        dto.setResultColumn("TRX_AMOUNT");
        dto.setResultType(ResultType.SUM);
        dto.setTable("FRONTEND." + table);
        dto.getDimensions().put("PLATFORM_TYPE","平台类型");
        dto.getConditions().put("总交易金额","ORDER_DATE >= '"+ start +"' AND ORDER_DATE <= '" + end + "'");
        dto.getConditions().put("成功交易金额","ORDER_DATE >= '"+ start +"' AND ORDER_DATE <= '" + end
                + "' AND  PAY_STATUS = 'SUCCESS'");
        dto.setGroupBy("ORDER_DATE");
        dto.setOrderBy("ORDER_DATE ASC");
        return dto;
    }

    /**
     * 分析汇总数据
     * @return
     */
    private Map<String,Object> analyseData(List<Map<String,Object>> orgData){
        Map<String, Object> analysedData = new HashMap<String, Object>();
        //根据不同维度汇总
        long totalCount = 0;//总数
        long successCount = 0;//成功数
        BigDecimal totalTrxAmount = new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP);//总成本
        BigDecimal successTrxAmount = new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP);//总成本
        BigDecimal totalCost = new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP);//总成本
        for (Map<String, Object> map : orgData){
            String platform = (String) map.get("PLATFORM_TYPE");
            analyseDataByPlatform(analysedData, platform, map);
        }
        for (FrontendProductType type : FrontendProductType.values()){
            Long type_totalCount = (Long) analysedData.get(type.name() + SPLIT + "totalCount");
            if (type_totalCount != null)
                totalCount += type_totalCount;
            Long type_successCount = (Long) analysedData.get(type.name() + SPLIT + "successCount");
            if (type_successCount != null)
                successCount += type_successCount;
            BigDecimal type_totalTrxAmount = (BigDecimal) analysedData.get(type.name() + SPLIT + "totalTrxAmount");
            if (type_totalTrxAmount != null)
                totalTrxAmount = totalTrxAmount.add(type_totalTrxAmount);
            BigDecimal type_successTrxAmount = (BigDecimal) analysedData.get(type.name() + SPLIT + "successTrxAmount");
            if (type_totalTrxAmount != null)
                successTrxAmount = successTrxAmount.add(type_successTrxAmount);
            BigDecimal type_totalCost = (BigDecimal) analysedData.get(type.name() + SPLIT + "totalCost");
            if (type_totalCost != null)
                totalCost = totalCost.add(type_totalCost);
            if (type_totalCount != null && type_totalCount > 0){
                analysedData.put(type.name() + SPLIT + "successRate", FrontendMathUtils.divide(type_successCount,type_totalCount));
            } else {
                analysedData.put(type.name() + SPLIT + "successRate", "0%");
            }
            if ( type_totalTrxAmount != null && type_successTrxAmount.compareTo(new BigDecimal("0")) > -1){
                analysedData.put(type.name() + SPLIT + "saRate",FrontendMathUtils.divide(type_successTrxAmount,type_totalTrxAmount));
            } else {
                analysedData.put(type.name() + SPLIT + "saRate", "0%");
            }

        }
        analysedData.put("successCount",successCount);
        analysedData.put("totalCount",totalCount);
        analysedData.put("successTrxAmount",successTrxAmount);
        analysedData.put("totalTrxAmount",totalTrxAmount);
        analysedData.put("totalCost",totalCost);
        if (totalCount != 0) {
            analysedData.put("successRate", FrontendMathUtils.divide(successCount, totalCount));
            analysedData.put("saRate",FrontendMathUtils.divide(successTrxAmount,totalTrxAmount));
        }else {
            analysedData.put("successRate", "---");
            analysedData.put("saRate", "---");
        }
        return analysedData;
    }

    /**
     * 平台维度分析
     * @param analysedData
     * @param platform
     * @param orgData
     */
    private void analyseDataByPlatform(Map analysedData ,String platform ,Map orgData){
        Long successCount = (Long) analysedData.get(platform + SPLIT + "successCount");
        if (successCount == null)
            successCount = 0l;
        Long totalCount = (Long) analysedData.get(platform + SPLIT + "totalCount");
        if (totalCount == null)
            totalCount = 0l;
        BigDecimal successTrxAmount = (BigDecimal) analysedData.get( platform + SPLIT + "successTrxAmount");
        if (successTrxAmount == null)
            successTrxAmount = new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP);
        BigDecimal totalTrxAmount = (BigDecimal) analysedData.get( platform + SPLIT + "totalTrxAmount");
        if (totalTrxAmount == null)
            totalTrxAmount = new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP);
        BigDecimal totalCost = (BigDecimal) analysedData.get( platform + SPLIT + "totalCost");
        if (totalCost == null)
            totalCost = new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP);

        Long trxCount = (Long) orgData.get("TRX_COUNT");
        if (trxCount == null)
            trxCount = 0l;
        BigDecimal trxAmount = (BigDecimal) orgData.get("TRX_AMOUNT");
        if (trxAmount == null)
            trxAmount = new BigDecimal("0").setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal bankCost = (BigDecimal) orgData.get("BANK_COST");
        if (bankCost == null)
            bankCost = new BigDecimal("0").setScale(4, BigDecimal.ROUND_HALF_UP);
        String payStatus = (String) orgData.get("PAY_STATUS");
        if (PayStatusEnum.SUCCESS.name().equals(payStatus)){
            successCount += trxCount;
            successTrxAmount = successTrxAmount.add(trxAmount);
        }
        totalCount += trxCount;
        totalTrxAmount = totalTrxAmount.add(trxAmount);
        totalCost = totalCost.add(bankCost);

        analysedData.put(platform + SPLIT + "successCount", successCount);
        analysedData.put(platform + SPLIT + "totalCount", totalCount);
        analysedData.put(platform + SPLIT + "successTrxAmount", successTrxAmount);
        analysedData.put(platform + SPLIT + "totalTrxAmount", totalTrxAmount);
        analysedData.put(platform + SPLIT + "totalCost",totalCost);
    }
}
