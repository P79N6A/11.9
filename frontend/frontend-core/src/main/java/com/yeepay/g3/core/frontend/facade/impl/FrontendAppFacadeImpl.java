package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.facade.frontend.dto.AppPayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.AppPayResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendAppFacade;

/**
 * 微信 app内支付接口（sdk，wap）
 * 
 * @author TML
 *
 */
//@Service
@Deprecated
public class FrontendAppFacadeImpl implements FrontendAppFacade {
//	
//	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendAppFacadeImpl.class);
//	
//	@Autowired
//	private FrontMPayBiz frontMPayBiz;

	public AppPayResponseDTO appH5Pay(AppPayRequestDTO appPayRequestDTO) {
//		try{
//            FeLoggerFactory.TAG_LOCAL.set("[用户H5请求]");
//            logger.info(appPayRequestDTO.toString());
//            AppPayResponseDTO appPayResponseDTO = frontMPayBiz.H5appPay(appPayRequestDTO);
//            logger.info(appPayResponseDTO.toString());
//            return appPayResponseDTO;
//        }finally {
//        	FeLoggerFactory.TAG_LOCAL.set(null);
//        }
		return null;
	}

	
}
