package com.yeepay.g3.core.payprocessor.biz.impl;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.biz.NcPayBiz;
import com.yeepay.g3.core.payprocessor.biz.RefundBiz;
import com.yeepay.g3.core.payprocessor.dao.CombPayRecordDao;
import com.yeepay.g3.core.payprocessor.dao.PayRecordDao;
import com.yeepay.g3.core.payprocessor.dao.ReverseRecordDao;
import com.yeepay.g3.core.payprocessor.entity.*;
import com.yeepay.g3.core.payprocessor.external.service.CsProcessService;
import com.yeepay.g3.core.payprocessor.external.service.RefundCenterService;
import com.yeepay.g3.core.payprocessor.service.PreAuthReverseRecordService;
import com.yeepay.g3.core.payprocessor.util.CommonUtils;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.MailSendHelper;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.dto.OperationRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OperationResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCancelRequestDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.RefundStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.facade.refund.dto.RefundResultNotifyDTO;
import com.yeepay.g3.facade.refund.enums.RefundResultNotifyStatusEnum;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/11/11.
 */
@Service("refundBiz")
public class RefundBizImpl extends BaseBizImpl implements RefundBiz {

    @Autowired
    private CsProcessService csProcessService;

    @Autowired
    private RefundCenterService refundCenterService;

    @Autowired
    private PreAuthReverseRecordService preAuthReverseRecordService;

    @Autowired
    private NcPayBiz ncPayBiz;

    private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(RefundBizImpl.class);

    @Override
    public void sendToCs() {
        PayLoggerFactory.TAG_LOCAL.set("[定时发送到清算中心]");
        Date end = new Date();
        Date start = DateUtils.addDay(end, -7);
        List<ReverseRecord> toRefundList = null;
        try {
            toRefundList = reverseRecordService.queryUnCsRecordByDate(start, end);
        } catch (Throwable th) {
            logger.error("[业务异常]", th);
        }
        if (!checkRefundList(toRefundList)) {
            PayLoggerFactory.TAG_LOCAL.remove();
            return;
        }
        for (ReverseRecord reverseRecord : toRefundList) {
            try {
                processCsRefund(reverseRecord);
            } catch (Throwable th) {
                logger.error("[业务异常]", th);
            }
        }
        PayLoggerFactory.TAG_LOCAL.remove();
    }

    @Override
    public void queryCsResult() {
        PayLoggerFactory.TAG_LOCAL.set("[定时查询清算中心入账状态]");
        Date end = new Date();
        Date start = DateUtils.addDay(end, -7);
        List<ReverseRecord> toRefundList = null;
        try {
            toRefundList = reverseRecordService.queryCsDoingRecordByDate(start, end);
        } catch (Throwable th) {
            logger.error("[业务异常]", th);
        }
        if (!checkRefundList(toRefundList)) {
            PayLoggerFactory.TAG_LOCAL.remove();
            return;
        }
        for (ReverseRecord record : toRefundList) {
            try {
                processCsQuery(record);
            } catch (Throwable th) {
                logger.error("[业务异常]", th);
            }

        }
        PayLoggerFactory.TAG_LOCAL.remove();
    }

    @Override
    public void sendToRefundCenter() {
        PayLoggerFactory.TAG_LOCAL.set("[定时发送到退款中心]");
        Date end = new Date();
        Date start = DateUtils.addDay(end, -7);
        List<ReverseRecord> toRefundList = null;
        try {
            toRefundList = reverseRecordService.queryUnRefundRecordByDate(start, end);
        } catch (Throwable th) {
            logger.error("[系统异常]", th);
        }
        if (!checkRefundList(toRefundList)) {
            PayLoggerFactory.TAG_LOCAL.remove();
            return;
        }
        for (ReverseRecord reverseRecord : toRefundList) {
            try {
                processCenterRefund(reverseRecord);
            } catch (Throwable th) {
                logger.error("[系统异常]", th);
            }
        }
        PayLoggerFactory.TAG_LOCAL.remove();

    }

