package com.yeepay.g3.core.frontend.util;


import com.yeepay.g3.core.frontend.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.frontend.enumtype.ExternalSystem;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.notifier.NotifyFacade;
import com.yeepay.g3.facade.notifier.dto.NotifyFeature;
import com.yeepay.g3.facade.notifier.dto.NotifyResultDTO;
import com.yeepay.g3.utils.common.encrypt.Digest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 16/8/8.
 */
public class MailSendHelper {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(MailSendHelper.class);

    public static final String userName = "FRONTEND";
    public static final String secretKey = "q1w2e3r4";
    public static final String ruleName = "frontend-mail";
    public static final String recipients = "mp_alert@yeepay.com";
    public static final String recipientsWithLeader = "mp_alert@yeepay.com";
    public static final String REFUND_ERROR_RULE = "frontend-refund-error";
    public static final String SEND_TO_REFUND_RULE = "frontend-send-to-refund-error";
    public static final String DAILY_DATA_RULE = "frontend-daily-data";
    public static final String MONTHLY_DATA_RULE = "frontend-monthly-data";
    private static NotifyFacade notifyFacade = RemoteFacadeProxyFactory.getService(NotifyFacade.class, ExternalSystem.NOTIFY);

    public static void sendEmail(Map message) {
        List<String> recipientList = Arrays.asList(recipients.split(","));
        String sign = Digest.md5Digest(userName + ruleName + recipients + secretKey);
        try {
            notifyFacade.notify(userName, sign, ruleName, recipientList, message);
        } catch (Exception e) {
            logger.error("send mail failed", e);
        }
    }

    public static void sendEmail(Map message, String ruleNameCode, String receivers) {
        List<String> recipientList = Arrays.asList(receivers.split(","));
        String sign = Digest.md5Digest(userName + ruleNameCode + receivers + secretKey);
        try {
            notifyFacade.notify(userName, sign, ruleNameCode, recipientList, message);
        } catch (Exception e) {
            logger.error("send mail failed", e);
        }
    }

    /**
     * 带附件发送邮件
     * @param message
     * @param ruleNameCode
     */
    public static void sendEmail(Map message, String ruleNameCode, NotifyFeature notifyFeature) {
        try {
            String sign = Digest.md5Digest(userName + ruleNameCode + recipients + secretKey);
            NotifyResultDTO notifyResultDTO = notifyFacade.notifySingle(userName, sign, ruleNameCode, recipientsWithLeader, message, notifyFeature);
            logger.info(notifyResultDTO.toString());
        } catch (Exception e) {
            logger.error("send mail failed", e);

        }
    }
}
