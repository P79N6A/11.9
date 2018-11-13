package com.yeepay.g3.core.nccashier.service.impl.api;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.core.nccashier.log.CashierTracer;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.ApiCashierRouterUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.param.ConfigAuthPayParam;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.BasicRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.BasicResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcSmsResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API收银台--基础模板
 * Created by ruiyang.du on 2017/6/28.
 */
public abstract class APICashierPaymentBaseTemplate extends NcCashierBaseService {


    @Resource
    protected OrderInfoAccessService orderInfoAccessAdapterService;
    @Resource
    protected NewOrderHandleService newOrderHandleService;
    @Resource
    protected MerchantVerificationService merchantVerificationService;
    @Resource
    protected PaymentRequestService paymentRequestService;
    @Resource
    protected PaymentProcessService paymentProcessService;
    @Resource
    protected CashierBankCardService cashierBankCardService;
    @Autowired
    private APICashierYJZFService apiCashierYJZFService;
    @Autowired
    private NcConfigService ncConfigService;

    /**
     * 开放及被扫支付(主扫、公众号、被扫) - 支付过程
     * @param apiCashierRequestDTO
     * @return
     */
    protected UnifiedAPICashierResponseDTO doOpenAndPassivePay(UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        //参数校验
        paramValidate(apiCashierRequestDTO);
        //反查交易系统，获取订单信息
        OrderDetailInfoModel orderInfo = queryOrderInfo(apiCashierRequestDTO.getToken(), apiCashierRequestDTO.getBizType());
		if (CommonUtil.enableSpanLog()) {
			MonitorLogSpanInfo monitorLogSpanInfo = new MonitorLogSpanInfo();
			monitorLogSpanInfo.setBizSys(orderInfo.getOrderSysNo());
			monitorLogSpanInfo.setEvent("apiFusionPaySuccessRate");//不好复用，可以考虑改名字
			monitorLogSpanInfo.setMerchantNo(orderInfo.getMerchantAccountCode());
			monitorLogSpanInfo.setMerchantOrderNo(orderInfo.getMerchantOrderId());
			PayOrderType payOrderType = getPayOrderType(apiCashierRequestDTO.getPayType());
			monitorLogSpanInfo.setPayTool(payOrderType==null?"":payOrderType.name());
			PlatformType platformType = getFePlatformType(apiCashierRequestDTO.getPayType());
			monitorLogSpanInfo.setPlatform(platformType == null ? "" : platformType.name());
			monitorLogSpanInfo.setStatus("0");// 下单
			CashierTracer.apiFusionPayRequestSpan(monitorLogSpanInfo);
		}
        //订单信息校验，含支付方式校验
        MerchantInNetConfigResult merchantInNetConfigResult = orderInfoValidate(apiCashierRequestDTO.getMerchantNo(),apiCashierRequestDTO.getPayTool(),apiCashierRequestDTO.getPayType(), orderInfo);
        //处理pay_request
        PaymentRequest paymentRequest = handlePaymentRequest(orderInfo, merchantInNetConfigResult, apiCashierRequestDTO,null);
        //预路由（仅公众号支付、生活号支付、小程序支付）
        if (PayTypeEnum.WECHAT_OPENID.name().equals(apiCashierRequestDTO.getPayType()) || PayTypeEnum.ZFB_SHH.name().equals(apiCashierRequestDTO.getPayType())
                || PayTypeEnum.XCX_OFFLINE_ZF.name().equals(apiCashierRequestDTO.getPayType())) {
            wechatPreRouter(paymentRequest, apiCashierRequestDTO);
        }
        //处理pay_record
        PaymentRecord paymentRecord = handlePaymentRecord(paymentRequest, apiCashierRequestDTO, apiCashierRequestDTO.getPayTool(), apiCashierRequestDTO.getPayType(),null);
        //尝试从缓存中获取PP支付结果，如有缓存，接口直接返回
        UnifiedAPICashierResponseDTO responseFromCache = getAPIResponseFromCache(paymentRecord,UnifiedAPICashierResponseDTO.class);
        if (responseFromCache != null) {
            return responseFromCache;
        }
        //调用PP前的参数处理逻辑（含paymentRecord状态校验）
        BasicRequestDTO processorRequestDTO = prepareToCallPayProcessor(paymentRecord, apiCashierRequestDTO, paymentRequest);
        //调用PP下单
        BasicResponseDTO openPayResponseDTO = requestPayProcessorPay(processorRequestDTO);
        //将支付结果保存到pay_record
        updatePaymentRecord(openPayResponseDTO, paymentRecord);
        //封装返回结果
        return buildResponse(openPayResponseDTO, apiCashierRequestDTO, paymentRequest, paymentRecord);
    }

