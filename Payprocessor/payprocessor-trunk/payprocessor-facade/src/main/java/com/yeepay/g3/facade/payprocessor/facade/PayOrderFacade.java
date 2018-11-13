package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.AccountPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountSyncPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.CflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.CflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayResponseDTO;

/***
 * 支付处理器下单接口
 */
public interface PayOrderFacade {

	/**
	 * 开放支付下单接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	OpenPayResponseDTO openRequest(OpenPayRequestDTO requestDTO);

	/**
	 * 网银下单接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	NetPayResponseDTO onlineRequest(NetPayRequestDTO requestDTO);

	/**
	 * 无卡下单接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	NcPayOrderResponseDTO ncRequest(NcPayOrderRequestDTO requestDTO);

	/**
	 * 开发支付预路由接口
	 * 
	 * @param openPrePayRequestDTO
	 * @return
	 */
	OpenPrePayResponseDTO openPrePay(OpenPrePayRequestDTO openPrePayRequestDTO);

	/**
	 * 分期下单接口
	 * @param requestDTO
	 * @return
	 */
	CflPayResponseDTO cflRequest(CflPayRequestDTO requestDTO);
	
	/**
	 * 被扫支付接口
	 * @param requestDTO
	 * @return
	 */
	PassiveScanPayResponseDTO passiveScanPay(PassiveScanPayRequestDTO requestDTO);
	
	/**
	 * 会员账户支付
	 * @param requestDTO
	 * @return
	 */
	AccountPayResponseDTO accountPay(AccountPayRequestDTO requestDTO);
	
	/**
	 * 会员账户支付同步接口
	 * @param requestDTO
	 * @return
	 */
	AccountSyncPayResponseDTO accountSyncPay(AccountPayRequestDTO requestDTO);
	
	/**
	 * 个人会员下单即支付同步接口
	 */
	PersonalMemberSyncPayResponseDTO personalMemberSyncPay(PersonalMemberSyncPayRequestDTO requestDTO);

	/**
     * 担保分期预路由
     * 
     * @param requestDTO
     * @return {@link NcGuaranteeCflPrePayResponseDTO}
     */
    NcGuaranteeCflPrePayResponseDTO guaranteeCflPrePay(NcGuaranteeCflPrePayRequestDTO requestDTO);
    
    /**
     * 担保分期下单
     * 
     * @param requestDTO
     * @return {@link NcGuaranteeCflPayResponseDTO}
     */
    NcGuaranteeCflPayResponseDTO guaranteeCflRequest(NcGuaranteeCflPayRequestDTO requestDTO);
}
