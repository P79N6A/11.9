package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryResponseDTO;

/**
 * 银行卡分期对外查询接口
 * 
 * @author duangduang
 *
 */
public interface BankCardInstallmentQueryFacade {

	/**
	 * 银行卡分期支持银行及期数相关信息查询接口
	 * 
	 * @description include 支持的银行、期数、手续费、最低限额信息
	 * @param rateInfoRequestDTO
	 * @return
	 */
	InstallmentInfoResponseDTO queryInstallmentRateInfos(InstallmentInfoRequestDTO rateInfoRequestDTO);

	/**
	 * 查询商户用户在易宝的签约关系集合
	 * 
	 * @description 签约关系对应的银行若已不支持，则该签约关系进入不支持列表。
	 * @param queryDTO
	 * @return
	 */
	SignRelationQueryResponseDTO querySignRelationList(SignRelationQueryRequestDTO queryDTO);
}
