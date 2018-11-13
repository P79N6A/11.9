package com.yeepay.g3.core.nccashier.service.impl.api;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.APICashierOpenAndPassivePayService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.ExtendInfoFromPayRequest;
import com.yeepay.g3.facade.frontend.dto.PromotionInfoDTO;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.BankPromotionInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.IdCardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.payprocessor.dto.BasicRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.BasicResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * API收银台-被扫支付-模板实现
 * Created by ruiyang.du on 2017/6/28.
 */
@Service("MSCANPAY_UnifiedAPICashierService")
public class APICashierPassiveScanServiceImpl extends APICashierPaymentBaseTemplate implements APICashierOpenAndPassivePayService {

    private Logger logger = NcCashierLoggerFactory.getLogger(APICashierPassiveScanServiceImpl.class);
    @Resource
    private PayProcessorService payProcessorService;

    @Override
    public UnifiedAPICashierResponseDTO pay(UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        return doOpenAndPassivePay(apiCashierRequestDTO);
    }

    @Override
    protected <T> void paramValidate(T apiCashierRequestDTO) {
        UnifiedAPICashierRequestDTO passivePayRequestDTO = (UnifiedAPICashierRequestDTO)apiCashierRequestDTO;
        if (StringUtils.isEmpty(passivePayRequestDTO.getMerchantStoreNo())) {
            logger.error("paramValidate()参数校验不通过，merchantStoreNo为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，merchantStoreNo为空");
        }
        if (StringUtils.isEmpty(passivePayRequestDTO.getMerchantTerminalId())) {
            logger.error("paramValidate()参数校验不通过，merchantTerminalId为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，merchantTerminalId为空");
        }
        if (StringUtils.isEmpty(passivePayRequestDTO.getPayEmpowerNo())) {
            logger.error("paramValidate()参数校验不通过，payEmpowerNo为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，payEmpowerNo为空");
        }
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
    @Override
    protected <T> PaymentRecord handlePaymentRecord(PaymentRequest paymentRequest, T apiCashierRequest, String payTool, String payType,String payMerchantNo) {
        if (paymentRequest == null || StringUtils.isBlank(payTool) || StringUtils.isBlank(payType) || apiCashierRequest == null ) {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
//        UnifiedAPICashierRequestDTO openPayRequest = (UnifiedAPICashierRequestDTO)apiCashierRequest;
        PaymentRecord paymentRecord = queryPaymentRecord(paymentRequest,payType);
        if (null != paymentRecord) {
            return paymentRecord;
        }
        logger.info("paymentRecord需要重新创建");
        paymentRecord = new PaymentRecord();
        paymentRecord.setAreaInfo(paymentRequest.getAreaInfo());
        paymentRecord.setBizModeCode(paymentRequest.getBizModeCode());
        paymentRecord.setCreateTime(new Date());
        paymentRecord.setIdCardType(IdCardTypeEnum.IDENTITY.toString());
        paymentRecord.setMcc(paymentRequest.getIndustryCatalog());
        paymentRecord.setMerchantName(paymentRequest.getMerchantName());
        paymentRecord.setMerchantNo(paymentRequest.getMerchantNo());
        paymentRecord.setMerchantOrderId(paymentRequest.getMerchantOrderId());
        paymentRecord.setPaymentAmount(paymentRequest.getOrderAmount());
        paymentRecord.setPaymentRequestId(paymentRequest.getId());
        paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
        paymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
        paymentRecord.setPayType(payType);// 商户传的支付类型
        paymentRecord.setPayTool(payTool);// 商户传的支付工具
        paymentRecord.setProductName(paymentRequest.getProductName());
        paymentRecord.setState(PayRecordStatusEnum.INIT);
        paymentRecord.setTokenId(UUID.randomUUID().toString());
        paymentRecord.setTradeSysNo(paymentRequest.getTradeSysNo());
        paymentRecord.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
        paymentRecord.setOrderOrderId(paymentRequest.getOrderOrderId());
        paymentRecord.setOrderSysNo(paymentRequest.getOrderSysNo());
        paymentRecord.setMemberNo(paymentRequest.getMemberNo());
        paymentRecord.setMemberType(StringUtils.isNotBlank(paymentRequest.getMemberType()) ? paymentRequest.getMemberType() : Constant.JOINLY);
        paymentRecord.setUpdateTime(new Date());
        //实际设备码128以内，超出了cardInfoId的长度
//        paymentRecord.setCardInfoId(openPayRequest.getMerchantTerminalId());//被扫的设备码
        paymentRecord.setVersion(paymentRequest.getVersion());
        long paymentRecordId = paymentProcessService.savePaymentRecord(paymentRecord);
        paymentRecord.setId(paymentRecordId);
        return paymentRecord;
    }

    /**
     * payRecord的补充校验，校验不通过时，需重新生成payRecord
     *
     * @param paymentRecord
     * @param
     * @return true if pass
     */
//    private boolean payRecordAvailable(PaymentRecord paymentRecord, UnifiedAPICashierRequestDTO apiCashierRequest) {
//        if (PayTool.MSCANPAY.name().equals(paymentRecord.getPayTool()) && apiCashierRequest.getPayType().equals(paymentRecord.getPayType())) {
//            //被扫的特有逻辑，授权码校验
//            return true;
//        }
//        return false;
//    }

    @Override
    protected <O extends BasicRequestDTO,I> O prepareToCallPayProcessor(PaymentRecord paymentRecord, I apiRequest, PaymentRequest paymentRequest) {
        PassiveScanPayRequestDTO passiveScanPayRequestDTO = new PassiveScanPayRequestDTO();
        UnifiedAPICashierRequestDTO apiCashierRequest = (UnifiedAPICashierRequestDTO)apiRequest;
        buildCommonRequestDTO(paymentRecord, paymentRequest, passiveScanPayRequestDTO, apiCashierRequest.getUserIp());
        buildPassiveScanRequestDTO(paymentRecord, apiCashierRequest, paymentRequest, passiveScanPayRequestDTO);
        return (O) passiveScanPayRequestDTO;
    }


    @Override
    protected <I extends BasicRequestDTO, O extends BasicResponseDTO> O requestPayProcessorPay(I requestDTO) {
        PassiveScanPayRequestDTO passiveScanPayRequestDTO = (PassiveScanPayRequestDTO) requestDTO;
        PassiveScanPayResponseDTO passiveScanPayResponseDTO = payProcessorService.merchantScanPay(passiveScanPayRequestDTO);
        return (O) passiveScanPayResponseDTO;
    }

    @Override
    protected <O extends BasicResponseDTO> void updatePaymentRecord(O response, PaymentRecord record) {
        if (null == response) {
            return;
        }
        PassiveScanPayResponseDTO passiveScanPayResponseDTO = (PassiveScanPayResponseDTO) response;
        if (passiveScanPayResponseDTO.getProcessStatus() == ProcessStatus.SUCCESS) {
            record.setPaymentOrderNo(passiveScanPayResponseDTO.getRecordNo());
            record.setBankChannelNo(passiveScanPayResponseDTO.getBankId());
            record.setBankCode(passiveScanPayResponseDTO.getBankId());
            record.setTradeSerialNo(passiveScanPayResponseDTO.getBankTrxId());
            record.setBankOrderNo(passiveScanPayResponseDTO.getBankOrderNo());
            record.setState(PayRecordStatusEnum.ORDERED);
            paymentProcessService.updateRecord(record);
        }

    }

    @Override
    protected <P extends BasicResponseDTO, A, O> O buildResponse(P payProcessorResponse, A apiRequest, PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
        UnifiedAPICashierRequestDTO apiCashierRequestDTO = (UnifiedAPICashierRequestDTO) apiRequest;
        UnifiedAPICashierResponseDTO unifiedAPICashierResponseDTO = buildBasicResponse(apiCashierRequestDTO);
        if (payProcessorResponse instanceof PassiveScanPayResponseDTO ){
            //added by zengzhi.han 20181016  一些非重要的扩展参数，比如 被扫是否需要密码(isNeedPassword)
            unifiedAPICashierResponseDTO.setExtParamMap(JSONObject.toJSONString(((PassiveScanPayResponseDTO)payProcessorResponse).getExtParam()));
            //added by zengzhi.han 20181024 优惠券信息
            BigDecimal cashFee = ((PassiveScanPayResponseDTO)payProcessorResponse).getCashFee();
            unifiedAPICashierResponseDTO.setCashFee(cashFee);
            BigDecimal settlementFee = ((PassiveScanPayResponseDTO)payProcessorResponse).getSettlementFee();
            unifiedAPICashierResponseDTO.setSettlementFee(settlementFee);
            List<PromotionInfoDTO> promotionInfoDTOS = ((PassiveScanPayResponseDTO)payProcessorResponse).getPromotionInfoDTOS();
            List<BankPromotionInfoDTO> bankPromotionInfoDTOS = null;
            if (promotionInfoDTOS!=null&&!promotionInfoDTOS.isEmpty()){
                bankPromotionInfoDTOS = new LinkedList<BankPromotionInfoDTO>();
                for (PromotionInfoDTO temp:promotionInfoDTOS){
                    BankPromotionInfoDTO bankPromotionInfoDTO  = new BankPromotionInfoDTO();
                    BeanUtils.copyProperties(temp,bankPromotionInfoDTO);
                    bankPromotionInfoDTOS.add(bankPromotionInfoDTO);
                }
            }
            unifiedAPICashierResponseDTO.setBankPromotionInfoDTOS(bankPromotionInfoDTOS);
        }
        saveAPIResponseIntoCache(unifiedAPICashierResponseDTO, paymentRecord.getId());
        return (O) unifiedAPICashierResponseDTO;
    }

    /**
     * 构建PP被扫支付接口的专有入参
     *
     * @param paymentRecord
     * @param apiCashierRequest
     * @param paymentRequest
     * @param passiveScanPayRequestDTO
     */
    private void buildPassiveScanRequestDTO(PaymentRecord paymentRecord, UnifiedAPICashierRequestDTO apiCashierRequest, PaymentRequest paymentRequest, PassiveScanPayRequestDTO passiveScanPayRequestDTO) {
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        passiveScanPayRequestDTO.setMerchantStoreNo(apiCashierRequest.getMerchantStoreNo());
        passiveScanPayRequestDTO.setMerchantTerminalId(apiCashierRequest.getMerchantTerminalId());
        passiveScanPayRequestDTO.setBasicProductCode(CommonUtil.getBasicProductCode(paymentRecord.getPayTool(), paymentRecord.getTradeSysNo()));
        passiveScanPayRequestDTO.setCustomerLevel(Constant.CUSTOMER_LEVEL_V);
        passiveScanPayRequestDTO.setDeviceInfo(null);
        passiveScanPayRequestDTO.setPayBusinessType(PayBusinessType.DC);
        passiveScanPayRequestDTO.setPayEmpowerNo(apiCashierRequest.getPayEmpowerNo());
        passiveScanPayRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));
        passiveScanPayRequestDTO.setPayOrderType(PayOrderType.PASSIVESCAN);
        passiveScanPayRequestDTO.setPayerIp(paymentRequest.getUserIp());
        passiveScanPayRequestDTO.setPlatformType(getFePlatformType(apiCashierRequest.getPayType()));
        ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
        Map<String, String> extParam = new HashMap<String, String>();
        if(StringUtils.isNotBlank(extendInfoFromPayRequest.getSpecifyChannelCodes())){
            extParam.put(Constant.SPECIFY_CHANNEL_CODES,extendInfoFromPayRequest.getSpecifyChannelCodes());//业务通道编码，放在extParam传入PP
        }
        if(StringUtils.isNotBlank(extendInfoFromPayRequest.getAreaCode())){
            extParam.put(Constant.AREA_CODE,extendInfoFromPayRequest.getAreaCode());//区域编码，放在extParam传入PP
        }
        if(StringUtils.isNotBlank(extendInfoFromPayRequest.getReportFee())){
            extParam.put(Constant.REPORT_FEE,extendInfoFromPayRequest.getReportFee());//报备费率，放在extParam传入PP
        }
        //added by zengzhi.han 20181016 新增 哆啦宝粉丝路由-微信被扫和支付宝被扫支付透传 reportId
        if (PayTypeEnum.WECHAT_SCAN.name().equals(apiCashierRequest.getPayType())
                ||PayTypeEnum.ALIPAY_SCAN.name().equals(apiCashierRequest.getPayType())){
            String extParamJson = apiCashierRequest.getExtParamMap();
            JSONObject apiExtParam = null;
            if(StringUtils.isNotBlank(extParamJson)){
                try {
                    apiExtParam = JSONObject.parseObject(extParamJson);
                }catch (Exception e){
                    throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，apiExtParam格式无效");
                }
            }
            extParam.put(Constant.WX_REPORT_ID,apiExtParam == null ? null : apiExtParam.getString(Constant.WX_REPORT_ID));//reportId，放在extParam传入PP
            extParam.put(Constant.WX_FOCUS_APP_ID,apiExtParam == null ? null : apiExtParam.getString(Constant.WX_FOCUS_APP_ID));//focusAppId，放在extParam传入PP
        }
        extParam.put(Constant.PARENT_MERCHANT_NO,paymentRequest.getParentMerchantNo());
        passiveScanPayRequestDTO.setExtParam(extParam);
        passiveScanPayRequestDTO.setAppId(extendInfoFromPayRequest.getOrigAppId());
    }


}
