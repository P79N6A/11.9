package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.service.NotifyService;
import com.yeepay.g3.core.payprocessor.service.ResultProcessService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PPTracer;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.cwh.param.BankCardDetailDTO;
import com.yeepay.g3.facade.frontend.dto.PromotionInfoDTO;
import com.yeepay.g3.facade.opr.dto.BankPromotionInfoDTO;
import com.yeepay.g3.facade.opr.dto.order.PaymentCallBackResDTO;
import com.yeepay.g3.facade.opr.dto.payment.PaymentDTO;
import com.yeepay.g3.facade.opr.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.opr.enumtype.CashierTypeEnum;
import com.yeepay.g3.facade.opr.enumtype.UserTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayCallBackRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayCallBackResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.*;
import com.yeepay.g3.facade.payprocessor.facade.PayProcessorCallBackFacade;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/11/16.
 */
@Service
public class NotifyServiceImpl extends AbstractService implements NotifyService {

	private static final Logger logger = PayLoggerFactory.getLogger(NotifyServiceImpl.class);

	@Autowired
	private ResultProcessService resultProcessService;

	@Autowired
	private CombAbstractService combAbstractService;

	@Override
	public void notify(PayRecord payRecord) {
		notify(payRecord, null);
	}

	@Override
	public void notify(PayRecord payRecord, Map<String, Object> extMap) {
		if (!TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
			logger.error("PayRecord not success, do not need notify " + payRecord.getRecordNo());
			return;
		}
		PaymentRequest paymentRequest = paymentRequestDao.selectByPrimaryKey(payRecord.getRequestId());
		if (!PaymentStatusEnum.SUCCESS.name().equals(paymentRequest.getPayStatus())
				|| OrderSystemStatusEnum.SUCCESS.equals(paymentRequest.getOrderSystemStatus())
				|| OrderSystemStatusEnum.REVERSE.equals(paymentRequest.getOrderSystemStatus())) {
			logger.warn(
					"PaymentRequest not success or ordersystemstatus already success or reverse , do not need notify ordersystem. "
							+ payRecord.getRecordNo());
			return;
		}
		notifyAndUpdate(paymentRequest, payRecord, extMap);
	}

