package com.yeepay.g3.facade.frontend.facade;

import com.yeepay.g3.facade.frontend.dto.AppPayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.AppPayResponseDTO;

/**
 * 微信 app内支付接口（sdk，wap）
 * @author songscorpio
 *
 */
@Deprecated
public interface FrontendAppFacade {
	/**
	 * h5支付
	 * @param dto
	 * @return
	 */
	public AppPayResponseDTO appH5Pay(AppPayRequestDTO dto);
	
	
}