    @Override
    public void receiveCenterNotify(RefundResultNotifyDTO refundResultNotifyDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[接收退款中心回调] - [recordNo = " + refundResultNotifyDTO.getRequestNo() + "]");
        try{
            ReverseRecord reverseRecord = reverseRecordService.queryByRecordNo(refundResultNotifyDTO.getRequestNo());
            if (reverseRecord ==null){
                logger.error("原订单不存在!orderNo:"+refundResultNotifyDTO.getRequestNo());
                return;
            }
            if (RefundResultNotifyStatusEnum.REFUND_SUCCESS.equals(refundResultNotifyDTO.getStatus())){
                //退款成功,更新退款状态
                reverseRecordService.updateRecordToSuccess(reverseRecord.getId());
            } else {
                //退款失败,邮件通知发起方
                Map<String,Object> errorMail = new HashMap<String, Object>();
                errorMail.put("recordNo",refundResultNotifyDTO.getRequestNo());
                errorMail.put("orderNo",refundResultNotifyDTO.getOrderNo());
                errorMail.put("errorMsg",refundResultNotifyDTO.getFailReason());
                MailSendHelper.sendEmail(errorMail,MailSendHelper.REFUND_ERROR_RULE,MailSendHelper.recipients);
            }
        } catch (Throwable th){
            logger.error("[系统异常]", th);
        } finally {
            PayLoggerFactory.TAG_LOCAL.remove();
        }
    }

    @Override
    public String receiveResponse(Map<String, Object> object) {
        if (object == null)
            return "ERROR";
        String recordNo = (String) object.get("bizRefundRequestNo");
        if (StringUtils.isBlank(recordNo))
            return "ERROR";
        PayLoggerFactory.TAG_LOCAL.set("[接受清算中心回调] - [recordNo = " + recordNo + "]");
        String result = "SUCCESS";
        try {
            ReverseRecord record = reverseRecordService.queryByRecordNo(recordNo);
            if (RefundStatusEnum.CSDOING.name().equals(record.getRefundStatus()))
                reverseRecordService.updateRecord(record.getId(), RefundStatusEnum.CSDOING.name(),
                    RefundStatusEnum.CSDONE.name());
            processCenterRefund(record);
        } catch (Throwable th){
            logger.error("[系统异常]", th);
            result = "ERROR";
        } finally {
            PayLoggerFactory.TAG_LOCAL.remove();
            return result;
        }
    }

    @Override
    public OperationResponseDTO batchRefund(OperationRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[批量补发退款] - [size = " + requestDTO.getRecordList().size() + "]");
        OperationResponseDTO responseDTO = new OperationResponseDTO();
        for (String recordNo : requestDTO.getRecordList()){
            try {
                ReverseRecord record = reverseRecordService.queryByRecordNo(recordNo);
                if (RefundStatusEnum.DONE.name().equals(record.getRefundStatus())
                        || RefundStatusEnum.RFDOING.name().equals(record.getRefundStatus())){
                     //忽略
                    responseDTO.setIgnore(responseDTO.getIgnore() + 1);
                    continue;
                } else if (RefundStatusEnum.INIT.name().equals(record.getRefundStatus())){
                    //发送到清算中心
                    processCsRefund(record);

                } else if (RefundStatusEnum.CSDOING.name().equals(record.getRefundStatus())){
                    //查询清算中心状态,并发送到退款中心
                    processCsQuery(record);
                } else if (RefundStatusEnum.CSDONE.name().equals(record.getRefundStatus())){
                    //发送到退款中心
                    processCenterRefund(record);
                }
                responseDTO.setSuccess(responseDTO.getSuccess() + 1);
            } catch (PayBizException e){
                logger.warn("[补发退款失败] [recordNo="+ recordNo +"]- [code = " +e.getDefineCode() + "] - [message = " + e.getMessage() + "]");
                responseDTO.getErrorList().add("[" + recordNo + "] - ["+ e.getDefineCode() +"]" + e.getMessage());
            } catch (Throwable th){
                logger.error("[补发退款失败] - [recordNo = " + recordNo + "]", th);
                responseDTO.getErrorList().add("[" + recordNo +"]" + th.getMessage());
            }
        }
        PayLoggerFactory.TAG_LOCAL.remove();
        return responseDTO;
    }


    private boolean checkRefundList(List refundList){
        if (refundList == null || refundList.size() < 1){
            logger.info("[无退款订单]");
            return false;
        }
        return true;
    }

    private void processCsRefund(ReverseRecord record){
        PaymentRequest payment = paymentRequestService.selectByPrimaryKey(record.getRequestId());
        PayRecord payRecord = payRecordService.queryRecordById(record.getRecordNo());
        // 如果是组合支付，不调清算中心，状态直接置成CSDONE
        if(payRecord.isCombinedPay()) {
            reverseRecordService.updateRecord(record.getId(), record.getRefundStatus(), RefundStatusEnum.CSDONE.name());
            return;
        }
        boolean result = csProcessService.processCsRefund(payment, payRecord);
        if (result){
            reverseRecordService.updateRecord(record.getId(), record.getRefundStatus(),
                    RefundStatusEnum.CSDOING.name());
        }
    }

