package com.yeepay.g3.core.payprocessor.external.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.facade.payprocessor.enumtype.CombTrxStatusEnum;
import org.springframework.stereotype.Service;

import com.yeepay.app.httpinvoke.dto.MobilePayOrderPaymentDetailDTO;
import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import com.yeepay.g3.core.payprocessor.external.service.RefundCenterService;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.MailSendHelper;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.cwh.param.BankCardDetailDTO;
import com.yeepay.g3.facade.refund.dto.InitiateOrderRefundParamDTO;
import com.yeepay.g3.facade.refund.dto.OrderPaymentRefundRequestDTO;
import com.yeepay.g3.facade.refund.enums.NotifyTypeEnum;
import com.yeepay.g3.facade.refund.enums.RefundClassifyEnum;
import com.yeepay.g3.facade.refund.exception.RefundRequestException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos.
 * @createDate 2016/11/14.
 */
@Service("refundCenterService")
public class RefundCenterServiceImpl extends AbstractService implements RefundCenterService {

    private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(RefundCenterServiceImpl.class);

    @Override
    public boolean processRefund(PaymentRequest payment, PayRecord payRecord, ReverseRecord reverseRecord) {
        logger.info("[recordNo = " + payRecord.getRecordNo() + "]发送到退款中心");
        boolean result = false;
        OrderPaymentRefundRequestDTO requestDTO = null;
        try {
            if(payRecord.isCombinedPay()) {
                CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
                requestDTO = buildRefundRequestDTO(payment, payRecord, combPayRecord, reverseRecord);
            }else {
                requestDTO = buildRefundRequestDTO(payment, payRecord, reverseRecord);
            }
            refundRequestFacade.initOrderRefund(requestDTO);
            result = true;
        } catch (RefundRequestException th){
            //发送到退款中心异常,异常处理
            logger.error("[recordNo = " + payRecord.getRecordNo() + "]发送到退款中心异常", th);
            //退款订单号重复才会抛出此异常
            result = true;
        } catch (Throwable th){
            //发送超时或者其他
            logger.error("[recordNo = " + payRecord.getRecordNo() + "]发送到退款中心异常", th);
            Map<String,Object> errorMail = new HashMap<String, Object>();
            errorMail.put("orderNo",payment.getOrderNo());
            errorMail.put("recordNo",payRecord.getRecordNo());
            errorMail.put("errorMsg", th.getMessage());
            errorMail.put("cause", th.getCause());
            MailSendHelper.sendEmail(errorMail,MailSendHelper.REFUND_ERROR_RULE,MailSendHelper.recipients);
        }
        return result;
    }

    private OrderPaymentRefundRequestDTO buildRefundRequestDTO(PaymentRequest payment, PayRecord payRecord, CombPayRecord combPayRecord, ReverseRecord reverseRecord) {
        OrderPaymentRefundRequestDTO requestDTO = buildRefundRequestDTO(payment, payRecord, reverseRecord);
        InitiateOrderRefundParamDTO orderRefundParam = requestDTO.getOrderRefundParam();
        // 第二支付子单如果冲正，则一起冲
        if(CombTrxStatusEnum.REVERSE.name().equals(combPayRecord.getStatus())) {
            orderRefundParam.setSecondPaymentOrderNo(combPayRecord.getPayOrderNo());
            orderRefundParam.setSecondBankOrderNo(combPayRecord.getBankOrderNo());
            orderRefundParam.setSecondRefundAmount(new Amount(combPayRecord.getAmount()));
            orderRefundParam.setSecondProductType(combPayRecord.getPayProduct());
            orderRefundParam.setSecondPayInterfaceCode(combPayRecord.getFrpCode());
            // 备注，此处应该将退款金额更换成firstAmount = amount - 营销金额
            orderRefundParam.setRefundAmount(new Amount(payRecord.getFirstPayAmount()));
        // 第二支付子单如果不冲正，则只冲第一支付子单
        } else {
            orderRefundParam.setRefundAmount(new Amount(payRecord.getFirstPayAmount()));
        }
        return requestDTO;
    }

