/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.nccashier.service.impl.api;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.enumtype.SynTypeEnum;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.service.APICflEasyService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.ncconfig.enumtype.BankEnum;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 类名称: APICflEasyServiceImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/4 下午3:54
 * @version: 1.0.0
 */
@Service
public class APICflEasyServiceImpl extends NcCashierBaseService implements APICflEasyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(APICflEasyServiceImpl.class);

    @Resource
    private MerchantVerificationService merchantVerificationService;
    @Autowired
    private CashierBankCardService cashierBankCardService;
    @Resource
    private BankCardLimitInfoService bankCardLimitInfoService;
    @Resource
    private CwhService cwhService;
    @Resource
    private PaymentProcessService paymentProcessService;
    @Resource
    private PayProcessorService payProcessorService;
    @Autowired
    private OrderPaymentService orderPaymentService;
    @Autowired
    private CashierBindCardService cashierBindCardService;


    /** 分期易产品信息 */
    private static ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.ZF_FQY, PayTypeEnum.ZF_FQY_API);
    /** 分期易支付流程--第二步，发送短验 */
    private static final int PAYMENT_PROCESS_SEND_SMS = 2;
    /** 分期易支付流程--第三步，确认支付 */
    private static final int PAYMENT_PROCESS_CONFIRM_PAY = 3;

    /**
     * 开通产品校验
     * @param merchantNo
     * @param transactionType
     * @return
     */
    @Override
    public MerchantInNetConfigResult verifyProductOpen(String merchantNo, TransactionTypeEnum transactionType) {
        VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
        requestParam.setMerchantNo(merchantNo);
        requestParam.setProductLevel(productLevel);
        requestParam.setTransactionType(transactionType);
        MerchantInNetConfigResult merchantInNetConfig = merchantVerificationService.verifyMerchantAuthority(requestParam);
        return merchantInNetConfig;
    }

    @Override
    public APICflEasyPaymentResponseDTO firstPayRequest(APICflEasyFirstRequestDTO requestDTO, PaymentRequest paymentRequest, CashierUserInfo user) {
        APICflEasyPaymentResponseDTO responseDTO = new APICflEasyPaymentResponseDTO();
        //查找或新建paymentRecord
        PaymentRecord paymentRecord = handlePaymentRecord(paymentRequest, user, CardInfoTypeEnum.TEMP, requestDTO.getCardInfoDTO(), null, requestDTO.getPeriod());
        if (PayRecordStatusEnum.INIT != paymentRecord.getState()) {
            //获取到非INIT状态的record，不再调用PP下单
            LOGGER.info("cflEasyfirstPayRequest() 首次支付下单，获取到已有的可用record，直接返回。recordId={}，短验类型={}，补充项={}", paymentRecord.getId(), paymentRecord.getSmsVerifyType(), paymentRecord.getNeedItem());
            handlerSMSTypeAndNeedItem(SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType()), paymentRecord.getNeedItem(), null, responseDTO);
            responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
            return responseDTO;
        }
        //构建调用PP入参，调用PP下单接口
        String riskInfo = buildTradeRiskInfoByUseripAndRequest(paymentRequest.getUserIp(), paymentRequest);
        NcCflEasyRequestDTO ncCflEasyRequestDTO = buildNcCflEasyRequestDTO(paymentRequest, paymentRecord, CardInfoTypeEnum.TEMP ,riskInfo,null);
        NcCflEasyResponseDTO ncCflEasyResponseDTO = payProcessorService.cflEasyCreatePayment(ncCflEasyRequestDTO);
        //更新record，并处理返回结果
        paymentProcessService.updateRecordNo(paymentRecord.getId(), ncCflEasyResponseDTO.getSmsType().name(), ncCflEasyResponseDTO.getRecordNo(),
                PayRecordStatusEnum.ORDERED, ncCflEasyResponseDTO.getNeedItem(),RedirectTypeEnum.NONE.name());
        //处理补充项（取款密码）、是否需短验
        handlerSMSTypeAndNeedItem(ncCflEasyResponseDTO.getSmsType(), ncCflEasyResponseDTO.getNeedItem(), ncCflEasyResponseDTO.getPayerInterestRate(), responseDTO);
        //处理返回值
        responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
        return responseDTO;
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
    private PaymentRecord handlePaymentRecord(PaymentRequest paymentRequest, CashierUserInfo user, CardInfoTypeEnum cardInfoTypeEnum, CardInfoDTO cardInfoDTO, String bindId, String period) {
        PaymentRecord paymentRecord = null;
        switch (cardInfoTypeEnum) {
            case TEMP: {
                // 构建paymentRecord的查找条件，并根据recordCondition查找paymentRecord，没有符合条件的paymentRecord时需要重新创建
                RecordCondition compareCondition = buildRecordCondition(cardInfoDTO.getCardno(), null, null, null, CardInfoTypeEnum.TEMP.name(), period, null); //构建condition
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
                RecordCondition compareCondition = buildRecordCondition(null, bindId, null, null, CardInfoTypeEnum.BIND.name(), period, null); //构建condition
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


    private RecordCondition buildRecordCondition(String cardNo, String bindId, String tradeSysNo, String tradeSysOrderId, String payTypes, String period, OrderAction orderAction) {
        RecordCondition condition = new RecordCondition();
        condition.setPayTool(PayTool.ZF_FQY.name()); // 要求payTool=一键支付
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
        if (StringUtils.isNotBlank(period)) {
            condition.setPeriod(period);
        }
        return condition;
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

    /**
     * 构建持卡人信息，包含根据卡号获取到的银行、卡类型等信息
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
        cardInfo.setCardType(com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum.valueOf(cardType));

        //构建持卡人信息
        PersonHoldCard personHoldCard = new PersonHoldCard();
        personHoldCard.setCard(cardInfo);
        personHoldCard.setPhoneN0(cardInfoDTO.getPhone());
        personHoldCard.setOwner(cardInfoDTO.getName());
        personHoldCard.setIdno(cardInfoDTO.getIdno());
        return personHoldCard;
    }

    /**
     * 构造pp下单请求
     */
    private NcCflEasyRequestDTO buildNcCflEasyRequestDTO(PaymentRequest paymentRequest,PaymentRecord payRecord,
                                                           CardInfoTypeEnum type,String riskInfo,CardInfoDTO cardInfo) throws CashierBusinessException {
        NcCflEasyRequestDTO param = new NcCflEasyRequestDTO();
        param.setGoodsInfo(riskInfo);
        buildBasicRequestDTO(paymentRequest,param);
        param.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
        param.setPayProduct(PayTool.ZF_FQY.name());
        param.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
        param.setPayScene(paymentRequest.getBizModeCode());
        param.setPayOrderType(PayOrderType.CFL_EASY);
        param.setMemberType(MemberTypeEnum.valueOf(payRecord.getMemberType()));
        param.setMemberNO(payRecord.getMemberNo());
        param.setCardInfoType(type);
        param.setCardInfoId(StringUtils.isNotBlank(payRecord.getBindId()) ? Long.parseLong(payRecord.getBindId()) : null);
        param.setPayTool(payRecord.getPayProductCode());
        param.setCflCount(payRecord.getPeriod());
        if (CardInfoTypeEnum.TEMP.equals(type) && cardInfo != null) {
            param.setCardInfoId(null);
            param.setBankCardInfoDTO(cardInfo.transferPayProcessBankCardInfoDTO());

        }
		/*设置零售产品码和基础产品码*/
        param.setBasicProductCode(CommonUtil.getBasicProductCode(param.getPayProduct(), paymentRequest.getTradeSysNo()));
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        param.setRetailProductCode(jsonObject.getString("saleProductCode"));
        return param;
    }


    /**
     * 下单后处理短验类型、补充项、提交补充项场景等
     * @param smsType
     * @param needItemNum
     * @param responseDTO      当cardInfoTypeEnum=TEMP时，需APIYJZFFirstPaymentResponseDTO；当cardInfoTypeEnum=BIND时，需APIYJZFBindPaymentResponseDTO
     */
    private void handlerSMSTypeAndNeedItem(SmsSendTypeEnum smsType, int needItemNum, BigDecimal payerRate, APICflEasyPaymentResponseDTO responseDTO) {
        //1，处理验证码类型及提交补充项场景
        //需要发验证码的，在请求短验时补充；不需要验证码的，在确认支付时补充
        if (smsType == SmsSendTypeEnum.NONE) {
            //无需验证码->验证码=none;提交补充=确认时
            responseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_NONE);
            responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_CONFIRM);
        } else if (smsType == SmsSendTypeEnum.BANK || smsType == SmsSendTypeEnum.YEEPAY 
        		|| smsType == SmsSendTypeEnum.BANK_PAY_SMS
        		) {
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
        //2，判断是否需要补充项信息
        if (0 == needItemNum) {
            //无需补充项->提交补充=NONE
            responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_NONE);
        }
        NCPayParamMode nCPayParamMode = new NCPayParamMode(needItemNum);
        // 3、处理补充项信息
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
            responseDTO.setNeedItems(s.substring(0, s.length() - 1));
        }
        if(payerRate != null) {
            responseDTO.setPayerRate(payerRate.setScale(4, RoundingMode.DOWN));
        }

    }


    @Override
    public APICflEasyPaymentResponseDTO bindPayRequest(APICflEasyBindRequestDTO requestDTO, PaymentRequest paymentRequest, CashierUserInfo user) {
        APICflEasyPaymentResponseDTO responseDTO = new APICflEasyPaymentResponseDTO();
        //查找或新建paymentRecord
        PaymentRecord paymentRecord = handlePaymentRecord(paymentRequest, user, CardInfoTypeEnum.BIND, null, requestDTO.getBindId(), requestDTO.getPeriod());
        if (PayRecordStatusEnum.INIT != paymentRecord.getState()) {
            //获取到非INIT状态的record，不再调用PP下单
            LOGGER.info("cflEasyBindPay() 二次支付下单，获取到已有的可用record，直接返回。recordId={}，短验类型={}，补充项={}", paymentRecord.getId(), paymentRecord.getSmsVerifyType(), paymentRecord.getNeedItem());
            handlerSMSTypeAndNeedItem(SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType()), paymentRecord.getNeedItem(), null, responseDTO);
            responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
            return responseDTO;
        }
        // 构建调用pp入参，调用pp下单接口
        String riskInfo = buildTradeRiskInfoByUseripAndRequest(paymentRequest.getUserIp(), paymentRequest);
        NcCflEasyRequestDTO ncCflEasyRequestDTO = buildNcCflEasyRequestDTO(paymentRequest, paymentRecord,CardInfoTypeEnum.BIND ,riskInfo,null);
        NcCflEasyResponseDTO ncCflEasyResponseDTO = payProcessorService.cflEasyCreatePayment(ncCflEasyRequestDTO);
        //更新record，并处理返回结果
        paymentProcessService.updateRecordNo(paymentRecord.getId(), ncCflEasyResponseDTO.getSmsType().name(), ncCflEasyResponseDTO.getRecordNo(),
                PayRecordStatusEnum.ORDERED, ncCflEasyResponseDTO.getNeedItem(),RedirectTypeEnum.NONE.name());
        //处理补充项、是否需短验
        handlerSMSTypeAndNeedItem(ncCflEasyResponseDTO.getSmsType(), ncCflEasyResponseDTO.getNeedItem(), ncCflEasyResponseDTO.getPayerInterestRate(), responseDTO);
        //处理返回值
        responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
        return responseDTO;

    }

    @Override
    public PaymentRecord queryPaymentRecord(OrderDetailInfoModel orderInfo, String recordId, int paymentProcess) {
        PaymentRecord paymentRecord = null;
        RecordCondition compareCondition = null;
        switch (paymentProcess) {
            case 2:
                compareCondition = buildRecordCondition(null, null, orderInfo.getOrderSysNo(), orderInfo.getUniqueOrderNo(), null, null, OrderAction.API_YJZF_SEND_SMS); //构建condition
                paymentRecord = paymentProcessService.getNonNullPaymentRecord(recordId, compareCondition);
                break;
            case 3:
                //获取paymentRecord
                compareCondition = buildRecordCondition(null, null, orderInfo.getOrderSysNo(), orderInfo.getUniqueOrderNo(), null, null, OrderAction.API_YJZF_CONFIRM_PAY); //构建condition
                paymentRecord = paymentProcessService.getNonNullPaymentRecord(recordId, compareCondition);
                break;
            default:
                break;
        }
        return paymentRecord;
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
                //2、如下单返回不要发短验，且需补充项，即需要在确认支付时补充==>则请求了发短验时，可以发短验，但要清空提交的补充卡信息     或者   不需要补充项 但传了  清空
                needClearCardInfo = (SmsSendTypeEnum.NONE.equals(ncpaySmsType) && needItemNum != 0 && suppliedCardInfo!=null) || (needItemNum == 0);
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


    @Override
    public APIBasicResponseDTO smsSend(CardInfoDTO cardInfoDTO, PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
        APIBasicResponseDTO responseDTO = new APIBasicResponseDTO();
        //保存临时卡
        long tmpId = 0L;
        if (cardInfoDTO != null) {
            CardInfoDTO completedCardInfo = completeCardInfo(cardInfoDTO, paymentRecord.getCardNo());
            tmpId = orderPaymentService.addPayTmpCard(completedCardInfo, paymentRequest, null);//获取临时卡id
        }
        requestPayProcessorSendSMS(paymentRecord, tmpId);
        //更新record，并处理返回结果
        paymentProcessService.updateRecordNo(paymentRecord.getId(), paymentRecord.getSmsVerifyType(), paymentRecord.getPaymentOrderNo(),
                PayRecordStatusEnum.SMS_SEND, paymentRecord.getNeedItem(),RedirectTypeEnum.NONE.name());
        return responseDTO;
    }


    /**
     * 封装参数，并请求PP发短验
     */
    private void requestPayProcessorSendSMS(PaymentRecord paymentRecord, long tmpCardId) {
        NcCflEasySmsRequestDTO requestDTO = new NcCflEasySmsRequestDTO();
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
        payProcessorService.cflEasySendSms(requestDTO);
    }


    @Override
    public APICflEasyConfirmPayResponseDTO confirmPay(CardInfoDTO cardInfoDTO,String verifyCode, PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
        APICflEasyConfirmPayResponseDTO responseDTO = new APICflEasyConfirmPayResponseDTO();
        //保存临时卡
        long tmpId = 0l;
        if (cardInfoDTO != null) {
            CardInfoDTO completedCardInfo = completeCardInfo(cardInfoDTO, paymentRecord.getCardNo());
            tmpId = orderPaymentService.addPayTmpCard(completedCardInfo, paymentRequest, null);//获取临时卡id
        }
        PayRecordResponseDTO payRecordResponseDTO = requestPayProcessorConfirmPay(paymentRecord,verifyCode, tmpId, paymentRequest);
        //更新record，并处理返回结果
        updateRecordWhenSuccess(paymentRecord, payRecordResponseDTO.getCardId());
        LOGGER.info("cflEasyConfirmPay() 确认支付成功，更新record状态为SUCCESS，recordId={}，短验类型={}，补充项={}，支付卡id={}", paymentRecord.getId(), paymentRecord.getSmsVerifyType(), paymentRecord.getNeedItem(), payRecordResponseDTO.getCardId());
        //绑卡逻辑
        bindCard(paymentRecord, paymentRequest, responseDTO);
        return responseDTO;
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
            NcCflEasyConfirmRequestDTO confirmDTO = new NcCflEasyConfirmRequestDTO();
            confirmDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
            confirmDTO.setSmsCode(verifyCode);
            if (tmpCardId != null && tmpCardId > 0) {
                confirmDTO.setTmpCardId(tmpCardId);
            }
            PayRecordResponseDTO payProcessorResponse = payProcessorService.cflEasySynConfirmPay(confirmDTO);
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
     * 确认支付成功后，更新record为成功状态，且保存卡id
     * @param paymentRecord
     * @param cardId        卡信息id
     */
    private void updateRecordWhenSuccess(PaymentRecord paymentRecord, String cardId) {
        paymentRecord.setState(PayRecordStatusEnum.SUCCESS);
        if(StringUtils.isNotBlank(cardId))
            paymentRecord.setCardInfoId(cardId);
        paymentProcessService.updateRecord(paymentRecord);
    }

    /**
     * 首次支付，确认支付成功后，绑卡逻辑
     *
     * @param paymentRecord
     * @param paymentRequest
     * @param responseDTO
     * @return 绑卡id
     */
    private void bindCard(PaymentRecord paymentRecord, PaymentRequest paymentRequest, APICflEasyConfirmPayResponseDTO responseDTO) {
        if (StringUtils.isBlank(paymentRequest.getIdentityId()) && StringUtils.isBlank(paymentRequest.getIdentityType())) {
            //下单时未传入用户类型及用户号
            return;
        }
        String bindId = cashierBindCardService.bindCard(paymentRequest, paymentRecord);
        if (StringUtils.isNotBlank(bindId)) {
            responseDTO.setBindId(bindId);
        }
    }
}