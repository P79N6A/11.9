package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.yeepay.g3.core.nccashier.gateway.service.UserCenterService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.ncmember.dto.*;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;

/**
 * Description 用户中心的接口
 * PackageName: com.yeepay.g3.core.nccashier.gateway.service.impl
 *
 * @author pengfei.chen
 * @since 16/12/29 18:48
 */
@Service("userCenterService")
public class UserCenterServiceImpl extends NcCashierBaseService implements UserCenterService {
    Logger logger = LoggerFactory.getLogger(UserCenterServiceImpl.class);
    @Override
    public MerchantConfigRespDTO queryUserRegisterInfo(MerchantConfigQueryDTO merchantConfigQueryDTO) {
        MerchantConfigRespDTO merchantConfigRespDTO;
        try {
            merchantConfigRespDTO = memberMerchantConfigFacade.query(merchantConfigQueryDTO);
        }catch (Throwable e){
            logger.error("",e);
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        if(merchantConfigRespDTO == null ){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }else if(StringUtils.isNotBlank(merchantConfigRespDTO.getErrorCode())) {
            throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), merchantConfigRespDTO.getErrorCode(),
                    merchantConfigRespDTO.getErrorMessage());
        }
        return merchantConfigRespDTO;
    }

    @Override
    public GetSharableRespDTO queryShareBindBankList(GetSharableReqDTO getSharableReqDTO) {
        GetSharableRespDTO getSharableRespDTO;
        try {
            getSharableRespDTO = bindCardFacade.getSharableCardList(getSharableReqDTO);
        }catch (Throwable e){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        if(getSharableRespDTO == null ){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }else if(StringUtils.isNotBlank(getSharableRespDTO.getErrorCode())) {
            throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), getSharableRespDTO.getErrorCode(),
                    getSharableRespDTO.getErrorMessage());
        }
        return getSharableRespDTO;
    }

    @Override
    public GetUsableRespDTO queryUseableBindBankList(MerchantUserDTO merchantUserDTO) {
		GetUsableRespDTO getUsableRespDTO;
		try {
			getUsableRespDTO = bindCardFacade.getUsableBindList(merchantUserDTO);
		} catch (Throwable e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (getUsableRespDTO == null) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		} else if (StringUtils.isNotBlank(getUsableRespDTO.getErrorCode())) {
			throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), getUsableRespDTO.getErrorCode(),
					getUsableRespDTO.getErrorMessage());
		}
		return getUsableRespDTO;
    }

    @Override
    public RequestAuthorityRespDTO shareBankAuthCreateOrder(RequestAuthorityReqDTO requestAuthorityReqDTO) {
        RequestAuthorityRespDTO requestAuthorityRespDTO;
        try {
            requestAuthorityRespDTO = memberAuthorityFacade.requestAuthority(requestAuthorityReqDTO);
        }catch (Throwable e){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        if(requestAuthorityRespDTO == null ){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }else if(StringUtils.isNotBlank(requestAuthorityRespDTO.getErrorCode())) {
            throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), requestAuthorityRespDTO.getErrorCode(),
                    requestAuthorityRespDTO.getErrorMessage());
        }
        return requestAuthorityRespDTO;
    }

    @Override
    public RequestAuthorityRespDTO shareBankAuthSendSms(long authorityId) {
        RequestAuthorityRespDTO requestAuthorityRespDTO;
        try {
            requestAuthorityRespDTO = memberAuthorityFacade.sendAuthoritySms(authorityId);
        }catch (Throwable e){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        if(requestAuthorityRespDTO == null ){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }else if(StringUtils.isNotBlank(requestAuthorityRespDTO.getErrorCode())) {
            throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), requestAuthorityRespDTO.getErrorCode(),
                    requestAuthorityRespDTO.getErrorMessage());
        }
        return requestAuthorityRespDTO;
    }

    @Override
    public ConfirmAuthorityRespDTO comfirmshareBankAuth(ConfirmAuthorityReqDTO confirmAuthorityReqDTO) {
        ConfirmAuthorityRespDTO confirmAuthorityRespDTO;
        try {
            confirmAuthorityRespDTO = memberAuthorityFacade.confirmAuthority(confirmAuthorityReqDTO);
        }catch (Throwable e){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        if(confirmAuthorityRespDTO == null ){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }else if(StringUtils.isNotBlank(confirmAuthorityRespDTO.getErrorCode())) {
            throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), confirmAuthorityRespDTO.getErrorCode(),
                    confirmAuthorityRespDTO.getErrorMessage());
        }
        return confirmAuthorityRespDTO;
    }


	@Override
	public SignRelationInfo getSignCardInfoByCardN0(String cardN0, CashierUserInfo externalUser) {
		SignRelationReqDTO requestDTO = new SignRelationReqDTO();
		requestDTO.setCardNo(cardN0);
		if(externalUser!=null){
			externalUser.supplySignRelationReqDTO(requestDTO);
		}
		SignRelationDetailDTO response = cardSignOrNot(requestDTO);
		return buildSignRelationInfo(response);
	}


	@Override
	public List<SignRelationInfo> getSignRelationList(CashierUserInfo externalUser) {
		SignRelationQueryReqDTO requestDTO = externalUser.buildSignRelationQueryReqDTO();
		List<SignRelationDetailDTO> signRelationDetailDTOs = getSignRelationList(requestDTO);
		return transfer(signRelationDetailDTOs);
	}

	@Override
	public SignRelationInfo getUserSignInfoBySignRelationId(String signRelationId) {
		SignRelationDetailDTO signRelationInfo = getSignRelation(signRelationId);
		return buildSignRelationInfo(signRelationInfo);
	}
	
	/**
	 * @title 根据签约关系ID获取签约关系
	 * @param signRelationId
	 * @return
	 */
	private SignRelationDetailDTO getSignRelation(String signRelationId) {
		SignRelationDetailDTO signRelationInfo = null;
		try {
			Long signRid = Long.valueOf(signRelationId);
			signRelationInfo = bankCardSignFacade.getSignRelation(signRid);
		} catch (Throwable t) {
			logger.error("根据signRelationId=" + signRelationId + "获取外部用户的签约关系信息异常, e=", t);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		
		if (signRelationInfo != null && StringUtils.isNotBlank(signRelationInfo.getErrorCode())) {
			throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), signRelationInfo.getErrorCode(),
					signRelationInfo.getErrorMessage());
		} 
		return signRelationInfo;
	}
	
	/**
	 * 校验卡号是否已经签约
	 * 
	 * @param requestDTO
	 * @return
	 */
	private SignRelationDetailDTO cardSignOrNot(SignRelationReqDTO requestDTO) {
		SignRelationDetailDTO response = null;
		try {
			response = bankCardSignFacade.getSignRelationOrInfo(requestDTO);
		} catch (Throwable t) {
			logger.error("param=" + requestDTO + "调用用户中心校验卡号是否已签约时异常, e=", t);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (response != null && StringUtils.isNotBlank(response.getErrorCode())) {
			throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), response.getErrorCode(),
					response.getErrorMessage());
		}
		return response;
	}
	
	/**
	 * 将用户中心的签约关系对象转为收银台内部的签约关系对象
	 * 
	 * @param signRelationInfo
	 * @return
	 */
	private SignRelationInfo buildSignRelationInfo(SignRelationDetailDTO signRelationInfo) {
		if (signRelationInfo == null) {
			return null;
		}
		SignRelationInfo userSignRelation = new SignRelationInfo();
		if (signRelationInfo.getSignRelationId() != null && signRelationInfo.getSignRelationId() > 0) {
			userSignRelation.setSignRelationId(String.valueOf(signRelationInfo.getSignRelationId()));
		}
		userSignRelation.setSignRelationStatus(signRelationInfo.getSignRelationStatus());

		CashierUserInfo externalUser = new CashierUserInfo();
		externalUser.setExternalUserId(signRelationInfo.getUserId());
		externalUser.setUserNo(signRelationInfo.getIdentityId());
		externalUser.setUserType(signRelationInfo.getIdentityType());
		externalUser.setMerchantNo(signRelationInfo.getMerchantNo());
		externalUser.setType(MemberTypeEnum.JOINLY.name());
		userSignRelation.setExternalUser(externalUser);
		SignInfoDetailDTO signCardInfo = signRelationInfo.getSignInfoDetailDTO();
		if (signCardInfo != null) {
			BankInfo bank = new BankInfo();
			bank.setBankCode(signCardInfo.getBankCode());
			bank.setBankName(signCardInfo.getBankName());
			bank.standardBankCode();
			CardInfo card = new CardInfo();
			card.setCardNo(signCardInfo.getCardNo());
			card.setBank(bank);
			SignCardInfo signInfo = new SignCardInfo();
			if (signCardInfo.getCardSignId() != null && signCardInfo.getCardSignId() > 0) {
				signInfo.setSignInfoId(String.valueOf(signCardInfo.getCardSignId()));
			}
			signInfo.setSignInfoStatus(signCardInfo.getSignInfoStatus());
			signInfo.setSignOrganization(signCardInfo.getSignOrganization());
			signInfo.setPhoneNo(signCardInfo.getMobile()); 
			signInfo.setCardInfo(card);
			userSignRelation.setSignCardInfo(signInfo);
		}
		return userSignRelation;
	}
	
	private List<SignRelationInfo> transfer(List<SignRelationDetailDTO> signRelationDetailDTOs) {
		if(CollectionUtils.isEmpty(signRelationDetailDTOs)){
			return null;
		}
		List<SignRelationInfo> installmentSignRelationInfos = new ArrayList<SignRelationInfo>();
		for (SignRelationDetailDTO relationDetailDTO : signRelationDetailDTOs) {
			SignRelationInfo installmentSignRelationInfo = buildSignRelationInfo(relationDetailDTO);
			installmentSignRelationInfos.add(installmentSignRelationInfo);
		}
		return installmentSignRelationInfos;
	}
	
	private List<SignRelationDetailDTO> getSignRelationList(SignRelationQueryReqDTO requestDTO) {
		SignRelationListDTO signRelationList = null;
		try {
			signRelationList = bankCardSignFacade.getSignRelations(requestDTO);
		} catch (Throwable t) {
			logger.error("getSignRelationList error, param=" + requestDTO, t);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}

		if (signRelationList == null) {
			return null;
		}
		if (StringUtils.isNotBlank(signRelationList.getErrorCode())) {
			throw CommonUtil.handleException(SysCodeEnum.NCMEMBER.name(), signRelationList.getErrorCode(),
					signRelationList.getErrorMessage());
		}
		return signRelationList.getSignRelationList();
	}

}
