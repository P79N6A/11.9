package com.yeepay.g3.core.nccashier.service.impl.api;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.cwh.param.*;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.ncconfig.enumtype.BankEnum;
import com.yeepay.g3.facade.ncconfig.enumtype.CardType;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.NcPayConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcSmsRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class APICashierYJZFServiceImpl extends NcCashierBaseService implements APICashierYJZFService {

    @Autowired
    private CashierBankCardService cashierBankCardService;
    @Resource
    private MerchantVerificationService merchantVerificationService;
    @Autowired
    private BankCardLimitInfoService bankCardLimitInfoService;
    @Autowired
    private PaymentProcessService paymentProcessService;
    @Autowired
    private CwhService cwhService;
    @Autowired
    private OrderPaymentService orderPaymentService;
    @Autowired
    private PayProcessorService payProcessorService;
    @Autowired
    private CashierBindCardService cashierBindCardService;
    @Resource
    private PersonalMemberService personalMemberService;

    private static final Logger LOGGER = LoggerFactory.getLogger(APICashierYJZFServiceImpl.class);
    /** 一键支付的三级产品信息 */
    private static ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.NCPAY, PayTypeEnum.BANK_PAY_WAP);
    /** 一键支付支付流程--第二步，发送短验 */
    private static final int PAYMENT_PROCESS_SEND_SMS = 2;
    /** 一键支付支付流程--第三步，确认支付 */
    private static final int PAYMENT_PROCESS_CONFIRM_PAY = 3;

    @Override
    public void validateFirstPaymentParam(APIYJZFFirstPaymentRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        // 校验卡号是否存在，且判断卡类型，如果为信用卡，校验cvv、有效期必须非空
        CardBinDTO cardBinDTO = cashierBankCardService.getCardBinInfo(requestDTO.getCardNo());
        if (cardBinDTO.getBankEnum() == null || cardBinDTO.getCardType() == null) {
            throw CommonUtil.handleException(Errors.INVALID_BANK_CARD_NO);
        }
        if (CardType.CREDIT == cardBinDTO.getCardType()) {
            if (StringUtils.isBlank(requestDTO.getCvv())) {
                throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",cvv不能为空");
            }
            if (StringUtils.isBlank(requestDTO.getAvlidDate())) {
                throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",avlidDate不能为空");
            }
        }
        // 当不需校验开通配置时，校验支付场景和商品类别码是否存在
        if (!requestDTO.isCheckProductOpen() && StringUtils.isBlank(requestDTO.getPayScene())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",payScene不能为空");
        }
        if (!requestDTO.isCheckProductOpen() && StringUtils.isBlank(requestDTO.getMcc())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",mcc不能为空");
        }
        // 商编的转化 —— 将OPR：等前缀去掉
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[一键支付API首次支付下单|firstPayRequest],token=" + requestDTO.getToken() + "]");
    }

    @Override
    public void validateBindPaymentParam(APIYJZFBindPaymentRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        // 当不需校验开通配置时，校验支付场景和商品类别码是否存在
        if (!requestDTO.isCheckProductOpen() && StringUtils.isBlank(requestDTO.getPayScene())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",payScene不能为空");
        }
        if (!requestDTO.isCheckProductOpen() && StringUtils.isBlank(requestDTO.getMcc())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",mcc不能为空");
        }
        // 商编的转化 —— 将OPR：等前缀去掉
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[一键支付API二次支付下单|bindPayRequest],token=" + requestDTO.getToken() + "]");
    }

    @Override
    public void validateSendSMSParam(APIYJZFSendSmsRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        // 商编的转化 —— 将OPR：等前缀去掉
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[一键支付API请求发短验|sendSMS],token=" + requestDTO.getToken() + "]");

    }

    @Override
    public void validateConfirmPayParam(APIYJZFConfirmPayRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        // 商编的转化 —— 将OPR：等前缀去掉
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[一键支付API确认支付|paymentConfirm],token=" + requestDTO.getToken() + "]");

    }

    @Override
    public MerchantInNetConfigResult verifyProductOpen(String merchantNo, boolean checkProduct, TransactionTypeEnum transactionType) {
        if (!checkProduct) {
            //无需校验产品开通的，直接返回
            return null;
        }
        VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
        requestParam.setMerchantNo(merchantNo);
        requestParam.setProductLevel(productLevel);
        requestParam.setTransactionType(transactionType);
        MerchantInNetConfigResult merchantInNetConfig = merchantVerificationService.verifyMerchantAuthority(requestParam);
        return merchantInNetConfig;
    }

    @Override
    public void validateSamePersionLimit(ValidateSamePersionLimitDTO requestDTO, OrderDetailInfoModel orderInfo) {
        if (StringUtils.isBlank(requestDTO.getUserNo()) || StringUtils.isBlank(requestDTO.getUserType())) {
            //userNo或userType为空，不进行同人限制信息校验
            return;
        }
        BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfoNoCache(orderInfo.getMerchantAccountCode(), requestDTO.getUserNo(),
                requestDTO.getUserType(), orderInfo.getProductOrderCode());
        if (bindLimitInfoResDTO == null || Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())) {
            //同人限制为空，直接通过校验
            return;
        }
        if (StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit()) && !bindLimitInfoResDTO.getUserNameLimit().equals(requestDTO.getOwner())) {
            //姓名不对
            throw new CashierBusinessException(Errors.CARD_INFO_NOT_SAME);
        }
        if (StringUtils.isNotBlank(bindLimitInfoResDTO.getIdentityNoLimit()) && !bindLimitInfoResDTO.getIdentityNoLimit().equals(requestDTO.getIdNo())) {
            //身份证不对
            throw new CashierBusinessException(Errors.CARD_INFO_NOT_SAME);
        }
        if (!bindLimitInfoResDTO.isCanBindNewCard()) {
            boolean canUseNewCard = bankCardLimitInfoService.judge3GFirstPay(orderInfo.getMerchantAccountCode(), orderInfo.getOrderSysNo());
            if (!canUseNewCard) {
                //达到绑卡上限，且商编未配置允许继续使用新卡
                throw new CashierBusinessException(Errors.OUT_BIND_CARD_LIMIT);
            }
        }
    }

    @Override
    public APIYJZFFirstPaymentResponseDTO firstPayRequest(APIYJZFFirstPaymentRequestDTO requestDTO, PaymentRequest paymentRequest, CashierUserInfo user) {
        APIYJZFFirstPaymentResponseDTO responseDTO = new APIYJZFFirstPaymentResponseDTO();
        //查找或新建paymentRecord
        PaymentRecord paymentRecord = handlePaymentRecord(paymentRequest, user, CardInfoTypeEnum.TEMP, requestDTO.getCardInfoDTO(), null);
        if (PayRecordStatusEnum.INIT != paymentRecord.getState()) {
            //获取到非INIT状态的record，不再调用PP下单
            LOGGER.info("bindPayRequest() 首次支付下单，获取到已有的可用record，直接返回。recordId={}，短验类型={}，补充项={}", paymentRecord.getId(), paymentRecord.getSmsVerifyType(), paymentRecord.getNeedItem());
            handlerSMSTypeAndNeedItem(SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType()), paymentRecord.getNeedItem(), CardInfoTypeEnum.TEMP, responseDTO);
            responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
            return responseDTO;
        }
        //构建调用PP入参，调用PP支付接口
        NcPayOrderResponseDTO payOrderResponseDTO = orderPaymentService.payProcessorRequestOrder(paymentRequest, paymentRecord, CardInfoTypeEnum.TEMP);
        //更新record，并处理返回结果
        paymentProcessService.updateRecordNo(paymentRecord.getId(), payOrderResponseDTO.getSmsType().name(), payOrderResponseDTO.getRecordNo(),
                PayRecordStatusEnum.ORDERED, payOrderResponseDTO.getNeedItem(),RedirectTypeEnum.NONE.name());
        //处理补充项（取款密码）、是否需短验
        handlerSMSTypeAndNeedItem(payOrderResponseDTO.getSmsType(), payOrderResponseDTO.getNeedItem(), CardInfoTypeEnum.TEMP, responseDTO);
        //处理返回值
        responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
        return responseDTO;
    }

    @Override
    public APIYJZFBindPaymentResponseDTO bindPayRequest(APIYJZFBindPaymentRequestDTO requestDTO, PaymentRequest paymentRequest, CashierUserInfo user) {
        APIYJZFBindPaymentResponseDTO responseDTO = new APIYJZFBindPaymentResponseDTO();
        //查找或新建paymentRecord
        PaymentRecord paymentRecord = handlePaymentRecord(paymentRequest, user, CardInfoTypeEnum.BIND, null, requestDTO.getBindId());
        if (PayRecordStatusEnum.INIT != paymentRecord.getState()) {
            //获取到非INIT状态的record，不再调用PP下单
            LOGGER.info("bindPayRequest() 二次支付下单，获取到已有的可用record，直接返回。recordId={}，短验类型={}，补充项={}", paymentRecord.getId(), paymentRecord.getSmsVerifyType(), paymentRecord.getNeedItem());
            handlerSMSTypeAndNeedItem(SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType()), paymentRecord.getNeedItem(), CardInfoTypeEnum.BIND, responseDTO);
            responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
            return responseDTO;
        }

        //构建调用PP入参，调用PP下单接口
        NcPayOrderResponseDTO payOrderResponseDTO = orderPaymentService.payProcessorRequestOrder(paymentRequest, paymentRecord, CardInfoTypeEnum.BIND);
        //更新record，并处理返回结果
        paymentProcessService.updateRecordNo(paymentRecord.getId(), payOrderResponseDTO.getSmsType().name(), payOrderResponseDTO.getRecordNo(),
                PayRecordStatusEnum.ORDERED, payOrderResponseDTO.getNeedItem(),RedirectTypeEnum.NONE.name());
        //处理补充项、是否需短验
        handlerSMSTypeAndNeedItem(payOrderResponseDTO.getSmsType(), payOrderResponseDTO.getNeedItem(), CardInfoTypeEnum.BIND, responseDTO);
        //处理返回值
        responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
        return responseDTO;
    }

    @Override
    public APIBasicResponseDTO sendSMS(APIYJZFSendSmsRequestDTO requestDTO, PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
        APIBasicResponseDTO responseDTO = new APIBasicResponseDTO();
        //保存临时卡
        long tmpId = 0l;
        if (requestDTO.getCardInfoDTO() != null) {
            CardInfoDTO completedCardInfo = completeCardInfo(requestDTO.getCardInfoDTO(), paymentRecord.getCardNo());
            tmpId = orderPaymentService.addPayTmpCard(completedCardInfo, paymentRequest, null);//获取临时卡id
        }
        requestPayProcessorSendSMS(paymentRecord, tmpId);
        //更新record，并处理返回结果
        paymentProcessService.updateRecordNo(paymentRecord.getId(), paymentRecord.getSmsVerifyType(), paymentRecord.getPaymentOrderNo(),
                PayRecordStatusEnum.SMS_SEND, paymentRecord.getNeedItem(),RedirectTypeEnum.NONE.name());
        return responseDTO;
    }

    @Override
    public APIYJZFConfirmPayResponseDTO confirmPay(APIYJZFConfirmPayRequestDTO requestDTO, PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
        APIYJZFConfirmPayResponseDTO responseDTO = new APIYJZFConfirmPayResponseDTO();
        //保存临时卡
        long tmpId = 0l;
        if (requestDTO.getCardInfoDTO() != null) {
            CardInfoDTO completedCardInfo = completeCardInfo(requestDTO.getCardInfoDTO(), paymentRecord.getCardNo());
            tmpId = orderPaymentService.addPayTmpCard(completedCardInfo, paymentRequest, null);//获取临时卡id
        }
        PayRecordResponseDTO payProcessorResponse = requestPayProcessorConfirmPay(paymentRecord, requestDTO.getVerifyCode(), tmpId,paymentRequest);
        //更新record，并处理返回结果
        updateRecordWhenSuccess(paymentRecord, payProcessorResponse.getCardId());
        LOGGER.info("sendSMS() 确认支付成功，更新record状态为SUCCESS，recordId={}，短验类型={}，补充项={}，支付卡id={}", paymentRecord.getId(), paymentRecord.getSmsVerifyType(), paymentRecord.getNeedItem(), payProcessorResponse.getCardId());
        //绑卡逻辑
        bindCard(paymentRecord, paymentRequest, responseDTO);
        return responseDTO;
    }

    @Override
    public PaymentRecord queryPaymentRecord(OrderDetailInfoModel orderInfo, String recordId, int paymentProcess) {
        PaymentRecord paymentRecord = null;
        RecordCondition compareCondition = null;
        switch (paymentProcess) {
            case 2:
                compareCondition = buildRecordCondition(null, null, orderInfo.getOrderSysNo(), orderInfo.getUniqueOrderNo(), null, OrderAction.API_YJZF_SEND_SMS); //构建condition
                paymentRecord = paymentProcessService.getNonNullPaymentRecord(recordId, compareCondition);
                break;
            case 3:
                //获取paymentRecord
                compareCondition = buildRecordCondition(null, null, orderInfo.getOrderSysNo(), orderInfo.getUniqueOrderNo(), null, OrderAction.API_YJZF_CONFIRM_PAY); //构建condition
                paymentRecord = paymentProcessService.getNonNullPaymentRecord(recordId, compareCondition);
                break;
            default:
                break;
        }
        return paymentRecord;
    }

    /**
     * 构建用户信息，包含外部用户的查询等
     *
     * @param userNo
     * @param userType
     * @param merchantNo
     * @return
     */
    @Override
    public CashierUserInfo buildMemberUser(String userNo, String userType, String merchantNo) {
        if (StringUtils.isBlank(userNo) || StringUtils.isBlank(userType)) {
            return null;
        }
        CashierUserInfo user = new CashierUserInfo();
        if (MemberTypeEnum.YIBAO.name().equals(userType)) {
            //三代会员，查询memberNo，组装数据返回
            user.setType(MemberTypeEnum.YIBAO.name());
            user.setUserNo(userNo);
            user.setUserType(MemberTypeEnum.YIBAO.name());
            String memberNo = personalMemberService.queryValidMemberNo(merchantNo,userNo);
            user.setExternalUserId(memberNo);
            return user;
        }
        //外部用户，查询卡账户
        ExternalUserRequestDTO userRequestDto = new ExternalUserRequestDTO();
        userRequestDto.setMerchantAccount(merchantNo);
        userRequestDto.setIdentityId(userNo);
        userRequestDto.setIdentityType(IdentityType.valueOf(userType));
        ExternalUserDTO externalUser = cwhService.getExternalUser(userRequestDto);
        user.setType(Constant.JOINLY);
        user.setUserNo(userNo);
        user.setExternalUserId(externalUser.getId());
        user.setUserType(userType);
        return user;
    }

    /**
     * 查找或新建paymentRecord
     * @param paymentRequest
     * @param user 用户信息
     * @param cardInfoTypeEnum 首次支付TEMP/二次支付BIND
     * @param cardInfoDTO 卡信息，首次支付时非空
     * @param bindId 绑卡id，二次支付时非空
     * @return
     */
    private PaymentRecord handlePaymentRecord(PaymentRequest paymentRequest, CashierUserInfo user, CardInfoTypeEnum cardInfoTypeEnum, CardInfoDTO cardInfoDTO, String bindId) {
        PaymentRecord paymentRecord = null;
        switch (cardInfoTypeEnum) {
            case TEMP: {
                // 构建paymentRecord的查找条件，并根据recordCondition查找paymentRecord，没有符合条件的paymentRecord时需要重新创建
                RecordCondition compareCondition = buildRecordCondition(cardInfoDTO.getCardno(), null, null, null, CardInfoTypeEnum.TEMP.name(), null); //构建condition
                CardInfoDTO completedCardInfo = completeCardInfo(cardInfoDTO, null);
                PersonHoldCard personHoldCard = buildPersonHoldCard(cardInfoDTO); //构建持卡人信息
                long tmpId = orderPaymentService.addPayTmpCard(completedCardInfo, paymentRequest, null);//获取临时卡id
                String externalUserId = user == null || MemberTypeEnum.YIBAO.name().equals(user.getType()) ? null : user.getExternalUserId();
                paymentRecord = paymentProcessService.createRecordWhenUnexsit(paymentRequest, compareCondition, personHoldCard,
                        CardInfoTypeEnum.TEMP.name(), externalUserId, null, Long.toString(tmpId),null);
                break;
            }
            case BIND: {
                // 构建paymentRecord的查找条件，并根据recordCondition查找paymentRecord，没有符合条件的paymentRecord时需要重新创建
                RecordCondition compareCondition = buildRecordCondition(null, bindId, null, null, CardInfoTypeEnum.BIND.name(), null); //构建condition
                CardInfoDTO completedCardInfo = cashierBindCardService.getCardInfoDTO(bindId);
                PersonHoldCard personHoldCard = buildPersonHoldCard(completedCardInfo); //构建持卡人信息
                String externalUserId = user == null || MemberTypeEnum.YIBAO.name().equals(user.getType()) ? null : user.getExternalUserId();
                paymentRecord = paymentProcessService.createRecordWhenUnexsit(paymentRequest, compareCondition, personHoldCard,
                        CardInfoTypeEnum.BIND.name(), externalUserId, null, null,null);
                break;
            }
            default:
                break;
        }
        return paymentRecord;
    }

    private RecordCondition buildRecordCondition(String cardNo, String bindId, String tradeSysNo, String tradeSysOrderId, String payTypes, OrderAction orderAction) {
        RecordCondition condition = new RecordCondition();
        condition.setPayTool(PayTool.NCPAY.name()); // 要求payTool=一键支付
        if (StringUtils.isNotBlank(payTypes)) {
            String[] payTypeArray = {payTypes};
            condition.setRecordPayTypes(payTypeArray); //payType，针对首次及二次支付下单
        }
        if (StringUtils.isNotBlank(cardNo)) {
            condition.setCardN0(cardNo); // 卡号，针对首次支付下单
        }
        if (StringUtils.isNotBlank(bindId)) {
            condition.setBindId(bindId); // 绑卡id，针对二次支付下单
        }
        if (StringUtils.isNotBlank(tradeSysNo)) {
            condition.setTradeSysNo(tradeSysNo); // tradeSysNo，针对发短验和确认支付
        }
        if (StringUtils.isNotBlank(tradeSysOrderId)) {
            condition.setTradeSysOrderId(tradeSysOrderId); // tradeSysOrderId，针对发短验和确认支付
        }
        if (orderAction != null) {
            condition.setOrderAction(orderAction); // orderAction，针对发短验和确认支付
        }
        return condition;
    }

    /**
     * 构建持卡人信息，包含根据卡号获取到的银行、卡类型等信息
     *
     * @param cardInfoDTO 卡信息DTO，要求已包含卡类型、银行名称、银行编码，以及卡四项信息
     * @return
     */
    private PersonHoldCard buildPersonHoldCard(CardInfoDTO cardInfoDTO) {
        CardInfo cardInfo = null;
        String bankCode = cardInfoDTO.getBankCode();
        String bankName = cardInfoDTO.getBankName();
        String cardType = cardInfoDTO.getCardType();
        //首次支付下单、二次支付下单，已获取过所需卡信息
        cardInfo = new CardInfo();
        BankInfo bank = new BankInfo();
        bank.setBankCode(bankCode);
        bank.setBankName(bankName);
        cardInfo.setBank(bank);
        cardInfo.setCardNo(cardInfoDTO.getCardno());
        cardInfo.setCardType(CardTypeEnum.valueOf(cardType));

        //构建持卡人信息
        PersonHoldCard personHoldCard = new PersonHoldCard();
        personHoldCard.setCard(cardInfo);
        personHoldCard.setPhoneN0(cardInfoDTO.getPhone());
        personHoldCard.setOwner(cardInfoDTO.getName());
        personHoldCard.setIdno(cardInfoDTO.getIdno());
        return personHoldCard;
    }

    /**
     * 下单后处理短验类型、补充项、提交补充项场景等
     *
     * @param smsType
     * @param needItemNum
     * @param cardInfoTypeEnum 首次 or 二次
     * @param responseDTO      当cardInfoTypeEnum=TEMP时，需APIYJZFFirstPaymentResponseDTO；当cardInfoTypeEnum=BIND时，需APIYJZFBindPaymentResponseDTO
     */
    private void handlerSMSTypeAndNeedItem(SmsSendTypeEnum smsType, int needItemNum, CardInfoTypeEnum cardInfoTypeEnum, APIYJZFPaymentResponseDTO responseDTO) {
        //1，处理验证码类型及提交补充项场景
        //需要发验证码的，在请求短验时补充；不需要验证码的，在确认支付时补充
        if (smsType == SmsSendTypeEnum.NONE) {
            //无需验证码->验证码=none;提交补充=确认时
            responseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_NONE);
            responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_CONFIRM);
        } else if (smsType == SmsSendTypeEnum.BANK || smsType == SmsSendTypeEnum.YEEPAY) {
            //短信验证码->验证码=SMS;提交补充=发短验时
            responseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_SMS);
            responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_VERITY);
        } else if (smsType == SmsSendTypeEnum.VOICE || smsType == SmsSendTypeEnum.MERCHANT_SEND) {
            //语音验证码或商户发送验证码->验证码=VOICE;提交补充=发短验时
            responseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_VOICE);
            responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_VERITY);
        } else {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        //2，处理补充项信息
        if (0 == needItemNum) {
            //无需补充项->提交补充=NONE
            responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_NONE);
        }
        NCPayParamMode nCPayParamMode = new NCPayParamMode(needItemNum);
        if (CardInfoTypeEnum.TEMP.equals(cardInfoTypeEnum)) {
            //首次支付，只判断下是否需补充取款密码
            ((APIYJZFFirstPaymentResponseDTO) responseDTO).setNeedBankPWD(nCPayParamMode.needBankPWD());
        } else if (CardInfoTypeEnum.BIND.equals(cardInfoTypeEnum)) {
            //二次支付，需判断各项信息是否需补充
            StringBuilder needItemStr = new StringBuilder();
            if (nCPayParamMode.needIdCardNumber()) {
                needItemStr.append("idnoIsNeed,");
            }
            if (nCPayParamMode.needUserName()) {
                needItemStr.append("ownerIsNeed,");
            }
            if (nCPayParamMode.needBankMobilePhone()) {
                needItemStr.append("phoneNoIsNeed,");
            }
            if (nCPayParamMode.needCvv()) {
                needItemStr.append("cvvIsNeed,");
            }
            if (nCPayParamMode.needAvlidDate()) {
                needItemStr.append("avlidDateIsNeed,");
            }
            if (nCPayParamMode.needBankPWD()) {
                needItemStr.append("bankPWDIsNeed,");
            }
            String s = needItemStr.toString();
            if (StringUtils.isNotBlank(s) && s.length() >= 1) {
                ((APIYJZFBindPaymentResponseDTO) responseDTO).setNeedItems(s.substring(0, s.length() - 1));
            }
        }
    }


    @Override
    public boolean paymentProcessInfoCheck(CardInfoDTO suppliedCardInfo, PaymentRecord paymentRecord, int paymentProcess, String verifyCode) {
        boolean checkNeedItem = false;//是否需校验提交的补充项
        boolean needClearCardInfo = false;//是否需清空补充卡信息
        int needItemNum = paymentRecord.getNeedItem();
        SmsSendTypeEnum ncpaySmsType = SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType());
        switch (paymentProcess) {
            case PAYMENT_PROCESS_SEND_SMS:
                //发短验：
                //1、如下单返回要发短验，且需补充项==>则需校验此步骤提交补充项
                checkNeedItem = !SmsSendTypeEnum.NONE.equals(ncpaySmsType) && needItemNum != 0;
                //2、如下单返回不要发短验，且需补充项，即需要在确认支付时补充==>则请求了发短验时，可以发短验，但要清空提交的补充卡信息
                needClearCardInfo = SmsSendTypeEnum.NONE.equals(ncpaySmsType) && needItemNum != 0 && suppliedCardInfo!=null;
                break;
            case PAYMENT_PROCESS_CONFIRM_PAY:
                //确认支付：
                //1、如下单返回需发短验
                if (!SmsSendTypeEnum.NONE.equals(ncpaySmsType)) {
                    if (paymentRecord.getState() != PayRecordStatusEnum.SMS_SEND) {
                        //1.1请求确认支付时，record必须为已发短验状态
                        throw CommonUtil.handleException(Errors.GET_SMS_FIRST);
                    }
                    if (StringUtils.isBlank(verifyCode)) {
                        //1.2请求确认支付时，入参必须传入验证码
                        throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ", 验证码不能为空");
                    }
                    if(needItemNum != 0 && suppliedCardInfo!=null){
                        //1.3需要在发短验时提交补充项，但请求确认支付时入参中有补充项，需要清空提交的卡信息
                        needClearCardInfo = true;
                    }
                } else if (needItemNum != 0) {
                    //2、如下单返回不需发短验，且需补充项，则需校验此步骤提交补充项；
                    checkNeedItem = true;
                }
                break;
            default:
                break;
        }
        if (checkNeedItem) {
            NCPayParamMode nCPayParamMode = new NCPayParamMode(needItemNum);
            if(suppliedCardInfo==null){
                throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",请提交所需卡信息");
            }
            if (nCPayParamMode.needIdCardNumber() && StringUtils.isBlank(suppliedCardInfo.getIdno())) {
                throw new CashierBusinessException(Errors.ID_NOT_NULL);
            }
            if (nCPayParamMode.needUserName() && StringUtils.isBlank(suppliedCardInfo.getName())) {
                throw new CashierBusinessException(Errors.NAME_NOT_NULL);
            }
            if (nCPayParamMode.needBankMobilePhone() && StringUtils.isBlank(suppliedCardInfo.getPhone())) {
                throw new CashierBusinessException(Errors.PHONE_NOT_NULL);
            }
            if (nCPayParamMode.needCvv() && StringUtils.isBlank(suppliedCardInfo.getCvv2())) {
                throw new CashierBusinessException(Errors.CVV2_NOT_NULL);
            }
            if (nCPayParamMode.needAvlidDate() && StringUtils.isBlank(suppliedCardInfo.getValid())) {
                throw new CashierBusinessException(Errors.VALIDATE_NOT_NULL);
            }
            if (nCPayParamMode.needBankPWD() && StringUtils.isBlank(suppliedCardInfo.getPass())) {
                throw new CashierBusinessException(Errors.PASSWORD_NOT_NULL);
            }
        }
        return needClearCardInfo;
    }

    /**
     * 封装参数，并请求PP发短验
     *
     * @param paymentRecord
     * @param tmpCardId
     */
    private void requestPayProcessorSendSMS(PaymentRecord paymentRecord, long tmpCardId) {
        NcSmsRequestDTO requestDTO = new NcSmsRequestDTO();
        requestDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
        //注：pay_record中保存的验证码类型，为SmsSendTypeEnum枚举值的字符串，此处要调用PP接口，->
        //需先根据逻辑转换成nccashier.enumtype.ReqSmsSendTypeEnum枚举的值，再等价转换成ncpay.enumtype.ReqSmsSendTypeEnum的枚举值
        ReqSmsSendTypeEnum reqSmsSendTypeEnum = CommonUtil.transferBindPaySMSType(SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType()));
        if (reqSmsSendTypeEnum == null) {
            LOGGER.info("requestPayProcessorSendSMS() 下单返回无需短验，商户仍请求发短验接口，将发送易宝短验");
            requestDTO.setSmsSendType(com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum.YEEPAY);
        } else {
            requestDTO.setSmsSendType(com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum.valueOf(reqSmsSendTypeEnum.name()));
        }
        if (tmpCardId != 0) {
            requestDTO.setTmpCardId(tmpCardId);
        }
        payProcessorService.verifyAndSendSms(requestDTO);
    }

    /**
     * 封装参数，并请求PP确认支付
     *
     * @param paymentRecord
     * @param tmpCardId
     */
    private PayRecordResponseDTO requestPayProcessorConfirmPay(PaymentRecord paymentRecord, String verifyCode, Long tmpCardId,PaymentRequest payRequest) {
        PayRecordStatusEnum origPayRecordStatusEnum = paymentRecord.getState();
        try {
            paymentProcessService.avoidRepeatPayWithException(paymentRecord, new PayRecordStatusEnum[]{PayRecordStatusEnum.ORDERED, PayRecordStatusEnum.SMS_SEND});
            NcPayConfirmRequestDTO confirmDTO = new NcPayConfirmRequestDTO();
            confirmDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
            confirmDTO.setSmsCode(verifyCode);
            if (tmpCardId != null && tmpCardId > 0) {
                confirmDTO.setTmpCardId(tmpCardId);
            }

            JSONObject extendInfoJson = CommonUtil.parseJson(payRequest.getExtendInfo());
            if(extendInfoJson !=null && extendInfoJson.get("groupTag") != null){
                Map<String, String> extMap = new HashMap<String, String>();
                extMap.put("groupTag",(String) extendInfoJson.get("groupTag"));
                confirmDTO.setExtParam(extMap);
            }

            PayRecordResponseDTO payProcessorResponse = payProcessorService.synConfirmPay(confirmDTO);
            return payProcessorResponse;
        } catch (CashierBusinessException e) {
            String errorCode = e.getDefineCode();
            if ("N400094".equals(errorCode) || "N400091".equals(errorCode)) {
                paymentProcessService.recoverRecordToObjStatus(paymentRecord.getId(), origPayRecordStatusEnum, PayRecordStatusEnum.PAYING);
            }
            throw e;
        }
    }

    /**
     * 首次支付，确认支付成功后，绑卡逻辑
     *
     * @param paymentRecord
     * @param paymentRequest
     * @param responseDTO
     * @return 绑卡id
     */
    private void bindCard(PaymentRecord paymentRecord, PaymentRequest paymentRequest, APIYJZFConfirmPayResponseDTO responseDTO) {
        if (StringUtils.isBlank(paymentRequest.getIdentityId()) && StringUtils.isBlank(paymentRequest.getIdentityType())) {
            //下单时未传入用户类型及用户号
            return;
        }
        String bindId = cashierBindCardService.bindCard(paymentRequest, paymentRecord);
        if (StringUtils.isNotBlank(bindId)) {
            responseDTO.setBindId(bindId);
        }
    }

    /**
     * 确认支付成功后，更新record为成功状态，且保存卡id
     *
     * @param paymentRecord
     * @param cardId        卡信息id
     */
    private void updateRecordWhenSuccess(PaymentRecord paymentRecord, String cardId) {
        paymentRecord.setState(PayRecordStatusEnum.SUCCESS);
        paymentRecord.setCardInfoId(cardId);
        paymentProcessService.updateRecord(paymentRecord);
    }

    /**
     * 首次支付下单/发短验/确认支付时调用，完善卡信息，包括卡类型、银行编码、银行名称等，用于后续保存record及保存临时卡；
     * @param cardInfoDTO  不完整的卡信息，首次支付下单时必含卡号
     * @param recordCardNo record中已保存的卡号，发短验/确认支付时传入
     * @return 补充完整了的卡信息
     */
    private CardInfoDTO completeCardInfo(CardInfoDTO cardInfoDTO, String recordCardNo) {
        String cardNo = StringUtils.isNotBlank(recordCardNo) ? recordCardNo : cardInfoDTO.getCardno();
        if(StringUtils.isBlank(recordCardNo)){
            //下单时调用此方法，需要获取卡类型、银行编码、银行名称
            CardBinDTO cardBinDTO = cashierBankCardService.getCardBinInfo(cardNo);
            if(cardBinDTO==null){
                throw new CashierBusinessException(Errors.INVALID_BANK_CARD_NO);
            }
            String cardType = cardBinDTO.getCardType().name();
            BankEnum bankEnum = cardBinDTO.getBankEnum();
            String bankCode = bankEnum.name();
            String bankName = bankEnum.getName();
            cardInfoDTO.setCardType(cardType);
            cardInfoDTO.setBankCode(bankCode);
            cardInfoDTO.setBankName(bankName);
        }
        cardInfoDTO.setCardno(cardNo);
        return cardInfoDTO;
    }

    public static void main(String[] args) {
        StringBuilder needItemStr = new StringBuilder();
        needItemStr.append(",");
/*        needItemStr.append("ownerIsNeed,");*/

        String s = needItemStr.toString();
        if (StringUtils.isNotBlank(s) && s.length() >= 1) {
            System.out.println(s.substring(0, s.length() - 1));
        }
    }
}
