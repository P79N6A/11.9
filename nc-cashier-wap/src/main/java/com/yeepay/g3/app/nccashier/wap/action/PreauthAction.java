package com.yeepay.g3.app.nccashier.wap.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.utils.Base64Util;
import com.yeepay.g3.app.nccashier.wap.utils.DataUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.vo.CardBinInfoResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardItemNecessary;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.BankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.BankCardReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.BankSupportDTO;
import com.yeepay.g3.facade.nccashier.dto.CardValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedValidatesDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 
 * @Description 预授权交易action
 * @author yangmin.peng
 * @since 2017年12月7日上午10:09:35
 */
@Controller
@RequestMapping(value = "/wap/preauth", method = { RequestMethod.POST, RequestMethod.GET })
public class PreauthAction extends WapBaseAction {
	private static final Logger logger = LoggerFactory.getLogger(PreauthAction.class);
	/**
	 * 本controller对应的错误码系统编码
	 */
	private static final SysCodeEnum SYSTEM_CODE = SysCodeEnum.NCCASHIER_WAP;

	/**
	 * 标准链接进来后跳转到预授权输卡号页
	 * 
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/routPayway")
	public Object preAuthRequestRoutPayway(String token) {
		logger.info("[monitor],event:nccashier_preAuth_routPayway,token:{}", token);
		RequestInfoDTO info = null;
		try {
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
						Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
			// 查询是否存在可用的银行
			List<BankSupportDTO> bankSupports = ncCashierService.supportBankList(info.getPaymentRequestId(),
					Constant.BNAK_RULE_CUSTYPE_PREAUTH);
			if (CollectionUtils.isEmpty(bankSupports)) {
				throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
			}
			BankCardReponseDTO response = ncCashierService.getBankCardInfo4Preauth(info.getPaymentRequestId(),
					Constant.BNAK_RULE_CUSTYPE_PREAUTH);
			List<BankCardDTO> useableBankCards = new ArrayList<BankCardDTO>();
			List<BankCardDTO> unuseableBankCards = new ArrayList<BankCardDTO>();
			boolean firstPreauthFlag = true;
			ModelAndView mv = new ModelAndView();
			mv.setViewName("pre-authorization");
			boolean showChangeCard = false;
			String useableBankCardsStr = null;
			String unuseableBankCardsStr = null;
			if (response != null) {
				if (response.getBankCardDTO() != null || CollectionUtils.isNotEmpty(response.getUnuseBankCardDTO())) {
					if (response.getBankCardDTO() != null) {
						// 可用绑卡列表非空
						if (CollectionUtils.isEmpty(response.getBankCardDTO().getOtherCards())) {
							useableBankCards.add(response.getBankCardDTO());
						} else {
							useableBankCards.addAll(response.getBankCardDTO().getOtherCards());
						}
					}
					if (CollectionUtils.isNotEmpty(response.getUnuseBankCardDTO())) {
						// 不可用绑卡列表非空
						unuseableBankCards.addAll(response.getUnuseBankCardDTO());
					}
					firstPreauthFlag = false;
					showChangeCard = response.getShowChangeCard();
					useableBankCardsStr = JSON.toJSONString(useableBankCards);
					unuseableBankCardsStr = JSON.toJSONString(unuseableBankCards);
				}
			}
			mv.addObject("useableBankCards", useableBankCardsStr);
			mv.addObject("unuseableBankCards", unuseableBankCardsStr);
			mv.addObject("showChangeCard", showChangeCard);
			mv.addObject("firstPreauthFlag", firstPreauthFlag);
			supplyBaseModelViewInfo4Preauth(info, token, mv,Constant.BNAK_RULE_CUSTYPE_PREAUTH);
			return mv;
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_preAuth_routPayway_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SYSTEM_CODE);
			ModelAndView errMv = createErrorMV(token, ex.getDefineCode(), ex.getMessage(), info);
			errMv.addObject("payTool", PayTool.YSQ);
			return errMv;
		}
	}

	/**
	 * 校验卡bin并获取必填项信息
	 * 
	 * @param token
	 * @param cardNo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ajax/validatecard")
	public void preAuthValidateCard(String token, String cardNo, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_preAuth_validateCard,token:{}", token);
		RequestInfoDTO info = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
						Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			cardNo = Base64Util.decode(cardNo);
			if (StringUtils.isEmpty(cardNo) && !DataUtil.isBankCardNum(cardNo)) {
				throw new CashierBusinessException(Errors.INVALID_BANK_CARD_NO.getCode(),
						Errors.INVALID_BANK_CARD_NO.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
			CardValidateRequestDTO requestDTO = new CardValidateRequestDTO();
			requestDTO.setCardno(cardNo);
			requestDTO.setRequestId(info.getPaymentRequestId());
			requestDTO.setCusType(Constant.BNAK_RULE_CUSTYPE_PREAUTH);
			NeedValidatesDTO validatesRes = ncCashierService.getCardValidates(requestDTO);
			buildPreAuthValidateCardResponse(result, validatesRes);
			result.put("token", token);
			result.put("bizStatus", AJAX_SUCCESS);
			ajaxResultWrite(response, result);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_preAuth_validateCard_exception,token:" + token, t);
			result.put("token", token);
			result.put("bizStatus", AJAX_FAILED);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SYSTEM_CODE);
			commonAjaxReturnNew(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	/**
	 * 预授权下单
	 * 
	 * @param token
	 * @param cardInfo
	 * @param cardBinInfo
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ajax/firstOrder")
	public void preAuthOrder(String token, CardInfoVO cardInfo, CardBinInfoResponseVO cardBinInfo,
			HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_preAuth_firstOrder,token:{}", token);
		RequestInfoDTO info = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
						Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			Base64Util.decryptCardInfoVO(cardInfo);
			cardInfo.validate();
			info = ncCashierService.requestBaseInfo(token);
			ReqSmsSendTypeEnum smsType = preauthService.preAuthOrderRequest(token, info, cardInfo, cardBinInfo);
			if (null != smsType) {
				result.put("smsType", smsType);
			}
			result.put("token", token);
			result.put("bizStatus", AJAX_SUCCESS);
			ajaxResultWrite(response, result);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_preAuth_firstOrder_exception,token:" + token, t);
			result.put("token", token);
			result.put("bizStatus", AJAX_FAILED);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SYSTEM_CODE);
			commonAjaxReturnNew(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	/**
	 * 预授权发短验
	 * 
	 * @param token
	 * @param smsType
	 * @param response
	 */
	@RequestMapping(value = "/ajax/smsSend")
	public void preAuthSmsSend(String token, ReqSmsSendTypeEnum smsType, CardInfoVO cardInfo,HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_preAuth_smsSend,token:{}", token);
		Map<String, Object> result = new HashMap<String, Object>();
		RequestInfoDTO info = null;
		try {
			if (StringUtils.isEmpty(token) || null == smsType) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
						Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			Base64Util.decryptCardInfoVO(cardInfo);
			cardInfo.validate();
			info = ncCashierService.requestBaseInfo(token);
			preauthService.preAuthSendSMS(token, info, cardInfo,smsType);
			result.put("token", token);
			result.put("bizStatus", AJAX_SUCCESS);
			ajaxResultWrite(response, result);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_preAuth_smsSend_exception,token:" + token, t);
			result.put("token", token);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SYSTEM_CODE);
			if ("N400093".equals(ex.getDefineCode())) {
				// 发短验次数超限，这笔订单无法再支付，跳到失败页
				result.put("bizStatus", AJAX_FAILED);
			} else {
				result.put("bizStatus", SMS_FAILD);
			}
			commonAjaxReturnNew(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	@RequestMapping(value = "/ajax/firstConfirm")
	public void preAuthConfirm(String token, String verifyCode, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_preAuth_Confirm,token:{}", token);
		Map<String, Object> result = new HashMap<String, Object>();
		RequestInfoDTO info = null;
		try {
			if (StringUtils.isEmpty(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
						Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
			preauthService.preAuthOrderConfirm(token, info, verifyCode);
			result.put("token", token);
			result.put("bizStatus", AJAX_SUCCESS);
			ajaxResultWrite(response, result);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_preAuth_confirm_exception,token:" + token, t);
			result.put("token", token);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SYSTEM_CODE);
			if ("N400094".equals(ex.getDefineCode()) || "N400091".equals(ex.getDefineCode())
					|| "N9003016".equals(ex.getDefineCode())) {
				result.put("bizStatus", SMS_FAILD);
			}else{
				result.put("bizStatus", AJAX_FAILED);
			}
			commonAjaxReturnNew(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	@RequestMapping(value = "/ajax/bindOrder")
	public void preAuthBindOrder(String token, String bindId, HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_preAuth_bindOrder,token:{}", token);
		RequestInfoDTO info = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(token) || StringUtils.isBlank(bindId)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
						Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			info = ncCashierService.requestBaseInfo(token);
			CashierPaymentResponseDTO preauthBindResponse = preauthService.preAuthBindOrderRequest(token, bindId, info);
			buildOrderNeedItem(result, preauthBindResponse);
			result.put("token", token);
			result.put("bizStatus", AJAX_SUCCESS);
			ajaxResultWrite(response, result);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_preAuth_bindOrder_exception,token:" + token, t);
			result.put("token", token);
			result.put("bizStatus", AJAX_FAILED);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SYSTEM_CODE);
			commonAjaxReturnNew(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	@RequestMapping(value = "/ajax/bindConfirm")
	public void preAuthBindOrderConfirm(String token, String verifyCode, CardInfoVO cardInfo,
			HttpServletResponse response) {
		logger.info("[monitor],event:nccashier_preAuth_bindConfirm,token:{}", token);
		RequestInfoDTO info = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(token)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(),
						Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
			}
			Base64Util.decryptCardInfoVO(cardInfo);
			cardInfo.validate();
			info = ncCashierService.requestBaseInfo(token);
			preauthService.preAuthBindOrderConfirm(token, info, verifyCode, cardInfo);
			result.put("token", token);
			result.put("bizStatus", AJAX_SUCCESS);
			ajaxResultWrite(response, result);
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_preAuth_bindConfirm_exception,token:" + token, t);
			result.put("token", token);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SYSTEM_CODE);
			if ("N400094".equals(ex.getDefineCode()) || "N400091".equals(ex.getDefineCode())
					|| "N9003016".equals(ex.getDefineCode())) {
				result.put("bizStatus", SMS_FAILD);
			}else{
				result.put("bizStatus", AJAX_FAILED);
			}
			commonAjaxReturnNew(response, result, ex.getDefineCode(), ex.getMessage());
		}
	}

	private void buildOrderNeedItem(Map<String, Object> result, CashierPaymentResponseDTO preauthBindResponse) {
		NeedBankCardDTO needBankCardDTO = preauthBindResponse.getNeedBankCardDto();
		if (null != needBankCardDTO && null != needBankCardDTO.getNeedSurportDTO()) {
			NeedSurportDTO needSurportDTO = needBankCardDTO.getNeedSurportDTO();
			CardItemNecessary cardItemNecessary = new CardItemNecessary();
			cardItemNecessary.setNeedAvlidDate(needSurportDTO.getAvlidDateIsNeed());
			cardItemNecessary.setNeedBankPWD(needSurportDTO.getBankPWDIsNeed());
			cardItemNecessary.setNeedCvv(needSurportDTO.getCvvIsNeed());
			cardItemNecessary.setNeedIdNo(needSurportDTO.getIdnoIsNeed());
			cardItemNecessary.setNeedOwner(needSurportDTO.getOwnerIsNeed());
			cardItemNecessary.setNeedPhoneNo(needSurportDTO.getPhoneNoIsNeed());
			CardInfoVO cardInfo = new CardInfoVO();
			cardInfo.setAvlidDate(needBankCardDTO.getAvlidDate());
			cardInfo.setCardNo(needBankCardDTO.getCardno());
			cardInfo.setCvv(needBankCardDTO.getCvv());
			cardInfo.setIdNo(needBankCardDTO.getIdno());
			cardInfo.setOwner(needBankCardDTO.getOwner());
			cardInfo.setPhoneNo(needBankCardDTO.getPhoneNo());
			result.put("autoFillItem", cardInfo);
			result.put("requiredCardItem", cardItemNecessary);
		}
		if (null != preauthBindResponse.getReqSmsSendTypeEnum()) {
			result.put("smsType", preauthBindResponse.getReqSmsSendTypeEnum().name());
		}
	}

	private void buildPreAuthValidateCardResponse(Map<String, Object> result, NeedValidatesDTO validatesRes) {
		CardBinInfoResponseVO cardBinInfo = new CardBinInfoResponseVO();
		CardInfoVO cardInfo = new CardInfoVO();
		if (null != validatesRes.getBankCard()) {
			cardBinInfo.setBankCode(validatesRes.getBankCard().getBankCode());
			cardBinInfo.setBankName(validatesRes.getBankCard().getBankName());
			cardBinInfo.setCardLater4(validatesRes.getBankCard().getCardlater());
			cardBinInfo.setCardType(validatesRes.getBankCard().getCardtype().name());

			String owner = validatesRes.getBankCard().getOwner();
			if (StringUtils.isNotBlank(owner)) {
				String name = HiddenCode.hiddenName(owner);
				cardInfo.setOwner(name);
			}
			String idno = validatesRes.getBankCard().getIdno();
			if (StringUtils.isNotBlank(idno)) {
				String idNO = HiddenCode.hiddenIdentityCode(idno);
				cardInfo.setIdNo(idNO);
			}
			String phoneNo = validatesRes.getBankCard().getPhoneNo();
			if (StringUtils.isNotBlank(phoneNo)) {
				String pn = HiddenCode.hiddenMobile(phoneNo);
				cardInfo.setPhoneNo(pn);
			}
		}
		CardItemNecessary cardItemNecessary = new CardItemNecessary();
		cardItemNecessary.setNeedAvlidDate(validatesRes.isNeedValid());
		cardItemNecessary.setNeedBankPWD(validatesRes.isNeedPass());
		cardItemNecessary.setNeedCvv(validatesRes.isNeedCVV());
		cardItemNecessary.setNeedIdNo(validatesRes.isNeedIdno());
		cardItemNecessary.setNeedOwner(validatesRes.isNeedName());
		cardItemNecessary.setNeedPhoneNo(validatesRes.isNeedMobile());
		cardItemNecessary.setMerchantSamePersonConf(validatesRes.isMerchantSamePersonConfig());
		result.put("cardBinInfo", cardBinInfo);
		result.put("autoFillItem", cardInfo);
		result.put("requiredCardItem", cardItemNecessary);
	}
}
