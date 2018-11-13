package com.yeepay.g3.app.nccashier.wap.action;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.app.nccashier.wap.utils.captcha.CaptchaServiceFactory;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 图片验证码
 * 
 * @author duangduang
 * @date 2017-06-02
 */
@Controller
@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
public class CaptchaAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaAction.class);

	@Autowired
	private NewWapPayService newWapPayService;

	@RequestMapping(value = "/captcha/refresh")
	public void refreshCode(String token, HttpServletResponse response) {
		LOGGER.info("[monitor],event:refreshCode request, token:{}", token);
		try {
			newWapPayService.validateRequestInfoDTO(token);
			setResponseHeaders(response);
			CaptchaServiceFactory.writeCaptchaToOutputStream(response.getOutputStream(),
					Constant.DEFAULT_IMG_FILE_FORMAT, token);
		} catch (Throwable t) {
			LOGGER.error("[monitor],event:refreshCode request, token:" + token, t);
		}
	}

	/**
	 * 设置返回头
	 * 
	 * @param response
	 */
	protected void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
	}
}
