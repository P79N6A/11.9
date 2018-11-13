package com.yeepay.g3.core.nccashier.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.component.open.facade.OpenMemberQueryAccountFacade;
import com.yeepay.g3.core.nccashier.dao.PaymentRecordDao;
import com.yeepay.g3.core.nccashier.dao.PaymentRequestDao;
import com.yeepay.g3.core.nccashier.dao.UserProcessDao;
import com.yeepay.g3.core.nccashier.dao.UserRequestInfoDao;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserAccount;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.enumtype.SystemEnum;
import com.yeepay.g3.core.nccashier.gateway.service.MarketInfoService;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantConfigCenterService;
import com.yeepay.g3.core.nccashier.gateway.service.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.nccashier.gateway.service.UserCenterService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.RecordCondition;
import com.yeepay.g3.facade.configcenter.facade.MerchantConfigFacade;
import com.yeepay.g3.facade.configcenter.facade.MerchantConfigurationQueryFacade;
import com.yeepay.g3.facade.cwh.facade.*;
import com.yeepay.g3.facade.fee.front.service.CalFeeModelManagementFacade;
import com.yeepay.g3.facade.fee.front.service.CalFeeTransactionFacade;
import com.yeepay.g3.facade.foundation.facade.MerchantLimitFacade;
import com.yeepay.g3.facade.frontend.facade.*;
import com.yeepay.g3.facade.frontend.installment.facade.InstallmentFacade;
import com.yeepay.g3.facade.frontend.installment.facade.InstallmentQueryFacade;
import com.yeepay.g3.facade.frontend.installment.facade.InstallmentReverseFacade;
import com.yeepay.g3.facade.merchant_platform.facade.HmacKeyFacade;
import com.yeepay.g3.facade.merchant_platform.facade.MerchantFacade;
import com.yeepay.g3.facade.mktg.facade.MarketingFacade;
import com.yeepay.g3.facade.mp.facade.UserFacade;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.PaymentRecordDTO;
import com.yeepay.g3.facade.nccashier.dto.TradeNoticeDTO;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.facade.*;
import com.yeepay.g3.facade.ncmember.facade.BankCardSignFacade;
import com.yeepay.g3.facade.ncmember.facade.BindCardFacade;
import com.yeepay.g3.facade.ncmember.facade.MemberAuthorityFacade;
import com.yeepay.g3.facade.ncpay.facade.NcPayResultTaskFacade;
import com.yeepay.g3.facade.ncpay.facade.NcPayncReverseFacade;
import com.yeepay.g3.facade.ncpay.facade.PaymentManageFacade;
import com.yeepay.g3.facade.ncpay.facade.PaymentManagerWrapperFacade;
import com.yeepay.g3.facade.nop.facade.AuthBindCardFacade;
import com.yeepay.g3.facade.nop.facade.QueryCardInfoFacade;
import com.yeepay.g3.facade.nop.facade.QueryProductStatusFacade;
import com.yeepay.g3.facade.opr.facade.OrderFacade;
import com.yeepay.g3.facade.payprocessor.dto.BasicRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.CombRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.ResponseStatusDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.facade.payprocessor.facade.*;
import com.yeepay.g3.facade.pccashier.pay.facade.CashierQueryFacade;
import com.yeepay.g3.facade.wechat.facade.WechatQRFacade;
import com.yeepay.g3.facade.yop.ca.facade.DigitalSecurityFacade;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.riskcontrol.facade.TradeLimitConfigQueryFacade;
import com.yeepay.riskcontrol.query.facade.DoorgodBlCheckFacade;

import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.core.AmqpTemplate;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * service是对dao、facade等接口的调用封装。这里统一定义了dao、util、facade
 * 
 * @author：peile.fan
 * @since：2016年5月19日 下午6:27:39
 * @version:
 */
public class NcCashierBaseService {
	
	@Resource
	protected PaymentRecordDao paymentRecordDao;
	@Resource
	protected PaymentRequestDao paymentRequestDao;
	@Resource
	protected UserProcessDao userProcessDao;

	@Resource
	protected UserProceeService userProceeService;

	@Resource
	protected AmqpTemplate amqpTemplate;

	@Resource
	protected WeChatOrderPaymentService weChatOrderPaymentService;
	
