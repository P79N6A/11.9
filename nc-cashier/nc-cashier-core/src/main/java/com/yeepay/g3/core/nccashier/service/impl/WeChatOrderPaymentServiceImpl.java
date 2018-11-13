/**
 * 
 */
package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserRequestInfo;
import com.yeepay.g3.core.nccashier.gateway.service.FrontEndService;
import com.yeepay.g3.core.nccashier.gateway.service.FrontedInstallmentService;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantInfoService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.WeChatOrderPaymentService;
import com.yeepay.g3.core.nccashier.utils.AESUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.core.nccashier.vo.ExtendInfoFromPayRequest;
import com.yeepay.g3.core.nccashier.vo.SimpleRecodeInfoModel;
import com.yeepay.g3.facade.frontend.dto.InstallmentOrderResponseDTO;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.merchant_platform.dto.LevelRespDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.CommonUtils;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhen.tan
 *
 */
@Service("weChatOrderPaymentService")
public class WeChatOrderPaymentServiceImpl extends NcCashierBaseService implements WeChatOrderPaymentService {
	
	private static final Logger logger = NcCashierLoggerFactory.getLogger(OrderPaymentServiceImpl.class);
	
	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private PaymentRequestService paymentRequestService;
	
	@Resource
	private FrontEndService frontEndService;
	
	@Resource
	private FrontedInstallmentService frontedInstallmentService;
	
	@Resource
	private PayProcessorService payProcessorService;
	
	@Resource
	private MerchantInfoService merchantInfoService;
	
	@Override
	public CombinedPaymentDTO validatePayBusinInfo(WeChatPayRequestDTO requestDto)
			throws CashierBusinessException {
		//进来的支付请求或为微信，或为支付宝，或为分期
		//老的paymentRecord或一键绑卡，或一键首次，或微信，或支付宝，或分期
		PaymentRequest payRequest =	paymentRequestService.findPaymentRequestByRequestId(requestDto.getRequestId());
		PaymentRecord record = null;
		Boolean isNeedOrderAgain = false;
		if (requestDto.getRecordId() > 0) {
			record = paymentProcessService
					.findRecordByPaymentRecordId(requestDto.getRecordId() + "");
			if (record != null) {
				if (!requestDto.getPayType().equals(record.getPayType())) {
					isNeedOrderAgain = true;
				} else {
					isNeedOrderAgain = false;
				}
			} 
			//记录不存在，需要创建record
			else {
				isNeedOrderAgain = true;
			}
		} 
		//首次下单，需要创建record
		else {
			isNeedOrderAgain = true;
		}

		CombinedPaymentDTO combinedPaymentDto = new CombinedPaymentDTO();
		combinedPaymentDto.setNeedOrderRecord(isNeedOrderAgain);
		combinedPaymentDto.setPaymentRequest(payRequest);
		if(!isNeedOrderAgain){
			combinedPaymentDto.setPaymentRecord(record);
		}

		logger.info("微信支付下单业务校验完成,下单标识={}", isNeedOrderAgain + "");
		return combinedPaymentDto;

	}
	
