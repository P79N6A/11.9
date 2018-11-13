package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.facade.nccashier.dto.QuataDTO;
import com.yeepay.riskcontrol.facade.v2.RcBlCheckReqDto;
import com.yeepay.riskcontrol.facade.v2.RcBlCheckRspDto;
import com.yeepay.riskcontrol.facade.v2.TradeLimitConfigQueryRequestDto;
import com.yeepay.riskcontrol.facade.v2.TradelimitDataQueryRequestDto;

/**
 * 风控服务类
 * @author xuewei.wang
 *
 */
public interface RiskControlService {
	
	/**
	 * 风控新提供的限额查询接口（单条查询）
	 * 
	 * @param requestDTO
	 * @return
	 */
	QuataDTO queryLimit(TradelimitDataQueryRequestDto requestDTO);
	
	/**
	 * 查询商户限额 - 风控老接口 已废弃
	 * @param reqDtO
	 * @return
	 */
	@Deprecated
	public QuataDTO queryLimit(TradeLimitConfigQueryRequestDto riskLimitVo);
	

	/**
	 * 风控检查
	 * @param requestDTO
	 * @return
	 */
	public RcBlCheckRspDto check(RcBlCheckReqDto requestDTO);
}
