package com.yeepay.g3.app.nccashier.wap.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.app.nccashier.wap.service.QRCodeScanService;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.RedisTemplate;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * pc扫码
 * @author duangduang
 * @since  2016-11-11
 */
@Service("qrCodeScanService")
public class QRCodeScanServiceImpl implements QRCodeScanService{
	
	private static final Logger logger = LoggerFactory.getLogger(QRCodeScanService.class);
	
	@Resource
	private NcCashierService ncCashierService;
	
	@Resource
	private NewWapPayService newWapPayService;

	@Override
	public boolean listenScanResult(String token, String isInstallment) {
		String isScanned = null;
		newWapPayService.validateRequestInfoDTO(token);
		long tryTimes = CommonUtil.getListenerServerTimeout();

		while (tryTimes > 0) {
			if("isInstallment".equals(isInstallment)){
				isScanned = RedisTemplate.getTargetFromRedis(Constant.INSTALLMENT_DIRECT_SCAN + token, String.class);
			}else{
				isScanned = RedisTemplate.getTargetFromRedis(Constant.SCAN_SIGN_KEY + token, String.class);
			}
			if (Constant.PC_QR_CODE_BE_SCANNED_SIGN.equals(isScanned)) {
				return true;
			}
			if (Constant.INSTALLMENT_QR_CODE_BE_SCANNED_SIGN
							.equals(isScanned)) {
				return true;
			}
			tryTimes--;
			try {
				Thread.sleep(1000);
			} catch (Throwable t) {
				logger.error("listenScanResult ex:{}", t);
			}

		}
		return false;
	}

	@Override
	public String getQrCode(String token, PayTypeEnum payType) {
		if(payType==null || !PayTypeEnum.isActiveScanPay(payType.name())){
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		String url = "";
		//主扫添加产品开通权限校验
		RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
        PayExtendInfo payExtendInfo = ncCashierService.getPayExtendInfo(info.getPaymentRequestId(),token);
        if(!payExtendInfo.containsPayType(payType)){
        	return null;
        }else{
        	url = newWapPayService.directPay(token, payType.name());
        }
		return url;
	}

}
