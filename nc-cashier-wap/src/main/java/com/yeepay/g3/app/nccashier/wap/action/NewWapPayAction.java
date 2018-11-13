package com.yeepay.g3.app.nccashier.wap.action;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.enumtype.BrowserType;
import com.yeepay.g3.app.nccashier.wap.utils.*;
import com.yeepay.g3.app.nccashier.wap.vo.AlipayStandardJumpInfo;
import com.yeepay.g3.app.nccashier.wap.vo.BankLimitAmountResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.PayResultQueryStateListenVO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.DirectPayType;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.CommonUtils;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 新wap收银台控制器 (移动收银台)
 * 一键支付/微信支付/支付宝支付/分期支付（一个选择页）
 *
 */
@Controller
@RequestMapping(value = "/newwap", method = {RequestMethod.POST, RequestMethod.GET})
public class NewWapPayAction extends WapBaseAction{

	private static final Logger logger = LoggerFactory.getLogger(NewWapPayAction.class);

	private static byte[] logoImage;

	/**
	 * 本controller层对应的系统编码
	 */
	private static final SysCodeEnum SYSTEM_CODE = SysCodeEnum.NCCASHIER_WAP;

	/**
	 * 构造WeChatPayRequestDTO对象
	 *
	 * @param token
	 * @param payType
	 *            该字段不可为空
	 * @param wpayId
	 * @param appId
	 * @param info
	 *            该对象不可为空
	 * @return
	 */
	private WeChatPayRequestDTO buildWeChatPayRequestDTO(String token, PayTypeEnum payType, String wpayId, String appId,
			RequestInfoDTO info) {
		WeChatPayRequestDTO requestDTO = new WeChatPayRequestDTO();
		requestDTO.setTokenId(token);
		requestDTO.setRequestId(info.getPaymentRequestId());
		requestDTO.setRecordId(info.getPaymentRecordId() == null ? 0 : info.getPaymentRecordId());
		requestDTO.setPayType(payType.name());
		requestDTO.setOpenId(wpayId);
		requestDTO.setAppId(appId);
		return requestDTO;
	}

