package com.yeepay.g3.app.nccashier.wap.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yeepay.g3.app.nccashier.wap.enumtype.BrowserType;
import com.yeepay.g3.app.nccashier.wap.utils.*;
import com.yeepay.g3.app.nccashier.wap.vo.*;
import com.yeepay.g3.facade.nccashier.dto.BankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.BankCardReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.BankSupportDTO;
import com.yeepay.g3.facade.nccashier.dto.BussinessTypeResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CardOwnerConfirmResDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;
import com.yeepay.g3.facade.nccashier.dto.PassCardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.TradeNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 无卡收银台的wap版（一键）
 * Created by xiewei on 15-10-12.
 */
@Controller
@RequestMapping(value = "/wap", method = {RequestMethod.POST, RequestMethod.GET})
public class WapPayAction extends WapBaseAction {
	
	static final Logger logger = LoggerFactory.getLogger(WapPayAction.class);
	
	/**
	 * 本controller对应的错误码系统编码
	 */
	private static final SysCodeEnum SYSTEM_CODE = SysCodeEnum.NCCASHIER_WAP;

	/**
	 * 收到请求后处理验证（业务方请求nccashier之前需要处理的  页面路由
	 *
     * @param request
     * @param requestId
     * @param merchantNo
     * @param fromNewWAP
     * @param token
     * @param directType
     * @param bizType
     * @return
     */
	@RequestMapping(value = "/request/{merchantNo}/{requestId}",method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestProcess(HttpServletRequest request,
			@PathVariable("requestId") String requestId,
			@PathVariable("merchantNo") String merchantNo,
			boolean fromNewWAP,String token,String directType,String bizType) {
		logger.info("[monitor],event:nccashier_requestProcess_Request,requestId:{},merchantNo:{}", requestId, merchantNo);
		String encryptRequestId = requestId;
		boolean tokenExist = false;
		if(StringUtils.isEmpty(token)){
			token = UUID.randomUUID().toString();
		}else{
			tokenExist = true;
		}
		try {
			String ypip = getUserIp(request);
			// 获取用户终端UA
			String ua = getUserUA(request);
			requestId = AESUtil.routeDecrypt(merchantNo, requestId);
			if (!tokenExist) {
				saveUserAccess(requestId, merchantNo, token, ypip, ua);
			}
			PayExtendInfo extendInfo = getPayExtendInfo(requestId,token);
			Object wapRouterReturn = wapRouter(requestId, encryptRequestId, merchantNo, fromNewWAP, token,
					extendInfo,directType,request,ua);
			if(wapRouterReturn instanceof String){
				ModelAndView mv = new ModelAndView();
				mv.addObject("directWechatUrl",wapRouterReturn);
				mv.setViewName("wechatBlank");
				return mv;
			}else{
				return wapRouterReturn;
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestProcess_decRequestid 异常,requestId:" + requestId
					+ ",merchantNo:" + merchantNo, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), null);
		}
	}

	@RequestMapping(value = "/fail", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView toFail(String token, String errorCode, String errorMsg, boolean needRepay,
			String payTool, HttpServletRequest request) {
		ModelAndView view = createErrorMV(token, errorCode, errorMsg, null);
		view.addObject("payTool",payTool);//前端会根据payTool，选择展示的文案
		if (needRepay) {
			RequestInfoDTO info = null;
			try {
				info = newWapPayService.validateRequestInfoDTO(token);
			} catch (Throwable t) {
				logger.warn("wap_to_failPage异常, token=" + token, t);
			}
			if (info != null) {
				String repayurl = null;
				OrderProcessorRequestDTO urlParamInfo = info.getUrlParamInfo();
				if(urlParamInfo!=null){
					String directPayType = urlParamInfo.getDirectPayType();
					repayurl = getH5IndexUrl(info.getPaymentRequestId(), info.getMerchantNo(), token, request,directPayType);
				}else {
					repayurl = getH5IndexUrl(info.getPaymentRequestId(), info.getMerchantNo(), token, request,null);
				}

				view.addObject("repayurl", repayurl);
			}
		}
		return view;
	}

	private void saveUserAccess(String requestId, String merchantNo, String token, String ypip,
			String ua) {
		UserAccessDTO userAccess = new UserAccessDTO();
		userAccess.setPaymentRequestId(requestId);
		userAccess.setUserIp(ypip);
		if (ua.length() > 300) {
			ua = ua.substring(0, 300);
		}
		userAccess.setUserUa(ua);
		userAccess.setTokenId(token);
		userAccess.setMerchantNo(merchantNo);
		ncCashierService.saveUserAccess(userAccess);
	}

	/**
	 * 一键支付wap重定向
	 *
	 * @param requestId
	 * @param token
	 * @return
	 */
	private Object toYJZFWap(long requestId,String token,String merchantNo,HttpServletRequest request){
		BussinessTypeResponseDTO bussinessTypeResponseDTO = ncCashierService.routerPayWay(requestId);
		if (requestId == bussinessTypeResponseDTO.getRequestId()
				&& ProcessStatusEnum.SUCCESS == bussinessTypeResponseDTO.getProcessStatusEnum()) {
			RedirectView view = null;
			if (BusinessTypeEnum.FIRSTPAY == bussinessTypeResponseDTO.getBusinessTypeEnum()
					|| BusinessTypeEnum.FIRSTPASS == bussinessTypeResponseDTO
							.getBusinessTypeEnum()) {
				view = new RedirectView(CommonUtil.getPreUrl(merchantNo,request)+wapctx + "/request/first?token=" + token);
			} else if (BusinessTypeEnum.FIRSTPASSCARDNO == bussinessTypeResponseDTO
					.getBusinessTypeEnum()) {
				view = new RedirectView(CommonUtil.getPreUrl(merchantNo,request)+wapctx + "/first/cardinfo?token=" + token);
			} else {
				view = new RedirectView(CommonUtil.getPreUrl(merchantNo,request)+wapctx + "/request/bind?token=" + token);
			}
			return view;
		}else{
			CashierBusinessException ex = ExceptionUtil.handleException(Errors.SYSTEM_EXCEPTION, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), null);
		}
	}

