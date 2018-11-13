package com.yeepay.g3.core.nccashier.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.payprocessor.dto.*;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.InstallmentPayTypeEnum;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.gateway.service.CallFeeService;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantConfigCenterService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.CashierBankCardService;
import com.yeepay.g3.core.nccashier.service.InstallmentService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.SignCardService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.fee.front.dto.CalFeeByRoleRequestDTO;
import com.yeepay.g3.facade.fee.front.dto.CalFeeByRoleResultDTO;
import com.yeepay.g3.facade.fee.front.enumtype.CalFeeOwnerSourceTypeEnum;
import com.yeepay.g3.facade.fee.front.enumtype.CalFeeRoleTypeEnum;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SignCardIdEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Service("installmentService")
public class InstallmentServiceImpl extends NcCashierBaseService implements InstallmentService {

	private static Logger logger = LoggerFactory.getLogger(InstallmentService.class);

	@Resource
	private PayProcessorService payProcessorService;

	@Resource
	private PaymentProcessService paymentProcessService;
	
	@Resource
	private NewOrderHandleService newOrderHandleService;
	
	@Resource
	private SignCardService signCardService;
	
	@Resource
	private CashierBankCardService cashierBankCardService;
	
	@Resource
	private CwhService cwhService;
	
	@Resource
	private CallFeeService callFeeService;
	
	@Resource
	private MerchantConfigCenterService merchantConfigCenterService;

	@Override
	public void signedCardOrder(InstallmentInfoNeeded installmentPayInfo) {
		PaymentRequest paymentRequest = installmentPayInfo.getPaymentRequest();
		PaymentRecord paymentRecord = installmentPayInfo.getPaymentRecord();
		InstallmentBankInfo currentBankInfo = installmentPayInfo.getCurrentBankInfo();
		BusinessSubsidie subsidie = installmentPayInfo.getBusinessSubsidie();
		NcPayCflOrderRequestDTO cflOrderRequestDTO = buildNcPayCflOrderRequestDTO(paymentRequest, paymentRecord,
				currentBankInfo, subsidie);
		NcPayCflOrderResponseDTO orderResp = payProcessorService.bankInstallmentRequest(cflOrderRequestDTO);
		paymentProcessService.updateRecordNo(paymentRecord.getId(), "", orderResp.getRecordNo(),
				PayRecordStatusEnum.ORDERED);
	}

	
	@Override
	public void smsSend(CombinedPaymentDTO installmentPayInfo) {
		PaymentRequest paymentRequest = installmentPayInfo.getPaymentRequest();
		PaymentRecord paymentRecord = installmentPayInfo.getPaymentRecord();
		try {
			NcPayCflSmsResponseDTO smsResponse = sendSms(paymentRequest, paymentRecord);
			if (PayRecordStatusEnum.SMS_SEND != paymentRecord.getState()) {
				paymentProcessService.updateRecordNo(paymentRecord.getId(), "", smsResponse.getRecordNo(),
						PayRecordStatusEnum.SMS_SEND);
			}
		} catch (CashierBusinessException e) {
			if ("N9003015".equals(e.getDefineCode())) {
				// 短信发送次数超限，此时该笔支付子单已经完结，无法再次支付，需要将record置为PAYING
				paymentProcessService.recoverRecordToObjStatus(paymentRecord.getId(), PayRecordStatusEnum.PAYING,
						PayRecordStatusEnum.SMS_SEND);
			}
			throw e;
		}

	}
	
	/**
	 * @title 发短验
	 * @description 构造支付处理器入参，调用支付处理器发短验接口
	 * @param paymentRequest
	 * @param paymentRecord
	 * @return
	 */
	private NcPayCflSmsResponseDTO sendSms(PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		NcPayCflSmsRequestDTO smsRequestDTO = buildNcPayCflSmsRequestDTO(paymentRequest, paymentRecord);
		return payProcessorService.bankInstallmentSendSms(smsRequestDTO);
	}