	/**
	 * 微信(支付宝、京东等)支付
	 *
	 * @param token
	 * @param payType
	 * @param wpayId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/wechat/pay", method = {RequestMethod.POST})
	@ResponseBody
	public String weChatPayRequest(String token, String payType, String wpayId,HttpServletRequest request,HttpServletResponse response) {
		logger.info("[monitor],event:weChatPayRequest request,token:{},payType:{},wpayId:{}", token, payType,wpayId);
		try {
			RequestInfoDTO info = checkRequestInfoDTO(token);
			if (StringUtils.isBlank(payType)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			PayExtendInfo extendInfo = getPayExtendInfo(info.getPaymentRequestId()+"",token);
			Map<String, Object> json = new HashMap<String, Object>();

			if(PayTypeEnum.ALIPAY_H5.name().equals(payType) || PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(payType)){
				String alipayVersion = extendInfo.toDecideAlipayVersion();
				if(StringUtils.isBlank(alipayVersion)){
					logger.error("支付宝下单，payType={}, token={}, 没有开通任何支付宝H5相关的支付方式", payType, token);
					throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
				}
				if(PayTypeEnum.ALIPAY_H5.name().equals(payType)){ // 从普通浏览器进来
					if(PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(alipayVersion)){
						// 预路由
						AlipayStandardJumpInfo jumpInfo = newWapPayService.alipayStandardPreHandleBeforeJumping(info.getPaymentRequestId()+"", token, info.getMerchantNo());
						return JSON.toJSONString(jumpInfo);
					}
				}
				if (PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(payType)) { // 从支付宝内部
					boolean alipayStandardSignal = WaChatPayUtils.checkEwalletH5PackingSiginal(token,
							PayTypeEnum.ALIPAY_H5_STANDARD.name());
					if (!alipayStandardSignal || !PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(alipayVersion)) {
						// 如果没有操作标识，或优先级判断非支付宝标准版，则报错
						logger.error(
								"weChatPayRequest() 不能使用支付宝h5标准版下单，参数 payType={},token={},wpayId={},商编={}，操作标识={},优先级={}",
								payType, token, wpayId, info.getMerchantNo(), alipayStandardSignal, alipayVersion);
						throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
					}
				}
			}

			if (PayTypeEnum.WECHAT_H5_WAP.name().equals(payType)) {
				int h5Preference = WaChatPayUtils.checkWechatH5Preference(info.getMerchantNo(), extendInfo);
				if (WaChatPayUtils.WECHAT_H5_CONFIG_NONE == h5Preference) {
					//1.0，报错
					logger.error("weChatPayRequest() 不能使用微信h5下单， payType={},token={},商编={}，商户未开通微信h5标配版或微信h5低配版", payType, token, info.getMerchantNo());
					throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
				}
				if (WaChatPayUtils.WECHAT_H5_PREFER_LOW == h5Preference) {
					//1.1，如果payType=WECHAT_H5_WAP，且优先级为使用低配版，则走低配版的预下单逻辑
					String targetUrl = newWapPayService.wechatH5PreRouter(extendInfo, info, token, request);
					String userIp = getUserIp(request);
					String ua = getUserUA(request);
					String phone = WaChatPayUtils.getPhone(ua);
					String lockKey= userIp+"_"+phone;
					String jumpUrl = WaChatPayUtils.buildWechatH5JumpUrl(targetUrl,lockKey,info.getPaymentRequestId()+"");
					if (StringUtils.isBlank(jumpUrl)) {
						logger.error("weChatPayRequest() 不能使用微信h5低配版预下单， payType={},token={},商编={}，获取传送门地址失败，请检查预路由结果或传送门API接口", payType, token, info.getMerchantNo());
						throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
					}else if ("LOCK".equals(jumpUrl)){
						throw new CashierBusinessException(Errors.NOT_GET_LOCK);
					}
                    WaChatPayUtils.recordEwalletH5PackingSiginal(token, PayTypeEnum.WECHAT_H5_LOW.name());
                    json.put("url", jumpUrl);
                    json.put("status", "success");
                    json.put("needRedirect", true);
                    return JSON.toJSONString(json);
                }
                //1.2，优先级使用标配版，按原有下单逻辑即可
            }
            if (PayTypeEnum.WECHAT_H5_LOW.name().equals(payType)) {
				//2，payType=WECHAT_H5_LOW
				if (!WaChatPayUtils.checkEwalletH5PackingSiginal(token, PayTypeEnum.WECHAT_H5_LOW.name()) || WaChatPayUtils.WECHAT_H5_PREFER_LOW != WaChatPayUtils.checkWechatH5Preference(info.getMerchantNo(), extendInfo)) {
					//2.1，如果没有操作标识，或优先级判断非低配版，则报错
					logger.error("weChatPayRequest() 不能使用微信h5低配版下单，参数 payType={},token={},wpayId={},商编={}，操作标识={},优先级={}",
							payType, token, wpayId, info.getMerchantNo(), WaChatPayUtils.checkEwalletH5PackingSiginal(token, PayTypeEnum.WECHAT_H5_LOW.name()),WaChatPayUtils.checkWechatH5Preference(info.getMerchantNo(), extendInfo));
					throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
				}
			}
			//3，都按原有流程下单
			WeChatPayRequestDTO requestDTO = new WeChatPayRequestDTO();
			requestDTO.setTokenId(token);
			requestDTO.setRequestId(info.getPaymentRequestId());
			requestDTO.setRecordId(info.getPaymentRecordId() == null ? 0 : info.getPaymentRecordId());
			requestDTO.setPayType(payType);
			requestDTO.setOpenId(wpayId);
			if(PayTypeEnum.WECHAT_OPENID.name().equals(payType) || PayTypeEnum.WECHAT_H5_LOW.name().equals(payType)){
				requestDTO.setAppId(extendInfo.getTargetAppId());
			}

			String result = ncCashierService.WeChatPay(requestDTO);
			if(PayTypeEnum.ALIPAY.name().equals(payType) || PayTypeEnum.ALIPAY_H5.name().equals(payType)){
				Map<String, Object> cancelConfig = CommonUtil.getAlipayPayUrl(result);
				Boolean cancelAlipayRoutePage = (Boolean) cancelConfig.get("cancelFlag");
				if(cancelAlipayRoutePage!=null && cancelAlipayRoutePage){
					json.put("url", cancelConfig.get("payUrl"));
					json.put("cancelAlipayRoutePage", cancelAlipayRoutePage);
				}else{
					json.put("url", result);
				}
			}else{
				json.put("url", result);
			}
			if((PayTypeEnum.WECHAT_OPENID.name().equals(payType) || PayTypeEnum.WECHAT_H5_LOW.name().equals(payType)) && Constant.PRE_ROUTE_SCENE_TYPE_EXT_JSAPIH5.equals(extendInfo.getSceneTypeExt())){
				//走jsapi通道的微信公众号(或微信h5低配版)支付，需要重定向到url
				json.put("needRedirect",true);
			}
			json.put("status", "success");
			return JSON.toJSONString(json);
		} catch (Throwable e) {
			logger.error("[monitor],event:weChatPayRequest_exception，token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("status", "failed");
			result.put("error", ex.getDefineCode());
			String errorcode = StringUtils.isEmpty(ex.getDefineCode()) ? "" : "(" + ex.getDefineCode() + ")";
			result.put("msg", ex.getMessage() + errorcode);
			return JSON.toJSONString(result);
		}
	}

	/**
	 * 获取支付宝页面展示标识，特别说明：在非支付宝非微信浏览器内且要使用支付宝标准版时，返回给前端的type为ALIPAY_H5
	 *
	 * @param alipayVersion
	 * @param browserType
	 * @return
	 */
	private String getAlipayPageLabel(String alipayVersion, BrowserType browserType) {
		boolean isAlipayBrower = (BrowserType.ALIPAY == browserType);
		if (!isAlipayBrower) {
			if (PayTypeEnum.ALIPAY.name().equals(alipayVersion)) {
				return ConstantUtil.ALIPAY;
			} else if (PayTypeEnum.ALIPAY_H5.name().equals(alipayVersion)) {
				return ConstantUtil.ALIPAY_H5;
			} else if (PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(alipayVersion)) {
				return ConstantUtil.ALIPAY_H5; // 非支付宝浏览器返回支付宝标准版
			}
		}
		return null;
	}

	private void setPropOfSelectedPage(ModelAndView view, PayExtendInfo extendInfo, String token,
			String requestId, String merchantNo) {
		view.setViewName(CommonUtil.h5PageCustomizedOrNot(merchantNo));
		payTypeToDisplay(view, extendInfo, token, requestId, merchantNo);
	}