    /**
     * 预授权订单冲正走ncpay撤销
     * 其他订单冲正走退款中心
     * @param reverse
     */
    private void processCenterRefund(ReverseRecord reverse){
        PaymentRequest payment = paymentRequestService.selectByPrimaryKey(reverse.getRequestId());
        PayRecord payRecord = payRecordService.queryRecordById(reverse.getRecordNo());
        // 预授权的订单走ncpay
        if(PayOrderType.PREAUTH_RE.name().equals(payRecord.getPayOrderType())
                || PayOrderType.PREAUTH_CM.name().equals(payRecord.getPayOrderType())
                || PayOrderType.PREAUTH_CC.name().equals(payRecord.getPayOrderType())
                || PayOrderType.PREAUTH_CL.name().equals(payRecord.getPayOrderType())) {
            processPreAuthReverse(payment, payRecord, reverse);
        }else {
            boolean result = refundCenterService.processRefund(payment, payRecord, reverse);
            if (result){
                reverseRecordService.updateRecord(reverse.getId(), RefundStatusEnum.CSDONE.name(),
                        RefundStatusEnum.RFDOING.name());
            }
        }
    }

    private void processCsQuery(ReverseRecord record){
        boolean result = csProcessService.queryCsRecord(record.getRecordNo());
        if (result) {
            //更新状态为cs处理完成
            reverseRecordService.updateRecord(record.getId(), record.getRefundStatus(),
                    RefundStatusEnum.CSDONE.name());
            //发送到退款中心
            processCenterRefund(record);
        }
    }


    /**
     * 处理预授权冲正
     * @param payment
     * @param payRecord
     */
    private void processPreAuthReverse(PaymentRequest payment, PayRecord payRecord, ReverseRecord reverse) {
        // 只有预授权、预授权完成的订单才能发起冲正
        if(!PayOrderType.PREAUTH_RE.name().equals(payRecord.getPayOrderType())) {
            return;
        }
        // 创建预授权冲正记录
        PreAuthReverseRecord preAuthReverseRecord = createPreAuthReverseRecord(payment, payRecord, reverse);
        ncPayBiz.ncPreAuthReverseCancel(preAuthReverseRecord, payment);
    }

    /**
     * 创建预授权（完成）撤销记录
     * @param payment
     * @param payRecord
     */
    private PreAuthReverseRecord createPreAuthReverseRecord(PaymentRequest payment, PayRecord payRecord, ReverseRecord reverse) {
        PreAuthReverseRecord preAuthReverseRecord = new PreAuthReverseRecord();
        preAuthReverseRecord.setRequestId(payment.getId());
        preAuthReverseRecord.setRecordNo(payRecord.getRecordNo());
        preAuthReverseRecord.setReverseNo(generateReverseNo());
        preAuthReverseRecord.setOrgPaymentNo(payRecord.getPaymentNo());
        preAuthReverseRecord.setOrgPayOrderType(payRecord.getPayOrderType());
        // 撤销
        if(PayOrderType.PREAUTH_RE.name().equals(payRecord.getPayOrderType())) {
            preAuthReverseRecord.setPayOrderType(PayOrderType.PREAUTH_CL.name());
        // 完成撤销
        } else if(PayOrderType.PREAUTH_CM.name().equals(payRecord.getPayOrderType())) {
            preAuthReverseRecord.setPayOrderType(PayOrderType.PREAUTH_CC.name());
        }
        preAuthReverseRecord.setCancelStatus(TrxStatusEnum.DOING.name());
        preAuthReverseRecord.setRequestTime(new Date());
        preAuthReverseRecord.setRemark(reverse.getRemark());
        // 获取ncpay的biz
        Long bizType = 0L;
        ExtendedInfo extendedInfo = payRecord.getExtendedInfo();
        if(extendedInfo != null) {
            Map<String, String> extParam = extendedInfo.getExtParam();
            if(MapUtils.isNotEmpty(extParam)) {
                String bizTypeString = extParam.get("bizType");
                if(StringUtils.isNotBlank(bizTypeString)) {
                    try {
                        bizType = Long.valueOf(bizTypeString);
                    } catch (Exception e) {
                        logger.error("预授权冲正获取bizType失败，recordNo=" + payRecord.getRecordNo(), e);
                    }
                }
            }
        }
        preAuthReverseRecord.setBiz(bizType);
        preAuthReverseRecordService.add(preAuthReverseRecord);
        return preAuthReverseRecord;
    }


    private String generateReverseNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        String dateStr = sdf.format(new Date());
        // 类型+日期+4位随机数
        String orderId = "REVERSE" + dateStr + CommonUtils.makeOrderNumber(4);
        return orderId;
    }
}
