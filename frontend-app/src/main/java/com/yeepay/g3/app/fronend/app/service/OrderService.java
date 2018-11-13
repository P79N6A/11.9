package com.yeepay.g3.app.fronend.app.service;

import com.yeepay.g3.app.fronend.app.dto.QueryResultGroup;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author chronos.
 * @createDate 2016/10/17.
 */

public interface OrderService {

    /**
     * 根据时间统计订单量
     * @param start
     * @param end
     * @return
     */
    List queryOrderByDate(Date start, Date end);

    QueryResultGroup queryWechatOrderByDate(Date start, Date end);

    /**
     * 查询每分钟的交易数据
     * @param start
     * @param end
     * @param type
     * @return
     */
    List queryMinCountByDate(Date start, Date end, String type) throws ParseException;

    /**
     * 查询每分钟成功交易数据
     * @param start
     * @param end
     * @param type
     * @return
     * @throws ParseException
     */
    List queryMinSuccessCountByDate(Date start, Date end, String type) throws ParseException;

    /**
     * 查询没小时的交易数据
     * @param start
     * @param end
     * @param type
     * @return
     */
    List queryHourCountByDate(Date start, Date end, String type) throws ParseException;

    /**
     * 查询时间范围内的成功交易总金额
     * @param start
     * @param end
     * @return
     */
    BigDecimal queryTradeAmountByDate(Date start, Date end) throws ParseException;

    /**
     * 查询时间范围内的成功订单总数
     * @param start
     * @param end
     * @return
     */
    Long queryOrderCountByDate(Date start, Date end);

    /**
     * 查询时间范围内的活跃商户数
     * @param start
     * @param end
     * @return
     */
    Long queryActiveMerchantByDate(Date start, Date end);
}
