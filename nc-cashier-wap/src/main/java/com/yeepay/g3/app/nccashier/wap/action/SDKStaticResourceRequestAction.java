package com.yeepay.g3.app.nccashier.wap.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * SDK收银台静态资源请求
 * @author xueping.ni
 * @date 2017-04-12
 */
@Controller
@RequestMapping(value = "/sdk", method = {RequestMethod.POST, RequestMethod.GET})
public class SDKStaticResourceRequestAction {
	
	@RequestMapping(value = "/payToolsChoose",method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestPayToolsChoose(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sdk/payToolsChoose");
		return mv;
	}
	
	@RequestMapping(value = "/paySuccess",method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestSuccess(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sdk/paySuccess");
		return mv;
	}
	@RequestMapping(value = "/payFail",method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestFail(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sdk/payFail");
		return mv;
	}
	
	@RequestMapping(value = "/processing",method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestProcess(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sdk/processing");
		return mv;
	}
	@RequestMapping(value = "/loading",method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestLoading(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sdk/loading");
		return mv;
	}
	
	
	@RequestMapping(value = "/changeOtherPayTypes",method = {RequestMethod.POST, RequestMethod.GET})
	public Object requestChangeOtherPayTypes(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sdk/changeOtherPayTypes");
		return mv;
	}
}