	/**
	 * 首次支付入口
	 *
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/request/first", method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestFirst(String token, HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_requestFirst_request,token:{}", token);
		RequestInfoDTO info = null;
		try {
			info = ncCashierService.requestBaseInfo(token);
			ModelAndView mv = new ModelAndView();
			mv.setViewName("first_index");
			//定制不展示易宝公司信息
			if(CommonUtil.notShowYeepayCompanyInfo(info.getMerchantNo(), info.getOrderSysNo())){
				mv.addObject(CommonUtil.NOT_SHOW_YEEPAY_COMPANY_INFO_FLAG, true);
			}
			supplyBaseModelViewInfo(info, token, mv);
			//获取是否支持定制活动
			mv.addObject("showActivity",ncCashierService.queryQualification4Activities());
			return mv;
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestFirst_BusinException,token:" + token, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			} else {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestFirst_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}

	}

	/**
	 * 请求绑卡支付
	 *
	 * @param token
	 * @param bindId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/request/bind", method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestBind(String token, String bindId, HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_requestBind_request,token:{},bindId:{}", token, bindId);
		RequestInfoDTO info = null;
		try {
			// 1 校验token并获取支付请求基本信息
			info = newWapPayService.validateRequestInfoDTO(token);
			// 2 获得支持的绑卡列表
			BankCardReponseDTO response = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
			BankCardDTO bindCardDTO = response.getBankCardDTO();
			if (bindCardDTO == null) {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
			}
			// 3 下单
			else {
				ModelAndView view = new ModelAndView();
				Map<String, Object> model = newWapPayService.getBindPayModel(info, token, bindId, bindCardDTO);

				for (String key : model.keySet()) {
					view.addObject(key, model.get(key));
				}
                PageRedirectDTO pageRedirectParam = (PageRedirectDTO) model.get("pageRedirectParam");
                if (pageRedirectParam !=null){
                    view.addObject("pageRedirectParam",JSONArray.toJSONString(pageRedirectParam));
                }
				view.setViewName("bind_index");
				//定制不展示易宝公司信息
				if(CommonUtil.notShowYeepayCompanyInfo(info.getMerchantNo(), info.getOrderSysNo())){
					view.addObject(CommonUtil.NOT_SHOW_YEEPAY_COMPANY_INFO_FLAG, true);
				}
				view.addObject("showChangeCard",response.getShowChangeCard());
				supplyBaseModelViewInfo(info, token, view);
				return view;
			}
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestBind_BusinException,token:" + token
					+ ",bindId:" + bindId, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				// modify by meiling.zhuang: 解决调用该方法处将返回值强转为ModelAndView的bug
				String queryUrl = CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token;
				ModelAndView queryView = new ModelAndView("redirect:" + queryUrl);
				queryView.addObject("isResultPage", true);
				return queryView;
//				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
//				ModelMap map = new ModelMap();
//				rv.setAttributesMap(map);
//				return rv;
			}else{
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestBind_SystemException,token:" + token + ",bindId:" + bindId,
					e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
	}

	/**
	 * 首次透传卡号
	 *
	 * @param cardNo
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/first/cardinfo")
	public Object cardInfo(String cardNo, String token, String isChangeCard,HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_cardInfo_request,token:{}", token);
		cardNo = Base64Util.decode(cardNo);
		return handleCardInfo(cardNo,token,isChangeCard,request);
	}

	/**
	 * 首次透传卡号
	 */
	@RequestMapping(value = "/ajax/first/cardinfo")
	@ResponseBody
	public void cardInfo4Ajax(String cardno, String token,HttpServletResponse response){
		logger.info("[monitor],event:nccashier_cardInfo_ajax_request,token:{}", token);
		cardno = Base64Util.decode(cardno);
		RequestInfoDTO info = null;
		BusinessTypeEnum businessType = null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}

			info = ncCashierService.requestBaseInfo(token);
			BankCardReponseDTO bankCardReponse =
					ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);

			businessType = bankCardReponse.getBusinessTypeEnum();
			BankCardDTO bindCardDTO = bankCardReponse.getBankCardDTO();
			PassCardInfoDTO passCardInfoDTO = bankCardReponse.getPassCardInfoDTO();
			if ((businessType == BusinessTypeEnum.FIRSTPASSCARDNO || businessType == BusinessTypeEnum.BINDPASSCARDNO)&& passCardInfoDTO != null) {
				cardno = passCardInfoDTO.getCardNo();
			}

			if (StringUtils.isNotEmpty(cardno)) {
				cardno = cardno.replaceAll(" ", "");
			}
			if (StringUtils.isEmpty(cardno) || !DataUtil.isBankCardNum(cardno)) {
				// 卡号空，要重新下单
				data.put("errorcode", Errors.INVALID_BANK_CARD_NO.getCode());
				data.put("errormsg", Errors.INVALID_BANK_CARD_NO.getMsg());
				data.put("status", AJAX_FAILED);
				ajaxResultWrite(response, data);
				return;
			} else {
				Map<String, Object> model = newWapPayService.getFirstPayModel(
						info.getPaymentRequestId(), cardno, businessType, bindCardDTO);
				BankCardDTO bc = (BankCardDTO) model.get("bc");
				data.put("haspay",(Boolean)model.get("haspay"));
				data.put("bankCode",bc.getBankCode());
				data.put("bankName",bc.getBankName());
				data.put("cardType",bc.getCardtype());
				data.put("cardlater",cardno.substring(cardno.length() - 4, cardno.length()));

			}
			data.put("status", AJAX_SUCCESS);
			ajaxResultWrite(response, data);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_cardInfo_request_BusinException,token:" + token,
					e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			data.put("errorcode", ex.getDefineCode());
			data.put("errormsg", ex.getMessage());
			data.put("status", AJAX_FAILED);
			ajaxResultWrite(response, data);
		} catch (Throwable e) {
			logger.error(
					"[monitor],event:nccashier_cardInfo_request_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			data.put("errorcode", ex.getDefineCode());
			data.put("errormsg", ex.getMessage());
			data.put("status", AJAX_FAILED);
			ajaxResultWrite(response, data);
		}
	}

	/**
	 * 一键支付，预路由下单
	 *
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/request/preRouter", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public void request4preRouter(String token, CardInfoDTO cardInfo, HttpServletResponse response) throws IOException {
		logger.info("[monitor],event:nccashier_request4preRouter_request,token:{}", token);
        Map<String, Object> data = new HashMap<String, Object>();
		try {
			//1、下单
			Base64Util.decryptCardInfoDTO(cardInfo);
			newWapPayService.validateCardInfo(null, cardInfo);
			Map<String, Object> result = newWapPayService.createPayment4RedirectUrl(token, cardInfo);
			//2、返回类型是否跳转页面,不跳转则返回补充项和短验证码类型
			result.put("status", AJAX_SUCCESS);
			ajaxResultWrite(response, result);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestPreRouter_BusinException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
            data.put("errorcode", ex.getDefineCode());
            data.put("errormsg", ex.getMessage());
            data.put("status", AJAX_FAILED);
            ajaxResultWrite(response, data);
		} catch (Throwable e) {
			logger.warn("[monitor],event:nccashier_requestPreRouter_bussinessException，token:" + token,
					e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
            data.put("errorcode", ex.getDefineCode());
            data.put("errormsg", ex.getMessage());
            data.put("status", AJAX_FAILED);
            ajaxResultWrite(response, data);
		}
	}

	/**
	 * 处理根据卡号获取卡信息的业务，将卡号解密的内容提出去，以便兼容后端直接调用
	 * @param cardNo
	 * @param token
	 * @param isChangeCard
	 * @param request
	 * @return
	 */
	private ModelAndView handleCardInfo(String cardNo, String token, String isChangeCard,HttpServletRequest request){
		RequestInfoDTO info = null;
		BusinessTypeEnum businessType = null;
		ModelAndView mv = null;
		try {
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			// 如果是换卡支付清空用户访问信息表中存在的上一个支付记录的recordid
			if ("changCard".equals(isChangeCard)) {
				ncCashierService.clearRecordId(token);
			}

			info = ncCashierService.requestBaseInfo(token);
			BankCardReponseDTO response =
					ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);

			businessType = response.getBusinessTypeEnum();
			BankCardDTO bindCardDTO = response.getBankCardDTO();
			PassCardInfoDTO passCardInfoDTO = response.getPassCardInfoDTO();
			if ((businessType == BusinessTypeEnum.FIRSTPASSCARDNO || businessType == BusinessTypeEnum.BINDPASSCARDNO)&& passCardInfoDTO != null) {
				cardNo = passCardInfoDTO.getCardNo();
			}

			if (StringUtils.isNotEmpty(cardNo)) {
				cardNo = cardNo.replaceAll(" ", "");
			}
			if (StringUtils.isEmpty(cardNo) || !DataUtil.isBankCardNum(cardNo)) {
				// 卡号空，要重新下单
				mv = createCardInfoErrorMV(businessType, info, token, request);
				mv.addObject("errormsg", Errors.INVALID_BANK_CARD_NO.getMsg());
				mv.addObject("cardNo", cardNo);
			} else {
				mv = new ModelAndView();
				mv.setViewName("first_cardInfo");
				//定制不展示易宝支付协议
				if(CommonUtil.notShowYeepayAgreementInfo(info.getMerchantNo(), info.getOrderSysNo())){
					mv.addObject(CommonUtil.NOT_SHOW_YEEPAY_AGREEMENT_INFO_FLAG, true);
				}
				//定制不展示易宝公司信息
				if(CommonUtil.notShowYeepayCompanyInfo(info.getMerchantNo(), info.getOrderSysNo())){
					mv.addObject(CommonUtil.NOT_SHOW_YEEPAY_COMPANY_INFO_FLAG, true);
				}
				mv.addObject("showChangeCard",response.getShowChangeCard());

				logger.info("自动绑卡提示autoBindTip={}", response.getAutoBindTipText());
				// modify 为某些商户定制的 自动绑卡提示
				mv.addObject("autoBindTip", response.getAutoBindTipText());
				supplyBaseModelViewInfo(info, token, mv);
				Map<String, Object> model = newWapPayService.getFirstPayModel(
						info.getPaymentRequestId(), cardNo, businessType, bindCardDTO);
				for (String key : model.keySet()) {
					mv.addObject(key, model.get(key));
				}
			}
			return mv;
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_cardInfo_request_BusinException,token:" + token,
					e);
			mv = createCardInfoErrorMV(businessType, info, token, request);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			mv.addObject("errormsg", ex.getMessage());
