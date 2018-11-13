package com.yeepay.g3.core.nccashier.service.impl;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.WechatAppService;
import com.yeepay.g3.core.nccashier.service.CashierPageInfoService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class CashierPageInfoServiceImpl extends NcCashierBaseService implements CashierPageInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CashierPageInfoServiceImpl.class);
    @Resource
    private PaymentRequestService paymentRequestService;

    @Resource
    private WechatAppService wechatAppService;

    @Override
    public String getYeepayWechatQRCode(String requestId) {
        String url = "";
        try {
            if (!StringUtils.isNumeric(requestId)) {
                logger.error("getYeepayWechatQRCode(), param requestId = {}, error = requestId is invalid", requestId);
                return url;
            }
            PaymentRequest paymentRequest = paymentRequestService.findPayRequestById(Long.parseLong(requestId));
            if (paymentRequest == null) {
                logger.error("getYeepayWechatQRCode(), param requestId = {}, error = paymentRequest is null", requestId);
                return url;
            }
            String merchantNo = paymentRequest.getMerchantNo();
            String merchantOrderNo = paymentRequest.getMerchantOrderId();
            if(!CommonUtil.checkNeedQRCode(merchantNo, paymentRequest.getOrderSysNo())){
                logger.warn("getYeepayWechatQRCode(), param requestId = {} ，对应merchantNo = {},商户定制了无需展示二维码，返回空", requestId, merchantNo);
                return url;
            }
            url = wechatAppService.getYeepayWechatQRCode(merchantNo, merchantOrderNo);
            return url;
        } catch (Throwable t) {
            logger.error("getYeepayWechatQRCode() requestId = " + requestId + ", error = ", t);
            return url;
        } finally {
            logger.info("getYeepayWechatQRCode() param requestId = {} ,result url = {}", requestId, url);
        }
    }

    @Override
    public String getYeepayWechatQRCode(String merchantNo, String merchantOrderId) {
        String url = "";
        try {
            if (StringUtils.isBlank(merchantNo) || StringUtils.isBlank(merchantOrderId)) {
                logger.error("getYeepayWechatQRCode(), param merchantNo = {}, merchantOrderId = {} ,error = param is null", merchantNo, merchantOrderId);
                return url;
            }
            if(!CommonUtil.checkNeedQRCode(merchantNo, null)){
                logger.warn("getYeepayWechatQRCode(), param merchantNo = {},商户定制了无需展示二维码，返回空", merchantNo);
                return url;
            }
            url = wechatAppService.getYeepayWechatQRCode(merchantNo, merchantOrderId);
            return url;
        } catch (Throwable t) {
            logger.error("getYeepayWechatQRCode() merchantNo = " + merchantNo + ", merchantOrderId = " + merchantOrderId + ",  error = ", t);
            return url;
        } finally {
            logger.info("getYeepayWechatQRCode() param requestId = {} ,merchantOrderId = {} ,result url = {}", merchantNo, merchantOrderId, url);
        }
    }
}
