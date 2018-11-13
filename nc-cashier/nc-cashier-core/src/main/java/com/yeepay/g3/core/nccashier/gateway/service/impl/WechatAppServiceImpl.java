package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.gateway.service.WechatAppService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class WechatAppServiceImpl extends NcCashierBaseService implements WechatAppService {

    Logger logger = LoggerFactory.getLogger(WechatAppServiceImpl.class);

    @Override
    public String getYeepayWechatQRCode(String merchantNo, String merchantOrderNo) {
        String url;
        try {
            url = wechatQRFacade.createForExam(merchantNo, merchantOrderNo);
        } catch (Throwable e) {
            logger.error("获取易宝微信公众号二维码异常,merchantNo="+merchantNo+", merchantOrderId="+merchantOrderNo+", 异常=", e);
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        if (StringUtils.isBlank(url)) {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        return url;
    }
}
