package com.yeepay.g3.core.payprocessor.service;

import java.sql.Timestamp;
import java.util.Date;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.dto.AccountPayResultMessage;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.account.pay.dto.PayOrderQueryDTO;
import com.yeepay.g3.facade.account.pay.dto.PayResultDTO;
import com.yeepay.g3.facade.account.pay.enums.RequestStatusEnum;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 处理账户支付的消息回调
 * 
 * @author：zhangxh
 * @since：2017年5月27日 下午6:05:08
 * @version:
 */
@Service
public class AccountPayResultProccess extends AbstractService {
    
    private static final Logger logger = PayLoggerFactory.getLogger(AccountPayResultProccess.class);

    @Autowired
    private ResultProcessService resultProcessService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private CombAbstractService combAbstractService;


    /**
     * 处理mq消息
     * @param resultMessage
     */
    public void processForAccountPayMsg(AccountPayResultMessage resultMessage, PayRecord payRecord) {

        if (RequestStatusEnum.FAIL.equals(resultMessage.getStatus())) {
            // 失败则记录错误信息，并通知收银台（不通知业务方）
            checkPayRecordForFail(payRecord);
            updatePayRecordToFail(payRecord, resultMessage);
            setPayResultToRedis(resultMessage.getOrderNo());
        } else if (RequestStatusEnum.SUCCESS.equals(resultMessage.getStatus())) {
            // 成功则记录订单信息、通知业务方、通知收银台
            // 查询支付子表
            checkPayRecord(payRecord);
            processPayRecord(payRecord, resultMessage);
            if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
                // 更新信息，然后发起退款
                // 判断是否发起过退款
                if (reverseRecordDao.queryByRecordNo(payRecord.getRecordNo()) == null) {
                    payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
                    createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), ConstantUtils.getAccountPayRefundRemark());
                }
            } else if (TrxStatusEnum.DOING.name().equals(payRecord.getStatus())) {
                resultProcessService.updatePaymentToSuccess(payRecord);
                notifyService.notify(payRecord);
                setPayResultToRedis(resultMessage.getOrderNo());
            }
        } else {
            // 其余状态不予处理
        }

    }

    /**
     * 处理mq消息，添加组合支付
     * @param resultMessage
     */
    public void processForAccountPayMsgComb(AccountPayResultMessage resultMessage, PayRecord payRecord) {
        if (RequestStatusEnum.FAIL.equals(resultMessage.getStatus())) {
            // 失败则记录错误信息，并通知收银台（不通知业务方）
            checkPayRecordForFail(payRecord);
            updatePayRecordToFail(payRecord, resultMessage);
            combAbstractService.updateFailByRecordNo(payRecord);
            setPayResultToRedis(resultMessage.getOrderNo());
        }else if(RequestStatusEnum.SUCCESS.equals(resultMessage.getStatus())) {
            // 检查第一支付子单，第二支付子单状态
            CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
            checkPayRecordComb(payRecord, combPayRecord);
            processPayRecord(payRecord, resultMessage);
            combAbstractService.processCombResult(payRecord, combPayRecord);
        }else {
            // 其余状态不予处理
        }
    }



    /**
     * 补单使用，根据查询的记过，进行处理
     * 
     * @param payResponseDTO
     * @return
     */
    public PayRecord processForAccountPayResponse(PayOrderQueryDTO payResponseDTO, PayRecord payRecord) {
        // 查询支付子表
        checkPayRecord(payRecord);
        AccountPayResultMessage resultMessage = transformResultMessage(payResponseDTO);
        processPayRecord(payRecord, resultMessage);
       
    		
        if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
            // 更新信息，然后发起退款
            // 判断是否发起过退款
            if (reverseRecordDao.queryByRecordNo(payRecord.getRecordNo()) == null) {
            	String reverseRemark ="冲正退款";
                String reverseRemarkAccount = ConstantUtils.getAccountPayRefundRemark();
	            	if(PayOrderType.ACCOUNT.name().equals(payRecord.getPayOrderType())){
	        			reverseRemark = reverseRemarkAccount;
	        		}
                payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
                createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), reverseRemark);
            }
        } else if (TrxStatusEnum.DOING.name().equals(payRecord.getStatus())) {
            resultProcessService.updatePaymentToSuccess(payRecord);
            notifyService.notify(payRecord);
        }
        return payRecord;
    }


    /**
     * 补单使用，根据查询的记过，进行处理
     * 添加组合支付
     */
    public PayRecord processForAccountPayResponseComb(PayOrderQueryDTO payResponseDTO, PayRecord payRecord) {
        // 检查第一支付子单，第二支付子单状态
        CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
        checkPayRecordComb(payRecord, combPayRecord);
        AccountPayResultMessage resultMessage = transformResultMessage(payResponseDTO);
        processPayRecord(payRecord, resultMessage);
        combAbstractService.processCombResult(payRecord, combPayRecord);
        return payRecord;
    }
    
    /**
     * 对同步结果处理
     * 
     * @param payResultDTO
     * @param payRecord
     */
    public void processSyncResponse(PayResultDTO payResultDTO, PayRecord payRecord){
        
        if (RequestStatusEnum.FAIL == payResultDTO.getStatus()){
            
            updatePayRecordToFail(payRecord, payResultDTO);
        } else if (RequestStatusEnum.SUCCESS == payResultDTO.getStatus()){
            
            try {
                resultProcessService.updatePaymentToSuccess(processPayRecord(payRecord, payResultDTO));
            } catch (Exception e) {
                logger.error("更新订单状态为成功异常: ", e);
                payRecord = payRecordDao.selectByPrimaryKey(payRecord.getRecordNo());
            }
            try {
                // 通知订单方
                notifyService.notify(payRecord);
            } catch (Exception e) {
                logger.error("通知订单方异常: ", e);
            }
        }
        
    }

    /**
     * 退同步结果处理，添加组合支付
     * @param payResultDTO
     * @param payRecord
     */
    public void processSyncResponseComb(PayResultDTO payResultDTO, PayRecord payRecord){
        if (RequestStatusEnum.FAIL == payResultDTO.getStatus()){
            updatePayRecordToFail(payRecord, payResultDTO);
            combAbstractService.updateFailByRecordNo(payRecord);
        } else if (RequestStatusEnum.SUCCESS == payResultDTO.getStatus()){
            CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
            checkPayRecordComb(payRecord, combPayRecord);
            processPayRecord(payRecord, payResultDTO);
            combAbstractService.processCombResult(payRecord, combPayRecord);
        }

    }


    /**
     * @param payResponseDTO
     * @return
     */
    private AccountPayResultMessage transformResultMessage(PayOrderQueryDTO payResponseDTO) {
        AccountPayResultMessage resultMessage = new AccountPayResultMessage();

        resultMessage.setAmount(payResponseDTO.getAmount());
        resultMessage.setCreateTime(payResponseDTO.getCreateTime());
        resultMessage.setCreditCustomerNo(payResponseDTO.getCreditCustomerNo());
        resultMessage.setDebitAccountNo(payResponseDTO.getDebitAccountNo());
        resultMessage.setDebitCustomerNo(payResponseDTO.getDebitCustomerNo());
        resultMessage.setExceptionCode(payResponseDTO.getExceptionCode());
        resultMessage.setExtInfo(payResponseDTO.getExtInfo());
        resultMessage.setFailureReason(payResponseDTO.getFailureReason());
        resultMessage.setFee(payResponseDTO.getFee());
        resultMessage.setFeeType(payResponseDTO.getFeeType());
        resultMessage.setOperator(payResponseDTO.getOperator());
        resultMessage.setOrderNo(payResponseDTO.getOrderNo());
        resultMessage.setPayInterfaceCode(payResponseDTO.getPayInterfaceCode());
        resultMessage.setStatus(payResponseDTO.getStatus());
        resultMessage.setTrxTime(payResponseDTO.getTrxTime());

        return resultMessage;
    }
    
    private void processPayRecord(PayRecord payRecord, AccountPayResultMessage resultMessage) {
        payRecord.setPayTime(resultMessage.getTrxTime());
        payRecord.setUpdateTime(new Date());
        if("CLOSE".equals(ConstantUtils.getNewAccountPay())) {
            payRecord.setBankOrderNo(payRecord.getRecordNo());
        }else {
            payRecord.setBankOrderNo(resultMessage.getAccountPayOrderNo());
        }
        payRecord.setFrpCode(resultMessage.getPayInterfaceCode());
        //如果下单的时候，没有传basicProductCode，底层支付系统会补充
        //将底层支付系统补充的，回写到支付处理器的数据库中
        if(StringUtils.isBlank(payRecord.getBasicProductCode())) {
            payRecord.setBasicProductCode(resultMessage.getBasicProductCode());
        }
    }
    
    private PayRecord processPayRecord(PayRecord payRecord, PayResultDTO payResultDTO) {
        payRecord.setPayTime(payResultDTO.getTrxTime());
        payRecord.setUpdateTime(new Date());
        if("CLOSE".equals(ConstantUtils.getNewAccountPay())) {
            payRecord.setBankOrderNo(payRecord.getRecordNo());
        }else {
            payRecord.setBankOrderNo(payResultDTO.getInnerOrderNo());
        }
        payRecord.setFrpCode(payResultDTO.getPayInterfaceCode());
        return payRecord;
    }

    /**
     * 修改支付子表状态，并将账户支付的错误码映射为支付处理器的错误码
     * 
     * @param payRecord
     * @param resultMessage
     */
    private void updatePayRecordToFail(PayRecord payRecord, AccountPayResultMessage resultMessage) {
        ErrorMeta errorMeta = ErrorCodeUtil.translateCode(ErrorCodeSource.ACCOUNTPAY.getSysCode(), resultMessage.getExceptionCode(),
                resultMessage.getFailureReason(), ErrorCode.P9001000);

        payRecordDao.updatePaymentToFail(payRecord.getRecordNo(), errorMeta.getErrorCode(), errorMeta.getErrorMsg());
    }
    
    /**
     * 支付记录状态更新为失败
     * 
     * @param payRecord
     * @param payResultDTO
     */
    private void updatePayRecordToFail(PayRecord payRecord, PayResultDTO payResultDTO){
        
        ErrorMeta errorMeta = ErrorCodeUtil.translateCode(ErrorCodeSource.ACCOUNTPAY.getSysCode(), payResultDTO.getExceptionCode(),
                payResultDTO.getFailureReason(), ErrorCode.P9001000);

        int row = payRecordDao.updatePaymentToFail(payRecord.getRecordNo(), errorMeta.getErrorCode(), errorMeta.getErrorMsg());
        
        if (0 == row){
            payRecord = payRecordDao.selectByPrimaryKey(payRecord.getRecordNo());
        } else {
            payRecord.setStatus(TrxStatusEnum.FAILUER.name());
            payRecord.setErrorCode(errorMeta.getErrorCode());
            payRecord.setErrorMsg(errorMeta.getErrorMsg());
        }
    }
}
