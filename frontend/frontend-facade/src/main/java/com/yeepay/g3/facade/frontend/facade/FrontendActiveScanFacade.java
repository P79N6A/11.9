package com.yeepay.g3.facade.frontend.facade;

import com.yeepay.g3.facade.frontend.dto.ActiveScanJsapiRequestDTO;
import com.yeepay.g3.facade.frontend.dto.ActiveScanRequestDTO;
import com.yeepay.g3.facade.frontend.dto.ActiveScanResponseDTO;

/**
 * 用户主扫，公众号支付（jsapi）
 * @author songscorpio
 *
 */
@Deprecated
public interface FrontendActiveScanFacade {

	/**
	 * 用户主扫接口
	 * @param activeScanRequestDTO
	 * @return
	 */
	public ActiveScanResponseDTO activeScan(ActiveScanRequestDTO activeScanRequestDTO);
	
	
	/**
	 * 公众号支付
	 * @param activeScanJsapiRequestDTO
	 * @return
	 */
	public ActiveScanResponseDTO activeScanJSAPI(ActiveScanJsapiRequestDTO activeScanJsapiRequestDTO);
	
}
