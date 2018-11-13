package com.yeepay.g3.app.nccashier.wap.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.app.nccashier.wap.utils.*;
import com.yeepay.g3.app.nccashier.wap.vo.*;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一收银台pc入口
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.action
 *
 * @author pengfei.chen
 * @since 16/10/31 10:39
 */
@Controller
@RequestMapping(value = "/newpc", method = {RequestMethod.POST, RequestMethod.GET})
public class NewPcPayAction extends WapBaseAction {
	
    private static final Logger logger = LoggerFactory.getLogger(NewPcPayAction.class);
    
    /**
     * 本controller对应的系统编码
     */
    private static final SysCodeEnum SYSTEM_CODE = SysCodeEnum.NCCASHIER_PC;
    
    
    /**
	 * 获取可用的绑卡列表，并判断是否可以更换卡
	 * 
	 * @param token
	 */
	@RequestMapping(value = "/bindcardlist", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object routeBindCardList(String token, HttpServletResponse httpResponse){
		try{
            validatePassBIndId(token);
			Map<String, Object> res = getBindCardList(token);
			ModelAndView mv = new ModelAndView();
            mv.setViewName("newpc/bind_card_pay");
			if(MapUtils.isNotEmpty(res)){
				for(String key : res.keySet()){
					mv.addObject(key, res.get(key));
				}
				mv.addObject("isNotFirst",true);
			}
			return mv;
		}catch(Throwable e){
			return createErrorMsg(null, e,"routeBindCardList_error",token, SYSTEM_CODE);
		}
	}	
    
    /**
     * newpc 的统一路由入口
     * @param request
     * @param token
     * @return
     */
    @RequestMapping(value = "/routpayway/{token}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Object requestProcess(@PathVariable("token") String token, HttpServletRequest request) {
        RequestInfoDTO info = null;
        try {
            info = checkRequestInfoDTO(token);
            // 监控日志埋点
            logger.info("[monitor],event:nccashierwap_payMethod_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());
            BussinessTypeResponseDTO bussinessTypeResponseDTO = ncCashierService.routerPayWay(info.getPaymentRequestId());
            //如果是首付支付或者是首次透传非卡号信息跳转到输入卡号页，如果是首次透传卡号信息
            if (BusinessTypeEnum.FIRSTPAY == bussinessTypeResponseDTO.getBusinessTypeEnum() || BusinessTypeEnum.FIRSTPASS == bussinessTypeResponseDTO.getBusinessTypeEnum()) {
                return requestFirst(token, request);
            } else if (BusinessTypeEnum.FIRSTPASSCARDNO == bussinessTypeResponseDTO.getBusinessTypeEnum()) {
                return passcardInfo(token, request);
            } else {
                return requestBind(token);
            }
        } catch (Throwable e) {
            return createErrorMsg(null, e,"routpayway_error",token, SYSTEM_CODE);
        }
    }
    
    

    /**
     * 跳转至首次输入卡号页面
     *
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/request/first", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Object requestFirst(String token, HttpServletRequest request) {
        logger.info("[monitor],event:nccashier_requestFirst_request,token:{}", token);
        ModelAndView mv = new ModelAndView();
        try {
            //跳转到首次支付有可能是来自绑卡支付的新增银行卡支付,如果是来自绑卡支付的换卡支付，增加标示(返回已经有银行卡支付)
            String isBindCardChangeCard = request.getParameter("isBindCardChangeCard");
            if ("isBindCardChangeCard".equals(isBindCardChangeCard)) {
                mv.addObject("isBindCardChangeCard", isBindCardChangeCard);
            }
            checkRequestInfoDTO(token);
            mv.setViewName("newpc/bind_first_pay");
            return mv;
        } catch (Throwable e) {
            return createErrorMsg(null, e,"request_first_error",token, SYSTEM_CODE);
        }
    }

    /**
     * 跳转到帮卡支付页面
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/request/bind", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Object requestBind(String token) {
        logger.info("[monitor],event:nccashier_requestBind_request,token:{}", token);
        RequestInfoDTO info = null;
        List<BankCardDTO> bindCards = new ArrayList<BankCardDTO>();
        try {
            info = checkRequestInfoDTO(token);
            BankCardReponseDTO response = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
            BankCardDTO bindCardDTO = response.getBankCardDTO();
            PassCardInfoDTO passCardInfoDTO = response.getPassCardInfoDTO();
            if (bindCardDTO == null) {
                throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
            } else {
                boolean bindTouchuanIsCardNo=false;
                ModelAndView view = new ModelAndView();
                if (null != passCardInfoDTO && StringUtils.isNotBlank(passCardInfoDTO.getCardNo())) {
                    bindTouchuanIsCardNo =true;
                    bindCards.add(response.getBankCardDTO());
                } else {
                    bindCards = bindCardDTO.getOtherCards();
                }

                //如果绑卡没有投传卡号，并且也可以显示新增卡支付
                if(!bindTouchuanIsCardNo && response.getShowChangeCard()){
                    view.addObject("showChangeCard", true);
                }else {
                    //如果授权了，则不能显示新增卡支付，否则可以显示新增卡支付
                    view.addObject("showChangeCard", !response.isAuthorized());
                }
                view.addObject("isNotFirst",true);
                view.addObject("authorized",response.isAuthorized());
                if(CollectionUtils.isNotEmpty(response.getUnuseBankCardDTO())){
                    bindCards.addAll(response.getUnuseBankCardDTO());
                }
                view.addObject("bindCards", bindCards);
                //易订货系统商维度解绑
                if(newWapPayService.checkAbleToUnbindCard(info.getParentMerchantNo())){
                    view.addObject("unBindCard", "true");
                }
                view.setViewName("newpc/bind_first_pay");


                if(ncCashierService.isPassBindId(info.getPaymentRequestId()))
                    view.addObject("showChangeCard", false);

                return view;
            }
        } catch (Throwable e) {
            return createErrorMsg(null,e,"request_bind_error",token, SYSTEM_CODE);
        }
    }
 

    /**
     * 校验输入的卡号以及获取卡bin信息(无透传卡号)
     * 该action可能是来自绑卡的新增卡支付
     *
     * @param cardno
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/request/notpasscardno")
    @ResponseBody
    public Object cardInfo(String cardno, String token, HttpServletRequest request, HttpServletResponse response) {
        logger.info("[monitor],event:nccashier_notpasscardno_request,token:{}", token);
        RequestInfoDTO info = null;
        ModelAndView modelAndView = new ModelAndView();
        Map<String,Object> result = new HashMap<String, Object>();
        try {
            info = checkRequestInfoDTO(token);
            if (StringUtils.isNotEmpty(cardno)) {
                cardno = Base64Util.decode(cardno);
                cardno = cardno.replaceAll(" ", "");
            }
            if (StringUtils.isEmpty(cardno) || !DataUtil.isBankCardNum(cardno)) {
                result.put("cardBinStatus",AJAX_FAILED);
                result.put("errorcode", Errors.INVALID_BANK_CARD_NO.getCode());
                result.put("errormsg", ExceptionUtil.handleException(Errors.INVALID_BANK_CARD_NO, SYSTEM_CODE).getMessage());
                return result;
            }
            //获取卡bin信息，如果该卡不支持会返回FAILED
            CardBinInfoDTO cardBinInfoDTO = ncCashierService.getCardBinInfo(cardno);
            if (cardBinInfoDTO.getProcessStatusEnum() == ProcessStatusEnum.FAILED) {
                result.put("cardBinStatus", AJAX_FAILED);
                result.put("errorcode",cardBinInfoDTO.getReturnCode());
                result.put("errormsg", cardBinInfoDTO.getReturnMsg());
                return result;
            } else {
                modelAndView.setViewName("newpc/cardbin_item");
                modelAndView.addObject("cardBinStatus", AJAX_SUCCESS);
                modelAndView.addObject("cardNo", cardno);
                modelAndView.addObject("cardType", cardBinInfoDTO.getCardTypeEnum());
                modelAndView.addObject("bankCode", cardBinInfoDTO.getBank());
                modelAndView.addObject("bankName", cardBinInfoDTO.getName());
                modelAndView.addObject("cardlater", cardno.substring(cardno.length()-4,cardno.length()));
                //如果是从绑卡支付跳转过来的新增卡支付(首次支付),清掉record
                if ("isBindCardChangeCard".equals(request.getParameter("isBindCardChangeCard"))) {
                    ncCashierService.clearRecordId(token);
                }
                getValidates(info, cardno, modelAndView);
                modelAndView.addObject("validateStatus", AJAX_SUCCESS);
                return modelAndView;
            }
        } catch (CashierBusinessException e) {
            logger.warn("[monitor],event:nccashier_cardInfo_request_BusinException,token:" + token, e);
            modelAndView.addObject("validateStatus", AJAX_FAILED);
            CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
            modelAndView.addObject("errorcode", ex.getDefineCode());
            modelAndView.addObject("errormsg", ex.getMessage());
            return modelAndView;
        } catch (Throwable e) {
            result.put("cardBinStatus", AJAX_FAILED);
            result.put("validateStatus", AJAX_FAILED);
            return createErrorMsg(result,e,"request_notpasscardno_error",token, SYSTEM_CODE);
        }
    }


    /**
     * 校验输入的卡号以及获取卡bin信息(透传卡号)
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/request/passcardno")
    @ResponseBody
    public Object passcardInfo(String token, HttpServletRequest request) {
        logger.info("[monitor],event:nccashier_passcard_request,token:{}", token);
        RequestInfoDTO info = null;
        ModelAndView mv = new ModelAndView();
        String cardNo = null;
        try {
            info = checkRequestInfoDTO(token);
            //获取透传的卡信息和用户信息
            cardNo = getValidates(info, null, mv);
            if (StringUtils.isEmpty(cardNo) || !DataUtil.isBankCardNum(cardNo)) {
                mv.addObject("cardNo", cardNo);
                throw new CashierBusinessException(Errors.INVALID_BANK_CARD_NO.getCode(), Errors.INVALID_BANK_CARD_NO.getMsg());
            } else {
                mv.setViewName("newpc/pass_cardno");
                return mv;
            }
        } catch (Throwable e) {
            return createErrorMsg(null, e,"request_passcardno_error",token, SYSTEM_CODE);
        }
    }

    /**
     * 首次支付发送短验AJAX
     *
     * @param token
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/firstpay/smsSend", method = {RequestMethod.POST})
    @ResponseBody
    public void smsSend(CardInfoDTO cardInfo, String token, HttpServletResponse response)
            throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        logger.info(
                "[monitor],event:nccashier_smsSend_request,token:{}", token);
        try {
            Base64Util.decryptCardInfoDTO(cardInfo);
            newWapPayService.validateCardInfoAndTimeout(cardInfo, token, null);
            ReqSmsSendTypeEnum reqSmsSendTypeEnum1 = newWapPayService.createPaymentOrSendSMS(token, null, null, cardInfo);
            result.put("smsType", reqSmsSendTypeEnum1);
            if (cardInfo != null) {
                result.put("phoneNo", cardInfo.getPhone());
            }
            //如果短验类型是语音，才获取发送方号码
            if(ReqSmsSendTypeEnum.VOICE == (reqSmsSendTypeEnum1)) {
                String sendSMSNo = ncCashierService.getSendSMSNo();
                result.put("sendSMSNo", sendSMSNo);
            }
            commonAjaxReturn(response, result, null, null);
        } catch (Throwable e) {
			logger.error("[monitor],event:nccashier_smsSend_sendSms_exception，token:" + token, e);
			result.put("bizStatus", AJAX_FAILED);
			if (cardInfo != null) {
				result.put("phoneNo", cardInfo.getPhone());
			}
			//短信验证码频繁*秒 错误码定制
			CashierBusinessException ex = null;
			if(e instanceof CashierBusinessException && Errors.GET_SMS_FREQUENT.getCode().equals(((CashierBusinessException)e).getDefineCode())){
				ex = (CashierBusinessException)e;
			}
			else{
				ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			}
			
			commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
        }

    }


    /**
     * 绑卡支付发送短验
     *
     * @param token
     * @param bindId
     * @param needBankCardDTO
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/bindpay/bindSmsSend", method = {RequestMethod.POST})
    @ResponseBody
    public void bindSmsSend(String token, String bindId, ReqSmsSendTypeEnum smsType, NeedBankCardDTO needBankCardDTO, HttpServletResponse response)
            throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Base64Util.decryptNeedBankCardDTO(needBankCardDTO);
            logger.info("[monitor],event:nccashier_bindSmsSend_request,token:{},needBankCardDTO:{},bindId:{}",token, needBankCardDTO, bindId);
            newWapPayService.validateBindCardAndTimeout(needBankCardDTO, token, bindId);
            newWapPayService.bindSendSMS(smsType, needBankCardDTO, token, bindId);
            result.put("smsType", smsType);
            //如果短验类型是语音，才获取发送方号码
            if(ReqSmsSendTypeEnum.VOICE == (smsType)) {
                String sendSMSNo = ncCashierService.getSendSMSNo();
                result.put("sendSMSNo", sendSMSNo);
            }
            commonAjaxReturn(response, result, null, null);
        } catch (Throwable e) {
            logger.error("[monitor],event:nccashier_bindSmsSend_sendSms_exception，token:" + token, e);
            result.put("bizStatus", AJAX_FAILED);
            CashierBusinessException ex = null;
			if(e instanceof CashierBusinessException && Errors.GET_SMS_FREQUENT.getCode().equals(((CashierBusinessException)e).getDefineCode())){
				ex = (CashierBusinessException)e;
			}
			else{
				ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			}
            commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
        }

    }


    /**
     * 绑卡支付下单AJAX
     * @param token
     * @param bindId
     * @throws IOException
     */
    @RequestMapping(value = "/bindpay/requestPayment", method = {RequestMethod.POST})
    @ResponseBody
    public Object requestPayment(String token, String bindId) throws IOException {
        logger.info(
                "[monitor],event:nccashier_requestPayment_request,token:{},bindId:{}",
                token, bindId);
        RequestInfoDTO info = null;
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            info = checkRequestInfoDTO(token);
            BankCardReponseDTO bankCardResponse = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
            BankCardDTO bindCardDTO = bankCardResponse.getBankCardDTO();
            if (bindCardDTO == null) {
                throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
            } else {
                result = newWapPayService.createPayment(token, bindId, info, bindCardDTO);
                if(result.get("smsType")!=null){
                    modelAndView.addObject("smsType",result.get("smsType"));
                }
                modelAndView.addObject("needBankCardDTO",result.get("needBankCardDTO"));
                modelAndView.setViewName("newpc/bind_item");
                return modelAndView;
            }
        } catch (Throwable e) {
            logger.error("[monitor],event:nccashier_requestPayment_exception，token:{}", token, e);
            result.put("bizStatus", AJAX_FAILED);
            return createErrorMsg(result,e,"bindpay_requestPayment_error",token, SYSTEM_CODE);
        }
    }


    /**
     * 首次AJAX确认支付
     *
     * @param cardInfo
     * @param token
     * @param verifycode
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/firstpay/confirm", method = {RequestMethod.POST})
    @ResponseBody
    public void firstpay(CardInfoDTO cardInfo, String token, String verifycode, HttpServletResponse response) {
        RequestInfoDTO info = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Base64Util.decryptCardInfoDTO(cardInfo);
            logger.info("[monitor],event:nccashier_firstpay_request,token:{},cardInfo:{}", token, cardInfo);
            info = checkRequestInfoDTO(token);
            newWapPayService.validateCardInfo(null, cardInfo);
            newWapPayService.firstPay(info, token, cardInfo, verifycode);
            result.put("token", token);
            commonAjaxReturn(response, result, null, null);
        } catch (CashierBusinessException e) {
            String viewTarget = checkCodeError(e);
            result.put("viewTarget", viewTarget);
            result.put("bizStatus", AJAX_FAILED);
            CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
            commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
        } catch (Throwable e) {
            logger.error("[monitor],event:nccashier_firstpay_exception，token:{},cardInfo:{}", token, cardInfo, e);
            result.put("token", token);
            result.put("bizStatus", AJAX_FAILED);
            CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
            commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
        }
    }


    /**
     * 绑卡AJAX确认支付
     *
     * @param token
     * @param verifycode
     * @param bindId
     * @param needBankCardDTO
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/bindpay/confirm", method = {RequestMethod.POST})
    @ResponseBody
    public void bindPay(String token, String verifycode, String bindId,
                        NeedBankCardDTO needBankCardDTO, HttpServletResponse response) throws IOException {
        logger.info(
                "[monitor],event:nccashier_bindPay_request,token:{},bindId:{}",
                token, bindId);
        RequestInfoDTO info = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(bindId)) {
                throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
            }
            Base64Util.decryptNeedBankCardDTO(needBankCardDTO);
            info = checkRequestInfoDTO(token);
            newWapPayService.bindPay(info, token, bindId, needBankCardDTO, verifycode);
            commonAjaxReturn(response, result, null, null);
        } catch (CashierBusinessException be) {
            logger.warn("[monitor],event:nccashier_bindPay_bussinessException,token:{},bindId:{}", token, bindId, be);
            String viewTarget = checkCodeError(be);
            result.put("viewTarget", viewTarget);
            result.put("bizStatus", AJAX_FAILED);
            CashierBusinessException ex = ExceptionUtil.handleException(be, SYSTEM_CODE);
            commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
        } catch (Throwable te) {
            logger.error("[monitor],event:nccashier_bindPay_exception,token:{},bindId:{}", token, bindId, te);
            result.put("bizStatus", AJAX_FAILED);
            CashierBusinessException ex = ExceptionUtil.handleException(te, SYSTEM_CODE);
            commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
        }
    }
    
	/**
	 * 获取微信二维码
	 * 
	 * @param token
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/wx/qrcode/request", method = { RequestMethod.POST, RequestMethod.GET })
	public void getWxPayQrCode(String token, HttpServletResponse response) throws Exception {
		if(StringUtils.isBlank(token)){
			logger.warn("获取微信二维码，token空");
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		this.getQrCode(token, PayTypeEnum.WECHAT_ATIVE_SCAN, response);
	}

	/**
	 * 获取支付宝二维码
	 * 
	 * @param token
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/zfb/qrcode/request", method = { RequestMethod.POST, RequestMethod.GET })
	public void getAlipayOrCode(String token, HttpServletResponse response) throws Exception {
		if(StringUtils.isBlank(token)){
			logger.warn("获取支付宝支付二维码，token空");
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		this.getQrCode(token, PayTypeEnum.ALIPAY, response);
	}

    /**
     * 获取京东钱包二维码
     *
     * @param token
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/jd/qrcode/request", method = { RequestMethod.POST, RequestMethod.GET })
    public void getJdQrCode(String token, HttpServletResponse response) throws Exception {
        if(StringUtils.isBlank(token)){
            logger.warn("获取京东钱包二维码，token空");
            throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
        }
        this.getQrCode(token, PayTypeEnum.JD_ATIVE_SCAN, response);
    }
	
	/**
	 * 获取银联二维码
	 * 
	 * @param token
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/union/qrcode/request", method = { RequestMethod.POST, RequestMethod.GET })
	public void getUnionQrCode(String token, HttpServletResponse response) throws Exception {
		if (StringUtils.isBlank(token)) {
			logger.warn("获取银联二维码，token空");
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		this.getQrCode(token, PayTypeEnum.UPOP_ATIVE_SCAN, response);
	}


    /**
     * 获取qq钱包二维码
     * @param token
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/qq/qrcode/request", method = { RequestMethod.POST, RequestMethod.GET })
    public void getQqQrCode(String token, HttpServletResponse response) throws Exception {
        if(StringUtils.isBlank(token)){
            logger.warn("获取qq钱包二维码，token空");
            throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
        }
        this.getQrCode(token, PayTypeEnum.QQ_ATIVE_SCAN, response);
    }
    
	/**
	 * 获取微信、支付宝、银联二维码
	 * @param token
	 * @param payType
	 * @param response
	 * @throws Exception
	 */
    private void getQrCode(String token, PayTypeEnum payType, HttpServletResponse response) throws Exception{
    	String url = qrCodeScanService.getQrCode(token, payType);
		BufferedImage bufImg = QrCodeUtil.encodeImage(url, IMAGE_SIZE, IMAGE_SIZE, null);
		bufImg.flush();
		ImageIO.write(bufImg, "png", response.getOutputStream());
    }

    /**
     * 获取移动收银台的二维码
     * @param token
     * @param response
     * @throws Exception
     */
    @Deprecated
    @RequestMapping(value = "/getScanPayQrCode", method = {RequestMethod.POST, RequestMethod.GET})
    public void getScanPayQrCode(String token, HttpServletResponse response) throws Exception {
    	RequestInfoDTO info = checkRequestInfoDTO(token);
        String repayUrl = ncCashierService.getURL("wap", info.getMerchantNo(), info.getPaymentRequestId());
        repayUrl = repayUrl + "?token=" + token;
        BufferedImage bufImg = QrCodeUtil.encodeImage(repayUrl, IMAGE_SIZE, IMAGE_SIZE, null);
        bufImg.flush();
        ImageIO.write(bufImg, "png", response.getOutputStream());
    }
    
    /**
     * 获取分期的二维码
     * @param token
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getHirePurchaseDirectQrCode", method = {RequestMethod.POST, RequestMethod.GET})
    public void getHirePurchaseDirectQrCode(String token, HttpServletResponse response) throws Exception {
    		RequestInfoDTO info = checkRequestInfoDTO(token);
    		if(null == info ){
    			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
    		}
        String repayUrl = ncCashierService.getURL("wap", info.getMerchantNo(), info.getPaymentRequestId());
        repayUrl = repayUrl + "?token=" + token+"&directType="+DirectPayType.CFL;
        
        BufferedImage bufImg = QrCodeUtil.encodeImage(repayUrl, IMAGE_SIZE, IMAGE_SIZE, null);
        bufImg.flush();
        ImageIO.write(bufImg, "png", response.getOutputStream());
    }

    /**
     * 获取直连一键支付的二维码，供IE8以下浏览器兼容使用
     * @param token
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getYJZFQrCode", method = {RequestMethod.POST, RequestMethod.GET})
    public void getYJZFQrCode(String token, HttpServletResponse response) throws Exception {
        RequestInfoDTO info = checkRequestInfoDTO(token);
        String repayUrl = ncCashierService.getURL("wap", info.getMerchantNo(), info.getPaymentRequestId());
        repayUrl = repayUrl + "?token=" + token + "&fromNewWAP=true";
        BufferedImage bufImg = QrCodeUtil.encodeImage(repayUrl, IMAGE_SIZE, IMAGE_SIZE, null);
        bufImg.flush();
        ImageIO.write(bufImg, "png", response.getOutputStream());
    }

    /**
     * 短验错误时停留在当前页
     *
     * @param ex
     */
    private String checkCodeError(CashierBusinessException ex) {
        String viewTarget = null;
        String errorCode1 = "N400091";
        String errorCode2 = "N400094";
        if (errorCode1.equals(ex.getDefineCode())) {
            return POP_ERROR;
        }
        if (errorCode2.equals(ex.getDefineCode())) {
            return POP_ERROR;
        }
        return viewTarget;
    }

    /**
     * 校验该卡是否符合商户配置
     *
     * @param info
     * @param cardNo
     */
    private String getValidates(RequestInfoDTO info, String cardNo, ModelAndView mv) {
        BankCardReponseDTO bankCardReponseDTO = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
        BankCardDTO bindCardDTO = bankCardReponseDTO.getBankCardDTO();
        //获取透传卡信息
        PassCardInfoDTO passCardInfoDTO = bankCardReponseDTO.getPassCardInfoDTO();
        if (bankCardReponseDTO.getBusinessTypeEnum() == BusinessTypeEnum.FIRSTPASSCARDNO && passCardInfoDTO != null) {
            if (StringUtils.isNotBlank(passCardInfoDTO.getCardNo())) {
                cardNo = passCardInfoDTO.getCardNo();
            }
        }
        //是否显示换其他卡支付
        mv.addObject("showChangeCard", bankCardReponseDTO.getShowChangeCard());
        
        // 自动绑卡的文案 前端不取
		mv.addObject("autoBindText", bankCardReponseDTO.getAutoBindTipText());
		mv.addObject("autoBindTip", bankCardReponseDTO.getAutoBindTipText());
        
        Map<String, Object> model = newWapPayService.getFirstPayModel(
                info.getPaymentRequestId(), cardNo, bankCardReponseDTO.getBusinessTypeEnum(), bindCardDTO);
        if (null != model.get("bc")) {
            BankCardDTO bc = (BankCardDTO) model.get("bc");
            mv.addObject("bankCode", bc.getBankCode());
            mv.addObject("bankName", bc.getBankName());
            mv.addObject("cardlater", bc.getCardlater());
            mv.addObject("cardtype", bc.getCardtype());
            mv.addObject("cardNo", bc.getCardno());
            if (bc.getBankQuata() != null) {
                mv.addObject("dayQuotaDou", bc.getBankQuata().getDayQuotaDou());//日限额
                mv.addObject("monthQuotaDou", bc.getBankQuata().getMonthQuotaDou());//月限额
                mv.addObject("orderQuotaDou", bc.getBankQuata().getOrderQuotaDou());//单比限额
            }
        }
        for (String key : model.keySet()) {
            mv.addObject(key, model.get(key));
        }
        return cardNo;
    }

    

    /**
     * pc统一收银台台页面展示action
     *
     * @return
     */
    @RequestMapping(value = "/request/{token}", method = {RequestMethod.POST, RequestMethod.GET})
    public Object unitePc(@PathVariable("token") String token, String compatibleView, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        RequestInfoDTO info = null;
        try {
            info = checkRequestInfoDTO(token);
            modelAndView.addObject("token", token);
            PayExtendInfo payExtendInfo = ncCashierService.getPayExtendInfo(info.getPaymentRequestId(),token);
            //商户开通的支付工具
            String[] payTools = payExtendInfo.getPayTool();
            String[] tools = payTools;
            modelAndView.addObject("expireTime", payExtendInfo.getExpireTime());
            boolean flag = true;
            //如果直连银行，直接跳转到银行页面
            String directType = payExtendInfo.getDirectPayType();
            if (DirectPayType.isDirectEbank(directType)) {
                String bankCode = "";
                try {
                    String[] strArry = directType.split("_");
                    bankCode = strArry[0];
                    String ebankAccountType = strArry[1];
                    String netPayerIp = PayerIpUtils.getNetPayerIp(request, bankCode, ebankAccountType);
                    EBankPayResponseVO eBankPayResponseVO = directToEBank(token, bankCode, ebankAccountType, info.getUrlParamInfo().getClientId(), netPayerIp);
                    if(Constant.TO_BANK.equals(eBankPayResponseVO.getRedirectType())){ // 跳过跳板机，跳转空白页，组装form表单，请求银行
                        ModelAndView ebankDirectLinkView = new ModelAndView("newpc/toBankPage");
                        ebankDirectLinkView.addObject("ebankUrlInfo", JSON.toJSONString(eBankPayResponseVO.getEbankUrlInfo()));
                        return ebankDirectLinkView;
                    } else if (StringUtils.isNotBlank(eBankPayResponseVO.getPayUrl())) { // 重定向到跳板机pcc.yeepay.com
                        return new RedirectView(eBankPayResponseVO.getPayUrl());
                    }else{
                        logger.error("直连网银异常 网银下单返回值的url都为空,directType:{},token:{}", directType, token);
                        throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
                    }

                } catch (CashierBusinessException e) {
                    if (Errors.EBANK_B2B_CLIENT_ID_NULL.getCode().equals(e.getDefineCode())) {
                        //直连网银B2B需客户号时，如果商户未传入客户号，前端直接提示用户输入客户号
                        modelAndView.addObject("needClientId", bankCode);
                    }
                    logger.warn("直连银行业务异常,仍然显示收银台,directType:{},token:{},error message:{}", directType, token, e.getMessage());
                    //【电信商户】网银定制，直连入参错误返回报错，不跳转至收银台
                    if(CommonUtil.checkDirectEbankToFailPage(info.getMerchantNo())){
                        return createNewPcErrorRV(token,false,"pc_direct__ebank_error",e,SYSTEM_CODE,info, info.getMerchantNo(), request);
                    }
                } catch (Throwable t) {
                    logger.warn("直连银行失败,仍然显示收银台,directType:{},token:{},error message:{}", directType, token, t.getMessage());
                    //【电信商户】网银定制，直连入参错误返回报错，不跳转至收银台
                    if(CommonUtil.checkDirectEbankToFailPage(info.getMerchantNo())){
                        return createNewPcErrorRV(token,false,"pc_direct__ebank_error",t,SYSTEM_CODE,info, info.getMerchantNo(), request);
                    }
                }
            }
            //非直连
            StringBuilder stringBuilder = new StringBuilder(32);
            for(String payTool : payTools) {
                if (PayTool.SCCANPAY.name().equals(payTool)) {
                    stringBuilder.append(payTool + ",");
                    modelAndView.addObject("showSccanPay", true);
                    modelAndView.addObject("wechatLogo", payExtendInfo.containsPayType(PayTypeEnum.WECHAT_ATIVE_SCAN));
                    modelAndView.addObject("alipayLogo", payExtendInfo.containsPayType(PayTypeEnum.ALIPAY));
                    // 银联主扫
                    modelAndView.addObject("unionLogo", payExtendInfo.containsPayType(PayTypeEnum.UPOP_ATIVE_SCAN));
                    modelAndView.addObject("jdLogo", payExtendInfo.containsPayType(PayTypeEnum.JD_ATIVE_SCAN));
                    // 开通QQ
                    modelAndView.addObject("qqLogo",
                            payExtendInfo.containsPayType(PayTypeEnum.QQ_ATIVE_SCAN) ? true : false);
                    // 开通一键扫码（二级产品用户扫码下开通了快捷扫码）
                    modelAndView.addObject("yjzfLogo", payExtendInfo.containsPayType(PayTypeEnum.NC_ATIVE_SCAN));
                } else if (PayTool.EANK.name().equals(payTool)) {
                    stringBuilder.append(payTool + ",");
                    modelAndView.addObject("showEankPay", true);
                } else if (PayTool.CFL.name().equals(payTool)) {
                    stringBuilder.append(payTool + ",");
                    modelAndView.addObject("showSccanPay", true);
                    modelAndView.addObject("installment", true);
                } else if (PayTool.NCPAY.name().equals(payTool) || PayTool.BK_ZF.name().equals(payTool)) {
                    stringBuilder.append(payTool + ",");
                    //需要判断当前的支付金额是否超过了支持的银行卡列表中的所有银行的限额 ,如果超出了所有的限额，需要提示用户,true 表示未全部超出限额
                    flag = ncCashierService.queryAllBankOverLimit(info.getAmount().doubleValue(), info.getPaymentRequestId() + "", info.getMerchantNo());
                    if (flag) {
                        modelAndView.addObject(PayTool.NCPAY.name().equals(payTool) ? "showNcPay" : "showBindCardPay",
                                true);
                    } else {
                        modelAndView.addObject("isOverLimit", "isOverLimit");
                    }
                } else if (PayTool.ZF_ZHZF.name().equals(payTool)) {
                    stringBuilder.append(payTool).append(",");
                    modelAndView.addObject("showAccountPay", true);
                } else if (PayTool.FYHK_FYHKZF.name().equals(payTool)){
                    stringBuilder.append(payTool).append(",");
                    modelAndView.addObject("showNotBankCardPay", true);
                    String notBankCardPayUrl = notBankCardPayService.getNotBankCardPayUrl(info);
                    modelAndView.addObject("notBankCardPayUrl", notBankCardPayUrl);
                } else if(PayTool.LOAD_NET.name().equals(payTool)){
                    modelAndView.addObject("showLoadNet", true);
                } else if(PayTool.LOAD_REMIT.name().equals(payTool)){
                    modelAndView.addObject("showLoadRemit", true);
                } else if(PayTool.ZF_FQY.name().equals(payTool)){
                    modelAndView.addObject("showClfEasy", true);
                }
            }
            //如果扫码、分期、网银、账户支付、非银行卡支付都不支持，且快捷全部超出限额,则直接抛出异常
            if(!flag && PayTool.isPayToolUnavailable(stringBuilder)){
                throw new CashierBusinessException(Errors.AMOUNT_OVER_LIMIT.getCode(), Errors.AMOUNT_OVER_LIMIT.getMsg());
            }

            setOrderInfo(info,modelAndView);
            //WEB收银台是否展示底部、头部等元素
            modelAndView.addObject("notShowYeepay", CommonUtil.judgeMerchantNotShowPcYeepayInfo(info.getMerchantNo()));
            modelAndView.addObject("jsTrys", CommonUtil.getJSListenerTimes());
            //获取是否支持定制活动
            modelAndView.addObject("showActivity",ncCashierService.queryQualification4Activities());


            //跳转页面

            if(StringUtils.isNotEmpty(compatibleView) && "Y".equals(compatibleView)){
                //兼容性视图，例如来自IE8以下旧版本的请求
                modelAndView.setViewName("newpc/pc_index_ie");
            }else if(CommonUtil.getCashierCustomizeWholeSwitch()){
                //全局开关如果打开，走非定制化默认收银台
                modelAndView.setViewName("newpc/pc_index");
            }else{
                MerchantCashierCustomizedLayoutSelectVO merchantCashierCustomizedLayoutInfo = merchantCashierTemplateCustomizedService
                        .queryMerchantCashierCustomizedLayoutSelectInfo(info.getMerchantNo());
                if(StringUtils.isNotBlank(merchantCashierCustomizedLayoutInfo.getLayoutNo())){
                    modelAndView.setViewName("customizedPc/pc_frame");
                    //商户选择的支付工具排序
                    modelAndView.addObject("payToolsOrder", JSON.toJSONString(CommonUtil.getValidPayTools(tools, merchantCashierCustomizedLayoutInfo.getPayToolOrder())));
                    setCustomizedHeadVm(request, modelAndView, merchantCashierCustomizedLayoutInfo);
                }else if(!CommonUtil.getYeepayVmCashierSwitch()){//ON为使用非定制化默认收银台，OFF为使用定制化默认收银台，默认值为ON
                    modelAndView.setViewName("customizedPc/pc_frame");
                    modelAndView.addObject("layoutId", Constant.MERCHANT_CASHIER_CUSTOMIZED_DEFAULT_LAYOUT_ID);
                    modelAndView.addObject("lessFileUrl", getCashierCustomizedFileUri(request, Constant.MERCHANT_CASHIER_CUSTOMIZED_DEFAULT_LAYOUT_ID,
                            Constant.MERCHANT_CASHIER_CUSTOMIZED_LESS_FILE_TYPE));
                    modelAndView.addObject("jsFileUrl", getCashierCustomizedFileUri(request, Constant.MERCHANT_CASHIER_CUSTOMIZED_DEFAULT_LAYOUT_ID,
                            Constant.MERCHANT_CASHIER_CUSTOMIZED_JS_FILE_TYPE));
                    modelAndView.addObject("payToolsOrder",JSON.toJSONString(CommonUtil.getValidPayTools(tools, null)));
                }else {
                    modelAndView.setViewName("newpc/pc_index");
                }
            }


        } catch (Throwable e) {
            String merchantNo = info == null ? null : info.getMerchantNo();
            return createNewPcErrorRV(token, false, "unitePc_error", e, SYSTEM_CODE, info, merchantNo, request);
        }
        return modelAndView;

    }

    /**
     * 查看支持的银行卡列表，来自商户配置中心
     *
     * @return
     */
    @RequestMapping(value = "/supportbanklist")
    @ResponseBody
    public Object querySupportBankList(String token, HttpServletRequest request) {
        RequestInfoDTO info = checkRequestInfoDTO(token);
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            Map<String,List<BankLimitAmountResponseVO>> bankLimitAmountListResponseDTO = ncCashierService.querySupportBankListFromMerchantConfig(info.getPaymentRequestId() + "", info.getMerchantNo(),CashierVersionEnum.PC.name());
            map.put("supportBanklist", bankLimitAmountListResponseDTO);
            return map;
        } catch (Throwable e) {
            map.put("bizStatus",AJAX_FAILED);
            return createErrorMsg(map,e,"supportbanklist_error",token, SYSTEM_CODE);
        }
    }


