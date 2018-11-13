package com.yeepay.g3.core.nccashier.service.impl.api;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.biz.NcCashierCoreBiz;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.APICashierBindCardPayService;
import com.yeepay.g3.core.nccashier.service.CashierBindCardService;
import com.yeepay.g3.core.nccashier.service.OrderPaymentService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.cwh.enumtype.IdcardType;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.dto.BasicResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * API收银台-绑卡支付-模板实现
 * Created by ruiyang.du on 2017/6/28.
 */
@Service
public class APICashierBindCardPayServiceImpl extends APICashierPaymentBaseTemplate implements APICashierBindCardPayService {

    private Logger logger = NcCashierLoggerFactory.getLogger(APICashierBindCardPayServiceImpl.class);

    @Resource
    private PayProcessorService payProcessorService;
    @Resource
    private OrderPaymentService orderPaymentService;
    @Resource
    private NcCashierCoreBiz ncCashierCoreBiz;
    @Resource
    private CashierBindCardService cashierBindCardService;

    private static final ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.BK_ZF, PayTypeEnum.BK_ZF);

    @Override
    public ApiBindPayPaymentResponseDTO requestPayment(ApiBindPayPaymentRequestDTO needItemRequestDTO) {
        return doRequestPayment(needItemRequestDTO);
    }

    @Override
    public ApiBindPaySendSmsResponseDTO requestSmsSend(ApiBindPaySendSmsRequestDTO param) {
        return doRequestSmsSend(param);
    }

    @Override
    public ApiBindPayConfirmResponseDTO confirmPayment(ApiBindPayConfirmRequestDTO param) {
        return doConfirmPayment(param);
    }

    /*以下为请求下单并获取补充项实现*/
    @Override
    protected <T> void paramValidate(T apiCashierRequestDTO) throws YeepayBizException {
        if (apiCashierRequestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + "，参数为空");
        }
        ApiBindPayPaymentRequestDTO needItemRequestDTO = (ApiBindPayPaymentRequestDTO)apiCashierRequestDTO;
        NcCashierLoggerFactory.TAG_LOCAL.set("[API绑卡支付下单|requestPayment]—[token=" + needItemRequestDTO.getToken() + "]");
        if (StringUtils.isBlank(needItemRequestDTO.getToken())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，token为空");
        }
        if (StringUtils.isBlank(needItemRequestDTO.getBindId())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，bindId为空");
        }
        if (StringUtils.isBlank(needItemRequestDTO.getUserNo())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，userNo为空");
        }
        if (StringUtils.isBlank(needItemRequestDTO.getUserType()) || (!IdentityType.getIdentityTypeMap().containsKey(needItemRequestDTO.getUserType()) && !MemberTypeEnum.YIBAO.name().equals(needItemRequestDTO.getUserType()))) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，userType非法");
        }
        if (StringUtils.isBlank(needItemRequestDTO.getMerchantNo())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，merchantNo为空");
        }
        if (!"1.0".equals(needItemRequestDTO.getVersion())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，version非法");
        }
        if (StringUtils.isBlank(needItemRequestDTO.getUserIp())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，userIp为空");
        }
        String merchantNo = needItemRequestDTO.getMerchantNo();
        if(merchantNo.startsWith("OPR:")){
            //从YOP透传来的商编，需去除"OPR:"前缀
            String[] merchantNoArray = merchantNo.split("OPR:");
            if (merchantNoArray != null && merchantNoArray.length >= 2) {
                needItemRequestDTO.setMerchantNo(merchantNoArray[1]);
            }
        }
    }

    @Override
    protected <T> PaymentRequest handlePaymentRequest(OrderDetailInfoModel orderInfo, MerchantInNetConfigResult merchantInNetConfig, T apiCashierRequest, CashierUserInfo cashierUser) {
        if (orderInfo == null || merchantInNetConfig == null || apiCashierRequest == null) {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        ApiBindPayPaymentRequestDTO needItemRequestDTO = (ApiBindPayPaymentRequestDTO)apiCashierRequest;

        //注：迁移到新逻辑，如下方
        PaymentRequestExtInfo extInfo = buildPaymentRequestExtInfo(needItemRequestDTO.getUserIp(), needItemRequestDTO.getBindId(),cashierUser);
        PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig, productLevel, extInfo);
        return paymentRequest;
    }

    @Override
    protected <T> PaymentRecord handlePaymentRecord(PaymentRequest paymentRequest, T apiCashierRequest, String payTool, String payType,String payMerchantNo) {
        if (paymentRequest == null || StringUtils.isBlank(payTool) || StringUtils.isBlank(payType) || apiCashierRequest == null ) {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        Map<String,Object> cardInfoMap = (Map<String,Object>)apiCashierRequest;
        String bindId = (String) cardInfoMap.get("bindId");
        CardInfoDTO cardInfoDTO = (CardInfoDTO) cardInfoMap.get("cardInfo");
        if(StringUtils.isBlank(bindId) || cardInfoDTO==null){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        PaymentRecord paymentRecord = queryPaymentRecord(paymentRequest, payType, bindId);
        if (null != paymentRecord) {
            return paymentRecord;
        }
        paymentRecord = new PaymentRecord();
        paymentRecord.setAreaInfo(paymentRequest.getAreaInfo());
        paymentRecord.setBankCode(cardInfoDTO.getBankCode());
        paymentRecord.setBindId(bindId);
        paymentRecord.setBizModeCode(paymentRequest.getBizModeCode());
        paymentRecord.setCardNo(StringUtils.isNotBlank(paymentRequest.getCardNo()) ? paymentRequest.getCardNo() : cardInfoDTO.getCardno());
        paymentRecord.setCardType(cardInfoDTO.getCardType());
        paymentRecord.setPhoneNo(StringUtils.isNotBlank(paymentRequest.getPhoneNo()) ? paymentRequest.getPhoneNo() : cardInfoDTO.getPhone());
        paymentRecord.setOwner(StringUtils.isNotBlank(paymentRequest.getOwner()) ? paymentRequest.getOwner() : cardInfoDTO.getName());
        paymentRecord.setCreateTime(new Date());
        paymentRecord.setIdCard(StringUtils.isNotBlank(paymentRequest.getIdCard()) ? paymentRequest.getIdCard() : cardInfoDTO.getIdno());
        paymentRecord.setIdCardType(IdCardTypeEnum.IDENTITY.toString());
        paymentRecord.setMcc(paymentRequest.getIndustryCatalog());
        paymentRecord.setMerchantName(paymentRequest.getMerchantName());
        paymentRecord.setMerchantNo(paymentRequest.getMerchantNo());
        paymentRecord.setMerchantOrderId(paymentRequest.getMerchantOrderId());
        paymentRecord.setPaymentAmount(paymentRequest.getOrderAmount());
        paymentRecord.setPaymentRequestId(paymentRequest.getId());
        paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
        paymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
        paymentRecord.setPayTool(payTool);
        paymentRecord.setPayType(payType);
        paymentRecord.setProductName(paymentRequest.getProductName());
        paymentRecord.setState(PayRecordStatusEnum.INIT);
        paymentRecord.setTokenId("");
        paymentRecord.setTradeSysNo(paymentRequest.getTradeSysNo());
        paymentRecord.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
        paymentRecord.setOrderOrderId(paymentRequest.getOrderOrderId());
        paymentRecord.setOrderSysNo(paymentRequest.getOrderSysNo());
        paymentRecord.setMemberNo((String) cardInfoMap.get("memberId"));
        paymentRecord.setMemberType(StringUtils.isNotBlank(paymentRequest.getMemberType()) ? paymentRequest.getMemberType() : Constant.JOINLY);
        if (StringUtils.isNotBlank(payMerchantNo)){
            paymentRecord.setPayMerchantNo(payMerchantNo);
        }
        paymentRecord.setUpdateTime(new Date());
        paymentRecord.setVersion(1);
        long paymentRecordId = paymentProcessService.savePaymentRecord(paymentRecord);
        paymentRecord.setId(paymentRecordId);
        return paymentRecord;
    }

    /**
     * 根据paymentRequest、paytype、bindId查询paymentRecord
     * @param paymentRequest
     * @param payType
     * @return
     */
    private PaymentRecord queryPaymentRecord(PaymentRequest paymentRequest, String payType, String bindId){
        PaymentRecord paymentRecord = null;
        List<PaymentRecord> paymentRecords = paymentProcessService.findRecordList(paymentRequest.getTradeSysOrderId(), paymentRequest.getTradeSysNo());
        List<PaymentRecord> paymentRecordsForPayType = new ArrayList<PaymentRecord>();
        if (CollectionUtils.isNotEmpty(paymentRecords)) {
            for (PaymentRecord record : paymentRecords) {
                if (PayRecordStatusEnum.SUCCESS == record.getState()) {
                    throw CommonUtil.handleException(Errors.THRANS_FINISHED);
                }
                if (payType.equals(record.getPayType()) && bindId.equals(record.getBindId())) {
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
     * 绑卡支付-订单信息补充-使用接口入参覆盖部分反查订单信息
     *
     * @param orderInfo
     * @param apiCashierRequest
     */
    private void orderInfoComplate(OrderDetailInfoModel orderInfo, ApiBindPayPaymentRequestDTO apiCashierRequest) {
        if (MemberTypeEnum.YIBAO.name().equals(apiCashierRequest.getUserType())) {
            orderInfo.setMemberNo(apiCashierRequest.getUserNo());
            orderInfo.setMemberType(MemberTypeEnum.YIBAO.name());
        } else {
            orderInfo.setIdentityId(apiCashierRequest.getUserNo());
            orderInfo.setIdentityType(StringUtils.isNotBlank(apiCashierRequest.getUserType()) ? IdentityType.valueOf(apiCashierRequest.getUserType()) : null);
        }
        orderInfo.setCashierVersion(CashierVersionEnum.API);
        orderInfo.setUserIp(apiCashierRequest.getUserIp());
    }

    @Override
    protected <O extends BasicRequestDTO, I> O prepareToCallPayProcessor(PaymentRecord paymentRecord, I apiRequest, PaymentRequest paymentRequest) {
        ApiBindPayPaymentRequestDTO needItemRequestDTO = (ApiBindPayPaymentRequestDTO) apiRequest;
        NcPayOrderRequestDTO ncPayOrderRequestDTO = new NcPayOrderRequestDTO();
        buildCommonRequestDTO(paymentRecord, paymentRequest, ncPayOrderRequestDTO, needItemRequestDTO.getUserIp());
        buildBindCardPayProcessorRequestDTO(paymentRecord, paymentRequest, ncPayOrderRequestDTO);
        return (O)ncPayOrderRequestDTO;
    }


    @Override
    protected <I extends BasicRequestDTO, O extends BasicResponseDTO> O requestPayProcessorPay(I requestDTO) {
        NcPayOrderRequestDTO ncPayRequestDTO = (NcPayOrderRequestDTO) requestDTO;
        NcPayOrderResponseDTO ncPayOrderResponseDTO = payProcessorService.ncPayRequest(ncPayRequestDTO);
        return (O) ncPayOrderResponseDTO;
    }

    @Override
    protected <O extends BasicResponseDTO> void updatePaymentRecord(O response, PaymentRecord record) {
        if (null == response) {
            return;
        }
        NcPayOrderResponseDTO ncPayOrderResponseDTO = (NcPayOrderResponseDTO) response;
        if (ncPayOrderResponseDTO.getProcessStatus() == ProcessStatus.SUCCESS) {
            paymentProcessService.updateRecordNo(record.getId(), ncPayOrderResponseDTO.getSmsType().name(), ncPayOrderResponseDTO.getRecordNo(), PayRecordStatusEnum.ORDERED, ncPayOrderResponseDTO.getNeedItem(),RedirectTypeEnum.NONE.name());
        }
    }

    @Override
    protected Map<String,Object> queryCardInfo(PaymentRequest paymentRequest, String bindId, CashierUserInfo cashierUser) {
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("bindId", bindId);
        //外部用户id (注：cashierUser对象中已获取外部用户id，包括三代会员的memberNo)
        infoMap.put("memberId", cashierUser.getExternalUserId());
        //根据绑卡id获取卡信息
        CardInfoDTO cardInfoDTO = cashierBindCardService.getCardInfoDTO(bindId);
        infoMap.put("cardInfo", cardInfoDTO);
        return infoMap;
    }

    @Override
    protected <P extends BasicResponseDTO, A, O> O buildResponse(P payProcessorResponse, A apiRequest, PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
        NcPayOrderResponseDTO ncPayOrderResponseDTO = (NcPayOrderResponseDTO) payProcessorResponse;
        ApiBindPayPaymentRequestDTO needItemRequestDTO = (ApiBindPayPaymentRequestDTO) apiRequest;
        //基本返回信息
        ApiBindPayPaymentResponseDTO needItemResponseDTO = buildBasicResponse(needItemRequestDTO);
        //其他绑卡支付返回信息
        SmsSendTypeEnum smsType = ncPayOrderResponseDTO.getSmsType();
        //验证码类型及提交补充项场景
        //需要发验证码的，在请求短验时补充；不需要验证码的，在确认支付时补充
        if(smsType==null){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }else if(smsType==SmsSendTypeEnum.NONE){
            //无需验证码->验证码=none;提交补充=确认时
            needItemResponseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_NONE);
            needItemResponseDTO.setSupplyNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_CONFIRM);
        }else if(smsType==SmsSendTypeEnum.BANK || smsType==SmsSendTypeEnum.YEEPAY){
            //短信验证码->验证码=SMS;提交补充=发短验时
            needItemResponseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_SMS);
            needItemResponseDTO.setSupplyNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_VERITY);
        }else if(smsType==SmsSendTypeEnum.VOICE){
            //语音验证码->验证码=VOICE;提交补充=发短验时
            needItemResponseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_VOICE);
            needItemResponseDTO.setSupplyNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_VERITY);
        }
        if (0 == ncPayOrderResponseDTO.getNeedItem()) {
            //无需补充项->提交补充=NONE
            needItemResponseDTO.setSupplyNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_NONE);
        }
        //处理补充项信息
        NeedBankCardDTO bkdto = orderPaymentService.getBindCardAndNeedSupplement(Long.parseLong(needItemRequestDTO.getBindId()),ncPayOrderResponseDTO.getNeedItem(),paymentRequest);
        NeedSurportDTO needSurportDTO = bkdto.getNeedSurportDTO();
        String needItemsString = null;
        if(needSurportDTO!=null){
            //有补充项
            StringBuilder needItemsStringBuilder = new StringBuilder();
            Class<?> clazz = needSurportDTO.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for(Field field:declaredFields){
                field.setAccessible(true);
                String fieldName = field.getName();
                if(!fieldName.contains("Need")){
                    continue;
                }
                Boolean filedValue = null;
                try {
                    filedValue = (Boolean) field.get(needSurportDTO);
                } catch (Exception e) {
                    throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
                }
                if(filedValue){
                    needItemsStringBuilder.append(fieldName).append(",");
                }
            }
            needItemsString = needItemsStringBuilder.toString();
            needItemsString = needItemsString.substring(0, needItemsString.length() - 1);
        }
        needItemResponseDTO.setNeedItems(needItemsString);
        saveAPIResponseIntoCache(needItemResponseDTO, paymentRecord.getId());
        needItemResponseDTO.setRecordId(String.valueOf(paymentRecord.getId()));
        return (O) needItemResponseDTO;
    }

    /**
     * 绑卡支付 - 封装通用返回结果
     *
     * @param needItemRequestDTO api接口入参
     */
    private ApiBindPayPaymentResponseDTO buildBasicResponse(ApiBindPayPaymentRequestDTO needItemRequestDTO) {
        ApiBindPayPaymentResponseDTO cashierReponseDTO = new ApiBindPayPaymentResponseDTO();
        cashierReponseDTO.setCode(APICashierPayResultEnum.SUCCESS.getCode());
        cashierReponseDTO.setMessage(APICashierPayResultEnum.SUCCESS.getMessage());
        cashierReponseDTO.setMerchantNo(needItemRequestDTO.getMerchantNo());
        cashierReponseDTO.setToken(needItemRequestDTO.getToken());
        cashierReponseDTO.setBindId(needItemRequestDTO.getBindId());
        return cashierReponseDTO;
    }

    /**
     * 封装绑卡支付-调用PP下单特有参数
     * @param paymentRecord
     * @param paymentRequest
     * @param ncPayOrderRequestDTO
     */
    private void buildBindCardPayProcessorRequestDTO(PaymentRecord paymentRecord, PaymentRequest paymentRequest, NcPayOrderRequestDTO ncPayOrderRequestDTO) {
        ncPayOrderRequestDTO.setPayProduct(PayTool.BK_ZF.name());
        ncPayOrderRequestDTO.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
        ncPayOrderRequestDTO.setPayScene(paymentRequest.getBizModeCode());
        ncPayOrderRequestDTO.setPayOrderType(PayOrderType.SALE);
        ncPayOrderRequestDTO.setMemberType(MemberTypeEnum.valueOf(paymentRecord.getMemberType()));
        ncPayOrderRequestDTO.setMemberNO(paymentRecord.getMemberNo());
        ncPayOrderRequestDTO.setCardInfoType(CardInfoTypeEnum.BIND);
        ncPayOrderRequestDTO.setCardInfoId(Long.parseLong(paymentRecord.getBindId()));
        ncPayOrderRequestDTO.setPayTool(paymentRecord.getPayProductCode());
        ncPayOrderRequestDTO.setBasicProductCode(CommonUtil.getBasicProductCode(PayTool.BK_ZF.name(),paymentRequest.getTradeSysNo()));
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        ncPayOrderRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));
		if(jsonObject.get("groupTag") != null){
			Map<String, String> extMap = new HashMap<String, String>();
			extMap.put("groupTag",(String) jsonObject.get("groupTag"));
			ncPayOrderRequestDTO.setExtParam(extMap);
		}
    }

    /*以下为请求发短验业务实现*/
    @Override
    protected void requestSmsParamValidate(ApiBindPaySendSmsRequestDTO sendSmsRequestDTO){
        if (sendSmsRequestDTO == null) {
            logger.error("paramValidate()参数校验不通过，绑卡支付下单，参数为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + "，参数为空");
        }
        NcCashierLoggerFactory.TAG_LOCAL.set("[API绑卡支付请求发短验|requestSmsSend]—[token=" + sendSmsRequestDTO.getToken() + "]");
        if (StringUtils.isBlank(sendSmsRequestDTO.getToken())) {
            logger.error("paramValidate()参数校验不通过，绑卡支付发短验，token为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，token为空");
        }
        if (!"1.0".equals(sendSmsRequestDTO.getVersion())) {
            logger.error("paramValidate()参数校验不通过，绑卡支付发短验，version非法");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，version非法");
        }
        if (StringUtils.isNotBlank(sendSmsRequestDTO.getIdCardType()) && !IdcardType.ID.name().equals(sendSmsRequestDTO.getIdCardType()) &&
                !IdcardType.OFFICERS.name().equals(sendSmsRequestDTO.getIdCardType()) &&!IdcardType.PASSPORT.name().equals(sendSmsRequestDTO.getIdCardType()) &&
                !IdcardType.POLICE.name().equals(sendSmsRequestDTO.getIdCardType()) &&!IdcardType.REENTRY.name().equals(sendSmsRequestDTO.getIdCardType()) &&
                !IdcardType.SOLDIER.name().equals(sendSmsRequestDTO.getIdCardType()) &&!IdcardType.TAIWAN.name().equals(sendSmsRequestDTO.getIdCardType()) ) {
            logger.error("paramValidate()参数校验不通过，绑卡支付发短验，idCardType非法");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，idCardType非法");
        }
        return;
    }

    @Override
    protected NcSmsResponseDTO saveTempCardAndRequestSMS(PaymentRequest paymentRequest, PaymentRecord paymentRecord, ApiBindPaySendSmsRequestDTO sendSmsRequestDTO){
        //处理补充项，保存临时卡
        Long tmpCardId = 0l;
        NeedBankCardDTO needBankCardDTO = buildNeedBankCardDTO(sendSmsRequestDTO);
        boolean needSMS = SmsSendTypeEnum.BANK.name().equals(paymentRecord.getSmsVerifyType()) || SmsSendTypeEnum.YEEPAY.name().equals(paymentRecord.getSmsVerifyType()) ||
                SmsSendTypeEnum.VOICE.name().equals(paymentRecord.getSmsVerifyType());//需要发短验：如果同时需要补充项，则为短验时补充
        if(needSMS){
            checkNeedItemSupply(paymentRecord.getNeedItem(),needBankCardDTO);
        }
        if(null != needBankCardDTO){
            tmpCardId = ncCashierCoreBiz.saveTmpCard(paymentRecord, needBankCardDTO,paymentRequest,false);
        }
        NcSmsRequestDTO requestDTO = new NcSmsRequestDTO();
        requestDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
        //注：pay_record中保存的验证码类型，为SmsSendTypeEnum枚举值的字符串，此处要调用PP接口，->
        //需先根据逻辑转换成nccashier.enumtype.ReqSmsSendTypeEnum枚举的值，再等价转换成ncpay.enumtype.ReqSmsSendTypeEnum的枚举值
        ReqSmsSendTypeEnum reqSmsSendTypeEnum = CommonUtil.transferBindPaySMSType(SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType()));
        // modify by meiling.zhuang:绑卡支付不需要发短验时，商户也请求发短验，允许发短验证
        requestDTO.setSmsSendType(reqSmsSendTypeEnum==null?com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum.YEEPAY:com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum.valueOf(reqSmsSendTypeEnum.name()));
        if(tmpCardId != 0){
            requestDTO.setTmpCardId(tmpCardId);
        }
        NcSmsResponseDTO ncSmsResponseDTO = payProcessorService.verifyAndSendSms(requestDTO);
        return ncSmsResponseDTO;
    }

    @Override
    protected ApiBindPaySendSmsResponseDTO buildSendSMSResponse(ApiBindPaySendSmsRequestDTO sendSmsRequestDTO) {
        ApiBindPaySendSmsResponseDTO sendSmsResponseDTO = new ApiBindPaySendSmsResponseDTO();
        sendSmsResponseDTO.setCode(APICashierPayResultEnum.SUCCESS.getCode());
        sendSmsResponseDTO.setMessage(APICashierPayResultEnum.SUCCESS.getMessage());
        sendSmsResponseDTO.setToken(sendSmsRequestDTO.getToken());
        return sendSmsResponseDTO;
    }

    /*以下为确认支付业务实现*/
    @Override
    protected void confirmPayParamValidate(ApiBindPayConfirmRequestDTO confirmPayRequestDTO){
        if (confirmPayRequestDTO == null) {
            logger.error("paramValidate()参数校验不通过，绑卡支付下单，参数为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + "，参数为空");
        }
        NcCashierLoggerFactory.TAG_LOCAL.set("[API绑卡支付确认支付|confirmPayment]—[token=" + confirmPayRequestDTO.getToken() + "]");
        if (StringUtils.isEmpty(confirmPayRequestDTO.getToken())) {
            logger.error("paramValidate()参数校验不通过，绑卡支付确认支付，token为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，token为空");
        }
        if (!"1.0".equals(confirmPayRequestDTO.getVersion())) {
            logger.error("paramValidate()参数校验不通过，绑卡支付确认支付，version非法");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，version非法");
        }
        if (StringUtils.isNotBlank(confirmPayRequestDTO.getIdCardType()) && !IdcardType.ID.name().equals(confirmPayRequestDTO.getIdCardType()) &&
                !IdcardType.OFFICERS.name().equals(confirmPayRequestDTO.getIdCardType()) &&!IdcardType.PASSPORT.name().equals(confirmPayRequestDTO.getIdCardType()) &&
                !IdcardType.POLICE.name().equals(confirmPayRequestDTO.getIdCardType()) &&!IdcardType.REENTRY.name().equals(confirmPayRequestDTO.getIdCardType()) &&
                !IdcardType.SOLDIER.name().equals(confirmPayRequestDTO.getIdCardType()) &&!IdcardType.TAIWAN.name().equals(confirmPayRequestDTO.getIdCardType()) ) {
            logger.error("paramValidate()参数校验不通过，绑卡支付发短验，idCardType非法");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，idCardType非法");
        }
        return;
    }

    @Override
    protected void handleVerifyCodeAndComfirmPay(PaymentRecord paymentRecord, PaymentRequest paymentRequest, ApiBindPayConfirmRequestDTO confirmPayRequestDTO) {
        //判断是否需要校验短验
        SmsSendTypeEnum ncpayResponseSmsType = SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType());
        boolean isneedVeryCode = true;
        if (ncpayResponseSmsType == SmsSendTypeEnum.NONE && CommonUtil.getRiskVerifyCodeSwitch()) {
            isneedVeryCode = false;
        }

        if (isneedVeryCode && StringUtils.isBlank(confirmPayRequestDTO.getVerifyCode())) {
            throw CommonUtil.handleException(Errors.VERIFYCODE_MISS);
        }
        //查询PP，判断订单状态
        PayRecordQueryRequestDTO requestDTO = new PayRecordQueryRequestDTO();
        requestDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
        PayRecordResponseDTO responseDTO = payProcessorService.query(requestDTO);
        if (isneedVeryCode && (responseDTO.getSmsState() == SmsCheckResultEnum.NONE)) {
            throw CommonUtil.handleException(Errors.GET_SMS_FIRST);
        }
        //处理补充项，保存临时卡
        NeedBankCardDTO needBankCardDTO = buildNeedBankCardDTO(confirmPayRequestDTO);
        boolean needSMS = SmsSendTypeEnum.BANK.name().equals(paymentRecord.getSmsVerifyType()) || SmsSendTypeEnum.YEEPAY.name().equals(paymentRecord.getSmsVerifyType()) ||
                SmsSendTypeEnum.VOICE.name().equals(paymentRecord.getSmsVerifyType());//不需要发短验：如果同时需要补充项，则为确认支付时补充
        if(!needSMS){
            checkNeedItemSupply(paymentRecord.getNeedItem(),needBankCardDTO);
        }
        Long tmpCardId = ncCashierCoreBiz.saveTmpCard(paymentRecord, needBankCardDTO, paymentRequest,false);
        ncCashierCoreBiz.payprocessorSyncConfirmPay(paymentRecord, confirmPayRequestDTO.getVerifyCode(), tmpCardId,paymentRequest);
    }

    @Override
    protected ApiBindPayConfirmResponseDTO buildComfirmBindedPayResponse(PaymentRecord paymentRecord, ApiBindPayConfirmRequestDTO confirmPayRequestDTO){
        ApiBindPayConfirmResponseDTO bindedConfirmPayResponseDTO = new ApiBindPayConfirmResponseDTO();
        bindedConfirmPayResponseDTO.setCode(APICashierPayResultEnum.SUCCESS.getCode());
        bindedConfirmPayResponseDTO.setMessage(APICashierPayResultEnum.SUCCESS.getMessage());
        bindedConfirmPayResponseDTO.setToken(confirmPayRequestDTO.getToken());
        return bindedConfirmPayResponseDTO;
    }

    /**
     * 将入参中的补充项提取出来，检查所需补充项是否提交，并构建NeedBankCardDTO对象
     * @param sendSmsRequestDTO
     * @return
     */
    private NeedBankCardDTO buildNeedBankCardDTO(ApiBindPaySendSmsRequestDTO sendSmsRequestDTO) {
        if (StringUtils.isBlank(sendSmsRequestDTO.getAvlidDate()) && StringUtils.isBlank(sendSmsRequestDTO.getOwner()) &&
                StringUtils.isBlank(sendSmsRequestDTO.getCvv()) && StringUtils.isBlank(sendSmsRequestDTO.getCardno()) &&
                StringUtils.isBlank(sendSmsRequestDTO.getBankPWD()) && StringUtils.isBlank(sendSmsRequestDTO.getIdCardType()) &&
                StringUtils.isBlank(sendSmsRequestDTO.getIdno()) && StringUtils.isBlank(sendSmsRequestDTO.getPhoneNo()) &&
                StringUtils.isBlank(sendSmsRequestDTO.getYpMobile())) {
            return null;
        }
        NeedBankCardDTO needBankCardDTO = new NeedBankCardDTO();
        needBankCardDTO.setAvlidDate(sendSmsRequestDTO.getAvlidDate());
        needBankCardDTO.setOwner(sendSmsRequestDTO.getOwner());
        needBankCardDTO.setCvv(sendSmsRequestDTO.getCvv());
        needBankCardDTO.setCardno(sendSmsRequestDTO.getCardno());
        needBankCardDTO.setBankPWD(sendSmsRequestDTO.getBankPWD());
        needBankCardDTO.setIdCardType(StringUtils.isBlank(sendSmsRequestDTO.getIdCardType()) ? null : IdcardType.valueOf(sendSmsRequestDTO.getIdCardType()));
        needBankCardDTO.setIdno(sendSmsRequestDTO.getIdno());
        needBankCardDTO.setPhoneNo(sendSmsRequestDTO.getPhoneNo());
        needBankCardDTO.setYpMobile(sendSmsRequestDTO.getYpMobile());
        return needBankCardDTO;
    }

    /**
     * 将入参中的补充项提取出来，检查所需补充项是否提交，并构建NeedBankCardDTO对象
     * @param confirmPayRequestDTO
     * @return
     */
    private NeedBankCardDTO buildNeedBankCardDTO(ApiBindPayConfirmRequestDTO confirmPayRequestDTO) {
        if (StringUtils.isBlank(confirmPayRequestDTO.getAvlidDate()) && StringUtils.isBlank(confirmPayRequestDTO.getOwner()) &&
                StringUtils.isBlank(confirmPayRequestDTO.getCvv()) && StringUtils.isBlank(confirmPayRequestDTO.getCardno()) &&
                StringUtils.isBlank(confirmPayRequestDTO.getBankPWD()) && StringUtils.isBlank(confirmPayRequestDTO.getIdCardType()) &&
                StringUtils.isBlank(confirmPayRequestDTO.getIdno()) && StringUtils.isBlank(confirmPayRequestDTO.getPhoneNo()) &&
                StringUtils.isBlank(confirmPayRequestDTO.getYpMobile())) {
            return null;
        }
        NeedBankCardDTO needBankCardDTO = new NeedBankCardDTO();
        needBankCardDTO.setAvlidDate(confirmPayRequestDTO.getAvlidDate());
        needBankCardDTO.setOwner(confirmPayRequestDTO.getOwner());
        needBankCardDTO.setCvv(confirmPayRequestDTO.getCvv());
        needBankCardDTO.setCardno(confirmPayRequestDTO.getCardno());
        needBankCardDTO.setBankPWD(confirmPayRequestDTO.getBankPWD());
        needBankCardDTO.setIdCardType(StringUtils.isBlank(confirmPayRequestDTO.getIdCardType()) ? null : IdcardType.valueOf(confirmPayRequestDTO.getIdCardType()));
        needBankCardDTO.setIdno(confirmPayRequestDTO.getIdno());
        needBankCardDTO.setPhoneNo(confirmPayRequestDTO.getPhoneNo());
        needBankCardDTO.setYpMobile(confirmPayRequestDTO.getYpMobile());
        return needBankCardDTO;
    }

    /**
     * 检查要求的补充项是否都提交
     * @param needItemNum
     * @param needBankCardDTO
     * @return
     */
    private void checkNeedItemSupply(int needItemNum,NeedBankCardDTO needBankCardDTO) {
        if (needItemNum == 0) {
            return;
        }
        if (needBankCardDTO == null) {
            //需要补充信息、补充项全为空：抛异常
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + "，请补充卡信息");
        }
        String binary = Integer.toBinaryString(needItemNum);
        char[] chars = binary.toCharArray();

        String cardno = needBankCardDTO.getCardno();
        String owner = needBankCardDTO.getOwner();
        String avlidDate = needBankCardDTO.getAvlidDate();
        String cvv = needBankCardDTO.getCvv();
        IdcardType idCardType = needBankCardDTO.getIdCardType();
        String idno = needBankCardDTO.getIdno();
        String phoneNo = needBankCardDTO.getPhoneNo();
        String ypMobile = needBankCardDTO.getYpMobile();
        String bankPWD = needBankCardDTO.getBankPWD();

        if (compareNeedItemBinary(chars, 1, cardno)) {
            //需要发短验、需要补充卡号、卡号为空：抛异常
            throw new CashierBusinessException(Errors.CARD_NOT_NULL);
        }
        if (compareNeedItemBinary(chars, 2, owner)) {
            //需要发短验、需要补充姓名、姓名为空：抛异常
            throw new CashierBusinessException(Errors.NAME_NOT_NULL);
        }
        if (compareNeedItemBinary(chars, 3, avlidDate)) {
            //需要发短验、需要补充有效期、有效期为空：抛异常
            throw new CashierBusinessException(Errors.VALIDATE_NOT_NULL);
        }
        if (compareNeedItemBinary(chars, 4, cvv)) {
            //需要发短验、需要补充cvv、cvv为空：抛异常
            throw new CashierBusinessException(Errors.CVV2_NOT_NULL);
        }
        if (chars.length >= 5 && "1".equals(Character.toString(chars[chars.length - 5])) && idCardType == null) {
            //需要发短验、需要补充证件类型、证件类型为空：抛异常
            throw new CashierBusinessException(Errors.ID_TYPE_NOT_NULL);
        }
        if (compareNeedItemBinary(chars, 6, idno)) {
            //需要发短验、需要补充证件号、证件号为空：抛异常
            throw new CashierBusinessException(Errors.ID_NOT_NULL);
        }
        if (compareNeedItemBinary(chars, 7, phoneNo)) {
            //需要发短验、需要补充银行手机、银行手机为空：抛异常
            throw new CashierBusinessException(Errors.PHONE_NOT_NULL);
        }
        if (compareNeedItemBinary(chars, 8, ypMobile)) {
            //需要发短验、需要补充易宝手机、易宝手机为空：抛异常
            throw new CashierBusinessException(Errors.PHONE_NOT_NULL);
        }
        if (compareNeedItemBinary(chars, 9, bankPWD)) {
            //需要发短验、需要补充取款密码、取款密码为空：抛异常
            throw new CashierBusinessException(Errors.PASSWORD_NOT_NULL);
        }
    }

    /**
     * 判断需要的补充项是否提交
     * @param chars
     * @param indexToEnd
     * @param item
     * @return
     */
    private boolean compareNeedItemBinary(char[] chars, int indexToEnd, String item) {
        return chars.length >= indexToEnd && "1".equals(Character.toString(chars[chars.length - indexToEnd])) && StringUtils.isBlank(item);
    }

    private PaymentRequestExtInfo buildPaymentRequestExtInfo(String userIp, String bindId, CashierUserInfo user) {
        PaymentRequestExtInfo extInfo = new PaymentRequestExtInfo();
        extInfo.setUserIp(userIp);
        extInfo.setBindId(bindId);
        extInfo.setCashierUser(user);
        return extInfo;
    }
}
