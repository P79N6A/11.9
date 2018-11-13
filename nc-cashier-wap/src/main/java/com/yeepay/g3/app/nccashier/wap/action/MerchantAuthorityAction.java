package com.yeepay.g3.app.nccashier.wap.action;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.facade.nccashier.dto.MerchantAuthorityResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.ShareCardAuthoritySendSmsResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ShareCardAuthoritySmsConfirmResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.action
 *
 * @author pengfei.chen
 * @since 17/1/4 16:08
 */
@Controller
@RequestMapping(value = "/merchantAuthority")
public class MerchantAuthorityAction extends WapBaseAction{
    private static final Logger logger = LoggerFactory.getLogger(MerchantAuthorityAction.class);


    @RequestMapping(value = "/request")
    @ResponseBody
    public Object merchantAuthorityRequest(String token) {
        logger.info("[monitor],nccahier_merchantAuthority_request,token:{}", token);
        RequestInfoDTO infoDTO;
        try {
            infoDTO = checkRequestInfoDTO(token);
            MerchantAuthorityResponseDTO merchantAuthorityResponseDTO = merchantAuthorityService.merchantAuthorityRequest(infoDTO.getPaymentRequestId());
            return JSON.toJSONString(merchantAuthorityResponseDTO);
        }catch (Throwable e) {
            logger.error("[monitor],event:nccahier_merchantAuthority_error，token:" + token, e);
            Map<String, Object> res = new HashMap<String, Object>();
            res.put("processStatusEnum","FAILED");
            return createErrorMsg(res,e,"nccahier_merchantAuthority_error",token,SysCodeEnum.NCCASHIER_PC);
        }
    }

    @RequestMapping(value = "/shareCardSendSms")
    @ResponseBody
    public Object shareCardSendSms(String token) {
        logger.info("[monitor],nccahier_shareCardSendSms_request,token:{}", token);
        RequestInfoDTO infoDTO;
        try {
            infoDTO = checkRequestInfoDTO(token);
            ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSmsResponseDTO = merchantAuthorityService.shareCardAuthoritySendSms(infoDTO.getPaymentRequestId());
            return JSON.toJSONString(shareCardAuthoritySendSmsResponseDTO);
        } catch (Throwable e) {
            logger.error("[monitor],event:nccahier_shareCardSendSms_error,token:" + token, e);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("smsSendStatus","SEND_FAILED");
            return createErrorMsg(map,e,"nccahier_shareCardSendSms",token,SysCodeEnum.NCCASHIER_PC);
        }
    }


    @RequestMapping(value = "/shareCardSmsConfirm")
    @ResponseBody
    public Object shareCardSmsConfirm(String token,String validateCode) {
        logger.info("[monitor],nccahier_shareCardSmsConfirm_request,token:{}", token);
        RequestInfoDTO infoDTO;
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            if(StringUtils.isBlank(validateCode)){
                throw new CashierBusinessException(Errors.VERIFYCODE_MISS.getCode(),Errors.VERIFYCODE_MISS.getMsg());
            }
            infoDTO = checkRequestInfoDTO(token);
            ShareCardAuthoritySmsConfirmResponseDTO response = merchantAuthorityService.shareCardAuthoritySmsConfirm(infoDTO.getPaymentRequestId(), validateCode);
            res.put("bankCardsToShow", response.getBankCardDTOList());
            res.put("showChangeCard", response.isChangeCard());
            res.put("smsConfirmStatus",response.getSmsValidateStatus());
            return JSON.toJSONString(res);
        } catch (Throwable e) {
            logger.warn("[monitor],event:getConfirmSignal_bussinessException,token:" + token, e);
            res.put("smsConfirmStatus","SMS_CONFIRM_FAILED");
            return createErrorMsg(res,e,"getConfirmSignal_bussinessExceptio",token,SysCodeEnum.NCCASHIER_PC);
        }
    }
}
