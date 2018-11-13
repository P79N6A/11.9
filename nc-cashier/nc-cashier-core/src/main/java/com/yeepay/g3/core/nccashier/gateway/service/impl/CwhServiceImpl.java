package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.CashierUserInfo;
import com.yeepay.g3.core.nccashier.vo.ExternalUserRequestDTO;
import com.yeepay.g3.facade.cwh.enumtype.BankCardType;
import com.yeepay.g3.facade.cwh.enumtype.BindCardStatus;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.cwh.enumtype.UserType;
import com.yeepay.g3.facade.cwh.param.*;
import com.yeepay.g3.facade.nccashier.dto.BankCardDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author：peile.fan
 * @since：2016年5月19日 下午6:26:58
 * @version:
 */
@Service("cwhService")
public class CwhServiceImpl extends NcCashierBaseService implements CwhService {
	public static final Logger logger = NcCashierLoggerFactory.getLogger(CwhServiceImpl.class);

	@Override
	public ExternalUserDTO getExternalUser(ExternalUserRequestDTO userDto) throws CashierBusinessException {
		
		ExternalUserDTO dto = externalUserCwhFacade.getUser(userDto.getMerchantAccount(),userDto.getIdentityId(),
				userDto.getIdentityType(),userDto.getUserName(),userDto.getUserIdentityNo());
	
		return dto;
	}
	
	@Override
	public BindCardDTO getBindCardInfoByBindId(Long bindId){
		BindCardDTO bindCardDTO = bindCardCwhFacade.getBindCardInfoById(bindId);
		return bindCardDTO;
	}
	
	@Override
	public BankCardDetailDTO getBankCard(String cardId) {
		BankCardDetailDTO bankCardDetailDTO = bankCardCwhFacade.getBankCard(cardId);
		return bankCardDetailDTO;
	}
	
	
	@Override
	public BankCardDetailDTO getBankCardDetailByCardNo(String cardNo) {
		BankCardDetailDTO bankCardDetailDTO = bankCardCwhFacade.getBankCardDetailByCardNo(cardNo);
	
		return bankCardDetailDTO;
	}
	
	@Override
	public PayTmpCardDTO getPayTmpCardByTempCardId(long tempCardId) {
		PayTmpCardDTO tmpcard = null;
		try{
		 tmpcard=payTmpCardCwhFacade.getTmpCard(tempCardId);
		}catch(Throwable e){
			logger.info("获取临时卡信息异常，临时卡ID为"+tempCardId,e);
		}
		
		return tmpcard;
	}
	
