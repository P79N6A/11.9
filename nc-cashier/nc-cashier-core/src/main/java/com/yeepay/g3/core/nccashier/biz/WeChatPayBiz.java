/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.JsapiRouteRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayResponseDTO;

/**
 * @author zhen.tan
 * 微信支付biz
 *
 */
public interface WeChatPayBiz {

	/**
	 * 微信支付下单
	 * @param requestDto
	 * @return
	 */
	public WeChatPayResponseDTO pay(WeChatPayRequestDTO requestDto);


	/**
	 * 公众号支付调取预路由功能
	 * @param jsapiRouteRequestDTO
	 * @return
	 */
	public JsapiRouteResponseDTO weChatRoute(JsapiRouteRequestDTO jsapiRouteRequestDTO);
}
