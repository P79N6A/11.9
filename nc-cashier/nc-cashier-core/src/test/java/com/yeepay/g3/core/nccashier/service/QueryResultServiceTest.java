package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.TradeSysCodeEnum;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by jimin.zhou on 18/1/2.
 */
public class QueryResultServiceTest extends BaseTest {

    @Resource
    private QueryResultService queryResultService;

    @Test
    public void test(){
        CommonUtil.getPpPayResultQueryState("1234");
    }

    @Test
    public void test2(){
        CombinedPaymentDTO combinedPaymentDto = new CombinedPaymentDTO();
        CashierQueryRequestDTO queryRequest = new CashierQueryRequestDTO();
        CashierQueryResponseDTO response = new CashierQueryResponseDTO();
        PaymentRecord paymentRecord = new PaymentRecord();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaySysCode("PAY_PROCCESOR");
        paymentRequest.setTradeSysNo(TradeSysCodeEnum.DS.toString());

        combinedPaymentDto.setPaymentRecord(paymentRecord);
        combinedPaymentDto.setPaymentRequest(paymentRequest);
        queryResultService.supplyQureyResult(combinedPaymentDto,queryRequest,response);
    }

}
