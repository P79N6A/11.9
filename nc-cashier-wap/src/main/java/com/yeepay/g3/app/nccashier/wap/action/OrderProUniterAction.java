package com.yeepay.g3.app.nccashier.wap.action;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.vo.SdkRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.UrlExtInfoVO;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.DirectPayType;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.UUID;

/**
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.action
 *
 * @author pengfei.chen
 * @since 17/2/17 11:48
 */
@Controller
@RequestMapping(value = "/cashier")
public class OrderProUniterAction extends WapBaseAction{
    private static final Logger logger = LoggerFactory.getLogger(OrderProUniterAction.class);

    @RequestMapping("/std")
    public Object requestRoute(HttpServletRequest request){
        CashierVersionEnum cashierVersionEnum = getTerminalInfo(request);
        UserAccessDTO userAccess = null;
        OrderProcessResponseDTO response = null;
        try {
            validateBasicParams(request);
            //校验参数,并且验证签名
            OrderProcessorRequestDTO orderProcessorRequestDTO = getUrlParamsString(request, "std");
            //下单
            response = ncCashierService.orderProcessorRequest(orderProcessorRequestDTO);
            // modify by meiling.zhuang：黑名单内的商户不允许进入收银台，其交易被拒绝
			RedirectView redirectView = refuseTransactionOfBlackListMerchant(response.getMerchantNo());
			if (redirectView != null) {
				return redirectView;
			}
            //检查风控reffer
			checkReffer(request, response.getUniqueOrderNo(), response.getMerchantNo());
            //保存userAccess
            userAccess = saveUserAccess(request, response);
            userAccess.setUrlParamInfo(JSONObject.toJSONString(orderProcessorRequestDTO));
            userAccess.setMerchantConfigInfo(response.getConfigInfo());
            userAccess.setCashierVersionEnum(cashierVersionEnum);
            ncCashierService.saveUserAccess(userAccess);

            StringBuilder redirectUrl = new StringBuilder(CommonUtil.getPreUrl(response.getMerchantNo(),request));
            if (cashierVersionEnum == CashierVersionEnum.PC) {
                redirectUrl.append(newpcctx).append("/request/").append(userAccess.getTokenId());
            } else {
                redirectUrl.append(wapctx).append("/request/").append(response.getMerchantNo()).append("/").append(response.getEncodeRequestId()).append("?token=").append(userAccess.getTokenId());
            }
            return new RedirectView(redirectUrl.toString());
        }catch (Throwable e){
            logger.error("[monitor],event:nccashierwap_requestSTD_SystemException,token:" + request.getParameter("token"), e);
            RequestInfoDTO info = getErrorOrderInfo(userAccess);
			// modify by meiling.zhuang：黑名单内的商户不允许进入收银台，其交易被拒绝
			String merchantNoToCheck = (response==null||StringUtils.isBlank(response.getMerchantNo())) ? request.getParameter("merchantNo")
					: response.getMerchantNo();
			RedirectView redirectView = refuseTransactionOfBlackListMerchant(merchantNoToCheck);
			if (redirectView != null) {
				return redirectView;
			} else {
				if (cashierVersionEnum == CashierVersionEnum.PC) {
					return createNewPcErrorRV(null, false, "unitePc_error", e, SysCodeEnum.NCCASHIER_PC, info,
							request.getParameter("merchantNo"), request);
				} else {
					CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
					return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
				}
			}

		}
    }
    
	/**
	 * 黑名单内的商户不允许进入收银台，其交易被拒绝，随便跳到哪个页面都行
	 * 
	 * @param merchantNoToCheck
	 * @return
	 */
	private RedirectView refuseTransactionOfBlackListMerchant(String merchantNoToCheck) {
		boolean notToShow = CommonUtil.judgeMerchantNotShowPcYeepayInfo(merchantNoToCheck);
		if (notToShow) {
			logger.info("商编={}在黑名单={}", merchantNoToCheck, notToShow);
			// 黑名单内的商户不允许进入收银台，跳到随便哪个页面吧
			return new RedirectView("https://www.gounianwangwang.com");
		}
		return null;
	}

