package com.yeepay.g3.core.frontend.facade.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.frontend.biz.QueryBiz;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendQueryFacade;

/**
 * 订单详情查询接口
 * 
 * @author songscorpio
 *
 */
@Service("frontendQueryFacade")
public class FrontendQueryFacadeImpl implements FrontendQueryFacade {
	
	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendQueryFacadeImpl.class);
	
	@Autowired
    private QueryBiz queryBiz;
	
	/**
	 * 查询订单的支付信息
	 * 请求参数校验
	   查询订单
	   组织返回参数
	 * @param dto
	 * @return
	 */
	public FrontendQueryResponseDTO queryOrderInfo(FrontendQueryRequestDTO frontendQueryRequestDTO){
		try{
			FeLoggerFactory.TAG_LOCAL.set("[查单]");
			logger.info(frontendQueryRequestDTO.toString());
			FrontendQueryResponseDTO frontendQueryResponseDTO = queryBiz.queryOrder(frontendQueryRequestDTO);
			logger.info(frontendQueryResponseDTO.toString());
			return frontendQueryResponseDTO;
		} finally {
	    	FeLoggerFactory.TAG_LOCAL.set(null);
	    }
	}
	
}
