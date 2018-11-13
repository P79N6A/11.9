/**
 * 
 */
package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.JsapiRouteRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayResponseDTO;

/**
 * @author zhen.tan
 * 微信API支付下单请求
 *
 */
public interface WeChatPayRequestFacade{

	/**
	 * 微信支付请求
	 * @param payRequest
	 * @return
	 */
	public WeChatPayResponseDTO pay(WeChatPayRequestDTO payRequest);


	/**
	 * 微信公众号预路由功能
	 * @param jsapiRouteRequestDTO
	 * @return
	 */
	public JsapiRouteResponseDTO routeWaChat(JsapiRouteRequestDTO jsapiRouteRequestDTO);

}
