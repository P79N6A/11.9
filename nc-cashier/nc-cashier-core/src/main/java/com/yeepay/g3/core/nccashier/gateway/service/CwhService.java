package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.core.nccashier.vo.CashierUserInfo;
import com.yeepay.g3.core.nccashier.vo.ExternalUserRequestDTO;
import com.yeepay.g3.facade.cwh.enumtype.UserType;
import com.yeepay.g3.facade.cwh.param.*;
import com.yeepay.g3.facade.nccashier.dto.BankCardDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

import java.util.List;

/**
 * 
 * @author：peile.fan,zhen.tan
 * @since：2016年5月19日 下午6:27:04
 * @version:
 */
public interface CwhService {

	/**
	 * 获取外部用户
	 * @param userDto
	 * @return
	 * @throws CashierBusinessException
	 */
	public ExternalUserDTO getExternalUser(ExternalUserRequestDTO userDto) throws CashierBusinessException; 
	
	/**
	 * 通过bindid获取绑卡信息
	 * @param bindId
	 * @return
	 */
	public BindCardDTO getBindCardInfoByBindId(Long bindId);
	
	/**
	 * 通过卡信息ID获取卡信息
	 * @param cardId
	 * @return
	 */
	public BankCardDetailDTO getBankCard(String cardId);
	
	/**
	 * 通过卡号获取卡信息
	 * @param cardNo
	 * @return
	 */
	public BankCardDetailDTO getBankCardDetailByCardNo(String cardNo);
	
	/**
	 * 根据临时卡ID获取临时卡信息
	 * @param tempCardId
	 * @return
	 */
	public PayTmpCardDTO getPayTmpCardByTempCardId(long tempCardId);
	
	/**
	 * 保存临时卡信息
	 * @param tmpCard
	 * @return
	 */
	public long addPayTmpCard(PayTmpCardDTO tmpCard);
	/**
	 * 根据外部用户ID和用户类型查询绑卡列表
	 * @param id
	 * @param external
	 * @return
	 */
	public List<BindCardDTO> getBindCardsByExternalId(String id,
			UserType external);

	/**
	 * 通过identityId和identityType和商户编号获取绑卡列表
	 */
	public List<BankCardDTO> getBindCardList(String merchantNo, String identityId,
			String identityType);



	public List<BankCardDTO> getBindCardListByBindId(String bindId, String identityId,
											 String identityType);


	/**
	 * 获取同人限制信息
	 * @param reqDTO
	 * @return
	 */
	public BindLimitInfoResDTO getBankCardLimitInfo(BindLimitInfoReqDTO reqDTO );
	/**
	 *  设置同人限制信息
	 * @param person
	 */
	public void setSamePersonLimit(ExternalUserUpdateRequestDTO externalUser);

	public BindParamResponseDTO unbindCard(
			UnbindParamRequestDTO unbindParamRequestDTO);
	/**
	 * 绑卡
	 * @param bindCardIdRequestDTO
	 */
	void bindCard(BindCardIdRequestDTO bindCardIdRequestDTO);

	/**
	 * 绑卡，供API一键支付使用
	 * @param bindCardIdRequestDTO
	 * @return 绑卡id
	 */
	String bindCard(BindCardInfoRequestDTO bindCardIdRequestDTO);

	/**
	 * 根据卡账户userId获取外部用户
	 * @param userId
	 * @return
	 */
	ExternalUserDTO getExternalUserById(String userId);


	/**
	 * 根据外部用户信息获取userId
	 * @param externalUser
	 * @return
	 */
	String getExternalUserId(CashierUserInfo externalUser);
}
