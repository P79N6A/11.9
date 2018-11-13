package com.yeepay.g3.core.nccashier.gateway.service.impl;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.RiskControlService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.dto.QuataDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.riskcontrol.facade.util.DoorgodProductType;
import com.yeepay.riskcontrol.facade.util.DoorgodProductionType;
import com.yeepay.riskcontrol.facade.v2.RcBlCheckReqDto;
import com.yeepay.riskcontrol.facade.v2.RcBlCheckRspDto;
import com.yeepay.riskcontrol.facade.v2.TradeLimitConfigQueryRequestDto;
import com.yeepay.riskcontrol.facade.v2.TradeLimitConfigQueryResponseDto;
import com.yeepay.riskcontrol.facade.v2.TradelimitDataQueryRequestDto;
import com.yeepay.riskcontrol.facade.v2.TradelimitDataQueryResponseDto;

/**
 * 风控服务实现类
 *
 */
@Service("riskControlService")
public class RiskControlServiceImpl extends NcCashierBaseService implements RiskControlService {

    private static final Logger logger = NcCashierLoggerFactory.getLogger(RiskControlServiceImpl.class);

    @Override
	public QuataDTO queryLimit(TradeLimitConfigQueryRequestDto requestDto) {

		TradeLimitConfigQueryResponseDto resDTO = null;
		try {
			resDTO = tradeLimitConfigQueryFacade.queryNew(requestDto);
			if(null!=resDTO){
				logger.info("风控服务resDTO:{}",ToStringBuilder.reflectionToString(resDTO));
				logger.info("invoke RiskControlServiceImpl.queryLimit..单笔限额："+resDTO.getSingleAmount()+"每日限额："+resDTO.getDayAmount()+"每月限额："+resDTO.getMonthAmount()+"单日限次："+resDTO.getDayCount()+"单月限次："+resDTO.getMonthCount());
				QuataDTO quataDTO=new QuataDTO();
				quataDTO.setOrderQuotaDou(resDTO.getSingleAmount());
				quataDTO.setMonthQuotaDou(resDTO.getMonthAmount());
				quataDTO.setDayQuotaDou(resDTO.getDayAmount());
				return quataDTO;
			}
		} catch (Exception e) {
			logger.error("query tradeLimitConfigError,e:", e); 
			return null;
		}
		return null;
	}
	
	@Override
	public RcBlCheckRspDto check(RcBlCheckReqDto requestDTO){
		requestDTO.setOccurTime(new Date());
		requestDTO.setProductId(DoorgodProductType.MERCHANT_CASHIER);
		requestDTO.setProduction(DoorgodProductionType.MERCHANT_CASHIER);
		requestDTO.setReferCheck(true);
		RcBlCheckRspDto response = doorgodBlCheckFacade.check(requestDTO);
		
		if(response == null || !response.isDealResult()){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		
		return response;
	}

	@Override
	public QuataDTO queryLimit(TradelimitDataQueryRequestDto requestDTO) {
		TradelimitDataQueryResponseDto resDTO = null;
		try {
			resDTO = tradeLimitConfigQueryFacade.queryTradelimitInfoByTradeinfo(requestDTO);
			if (null != resDTO) {
				logger.info("风控服务resDTO:{}", ToStringBuilder.reflectionToString(resDTO));
				logger.info("invoke RiskControlServiceImpl.queryLimit..单笔限额：" + resDTO.getSingleAmount() + "每日限额："
						+ resDTO.getDayAmount() + "每月限额：" + resDTO.getMonthAmount() + "单日限次：" + resDTO.getDayCount()
						+ "单月限次：" + resDTO.getMonthCount());
				if ("0000".equals(resDTO.getRetCode())) {
					QuataDTO quataDTO = new QuataDTO();
					quataDTO.setOrderQuotaDou(resDTO.getSingleAmount());
					quataDTO.setMonthQuotaDou(resDTO.getMonthAmount());
					quataDTO.setDayQuotaDou(resDTO.getDayAmount());
					return quataDTO;
				}
			}
		} catch (Exception e) {
			logger.error("query tradelimitInfoByTradeinfoError, e:", e);
			return null;
		}
		return null;
	}
}