    /**
     * 绑卡支付 - 请求下单并获取补充项
     * @param needItemRequestDTO
     * @return
     */
    protected ApiBindPayPaymentResponseDTO doRequestPayment(ApiBindPayPaymentRequestDTO needItemRequestDTO) {
        //参数校验
        paramValidate(needItemRequestDTO);
        //反查交易系统，获取订单信息
        OrderDetailInfoModel orderInfo = queryOrderInfo(needItemRequestDTO.getToken(), needItemRequestDTO.getBizType());
        //订单信息校验，含支付方式校验
        MerchantInNetConfigResult merchantInNetConfigResult = orderInfoValidate(needItemRequestDTO.getMerchantNo(),PayTool.BK_ZF.name(),PayTypeEnum.BK_ZF.name(), orderInfo);

        //获取外部用户id或memberNo
        String payMerchantNo = orderInfo.getMerchantAccountCode();
        if (StringUtils.isNotBlank(needItemRequestDTO.getPayMerchantNo())){
            //校验只有易宝三代会员才能调用跨商编支付
            if (!MemberTypeEnum.YIBAO.name().equals(needItemRequestDTO.getUserType())){
                throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，userType有误");
            }

            //校验是否开通授权支付
            ConfigAuthPayParam configAuthPayParam = new ConfigAuthPayParam();
            configAuthPayParam.setPayMerchantNo(needItemRequestDTO.getPayMerchantNo());
            configAuthPayParam.setParentMerchantNo(orderInfo.getParentMerchantNo());
            ncConfigService.hasAuthPay(configAuthPayParam);

            payMerchantNo = needItemRequestDTO.getPayMerchantNo();
        }

        CashierUserInfo cashierUser = apiCashierYJZFService.buildMemberUser(needItemRequestDTO.getUserNo(), needItemRequestDTO.getUserType(), payMerchantNo);

        //处理pay_request
        PaymentRequest paymentRequest = handlePaymentRequest(orderInfo, merchantInNetConfigResult, needItemRequestDTO, cashierUser);
        //查询外部用户，并记录外部用户ID等
        Map<String, Object> cardInfoMap = queryCardInfo(paymentRequest, needItemRequestDTO.getBindId(), cashierUser);
        //处理pay_record
        // TODO: 2018/9/4 支付记录中扩展参数没有存 reportId 
        PaymentRecord paymentRecord = handlePaymentRecord(paymentRequest, cardInfoMap, PayTool.BK_ZF.name(), CardInfoTypeEnum.BIND.name(),needItemRequestDTO.getPayMerchantNo());

        //尝试从缓存中获取PP支付结果，如有缓存，接口直接返回
        ApiBindPayPaymentResponseDTO responseFromCache = getAPIResponseFromCache(paymentRecord,ApiBindPayPaymentResponseDTO.class);
        if (responseFromCache != null) {
            responseFromCache.setRecordId(String.valueOf(paymentRecord.getId()));
            return responseFromCache;
        }
        //调用PP前的参数处理逻辑
        BasicRequestDTO processorRequestDTO = prepareToCallPayProcessor(paymentRecord, needItemRequestDTO, paymentRequest);
        //调用PP下单
        BasicResponseDTO openPayResponseDTO = requestPayProcessorPay(processorRequestDTO);
        //将支付结果保存到pay_record
        updatePaymentRecord(openPayResponseDTO, paymentRecord);
        //封装返回结果
        return buildResponse(openPayResponseDTO, needItemRequestDTO, paymentRequest, paymentRecord);
    }

    /**
     * 绑卡支付 - 请求发送短验
     * @param sendSmsRequestDTO
     * @return
     */
    protected ApiBindPaySendSmsResponseDTO doRequestSmsSend(ApiBindPaySendSmsRequestDTO sendSmsRequestDTO) {
        //参数校验
        requestSmsParamValidate(sendSmsRequestDTO);
        //反查交易系统，获取订单信息
        OrderDetailInfoModel orderInfo = queryOrderInfo(sendSmsRequestDTO.getToken(), sendSmsRequestDTO.getBizType());
        //查询pay_request
        PaymentRequest paymentRequest = queryPaymentRequest(orderInfo);
        if (paymentRequest == null) {
            throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
        }
        //查询pay_record
        PaymentRecord paymentRecord = queryPaymentRecord(paymentRequest,CardInfoTypeEnum.BIND.name());
        if (paymentRecord == null || StringUtils.isBlank(paymentRecord.getPaymentOrderNo())) {
            throw CommonUtil.handleException(Errors.PAY_RECORD_NULL);
        }
        //保存临时卡，调用PP请求发短验
        saveTempCardAndRequestSMS(paymentRequest, paymentRecord, sendSmsRequestDTO);
        //封装返回
        ApiBindPaySendSmsResponseDTO bindPaySendSmsResponse = buildSendSMSResponse(sendSmsRequestDTO);
        bindPaySendSmsResponse.setRecordId(String.valueOf(paymentRecord.getId()));
        return bindPaySendSmsResponse;
    }

