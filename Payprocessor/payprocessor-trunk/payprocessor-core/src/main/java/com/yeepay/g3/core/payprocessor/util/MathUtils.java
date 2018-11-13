package com.yeepay.g3.core.payprocessor.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author chronos.
 * @createDate 2016/10/9.
 */
public class MathUtils {

    private static final DecimalFormat format = new DecimalFormat("0.00%");

    /**
     * Long 除法,返回百分比
     * @param numerator 分子
     * @param denominator 分母
     * @return 两位小数百分比 eg: 67.34%
     */
    public static String divide(long numerator,long denominator){
        double rate = (double)numerator/denominator;
        return format.format(rate);
    }

    /**
     * BigDecimal除法
     * @param numerator 分子
     * @param denominator 分母
     * @return 两位小数百分比 eg: 67.34%
     */
    public static String divide(BigDecimal numerator, BigDecimal denominator){
        BigDecimal rate = numerator.divide(denominator,BigDecimal.ROUND_HALF_UP);
        return format.format(rate);
    }
}
