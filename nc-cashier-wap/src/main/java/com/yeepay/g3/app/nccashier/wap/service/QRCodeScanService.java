package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;

/**
 * pc扫码
 * 
 * @author duangduang
 * @since 2016-11-11
 */
public interface QRCodeScanService {

	/**
	 * 监听二维码已扫标识
	 * 
	 * @param token
	 * @param isInstallment
	 * @return
	 */
	boolean listenScanResult(String token, String isInstallment);

	/**
	 * 获取微信支付/支付宝支付的二维码
	 * 
	 * @param token
	 * @param payType
	 * @return
	 * @throws Exception 
	 */
	String getQrCode(String token, PayTypeEnum payType);

}
