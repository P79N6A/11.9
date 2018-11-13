package com.yeepay.g3.core.payprocessor.external.service;

import java.net.SocketTimeoutException;
import java.util.Date;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.facade.account.pay.dto.PayOrderQueryDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountSyncPayResponseDTO;

/**
 * 
 * @author：zhangxh    
 * @since：2017年5月16日 上午11:23:45 
 * @version:
 */
public interface AccountPayService {
    
    /**
     * 账户异步支付
     * @return
     * @throws SocketTimeoutException 
     */
    AccountPayResponseDTO pay(AccountPayRequestDTO requestDTO, PayRecord payRecord) throws SocketTimeoutException;
    
    /**
     * 账户同步支付
     * @return
     * @throws SocketTimeoutException 
     */
    AccountSyncPayResponseDTO paySync(AccountPayRequestDTO requestDTO, PayRecord payRecord) throws SocketTimeoutException;


    /**
     * 账户异步支付(新)
     * @return
     * @throws SocketTimeoutException
     */
    AccountPayResponseDTO enterprisePay(AccountPayRequestDTO requestDTO, PayRecord payRecord) throws SocketTimeoutException;

    /**
     * 账户同步支付(新)
     * @return
     * @throws SocketTimeoutException
     */
    AccountSyncPayResponseDTO enterprisePaySync(AccountPayRequestDTO requestDTO, PayRecord payRecord) throws SocketTimeoutException;


    /**
     * 账户支付查询
     * @param orderNo
     * @param trxTime
     * @return
     */
    PayOrderQueryDTO queryPayInfoByOrderNo(String orderNo,Date trxTime);

}
