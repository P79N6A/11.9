package com.yeepay.g3.app.nccashier.wap.action;

import com.yeepay.g3.app.nccashier.wap.utils.*;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.DirectPayType;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * 如果是统一收银台PC的扫码支付，会进入到该controller
 * 
 *
 */
@Controller
@RequestMapping(value = "/sccanpay", method = {RequestMethod.POST, RequestMethod.GET})
public class SccanPayAction extends WapBaseAction {

	private static final Logger logger = LoggerFactory.getLogger(SccanPayAction.class);
	
	/**
	 * 本controller对应的错误码系统编码
	 */
	private static final SysCodeEnum SYSTEM_CODE = SysCodeEnum.NCCASHIER_WAP;

	/**
	 * 请求h5收银台
	 * 
	 * @param token
	 * @param requestId
	 * @param merchantNo
	 * @param request
	 * @param wpayId
	 *            openId
	 * @return
	 */
	@RequestMapping(value = "/request", method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestNewWap(String token, String requestId, String merchantNo,
			HttpServletRequest request, String wpayId,String directType) {
		RequestInfoDTO info = null;
		try {
			//写入已扫标识
			if(StringUtils.isNotBlank(directType)&&DirectPayType.CFL.name().equals(directType)){
				RedisTemplate.setCacheObjectSumValue(Constant.INSTALLMENT_DIRECT_SCAN + token, 
						Constant.INSTALLMENT_QR_CODE_BE_SCANNED_SIGN,Constant.SCAN_SIGN_TIMEOUT);
				String url = newWapPayService.directPay(token, PayTypeEnum.CFL.name());
				return new RedirectView(url);
			}
			
			RedisTemplate.setCacheObjectSumValue(Constant.SCAN_SIGN_KEY + token, 
					Constant.PC_QR_CODE_BE_SCANNED_SIGN,Constant.SCAN_SIGN_TIMEOUT);
			/**
			 * 如果直连微信h5/直连支付宝直接请求微信支付
			 */
			if(StringUtils.isNotBlank(directType)){				
				if(DirectPayType.ALIPAY.name().equals(directType)){
					String url = newWapPayService.directPay(token, PayTypeEnum.ALIPAY.name());
					return new RedirectView(url);
				}
			}



			String bankPayUrl = "/wap/request/" + merchantNo + "/" + requestId + "?token=" + token
					+ "&fromNewWAP=true";
			requestId = AESUtil.routeDecrypt(merchantNo, requestId);
			logger.info("nccashier_sccanpay_request requestId="+requestId+" openId="+wpayId);
			// 获取用户终端UA
			String ua = getUserUA(request);
			boolean isWeChatBrowser = ua.indexOf("MicroMessenger") > -1 ? true : false;
			boolean isAlipayBrower = ua.indexOf("Alipay") > -1 ? true : false;
			ModelAndView view = new ModelAndView();
			PayExtendInfo extendInfo = getPayExtendInfo(requestId,token);
			info = getRequestInfoByToken(token);
			if(DirectPayType.ALIPAY.name().equals(extendInfo.getDirectPayType())){
				// 直连支付宝
				String url = newWapPayService.directPay(token, PayTypeEnum.ALIPAY.name());
				return new RedirectView(url);
			}else if(!isWeChatBrowser && DirectPayType.WECHAT.name().equals(extendInfo.getDirectPayType())){
				// 直连wap微信H5支付
				String url = newWapPayService.directPay(token, PayTypeEnum.WECHAT_H5_WAP.name());
				return new RedirectView(url);
			}
			view.addObject("weChatPay", "NONE");
			view.addObject("alipay", extendInfo.containsPayType(PayTypeEnum.ALIPAY));
			view.addObject("bankPay", extendInfo.containsPayType(PayTypeEnum.BANK_PAY_WAP));
			view.addObject("jdPay", extendInfo.containsPayType(PayTypeEnum.JD_H5));

			String bizType = info.getUrlParamInfo().getBizType();
			// 微信浏览器
			if (isWeChatBrowser) {
				logger.info("Request from Wechat Browser");
				//优先走微信
				if (extendInfo.containsPayType(PayTypeEnum.WECHAT_OPENID)) {
					/* 微信公众号支付start */
					boolean redirectJsapi = false; //当走公众号支付jsapi通道时，来客和一码付要求直接微信下单并重定向，其他接入方先展示收银台
					if("17".equals(info.getOrderSysNo()) || "LK".equals(bizType)){
						redirectJsapi = true;
					}
					String url = wechatOpenIdPreRouter(view,wpayId,extendInfo,info,token,redirectJsapi);
					if(StringUtils.isNotEmpty(url)){
						//重定向到jsapi的微信公众号支付；或走微信oauth2.0获取openId
						return new RedirectView(url);
					}
					/* 微信公众号支付end */
				} else if(extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP)){
					view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_WAP.name());
				}
			} else if (isAlipayBrower) {
				logger.info("Request from alipay Browser");
				if (extendInfo.containsPayType(PayTypeEnum.ALIPAY)) {
					// 直连支付宝
					String url = newWapPayService.directPay(token, PayTypeEnum.ALIPAY.name());
					return new RedirectView(url);
				}
				if(extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP)){
					//微信H5
					view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_WAP.name());
				}
			} else {
				if (extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP)) {
					view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_WAP.name());
				}
			}
			setOrderInfo(info,view);
			view.setViewName("newWap");
			view.addObject("token", token);
			view.addObject("bankPayUrl", bankPayUrl);
			view.addObject("timeout", 1000 * CommonUtil.getMQListerTimeout());
			return view;

		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:event:nccashier_sccanpay_request_BusinException,token:"
					+ token, e);
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
			logger.error("[monitor],event:nccashier_requestNewWap_SystemException,token:" + token,
					e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
	}


	/**
	 * 获取openId ； 接收微信系统回调。
	 * @param request
	 * @param parmaMap
	 * @return
	 */
	@RequestMapping("auth2Callback")
	public Object auth2Callback(HttpServletRequest request, Map<String, Object> parmaMap) {
		RequestInfoDTO info = null;
		String token = "";
		try {
			token = request.getParameter("state");
			String code = request.getParameter("code");
			logger.info("接受微信回调,token={},code={}", token, code);
			info = checkRequestInfoDTO(token);
			PayExtendInfo payExtendInfo = getPayExtendInfo(info.getPaymentRequestId() + "", token);
			String suffixUrl = oauthForOpenId(code, token, info, payExtendInfo);
			String url;
			if(payExtendInfo.isSccanPay()){
				url = CommonUtil.getPreUrl(info.getMerchantNo(),request)+sccanpay + "/request?" + suffixUrl;
			}else {
				url = CommonUtil.getPreUrl(info.getMerchantNo(),request)+newwapctx + "/request?" + suffixUrl;
			}
			return new RedirectView(url);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestNewWap_BusinException,token:" + token, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			} else {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestNewWap_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
		}
	}

	/**
	 * 接收微信系统回调，供微信h5低配版使用
	 * @param request
	 * @param parmaMap
	 * @return
	 */
	@RequestMapping("callbackForWechatLow")
	public Object callbackForWechatLow(HttpServletRequest request, Map<String, Object> parmaMap) {
		RequestInfoDTO info = null;
		String token = "";
		try {
			token = request.getParameter("state");
			String code = request.getParameter("code");
			logger.info("接受微信回调,token={},code={}", token, code);
			info = checkRequestInfoDTO(token);
			PayExtendInfo payExtendInfo = getPayExtendInfo(info.getPaymentRequestId() + "", token);
			String suffixUrl = oauthForOpenId(code, token, info, payExtendInfo);
			String url = CommonUtil.getPreUrl(info.getMerchantNo(), request) + newwapctx + "/wechatlow?" + suffixUrl;
			return new RedirectView(url);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestNewWap_BusinException,token:" + token, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			} else {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestNewWap_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
		}
	}

	private String oauthForOpenId(String code,String token,RequestInfoDTO info,PayExtendInfo payExtendInfo) throws Exception {
		String openId = WaChatPayUtils.getWechatOpenId(payExtendInfo.getTargetAppId(),code,payExtendInfo.getAppSecert());
		Long requestId=info.getPaymentRequestId();
		String merchantNo = info.getMerchantNo();
		String encryptRequestId = AESUtil.routeEncrypt(merchantNo, String.valueOf(requestId));
		String suffixUrl = "token=" + token + "&requestId="+ encryptRequestId + "&merchantNo=" + info.getMerchantNo() + "&wpayId=" + openId;
		return suffixUrl;
	}

	/**
	 * 接收微信系统回调，用于模拟业务方接口--->
	 * 业务方可定制自行获取openid，后再跳转收银台，此时业务方需提供类似接口，接受微信回调，获取openid，并跳转到收银台。
	 * @param request
	 * @param parmaMap
	 * @return
	 */
	@RequestMapping("auth2CallbackBiz")
	public Object auth2CallbackForBiz(HttpServletRequest request, Map<String, Object> parmaMap) {
		RequestInfoDTO info = null;
		String token = "";
		String openId = "";
		String code = request.getParameter("code");
		try {
			token = request.getParameter("state");
            info = checkRequestInfoDTO(token);
			if(StringUtils.isNotBlank(CommonUtil.mockOpenIdForBiz())){
				//若QA环境无法正常从微信授权回调，直接读取配置的openid
				openId = CommonUtil.mockOpenIdForBiz();
			}else {
				PayExtendInfo payExtendInfo = getPayExtendInfo(info.getPaymentRequestId() + "", token);
				openId = WaChatPayUtils.getWechatOpenId(payExtendInfo.getTargetAppId(),code,payExtendInfo.getAppSecert());
			}

			StringBuffer suffixUrl = new StringBuffer("openid=");
			suffixUrl.append(openId).append("&timestamp=").append(System.currentTimeMillis()).append("&token=").append(token);
			StringBuffer textToSign = new StringBuffer(suffixUrl).append("&CASHIER");
			suffixUrl.append("&sign=").append(MD5Util.encryptMD5(textToSign.toString())).toString();

			String url = CommonUtil.getPreUrl(info.getMerchantNo(),request)+newwapctx + "/openidredirect?" + suffixUrl;
			logger.info("模拟业务方接受微信回调,token={},code={},要跳转到的收银台地址={}", token, code,url);
			return new RedirectView(url);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_auth2CallbackForBiz_BusinException,token:" + token, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(CommonUtil.getPreUrl(info.getMerchantNo(),request)+wapctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			} else {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_auth2CallbackForBiz_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
		}
	}
}
