/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yeepay.g3.core.nccashier.biz.RiskControlBiz;
import com.yeepay.g3.core.nccashier.gateway.service.RiskControlService;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferRequestDTO;
import com.yeepay.riskcontrol.facade.util.DoorgodCheckFactorType;
import com.yeepay.riskcontrol.facade.v2.RcBlCheckReqDto;
import com.yeepay.riskcontrol.facade.v2.RcBlCheckRspDto;

/**
 * @author zhen.tan
 *
 */
@Component
public class RiskControlBizImpl extends NcCashierBaseBizImpl implements RiskControlBiz {

	
	@Resource
	private RiskControlService riskControlService;
	
	@Override
	public CheckRefferReponseDTO checkReffer(CheckRefferRequestDTO requestDTO) {
		
		CheckRefferReponseDTO responseDTO = new CheckRefferReponseDTO();
		try {
			RcBlCheckReqDto request = new RcBlCheckReqDto();
			request.setBizOrder(requestDTO.getBizOrderId());
			request.setSequenceId(requestDTO.getBizOrderId());
			
			Map<DoorgodCheckFactorType, Object> checkParams = new HashMap<DoorgodCheckFactorType, Object>();
			checkParams.put(DoorgodCheckFactorType.refer, requestDTO.getReffer());
			checkParams.put(DoorgodCheckFactorType.merchantNo, requestDTO.getMerchantAccountCode());
			request.setCheckParams(checkParams);
			//支付referer校验，都要走白名单
			request.setIsWhiteCheck("1");
			RcBlCheckRspDto response = riskControlService.check(request);
			responseDTO.setResultCode(response.getResultCode().getValue());
		} catch (Throwable e) {
			handleException(responseDTO, e);
		}
	
		return responseDTO;
	}

}
