package com.yeepay.g3.core.nccashier.gateway.service;

/**
 * 综合服务产品组，微信相关接口
 * Created by ruiyang.du on 2017/9/6.
 */
public interface WechatAppService {

    /**
     * 获取易宝微信公众号二维码
     * @param merchantNo
     * @param merchantOrderNo
     * @return
     */
    String getYeepayWechatQRCode(String merchantNo,String merchantOrderNo);
}