	/**
	 * 支付宝标准版：支付宝内支付选择页的页面属性设置
	 *
	 * @param view
	 * @param extendInfo
	 * @param token
	 * @param requestId
	 * @param merchantNo
	 */
	private void setPropOfSelectedPageForStdAlipay(ModelAndView view, PayExtendInfo extendInfo, String token,
			String requestId, String merchantNo) {
		view.setViewName(CommonUtil.h5PageCustomizedOrNot(merchantNo));
		payTypeToDisplay(view, extendInfo, token, requestId, merchantNo);
		if (extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP)
				|| extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_LOW)) {
			view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_WAP.name());
		}
		boolean existUsablePayTool = checkPayTypesAvailable(view);
		if(!existUsablePayTool){
			 throw new CashierBusinessException(Errors.NO_PAYTYPE_AVAILABLE_ERROR);
		}
	}

	/**
	 * 在H5收银台支付选择页面要展示的支付工具
	 *
	 * @param view
	 * @param extendInfo
	 * @param token
	 * @param requestId
	 * @param merchantNo
	 */
	private void payTypeToDisplay(ModelAndView view, PayExtendInfo extendInfo, String token, String requestId,
			String merchantNo) {
		view.addObject("weChatPay", "NONE");
		view.addObject("alipay", false);
		// 消费分期是否可用
		view.addObject("installment", extendInfo.containsCflPayTypes());
		// 绑卡支付是否可用
		view.addObject("bindCardPay", extendInfo.containsPayType(PayTypeEnum.BK_ZF));
		// 一键支付是否可用
		view.addObject("bankPay", extendInfo.containsPayType(PayTypeEnum.BANK_PAY_WAP));
		String bankPayUrl = "/wap/request/" + merchantNo + "/" + requestId + "?token=" + token + "&fromNewWAP=true";
		view.addObject("bankPayUrl", bankPayUrl);
		// 京东支付是否可用
		view.addObject("jdPay", extendInfo.containsPayType(PayTypeEnum.JD_H5));
		// 账户支付是否可用
		boolean openAccountPay = extendInfo.containsPayType(PayTypeEnum.ZF_ZHZF);
		view.addObject("showAccountPay", openAccountPay);
		view.addObject("accountPayUrl", openAccountPay ? "/wap/accountpay/index?token=" + token : null);
		// 银行卡分期是否可用
		boolean bcardInstallmentPay = extendInfo.containsPayType(PayTypeEnum.YHKFQ_ZF);
		view.addObject("bcardInstallmentPay", bcardInstallmentPay);
		view.addObject("bcardInstallmentUrl", bcardInstallmentPay ? "/bankinstallment/wap/index?token=" + token : null);
		// 担保分期是否可用
		boolean guaranteeInstallmentPay = extendInfo.containsPayType(PayTypeEnum.DBFQ_TL);
		view.addObject("guaranteeInstallmentPay", guaranteeInstallmentPay);
		view.addObject("guaranteeInstallmentUrl", guaranteeInstallmentPay ? "/guarantee/wap/index?token=" + token : null);


	}

	/**
	 * 请求移动收银台非直连（提供支付工具选择页面）
	 * @param token
	 * @param requestId
	 * @param merchantNo
	 * @param request
	 * @param wpayId 微信公众号支付，从微信授权回调时，会传入该参数
	 * @return
	 */
	@RequestMapping(value = "/request", method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestNewWap(String token,String requestId, String merchantNo,HttpServletRequest request,String wpayId) {
		logger.info("[monitor],event:nccashier_requestNewWap_request,token:{}", token);
		RequestInfoDTO info = null;
		try {
			info = checkRequestInfoDTO(token);
			ModelAndView view = new ModelAndView();
			BrowserType browserType = RequestUtils.getBrowserType(request);
			String encryptId = requestId;
			requestId = AESUtil.routeDecrypt(merchantNo, requestId);
			PayExtendInfo extendInfo = getPayExtendInfo(requestId,token);

			// 除了微信和支付宝外的支付工具是否可用逻辑判断
			setPropOfSelectedPage(view, extendInfo, token, encryptId, merchantNo);

			// 判断当前使用的支付宝支付方式是什么，返回相应的前端标识
			String alipayVersion = extendInfo.toDecideAlipayVersion();
			String alipayKey = getAlipayPageLabel(alipayVersion, browserType);
			if(StringUtils.isNotBlank(alipayKey)){
				view.addObject(alipayKey, true);
			}

			boolean isAlipayBrower = BrowserType.ALIPAY == browserType;
			boolean isWeChatBrowser = BrowserType.WECHAT == browserType;
			if (isWeChatBrowser) {
                logger.info("Request from Wechat Browser");
                if (extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_LOW) && WaChatPayUtils.checkEwalletH5PackingSiginal(token, PayTypeEnum.WECHAT_H5_LOW.name())) {
					//如果开通了h5低配版，且有低配版操作标识(微信h5低配版中，只有jsapi的会走到这里，normal的会走到下方requestNewWapForWechatLow方法)
					//传给前端weChatPay=WECHAT_H5_LOW，以便当用户在微信环境内中断操作时，可以直接重新发起支付
					view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_LOW.name());
                } else if (extendInfo.containsPayType(PayTypeEnum.WECHAT_OPENID)) {
                		String bizType = info.getUrlParamInfo().getBizType();
                    // 微信公众号
                    boolean redirectJsapi = false; //当走公众号支付jsapi通道时，来客和一码付要求直接微信下单并重定向，其他接入方先展示收银台
                    if ("17".equals(info.getOrderSysNo()) || "LK".equals(bizType)) {
                        redirectJsapi = true;
                    }
                    String url = wechatOpenIdPreRouter(view, wpayId, extendInfo, info, token, redirectJsapi);
                    if (StringUtils.isNotEmpty(url)) {
                        //重定向到jsapi的微信公众号支付；或走微信oauth2.0获取openId
                        return new RedirectView(url);
                    }
                } else if (extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP) || extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_LOW)) {
                    // 微信h5标配版
                    view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_WAP.name());
                }
                //如果商户仅开通了微信公众号支付，且预路由判定无法使用公众号支付，需直接展示错误页进行提示
                if (!checkPayTypesAvailable(view)) {
                    throw new CashierBusinessException(Errors.NO_PAYTYPE_AVAILABLE_ERROR);
                }
            } else if (isAlipayBrower) {
				logger.info("Request from alipay Browser");
				if(PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(alipayVersion) && WaChatPayUtils.checkEwalletH5PackingSiginal(token, PayTypeEnum.ALIPAY_H5_STANDARD.name())){
					// 区别【直接从标准版链接进来】与【从普通浏览器选择支付标准版后通过传送门进来支付宝浏览器】的标识：redis标识
					// 讨论的结果：忽略标识过期的问题，过期导致不可支付的情况是允许的
					String alipayAuth2Url = newWapPayService.alipayLifeNoPreRouteIgnoreOriUserId(requestId, token);
					return new RedirectView(alipayAuth2Url);
				}
				else if(extendInfo.containsPayType(PayTypeEnum.ZFB_SHH)){
					// 如果开了支付宝生活号，优先走支付宝生活号
					String alipayLifeNoAuth2Url = alipayLifeNoPayPreRoute(token, wpayId, extendInfo, info, view);
					if(StringUtils.isNotBlank(alipayLifeNoAuth2Url)){
						return new RedirectView(alipayLifeNoAuth2Url);
					}
				}
				else{
					boolean openAlipay = PayTypeEnum.ALIPAY.name().equals(alipayVersion);
					boolean openAlipayH5 = PayTypeEnum.ALIPAY_H5.name().equals(alipayVersion);
					if(openAlipay || openAlipayH5){
						// 直连支付宝
						String url = newWapPayService.directPay(token, openAlipay?PayTypeEnum.ALIPAY.name():PayTypeEnum.ALIPAY_H5.name());
						return new RedirectView(url);
					}
				}
				if(extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP) || extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_LOW)){
					//微信H5
					view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_WAP.name());
				}
				//如果商户仅开通了生活号支付，且预路由判定无法使用生活号支付，需直接展示错误页进行提示
				if(!checkPayTypesAvailable(view)){
					throw new CashierBusinessException(Errors.NO_PAYTYPE_AVAILABLE_ERROR);
				}
			} else {
				if (extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_WAP) || extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_LOW)) {
					// 非微信浏览器，微信h5，展示WECHAT_H5_WAP。当用户后续选择微信支付后，会进行高低配的路由判断。
					view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_WAP.name());
				}
			}
			setOrderInfo(info, view);

			//在线客服和常见问题是否显示
			view.addObject("customService", CommonUtil.wapShowCustomService(merchantNo, info.getOrderSysNo()));
			view.addObject("FAQ", CommonUtil.wapShowFAQ(merchantNo, info.getOrderSysNo()));

			//获取是否支持定制活动
			view.addObject("showActivity",ncCashierService.queryQualification4Activities());

			view.addObject("token", token);
			view.addObject("timeout", 1000 * CommonUtil.getMQListerTimeout());
			view.addObject("expireTime", extendInfo.getExpireTime());

			return view;

		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestNewWap_BusinException,token:" + token, e);
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
			logger.error("[monitor],event:nccashier_requestNewWap_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
	}

	/**
	 * 该接口只接受支付宝回调回来的结果
	 *
	 * @param token
	 * @param requestId
	 * @param merchantNo
	 * @param request
	 * @param wpayId
	 * @return
	 */
	@RequestMapping(value = "/stdAlipay", method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestNewWapForStdAlipay(String token, String requestId, String merchantNo,
			HttpServletRequest request, String wpayId) {
		logger.info("[monitor],event:支付宝h5标准版，支付宝授权回调后页面处理,token:{}", token);
		RequestInfoDTO info = null;
		try {
			BrowserType browserType = RequestUtils.getBrowserType(request);
			if (BrowserType.ALIPAY != browserType) {
				// 支付宝标准版要求在支付宝环境内才能调用支付处理器下单，获取tradeNo
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
			}
			info = checkRequestInfoDTO(token);
			String decryptRequestId = AESUtil.routeDecrypt(merchantNo, requestId);
			PayExtendInfo extendInfo = getPayExtendInfo(decryptRequestId, token); // 使用解密后的key
			ModelAndView view = new ModelAndView();
			view.addObject("token", token);
			if (extendInfo.containsPayType(PayTypeEnum.ALIPAY_H5_STANDARD)) {  // 开通了标准版
				if (DirectPayType.ALIPAY.name().equals(extendInfo.getDirectPayType())) {// 直连
					if (StringUtils.isBlank(wpayId) || ConstantUtil.WPAY_ID_NONE.equals(wpayId)) {
						// 直连模式下，获取到的支付宝生活号userId为空，抛异常
						throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
					}
					// 直连支付宝h5标准版的，直接下单
					WeChatPayRequestDTO requestDTO = buildWeChatPayRequestDTO(token, PayTypeEnum.ALIPAY_H5_STANDARD,
							wpayId, null, info);
					String result = ncCashierService.WeChatPay(requestDTO);
					view.addObject("tradeNO", result); // 这里的result是一个字符串tradeNO
					view.setViewName("alipayBlank"); // 支付宝标准版直连白页
					return view;
				}else { // 非直连
					// 非直连模式下，获取到的支付宝生活号的userId为空，也要展示支付宝，下单会报错
					view.addObject(ConstantUtil.ALIPAY_TYPE, PayTypeEnum.ALIPAY_H5_STANDARD.name());
					view.addObject("aliUserId", wpayId);
					// 从这个链接进来的，都认为使用支付宝标准版，因此其他支付宝的支付方式就不用考虑了。但是得兼容微信。
					setPropOfSelectedPageForStdAlipay(view, extendInfo, token, requestId, merchantNo);
					setOrderInfo(info, view);
					view.addObject("timeout", 1000 * CommonUtil.getMQListerTimeout());
					view.addObject("expireTime", extendInfo.getExpireTime());
					return view;
				}
			}else{ // 没有开通标准版，从这个链接进来就是有异常
				logger.error("requestNewWapForStdAlipay_商户优先不走支付宝标准版, merchantNo={}, token={}", merchantNo, token);
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);

			}
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestNewWap_BusinException,token:" + token, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(
						CommonUtil.getPreUrl(info.getMerchantNo(), request) + wapctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			} else {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
				return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestNewWap_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
	}

	/**
	 * 请求移动收银台非直连（提供支付工具选择页面）：专供微信h5低配版接收微信oauth回调后使用
	 * @param token
	 * @param requestId
	 * @param merchantNo
	 * @param request
	 * @param wpayId
	 * @return
	 */
	@RequestMapping(value = "/wechatlow", method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestNewWapForWechatLow(String token,String requestId, String merchantNo,HttpServletRequest request,String wpayId) {
		logger.info("[monitor],event:微信h5低配版，回调后页面处理,token:{}", token);
		RequestInfoDTO info = null;
		try {
			ModelAndView view = new ModelAndView();
			info = checkRequestInfoDTO(token);
			String encryptRequestId = "";
			if(StringUtils.isBlank(requestId)){
				//从商户重定向来的请求，不会传递参数requestId，直接从RequestInfoDTO中取即可
				requestId = Long.toString(info.getPaymentRequestId());
				encryptRequestId = AESUtil.routeEncrypt(merchantNo, requestId);
			}else {
				encryptRequestId = requestId;
				requestId = AESUtil.routeDecrypt(merchantNo, requestId);
			}
			String bankPayUrl = "/wap/request/"+merchantNo+"/"+encryptRequestId+"?token="+token+"&fromNewWAP=true";
			PayExtendInfo extendInfo = getPayExtendInfo(requestId,token);

			String ua = RequestUtils.getUserUA(request);
			boolean isWeChatBrowser = ua.indexOf("MicroMessenger")>-1;
			if (!isWeChatBrowser) {
				//此处必须是微信浏览器，否则报错
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
			}

			setOrderInfo(info, view);
			view.addObject("token", token);
			view.addObject("timeout", 1000 * CommonUtil.getMQListerTimeout());
			view.addObject("expireTime", extendInfo.getExpireTime());

			view.addObject("weChatPay", "NONE");
			if (extendInfo.containsPayType(PayTypeEnum.WECHAT_H5_LOW) && StringUtils.isNotBlank(wpayId)) {
				if (DirectPayType.WECHAT.name().equals(extendInfo.getDirectPayType())) {
					//直连微信h5低配版的，直接微信支付下单，并走空白页
					WeChatPayRequestDTO requestDTO = new WeChatPayRequestDTO();
					requestDTO.setTokenId(token);
					requestDTO.setRequestId(info.getPaymentRequestId());
					requestDTO.setRecordId(info.getPaymentRecordId() == null ? 0 : info.getPaymentRecordId());
					requestDTO.setPayType(PayTypeEnum.WECHAT_H5_LOW.name());
					requestDTO.setOpenId(wpayId);
					requestDTO.setAppId(extendInfo.getTargetAppId());
					String result = ncCashierService.WeChatPay(requestDTO);
					view.addObject("url", result);
					view.setViewName("wechatLow");
					return view;
				} else {
					//非直连
					view.addObject("weChatPay", PayTypeEnum.WECHAT_H5_LOW.name());
					view.addObject("wpayId", wpayId);
				}
			}

			String customizedMerchant = CommonUtil.getSysConfigFrom3G(ConstantUtil.WAP_CUSTOMIZED_MERCHANT_NO);
			if(StringUtils.isNotBlank(customizedMerchant) && customizedMerchant.contains(info.getMerchantNo())){
				//使用定制化wap收银台的商户，走新的定制化页面
				view.setViewName("newWapCustomized");
			}else {
				view.setViewName("newWap");
			}

			view.addObject("bankPayUrl", bankPayUrl);
			view.addObject("installment", extendInfo.containsCflPayTypes());
			view.addObject("bindCardPay", extendInfo.containsPayType(PayTypeEnum.BK_ZF));
			view.addObject("bankPay", extendInfo.containsPayType(PayTypeEnum.BANK_PAY_WAP));
			view.addObject("jdPay", extendInfo.containsPayType(PayTypeEnum.JD_H5));
			boolean openAccountPay = extendInfo.containsPayType(PayTypeEnum.ZF_ZHZF);
			view.addObject("showAccountPay", openAccountPay);
			if(openAccountPay){
				view.addObject("accountPayUrl", "/wap/accountpay/index?token=" + token);
			}
			boolean bcardInstallmentPay = extendInfo.containsPayType(PayTypeEnum.YHKFQ_ZF);
			view.addObject("bcardInstallmentPay", bcardInstallmentPay);
			view.addObject("bcardInstallmentUrl", bcardInstallmentPay ? "/bankinstallment/wap/index?token=" + token : null);
			// 担保分期是否可用
			boolean guaranteeInstallmentPay = extendInfo.containsPayType(PayTypeEnum.DBFQ_TL);
			view.addObject("guaranteeInstallmentPay", guaranteeInstallmentPay);
			view.addObject("guaranteeInstallmentUrl", guaranteeInstallmentPay ? "/guarantee/wap/index?token=" + token : null);



			if (extendInfo.containsPayType(PayTypeEnum.ALIPAY)) {
				// EWALLET下的ALIPAY（兼容历史配置）
				view.addObject("alipay", true);
			} else if (extendInfo.containsPayType(PayTypeEnum.ALIPAY_H5)) {
				// 当没有老的ALIPAY时，再去判断有无ALIPAY_H5
				// EWALLET_H5下的ALIPAY_H5（新的支付宝支付h5端配置方式)
				view.addObject("alipay_h5", true);
			}
			return view;

		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_requestNewWap_BusinException,token:" + token, e);
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
			logger.error("[monitor],event:nccashier_requestNewWap_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}

	}


	/**
	 * 支付宝生活号预路由
	 *
	 * @param token
	 * @param alipayUserId
	 * @param extendInfo
	 * @param info
	 * @param view
	 * @return null/授权的url
	 * @throws Throwable
	 */
	private String alipayLifeNoPayPreRoute(String token, String alipayUserId, PayExtendInfo extendInfo,
			RequestInfoDTO info, ModelAndView view) throws Throwable {

		if (StringUtils.isBlank(alipayUserId)) { // 还未授权
			JsapiRouteResponseDTO jsapiRouteResponseDTO = null;
			try{
				jsapiRouteResponseDTO = ncCashierService.getAppid(info.getPaymentRequestId() + "", PayTypeEnum.ZFB_SHH.name());
			}catch(Throwable t){
				logger.warn("支付宝生活号预路由异常, token=" + token, t);
			}
			if(jsapiRouteResponseDTO==null || !Constant.PRE_ROUTE_STATUS_SUCCESS.equals(jsapiRouteResponseDTO.getDealStatus())){
				return null;
			}
			if (StringUtils.isNotBlank(extendInfo.getOrigAlipayUserId())) {
				// 直接使用商户透传的userId支付
				view.addObject(ConstantUtil.ALIPAY_TYPE, PayTypeEnum.ZFB_SHH.name());
				view.addObject("aliUserId", extendInfo.getOrigAlipayUserId());
				return null;
			} else {
				// 做授权，获取userId
				return WaChatPayUtils.parseAlipayLifeNoAuth2Url(token, PayTypeEnum.ZFB_SHH);
			}

		}

		else if (!"none".equals(alipayUserId)) { // 已经授权，并且拿到userId
			view.addObject(ConstantUtil.ALIPAY_TYPE, PayTypeEnum.ZFB_SHH.name());
			view.addObject("aliUserId", alipayUserId);
		}

		return null;
	}

	/**
	 * 获取二维码
	 * @param token
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "getQrCode", method = {RequestMethod.POST, RequestMethod.GET})
	public void getQrCode(String token,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InputStream logoImage = getLogoImage(request);

		RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
		WeChatPayRequestDTO requestDTO = new WeChatPayRequestDTO();
		requestDTO.setTokenId(token);
		requestDTO.setRequestId(info.getPaymentRequestId());
		requestDTO.setRecordId(info.getPaymentRecordId()==null?0:info.getPaymentRecordId().longValue());
		requestDTO.setPayType(PayTypeEnum.WECHAT_ATIVE_SCAN.name());
		String url = ncCashierService.WeChatPay(requestDTO);
		BufferedImage bufImg = QrCodeUtil.encodeImage(url, IMAGE_SIZE, IMAGE_SIZE, logoImage);
		bufImg.flush();
		ImageIO.write(bufImg, "png", response.getOutputStream());
	}

	/**
	 * 监听支付结果（适用微信支付宝等第三方聚合支付）
	 * @param token
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/wechat/getMQNotifySignal", method = {RequestMethod.POST})
	public void getMQNotifySingal(String token,HttpServletRequest request,HttpServletResponse response) {
			logger.info("[monitor],event:getFrontNotifySingal,token:{}", token);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			RequestInfoDTO info = newWapPayService.validateRequestInfoDTO(token);
			String frontNotify = null;
			if(!CommonUtils.isPayProcess(info.getPaySysCode(),info.getTradeSysNo())){//非订单处理器订单，监听自己的redis
				frontNotify = RedisTemplate.getTargetFromRedis(Constant.FRONT_NOTIFY_KEY+token, String.class);
				long trys = CommonUtil.getMQListerTimeout();
				while(!"SUCCESS".equals(frontNotify) && trys>0){
					Thread.sleep(1000);
					trys--;
					frontNotify = RedisTemplate.getTargetFromRedis(Constant.FRONT_NOTIFY_KEY+token, String.class);
				}
			}else{//支付处理器订单，监听支付处理器的redis
				PayResultQueryStateListenVO listenResult = newWapPayService.listenCanPayResultQuery(token);
				if(listenResult.isQueryState()){
					frontNotify = "SUCCESS";
				}
			}

			Map<String, Object> json = new HashMap<String, Object>();
			json.put("status", frontNotify);
			out.println(JSON.toJSONString(json));
			out.flush();
		} catch (Throwable e) {
			logger.error("[monitor],event:getFrontNotifySingal_exception，token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ajaxErrorReturn(out, "failed", ex.getDefineCode(), ex.getMessage());
		} finally {
			out.close();
		}
	}

	/**
	 * 获取确认支付标识（适用微信支付宝等第三方聚合支付）
	 * @param token
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/wechat/getConfirmSignal", method = {RequestMethod.POST})
	public void getConfirmSignal(String token,HttpServletRequest request,HttpServletResponse response) {
			logger.info("[monitor],getConfirmSignal,token:{}", token);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}

			String confirmPay = RedisTemplate.getTargetFromRedis(Constant.CONFIRM_PAY_FLAG+token, String.class);

			Map<String, Object> json = new HashMap<String, Object>();
			json.put("status", confirmPay);
			out.println(JSON.toJSONString(json));
			out.flush();
		} catch (Throwable e) {
			logger.error("[monitor],event:getConfirmSignal_exception，token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ajaxErrorReturn(out, "failed", ex.getDefineCode(), ex.getMessage());
		} finally {
			out.close();
		}
	}

	/**
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private InputStream getLogoImage(HttpServletRequest request) throws IOException {
		if (logoImage == null) {
			int BUFFER_SIZE = 4096;
			InputStream inputStream = request.getSession().getServletContext().getResourceAsStream(LOGO_PATH);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int count = -1;
			while ((count = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1)
				outStream.write(buffer, 0, count);
			logoImage = outStream.toByteArray();
		}
		return new ByteArrayInputStream(logoImage);
	}

	/**
	 * <前置绑卡路由> 适用于绑卡支付和一键支付
	 * 绑卡列表不为空：跳转到前置绑卡页面（即快捷支付栏展示默认绑卡，有“更换”按钮，点击更换卡展示绑卡列表）
	 * 绑卡列表为空：跳转到原移动收银台页面（即快捷支付栏展示银行卡支付，无“更换”按钮） 
	 * 异常： 展示移动收银台页面
	 *
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/preBindRoute")
	public @ResponseBody void preBindRoute(String token, HttpServletResponse httpResponse) {
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
						//可用前置绑卡非空==>展示可用前置绑卡
						if (CollectionUtils.isEmpty(response.getBankCardDTO().getOtherCards())) {
							bankCardsToShow.add(response.getBankCardDTO());
						} else {
							bankCardsToShow.addAll(response.getBankCardDTO().getOtherCards());
						}
					} else if (response.getPassCardInfoDTO() != null && StringUtils.isNotEmpty(response.getPassCardInfoDTO().getCardNo())) {
						//可用前置卡为空，且有透传卡信息时：直接返回空的待展示列表
						ajaxResultWrite(httpResponse, res);
						return;
					}

					if (CollectionUtils.isNotEmpty(response.getUnuseBankCardDTO())) {
						//不可用前置绑卡列表非空==>增加展示不可用前置绑卡
						bankCardsToShow.addAll(response.getUnuseBankCardDTO());
					}
					if(!response.isAuthorized()){
						response.setShowChangeCard(true);
					}
					res.put("bankCardsToShow", bankCardsToShow);
					res.put("authorized", response.isAuthorized());
					res.put("showChangeCard", response.getShowChangeCard());

					if(ncCashierService.isPassBindId(info.getPaymentRequestId()))
						res.put("showChangeCard", false);
				}
			}
			ajaxResultWrite(httpResponse, res);
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_preBindRoute_exception,token:" + token, e);
			ajaxResultWrite(httpResponse, res);
		}
	}

	/**
	 * 跳转到支持银行及限额展示页面
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/toSupportBankPage")
	public Object toSupportBankPage(String token) {
		ModelAndView view = new ModelAndView();
		view.setViewName("supportBank");
		view.addObject("token", token);
		return view;
	}

	/**
	 * 查看支持的银行卡列表，来自商户配置中心
	 *
	 * @return
	 */
	@RequestMapping(value = "/supportbanklist")
	@ResponseBody
	public Object querySupportBankList(String token) {
		RequestInfoDTO info = checkRequestInfoDTO(token);
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			Map<String,List<BankLimitAmountResponseVO>> bankLimitAmountListResponseDTO = ncCashierService.querySupportBankListFromMerchantConfig(info.getPaymentRequestId() + "", info.getMerchantNo(), CashierVersionEnum.WAP.name());
			map.put("supportBanklist", bankLimitAmountListResponseDTO);
			return map;
		} catch (Throwable e) {
			map.put("bizStatus",AJAX_FAILED);
			return createErrorMsg(map,e,"supportbanklist_error",token, SYSTEM_CODE);
		}
	}

	/**
	 * 判断H5收银台上是否全部支付方式都不可用
	 *
	 * @param view
	 * @return
	 */
	private boolean checkPayTypesAvailable(ModelAndView view) {
		ModelMap modelMap = view.getModelMap();
		if (modelMap == null) {
			return false;
		}
		return "WECHAT_H5_WAP".equals(modelMap.get("weChatPay"))  // 微信H5
				|| "WECHAT_OPENID".equals(modelMap.get("weChatPay")) // 微信公众号
				|| (modelMap.get("alipay") != null && true == (Boolean) modelMap.get("alipay")) // 支付宝
				|| (modelMap.get("alipay_h5") != null && true == (Boolean) modelMap.get("alipay_h5")) // 支付宝H5
				|| (modelMap.get("installment") != null && true == (Boolean) modelMap.get("installment")) // 消费分期
				|| (modelMap.get("bankPay") != null && true == (Boolean) modelMap.get("bankPay")) // 一键支付
				|| (modelMap.get("showAccountPay") != null && true == (Boolean) modelMap.get("showAccountPay")) // 企业账户支付
				|| (modelMap.get("bcardInstallmentPay") != null && true == (Boolean) modelMap.get("bcardInstallmentPay")) // 银行卡分期
				|| (modelMap.get("bindCardPay") != null && true == (Boolean) modelMap.get("bindCardPay")) // 绑卡支付
				|| (modelMap.get("guaranteeInstallmentPay") != null && true == (Boolean) modelMap.get("guaranteeInstallmentPay")) //通联分期
				|| ((PayTypeEnum.ALIPAY_H5_STANDARD.name()).equals(modelMap.get(ConstantUtil.ALIPAY_TYPE))) // 支付宝标准版
				|| ((PayTypeEnum.ZFB_SHH.name()).equals(modelMap.get(ConstantUtil.ALIPAY_TYPE))); // 支付宝生活号
		// TODO 没有京东支付
	}

	/**
	 * 获取公众号二维码，展示在WAP查询页、成功页及失败页
	 * @return
	 */
	@RequestMapping(value = "/yeepay/qrcode")
	@ResponseBody
	public String getYeepayWechatQRCode(String merchantNo,String orderId,String token){
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
		}finally {
			logger.info("getYeepayWechatQRCode() 获取公众号二维码，入参token = {} , 获取到url = {}", token, url);
		}
	}

	/**
	 * 微信公众号支付，由业务方调用微信授权获取openId后，跳转到收银台
	 * @param token
	 * @param openid
	 * @param timestamp
	 * @param request
	 * @param sign
	 * @return
	 */
	@RequestMapping(value = "/openidredirect", method = {RequestMethod.POST, RequestMethod.GET})
	public Object openIdRequestFromBiz(String openid, String timestamp, String token, String sign, HttpServletRequest request) {
		logger.info("[monitor],event:微信公众号,由业务方获取openId,回调后页面处理,token:{}", token);
		RequestInfoDTO info = null;
		try {
			//1，参数校验
			if(StringUtils.isBlank(openid) || StringUtils.isBlank(timestamp)
					|| StringUtils.isBlank(token) || StringUtils.isBlank(sign)){
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			//2，验签
			StringBuffer textToSign = new StringBuffer("openid=");
			textToSign.append(openid).append("&timestamp=").append(timestamp)
					.append("&token=").append(token).append("&CASHIER");
			if (!MD5Util.signVerify(textToSign.toString(), sign)) {
				throw new CashierBusinessException(Errors.SIGN_ERROR);
			}
			//3，验证时间戳过期
			long distance = 0l;
			try {
				distance = System.currentTimeMillis() - Long.parseLong(timestamp);
			}catch (Throwable e){
				logger.error("转换时间戳异常timestamp="+timestamp);
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			if (distance > 60 * 1000) {
				throw new CashierBusinessException(Errors.TIMESTAMP_OUT_OF_EXPIRE_DATE);
			}
			//4，处理参数，并调用到requestNewWap
			info = checkRequestInfoDTO(token);
			//注：从商户重定向来的请求，不会传递参数requestId，直接从RequestInfoDTO中取即可
			String requestId = Long.toString(info.getPaymentRequestId());
			String merchantNo = info.getMerchantNo();
			String encryptRequestId = AESUtil.routeEncrypt(merchantNo, requestId);
			return requestNewWap(token,encryptRequestId,merchantNo,request,openid);
		} catch (Exception e) {
			logger.error("[monitor],event:微信公众号,由业务方获取openId,回调后页面处理,异常,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}
	}

	/**
	 * 用于模拟-微信公众号支付-跳转业务方或商户获取openid
	 * @param appid
	 * @param timestamp
	 * @param token
	 * @param sign
	 * @return
	 */
	@RequestMapping(value = "/testbizoauth")
	public Object testBizOauth(String appid,String timestamp,String token,String sign, HttpServletRequest request){
		logger.info("模拟定制业务方获取OpenId的流程--testbizoauth() 入参appid={},timestamp={},token={},sign={}",appid,timestamp,token,sign);
		String toWeChatUrl = null;
		try {
			StringBuffer textToSign = new StringBuffer("appid=");
			textToSign.append(appid).append("&timestamp=").append(timestamp)
					.append("&token=").append(token).append("&CASHIER");
			if (!MD5Util.signVerify(textToSign.toString(), sign)) {
				throw new CashierBusinessException(Errors.SIGN_ERROR);
			}

			if(StringUtils.isNotBlank(CommonUtil.mockOpenIdForBiz())){
				//若QA环境无法正常从微信授权回调，直接绕过微信授权接口，跳转到微信回调到的收银台接口
				RequestInfoDTO info = checkRequestInfoDTO(token);
				toWeChatUrl = CommonUtil.getPreUrl(info.getMerchantNo(),request) + "/sccanpay/auth2CallbackBiz?state="+token;
			}else {
				toWeChatUrl = WaChatPayUtils.toWechatAuth(appid, token);
				toWeChatUrl = toWeChatUrl.replace("auth2Callback", "auth2CallbackBiz");
			}
		} catch (UnsupportedEncodingException e) {
			logger.warn("模拟定制业务方获取OpenId的流程--testbizoauth() 异常=",e);
		}
		return new RedirectView(toWeChatUrl);
	}

	/**
	 * 微信H5标准版-使用传送门传送的接口地址-->传送到微信后，在此接口内，系统将拼接并跳转微信oauth授权接口，完成授权
	 * @return
	 */
	@RequestMapping(value = "/jumpwx")
	public Object wechatLowPreJump(String token,HttpServletRequest request) {
		logger.info("[monitor],event:wechatLowPreJump,token:{}", token);
		String toWechatAuthForH5Low = null;
		RequestInfoDTO info = null;
		try {
			info = checkRequestInfoDTO(token);
			/*	模拟微信oauth获取openid的流程 start*/
			if (StringUtils.isNotBlank(mockOpenidForWXH5Low(token, info.getMerchantNo(), request))) {
				//若商编配置了模拟的openid，直接跳转到微信h5标准版的支付工具选择页，跳过微信授权流程
				return new RedirectView(mockOpenidForWXH5Low(token, info.getMerchantNo(), request));
			}
			/*	模拟微信oauth获取openid的流程 end*/
			PayExtendInfo extendInfo = getPayExtendInfo(info.getPaymentRequestId() + "", token);
			if (StringUtils.isNotBlank(extendInfo.getAppSecert())) {
				toWechatAuthForH5Low = WaChatPayUtils.toWechatAuthForH5Low(extendInfo.getTargetAppId(), token);
			} else {
				//未获取到appSecret，拼接商户参数到微信oauth授权地址
				toWechatAuthForH5Low = WaChatPayUtils.toWechatAuthForH5Low(extendInfo.getTargetAppId(), token, info.getMerchantNo());
			}
			return new RedirectView(toWechatAuthForH5Low);
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_wechatLowPreJump_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
		}finally{
			//释放redis锁
			String userIp = getUserIp(request);
			String ua = getUserUA(request);
			String phone = WaChatPayUtils.getPhone(ua);
			WaChatPayUtils.releaseLock(userIp+"_"+phone);
		}
	}

	/**
	 * QA及内测环境，因回调地址问题，无法正常获取微信oauth回调，可读取配置的openid，跳过微信授权流程
	 * @return
	 */
	private String mockOpenidForWXH5Low(String token, String merchantNo, HttpServletRequest request) {
		String mockOpenid = CommonUtil.getWechatH5LowMockOpenid(merchantNo);
		if (StringUtils.isBlank(mockOpenid)) {
			return null;
		} else {
		    logger.info("mockOpenidForWXH5Low--微信h5标准版，获取模拟openid，merchantNo={},openid={},token={}",merchantNo,mockOpenid,token);
			String suffixUrl = "token=" + token + "&merchantNo=" + merchantNo + "&wpayId=" + mockOpenid;
			return CommonUtil.getPreUrl(merchantNo, request) + newwapctx + "/wechatlow?" + suffixUrl;
		}
	}
}
