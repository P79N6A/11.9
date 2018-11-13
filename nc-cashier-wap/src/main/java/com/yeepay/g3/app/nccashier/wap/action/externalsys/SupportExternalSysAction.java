package com.yeepay.g3.app.nccashier.wap.action.externalsys;

import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.app.nccashier.wap.service.externalsys.NonCashierService;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ConstantUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.app.nccashier.wap.utils.MD5Util;
import com.yeepay.g3.app.nccashier.wap.vo.externalsys.NcauthReceiveBankRequestParam;
import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
public class SupportExternalSysAction {
	
	private static Logger logger = LoggerFactory.getLogger(SupportExternalSysAction.class);

	@Resource
	private NonCashierService nonCashierService;

	@Resource
	protected NewWapPayService newWapPayService;

	@Resource
	protected NcCashierService ncCashierService;
	
	/**
	 * 智能网银H5前端回调
	 * 
	 * @param paymentno
	 * @return
	 */
	@RequestMapping(value = "/intelinet/result", method = { RequestMethod.POST, RequestMethod.GET })
	public Object receiveIntelligentNetResultFrontCallback(String paymentno) {
		try {
			if (StringUtils.isBlank(paymentno)) {
				// 如果paymentno为空，跳转到失败
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			// 调用nc-cashier-core获取跳转业务方的回调地址
			String url = nonCashierService.receiveIntelligentNetResultFrontCallback(paymentno);
			return new RedirectView(url);
		} catch (CashierBusinessException e) {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("front_callback");
			if (Errors.ORDER_STATUS_UNKNOWN.getCode().equals(e.getDefineCode())) {
				// 处理中，页面有重新查询按钮
				mv.addObject("bizStatus", ConstantUtil.AJAX_SUCCESS);
				mv.addObject("frontUrl",
						CommonUtil.getCashierUrlDefaultPrefix() + "/intelinet/result?paymentno=" + paymentno);
			} else {
				// 错误，页面展示错误码和错误描述信息
				mv.addObject("bizStatus", ConstantUtil.AJAX_FAILED);
				CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
				mv.addObject("errorCode", ex.getDefineCode());
				mv.addObject("errorMsg", ex.getMessage());
			}
			return mv;
		}
	}

	/**
	 * 帮鉴权中心做的接收用户在银行页面鉴权成功之后，回调银行子系统，银行子系统前端回调的入口
	 * 
	 * @param requestid
	 *            请求流水号
	 * @param errorcode
	 *            是否成功 true/false
	 * @param errormsg
	 *            错误码
	 * @param success
	 *            错误原因
	 */
	@RequestMapping(value = "/auth/result", method = { RequestMethod.POST, RequestMethod.GET })
	public RedirectView receiveAuthResultFrontendNotify(String requestid, String errorcode, String errormsg,
			boolean success) {
		logger.info("receiveAuthResultFrontendNotify requestid:{}, errorcode:{}, errormsg:{}, success:{}", requestid, errorcode, errormsg, success);
		NcauthReceiveBankRequestParam ncauthReceiveBankRequestParam = buildNcauthReceiveBankRequestParam(requestid,
				errorcode, errormsg, success);
		String url = nonCashierService.receiveFrontendNotify(ncauthReceiveBankRequestParam);
		if (StringUtils.isBlank(url)) {
			url = CommonUtil.getCashierUrlDefaultPrefix() + "static/common_error.html";
		}
		return new RedirectView(url);
	}

	/**
	 * 签约回调
	 * @param token
	 * @param orderType
	 * @param bindId
	 * @param sign
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/sign/callback", method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestSignCallBack(String token, String orderType, String bindId,String sign, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("[monitor],event:requestSignCallBack,token:{},orderType:{},bindId:{}", token,orderType,bindId);

		StringBuffer params = new StringBuffer();
		params.append("token=").append(token).append("&orderType=").append(orderType).append("&bindId=").append(bindId).append("&salt=").append(CommonUtil.SALT);

		if (!MD5Util.signVerify(params.toString(), sign)) {
			return new RedirectView( CommonUtil.getCashierUrlDefaultPrefix() + "static/common_error.html");
		}

		String url;
		if (NCCashierOrderTypeEnum.FIRST.name().equals(orderType)){
			url = CommonUtil.getCashierUrlDefaultPrefix()+"/wap/pay/first";
		}else if (NCCashierOrderTypeEnum.BIND.name().equals(orderType)){
			url =CommonUtil.getCashierUrlDefaultPrefix()+"/wap/pay/bind";
		}else{
			url = CommonUtil.getCashierUrlDefaultPrefix() + "static/common_error.html";
		}
		response.setContentType( "text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<form name='paySubmit' method='post'  action='"+url+"' >");
		out.println("<input type='hidden' name='token' value='" + token+ "'>");
		out.println("<input type='hidden' name='bindId' value='" + bindId+ "'>");
		out.println("</form>");
		out.println("<script>");
		out.println("  document.paySubmit.submit()");
		out.println("</script>");
		return null;
	}


	private NcauthReceiveBankRequestParam buildNcauthReceiveBankRequestParam(String requestId, String errorCode,
			String errorMsg, boolean success) {
		NcauthReceiveBankRequestParam ncauthReceiveBankRequestParam = new NcauthReceiveBankRequestParam();
		ncauthReceiveBankRequestParam.setErrorCode(errorCode);
		ncauthReceiveBankRequestParam.setErrorMsg(errorMsg);
		ncauthReceiveBankRequestParam.setRequestId(requestId);
		ncauthReceiveBankRequestParam.setSuccess(success);
		return ncauthReceiveBankRequestParam;
	}
}
