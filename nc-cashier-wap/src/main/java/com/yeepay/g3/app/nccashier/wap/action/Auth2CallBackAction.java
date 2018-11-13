package com.yeepay.g3.app.nccashier.wap.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.yeepay.g3.app.nccashier.wap.utils.AESUtil;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.utils.WaChatPayUtils;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * auth2授权回调action
 * 
 * @author duangduang
 *
 */
@Controller
public class Auth2CallBackAction extends WapBaseAction {

	private static Logger logger = LoggerFactory.getLogger(Auth2CallBackAction.class);

	/**
	 * 支付宝标准版：接受支付宝授权成功后的回调
	 * 
	 * @param auth_code
	 * @param app_id
	 * @param scope
	 * @param state
	 * @param request
	 * @return
	 */
	@RequestMapping("/alipayStd/callback")
	public Object alipayStandardAuth2CallBack(String auth_code, String app_id, String scope, String state,
			HttpServletRequest request) {
		logger.info("接受支付宝标准版回调,token={}, auth_code={}, app_id={}, scope={}", state, auth_code, app_id, scope);
		return alipayLifeNoAuth2CallBack(auth_code, app_id, scope, state, PayTypeEnum.ALIPAY_H5_STANDARD, request);
	}
	
	/**
	 * 支付宝生活号回调
	 * 
	 * @param auth_code
	 * @param app_id
	 * @param scope
	 * @param state
	 * @param request
	 * @return
	 */
	@RequestMapping("/alipay/auth2Callback")
	public Object alipayLifeNoAuth2CallBack(String auth_code, String app_id, String scope, String state,
			HttpServletRequest request) {
		logger.info("接受支付宝生活号回调,token={}, auth_code={}, app_id={}, scope={}", state, auth_code, app_id, scope);
		return alipayLifeNoAuth2CallBack(auth_code, app_id, scope, state, PayTypeEnum.ZFB_SHH, request);
	}
	
	private Object alipayLifeNoAuth2CallBack(String auth_code, String app_id, String scope, String state,
			PayTypeEnum currentPayType, HttpServletRequest request) {
		RequestInfoDTO info = null;
		String token = state;
		try {
			info = checkRequestInfoDTO(token);
			String userId = WaChatPayUtils.getAlipayUserId(app_id, auth_code, token);
			// 重定向到微信支付
			Long requestId = info.getPaymentRequestId();
			String merchantNo = info.getMerchantNo();
			String encryptRequestId = AESUtil.routeEncrypt(merchantNo, String.valueOf(requestId));
			String url = null;
			if (PayTypeEnum.ZFB_SHH == currentPayType) {
				url = CommonUtil.getPreUrl(info.getMerchantNo(), request) + newwapctx + "/request?token=" + token
						+ "&requestId=" + encryptRequestId + "&merchantNo=" + info.getMerchantNo() + "&wpayId="
						+ userId;
			} else if (PayTypeEnum.ALIPAY_H5_STANDARD == currentPayType) {
				url = CommonUtil.getPreUrl(info.getMerchantNo(), request) + newwapctx + "/stdAlipay?token=" + token
						+ "&requestId=" + encryptRequestId + "&merchantNo=" + info.getMerchantNo() + "&wpayId="
						+ userId;
			}
			return new RedirectView(url);
		} catch (CashierBusinessException e) {
			logger.warn("[monitor],event:nccashier_alipayLifeNoAuth2CallBack_BusinException,token:" + token, e);
			if (Errors.THRANS_FINISHED.getCode().equals(e.getDefineCode())) {
				RedirectView rv = new RedirectView(
						CommonUtil.getPreUrl(info.getMerchantNo(), request) + wapctx + "/query/result?token=" + token);
				ModelMap map = new ModelMap();
				rv.setAttributesMap(map);
				return rv;
			} else {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
				return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
			}
		} catch (Throwable e) {
			logger.error("[monitor],event:nccashier_requestNewWap_SystemException,token:" + token, e);
			CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
			return createErrorMV(null, ex.getDefineCode(), ex.getMessage(), info);
		}

	}
	
}
