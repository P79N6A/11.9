package com.yeepay.g3.core.payprocessor.external.service;

import java.net.SocketTimeoutException;

import com.yeepay.g3.component.member.dto.MemberFundTradeInfoDTO;
import com.yeepay.g3.component.member.enumtype.MemberTradeTypeEnum;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayResponseDTO;

/**
 * 
 * @author：zhangxh    
 * @since：2017年5月16日 上午11:23:45 
 * @version:
 */
public interface PersonalMemberPayService {
    
    /**
     * 会员余额支付
     * @param payParams
     * @return
     * @throws SocketTimeoutException 
     */
	String syncPay(PersonalMemberSyncPayRequestDTO requestDTO, PayRecord payRecord,PersonalMemberSyncPayResponseDTO response ) ;

    /**
     * 会员余额支付查询
     * @param orderNo
     * @return
     */
    MemberFundTradeInfoDTO queryPayInfoByOrderNo(String orderNo,MemberTradeTypeEnum tradeType);

}