    @Deprecated
    @RequestMapping("/wx")
    public Object requestWx(HttpServletRequest request){
        CashierVersionEnum cashierVersionEnum = getTerminalInfo(request);
        UserAccessDTO userAccess = null;
        try {
            if (cashierVersionEnum != CashierVersionEnum.WXGZH) {
                //抛出异常
                throw new CashierBusinessException(Errors.REQUEST_TERMINAL_NO_SUPPORT.getCode(), Errors.REQUEST_TERMINAL_NO_SUPPORT.getMsg());
            }
            validateBasicParams(request);
            //校验参数,并且验证签名
            OrderProcessorRequestDTO orderProcessorRequestDTO = getUrlParamsString(request, "wx");
            orderProcessorRequestDTO.setCashierVersion(CashierVersionEnum.WXGZH);
            //下单
            OrderProcessResponseDTO response = ncCashierService.orderProcessorRequest(orderProcessorRequestDTO);
            //检查风控reffer
            checkReffer(request, response.getUniqueOrderNo(), response.getMerchantNo());
            //保存userAccess
            userAccess = saveUserAccess(request, response);
            userAccess.setUrlParamInfo(JSONObject.toJSONString(orderProcessorRequestDTO));
            userAccess.setMerchantConfigInfo(response.getConfigInfo());
            userAccess.setCashierVersionEnum(cashierVersionEnum);
            ncCashierService.saveUserAccess(userAccess);
            return new RedirectView(CommonUtil.getPreUrl(response.getMerchantNo(),request) + wapctx + "/request/" + response.getMerchantNo() + "/" + response.getEncodeRequestId() + "?token=" + userAccess.getTokenId());
        }catch (Throwable e){
            logger.error("[monitor],event:nccashierwap_requestWX_SystemException,token:" + request.getParameter("token"), e);
            RequestInfoDTO info = getErrorOrderInfo(userAccess);
            if(cashierVersionEnum == CashierVersionEnum.PC){
                return createNewPcErrorRV(null, false,"unitePc_error",e, SysCodeEnum.NCCASHIER_PC, info, request.getParameter("merchantNo"), request);
            }else {
                CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
                return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
            }
        }
    }

    @RequestMapping("/aqr")
    public Object requestScanpay(HttpServletRequest request){
        CashierVersionEnum cashierVersionEnum = getTerminalInfo(request);
        UserAccessDTO userAccess = null;
        try {
            validateBasicParams(request);
            //校验参数,并且验证签名
            OrderProcessorRequestDTO orderProcessorRequestDTO = getUrlParamsString(request, "aqr");
            //下单
            OrderProcessResponseDTO response = ncCashierService.orderProcessorRequest(orderProcessorRequestDTO);
            //检查风控reffer
            checkReffer(request, response.getUniqueOrderNo(), response.getMerchantNo());
            //保存userAccess
            userAccess = saveUserAccess(request, response);
            userAccess.setUrlParamInfo(JSONObject.toJSONString(orderProcessorRequestDTO));
            userAccess.setMerchantConfigInfo(response.getConfigInfo());
            userAccess.setCashierVersionEnum(cashierVersionEnum);
            ncCashierService.saveUserAccess(userAccess);
            if (cashierVersionEnum == CashierVersionEnum.PC) {
                return new RedirectView(CommonUtil.getPreUrl(response.getMerchantNo(),request) + newpcctx + "/request/" + userAccess.getTokenId());
            } else {
                return new RedirectView(CommonUtil.getPreUrl(response.getMerchantNo(),request) + wapctx + "/request/" + response.getMerchantNo() + "/" + response.getEncodeRequestId() + "?token=" + userAccess.getTokenId());
            }
        }catch (Throwable e){
            logger.error("[monitor],event:nccashierwap_requestAQR_SystemException,token:" + request.getParameter("token"), e);
            RequestInfoDTO info = getErrorOrderInfo(userAccess);
            if(cashierVersionEnum == CashierVersionEnum.PC){
                return createNewPcErrorRV(null, false,"unitePc_error",e, SysCodeEnum.NCCASHIER_PC, info,  request.getParameter("merchantNo"), request);
            }else {
                CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
                return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
            }
        }
    }