	private void notifyAndUpdate(PaymentRequest payment, PayRecord record, Map<String, Object> extMap) {
		/**
		 * 兼容回调订单处理器，其他业务方走新的回调逻辑
		 */
		if ("DS".equals(payment.getOrderSystem())) {
			PaymentDTO paymentDto = buildPaymentDto(payment, record, extMap);
			PaymentCallBackResDTO paymentCallBackResDto = paymentFacade.paymentCallBack(paymentDto);
			if (StringUtils.equals("OPR13001", paymentCallBackResDto.getCode())
					|| StringUtils.equals("OPR00000", paymentCallBackResDto.getCode())) {
				paymentRequestDao.updateRequestToNotifySuccess(payment.getId());
				// 监控埋点
				if(ConstantUtils.enableSpanLog()){
					PPTracer.paymentSuccessRateResponseSpan(payment, record);
				}
			} else if (StringUtils.equals("OPR13002", paymentCallBackResDto.getCode())
					|| StringUtils.equals("OPR13003", paymentCallBackResDto.getCode())
					|| StringUtils.equals("OPR13004", paymentCallBackResDto.getCode())
					// 该错误码支持营销类订单的冲正（ps：下面预授权的通知方法里不加是因为预授权不支持营销订单）
					|| StringUtils.equals("OPR15035", paymentCallBackResDto.getCode())) {
				resultProcessService.updatePaymentToReverse(payment, record,"订单方撤销冲正");
			}
		} else {
			PayCallBackResponseDTO response = payCallBack(payment, record);
			if (response != null) {
				if (response.getCallBackStatus() == CallBackStatusEnum.SUCCESS) {
					paymentRequestDao.updateRequestToNotifySuccess(payment.getId());
					// 监控埋点
					if(ConstantUtils.enableSpanLog()){
						PPTracer.paymentSuccessRateResponseSpan(payment, record);
					}
				} else if (response.getCallBackStatus() == CallBackStatusEnum.REVERSE) {
					resultProcessService.updatePaymentToReverse(payment, record,"订单方撤销冲正");
				}
			}
		}
	}
	/**
	 * 构造通知订单处理器参数
	 *
	 * @param paymentRequest
	 * @param payRecord
	 * @param extMap
	 * @return
	 */
	private PaymentDTO buildPaymentDto(PaymentRequest paymentRequest, PayRecord payRecord, Map<String, Object> extMap) {
		PaymentDTO paymentDto = new PaymentDTO();
		paymentDto.setPaymentExt(new HashMap<String, String>());
		paymentDto.setBankId(payRecord.getBankId());
		paymentDto.setBankOrderId(payRecord.getBankOrderNo());
		paymentDto.setBankPaySuccessDate(payRecord.getPayTime());
		paymentDto.setCardType(CardTypeEnum.getCardTypeEnum(payRecord.getCardType()));
		paymentDto.setChannelId(payRecord.getFrpCode());
		paymentDto.setCost(payRecord.getCost());
		paymentDto.setPayAmount(paymentRequest.getAmount().add(payRecord.getUserFee()).setScale(2,BigDecimal.ROUND_HALF_UP));
		// 预授权的状态，由下面单独的分支控制,不是统一的成功
		paymentDto.setPaymentStatus(com.yeepay.g3.facade.opr.enumtype.PaymentStatusEnum.SUCCESSED);
		paymentDto.setPaySuccessDate(payRecord.getPayTime());
		paymentDto.setUniqueOrderNo(paymentRequest.getDealUniqueSerialNo());
		paymentDto.setPaymentSysNo(paymentRequest.getRecordNo());
		paymentDto.setPaymentIp(payRecord.getPayerIp());
		paymentDto.setCashierType(CashierTypeEnum.getCashierTypeEnum(payRecord.getCashierVersion()));
		paymentDto.setPaymentProductString(payRecord.getPayProduct());
		paymentDto.setNetPayType(payRecord.getBankAccountType());
		paymentDto.setBankTrxId(payRecord.getBankTrxId());
		paymentDto.setInstCompany(payRecord.getLoanCompany());
		paymentDto.setInstNumber(payRecord.getLoanTerm());
		paymentDto.setPlatformType(payRecord.getPlatformType());
		paymentDto.setUserFee(payRecord.getUserFee().setScale(2,BigDecimal.ROUND_HALF_UP));
        paymentDto.setBasicProductCode(payRecord.getBasicProductCode());//基础产品码
        paymentDto.setOpenID(payRecord.getOpenId());

		//补充卡券信息
		if(extMap!=null){
			if(extMap.get(ConstantUtils.PROMOTION_CASH_FEE)!=null){
				BigDecimal cashFee = (BigDecimal) extMap.get(ConstantUtils.PROMOTION_CASH_FEE);
				paymentDto.setCashFee(cashFee);
			}
			if(extMap.get(ConstantUtils.PROMOTION_SETTLEMENT_FEE)!=null){
				BigDecimal settlementFee = (BigDecimal) extMap.get(ConstantUtils.PROMOTION_SETTLEMENT_FEE);
				paymentDto.setSettlementFee(settlementFee);
			}
			if(extMap.get(ConstantUtils.PROMOTION_INFO_DTOS)!=null){
				List<PromotionInfoDTO> promotionInfoDTOS = (List) extMap.get(ConstantUtils.PROMOTION_INFO_DTOS);
				if (CollectionUtils.isNotEmpty(promotionInfoDTOS)) {
					List<BankPromotionInfoDTO> bankPromotionInfoDTOS = new LinkedList<BankPromotionInfoDTO>();
					for (PromotionInfoDTO temp : promotionInfoDTOS) {
						BankPromotionInfoDTO bankPromotionInfoDTO = new BankPromotionInfoDTO();
						BeanUtils.copyProperties(temp,bankPromotionInfoDTO);
						bankPromotionInfoDTOS.add(bankPromotionInfoDTO);
					}
					paymentDto.setBankPromotionInfoDTOs(bankPromotionInfoDTOS);
				}
			}
		}

		if(payRecord.getExtendedInfo()!=null){
			paymentDto.setUserNo(payRecord.getExtendedInfo().getUserId());
			paymentDto.setUserType(UserTypeEnum.getUserTypeEnum(payRecord.getExtendedInfo().getUserType()));
			paymentDto.setWalletLevel(payRecord.getExtendedInfo().getWalletLevel());
//			paymentDto.setPaymentExt(payRecord.getExtendedInfo().getExtParam());
			if(payRecord.getExtendedInfo().getExtParam() != null) {
				paymentDto.getPaymentExt().putAll(payRecord.getExtendedInfo().getExtParam());
			}
		}
		// 卡账户信息
		if (StringUtils.isNotBlank(payRecord.getCardId())) {
			BankCardDetailDTO bankCardDetailDTO = bankCardCwhFacade.getBankCardDetail(payRecord.getCardId());
			if (bankCardDetailDTO != null && bankCardDetailDTO.getBaseInfo()!=null) {
				paymentDto.setBankCardNo(bankCardDetailDTO.getBaseInfo().getCardNo());
				paymentDto.setBankName(bankCardDetailDTO.getBaseInfo().getBankName());
				paymentDto.setCardName(bankCardDetailDTO.getBaseInfo().getOwner());
				paymentDto.setIdCardNo(bankCardDetailDTO.getBaseInfo().getIdcard());
				paymentDto.setMobilePhoneNo(StringUtils.isNotBlank(bankCardDetailDTO.getBaseInfo().getBankMobile())
						? bankCardDetailDTO.getBaseInfo().getBankMobile()
						: bankCardDetailDTO.getBaseInfo().getYbMobile());
			}
		}
		// 银行卡分期订单
		if(PayOrderType.BK_CFL.name().equals(payRecord.getPayOrderType())) {
			paymentDto.setBankcardInstNumber(payRecord.getCflCount());
			paymentDto.setBankcardInstFeeRate(payRecord.getCflRate());
			paymentDto.setMerchantSubsidyFeeRate(payRecord.getMerchantFeeSubsidy());
			paymentDto.setMerchantSubsidyFee(payRecord.getMerchantAmountSubsidy());
		} else if(PayOrderType.MEMBER_PAY.name().equals(payRecord.getPayOrderType())) {
			paymentDto.getPaymentExt().put("memberNo", payRecord.getDebitAccountNo());//会员编号
			
		} else if (PayOrderType.GUAR_CFL.name().equals(payRecord.getPayOrderType())) {//担保分期
		    paymentDto.setBankcardInstNumber(payRecord.getCflCount());
            paymentDto.setBankcardInstFeeRate(payRecord.getCflRate());
//          paymentDto.setMerchantSubsidyFeeRate(payRecord.getMerchantFeeSubsidy());
//          paymentDto.setMerchantSubsidyFee(payRecord.getMerchantAmountSubsidy());
		// 预授权
		}else if(validePreauthOrder(payRecord.getPayOrderType())){
			setPreAuthType(paymentDto, payRecord);
			// 预授权完成和完成撤销的订单，成功和失败，都需返回预授权完成金额
			// 并且用payAmount表示预授权完成的金额
			if(PayOrderType.PREAUTH_CM.name().equals(payRecord.getPayOrderType())
					|| PayOrderType.PREAUTH_CC.name().equals(payRecord.getPayOrderType())) {
				paymentDto.setPayAmount((payRecord.getAmount().add(payRecord.getUserFee())).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
			if(TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
				paymentDto.setPaymentStatus(com.yeepay.g3.facade.opr.enumtype.PaymentStatusEnum.SUCCESSED);
			}else if(TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())) {
				paymentDto.setPaymentStatus(com.yeepay.g3.facade.opr.enumtype.PaymentStatusEnum.FAILED);
			}else {
				paymentDto.setPaymentStatus(com.yeepay.g3.facade.opr.enumtype.PaymentStatusEnum.PROCESSING);
			}
		} else if(PayOrderType.CFL_EASY.name().equals(payRecord.getPayOrderType())) {
			// 分期易
			paymentDto.setInstNumber(String.valueOf(payRecord.getCflCount()));
		}
		// 组合支付
		if(payRecord.isCombinedPay()) {
			CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
			if(combPayRecord != null) {
				Map<String, String> paymentExt = paymentDto.getPaymentExt();
				paymentExt.put("payOrderType", combPayRecord.getPayOrderType());
				paymentExt.put("payOrderNo", combPayRecord.getPayOrderNo());
				paymentExt.put("bankOrderNo", combPayRecord.getBankOrderNo());
				paymentExt.put("amount", combPayRecord.getAmount() == null ? null : combPayRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				paymentExt.put("paySuccDate", combPayRecord.getPayTime() == null ? null : combPayRecord.getPayTime().toString());
				paymentExt.put("channelId", combPayRecord.getFrpCode());
				paymentExt.put("payProduct", combPayRecord.getPayProduct());
				paymentExt.put("firstPayAmount", payRecord.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				paymentExt.put("status", combPayRecord.getStatus());
				paymentExt.put("isCombinePay", "true");
				paymentDto.getPaymentExt().putAll(paymentExt);
			}
		}

		return paymentDto;
	}

	private PayCallBackRequestDTO buildCallBackRequestDto(PaymentRequest paymentRequest, PayRecord payRecord) {
		PayCallBackRequestDTO callBackDto = new PayCallBackRequestDTO();
		callBackDto.setOrderNo(paymentRequest.getOrderNo());
		callBackDto.setOrderSystem(paymentRequest.getOrderSystem());
		callBackDto.setRequestSystem(payRecord.getRequestSystem());
		callBackDto.setRequestSysId(payRecord.getRequestSysId());
		callBackDto.setCustomerName(paymentRequest.getCustomerName());
		callBackDto.setCustomerNo(paymentRequest.getCustomerNo());
		callBackDto.setBankId(payRecord.getBankId());
		callBackDto.setBankOrderNo(payRecord.getBankOrderNo());
		callBackDto.setBankSeq(payRecord.getBankSeq());
		callBackDto.setBankTrxId(payRecord.getBankTrxId());
		callBackDto.setCardType(payRecord.getCardType());
		callBackDto.setCashierVersion(payRecord.getCashierVersion());
		callBackDto.setPayOrderType(payRecord.getPayOrderType());
		callBackDto.setChannelId(payRecord.getFrpCode());
		callBackDto.setCost(payRecord.getCost());
		callBackDto.setDealUniqueSerialNo(paymentRequest.getDealUniqueSerialNo());
		callBackDto.setLoanCompany(payRecord.getLoanCompany());
		callBackDto.setLoanTerm(payRecord.getLoanTerm());
		callBackDto.setNetPayType(payRecord.getBankAccountType());
//		callBackDto.setPayAmount(payRecord.getAmount());
		callBackDto.setPayerIp(payRecord.getPayerIp());
		callBackDto.setPayProduct(payRecord.getPayProduct());
		callBackDto.setPayTime(payRecord.getPayTime());
		callBackDto.setChannelId(payRecord.getFrpCode());
		callBackDto.setPayAmount(paymentRequest.getAmount().add(payRecord.getUserFee()).setScale(2, BigDecimal.ROUND_HALF_UP));
		callBackDto.setRecordNo(payRecord.getRecordNo());
		callBackDto.setPayStatus(payRecord.getStatus());
		callBackDto.setUserFee(payRecord.getUserFee().setScale(2, BigDecimal.ROUND_HALF_UP));
		callBackDto.setBasicProductCode(payRecord.getBasicProductCode());
		callBackDto.setOpenId(payRecord.getOpenId());

//		add by xueping.ni 20170526
		if(StringUtils.isNotBlank(payRecord.getPlatformType())){
			callBackDto.setPlatformType(payRecord.getPlatformType());
		}
		callBackDto.setDebitAccountNo(payRecord.getDebitAccountNo());//个人会员支付时该字段为memberNo
		callBackDto.setDebitCustomerNo(payRecord.getDebitCustomerNo());
		
		if(payRecord.getExtendedInfo()!=null){
			callBackDto.setUserId(payRecord.getExtendedInfo().getUserId());
			callBackDto.setUserType(payRecord.getExtendedInfo().getUserType());
			callBackDto.setExtendMsg(ConstantUtils.transferExtendMsg(payRecord.getExtendedInfo()));
			callBackDto.setWalletLevel(payRecord.getExtendedInfo().getWalletLevel());// added by zhijun.wang 20171124
		}
		// 卡账户信息
		if (StringUtils.isNotBlank(payRecord.getCardId())) {
			BankCardDetailDTO bankCardDetailDTO = bankCardCwhFacade.getBankCardDetail(payRecord.getCardId());
			if (bankCardDetailDTO != null && bankCardDetailDTO.getBaseInfo() != null) {
				callBackDto.setCardNo(bankCardDetailDTO.getBaseInfo().getCardNo());
				callBackDto.setBankName(bankCardDetailDTO.getBaseInfo().getBankName());
				callBackDto.setOwner(bankCardDetailDTO.getBaseInfo().getOwner());
				callBackDto.setIdCardNo(bankCardDetailDTO.getBaseInfo().getIdcard());
				callBackDto.setMobilePhoneNo(StringUtils.isNotBlank(bankCardDetailDTO.getBaseInfo().getBankMobile())
						? bankCardDetailDTO.getBaseInfo().getBankMobile()
						: bankCardDetailDTO.getBaseInfo().getYbMobile());
			}
		}
		// 银行卡分期订单
		if(PayOrderType.BK_CFL.name().equals(payRecord.getPayOrderType())) {
			callBackDto.setCflCount(payRecord.getCflCount());
			callBackDto.setCflRate(payRecord.getCflRate());
			callBackDto.setMerchantFeeSubsidy(payRecord.getMerchantFeeSubsidy());
			callBackDto.setMerchantAmountSubsidy(payRecord.getMerchantAmountSubsidy());
		}
		//个人会员支付
		if(PayOrderType.MEMBER_PAY.name().equals(payRecord.getPayOrderType())) {
			//没有特殊字段
		}
		//担保分期
		if (PayOrderType.GUAR_CFL.name().equals(payRecord.getPayOrderType())) {
		    callBackDto.setCflCount(payRecord.getCflCount());
            callBackDto.setCflRate(payRecord.getCflRate());
		}
		if(payRecord.isCombinedPay()) {
			CombResponseDTO combResponseDTO = combAbstractService.bulidCombResponse(payRecord.getRecordNo());
			callBackDto.setCombResponseDTO(combResponseDTO);
			callBackDto.setFirstPayAmount(payRecord.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		// 预授权
		if(validePreauthOrder(payRecord.getPayOrderType())) {
			callBackDto.setPreAuthAmount(paymentRequest.getPreAuthAmount());
			callBackDto.setReturnCode(payRecord.getErrorCode());
			callBackDto.setReturnMsg(payRecord.getErrorMsg());
		}
		// 分期易
		if(PayOrderType.CFL_EASY.name().equals(payRecord.getPayOrderType())) {
			callBackDto.setCflCount(payRecord.getCflCount());
		}
		return callBackDto;
	}

	/**
	 * 判断是否预授权
	 */
	private boolean validePreauthOrder(String payOrderType){
		boolean preauthOrder = false;
		if (PayOrderType.PREAUTH_RE.name().equals(payOrderType)
				|| PayOrderType.PREAUTH_CM.name().equals(payOrderType)
				|| PayOrderType.PREAUTH_CC.name().equals(payOrderType)
				|| PayOrderType.PREAUTH_CL.name().equals(payOrderType)) {
			preauthOrder = true;
		}
		return preauthOrder;
		
	}

	/**
	 * 根据订单类型转换成opr的预授权类型
	 */
	private void setPreAuthType(PaymentDTO paymentDto, PayRecord payRecord) {
		if(PayOrderType.PREAUTH_RE.name().equals(payRecord.getPayOrderType())) {
			paymentDto.setPreAuthTypeString(ConstantUtils.OPR_PRE_AUTH);
		}else if(PayOrderType.PREAUTH_CM.name().equals(payRecord.getPayOrderType())) {
			paymentDto.setPreAuthTypeString(ConstantUtils.OPR_PRE_AUTH_COMPLETE);
		}else if(PayOrderType.PREAUTH_CL.name().equals(payRecord.getPayOrderType())) {
			paymentDto.setPreAuthTypeString(ConstantUtils.OPR_PRE_AUTH_REPEAL);
		}else if(PayOrderType.PREAUTH_CC.name().equals(payRecord.getPayOrderType())) {
			paymentDto.setPreAuthTypeString(ConstantUtils.OPR_PRE_AUTH_COMPLETEREPEAL);
		}
	}

	private PayCallBackResponseDTO payCallBack(PaymentRequest paymentRequest, PayRecord payRecord) {
		PayCallBackResponseDTO callbackResult = null;
		try {
			String orderSys = paymentRequest.getOrderSystem();
			String callBackUrl = ConstantUtils.getCallBackServiceUrl(orderSys);
			PayCallBackRequestDTO dto = buildCallBackRequestDto(paymentRequest, payRecord);
			PayProcessorCallBackFacade callBackFacade = null;
			if (StringUtils.isBlank(callBackUrl)) {
				throw new RuntimeException("[callBackFacade] orderSys=" + orderSys + " 没有配置[callBackFacade]接口");
			} 
			
			String type = "HESSIAN";
			if (callBackUrl.indexOf(",") != -1) {
				type = callBackUrl.split(",")[1];
				callBackUrl = callBackUrl.split(",")[0];
			}
			if (type.equals("HESSIAN")) {
				// HTTPINVOKER 不支持超时回调
				callBackFacade = RemoteServiceFactory.getService(PayProcessorCallBackFacade.class,
						"protocol=" + RemotingProtocol.HESSIAN + "&serviceUrl=" + callBackUrl.toString());
			} else {
				callBackFacade = RemoteServiceFactory.getService(callBackUrl.toString(),
						RemotingProtocol.HTTPINVOKER, PayProcessorCallBackFacade.class);
			}
			
			if (callBackFacade != null) {
				logger.info("[callBackFacade|hessian]-[dealUniqueSerialNo=" + paymentRequest.getDealUniqueSerialNo()
						+ " callBackDto=" + dto + "]");
				callbackResult = callBackFacade.payCallBack(dto);
				logger.info("[callBackFacade|hessian]-[dealUniqueSerialNo=" + paymentRequest.getDealUniqueSerialNo()
						+ " callbackResult=" + callbackResult + "]");
			}
		} catch (Throwable e) {
			logger.warn("[callBackFacade|hessian] -[dealUniqueSerialNo=" + paymentRequest.getDealUniqueSerialNo() + "]"
					+ "dubbo接口回调订单方出现异常", e);
		}
		return callbackResult;
	}


	/**
	 * 预授权通知业务方
	 */
	@Override
	public void notifyForPreAuth(PayRecord payRecord) {
		if (!TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())
				&& !TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())) {
			logger.error("PayRecord not success or failure, do not need notify " + payRecord.getRecordNo());
			return;
		}
		PaymentRequest paymentRequest = paymentRequestDao.selectByPrimaryKey(payRecord.getRequestId());
		try{
			notifyAndUpdateForPreAuth(paymentRequest, payRecord);
		}catch (Throwable t) {
			logger.error("通知业务方异常，recordNo=" + payRecord.getRecordNo(), t);
		}

	}


	/**
	 * 预授权
	 * 通知业务方
	 * 只支持预授权的冲正
	 * @param payment
	 * @param record
	 */
	private void notifyAndUpdateForPreAuth(PaymentRequest payment, PayRecord record) {
		if ("DS".equals(payment.getOrderSystem())) {
			PaymentDTO paymentDto = buildPaymentDto(payment, record, null);
			PaymentCallBackResDTO paymentCallBackResDto = paymentFacade.paymentCallBack(paymentDto);
			if (StringUtils.equals("OPR13001", paymentCallBackResDto.getCode())
					|| StringUtils.equals("OPR00000", paymentCallBackResDto.getCode())) {
				paymentRequestDao.updateRequestToNotifySuccess(payment.getId());
			} else if (StringUtils.equals("OPR13002", paymentCallBackResDto.getCode())
					|| StringUtils.equals("OPR13003", paymentCallBackResDto.getCode())
					|| StringUtils.equals("OPR13004", paymentCallBackResDto.getCode())) {
				if(PayOrderType.PREAUTH_RE.name().equals(record.getPayOrderType())) {
					resultProcessService.updatePaymentToReverse(payment, record,"订单方撤销冲正");
				}else {
					logger.error("非正常的预授权订单冲正，recordNo=" + record.getRecordNo());
				}
			}
		} else {
			PayCallBackResponseDTO response = payCallBack(payment, record);
			if (response != null) {
				if (response.getCallBackStatus() == CallBackStatusEnum.SUCCESS) {
					paymentRequestDao.updateRequestToNotifySuccess(payment.getId());
				} else if (response.getCallBackStatus() == CallBackStatusEnum.REVERSE) {
					if(PayOrderType.PREAUTH_RE.name().equals(record.getPayOrderType())) {
						resultProcessService.updatePaymentToReverse(payment, record,"订单方撤销冲正");
					}else {
						logger.error("非正常的预授权订单冲正，recordNo=" + record.getRecordNo());
					}
				}
			}
		}
	}

	/**
	 * 通知opr暂停入账
	 * @param payment
	 * @param record
	 */
	@Override
	public void notifyOprNotToAccount(PaymentRequest payment, PayRecord record) {
		if ("DS".equals(payment.getOrderSystem())) {
			PaymentDTO paymentDto = buildPaymentDto(payment, record, null);
			// 通知opr预授权完成撤销，暂停入账
			paymentDto.setPreAuthTypeString(ConstantUtils.OPR_PRE_AUTH_COMPLETEPAUSE);
			PaymentCallBackResDTO paymentCallBackResDto = paymentFacade.paymentCallBack(paymentDto);
			if (StringUtils.equals("OPR13001", paymentCallBackResDto.getCode())
					|| StringUtils.equals("OPR00000", paymentCallBackResDto.getCode())) {
				logger.info("预授权完成撤销，通知opr暂停入账成功，recordNo=" + record.getRecordNo() + ", outTradeNo=" + payment.getOutTradeNo());
			// 预授权完成撤销时，需要先通知不要入账，如果返回不是成功码，直接抛异常，中断完成撤销操作
			} else {
				logger.error("预授权完成撤销，通知opr暂停入账失败, recordNo=" + record.getRecordNo() + ", outTradeNo=" + payment.getOutTradeNo());
				throw new PayBizException(ErrorCode.P9003056);
			}
		} else {
			PayCallBackResponseDTO response = payCallBack(payment, record);
			if (response != null) {
				if (response.getCallBackStatus() == CallBackStatusEnum.SUCCESS) {
					logger.info("预授权完成撤销，通知业务方暂停入账成功，recordNo=" + record.getRecordNo() + ", outTradeNo=" + payment.getOutTradeNo());
				}else {
					logger.error("预授权完成撤销，通知业务方暂停入账失败, recordNo=" + record.getRecordNo() + ", outTradeNo=" + payment.getOutTradeNo());
					throw new PayBizException(ErrorCode.P9003056);
				}
			}else {
				logger.error("预授权完成撤销，通知业务方暂停入账失败, recordNo=" + record.getRecordNo() + ", outTradeNo=" + payment.getOutTradeNo());
				throw new PayBizException(ErrorCode.P9003056);
			}
		}

	}

}