	@Override
	public void weChatCreateRecord(WeChatPayRequestDTO requestDto,CombinedPaymentDTO combinedPaymentDto) {
		PaymentRequest payRequest = combinedPaymentDto.getPaymentRequest();
		// 创建支付订单记录
		PaymentRecord paymentRecord = buildPaymentRecord(requestDto, payRequest);
		long recordId = paymentProcessService.savePaymentRecord(paymentRecord);
		requestDto.setRecordId(recordId);
		combinedPaymentDto.setPaymentRecord(paymentRecord);
		userProceeService.getAndUpdatePaymentRecordId(requestDto.getTokenId(), recordId + "");
	}

	
	@Override
	public String callFEPay(WeChatPayRequestDTO requestDto,CombinedPaymentDTO combinedPaymentDto){
		PaymentRequest paymentRequest = combinedPaymentDto.getPaymentRequest();
		PaymentRecord paymentRecord = combinedPaymentDto.getPaymentRecord();
		String riskInfo = buildTradeRiskInfoUseTokenAndRequest(requestDto.getTokenId(),paymentRequest);
		LevelRespDTO respDTO = merchantInfoService.getMerchantLevel(paymentRequest.getMerchantNo());
		if(!CommonUtils.isPayProcess(paymentRequest.getPaySysCode(),paymentRequest.getTradeSysNo())){//非订单处理器请求，走FE
			//非订单处理器请求
//			分期走FE
			if(PayTypeEnum.CFL.name().equals(requestDto.getPayType())){
				InstallmentOrderResponseDTO response = frontedInstallmentService.callInstallmentCreateOrder(paymentRequest,paymentRecord,respDTO);
				paymentProcessService.updateRecordNo(requestDto.getRecordId(),"",response.getOrderNo() ,
						PayRecordStatusEnum.PAYING);
				return response.getPayUrl();
			
			}else{
				PayRequestDTO payRequestDTO =buildPayRequestDTO(paymentRequest,requestDto);
				payRequestDTO.setGoodsInfo(riskInfo);
//				modify by xueping.ni 来客需求传所有商户类型为V类
				payRequestDTO.setCustomerLevel(Constant.CUSTOMER_LEVEL_V);
				payRequestDTO.setPaymentProduct(paymentRecord.getPayTool());
				payRequestDTO.setBasicProductCode(CommonUtil.getBasicProductCode(paymentRecord.getPayTool(),paymentRequest.getTradeSysNo()));
				logger.info("[调用FE传入参数,extParam:]"+payRequestDTO.getExtParam());
				PayResponseDTO payResponseDTO = frontEndService.frontendOpenPay(payRequestDTO);
				paymentProcessService.updateRecordNo(requestDto.getRecordId(),"",payResponseDTO.getOrderNo() , PayRecordStatusEnum.PAYING);
				return payResponseDTO.getPrepayCode();
			}
			
		}else{
			//走支付处理器的分期
			if (PayTypeEnum.CFL.name().equals(requestDto.getPayType())) {
				CflPayRequestDTO requestDTO = new CflPayRequestDTO();
				buildBasicRequestDTO(paymentRequest, requestDTO);
				buildInstallmentRequestDTO(requestDTO, paymentRecord, paymentRequest, riskInfo, respDTO.getCheckLevel());
				//支持营销立减
				requestDTO.setCombRequestDTO(buildCompPayInfo(requestDto.getTokenId(), paymentRequest,
						requestDTO.getPayProduct(), paymentRecord.getCardType(), paymentRecord.getBankCode()));
				CflPayResponseDTO response = payProcessorService.cflRequest(requestDTO);
				paymentProcessService.updateRecordNo(requestDto.getRecordId(), "", response.getRecordNo(), PayRecordStatusEnum.PAYING);
				return response.getPayUrl();
			}

			OpenPayRequestDTO openPayRequestDTO = new OpenPayRequestDTO();
			buildBasicRequestDTO(paymentRequest,openPayRequestDTO);
			//modify by xueping.ni 来客需求传所有商户类型为V类
			openPayRequestDTO.setCustomerLevel(Constant.CUSTOMER_LEVEL_V);
			openPayRequestDTO.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
			openPayRequestDTO.setPayProduct(paymentRecord.getPayTool());
			openPayRequestDTO.setGoodsInfo(riskInfo);
			buildOpenPayRequestDTO(openPayRequestDTO, paymentRequest, requestDto);
			openPayRequestDTO.setBasicProductCode(CommonUtil.getBasicProductCode(paymentRecord.getPayTool(),paymentRequest.getTradeSysNo()));
			openPayRequestDTO.setPageCallBack(CommonUtil.getH5FrontCallbackUrl(paymentRequest.getMerchantNo(), paymentRecord.getTokenId()));
			//支持营销立减
			openPayRequestDTO.setCombRequestDTO(buildCompPayInfo(requestDto.getTokenId(),paymentRequest,openPayRequestDTO.getPayProduct(),requestDto.getPayType(),paymentRecord.getBankCode()));

			OpenPayResponseDTO responseDTO = payProcessorService.openPayRequest(openPayRequestDTO);
			paymentProcessService.updateRecordNo(requestDto.getRecordId(),"",responseDTO.getRecordNo(),PayRecordStatusEnum.PAYING);
			
			//主扫支付时，把主扫的record精简信息放入缓存，便于前端成功监听支付结果
			if(openPayRequestDTO.getPayOrderType() ==PayOrderType.ACTIVESCAN){
				SimpleRecodeInfoModel recordInfo = new SimpleRecodeInfoModel(requestDto.getRecordId(),responseDTO.getRecordNo());
				
				
				String resultJson = RedisTemplate.getTargetFromRedisToString(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + requestDto.getTokenId());
				List<SimpleRecodeInfoModel> result = JSONObject.parseArray(resultJson, SimpleRecodeInfoModel.class);
				if(CollectionUtils.isEmpty(result)){
					result = new ArrayList<SimpleRecodeInfoModel>();
					result.add(recordInfo);
					RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + requestDto.getTokenId(), result,
							Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY_TIMEOUT);
				} else if(!result.contains(recordInfo)){
					result.add(recordInfo);
					RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + requestDto.getTokenId(), result,
							Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY_TIMEOUT);
				}
			}
			return responseDTO.getPrepayCode();
		}
		
	}


	private void buildInstallmentRequestDTO(CflPayRequestDTO requestDTO,
			PaymentRecord paymentRecord, PaymentRequest paymentRequest, String riskInfo, String merchantLevel) {
		requestDTO.setPayProduct(paymentRecord.getPayTool());
		requestDTO.setCustomerLevel(merchantLevel);
		requestDTO.setGoodsInfo(riskInfo);
		requestDTO.setProductType("VIRTUAL");
		requestDTO.setPayOrderType(PayOrderType.ONLINE);
		requestDTO.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
		requestDTO.setIdentityId(paymentRequest.getIdentityId());
		requestDTO.setIdentityType(paymentRequest.getIdentityType());
		requestDTO.setProductName(paymentRequest.getProductName());
		requestDTO.setPageCallBack(CommonUtil.getH5FrontCallbackUrl(paymentRequest.getMerchantNo(), paymentRecord.getTokenId()));
		/*只需要设置零售产品码*/
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		requestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));//零售产品码
	}


	@Override
	public JsapiRouteResponseDTO weChatPreRoute(PaymentRequest paymentRequest, String type) {
		OpenPrePayRequestDTO openPrePayRequestDTO = buildPrePayRequestDTO(paymentRequest, type);
		OpenPrePayResponseDTO openPrePayResponseDTO = payProcessorService.payOrderPrePay(openPrePayRequestDTO);
		JsapiRouteResponseDTO jsapiRouteResponseDTO = buildJsapiRouteResponseDTO(openPrePayResponseDTO);
		String extendInfo  = setExtendInfo(paymentRequest.getExtendInfo(),openPrePayResponseDTO, type);
		paymentRequest.setExtendInfo(extendInfo);
		paymentRequest.setUpdateTime(new Date());
		paymentRequestService.updatePayRequestExtendInfoById(paymentRequest);
		return jsapiRouteResponseDTO;
	}

	/**
	 * 构造支付记录
	 * 
	 * @param requestDto
	 * @param payRequest
	 * @return
	 */
	private PaymentRecord buildPaymentRecord(WeChatPayRequestDTO requestDto,PaymentRequest payRequest) {
		PaymentRecord paymentRecord = new PaymentRecord();
		paymentRecord.setAreaInfo(payRequest.getAreaInfo());
		paymentRecord.setBizModeCode(payRequest.getBizModeCode());	
		paymentRecord.setCreateTime(new Date());
		paymentRecord.setIdCardType(IdCardTypeEnum.IDENTITY.toString());
		paymentRecord.setMcc(payRequest.getIndustryCatalog());
		paymentRecord.setMerchantName(payRequest.getMerchantName());
		paymentRecord.setMerchantNo(payRequest.getMerchantNo());
		paymentRecord.setMerchantOrderId(payRequest.getMerchantOrderId());
		paymentRecord.setPaymentAmount(payRequest.getOrderAmount());
		paymentRecord.setPaymentRequestId(payRequest.getId());
		if(!CommonUtils.isPayProcess(payRequest.getPaySysCode(),payRequest.getTradeSysNo())){
			paymentRecord.setPaymentSysNo(PaymentSysCode.FE);
		}else{
			paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
		}
		paymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
		paymentRecord.setPayType(requestDto.getPayType());
		paymentRecord.setProductName(payRequest.getProductName());
		paymentRecord.setState(PayRecordStatusEnum.INIT);
		paymentRecord.setTokenId(requestDto.getTokenId());
		paymentRecord.setTradeSysNo(payRequest.getTradeSysNo());
		paymentRecord.setTradeSysOrderId(payRequest.getTradeSysOrderId());
		paymentRecord.setOrderOrderId(payRequest.getOrderOrderId());
		paymentRecord.setOrderSysNo(payRequest.getOrderSysNo());
		paymentRecord.setMemberNo("");
		paymentRecord.setMemberType(StringUtils.isNotBlank(payRequest.getMemberType())? payRequest.getMemberType():Constant.JOINLY);
		paymentRecord.setUpdateTime(new Date());
		paymentRecord.setVersion(1);
		
		String currentCashierVersion = payRequest.getCashierVersion();
		if(CommonUtil.cashierVersionGetFromUserRequestInfo()){
			UserRequestInfo userRequestInfo = userRequestInfoDao.getUserRequestInfoBytoken(paymentRecord.getTokenId());
			if(userRequestInfo!=null && StringUtils.isNotBlank(userRequestInfo.getCashierVersion())){
				currentCashierVersion = userRequestInfo.getCashierVersion();
			}
		}
		
		try {//开通CFL的会开PC收银台，所以判断顺序不能调整
			if (PayTypeEnum.CFL.name().equals(requestDto.getPayType())) {
				paymentRecord.setPayTool(PayTool.CFL.name());
			}else if(CashierVersionEnum.PC.name().equals(currentCashierVersion)){
				paymentRecord.setPayTool(PayTool.SCCANPAY.name());
			}else if (PayTypeEnum.WECHAT_OPENID.name().equals(requestDto.getPayType())) {
				paymentRecord.setPayTool(PayTool.WECHAT_OPENID.name());
			}else if(PayTypeEnum.ZFB_SHH.name().equals(requestDto.getPayType())){
				paymentRecord.setPayTool(PayTool.ZFB_SHH.name());
			}else if(CashierVersionEnum.WAP.name().equals(currentCashierVersion)) {
				//钱包支付，payTool可能为EWALLET或EWALLETH5。当payType为ALIPAY时，判定payTool为EWALLET，否则均为EWALLETH5
				if (PayTypeEnum.ALIPAY.name().equals(requestDto.getPayType())) { 
					paymentRecord.setPayTool(PayTool.EWALLET.name());
				}else {
					paymentRecord.setPayTool(PayTool.EWALLETH5.name());
				}
			}
		} catch (Exception e) {

		}
		return paymentRecord;
	}
	
	/*构建预路由请求参数*/
	private OpenPrePayRequestDTO buildPrePayRequestDTO(PaymentRequest paymentRequest, String type){
		OpenPrePayRequestDTO prePayRequestDTO = new OpenPrePayRequestDTO();
		ExtendInfoFromPayRequest extendInfo = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
		String basicProductCode = null;
		if(PayTypeEnum.ZFB_SHH.name().equals(type)){
			prePayRequestDTO.setPlatformType(PlatformType.ALIPAY.name());
			prePayRequestDTO.setAppId(extendInfo.getOrigAliAppId());
			basicProductCode = CommonUtil.getBasicProductCode(PayTool.ZFB_SHH.name(),paymentRequest.getTradeSysNo());
		}else if(PayTypeEnum.XCX_OFFLINE_ZF.name().equals(type)){
			prePayRequestDTO.setPlatformType(PlatformType.WECHAT.name());
			prePayRequestDTO.setAppId(extendInfo.getOrigAppId());
			basicProductCode = CommonUtil.getBasicProductCode(PayTool.XCX_OFFLINE_ZF.name(),paymentRequest.getTradeSysNo());
		}else{
			prePayRequestDTO.setAppId(extendInfo.getOrigAppId());
			// 雪平确认： 不传的话，走微信公众号预路由
			prePayRequestDTO.setPlatformType(PlatformType.WECHAT.name());
			basicProductCode = CommonUtil.getBasicProductCode(PayTool.WECHAT_OPENID.name(), paymentRequest.getTradeSysNo());
		}
		prePayRequestDTO.setRetailProductCode(extendInfo.getSaleProductCode());
		prePayRequestDTO.setBasicProductCode(basicProductCode);
		prePayRequestDTO.setDealUniqueSerialNo(paymentRequest.getTradeSysOrderId());
		prePayRequestDTO.setCustomerNumber(paymentRequest.getMerchantNo());
		prePayRequestDTO.setTotalAmount(paymentRequest.getOrderAmount());
		// 做个人会员充值的需求，影响范围：消费和充值，但是不要影响原来一键的微信公众号和支付宝标准版
		if(CommonUtils.isPayProcess(paymentRequest.getPaySysCode(),paymentRequest.getTradeSysNo())){
			prePayRequestDTO.setPayBusinessType(transferCardTypeToPayBusinessType(paymentRequest.getCardType()));
		}else{
			prePayRequestDTO.setPayBusinessType(PayBusinessType.DC);
		}
		prePayRequestDTO.setRequestSystem(PayProductCode.NCCASHIER);
//		modify by xueping.ni 来客需求传所有商户类型为V类
		prePayRequestDTO.setCustomerLevel(Constant.CUSTOMER_LEVEL_V);

		Map<String,String> extParam = new HashMap<String, String>();
		if(StringUtils.isNotBlank(extendInfo.getSpecifyChannelCodes())){
			extParam.put(Constant.SPECIFY_CHANNEL_CODES,extendInfo.getSpecifyChannelCodes());
		}
		if(StringUtils.isNotBlank(extendInfo.getReportFee())){
			extParam.put(Constant.REPORT_FEE,extendInfo.getReportFee());
		}
		//哆啦宝粉丝预路由公众号支付 reportId 透传
		//added by zengzhi.han 20181016 新增小程序支付 reportId
		if(PayTypeEnum.WECHAT_OPENID.name().equals(type)||PayTypeEnum.XCX_OFFLINE_ZF.name().equals(type)){
			extParam.put(Constant.WX_REPORT_ID,extendInfo.getReportId());
			extParam.put(Constant.WX_FOCUS_APP_ID,extendInfo.getFocusAppId());
		}
		extParam.put(Constant.PARENT_MERCHANT_NO,paymentRequest.getParentMerchantNo());
		prePayRequestDTO.setExtParam(extParam);
		return prePayRequestDTO;
	}
	//封装fe统一下单接口的参数
	private PayRequestDTO buildPayRequestDTO(PaymentRequest request,WeChatPayRequestDTO requestDto){
		PayRequestDTO payRequestDTO = new PayRequestDTO();
		payRequestDTO.setPayBusinessType(PayBusinessType.DC);
		payRequestDTO.setRequestSystem(PayProductCode.NCCASHIER);
		payRequestDTO.setRequestId(request.getTradeSysOrderId());
		payRequestDTO.setPlatformType(getFePlatformType(requestDto.getPayType()));
		payRequestDTO.setCustomerNumber(request.getMerchantNo());
		payRequestDTO.setOutTradeNo(request.getMerchantOrderId());
		payRequestDTO.setDealUniqueSerialNo(request.getTradeSysOrderId());
		payRequestDTO.setTotalAmount(request.getOrderAmount());
		payRequestDTO.setGoodsDescription(request.getProductName());
		payRequestDTO.setPayerIp(request.getUserIp());
		payRequestDTO.setPageCallBack(getCallBackUrl(requestDto.getTokenId(), request.getMerchantNo()));
		payRequestDTO.setOrderSystem("YJZF");
		payRequestDTO.setCustomerName(request.getMerchantName());
		payRequestDTO.setOrderType(getFeOrderType(requestDto.getPayType()));
		payRequestDTO.setOpenId(requestDto.getOpenId());
		payRequestDTO.setAppId(requestDto.getAppId());
		JSONObject jsonObject = CommonUtil.parseJson(request.getExtendInfo());
		payRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));
		payRequestDTO.setPayInterface(jsonObject.getString("payInterface"));
		payRequestDTO.setReportMerchantNo(jsonObject.getString("reportMerchantNo"));//二级商户号
		if(StringUtils.isNotBlank(jsonObject.getString("bankTotalCost"))) {
			payRequestDTO.setBankTotalCost(new BigDecimal(jsonObject.getString("bankTotalCost")));
		}
		ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest.getFromJson(request.getExtendInfo());
		//聚合支付透传姓名身份证号
		Map<String, String> h5ExtParam = new HashMap<String, String>();
		h5ExtParam.put("IDCardNo",extendInfoFromPayRequest.getIdCard());
		h5ExtParam.put("payerName",extendInfoFromPayRequest.getOwner());
		if(PayTypeEnum.WECHAT_H5_WAP.name().equals(requestDto.getPayType())){
			h5ExtParam.put("appName", extendInfoFromPayRequest.getAppName());
			h5ExtParam.put("platForm", extendInfoFromPayRequest.getPlatForm());
			h5ExtParam.put("appStatement", extendInfoFromPayRequest.getAppStatement());
		}
		payRequestDTO.setExtParam(h5ExtParam);
		return payRequestDTO;
	}

	/**
	 *构建开放支付的参数
	 * @param openPayRequestDTO
	 * @param request
	 * @param requestDto
	 */
	private void buildOpenPayRequestDTO(OpenPayRequestDTO openPayRequestDTO,PaymentRequest request,WeChatPayRequestDTO requestDto){
		//获取扩展信息
		ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest.getFromJson(request.getExtendInfo());
		if(extendInfoFromPayRequest!=null){
			openPayRequestDTO.setRetailProductCode(extendInfoFromPayRequest.getSaleProductCode());//零售产品码
			openPayRequestDTO.setPayInterface(extendInfoFromPayRequest.getPayInterface());//通道编码
			if(StringUtils.isNotBlank(extendInfoFromPayRequest.getBankTotalCost())) {//通道成本
				openPayRequestDTO.setBankTotalCost(new BigDecimal(extendInfoFromPayRequest.getBankTotalCost()));
			}
			openPayRequestDTO.setReportMerchantNo(extendInfoFromPayRequest.getReportMerchantNo());//二级商户号
		}
//		openPayRequestDTO.setPayBusinessType(PayBusinessType.DC);
		openPayRequestDTO.setPayBusinessType(transferCardTypeToPayBusinessType(request.getCardType()));
		openPayRequestDTO.setAppId(requestDto.getAppId());
		openPayRequestDTO.setOpenId(requestDto.getOpenId());
		openPayRequestDTO.setPlatformType(getFePlatformType(requestDto.getPayType()));
		openPayRequestDTO.setPayOrderType(getPayOrderType(requestDto.getPayType()));
		openPayRequestDTO.setWalletLevel(getWalletLevelInfo(requestDto.getPayType()));

		//聚合支付透传姓名身份证号、微信h5部分通道透传参数、报备费率等
		Map<String, String> h5ExtParam = new HashMap<String, String>();
		h5ExtParam.put("IDCardNo",extendInfoFromPayRequest.getIdCard());
		h5ExtParam.put("payerName",extendInfoFromPayRequest.getOwner());
		if(PayTypeEnum.WECHAT_H5_WAP.name().equals(requestDto.getPayType())){
			h5ExtParam.put("appName", extendInfoFromPayRequest.getAppName());
			h5ExtParam.put("platForm", extendInfoFromPayRequest.getPlatForm());
			h5ExtParam.put("appStatement", extendInfoFromPayRequest.getAppStatement());
		}
		if(StringUtils.isNotBlank(extendInfoFromPayRequest.getReportFee())){
			h5ExtParam.put(Constant.REPORT_FEE,extendInfoFromPayRequest.getReportFee());//报备费率，放在extParam传入PP
		}
		openPayRequestDTO.setExtParam(h5ExtParam);

	}
	
	/**
	 * 将cardType转为PayBusinessType
	 * 
	 * @param cardType
	 * @return
	 */
	private PayBusinessType transferCardTypeToPayBusinessType(String cardType) {
		if (CardTypeEnum.DEBIT.name().equals(cardType)) {
			return PayBusinessType.OD;
		}
		if (CardTypeEnum.CREDIT.name().equals(cardType)) {
			return PayBusinessType.OC;
		}
		return PayBusinessType.DC;
	}

	private String getWalletLevelInfo(String payType){
		String walletLevel = "";
		if(PayTypeEnum.WECHAT_H5_LOW.name().equals(payType)){
			walletLevel = Constant.WALLET_LEVEL_LOW; 
		}else if(PayTypeEnum.WECHAT_H5_WAP.name().equals(payType)){
			walletLevel = Constant.WALLET_LEVEL_HIGH;
		}else if(PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(payType)){ 
			walletLevel = Constant.WALLET_LEVEL_STANDARD;
		}else if(PayTypeEnum.ALIPAY_H5.name().equals(payType)){
			walletLevel = Constant.WALLET_LEVEL_LOW; // 产品确认ALIPAY_H5需要传LOW，ALIPAY不用传
		}
		return walletLevel;
	}
	private PlatformType getFePlatformType(String payType){
		if (PayTypeEnum.ALIPAY.name().equals(payType) || PayTypeEnum.ALIPAY_H5.name().equals(payType)
				|| PayTypeEnum.ZFB_SHH.name().equals(payType)
				|| PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(payType)) {
			return PlatformType.ALIPAY;
		} else if (PayTypeEnum.UPOP_ATIVE_SCAN.name().equals(payType)) {
			return PlatformType.OPEN_UPOP;
		} else if (PayTypeEnum.JD_H5.name().equals(payType) || PayTypeEnum.JD_ATIVE_SCAN.name().equals(payType)) {
			return PlatformType.JD;
		} else if (PayTypeEnum.QQ_ATIVE_SCAN.name().equals(payType)) {
			return PlatformType.QQ;
		} else {
			return PlatformType.WECHAT;
		}
	}

	private String getCallBackUrl(String token, String merchantNo){
		String urlPrefix = CommonUtil.getCashierUrlPrefix(merchantNo);
		String pageCallbackUrl = urlPrefix+"/wap/query/result?token="+token;
		return pageCallbackUrl;
	}

	private JsapiRouteResponseDTO buildJsapiRouteResponseDTO(OpenPrePayResponseDTO openPrePayResponseDTO){
		JsapiRouteResponseDTO jsapiRouteResponseDTO = new JsapiRouteResponseDTO();
		jsapiRouteResponseDTO.setAppId(openPrePayResponseDTO.getAppId());
		jsapiRouteResponseDTO.setAppSecret(openPrePayResponseDTO.getAppSecret());
		//modified by yangmin.peng 20170527获取预路由状态和场景扩展类型
		jsapiRouteResponseDTO.setDealStatus(Integer.toString(openPrePayResponseDTO.getDealStatus() == null ? 0 : openPrePayResponseDTO.getDealStatus()));
		jsapiRouteResponseDTO.setSceneTypeExt(openPrePayResponseDTO.getSceneTypeExt());
		return jsapiRouteResponseDTO;
	}

	private String setExtendInfo(String orgExtendInfo,OpenPrePayResponseDTO openPrePayResponseDTO, String type){
		JSONObject jsonObject = CommonUtil.parseJson(orgExtendInfo);
		if(PayTypeEnum.ZFB_SHH.name().equals(type)){
			jsonObject.put("targetAliAppId",openPrePayResponseDTO.getAppId());
			jsonObject.put("aliAppSecret", AESUtil.aesEncrypt(openPrePayResponseDTO.getAppSecret()));
		}else{
			jsonObject.put("targetAppId",openPrePayResponseDTO.getAppId());
			jsonObject.put("appSecret", AESUtil.aesEncrypt(openPrePayResponseDTO.getAppSecret()));
		}
		jsonObject.put("bankTotalCost",openPrePayResponseDTO.getBankTotalCost());//通道成本
		jsonObject.put("payInterface",openPrePayResponseDTO.getPayInterface());//通道编码
		jsonObject.put("reportMerchantNo",openPrePayResponseDTO.getReportMerchantNo());//二级商户号
		jsonObject.put("sceneTypeExt",openPrePayResponseDTO.getSceneTypeExt()); //jsapiH5 or normal
		return jsonObject.toJSONString();
	}

	private PayOrderType getPayOrderType(String payType){
		if (PayTypeEnum.isActiveScanPay(payType) || PayTypeEnum.ALIPAY_H5.name().equals(payType)) {
			return PayOrderType.ACTIVESCAN;
		} else if (PayTypeEnum.WECHAT_H5_WAP.name().equals(payType) || PayTypeEnum.JD_H5.name().equals(payType)) {
			return PayOrderType.H5APP;
		} else if (PayTypeEnum.WECHAT_OPENID.name().equals(payType)
				|| PayTypeEnum.WECHAT_H5_LOW.name().equals(payType)) {
			return PayOrderType.JSAPI;
		} else if (PayTypeEnum.ZFB_SHH.name().equals(payType)
				|| PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(payType)) {
			return PayOrderType.LN;
		}else if (PayTypeEnum.XCX_OFFLINE_ZF.name().equals(payType)) {
            return PayOrderType.MINI_PROGRAM;// 小程序支付(线下)
		}  else {
			return null;
		}
	}

	private OrderType getFeOrderType(String payType){
		if(PayTypeEnum.ALIPAY.name().equals(payType) || PayTypeEnum.WECHAT_ATIVE_SCAN.name().equals(payType) || PayTypeEnum.JD_ATIVE_SCAN.name().equals(payType)){
			return OrderType.ACTIVESCAN;
		}else if(PayTypeEnum.WECHAT_H5_WAP.name().equals(payType) || PayTypeEnum.JD_H5.name().equals(payType)) {
			return OrderType.H5APP;
		}else if(PayTypeEnum.WECHAT_OPENID.name().equals(payType) || PayTypeEnum.WECHAT_H5_LOW.name().equals(payType)){
			return OrderType.JSAPI;
		}else if(PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(payType)){
			return OrderType.LN; // 支付宝标准版
		}else if(PayTypeEnum.XCX_OFFLINE_ZF.name().equals(payType)){
			return OrderType.MINI_PROGRAM; // 小程序支付(线下)
		}else {
			return null;
		}
	}
}



