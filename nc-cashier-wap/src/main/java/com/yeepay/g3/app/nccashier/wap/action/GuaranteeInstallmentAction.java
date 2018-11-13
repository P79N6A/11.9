package com.yeepay.g3.app.nccashier.wap.action;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.service.GuaranteeInstallmentService;
import com.yeepay.g3.app.nccashier.wap.utils.Base64Util;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentCardNoCheckResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPaymentRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPaymentResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPrePayResponseVO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 担保分期action
 */
@Controller
@RequestMapping(value = "/guarantee")
public class GuaranteeInstallmentAction extends WapBaseAction {

    private static Logger logger = LoggerFactory.getLogger(GuaranteeInstallmentAction.class);

    @Autowired
    protected GuaranteeInstallmentService guaranteeInstallmentService;

    /**
     * 跳转到wap收银台担保分期页
     *
     * @return 支持的银行及期数
     */
    @RequestMapping(value = "/wap/index")
    public ModelAndView toGuaranteeInstallment(String token, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("guarantee"); // 银行卡分期主页
        mv.addObject("token", token);
        RequestInfoDTO info = null;
        try {
            info = newWapPayService.validateRequestInfoDTO(token);
            this.hasOpenProduct(info.getPaymentRequestId(), token, PayTypeEnum.DBFQ_TL);
            InstallmentBankResponseVO response = guaranteeInstallmentService.getSupportBankAndPeriods(info.getPaymentRequestId());
            mv.addObject("periodList", MapUtils.isEmpty(response.getPeriodListOfBank()) ? null
                    : JSON.toJSONString(response.getPeriodListOfBank()));
            mv.addObject("usableBankList", JSON.toJSONString(response.getUsableBankList()));
        } catch (Throwable t) {
            logger.error("[monitor],event:nccashier_routeInstallmentIndex异常,token:" + token, t);
            CashierBusinessException ex = ExceptionUtil.handleException(t, SysCodeEnum.NCCASHIER_WAP);
            String repayUrl = getH5IndexUrl(info.getPaymentRequestId(), info.getMerchantNo(), token, request,null);
            return createErrorMV(token, ex.getDefineCode(), ex.getMessage(), null, "pay_fail", repayUrl);
        }
        return mv;
    }


    /**
     * 预下单接口
     *
     * @param token
     * @param bankCode 银行编码
     * @param period   期数
     * @param request
     * @return
     */
    @RequestMapping(value = "/ajax/prepay")
    @ResponseBody
    public String prePay(String token, String bankCode, String period, HttpServletRequest request) {
        GuaranteeInstallmentPrePayResponseVO responseVO = new GuaranteeInstallmentPrePayResponseVO();
        RequestInfoDTO info = null;
        try {
            if (StringUtils.isBlank(token) || StringUtils.isBlank(bankCode) || StringUtils.isBlank(period)
                    || !StringUtils.isNumeric(period) || request == null) {
                throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
            }
            info = checkRequestInfoDTO(token);
            responseVO = guaranteeInstallmentService.prePay(info, bankCode, period, token);
            responseVO.setBizStatus(AJAX_SUCCESS);
        } catch (Throwable t) {
            logger.warn("prePay() 担保分期预下单，token=  " + token + "，异常 = ", t);
            CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
            responseVO.setErrorcode(ex.getDefineCode());
            responseVO.setErrormsg(ex.getMessage());
            responseVO.setBizStatus(AJAX_FAILED);
        }
        responseVO.setToken(token);
        logger.info("prePay() 担保分期预下单，返回结果 = {}", responseVO);
        return JSON.toJSONString(responseVO);
    }


    /**
     * 请求支付接口
     *
     * @param requestVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/ajax/pay")
    @ResponseBody
    public String requestPayment(GuaranteeInstallmentPaymentRequestVO requestVO, HttpServletRequest request) {
        GuaranteeInstallmentPaymentResponseVO responseVO = new GuaranteeInstallmentPaymentResponseVO();
        RequestInfoDTO info = null;
        String token = requestVO == null ? null : requestVO.getToken();
        try {
            if (requestVO ==null || request == null) {
                throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
            }
            Base64Util.decryptCardInfoVO(requestVO);
            requestVO.validate();
            info = checkRequestInfoDTO(requestVO.getToken());
            responseVO = guaranteeInstallmentService.requestPayment(info, requestVO);
            responseVO.setBizStatus(AJAX_SUCCESS);
        } catch (Throwable t) {
            logger.warn("requestPayment() 担保分期请求支付，token=  " + token + "，异常 = ", t);
            CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
            String code = ex.getDefineCode();
            responseVO.setErrorcode(code);
            responseVO.setErrormsg(ex.getMessage());
            if(CommonUtil.guaranteeInstallPaymentErrorToFailPage(code)){
                responseVO.setBizStatus(AJAX_FAILED);
            }else {
                responseVO.setBizStatus(ORDER_FAILED);
            }
        }
        responseVO.setToken(token);
        logger.info("requestPayment() 担保分期请求支付，返回结果 = {}", responseVO);
        return JSON.toJSONString(responseVO);
    }

    /**
     * 校验卡号是否是指定银行的合法的卡号，并返回卡类型等
     * @param token
     * @param bankCode
     * @param cardNo
     * @param request
     * @return
     */
    @RequestMapping(value = "/ajax/check")
    @ResponseBody
    public String checkCardNo(String token, String bankCode, String cardNo, HttpServletRequest request){
        GuaranteeInstallmentCardNoCheckResponseVO responseVO = new GuaranteeInstallmentCardNoCheckResponseVO();
        RequestInfoDTO info = null;
        try {
            if (StringUtils.isBlank(token) || StringUtils.isBlank(bankCode) || StringUtils.isBlank(cardNo) || request == null) {
                throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
            }
            info = checkRequestInfoDTO(token);
            cardNo = Base64Util.decode(cardNo);
            responseVO = guaranteeInstallmentService.checkCardNo(info, cardNo, bankCode);
            responseVO.setBizStatus(AJAX_SUCCESS);
        } catch (Throwable t) {
            logger.warn("checkCardNo() 担保分期卡号校验，token=  " + token + "，异常 = ", t);
            CashierBusinessException ex = ExceptionUtil.handleException(t, judgeSysCode(request, info));
            responseVO.setErrorcode(ex.getDefineCode());
            responseVO.setErrormsg(ex.getMessage());
            responseVO.setBizStatus(AJAX_FAILED);
        }
        responseVO.setToken(token);
        logger.info("checkCardNo() 担保分期卡号校验，返回结果 = {}", responseVO);
        return JSON.toJSONString(responseVO);
    }

}