	@Resource
	protected UserCenterService userCenterService;
	
	@Resource
	protected MerchantConfigCenterService merchantConfigService;
	
	@Resource
	protected MarketInfoService marketInfoService;
	
	@Resource
	protected MarketInfoManageService marketInfoManageService;
	
	@Resource
	protected UserRequestInfoDao userRequestInfoDao;



	private static Logger logger = LoggerFactory.getLogger(NcCashierBaseService.class);



	/**
	 * 扩展用户服务
	 */
	protected ExternalUserCwhFacade externalUserCwhFacade = RemoteFacadeProxyFactory
			.getService(ExternalUserCwhFacade.class, SystemEnum.NCCWH.name());
	/**
	 * 临时卡处理服务
	 */
	protected PayTmpCardCwhFacade payTmpCardCwhFacade = RemoteFacadeProxyFactory.getService(PayTmpCardCwhFacade.class,
			SystemEnum.NCCWH.name());
	/**
	 * 绑卡处理服务
	 */
	protected BindCardCwhFacade bindCardCwhFacade = RemoteFacadeProxyFactory.getService(BindCardCwhFacade.class,
			SystemEnum.NCCWH.name());
	protected BankCardCwhFacade bankCardCwhFacade = RemoteFacadeProxyFactory.getService(BankCardCwhFacade.class,
			SystemEnum.NCCWH.name());

	protected PaymentManagerWrapperFacade paymentManagerWrapperFacade = RemoteFacadeProxyFactory
			.getService(PaymentManagerWrapperFacade.class, SystemEnum.NCPAY.name());
	
	/**
	 * ncconfig
	 */
	protected ConfigCashierFacade configCashierFacade = RemoteFacadeProxyFactory.getService(ConfigCashierFacade.class,
			SystemEnum.NCCONFIG.name());

	/**
	 * ncconfig 是否授权跨商编支付
	 */
	protected ConfigAuthPayFacade configAuthPayFacade = RemoteFacadeProxyFactory.getService(ConfigAuthPayFacade.class,
			SystemEnum.NCCONFIG.name());

	/**
	 * ncconfig 网银收银台模板facade
	 */
	protected OnlineBankCashierTemplateFacade onlineBankCashierTemplateFacade = RemoteFacadeProxyFactory.getService(OnlineBankCashierTemplateFacade.class,
			SystemEnum.NCCONFIG.name());
	/**
	 * ncconfig 收银台定制化facade
	 */
	protected NccashierMerchantLayoutConfigFacade nccashierMerchantLayoutConfigFacade = RemoteFacadeProxyFactory
			.getService(NccashierMerchantLayoutConfigFacade.class, SystemEnum.NCCONFIG.name());
	protected NccashierMerchantCustomizedFileFacade nccashierMerchantCustomizedFileFacade = RemoteFacadeProxyFactory
			.getService(NccashierMerchantCustomizedFileFacade.class, SystemEnum.NCCONFIG.name());
	
	protected PaymentManageFacade paymentManageFacade = RemoteFacadeProxyFactory.getService(PaymentManageFacade.class,
			SystemEnum.NCPAY.name());
	protected NcPayncReverseFacade ncPayncReverseFacade = RemoteFacadeProxyFactory
			.getService(NcPayncReverseFacade.class, SystemEnum.NCPAY.name());
	protected NcPayResultTaskFacade ncPayResultTaskFacade = RemoteFacadeProxyFactory
			.getService(NcPayResultTaskFacade.class, SystemEnum.NCPAY.name());
	protected ConfigCommonFacade configCommonFacade = RemoteFacadeProxyFactory.getService(ConfigCommonFacade.class,
			SystemEnum.NCCONFIG.name());

	protected CardUserInfoQueryFacade cardUserInfoQueryFacade = RemoteFacadeProxyFactory
			.getService(CardUserInfoQueryFacade.class, SystemEnum.NCCWH.name());

	protected FrontendRefundFacade frontendRefundFacade = RemoteFacadeProxyFactory
			.getService(FrontendRefundFacade.class, SystemEnum.FRONTEND.name());

	protected FrontendQueryFacade frontendQueryFacade = RemoteFacadeProxyFactory.getService(FrontendQueryFacade.class,
			SystemEnum.FRONTEND.name());

