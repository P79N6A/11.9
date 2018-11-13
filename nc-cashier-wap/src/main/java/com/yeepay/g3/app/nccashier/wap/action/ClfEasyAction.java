package com.yeepay.g3.app.nccashier.wap.action;/**
 * @program: nc-cashier-parent
 * @description: 分期易Action
 * @author: jimin.zhou
 * @create: 2018-10-17 14:04
 **/

import com.yeepay.g3.app.nccashier.wap.service.CflEasyService;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.vo.ResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.SmsSendResponseVo;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyBankInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyConfirmPayRequestVo;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyOrderReponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyOrderRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyPreRouteRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasySmsSendRequestVo;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @description: 分期易Action
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-17 14:04
 **/

@Controller
@RequestMapping(value = "/clfEasy")
public class ClfEasyAction extends WapBaseAction{
	
    private static Logger logger = LoggerFactory.getLogger(ClfEasyAction.class);

    @Resource
	private CflEasyService cflEasyService;

	/**
	 * 分期易模块
	 * 
	 * @param token
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/index")
	public void toClfEasy(String token, HttpServletRequest request, HttpServletResponse httpServletResponse) {
		CflEasyBankInfoVO response = new CflEasyBankInfoVO();
		RequestInfoDTO info = null;
		try {
			// 校验token
			info = newWapPayService.validateRequestInfoDTO(token);
			// 产品开通校验
			openClfEasy(info.getPaymentRequestId(), token);
			// 获取支持的分期易银行和期数等信息
			cflEasyService.getSupportCflEasyBankInfo(response, token, info.getPaymentRequestId());
			if (response.hasNoUsableBank()) {
				throw new CashierBusinessException(Errors.QUERY_BANK_LIST_ERROR);
			}
			response.setBizStatus(AJAX_SUCCESS);
		} catch (Throwable t) {
			logger.warn("getSupportCflEasyBankInfo fail, token=" + token + "t=", t);
			CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
			response.setErrorCode(ex.getDefineCode());
			response.setErrorMsg(ex.getMessage());
			response.setBizStatus(AJAX_FAILED);
		}
		ajaxResultWrite(httpServletResponse, response);
	}

	@ResponseBody
    @RequestMapping(value = "/prerouter")
	public void preRoute(HttpServletRequest request, CflEasyPreRouteRequestVO requestVo,
			HttpServletResponse httpServletResponse) {
		RequestInfoDTO info = null;
		CflEasyOrderReponseVO response = new CflEasyOrderReponseVO();
		try {
			// 入参校验
			if (requestVo == null) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			requestVo.validate();
			info = newWapPayService.validateRequestInfoDTO(requestVo.getToken());
			// 卡号解密
			requestVo.decode();
			// 产品开通校验
			openClfEasy(info.getPaymentRequestId(), requestVo.getToken());
			// cardBin校验、预路由
			cflEasyService.cflEasyPreRoute(requestVo, response, info.getPaymentRequestId());
			response.setBizStatus(AJAX_SUCCESS);
		} catch (Throwable t) {
			logger.warn("getSupportCflEasyBankInfo fail, token=" + requestVo.getToken() + "t=", t);
			CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
			response.setErrorCode(ex.getDefineCode());
			response.setErrorMsg(ex.getMessage());
			response.setBizStatus(AJAX_FAILED);
		}
		ajaxResultWrite(httpServletResponse, response);
	}

    /**
     * 下单
     * @param request
     * @param requestVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/request")
    public void order(HttpServletRequest request, CflEasyOrderRequestVO requestVo, HttpServletResponse httpServletResponse) {
    		CflEasyOrderReponseVO response = new CflEasyOrderReponseVO();
		RequestInfoDTO info = null;
		try {
			// 入参校验
			if (requestVo==null) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			requestVo.validate();
			info = newWapPayService.validateRequestInfoDTO(requestVo.getToken());
			// 敏感信息解密
			requestVo.decode();
			// 产品开通校验
			openClfEasy(info.getPaymentRequestId(), requestVo.getToken());
			// 获取支持的分期易银行和期数等信息
			cflEasyService.cflEasyOrder(requestVo, response, info.getPaymentRequestId());
			response.setBizStatus(AJAX_SUCCESS);
		} catch (Throwable t) {
			logger.warn("getSupportCflEasyBankInfo fail, token=" + requestVo.getToken() + "t=", t);
			CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
			response.setErrorCode(ex.getDefineCode());
			response.setErrorMsg(ex.getMessage());
			response.setBizStatus(AJAX_FAILED);
		}
		ajaxResultWrite(httpServletResponse, response);
    }

    /**
     * 发短验
     */
    @ResponseBody
    @RequestMapping(value = "/ajax/smsSend")
    public void sendSms(HttpServletRequest request, CflEasySmsSendRequestVo requestVo, HttpServletResponse httpServletResponse) {
        SmsSendResponseVo resVo = new SmsSendResponseVo();
        resVo.setSmsType(requestVo.getSmsType());
        resVo.setBizStatus(AJAX_SUCCESS);
        resVo.setToken(requestVo.getToken());
        RequestInfoDTO info = null;
        try {
            requestVo.decode();
            info = newWapPayService.validateRequestInfoDTO(requestVo.getToken());
            //产品开通校验
            openClfEasy(info.getPaymentRequestId(), requestVo.getToken());
            //发送短信
            requestVo.setRecordId(info.getPaymentRecordId());
            requestVo.setRequestId(info.getPaymentRequestId());
			cflEasyService.clfEasySmsSend(requestVo,resVo);
            resVo.setPhoneNo(requestVo.getPhoneNo());
        } catch (Throwable t) {
            logger.warn("clfsendSms fail, token=" + requestVo.getToken() + "t=", t);
            CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
            resVo.setErrorCode(ex.getDefineCode());
            resVo.setErrorMsg(ex.getMessage());
            resVo.setBizStatus(AJAX_FAILED);
        }
        ajaxResultWrite(httpServletResponse, resVo);
    }