	@Override
	public UrlInfo openAndPay(InstallmentInfoNeeded installmentPayInfo) {
		PaymentRequest paymentRequest = installmentPayInfo.getPaymentRequest();
		PaymentRecord paymentRecord = installmentPayInfo.getPaymentRecord();
		InstallmentBankInfo currentBankInfo = installmentPayInfo.getCurrentBankInfo();
		BusinessSubsidie businessSubsidie = installmentPayInfo.getBusinessSubsidie();
		NcPayCflOpenRequestDTO orderAndPayRequestDTO = buildNcPayCflOpenRequestDTO(paymentRequest, paymentRecord,
				currentBankInfo, businessSubsidie);
		NcPayCflOpenResponseDTO cflOpenAndPayResponse = payProcessorService
				.bankInstallmentOpenAndPay(orderAndPayRequestDTO);
		// 把支付处理器的支付子订单号更新到无卡收银台的支付记录表中
		paymentProcessService.updateRecordNo(paymentRecord.getId(), "", cflOpenAndPayResponse.getRecordNo(),
				PayRecordStatusEnum.ORDERED); // ordered状态的订单可以改成success
		return buildUrlInfo(cflOpenAndPayResponse);
	}

	private UrlInfo buildUrlInfo(NcPayCflOpenResponseDTO cflOpenAndPayResponse) {
		UrlInfo urlInfo = new UrlInfo();
		urlInfo.setUrl(cflOpenAndPayResponse.getPayUrl());
		urlInfo.setCharset(cflOpenAndPayResponse.getCharset());
		urlInfo.setParams(cflOpenAndPayResponse.getParamMap());
		urlInfo.setMethod(cflOpenAndPayResponse.getMethod());
		return urlInfo;
	}