	protected MerchantFacade merchantFacade = RemoteFacadeProxyFactory.getService(MerchantFacade.class,
			SystemEnum.MERCHANT_PLATEFORM.name());

	protected OrderFacade orderFacade = RemoteFacadeProxyFactory.getService(OrderFacade.class,
			SystemEnum.ORDER_PROCCESOR.name());

	protected PayOrderFacade payOrderFacade = RemoteFacadeProxyFactory.getService(PayOrderFacade.class,
			SystemEnum.PAY_PROCCESOR.name());

	protected PayManageFacade payManageFacade = RemoteFacadeProxyFactory.getService(PayManageFacade.class,
			SystemEnum.PAY_PROCCESOR.name());

	protected PayPreAuthFacade payPreAuthFacade = RemoteFacadeProxyFactory.getService(PayPreAuthFacade.class,
			SystemEnum.PAY_PROCCESOR.name());
	protected PayCflEasyFacade payCflEasyFacade = RemoteFacadeProxyFactory.getService(PayCflEasyFacade.class,
			SystemEnum.PAY_PROCCESOR.name());
	protected PayProcessorQueryFacade payProcessorQueryFacade = RemoteFacadeProxyFactory
			.getService(PayProcessorQueryFacade.class, SystemEnum.PAY_PROCCESOR.name());

	protected MerchantLimitFacade merchantLimitFacade = RemoteFacadeProxyFactory.getService(MerchantLimitFacade.class,
			SystemEnum.MERCHANT_CONFIG.name());

	protected MerchantConfigurationQueryFacade merchantConfigurationQueryFacade= RemoteFacadeProxyFactory
			.getService(MerchantConfigurationQueryFacade.class, SystemEnum.MERCHANT_CONFIG.name());


	protected MerchantConfigFacade merchantConfigFacade = RemoteFacadeProxyFactory
			.getService(MerchantConfigFacade.class, SystemEnum.MERCHANT_CONFIG.name());

	protected PayRecordQueryFacade payRecordQueryFacade = RemoteFacadeProxyFactory
			.getService(PayRecordQueryFacade.class, SystemEnum.PAY_PROCCESOR.name());
	
	protected InstallmentFacade installmentFacade = RemoteFacadeProxyFactory
			.getService(InstallmentFacade.class, SystemEnum.FRONTEND.name());
	
	protected InstallmentQueryFacade installmentQueryFacade = RemoteFacadeProxyFactory
			.getService(InstallmentQueryFacade.class, SystemEnum.FRONTEND.name());
	
	/** FE分期子系统 冲正接口 */
	protected InstallmentReverseFacade installmentReverseFacade = RemoteFacadeProxyFactory
			.getService(InstallmentReverseFacade.class, SystemEnum.FRONTEND.name());
	/**
	 * 获取风控服务
	 */
	protected TradeLimitConfigQueryFacade tradeLimitConfigQueryFacade = RemoteFacadeProxyFactory
			.getService(TradeLimitConfigQueryFacade.class, SystemEnum.RISKCONTROL.name());
	
	protected DoorgodBlCheckFacade doorgodBlCheckFacade = RemoteFacadeProxyFactory
			.getService(DoorgodBlCheckFacade.class, SystemEnum.RISKCONTROL.name());

	protected FrontendPayFacade frontendPayFacade = RemoteFacadeProxyFactory
			.getService(FrontendPayFacade.class, SystemEnum.FRONTEND.name());


	//用户中心的有关绑卡列表facade接口
	protected BindCardFacade bindCardFacade = RemoteFacadeProxyFactory
			.getService(BindCardFacade.class, SystemEnum.USER_CENTER.name());
	//用户中心的有用户共享卡授权的facade接口
	protected MemberAuthorityFacade memberAuthorityFacade = RemoteFacadeProxyFactory
			.getService(MemberAuthorityFacade.class, SystemEnum.USER_CENTER.name());
	//用户中心的有商户注册的facade接口
	protected com.yeepay.g3.facade.ncmember.facade.MerchantConfigFacade memberMerchantConfigFacade = RemoteFacadeProxyFactory
			.getService(com.yeepay.g3.facade.ncmember.facade.MerchantConfigFacade.class, SystemEnum.USER_CENTER.name());
	// 用户中心提供的银行卡签约相关接口
		protected BankCardSignFacade bankCardSignFacade = RemoteFacadeProxyFactory.getService(BankCardSignFacade.class,
				SystemEnum.USER_CENTER.name());
		
