package com.yeepay.g3.core.nccashier.service;

/**
 * 封装用于展示在收银台页面上的独立信息
 * Created by ruiyang.du on 2017/9/4.
 */
public interface CashierPageInfoService {

    /**
     * 获取微信公众号二维码地址，用于展示在PC和WAP的成功及失败页
     * @param requestId
     * @return
     */
    String getYeepayWechatQRCode(String requestId);

    /**
     * 获取微信公众号二维码地址，用于展示在PC和WAP的成功及失败页
     * @param merchantNo
     * @param merchantOrderId
     * @return
     */
    String getYeepayWechatQRCode(String merchantNo,String merchantOrderId);
}
