package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.NotifyBiz;
import com.yeepay.g3.core.payprocessor.biz.QueryBiz;
import com.yeepay.g3.core.payprocessor.biz.RefundBiz;
import com.yeepay.g3.core.payprocessor.biz.impl.NotifyBizImpl;
import com.yeepay.g3.core.payprocessor.dao.CombPayRecordDao;
import com.yeepay.g3.core.payprocessor.dao.PayRecordDao;
import com.yeepay.g3.core.payprocessor.dao.ReverseRecordDao;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.dto.OperationRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OperationResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.RefundStatusEnum;
import com.yeepay.g3.facade.payprocessor.facade.PayProcessorOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chronos.
 * @createDate 2016/11/24.
 */
@Service("payProcessorOperationFacade")
public class PayProcessorOperationFacadeImpl implements PayProcessorOperationFacade {

    @Autowired
    private QueryBiz queryBiz;

    @Autowired
    private NotifyBiz notifyBiz;

    @Autowired
    private RefundBiz refundBiz;

    @Override
    public OperationResponseDTO batchRepair(OperationRequestDTO requestDTO) {
        return queryBiz.batchRepair(requestDTO);
    }

    @Override
    public OperationResponseDTO batchReNotify(OperationRequestDTO requestDTO) {
        return notifyBiz.batchReNotify(requestDTO);
    }

    @Override
    public OperationResponseDTO batchRefund(OperationRequestDTO requestDTO) {
        return refundBiz.batchRefund(requestDTO);
    }
}