	protected CashierQueryFacade cashierQueryFacade = RemoteFacadeProxyFactory
			.getService(CashierQueryFacade.class, SystemEnum.BIBASIC_ORDER.name());
	
	protected DigitalSecurityFacade digitalSecurityFacade = RemoteFacadeProxyFactory.getService(DigitalSecurityFacade.class, SystemEnum.YOP.name());

	protected CalFeeTransactionFacade calFeeTransactionFacade = RemoteFacadeProxyFactory.getService(CalFeeTransactionFacade.class,SystemEnum.CALL_FEE.name());
	
	/**
	 * 费率查询
	 */
	protected CalFeeModelManagementFacade calFeeModelManagementFacade = RemoteFacadeProxyFactory
			.getService(CalFeeModelManagementFacade.class, SystemEnum.CALL_FEE.name());
	
	/** 商户平台系统**/
	protected UserFacade userFacade = RemoteFacadeProxyFactory.getService(UserFacade.class,
			SystemEnum.MERCHANT_PLATFORM.name());
	
	protected HmacKeyFacade hmacKeyFacade = RemoteFacadeProxyFactory.getService(HmacKeyFacade.class,
			SystemEnum.MERCHANT_PLATFORM.name());

	/* 综合服务产品组-微信公众号相关*/
	protected WechatQRFacade wechatQRFacade = RemoteFacadeProxyFactory.getService(WechatQRFacade.class,SystemEnum.WECHAT_APP.name());

	//无交易订单处理器
	protected AuthBindCardFacade authBindCardFacade = RemoteFacadeProxyFactory.getService(AuthBindCardFacade.class,
			SystemEnum.NO_TRADING_PROCCESSOR.name());
	protected QueryCardInfoFacade queryCardInfoFacade = RemoteFacadeProxyFactory.getService(QueryCardInfoFacade.class,
			SystemEnum.NO_TRADING_PROCCESSOR.name());
	protected QueryProductStatusFacade queryProductStatusFacade = RemoteFacadeProxyFactory.getService(QueryProductStatusFacade.class,
			SystemEnum.NO_TRADING_PROCCESSOR.name());

	//三代会员子系统
	protected OpenMemberQueryAccountFacade openMemberQueryAccountFacade = RemoteFacadeProxyFactory.getService(OpenMemberQueryAccountFacade.class,
			SystemEnum.MEMBER.name());
	
	/**
	 * 营销系统
	 */
	protected MarketingFacade marketingFacade = RemoteFacadeProxyFactory.getService(MarketingFacade.class, SystemEnum.MARKETING.name());

	protected OrderNoticeDTO orderNoticeTransfer(PaymentRequest paymentRequest,
			List<PaymentRecord> recordList) {
		OrderNoticeDTO orderNoticeDTO = new OrderNoticeDTO();
		orderNoticeDTO.setErrorCode(paymentRequest.getErrorCode());
		orderNoticeDTO.setErrorMsg(paymentRequest.getErrorMsg());
		orderNoticeDTO.setMerchantNo(paymentRequest.getMerchantNo());
		orderNoticeDTO.setMerchantOrderId(paymentRequest.getMerchantOrderId());
		orderNoticeDTO.setPaymentOrderNo(paymentRequest.getPaymentOrderNo());
		orderNoticeDTO.setPaymentAmount(paymentRequest.getOrderAmount());
		orderNoticeDTO.setCost(paymentRequest.getCost());
		orderNoticeDTO.setPay_time(paymentRequest.getPayTime());
		orderNoticeDTO.setPaymentStatus(PayRequestStatusEnum.valueOf(paymentRequest.getState()));
		orderNoticeDTO.setTradeSysNo(paymentRequest.getTradeSysNo());
		orderNoticeDTO.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
		orderNoticeDTO.setOrderOrderId(paymentRequest.getOrderOrderId());
		orderNoticeDTO.setOrderSysNo(paymentRequest.getOrderSysNo());
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		orderNoticeDTO.setBasicProductCode(jsonObject.getString("basicProductCode"));//零售产品码
		
		List<PaymentRecordDTO> list = null;
		if (null != recordList && recordList.size() > 0) {
			list = new ArrayList<PaymentRecordDTO>();
			for (PaymentRecord paymentRecord : recordList) {
				PaymentRecordDTO paymentRecordDTO = paymentRecordTransferPaymentRecordDTO(paymentRecord);
				list.add(paymentRecordDTO);
			}
		}
		orderNoticeDTO.setPaymentRecordDTOList(list);
		return orderNoticeDTO;
	}

