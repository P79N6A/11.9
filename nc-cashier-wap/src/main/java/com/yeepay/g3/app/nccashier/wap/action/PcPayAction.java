package com.yeepay.g3.app.nccashier.wap.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yeepay.g3.app.nccashier.wap.utils.*;
import com.yeepay.g3.app.nccashier.wap.vo.CarnivalVO;
import com.yeepay.g3.utils.common.json.JSONUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.BankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.BankCardReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.BankSupportDTO;
import com.yeepay.g3.facade.nccashier.dto.BussinessTypeResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CardOwnerConfirmResDTO;
import com.yeepay.g3.facade.nccashier.dto.ErrorCodeDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.PassCardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.TradeNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessDTO;
import com.yeepay.g3.facade.nccashier.enumtype.BusinessTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.TradeStateEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 网页版收银台
 * Created by liyong on 16-05-24
 */
@Controller
@RequestMapping(value = "/pc", method = { RequestMethod.POST, RequestMethod.GET })
public class PcPayAction {
	
	private static final Logger logger = LoggerFactory.getLogger(PcPayAction.class);
	
	/**
	 * 本controller对应的系统编码
	 */
	private static final SysCodeEnum SYSTEM_CODE = SysCodeEnum.NCCASHIER_WAP;
	
	@Resource
	private NcCashierService ncCashierService;
	
	@Resource
	private NewWapPayService newWapPayService;
	
	public static final String ctx = "/pc";
	private static final String AJAX_SUCCESS = "success";
	private static final String AJAX_FAILED = "failed";
	private static final String POP_ERROR ="popup";