	/**
	 * PC网银支付首页模块 [ajax返回一个view] 
	 * 1 当网银首页加载成功时，返回网银首页vm模块 [newpc/ebank_index]
	 * 2 当网银首页加载失败时，返回失败原因
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/ebank/index")
    @ResponseBody
	public Object eBankIndex(String token ,String compatibleView) {
		logger.info("[monitor],event:nccashier_eBankIndex_request,token:{},compatibleView:{}", token, compatibleView);
		try {
			EBankSupportBanksVO response = eBankPayService.ebankIndexShow(token);
			ModelAndView mv = new ModelAndView();
			if(StringUtils.isNotEmpty(compatibleView) && "Y".equals(compatibleView)){
                mv.setViewName("newpc/ebank_index_ie");
            }else {
                mv.setViewName("newpc/ebank_index");
            }
			mv.addObject("response", response);
			return mv;
		} catch (Throwable e) {
			return createErrorMsg(null,e,"ebank_index_error",token, SYSTEM_CODE);
		}

	}


	/**
	 * pc网银支付 确认支付 
     * 1 网银下单成功 - 返回success，并提供token&查询结果的链接&支付遇到问题的链接
	 * 2 网银下单失败 - 返回fail，有失败原因和失败码
	 * 
	 * @param param
	 */
	@RequestMapping(value = "/ebank/pay")
	@ResponseBody
	public void ebankPay(EBankPayRequestVO param, HttpServletRequest request,HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_ebankPay_request,token:{}", param);
		EBankPayResponseVO data = null;
		try {
            param.setDirectEbankPay(false);
            // modify by meiling.zhuang：传用户IP，按照银行定制
            param.setNetPayerIp(PayerIpUtils.getNetPayerIp(request, param.getBankCode(), param.getEbankAccountType()));
			data = eBankPayService.ebankPay(param);
			data.setResultUrl(request.getContextPath()+getUrlNonParam(param.getToken(), request.getContextPath()+"/ebank/result/"));
			data.setQuestionUrl(ConstantUtil.QUICK_PAY_QUESTION_URL);
		} catch (Throwable t) {
			CashierBusinessException e = ExceptionUtil.handleException(t, SYSTEM_CODE);
			data = (data==null) ? new EBankPayResponseVO() : data;
			data.setErrorCode(e.getDefineCode());
			data.setErrorMsg(e.getMessage());
		}
		data.setToken(param.getToken());
		ajaxResultWrite(response, data);
	}


