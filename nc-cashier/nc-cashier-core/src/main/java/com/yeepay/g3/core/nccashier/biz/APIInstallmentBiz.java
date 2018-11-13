package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentComfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentSmsRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryResponseDTO;

/**
 * 分期支付BIZ层
 * 
 * @author duangduang
 *
 */
public interface APIInstallmentBiz {

	/**
	 * 银行卡分期下单接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO request(APIInstallmentRequestDTO requestDTO);

	/**
	 * 银行卡分期发短验接口（前提：已下单，且是签约卡下单）
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO smsSend(APIInstallmentSmsRequestDTO requestDTO);

	/**
	 * 银行卡分期确认支付
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO confirmPay(APIInstallmentComfirmRequestDTO requestDTO);

	/**
	 * @Title 分期信息查询接口
	 * @Description 查询支持的分期银行、期数、最低限额、商户补贴费率、手续费收取方式
	 * @param rateInfoRequestDTO
	 * @return
	 */
	InstallmentInfoResponseDTO queryInstallmentRateInfos(InstallmentInfoRequestDTO rateInfoRequestDTO);

	/**
	 * @Title 外部用户签约关系查询接口
	 * @Description 查询出可用及不可用的所有签约关系
	 * @param queryDTO
	 * @return
	 */
	SignRelationQueryResponseDTO querySignRelationList(SignRelationQueryRequestDTO queryDTO);

}