    /**
     * 组装退款请求参数
     * 发送退款请求到退款中心
     * @param payment
     * @param record
     */
    private OrderPaymentRefundRequestDTO buildRefundRequestDTO(PaymentRequest payment, PayRecord record, ReverseRecord reverseRecord){
        OrderPaymentRefundRequestDTO requestDTO = new OrderPaymentRefundRequestDTO();
        requestDTO.setRefundClassify(RefundClassifyEnum.ORDER_REFUND);//退款类型 目前只有交易退款
        requestDTO.setNoCardSysFlowNo(record.getBankTrxId());//银行子系统系统流水号
        requestDTO.setErrorRefund(true);//差错退款
        requestDTO.setDateForAccountCheck(record.getPayTime());
        requestDTO.setRelatedTransId(payment.getOrderNo());//关联交易号

        InitiateOrderRefundParamDTO orderRefundParam = new InitiateOrderRefundParamDTO();
        orderRefundParam.setAgencyNo("YEEPAY");//固定
        orderRefundParam.setInitiator(ConstantUtils.SYS_NO);//约定配置为PP
        //退款产品类型
        orderRefundParam.setProductType(ConstantUtils.getRefundProduct(record.getPayOrderType(), record.getBankAccountType()));
        orderRefundParam.setNotifyType(NotifyTypeEnum.HESSIAN);
        orderRefundParam.setResultNotifyUrl(ConstantUtils.getRefundCallbackUrl());
        orderRefundParam.setPaymentOrderNo(record.getRecordNo());//支付订单号
        orderRefundParam.setSalesProductCode(record.getRetailProductCode());//营销产品码
        orderRefundParam.setBasicProductCode(record.getBasicProductCode());//基础产品码
        
        BigDecimal totalAmount = null;
        if(record.getAmount() != null && record.getUserFee() != null){
        	totalAmount = record.getAmount().add(record.getUserFee());
        }else{
        	totalAmount = record.getAmount();
        }
        
        orderRefundParam.setRefundAmount(new Amount(totalAmount));//退款金额,退支付金额
        orderRefundParam.setRefundPaymentId(reverseRecord.getId());
        orderRefundParam.setRequestNo(record.getRecordNo());//退款请求号,同样复用支付订单号
        orderRefundParam.setRefundRemark(reverseRecord.getRemark());
        requestDTO.setOrderRefundParam(orderRefundParam);

        MobilePayOrderPaymentDetailDTO baseOrderPaymentDetailDTO =
                new MobilePayOrderPaymentDetailDTO();

        baseOrderPaymentDetailDTO.setBankOrderId(record.getBankOrderNo());//银行子系统订单号
        baseOrderPaymentDetailDTO.setOrderAmount(totalAmount.doubleValue());
        baseOrderPaymentDetailDTO.setExternalId(record.getRecordNo());
        baseOrderPaymentDetailDTO.setRequestId(payment.getOutTradeNo());//商户订单号
        baseOrderPaymentDetailDTO.setCustomerNumber(payment.getCustomerNo());
        baseOrderPaymentDetailDTO.setConfirmOrCancel(record.getPayTime());//支付完成时间是干嘛用的
        baseOrderPaymentDetailDTO.setPayAmount(totalAmount.doubleValue());
        baseOrderPaymentDetailDTO.setPaymentId(System.nanoTime());
        baseOrderPaymentDetailDTO.setBankTrxId(record.getBankSeq());//银行流水号
        baseOrderPaymentDetailDTO.setG3PayInterfaceCode(record.getFrpCode()); //支付接口必填
		// 卡账户信息
		if (StringUtils.isNotBlank(record.getCardId())) {
			BankCardDetailDTO bankCardDetailDTO = bankCardCwhFacade.getBankCardDetail(record.getCardId());
			if (bankCardDetailDTO != null) {
				//姓名
				baseOrderPaymentDetailDTO.setFullname(bankCardDetailDTO.getBaseInfo().getOwner());
				//银行名称
				baseOrderPaymentDetailDTO.setBankName(bankCardDetailDTO.getBaseInfo().getBankCode());
				//卡号
				baseOrderPaymentDetailDTO.setCardNumber(bankCardDetailDTO.getBaseInfo().getCardNo());			
			}
		}
		baseOrderPaymentDetailDTO.setCardTypeForfund(record.getCardType());
        requestDTO.setPaymentOrder(baseOrderPaymentDetailDTO);
        List<Object> orderDTOList = new ArrayList<Object>();
        orderDTOList.add(baseOrderPaymentDetailDTO);
        requestDTO.setPaymentDetail(orderDTOList);
        return requestDTO;

    }

}
