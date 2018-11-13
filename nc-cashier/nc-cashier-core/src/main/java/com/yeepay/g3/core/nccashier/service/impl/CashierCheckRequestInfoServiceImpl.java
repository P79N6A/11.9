package com.yeepay.g3.core.nccashier.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.NotifyProtocolEnum;
import com.yeepay.g3.core.nccashier.service.BankCardLimitInfoService;
import com.yeepay.g3.core.nccashier.service.CashierCheckRequestInfoService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.RemoteServiceCaller;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.utils.jdbc.dal.utils.StringUtils;

@Service("cashierCheckRequestInfoService")
public class CashierCheckRequestInfoServiceImpl extends NcCashierBaseService implements
		CashierCheckRequestInfoService {
	private static final Logger logger = LoggerFactory
			.getLogger(CashierCheckRequestInfoServiceImpl.class);
	@Resource
	private BankCardLimitInfoService bankCardLimitInfoService;

	@Override
	public void checkPassInfo(PaymentRequest paymentRequest) {
		String samePerson = null;
		String merchantNo = paymentRequest.getMerchantNo();
		try {
			samePerson =
					(String) RemoteServiceCaller
							.executeRemote(
									"com.yeepay.g3.facade.nctradeconfig.facade.MerchantPassCheckInoSetQueryFacade.queryMerchantPassCheckInfo",
									merchantNo, NotifyProtocolEnum.HESSIAN);
			logger.info("nctrade返回的同人参数配置信息为" + samePerson);
		} catch (Throwable e) {
			logger.error(
					"[monitor],event:nccashier_checkPassInfo_getPassCheckInfoSet error,merchantNo:{}",
					merchantNo);
		}
		if (StringUtils.isNotBlank(samePerson)) {
			BindLimitInfoResDTO bindLimitInfoResDTO =
					bankCardLimitInfoService.getSamePersonInfo(paymentRequest);
			if ("1".equals(samePerson) && null == bindLimitInfoResDTO) {
				if (StringUtils.isBlank(paymentRequest.getOwner())) {
					throw new CashierBusinessException("3200020", "姓名未传,请联系商家");
				}
				if (StringUtils.isBlank(paymentRequest.getIdCard())) {
					throw new CashierBusinessException("3200020", "身份证号码未传,请联系商家");
				}
			} else if ("1".equals(samePerson)
					&& null != bindLimitInfoResDTO
					&& !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO
							.getBindCardLimitType())) {//商户配置为同人
				if (StringUtils.isBlank(bindLimitInfoResDTO.getUserNameLimit())
						|| StringUtils.isBlank(bindLimitInfoResDTO.getIdentityNoLimit())) {
					if (StringUtils.isBlank(paymentRequest.getOwner())) {
						throw new CashierBusinessException("3200020", "姓名未传,请联系商家");
					}
					if (StringUtils.isBlank(paymentRequest.getIdCard())) {
						throw new CashierBusinessException("3200020", "身份证号码未传,请联系商家");
					}
				}

			}else if("1".equals(samePerson)
					&& null != bindLimitInfoResDTO
					&& Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO
							.getBindCardLimitType())){//商户为普通
					if (StringUtils.isBlank(paymentRequest.getOwner())) {
						throw new CashierBusinessException("3200020", "姓名未传,请联系商家");
					}
					if (StringUtils.isBlank(paymentRequest.getIdCard())) {
						throw new CashierBusinessException("3200020", "身份证号码未传,请联系商家");
					}
			}

		}
	}
}
