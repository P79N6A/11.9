package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.facade.frontend.dto.ActiveScanJsapiRequestDTO;
import com.yeepay.g3.facade.frontend.dto.ActiveScanRequestDTO;
import com.yeepay.g3.facade.frontend.dto.ActiveScanResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendActiveScanFacade;

/**
 * 用户主扫，公众号支付（jsapi）
 * 
 * @author TML
 *
 */
//@Service
@Deprecated
public class FrontendActiveScanFacadeImpl implements FrontendActiveScanFacade {
	
//	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendActiveScanFacadeImpl.class);
//	
//	@Autowired
//	private FrontMPayBiz frontMPayBiz;
	
	public ActiveScanResponseDTO activeScan(ActiveScanRequestDTO activeScanRequestDTO) {
//		try{
//            FeLoggerFactory.TAG_LOCAL.set("[用户主扫请求]");
//            logger.info(activeScanRequestDTO.toString());
//            ActiveScanResponseDTO activeScanResponseDTO = frontMPayBiz.activeScan(activeScanRequestDTO);
//            logger.info(activeScanResponseDTO.toString());
//            return activeScanResponseDTO;
//        }finally {
//        	FeLoggerFactory.TAG_LOCAL.set(null);
//        }
		return null;
    }

	@Override
	public ActiveScanResponseDTO activeScanJSAPI(ActiveScanJsapiRequestDTO activeScanJsapiRequestDTO) {
//		try{
//            FeLoggerFactory.TAG_LOCAL.set("[用户公众号请求]");
//            logger.info(activeScanJsapiRequestDTO.toString());
//            ActiveScanResponseDTO activeScanResponseDTO = frontMPayBiz.activeScanJSAPI(activeScanJsapiRequestDTO);
//            logger.info(activeScanResponseDTO.toString());
//            return activeScanResponseDTO;
//        }finally {
//        	FeLoggerFactory.TAG_LOCAL.set(null);
//        }
		return null;
	}
}