//			mv.addObject("cardNo", cardNo);
			return mv;
		} catch (Throwable e) {
			logger.error(
					"[monitor],event:nccashier_cardInfo_request_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			mv = createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
			return mv;
		}
	}

	/**
	 * 发送短验AJAX
	 *
	 * @param token
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajax/smsSend", method = {RequestMethod.POST})
	@ResponseBody
	public void smsSend(HttpServletRequest request, CardInfoDTO cardInfo, String token,
			String bindId, ReqSmsSendTypeEnum reqSmsSendTypeEnum, HttpServletResponse response)
					throws IOException {
		logger.info("[monitor],event:nccashier_smsSend_request,token:{},smsSend:{},bindId:{}",
				token, bindId, reqSmsSendTypeEnum);
		PrintWriter out = response.getWriter();
		try {
			Base64Util.decryptCardInfoDTO(cardInfo);
			newWapPayService.validateCardInfoAndTimeout(cardInfo, token, bindId);
			ReqSmsSendTypeEnum reqSmsSendTypeEnum1 = newWapPayService.createPaymentOrSendSMS(token,
					bindId, reqSmsSendTypeEnum, cardInfo);
			ajaxSmsReturn(out, cardInfo, AJAX_SUCCESS, reqSmsSendTypeEnum1, null, null);
			logger.info("invoke WapPayAction.smsSend Ajax返回成功...token:{}", token);
		}  catch (Throwable e) {
			logger.error("[monitor],event:nccashier_smsSend_sendSms_exception，token:" + token, e);
			//短信验证码频繁*秒 错误码定制 
			CashierBusinessException ex = null;
			if(e instanceof CashierBusinessException && Errors.GET_SMS_FREQUENT.getCode().equals(((CashierBusinessException)e).getDefineCode())){
				ex = (CashierBusinessException)e;
			}
			else{
				ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			}
			ajaxSmsReturn(out, cardInfo, AJAX_FAILED, null, ex.getDefineCode(), ex.getMessage());
		}

	}
	
	/**
	 * 绑卡支付发短验（发短验时，会先验证必填项，只有必填项通过，才会发短验）
	 * 
	 * @param request
	 * @param token
	 * @param bindId
	 * @param smsType
	 * @param needBankCardDTO
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajax/bindSmsSend", method = {RequestMethod.POST})
	@ResponseBody
	public void bindSmsSend(HttpServletRequest request, String token,
			String bindId, ReqSmsSendTypeEnum smsType, NeedBankCardDTO needBankCardDTO,HttpServletResponse response)
					throws IOException {
		PrintWriter out = response.getWriter();
		try {
			Base64Util.decryptNeedBankCardDTO(needBankCardDTO);
			logger.info("[monitor],event:nccashier_bindSmsSend_request,token:{},needBankCardDTO:{},bindId:{}",token, needBankCardDTO, bindId);
			newWapPayService.validateBindCardAndTimeout(needBankCardDTO, token, bindId);
			newWapPayService.bindSendSMS(smsType, needBankCardDTO, token, bindId);
			ajaxSmsReturn(out, null, AJAX_SUCCESS, smsType, null, null);
			logger.info("bindSmsSend Ajax返回成功...token:{}", token);
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_bindSmsSend_sendSms_exception，token:" + token + ",needBankCardDTO:" + needBankCardDTO + ",bindId:" + bindId + ",异常：", e);
			CashierBusinessException ex = null;
			if(e instanceof CashierBusinessException && Errors.GET_SMS_FREQUENT.getCode().equals(((CashierBusinessException)e).getDefineCode())){
				ex = (CashierBusinessException)e;
			}
			else{
				ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			}
			ajaxSmsReturn(out, null, AJAX_FAILED, null, ex.getDefineCode(), ex.getMessage());
		}

	}

	/**
	 * 首次支付ajax确认接口
	 * 
	 * @param cardInfo
	 * @param verifycode
	 * @param token
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/ajax/pay/first", method = {RequestMethod.POST})
	@ResponseBody
	public void firstPay(CardInfoDTO cardInfo, String verifycode, String token, HttpServletRequest request,
			HttpServletResponse response) {
		RequestInfoDTO info = null;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("token", token);
		try {
			Base64Util.decryptCardInfoDTO(cardInfo);
			logger.info("[monitor],event:nccashier_firstPay_request,token:{},cardInfo:{}",token, cardInfo);
			info = newWapPayService.validateRequestInfoDTO(token);
			newWapPayService.validateCardInfo(null, cardInfo);
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_firstPay_exception，token:"+token+",cardInfo:"+cardInfo+"，异常：", e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			data.put("errorcode", ex.getDefineCode());
			data.put("errormsg", ex.getMessage());
			data.put("bizStatus", AJAX_FAILED);
			ajaxResultWrite(response, data);
			return;
		}
		try {
			newWapPayService.firstPay(info, token, cardInfo, verifycode);
			data.put("bizStatus", AJAX_SUCCESS);
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_firstPay_exception，token:{},cardInfo:{}", token,cardInfo, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			// 短信验证码发送或校验失败，允许停留在当前页重新发短验
			boolean smsValidateFail = "N400094".equals(ex.getDefineCode()) || "N400091".equals(ex.getDefineCode())
					|| "P400094".equals(ex.getDefineCode()) || "P400091".equals(ex.getDefineCode());
			data.put("bizStatus", smsValidateFail ? SMS_FAILD : AJAX_FAILED);
			data.put("errorcode", ex.getDefineCode());
			data.put("errormsg", ex.getMessage());
			if (!smsValidateFail) {
				data.put("needRepay", true);
			}
		}
		ajaxResultWrite(response, data);
	}

	/**
	 * 首次支付-确认支付请求入口
	 *
	 * @param token
	 * @param verifycode
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pay/first", method = {RequestMethod.POST})
	public Object firstPay(CardInfoDTO cardInfo, String verifycode, String token,
			HttpServletResponse response, HttpServletRequest request) {
		RequestInfoDTO info = null;
		try {
			Base64Util.decryptCardInfoDTO(cardInfo);
			logger.info("[monitor],event:nccashier_firstPay_request,token:{},cardInfo:{}",token, cardInfo);
			info = newWapPayService.validateRequestInfoDTO(token);
			newWapPayService.validateCardInfo(null, cardInfo);
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_firstPay_exception，token:"+token+",cardInfo:"+cardInfo+"，异常：", e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
		try {
			newWapPayService.firstPay(info, token, cardInfo, verifycode);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_firstPay_bussinessException，token:{},cardInfo:{}",token, cardInfo, e);
			ModelAndView mv = handleCardInfo(cardInfo.getCardno(), token, "", request);
			String errorcode = StringUtils.isEmpty(e.getDefineCode()) ? "" : "(" + e.getDefineCode() + ")";
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			mv.addObject("errormsg", ex.getMessage() + errorcode);
			mv.addObject("cardInfo", cardInfo);
			return mv;
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_firstPay_exception，token:{},cardInfo:{}", token,cardInfo, e);
			ModelAndView mv = handleCardInfo(cardInfo.getCardno(), token, "", request);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			mv.addObject("errormsg", ex.getMessage());
			mv.addObject("cardInfo", cardInfo);
			return mv;
		}
		RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
		ModelMap map = new ModelMap();
		rv.setAttributesMap(map);
		return rv;
	}
	
	
	/**
	 * 异步确认支付接口
	 * 
     * @param bindId
     * @param token
     * @param verifycode
     * @param needBankCardDTO
     * @param request
     * @param response
     */
	@RequestMapping(value = "/ajax/pay/bind", method = { RequestMethod.POST })
	@ResponseBody
	public void bindPay(String bindId, String token, String verifycode, NeedBankCardDTO needBankCardDTO, HttpServletRequest request, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_ajax_bindPay_request,token:{},bindId:{}", token, bindId);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("token", token);
		RequestInfoDTO info = null;
		try {
			if (StringUtils.isBlank(token) || StringUtils.isBlank(bindId)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			info = ncCashierService.requestBaseInfo(token);
		} catch (Throwable e) {
			// Tips 这种情况应该是不允许重新支付的
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			data.put("errorcode", ex.getDefineCode());
			data.put("errormsg", ex.getMessage());
			data.put("bizStatus", AJAX_FAILED);
			ajaxResultWrite(response, data);
			return;
		}
		try {
			Base64Util.decryptNeedBankCardDTO(needBankCardDTO);
			newWapPayService.bindPay(info, token, bindId, needBankCardDTO, verifycode);
			data.put("bizStatus", AJAX_SUCCESS);
		} catch (Throwable e) {
			logger.warn(
					"[monitor],event:nccashier_ajax_bindPay_exception,token:" + token + ",bindId:" + bindId + "，异常：",
					e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			// 短信验证码发送或校验失败，允许停留在当前页重新发短验
			boolean smsValidateFail = "N400094".equals(ex.getDefineCode()) || "N400091".equals(ex.getDefineCode())
					|| "P400094".equals(ex.getDefineCode()) || "P400091".equals(ex.getDefineCode());
			data.put("bizStatus", smsValidateFail ? SMS_FAILD : AJAX_FAILED);
			data.put("errorcode", ex.getDefineCode());
			data.put("errormsg", ex.getMessage());
			if (!smsValidateFail) {
				data.put("needRepay", true);
			}
		}
		ajaxResultWrite(response, data);
	}
	

	/**
	 * 绑卡支付-确认支付 TODO 这个接口是不是可以下线了
	 *
     * @param bindId
     * @param token
     * @param verifycode
     * @param needBankCardDTO
     * @param reqSmsSendTypeEnum
     * @param request
     * @return
     */
	@RequestMapping(value = "/pay/bind", method = {RequestMethod.POST})
	public Object bindPay(String bindId, String token, String verifycode,
			NeedBankCardDTO needBankCardDTO, ReqSmsSendTypeEnum reqSmsSendTypeEnum,
			HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_bindPay_request,token:{},bindId:{}", token, bindId);
		RequestInfoDTO info = null;
		try {
			if (StringUtils.isBlank(token) || StringUtils.isBlank(bindId)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
		} catch (Throwable e) {
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
		
		try {
			Base64Util.decryptNeedBankCardDTO(needBankCardDTO);
			newWapPayService.bindPay(info, token, bindId, needBankCardDTO, verifycode);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_bindPay_bussinessException,token:"+token+",bindId:"+bindId+"，异常：", e);
			ModelAndView mv = (ModelAndView) requestBind(token, bindId, request);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			String errorcode = StringUtils.isEmpty(e.getDefineCode()) ? "" : "(" + e.getDefineCode() + ")";
			mv.addObject("errormsg", ex.getMessage() + errorcode);
			return mv;
		} catch (Throwable e1) {
			logger.error("[monitor],event:nccashier_bindPay_exception,token:"+token+"bindId"+bindId, e1);
			CashierBusinessException ex = ExceptionUtil.handleException(e1, SYSTEM_CODE);
			ModelAndView mv = (ModelAndView) requestBind(token, bindId, request);
			mv.addObject("errormsg", ex.getMessage() + ex.getDefineCode());
			return mv;
		}

		RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
		ModelMap map = new ModelMap();
		rv.setAttributesMap(map);
		return rv;
	}

	/**
	 * 获取支持银行列表AJAX
	 *
	 * @param token
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajax/bankList", method = {RequestMethod.POST})
	public void showBanksList(String token, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.info("[monitor],event:nccashier_showBanksList_request,token:{}", token);
		PrintWriter out = response.getWriter();
		try {
			RequestInfoDTO info = newWapPayService.validateRequestInfoDTO(token);
			List<BankSupportDTO> bankSupports =
					ncCashierService.supportBankList(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_PREAUTH);

			List<BankInfoVO> debitBankList = new ArrayList<BankInfoVO>();
			List<BankInfoVO> creditBankList = new ArrayList<BankInfoVO>();
			if (CollectionUtils.isNotEmpty(bankSupports)) {
				for (BankSupportDTO b : bankSupports) {
					BankInfoVO bi = new BankInfoVO();
					bi.setBankCode(b.getBankCode());
					bi.setBankName(b.getBankName());
					bi.setCardType(b.getBanktype().name());
					if (CardTypeEnum.CREDIT == b.getBanktype()) {
						creditBankList.add(bi);
					} else {
						debitBankList.add(bi);
					}
				}
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("debitBankList", debitBankList);
			result.put("creditBankList", creditBankList);
			result.put("bizStatus", AJAX_SUCCESS);
			out.println(JSON.toJSONString(result));
			out.flush();
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_showBanksList_exception，token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ajaxErrorReturn(out, AJAX_FAILED, ex.getDefineCode(), ex.getMessage());
		} finally {
			out.close();
		}
	}


	/**
	 * 查询支付结果
	 *
     * @param token
     * @param isRequery
     * @param request
     * @return
     * @throws IOException
     */
	@RequestMapping(value = "/query/result", method = {RequestMethod.POST, RequestMethod.GET})
	public Object queryResult(String token, String isRequery, HttpServletRequest request)
			throws IOException {
		ModelAndView mv = new ModelAndView();
		mv.addObject("token", token);
		mv.setViewName("query_index");
		try {
			RequestInfoDTO info = newWapPayService.validateRequestInfoDTO(token);
			//定制不展示易宝公司信息
			if(CommonUtil.notShowYeepayCompanyInfo(info.getMerchantNo(), info.getOrderSysNo())){
				mv.addObject(CommonUtil.NOT_SHOW_YEEPAY_COMPANY_INFO_FLAG, true);
			}
			logger.info("[monitor],event:nccashierwap_queryResult_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(), info.getMerchantNo());
			//如果是sdk收银台，则跳转到一个新的页面
			if(info.getCashierVersionEnum() == CashierVersionEnum.SDK){
				mv.setViewName("sdk_result");
				mv.addObject("paymentRequestId",info.getPaymentRequestId());
				mv.addObject("paymentRecordId",info.getPaymentRecordId());
				return mv;
			}
			supplyBaseModelViewInfo(info, token, mv);
			TradeNoticeDTO tradeNoticeDTO = newWapPayService.queryResult(info, isRequery);
			if (tradeNoticeDTO != null) {
				Object finalStateMV = getQueryView(tradeNoticeDTO, token, mv,info);
				if (finalStateMV != null) {
					if(finalStateMV instanceof String){
						return new RedirectView((String) finalStateMV);
					}
					return finalStateMV;
				}
			}
			return mv;
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_queryResult_BusinException,token:" + token, e);
			return mv;
		} catch (Exception e) {
			logger.error("[monitor],event:nccashier_queryResult_SystemException,token:" + token, e);
			return mv;
		}
	}
	
	@RequestMapping(value = "/ajax/setCardOwner", method = {RequestMethod.POST})
	@ResponseBody
	public void setCardOwnerInfo(HttpServletRequest request, String bindId,
			String name, String idCardNo, String token,
			HttpServletResponse response){
		String token1 = request.getParameter("token");
		try {
			name = Base64Util.decode(name);
			idCardNo = Base64Util.decode(idCardNo);
			logger.info("[monitor],event:nccashier_setCardOwnerInfo_request,token={},bindId:{},idCardNo:{},name:{}", token, token1,idCardNo,name);
			RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
			if (StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(idCardNo)) {
				newWapPayService.setCardOwner(bindId, name, idCardNo, info);
			}
			if (StringUtils.isNotBlank(bindId)) {
				newWapPayService.setCardOwner(bindId, null, null, info);
			}
		} catch (Throwable e) {
			logger.error("设置同人限制信息异常，入参token = "+token+",bindId = "+token1+" ,异常为 ", e);
		}

	}

	private Object getQueryView(TradeNoticeDTO tradeNoticeDTO, String token,
			ModelAndView sourceMV,RequestInfoDTO info) throws Exception {
		sourceMV.addObject("payTool", tradeNoticeDTO.getPayTool()); // 支付工具
		if (tradeNoticeDTO.getTradeState() == TradeStateEnum.SUCCESS) {
			//收款宝部分商户的支付成功页跳转至他们自己的成功页面 modified by yangmin.peng 
			List<String> orderSysNolist = CommonUtil.getOrderSysNoList();
			//商编跳转成功页面
			if (orderSysNolist.contains(tradeNoticeDTO.getMerchantNo()) && StringUtils.isNotBlank(tradeNoticeDTO.getFrontCallBackUrl())) {
				return tradeNoticeDTO.getFrontCallBackUrl();
			}

			//业务方跳转成功页面
			if(orderSysNolist.contains(tradeNoticeDTO.getOrderSysNo()) && StringUtils.isNotBlank(tradeNoticeDTO.getFrontCallBackUrl())){
				return tradeNoticeDTO.getFrontCallBackUrl();
			}

			CardOwnerConfirmResDTO cardOwnerConfirmResDTO = newWapPayService.getOwnersInfo(info);
			sourceMV.addObject("cardOwnerConfirmResDTO",JSON.toJSONString(cardOwnerConfirmResDTO));
			//查询参加嘉年华的资格 add by xueping.ni 2016-11-08;update  jimin.zhou	  20171024
			tradeNoticeDTO.setTradeSysNo(info.getTradeSysNo());
			CarnivalVO canrivalVo = newWapPayService.queryQualification4Carnival(tradeNoticeDTO);
			logger.info("[JNH_嘉年华易宝大促]WapPayAction,token={},merNo:{},CarnivalVO:{}",
					token, tradeNoticeDTO.getMerchantNo(), JSONUtils.toJsonString(canrivalVo));
			if(canrivalVo.isShowCarnival()){
				sourceMV.addObject("showCarnival",canrivalVo.isShowCarnival());
				sourceMV.addObject("carnivalUrl",canrivalVo.getCarnivalUrl());
			}

			SuccessActivateVO successActivateVO = newWapPayService.querySuccessActivate(tradeNoticeDTO);
			logger.info("[支付成功活动]WapPayAction,token={},merNo:{},successActivateVO:{}",
					token, tradeNoticeDTO.getMerchantNo(), JSONUtils.toJsonString(successActivateVO));
			if (successActivateVO.isShowSuccessActivate()){
				sourceMV.addObject("showSuccessActivate",successActivateVO.isShowSuccessActivate());
				sourceMV.addObject("successActUrl",successActivateVO.getSuccessActUrl());
			}

			BigDecimal orderAmount = tradeNoticeDTO.getPaymentAmount();
			sourceMV.addObject("merchantUrl", tradeNoticeDTO.getFrontCallBackUrl());
			sourceMV.addObject("amount", orderAmount);
			sourceMV.addObject("notifyData", AESUtil.aesEncrypt(JSON.toJSONString(tradeNoticeDTO)));
			sourceMV.setViewName("pay_success");
			sourceMV.addObject("actualAmount", tradeNoticeDTO.getActualAmount());
			boolean showAd = CommonUtil.merchantShowAdWhenH5PaySuccess(info == null ? "" : info.getMerchantNo());
			if (showAd) {
				sourceMV.addObject("showAdvtisement", true);
			}
			// 银行卡分期所需信息
			String period = tradeNoticeDTO.getPeriod();
			sourceMV.addObject("bankCode", tradeNoticeDTO.getBankCode()); //银行编码
			sourceMV.addObject("period", period); // 期数
			// 担保分期所需信息
			String serviceChargeRate = tradeNoticeDTO.getServiceChargeRate();
			if(StringUtils.isNotBlank(serviceChargeRate) && StringUtils.isNotBlank(period)){
				BigDecimal realAmount = orderAmount.multiply(new BigDecimal(serviceChargeRate)).add(orderAmount);
				BigDecimal amountPerPeriod = realAmount.divide(new BigDecimal(period),2, RoundingMode.DOWN);
				sourceMV.addObject("realAmount", realAmount.setScale(2, RoundingMode.DOWN)); // 实际支付金额(订单金额+手续费)
				sourceMV.addObject("amountPerPeriod", amountPerPeriod); // 每期应还(实际金额/期数)
			}
			return sourceMV;
		} else if (tradeNoticeDTO.getTradeState() == TradeStateEnum.FAILED) {
			String repayUrl = null;
			try {//有补充项的绑卡支付，报身份信息错误。解绑该卡 
				Map<String,String> errorsInfo = newWapPayService.getPayFailErrors();
				if(null!=errorsInfo&&errorsInfo.containsKey(tradeNoticeDTO.getErrorCode())){
					newWapPayService.unbindCard(info.getPaymentRequestId(),info.getPaymentRecordId());
				}
				repayUrl = ncCashierService.getURL(tradeNoticeDTO.getCashierVersion(),
						tradeNoticeDTO.getMerchantNo(), tradeNoticeDTO.getPaymentRequestId());
				repayUrl = repayUrl + "?token=" + token;
			} catch (Exception e) {
				logger.error("[monitor],event:nccashier_queryResult_getURL_exception,token:{}",
						token, e);
				throw e;
			}
			ErrorCodeDTO ex = ExceptionUtil.handleException(tradeNoticeDTO.getErrorCode(), tradeNoticeDTO.getErrorMsg(), SYSTEM_CODE);
			return (ModelAndView) createRepayErrorRV(token, ex.getExternalErrorCode(), ex.getExternalErrorMsg(), repayUrl, sourceMV);
		} else if (tradeNoticeDTO.getTradeState() == TradeStateEnum.CANCEL) {
			sourceMV.addObject("merchantUrl", tradeNoticeDTO.getFrontCallBackUrl());
			sourceMV.setViewName("pay_cancel");
			return sourceMV;
		}

		return null;
	}

	private ModelAndView createCardInfoErrorMV(BusinessTypeEnum businessType, RequestInfoDTO info,
			String token, HttpServletRequest request) {
		ModelAndView mv = null;
		if (businessType == BusinessTypeEnum.FIRSTPASSCARDNO) {
			CashierBusinessException ex = ExceptionUtil.handleException(Errors.INVALID_BANK_CARD_NO, SYSTEM_CODE);
			mv = createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		} else if (businessType == BusinessTypeEnum.FIRSTPAY
				|| businessType == BusinessTypeEnum.FIRSTPASS) {
			mv = (ModelAndView) requestFirst(token, request);
		} else {
			mv = (ModelAndView) requestBind(token, null, request);
		}
		return mv;
	}

	private void ajaxSmsReturn(PrintWriter out, CardInfoDTO cardInfoDTO, String status,
			ReqSmsSendTypeEnum reqSmsSendTypeEnum, String errorcode, String errormsg) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("status", status);
			result.put("reqSmsSendTypeEnum", reqSmsSendTypeEnum);
			if(cardInfoDTO != null){
				result.put("phoneNo", cardInfoDTO.getPhone());
			}
			result.put("error", errorcode);
			errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
			result.put("msg", errormsg + errorcode);
			String json = JSON.toJSONString(result);
			out.println(json);
			out.flush();
		} finally {
			out.close();
		}
	}

	private Object wapRouter(String requestId, String encryptRequestId, String merchantNo,
			boolean fromNewWAP, String token, PayExtendInfo extendInfo,String directType,HttpServletRequest request,String ua) throws Exception {
		// PC ,扫码支付 - 走移动收银台
		if (!fromNewWAP && extendInfo.isInstallment() && DirectPayType.CFL.name().equals(directType)) {
			return new RedirectView(CommonUtil.getPreUrl(merchantNo,request)+sccanpay + "/request?token=" + token
					+ "&requestId=" + encryptRequestId + "&merchantNo="
					+ merchantNo + "&directType=" + DirectPayType.CFL);
		} 
		else if(!fromNewWAP && extendInfo.isSccanPay() && DirectPayType.ALIPAY.name().equals(directType)){
			return new RedirectView(CommonUtil.getPreUrl(merchantNo,request)+sccanpay + "/request?token=" + token
					+ "&requestId=" + encryptRequestId + "&merchantNo="
					+ merchantNo + "&directType=" + DirectPayType.ALIPAY);
		}
		else if(!fromNewWAP && extendInfo.isSccanPay()){
			StringBuilder redirectUrl = new StringBuilder(CommonUtil.getPreUrl(merchantNo,request));
			redirectUrl.append(sccanpay).append("/request?token=").append(token).append("&requestId=").append(encryptRequestId).append("&merchantNo=").append(merchantNo);
			return new RedirectView(redirectUrl.toString());
		}
		else if ((StringUtils.isNotEmpty(extendInfo.getPayTypes()))
				&& (extendInfo.containsPayType(PayTypeEnum.WECHAT_OPENID)
						|| extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP)
						|| extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_LOW)
						|| extendInfo.containsPayType(PayTypeEnum.BANK_PAY_WAP)
						|| extendInfo.containsPayType(PayTypeEnum.ALIPAY)
						|| extendInfo.containsPayType(PayTypeEnum.ALIPAY_H5)
						|| extendInfo.containsPayType(PayTypeEnum.ALIPAY_H5_STANDARD)
						|| extendInfo.containsPayType(PayTypeEnum.CFL)
						|| extendInfo.containsPayType(PayTypeEnum.ZF_ZHZF)
						|| extendInfo.containsPayType(PayTypeEnum.JD_H5)
						|| extendInfo.containsPayType(PayTypeEnum.ZFB_SHH)
						|| extendInfo.containsPayType(PayTypeEnum.BK_ZF)
						|| extendInfo.containsPayType(PayTypeEnum.YHKFQ_ZF)
						|| extendInfo.containsPayType(PayTypeEnum.YSQ)
						|| extendInfo.containsPayType(PayTypeEnum.DBFQ_TL))
				&& !fromNewWAP) {
			// 移动收银台
			logger.info("[monitor],event:nccashier_router,requestId:{},merchantNo:{},payTypes:{},directPayType:{}",
					requestId, merchantNo, extendInfo.getPayTypes(), extendInfo.getDirectPayType());
			//如果只开通预授权，且未传直连参数，则报错
			String onlyYsqPayType = "["+PayTypeEnum.YSQ.value()+"]";
			if(onlyYsqPayType.equals(extendInfo.getPayTypes()) && 
					(StringUtils.isEmpty(extendInfo.getDirectPayType()) || DirectPayType.NONE.name().equals(extendInfo.getDirectPayType()))){
				throw new CashierBusinessException(Errors.NOT_OPEN_PRODUCT_ERROR);
			}
			int wechatH5Preference = WaChatPayUtils.checkWechatH5Preference(merchantNo,extendInfo);
			if (StringUtils.isEmpty(extendInfo.getDirectPayType()) || DirectPayType.NONE.name().equals(extendInfo.getDirectPayType())) {
				// 移动收银台非直连
				StringBuilder redirectUrl = new StringBuilder(CommonUtil.getPreUrl(merchantNo,request));
				redirectUrl.append(newwapctx).append("/request?token=").append(token).append("&requestId=").append(encryptRequestId).append("&merchantNo=").append(merchantNo);
				return new RedirectView(redirectUrl.toString());
			} else if (extendInfo.containsPayType(PayTypeEnum.BANK_PAY_WAP) && DirectPayType.YJZF.name().equals(extendInfo.getDirectPayType())) {
				// 直连一键支付
				// 监控日志埋点
				logger.info("[monitor],event:nccashierwap_payMethod_request,requestId:{},merchantNo:{}", requestId,merchantNo);
				return toYJZFWap(Long.valueOf(requestId), token,merchantNo,request);
			} else if (extendInfo.containsPayType(PayTypeEnum.YSQ) && DirectPayType.YSQ.name().equals(extendInfo.getDirectPayType())){
				// 直连预授权
				return new RedirectView(CommonUtil.getPreUrl(merchantNo,request)+wapctx + "/preauth/routPayway?token=" + token);
			} else if (WaChatPayUtils.WECHAT_H5_PREFER_HIGH == wechatH5Preference && DirectPayType.WECHAT.name().equals(extendInfo.getDirectPayType())) {
				// 直连微信H5标配版
				String url = newWapPayService.directPay(token, PayTypeEnum.WECHAT_H5_WAP.name());
				if(CommonUtil.checkDirectWechartH5ToBlankPage(merchantNo)){
					return url;
				}else{
					return new RedirectView(url);
				}
			} else if (WaChatPayUtils.WECHAT_H5_PREFER_LOW == wechatH5Preference && DirectPayType.WECHAT.name().equals(extendInfo.getDirectPayType())) {
				// 直连微信H5低配版
				if(Constant.PRE_ROUTE_SCENE_TYPE_EXT_JSAPIH5.equals(extendInfo.getSceneTypeExt()) && ua.contains("MicroMessenger")){
					//jsapi通道的，选择重新支付后，且确认当前已经在微信环境，则不需要再次使用传送门
					String url = newWapPayService.directPay(token, PayTypeEnum.WECHAT_H5_LOW.name());
					return new RedirectView(url);
				}
				//从std接口第一次重定向到此处的，微信h5低配版直连。
				RequestInfoDTO info = checkRequestInfoDTO(token);
				String targetUrl = newWapPayService.wechatH5PreRouter(extendInfo, info, token, request);
				
				String userIp = getUserIp(request);
				String phone = WaChatPayUtils.getPhone(ua);
				String lockKey= userIp+"_"+phone;
				String jumpUrl = WaChatPayUtils.buildWechatH5JumpUrl(targetUrl,lockKey,requestId);
				if (StringUtils.isBlank(jumpUrl)) {
					logger.error("weChatPayRequest() 直连微信h5低配版，预路由失败,token={},商编={}，获取传送门地址失败，请检查预路由结果或传送门API接口", token, info.getMerchantNo());
					throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
				}else if ("LOCK".equals(jumpUrl)){
					throw new CashierBusinessException(Errors.NOT_GET_LOCK);
				}
				WaChatPayUtils.recordEwalletH5PackingSiginal(token, PayTypeEnum.WECHAT_H5_LOW.name());
				return new RedirectView(jumpUrl);
			}
			else if(DirectPayType.ALIPAY.name().equals(extendInfo.getDirectPayType())){
				// 走到这些else if的说明directPayType一定不为空
				String alipayVersion = extendInfo.toDecideAlipayVersion();
				if(PayTypeEnum.ALIPAY.name().equals(alipayVersion) || PayTypeEnum.ALIPAY_H5.name().equals(alipayVersion)){
					String url = newWapPayService.directPay(token, alipayVersion); // 直连支付宝或支付宝H5
					return new RedirectView(url);
				}else if(PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(alipayVersion)){
					return driectAlipayStandard(request, requestId, token, merchantNo);
				}else{
					throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
				}
			}else if (extendInfo.containsCflPayTypes() && DirectPayType.CFL.name().equals(extendInfo.getDirectPayType())) {
				// 直连分期付款
				String url = newWapPayService.directPay(token, PayTypeEnum.CFL.name());
				return new RedirectView(url);
			} else if (extendInfo.containsPayType(PayTypeEnum.ZF_ZHZF) && DirectPayType.ZHZF.name().equals(extendInfo.getDirectPayType())) {
				// 直连账户支付
				return accountPayIndex(token, request);
			} else if (extendInfo.containsPayType(PayTypeEnum.JD_H5) && DirectPayType.JD.name().equals(extendInfo.getDirectPayType())) {
				// 直连京东支付
				String url = newWapPayService.directPay(token, PayTypeEnum.JD_H5.name());
				return new RedirectView(url);
			} else if(extendInfo.containsPayType(PayTypeEnum.DBFQ_TL) && DirectPayType.DBFQ.name().equals(extendInfo.getDirectPayType())){
				// 直连担保分期
				StringBuilder redirectUrl = new StringBuilder(CommonUtil.getPreUrl(merchantNo,request))
						.append("/guarantee/wap/index?token=").append(token);
				return new RedirectView(redirectUrl.toString());
			}
			else {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
		} else {
			// 监控日志埋点
			logger.info("[monitor],event:nccashierwap_payMethod_request,requestId:{},merchantNo:{}", requestId,merchantNo);
			return toYJZFWap(Long.valueOf(requestId), token,merchantNo,request);
		}
	}
	
	
	/**
	 * 支付宝标准版直连
	 * 
	 * @param request
	 * @param requestId
	 * @param token
	 * @param merchantNo
	 * @return
	 * @throws Exception
	 */
	private Object driectAlipayStandard(HttpServletRequest request, String requestId, String token, String merchantNo)
			throws Exception {
		BrowserType browserType = RequestUtils.getBrowserType(request);
		if (BrowserType.WECHAT == browserType) {
			logger.error("wapRoute 直连支付宝标准版要求在非微信浏览器环境内");
			throw new CashierBusinessException(Errors.BROWSER_NOT_SUPPORT); // 浏览器不支持
		} else if (BrowserType.ALIPAY == browserType) {
			// 直连模式下，当检测到当前浏览器是支付宝时，无需传送，直接去做授权，获取userId
			String alipayAuth2Url = newWapPayService.alipayLifeNoPreRouteIgnoreOriUserId(requestId, token);
			return new RedirectView(alipayAuth2Url);
		} else {
			// 直连标准版 预路由、获取授权key
			AlipayStandardJumpInfo jumpInfo = newWapPayService.alipayStandardPreHandleBeforeJumping(requestId, token,
					merchantNo);
			ModelAndView view = new ModelAndView();
			view.addObject("jumpInfo", JSON.toJSONString(jumpInfo));
			view.setViewName("alipayEvokeBlank");
			return view;
		}
	}
	
	/**
	 * 前置绑卡下单
     *
     * @param token
     * @param bindId
     * @param response
     * @throws IOException
     */
	@RequestMapping(value = "/request/new/bind", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public void requestNewBind(String token, String bindId,  HttpServletResponse response) throws IOException {
		logger.info("[monitor],event:nccashier_requestBind_request,token:{},bindId:{}", token, bindId);
		RequestInfoDTO info = null;
		PrintWriter out = response.getWriter();
		try {
			if (StringUtils.isBlank(bindId)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = checkRequestInfoDTO(token);
			Map<String, Object> model = newWapPayService.createPayment(token, bindId,info, null);

			PageRedirectDTO pageRedirectDTO = (PageRedirectDTO) model.get("pageRedirectParam");
			String smsType = (String)model.get("smsType");
			NeedBankCardDTO needBankCardDTO = (NeedBankCardDTO)model.get("needBankCardDTO");
			boolean needSupport = false;
			if(needBankCardDTO != null && needBankCardDTO.getNeedSurportDTO() != null){
				NeedSurportDTO needSupportDTO = needBankCardDTO.getNeedSurportDTO();
				needSupport = needSupportDTO.getAvlidDateIsNeed() || needSupportDTO.getBankPWDIsNeed()
						|| needSupportDTO.getCvvIsNeed() || needSupportDTO.getIdnoIsNeed()
						|| needSupportDTO.getOwnerIsNeed() || needSupportDTO.getPhoneNoIsNeed()
						|| needSupportDTO.getYpMobileIsNeed();
			}
			String next = "";
			if (null != pageRedirectDTO) {
				next = "redirect";
			}else {
				if (needSupport) {
					next = "support";
				} else if (StringUtils.isNotBlank(smsType)) {
					newWapPayService.validateBindCardAndTimeout(needBankCardDTO, token, bindId);
					newWapPayService.bindSendSMS(ReqSmsSendTypeEnum.valueOf(smsType), new NeedBankCardDTO(), token, bindId);
					next = "sms";
				} else {
					next = "pay";
				}
			}

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("status", AJAX_SUCCESS);
			result.put("reqSmsSendTypeEnum", smsType);
			result.put("next", next);
			result.put("pageRedirectParam",pageRedirectDTO);
			ajaxResultWrite(response,result);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestBind_BusinException,token:" + token
					+ ",bindId:" + bindId, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ajaxSmsReturn(out, null, AJAX_FAILED, null, ex.getDefineCode(), ex.getMessage());
		} catch (Throwable e) {
			logger.warn("[monitor],event:nccashier_smsSend_sendSms_bussinessException，token:" + token,
					e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ajaxSmsReturn(out, null, AJAX_FAILED, null, ex.getDefineCode(), ex.getMessage());
		}
		
		
	}
	
	/**
	 * 获取补充项信息
	 * @param token
	 * @param bindId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/request/needItem", method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestNeedItem(String token, String bindId, HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_requestBind_request,token:{},bindId:{}", token, bindId);
		RequestInfoDTO info = null;
		try {
			if (StringUtils.isBlank(bindId)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = checkRequestInfoDTO(token);
			ModelAndView view = new ModelAndView();
			view.addObject("bindId", bindId);
			/**
			 * 此处复用下单的接口，实际上不会再下单，只会获取补充项信息
			 */
			Map<String, Object> model = newWapPayService.createPayment( token, bindId,info, null);
			for (String key : model.keySet()) {
				
				if("smsType".equals("key") && model.get("smsType") == null){
					view.addObject("smsType", "null");
				}else{
					view.addObject(key, model.get(key));
				}
				
			}
			view.setViewName("bind_item");
			//定制不展示易宝支付协议
			if(CommonUtil.notShowYeepayAgreementInfo(info.getMerchantNo(), info.getOrderSysNo())){
				view.addObject(CommonUtil.NOT_SHOW_YEEPAY_AGREEMENT_INFO_FLAG, true);
			}
			//定制不展示易宝公司信息
			if(CommonUtil.notShowYeepayCompanyInfo(info.getMerchantNo(), info.getOrderSysNo())){
				view.addObject(CommonUtil.NOT_SHOW_YEEPAY_COMPANY_INFO_FLAG, true);
			}
			if(StringUtils.isNotBlank(info.getPayTools()) && info.getPayTools().contains(PayTool.BK_ZF.name())){
				view.addObject("bindCardPay", true);
			}else{
				view.addObject("bankPay", true);
			}
			supplyBaseModelViewInfo(info, token, view);
			return view;
			
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestNeedItem_BusinException,token:" + token
					+ ",bindId:" + bindId, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			}else{
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestNeedItem_SystemException,token:" + token
					+ ",bindId:" + bindId, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token,ex.getDefineCode(), ex.getMessage(), info);
		}
	}
	
	/**
	 * 前置绑卡-换卡支付请求,走首次输卡号或者透传卡号
     *
     * @param token
     * @param request
     * @return
     */
	@RequestMapping(value = "/request/changecard", method = {RequestMethod.POST, RequestMethod.GET})
	public Object changeCardRequest(String token, HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_changeCardRequest_request,token:{}", token);
		RequestInfoDTO info = null;
		try {
			info = checkRequestInfoDTO(token);
			// 监控日志埋点
			logger.info("[monitor],event:nccashierwap_payMethod_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());
			BussinessTypeResponseDTO bussinessTypeResponseDTO = ncCashierService.routerPayWay(info.getPaymentRequestId());
			if (BusinessTypeEnum.FIRSTPAY == bussinessTypeResponseDTO.getBusinessTypeEnum()
					|| BusinessTypeEnum.FIRSTPASS == bussinessTypeResponseDTO.getBusinessTypeEnum()
					|| BusinessTypeEnum.BINDPASS == bussinessTypeResponseDTO.getBusinessTypeEnum()
					|| BusinessTypeEnum.BINDPAY == bussinessTypeResponseDTO.getBusinessTypeEnum()) {
				return requestFirst(token,request);
			} else {
				return cardInfo(null, token, "changCard",request);
			} 
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestNeedItem_BusinException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token,ex.getDefineCode(), ex.getMessage(), info);
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestNeedItem_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token,ex.getDefineCode(), ex.getMessage(), info);
		}
	}
	
	
	/**
	 * 会员支付页面入口
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/accountpay/index")
	public Object accountPayIndex(String token, HttpServletRequest request) {
		logger.info("[monitor],event:wap_accountpayindex_request,token:{}", token);
		RequestInfoDTO info = null;
		try {
			info = newWapPayService.validateRequestInfoDTO(token);
            PayExtendInfo payExtendInfo = ncCashierService.getPayExtendInfo(info.getPaymentRequestId(),token);
			if (!payExtendInfo.containsPayType(PayTypeEnum.ZF_ZHZF)) {
				throw new CashierBusinessException(Errors.NOT_OPEN_PRODUCT_ERROR);
			}
			accountPayService.checkExpireTime(payExtendInfo.getExpireTime());
			ModelAndView mv = new ModelAndView();
			mv.setViewName("account_pay");
			mv.addObject("token", token);
			setOrderInfo(info, mv);
			return mv;
		} catch (Throwable t) {
			return handleWapException(t, "wap_accountPayIndex_exception", token, info, request);
		}
	}

	/**
	 * wap账户支付下单接口
	 * 
	 * @param param
	 * @param bindingResult
	 * @param httpResponse
	 */
	@RequestMapping(value = "/accountpay/pay")
	public Object accountPay(@Valid AccountPayRequestVO param, BindingResult bindingResult,
			HttpServletResponse httpResponse, HttpServletRequest request) {
		AccountPayResponseVO responseVO = null;
		RequestInfoDTO info = null;
		String token = (param == null ? "" : param.getToken());
		try {
			Base64Util.decryptAccountPayRequestVO(param);
			logger.info("[monitor],event:wap_accountpay_request,param:{}", param);
			info = accountPayService.validateAccountPayParam(param, bindingResult);
			responseVO = accountPayService.accountPay(param, info);
			if (responseVO != null && Constant.FAIL.equals(responseVO.getBizStatus())) {
				throw new CashierBusinessException(responseVO.getErrorcode(), responseVO.getErrormsg());
			}
		} catch (Throwable t) {
			logger.error("[monitor],event:wap_accountpay_error,param:" + param, t);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SYSTEM_CODE);
			if (Errors.THRANS_EXP_DATE.getCode().equals(ex)) {
				return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
			} else if (!(Errors.THRANS_FINISHED.getCode().equals(ex.getDefineCode())) && !("P9002001".equals(ex.getDefineCode()))) {
				ModelAndView curMv = (ModelAndView) accountPayIndex(token, request);
				
				if(responseVO!=null){
					curMv.addObject("retryTimes", responseVO.getRetryTimes());
					curMv.addObject("bizStatus", responseVO.getBizStatus());
					curMv.addObject("frozenTime", responseVO.getFrozenTime());
				}
				curMv.addObject("errorCode", ex.getDefineCode());
				curMv.addObject("errorMsg", ex.getMessage());
				return curMv;
			}
		}
		RedirectView rv = new RedirectView(
				CommonUtil.getPreUrl(info.getMerchantNo(), request) + wapctx + "/query/result?token=" + token);
		ModelMap map = new ModelMap();
		rv.setAttributesMap(map);
		return rv;
	}
	/**
	 * 重新支付跳转H5收银台
	 */
	@RequestMapping(value = "/wapRePay")
	public Object wapRePay(String token, HttpServletRequest request) {
		RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
		String encryptRequestId = "";
		try {
			encryptRequestId = AESUtil.routeEncrypt(info.getMerchantNo(), String.valueOf(info.getPaymentRequestId()));
		}catch (Exception e){
			logger.error("编码requestId失败,requestId:"+info.getPaymentRequestId());
		}
		StringBuilder redirectUrl = new StringBuilder(CommonUtil.getPreUrl(info.getMerchantNo(),request));
		redirectUrl.append(wapctx)
		  .append("/request/")
		  .append(info.getMerchantNo())
		  .append("/")
		  .append(encryptRequestId)
		  .append("?token=").append(token);
		return new RedirectView(redirectUrl.toString());
	}

	/**
	 * 重定向商户回调地址
	 * @return
	 */
	@RequestMapping(value = "/redirectMerchantCallBack/{requestId}")
	public Object redirectMerchantCallBack(@PathVariable("requestId") String requestId){
		logger.info("[monitor],event:redirectMerchantCallBack,requestId:{}", requestId);
		String urlPrefix =CommonUtil.getCashierUrlDefaultPrefix();
		String staticSuccessPage = urlPrefix + "/static/api_success.html";
		try {
			if(!StringUtils.isNumeric(requestId)){
				return new RedirectView(staticSuccessPage);
			}
			RequestInfoDTO info = new RequestInfoDTO();
			info.setPaymentRequestId(Long.parseLong(requestId));
			String merchantPageCallBack = ncCashierService.getMerchantPageCallBack(info);
			if(StringUtils.isNotEmpty(merchantPageCallBack)){
				return new RedirectView(merchantPageCallBack);
			}else {
				return new RedirectView(staticSuccessPage);
			}
		}catch (Exception e) {
			logger.warn("redirectMerchantCallBack() 重定向商户回调地址，获取商户地址异常，requestId=" + requestId + ",e=", e);
			return new RedirectView(staticSuccessPage);
		}
	}
}
