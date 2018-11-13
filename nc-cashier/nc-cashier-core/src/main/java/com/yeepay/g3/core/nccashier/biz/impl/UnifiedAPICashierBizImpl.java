package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.UnifiedAPICashierBiz;
import com.yeepay.g3.core.nccashier.log.CashierTracer;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.APICashierBindCardPayService;
import com.yeepay.g3.core.nccashier.service.APICashierOpenAndPassivePayService;
import com.yeepay.g3.core.nccashier.utils.ApiCashierRouterUtil;
import com.yeepay.g3.core.nccashier.utils.ApplicationContextUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnifiedAPICashierBizImpl implements UnifiedAPICashierBiz {

    Logger logger = NcCashierLoggerFactory.getLogger(UnifiedAPICashierBiz.class);

    @Autowired
    private ApplicationContextUtil applicationContextUtil;
    @Autowired
    private APICashierBindCardPayService apiBindCardPayService;

    @Override
    public UnifiedAPICashierResponseDTO pay(UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        //1,基本参数校验，支付方式路由
        UnifiedAPICashierResponseDTO reponseDTO = new UnifiedAPICashierResponseDTO();
        long start = System.currentTimeMillis();
        try {
            commonParamValidate(apiCashierRequestDTO);
            NcCashierLoggerFactory.TAG_LOCAL.set("[API聚合及被扫支付|pay]—[token=" + apiCashierRequestDTO.getToken() + "]");
            ApiCashierRouterUtil.payTypeRequestRoute(apiCashierRequestDTO);
            String serviceName = ApiCashierRouterUtil.getServiceName(apiCashierRequestDTO.getPayTool());
            APICashierOpenAndPassivePayService APICashierOpenAndPassivePayService = applicationContextUtil.getBean(serviceName + "_UnifiedAPICashierService", APICashierOpenAndPassivePayService.class);
            if (APICashierOpenAndPassivePayService == null) {
            		// 正常情况下，是走不到这个分支的
                logger.error("【API收银台，聚合及被扫支付接口】根据支付方式获取业务工厂失败");
                throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
            }
            //2，各支付方式逻辑处理
            reponseDTO = APICashierOpenAndPassivePayService.pay(apiCashierRequestDTO);
            return reponseDTO;
        } catch (YeepayBizException e) {
            reponseDTO = errorResult(e.getDefineCode(), e.getMessage(), apiCashierRequestDTO);
            return reponseDTO;
        } catch (Throwable t) {
        	// 走到这儿，一般是系统或系统间调用发生了网络异常或其他未知异常
            logger.error("【API收银台，聚合及被扫支付接口】失败，异常为 : ", t);
            reponseDTO = errorResult(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg(), apiCashierRequestDTO);
            return reponseDTO;
        }finally {
//            if(CommonUtil.enableSpanLog()){
//                CashierTracer.apiFusionPayResponseSpan(apiCashierRequestDTO.getToken(), apiCashierRequestDTO.getMerchantNo(), apiCashierRequestDTO.getPayTool(),
//                        apiCashierRequestDTO.getPayType(), reponseDTO.getMessage(), reponseDTO.getCode(), start);
//            }
        }
    }

    @Override
    public ApiBindPayPaymentResponseDTO requestPayment(ApiBindPayPaymentRequestDTO needItemRequestDTO) {
        ApiBindPayPaymentResponseDTO reponseDTO = null;
        StringBuilder logInfo = new StringBuilder("【API收银台，绑卡支付请求补充项接口】，请求参数 = ");
        try {
            logInfo.append(needItemRequestDTO);
            //1，绑卡支付请求补充项逻辑处理
            reponseDTO = apiBindCardPayService.requestPayment(needItemRequestDTO);
            logInfo.append(" , 返回结果 = ").append(reponseDTO);
            logger.info(logInfo.toString());
            return reponseDTO;
        } catch (YeepayBizException e) {
            logInfo.append("，业务异常，异常为 : ");
            logger.error(logInfo.toString(), e);
            return errorResult(e.getDefineCode(), e.getMessage(), needItemRequestDTO);
        } catch (Throwable t){
            logInfo.append("，内部异常，异常为 : ");
            logger.error(logInfo.toString(), t);
            return errorResult(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg(), needItemRequestDTO);
        }
    }

    @Override
    public ApiBindPaySendSmsResponseDTO requestSmsSend(ApiBindPaySendSmsRequestDTO param) {
        ApiBindPaySendSmsResponseDTO reponseDTO = null;
        StringBuilder logInfo = new StringBuilder("【API收银台，绑卡支付请求发短验接口】，请求参数 = ");
        try {
            logInfo.append(param);
            //1，绑卡支付请求发短验逻辑处理
            reponseDTO = apiBindCardPayService.requestSmsSend(param);
            logInfo.append(" , 返回结果 = ").append(reponseDTO);
            logger.info(logInfo.toString());
            return reponseDTO;
        } catch (YeepayBizException e) {
            logInfo.append("，业务异常，异常为 : ");
            logger.error(logInfo.toString(), e);
            return errorResult(e.getDefineCode(), e.getMessage(), param);
        } catch (Throwable t){
            logInfo.append("，内部异常，异常为 : ");
            logger.error(logInfo.toString(), t);
            return errorResult(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg(), param);
        }
    }

    @Override
    public ApiBindPayConfirmResponseDTO confirmPayment(ApiBindPayConfirmRequestDTO param) {
        ApiBindPayConfirmResponseDTO reponseDTO = null;
        StringBuilder logInfo = new StringBuilder("【API收银台，绑卡支付确认支付接口】，请求参数 = ");
        try {
            logInfo.append(param);
            //1，绑卡支付请求发短验逻辑处理
            reponseDTO = apiBindCardPayService.confirmPayment(param);
            logInfo.append(" , 返回结果 = ").append(reponseDTO);
            logger.info(logInfo.toString());
            return reponseDTO;
        } catch (YeepayBizException e) {
            logInfo.append("，业务异常，异常为 : ");
            logger.error(logInfo.toString(), e);
            return errorResult(e.getDefineCode(), e.getMessage(), param);
        } catch (Throwable t){
            logInfo.append("，内部异常，异常为 : ");
            logger.error(logInfo.toString(), t);
            return errorResult(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg(), param);
        }
    }


    /**
     * 聚合及被扫支付，各支付方式通用参数的非空校验
     *
     * @param apiCashierRequestDTO
     */
    private void commonParamValidate(UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        if (apiCashierRequestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，入参对象为空");
        }
        if (StringUtils.isEmpty(apiCashierRequestDTO.getPayTool())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，payTool为空");
        }
        if (StringUtils.isEmpty(apiCashierRequestDTO.getPayType())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，payType为空");
        }
        if (StringUtils.isEmpty(apiCashierRequestDTO.getToken())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，token为空");
        }
        if (StringUtils.isEmpty(apiCashierRequestDTO.getMerchantNo())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，merchantNo为空");
        }
        if (!"1.0".equals(apiCashierRequestDTO.getVersion())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，version非法");
        }
        if (StringUtils.isEmpty(apiCashierRequestDTO.getUserIp())) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，userIp为空");
        }
    }

    /**
     * 聚合及被扫支付，处理失败返回
     * <p>注：错误码和错误信息会经过对外映射
     * @param code
     * @param message
     * @param apiCashierRequestDTO
     * @return
     */
    private UnifiedAPICashierResponseDTO errorResult(String code, String message, UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        UnifiedAPICashierResponseDTO reponseDTO = new UnifiedAPICashierResponseDTO();
        CashierBusinessException outerException = CommonUtil.handleApiCashierException(code, message);
        reponseDTO.setCode(outerException.getDefineCode());
        reponseDTO.setMessage(outerException.getMessage());
        if (apiCashierRequestDTO != null) {
            reponseDTO.setToken(apiCashierRequestDTO.getToken());
            reponseDTO.setMerchantNo(apiCashierRequestDTO.getMerchantNo());
            reponseDTO.setPayTool(ApiCashierRouterUtil.payToolResponseRoute(apiCashierRequestDTO.getPayTool()));
            reponseDTO.setPayType(ApiCashierRouterUtil.payTypeResponseRoute(apiCashierRequestDTO.getPayType()));
        }
        return reponseDTO;
    }

    /**
     * 绑卡支付，请求下单并获取补充项，处理失败返回
     * <p>注：错误码和错误信息会经过对外映射
     * @param code
     * @param message
     * @param apiCashierRequestDTO
     * @return
     */
    private ApiBindPayPaymentResponseDTO errorResult(String code, String message, ApiBindPayPaymentRequestDTO apiCashierRequestDTO) {
        ApiBindPayPaymentResponseDTO reponseDTO = new ApiBindPayPaymentResponseDTO();
        CashierBusinessException outerException = CommonUtil.handleApiCashierException(code, message);
        reponseDTO.setCode(outerException.getDefineCode());
        reponseDTO.setMessage(outerException.getMessage());
        if (apiCashierRequestDTO != null) {
            reponseDTO.setToken(apiCashierRequestDTO.getToken());
            reponseDTO.setMerchantNo(apiCashierRequestDTO.getMerchantNo());
            reponseDTO.setBindId(apiCashierRequestDTO.getBindId());
        }
        return reponseDTO;
    }
    /**
     * 绑卡支付，请求发短验，处理失败返回
     * <p>注：错误码和错误信息会经过对外映射
     * @param code
     * @param message
     * @param apiCashierRequestDTO
     * @return
     */
    private ApiBindPaySendSmsResponseDTO errorResult(String code, String message, ApiBindPaySendSmsRequestDTO apiCashierRequestDTO) {
        ApiBindPaySendSmsResponseDTO reponseDTO = new ApiBindPaySendSmsResponseDTO();
        CashierBusinessException outerException = CommonUtil.handleApiCashierException(code, message);
        reponseDTO.setCode(outerException.getDefineCode());
        reponseDTO.setMessage(outerException.getMessage());
        if (apiCashierRequestDTO != null) {
            reponseDTO.setToken(apiCashierRequestDTO.getToken());
        }
        return reponseDTO;
    }
    /**
     * 绑卡支付，支付确认，处理失败返回
     * <p>注：错误码和错误信息会经过对外映射
     * @param code
     * @param message
     * @param apiCashierRequestDTO
     * @return
     */
    private ApiBindPayConfirmResponseDTO errorResult(String code, String message, ApiBindPayConfirmRequestDTO apiCashierRequestDTO) {
        ApiBindPayConfirmResponseDTO reponseDTO = new ApiBindPayConfirmResponseDTO();
        CashierBusinessException outerException = CommonUtil.handleApiCashierException(code, message);
        reponseDTO.setCode(outerException.getDefineCode());
        reponseDTO.setMessage(outerException.getMessage());
        if (apiCashierRequestDTO != null) {
            reponseDTO.setToken(apiCashierRequestDTO.getToken());
        }
        return reponseDTO;
    }
}