    /**
     * 充值系统请求收银台首页模块
     * @param token
     * @param compatibleView
     * @return
     */
    @RequestMapping(value = "/bacLoad/index")
    @ResponseBody
    public Object bacLoadIndex(String token ,String compatibleView,String type) {
        logger.info("[monitor],event:nccashier_bacLoadIndex_request,token:{},compatibleView:{}", token, compatibleView);

        try {
            EBankSupportBanksVO response = eBankPayService.getLoadSupportEBanks(token,type);
            ModelAndView mv = new ModelAndView();
            if(StringUtils.isNotEmpty(compatibleView) && "Y".equals(compatibleView)){
                mv.setViewName("newpc/recharge_ebank_index_ie");
            }else {
                mv.setViewName("newpc/recharge_ebank_index");
            }
            mv.addObject("response", response);
            mv.addObject("type", type);
            return mv;
        } catch (Throwable e) {
            return createErrorMsg(null,e,"bacLoadIndex_error",token, SYSTEM_CODE);
        }

    }





    /**
	 * 监听pc二维码已扫标识
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/scan/listen")
	@ResponseBody
	public void listenScanResult(String token,String isInstallment, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_pc_listenScanResult,token:{}", token);
		ScanListenResponseVO data = new ScanListenResponseVO();
		data.setToken(token);
		try {
			boolean isScan = qrCodeScanService.listenScanResult(token,isInstallment);
			data.setScan(isScan);
		} catch (Throwable t) {
			CashierBusinessException e = ExceptionUtil.handleException(t, SYSTEM_CODE);
			data.setErrorCode(e.getDefineCode());
			data.setErrorCode(e.getMessage());
		}
		ajaxResultWrite(response, data);

	}

	/**
	 * 监听支付处理器中支付结果是否可查状态 [快捷&扫码]
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/result/querystate")
	@ResponseBody
	public void listenPayResultQueryState(String token, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_pc_queryPayResult,token:{}", token);
		PayResultQueryStateListenVO data = new PayResultQueryStateListenVO();
        data.setResultQueryUrl("/query/result");
        try {
			data = newWapPayService.listenCanPayResultQuery(token);
		} catch (Throwable t) {
			CashierBusinessException e = ExceptionUtil.handleException(t, SYSTEM_CODE);
			data.setErrorCode(e.getDefineCode());
			data.setErrorCode(e.getMessage());
		}
		ajaxResultWrite(response, data);
	}

	/**
	 * pc快捷&扫码支付结果查询
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/query/result")
	@ResponseBody
	public void queryPayResult(String token, HttpServletRequest request,HttpServletResponse httpResponse) {
		logger.info("[monitor],event:nccashier_queryResult,token:{}", token);
		Map<String, Object> resData = new HashMap<String, Object>();
		resData.put("token", token);
		try{
			RequestInfoDTO info = checkRequestInfoDTO(token);
			TradeNoticeDTO payResult = newWapPayService.queryPayResult(info, token);
			resData.put("resultState", payResult.getTradeState());
			resData.put("merchantOrderId",payResult.getMerchantOrderId());
			// 成功态 - 返回成功页面展示的访问链接，见paySuccess方法
			if (TradeStateEnum.SUCCESS.equals(payResult.getTradeState())) {
				resData.put("successUrl",request.getContextPath()+getUrlNonParam(token,"/pay/success/"));
			}
			// 取消态 - 订单因超时被冲正取消掉，在当前页面提示用户，并允许重新下单，提示内容见产品原型 
			else if(TradeStateEnum.CANCEL.equals(payResult.getTradeState())){
				resData.put("resultState", TradeStateEnum.FAILED);
				resData.put("errormsg", ExceptionUtil.handleException(Errors.ORDER_CANCEL_ERROR, SYSTEM_CODE).getMessage());
				resData.put("errorcode", Errors.ORDER_CANCEL_ERROR.getCode());
			}
			// 失败 - 将失败码和失败原因展示在当前页面，并允许重新下单，展示样式见产品原型
			else if (TradeStateEnum.FAILED.equals(payResult.getTradeState())) {
				ErrorCodeDTO error = ExceptionUtil.handleException(payResult.getErrorCode(), payResult.getErrorMsg(), SYSTEM_CODE);
				resData.put("errormsg", error.getExternalErrorCode());
				resData.put("errorcode", error.getExternalErrorMsg());
            }
			// 支付中（未查到终态结果） - 弹窗，显示“重新支付/支付遇到问题”，点击重新支付，调用冲正接口，并关闭弹出框，允许重新支付
			else {
				resData.put("questionUrl", ConstantUtil.QUICK_PAY_QUESTION_URL);
			}

		}catch(Throwable t){
			CashierBusinessException e = ExceptionUtil.handleException(t, SYSTEM_CODE);
			resData.put("resultState", TradeStateEnum.FAILED.name());
			resData.put("errormsg", e.getMessage());
			resData.put("errorcode", e.getDefineCode());
		}
		ajaxResultWrite(httpResponse, resData);
	}
	
	/**
	 * pc快捷/扫码支付处理成功时，跳转页面
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/pay/success/{token}")
	public ModelAndView paySuccess(@PathVariable("token") String token, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
        RequestInfoDTO info = null;
		try{
            info = checkRequestInfoDTO(token);
            setOrderInfo(info,mv);
            TradeNoticeDTO payResult = newWapPayService.queryPayResult(info, token);
            mv.addObject("actualAmount", payResult.getActualAmount());
            logger.info("token={}实际支付金额actualAmount={}", token, payResult.getActualAmount());
			//支付成功-跳转到成功页面
			mv.addObject("merchantUrl", payResult.getFrontCallBackUrl());
			MerchantCashierCustomizedLayoutSelectVO merchantCashierCustomizedLayoutInfo = merchantCashierTemplateCustomizedService
					.queryMerchantCashierCustomizedLayoutSelectInfo(info.getMerchantNo());
            //嘉年华活动  add  jimin.zhou	  20171024
            payResult.setTradeSysNo(info.getTradeSysNo());
            CarnivalVO canrivalVo = newWapPayService.queryQualification4Carnival(payResult);
            logger.info("[JNH_嘉年华易宝大促]NewPcPayAction.paySuccess,token={},merNo:{},CarnivalVO:{}",
                    token, payResult.getMerchantNo(), JSONUtils.toJsonString(canrivalVo));
            if(canrivalVo.isShowCarnival()){
                mv.addObject("showCarnival",canrivalVo.isShowCarnival());
                mv.addObject("carnivalUrl",canrivalVo.getCarnivalUrl());
            }

            // 展示是否展示易宝元素
            mv.addObject("notShowYeepay", CommonUtil.judgeMerchantNotShowPcYeepayInfo(info.getMerchantNo()));
			if(StringUtils.isNotBlank(merchantCashierCustomizedLayoutInfo.getLayoutNo())){
				mv.setViewName("customizedPc/pay_success");
				setCustomizedHeadVm(request, mv, merchantCashierCustomizedLayoutInfo);	
			}else{
				mv.setViewName("newpc/pay_success");
			}


            //分期易成功页面显示
            if(payResult!=null && PayTool.ZF_FQY.name().equals(payResult.getPayTool())){
                try{
                    String period = payResult.getPeriod();
                    mv.addObject("bankCode", payResult.getBankCode()); //银行编码
                    mv.addObject("period", period); // 期数
                    mv.addObject("fee", "0.00"); // 期数
                    if(StringUtils.isNotBlank(period)){
                        BigDecimal realAmount = payResult.getPaymentAmount();
                        BigDecimal amountPerPeriod = realAmount.divide(new BigDecimal(period),2, RoundingMode.UP);
                        mv.addObject("amountPerPeriod", amountPerPeriod); // 每期应还(实际金额/期数)
                    }
                }catch (Exception e){

                }
            }

		}catch(Throwable e){
            String merchantNo = info == null ? null : info.getMerchantNo();
            return createNewPcErrorRV(token,false,"pc_successOrCancel_error",e,SYSTEM_CODE,info, merchantNo, request);
		}
		return mv;
	}
	
	/**
	 * 网银支付结果查询
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/ebank/result/{token}")
	public ModelAndView ebankPayResultQuery(@PathVariable("token") String token,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
        RequestInfoDTO info = null;
		try{
            info = checkRequestInfoDTO(token);
            MerchantCashierCustomizedLayoutSelectVO merchantCashierCustomizedLayoutInfo = merchantCashierTemplateCustomizedService
					.queryMerchantCashierCustomizedLayoutSelectInfo(info.getMerchantNo());					
			setOrderInfo(info,mv);
			TradeNoticeDTO payResult = newWapPayService.queryPayResult(info, token);
			//成功 - 跳转到成功页面
			if (TradeStateEnum.SUCCESS.equals(payResult.getTradeState())) {
				mv.addObject("merchantUrl", payResult.getFrontCallBackUrl());
				mv.addObject("actualAmount", payResult.getActualAmount());
				logger.info("token={}实际支付金额actualAmount={}", token, payResult.getActualAmount());
                //嘉年华活动    add  jimin.zhou	  20171024
                payResult.setTradeSysNo(info.getTradeSysNo());
                CarnivalVO canrivalVo = newWapPayService.queryQualification4Carnival(payResult);
                logger.info("[JNH_嘉年华易宝大促]NewPcPayAction.ebankPayResultQuery,token={},merNo:{},CarnivalVO:{}",
                        token, payResult.getMerchantNo(), JSONUtils.toJsonString(canrivalVo));
                if(canrivalVo.isShowCarnival()){
                    mv.addObject("showCarnival",canrivalVo.isShowCarnival());
                    mv.addObject("carnivalUrl",canrivalVo.getCarnivalUrl());
                }
				if(StringUtils.isNotBlank(merchantCashierCustomizedLayoutInfo.getLayoutNo())){
					mv.setViewName("customizedPc/pay_success");
					setCustomizedHeadVm(request, mv, merchantCashierCustomizedLayoutInfo);	
				}else{
					mv.setViewName("newpc/pay_success");
				}
			}
			//失败 - 跳转到失败页面，提供失败原因&失败码，并允许重新支付
			else if(TradeStateEnum.FAILED.equals(payResult.getTradeState())){
				ErrorCodeDTO error = ExceptionUtil.handleException(payResult.getErrorCode(), payResult.getErrorMsg(), SYSTEM_CODE);
				mv.addObject("errorcode", error.getExternalErrorCode());
				mv.addObject("errormsg", error.getExternalErrorMsg());
				mv.addObject("repayUrl", request.getContextPath()+getUrlNonParam(token, "/request/"));
				if(StringUtils.isNotBlank(merchantCashierCustomizedLayoutInfo.getLayoutNo())){
					mv.setViewName("customizedPc/pay_fail");
					setCustomizedHeadVm(request, mv, merchantCashierCustomizedLayoutInfo);	
				}else{
					mv.setViewName("newpc/pay_fail");
				}
			}
			//冲正 - 跳转到冲正页面
			else if(TradeStateEnum.CANCEL.equals(payResult.getTradeState())){
				mv.addObject("errormsg", ExceptionUtil.handleException(Errors.ORDER_CANCEL_ERROR, SYSTEM_CODE).getMessage());
				mv.addObject("errorcode", Errors.ORDER_CANCEL_ERROR.getCode());
				if(StringUtils.isNotBlank(merchantCashierCustomizedLayoutInfo.getLayoutNo())){
					mv.setViewName("customizedPc/pay_fail");
					setCustomizedHeadVm(request, mv, merchantCashierCustomizedLayoutInfo);	
				}else{
					mv.setViewName("newpc/pay_fail");
				}	
			}
			//支付中 - 跳转到处理中页面
			else{
				if(StringUtils.isNotBlank(merchantCashierCustomizedLayoutInfo.getLayoutNo())){
					mv.setViewName("customizedPc/paying");
					setCustomizedHeadVm(request, mv, merchantCashierCustomizedLayoutInfo);	
				}else{
					mv.setViewName("newpc/paying"); //支付处理中的页面，提供查询支付结果按钮
				}
				mv.addObject("requeryUrl", request.getContextPath()+getUrlNonParam(token,"/ebank/result/"));
			}
		} catch(Throwable e){
            String merchantNo = info == null ? null : info.getMerchantNo();
            return createNewPcErrorRV(token,false,"pc_ebankPayResultQuery_error", e, SYSTEM_CODE,info, merchantNo, request);
		}
		return mv;
	}
	
	
	/**
	 * 获取支付身份信息
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/getOwnerInfo",method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public void getOwnerInfo(String token, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_pc_getOwnerInfo,token:{}", token);
		RequestInfoDTO info = null;
		CardOwnerConfirmResDTO cardOwnerConfirmResDTO = null;
		
		try {
			info = checkRequestInfoDTO(token);
			cardOwnerConfirmResDTO = newWapPayService.getOwnersInfo(info);
		} catch (Throwable t) {
			logger.error("获取支付身份信息异常,token为"+token,t);
		}
		ajaxResultWrite(response, cardOwnerConfirmResDTO);

	}
	
	
	
	/**
	 * 设置支付身份信息
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/setOwnerInfo",method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public void setOwnerInfo(String token, String idno, String name,
			String bindId, HttpServletResponse response) {
        RequestInfoDTO info = null;
		try {
            name = Base64Util.decode(name);
            idno = Base64Util.decode(idno);
            logger.info("[monitor],event:nccashier_pc_setOwnerInfo,token:{},idno:{},name:{},bindId:{}",token, idno, name, bindId);
            info = checkRequestInfoDTO(token);
			if (StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(idno)) {
				newWapPayService.setCardOwner(bindId, name, idno, info);
			}
			if (StringUtils.isNotBlank(bindId)) {
				newWapPayService.setCardOwner(bindId, null, null, info);
			}
		
		} catch (Throwable t) {
			logger.error("设置支付身份信息异常,token为" + token, t);
		}
	}
	
	
	private EBankPayResponseVO directToEBank(String token, String bankCode, String ebankAccountType, String clientId, String netPayerIp){
        EBankPayRequestVO eBankPayRequestVO = new EBankPayRequestVO();
        eBankPayRequestVO.setToken(token);
        eBankPayRequestVO.setBankCode(bankCode);
        eBankPayRequestVO.setEbankAccountType(ebankAccountType);
        eBankPayRequestVO.setClientId(clientId);
        eBankPayRequestVO.setDirectEbankPay(true);
        eBankPayRequestVO.setNetPayerIp(netPayerIp);
        EBankPayResponseVO data = eBankPayService.ebankPay(eBankPayRequestVO);
        return data;
    }

	/**
	 * 返回会员支付页面模块，若有异常，返回错误信息
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/accountpay/index")
	@ResponseBody
	public Object accountPayIndex(String token) {
		logger.info("[monitor],event:pc_accountpayindex_request,token:{}", token);
		try {
			RequestInfoDTO info = newWapPayService.validateRequestInfoDTO(token);

			PayExtendInfo payExtendInfo = ncCashierService.getPayExtendInfo(info.getPaymentRequestId(), token);
			if (!payExtendInfo.containsPayType(PayTypeEnum.ZF_ZHZF)) {
				throw new CashierBusinessException(Errors.NOT_OPEN_PRODUCT_ERROR);
			}
			accountPayService.checkExpireTime(payExtendInfo.getExpireTime());
			ModelAndView mv = new ModelAndView();
			mv.setViewName("newpc/account_pay");
			mv.addObject("token", token);
			return mv;
		} catch (Throwable t) {
			return createErrorMsg(null, t, "pc_accountpay_index_error", token, SYSTEM_CODE);
		}
	}

	/**
	 * 账户支付下单接口
	 * 
	 * @param param
	 * @param bindingResult
	 * @param httpResponse
	 */
	@RequestMapping(value = "/accountpay/pay")
	public @ResponseBody void accountPay(@Valid AccountPayRequestVO param, BindingResult bindingResult,
			HttpServletResponse httpResponse) {
		BasicResponseVO responseVO = null;
		try {
            Base64Util.decryptAccountPayRequestVO(param);
            logger.info("[monitor],event:pc_accountpay_request,param:{}", param);
			RequestInfoDTO requestInfo = accountPayService.validateAccountPayParam(param, bindingResult);
			responseVO = accountPayService.accountPay(param, requestInfo);
		} catch (Throwable t) {
			logger.error("[monitor],event:pc_accountpay_error,param:" + param, t);
			responseVO = handleAjaxException(t, SYSTEM_CODE);
		}
		ajaxResultWrite(httpResponse, responseVO);
	}