    /**
     * 确认支付
     */
    @ResponseBody
    @RequestMapping(value = "/ajax/confirmPay")
    public void confirmPay(HttpServletRequest request, CflEasyConfirmPayRequestVo requestVo, HttpServletResponse httpServletResponse) {
        ResponseVO resVo = new ResponseVO();
        resVo.setBizStatus(AJAX_SUCCESS);
        resVo.setToken(requestVo.getToken());
        RequestInfoDTO info = null;
        try {
            requestVo.decode();
            if (StringUtils.isBlank(requestVo.getVerifycode()) || requestVo.getVerifycode().length() != 6) {
                throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
            }
            info = newWapPayService.validateRequestInfoDTO(requestVo.getToken());
            //产品开通校验
            openClfEasy(info.getPaymentRequestId(), requestVo.getToken());
            //确认支付
            requestVo.setRecordId(info.getPaymentRecordId());
            requestVo.setRequestId(info.getPaymentRequestId());
			cflEasyService.clfEasyConfirmPay(requestVo,resVo);
        } catch (Throwable t) {
            logger.warn("clfconfirmPay fail, token=" + requestVo.getToken() + "t=", t);
            CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
            resVo.setErrorCode(ex.getDefineCode());
            resVo.setErrorMsg(ex.getMessage());
            resVo.setBizStatus(AJAX_FAILED);
        }
        ajaxResultWrite(httpServletResponse, resVo);
    }

    /**
     * 是否开通分期易
     * @param requestId
     * @param token
     */
    private void openClfEasy(long requestId, String token) {
        PayExtendInfo payExtendInfo = ncCashierService.getPayExtendInfo(requestId, token);
        if (!payExtendInfo.containsPayType(PayTypeEnum.ZF_FQY_NORMAL)) {
            throw new CashierBusinessException(Errors.NOT_OPEN_PRODUCT_ERROR);
        }
    }




}
