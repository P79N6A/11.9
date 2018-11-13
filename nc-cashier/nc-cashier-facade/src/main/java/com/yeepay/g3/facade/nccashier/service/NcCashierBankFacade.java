package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

/**
* @author zhen.tan
* @since：2016年5月25日 下午6:27:39
*/
public interface NcCashierBankFacade {

	/**
	 * 取得卡必填项
	 * @param requestDto
	 * @return
	 */
	public CardValidateResponseDTO getCardValidates(CardValidateRequestDTO requestDto);

	/**
	 * 获得支持银行列表
	 * @param requestId
	 * @return
	 * @throws CashierBusinessException
	 */
	public SupportBanksResponseDTO supportBankList(long requestId,String cusType);


	/**
	 * 获取卡信息
	 */
	public BankCardReponseDTO getBankCardInfo(long requestId,String cusType);

	/**
	 * 透传绑卡id校验
	 * @param requestId
	 * @return
     */
	public BasicResponseDTO validatePassBindId(long requestId);


	/**
	 * 是否是透传卡号的情况
	 * @param requestId
	 * @return
     */
	public PassBindIdDTO isPassBindId(long requestId);



	/**
	 * 通过银行卡获取卡bin信息
	 * @param cardno
	 * @return
	 */
	public CardBinInfoDTO getCardBinInfo(String cardno);
	
	/**
	 * 获取银行卡分期支持的银行列表，最低限额大于订单金额的银行放在不可用列表
	 * 
	 * @param requestId
	 * @return
	 */
	InstallmentBanksResponseDTO getInstallmentBankList(long requestId);
	/**
	 * 预授权：获取绑卡信息，不处理透传项
	 * @param requestId
	 * @param cusType
	 * @return
	 */
	BankCardReponseDTO getBankCardInfo4Preauth(long requestId, String cusType);
	
}