	/**
	 * 收到请求后处理验证
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/request/{merchantNo}/{requestId}", method = {
			RequestMethod.POST, RequestMethod.GET })
	public Object requestProcess(HttpServletRequest request,
			@PathVariable("requestId") String requestId,
			@PathVariable("merchantNo") String merchantNo,
			String token) {
		logger.info(
				"[monitor],event:nccashier_requestProcess_Request,requestId:{},merchantNo:{}",
				requestId, merchantNo);
		RedirectView view = null;
		RequestInfoDTO info =null;
		if(StringUtils.isEmpty(token)){
			token = UUID.randomUUID().toString();
		}
		try{
			//解密支付请求ID
			requestId = AESUtil.routeDecrypt(merchantNo, requestId);
			long requestID = Long.parseLong(requestId);
			//获取用户IP
			String ypip = RequestUtils.getUserIP(request);
			if (StringUtils.isEmpty(ypip)) {
				// 跳转到出错页面
				CashierBusinessException ex = ExceptionUtil.handleException(Errors.SYSTEM_EXCEPTION, SYSTEM_CODE);
				return createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), null);
			}
			// 获取用户终端UA
			String ua = RequestUtils.getUserUA(request);
			if (StringUtils.isEmpty(ua)) {
				// 跳转到出错页面
				CashierBusinessException ex = ExceptionUtil.handleException(Errors.SYSTEM_EXCEPTION, SYSTEM_CODE);
				return createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), null);
			}
			if (ua.length() > 300) {
				ua = ua.substring(0, 300);
			}
			
			UserAccessDTO userAccess = new UserAccessDTO();
			userAccess.setPaymentRequestId(requestId);
			userAccess.setUserIp(ypip);
			userAccess.setUserUa(ua);
			userAccess.setTokenId(token);
			userAccess.setMerchantNo(merchantNo);
			ncCashierService.saveUserAccess(userAccess);
			info = ncCashierService.requestBaseInfo(token);
			// 监控日志埋点
			logger.info("[monitor],event:nccashierwap_payMethod_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),merchantNo);
			BussinessTypeResponseDTO bussinessTypeResponseDTO = ncCashierService.routerPayWay(requestID);
				if (BusinessTypeEnum.FIRSTPAY == bussinessTypeResponseDTO.getBusinessTypeEnum()
						|| BusinessTypeEnum.FIRSTPASS == bussinessTypeResponseDTO
								.getBusinessTypeEnum()) {
					view = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+ctx + "/request/first?token=" + token);
				} else if (BusinessTypeEnum.FIRSTPASSCARDNO == bussinessTypeResponseDTO
						.getBusinessTypeEnum()) {
					view = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+ctx + "/first/cardinfo?token=" + token);
				} else {
					view = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+ctx + "/request/bind?token=" + token);
				}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestProcess_saveUserAccess_exception，requestId:{},merchantNo:{}",
					requestId, merchantNo, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
		
		return view;
	}

	@RequestMapping(value = "/request/first", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object requestFirst(String token, HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_requestFirst_request,token:{}",
				token);
		RequestInfoDTO info = null;
		try {
			info = ncCashierService.requestBaseInfo(token);
			ModelAndView mv = new ModelAndView();
			mv.setViewName("onlinebank/first_pay");
			supplyBaseModelViewInfo(info, token, mv);
			return mv;
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestFirst_BusinException,token:" + token, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+ctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			} else {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestFirst_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
	}


	@RequestMapping(value = "/request/bind", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object requestBind(String token, String bindId, HttpServletRequest request) {
		logger.info(
				"[monitor],event:nccashier_requestBind_request,token:{},bindId:{}",
				token, bindId);
		RequestInfoDTO info = null;
		List<BankCardDTO> bindCards = new ArrayList<BankCardDTO>();
		try {
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
			BankCardReponseDTO response = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
			BankCardDTO bindCardDTO = response.getBankCardDTO();
			PassCardInfoDTO passCardInfoDTO = response.getPassCardInfoDTO();
			if (bindCardDTO == null) {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
			}else {
				ModelAndView view = new ModelAndView();
				view.setViewName("onlinebank/bind_pay");
				if(null!=passCardInfoDTO&&StringUtils.isNotBlank(passCardInfoDTO.getCardNo())){
					view.addObject("bindTouchuanIsCardNo", true);
					bindCards.add(response.getBankCardDTO());
				}else{
					view.addObject("bindTouchuanIsCardNo", false);
					bindCards = bindCardDTO.getOtherCards();
				}
				view.addObject("showChangeCard",response.getShowChangeCard());
				view.addObject("bindCards", bindCards);
				supplyBaseModelViewInfo(info, token, view);
				return view;
			}
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestBind_BusinException,token:" + token
					+ ",bindId:" + bindId, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+ctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			}else{
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestBind_SystemException,token:" + token
					+ ",bindId:" + bindId, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
	}

	/**
	 * 校验卡号提交订单
	 *
	 * @param cardNo
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/first/cardinfo")
	public Object cardInfo(String cardNo, String token, HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_cardInfo_request,token:{}", token);
		RequestInfoDTO info = null;
		BusinessTypeEnum businessType = null;
		ModelAndView mv = null;
		Boolean firstTouchuanIsCardNo = null;//首次透传是否有卡号
		String isBindPay = request.getParameter("isBindPay");
		// 是否是绑卡支付的其他卡支付，如果是绑卡支付的其他卡支付，则在卡信息页点击其他卡支付时，跳转回绑卡支付页
		String isChangeCard = request.getParameter("isChangeCard");
		
		try {
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			cardNo = Base64Util.decode(cardNo);
			// 如果是换卡支付清空用户访问信息表中存在的上一个支付记录的recordid
			if ("changCard".equals(isChangeCard)) {
				ncCashierService.clearRecordId(token);
			}

			info = ncCashierService.requestBaseInfo(token);
			BankCardReponseDTO response = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);

			businessType = response.getBusinessTypeEnum();
			BankCardDTO bindCardDTO = response.getBankCardDTO();
			PassCardInfoDTO passCardInfoDTO = response.getPassCardInfoDTO();
			if (businessType == BusinessTypeEnum.FIRSTPASSCARDNO && passCardInfoDTO != null) {
				if(StringUtils.isNotBlank(passCardInfoDTO.getCardNo())){
					firstTouchuanIsCardNo = true;
					cardNo = passCardInfoDTO.getCardNo();
				}
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
				mv.addObject("showChangeCard",response.getShowChangeCard());
				mv.addObject("firstTouchuanIsCardNo",firstTouchuanIsCardNo);//首次透传是否有卡号
				supplyBaseModelViewInfo(info, token, mv);
				Boolean isBindPay1 = null;
				if("bindPay".equals(isBindPay)){
					isBindPay1 = true; 
				}
				mv.addObject("isBindPay",isBindPay1);
				Map<String, Object> model = newWapPayService.getFirstPayModel(
						info.getPaymentRequestId(), cardNo, businessType, bindCardDTO);
				if(null!= model.get("bc")){
					BankCardDTO bc= (BankCardDTO)model.get("bc");
					if(bc.getCardtype() == CardTypeEnum.DEBIT){
						mv.setViewName("onlinebank/debit_cardInfo");
					}else{
						mv.setViewName("onlinebank/credit_cardInfo");
					}
					mv.addObject("bc", bc);
				}
				for (String key : model.keySet()) {
					mv.addObject(key, model.get(key));
				}
			}
			return mv;
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_cardInfo_request_BusinException,token:" + token, e);
			mv = createCardInfoErrorMV(businessType, info, token, request);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			mv.addObject("errormsg", ex.getMessage());
			mv.addObject("cardNo", cardNo);

			return mv;
		} catch (Throwable e) {
			logger.error(
					"[monitor],event:nccashier_cardInfo_request_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
		
	}

	/**
	 *首次支付发送短验AJAX
	 *
	 * @param token
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajax/smsSend", method = { RequestMethod.POST })
	@ResponseBody
	public void smsSend(HttpServletRequest request, CardInfoDTO cardInfo,
			NeedBankCardDTO needBankCardDTO, String token, 
			ReqSmsSendTypeEnum reqSmsSendTypeEnum, HttpServletResponse response)
			throws IOException {
		PrintWriter out = response.getWriter();
		try {
			Base64Util.decryptCardInfoDTO(cardInfo);
			Base64Util.decryptNeedBankCardDTO(needBankCardDTO);
			logger.info("[monitor],event:nccashier_smsSend_request,needBankCardDTO:{},token:{},reqSmsSendTypeEnum:{}",needBankCardDTO, token,  reqSmsSendTypeEnum);
			newWapPayService.validateCardInfoAndTimeout(cardInfo, token, null);
			ReqSmsSendTypeEnum reqSmsSendTypeEnum1 = newWapPayService.createPaymentOrSendSMS(token,
					null, reqSmsSendTypeEnum, cardInfo);
			ajaxSmsReturn(out, cardInfo, AJAX_SUCCESS, reqSmsSendTypeEnum1, null, null);
		} catch (Throwable e) {
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
	 * 绑卡支付发送短验
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
			String bindId, ReqSmsSendTypeEnum reqSmsSendTypeEnum, NeedBankCardDTO needBankCardDTO,HttpServletResponse response)
					throws IOException {
		PrintWriter out = response.getWriter();
		try {
			Base64Util.decryptNeedBankCardDTO(needBankCardDTO);
			logger.info("[monitor],event:nccashier_bindSmsSend_request,token:{},needBankCardDTO:{},bindId:{}",token, needBankCardDTO, bindId);
			newWapPayService.validateBindCardAndTimeout(needBankCardDTO, token, bindId);
			newWapPayService.bindSendSMS(reqSmsSendTypeEnum,needBankCardDTO, token, bindId);
			ajaxSmsReturn(out, null, AJAX_SUCCESS, reqSmsSendTypeEnum, null, null);
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_bindSmsSend_sendSms_exception，token:" + token, e);
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
	 * 绑卡支付下单AJAX
	 *
	 * @param request
	 * @param token
	 * @param bankCardDTO
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajax/requestPayment", method = { RequestMethod.POST })
	@ResponseBody
	public void requestPayment(HttpServletRequest request, String token,
			String bindId, HttpServletResponse response) throws IOException {
		logger.info(
				"[monitor],event:nccashier_requestPayment_request,token:{},bindId:{}",
				token, bindId);
		RequestInfoDTO info = null;
		Map<String, Object> result = null;
		PrintWriter out = response.getWriter();
		try{
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
			BankCardReponseDTO bankCardResponse = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
			BankCardDTO bindCardDTO = bankCardResponse.getBankCardDTO();
			
			if (bindCardDTO == null) {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
			}else{
				result = newWapPayService.createPayment(token, bindId,info,bindCardDTO);
				ajaxPaymentReturn(out, result,  AJAX_SUCCESS, null, null);
			}
		}catch(Throwable e){
			logger.error("[monitor],event:nccashier_requestPayment_exception，token:{}", token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ajaxPaymentReturn(out, result, AJAX_FAILED, ex.getDefineCode(), ex.getMessage());
		}
	}

	/**
	 * 首次AJAX确认支付
	 *
	 * @param cardInfo
	 * @param token
	 * @param verifycode
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajax/firstpay", method = { RequestMethod.POST })
	@ResponseBody
	public void firstpay(CardInfoDTO cardInfo, String token, String verifycode,
			HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		RequestInfoDTO info = null;
		PrintWriter out = response.getWriter();
		try{
			Base64Util.decryptCardInfoDTO(cardInfo);
			logger.info("[monitor],event:nccashier_firstpay_request,token:{},cardInfo:{}",token, cardInfo);
			info = newWapPayService.validateRequestInfoDTO(token);
			newWapPayService.validateCardInfo(null, cardInfo);
			newWapPayService.firstPay(info, token, cardInfo, verifycode);
			ajaxConfirmPayReturn(out, null, token, AJAX_SUCCESS, null, null, null);
		}catch(CashierBusinessException e){
			String viewTarget = checkCodeError(e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ajaxConfirmPayReturn(out,null, token, AJAX_FAILED, ex.getDefineCode(), ex.getMessage(),viewTarget);
		}catch(Throwable e){
			logger.error("[monitor],event:nccashier_firstpay_exception，token:{},cardInfo:{}", token, cardInfo, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ajaxConfirmPayReturn(out, null, token, AJAX_FAILED, ex.getDefineCode(), ex.getMessage(), null);
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
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajax/bindPay", method = { RequestMethod.POST })
	@ResponseBody
	public void bindPay(String token, String verifycode, String bindId,
			NeedBankCardDTO needBankCardDTO, HttpServletResponse response,
			HttpServletRequest request) throws IOException {
		logger.info("[monitor],event:nccashier_bindPay_request,token:{},bindId:{}",token, bindId);
		RequestInfoDTO info = null;
		PrintWriter out = response.getWriter();
		try{
			if (StringUtils.isBlank(token)||StringUtils.isBlank(bindId)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
			Base64Util.decryptNeedBankCardDTO(needBankCardDTO);
			newWapPayService.bindPay(info, token, bindId, needBankCardDTO, verifycode);
			ajaxConfirmPayReturn(out, null, token, AJAX_SUCCESS, null, null, null);
		} catch (CashierBusinessException be) {
			logger.warn(
					"[monitor],event:nccashier_bindPay_bussinessException,token:{},bindId:{}",
					token, bindId, be);
			String viewTarget = checkCodeError(be);
			CashierBusinessException ex = ExceptionUtil.handleException(be, SYSTEM_CODE);
			ajaxConfirmPayReturn(out, null, token, AJAX_FAILED, ex.getDefineCode(), ex.getMessage(), viewTarget);
		} catch (Throwable te) {
			logger.error(
					"[monitor],event:nccashier_bindPay_exception,token:{},bindId:{}",
					token, bindId, te);
			CashierBusinessException ex = ExceptionUtil.handleException(te, SYSTEM_CODE);
			String errormsg = ex.getMessage() + ex.getDefineCode();
			ajaxConfirmPayReturn(out, null, token, AJAX_FAILED, ex.getDefineCode(), errormsg, null);
		}
	}
	/**
	 * 短验错误时停留在当前页
	 * @param ex
	 * @param viewTarget
	 */
	private String checkCodeError(CashierBusinessException ex) {
		String viewTarget =null;
		String errorCode1="N400091";
		String errorCode2="N400094";
		if(errorCode1.equals(ex.getDefineCode())){
			viewTarget = POP_ERROR;
		}
		if(errorCode2.equals(ex.getDefineCode())){
			viewTarget = POP_ERROR;
		}
		return viewTarget;
	}