	private PaymentRecordDTO paymentRecordTransferPaymentRecordDTO(PaymentRecord paymentRecord) {
		PaymentRecordDTO paymentRecordDTO = new PaymentRecordDTO();
		paymentRecordDTO.setAreaInfo(paymentRecord.getAreaInfo());
		paymentRecordDTO.setBankChannelNo(paymentRecord.getBankChannelNo());
		paymentRecordDTO.setBankCode(paymentRecord.getBankCode());
		paymentRecordDTO.setBankOrderNo(paymentRecord.getBankOrderNo());
		paymentRecordDTO.setBindId(paymentRecord.getBindId());
		paymentRecordDTO.setCardInfoId(paymentRecord.getCardInfoId());
		paymentRecordDTO.setBizModeCode(paymentRecord.getBizModeCode());
		paymentRecordDTO.setCardNo(paymentRecord.getCardNo());
		paymentRecordDTO.setCardType(paymentRecord.getCardType());
		paymentRecordDTO.setCreateTime(paymentRecord.getCreateTime());
		paymentRecordDTO.setErrorCode(paymentRecord.getErrorCode());
		paymentRecordDTO.setErrorMsg(paymentRecord.getErrorMsg());
		paymentRecordDTO.setIdCard(paymentRecord.getIdCard());
		paymentRecordDTO.setIdCardType(paymentRecord.getIdCardType());
		paymentRecordDTO.setMcc(paymentRecord.getMcc());
		paymentRecordDTO.setMerchantName(paymentRecord.getMerchantName());
		paymentRecordDTO.setMerchantNo(paymentRecord.getMerchantNo());
		paymentRecordDTO.setMerchantOrderId(paymentRecord.getMerchantOrderId());
		paymentRecordDTO.setOwner(paymentRecord.getOwner());
		paymentRecordDTO.setPaymentAmount(paymentRecord.getPaymentAmount());
		paymentRecordDTO.setPaymentOrderNo(paymentRecord.getPaymentOrderNo());
		paymentRecordDTO.setPaymentRequestId(paymentRecord.getPaymentRequestId());
		paymentRecordDTO.setPaymentSysNo(paymentRecord.getPaymentSysNo());
		paymentRecordDTO.setPayProductCode(paymentRecord.getPayProductCode());
		paymentRecordDTO.setPayType(paymentRecord.getPayType());
		paymentRecordDTO.setPhoneNo(paymentRecord.getPhoneNo());
		paymentRecordDTO.setProductName(paymentRecord.getProductName());
		paymentRecordDTO.setSmsVerifyType(paymentRecord.getSmsVerifyType());
		paymentRecordDTO.setState(paymentRecord.getState());
		paymentRecordDTO.setTradeRiskInfo(paymentRecord.getTradeRiskInfo());
		paymentRecordDTO.setTradeSysNo(paymentRecord.getTradeSysNo());
		paymentRecordDTO.setTradeSysOrderId(paymentRecord.getTradeSysOrderId());
		paymentRecordDTO.setOrderOrderId(paymentRecord.getOrderOrderId());
		paymentRecordDTO.setOrderSysNo(paymentRecord.getOrderSysNo());
		paymentRecordDTO.setCost(paymentRecord.getCost());
		paymentRecordDTO.setTradeSerialNo(paymentRecord.getTradeSerialNo());
		paymentRecordDTO.setUpdateTime(paymentRecord.getUpdateTime());
		paymentRecordDTO.setPayTool(paymentRecord.getPayTool());
		return paymentRecordDTO;
	}

