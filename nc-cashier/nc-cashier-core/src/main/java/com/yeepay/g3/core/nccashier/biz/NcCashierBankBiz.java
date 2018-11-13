/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * 银行（卡）相关biz
 * 
 * @author：zhen.tan
 * @since：2016年5月27日 上午9:31:25
 *
 */
public interface NcCashierBankBiz {

	/**
	 * 获取银行卡必填项
	 * @param requestDto
	 * @return
	 */
	public CardValidateResponseDTO getCardValidates(CardValidateRequestDTO requestDto);
	
	/**
	 * 获取支持银行列表
	 * @param requestId
	 * @return
	 */
	public SupportBanksResponseDTO supportBankList(long requestId,String cusType);
	/**
	 * 获取卡信息
	 * @param requestId
	 * @return
	 */

	public BankCardReponseDTO getBankCardInfo(long requestId,String cusType);

	/**
	 * 透传卡号校验
	 * @param requestId
	 * @return
     */
	public BasicResponseDTO validatePassBindId(long requestId);


	/**
	 * 是否是透传绑卡id的情况
	 * @param requestId
	 * @return
     */
	public PassBindIdDTO isPassBindId(long requestId);



	/**
         * 通过卡号获取卡bin信息
         * @param cardno
         * @return
         */
	public CardBinInfoDTO getCardBinInfo(String cardno);
	
	
	InstallmentBanksResponseDTO getBankList(long requestId);
	/**
	 * 预授权：获取绑卡信息，不处理透传信息
	 * @param requestId
	 * @param cusType
	 * @return
	 */
	BankCardReponseDTO getBankCardInfo4Preauth(long requestId, String cusType);
}
