package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.CashierRouteBiz;
import com.yeepay.g3.core.nccashier.dao.UserRequestInfoDao;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.CashierBankCardService;
import com.yeepay.g3.core.nccashier.service.CashierCheckRequestInfoService;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.utils.AESUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.ExtendInfoFromPayRequest;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;
import com.yeepay.g3.core.nccashier.vo.TransparentCardInfo;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.DirectPayType;
import com.yeepay.g3.facade.nccashier.enumtype.PayRequestStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class CashierRouteBizImpl extends NcCashierBaseBizImpl implements CashierRouteBiz {

	@Resource
	private CashierBankCardService cashierBankCardService;
	
	@Resource
	private CashierCheckRequestInfoService cashierCheckRequestInfoService;

	@Resource
	private NewOrderHandleService newOrderHandleService;
	
	@Resource
	private UserRequestInfoDao userRequestInfoDao;
	
	@Resource
	private MerchantVerificationService merchantVerificationService;
	
	@Override
	public CashierResultDTO receiptRequest(CashierRequestDTO cashierRequestDTO)
			throws CashierBusinessException {
		CashierResultDTO cashierResultDTO = null;
		PaymentRequest paymentRequest = requestparamTransfer(cashierRequestDTO);// 封装请求参数
		long id = paymentRequestService.savePaymentRequest(paymentRequest);// 保存到数据库
		cashierResultDTO = resultParamTransfer(cashierRequestDTO, id, paymentRequest);// 封装返回参数
		return cashierResultDTO;
	}

	private PaymentRequest requestparamTransfer(CashierRequestDTO cashierRequestDTO) {
		PaymentRequest paymentRequest = new PaymentRequest();
		// app信息
		paymentRequest.setAppInfo(cashierRequestDTO.getAppInfo());
		// 地域信息
		paymentRequest.setAreaInfo(cashierRequestDTO.getAreaInfo());
		// 银行编码
		paymentRequest.setBankCode(cashierRequestDTO.getBankCode());
		// 业务模式编码
		paymentRequest.setBizModeCode(cashierRequestDTO.getBizModeCode());
		// 卡号
		if (StringUtils.isNotBlank(cashierRequestDTO.getCardNo())) {
			paymentRequest.setCardNo(cashierRequestDTO.getCardNo());
		}
		// 卡类型
		if (cashierRequestDTO.getCardType() != null) {
			paymentRequest.setCardType(cashierRequestDTO.getCardType().toString());
		}
		// 收银台版本
		if (null != cashierRequestDTO.getCashierVersion()) {
			paymentRequest.setCashierVersion(cashierRequestDTO.getCashierVersion().toString());
		}
		// payment_request创建日期
		paymentRequest.setCreateTime(new Date());
		// 订单系统的订单创建日期
		paymentRequest.setOrderTime(cashierRequestDTO.getOrderCreateDate());
		// 币种
		if (null != cashierRequestDTO.getCurrency()) {
			paymentRequest.setCurrency(cashierRequestDTO.getCurrency().toString());
		}
		// 证件号
		if (StringUtils.isNotBlank(cashierRequestDTO.getIdcard())) {
			paymentRequest.setIdCard(cashierRequestDTO.getIdcard());
		}
		// 证件类型
		if (null != cashierRequestDTO.getIdcardType()) {
			paymentRequest.setIdCardType(cashierRequestDTO.getIdcardType().toString());
		}
		// 用户标识
		paymentRequest.setIdentityId(cashierRequestDTO.getIdentityId());
		// 商品类别码
		if(StringUtils.isNotBlank(cashierRequestDTO.getGoodsCategoryCode())){
			paymentRequest.setIndustryCatalog(cashierRequestDTO.getGoodsCategoryCode());
		}else if(null!=cashierRequestDTO.getIndustryCatalog()){
			paymentRequest.setIndustryCatalog(cashierRequestDTO.getIndustryCatalog().getValue());
		}
		// 用户类型
		if (null != cashierRequestDTO.getIdentityType()) {
			paymentRequest.setIdentityType(cashierRequestDTO.getIdentityType().toString());
		}
		// 商户名称
		paymentRequest.setMerchantName(cashierRequestDTO.getMerchantName().length()>32?cashierRequestDTO.getMerchantName().substring(0, 32):cashierRequestDTO.getMerchantName());
		// 商户编号
		paymentRequest.setMerchantNo(cashierRequestDTO.getMerchantNo());
		// 商户订单号
		paymentRequest.setMerchantOrderId(cashierRequestDTO.getMerchantOrderId());
		paymentRequest.setFrontCallBackUrl(cashierRequestDTO.getFrontCallBackUrl());
		// 出错返回商户url
		paymentRequest.setMerchantOrderUrl(cashierRequestDTO.getMerchantOrderUrl());
		// 订单金额
		paymentRequest.setOrderAmount(cashierRequestDTO.getOrderAmount());
		// 订单有效期
		paymentRequest.setOrderExpDate(cashierRequestDTO.getOrderExpDate() <= 0 ? 24*60
				: cashierRequestDTO.getOrderExpDate());
		// 姓名
		if (StringUtils.isNotBlank(cashierRequestDTO.getOwner())) {
			paymentRequest.setOwner(cashierRequestDTO.getOwner());
		}
		// 支付时间
		paymentRequest.setPayTime(new Date());
		// 商品名称
		paymentRequest.setProductName(cashierRequestDTO.getProductName().length()>100?cashierRequestDTO.getProductName().substring(0, 100):cashierRequestDTO.getProductName());
		// 状态
		paymentRequest.setState(PayRequestStatusEnum.INIT.getValue());// 待支付
		// 终端信息
		paymentRequest.setTerminalInfo(cashierRequestDTO.getTerminalInfo());
		// 主题编码
		paymentRequest.setThemeCustomCode(cashierRequestDTO.getThemeCustomCode());
		// 交易风控信息
		paymentRequest.setTradeRiskInfo(cashierRequestDTO.getTradeRiskInfo());
		// 查询风控参数
		paymentRequest.setQueryRiskInfo(cashierRequestDTO.getQueryRiskInfo());
		// 交易系统编号
		paymentRequest.setTradeSysNo(cashierRequestDTO.getTradeSysNo());
		// 交易系统订单号
		paymentRequest.setTradeSysOrderId(cashierRequestDTO.getTradeSysOrderId());
		// 订单方订单号 收款宝订单号为空，将订单方订单号存为交易系统订单号
		if (StringUtils.isNotBlank(cashierRequestDTO.getOrderOrderId())) {
			paymentRequest.setOrderOrderId(cashierRequestDTO.getOrderOrderId());
		} else {
			paymentRequest.setOrderOrderId(cashierRequestDTO.getTradeSysOrderId());
		}

		// 订单方编码 收款宝订单号为空，将订单方编码存为交易系统编码
		if (StringUtils.isNotBlank(cashierRequestDTO.getOrderSysNo())) {
			paymentRequest.setOrderSysNo(cashierRequestDTO.getOrderSysNo());
		} else {
			paymentRequest.setOrderSysNo(cashierRequestDTO.getTradeSysNo());
		}

		// 手机号
		paymentRequest.setPhoneNo(cashierRequestDTO.getPhoneNo());
		// 更新时间
		paymentRequest.setUpdateTime(new Date());
		// 用户ip
		paymentRequest.setUserIp(cashierRequestDTO.getUserIp());
		// UA信息
		String ua = cashierRequestDTO.getUserUA();
		if (StringUtils.isNotEmpty(ua) && ua.length() > 150) {
			ua = ua.substring(0, 150);
		}
		paymentRequest.setUserUA(ua);
		// 版本
		paymentRequest.setVersion(1);
		// 备注
		paymentRequest.setRemark(cashierRequestDTO.getRemark());
		
		paymentRequest.setExtendInfo(cashierRequestDTO.getExtendInfo());
		//商户类型,默认商户类型为JOINLY联名账户
		if(StringUtils.isNotBlank(cashierRequestDTO.getMemberType())){
			paymentRequest.setMemberType(cashierRequestDTO.getMemberType());
		}else{
			paymentRequest.setMemberType(Constant.JOINLY);
		}
		//商户ID
		if(StringUtils.isNotBlank(cashierRequestDTO.getMemberNo())){
			paymentRequest.setMemberNo(cashierRequestDTO.getMemberNo());
		}
		paymentRequest.setMerchantConfigFrom(Constant.MERCHANT_CONFIG_FROM_PAYMENT_REQUEST);

		//从同一配置读取是否迁移   key:业务方编码，商编，default ；value：PAY_PROCCESOR
		paymentRequest.setPaySysCode(CommonUtil.getPaySysTypeByUniform(paymentRequest.getOrderSysNo(),paymentRequest.getMerchantNo()));

		return paymentRequest;
	}
	
	@Override
	public String getURL(String cashierVer, String merchantNo, long id) {
		logger.info(
				"[monitor],event:nccashier_receiptRequest_组装URL,cashierVer:{},merchantNo:{},requestId:{}",
				cashierVer, merchantNo, id);
		StringBuffer url = null; 
		String encId = null;
		try {
			String urlPrefix = CommonUtil.getCashierUrlPrefix(merchantNo);
			PayExtendInfo payExtendInfo = getPayExtendInfo(id,null);
			if (payExtendInfo.isSccanPay() || payExtendInfo.isInstallment()) {
				url = new StringBuffer(urlPrefix +CashierVersionEnum.WAP.name().toLowerCase()  + "/request/" + merchantNo + "/");
			} else {
				url = new StringBuffer(
						urlPrefix + cashierVer.toLowerCase() + "/request/" + merchantNo + "/");
			}
			encId = AESUtil.routeEncrypt(merchantNo, String.valueOf(id));
		} catch (UnsupportedEncodingException e) {
			logger.error("加密requestID组装异常", e);
			throw CommonUtil.handleException(Errors.ENCODE_ERROR);
		} catch (Throwable e) {
			logger.error("加密requestID组装返回URL异常", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		// 组装url
		url.append(encId);
		return url.toString();
	}


	private CashierResultDTO resultParamTransfer(CashierRequestDTO cashierRequestDTO, long id,
			PaymentRequest paymentRequest) {
		CashierResultDTO cashierResultDTO = new CashierResultDTO();
		String url = getURL(cashierRequestDTO.getCashierVersion().toString(),
				cashierRequestDTO.getMerchantNo(), paymentRequest);
		cashierResultDTO.setCashierCallURL(url);
		cashierResultDTO.setTradeSysOrderId(cashierRequestDTO.getTradeSysOrderId());
		cashierResultDTO.setTradeSysNo(cashierRequestDTO.getTradeSysNo());
		cashierResultDTO.setOrderOrderId(cashierRequestDTO.getOrderOrderId());
		cashierResultDTO.setOrderSysNo(cashierRequestDTO.getOrderSysNo());
		cashierResultDTO.setPaySysCode(paymentRequest.getPaySysCode());
		return cashierResultDTO;
	}

	private String getURL(String cashierVer, String merchantNo, PaymentRequest paymentRequest) {
		StringBuffer url = null;
		String encId = null;
		try {
			String urlPrefix = 	CommonUtil.getCashierUrlPrefix(merchantNo);
			PayExtendInfo payExtendInfo = new PayExtendInfo();
			merchantVerificationService.setPayExtendsInfoByJson(payExtendInfo,paymentRequest.getRemark(),paymentRequest.getExtendInfo(),paymentRequest.getOrderSysNo());
			payExtendInfo.setCashierVersion(paymentRequest.getCashierVersion());

			if (payExtendInfo.isSccanPay() || payExtendInfo.isInstallment()) {
				url = new StringBuffer(urlPrefix + CashierVersionEnum.WAP.name().toLowerCase()
						+ "/request/" + merchantNo + "/");
			} else {
				url = new StringBuffer(
						urlPrefix + cashierVer.toLowerCase() + "/request/" + merchantNo + "/");
			}
			encId = AESUtil.routeEncrypt(merchantNo, String.valueOf(paymentRequest.getId()));
		} catch (UnsupportedEncodingException e) {
			logger.error("加密requestID组装异常", e);
			throw CommonUtil.handleException(Errors.ENCODE_ERROR);
		} catch (Throwable e) {
			logger.error("加密requestID组装返回URL异常", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);

		}
		// 组装url
		url.append(encId);
		return url.toString();
	}

	
	@Override
	@Deprecated
	public String routeDecrypt(String merchantNo, String encRequestId) {
		String requestId;
		try {
			requestId = AESUtil.routeDecrypt(merchantNo, encRequestId);
		} catch (UnsupportedEncodingException e) {
			throw CommonUtil.handleException(Errors.DECODE_ERROR);
		}
		return requestId;
	}

	@Override
	public BussinessTypeResponseDTO routerPayWay(long requestId,String cusType) {
		BussinessTypeResponseDTO bussinessTypeResponseDTO = new BussinessTypeResponseDTO();
		try {
			if(requestId <=0){
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
			}
			bussinessTypeResponseDTO.setRequestId(requestId);
			NcCashierLoggerFactory.TAG_LOCAL.set("[路由|routerPayWay]—[requestId=" + requestId + "]");
			PaymentRequest paymentRequest =
					paymentRequestService.findPaymentRequestByRequestId(requestId);
			cashierCheckRequestInfoService.checkPassInfo(paymentRequest); //校验商户透传的同人信息
			// 监控埋点里没有看到这个日志，但是先保留着吧
			logger.info("[monitor],event:nccashier_payMethod_request,merchantOrderId:{},requestId:{},merchantNo:{}",
					paymentRequest.getMerchantOrderId(), requestId, paymentRequest.getMerchantNo());
			cashierBankCardService.getPayType(paymentRequest, bussinessTypeResponseDTO,cusType);
		} catch (Throwable e) {
			handleException(bussinessTypeResponseDTO, e);
		} finally {
			NcCashierLoggerFactory.TAG_LOCAL.set(null);
		}
		return bussinessTypeResponseDTO;
	}
	
	@Override
	public PayExtendInfo getPayExtendInfo(long requestId,String tokenId){
		NcCashierLoggerFactory.TAG_LOCAL.set("[获取支付扩展信息|getPayExtendInfo],tokenId="+tokenId+",paymentRequestId="+requestId);
		PaymentRequest paymentRequest = paymentRequestService.findPayRequestById(requestId);
		if(paymentRequest != null ){
			PayExtendInfo info = new PayExtendInfo();
			info.setIdentityType(paymentRequest.getIdentityType());
			info.setIdentityId(paymentRequest.getIdentityId());
			// 超时时间
			long expireDate = paymentRequestService.getExpiredTime(paymentRequest);
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			info.setExpireTime(dateFormater.format(new Date(expireDate)));
			merchantVerificationService.getMerchantInNetConfig(tokenId, paymentRequest, info);
			return info;
		}
		return null;
	}

	@Override
	public List<MerchantProductDTO> getNewMerchantInNetConfig(long requestId, String tokenId) {
		NcCashierLoggerFactory.TAG_LOCAL.set("[获取配置信息信息|tokenId="+tokenId+",paymentRequestId="+requestId);
		PaymentRequest paymentRequest = paymentRequestService.findPayRequestById(requestId);
		if(paymentRequest != null ){
			return merchantVerificationService.getNewMerchantInNetConfig(tokenId, paymentRequest);
		}
		return null;
	}

	@Override
	public NewOrderRequestResponseDTO newOrderRequest(String orderNo,int selectOrderSysNo) {
		NewOrderRequestResponseDTO response = new NewOrderRequestResponseDTO();
		response.setOrderNo(orderNo);
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[router-newOrderRequest]—[orderSysOrderNo=" + orderNo + ",orderSysNo="+selectOrderSysNo+"]");
			newOrderRequest(response,orderNo,selectOrderSysNo);
		} catch (Throwable e) {
			handleException(response, e);
		} finally {
			NcCashierLoggerFactory.TAG_LOCAL.set(null);
		}
		return response;
	}

	

	private void validateOrderProcessRequestParams(OrderProcessorRequestDTO requestDTO){
		if(requestDTO==null || StringUtils.isBlank(requestDTO.getToken())
				|| requestDTO.getCashierVersion() == null ){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public OrderProcessResponseDTO orderProcessRequest(
			OrderProcessorRequestDTO orderProcessorRequestDTO) {
		OrderProcessResponseDTO orderProcessResponseDTO = new OrderProcessResponseDTO();
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[orderProcessRequest]—[token=" + orderProcessorRequestDTO.getToken()+"]");
			validateOrderProcessRequestParams(orderProcessorRequestDTO);
			orderProcessorRequest(orderProcessResponseDTO, orderProcessorRequestDTO);
			return orderProcessResponseDTO;
		}catch (Throwable e){
			handleException(orderProcessResponseDTO,e);
			return orderProcessResponseDTO;
		}
	}
	
	
	
	
	/**
	 * paymentRequest下单处理(订单信息来自反查交易系统)
	 * @param response
	 * @param orderNo
	 * @throws UnsupportedEncodingException
	 */
	private void newOrderRequest(NewOrderRequestResponseDTO response,String orderNo,int selectOrderSysNo) throws UnsupportedEncodingException{
		OrderSysConfigDTO orderSysConfigDTO = CommonUtil.getBizSysCnfigParams(selectOrderSysNo == 2 ? CommonUtil.G2NET_OLD_KEY : CommonUtil.BIZ_SYS_CONFIG_DEFAULT_KEY);
		OrderDetailInfoModel orderInfo = orderInfoAccessAdapterService.getOrderDetailInfoModel(orderNo,orderSysConfigDTO);
		if(TransactionTypeEnum.PREAUTH == orderInfo.getTransactionType()){
			throw new CashierBusinessException(Errors.ORDER_TYPE_IS_PREAUTH);
		}
		PaymentRequest paymentRequest = paymentRequestService.findPayRequestByTradeSysOrderId(
				orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo());
		response.setUniqueOrderNo(orderInfo.getUniqueOrderNo());
		long id = 0;
		if(paymentRequest == null){
			paymentRequest = orderInfo.toPaymentRequest();
			newOrderHandleService.suppleConfigCenterInfoToPaymentRequest(paymentRequest,orderInfo);
			paymentRequest.setFee(newOrderHandleService.queryUserFee(paymentRequest,orderInfo.getCallFeeItem()));
			paymentRequest.setMerchantConfigFrom(Constant.MERCHANT_CONFIG_FROM_PAYMENT_REQUEST);
			try {
				id = paymentRequestService.savePaymentRequest(paymentRequest);
			} catch (CashierBusinessException e) {
				if(Errors.REPEAT_ORDER.getCode().equals(e.getDefineCode())){
					paymentRequest = paymentRequestService.findPayRequestByTradeSysOrderId(
							orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo());
					id = paymentRequest.getId();
				}else{
					throw e;
				}
			}
		}else{
			newOrderHandleService.suppleConfigCenterInfoToPaymentRequest(paymentRequest,orderInfo);
			paymentRequestService.updateUserRequestInfo(paymentRequest);

			id = paymentRequest.getId();
		}
		
		boolean isOutTime = paymentRequestService.isRequestExpired(paymentRequest);
		if(isOutTime){
			throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
		}
		
		response.setRequestId(id);
		response.setEncodeRequestId(AESUtil.routeEncrypt(orderInfo.getMerchantAccountCode(), String.valueOf(id)));
		response.setMerchantAccountCode(paymentRequest.getMerchantNo());
		response.setVersion(CashierVersionEnum.valueOf(paymentRequest.getCashierVersion()));
	}
	
	/**
	 * paymentRequest下单处理(订单信息来自反查交易系统+前端URL)
	 * @param responseDTO
	 * @param orderProcessorRequestDTO
	 */
	private void orderProcessorRequest(OrderProcessResponseDTO responseDTO, OrderProcessorRequestDTO orderProcessorRequestDTO){
		//获取业务方配置信息
		OrderSysConfigDTO orderSysConfigDTO = CommonUtil.getBizSysCnfigParams(orderProcessorRequestDTO.getBizType());
		OrderDetailInfoModel orderInfo = orderInfoAccessAdapterService.getOrderDetailInfoModel(orderProcessorRequestDTO.getToken(),orderSysConfigDTO);
		if(TransactionTypeEnum.PREAUTH == orderInfo.getTransactionType() && !PayTypeEnum.YSQ.name().equals(orderProcessorRequestDTO.getDirectPayType())){
			// 只有订单处理器才可能返回transactionType=预授权
			throw new CashierBusinessException(Errors.ORDER_TYPE_IS_PREAUTH);
		}
		newOrderHandleService.orderReferCheck(orderSysConfigDTO,orderInfo);
		supplyOrderDetailInfoModelFromOrderProcessorRequestDTO(orderInfo,orderProcessorRequestDTO);
		
		PaymentRequest paymentRequest = paymentRequestService.findPayRequestByTradeSysOrderId(
				orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo());
		long requestId = 0;
		String configResult;
		if(paymentRequest == null){
			paymentRequest = orderInfo.toPaymentRequest();
			if(StringUtils.isNotBlank(orderProcessorRequestDTO.getAppId())){
				paymentRequest.setAppID(orderProcessorRequestDTO.getAppId());
			}
			// add by meiling.zhuang：将openId放入paymentRequest中的extendInfo中
			paymentRequest.setOpenIdToExtendInfo(orderProcessorRequestDTO.getOpenId());
			supplyExtInfoFromOrderProcessorRequestDTO(paymentRequest, orderProcessorRequestDTO);
			paymentRequest.setMerchantConfigFrom(Constant.MERCHANT_CONFIG_FROM_USER_REQUEST);
			configResult = newOrderHandleService.suppleConfigCenterInfoToPaymentRequest(paymentRequest,orderInfo);
			paymentRequest.setFee(newOrderHandleService.queryUserFee(paymentRequest,orderInfo.getCallFeeItem()));
			try {
				requestId = paymentRequestService.savePaymentRequest(paymentRequest);
			} catch (CashierBusinessException e) {
				if(Errors.REPEAT_ORDER.getCode().equals(e.getDefineCode())){
					paymentRequest = paymentRequestService.findPayRequestByTradeSysOrderId(
							orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo());
				}else{
					throw e;
				}
			}
		}else {
			boolean isOutTime = paymentRequestService.isRequestExpired(paymentRequest);
			if(isOutTime){
				throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
			}
			configResult = newOrderHandleService.suppleConfigCenterInfoToPaymentRequest(paymentRequest,orderInfo);
			//更新用户请求信息
			updatePaymentRequest(paymentRequest, orderProcessorRequestDTO);
			requestId = paymentRequest.getId();
		}
		responseDTO.setConfigInfo(configResult);
		responseDTO.setRequestId(requestId);
		responseDTO.setUniqueOrderNo(orderInfo.getUniqueOrderNo());
		responseDTO.setMerchantNo(paymentRequest.getMerchantNo());
		try {
			responseDTO.setEncodeRequestId(AESUtil.routeEncrypt(orderInfo.getMerchantAccountCode(), String.valueOf(requestId)));
		}catch (Exception e){
			logger.error("编码requestId失败,requestId:"+paymentRequest.getId());
		}
	}
	
	/**
	 * 补充url链接的扩展参数
	 * 
	 * @param paymentRequest
	 * @param orderProcessorRequestDTO
	 */
	private void supplyExtInfoFromOrderProcessorRequestDTO(PaymentRequest paymentRequest,
			OrderProcessorRequestDTO orderProcessorRequestDTO) {
		ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest
				.getFromJson(paymentRequest.getExtendInfo());
		if (StringUtils.isNotBlank(orderProcessorRequestDTO.getAliAppId())) {
			extendInfoFromPayRequest.setOrigAliAppId(orderProcessorRequestDTO.getAliAppId());
		}
		if (StringUtils.isNotBlank(orderProcessorRequestDTO.getAliUserId())) {
			extendInfoFromPayRequest.setOrigAliUserId(orderProcessorRequestDTO.getAliUserId());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getAppName())){
			extendInfoFromPayRequest.setAppName(orderProcessorRequestDTO.getAppName());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getAppStatement())){
			extendInfoFromPayRequest.setAppStatement(orderProcessorRequestDTO.getAppStatement());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getPlatForm())){
			extendInfoFromPayRequest.setPlatForm(orderProcessorRequestDTO.getPlatForm());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getReportFee())){
			extendInfoFromPayRequest.setReportFee(orderProcessorRequestDTO.getReportFee());
		}
		paymentRequest.setExtendInfo(extendInfoFromPayRequest.toString());
	}
	
	/**
	 * 把前端URL里的参数补充到OrderDetailInfoModel
	 * @param orderInfo
	 * @param orderProcessorRequestDTO
	 */
	private void supplyOrderDetailInfoModelFromOrderProcessorRequestDTO(OrderDetailInfoModel orderInfo,
			OrderProcessorRequestDTO orderProcessorRequestDTO){
		if(!orderInfo.getSignedMerchantAccountCode().equals(orderProcessorRequestDTO.getMerchantNo())){
			//注：signedMerchantAccountCode，为订单处理器专门提供的用于校验商编的属性
			throw CommonUtil.handleException(Errors.REQUEST_MERCHANT_NO_ERROR);
		}
		// 反查订单的appId干掉了
		orderInfo.setAppId(orderProcessorRequestDTO.getAppId());
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getCardType())){
			if(orderInfo.getCardInfo()==null) {
				orderInfo.setCardInfo(new TransparentCardInfo());
			}
			orderInfo.getCardInfo().setCardType(CardTypeEnum.valueOf(orderProcessorRequestDTO.getCardType()));
		}
		orderInfo.setCashierVersion(orderProcessorRequestDTO.getCashierVersion());
		// TODO 若是链接中传了directPayType，那么反查订单时的directPayType会被覆盖
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getDirectPayType())) {
			orderInfo.setDirectPayType(DirectPayType.changeDirectTypeEnum(orderProcessorRequestDTO.getDirectPayType()));
		}
		// TODO 不管链接传没传userNo和userType，反查订单时的userNo和userType都会被覆盖
		if (MemberTypeEnum.YIBAO.name().equals(orderProcessorRequestDTO.getUserType())) {
			orderInfo.setMemberNo(orderProcessorRequestDTO.getUserNo());
			orderInfo.setMemberType("YIBAO");
		} else {
			orderInfo.setIdentityId(orderProcessorRequestDTO.getUserNo());
			orderInfo.setIdentityType(StringUtils.isNotBlank(orderProcessorRequestDTO.getUserType()) ? IdentityType.valueOf(orderProcessorRequestDTO.getUserType()):null);
		}
		orderInfo.setUserIp(orderProcessorRequestDTO.getUserIp());
	}
	

	
	private void updatePaymentRequest(PaymentRequest paymentRequest,OrderProcessorRequestDTO orderProcessorRequestDTO){
		PaymentRequest newPaymentRequest = new PaymentRequest();
		newPaymentRequest.setCashierVersion(orderProcessorRequestDTO.getCashierVersion().name());
		newPaymentRequest.setVersion(CashierVersionEnum.getVersionValue(orderProcessorRequestDTO.getCashierVersion()));
		
		String extendInfo = paymentRequest.getExtendInfo();
		ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest
				.getFromJson(extendInfo);
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getAppId())){
			extendInfoFromPayRequest.setOrigAppId( orderProcessorRequestDTO.getAppId());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getOpenId())){
			extendInfoFromPayRequest.setOrigOpenId(orderProcessorRequestDTO.getOpenId());
		}
		if (StringUtils.isNotBlank(orderProcessorRequestDTO.getAliAppId())) {
			extendInfoFromPayRequest.setOrigAliAppId(orderProcessorRequestDTO.getAliAppId());
		}
		if (StringUtils.isNotBlank(orderProcessorRequestDTO.getAliUserId())) {
			extendInfoFromPayRequest.setOrigAliUserId(orderProcessorRequestDTO.getAliUserId());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getAppName())){
			extendInfoFromPayRequest.setAppName(orderProcessorRequestDTO.getAppName());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getAppStatement())){
			extendInfoFromPayRequest.setAppStatement(orderProcessorRequestDTO.getAppStatement());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getPlatForm())){
			extendInfoFromPayRequest.setPlatForm(orderProcessorRequestDTO.getPlatForm());
		}
		if(StringUtils.isNotBlank(orderProcessorRequestDTO.getReportFee())){
			extendInfoFromPayRequest.setReportFee(orderProcessorRequestDTO.getReportFee());
		}
		//ncPay支付场景
		if(StringUtils.isNotEmpty(paymentRequest.getBizModeCode())){
			newPaymentRequest.setBizModeCode(paymentRequest.getBizModeCode());
		}
		//网银支付场景
		if(StringUtils.isNotEmpty(paymentRequest.getEbankPayScene())){
			newPaymentRequest.setEbankPayScene(paymentRequest.getEbankPayScene());
		}
		newPaymentRequest.setExtendInfo(extendInfoFromPayRequest.toString());
		newPaymentRequest.setId(paymentRequest.getId());
		newPaymentRequest.setRemark(paymentRequest.getRemark());
		newPaymentRequest.setUpdateTime(new Date());
		newPaymentRequest.setIdentityId(orderProcessorRequestDTO.getUserNo());
		newPaymentRequest.setIdentityType(orderProcessorRequestDTO.getUserType());
		newPaymentRequest.setCardType(orderProcessorRequestDTO.getCardType());
		logger.info("更新用户请求信息,paymentRequest:"+newPaymentRequest.toString());

		paymentRequestService.updateUserRequestInfo(newPaymentRequest);
	}
}