	protected TradeNoticeDTO makeWapTradeNoticeDTO(PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		TradeNoticeDTO tradeNoticeDTO = new TradeNoticeDTO();
		if (null != paymentRequest) {
			tradeNoticeDTO.setFrontCallBackUrl(paymentRequest.getFrontCallBackUrl());
			tradeNoticeDTO.setMerchantNo(paymentRequest.getMerchantNo());
			tradeNoticeDTO.setCashierVersion(paymentRequest.getCashierVersion());
			tradeNoticeDTO.setPaymentRequestId(paymentRequest.getId());
			tradeNoticeDTO.setPaymentAmount(paymentRequest.getOrderAmount());
			tradeNoticeDTO.setPayCompleteDate(paymentRequest.getPayTime());
			tradeNoticeDTO.setOrderSysNo(paymentRequest.getOrderSysNo());
		}
		if (null != paymentRecord) {
			CashierBusinessException errorInfo = CommonUtil.handleException(SysCodeEnum.NCPAY.name(),
					paymentRecord.getErrorCode(), paymentRecord.getErrorMsg());
			tradeNoticeDTO.setTradeState(TradeStateEnum.valueOf(paymentRecord.getState().name()));
			tradeNoticeDTO.setErrorCode(errorInfo.getDefineCode());
			tradeNoticeDTO.setErrorMsg(errorInfo.getMessage());
			tradeNoticeDTO.setBankChannelNo(paymentRecord.getBankChannelNo());
			tradeNoticeDTO.setBankOrderNo(paymentRecord.getBankOrderNo());
			tradeNoticeDTO.setCardInfoId(paymentRecord.getCardInfoId());
			tradeNoticeDTO.setCardNo(paymentRecord.getCardNo());
			tradeNoticeDTO.setCardType(paymentRecord.getCardType());
			tradeNoticeDTO.setCost(paymentRecord.getCost());
			tradeNoticeDTO.setIdCard(paymentRecord.getIdCard());
			tradeNoticeDTO.setIdCardType(paymentRecord.getIdCardType());
			tradeNoticeDTO.setMerchantOrderId(paymentRecord.getMerchantOrderId());
			tradeNoticeDTO.setOrderOrderId(paymentRecord.getOrderOrderId());
			tradeNoticeDTO.setOrderSysNo(paymentRecord.getOrderSysNo());
			tradeNoticeDTO.setOwner(paymentRecord.getOwner());
			tradeNoticeDTO.setPaymentOrderNo(paymentRecord.getPaymentOrderNo());
			tradeNoticeDTO.setPayType(paymentRecord.getPayType());
			tradeNoticeDTO.setPhoneNo(paymentRecord.getPhoneNo());
			tradeNoticeDTO.setTradeSysNo(paymentRecord.getTradeSysNo());
			tradeNoticeDTO.setTradeSysOrderId(paymentRecord.getTradeSysOrderId());
		}
		return tradeNoticeDTO;
	}

	/****************************************************************************/
	/*****************************   构造风控参数   *******************************/


	protected String buildTradeRiskInfoByRequest(PaymentRequest payRequest){
		return makeTradeRiskInfo(payRequest.getTradeRiskInfo(),payRequest.getTerminalInfo(),null,
				null,payRequest.getParentMerchantNo(),null,payRequest);
	}

	/**
	 * 通过token和request组装风控信息
	 *
	 * terminalinfo中的字段terminalId,terminalType拿出来放到大json中了
	 * @param token
	 * @param payRequest
     * @return
     */
	protected String buildTradeRiskInfoUseTokenAndRequest(String token, PaymentRequest payRequest) {
		UserAccount userAccount = userProceeService.getUserAccountInfo(token);
		String riskInfo = payRequest.getTradeRiskInfo();
		String terminalinfo = payRequest.getTerminalInfo();
		String userip = null;
		String userua = null;
		if (userAccount != null){
			userip = userAccount.getUserIp();
			userua = userAccount.getUserUa();
		}
		//request保存的可能不是真实的用户ip,需要用userAccount中的ip替换
		if(StringUtils.isNotBlank(userip))
			payRequest.setUserIp(userip);
		return makeTradeRiskInfo(riskInfo,terminalinfo,userua,userip,payRequest.getParentMerchantNo(),null,payRequest);
	}


