package com.yeepay.g3.core.nccashier.service.impl.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.ApiResultTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.APICashierOpenAndPassivePayService;
import com.yeepay.g3.core.nccashier.utils.ApiCashierRouterUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.ExtendInfoFromPayRequest;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.IdCardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.payprocessor.dto.BasicRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.BasicResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * API收银台-开放支付(主扫、公众号)-模板实现
 * Created by ruiyang.du on 2017/6/28.
 */
@Service("SCCANPAY_OPENID_UnifiedAPICashierService")
public class APICashierOpenPayServiceImpl extends APICashierPaymentBaseTemplate implements APICashierOpenAndPassivePayService {

    private Logger logger = NcCashierLoggerFactory.getLogger(APICashierOpenPayServiceImpl.class);
    @Resource
    private PayProcessorService payProcessorService;

    @Override
    public UnifiedAPICashierResponseDTO pay(UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        return doOpenAndPassivePay(apiCashierRequestDTO);
    }

    @Override
    protected <T> void paramValidate(T apiCashierRequestDTO) {
        UnifiedAPICashierRequestDTO openPayRequestDTO = (UnifiedAPICashierRequestDTO)apiCashierRequestDTO;
        boolean openWechatOpenId = PayTool.WECHAT_OPENID.name().equals(openPayRequestDTO.getPayTool());
        boolean openAlipayLifeNo = PayTool.ZFB_SHH.name().equals(openPayRequestDTO.getPayTool());
        boolean openWechatMiniProgram = PayTool.XCX_OFFLINE_ZF.name().equals(openPayRequestDTO.getPayTool());

        if (!openWechatOpenId && !openAlipayLifeNo && !openWechatMiniProgram) {
            return;
        }
        if ((openWechatOpenId || openWechatMiniProgram) && StringUtils.isEmpty(openPayRequestDTO.getAppId())) {
            logger.error("paramValidate()参数校验不通过，公众号支付或小程序支付，appId为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，appId为空");
        }
        if (StringUtils.isEmpty(openPayRequestDTO.getOpenId())) {
            logger.error("paramValidate()参数校验不通过，公众号支付、生活号支付或小程序支付，openId为空");
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，openId为空");
        }
    }

    @Override
    protected <P extends BasicResponseDTO, A, O> O buildResponse(P payProcessorResponse, A apiRequest, PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
        UnifiedAPICashierRequestDTO apiCashierRequestDTO = (UnifiedAPICashierRequestDTO) apiRequest;
        UnifiedAPICashierResponseDTO unifiedAPICashierResponseDTO = buildBasicResponse(apiCashierRequestDTO);
        OpenPayResponseDTO openPayResponseDTO = (OpenPayResponseDTO) payProcessorResponse;
        unifiedAPICashierResponseDTO.setResultData(openPayResponseDTO.getPrepayCode());
        unifiedAPICashierResponseDTO.setResultType(ApiResultTypeEnum.URL.getType());
        if (PayTypeEnum.WECHAT_OPENID.name().equals(apiCashierRequestDTO.getPayType())) {
            String extendInfo = paymentRequest.getExtendInfo();
            JSONObject json = JSONObject.parseObject(extendInfo);
            String sceneTypeExt = json.getString("sceneTypeExt");
            if (Constant.PRE_ROUTE_SCENE_TYPE_EXT_NORMAL.equals(sceneTypeExt)) {
                //注：微信公众号支付，且走jsapi成功的，返回结果需调用方重定向。其他的开放支付接口结果无需关注此标识。
                unifiedAPICashierResponseDTO.setResultType(ApiResultTypeEnum.JSON.getType());
            }
        }else if(PayTypeEnum.ZFB_SHH.name().equals(apiCashierRequestDTO.getPayType())){
        		Map<String, String> datas = new HashMap<String, String>();
        		datas.put(ApiCashierRouterUtil.ALIPAY_LIFE_NO_PREPAY_CODE, openPayResponseDTO.getPrepayCode());
        		String prepayCode = JSON.toJSONString(datas);
        		unifiedAPICashierResponseDTO.setResultData(prepayCode);
                unifiedAPICashierResponseDTO.setResultType(ApiResultTypeEnum.JSON.getType());
        }else if(PayTypeEnum.XCX_OFFLINE_ZF.name().equals(apiCashierRequestDTO.getPayType())){
            // 微信小程序支付，返回类型为json
            unifiedAPICashierResponseDTO.setResultType(ApiResultTypeEnum.JSON.getType());
        }else if(PayTypeEnum.WECHAT_SDK.name().equals(apiCashierRequestDTO.getPayType())){
            // 微信SDK支付，返回类型为json
            unifiedAPICashierResponseDTO.setResultType(ApiResultTypeEnum.JSON.getType());
        }
        
        saveAPIResponseIntoCache(unifiedAPICashierResponseDTO, paymentRecord.getId());
        return (O) unifiedAPICashierResponseDTO;
    }


