package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.core.nccashier.vo.CashierUserInfo;
import com.yeepay.g3.core.nccashier.vo.SignRelationInfo;
import com.yeepay.g3.facade.ncmember.dto.*;

import java.util.List;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.gateway.service
 *
 * @author pengfei.chen
 * @since 16/12/29 18:46
 */
public interface UserCenterService {
    /**
     * 用户中心查询商户的注册信息
     * @param merchantConfigQueryDTO
     * @return
     */
    public MerchantConfigRespDTO queryUserRegisterInfo(MerchantConfigQueryDTO merchantConfigQueryDTO);

    /**
     * 查询可共享的绑卡列表
     * @param getSharableReqDTO
     * @return
     */
    public GetSharableRespDTO queryShareBindBankList(GetSharableReqDTO getSharableReqDTO);

	/**
	 * 查询可用的绑卡列表(如果授权了，绑卡列表包含可共享的，如果没授权，相当于是卡账户的绑卡列表) 
	 * 三代会员授权字段返回false
	 * 
	 * @param merchantUserDTO
	 * @return
	 */
	public GetUsableRespDTO queryUseableBindBankList(MerchantUserDTO merchantUserDTO);

    /**
     * 请求授权(创建授权订单)
     * @param requestAuthorityReqDTO
     * @return
     */
    RequestAuthorityRespDTO shareBankAuthCreateOrder(RequestAuthorityReqDTO requestAuthorityReqDTO);

    /**
     * 授权发短信
     * @param authorityId
     * @return
     */
    public RequestAuthorityRespDTO shareBankAuthSendSms(long authorityId);

    /**
     * 授权确认
     * @param confirmAuthorityReqDTO
     * @return
     */
    public ConfirmAuthorityRespDTO comfirmshareBankAuth(ConfirmAuthorityReqDTO confirmAuthorityReqDTO);

	/**
	 * @title 根据卡号获取签约信息及对应的外部用户的签约关系信息
	 * @description
	 * @param cardN0
	 * @param externalUser
	 *            外部用户信息，可为空
	 * @return
	 */
	SignRelationInfo getSignCardInfoByCardN0(String cardN0, CashierUserInfo externalUser);


	/**
	 * @title 获取外部用户对应的可用签约关系列表
	 * @param externalUser
	 * @return
	 */
	List<SignRelationInfo> getSignRelationList(CashierUserInfo externalUser);

	/**
	 * @title 根据签约关系ID获取签约关系
	 * @param signRelationId
	 * @return
	 */
	SignRelationInfo getUserSignInfoBySignRelationId(String signRelationId);
	
}
