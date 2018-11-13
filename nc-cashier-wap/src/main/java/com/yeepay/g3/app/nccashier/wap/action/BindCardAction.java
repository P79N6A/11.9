package com.yeepay.g3.app.nccashier.wap.action;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.utils.Base64Util;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardAuthBindResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardBinInfoResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardMerchantRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.NopBindCardOrderInfoVO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * @Description 收银台纯绑卡action
 * @author yangmin.peng
 * @since 2017年8月22日下午5:47:58
 */
@Controller
public class BindCardAction extends WapBaseAction {

	private static final Logger logger = LoggerFactory.getLogger(BindCardAction.class);
	/**
	 * 收银台纯绑卡-PC/WAP用
	 * 商户调用该接口如果发生错误，直接给商户抛异常
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping("/cashier/bindcard")
	public Object requestBindCard(HttpServletRequest request,HttpServletResponse response) {
		CashierVersionEnum cashierVersionEnum = null;
		BindCardMerchantRequestVO bindCardRequestVO = null;
		ModelAndView mv = new ModelAndView();
		try {
			bindCardRequestVO = validateBindCardPayParams(request);
			mv.addObject("bizStatus", AJAX_SUCCESS);
		} catch (Throwable t) {
			logger.error("商户单独绑卡请求失败, merchantNo="+request.getParameter("merchantNo")+", merchantFlowId="+request.getParameter("merchantFlowId"),t);
			mv.addObject("bizStatus", AJAX_FAILED);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER);
			mv.addObject("errorcode", ex.getDefineCode());
			mv.addObject("errormsg", ex.getMessage());
		} finally {
			mv.addObject("bindCardMerchantRequest", JSON.toJSONString(bindCardRequestVO));
			cashierVersionEnum = getTerminalInfo(request);
			if (cashierVersionEnum == CashierVersionEnum.PC) {
				mv.setViewName("customizedPc/bindCard");
			} else {
				mv.setViewName("vue_bindCardPay");
			}
			return mv;
		}
	}

	private BindCardMerchantRequestVO validateBindCardPayParams(HttpServletRequest request) {
		String merchantNo = request.getParameter("merchantNo");
		String merchantFlowId = request.getParameter("merchantFlowId");
		String timestamp = request.getParameter("timestamp");
		String userNo = request.getParameter("userNo");
		String userType = request.getParameter("userType");
		String bindCallBackUrl = request.getParameter("bindCallBackUrl");
		String cardType = request.getParameter("cardType") == null ? "" : request.getParameter("cardType");
		String bizType = request.getParameter("bizType") == null ? "" : request.getParameter("bizType");
		String bindFrontCallBackUrl = request.getParameter("bindFrontCallBackUrl") == null ? ""
				: request.getParameter("bindFrontCallBackUrl");
		String extendInfo = request.getParameter("ext") == null ? "" : request.getParameter("ext");
		String sign = request.getParameter("sign");
		if (StringUtils.isBlank(merchantNo) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(sign)
				|| StringUtils.isBlank(userNo) || StringUtils.isBlank(userType) || StringUtils.isBlank(merchantFlowId)
				|| StringUtils.isBlank(bindCallBackUrl)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
					Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		long times = 0l;
		try {
			times = Long.parseLong(timestamp);
		} catch (Throwable e) {
			logger.error("转换时间戳异常timestamp=" + timestamp);
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
					Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		CommonUtil.checkUrlOutOfExpDate(times);
		StringBuilder sb = new StringBuilder();
		TreeMap<String, String> plainTreeMap = new TreeMap<String, String>();
		plainTreeMap.put("merchantNo", merchantNo);
		plainTreeMap.put("cardType", cardType);
		plainTreeMap.put("merchantFlowId", merchantFlowId);
		plainTreeMap.put("timestamp", timestamp);
		plainTreeMap.put("userNo", userNo);
		plainTreeMap.put("userType", userType);
		plainTreeMap.put("bindCallBackUrl", bindCallBackUrl);
		plainTreeMap.put("bizType", bizType);
		plainTreeMap.put("bindFrontCallBackUrl", bindFrontCallBackUrl);
		plainTreeMap.put("ext", extendInfo);
		for (Entry<String, String> entry : plainTreeMap.entrySet()) {
			if (StringUtils.isNotBlank(entry.getValue())) {
				sb.append("&" + entry.getKey() + "=").append(entry.getValue());
			}
		}
		String plainText = sb.toString();
		if(plainText.startsWith("&")){
			plainText = plainText.substring(1,plainText.length());
		}
		String appKey = null;
		//如果bizType为空，则
        if(CommonUtil.checkBizType(bizType)){
            appKey=merchantNo;
        }
		yopVerify(appKey, null, plainText, sign);
		if (!bindCardService.getNopBindCardOpenStatus(merchantNo)) {
			throw new CashierBusinessException(Errors.NOT_OPEN_BIND_CARD.getCode(), Errors.NOT_OPEN_BIND_CARD.getMsg());
		}
		BindCardMerchantRequestVO bindCardRequestVO = new BindCardMerchantRequestVO();
		bindCardRequestVO.setBindCallBackUrl(bindCallBackUrl);
		bindCardRequestVO.setBindFrontCallBackUrl(bindFrontCallBackUrl);
		bindCardRequestVO.setBizType(StringUtils.isBlank(bizType) ? "DS" : bizType);
		bindCardRequestVO.setBizFlowId(UUID.randomUUID().toString());
		bindCardRequestVO.setCardType(cardType);
		bindCardRequestVO.setExt(extendInfo);
		bindCardRequestVO.setMerchantNo(merchantNo);
		bindCardRequestVO.setMerchantFlowId(merchantFlowId);
		bindCardRequestVO.setUserNo(userNo);
		bindCardRequestVO.setUserType(userType);
		return bindCardRequestVO;
	}
	/**
	 *WAP收银台 用户点击新增银行卡后，跳转至绑卡输卡号页面
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bindCard/requestFirst")
	public Object requestFirst(String token, HttpServletRequest request){
		RequestInfoDTO info = null;
		try{
			if(StringUtils.isBlank(token)){
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			ModelAndView mv = new ModelAndView();
			BindCardMerchantRequestVO bindCardMerchantRequest = new BindCardMerchantRequestVO();
			info = ncCashierService.requestBaseInfo(token);
			bindCardMerchantRequest.setUserNo(info.getUrlParamInfo().getUserNo());
			bindCardMerchantRequest.setUserType(info.getUrlParamInfo().getUserType());
			bindCardMerchantRequest.setCardType(info.getUrlParamInfo().getCardType());
			// 使用收单商编
			bindCardMerchantRequest.setMerchantNo(info.getMerchantNo());
			bindCardMerchantRequest.setMerchantFlowId(info.getOrderid());
			bindCardMerchantRequest.setBizFlowId(info.getOrderid());
			bindCardMerchantRequest.setBizType(info.getUrlParamInfo().getBizType());
			setOrderInfo(info, mv);
			mv.addObject("bindCardMerchantRequest", JSON.toJSONString(bindCardMerchantRequest));
			mv.addObject("token", token);
			mv.setViewName("vue_bindCardPay");
			//获取是否支持定制活动
			mv.addObject("showActivity",ncCashierService.queryQualification4Activities());
			return mv;
		}catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestFirst_BusinException,token:" + token, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			} else {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
				return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestFirst_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
	}
	
	
	/**
	 * PC收银台绑卡支付过程中获取模拟的商户订单信息/wap收银台点击新增银行卡时，首先调用该接口（如果报错的话，获取错误信息），然后再调用requestFirst
	 * @param token
	 * @param bindCardMerchantRequest
	 * @return
	 */
	@RequestMapping(value = "/bindCard/prepareBindCard")
	@ResponseBody
	public void prepareBindCard(String token, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_bindCard_requestFirst,token:{}", token);
		RequestInfoDTO info = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			validatePassBIndId(token);
			if(StringUtils.isBlank(token)){
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
			//如果商户请求标准链接时，未传userNo和userType，则跳转输卡号页直接报错
			if(StringUtils.isBlank(info.getUrlParamInfo().getUserNo()) || StringUtils.isBlank(info.getUrlParamInfo().getUserType())){
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			result.put("bizStatus", AJAX_SUCCESS);
			BindCardMerchantRequestVO bindCardMerchantRequest = new BindCardMerchantRequestVO();
			bindCardMerchantRequest.setBindCallBackUrl("");
			bindCardMerchantRequest.setBindFrontCallBackUrl("");
			bindCardMerchantRequest.setBizFlowId(info.getOrderid());
			bindCardMerchantRequest.setExt("");
			bindCardMerchantRequest.setUserNo(info.getUrlParamInfo().getUserNo());
			bindCardMerchantRequest.setUserType(info.getUrlParamInfo().getUserType());
			bindCardMerchantRequest.setCardType(info.getUrlParamInfo().getCardType());
			bindCardMerchantRequest.setMerchantNo(info.getMerchantNo());
			bindCardMerchantRequest.setMerchantFlowId(info.getOrderid());
			bindCardMerchantRequest.setBizType(StringUtils.isNotBlank(info.getUrlParamInfo().getBizType()) ? info.getUrlParamInfo().getBizType():"DS");
			result.put("bindCardMerchantRequest", bindCardMerchantRequest);
			result.put("token", token);
			ajaxResultWrite(response, result);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_bindCard_requestFirst_exception,token:" + token, t);
			result.put("token", token);
			result.put("bizStatus", AJAX_FAILED);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER_WAP);
			commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	/**
	 * 卡bin校验请求-WAP/PC用
	 * 
	 * @param token
	 * @param cardno
	 * @param bindCardMerchantRequest
	 * @param response
	 */
	@RequestMapping(value = "/bindCard/binInfo")
	@ResponseBody
	public void getBindCardBinInfo(String token, String cardno, BindCardMerchantRequestVO bindCardMerchantRequest,
			HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_bindCard_binInfo_request,token:{},bindCardMerchantRequest:{}", token,bindCardMerchantRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			cardno = Base64Util.decode(cardno);
			NOPCardBinResponseDTO bindCardInfoDTO = bindCardService.getNopCardBinInfo(token, cardno, bindCardMerchantRequest);
			BindCardBinInfoResponseVO bindCardInfo = new BindCardBinInfoResponseVO();
			bindCardInfo.setBankCode(bindCardInfoDTO.getBankCode());
			bindCardInfo.setBankName(bindCardInfoDTO.getBankName());
			bindCardInfo.setCardType(bindCardInfoDTO.getCardType());
			bindCardInfo.setCardlater(cardno.substring(cardno.length()-4,cardno.length()));
			result.put("token", token);
			result.put("bizStatus", AJAX_SUCCESS);
			result.put("bindCardInfo", bindCardInfo);
			result.put("bindCardMerchantRequest", bindCardMerchantRequest);
			commonAjaxReturn(response, result, null, null);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_bindCard_exception,token:" + token, t);
			result.put("bizStatus", AJAX_FAILED);
			result.put("token", token);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER);
			commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	/**
	 * 鉴权绑卡请求-PC/WAP用
	 * 
	 * @param token
	 * @param cardInfo
	 * @param bindCardMerchantRequest
	 * @param response
	 */
	@RequestMapping(value = "/bindCard/authBindCard")
	@ResponseBody
	public void authBindCardRequest(String token, BindCardInfoVO cardInfo, BindCardMerchantRequestVO bindCardMerchantRequest,
			HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_bindCard_authBindCardRequest,token:{},cardInfo:{},bindCardMerchantRequest:{}", token,cardInfo,bindCardMerchantRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		NOPAuthBindResponseDTO authBindCardResponse = null;
		try {
			Base64Util.decryptBindCardInfoVO(cardInfo);
			authBindCardResponse = bindCardService.authBindCardRequest(token, cardInfo,
					bindCardMerchantRequest);
			BindCardAuthBindResponseVO authBindRequest = new BindCardAuthBindResponseVO();
			authBindRequest.setNopOrderId(authBindCardResponse.getNopOrderId());
			authBindRequest.setRequestFlowId(authBindCardResponse.getRequestFlowId());
			result.put("bizStatus", AJAX_SUCCESS);
			result.put("authBindRequest", authBindRequest);
			result.put("bindCardMerchantRequest", bindCardMerchantRequest);
			commonAjaxReturn(response, result, null, null);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_authBindCardRequest_exception,token:" + token, t);
			result.put("bizStatus", AJAX_FAILED);

			if(t instanceof CashierBusinessException && Errors.SMS_SEND_FRILED.getCode().equals(((CashierBusinessException)t).getDefineCode())){
				result.put("smsStatus", Constant.SMS_SEND_ERROR);//短验发送失败，可以重新发送短信
			}
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER);
			
			commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	/**
	 * 鉴权绑卡确认-PC/WAP
	 * 
	 * @param token
	 * @param smsCode
	 * @param bindCardMerchantRequest
	 * @param response
	 */
	@RequestMapping(value = "/bindCard/authBindCardConfirm")
	@ResponseBody
	public void authBindCardConfirm(String token, String smsCode, BindCardInfoVO cardInfo, NopBindCardOrderInfoVO nopOrderInfo, BindCardMerchantRequestVO bindCardMerchantRequest,
			HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_bindCard_authBindCardConfirm,token:{},nopOrderInfo:{},bindCardMerchantRequest:{}", token,nopOrderInfo,bindCardMerchantRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(StringUtils.isBlank(nopOrderInfo.getRequestFlowId())){
				throw new CashierBusinessException(Errors.GET_SMS_FIRST.getCode(),Errors.GET_SMS_FIRST.getMsg());
			}
			Base64Util.decryptBindCardInfoVO(cardInfo);
			NOPAuthBindConfirmResponseDTO authBindCardConfirmResponse = bindCardService.authBindCardConfirm(token,smsCode,
					cardInfo,nopOrderInfo,bindCardMerchantRequest);
			BindCardAuthBindResponseVO authBindConfirm = new BindCardAuthBindResponseVO();
			authBindConfirm.setBindId(authBindCardConfirmResponse.getBindId());
			authBindConfirm.setNopOrderId(authBindCardConfirmResponse.getNopOrderId());
			authBindConfirm.setRequestFlowId(authBindCardConfirmResponse.getRequestFlowId());
			if(StringUtils.isNotBlank(token)){
				FirstBindCardPayResponseDTO bindPayResponseDTO = authBindCardConfirmResponse.getBindPayResponse(); 
				if( bindPayResponseDTO != null && bindPayResponseDTO.isFinishPay()){
					result.put("bizStatus", BIND_PAY_AJAX_SUCCESS);
				}else if(bindPayResponseDTO != null 
						&& (bindPayResponseDTO.isLoseNeedItem() ||  bindPayResponseDTO.getReqSmsSendTypeEnum() != null)){
					result.put("bizStatus", BIND_PAY_AJAX_NEED_ITEM);
					result.put("smsType", bindPayResponseDTO.getReqSmsSendTypeEnum()==null?null:bindPayResponseDTO.getReqSmsSendTypeEnum().name());
					result.put("needBankCardItem", newWapPayService.hiddenNeedBankCardDTO(bindPayResponseDTO.getNeedBankCardDto()));
				}
			}else{
				String frontCallBackUrl = buildFrontCallbackUrl(bindCardMerchantRequest);
				result.put("frontCallBackUrl", frontCallBackUrl);
				result.put("bizStatus", BIND_CARD_AJAX_SUCCESS);
			}
			result.put("authBindConfirm", authBindConfirm);
			result.put("bindCardMerchantRequest", bindCardMerchantRequest);
			commonAjaxReturn(response, result, null, null);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_authBindCardConfirm_exception,token:" + token, t);
			if(t instanceof CashierBusinessException && Errors.SMS_VERIFY_FAILED.getCode().equals(((CashierBusinessException)t).getDefineCode())){
				result.put("smsStatus", Constant.SMS_VERIFY_ERROR);//短验验证失败，可以重新发送短信
			}else if(t instanceof CashierBusinessException && Errors.SMS_INPUT_ERROR.getCode().equals(((CashierBusinessException)t).getDefineCode())){
				result.put("smsStatus", Constant.SMS_INPUT_ERROR);//短验输入错误，可以重新输入短信
			}else if(t instanceof CashierBusinessException && Errors.GET_SMS_FIRST.getCode().equals(((CashierBusinessException)t).getDefineCode())){
				result.put("smsStatus", Constant.SMS_NOT_GET);//请先调用获取短验接口
			}
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER);
			if(StringUtils.isNotBlank(token)){
				if(Errors.SYSTEM_EXCEPTION.getCode().equals(ex.getDefineCode())){
					result.put("bizStatus", BIND_PAY_AJAX_UNKNOWN);
				}else if("N9002001".equals(ex.getDefineCode()) || "P9002001".equals(ex.getDefineCode())){
					result.put("bizStatus", BIND_PAY_AJAX_SUCCESS);
				}else{
					// 允许重新支付
					result.put("bizStatus", BIND_PAY_AJAX_FAIL);
					result.put("needRepay", true);
				}
			}else{
				if(Errors.SYSTEM_EXCEPTION.getCode().equals(ex.getDefineCode())){
					result.put("bizStatus", BIND_CARD_AJAX_UNKNOWN);
				}else{
					result.put("bizStatus", BIND_CARD_AJAX_FAIL);
				}
			}
			commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	/**
	 * 绑卡发短验-PC/WAP
	 * 
	 * @param token
	 * @param bindCardMerchantRequest
	 * @param response
	 */
	@RequestMapping(value = "/bindCard/authBindCardSMS")
	@ResponseBody
	public void authBindCardSMS(String token, NopBindCardOrderInfoVO nopOrderInfo,BindCardMerchantRequestVO bindCardMerchantRequest,
			HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_bindCard_authBindCardSMS,token:{},nopOrderInfo:{},bindCardMerchantRequest:{}", token,nopOrderInfo,bindCardMerchantRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			NOPAuthBindSMSResponseDTO authBindCardSMSDTO = bindCardService.authBindCardSMS(token, nopOrderInfo,bindCardMerchantRequest);
			BindCardAuthBindResponseVO authBindSMSVO = new BindCardAuthBindResponseVO();
			authBindSMSVO.setNopOrderId(authBindCardSMSDTO.getNopOrderId());
			authBindSMSVO.setRequestFlowId(authBindCardSMSDTO.getRequestFlowId());
			result.put("bizStatus", AJAX_SUCCESS);
			result.put("authBindSMS", authBindSMSVO);
			result.put("bindCardMerchantRequest", bindCardMerchantRequest);
			commonAjaxReturn(response, result, null, null);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_authBindCardConfirm_exception,token:" + token, t);
			result.put("bizStatus", AJAX_FAILED);
			if(t instanceof CashierBusinessException && Errors.SMS_SEND_FRILED.getCode().equals(((CashierBusinessException)t).getDefineCode())){
				result.put("smsStatus", Constant.SMS_SEND_ERROR);//短验发送失败，可以重新发送短信
			}else if(t instanceof CashierBusinessException && Errors.DO_NOT_PERMIT_SMS.getCode().equals(((CashierBusinessException)t).getDefineCode())){
				result.put("smsStatus", Constant.SMS_NOT_PERMIT);//短验发送失败，不能再次发送短信
			}
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER);
			commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}
	

	/**
	 * 查询绑卡订单信息-PC/WAP
	 * 
	 * @param token
	 * @param bindCardMerchantRequest
	 * @param response
	 */
	@RequestMapping(value = "/bindCard/queryBindCardOrder")
	@ResponseBody
	public void queryBindCardOrder(String token, NopBindCardOrderInfoVO nopOrderInfo,BindCardMerchantRequestVO bindCardMerchantRequest,
			HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_bindCard_queryBindCardOrder,token:{},nopOrderInfo:{},bindCardMerchantRequest:{}", token,nopOrderInfo,bindCardMerchantRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			NOPQueryOrderResponseDTO queryNopOrderStatus = bindCardService.queryNopOrderStatus(nopOrderInfo,bindCardMerchantRequest);
			if ("BIND_SUCCESS".equals(queryNopOrderStatus.getOrderStatus())) {
				result.put("orderStatus", BIND_CARD_AJAX_SUCCESS);
				String frontCallBackUrl = buildFrontCallbackUrl(bindCardMerchantRequest);
				result.put("frontCallBackUrl", frontCallBackUrl);
			} else if("BIND_FAILURE".equals(queryNopOrderStatus.getOrderStatus())){
				result.put("orderStatus", BIND_CARD_AJAX_FAIL);
			} else {
				result.put("orderStatus", BIND_CARD_AJAX_UNKNOWN);
			}
			result.put("bizStatus", AJAX_SUCCESS);
			result.put("bindCardMerchantRequest", bindCardMerchantRequest);
			result.put("nopOrderInfo", nopOrderInfo);
			commonAjaxReturn(response, result, null, null);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_authBindCardConfirm_exception,token:" + token, t);
			result.put("bizStatus", AJAX_FAILED);
			result.put("orderStatus", BIND_CARD_AJAX_UNKNOWN);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER);
			commonAjaxReturn(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	private String buildFrontCallbackUrl(BindCardMerchantRequestVO bindCardMerchantRequest){
		StringBuffer url = new StringBuffer("");
		if(StringUtils.isNotBlank(bindCardMerchantRequest.getBindFrontCallBackUrl())){
			url.append(bindCardMerchantRequest.getBindFrontCallBackUrl());
			if(bindCardMerchantRequest.getBindFrontCallBackUrl().indexOf("?")>-1){
				url.append("&merchantNo=").append(bindCardMerchantRequest.getMerchantNo());
				url.append("&merchantFlowId=").append(bindCardMerchantRequest.getMerchantFlowId());
			}else{
				url.append("?merchantNo=").append(bindCardMerchantRequest.getMerchantNo());
				url.append("&merchantFlowId=").append(bindCardMerchantRequest.getMerchantFlowId());
			}
			StringBuffer signContent = new StringBuffer("");
			signContent.append("merchantNo=").append(bindCardMerchantRequest.getMerchantNo())
					   .append("&merchantFlowId=").append(bindCardMerchantRequest.getMerchantFlowId());
			String sign = ncCashierService.sign(signContent.toString());
			if(StringUtils.isEmpty(sign)){
				throw new CashierBusinessException(Errors.SIGN_ERROR.getCode(), Errors.SIGN_ERROR.getMsg());
			}else {
				url.append("&sign="+sign);
			}
		}
		return url.toString();
	}
}