    @Override
    protected <O extends BasicRequestDTO,I> O prepareToCallPayProcessor(PaymentRecord paymentRecord, I apiRequest, PaymentRequest paymentRequest) {
        OpenPayRequestDTO openPayRequestDTO = new OpenPayRequestDTO();
        UnifiedAPICashierRequestDTO apiCashierRequest = (UnifiedAPICashierRequestDTO)apiRequest;
        buildCommonRequestDTO(paymentRecord, paymentRequest, openPayRequestDTO, apiCashierRequest.getUserIp());
        buildOpenPayProcessorRequestDTO(paymentRecord, apiCashierRequest, paymentRequest, openPayRequestDTO);
        return (O) openPayRequestDTO;
    }


    @Override
    protected <I extends BasicRequestDTO, O extends BasicResponseDTO> O requestPayProcessorPay(I requestDTO) {
        OpenPayRequestDTO openPayRequestDTO = (OpenPayRequestDTO) requestDTO;
        OpenPayResponseDTO openPayResponseDTO = payProcessorService.openPayRequest(openPayRequestDTO);
        return (O) openPayResponseDTO;
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
        UnifiedAPICashierRequestDTO openPayRequest = (UnifiedAPICashierRequestDTO)apiCashierRequest;
        PaymentRecord paymentRecord = queryPaymentRecord(paymentRequest,payType);
        if (null != paymentRecord) {
        	 	logger.info("无需创建paymentRecord, recordId={}", paymentRecord.getId());
            return paymentRecord;
        }
       
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
        paymentRecord.setCardInfoId(openPayRequest.getMerchantTerminalId());//被扫的授权码
        paymentRecord.setVersion(paymentRequest.getVersion());
        long paymentRecordId = paymentProcessService.savePaymentRecord(paymentRecord);
        paymentRecord.setId(paymentRecordId);
        return paymentRecord;
    }

    /**
     * 封装开放支付的专有参数
     *
     * @param paymentRecord
     * @param apiCashierRequest
     * @param paymentRequest
     * @param openPayRequestDTO
     */
    private void buildOpenPayProcessorRequestDTO(PaymentRecord paymentRecord, UnifiedAPICashierRequestDTO apiCashierRequest, PaymentRequest paymentRequest, OpenPayRequestDTO openPayRequestDTO) {
        openPayRequestDTO.setCustomerLevel(Constant.CUSTOMER_LEVEL_V);
        openPayRequestDTO.setBasicProductCode(CommonUtil.getBasicProductCode(paymentRecord.getPayTool(), paymentRecord.getTradeSysNo()));
        ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
        openPayRequestDTO.setRetailProductCode(extendInfoFromPayRequest.getSaleProductCode());//零售产品码
        openPayRequestDTO.setPayInterface(extendInfoFromPayRequest.getPayInterface());//通道编码
        if (StringUtils.isNotBlank(extendInfoFromPayRequest.getBankTotalCost())) {//通道成本
            openPayRequestDTO.setBankTotalCost(new BigDecimal(extendInfoFromPayRequest.getBankTotalCost()));
        }
        openPayRequestDTO.setReportMerchantNo(extendInfoFromPayRequest.getReportMerchantNo());//二级商户号
        openPayRequestDTO.setPayBusinessType(PayBusinessType.DC);
        openPayRequestDTO.setAppId(apiCashierRequest.getAppId());
        openPayRequestDTO.setOpenId(apiCashierRequest.getOpenId());
        openPayRequestDTO.setPlatformType(getFePlatformType(apiCashierRequest.getPayType()));
        openPayRequestDTO.setPayOrderType(getPayOrderType(apiCashierRequest.getPayType()));
		String pageCallbackUrl = CommonUtil.getApiFrontRedirectUrl(paymentRequest.getMerchantNo(),
				paymentRequest.getId());
        openPayRequestDTO.setPageCallBack(pageCallbackUrl);
        Map<String, String> extParam = new HashMap<String, String>();
        if(StringUtils.isNotBlank(extendInfoFromPayRequest.getSpecifyChannelCodes())){
            extParam.put(Constant.SPECIFY_CHANNEL_CODES,extendInfoFromPayRequest.getSpecifyChannelCodes());//业务通道编码，放在extParam传入PP
        }
        if(StringUtils.isNotBlank(extendInfoFromPayRequest.getReportFee())){
            extParam.put(Constant.REPORT_FEE,extendInfoFromPayRequest.getReportFee());//报备费率，放在extParam传入PP
        }
        //added by zengzhi.han 20181016 增加微信小程序透传reportId
        if (PayTypeEnum.WECHAT_OPENID.name().equals(apiCashierRequest.getPayType())
                ||PayTypeEnum.XCX_OFFLINE_ZF.name().equals(apiCashierRequest.getPayType())){
            //哆啦宝微信公众号粉丝路由透传 reportId 和 focusAppId
            ExtendInfoFromPayRequest extendInfo = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
            if (extendInfo!=null){
                extParam.put(Constant.WX_REPORT_ID,extendInfo.getReportId());//reportId，放在extParam传入PP
                extParam.put(Constant.WX_FOCUS_APP_ID,extendInfo.getFocusAppId());//focusAppId，放在extParam传入PP
            }
        }
        extParam.put(Constant.PARENT_MERCHANT_NO,paymentRequest.getParentMerchantNo());
        openPayRequestDTO.setExtParam(extParam);
    }



    

}
