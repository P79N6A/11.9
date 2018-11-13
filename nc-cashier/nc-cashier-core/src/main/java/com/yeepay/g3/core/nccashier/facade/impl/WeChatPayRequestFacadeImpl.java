/**
 * 
 */
package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.biz.WeChatPayBiz;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayResponseDTO;
import com.yeepay.g3.facade.nccashier.service.WeChatPayRequestFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhen.tan
 *
 */
@Service("weChatPayRequestFacade")
public class WeChatPayRequestFacadeImpl implements WeChatPayRequestFacade {

	@Resource
	private WeChatPayBiz weChatPayBiz;
	
	@Override
	public WeChatPayResponseDTO pay(WeChatPayRequestDTO payRequest) {
		return weChatPayBiz.pay(payRequest);
	}

	@Override
	public JsapiRouteResponseDTO routeWaChat(JsapiRouteRequestDTO jsapiRouteRequestDTO) {
		return weChatPayBiz.weChatRoute(jsapiRouteRequestDTO);
	}

}
