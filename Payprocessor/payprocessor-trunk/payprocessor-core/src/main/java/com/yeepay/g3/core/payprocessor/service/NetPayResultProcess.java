package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import com.yeepay.g3.facade.frontend.dto.NetPayResultMessage;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author chronos.
 * @createDate 2016/12/16.
 */
@Service
public class NetPayResultProcess extends AbstractService {

    @Autowired
    private ResultProcessService resultProcessService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private CombAbstractService combAbstractService;

    public void processForFePayMsg(NetPayResultMessage resultMessage, PayRecord payRecord) {
        // 查询支付子表
        checkPayRecord(payRecord);
        processPayRecord(payRecord, resultMessage);
        if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
            //更新信息，然后发起退款
            //判断是否发起过退款
            if(reverseRecordDao.queryByRecordNo(payRecord.getRecordNo())==null){
                payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
                createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正退款");
            }
        }else if(TrxStatusEnum.DOING.name().equals(payRecord.getStatus())){
            resultProcessService.updatePaymentToSuccess(payRecord);
            notifyService.notify(payRecord);
            setPayResultToRedis(resultMessage.getRequestId());
        }
    }

    private void processPayRecord(PayRecord payRecord, NetPayResultMessage resultMessage) {
        payRecord.setBankId(resultMessage.getPayBank());
        payRecord.setBankOrderNo(resultMessage.getOrderNo());
        payRecord.setBankTrxId(resultMessage.getTransactionId());
        payRecord.setCost(resultMessage.getBankTotalCost());
        payRecord.setPayTime(resultMessage.getPaySuccessTime());
        payRecord.setCardType(
                resultMessage.getPayBankcardType() == null ? null : resultMessage.getPayBankcardType().name());
        payRecord.setFrpCode(resultMessage.getPayInterface());
        payRecord.setUpdateTime(new Date());
        //如果下单的时候，没有传basicProductCode，底层支付系统会补充
        //将底层支付系统补充的，回写到支付处理器的数据库中
        if(StringUtils.isBlank(payRecord.getBasicProductCode())) {
            payRecord.setBasicProductCode(resultMessage.getBasicProductCode());
        }
    }


    public void processForFePayMsgComb(NetPayResultMessage resultMessage, PayRecord payRecord) {
        CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
        checkPayRecordComb(payRecord, combPayRecord);
        processPayRecord(payRecord, resultMessage);
        combAbstractService.processCombResult(payRecord, combPayRecord);
    }


}