    /**
     * 重定向到商户回调地址（网银直连，解决商户回调地址超长的问题）
     * @return
     */
    @RequestMapping(value = "/ebank/merchantredirect/{token}")
    public Object redirectMerchantCallBack(@PathVariable("token") String token){
        logger.info("[monitor],event:pc_ebank_merchantredirect,token:{}", token);
        String urlPrefix =CommonUtil.getSysConfigFrom3G(CommonUtil.NCCASHIER_ENTER_WAP_URL_PREFIX);
        if (StringUtils.isEmpty(urlPrefix)) {
            urlPrefix = "https://shouyin.yeepay.com/nc-cashier-wap/";
        }
        String yeepayCallBack = urlPrefix + "newpc/ebank/result/" + token;
        try {
            RequestInfoDTO info = checkRequestInfoDTO(token);
            String merchantPageCallBack = ncCashierService.getMerchantPageCallBack(info);
            if(StringUtils.isNotEmpty(merchantPageCallBack)){
                return new RedirectView(merchantPageCallBack);
            }else {
                return new RedirectView(yeepayCallBack);
            }
        }catch (Throwable t) {
            logger.warn("getMerchantPageCallBack() 重定向商户回调地址，获取商户地址异常，t=", t);
            return new RedirectView(yeepayCallBack);
        }
    }

