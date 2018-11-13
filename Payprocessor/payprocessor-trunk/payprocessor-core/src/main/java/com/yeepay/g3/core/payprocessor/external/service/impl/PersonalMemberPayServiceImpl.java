package com.yeepay.g3.core.payprocessor.external.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.component.member.dto.MemberFundTradeInfoDTO;
import com.yeepay.g3.component.member.dto.MemberPaymentDTO;
import com.yeepay.g3.component.member.enumtype.MemberTradeTypeEnum;
import com.yeepay.g3.component.member.enumtype.TradeCurrencyTypeEnum;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.external.service.PersonalMemberPayService;
import com.yeepay.g3.core.payprocessor.service.PayRecordService;
import com.yeepay.g3.core.payprocessor.service.PersonalMemberPayResultProccess;
import com.yeepay.g3.core.payprocessor.service.SendMqService;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.member.exception.MemberSysBaseException;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 
 * @author：zhangxh
 * @since：2017年5月16日 上午11:24:45
 * @version:
 */
@Service
public class PersonalMemberPayServiceImpl extends AbstractService implements
		PersonalMemberPayService {

	private static final Logger logger = PayLoggerFactory
			.getLogger(PersonalMemberPayServiceImpl.class);

	@Autowired
	private SendMqService sendMqService;
	
	@Autowired
	private PayRecordService payRecordService;

	@Autowired
	private PersonalMemberPayResultProccess personalMemberPayResultProccess;

	@Override
	public String syncPay(PersonalMemberSyncPayRequestDTO requestDTO,
			PayRecord payRecord,PersonalMemberSyncPayResponseDTO response ) {
		MemberPaymentDTO payRequestParam = buildPayRequestParam(requestDTO,
				payRecord);
		String fundFlowId = null;
		try {
			fundFlowId = g2MemberComponentFacade.launchPaymentNew(payRequestParam);
		}catch (IllegalArgumentException e) {
			throw new PayBizException(ErrorCode.P9001001);
		}catch (YeepayBizException e) {
			// 支付失败
			String errorCode = null;
			String errormsg = null;
			if(e instanceof MemberSysBaseException) {
				if (StringUtils.isNotBlank(e.getDefineCode())) {
					ErrorMeta meta = ErrorCodeUtil.translateCode(ErrorCodeSource.MEMBER.getSysCode(),
							e.getDefineCode(), e.getMessage(), ErrorCode.P9004079);
					if (meta != null) {
						errorCode = meta.getErrorCode();
						errormsg = meta.getErrorMsg();
					} else {
						errorCode = "M" + e.getDefineCode();
						errormsg = e.getMessage();
					}
				}
			}
			
			StringBuilder log = new StringBuilder();
			log.append("[MEMBER_PAY业务异常,originDefine=").append(e.getDefineCode())
			.append(",originMessage=").append(e.getMessage())
			.append(",convertedCode=").append(errorCode)
			.append(",convertedMessage=").append(errormsg);
			logger.error(log.toString());
		
			payRecordService.updateNcPaymentToFail(payRecord.getRecordNo(), errorCode, errormsg);
			response.setProcessStatus(ProcessStatus.SUCCESS);
			response.setTrxStatus(TrxStatusEnum.FAILUER);
			response.setRecordNo(payRecord.getRecordNo());
			response.setResponseCode(errorCode);
			response.setResponseMsg(errormsg);
			
//			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.MEMBER.getSysCode(), e.getDefineCode(), e.getMessage(),
//					ErrorCode.P9003035);
		} catch (Throwable th) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return fundFlowId;
	}
	
	@Override
	public MemberFundTradeInfoDTO queryPayInfoByOrderNo(String orderNo,
			MemberTradeTypeEnum tradeType) {
		try {
			return g2MemberComponentFacade.queryFundTransInfoByRequestNo(
					orderNo, tradeType);
		} catch (Throwable t) {
			logger.error("[请求MEMBER-PAY查单失败]", t);
		}
		return null;
	}

	private MemberPaymentDTO buildPayRequestParam(
			PersonalMemberSyncPayRequestDTO requestDTO, PayRecord payRecord) {
		MemberPaymentDTO payRequestParam = new MemberPaymentDTO();
		// 组合支付，第一支付单金额不等于订单金额
		if(payRecord.isCombinedPay()) {
			payRequestParam.setAmount(new Amount(payRecord.getFirstPayAmount()));
		}else {
			payRequestParam.setAmount(new Amount(requestDTO.getAmount()));
		}
		payRequestParam.setClientIp(requestDTO.getPayerIp());
		payRequestParam.setCurrencyType(TradeCurrencyTypeEnum.RMB);
		payRequestParam.setMcc(requestDTO.getIndustryCode());
		payRequestParam.setMemberNo(requestDTO.getMemberNo());

		payRequestParam.setRelateFlowId(requestDTO.getOutTradeNo());// 关联的资金交易流水号
		payRequestParam.setRequestNo(payRecord.getRecordNo());// 请求编号 查状态时用到
		payRequestParam.setTradeInitiator(requestDTO.getOrderSystem());// 交易系统业务发起方
		payRequestParam.setMerchantNo(requestDTO.getCustomerNumber());
		payRequestParam.setExtParam(requestDTO.getExtParam());
		return payRequestParam;
	}

}
