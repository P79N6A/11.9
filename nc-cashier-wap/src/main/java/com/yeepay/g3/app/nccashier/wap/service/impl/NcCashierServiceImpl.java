package com.yeepay.g3.app.nccashier.wap.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.RedisTemplate;
import com.yeepay.g3.app.nccashier.wap.vo.ActivityVo;
import com.yeepay.g3.app.nccashier.wap.vo.BankLimitAmountResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.ResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.SmsSendResponseVo;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyConfirmPayRequestVo;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasySmsSendRequestVo;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyConfirmPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.service.*;
import com.yeepay.g3.util.ncmock.MockRemoteServiceFactory;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhen.tan
 * 
 */
@Service("ncCashierService")
public class NcCashierServiceImpl implements NcCashierService {
	private static final Logger logger = LoggerFactory.getLogger(NcCashierServiceImpl.class);

	private static final String CAHSHIER_URL = null;

	private APIMerchantScanPayFacade apiMerchantScanPayFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, APIMerchantScanPayFacade.class);

	private NcCashierCoreFacade ncCashierCoreFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, NcCashierCoreFacade.class);

	private NcCashierUserAccessFacade ncCashierUserAccessFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, NcCashierUserAccessFacade.class);

	private CashierRouteFacade cashierRouteFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, CashierRouteFacade.class);

	private NcCashierBankFacade ncCashierBankFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, NcCashierBankFacade.class);
	private CashierSetsFacade cashierSetsFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, CashierSetsFacade.class);

	private WeChatPayRequestFacade weChatAPIPayRequestFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, WeChatPayRequestFacade.class);
	private CashierCardOwnerFacade cashierCardOwnerFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, CashierCardOwnerFacade.class);

	private RiskControlFacade riskControlFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, RiskControlFacade.class);

	private BankInstallmentFacade bankInstallmentFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, BankInstallmentFacade.class);

	private ClfEasyFacade clfEasyFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, ClfEasyFacade.class);

	/**
	 * pc网银支付相关业务 facade服务
	 */
	private EBankPayFacade eBankPayFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL, RemotingProtocol.HESSIAN,
			EBankPayFacade.class);

	/**
	 * 支付结果查询相关业务 facade服务
	 */
	private ProcessorPayResultQueryFacade processorPayResultQueryFacade = MockRemoteServiceFactory
			.getService(CAHSHIER_URL, RemotingProtocol.HESSIAN, ProcessorPayResultQueryFacade.class);

	private NcCashierUserCenterFacade ncCashierUserCenterFacade = MockRemoteServiceFactory
			.getService(RemotingProtocol.HESSIAN, NcCashierUserCenterFacade.class);

	/**
	 * 账户支付
	 */
	private AccountPayFacade accountPayFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, AccountPayFacade.class);

	private MerchantCashierCustomizedFacade merchantCashierCustomizedFacade = MockRemoteServiceFactory
			.getService(RemotingProtocol.HESSIAN, MerchantCashierCustomizedFacade.class);
	/**
	 * 非银行卡支付
	 */
	private NotBankCardPayFacade notBankCardPayFacade = MockRemoteServiceFactory.getService(RemotingProtocol.HESSIAN,
			NotBankCardPayFacade.class);

	/**
	 * 无交易订单处理器
	 */
	private NoTradingProcessorFacade noTradingProcessorFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, NoTradingProcessorFacade.class);
	/**
	 * 预授权支付
	 */
	private PreauthFacade ncCashierPreauthFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, PreauthFacade.class);

	/**
	 * 担保分期
	 */
	private GuaranteeInstallmentFacade guaranteeInstallmentFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, GuaranteeInstallmentFacade.class);

	/**
	 * 营销信息
	 */
	private MarketActivityFacade marketActivityFacade = MockRemoteServiceFactory.getService(CAHSHIER_URL,
			RemotingProtocol.HESSIAN, MarketActivityFacade.class);
	
	@Override
	public CashierPaymentResponseDTO createPayment(CashierPaymentRequestDTO requestDTO) {
		CashierPaymentResponseDTO response = ncCashierCoreFacade.createPayment(requestDTO);
		if (response == null || !requestDTO.getTokenId().equals(response.getTokenId())) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public void sendSMS(CashierSmsSendRequestDTO requestDTO) {

		CashierSmsSendResponseDTO response = ncCashierCoreFacade.sendSms(requestDTO);
		if (response == null || !requestDTO.getTokenId().equals(response.getTokenId())) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}

	}

	@Override
	public TradeNoticeDTO queryPayResult(CashierQueryRequestDTO requestDTO) {

		CashierQueryResponseDTO response = ncCashierCoreFacade.queryPayResult(requestDTO);
		if (response == null || requestDTO.getRecordId() != response.getRecordId()) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response.getTradeNoticeDTO();
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}

	}

	@Override
	public NeedValidatesDTO getCardValidates(CardValidateRequestDTO requestDTO) {

		CardValidateResponseDTO response = ncCashierBankFacade.getCardValidates(requestDTO);
		if (response == null || requestDTO.getRequestId() != response.getRequestId()) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response.getNeedValidatesDTO();
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}

	}

	@Override
	public List<BankSupportDTO> supportBankList(long requestId,String cusType) {

		SupportBanksResponseDTO response = ncCashierBankFacade.supportBankList(requestId,cusType);
		if (response == null || requestId != response.getRequestId()) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response.getSupportBankList();
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}

	}

	@Override
	public RequestInfoDTO requestBaseInfo(String tokenId) {
		RequestInfoDTO response = ncCashierUserAccessFacade.requestBaseInfo(tokenId);

		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}

	}

	@Override
	public void saveUserAccess(UserAccessDTO userAccess) {

		UserAccessResponseDTO response = ncCashierUserAccessFacade.saveUserAccount(userAccess);
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public void clearRecordId(String tokenId) {
		BasicResponseDTO response = ncCashierUserAccessFacade.clearRecordId(tokenId);
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}

	}

	@Override
	public BussinessTypeResponseDTO routerPayWay(long requestId) {
		BussinessTypeResponseDTO bussinessTypeResponseDTO = null;
		bussinessTypeResponseDTO = cashierRouteFacade.routerPayWay(requestId,Constant.BNAK_RULE_CUSTYPE_SALE);
		if (null == bussinessTypeResponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (requestId != bussinessTypeResponseDTO.getRequestId()) {
			throw new CashierBusinessException(Errors.REQUEST_NOT_SAME.getCode(), Errors.REQUEST_NOT_SAME.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == bussinessTypeResponseDTO.getProcessStatusEnum()) {
			return bussinessTypeResponseDTO;
		} else {
			throw new CashierBusinessException(bussinessTypeResponseDTO.getReturnCode(),
					bussinessTypeResponseDTO.getReturnMsg());
		}
	}

	public void checkReffer(CheckRefferRequestDTO requestDTO) {
		CheckRefferReponseDTO response = null;
		try {
			response = riskControlFacade.checkReffer(requestDTO);
		} catch (Exception e) {
			logger.error("交易订单号=" + requestDTO.getBizOrderId() + "风控reffer校验异常", e);
		}

		if (response != null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			if ("9003".equals(response.getResultCode()) || "9004".equals(response.getResultCode())) {
				throw new CashierBusinessException(Errors.ORDER_RISK_ERROR.getCode(), Errors.ORDER_RISK_ERROR.getMsg());
			}
		}
	}

	@Override
	public MerchantAuthorityResponseDTO merchantAuthorityRequest(
			MerchantAuthorityRequestDTO merchantAuthorityRequestDTO) {
		MerchantAuthorityResponseDTO responseDTO;
		try {
			responseDTO = ncCashierUserCenterFacade.merchantAuthority(merchantAuthorityRequestDTO);
		} catch (Throwable e) {
			logger.error("调取nccashier商户授权接口异常,requestId:{}", merchantAuthorityRequestDTO.getRequestId());
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		handleResponse(responseDTO);
		return responseDTO;
	}

	@Override
	public ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSms(
			ShareCardAuthoritySendSmsRequestDTO shareCardAuthoritySendSmsRequestDTO) {
		ShareCardAuthoritySendSmsResponseDTO responseDTO;
		try {
			responseDTO = ncCashierUserCenterFacade.shareCardAuthoritySendSms(shareCardAuthoritySendSmsRequestDTO);
		} catch (Throwable e) {
			logger.error("调取nccashier共享卡发短验接口异常,requestId:{}", shareCardAuthoritySendSmsRequestDTO.getRequestId());
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		handleResponse(responseDTO);
		return responseDTO;
	}

	@Override
	public ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirm(
			ShareCardAuthoritySmsConfirmRequestDTO shareCardAuthoritySmsConfirmRequestDTO) {
		ShareCardAuthoritySmsConfirmResponseDTO responseDTO;
		try {
			responseDTO = ncCashierUserCenterFacade
					.shareCardAuthoritySmsConfirm(shareCardAuthoritySmsConfirmRequestDTO);
		} catch (Throwable e) {
			logger.error("调取nccashier共享卡确认短验接口异常,requestId:{}", shareCardAuthoritySmsConfirmRequestDTO.getRequestId());
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		handleResponse(responseDTO);
		return responseDTO;
	}
	@Override
	public BankCardReponseDTO getBankCardInfo4Preauth(long requestId,String cusType) {
		BankCardReponseDTO bankCardReponseDTO = ncCashierBankFacade.getBankCardInfo4Preauth(requestId,cusType);
		if (null == bankCardReponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (requestId != bankCardReponseDTO.getRequestId()) {
			throw new CashierBusinessException(Errors.REQUEST_NOT_SAME.getCode(), Errors.REQUEST_NOT_SAME.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == bankCardReponseDTO.getProcessStatusEnum()) {
			return bankCardReponseDTO;
		} else {
			throw new CashierBusinessException(bankCardReponseDTO.getReturnCode(), bankCardReponseDTO.getReturnMsg());
		}
	}

    @Override
    public GuaranteeInstallmentPrePayResponseDTO guaranteeInstallmentPrePay(GuaranteeInstallmentPrePayRequestDTO requestDTO) {
		GuaranteeInstallmentPrePayResponseDTO prePayResponseDTO = guaranteeInstallmentFacade.prePay(requestDTO);
		if (null == prePayResponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == prePayResponseDTO.getProcessStatusEnum()) {
			return prePayResponseDTO;
		} else {
			throw new CashierBusinessException(prePayResponseDTO.getReturnCode(), prePayResponseDTO.getReturnMsg());
		}
    }

	@Override
	public GuaranteeInstallmentPaymentResponseDTO guaranteeInstallmentRequestPayment(GuaranteeInstallmentPaymentRequestDTO requestDTO) {
		GuaranteeInstallmentPaymentResponseDTO paymentResponseDTO = guaranteeInstallmentFacade.requestPayment(requestDTO);
		if (null == paymentResponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == paymentResponseDTO.getProcessStatusEnum()) {
			return paymentResponseDTO;
		} else {
			throw new CashierBusinessException(paymentResponseDTO.getReturnCode(), paymentResponseDTO.getReturnMsg());
		}
	}

	@Override
	public InstallmentRouteResponseDTO guaranteeInstallmentgetSupportBankAndPeriods(long requestId) {
		InstallmentRouteResponseDTO installmentRouteResponseDTO = guaranteeInstallmentFacade.getSupportBankAndPeriods(requestId);
		if (null == installmentRouteResponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == installmentRouteResponseDTO.getProcessStatusEnum()) {
			return installmentRouteResponseDTO;
		} else {
			throw new CashierBusinessException(installmentRouteResponseDTO.getReturnCode(), installmentRouteResponseDTO.getReturnMsg());
		}
	}

	@Override
	public BankCardReponseDTO getBankCardInfo(long requestId,String cusType) {
		BankCardReponseDTO bankCardReponseDTO = ncCashierBankFacade.getBankCardInfo(requestId,cusType);
		if (null == bankCardReponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (requestId != bankCardReponseDTO.getRequestId()) {
			throw new CashierBusinessException(Errors.REQUEST_NOT_SAME.getCode(), Errors.REQUEST_NOT_SAME.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == bankCardReponseDTO.getProcessStatusEnum()) {
			return bankCardReponseDTO;
		} else {
			throw new CashierBusinessException(bankCardReponseDTO.getReturnCode(), bankCardReponseDTO.getReturnMsg());
		}
	}

	@Override
	public BasicResponseDTO validatePassBindId(long requestId) {
		BasicResponseDTO basicResponseDTO = ncCashierBankFacade.validatePassBindId(requestId);
		if (null == basicResponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == basicResponseDTO.getProcessStatusEnum()) {
			return basicResponseDTO;
		} else {
			throw new CashierBusinessException(basicResponseDTO.getReturnCode(), basicResponseDTO.getReturnMsg());
		}
	}

	@Override
	public Boolean isPassBindId(long requestId) {
		PassBindIdDTO isPassBindId = ncCashierBankFacade.isPassBindId(requestId);
		if (null == isPassBindId) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == isPassBindId.getProcessStatusEnum()) {
			return isPassBindId.isPassBindId();
		} else {
			throw new CashierBusinessException(isPassBindId.getReturnCode(), isPassBindId.getReturnMsg());
		}
	}

	@Override
	public CashierPayResponseDTO firstPay(CashierFirstPayRequestDTO firstPayRequest) {
		CashierPayResponseDTO cashierPayResponseDTO = null;
		cashierPayResponseDTO = ncCashierCoreFacade.firstPay(firstPayRequest);
		if (null == cashierPayResponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == cashierPayResponseDTO.getProcessStatusEnum()) {
			return cashierPayResponseDTO;
		} else {
			throw new CashierBusinessException(cashierPayResponseDTO.getReturnCode(),
					cashierPayResponseDTO.getReturnMsg());
		}
	}

	@Override
	public CashierPayResponseDTO bindPay(CashierBindPayRequestDTO bindPayRequest) {
		CashierPayResponseDTO cashierPayResponseDTO = null;
		cashierPayResponseDTO = ncCashierCoreFacade.bindPay(bindPayRequest);
		if (null == cashierPayResponseDTO) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (ProcessStatusEnum.SUCCESS == cashierPayResponseDTO.getProcessStatusEnum()) {
			return cashierPayResponseDTO;
		} else {
			throw new CashierBusinessException(cashierPayResponseDTO.getReturnCode(),
					cashierPayResponseDTO.getReturnMsg());
		}
	}

	@Override
	public String getURL(String cashierVer, String merchantNo, long id) {
		String url = cashierRouteFacade.getURL(cashierVer, merchantNo, id);
		return url;
	}

	@Override
	public String getSendSMSNo() {
		String smsSendNo = null;
		try {
			smsSendNo = cashierSetsFacade.getSendSMSNo();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return smsSendNo;
	}

	@Override
	public PayExtendInfo getPayExtendInfo(long requestId, String tokenId) {
		return cashierRouteFacade.getPayExtendInfo(requestId, tokenId);
	}

	@Override
	public List<MerchantProductDTO> getNewMerchantInNetConfig(long requestId, String tokenId) {
		return cashierRouteFacade.getNewMerchantInNetConfig(requestId,tokenId);
	}

	@Override
	public String WeChatPay(WeChatPayRequestDTO payRequest) {
		WeChatPayResponseDTO response = weChatAPIPayRequestFacade.pay(payRequest);
		if (response == null || !payRequest.getTokenId().equals(response.getTokenId())) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			RedisTemplate.setCacheObjectSumValue(Constant.CONFIRM_PAY_FLAG + payRequest.getTokenId(), "SUCCESS",
					120 * 1000);
			return response.getResult();
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public boolean queryQualification4Carnival(String merchantNo) {
		boolean canJoin = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date start = null;// 开始时间
			Date end = null;// 结束时间
			Map<String, String> businessInfoMap = CommonUtil.getSysConfigFrom3G("OL_NCCASHIER_CARNIVAL_SWITCH_2018_0402");
			if (null != businessInfoMap) {
				// 活动开始时间与结束时间，若未配置则默认开始时间为 4月2日上午10：00 结束时间为 4月15日16：00
				// 00:00:00
				String startTime = businessInfoMap.get("START_TIME");
				String endTime = businessInfoMap.get("END_TIME");
				// 开始时间
				if (StringUtils.isNotBlank(startTime)) {
					try {
						start = sdf.parse(startTime);
					} catch (ParseException e) {
						logger.warn("嘉年华开始时间设置错误", e);
						start = sdf.parse("2018-04-02 10:00:00");
					}
				} else {
					start = sdf.parse("2018-04-02 10:00:00");
				}
				// 结束时间
				if (StringUtils.isNotBlank(endTime)) {
					try {
						end = sdf.parse(endTime);
					} catch (ParseException e) {
						logger.warn("嘉年华结束时间设置错误", e);
						end = sdf.parse("2018-04-15 16:00:00");
					}
				} else {
					end = sdf.parse("2018-04-15 16:00:00");
				}
				Date now = new Date();
				// 判断是否处于嘉年华活动期间
				if (now.after(start) && now.before(end)) {
					canJoin = true;
					logger.info("处于活动期间");
				}
			}
		} catch (Throwable e) {
			logger.error("获取参加嘉年华资格时报错", e);
		}

		return canJoin;
	}

	@Override
	public ActivityVo queryQualification4Activities() {
		ActivityVo activityVo = new ActivityVo();
		activityVo.setShowActivity(CommonUtil.activitiesTimeJudge());
//		logger.info("[是否能够参加活动]:"+activityVo.isShowActivity());
		return activityVo;

	}

	@Override
	public CardBinInfoDTO getCardBinInfo(String cardno) {
		return ncCashierBankFacade.getCardBinInfo(cardno);
	}

	@Override
	public EBankSupportBanksResponseDTO ebankSupportBankList(EBankSupportBanksRequestDTO request) {
		EBankSupportBanksResponseDTO response = eBankPayFacade.querySupportBankList(request);
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public EBankSupportBanksResponseDTO getBacLoadSuportBanks(EBankSupportBanksRequestDTO request) {
		EBankSupportBanksResponseDTO response = eBankPayFacade.getBacLoadSuportBanks(request);
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public NewOrderRequestResponseDTO newOrderRequest(String orderNo, int selectOrderSysNo) {
		NewOrderRequestResponseDTO response = cashierRouteFacade.newOrderRequest(orderNo, selectOrderSysNo);
		if (response == null || !orderNo.equals(response.getOrderNo())) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public EBankCreatePaymentResponseDTO ebankCreatePayment(EBankCreatePaymentRequestDTO request) {
		EBankCreatePaymentResponseDTO response = eBankPayFacade.createPayment(request);
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public PayResultQuerySignListenResponseDTO listenCanPayResultQuery(PayResultQuerySignListenRequestDTO request) {
		PayResultQuerySignListenResponseDTO response = processorPayResultQueryFacade.listenCanPayResultQuery(request);
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public Map<String, List<BankLimitAmountResponseVO>> querySupportBankListFromMerchantConfig(String requestId,
			String merchantNo, String cashierVersion) {
		/**
		 * 通过银行进行分组，一个map中放两条记录
		 */
		BankLimitAmountListResponseDTO bankLimitAmountListResponseDTO = queryLimitAmount(requestId, merchantNo);
		if (bankLimitAmountListResponseDTO.getLimitAmountResponseDTOList() == null) {
			return null;
		}
		Map<String, List<BankLimitAmountResponseVO>> map = new HashMap<String, List<BankLimitAmountResponseVO>>(30);
		for (BankLimitAmountResponseDTO bankLimitAmountResponseDTO : bankLimitAmountListResponseDTO
				.getLimitAmountResponseDTOList()) {
			BankLimitAmountResponseVO bankLimitAmountResponseVO = buildBankLimitAmountResponseVO(
					bankLimitAmountResponseDTO, cashierVersion);
			if (map.containsKey(bankLimitAmountResponseVO.getBankCode())) {
				map.get(bankLimitAmountResponseVO.getBankCode()).add(bankLimitAmountResponseVO);
			} else {
				List<BankLimitAmountResponseVO> list = new ArrayList<BankLimitAmountResponseVO>();
				list.add(bankLimitAmountResponseVO);
				map.put(bankLimitAmountResponseVO.getBankCode(), list);
			}
		}
		return map;
	}

	/**
	 * 构建VO对象，并过滤限额数据中过大的金额
	 * 
	 * @param bankLimitAmountResponseDTO
	 * @param cashierVersion
	 */
	private BankLimitAmountResponseVO buildBankLimitAmountResponseVO(
			BankLimitAmountResponseDTO bankLimitAmountResponseDTO, String cashierVersion) {
		if (bankLimitAmountResponseDTO == null) {
			return null;
		}
		BankLimitAmountResponseVO bankLimitAmountResponseVO = new BankLimitAmountResponseVO();
		bankLimitAmountResponseVO.setBankCode(bankLimitAmountResponseDTO.getBankCode());
		bankLimitAmountResponseVO.setBankName(bankLimitAmountResponseDTO.getBankName());
		bankLimitAmountResponseVO.setDebit(bankLimitAmountResponseDTO.getDebit());
		bankLimitAmountResponseVO
				.setLimitMinOfBill(filterBigAmount(bankLimitAmountResponseDTO.getLimitMinOfBill(), cashierVersion));
		bankLimitAmountResponseVO
				.setLimitOfBill(filterBigAmount(bankLimitAmountResponseDTO.getLimitOfBill(), cashierVersion));
		bankLimitAmountResponseVO
				.setLimitOfDay(filterBigAmount(bankLimitAmountResponseDTO.getLimitOfDay(), cashierVersion));
		bankLimitAmountResponseVO
				.setLimitOfMonth(filterBigAmount(bankLimitAmountResponseDTO.getLimitOfMonth(), cashierVersion));
		return bankLimitAmountResponseVO;
	}

	/**
	 * 过滤限额数据中过大的金额：21000000（分） -> 21万（元） ； 21700000（分） -> 21万（元） ； 999900（分） ->
	 * 9999（元）
	 * 
	 * @param limitLong
	 * @return
	 */
	private String filterBigAmount(long limitLong, String cashierVersion) {
		if (!CashierVersionEnum.WAP.name().equals(cashierVersion)) {
			// 非WAP收银台限额接口的，直接返回
			return Long.toString(limitLong);
		}
		BigDecimal orignalLimit = new BigDecimal(limitLong);
		if (orignalLimit.longValue() <= 0) {
			// 原始的数据，非正数的，直接返回
			return orignalLimit.toString();
		}
		// WAP收银台
		BigDecimal limit = orignalLimit.divide(new BigDecimal(100), RoundingMode.DOWN);// 限额的原始数据单位是分，除以100得到元
		if (limit.longValue() < 10000) {
			// 小于10000元的，直接返回
			return limit.toString();
		}
		// 大于等于10000元的，转换格式
		String limitString = limit.toString();
		String amountOfTenThousands = limitString.substring(0, limitString.length() - 4);
		return amountOfTenThousands + "万";
	}

	@Override
	public Boolean queryAllBankOverLimit(double amount, String requestId, String merchantNo) {
        Boolean flg = false;
		try {
            Boolean flag = RedisTemplate.getTargetFromRedis(Constant.OL_NCCASHIER_BANK_LIMIT + merchantNo + requestId + amount,
                    Boolean.class);

            if (flag != null) {
                return flag;
            }

			BankLimitAmountListResponseDTO bankLimitAmountListResponseDTO = queryLimitAmount(requestId, merchantNo);
			// 如果所有的全部超出限额,false
			for (BankLimitAmountResponseDTO bankLimitAmountResponseDTO : bankLimitAmountListResponseDTO
					.getLimitAmountResponseDTOList()) {
				if ((bankLimitAmountResponseDTO.getLimitOfBill() != -1
						&& amount * 100 > bankLimitAmountResponseDTO.getLimitOfBill())
						|| (bankLimitAmountResponseDTO.getLimitMinOfBill() != -1
								&& amount * 100 < bankLimitAmountResponseDTO.getLimitMinOfBill())) {
					continue;
				} else {
					flg = true;
				}
			}
			if (CollectionUtils.isEmpty(bankLimitAmountListResponseDTO.getLimitAmountResponseDTOList())) {
				logger.error("[monitor],event:没有查询到支持的银行卡列表为空,merchantNo:{}", merchantNo);
			}
		} catch (Throwable r) {
			logger.error("[monitor],event:queryAllBankOverLimit_error,merchantNo:{}", merchantNo, r);
            flg = true;
		}
        RedisTemplate.setCacheObjectSumValue(Constant.OL_NCCASHIER_BANK_LIMIT + merchantNo + requestId + amount,
                flg, Constant.NCCASHIER_BANK_LIMIT_TIME);
		return flg;
	}

	@Override
	public List<JSONObject> bindCradLimitSort(List<BankCardDTO> list, double amount, String requestId,
			String merchantNo) {
		List<JSONObject> over = new ArrayList<JSONObject>();
		List<JSONObject> noover = new ArrayList<JSONObject>();
		try {
			BankLimitAmountListResponseDTO bankLimitAmountListResponseDTO = queryLimitAmount(requestId, merchantNo);
			int size = list.size();
			for (int i = 0; i < size; i++) {
				for (BankLimitAmountResponseDTO bankLimitAmountResponseDTO : bankLimitAmountListResponseDTO
						.getLimitAmountResponseDTOList()) {
					// 如果卡类型和银行编码一样，判断限额
					if (bankLimitAmountResponseDTO.getBankCode().equals(list.get(i).getBankCode())
							&& bankLimitAmountResponseDTO.getDebit().equals(list.get(i).getCardtype().name())) {
						// 超出限额
						if ((bankLimitAmountResponseDTO.getLimitOfBill() != -1)
								&& (amount * 100 > bankLimitAmountResponseDTO.getLimitOfBill()
										|| amount * 100 < bankLimitAmountResponseDTO.getLimitMinOfBill())) {
							JSONObject json = (JSONObject) JSON.toJSON(list.get(i));
							json.put("limit", true);
							over.add(json);
						} else {
							JSONObject json = (JSONObject) JSON.toJSON(list.get(i));
							json.put("limit", false);
							noover.add(json);
						}
						break;
					}
				}
			}
			noover.addAll(over);
			if (CollectionUtils.isEmpty(noover)) {
				throw new Exception("绑卡列表中的卡在商户配置中支持的银行卡列表查不到");
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:bindCardLimitSort_error,merchantNo:{}", merchantNo, e);
			for (BankCardDTO bankCardDTO : list) {
				JSONObject json = (JSONObject) JSON.toJSON(bankCardDTO);
				json.put("limit", false);
				noover.add(json);
			}
		}
		return noover;
	}

	@Override
	public JsapiRouteResponseDTO getAppid(String requestId, String payType) {
		JsapiRouteRequestDTO jsapiRouteRequestDTO = new JsapiRouteRequestDTO();
		jsapiRouteRequestDTO.setRequestId(requestId);
		jsapiRouteRequestDTO.setPayType(payType);
		JsapiRouteResponseDTO jsapiRouteResponseDTO = weChatAPIPayRequestFacade.routeWaChat(jsapiRouteRequestDTO);
		if (jsapiRouteResponseDTO == null) {
			logger.error("获取预路由appid失败，返回结果为空");
			return new JsapiRouteResponseDTO();
		}
		return jsapiRouteResponseDTO;
	}

	/**
	 * 如果透传了卡类型，会返回透传卡类型所对应卡类型所支持的卡列表，如果没有透传，返回所有
	 * 
	 * @param requestId
	 * @param merchantNo
	 * @return
	 */
	private BankLimitAmountListResponseDTO queryLimitAmount(String requestId, String merchantNo) {
		BankLimiAmountRequestDTO bankLimiAmountRequestDTO = new BankLimiAmountRequestDTO();
		bankLimiAmountRequestDTO.setRequestId(requestId);
		bankLimiAmountRequestDTO.setMerchantNo(merchantNo);
		BankLimitAmountListResponseDTO bankLimitAmountListResponseDTO = ncCashierCoreFacade
				.queryBankLimitAmountList(bankLimiAmountRequestDTO);
		if(bankLimitAmountListResponseDTO == null){
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}else if (bankLimitAmountListResponseDTO.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return bankLimitAmountListResponseDTO;
		} else {
			throw new CashierBusinessException(bankLimitAmountListResponseDTO.getReturnCode(), bankLimitAmountListResponseDTO.getReturnMsg());
		}
	}

	@Override
	public void setCardOwner(String bindId, String name, String idCardNo, long requestId) {
		cashierCardOwnerFacade.setCardOwner(bindId, name, idCardNo, requestId);
	}

	@Override
	public void unbindCard(long paymentRequestId, String paymentOrderNo) {
		cashierCardOwnerFacade.unbindCard(paymentRequestId, paymentOrderNo);
	}

	@Override
	public CardOwnerConfirmResDTO getOwnersInfo(long paymentRequestId, long paymentRecordId) {
		return cashierCardOwnerFacade.getCardOwners(paymentRequestId, paymentRecordId);
	}

	@Override
	public void unbindCard(long paymentRequestId, long paymentRecordId) {
		cashierCardOwnerFacade.unbindCard(paymentRequestId, paymentRecordId);
	}

	private void handleResponse(BasicResponseDTO response) {
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.FAILED) {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public boolean yopVerify(String appKey, String plaintext, String signature) {
		return cashierRouteFacade.yopVerify(appKey, plaintext, signature);
	}

	@Override
	public String sign(String plaintext) {
		return cashierRouteFacade.yopSign(plaintext);
	}

	@Override
	public void apiMerchantScanPay(APIMerchantScanPayDTO request) {
		BasicResponseDTO response = null;
		try {
			response = apiMerchantScanPayFacade.pay(request);
		} catch (Throwable t) {
			logger.error("调取nccashier商家扫码支付接口异常,request:{}", request);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		handleResponse(response);
	}

	@Override
	public OrderProcessResponseDTO orderProcessorRequest(OrderProcessorRequestDTO orderProcessorRequestDTO) {
		OrderProcessResponseDTO response = cashierRouteFacade.orderProcessorRequest(orderProcessorRequestDTO);
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public MerchantCashierCustomizedLayoutSelectDTO queryMerchantCashierCustomizedLayoutSelectInfo(String merchantNo) {
		MerchantCashierCustomizedLayoutSelectDTO response = null;
		try {
			// 先从Redis获取
			response = RedisTemplate.getTargetFromRedis(Constant.CASHIER_CUSTOMIZED_LAYOUT_SELECT_INFO + merchantNo,
					MerchantCashierCustomizedLayoutSelectDTO.class);
			if (null != response) {
				return response;
			}
			response = merchantCashierCustomizedFacade.queryMerchantCashierCustomizedLayoutSelectInfo(merchantNo);
			if (null != response) {
				RedisTemplate.setCacheObjectSumValue(Constant.CASHIER_CUSTOMIZED_LAYOUT_SELECT_INFO + merchantNo,
						response, Constant.CASHIER_CUSTOMIZED_LAYOUT_SELECT_INFO_TIME);
			}
		} catch (Throwable t) {
			logger.warn("获取商户选择的定制化收银台异常,merchantNo:" + merchantNo, t);
		}
		return response;
	}

	@Override
	public MerchantCashierCustomizedFileDTO queryMerchantCashierCustomizedFile(String fileId, String fileType) {
		MerchantCashierCustomizedFileDTO response = null;
		try {
			response = merchantCashierCustomizedFacade.queryMerchantCashierCustomizedFile(fileId, fileType);
		} catch (Throwable t) {
			logger.warn("获取收银台定制模版文件异常,fileId" + fileId + "fileType" + fileType, t);
		}
		return response;
	}

	@Override
	public AccountPayValidateResponseDTO accountPayValidate(AccountPayValidateRequestDTO validateDTO) {
		AccountPayValidateResponseDTO response = accountPayFacade.validateMerchantAccount(validateDTO);
		if (response == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		return response;
	}

	@Override
	public void accountPay(CashierAccountPayRequestDTO payDTO) {
		CashierAccountPayResponseDTO response = null;
		try {
			response = accountPayFacade.pay(payDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier会员支付接口异常,request:{}", payDTO);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		handleResponse(response);
	}

	@Override
	public String getMerchantPageCallBack(RequestInfoDTO requestInfoDTO) {
		String merchantPageCallBack = processorPayResultQueryFacade.getMerchantPageCallBack(requestInfoDTO);
		return merchantPageCallBack;
	}

	@Override
	public String getNotBankCardPayHmac(String merchantNo, String source) {
		String hmac = "";
		try {
			hmac = notBankCardPayFacade.getNotBankCardPayHmac(merchantNo, source);
		} catch (Throwable t) {
			logger.error("调用nccashier非银行支付接口异常,merchant:{},source:{}", merchantNo, source);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		return hmac;
	}

	@Override
	public String getYeepayWechatQRCode(String requestId) {
		String url = "";
		try {
			url = ncCashierCoreFacade.getYeepayWechatQRCode(requestId);
			return url;
		} catch (Throwable t) {
			logger.error("调用nccashier易宝微信公众号二维码地址接口异常,返回空,requestId:" + requestId + ",错误：", t);
			return url;
		}
	}

	@Override
	public String getYeepayWechatQRCode(String merchantNo, String merchantOrderId) {
		String url = "";
		try {
			url = ncCashierCoreFacade.getYeepayWechatQRCode(merchantNo, merchantOrderId);
			return url;
		} catch (Throwable t) {
			logger.error("调用nccashier易宝微信公众号二维码地址接口异常,返回空,merchantNo:" + merchantNo + ",merchantOrderId:"
					+ merchantOrderId + ",错误：", t);
			return url;
		}
	}

	@Override
	public void unbindCardActive(String paymentRequestId, String bindId) {
		cashierCardOwnerFacade.unbindCardActive(paymentRequestId, bindId);
	}

	@Override
	public NOPCardBinResponseDTO getNopCardBinInfo(NOPCardBinRequestDTO cardBinRequestDTO) {
		NOPCardBinResponseDTO response = null;
		try {
			response = noTradingProcessorFacade.getNopCardBinInfo(cardBinRequestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-NOP查询卡bin信息接口异常", t);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public NOPAuthBindResponseDTO authBindCardRequest(NOPAuthBindRequestDTO authBindRequestDTO) {
		NOPAuthBindResponseDTO response = null;
		try {
			response = noTradingProcessorFacade.authBindCardRequest(authBindRequestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-NOP鉴权绑卡请求异常", t);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public NOPAuthBindConfirmResponseDTO authBindCardConfirm(NOPAuthBindConfirmRequestDTO authBindConfirmRequest) {
		NOPAuthBindConfirmResponseDTO response = null;
		try {
			response = noTradingProcessorFacade.authBindCardConfirm(authBindConfirmRequest);
		} catch (Throwable t) {
			logger.error("调用nccashier-NOP鉴权绑卡确认异常", t);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public NOPAuthBindSMSResponseDTO authBindCardSMS(NOPAuthBindSMSRequestDTO authBindSMSRequest) {
		NOPAuthBindSMSResponseDTO response = null;
		try {
			response = noTradingProcessorFacade.authBindCardReSendSMS(authBindSMSRequest);
		} catch (Throwable t) {
			logger.error("调用nccashier-NOP重发短信异常", t);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public NOPQueryOrderResponseDTO queryNopOrderStatus(NOPQueryOrderRequestDTO queryOrderRequest) {
		NOPQueryOrderResponseDTO response = null;
		try {
			response = noTradingProcessorFacade.queryNopOrderStatus(queryOrderRequest);
		} catch (Throwable t) {
			logger.error("调用nccashier-NOP查询NOP订单信息异常", t);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public NOPQueryBindCardOpenStatusResponseDTO getNopBindCardOpenStatus(String merchantNo) {
		NOPQueryBindCardOpenStatusResponseDTO response = null;
		try {
			response = noTradingProcessorFacade.getNopBindCardOpenStatus(merchantNo);
		} catch (Throwable t) {
			logger.error("调用nccashier-NOP获取绑卡产品开通状态异常", t);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public InstallmentBanksResponseDTO getInstallmentBanks(long requestId) {
		InstallmentBanksResponseDTO bankList = null;
		try {
			bankList = ncCashierBankFacade.getInstallmentBankList(requestId);
		} catch (Throwable e) {
			logger.error("调用nccashier-获取银行卡分期支持银行列表异常", e);
		}
		handleResponse(bankList);
		return bankList;
	}

	@Override
	public InstallmentRouteResponseDTO installmentRoutePayWay(long requestId) {
		InstallmentRouteResponseDTO response = null;
		try {
			response = bankInstallmentFacade.routePayWay(requestId);
		} catch (Throwable t) {
			logger.error("调用nccashier-银行卡分期路由异常", t);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public InstallmentFeeInfoResponseDTO getInstallmentFeeInfo(InstallmentFeeInfoRequestDTO requestDTO) {
		InstallmentFeeInfoResponseDTO response = null;
		try {
			response = bankInstallmentFacade.getInstallmentFeeInfo(requestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-银行卡分期获取费率信息异常", t);
		}

		handleResponse(response);
		return response;
	}

	public InstallmentFeeInfoResponseDTO mockFee() {
		InstallmentFeeInfoResponseDTO response = new InstallmentFeeInfoResponseDTO();
		response.setFeeAmount(new BigDecimal(10.50000));
		response.setFeeAmountAfterSubsidy(new BigDecimal(9.50000));
		response.setFirstPayment(new BigDecimal(109.50000));
		response.setOrderAmount(new BigDecimal(300));
		response.setTerminalPayment(new BigDecimal(109.50000));
		return response;
	}

	@Override
	public CardNoOrderResponseDTO orderByCardNo(CardNoOrderRequestDTO requestDTO) {
		CardNoOrderResponseDTO response = null;
		try {
			response = bankInstallmentFacade.orderByCardNo(requestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-银行卡分期卡号下单异常", t);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public BasicResponseDTO orderBySignRelationId(SignRelationIdOrderRequestDTO requestDTO) {
		BasicResponseDTO response = null;
		try {
			response = bankInstallmentFacade.orderBySignRelationId(requestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-银行卡分期签约下单异常", t);
		}
		handleResponse(response);
		return response;
	}

	@Override
	public BasicResponseDTO bankInstallmentSmsSend(Long requestId, Long recordId) {
		InstallmentSmsRequestDTO requestDTO = buildInstallmentSmsRequestDTO(requestId, recordId);
		BasicResponseDTO response = null;
		try {
			response = bankInstallmentFacade.sendSms(requestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-银行卡分期发短验异常", t);
		}
		handleResponse(response);
		return response;
	}

	private InstallmentSmsRequestDTO buildInstallmentSmsRequestDTO(long requestId, long recordId) {
		InstallmentSmsRequestDTO requestDTO = new InstallmentSmsRequestDTO();
		requestDTO.setRecordId(recordId);
		requestDTO.setRequestId(requestId);
		return requestDTO;
	}

	@Override
	public BasicResponseDTO bankInstallmentConfirmPay(Long requestId, Long recordId, String verifyCode) {
		InstallmentConfirmRequestDTO requestDTO = buildInstallmentConfirmRequestDTO(requestId, recordId, verifyCode);
		BasicResponseDTO response = null;
		try {
			response = bankInstallmentFacade.confirmPay(requestDTO);
		} catch (Throwable t) {
			logger.error("调用nccashier-银行卡分期卡号下单异常", t);
		}
		handleResponse(response);
		return response;
	}


	private InstallmentConfirmRequestDTO buildInstallmentConfirmRequestDTO(long requestId, long recordId,
			String verifyCode) {
		InstallmentConfirmRequestDTO requestDTO = new InstallmentConfirmRequestDTO();
		requestDTO.setRecordId(recordId);
		requestDTO.setRequestId(requestId);
		requestDTO.setVerifyCode(verifyCode);
		return requestDTO;
	}

	@Override
	public CashierPaymentResponseDTO preauthOrderRequest(CashierPaymentRequestDTO requestDTO) {
		CashierPaymentResponseDTO response = ncCashierPreauthFacade.preAuthOrderRequest(requestDTO);
		if (response == null || !requestDTO.getTokenId().equals(response.getTokenId())) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}
	@Override
	public CashierPaymentResponseDTO preauthBindOrderRequest(CashierPaymentRequestDTO requestDTO) {
		CashierPaymentResponseDTO response = ncCashierPreauthFacade.preAuthBindOrderRequest(requestDTO);
		if (response == null || !requestDTO.getTokenId().equals(response.getTokenId())) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return response;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}
	@Override
	public void preauthOrderConfirm(PayConfirmBaseRequestDTO requestDTO){
		PayConfirmBaseResponseDTO response = ncCashierPreauthFacade.preAuthOrderConfirm(requestDTO);
		if(null == response || !requestDTO.getTokenId().equals(response.getTokenId())){
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}

	@Override
	public void preauthBindOrderConfirm(PreauthBindConfirmRequestDTO requestDTO) {
		PayConfirmBaseResponseDTO response = ncCashierPreauthFacade.preAuthBindOrderConfirm(requestDTO);
		if(null == response || !requestDTO.getTokenId().equals(response.getTokenId())){
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}
	
	@Override
	public void preAuthSendSms(CashierSmsSendRequestDTO requestDTO) {
		CashierSmsSendResponseDTO response = ncCashierPreauthFacade.preAuthSendSms(requestDTO);
		if(null == response || !requestDTO.getTokenId().equals(response.getTokenId())){
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		} else if (response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS) {
			return;
		} else {
			throw new CashierBusinessException(response.getReturnCode(), response.getReturnMsg());
		}
	}
	
	@Override
	public MarketActivityResponseDTO getMarketActivityInfo(MarketActivityRequestDTO requestDTO) {
		MarketActivityResponseDTO response = marketActivityFacade.getMarketActivityInfo(requestDTO);
		if (null == response || response.getProcessStatusEnum() == ProcessStatusEnum.FAILED) {
			return null;
		}
		return response;
	}
	
	
	
}
