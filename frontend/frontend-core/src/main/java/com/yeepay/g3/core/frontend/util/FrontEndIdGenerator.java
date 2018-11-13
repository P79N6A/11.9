package com.yeepay.g3.core.frontend.util;

import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import org.apache.commons.lang.math.RandomUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 基于redis的id生成器
 */
public class FrontEndIdGenerator {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontEndIdGenerator.class);


    /**
     * 生成格式化的id，格式：(15位时间戳yyMMddHHmmssSSS)+(其他格式)+(8位随机字符)
     * @param extPart id中(其他格式)部分
     * @return
     */
    public static String getNextFormattedId(String extPart) {
        try {
            Date today = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
            String datePart = dateFormat.format(today);
            String uuidPart = UUID.randomUUID().toString().substring(0, 8);
            return datePart + extPart + uuidPart;
        } catch (Exception e) {
            logger.warn("getNextFormattedId 异常 = ", e);
            return null;
        }
    }

}