    /**
     * 绑卡支付 - 确认支付
     * @param confirmPayRequestDTO
     * @return
     */
    protected ApiBindPayConfirmResponseDTO doConfirmPayment(ApiBindPayConfirmRequestDTO confirmPayRequestDTO) {
        //参数校验
        confirmPayParamValidate(confirmPayRequestDTO);
        //反查交易系统，获取订单信息
        OrderDetailInfoModel orderInfo = queryOrderInfo(confirmPayRequestDTO.getToken(), confirmPayRequestDTO.getBizType());
        //查询pay_request
        PaymentRequest paymentRequest = queryPaymentRequest(orderInfo);
        if (paymentRequest == null) {
            throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
        }
        //查询pay_record
        PaymentRecord paymentRecord = queryPaymentRecord(paymentRequest,CardInfoTypeEnum.BIND.name());
        if (paymentRecord == null || StringUtils.isEmpty(paymentRecord.getPaymentOrderNo())) {
            throw CommonUtil.handleException(Errors.PAY_RECORD_NULL);
        }
        if (PayRecordStatusEnum.FAILED == paymentRecord.getState()) {
            throw CommonUtil.handleException(Errors.THRANS_FINISHED);
        }
        //调用PP校验验证码，保存临时卡，调用PP确认绑卡支付，更新信息到record
        handleVerifyCodeAndComfirmPay(paymentRecord, paymentRequest, confirmPayRequestDTO);
        //封装返回
        ApiBindPayConfirmResponseDTO bindPayConfirmResponseDTO = buildComfirmBindedPayResponse(paymentRecord, confirmPayRequestDTO);
        bindPayConfirmResponseDTO.setRecordId(String.valueOf(paymentRecord.getId()));
        return bindPayConfirmResponseDTO;
    }

    /**
     * 开放及被扫支付 - 参数校验 - 需各支付方式具体实现
     *
     * @param apiCashierRequestDTO API接口入参
     * @throws YeepayBizException 如果参数校验不通过
     */
    protected abstract <T> void paramValidate(T apiCashierRequestDTO) throws YeepayBizException;

    /**
     * 尝试根据recordId从缓存中获取接口结果，如有缓存，接口直接返回
     *
     * @param paymentRecord
     * @return
     */
    protected <T> T getAPIResponseFromCache(PaymentRecord paymentRecord,Class<T> clazz) {
        if (paymentRecord == null || paymentRecord.getId() == null) {
            return null;
        }
        T payProcessorResult = RedisTemplate.getTargetFromRedis(Constant.NCCASHIER_API_PAY_RESULT_KEY + paymentRecord.getId(), clazz);
        if (payProcessorResult != null) {
            return payProcessorResult;
        }
        return null;
    }