	private NcPayCflOpenRequestDTO buildNcPayCflOpenRequestDTO(PaymentRequest paymentRequest,
			PaymentRecord paymentRecord, InstallmentBankInfo currentBankInfo, BusinessSubsidie subsidie) {
		NcPayCflOpenRequestDTO cflOpenPayRequestDTO = new NcPayCflOpenRequestDTO();
		buildBasicRequestDTO(paymentRequest, paymentRecord, cflOpenPayRequestDTO);
		String riskInfo = buildTradeRiskInfoByUseripAndRequest(paymentRequest.getUserIp(), paymentRequest);
		cflOpenPayRequestDTO.setGoodsInfo(riskInfo);
		cflOpenPayRequestDTO.setCardNo(paymentRecord.getCardNo());
		if(CashierVersionEnum.API.name().equals(paymentRequest.getCashierVersion())){
			cflOpenPayRequestDTO.setPageCallBack(CommonUtil.getApiFrontRedirectUrl(paymentRequest.getMerchantNo(), paymentRequest.getId()));
		}else{
			cflOpenPayRequestDTO.setPageCallBack(CommonUtil.getH5FrontCallbackUrl(paymentRequest.getMerchantNo(), paymentRecord.getTokenId()));
		}
		cflOpenPayRequestDTO.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
		cflOpenPayRequestDTO.setPayScene(paymentRequest.getBizModeCode());
		cflOpenPayRequestDTO.setPayOrderType(PayOrderType.BK_CFL); 
		// paymentRecord在设置memberType时 已经保证了非空
		cflOpenPayRequestDTO.setMemberType(MemberTypeEnum.valueOf(paymentRecord.getMemberType()));
		cflOpenPayRequestDTO.setMemberNO(paymentRecord.getMemberNo());
		cflOpenPayRequestDTO.setPayTool(paymentRecord.getPayTool());
		// 期数的类型
		cflOpenPayRequestDTO.setCflCount(paymentRecord.getPeriod());
		cflOpenPayRequestDTO.setCflRate(new BigDecimal(currentBankInfo.getInstallmentRateInfos().get(0).getRate()).divide(new BigDecimal(100)));
		FeeInfo feeInfo = calAllowanceFeeInfo(paymentRequest, currentBankInfo.getBankInfo().getBankCode(), paymentRecord.getPeriod()+"");
		cflOpenPayRequestDTO.setMerchantAmountSubsidy(feeInfo==null?null:feeInfo.getPayeeFee());
		cflOpenPayRequestDTO.setMerchantFeeSubsidy(feeInfo==null?null:feeInfo.getPayeeCalPolicyVal());
		/* 设置零售产品码和基础产品码 */
		cflOpenPayRequestDTO.setBasicProductCode(
				CommonUtil.getBasicProductCode(paymentRecord.getPayTool(), paymentRequest.getTradeSysNo()));
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		cflOpenPayRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));
		return cflOpenPayRequestDTO;
	}
	
	/**
	 * 调用计费获取分期补贴费率
	 * 
	 * @param paymentRequest
	 * @return
	 */
	private FeeInfo calAllowanceFeeInfo(PaymentRequest paymentRequest, String bankCode,
			String number) {
		CalFeeByRoleRequestDTO requestDTO = buildCalFeeByRoleRequestDTO(paymentRequest, bankCode, number);
		CalFeeByRoleResultDTO result = callFeeService.callFeeByRole(requestDTO);
		return buildFeeInfo(result);
	}
	
	/**
	 * 构造银行卡分期商户补贴手续费的入参
	 * 
	 * @param paymentRequest
	 * @param bankCode
	 * @param number
	 * @return
	 */
	private CalFeeByRoleRequestDTO buildCalFeeByRoleRequestDTO(PaymentRequest paymentRequest, String bankCode,
			String number) {
		CalFeeByRoleRequestDTO requestDTO = new CalFeeByRoleRequestDTO();
		requestDTO.setAmount(paymentRequest.getOrderAmount());
		requestDTO.setBankInterNumber(Constant.ALL);
		requestDTO.setCustomerName(paymentRequest.getMerchantName());
		requestDTO.setCustomerNumber(paymentRequest.getMerchantNo());
		requestDTO.setFeeRole(CalFeeRoleTypeEnum.PAYEE);
		requestDTO.setFlowNumber(UUID.randomUUID()+"");
		requestDTO.setOrderNumber(paymentRequest.getMerchantOrderId());
		ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
		FeeParam	feeConfig = CommonUtil.getCalFeeInfo(paymentRequest.getTradeSysNo(), StringUtils.isBlank(extendInfoFromPayRequest.getProductVersion())?Constant.DS_USER_FEE_ITEM:extendInfoFromPayRequest.getProductVersion(), Constant.INSTALLMENT_ALLOWANCE_PAY_PRODUCT);
		requestDTO.setCalFeeItemStr(feeConfig.getCalFeeItem());
		requestDTO.setPayProduct(feeConfig.getPayProduct());
		requestDTO.setPayWay(bankCode);
		requestDTO.setVersion(number);
		requestDTO.setSource(CalFeeOwnerSourceTypeEnum.MERCHANT);
		requestDTO.setSysCode(SysCodeEnum.NCCASHIER.name());
		requestDTO.setTransferTime(paymentRequest.getPayTime());
		return requestDTO;
	}

	/**
	 * 构造费率返回值
	 * 
	 * @param calFeeByRoleResultDTO
	 * @return
	 */
	private FeeInfo buildFeeInfo(CalFeeByRoleResultDTO calFeeByRoleResultDTO) {
		if(calFeeByRoleResultDTO==null){
			return null;
		}
		FeeInfo feeInfo = new FeeInfo();
		feeInfo.setChargeType(calFeeByRoleResultDTO.getChargeType().name());
		feeInfo.setFeeRole(calFeeByRoleResultDTO.getFeeRole().name());
		feeInfo.setPayeeFee(calFeeByRoleResultDTO.getPayeeFee());
		feeInfo.setPayerFee(calFeeByRoleResultDTO.getPayerFee());
		feeInfo.setPayeeCalPolicy(calFeeByRoleResultDTO.getPayeeCalPolicy());
		feeInfo.setPayerCalPolicy(calFeeByRoleResultDTO.getPayerCalPolicy());
		feeInfo.setPayeeCalPolicyVal(calFeeByRoleResultDTO.getPayeeCalPolicyVal());
		return feeInfo;
	}


	private NcPayCflOrderRequestDTO buildNcPayCflOrderRequestDTO(PaymentRequest paymentRequest,
			PaymentRecord paymentRecord, InstallmentBankInfo currentBankInfo, BusinessSubsidie subsidie) {
		NcPayCflOrderRequestDTO cflOrderRequestDTO = new NcPayCflOrderRequestDTO();
		buildBasicRequestDTO(paymentRequest, paymentRecord, cflOrderRequestDTO);
		String riskInfo = buildTradeRiskInfoByUseripAndRequest(paymentRequest.getUserIp(), paymentRequest);
		cflOrderRequestDTO.setGoodsInfo(riskInfo);
		cflOrderRequestDTO.setCardNo(paymentRecord.getCardNo());
		cflOrderRequestDTO.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
		cflOrderRequestDTO.setPayOrderType(PayOrderType.BK_CFL); 
		cflOrderRequestDTO.setMemberType(MemberTypeEnum.valueOf(paymentRecord.getMemberType()));
		cflOrderRequestDTO.setMemberNO(paymentRecord.getMemberNo());
		cflOrderRequestDTO.setUserId(paymentRequest.getIdentityId());
		cflOrderRequestDTO.setUserType(paymentRequest.getIdentityType());
		cflOrderRequestDTO.setPayTool(paymentRecord.getPayTool());
		cflOrderRequestDTO.setCflCount(paymentRecord.getPeriod());
		if(currentBankInfo.getTargetInstallmentRateInfo()!=null){
			cflOrderRequestDTO.setCflRate(new BigDecimal(currentBankInfo.getTargetInstallmentRateInfo().getRate()).divide(new BigDecimal(100)));
		}else{
			cflOrderRequestDTO.setCflRate(new BigDecimal(currentBankInfo.getInstallmentRateInfos().get(0).getRate()).divide(new BigDecimal(100)));
		}
		
		FeeInfo feeInfo = calAllowanceFeeInfo(paymentRequest, currentBankInfo.getBankInfo().getBankCode(), paymentRecord.getPeriod()+"");
		cflOrderRequestDTO.setMerchantAmountSubsidy(feeInfo==null?null:feeInfo.getPayeeFee());
		cflOrderRequestDTO.setMerchantFeeSubsidy(feeInfo==null?null:feeInfo.getPayeeCalPolicyVal());
		
		if (InstallmentPayTypeEnum.SIGN_RELATION.name().equals(paymentRecord.getPayType())) {
			cflOrderRequestDTO.setSignCardIdType(SignCardIdEnum.SIGN_RELATION); 
			cflOrderRequestDTO.setSignCardId(Long.valueOf(paymentRecord.getBindId()));
		} else {
			cflOrderRequestDTO.setSignCardIdType(SignCardIdEnum.SIGN_INFO);
			cflOrderRequestDTO.setSignCardId(Long.valueOf(paymentRecord.getCardInfoId()));
		}
		cflOrderRequestDTO.setBasicProductCode(
				CommonUtil.getBasicProductCode(paymentRecord.getPayTool(), paymentRequest.getTradeSysNo()));
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		cflOrderRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));
		return cflOrderRequestDTO;
	}

	private NcPayCflSmsRequestDTO buildNcPayCflSmsRequestDTO(PaymentRequest paymentRequest,
			PaymentRecord paymentRecord) {
		NcPayCflSmsRequestDTO smsRequestDTO = new NcPayCflSmsRequestDTO();
		smsRequestDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
		smsRequestDTO.setMobileNo(paymentRecord.getPhoneNo());
		return smsRequestDTO;
	}

	private NcPayCflConfirmRequestDTO buildNcPayCflConfirmRequestDTO(PaymentRecord payrecord, String verifyCode) {
		NcPayCflConfirmRequestDTO confirmDTO = new NcPayCflConfirmRequestDTO();
		confirmDTO.setRecordNo(payrecord.getPaymentOrderNo());
		confirmDTO.setSmsCode(verifyCode);
		return confirmDTO;
	}
	private NcPayCflSynConfirmRequestDTO buildNcPayCflSynConfirmRequestDTO(PaymentRecord payrecord, String verifyCode) {
		NcPayCflSynConfirmRequestDTO confirmDTO = new NcPayCflSynConfirmRequestDTO();
		confirmDTO.setRecordNo(payrecord.getPaymentOrderNo());
		confirmDTO.setSmsCode(verifyCode);
		return confirmDTO;
	}

	@Override
	public void comfirmPay(PaymentRecord payrecord, String verifyCode) {
		paymentProcessService.avoidRepeatPayWithException(payrecord, new PayRecordStatusEnum[] { PayRecordStatusEnum.SMS_SEND }); // 防重处理
		NcPayCflConfirmRequestDTO confirmDTO = buildNcPayCflConfirmRequestDTO(payrecord, verifyCode);
		try {
			payProcessorService.bankInstallmentConfirmPay(confirmDTO);
		} catch (CashierBusinessException e) {
			// 针对短信校验的出错误码，可以支持重复支付，支付订单的状态回滚
			// N400094 短信验证码错误，N400091 短语验证码已经过期
			if ("N400094".equals(e.getDefineCode()) || "N400091".equals(e.getDefineCode()) || "N9003016".equals(e.getDefineCode())) {
				paymentProcessService.recoverRecordToObjStatus(payrecord.getId(), PayRecordStatusEnum.SMS_SEND, PayRecordStatusEnum.PAYING);
				throw e;
			} else {
				throw new CashierBusinessException(Errors.INSTALLMENT_PAY_EXCEPTION);
			}
		}
	}

	@Override
	public void syncComfirmPay(PaymentRecord payrecord, String verifyCode) {
		boolean isRepeatePay = updateRecordToPaying(payrecord.getId());
		if (isRepeatePay) {
			throw new CashierBusinessException(Errors.REPEAT_ORDER);
		}
		NcPayCflSynConfirmRequestDTO confirmDTO = buildNcPayCflSynConfirmRequestDTO(payrecord, verifyCode);
		try {
			payProcessorService.bankInstallmentSyncConfirmPay(confirmDTO);
		} catch (CashierBusinessException e) {
			// 针对短信校验的出错误码，可以支持重复支付，支付订单的状态回滚
			// N400094 短信验证码错误，N400091 短语验证码已经过期
			if ("N400094".equals(e.getDefineCode()) || "N400091".equals(e.getDefineCode()) || "N9003016".equals(e.getDefineCode())) {
				paymentProcessService.recoverRecordToObjStatus(payrecord.getId(), PayRecordStatusEnum.SMS_SEND, PayRecordStatusEnum.PAYING);
			}
			throw e;
		}
	}
	
	@Override
	public void checkSmsSend(PaymentRecord payrecord, boolean isneedVeryCode){
		PayRecordQueryRequestDTO requestDTO = new PayRecordQueryRequestDTO();
		requestDTO.setRecordNo(payrecord.getPaymentOrderNo());
		PayRecordResponseDTO responseDTO = payProcessorService.query(requestDTO);
		if (isneedVeryCode && (responseDTO.getSmsState() == SmsCheckResultEnum.NONE)) {
			throw CommonUtil.handleException(Errors.GET_SMS_FIRST);
		}
	}

	// 防重处理
	private boolean updateRecordToPaying(Long paymentRecordId) {
		boolean isRepeatePay = false;
		try {
			paymentProcessService.updateRecordStateBaseOnOriginalStatus(paymentRecordId, PayRecordStatusEnum.PAYING,
					Arrays.asList(new PayRecordStatusEnum[] { PayRecordStatusEnum.SMS_SEND }));
		} catch (CashierBusinessException e) {
			logger.error("更新支付订单为paying状态失败", e);
			isRepeatePay = true;
		}
		return isRepeatePay;
	}
	
	/**
	 * 构造银行卡分期，支付记录短信验证或者确认支付用来比对record的条件对象
	 */
	private RecordCondition buildSmsOrConfirmRecordCondition(PaymentRequest paymentRequest, String recordId,
			OrderAction orderAction, String recordPayTypes[]) {
		RecordCondition condition = new RecordCondition();
		condition.setPayTool(PayTool.YHKFQ_ZF.name());
		condition.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
		condition.setTradeSysNo(paymentRequest.getTradeSysNo());
		condition.setRecordId(recordId);
		condition.setOrderAction(orderAction);
		condition.setRecordPayTypes(recordPayTypes);
		return condition;
	}


	@Override
	public String isSignRelationIdIllegal(String signRetionId, CashierUserInfo externalUser, InstallmentInfoNeeded request) {
		SignRelationInfo currentSignInfo = signCardService.getSignCardInfoBySignRelationId(signRetionId);
		compareExtermalUser(externalUser, currentSignInfo.getExternalUser());
		request.setCurrentSignInfo(currentSignInfo);
		request.setPayType(InstallmentPayTypeEnum.SIGN_RELATION);
		request.setCashierUser(currentSignInfo.getExternalUser());
		return currentSignInfo.getSignCardInfo().getCardInfo().getBank().getBankCode();
	}


	/**
	 * @title 校验签约关系实体中的外部用户是否与商户传给收银台的一致
	 * @param externalUser
	 *            商户传的外部用户的入参，由merchantN0，identityId和identityType三者构成，任一个都不为空
	 * @param validUser
	 */
	private void compareExtermalUser(CashierUserInfo externalUser, CashierUserInfo validUser) {
		if (validUser == null || externalUser == null) {
			return;
		}
		if (validUser.formByIdentityId()) {
			boolean legal = validUser.compare(externalUser);
			if (!legal) {
				throw new CashierBusinessException(Errors.SYSTEM_SIGNRID_NULL);
			}
		} else {
			if (StringUtils.isBlank(validUser.getExternalUserId())) {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
			}
			String userId = cwhService.getExternalUserId(externalUser);
			if (StringUtils.isBlank(userId) || !userId.equals(validUser.getExternalUserId())) {
				throw new CashierBusinessException(Errors.SYSTEM_SIGNRID_NULL);
			}
		}
	}


	@Override
	public String isCardNoIllegal(String cardNo, CashierUserInfo externalUser, InstallmentInfoNeeded request) {
		// 查询cardBin不能交给用户中心做，因为如果这张卡未签约时，用户中心返回空对象
		CardInfo cardBin = cashierBankCardService.getNonNullCardBin(cardNo);
		judgeSignStatusOfCardNo(cardNo, externalUser, request);
		return cardBin.getBank().getBankCode();
	}


	@Override
	public void judgeSignStatusOfCardNo(String cardNo, CashierUserInfo externalUser, InstallmentInfoNeeded request) {
		SignRelationInfo signRelationInfo = signCardService.getSignCardInfoByCardNo(cardNo, externalUser);
		// 传卡号+外部用户的情况，无需校验identityId这些信息
		if (signRelationInfo != null) {
			request.setCashierUser(signRelationInfo.getExternalUser());
		}
		// 分支判断顺序不能换！
		if ((signRelationInfo == null) || !(signRelationInfo.isCardSigned())) {// 返回的签约关系实体为空或签约关系记录中的签约卡无效，则认为是首次
			request.setPayType(InstallmentPayTypeEnum.FIRST);
		} else if (signRelationInfo.isSignRelationIllegal()) {// 签约关系（这种情况表明商户肯定传了userNo和userType）
			request.setPayType(InstallmentPayTypeEnum.SIGN_RELATION);
		} else if (signRelationInfo.isCardSigned()) {// 卡已签约，无签约关系
			request.setPayType(InstallmentPayTypeEnum.SIGN_CARD);
		} else {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		request.setCurrentSignInfo(signRelationInfo);
	}

	@Override
	public PaymentRecord exsitValidRecordToSms(PaymentRequest paymentRequest, String recordId){
		String recordPayTypes[] = { InstallmentPayTypeEnum.SIGN_CARD.name(),
				InstallmentPayTypeEnum.SIGN_RELATION.name() };
		RecordCondition condition = buildSmsOrConfirmRecordCondition(paymentRequest, recordId, OrderAction.SEND_SMS, recordPayTypes);
		PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(recordId, condition);
		return paymentRecord;
	}
	
	@Override
	public PaymentRecord getRecordToOrder(String cardNo, InstallmentInfoNeeded infoNeeded, PaymentRequest paymentRequest){
		RecordCondition condition = infoNeeded.buildRecordCondition(cardNo, OrderAction.ORDER, CashierVersionEnum.valueOf(paymentRequest.getCashierVersion()), infoNeeded.getToken());
		PersonHoldCard person = buildPersonHoldCard(cardNo, infoNeeded);
		PaymentRecord paymentRecord = paymentProcessService.createRecordWhenUnexsit(paymentRequest, condition, person,
				infoNeeded.getPayType().name(), infoNeeded.getCashierUser() != null
						? infoNeeded.getCashierUser().getExternalUserId() : "", infoNeeded.getToken(),null,null);
		
		infoNeeded.setPaymentRecord(paymentRecord);
		boolean legal = paymentRecord.checkStatusEnd(condition.getOrderAction());
		// 已签约卡的支付，只允许下单一次
		if (!legal && !InstallmentPayTypeEnum.FIRST.name().equals(paymentRecord.getPayType())) {
			throw new CashierBusinessException(Errors.REPEAT_ORDER);
		}
		return paymentRecord;
	}
	
	private PersonHoldCard buildPersonHoldCard(String cardNo, InstallmentInfoNeeded infoNeeded) {
		PersonHoldCard person = infoNeeded.buildPersonHoldCard();
		person.getCard().setCardNo(cardNo);
		return person;
	}
	
	@Override
	public PaymentRecord getRecordToConfirm(String recordId, PaymentRequest paymentRequest){
		String recordPayTypes[] = { InstallmentPayTypeEnum.SIGN_CARD.name(),
				InstallmentPayTypeEnum.SIGN_RELATION.name() };
		RecordCondition condition = buildSmsOrConfirmRecordCondition(paymentRequest, recordId, OrderAction.CONFIRM_PAY, recordPayTypes);
		PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(recordId, condition);
		return paymentRecord;
	}

	@Override
	public List<SignRelationInfo> getSignCardList(PaymentRequest paymentRequest) {
		if (StringUtils.isBlank(paymentRequest.getIdentityId())
				|| StringUtils.isBlank(paymentRequest.getIdentityType())) {
			return null;
		}
		CashierUserInfo externalUser = CashierUserInfo.buildOrignalExternalUser(paymentRequest.getIdentityId(),paymentRequest.getIdentityType(),paymentRequest.getMerchantNo());
		return signCardService.getSignCardList(externalUser);
	}

	
	public void calculateInstallmentAmount(PaymentRequest paymentRequest, InstallmentFeeInfoRequestDTO requestDTO, InstallmentFeeInfoResponseDTO response){
		FeeInfo feeInfo = calAllowanceFeeInfo(paymentRequest, requestDTO.getBankCode(), requestDTO.getPeriod());
		calculateInstallmentAmount(feeInfo, paymentRequest.getOrderAmount(), requestDTO, response);
	}
	
	private InstallmentBankInfo getTargetInstallmentBankInfo(String bankCode, String period) {
		Map<String, InstallmentBankInfo> rateInfoMap = CommonUtil.getInstallmentInfo();
		if (MapUtils.isEmpty(rateInfoMap)) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		InstallmentBankInfo info = rateInfoMap.get(bankCode);
		if(info!=null){
			InstallmentRateInfo rateInfo = info.getInstallmentRateInfoByPeriod(period);
			if(rateInfo!=null){
				info.setTargetInstallmentRateInfo(rateInfo);
			}
			return info;
		}
		return null;
	}
	
	private void calculateInstallmentAmount(FeeInfo feeInfo, BigDecimal orderAmount, InstallmentFeeInfoRequestDTO requestDTO, InstallmentFeeInfoResponseDTO response){
		InstallmentBankInfo bankInfo = getTargetInstallmentBankInfo(requestDTO.getBankCode(), requestDTO.getPeriod());
		if(bankInfo==null){
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
		}
		
		BigDecimal feeRate = bankInfo.getTargetInstallmentRateInfo().formatRate(); // 分期利率(对应统一配置配置的值)
		BigDecimal realFeeAmount = orderAmount.multiply(feeRate).setScale(2, BigDecimal.ROUND_HALF_UP); // 用户真正承担的分期利息金额
		response.setFeeAmount(realFeeAmount); // 补贴前的手续费-实际银行收取的分期利息 
		if(feeInfo!=null && feeInfo.getPayeeFee()!=null && feeInfo.getPayeeFee().compareTo(BigDecimal.ZERO) == 1){
			// 计费中心返回的费率大于0,就认为是全补贴(没有部分补贴) _ 用户真正承担的分期利息为0
			realFeeAmount = BigDecimal.ZERO.setScale(2);
		}
		response.setFeeAmountAfterSubsidy(realFeeAmount); // 用户真正承担的分期利息
		
		BigDecimal terminalPayment = orderAmount.divide(new BigDecimal(requestDTO.getPeriod()), 2, BigDecimal.ROUND_HALF_UP); //每期应还款额
		BigDecimal firstPayment = null; // 首期应还款额
		if(bankInfo.getRateWay().equals("BY_ONE_TIME")){
			/** 一次性付清利息的场景 **/
			//首期应还款额 = 订单金额/期数 + 用户真正承担的分期利息
			//每期应还款额 = 订单金额/期数
			firstPayment = terminalPayment.add(realFeeAmount).setScale(2);
		}else{
			/** 分期付利息的场景 **/
			//首期应还款额 = 每期应还款额 = 订单金额/期数 + 用户真正承担的分期利息
			BigDecimal divideFee = realFeeAmount.divide(new BigDecimal(requestDTO.getPeriod()), 2, BigDecimal.ROUND_HALF_UP);
			firstPayment = terminalPayment.add(divideFee).setScale(2);
			terminalPayment = firstPayment;
		}
		response.setFirstPayment(firstPayment);
		response.setTerminalPayment(terminalPayment);
		response.setOrderAmount(orderAmount);
		//交易金额、首期应还、每期应还、手续费（用户真正承担的）
	}
	
}
