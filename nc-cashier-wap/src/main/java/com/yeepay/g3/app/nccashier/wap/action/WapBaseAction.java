package com.yeepay.g3.app.nccashier.wap.action;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.service.*;
import com.yeepay.g3.app.nccashier.wap.utils.*;
import com.yeepay.g3.app.nccashier.wap.vo.BasicResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.MerchantCashierCustomizedLayoutSelectVO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WapBaseAction{

	private static final Logger logger = LoggerFactory.getLogger(WapBaseAction.class);

	//无卡收银台wap
	protected static final String wapctx = "/wap";

	//移动收银台（wap端路由支付方式：一键支付/微信支付/支付宝支付/分期支付）
	protected static final String newwapctx = "/newwap";

	//扫码支付
	protected static final String sccanpay = "/sccanpay";
	
	protected static final String newpcctx = "/newpc";

	protected static final String LOGO_PATH = "/static/images/logo.png";

	protected static final int IMAGE_SIZE = 230;

	protected static final String AJAX_SUCCESS = "success";

	protected static final String AJAX_FAILED = "failed";
	
	protected static final String SMS_FAILD = "smsFailed";

	protected static final String ORDER_FAILED = "orderFailed";
	
	protected static final String AJAX_UNKNOWN = "unknown";
	
	protected static final String BIND_PAY_AJAX_SUCCESS = "bindPaySuccess";
	
	protected static final String BIND_CARD_AJAX_SUCCESS = "bindCardSuccess";
	
	protected static final String BIND_PAY_AJAX_NEED_ITEM = "bindPayNeedItem";
	protected static final String BIND_PAY_AJAX_FAIL = "bindPayFail";
	protected static final String BIND_PAY_AJAX_UNKNOWN = "bindPayUnknown";
	protected static final String BIND_CARD_AJAX_FAIL = "bindCardFail";
	protected static final String BIND_CARD_AJAX_UNKNOWN = "bindCardUnKnown";

	protected static final String POP_ERROR = "popup";
	 
	@Resource
	protected NcCashierService ncCashierService;
	
	@Resource
	protected NewWapPayService newWapPayService;
	
	@Resource
	protected EBankPayService eBankPayService;

	@Resource
	protected QRCodeScanService qrCodeScanService;

	@Resource
	protected MerchantAuthorityService merchantAuthorityService;
	
	@Resource
	protected AccountPayService accountPayService;

	@Resource
	protected MerchantCashierTemplateCustomizedService merchantCashierTemplateCustomizedService;
	
	@Resource
	protected NotBankCardPayService notBankCardPayService;
	
	@Resource
	protected BindCardService bindCardService;
	
	@Resource
	protected PreauthService preauthService;

	
	protected PayExtendInfo getPayExtendInfo(String requestId,String tokenId) {
		PayExtendInfo extendInfo = ncCashierService.getPayExtendInfo(Long.parseLong(requestId),tokenId);
		if (extendInfo == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		return extendInfo;
	}
	protected Object createRepayErrorRV(String token, String errorcode, String errormsg,
			String repayurl, ModelAndView mv) {
		mv.setViewName("pay_fail");
		mv.addObject("errorcode", errorcode);
		errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
		mv.addObject("errormsg", errormsg + errorcode);
		mv.addObject("repayurl", repayurl);
		return mv;
	}

	protected ModelAndView createErrorMV(String token, String errorcode, String errormsg,
			RequestInfoDTO info) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("pay_fail");
		mv.addObject("token", token);
		mv.addObject("errorcode", errorcode);
		errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
		mv.addObject("errormsg", errormsg + errorcode);
		if (null != info) {
			//定制不展示易宝公司信息
			if(CommonUtil.notShowYeepayCompanyInfo(info.getMerchantNo(), info.getOrderSysNo())){
				mv.addObject(CommonUtil.NOT_SHOW_YEEPAY_COMPANY_INFO_FLAG, true);
			}
			mv.addObject("merchantNo", info.getMerchantNo());
			mv.addObject("orderId", info.getOrderid());
			mv.addObject("theme", info.getTheme());
		}
		return mv;
	}
	
	protected ModelAndView createErrorMV(String token, String errorcode, String errormsg,
			RequestInfoDTO info, String errorViewName, String repayUrl) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName(errorViewName);
		mv.addObject("repayurl",repayUrl);
		mv.addObject("token", token);
		mv.addObject("errorcode", errorcode);
		errorcode = StringUtils.isEmpty(errorcode) ? "" : "(" + errorcode + ")";
		mv.addObject("errormsg", errormsg + errorcode);
		if (null != info) {
			mv.addObject("merchantNo", info.getMerchantNo());
			mv.addObject("orderId", info.getOrderid());
			mv.addObject("theme", info.getTheme());
		}
		return mv;
	}

	protected void ajaxErrorReturn(PrintWriter out, String status, String errorcode,
			String errormsg) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("status", status);
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
	
	protected RequestInfoDTO getRequestInfoByToken(String token) {
		RequestInfoDTO info  = ncCashierService.requestBaseInfo(token);
		return info;
	}
	
	protected String getUserUA(HttpServletRequest request) {
		String ua = RequestUtils.getUserUA(request);
		if (StringUtils.isEmpty(ua)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		return ua;
	}
	
	protected String getUserIp(HttpServletRequest request) {
		String ypip = RequestUtils.getUserIP(request);
		if (StringUtils.isEmpty(ypip)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		return ypip;
	}
	
	/**
	 * ajax返回data
	 * @param response
	 * @param data
	 * @throws IOException
	 */
	protected void ajaxResultWrite(HttpServletResponse response, Object data) {
		PrintWriter out = null;
		try{
			out = response.getWriter();
			String resultJson = JSON.toJSONString(data);
			out.println(resultJson);
			out.flush();
		}catch (IOException e){
			logger.error("httpResponse writes error,e:{}",e);
		}
		finally{
			if (out!=null) {
				out.close();
			}
		}
	}
	
	/**
	 * action请求异常，返回携带错误码&错误描述信息的vo
	 * 
	 * @param t
	 * @param sysCode
	 * @return
	 */
	protected BasicResponseVO handleAjaxException(Throwable t, SysCodeEnum sysCode) {
		if ((t instanceof CashierBusinessException)
				&& (Errors.THRANS_FINISHED.getCode().equals(((CashierBusinessException) t).getDefineCode())
						|| ("N9002001".equals(((CashierBusinessException) t).getDefineCode())))) {
			return new BasicResponseVO(Constant.SUCCESS);
		}
		CashierBusinessException e = ExceptionUtil.handleException(t, sysCode);
		BasicResponseVO responseVO = new BasicResponseVO(e.getDefineCode(), e.getMessage(), Constant.FAIL);
		return responseVO;
	}

	/**
	 * 通用的ajax 的返回结果
	 * @param result
	 */
	protected void commonAjaxReturn(HttpServletResponse response, Map<String, Object> result, String errorcode, String errormsg) {
		PrintWriter out = null;
		String json = "";
		try {
			if (result.get("bizStatus") == null) {
				result.put("bizStatus", AJAX_SUCCESS);
			}
			out = response.getWriter();
			result.put("errorcode", errorcode);
			result.put("errormsg", errormsg);
			json = JSON.toJSONString(result);
			out.println(json);
		} catch (Exception e) {
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	/**
	 * 通用的ajax 的返回结果
	 * @param result
	 */
	protected void commonAjaxReturnNew(HttpServletResponse response, Map<String, Object> result, String errorcode, String errormsg) {
		PrintWriter out = null;
		String json = "";
		try {
			if (result.get("bizStatus") == null) {
				result.put("bizStatus", AJAX_SUCCESS);
			}
			out = response.getWriter();
			result.put("errorCode", errorcode);
			result.put("errorMsg", errormsg);
			json = JSON.toJSONString(result);
			out.println(json);
		} catch (Exception e) {
		} finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * 获取无参数url
	 * @param token
	 * @param relativeUrl
	 * @return
	 */
	protected String getUrlNonParam(String token, String relativeUrl){
		return new StringBuffer(newpcctx).append(relativeUrl).append(token).toString();
	}

	
	/**
	 * pc统一收银台局部异常页
	 *
	 * @param token
	 * @param errorEvent
	 * @return
	 */
	protected ModelAndView createNewPcErrorRV(String token, boolean isPartialError, String errorEvent, Throwable e,
			SysCodeEnum sysCode) {

		if (e != null) {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token, e);
		} else {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token);
		}


		ModelAndView mv = new ModelAndView();
		mv.setViewName("newpc/pay_fail");
		
		CashierBusinessException ex = ExceptionUtil.handleException(e, sysCode);
		mv.addObject("token", token);
		mv.addObject("errorcode", ex.getDefineCode());
		mv.addObject("errormsg", ex.getMessage());
		return mv;
	}
	protected ModelAndView createBindCardPcErrorMV(String token, String errorEvent, Throwable e,
			SysCodeEnum sysCode) {
		
		if (e != null) {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token, e);
		} else {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token);
		}
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("newpc/bindCardFail");
		
		CashierBusinessException ex = ExceptionUtil.handleException(e, sysCode);
		mv.addObject("token", token);
		mv.addObject("errorcode", ex.getDefineCode());
		mv.addObject("errormsg", ex.getMessage());
		return mv;
	}
	protected ModelAndView createBindCardWapErrorMV(String token, String errorEvent, Throwable e,
			SysCodeEnum sysCode) {
		
		if (e != null) {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token, e);
		} else {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token);
		}
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("newWapBindCardFail");
		
		CashierBusinessException ex = ExceptionUtil.handleException(e, sysCode);
		mv.addObject("token", token);
		mv.addObject("errorcode", ex.getDefineCode());
		mv.addObject("errormsg", ex.getMessage());
		return mv;
	}
	/**
	 * pc统一收银台局部异常页，为定制化设计
	 *
	 * @param token
	 * @param errorEvent
	 * @return
	 */
	protected ModelAndView createNewPcErrorRV(String token, boolean isPartialError, String errorEvent, Throwable e,
			SysCodeEnum sysCode, String merchantNo, HttpServletRequest request) {
		
		if (e != null) {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token, e);
		} else {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token);
		}
		
		
		ModelAndView mv = new ModelAndView();
		MerchantCashierCustomizedLayoutSelectVO merchantCashierCustomizedLayoutInfo = merchantCashierTemplateCustomizedService
				.queryMerchantCashierCustomizedLayoutSelectInfo(merchantNo);			
		if(StringUtils.isNotBlank(merchantCashierCustomizedLayoutInfo.getLayoutNo())){
			mv.setViewName("customizedPc/pay_fail");
			setCustomizedHeadVm(request, mv, merchantCashierCustomizedLayoutInfo);	
		}else{
			mv.setViewName("newpc/pay_fail");
		}
		
		CashierBusinessException ex = ExceptionUtil.handleException(e, sysCode);
		mv.addObject("token", token);
		mv.addObject("errorcode", ex.getDefineCode());
		mv.addObject("errormsg", ex.getMessage());
		return mv;
	}

	/**
	 * pc统一收银台局部异常页--新接口，增加参数info，可以将订单信息等带回前端
	 * @param token
	 * @param isPartialError
	 * @param errorEvent
	 * @param e
	 * @param sysCode
	 * @param info - RequestInfoDTO info，包含订单信息，参数可选
	 * @return
	 */
	protected ModelAndView createNewPcErrorRV(String token, boolean isPartialError, String errorEvent, Throwable e, SysCodeEnum sysCode, RequestInfoDTO info) {
		ModelAndView mv = createNewPcErrorRV(token,isPartialError,errorEvent,e,sysCode);
		if(null != info){
			setOrderInfo(info,mv);
		}
		return mv;
	}
	/**
	 * pc统一收银台局部异常页--新接口，增加参数merchantNo，为定制化设计
	 * @param token
	 * @param isPartialError
	 * @param errorEvent
	 * @param e
	 * @param sysCode
	 * @param info - RequestInfoDTO info，包含订单信息，参数可选
	 * @return
	 */
	protected ModelAndView createNewPcErrorRV(String token, boolean isPartialError, String errorEvent, Throwable e, SysCodeEnum sysCode, RequestInfoDTO info, String merchantNo, HttpServletRequest request) {
		ModelAndView mv = createNewPcErrorRV(token,isPartialError,errorEvent,e,sysCode,merchantNo, request);
		if(null != info){
			setOrderInfo(info,mv);
		}
		return mv;
	}


	/**
	 * view 中设置基本的订单信息
	 * @param info
	 * @param modelAndView
	 */
	protected void setOrderInfo(RequestInfoDTO info,ModelAndView modelAndView){
		modelAndView.addObject("orderId", info.getOrderid());//商户订单号
		modelAndView.addObject("productName", info.getProductname());// 商品名称
		modelAndView.addObject("amount", info.getAmount());// 金额
		modelAndView.addObject("companyName", info.getCompanyname());// 商户名称
		// modify by meiling —— 钱麦要求：针对10014884620商户的交易，不在H5收银台上显示{付款给***}
		modelAndView.addObject("companyText", CommonUtil.getCompanyInfoShowText(info.getMerchantNo(),
				info.getOrderSysNo(), info.getCompanyname(), null));
		modelAndView.addObject("merchantNo",info.getMerchantNo());
		if(info.getTradeTime()!=null){
			modelAndView.addObject("tradeTime", DataUtil.getFormatTimeData(info.getTradeTime()));
		}
		if(info.getFee()!=null){
			modelAndView.addObject("amount", info.getAmount().add(info.getFee()));// 金额
			modelAndView.addObject("fee", info.getFee());
		}
	}




	/**
	 * 错误信息返回数据
	 * 
	 * @param map
	 * @param e
	 */
	protected Object createErrorMsg(Map<String, Object> map, Throwable e, String errorEvent, String token,
			SysCodeEnum sysCode) {
		if (e != null) {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token, e);
		} else {
			logger.error("[monitor],event:" + errorEvent + ",token:" + token);
		}
		if (map == null) {
			map = new HashMap<String, Object>();
		}

		CashierBusinessException ex = ExceptionUtil.handleException(e, sysCode);
		map.put("errorcode", ex.getDefineCode());
		map.put("errormsg", ex.getMessage());
		return JSON.toJSONString(map);
	}

	
	/**
	 * 检查基本信息的返回
	 */
	protected RequestInfoDTO checkRequestInfoDTO(String token) {
		if (StringUtils.isBlank(token)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		RequestInfoDTO requestInfoDTO = ncCashierService.requestBaseInfo(token);
		if (requestInfoDTO == null || requestInfoDTO.getPaymentRequestId() == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		} else {
			return requestInfoDTO;
		}
	}
	/**
	 * 收银台定制化生成模版文件访问路径
	 * @param request
	 * @param fileId
	 * @param fileType
	 * @return
	 */
	protected String getCashierCustomizedFileUri(HttpServletRequest request,String fileId,String fileType){
		StringBuilder sb = new StringBuilder();
		String fileName = "";
		if(Constant.MERCHANT_CASHIER_CUSTOMIZED_LESS_FILE_TYPE.equals(fileType)){
			fileName = "layout-"+fileId+".less";
		}else if(Constant.MERCHANT_CASHIER_CUSTOMIZED_JS_FILE_TYPE.equals(fileType)){
			fileName = "template-"+fileId+".js";
		}else{
			fileName = "logo-"+fileId+".jpg";
		}
		sb.append(request.getContextPath())
		  .append(File.separator)
		  .append("layout")
		  .append(File.separator)
		  .append(fileName);
		return sb.toString();
	}
	/**
	 * 判断是移动端还是pc端
	 * @param request
	 * @return
	 */
	public CashierVersionEnum getTerminalInfo(HttpServletRequest request){
		
		// 先取业务方定制的收银台版本
		CashierVersionEnum cashierVersion = CommonUtil.getCustomCashierVersionByBizType(request.getParameter("bizType"));
		if(cashierVersion!=null){
			return cashierVersion;
		}
		
		// 业务方未定制，取UA
		String ua = getUserUA(request);
		logger.info("token={}请求获取ua信息={}", request.getParameter("token"), ua);
		
		//为了考虑pc端的支付方式更多些，所以这么些，正常情况不会出现的
		if(StringUtils.isBlank(ua)){
			return CashierVersionEnum.PC;
		}
		// modify by meiling.zhuang : 去掉公众号收银台，用户使用微信浏览器进入的是WAP H5收银台 —— 需要确认从微信浏览器进入的ua值是否包含Mobile
		if(ua.indexOf("Mobile")!=-1){
			return CashierVersionEnum.WAP;
		} 
		else if(ua.indexOf("Windows")!=-1 || ua.indexOf("MAC")!=-1){
			return CashierVersionEnum.PC;
		}
		else {
			return CashierVersionEnum.PC;
		}
	}
	
	/**
	 * 当发生异常时，尝试根据token查询订单信息
	 * @param userAccess
	 * @return
	 */
	protected RequestInfoDTO getErrorOrderInfo(UserAccessDTO userAccess) {
		RequestInfoDTO info = null;
		try {
			if (userAccess != null && StringUtils.isNotEmpty(userAccess.getTokenId())) {
				info = checkRequestInfoDTO(userAccess.getTokenId());
			}
			return info;
		} catch (Throwable t) {
			return null;
		}
	}
	
	/**
	 * 处理wap action的异常信息
	 * @param t
	 * @param eventName
	 * @param token
	 * @param info
	 * @param request
	 * @return
	 */
	protected Object handleWapException(Throwable t, String eventName, String token, RequestInfoDTO info,
			HttpServletRequest request) {
		if (t instanceof CashierBusinessException) {
			logger.warn("[monitor],event:" + eventName + ", token:" + token, t);
		} else {
			logger.error("[monitor],event:" + eventName + ", token:" + token, t);
		}
		
		// 否则跳到失败页
		CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER_WAP);
		return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
	}

	/**
	 * 微信公众号支付，预路由相关逻辑
	 * @param view
	 * @param wpayId
	 * @param extendInfo
	 * @param info
	 * @param redirectJsapi
	 * @return url to wechat oauth
	 */
	protected String wechatOpenIdPreRouter(ModelAndView view, String wpayId, PayExtendInfo extendInfo, RequestInfoDTO info, String token, boolean redirectJsapi) throws Exception {
		if (view == null || extendInfo == null || info == null || StringUtils.isEmpty(token)) {
			return null;
		}
		if (StringUtils.isNotBlank(wpayId)) {
			//wpayId非空，即已经经过微信oauth2认证，进行回调
			if ("none".equals(wpayId)) {
				return null;
			} else {
				view.addObject("weChatPay", PayTypeEnum.WECHAT_OPENID.name());
				view.addObject("wpayId", wpayId);
				return null;
			}
		}
		//wpayId为空，调取预路由接口，获取AppId
		JsapiRouteResponseDTO jsapiRouteResponseDTO = null;
		try {
			jsapiRouteResponseDTO = ncCashierService.getAppid(info.getPaymentRequestId() + "", PayTypeEnum.WECHAT_OPENID.name());
		} catch (Throwable t) {
			logger.warn("预路由调用异常，不展示微信支付，requestId = "+info.getPaymentRequestId()+", token = "+token+"，异常 = ",t);
			return null;
		}

		if (jsapiRouteResponseDTO == null || !Constant.PRE_ROUTE_STATUS_SUCCESS.equals(jsapiRouteResponseDTO.getDealStatus())) {
			// 预路由返回状态非成功
			return null;
		}
		if (Constant.PRE_ROUTE_SCENE_TYPE_EXT_JSAPIH5.equals(jsapiRouteResponseDTO.getSceneTypeExt())) {
			//如果支持，优先走jsapi（h5的微信公众号支付）
			if(redirectJsapi){
				//来客和一码付要求：直接微信支付下单，并重定向到微信地址，不展示收银台
				try {
					String url = newWapPayService.directPay(token, PayTypeEnum.WECHAT_OPENID.name());
					return url;
				} catch (Throwable t) {
					logger.warn("跳转jsapiH5失败，仍然展示移动收银台 token="+token+", error message=", t);
					return null;
				}
			}else {
				//其他接入方：先进入收银台，用户选择微信支付后，再进行微信支付
				view.addObject("weChatPay", PayTypeEnum.WECHAT_OPENID.name());
				return null;
			}
		} else if (StringUtils.isNotBlank(jsapiRouteResponseDTO.getAppId())) {
			//走normal通道
			if (jsapiRouteResponseDTO.getAppId().equals(extendInfo.getOrigAppId())) {
				// 预appId=透传的appId
				if (StringUtils.isNotBlank(extendInfo.getOrigOpenId())) {
					// 有透传openId, 直接走报备通道（支付）
					view.addObject("weChatPay", PayTypeEnum.WECHAT_OPENID.name());
					view.addObject("wpayId", extendInfo.getOrigOpenId());
					return null;
				} else if ("WECHAT".equals(extendInfo.getIdentityType()) && StringUtils.isNotBlank(extendInfo.getIdentityId())) {
					//没有透传openId,但透传indentityType为WECHAT且透传identityId
					view.addObject("weChatPay", PayTypeEnum.WECHAT_OPENID.name());
					view.addObject("wpayId", extendInfo.getIdentityId());
					return null;
				}
			}
			if (StringUtils.isNotBlank(jsapiRouteResponseDTO.getAppSecret())) {
				// 没有透传openId,且（indentityType非WECHAT或未透传identityId），但预路由获取到appSecret
				// 预路由的appId与透传的appId不相等 ，但预路由获取到appSecret
				String toWeChatUrl = WaChatPayUtils.toWechatAuth(jsapiRouteResponseDTO.getAppId(), token);
				return toWeChatUrl;
			}
			//预路由没有获取appSecret，判断下商户或业务方是否定制了由业务方获取openId。目前有易码付定制。
			String bizOauthUrl = CommonUtil.getBizOauthUrl(info.getMerchantNo(), info.getOrderSysNo(), token, jsapiRouteResponseDTO.getAppId());
			return bizOauthUrl;
		}
		//不满足以上情况的，不予增加weChatPay=WECHAT_OPENID的标识
		return null;
	}
	
	
	
	/**
	 * 往定制化head.vm赋值
	 * @param request
	 * @param modelAndView
	 * @param merchantCashierCustomizedLayoutInfo
	 */
	public void setCustomizedHeadVm(HttpServletRequest request, ModelAndView modelAndView,
			MerchantCashierCustomizedLayoutSelectVO merchantCashierCustomizedLayoutInfo) {
		//默认支付工具排序，pc收银台非首页展示用
		String[] defaultPayToolsOrder={ "NCPAY", "EANK", "SCCANPAY", "ZF_ZHZF"}; 
		//商户选择走定制化模版，获取定制化文件
		merchantCashierTemplateCustomizedService.queryMerchantCashierCustomizedFile(merchantCashierCustomizedLayoutInfo);
		if(null == modelAndView.getModelMap().get("payToolsOrder")){
			modelAndView.addObject("payToolsOrder", JSON.toJSONString(defaultPayToolsOrder));
		}
		modelAndView.addObject("mainColor", merchantCashierCustomizedLayoutInfo.getFrontColor());
		modelAndView.addObject("fontColor", merchantCashierCustomizedLayoutInfo.getBackColor());
		modelAndView.addObject("needCustomService", merchantCashierCustomizedLayoutInfo.isNeedCustomService());
		modelAndView.addObject("layoutId", merchantCashierCustomizedLayoutInfo.getLayoutNo());		
		modelAndView.addObject("servicePhone",merchantCashierCustomizedLayoutInfo.getServicePhone());
		/**
		 * 商户选择定制化收银台时，返回给前端的文件路径
		 * /nc-cashier-wap/layout/template-10040007800.js
		 * /nc-cashier-wap/layout/logo-10040007800.jpg
		 * /nc-cashier-wap/layout/layout-10040007800.less
		 */
		modelAndView.addObject("lessFileUrl",getCashierCustomizedFileUri(request, merchantCashierCustomizedLayoutInfo.getLayoutNo(),
						Constant.MERCHANT_CASHIER_CUSTOMIZED_LESS_FILE_TYPE));
		modelAndView.addObject("jsFileUrl",getCashierCustomizedFileUri(request, merchantCashierCustomizedLayoutInfo.getLayoutNo(),
				Constant.MERCHANT_CASHIER_CUSTOMIZED_JS_FILE_TYPE));
		if(StringUtils.isNotBlank(merchantCashierCustomizedLayoutInfo.getLogoNo())){
			modelAndView.addObject("logoFileUrl", getCashierCustomizedFileUri(request, merchantCashierCustomizedLayoutInfo.getLogoNo(),
					Constant.MERCHANT_CASHIER_CUSTOMIZED_LOGO_FILE_TYPE));		
		}
	}
	
	/**
	 * 获取H5收银台支付方式选择页的url
	 * 
	 * @param requestId
	 * @param merchantNo
	 * @param token
	 * @param request
	 * @return
	 */
	protected String getH5IndexUrl(Long requestId, String merchantNo, String token, HttpServletRequest request, String directPayType) {
		if (requestId == null || requestId <= 0 || StringUtils.isBlank(merchantNo)) {
			return null;
		}
		String encryptRequestId = null;
		try {
			encryptRequestId = AESUtil.routeEncrypt(merchantNo, String.valueOf(requestId));
		} catch (Exception e) {
			logger.error("编码requestId失败,requestId:" + requestId);
		}
		if (StringUtils.isBlank(encryptRequestId)) {
			return null;
		}
		StringBuilder repayurl = new StringBuilder(CommonUtil.getPreUrl(merchantNo, request));
		repayurl.append(wapctx).append("/request/").append(merchantNo).append("/").append(encryptRequestId).append("?token=").append(token);
		if(StringUtils.isNotBlank(directPayType)){
			repayurl.append("&directType=").append(directPayType);
		}
		return repayurl.toString();
	}
	
	/**
	 * 一键支付/绑卡支付，获取当前外部用户已绑卡列表【可用+不可用】
	 * 
	 * @param token
	 * @return
	 */
	protected Map<String, Object> getBindCardList(String token) {
		RequestInfoDTO info = null;
		BankCardReponseDTO response = null;
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			info = checkRequestInfoDTO(token);
			response = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
			if (response != null) {
				if (response.getBankCardDTO() != null || CollectionUtils.isNotEmpty(response.getUnuseBankCardDTO())) {
					List<BankCardDTO> bankCardsToShow = new ArrayList<BankCardDTO>();

					if (response.getBankCardDTO() != null) {
						// 可用前置绑卡非空==>展示可用前置绑卡
						if (CollectionUtils.isEmpty(response.getBankCardDTO().getOtherCards())) {
							bankCardsToShow.add(response.getBankCardDTO());
						} else {
							bankCardsToShow.addAll(response.getBankCardDTO().getOtherCards());
						}
					} else if (response.getPassCardInfoDTO() != null
							&& StringUtils.isNotEmpty(response.getPassCardInfoDTO().getCardNo())) {
						// 可用前置卡为空，且有透传卡信息时：直接返回空的待展示列表
						return res;
					}

					if (CollectionUtils.isNotEmpty(response.getUnuseBankCardDTO())) {
						// 不可用前置绑卡列表非空==>增加展示不可用前置绑卡
						bankCardsToShow.addAll(response.getUnuseBankCardDTO());
					}
					if (!response.isAuthorized()) {
						response.setShowChangeCard(true);
					}
					res.put("bankCardsToShow", bankCardsToShow);
					res.put("authorized", response.isAuthorized());
					res.put("showChangeCard", response.getShowChangeCard());


					if(ncCashierService.isPassBindId(info.getPaymentRequestId()))
						res.put("showChangeCard", false);
				}
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_preBindRoute_exception,token:" + token, e);
		}
		return res;
	}


	/**
	 * 校验透传绑卡是否正常
	 * @return
     */
	protected  boolean validatePassBIndId(String token){
		RequestInfoDTO info = null;
		info = checkRequestInfoDTO(token);
		BasicResponseDTO response = ncCashierService.validatePassBindId(info.getPaymentRequestId());
		if (response != null) {
			return true;
		}
		return false;
	}


	/**
	 * 验证签名
	 * 
	 * @param merchantNo
	 * @param oldText
	 *            未加扩展参数之前的签名明文
	 * @param text
	 * @param sign
	 */
	protected void yopVerify(String merchantNo, String oldText, String text, String sign) {
		boolean success = ncCashierService.yopVerify(merchantNo, text, sign);
		if (!success) {
			if(StringUtils.isBlank(oldText)){
				throw new CashierBusinessException(Errors.SECURITY_ERROR.getCode(), Errors.SECURITY_ERROR.getMsg());
			}
			else{
				success = ncCashierService.yopVerify(merchantNo, oldText, sign);
				if (!success) {
					throw new CashierBusinessException(Errors.SECURITY_ERROR.getCode(), Errors.SECURITY_ERROR.getMsg());
				}
			}
		}
	}
	
	protected SysCodeEnum judgeSysCode(HttpServletRequest request, RequestInfoDTO info) {
		CashierVersionEnum version = null;
		if (info != null && info.getCashierVersionEnum() != null) {
			version = info.getCashierVersionEnum();
		} else {
			version = getTerminalInfo(request);
		}
		if (CashierVersionEnum.PC.equals(version)) {
			return SysCodeEnum.NCCASHIER_PC;
		} else {
			return SysCodeEnum.NCCASHIER_WAP;
		}
	}
	protected void supplyBaseModelViewInfo4Preauth(RequestInfoDTO info, String token, ModelAndView view,String cusType){
		String sendSMSNo = null;
		view.addObject("orderid", info.getOrderid());
		view.addObject("productname", info.getProductname());// 商品名称
		view.addObject("amount", info.getAmount());// 金额
		if(info.getFee()!=null){
			view.addObject("amount", info.getAmount().add(info.getFee()));// 金额
			view.addObject("fee", info.getFee());
		}
		view.addObject("companyname", info.getCompanyname());// 商户名称 - 收款方名称
		view.addObject("companyText", CommonUtil.getCompanyInfoShowText(info.getMerchantNo(), info.getOrderSysNo(),
				info.getCompanyname(), cusType));
		view.addObject("token", token);// 防止同一浏览器同时支付session篡改
		sendSMSNo = ncCashierService.getSendSMSNo();
		view.addObject("sendSMSNo", sendSMSNo);
		if (null != info.getTheme()) {
			//定制不展示易宝公司信息
			if(CommonUtil.notShowYeepayCompanyInfo(info.getMerchantNo(), info.getOrderSysNo())){
				ThemeResult themeResult = new ThemeResult();
				themeResult.setShowBottomInfo(false);
				themeResult.setShowMerchantName(false);
				info.setTheme(themeResult);
			}
			view.addObject("theme", info.getTheme());
		}
	}
	// TODO更换新key
	protected void supplyBaseModelViewInfo(RequestInfoDTO info, String token, ModelAndView view) {
		supplyBaseModelViewInfo4Preauth(info, token, view, null);
	}


	/**
	 * 是否开通指定产品
	 * @param requestId
	 * @param token
	 * @param payType
     */
	protected void hasOpenProduct(long requestId, String token,PayTypeEnum payType) {
		PayExtendInfo payExtendInfo = ncCashierService.getPayExtendInfo(requestId, token);
		if (!payExtendInfo.containsPayType(payType)) {
			throw new CashierBusinessException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}
	}



}