	/**
	 *
	 * @param tooluserip
	 * @param payRequest
     * @return
     */
	protected String buildTradeRiskInfoByTooluseripAndRequest(String tooluserip, PaymentRequest payRequest) {
		String riskInfo = payRequest.getTradeRiskInfo();
		String userip = payRequest.getUserIp();
		String terminalinfo = payRequest.getTerminalInfo();
		return makeTradeRiskInfo(riskInfo,terminalinfo,null,userip,null,tooluserip,payRequest);
	}


	/**
	 * 通过userIp和request构造风控参数
	 * @param userIp
	 * @param payRequest
     * @return
     */
	protected String buildTradeRiskInfoByUseripAndRequest(String userIp, PaymentRequest payRequest) {
		String riskInfo = payRequest.getTradeRiskInfo();
		return makeTradeRiskInfo(riskInfo,payRequest.getTerminalInfo(),null,userIp,null,null,payRequest);
	}

	private void buildTerminalInfo(String terminalinfo,Map<String, String> goodsinfo){
		if (StringUtils.isNotBlank(terminalinfo)) {
			Map<String, String> terminalinfoMap = new HashMap<String, String>();
			terminalinfoMap = JSONUtils.jsonToMap(terminalinfo, String.class, String.class);
			goodsinfo.put("terminalId", terminalinfoMap.get("terminalId"));
			goodsinfo.put("terminalType", terminalinfoMap.get("terminalType"));
		}
	}



	@SuppressWarnings("unchecked")
	private String makeTradeRiskInfo(String riskInfo,String terminalinfo,String userua,String userip,
									 String systemMerchantNo,String tooluserip,PaymentRequest payRequest){
		String tradeRiskInfo = null;
		Map<String, String> goodsinfo = new HashMap<String, String>();
		try {
			if (StringUtils.isNotBlank(riskInfo)) {
				goodsinfo = JSONUtils.jsonToMap(riskInfo, String.class, String.class);
			}
		} catch (Exception e) {
			logger.warn("requestId=" + payRequest.getId() + "转化tradeRiskInfo=" + riskInfo + "异常", e);
		}
		if (StringUtils.isNotBlank(terminalinfo))
			buildTerminalInfo(terminalinfo,goodsinfo);
		if (StringUtils.isNotBlank(userua))
			goodsinfo.put("userua", userua);
		if(StringUtils.isNotBlank(systemMerchantNo))
			goodsinfo.put("systemMerchantNo", systemMerchantNo);
		if(StringUtils.isNotBlank(tooluserip))
			goodsinfo.put("tooluserip", tooluserip);
		if (StringUtils.isNotBlank(userip))
			goodsinfo.put("userIp", userip);
		if (MapUtils.isNotEmpty(goodsinfo))
			tradeRiskInfo = JSONUtils.toJsonString(goodsinfo);
		return tradeRiskInfo;
	}


	/***************************************************************************/
	/***************************************************************************/

	protected CombRequestDTO buildCompPayInfo(String token, PaymentRequest paymentRequest, String payProduct,
			String type, String bankCode) {
		// 组合支付信息
		Set<String> marketActivityIds = marketInfoManageService.getMarketActivityInfoByPayInfo(token, paymentRequest,
				payProduct, type, bankCode);
		if (CollectionUtils.isEmpty(marketActivityIds)) {
			return null;
		}

		CombRequestDTO combRequestDTO = new CombRequestDTO();
		combRequestDTO.setMarketingNo(marketActivityIds.size() == 1 ? marketActivityIds.iterator().next()
				: JSON.toJSONString(marketActivityIds));
		combRequestDTO.setPayOrderType(CombPayOrderTypeEnum.MKTG.name());
		combRequestDTO.setPaymentType(type);
		return combRequestDTO;
	}