    /**
     * 获取公众号二维码，展示在PC查询页、成功页及失败页
     * @return
     */
    @RequestMapping(value = "/yeepay/qrcode")
    @ResponseBody
    public String getYeepayWechatQRCode(String merchantNo,String orderId,String token) {
        String url = "";
        try {
            if (StringUtils.isNotBlank(merchantNo) && StringUtils.isNotBlank(orderId)) {
                //优先直接按merchantNo和orderId去获取
                url = ncCashierService.getYeepayWechatQRCode(merchantNo, orderId);
                return url;
            }
            RequestInfoDTO requestInfoDTO = checkRequestInfoDTO(token);
            if (requestInfoDTO == null) {
                logger.error("getYeepayWechatQRCode() 获取公众号二维码，入参token = {} , 根据token获取请求信息失败", token);
                return url;
            }
            Long paymentRequestId = requestInfoDTO.getPaymentRequestId();
            url = ncCashierService.getYeepayWechatQRCode(Long.toString(paymentRequestId));
            return url;
        } finally {
            logger.info("getYeepayWechatQRCode() 获取公众号二维码，入参merchantNo = {} ,orderId = {} , 获取到url = {}", merchantNo, orderId, url);
        }
    }

    /**
     * 绑卡支付，在已绑卡列表，主动解绑选中的卡
     * @param token
     * @param bindId
     * @return
     */
    @RequestMapping(value = "/unbindCard")
    @ResponseBody
    public Object unbindCard(String token,String bindId){
        logger.info("unbindCard() 绑卡支付，主动解绑卡，入参token = {}, bindId = {}", token, bindId);
        Map<String,String> resultMap = new HashMap<String, String>();
        try {
            RequestInfoDTO requestInfoDTO = checkRequestInfoDTO(token);
            if(requestInfoDTO == null) {
                logger.error("unbindCard() 绑卡支付，主动解绑卡，入参token = {} , 根据token获取请求信息失败", token);
                resultMap.put("bizStatus","fail");
                resultMap.put("errorCode",Errors.SYSTEM_INPUT_EXCEPTION.getCode());
                resultMap.put("errorMsg",Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
                return JSONObject.toJSONString(resultMap);
            }
            Long paymentRequestId = requestInfoDTO.getPaymentRequestId();
            newWapPayService.unbindCard(Long.toString(paymentRequestId),bindId);
            resultMap.put("bizStatus","success");
            return JSONObject.toJSONString(resultMap);
        }catch (YeepayBizException e){
            logger.error("unbindCard() 绑卡支付，主动解绑卡，业务异常：", e);
            resultMap.put("bizStatus","fail");
            resultMap.put("errorCode",e.getDefineCode());
            resultMap.put("errorMsg",e.getMessage());
            return JSONObject.toJSONString(resultMap);
        }catch (Throwable t){
            logger.error("unbindCard() 绑卡支付，主动解绑卡，异常：", t);
            resultMap.put("bizStatus","fail");
            resultMap.put("errorCode",Errors.SYSTEM_EXCEPTION.getCode());
            resultMap.put("errorMsg",Errors.SYSTEM_EXCEPTION.getMsg());
            return JSONObject.toJSONString(resultMap);
        }
    }
}