package com.yeepay.g3.core.nccashier.service.impl;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-07-17 17:18
 **/

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.APIPayResultQueryService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.nccashier.dto.APIPayResultQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.payprocessor.dto.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-07-17 17:18
 **/

@Service
public class APIPayResultQueryServiceImpl implements APIPayResultQueryService {


    @Resource
    private PayProcessorService payProcessorService;

    @Resource
    private PaymentProcessService paymentProcessService;



    @Override
    public PaymentRecord getRecordByIdUseRedis(long recordId) {
        PaymentRecord record = null;
        record = RedisTemplate.getTargetFromRedis(getRedisKey(recordId), PaymentRecord.class);
        if(record==null)
            record = paymentProcessService.findRecordByPaymentRecordId(recordId+"");
        if(record==null)
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
                    Errors.INPUT_PARAM_NULL.getMsg() + ", 请求单号未查询到记录");
        return record;
    }

    @Override
    public APIPayResultQueryResponseDTO gerPayResultByQueryPP(PaymentRecord record) {
        APIPayResultQueryResponseDTO queryResult = new APIPayResultQueryResponseDTO();
        if(PayTool.YSQ.name().equals(record.getPayTool())){	//预授权查询主单
            QueryRequestDTO dto = new QueryRequestDTO();
            dto.setOrderNo(record.getTradeSysOrderId());
            dto.setOrderSystem(record.getTradeSysNo());
            QueryResponseDTO responseDTO = payProcessorService.queryOrderResult(dto);
            queryResult.init(responseDTO.getPayOrderType().name(), responseDTO.getPaymentStatus().name());
        }
        else {	//查询子单
            PayRecordQueryRequestDTO dto = new PayRecordQueryRequestDTO();
            dto.setRecordNo(record.getPaymentOrderNo());
            PayRecordResponseDTO responseDTO = payProcessorService.query(dto);
            queryResult.init(responseDTO.getPayOrderType(),responseDTO.getTrxStatus().name());
        }
        return queryResult;
    }

    @Override
    public void saveRedisWhenQuerySucess(PaymentRecord record) {
        RedisTemplate.setCacheObjectSumValue(getRedisKey(record.getId()), record, 5 * 60 * 1000);
    }

    private String getRedisKey(long recordId){
        return CommonUtil.PREFIX_NCCASHIER + "QUERY_PAY_RESULT" + "_" + recordId;
    }

}
