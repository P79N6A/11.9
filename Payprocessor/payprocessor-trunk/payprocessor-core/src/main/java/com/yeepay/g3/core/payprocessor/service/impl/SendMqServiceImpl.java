package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.dto.AccountPayResultMessage;
import com.yeepay.g3.core.payprocessor.service.SendMqService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 
 * @author：zhangxh
 * @since：2017年5月26日 上午11:43:50
 * @version:
 */
@Service
public class SendMqServiceImpl implements SendMqService {
    //账户支付，没有敏感项，不使用PayLoggerFactory。
    //使用PayLoggerFactory会出错
    private static final Logger logger = LoggerFactory.getLogger(SendMqService.class);

    private static final String QUEUE_ACCOUNT_PAY_RESULT = "ACCOUNT_PAY_RESULT_QUEUE";

    private static final String QUEUE_PAY_RECORD_RESULT = "PP_NOTIFY_NCCASHIER_QUEUE";

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void sendAccountPayResultMessageMq(AccountPayResultMessage accountPayResultMessage) {
        try {
            logger.info("send mq message , routing key:{}", QUEUE_ACCOUNT_PAY_RESULT);
            amqpTemplate.convertAndSend(QUEUE_ACCOUNT_PAY_RESULT, JSONUtils.toJsonString(accountPayResultMessage));
            logger.info("send mq success. routing key:{} , message:{}", 
                    QUEUE_ACCOUNT_PAY_RESULT, JSONUtils.toJsonString(accountPayResultMessage));
        } catch (Throwable t) {
            logger.error("send mq error.routing key: " + QUEUE_ACCOUNT_PAY_RESULT 
                    + ",message: " + accountPayResultMessage.toString(), t);
        }

    }

    @Override
    public void sendPayRecordResultMessageMq(PayRecordResponseDTO responseDTO) {
        try {
            logger.info("send mq message, routing key:{}", QUEUE_PAY_RECORD_RESULT);
            amqpTemplate.convertAndSend(QUEUE_PAY_RECORD_RESULT, JSONUtils.toJsonString(responseDTO));
            logger.info("send mq success. routing key:{} , message:{}",
                    QUEUE_PAY_RECORD_RESULT, JSONUtils.toJsonString(responseDTO));
        } catch (Throwable t) {
            logger.error("send mq error.routing key: " + QUEUE_PAY_RECORD_RESULT
                    + ",message: " + responseDTO.toString(), t);
        }
    }

}
