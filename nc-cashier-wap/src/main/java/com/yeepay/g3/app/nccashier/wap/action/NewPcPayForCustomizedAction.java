package com.yeepay.g3.app.nccashier.wap.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yeepay.g3.app.nccashier.wap.utils.Base64Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.utils.DataUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.vo.EBankSupportBanksVO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.BankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.BankCardReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.BussinessTypeResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardBinInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.PassCardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.BusinessTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 
 * @Description 收银台模版定制化PC入口
 * @author yangmin.peng
 * @since 2017年6月16日下午5:46:37
 */
@Controller
@RequestMapping(value = "/newpc", method = { RequestMethod.POST, RequestMethod.GET })
public class NewPcPayForCustomizedAction extends WapBaseAction {

	private static final Logger logger = LoggerFactory.getLogger(NewPcPayForCustomizedAction.class);

	/**
	 * 本controller对应的系统编码
	 */
	private static final SysCodeEnum SYSTEM_CODE = SysCodeEnum.NCCASHIER_PC;


	/**
	 * 获取可用的绑卡列表，并判断是否可以更换卡
	 *
	 * @param token
	 */
	@RequestMapping(value = "/customeized/bindcardlist", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void routeBindCardList(String token, HttpServletResponse httpResponse) {
		Map<String, Object> res = getBindCardList(token);
		ajaxResultWrite(httpResponse, res);
	}

	/**
	 * 商户定制化收银台newpc 的统一路由入口
	 * 
	 * @param request
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/routpaywayCustomized", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object requestProcess(String token) {
		RequestInfoDTO info = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			info = checkRequestInfoDTO(token);
			logger.info("[monitor],event:nccashier_routpaywayCustomized_request,requestId:{},merchantNo:{}",
					info.getPaymentRequestId(), info.getMerchantNo());
			BussinessTypeResponseDTO bussinessTypeResponseDTO = ncCashierService
					.routerPayWay(info.getPaymentRequestId());
			// 如果是首付支付或者是首次透传非卡号信息跳转到输入卡号页，如果是首次透传卡号信息
			if (BusinessTypeEnum.FIRSTPAY == bussinessTypeResponseDTO.getBusinessTypeEnum()
					|| BusinessTypeEnum.FIRSTPASS == bussinessTypeResponseDTO.getBusinessTypeEnum()) {
				//前端调用requestFirst
				result.put("ncpayType", "firstPay");
			} else if (BusinessTypeEnum.FIRSTPASSCARDNO == bussinessTypeResponseDTO.getBusinessTypeEnum()) {
				//前端调用passcardInfo;
				result.put("ncpayType", "firstPass");
			} else {
				//前端调用requestBind;
				result.put("ncpayType", "bindPay");
			}
			return JSON.toJSONString(result);
		} catch (Throwable e) {
			return createErrorMsg(null, e, "routpaywayCustomized_error", token, SYSTEM_CODE);
		}
	}

	/**
	 * 跳转至首次输入卡号页面
	 *
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/requestCustomized/first", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object requestFirst(String token, HttpServletRequest request) {
		logger.info("[monitor],event:nccashier_requestCustomized_request,token:{}", token);
		// ModelAndView mv = new ModelAndView();
		Map<String, String> result = new HashMap<String, String>();
		try {
			// 跳转到首次支付有可能是来自绑卡支付的新增银行卡支付,如果是来自绑卡支付的换卡支付，增加标示(返回已经有银行卡支付)
			String isBindCardChangeCard = request.getParameter("isBindCardChangeCard");
			if ("isBindCardChangeCard".equals(isBindCardChangeCard)) {
				// mv.addObject("isBindCardChangeCard", isBindCardChangeCard);
				result.put("isBindCardChangeCard", isBindCardChangeCard);
			}
			checkRequestInfoDTO(token);
			// mv.setViewName("newpc/bind_first_pay");
			return JSON.toJSONString(result);
		} catch (Throwable e) {
			return createErrorMsg(null, e, "request_customized_first_error", token, SYSTEM_CODE);
		}
	}

	/**
	 * 校验输入的卡号以及获取卡bin信息(透传卡号)
	 * 
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/requestCustomized/passcardno")
	@ResponseBody
	public Object passcardInfo(String token) {
		logger.info("[monitor],event:nccashier_requestCustomized_passcard,token:{}", token);
		RequestInfoDTO info = null;
		// ModelAndView mv = new ModelAndView();
		Map<String, Object> result = new HashMap<String, Object>();
		String cardNo = null;
		try {
			info = checkRequestInfoDTO(token);
			// 获取透传的卡信息和用户信息
			cardNo = getValidates(info, null, result);
			if (StringUtils.isEmpty(cardNo) || !DataUtil.isBankCardNum(cardNo)) {
				// mv.addObject("cardNo", cardNo);
				result.put("cardNo", cardNo);
				throw new CashierBusinessException(Errors.INVALID_BANK_CARD_NO.getCode(),
						Errors.INVALID_BANK_CARD_NO.getMsg());
			} else {
				// mv.setViewName("newpc/pass_cardno");
				return JSON.toJSONString(result);
			}
		} catch (Throwable e) {
			return createErrorMsg(null, e, "requestCustomized_passcardno_error", token, SYSTEM_CODE);
		}
	}

	/**
	 * 跳转到绑卡支付页面
	 *
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/requestCustomized/bind", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object requestBind(String token) {
		logger.info("[monitor],event:nccashier_requestCustomized_bind,token:{}", token);
		RequestInfoDTO info = null;
		List<BankCardDTO> bindCards = new ArrayList<BankCardDTO>();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			info = checkRequestInfoDTO(token);
			BankCardReponseDTO response = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
			BankCardDTO bindCardDTO = response.getBankCardDTO();
			PassCardInfoDTO passCardInfoDTO = response.getPassCardInfoDTO();
			if (bindCardDTO == null) {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
			} else {
				boolean bindTouchuanIsCardNo = false;
				// ModelAndView view = new ModelAndView();
				if (null != passCardInfoDTO && StringUtils.isNotBlank(passCardInfoDTO.getCardNo())) {
					bindTouchuanIsCardNo = true;
					bindCards.add(response.getBankCardDTO());
				} else {
					bindCards = bindCardDTO.getOtherCards();
				}

				// 如果绑卡没有投传卡号，并且也可以显示新增卡支付
				if (!bindTouchuanIsCardNo && response.getShowChangeCard()) {
					// view.addObject("showChangeCard", true);
					result.put("showChangeCard", true);
				} else {
					// 如果授权了，则不能显示新增卡支付，否则可以显示新增卡支付
					result.put("showChangeCard", !response.isAuthorized());
				}
				result.put("isNotFirst", true);
				result.put("authorized", response.isAuthorized());
				if (CollectionUtils.isNotEmpty(response.getUnuseBankCardDTO())) {
					bindCards.addAll(response.getUnuseBankCardDTO());
				}
				result.put("bindCards", bindCards);
				//易订货系统商维度解绑
				if(newWapPayService.checkAbleToUnbindCard(info.getParentMerchantNo())){
					result.put("unBindCard", "true");
				}

				if(ncCashierService.isPassBindId(info.getPaymentRequestId()))
					result.put("showChangeCard", false);

				return JSON.toJSONString(result);
			}
		} catch (Throwable e) {
			return createErrorMsg(null, e, "requestCustomized_bind_error", token, SYSTEM_CODE);
		}
	}

	/**
	 * 校验输入的卡号以及获取卡bin信息(无透传卡号) 该action可能是来自绑卡的新增卡支付
	 *
	 * @param cardno
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/requestCustomized/notpasscardno")
	@ResponseBody
	public Object cardInfo(String cardno, String token, HttpServletRequest request, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_notpasscardno_requestCustomized,token:{}", token);
		RequestInfoDTO info = null;
		// ModelAndView modelAndView = new ModelAndView();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			cardno = Base64Util.decode(cardno);
			info = checkRequestInfoDTO(token);
			if (StringUtils.isNotEmpty(cardno)) {
				cardno = cardno.replaceAll(" ", "");
			}
			if (StringUtils.isEmpty(cardno) || !DataUtil.isBankCardNum(cardno)) {
				result.put("cardBinStatus", AJAX_FAILED);
				result.put("errorcode", Errors.INVALID_BANK_CARD_NO.getCode());
				result.put("errormsg",
						ExceptionUtil.handleException(Errors.INVALID_BANK_CARD_NO, SYSTEM_CODE).getMessage());
				return JSON.toJSONString(result);
			}
			// 获取卡bin信息，如果该卡不支持会返回FAILED
			CardBinInfoDTO cardBinInfoDTO = ncCashierService.getCardBinInfo(cardno);
			if (cardBinInfoDTO.getProcessStatusEnum() == ProcessStatusEnum.FAILED) {
				result.put("cardBinStatus", AJAX_FAILED);
				result.put("errorcode", cardBinInfoDTO.getReturnCode());
				result.put("errormsg", cardBinInfoDTO.getReturnMsg());
				return JSON.toJSONString(result);
			} else {
				// modelAndView.setViewName("newpc/cardbin_item");
				result.put("cardBinStatus", AJAX_SUCCESS);
				result.put("cardNo", cardno);
				result.put("cardType", cardBinInfoDTO.getCardTypeEnum());
				result.put("bankCode", cardBinInfoDTO.getBank());
				result.put("bankName", cardBinInfoDTO.getName());
				result.put("cardlater", cardno.substring(cardno.length() - 4, cardno.length()));
				// 如果是从绑卡支付跳转过来的新增卡支付(首次支付),清掉record
				if ("isBindCardChangeCard".equals(request.getParameter("isBindCardChangeCard"))) {
					ncCashierService.clearRecordId(token);
				}
				getValidates(info, cardno, result);
				result.put("validateStatus", AJAX_SUCCESS);
				return JSON.toJSONString(result);
			}
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_cardInfo_requestCustomized_BusinException,token:" + token, e);
			result.put("validateStatus", AJAX_FAILED);
			return createErrorMsg(result, e, "requestCustomized_notpasscardno_error", token, SYSTEM_CODE);
		} catch (Throwable e) {
			result.put("cardBinStatus", AJAX_FAILED);
			result.put("validateStatus", AJAX_FAILED);
			return createErrorMsg(result, e, "requestCustomized_notpasscardno_error", token, SYSTEM_CODE);
		}
	}

	/**
	 * 绑卡支付下单AJAX
	 * 
	 * @param token
	 * @param bindId
	 * @throws IOException
	 */
	@RequestMapping(value = "/bindpayCustomized/requestPayment", method = { RequestMethod.POST })
	@ResponseBody
	public Object requestPayment(String token, String bindId) throws IOException {
		logger.info("[monitor],event:nccashier_requestPayment_bindpayCustomized,token:{},bindId:{}", token, bindId);
		RequestInfoDTO info = null;
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> payment = new HashMap<String, Object>();
		try {
			info = checkRequestInfoDTO(token);
			BankCardReponseDTO bankCardResponse = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
			BankCardDTO bindCardDTO = bankCardResponse.getBankCardDTO();
			if (bindCardDTO == null) {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
			} else {
				payment = newWapPayService.createPayment(token, bindId, info, bindCardDTO);
				if (payment.get("smsType") != null) {
					result.put("smsType", payment.get("smsType"));
				}
				result.put("needBankCardDTO", payment.get("needBankCardDTO"));
				// modelAndView.setViewName("newpc/bind_item");
				return JSON.toJSONString(result);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestPayment_bindpayCustomized_exception，token:{}", token, e);
			result.put("bizStatus", AJAX_FAILED);
			return createErrorMsg(result, e, "bindpay_requestPayment_error", token, SYSTEM_CODE);
		}
	}

	/**
	 * PC网银支付首页模块
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/ebankCustomized/index")
	@ResponseBody
	public Object eBankIndex(String token /* ,String compatibleView */) {
		
		logger.info("[monitor],event:nccashier_eBankCustomizedIndex_request,token:{}", token);
		try {
			EBankSupportBanksVO response = eBankPayService.ebankIndexShow(token);
			return JSON.toJSONString(response);
		} catch (Throwable e) {
			return createErrorMsg(null, e, "ebank_index_error", token, SYSTEM_CODE);
		}

	}
	
	/**
	 * PC收银台扫码支付支持的具体的支付方式（微信、支付宝、分期、银联）
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/scanPayToolsSupportCustomized")
	@ResponseBody
	public Object scanPayToolsSupport(String token){
		logger.info("[monitor],event:nccashier_scanPayToolsSupport,token:{}", token);
		RequestInfoDTO info = null;
		Map<String, Object> result = new HashMap<String, Object>();
        try {
            info = checkRequestInfoDTO(token);
            PayExtendInfo payExtendInfo = ncCashierService.getPayExtendInfo(info.getPaymentRequestId(),token);
            //商户开通的支付工具
            String[] payTools = payExtendInfo.getPayTool();
            // 微信主扫
            result.put("wechatLogo", payExtendInfo.containsPayType(PayTypeEnum.WECHAT_ATIVE_SCAN));
            // 支付宝主扫
            	result.put("alipayLogo", payExtendInfo.containsPayType(PayTypeEnum.ALIPAY));
            // 银联主扫
            	result.put("unionLogo", payExtendInfo.containsPayType(PayTypeEnum.UPOP_ATIVE_SCAN));
			// 京东主扫
			result.put("jdLogo", payExtendInfo.containsPayType(PayTypeEnum.JD_ATIVE_SCAN));
			// QQ钱包主扫
			result.put("qqLogo", payExtendInfo.containsPayType(PayTypeEnum.QQ_ATIVE_SCAN));
			// 快捷扫码
			result.put("yjzfLogo", payExtendInfo.containsPayType(PayTypeEnum.NC_ATIVE_SCAN));
			// 消费分期
            List<String> payToolsList = Arrays.asList(payTools);
            	result.put("installment", payToolsList.contains(PayTool.CFL.name()));
			return JSON.toJSONString(result);
		} catch (Throwable e) {
			return createErrorMsg(null, e, "scanPayToolsSupport_error", token, SYSTEM_CODE);
		}
	}
	/**
	 * 校验该卡是否符合商户配置
	 * 
	 * @param info
	 * @param cardNo
	 */
	private String getValidates(RequestInfoDTO info, String cardNo, Map<String, Object> result) {
		BankCardReponseDTO bankCardReponseDTO = ncCashierService.getBankCardInfo(info.getPaymentRequestId(),Constant.BNAK_RULE_CUSTYPE_SALE);
		BankCardDTO bindCardDTO = bankCardReponseDTO.getBankCardDTO();
		// 获取透传卡信息
		PassCardInfoDTO passCardInfoDTO = bankCardReponseDTO.getPassCardInfoDTO();
		if (bankCardReponseDTO.getBusinessTypeEnum() == BusinessTypeEnum.FIRSTPASSCARDNO && passCardInfoDTO != null) {
			if (StringUtils.isNotBlank(passCardInfoDTO.getCardNo())) {
				cardNo = passCardInfoDTO.getCardNo();
			}
		}
		// 是否显示换其他卡支付
		result.put("showChangeCard", bankCardReponseDTO.getShowChangeCard());

		// 自动绑卡的文案 前端不取
		result.put("autoBindText", bankCardReponseDTO.getAutoBindTipText());
		result.put("autoBindTip", bankCardReponseDTO.getAutoBindTipText());

		Map<String, Object> model = newWapPayService.getFirstPayModel(info.getPaymentRequestId(), cardNo,
				bankCardReponseDTO.getBusinessTypeEnum(), bindCardDTO);
		if (null != model.get("bc")) {
			BankCardDTO bc = (BankCardDTO) model.get("bc");
			result.put("bankCode", bc.getBankCode());
			result.put("bankName", bc.getBankName());
			result.put("cardlater", bc.getCardlater());
			result.put("cardtype", bc.getCardtype());
			result.put("cardNo", bc.getCardno());
			if (bc.getBankQuata() != null) {
				result.put("dayQuotaDou", bc.getBankQuata().getDayQuotaDou());// 日限额
				result.put("monthQuotaDou", bc.getBankQuata().getMonthQuotaDou());// 月限额
				result.put("orderQuotaDou", bc.getBankQuata().getOrderQuotaDou());// 单比限额
			}
		}
		for (String key : model.keySet()) {
			result.put(key, model.get(key));
		}
		return cardNo;
	}
}