    /**
     * 将api接口返回结果保存到redis
     *
     * @param reponseDTO
     */
    protected <T> void saveAPIResponseIntoCache(T reponseDTO, Long recordId) {
        if (reponseDTO == null) {
            return;
        }
        int expireTime;
        String expireTimeConfig = CommonUtil.getSysConfigFrom3G(Constant.NCCASHIER_API_PAY_EXPIRE_CONFIG_KEY);
        if (StringUtils.isEmpty(expireTimeConfig)) {
            expireTime = Constant.NCCASHIER_API_PAY_RESULT_EXPIRE;
        } else {
            expireTime = Integer.parseInt(expireTimeConfig);
        }
        RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_API_PAY_RESULT_KEY + recordId, reponseDTO, expireTime * 1000);
    }

    /**
     * 开放及被扫支付 - 封装返回结果 - 需要子类在调用buildBasicResponse的基础上，各自实现专用逻辑
     *
     * @param payProcessorResponse PP返回结果
     * @param apiRequest           API接入入参
     * @param paymentRequest       收银台支付请求
     * @param paymentRecord        收银台支付记录
     */
    protected abstract <P extends BasicResponseDTO, A, O> O buildResponse(P payProcessorResponse, A apiRequest, PaymentRequest paymentRequest, PaymentRecord paymentRecord) ;

    /**
     * 开放及被扫支付 - 封装通用返回结果
     *
     * @param apiCashierRequestDTO api接口入参
     */
    protected UnifiedAPICashierResponseDTO buildBasicResponse(UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        UnifiedAPICashierResponseDTO cashierReponseDTO = new UnifiedAPICashierResponseDTO();
        cashierReponseDTO.setCode(APICashierPayResultEnum.SUCCESS.getCode());
        cashierReponseDTO.setMessage(APICashierPayResultEnum.SUCCESS.getMessage());
        cashierReponseDTO.setMerchantNo(apiCashierRequestDTO.getMerchantNo());
        cashierReponseDTO.setToken(apiCashierRequestDTO.getToken());
        cashierReponseDTO.setPayTool(ApiCashierRouterUtil.payToolResponseRoute(apiCashierRequestDTO.getPayTool()));
        cashierReponseDTO.setPayType(ApiCashierRouterUtil.payTypeResponseRoute(apiCashierRequestDTO.getPayType()));
        return cashierReponseDTO;
    }

    /**
     * 调用PP前的参数处理逻辑 - 需要子类在调用buildCommonRequestDTO的基础上，各自实现专用逻辑
     *
     * @param paymentRecord     支付记录
     * @param apiRequest        API接口入参
     * @param paymentRequest    支付请求
     * @return PP支付接口入参
     */
    protected abstract <O extends BasicRequestDTO,I> O prepareToCallPayProcessor(PaymentRecord paymentRecord, I apiRequest, PaymentRequest paymentRequest);


    /**
     * 调用PP前的通用参数处理逻辑
     * @param paymentRecord
     * @param paymentRequest
     * @param payProcessorRequestDTO
     * @param userIp 用于封装风控参数
     */
    protected void buildCommonRequestDTO(PaymentRecord paymentRecord, PaymentRequest paymentRequest, BasicRequestDTO payProcessorRequestDTO,String userIp) {
        buildBasicRequestDTO(paymentRequest, payProcessorRequestDTO);
        String riskInfo = buildTradeRiskInfoByTooluseripAndRequest(userIp, paymentRequest);
        payProcessorRequestDTO.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
        payProcessorRequestDTO.setPayProduct(paymentRecord.getPayTool());
        payProcessorRequestDTO.setGoodsInfo(riskInfo);
    }

    /**
     * 调用PP支付下单
     *
     * @param requestDTO PP支付入参
     * @return PP支付结果
     */
    protected abstract <I extends BasicRequestDTO, O extends BasicResponseDTO> O requestPayProcessorPay(I requestDTO);

    /**
     * 将支付结果保存到pay_record
     * <p>注：基础实现。如被扫支付等需根据业务重写此方法。
     *
     * @param record
     * @return
     */
    protected <O extends BasicResponseDTO> void updatePaymentRecord(O response, PaymentRecord record) {
        if (response != null && response.getProcessStatus() == ProcessStatus.SUCCESS) {
            record.setPaymentOrderNo(response.getRecordNo());
            record.setState(PayRecordStatusEnum.PAYING);
            paymentProcessService.updateRecord(record);
        }
    }


    /**
     * 反查交易系统，获取订单信息，及支付方式校验（不走缓存，且一定获取完整订单信息）
     *
     * @param token
     * @param bizType
     * @return
     */
    protected OrderDetailInfoModel queryOrderInfo(String token, String bizType) {
        //获取业务方配置信息
        OrderSysConfigDTO configDTO = CommonUtil.getBizSysCnfigParams(bizType);
        // 反查订单处理器订单信息
        OrderDetailInfoModel orderInfo = orderInfoAccessAdapterService.getOrderDetailInfoModel(token, configDTO);
        //订单reffer检查
        newOrderHandleService.orderReferCheck(configDTO, orderInfo);
        // 校验订单类型
        if(TransactionTypeEnum.PREAUTH == orderInfo.getTransactionType()){
        		throw new CashierBusinessException(Errors.ORDER_TYPE_IS_PREAUTH);
        }
        return orderInfo;
    }


    /**
     * 处理pay_request(注：此基类实现目前共开放支付、被扫支付使用，绑卡支付已单独在子类实现)
     *
     * @param orderInfo           反查到的订单信息
     * @param merchantInNetConfig 商户入网配置
     * @param apiCashierRequest   API接口-请求入参
     * @param userInfo
     * @return 已有的，或新建的pay_request
     */
    protected <T> PaymentRequest handlePaymentRequest(OrderDetailInfoModel orderInfo, MerchantInNetConfigResult merchantInNetConfig, T apiCashierRequest, CashierUserInfo userInfo){
        if (orderInfo == null || merchantInNetConfig == null || apiCashierRequest == null) {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        UnifiedAPICashierRequestDTO requestDTO = (UnifiedAPICashierRequestDTO)apiCashierRequest;
        //1，查询paymentRequest是否已存在
        PaymentRequest paymentRequest = queryPaymentRequest(orderInfo);
        if(paymentRequest!=null){
            //开放支付特有逻辑：校验pay_request是否需要变更
            boolean change = supplyPayRequestInfo(paymentRequest, requestDTO);
            if(change){
                paymentRequestService.updatePayRequestExtendInfoById(paymentRequest);
            }
            //paymentRequest存在，直接返回
            return paymentRequest;
        }
        //2.2 paymentRequest不存在，新建
        //2.2.1 订单等基本信息
        orderInfoComplate(orderInfo, requestDTO);
        paymentRequest = orderInfo.toPaymentRequest();
        //2.2.2 支付方式开通信息
        paymentRequest.setMerchantConfigFrom(Constant.MERCHANT_CONFIG_FROM_PAYMENT_REQUEST);
        paymentRequest.setIndustryCatalog(merchantInNetConfig.getMcc());
        paymentRequest.setFee(newOrderHandleService.queryUserFee(paymentRequest, orderInfo.getCallFeeItem()));
        paymentRequest.setRemark(buildRemark(merchantInNetConfig));

        //2.2.3 公众号支付特有信息： appId，openId等
        if (StringUtils.isNotBlank(requestDTO.getAppId())) {
            paymentRequest.setAppID(requestDTO.getAppId());
        }
        supplyPayRequestInfo(paymentRequest, requestDTO);
        try {
            long requestId = paymentRequestService.savePaymentRequest(paymentRequest);
            paymentRequest.setId(requestId);
            return paymentRequest;
        } catch (CashierBusinessException e) {
            if (Errors.REPEAT_ORDER.getCode().equals(e.getDefineCode())) {
                paymentRequest = paymentRequestService.findPayRequestByTradeSysOrderId(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo());
                return paymentRequest;
            } else {
                throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
            }
        }
    }


    /**
     * 根据订单信息查询paymentRequest
     * @param orderInfo
     * @return
     */
    protected PaymentRequest queryPaymentRequest(OrderDetailInfoModel orderInfo){
        //1，查询paymentRequest是否已存在
        PaymentRequest paymentRequest = paymentRequestService.findPayRequestByTradeSysOrderId(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo());
        if (paymentRequest == null) {
            return null;
        }
        //2.1 paymentRequest已存在，若超时则报错，否则返回
        if (paymentRequestService.isRequestExpired(paymentRequest)) {
            throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
        }
        return paymentRequest;
    }

    /**
     * 处理pay_record
     *
     * @param paymentRequest    支付请求
     * @param apiCashierRequest API接口入参-开放或被扫支付-支付请求
     * @param payTool           支付工具
     * @param payType           支付方式
     * @return 已有的，或新建的pay_record
     */
    protected abstract <T> PaymentRecord handlePaymentRecord(PaymentRequest paymentRequest, T apiCashierRequest, String payTool, String payType,String payMerchantNo);

    /**
     * 根据paymentRequest查询paymentRecord
     *
     * @param paymentRequest
     * @param payType
     * @return
     */
    protected PaymentRecord queryPaymentRecord(PaymentRequest paymentRequest,String payType){
        PaymentRecord paymentRecord = null;
        List<PaymentRecord> paymentRecords = paymentProcessService.findRecordList(paymentRequest.getTradeSysOrderId(), paymentRequest.getTradeSysNo());
        List<PaymentRecord> paymentRecordsForPayType = new ArrayList<PaymentRecord>();
        if (CollectionUtils.isNotEmpty(paymentRecords)) {
            for (PaymentRecord record : paymentRecords) {
                if (PayRecordStatusEnum.SUCCESS == record.getState()) {
                    throw CommonUtil.handleException(Errors.THRANS_FINISHED);
                }
                if (payType.equals(record.getPayType())) {
                    paymentRecordsForPayType.add(record);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(paymentRecordsForPayType)) {
            //匹配当前payType的pay_record集合非空，取第一个（创建时间最新的）
            paymentRecord = paymentRecordsForPayType.get(0);
        }
        return paymentRecord;
    }
    /**
     * 预路由--微信公众号支付（支付宝生活号、微信小程序）逻辑
     *
     * @param paymentRequest
     * @return
     */
    protected void wechatPreRouter(PaymentRequest paymentRequest, UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
		// 优先尝试走jsapi通道
		JsapiRouteResponseDTO jsapiRouteResponseDTO = null;
		try {
			jsapiRouteResponseDTO = weChatOrderPaymentService.weChatPreRoute(paymentRequest,
					apiCashierRequestDTO.getPayType());
		} catch (Throwable t) {
			// 预路由失败，报错返回
			throw CommonUtil.handleException(Errors.NO_PAYTYPE_AVAILABLE_ERROR);
		}

		if (jsapiRouteResponseDTO == null
				|| !Constant.PRE_ROUTE_STATUS_SUCCESS.equals(jsapiRouteResponseDTO.getDealStatus())) {
			// 预路由失败，报错返回
			throw CommonUtil.handleException(Errors.NO_PAYTYPE_AVAILABLE_ERROR);
		}

		if (PayTypeEnum.ZFB_SHH.name().equals(apiCashierRequestDTO.getPayType())) {
			return;
		}

		if (PayTypeEnum.WECHAT_OPENID.name().equals(apiCashierRequestDTO.getPayType()) || PayTypeEnum.XCX_OFFLINE_ZF.name().equals(apiCashierRequestDTO.getPayType())) {
			if (Constant.PRE_ROUTE_SCENE_TYPE_EXT_JSAPIH5.equals(jsapiRouteResponseDTO.getSceneTypeExt())) {
				// 可以走jsapi，无需appId和openId
				return;
			}
			if (StringUtils.isNotBlank(jsapiRouteResponseDTO.getAppId())
					&& jsapiRouteResponseDTO.getAppId().equals(apiCashierRequestDTO.getAppId())
					&& StringUtils.isNotBlank(apiCashierRequestDTO.getOpenId())) {
				// jsapi失败，走原生通道，判断下appId、openId
				// 预appId=透传的appId，且有透传openId, 直接走报备通道（走normal通道的公众号支付）
				return;
			}
		}

		// jsapi通道不可用，原生公众号（生活号、小程序）通道均不可用
		throw CommonUtil.handleException(Errors.NO_PAYTYPE_AVAILABLE_ERROR);
    }

    /**
     * 订单信息校验，并返回开通配置信息
     *
     * @param merchantNo
     * @param payTool
     * @param payType
     * @param orderInfo            订单信息
     */
    protected MerchantInNetConfigResult orderInfoValidate(String merchantNo, String payTool, String payType, OrderDetailInfoModel orderInfo) {
        if (StringUtils.isBlank(merchantNo) || StringUtils.isBlank(payTool) || StringUtils.isBlank(payType) || orderInfo == null) {
            // 这里走不到吧
        		throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        //1，商编校验
        if (!merchantNo.equals(orderInfo.getSignedMerchantAccountCode())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，token与商编不匹配");
        }
        //2，开通产品校验
		VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
		requestParam.setMerchantNo(orderInfo.getMerchantAccountCode());
		requestParam.setProductLevel(
				new ProductLevel(CashierVersionEnum.API, PayTool.valueOf(payTool), PayTypeEnum.valueOf(payType)));
		requestParam.setTransactionType(orderInfo.getTransactionType());
		MerchantInNetConfigResult merchantInNetConfigResult = merchantVerificationService.verifyMerchantAuthority(requestParam);
        return merchantInNetConfigResult;
    }

    /**
     * 调用PP前的参数处理逻辑--生成PlatformType
     *
     * @param payType
     * @return
     */
    protected PlatformType getFePlatformType(String payType) {
        if (PayTypeEnum.ALIPAY.name().equals(payType) || PayTypeEnum.ALIPAY_SCAN.name().equals(payType)
                || PayTypeEnum.ZFB_SHH.name().equals(payType) || PayTypeEnum.ALIPAY_SDK.name().equals(payType)) {
            return PlatformType.ALIPAY;
        } else if (PayTypeEnum.UPOP_ATIVE_SCAN.name().equals(payType) || PayTypeEnum.UPOP_PASSIVE_SCAN.name().equals(payType)) {
            return PlatformType.OPEN_UPOP;
        } else if (PayTypeEnum.WECHAT_SCAN.name().equals(payType) || PayTypeEnum.WECHAT_ATIVE_SCAN.name().equals(payType)
                || PayTypeEnum.WECHAT_OPENID.name().equals(payType) || PayTypeEnum.WECHAT_SDK.name().equals(payType) || PayTypeEnum.XCX_OFFLINE_ZF.name().equals(payType)) {
            return PlatformType.WECHAT;
        } else if (PayTypeEnum.JD_H5.name().equals(payType) || PayTypeEnum.JD_ATIVE_SCAN.name().equals(payType) || PayTypeEnum.JD_PASSIVE_SCAN.name().equals(payType)) {
            return PlatformType.JD;
        } else if(PayTypeEnum.QQ_ATIVE_SCAN.name().equals(payType) || PayTypeEnum.QQ_PASSIVE_SCAN.name().equals(payType)){
            return PlatformType.QQ;
        }
        else {
            return null;
        }
    }

    /**
     * 调用PP前的参数处理逻辑--根据payType生成PayOrderType
     * <p>注意：此方法只适用于API收银台openPay支付（主扫、公众号）前；被扫支付时统一传PASSIVESCAN
     *
     * @param payType
     * @return
     */
    protected PayOrderType getPayOrderType(String payType) {
        if (PayTypeEnum.isActiveScanPay(payType)) {
            return PayOrderType.ACTIVESCAN;
        } else if (PayTypeEnum.WECHAT_H5_WAP.name().equals(payType) || PayTypeEnum.JD_H5.name().equals(payType)) {
            return PayOrderType.H5APP;
        } else if (PayTypeEnum.WECHAT_OPENID.name().equals(payType)) {
            return PayOrderType.JSAPI;
        } else if(PayTypeEnum.ZFB_SHH.name().equals(payType)){
        	 	return PayOrderType.LN;
        } else if(PayTypeEnum.WECHAT_SDK.name().equals(payType) || PayTypeEnum.ALIPAY_SDK.name().equals(payType)){
            return PayOrderType.SDK;
        } else if (PayTypeEnum.XCX_OFFLINE_ZF.name().equals(payType)) {
            return PayOrderType.MINI_PROGRAM;
        } else {
            return null;
        }
    }


    /**
     * 封装payType 和 payTool 信息，保存在remark字段
     *
     * @param merchantInNetConfig
     * @return
     */
    protected String buildRemark(MerchantInNetConfigResult merchantInNetConfig) {
        List<String> payTypes = new ArrayList<String>();
        for (String payType : merchantInNetConfig.getPayTypes()) {
            PayTypeEnum payTypeEnum = PayTypeEnum.valueOf(payType);
            payTypes.add(payTypeEnum.value() + "");
        }
        JSONObject configJson = new JSONObject();
        configJson.put("PayType", payTypes.toString());
        configJson.put("payTool", merchantInNetConfig.getPayToolAndPayTypeMap().keySet());
        return configJson.toJSONString();
    }

    /**
     * 开放及被扫支付-订单信息补充-使用接口入参覆盖部分反查订单信息
     *
     * @param orderInfo
     * @param apiCashierRequest
     */
    protected void orderInfoComplate(OrderDetailInfoModel orderInfo, UnifiedAPICashierRequestDTO apiCashierRequest) {
        orderInfo.setCashierVersion(CashierVersionEnum.API);
        orderInfo.setUserIp(apiCashierRequest.getUserIp());
        orderInfo.setAppId(apiCashierRequest.getAppId());
    }

    /**
     * 开放及被扫支付，补充paymentRequest信息
     *
     * @param paymentRequest
     * @param apiCashierRequest
     * @return
     */
    private boolean supplyPayRequestInfo(PaymentRequest paymentRequest, UnifiedAPICashierRequestDTO apiCashierRequest) {
        String extParamJson = apiCashierRequest.getExtParamMap();
        JSONObject extParam = null;
        if(StringUtils.isNotBlank(extParamJson)){
            try {
                extParam = JSONObject.parseObject(extParamJson);
            }catch (Exception e){
                throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，extParamMap格式无效");
            }
        }
        ExtendInfoFromPayRequest extendInfo = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
        //哆啦宝微信公众号粉丝路由特有功能,将 reportId 和 focusAppId 放入扩展字段中保存到数据库
        extendInfo.setReportId(extParam == null ? null : extParam.getString(Constant.WX_REPORT_ID));
        extendInfo.setFocusAppId(extParam == null ? null : extParam.getString(Constant.WX_FOCUS_APP_ID));

        //判断是否需要更新appId、openId和specifyChannelCodes
        String specifyChannelCodes = extParam == null ? null : extParam.getString(Constant.SPECIFY_CHANNEL_CODES);
        String reportFee = extParam == null ? null : extParam.getString(Constant.REPORT_FEE);
        boolean change = false;
        PayTool payTool = PayTool.valueOf(apiCashierRequest.getPayTool());
        switch (payTool) {
            case WECHAT_OPENID:
                //微信公众号appId和openId
                if (valueHasChanged(apiCashierRequest.getOpenId(), extendInfo.getOrigOpenId())) {
                    extendInfo.setOrigOpenId(apiCashierRequest.getOpenId());
                    change = true;
                }
                if (valueHasChanged(apiCashierRequest.getAppId(), extendInfo.getOrigAppId())) {
                    extendInfo.setOrigAppId(apiCashierRequest.getAppId());
                    change = true;
                }
                break;
            case ZFB_SHH:
                //支付宝生活号appId和openId
                if (valueHasChanged(apiCashierRequest.getOpenId(), extendInfo.getOrigAliUserId())) {
                    extendInfo.setOrigAliUserId(apiCashierRequest.getOpenId());
                    change = true;
                }
                break;
            case XCX_OFFLINE_ZF:
                //微信小程序appId和openId
                if (valueHasChanged(apiCashierRequest.getOpenId(), extendInfo.getOrigOpenId())) {
                    extendInfo.setOrigOpenId(apiCashierRequest.getOpenId());
                    change = true;
                }
                if (valueHasChanged(apiCashierRequest.getAppId(), extendInfo.getOrigAppId())) {
                    extendInfo.setOrigAppId(apiCashierRequest.getAppId());
                    change = true;
                }
                break;
            case MSCANPAY:
                //被扫appId
                if (valueHasChanged(apiCashierRequest.getAppId(), extendInfo.getOrigAppId())) {
                    extendInfo.setOrigAppId(apiCashierRequest.getAppId());
                    change = true;
                }
                break;
            default:
                break;
        }
        if (valueHasChanged(specifyChannelCodes, extendInfo.getSpecifyChannelCodes())) {
            //业务通道编码，微信支付宝主被扫、微信公众号、支付宝生活号使用
            extendInfo.setSpecifyChannelCodes(specifyChannelCodes);
            change = true;
        }
        if (valueHasChanged(reportFee, extendInfo.getReportFee())) {
            //报备费率（用于聚合支付，区分线上线下费率）
            extendInfo.setReportFee(reportFee);
            change = true;
        }
        if (change) {
            paymentRequest.setExtendInfo(extendInfo.toString());
        }
        return change;
    }

    private boolean valueHasChanged(String oldValue,String newValue){
        if(StringUtils.isBlank(oldValue) && StringUtils.isBlank(newValue)){
            //旧为空，新为空-->未改变
            return false;
        }
        if(StringUtils.isNotBlank(oldValue) && StringUtils.isNotBlank(newValue) && oldValue.equals(newValue)){
            //旧非空，新非空，旧==新，-->未改变
            return false;
        }
        return true;
    }

    /**
     * 绑卡支付 - 请求下单- 查询卡信息
     * @param paymentRequest
     * @param bindId
     * @param cashierUser
     */
    protected Map<String,Object> queryCardInfo(PaymentRequest paymentRequest, String bindId, CashierUserInfo cashierUser){
        return null;
    }

    /**
     * 绑卡支付 - 请求发短验 - 参数校验
     * @param sendSmsRequestDTO
     */
    protected void requestSmsParamValidate(ApiBindPaySendSmsRequestDTO sendSmsRequestDTO){
        return;
    }

    /**
     * 绑卡支付 - 请求发短验 - 保存临时卡及调用PP请求发短验
     * @param paymentRequest
     * @param paymentRecord
     * @param sendSmsRequestDTO
     */
    protected NcSmsResponseDTO saveTempCardAndRequestSMS(PaymentRequest paymentRequest, PaymentRecord paymentRecord, ApiBindPaySendSmsRequestDTO sendSmsRequestDTO){
        return null;
    }

    /**
     * 绑卡支付 - 请求发短验 - 封装返回结果
     * @param sendSmsRequestDTO
     * @return
     */
    protected ApiBindPaySendSmsResponseDTO buildSendSMSResponse(ApiBindPaySendSmsRequestDTO sendSmsRequestDTO){
        return null;
    }

    /**
     * 绑卡支付 - 确认支付 - 参数校验
     * @param confirmPayRequestDTO
     */
    protected void confirmPayParamValidate(ApiBindPayConfirmRequestDTO confirmPayRequestDTO){
        return;
    }

    /**
     * 绑卡支付-确认支付-调用PP校验验证码，保存临时卡，调用PP确认绑卡支付，更新信息到record
     * @param paymentRecord
     * @param paymentRequest
     * @param confirmPayRequestDTO
     */
    protected void handleVerifyCodeAndComfirmPay(PaymentRecord paymentRecord, PaymentRequest paymentRequest, ApiBindPayConfirmRequestDTO confirmPayRequestDTO){
        return;
    }

    /**
     * 绑卡支付-确认支付-封装返回
     * @param paymentRecord
     * @param confirmPayRequestDTO
     * @return
     */
    protected ApiBindPayConfirmResponseDTO buildComfirmBindedPayResponse(PaymentRecord paymentRecord, ApiBindPayConfirmRequestDTO confirmPayRequestDTO){
        return null;
    }
}