	@Override
	public long addPayTmpCard(PayTmpCardDTO tmpCard) {
		long tmpCardId=payTmpCardCwhFacade.add(tmpCard);
		
		if(tmpCardId==0){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		return tmpCardId;
	}

	@Override
	public List<BindCardDTO> getBindCardsByExternalId(String id,
			UserType external) {
		List<BindCardDTO> bindCardList = null;
		try{
			bindCardList = bindCardCwhFacade.getBindCardTotalList(id, external, null, BindCardStatus.VALID);
		}catch(Throwable e){
			logger.error("获取绑卡列表异常"+e);
		}
				
		return bindCardList;
	}

	@Override
	public List<BankCardDTO> getBindCardList(String merchantNo, String identityId,
			String identityType) {

		List<BankCardDTO> banklist = null;
		if (StringUtils.isNotEmpty(identityId) && StringUtils.isNotEmpty(identityType)) {
			ExternalUserDTO externalUserDTO = this.getExternalUser(merchantNo, identityId,
					IdentityType.valueOf(identityType), null, null);
			if (null != externalUserDTO) {
				List<BindCardDTO> bindCardList =
						this.getBindCardList(externalUserDTO.getId(), UserType.EXTERNAL);
				if (null != bindCardList && bindCardList.size() > 0) {
					banklist = new ArrayList<BankCardDTO>();
					for (BindCardDTO bindCardDTO : bindCardList) {
						BankCardDTO bindcard = makeBindCard(bindCardDTO);
						banklist.add(bindcard);
					}
				}
			}
		}
		return banklist;

	}

	@Override
	public List<BankCardDTO> getBindCardListByBindId(String bindId, String identityId, String identityType) {
		List<BankCardDTO> banklist = null;
		UserType userType = null;
		if (StringUtils.isNotEmpty(identityId) && StringUtils.isNotEmpty(identityType)) {
			if (MemberTypeEnum.YIBAO.name().equals(identityType))
				userType = UserType.MEMBER;
			else
				userType = UserType.EXTERNAL;
			List<BindCardDTO> bindCardList = this.getBindCardList(identityId,userType ,bindId);
			if (null != bindCardList && bindCardList.size() > 0) {
				banklist = new ArrayList<BankCardDTO>();
				for (BindCardDTO bindCardDTO : bindCardList) {
					BankCardDTO bindcard = makeBindCard(bindCardDTO);
					banklist.add(bindcard);
				}
			}
		}
		return banklist;
	}


	/**
	 * 获取外部用户
	 */

	private ExternalUserDTO getExternalUser(String merchantAccount, String identityId,
			IdentityType identityType, String userName, String userIdentityNo)
					throws CashierBusinessException {
		ExternalUserDTO dto = externalUserCwhFacade.getUser(merchantAccount, identityId,
				identityType, userName, userIdentityNo);
		return dto;
	}

	private List<BindCardDTO> getBindCardList(String userId, UserType userType) {
		List<BindCardDTO> list = bindCardCwhFacade.getBindCardTotalList(userId, userType, null,
				BindCardStatus.VALID);;
		return list;
	}


	private List<BindCardDTO> getBindCardList(String userId, UserType userType,String bindId) {
		List<BindCardDTO> list = bindCardCwhFacade.getBindCardTotalList(userId, userType, Long.parseLong(bindId),
				BindCardStatus.VALID);;
		return list;
	}

	private BankCardDTO makeBindCard(BindCardDTO cwhbindcard) {
		BankCardDTO bindcard = new BankCardDTO();
		bindcard.setBankCode(cwhbindcard.getBankCode());
		bindcard.setBankName(cwhbindcard.getBankName());
		bindcard.setBindid(cwhbindcard.getBindId());
		String cardno = cwhbindcard.getCardNo();
		bindcard.setCardlater(cardno.substring(cardno.length() - 4, cardno.length()));
		if (BankCardType.CREDITCARD.equals(cwhbindcard.getBankCardType())) {
			bindcard.setCardtype(CardTypeEnum.CREDIT);
		} else if (BankCardType.DEBITCARD.equals(cwhbindcard.getBankCardType())) {
			bindcard.setCardtype(CardTypeEnum.DEBIT);
		}
		bindcard.setCardno(cardno);
		bindcard.setIdno(cwhbindcard.getIdcard());
		bindcard.setOwner(cwhbindcard.getOwner());
		bindcard.setNeedCheck(true);
		bindcard.setPhoneNo(cwhbindcard.getBankMobile());
		bindcard.setYpMobile(cwhbindcard.getYbMobile());
		return bindcard;
	}

	@Override
	public BindLimitInfoResDTO getBankCardLimitInfo(BindLimitInfoReqDTO reqDTO ) {
		BindLimitInfoResDTO bindLimitInfoResDTO = null;
		try{
		 bindLimitInfoResDTO = cardUserInfoQueryFacade.queryBindLimitInfo(reqDTO);
		}catch(Throwable e){
		logger.error("获取卡账户限制信息异常",e);
		logger.error("[monitor],event:nccashier_checkPassInfo_getSamePersonInfo error,merchantNo:{}",reqDTO.getMerchantNo());
		}
		return bindLimitInfoResDTO;
	}
	/**
	 * 	设置同人限制值
	 */
	@Override
	public void setSamePersonLimit(ExternalUserUpdateRequestDTO externalUser) {
		logger.info("设置同人限制值信息信息"+externalUser.toString());
		try{
			ResponseDTO external = externalUserCwhFacade.updateExternalUserById(externalUser);
			if(null!=external){
				logger.error("[monitor],event:nccashier_setSamePersonLimit error,externalUser:{}",externalUser);//设置同人限制值失败，打印报警日志
			}
			logger.info("设置同人限制信息成功,外部用户ID为"+externalUser.getId());
		}catch(Throwable e){
			logger.error("[monitor],event:nccashier_setSamePersonLimit error,externalUser:{}",externalUser);
			logger.error("设置同人限制值失败+"+externalUser.toString(),e);//设置同人限制值失败，打印报警日志
			
		}
	}

	@Override
	public BindParamResponseDTO unbindCard(
			UnbindParamRequestDTO bindParamRequestDTO) {
		logger.info("解绑卡信息"+bindParamRequestDTO.toString());
		BindParamResponseDTO unbindRes = null;
		try{
			unbindRes = bindCardCwhFacade.unbindById(bindParamRequestDTO);
			logger.info("解绑卡成功，绑卡ID为"+unbindRes.getBindId());
		}catch(Throwable e){
			logger.error("[monitor],event:nccashier_unbindCard error,bindParamRequestDTO:{}",bindParamRequestDTO);
			logger.error("解绑卡信息报错"+bindParamRequestDTO,e);
		}
		
		return unbindRes;
	}
	public void bindCard(BindCardIdRequestDTO bindCardIdRequestDTO) {
		try {
			BindCardResponseDTO res = bindCardCwhFacade.checkAndBindCardInfo(bindCardIdRequestDTO);
			if (StringUtils.isNotBlank(res.getBindId())) {
				logger.info("绑卡成功，返回的绑卡ID为" + res.getBindId());
			}
		} catch (Throwable e) {
			logger.error("调用卡账户绑卡异常", e);
		}
	}

	@Override
	public String bindCard(BindCardInfoRequestDTO bindCardIdRequestDTO) {
		try {
			BindCardResponseDTO res = bindCardCwhFacade.saveAndBindCardInfo(bindCardIdRequestDTO);
			if (StringUtils.isNotBlank(res.getBindId())) {
				logger.info("绑卡成功，返回的绑卡ID为" + res.getBindId());
			}
			return res.getBindId();
		} catch (Throwable e) {
			logger.error("调用卡账户绑卡异常", e);
			return null;
		}
	}

	@Override
	public ExternalUserDTO getExternalUserById(String userId) {
		try {
			ExternalUserDTO user = externalUserCwhFacade.getUserByUserId(userId);
			return user;
		}catch (Throwable e) {
			logger.error("getExternalUserById()调用卡账户externalUserCwhFacade.getUserByUserId 异常,userId=" + userId + ",异常=", e);
			return null;
		}

	}

	@Override
	public String getExternalUserId(CashierUserInfo externalUser) {
		if(externalUser.getUserType().equals(MemberTypeEnum.YIBAO.name())){
			return null;
		}
		ExternalUserDTO externalUserDTO = getExternalUser(externalUser.getMerchantNo(), externalUser.getUserNo(),
				IdentityType.valueOf(externalUser.getUserType()), null, null);
		if (externalUserDTO == null) {
			return null;
		}
		return externalUserDTO.getId();
	}
}
