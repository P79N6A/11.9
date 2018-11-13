package com.yeepay.g3.app.fronend.app.service;

import java.util.Date;
import java.util.List;

/**
 * @author chronos.
 * @createDate 2016/10/17.
 */
public interface DataService {
    /**
     * 统计商户交易量
     * @param end
     * @param step
     * @return
     */
    List statisticByCustomer(Date end, Integer step);
}