	protected void buildBasicRequestDTO(PaymentRequest request, BasicRequestDTO requestDTO) {
		requestDTO.setDealUniqueSerialNo(request.getTradeSysOrderId());
		requestDTO.setRequestSystem(Constant.NCCASHIER_MQ_QUEUE_NAME);
		requestDTO.setRequestSysId(request.getId() + "");
		requestDTO.setOrderSystem(request.getTradeSysNo());
		requestDTO.setOutTradeNo(request.getMerchantOrderId());
		requestDTO.setOrderNo(request.getTradeSysOrderId());
		requestDTO.setCustomerNumber(request.getMerchantNo());
		requestDTO.setCustomerName(request.getMerchantName());
		requestDTO.setProductName(request.getProductName());
		requestDTO.setAmount(request.getOrderAmount());
		requestDTO.setPayerIp(request.getUserIp());
		requestDTO.setIndustryCode(request.getIndustryCatalog());
		if(request.getFee()!=null){
			requestDTO.setUserFee(request.getFee());
		}
		
		String orderExpDateType = StringUtils.isBlank(request.getOrderExpDateType())?"MINUTE":request.getOrderExpDateType();
		if("DAY".equals(orderExpDateType)){
			requestDTO.setExpireDate(DateUtils.addDay(request.getOrderTimeNotNull(), request.getOrderExpDate()));
		}else if("HOUR".equals(orderExpDateType)){
			requestDTO.setExpireDate(DateUtils.addHour(request.getOrderTimeNotNull(), request.getOrderExpDate()));
		}else if("MINUTE".equals(orderExpDateType)){
			requestDTO.setExpireDate(DateUtils.addMinute(request.getOrderTimeNotNull(), request.getOrderExpDate()));
		}else if("SECOND".equals(orderExpDateType)){
			requestDTO.setExpireDate(DateUtils.addSecond(request.getOrderTimeNotNull(), request.getOrderExpDate()));
		}else {
			requestDTO.setExpireDate(DateUtils.addMinute(request.getOrderTimeNotNull(), request.getOrderExpDate()));
		}

		requestDTO.setRiskProduction(StringUtils.isNotBlank(request.getRiskProduction())?request.getRiskProduction():CommonUtil.getRiskProduction(request.getOrderSysNo()));
		if(Constant.YIBAO.equals(request.getMemberType())){
			requestDTO.setUserType(Constant.YIBAO);
			requestDTO.setUserId(request.getMemberNo());
		}else{
			requestDTO.setUserId(request.getIdentityId());
			requestDTO.setUserType(request.getIdentityType());
		}
		
	}
	
	protected void buildBasicRequestDTO(PaymentRequest paymentRequest, PaymentRecord paymentRecord, BasicRequestDTO requestDTO){
		buildBasicRequestDTO(paymentRequest, requestDTO);
		requestDTO.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
		requestDTO.setPayProduct(paymentRecord.getPayTool());
	}
	
	protected PayRecordStatusEnum checkSyncTrxStatusOfPP(TrxStatusEnum trxStatus) {
		if (TrxStatusEnum.SUCCESS.equals(trxStatus)) {
			return PayRecordStatusEnum.SUCCESS;
		} else if (TrxStatusEnum.FAILUER.equals(trxStatus)) {
			return PayRecordStatusEnum.FAILED;
		} else if (TrxStatusEnum.REVERSE.equals(trxStatus)) {
			return PayRecordStatusEnum.FAILED;
		}
		// 剩余一个是DOING状态，这种状态就维持PAYING状态，就会员支付这个来说，理论上应该都会是有终态的
		return PayRecordStatusEnum.PAYING;
	}
	
	protected void handleTrxStatusOfPPWithException(TrxStatusEnum trxStatus, ResponseStatusDTO responseDTO){
		if(TrxStatusEnum.DOING.equals(trxStatus)){
			throw CommonUtil.handleException(Errors.ORDER_STATUS_UNKNOWN);
		}
		else if (TrxStatusEnum.REVERSE.equals(trxStatus)) {
			throw CommonUtil.handleException(Errors.ORDER_STATUS_REVERSE);
		}
		else if(TrxStatusEnum.FAILUER.equals(trxStatus)){
			throw CommonUtil.handleException(SysCodeEnum.PP.name(), responseDTO.getResponseCode(),
					responseDTO.getResponseMsg());
		}
	}


	public RecordCondition buildRecordCondition(PaymentRequest paymentRequest, String recordId,
															 OrderAction orderAction, String recordPayTypes[],PayTool payTool) {
		RecordCondition condition = new RecordCondition();

		condition.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
		condition.setTradeSysNo(paymentRequest.getTradeSysNo());
		condition.setRecordId(recordId);
		if(payTool!=null)
			condition.setPayTool(payTool.name());
		if(orderAction!=null)
			condition.setOrderAction(orderAction);
		if(recordPayTypes!= null)
			condition.setRecordPayTypes(recordPayTypes);
		return condition;
	}




}