    @RequestMapping("/sdk")
    public Object requestSdk(@Valid SdkRequestVO sdkRequestVO,BindingResult bindingResult,HttpServletRequest request){
        try {
            if(bindingResult.hasErrors()){
              FieldError fieldError = bindingResult.getFieldError();
                throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), fieldError.getDefaultMessage());
            }
            //验证签名
            yopVerify(sdkRequestVO.getMerchantNo(),sdkRequestVO.getText(),sdkRequestVO.getNewText(), sdkRequestVO.getSign());
            OrderProcessorRequestDTO orderProcessorRequestDTO = new OrderProcessorRequestDTO();
            //属性拷贝
            BeanUtils.copyProperties(orderProcessorRequestDTO,sdkRequestVO);
            orderProcessorRequestDTO.setCashierVersion(CashierVersionEnum.SDK);
            //下单
            OrderProcessResponseDTO response = ncCashierService.orderProcessorRequest(orderProcessorRequestDTO);
            //保存userAccess
            UserAccessDTO userAccess = saveUserAccess(request, response);
            userAccess.setUrlParamInfo(JSONObject.toJSONString(orderProcessorRequestDTO));
            userAccess.setMerchantConfigInfo(response.getConfigInfo());
            userAccess.setCashierVersionEnum(CashierVersionEnum.SDK);
            ncCashierService.saveUserAccess(userAccess);
            return new RedirectView(CommonUtil.getPreUrl(response.getMerchantNo(),request) + wapctx + "/request/" + response.getMerchantNo() + "/" + response.getEncodeRequestId() + "?token=" + userAccess.getTokenId()+"&fromNewWAP=true");
        }catch (Throwable e){
            logger.error("[monitor],event:nccashierwap_requestSDK_SystemException,token:" + sdkRequestVO.getToken(), e);
            CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
            return createErrorMV(sdkRequestVO.getToken(), ex.getDefineCode(), ex.getMessage(), null);
        }

    }

	

    private OrderProcessorRequestDTO getUrlParamsString(HttpServletRequest request,String url){
        OrderProcessorRequestDTO orderProcessorRequestDTO = new OrderProcessorRequestDTO();
        StringBuilder stringBuilder = new StringBuilder();
        String token = request.getParameter("token");
        String bizType = request.getParameter("bizType");
        String merchantNo = request.getParameter("merchantNo");
        String timestamp = request.getParameter("timestamp");
        String cardType = request.getParameter("cardType");
        String userNo = request.getParameter("userNo")==null?"":request.getParameter("userNo");
        String userType = request.getParameter("userType")==null?"":request.getParameter("userType");
        String sign = request.getParameter("sign");
        String directPayType = request.getParameter("directPayType");
        String appId = request.getParameter("appId");
        String openId = request.getParameter("openId");
        String extendJson = request.getParameter("ext") == null ? "" : request.getParameter("ext");//扩展参数

        String appKey = null;
        if(CommonUtil.checkBizType(bizType)){
            bizType="";
            appKey=merchantNo;
        }else {
            bizType="&bizType="+bizType;
        }

        String oldText = ""; // 未加扩展参数之前的
        String newText = "";
        if("wx".equals(url)){
            userType = "WECHAT";
            userNo = openId;
            if(StringUtils.isBlank(appId) || StringUtils.isBlank(openId)){
                throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
            }
            orderProcessorRequestDTO.setOpenId(openId);
            orderProcessorRequestDTO.setAppId(appId);
            oldText = stringBuilder.append("merchantNo=").append(merchantNo).append("&token=").append(token).
                    append("&timestamp=").append(timestamp).append("&appId=").append(appId).
                    append("&openId=").append(openId).append(bizType).toString();
            newText = stringBuilder.append("&ext=").append(extendJson).toString();
            logger.info("wx链接请求参与验证签名的字符串oldText={},newText={},token={}",oldText,newText, token);
        }
        //如果是扫码即使卡类型是传了也不校验 相当于移动端的标准版
        else if("aqr".equals(url)) {
            oldText = stringBuilder.append("merchantNo=").append(merchantNo).append("&token=").append(token).
                    append("&timestamp=").append(timestamp).append("&userNo=").append(userNo)
                    .append("&userType=").append(userType).append(bizType).toString();
            newText = stringBuilder.append("&ext=").append(extendJson).toString();
            logger.info("aqr链接请求参与验证签名的字符串oldText={},newText={},token={}", oldText, newText, token);
            
            // modify by zml:标准版支持商户（业务方）传openId和appId
            parseExtendInfo(extendJson, orderProcessorRequestDTO);
        }
        // 标准版
        else if("std".equals(url)){
            if(StringUtils.isBlank(cardType)){
                cardType="";
            }
            if(StringUtils.isBlank(directPayType)){
                directPayType = "";
            }else{
	            if(CommonUtil.checkDirectEbankToFailPage(merchantNo) && DirectPayType.notCorrectDirectType(directPayType)){
	            	throw new CashierBusinessException(Errors.DIRECT_PAY_TYPE_ERROR.getCode(), Errors.DIRECT_PAY_TYPE_ERROR.getMsg());
	            }
            }
            orderProcessorRequestDTO.setCardType(cardType);
            orderProcessorRequestDTO.setDirectPayType(directPayType);
            // modify by zml:标准版支持商户（业务方）传openId和appId
            parseExtendInfo(extendJson, orderProcessorRequestDTO);
            // 签名明文拼接方式 ，需要将openId和appId加进去
            oldText = stringBuilder.append("merchantNo=").append(merchantNo).append("&token=")
					.append(token).append("&timestamp=").append(timestamp).append("&directPayType=")
					.append(directPayType).append("&cardType=").append(cardType).append("&userNo=").append(userNo)
					.append("&userType=").append(userType).append(bizType).toString();
			newText = stringBuilder.append("&ext=").append(extendJson).toString();
        }
        yopVerify(appKey, oldText, newText, sign);
        orderProcessorRequestDTO.setToken(token);
        orderProcessorRequestDTO.setUserNo(userNo);
        orderProcessorRequestDTO.setUserType(userType);
        orderProcessorRequestDTO.setCashierVersion(getTerminalInfo(request));
        orderProcessorRequestDTO.setMerchantNo(merchantNo);
        orderProcessorRequestDTO.setUserIp(getUserIp(request));
        orderProcessorRequestDTO.setBizType(request.getParameter("bizType"));
        return orderProcessorRequestDTO;
    }

	/**
	 * 标准版的扩展信息解析
	 * 
	 * @param extParam
	 * @param urlParam
	 */
	private void parseExtendInfo(String extParam, OrderProcessorRequestDTO urlParam) {
		if (StringUtils.isBlank(extParam) || urlParam == null) {
			return;
		}
		
		UrlExtInfoVO urlExtInfoVO = UrlExtInfoVO.parseExtJson(extParam);
		if(urlExtInfoVO==null){
			return;
		}
	
		urlParam.setOpenId(urlExtInfoVO.getOpenId());
		urlParam.setAppId(urlExtInfoVO.getAppId());
        urlParam.setClientId(urlExtInfoVO.getClientId());
        urlParam.setAliAppId(urlExtInfoVO.getAliAppId());
        urlParam.setAliUserId(urlExtInfoVO.getAliUserId());
        urlParam.setPlatForm(urlExtInfoVO.getPlatForm());
        urlParam.setAppName(urlExtInfoVO.getAppName());
        urlParam.setAppStatement(urlExtInfoVO.getAppStatement());
        urlParam.setReportFee(urlExtInfoVO.getReportFee());
	}
	
	
    private JSONObject validateBasicParams(HttpServletRequest request){
        String token = request.getParameter("token");
        String timestamp = request.getParameter("timestamp");
        String merchantNo = request.getParameter("merchantNo");
        String sign = request.getParameter("sign");
        if(StringUtils.isBlank(merchantNo) ||StringUtils.isBlank(token) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(sign)) {
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
        }
        long times = 0l;
        try {
            times = Long.parseLong(timestamp);
        }catch (Throwable e){
            logger.error("转换时间戳异常timestamp="+timestamp);
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
        }
        CommonUtil.checkUrlOutOfExpDate(times);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);
        jsonObject.put("timestamp",timestamp);
        jsonObject.put("merchantNo",merchantNo);
        return jsonObject;
    }
    
	

    private UserAccessDTO saveUserAccess(HttpServletRequest request,OrderProcessResponseDTO orderProcessResponseDTO){
        //获取用户IP
        String ypip = getUserIp(request);
        // 获取用户终端UA
        String ua = getUserUA(request);
        String userToken = UUID.randomUUID().toString();
        if (ua.length() > 300) {
            ua = ua.substring(0, 300);
        }
        UserAccessDTO userAccess = new UserAccessDTO();
        userAccess.setPaymentRequestId(orderProcessResponseDTO.getRequestId()+"");
        userAccess.setUserIp(ypip);
        userAccess.setUserUa(ua);
        userAccess.setTokenId(userToken);
        userAccess.setMerchantNo(orderProcessResponseDTO.getMerchantNo());
        return userAccess;
    }
    private void checkReffer(HttpServletRequest request,String uniqueOrderNo,String merchantNo){
        String reffer = request.getHeader("referer");
        if(StringUtils.isNotBlank(reffer)){
            CheckRefferRequestDTO requestDTO = new CheckRefferRequestDTO();
            requestDTO.setBizOrderId(uniqueOrderNo);
            requestDTO.setMerchantAccountCode(merchantNo);
            requestDTO.setReffer(reffer);
            ncCashierService.checkReffer(requestDTO);
        }
    }

}
