package com.yeepay.g3.core.payprocessor.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.biz.AccountPayBiz;
import com.yeepay.g3.core.payprocessor.biz.FePayBiz;
import com.yeepay.g3.core.payprocessor.biz.NcPayBiz;
import com.yeepay.g3.core.payprocessor.biz.PersonalMemberPayBiz;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountSyncPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.CflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.CflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountSyncPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayResponseDTO;
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
import com.yeepay.g3.facade.payprocessor.facade.PayOrderFacade;

/**
 * @author chronos.
 * @createDate 2016/11/9.
 */
@Service("payOrderFacade")
public class PayOrderFacadeImpl implements PayOrderFacade {

	@Autowired
	private FePayBiz fePayBiz;

	@Autowired
	private NcPayBiz ncPayBiz;
	
	@Autowired
	private AccountPayBiz accountPayBiz;
	
	@Autowired
	private PersonalMemberPayBiz personalMemberPayBiz;

	@Override
	public OpenPayResponseDTO openRequest(OpenPayRequestDTO requestDTO) {
		return fePayBiz.openPay(requestDTO);
	}

	@Override
	public NetPayResponseDTO onlineRequest(NetPayRequestDTO requestDTO) {
		return fePayBiz.netPay(requestDTO);
	}

	@Override
	public NcPayOrderResponseDTO ncRequest(NcPayOrderRequestDTO requestDTO) {
		return ncPayBiz.ncRequest(requestDTO);
	}

	@Override
	public OpenPrePayResponseDTO openPrePay(OpenPrePayRequestDTO openPrePayRequestDTO) {
		return fePayBiz.openPrePay(openPrePayRequestDTO);
	}

	@Override
	public CflPayResponseDTO cflRequest(CflPayRequestDTO requestDTO) {
		return fePayBiz.cflPay(requestDTO);
	}
	
	@Override
	public PassiveScanPayResponseDTO passiveScanPay(PassiveScanPayRequestDTO requestDTO) {
		return fePayBiz.passiveScanPay(requestDTO);
	}

    @Override
    public AccountPayResponseDTO accountPay(AccountPayRequestDTO requestDTO) {
        return accountPayBiz.accountPay(requestDTO);
    }
    
    @Override
    public AccountSyncPayResponseDTO accountSyncPay(AccountPayRequestDTO requestDTO) {
        return accountPayBiz.accountSyncPay(requestDTO);
    }

    @Override
	public PersonalMemberSyncPayResponseDTO personalMemberSyncPay(
			PersonalMemberSyncPayRequestDTO requestDTO) {
		return personalMemberPayBiz.personalMemberSyncPay(requestDTO);
	}

    @Override
    public NcGuaranteeCflPrePayResponseDTO guaranteeCflPrePay(NcGuaranteeCflPrePayRequestDTO requestDTO) {
        return ncPayBiz.authCflPrePay(requestDTO);
    }

    @Override
    public NcGuaranteeCflPayResponseDTO guaranteeCflRequest(NcGuaranteeCflPayRequestDTO requestDTO) {
        return ncPayBiz.authCflRequest(requestDTO);
    }
	
}
