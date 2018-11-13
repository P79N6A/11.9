package com.yeepay.g3.app.nccashier.wap.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.service.BankInstallmentService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.app.nccashier.wap.utils.Base64Util;
import com.yeepay.g3.app.nccashier.wap.utils.DataUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.BasicResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardNoOrderResVO;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentAmountInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.UrlInfoVO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationIdOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.UrlInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 银行卡分期controller
 * 
 * @author duangduang
 */
@Controller
@RequestMapping(value = "/bankinstallment", method = { RequestMethod.POST, RequestMethod.GET })
public class BankInstallmentAction extends WapBaseAction {

	private static Logger logger = LoggerFactory.getLogger(BankInstallmentAction.class);

	@Resource
	private NewWapPayService newWapPayService;

	@Resource
	private NcCashierService ncCashierService;

	@Resource
	private BankInstallmentService bankInstallmentService;

	private void openBankInstallment(long requestId, String token) {
		PayExtendInfo payExtendInfo = ncCashierService.getPayExtendInfo(requestId, token);
		if (!payExtendInfo.containsPayType(PayTypeEnum.YHKFQ_ZF)) {
			throw new CashierBusinessException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}
	}

	/**
	 * H5银行卡分期页面跳转
	 * 
	 * @param token
	 */
	@RequestMapping(value = "/wap/index")
	public ModelAndView routeInstallmentIndex(String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("bank_card_stage"); // 银行卡分期主页
		mv.addObject("token", token);
		RequestInfoDTO info = null;
		try {
			info = newWapPayService.validateRequestInfoDTO(token);
			openBankInstallment(info.getPaymentRequestId(), token);
			InstallmentBankResponseVO response = bankInstallmentService.routePayWay(info.getPaymentRequestId());
			mv.addObject("mode", response.getMode());
			mv.addObject("usableBankList", CollectionUtils.isEmpty(response.getUsableBankList()) ? null
					: JSON.toJSONString(response.getUsableBankList()));
			mv.addObject("periodList", MapUtils.isEmpty(response.getPeriodListOfBank()) ? null
					: JSON.toJSONString(response.getPeriodListOfBank()));
			mv.addObject("usableSignRelationList", CollectionUtils.isEmpty(response.getUsableSignRelationList()) ? null
					: JSON.toJSONString(response.getUsableSignRelationList()));
			mv.addObject("unusableSignRelationList", CollectionUtils.isEmpty(response.getUnusableSignRelationList())
					? null : JSON.toJSONString(response.getUnusableSignRelationList()));
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_routeInstallmentIndex异常,token:" + token, t);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER_WAP);
			if(info==null){
				return createErrorMV(token,Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg(),null);
			}
			String repayUrl = getH5IndexUrl(info.getPaymentRequestId(), info.getMerchantNo(), token, request,null);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), null, "pay_fail", repayUrl);
		}
		return mv;
	}

	/**
	 * 用户点击换卡支付
	 * 
	 * @param token
	 */
	@RequestMapping(value = "/wap/changecard")
	public ModelAndView changeCardToPay(String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("bank_card_stage"); // 银行卡分期主页
		mv.addObject("mode", "NEW"); // 标识此时要展示输入卡号页
		mv.addObject("token", token);
		RequestInfoDTO info = null;
		try {
			info = newWapPayService.validateRequestInfoDTO(token);
			openBankInstallment(info.getPaymentRequestId(), token);
			InstallmentBankResponseVO response = bankInstallmentService.getUsableBankList(info.getPaymentRequestId());
			mv.addObject("usableBankList", JSON.toJSONString(response.getUsableBankList()));
			mv.addObject("periodList", MapUtils.isEmpty(response.getPeriodListOfBank()) ? null
					: JSON.toJSONString(response.getPeriodListOfBank()));
		} catch (Throwable t) {
			logger.error("[monitor],event:nccashier_changeCardToPay异常,token:" + token, t);
			CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER_WAP);
			if(info==null){
				return createErrorMV(token,Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg(),null);
			}
			String repayUrl = getH5IndexUrl(info.getPaymentRequestId(), info.getMerchantNo(), token, request,null);
			return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), null, "pay_fail", repayUrl);
		}
		return mv;
	}

	/**
	 * 获取某个银行下某期对应的手续费、首期还款等相关信息
	 * 
	 * @param bankCode
	 * @param number
	 * @param token
	 */
	@RequestMapping(value = "/ajax/rateinfo")
	@ResponseBody
	public void getInstallmentInfoByBankCode(InstallmentBankRequestVO requestVO,
			HttpServletResponse httpServletResponse, HttpServletRequest request) {
		RequestInfoDTO info = null;
		InstallmentAmountInfoVO amountInfoVO = new InstallmentAmountInfoVO();
		try {
			if (requestVO == null || StringUtils.isBlank(requestVO.getToken())
					|| StringUtils.isBlank(requestVO.getBankCode()) || StringUtils.isBlank(requestVO.getPeriod())) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			info = newWapPayService.validateRequestInfoDTO(requestVO.getToken());
			amountInfoVO = bankInstallmentService.getRateInfo(info, requestVO);
			amountInfoVO.setBizStatus(AJAX_SUCCESS);
		} catch (Throwable t) {
			CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
			amountInfoVO.setErrorcode(ex.getDefineCode());
			amountInfoVO.setErrormsg(ex.getMessage());
			String repayUrl = getH5IndexUrl(info.getPaymentRequestId(), info.getMerchantNo(),
					requestVO == null ? null : requestVO.getToken(), request,null);
			amountInfoVO.setRepayurl(repayUrl);
			amountInfoVO.setBizStatus(AJAX_FAILED);
		}
		ajaxResultWrite(httpServletResponse, amountInfoVO);
	}

	/**
	 * 卡号下单接口
	 * 
	 * @param cardNo
	 * @param token
	 */
	@RequestMapping(value = "/ajax/cardno/order")
	@ResponseBody
	public void orderByCardNo(String bankCode, String period, String cardNo, String token,
			HttpServletResponse httpServletResponse, HttpServletRequest request) {
		CardNoOrderResVO resVo = new CardNoOrderResVO();
		resVo.setBizStatus(AJAX_SUCCESS);
		resVo.setToken(token);
		RequestInfoDTO info = null;
		try {
			cardNo = Base64Util.decode(cardNo);
			if (StringUtils.isEmpty(cardNo) || !DataUtil.isBankCardNum(cardNo)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			info = newWapPayService.validateRequestInfoDTO(token);
			openBankInstallment(info.getPaymentRequestId(), token);
			CardNoOrderRequestDTO requestDTO = buildCardNoOrderRequestDTO(bankCode, period, cardNo,
					info.getPaymentRequestId(), token);
			CardNoOrderResponseDTO response = ncCashierService.orderByCardNo(requestDTO);
			if (response.getUrlInfo() != null) {
				resVo.setCardState("UNSIGNED");
				resVo.setUrlInfo(buildUrlInfoVO(response.getUrlInfo()));
			} else {
				resVo.setCardState("SIGNED");
			}
		} catch (Throwable t) {
			CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
			resVo.setErrorcode(ex.getDefineCode());
			resVo.setErrormsg(ex.getMessage());
			resVo.setBizStatus(AJAX_FAILED);
		}
		ajaxResultWrite(httpServletResponse, resVo);
	}

	public UrlInfoVO buildUrlInfoVO(UrlInfoDTO urlInfo) {
		UrlInfoVO urlInfoVO = new UrlInfoVO();
		urlInfoVO.setCharset(urlInfo.getCharset());
		urlInfoVO.setMethod(urlInfo.getMethod());
		urlInfoVO.setParams(urlInfo.getParams());
		urlInfoVO.setUrl(urlInfo.getUrl());
		return urlInfoVO;
	}

	public SignRelationIdOrderRequestDTO buildSignRelationIdOrderRequestDTO(String bankCode, String period,
			String signRelationId, long requestId, String token) {
		SignRelationIdOrderRequestDTO requestDTO = new SignRelationIdOrderRequestDTO();
		requestDTO.setBankCode(bankCode);
		requestDTO.setPeriod(period);
		requestDTO.setRequestId(requestId);
		requestDTO.setTokenId(token);
		requestDTO.setSignRelationId(Long.valueOf(signRelationId));
		return requestDTO;
	}

	public CardNoOrderRequestDTO buildCardNoOrderRequestDTO(String bankCode, String period, String cardNo,
			long requestId, String token) {
		CardNoOrderRequestDTO requestDTO = new CardNoOrderRequestDTO();
		requestDTO.setCardNo(cardNo);
		requestDTO.setRequestId(requestId);
		requestDTO.setBankCode(bankCode);
		requestDTO.setPeriod(period);
		requestDTO.setTokenId(token);
		return requestDTO;
	}

	/**
	 * 签约关系ID下单接口
	 * 
	 * @param signRelationId
	 * @param token
	 */
	@RequestMapping(value = "/ajax/signed/order")
	public @ResponseBody void orderBySignRelationId(String bankCode, String period, String signRelationId, String token,
			HttpServletResponse httpServletResponse, HttpServletRequest request) {
		BasicResponseVO resVo = new BasicResponseVO();
		resVo.setBizStatus(AJAX_SUCCESS);
		resVo.setToken(token);
		RequestInfoDTO info = null;
		try {
			if (StringUtils.isBlank(signRelationId) || StringUtils.isBlank(bankCode) || StringUtils.isBlank(period)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			info = newWapPayService.validateRequestInfoDTO(token);
			openBankInstallment(info.getPaymentRequestId(), token);
			SignRelationIdOrderRequestDTO requestDTO = buildSignRelationIdOrderRequestDTO(bankCode, period,
					signRelationId, info.getPaymentRequestId(), token);
			ncCashierService.orderBySignRelationId(requestDTO);
		} catch (Throwable t) {
			CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
			resVo.setErrorcode(ex.getDefineCode());
			resVo.setErrormsg(ex.getMessage());
			resVo.setBizStatus(AJAX_FAILED);
		}
		ajaxResultWrite(httpServletResponse, resVo);
	}

	@RequestMapping(value = "/ajax/signed/sms")
	@ResponseBody
	public void sendSms(String token, HttpServletResponse httpServletResponse, HttpServletRequest request) {
		BasicResponseVO resVo = new BasicResponseVO();
		resVo.setBizStatus(AJAX_SUCCESS);
		resVo.setToken(token);
		RequestInfoDTO info = null;
		try {
			info = newWapPayService.validateRequestInfoDTO(token);
			openBankInstallment(info.getPaymentRequestId(), token);
			ncCashierService.bankInstallmentSmsSend(info.getPaymentRequestId(), info.getPaymentRecordId());
		} catch (Throwable t) {
			logger.warn("sendSms fail, t=", t);
			CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
			resVo.setErrorcode(ex.getDefineCode());
			resVo.setErrormsg(ex.getMessage());
			if ("N9003015".equals(ex.getDefineCode())) {
				// 发短验次数超限，这笔订单无法再支付，跳到失败页
				resVo.setBizStatus(AJAX_FAILED);
			} else {
				resVo.setBizStatus(SMS_FAILD);
			}
		}
		ajaxResultWrite(httpServletResponse, resVo);
	}

	@RequestMapping(value = "/ajax/signed/confirm")
	@ResponseBody
	public void confirmPay(String token, String verifyCode, HttpServletResponse httpServletResponse,
			HttpServletRequest request) {
		BasicResponseVO resVo = new BasicResponseVO();
		resVo.setBizStatus(AJAX_SUCCESS);
		resVo.setToken(token);
		RequestInfoDTO info = null;
		try {
			if (StringUtils.isBlank(verifyCode) || verifyCode.length() != 6) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			info = newWapPayService.validateRequestInfoDTO(token);
			openBankInstallment(info.getPaymentRequestId(), token);
			ncCashierService.bankInstallmentConfirmPay(info.getPaymentRequestId(), info.getPaymentRecordId(),
					verifyCode);
		} catch (Throwable t) {
			logger.warn("sendSms fail, t=", t);
			CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
			resVo.setErrorcode(ex.getDefineCode());
			resVo.setErrormsg(ex.getMessage());
			if ("N400094".equals(ex.getDefineCode()) || "N400091".equals(ex.getDefineCode())
					|| "N9003016".equals(ex.getDefineCode())) {
				resVo.setBizStatus(SMS_FAILD);
			} else {
				resVo.setBizStatus(AJAX_FAILED);
			}
		}
		ajaxResultWrite(httpServletResponse, resVo);
	}

}