	/**
	 * 校验输入的卡号是否在支持的卡表中AJAX
	 *
	 * @param token
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajax/check", method = { RequestMethod.POST })
	public void checkCardNo(String token, String cardno,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		RequestInfoDTO info = null;
		PrintWriter out = response.getWriter();
		logger.info("[monitor],event:nccashier_showBanksList_request,token:{}",token);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			cardno = Base64Util.decode(cardno);
			info = newWapPayService.validateRequestInfoDTO(token);
			result = newWapPayService.getCardValidates(info.getPaymentRequestId(), cardno);
			ajaxcheckCardNoReturn(out, result, AJAX_SUCCESS, null, null);
		} catch (Throwable te) {
			logger.error(
					"[monitor],event:nccashier_showBanksList_exception,token:{}",
					token, te);
			CashierBusinessException ex = ExceptionUtil.handleException(te, SYSTEM_CODE);
			ajaxcheckCardNoReturn(out, result, AJAX_FAILED, ex.getDefineCode(), ex.getMessage());
		}
	}

	/**
	 * 支付失败
	 * 
	 * @param errormsg
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "pay/fail", method = { RequestMethod.POST })
	public Object payfail(String token, String errormsg) {
		/*
		 * 该方法为以下两种情况调用 第一、同一笔订单只能发送5次短验，超过5次则跳转到支付失败页，点击重新支付；
		 * 第二、确认支付时，非短验错误的情况，直接跳转到支付失败页，可点击重新支付；
		 */
		logger.info("[monitor],event:nccashier_payfail_request,token={}", token);
		RequestInfoDTO info = null;
		ModelAndView mv = null;
		String repayUrl = null;
		try {
			info = ncCashierService.requestBaseInfo(token);
			mv = getBaseModelView(info, token);
			if (null != info&&StringUtils.isNotBlank(errormsg)&&!errormsg.contains("重新下单")) {
				repayUrl = ncCashierService.getURL("PC",
						info.getMerchantNo(),
						Long.valueOf(info.getPaymentRequestId()));
			}
			mv.setViewName("onlinebank/pay_fail");
			mv.addObject("errormsg", errormsg);
			mv.addObject("repayurl", repayUrl);
		} catch (Throwable te) {
			logger.error(
					"[monitor],event:nccashier_payfail_request_Throwable,token={},ex:{}",
					token,te);
			CashierBusinessException ex = ExceptionUtil.handleException(te, SYSTEM_CODE);
			ModelAndView errormv = getBaseModelView(info, token);
			return SystemErrorRV(ex.getDefineCode(), ex.getMessage(), errormv);
		}
		return mv;
	}

	/**
	 * 查询支付结果
	 *
	 * @param token
	 * @param transOrderId
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/query/result", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object queryResult(String token, String isRequery)
			throws InterruptedException, IOException {
		RequestInfoDTO info = null;
		ModelAndView mv = new ModelAndView();
		mv.addObject("token", token);
		mv.setViewName("onlinebank/queryfail_jump");
		try {
			info = newWapPayService.validateRequestInfoDTO(token);
			logger.info("[monitor],event:nccashierwap_queryResult_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(), info.getMerchantNo());
			this.supplyBaseModelViewInfo(info, token, mv);
			TradeNoticeDTO tradeNoticeDTO = newWapPayService.queryResult(info, isRequery);
			if (tradeNoticeDTO != null) {
				ModelAndView finalStateMV = getQueryView(tradeNoticeDTO, token, mv,info);
				if (finalStateMV != null) {
					return finalStateMV;
				}
			}
			return mv;
		}  catch (CashierBusinessException e) {
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
			RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
			name = Base64Util.decode(name);
			idCardNo = Base64Util.decode(idCardNo);
			logger.info("[monitor],event:nccashier_setCardOwnerInfo_request,token={},bindId:{},idCardNo:{},name:{}", token,token1,idCardNo,name);
			if (StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(idCardNo)) {
				newWapPayService.setCardOwner(bindId, name, idCardNo, info);
			}
			if (StringUtils.isNotBlank(bindId)) {
				newWapPayService.setCardOwner(bindId, null, null, info);
			}
		} catch (Throwable e) {
			logger.error("设置同人限制信息异常", e);
		}

	}

	
	private ModelAndView getQueryView(TradeNoticeDTO tradeNoticeDTO, String token,
			ModelAndView sourceMV,RequestInfoDTO info) throws Exception {
		if (tradeNoticeDTO.getTradeState() == TradeStateEnum.SUCCESS) {
			CardOwnerConfirmResDTO cardOwnerConfirmResDTO = newWapPayService.getOwnersInfo(info);
			sourceMV.addObject("cardOwnerConfirmResDTO",JSON.toJSONString(cardOwnerConfirmResDTO));
			//查询参加嘉年华的资格 add by xueping.ni 2016-11-08;update  jimin.zhou	  20171024
			tradeNoticeDTO.setTradeSysNo(info.getTradeSysNo());
			CarnivalVO canrivalVo = newWapPayService.queryQualification4Carnival(tradeNoticeDTO);
			logger.info("[JNH_嘉年华易宝大促]PcPayAction,token={},merNo:{},CarnivalVO:{}",
					token, tradeNoticeDTO.getMerchantNo(),JSONUtils.toJsonString(canrivalVo));
			if(canrivalVo.isShowCarnival()){
				sourceMV.addObject("showCarnival",canrivalVo.isShowCarnival());
				sourceMV.addObject("carnivalUrl",canrivalVo.getCarnivalUrl());
			}
			sourceMV.addObject("merchantUrl", tradeNoticeDTO.getFrontCallBackUrl());
			sourceMV.addObject("amount", tradeNoticeDTO.getPaymentAmount());
			sourceMV.addObject("notifyData", AESUtil.aesEncrypt(JSON.toJSONString(tradeNoticeDTO)));
			sourceMV.addObject("actualAmount", tradeNoticeDTO.getActualAmount());
			sourceMV.setViewName("onlinebank/pay_success");
			return sourceMV;
		} else if (tradeNoticeDTO.getTradeState() == TradeStateEnum.FAILED) {
			Map<String,String> errorsInfo = newWapPayService.getPayFailErrors();
			if(null!=errorsInfo&&errorsInfo.containsKey(tradeNoticeDTO.getErrorCode())){
				newWapPayService.unbindCard(info.getPaymentRequestId(),info.getPaymentRecordId());
			}
			String repayUrl = null;
			try {
				repayUrl = ncCashierService.getURL(tradeNoticeDTO.getCashierVersion(),
						tradeNoticeDTO.getMerchantNo(), tradeNoticeDTO.getPaymentRequestId());
			} catch (Exception e) {
				logger.error("[monitor],event:nccashier_queryResult_getURL_exception,token:{}",
						token, e);
				throw e;
			}
			ErrorCodeDTO ex = ExceptionUtil.handleException(tradeNoticeDTO.getErrorCode(), tradeNoticeDTO.getErrorMsg(),
					SYSTEM_CODE);
			return (ModelAndView) createRepayErrorRV(token, ex.getExternalErrorCode(), ex.getExternalErrorMsg(), repayUrl, sourceMV);
		} else if (tradeNoticeDTO.getTradeState() == TradeStateEnum.CANCEL) {
			sourceMV.addObject("merchantUrl", tradeNoticeDTO.getFrontCallBackUrl());
			sourceMV.setViewName("onlinebank/loading_error");
			return sourceMV;
		}

		return null;
	}
	
	private ModelAndView createSystemErrorRV(String token, String errorcode,
			String errormsg, RequestInfoDTO info) {
		logger.info(
				"[monitor],event:nccashier_createSystemErrorRV,token:{},errormsg:{},RequestInfoDTO:{}",
				token, errormsg, info);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("onlinebank/pay_fail");
		this.supplyBaseModelViewInfo(info, token, mv);
		mv.addObject("token", token);
		mv.addObject("errorcode", errorcode);
		errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
		mv.addObject("errormsg", errormsg + errorcode);
		if (null != info&&StringUtils.isNotBlank(errormsg)&&!errormsg.contains("重新下单")) {
			String repayUrl = ncCashierService.getURL("PC",
					info.getMerchantNo(),
					Long.valueOf(info.getPaymentRequestId()));
			mv.addObject("repayurl", repayUrl);// 所有支付异常都到支付失败页，可点击从新支付支付按钮从新
			logger.info("请求数据 baseInfo=", info.toString());
			if (null != info.getTheme()) {
				mv.addObject("theme", info.getTheme());
			}
		}
		return mv;
	}

	private Object createRepayErrorRV(String token, String errorcode,
			String errormsg, String repayurl, ModelAndView mv) {
		logger.info(
				"[monitor],event:nccashier_createRepayErrorRV,token:{},errormsg:{}",
				token, errormsg);
		mv.setViewName("onlinebank/pay_fail");
		mv.addObject("errorcode", errorcode);
		errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
		mv.addObject("errormsg", errormsg + errorcode);
		mv.addObject("repayurl", repayurl);
		return mv;
	}

	private Object SystemErrorRV(String errorcode, String errormsg,
			ModelAndView mv) {
		logger.info(
				"[monitor],event:nccashier_createSystemErrorRV,errormsg:{}",
				errormsg);
		mv.setViewName("onlinebank/pay_fail");
		mv.addObject("errorcode", errorcode);
		errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
		mv.addObject("errormsg", errormsg + errorcode);
		return mv;
	}

	private ModelAndView getBaseModelView(RequestInfoDTO baseInfo,
			String token) {
		logger.info(
				"[monitor],event:nccashier_getBaseModelView,token:{},userAccessDTO:{}",
				token, ToStringBuilder.reflectionToString(baseInfo));
		ModelAndView view = null;
		if (baseInfo != null) {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			String transtime = simpleDateFormat.format(date);
			if (null != baseInfo) {
				logger.info(
						"[monitor],event:nccashier_用户基本请求信息,token:{},baseInfo:{}",
						token, ToStringBuilder.reflectionToString(baseInfo));
				view = new ModelAndView();
				view.addObject("orderid", baseInfo.getOrderid());
				view.addObject("transtime", transtime);
				view.addObject("productname", baseInfo.getProductname());// 商品名称
				view.addObject("amount", baseInfo.getAmount());// 金额
				view.addObject("companyname", baseInfo.getCompanyname());// 商户名称
				view.addObject("token", token);// 防止同一浏览器同时支付session篡改
				if (null != baseInfo.getTheme()) {
					view.addObject("theme", baseInfo.getTheme());
				}

			}
		}
		return view;
	}

	private void ajaxcheckCardNoReturn(PrintWriter out,
			Map<String, Object> result, String status, String errorcode,
			String errormsg) {
		logger.info(
				"[monitor],event:nccashier_ajaxcheckCardNoReturn_request,status:{},errorcode:{},errormsg:{}",
				status, errorcode, errormsg);
		if(null ==result.get("status") ){
			result.put("status", status);
		}
		if(null ==result.get("error") ){
			result.put("error", errorcode);
		}
		errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
		result.put("msg", errormsg + errorcode);
		String json = "";
		try {
			json = JSON.toJSONString(result);
			out.println(json);
		} catch (Exception e) {
			logger.error(
					"[monitor],event:nccashier_SmsAjaxReturn toJSONString异常", e);
		} finally {
			logger.info("ajaxReturn提示信息:{}", json);
			out.flush();
			out.close();
		}
	}

	private void ajaxPaymentReturn(PrintWriter out,
			Map<String, Object> result,  String status,
			String errorcode, String errormsg) {
		logger.info(
				"[monitor],event:nccashier_SmsAjaxReturn_request,reqSmsSendTypeEnum:{},status:{},errorcode:{},errormsg:{}",
				status, errorcode, errormsg);
		if(result==null){
			result = new HashMap<String, Object>();
		}
		result.put("status", status);
		result.put("error", errorcode);
		errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
		result.put("msg", errormsg + errorcode);
		String json = "";
		try {
			json = JSON.toJSONString(result);
			out.println(json);
		} catch (Exception e) {
			logger.error(
					"[monitor],event:nccashier_SmsAjaxReturn toJSONString异常", e);
		} finally {
			logger.info("ajaxReturn提示信息:{}", json);
			out.flush();
			out.close();
		}
	}

	private void ajaxConfirmPayReturn(PrintWriter out, String repayUrl,
			String token, String status, String errorcode, String errormsg, String jumpTarget) {
		logger.info(
				"[monitor],event:nccashier_SmsAjaxReturn_request,reqSmsSendTypeEnum:{},status:{},errorcode:{},errormsg:{}",
				status, errorcode, errormsg);
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(repayUrl)){
			logger.info("ajaxConfirmPayReturn重新支付按钮内容："+repayUrl);
			result.put("repayurl", repayUrl);
		}
		result.put("token", token);
		result.put("status", status);
		result.put("jumpTarget", jumpTarget);
		result.put("error", errorcode);
		result.put("msg", errormsg);
		String json = "";
		try {
			json = JSON.toJSONString(result);
			out.println(json);
		} catch (Exception e) {
			logger.error(
					"[monitor],event:nccashier_ComfirmPayAjaxReturn toJSONString异常", e);
		} finally {
			out.flush();
			out.close();
		}
	}

	private void supplyBaseModelViewInfo(RequestInfoDTO info, String token, ModelAndView view) {
		if(info==null){
			return;
		}
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		String transtime = simpleDateFormat.format(date);
		String sendSMSNo = null;
		
		view.addObject("orderid", info.getOrderid());
		view.addObject("productname", info.getProductname());// 商品名称
		view.addObject("amount", info.getAmount());// 金额
		view.addObject("companyname", info.getCompanyname());// 商户名称
		view.addObject("token", token);// 防止同一浏览器同时支付session篡改
		view.addObject("transtime", transtime);
		sendSMSNo = ncCashierService.getSendSMSNo();
		view.addObject("sendSMSNo", sendSMSNo);
		if (null != info.getTheme()) {
			view.addObject("theme", info.getTheme());
		}
		// 页面底部展示——支持的银行列表
		List<BankSupportDTO> bankSupports = ncCashierService.supportBankList(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
		List<BankSupportDTO> debitList = new ArrayList<BankSupportDTO>();
		List<BankSupportDTO> creditList = new ArrayList<BankSupportDTO>();
		if (null != bankSupports) {
			for (BankSupportDTO b : bankSupports) {
				if (CardTypeEnum.CREDIT.equals(b.getBanktype())) {
					creditList.add(b);
				} else {
					debitList.add(b);
				}
			}
		}
		view.addObject("debitList", debitList);
		view.addObject("creditList", creditList);
		
	}
	private ModelAndView createCardInfoErrorMV(BusinessTypeEnum businessType, RequestInfoDTO info,
			String token, HttpServletRequest request) {
		ModelAndView mv = null;
		if (businessType == BusinessTypeEnum.FIRSTPASSCARDNO) {
			CashierBusinessException ex = ExceptionUtil.handleException(Errors.INVALID_BANK_CARD_NO, SYSTEM_CODE);
			mv = createSystemErrorRV(token, ex.getDefineCode(), ex.getMessage(), info);
		} else if (businessType == BusinessTypeEnum.FIRSTPAY || businessType == BusinessTypeEnum.FIRSTPASS) {
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

}
