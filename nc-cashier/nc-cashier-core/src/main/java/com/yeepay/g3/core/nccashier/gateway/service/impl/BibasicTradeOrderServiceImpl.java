package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.gateway.service.BibasicTradeOrderService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.pccashier.pay.dto.CashierQueryResponseDTO;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.gateway.service.impl
 *
 * @author pengfei.chen
 * @since 17/1/17 16:40
 */
@Service("bibasicTradeOrderService")
public class BibasicTradeOrderServiceImpl extends NcCashierBaseService implements BibasicTradeOrderService {
    @Override
    public CashierQueryResponseDTO queryOrderDetailFromBibasic(String orderNo) {
        CashierQueryResponseDTO cashierQueryResponseDTO;
        try {
            cashierQueryResponseDTO = cashierQueryFacade.queryOrder(orderNo);
           //cashierQueryResponseDTO =this.getMock(orderNo);
        } catch (Throwable e) {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        handleException(cashierQueryResponseDTO);
        return cashierQueryResponseDTO;
    }

    private void handleException(CashierQueryResponseDTO responseDTO){
        if(responseDTO== null ){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }else if(StringUtils.isNotBlank(responseDTO.getResponseCode())){
            throw CommonUtil.handleException(SysCodeEnum.PCCASHIER.name(), responseDTO.getResponseCode(), responseDTO.getResponseMsg());
        }
    }

}
