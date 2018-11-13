package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.InstallmentPayTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.GuaranteeInstallmentService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.CardInfo;
import com.yeepay.g3.core.nccashier.vo.ExternalUserRequestDTO;
import com.yeepay.g3.core.nccashier.vo.PersonHoldCard;
import com.yeepay.g3.core.nccashier.vo.RecordCondition;
import com.yeepay.g3.facade.cwh.param.ExternalUserDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class GuaranteeInstallmentServiceImpl extends NcCashierBaseService implements GuaranteeInstallmentService {

    private static Logger logger = NcCashierLoggerFactory.getLogger(GuaranteeInstallmentServiceImpl.class);

    @Resource
    private PaymentProcessService paymentProcessService;

    @Resource
    private CwhService cwhService;

    @Resource
    private PayProcessorService payProcessorService;


    @Override
    public NcGuaranteeCflPrePayResponseDTO callGuaranteeInstallmentPrePay(PaymentRequest paymentRequest, GuaranteeInstallmentPrePayRequestDTO requestDTO) {
        NcGuaranteeCflPrePayRequestDTO  authCflPrePayRequestDTO = buildPrePayRequestDTO(paymentRequest, requestDTO);
        NcGuaranteeCflPrePayResponseDTO responseDTO = payProcessorService.guaranteeCflPrePay(authCflPrePayRequestDTO);
        return responseDTO;
    }

    /**
     * 构建PP的担保分期预路由接口入参
     * @param paymentRequest
     * @param requestDTO
     * @return
     */
    private NcGuaranteeCflPrePayRequestDTO buildPrePayRequestDTO(PaymentRequest paymentRequest, GuaranteeInstallmentPrePayRequestDTO requestDTO){
        NcGuaranteeCflPrePayRequestDTO  payProcessorRequestDTO = new NcGuaranteeCflPrePayRequestDTO ();
        //通用参数
        buildBasicRequestDTO(paymentRequest,payProcessorRequestDTO);
        String riskInfo = buildTradeRiskInfoUseTokenAndRequest(requestDTO.getToken(),paymentRequest);
        payProcessorRequestDTO.setGoodsInfo(riskInfo);
        payProcessorRequestDTO.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
        payProcessorRequestDTO.setPayProduct(PayTool.DBFQ.name());
        payProcessorRequestDTO.setPayOrderType(PayOrderType.GUAR_CFL);
        payProcessorRequestDTO.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
        payProcessorRequestDTO.setBasicProductCode(CommonUtil.getBasicProductCode(PayTool.DBFQ.name(), paymentRequest.getTradeSysNo()));
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        payProcessorRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));
        //担保分期业务参数
        payProcessorRequestDTO.setAccountType(CommonUtil.getBankAccountType(BankAccountTypeEnum.B2C).name());//对公对私
        payProcessorRequestDTO.setSceneType(judgeSceneType(paymentRequest.getCashierVersion()));//场景类型
        payProcessorRequestDTO.setAcceptSignSMS(true);//是否接受签约短验
        payProcessorRequestDTO.setAcceptPaySMS(true);//是否接受支付短验
        payProcessorRequestDTO.setCardType(CardTypeEnum.CREDIT.name());//卡类型
        payProcessorRequestDTO.setBankCode(requestDTO.getBankCode());//银行编码
        payProcessorRequestDTO.setCflCount(requestDTO.getPeriod());//期数
        return payProcessorRequestDTO;
    }

    @Override
    public GuaranteeInstallmentPrePayResponseDTO handleGuaranteeInstallmentInfo(GuaranteeInstallmentPrePayRequestDTO requestDTO, PaymentRequest paymentRequest, NcGuaranteeCflPrePayResponseDTO ncGuaranteeCflPrePayResponseDTO) {
        GuaranteeInstallmentPrePayResponseDTO prePayResponseDTO = new GuaranteeInstallmentPrePayResponseDTO();
        prePayResponseDTO.setBankCode(requestDTO.getBankCode());
        prePayResponseDTO.setPreRouteId(Long.toString(ncGuaranteeCflPrePayResponseDTO.getPreRouteId()));
        //金额及费率相关
        int period = requestDTO.getPeriod();
        //金额，保留2位小数
        BigDecimal orderAmount = paymentRequest.getOrderAmount().setScale(2, RoundingMode.DOWN);
        //手续费率（保留4位小数，以便前端转换为0.00%格式后显示2位小数）
        BigDecimal serviceChargeRate = ncGuaranteeCflPrePayResponseDTO.getPayerInterestRate().setScale(4, RoundingMode.DOWN);
        //手续费，保留2位小数
        BigDecimal serviceCharge = orderAmount.multiply(serviceChargeRate).setScale(2, RoundingMode.DOWN);
        //每期应还，保留2位小数
        BigDecimal amountPerPeriod = orderAmount.add(serviceCharge).divide(new BigDecimal(period),2 , RoundingMode.DOWN);
        prePayResponseDTO.setPeriod(period);
        prePayResponseDTO.setOrderAmount(orderAmount);
        prePayResponseDTO.setServiceChargeRate(serviceChargeRate);
        prePayResponseDTO.setServiceCharge(serviceCharge);
        prePayResponseDTO.setAmountPerPeriod(amountPerPeriod);
        //补充项相关
        Integer requiredVerifyPayItem = ncGuaranteeCflPrePayResponseDTO.getRequiredVerifyPayItem();
        recordSupplyItemInfo(prePayResponseDTO,requiredVerifyPayItem);
        return prePayResponseDTO;
    }

    @Override
    public PaymentRecord createRecordWhenUnexist(PaymentRequest paymentRequest, GuaranteeInstallmentPaymentRequestDTO requestDTO, CardInfo cardInfo) {
        RecordCondition condition = buildGuaranteeInstallmentRecordCondition(paymentRequest, requestDTO);
        PersonHoldCard person = buildGuaranteeInstallmentPersonHoldCard(requestDTO, cardInfo);
        String externalUserId = null;
        if (StringUtils.isNotBlank(paymentRequest.getIdentityId()) && StringUtils.isNotBlank(paymentRequest.getIdentityType())) {
            ExternalUserRequestDTO userReqeustDto = new ExternalUserRequestDTO(paymentRequest);
            ExternalUserDTO externalUser = cwhService.getExternalUser(userReqeustDto);
            externalUserId = externalUser.getId();
        }
        PaymentRecord paymentRecord = paymentProcessService.createRecordWhenUnexsit(paymentRequest, condition, person,
                InstallmentPayTypeEnum.FIRST.name(), externalUserId, requestDTO.getToken(), null,null);
        return paymentRecord;
    }


    @Override
    public NcGuaranteeCflPayResponseDTO callGuaranteeInstallmentPayment(PaymentRequest paymentRequest, PaymentRecord paymentRecord, GuaranteeInstallmentPaymentRequestDTO requestDTO) {
        NcGuaranteeCflPayRequestDTO authCflPaymentRequestDTO = buildPaymentRequestDTO(paymentRequest, requestDTO);
        NcGuaranteeCflPayResponseDTO responseDTO = payProcessorService.guaranteeCflRequest(authCflPaymentRequestDTO);
        return responseDTO;
    }

    @Override
    public GuaranteeInstallmentPaymentResponseDTO updateRecordAndReturn(NcGuaranteeCflPayResponseDTO ncGuaranteeCflPayResponseDTO, PaymentRecord paymentRecord) {
        GuaranteeInstallmentPaymentResponseDTO responseDTO = new GuaranteeInstallmentPaymentResponseDTO();
        JSONObject paymentExt = new JSONObject();
        if (ncGuaranteeCflPayResponseDTO.getNeedRedirect()) {
            //需跳转到通道提供的确认支付页面
            responseDTO.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
            responseDTO.setPayUrl(ncGuaranteeCflPayResponseDTO.getRedirectUrl());
            responseDTO.setMethod(ncGuaranteeCflPayResponseDTO.getMethod());
            responseDTO.setEncoding(ncGuaranteeCflPayResponseDTO.getEncoding());
            responseDTO.setParamMap(ncGuaranteeCflPayResponseDTO.getParamMap());
            //paymentExt中保存分期手续费率
            paymentExt.put(Constant.GUARANTEE_INSTALLMENT_SERVICE_CHARGE_RATE, ncGuaranteeCflPayResponseDTO.getPayerInterestRate().toString());
        } else {
            //后续将增加是否需短验的判断；本期只支持跳转，如不可跳转，直接报错
            throw new CashierBusinessException(Errors.INSTALLMENT_PAY_EXCEPTION);
        }
        paymentProcessService.updateRecordStatusAndPaymentExt(paymentRecord.getId(), ncGuaranteeCflPayResponseDTO.getRecordNo(), PayRecordStatusEnum.ORDERED, paymentExt.toJSONString());
        return responseDTO;
    }

    @Override
    public void getSupportBankAndPeriods(BigDecimal orderAmount, InstallmentRouteResponseDTO responseDTO) {

        List<InstallmentBankInfoDTO> usableBankList = new ArrayList<InstallmentBankInfoDTO>();
        // 对于担保分期  所有银行支持限额一样  从统一配置中读取上限或者下限
        List<Long> maxLimitAndMinLimit = CommonUtil.getDbfqMaxLimitAndMinLimit();
        BigDecimal maxLimit = new BigDecimal(maxLimitAndMinLimit.get(0));
        BigDecimal minLimit = new BigDecimal(maxLimitAndMinLimit.get(1));
        if(orderAmount.compareTo(maxLimit) == 1){
            throw new CashierBusinessException(Errors.AMOUNT_HIGHER_MAX);
        }
        if(minLimit.compareTo(orderAmount) == 1){
            throw new CashierBusinessException(Errors.AMOUNT_LOWER_MIN);
        }
        else{
            //目前直接从配置中心查询
            Map<String,List<String>> supportBankAndPeriods = CommonUtil.getSupportBankAndPeriodsUtil();
            Map<String,String> bankCodeAndName = CommonUtil.getSupportBanKNameAndCode();
            if(MapUtils.isNotEmpty(supportBankAndPeriods)){

                for(Map.Entry<String,List<String>> map: supportBankAndPeriods.entrySet()){
                    InstallmentBankInfoDTO installmentBankInfoDTO = new InstallmentBankInfoDTO();
                    BankInfoDTO bankInfoDTO = new BankInfoDTO();
                    bankInfoDTO.setBankCode(map.getKey().toString());
                    bankInfoDTO.setBankName(bankCodeAndName.get(map.getKey().toString()));
                    List<InstallmentPeriodAndRateInfoDTO> list = new ArrayList<InstallmentPeriodAndRateInfoDTO>();
                    for(String s:map.getValue()){
                        InstallmentPeriodAndRateInfoDTO installmentPeriodAndRateInfoDTO = new InstallmentPeriodAndRateInfoDTO();
                        installmentPeriodAndRateInfoDTO.setPeriod(s);
                        list.add(installmentPeriodAndRateInfoDTO);
                    }
                    installmentBankInfoDTO.setBank(bankInfoDTO);
                    installmentBankInfoDTO.setNumsAndRates(list);
                    usableBankList.add(installmentBankInfoDTO);
                }

            }
            responseDTO.setUsableBankList(usableBankList);
        }
    }

    /**
     * 构建PP的担保分期支付下单接口入参
     * @param paymentRequest
     * @param requestDTO
     * @return
     */
    private NcGuaranteeCflPayRequestDTO  buildPaymentRequestDTO(PaymentRequest paymentRequest, GuaranteeInstallmentPaymentRequestDTO requestDTO) {
        NcGuaranteeCflPayRequestDTO  payProcessorRequestDTO = new NcGuaranteeCflPayRequestDTO ();
        //通用参数
        buildBasicRequestDTO(paymentRequest,payProcessorRequestDTO);
        String riskInfo = buildTradeRiskInfoUseTokenAndRequest(requestDTO.getToken(),paymentRequest);
        payProcessorRequestDTO.setGoodsInfo(riskInfo);
        payProcessorRequestDTO.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
        payProcessorRequestDTO.setPayProduct(PayTool.DBFQ.name());
        payProcessorRequestDTO.setPayOrderType(PayOrderType.GUAR_CFL);
        payProcessorRequestDTO.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
        payProcessorRequestDTO.setBasicProductCode(CommonUtil.getBasicProductCode(PayTool.DBFQ.name(), paymentRequest.getTradeSysNo()));
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        payProcessorRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));
        //担保分期业务参数
        payProcessorRequestDTO.setAccountType(CommonUtil.getBankAccountType(BankAccountTypeEnum.B2C).name());//对公对私
        payProcessorRequestDTO.setSceneType(judgeSceneType(paymentRequest.getCashierVersion()));//场景类型
        payProcessorRequestDTO.setCardType(CardTypeEnum.CREDIT.name());//卡类型
        payProcessorRequestDTO.setBankCode(requestDTO.getBankCode());//银行编码
        payProcessorRequestDTO.setCflCount(requestDTO.getPeriod());//期数
        payProcessorRequestDTO.setPreRouteId(Long.parseLong(requestDTO.getPreRouteId()));//预路由ID
        payProcessorRequestDTO.setIdType("IDCARD");//证件类型
        payProcessorRequestDTO.setIdNo(requestDTO.getIdNo());//证件号
        payProcessorRequestDTO.setCardNo(requestDTO.getCardNo());//卡号
        payProcessorRequestDTO.setMobileNo(requestDTO.getPhoneNo());//手机号
        payProcessorRequestDTO.setAccountName(requestDTO.getOwner());//姓名
        payProcessorRequestDTO.setCreditCardCvv(requestDTO.getCvv());//cvv
        payProcessorRequestDTO.setCreditCardExpiryDate(requestDTO.getAvlidDate());//有效期
        payProcessorRequestDTO.setBankPwd(requestDTO.getBankPWD());//取款密码
        payProcessorRequestDTO.setPageRedirectUrl(CommonUtil.getH5FrontCallbackUrl(paymentRequest.getMerchantNo(), requestDTO.getToken()));//页面回调地址，使用查询页
        payProcessorRequestDTO.setMerchantFeeSubsidy(null); //商户补贴手续费率
        return payProcessorRequestDTO;
    }

    /**
     * 构建RecordCondition
     * @param paymentRequest
     * @param requestDTO
     * @return
     */
    private RecordCondition buildGuaranteeInstallmentRecordCondition(PaymentRequest paymentRequest, GuaranteeInstallmentPaymentRequestDTO requestDTO) {
        RecordCondition condition = new RecordCondition();
        condition.setCashierVersion(CashierVersionEnum.valueOf(paymentRequest.getCashierVersion()));
        condition.setPayTool(PayTool.DBFQ.name());
        String[] recordPayTypes = {InstallmentPayTypeEnum.FIRST.name()};
        condition.setRecordPayTypes(recordPayTypes);
        condition.setToken(requestDTO.getToken());
        condition.setCardN0(requestDTO.getCardNo());
        condition.setPeriod(Integer.toString(requestDTO.getPeriod()));
        return condition;
    }

    /**
     * 构建PersonHoldCard
     * @param requestDTO
     * @param cardInfo
     * @return
     */
    private PersonHoldCard buildGuaranteeInstallmentPersonHoldCard(GuaranteeInstallmentPaymentRequestDTO requestDTO, CardInfo cardInfo) {
        PersonHoldCard personHoldCard = new PersonHoldCard();
        personHoldCard.setCard(cardInfo);
        personHoldCard.setPhoneN0(requestDTO.getPhoneNo());
        personHoldCard.setOwner(requestDTO.getOwner());
        personHoldCard.setIdno(requestDTO.getIdNo());
        return personHoldCard;
    }


    /**
     * 转换需补充项格式，并记录到返回DTO中
     * @param prePayResponseDTO 接口返回DTO
     * @param supplyItemInt 整数表示的需补充项
     */
    private void recordSupplyItemInfo(GuaranteeInstallmentPrePayResponseDTO prePayResponseDTO, int supplyItemInt){
        try {
            if (0 == supplyItemInt) {
                prePayResponseDTO.setNeedSupply("N");
                return;
            }
            prePayResponseDTO.setNeedSupply("Y");

            NCPayParamMode nCPayParamMode = new NCPayParamMode(supplyItemInt);
            if (nCPayParamMode.needUserName()) {
                prePayResponseDTO.setNeedOwner(true);
            }
            if (nCPayParamMode.needAvlidDate()) {
                prePayResponseDTO.setNeedAvlidDate(true);
            }
            if (nCPayParamMode.needCvv()) {
                prePayResponseDTO.setNeedCvv(true);
            }
            if (nCPayParamMode.needIdCardNumber()) {
                prePayResponseDTO.setNeedIdNo(true);
            }
            if (nCPayParamMode.needBankMobilePhone()) {
                prePayResponseDTO.setNeedPhoneNo(true);
            }
            if (nCPayParamMode.needBankPWD()) {
                prePayResponseDTO.setNeedBankPWD(true);
            }
        } catch (Exception e) {
            logger.warn("recordSupplyItemInfo() supplyItemInt = "+supplyItemInt+" ,error:", e);
            throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
        }
    }

    /**
     * 根据cashierVersion判断sceneType
     * @return MB/PC/API
     */
    private String judgeSceneType(String version){
        if("WAP".equals(version)){
            return "MB";
        }else if("PC".equals(version)){
            return "PC";
        }else if ("API".equals(version)){
            return "API";
        }else {
            return null;
        }
    }
}
