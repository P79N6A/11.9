package com.yeepay.g3.core.frontend.biz.impl;

import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.biz.RefundManageBiz;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.util.MailSendHelper;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.*;
import com.yeepay.g3.facade.frontend.enumtype.*;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chronos.
 * @createDate 16/8/4.
 */
@Service("refundManageBiz")
public class RefundManageBizImpl extends AbstractBiz implements RefundManageBiz {

    private final static FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(RefundManageBizImpl.class);

    @Override
    public FrontendRefundResponseDTO refundRequest(FrontendRefundRequestDTO dto) {
        FrontendRefundResponseDTO responseDTO = new FrontendRefundResponseDTO();
        PayOrder order = queryBySystemAndRequestId(dto.getRequestSystem(), dto.getRequestId(),
                dto.getPlatformType().name());
        validateOrder(order);
        updateRefundStatus(dto.getRefundType(), order);
        generateResponse(responseDTO, order);
        return responseDTO;
    }

    public void refundByDate(Date start, Date end, String platformType){
        List<PayOrder> payOrders = payOrderService.queryUnRefundByDate(start,end,platformType);
        if (payOrders==null || payOrders.size()<1){
            return;
        }
        logger.info("error refund send to refund center,size:"+payOrders.size());
        Map<String,String> errorMap = new HashMap<String, String>();
        for (PayOrder order:payOrders){
            try {
                routerRefundCenterService.submitRefundRequest(order);
            } catch (Throwable th){
                errorMap.put(order.getOrderNo(),th.getMessage());
            }
        }
        if (errorMap.size()<1){
            return;
        }
        //邮件通知失败记录
        Map<String,Object> errorMsg = new HashMap<String, Object>();
        errorMsg.put("failReason","发送到退款中心失败");
        errorMsg.put("errorMap",errorMap.toString());
        MailSendHelper.sendEmail(errorMsg,MailSendHelper.SEND_TO_REFUND_RULE,MailSendHelper.recipients);
    }

    /**
     * 单笔退款接口
     */
    public FeOperationResponseDTO refundOrders(FeOperationRequestDTO requestDTO){
        logger.info("[size = " + requestDTO.getOrderNos().size() + "]");
        FeOperationResponseDTO responseDTO = new FeOperationResponseDTO();
        for (BasicOperationDTO operate : requestDTO.getOrderNos()) {
            try {
                logger.info("[" + operate.getRequestId() + "] - [START]");
                PayOrder payOrder = queryBySystemAndRequestId(operate.getRequestSystem(),
                        operate.getRequestId(), operate.getPlatformType());
                if (!PayStatusEnum.SUCCESS.name().equals(payOrder.getPayStatus())
                        || !RefundStatusEnum.INIT.name().equals(payOrder.getRefundStatus())){
                    responseDTO.setIgnore(responseDTO.getIgnore() + 1);
                    continue;
                }
                routerRefundCenterService.submitRefundRequest(payOrder);
                responseDTO.setSuccess(responseDTO.getSuccess() + 1);
                logger.info("[" + operate.getRequestId() + "] - [SUCCESS]");
            } catch (FrontendBizException e) {
                responseDTO.getErrorList().add("[" + operate + "] - [" + e.getDefineCode() + e.getMessage() + "]");
            } catch (Throwable e) {
                logger.error("[" + operate.getRequestId() +"] - [FAILURE]", e);
                responseDTO.getErrorList().add("[" + operate + "] - [" + e.getMessage() + "]");
            }
        }
        return responseDTO;
    }

    /**
     * 校验订单信息
     *
     * @param order
     */
    private void validateOrder(PayOrder order) {
        if (!PayStatusEnum.SUCCESS.name().equals(order.getPayStatus())) {
            throw new FrontendBizException(ErrorCode.F0002005, "order status invalid.");
        }
    }

    private void validateDirectRefund(PayOrder order){
        validateOrder(order);
        if (RefundStatusEnum.NONE.name().equals(order.getRefundStatus())){
            throw new FrontendBizException(ErrorCode.F0002005, "该订单不能发起退款");
        }
    }

    /**
     * 更新退款状态
     * 订单已经退款了,返回退款状态
     *
     * @param refundType
     * @param order
     */
    private void updateRefundStatus(RefundType refundType, PayOrder order) {
        if (StringUtils.isNotBlank(order.getRefundStatus())
            && !RefundStatusEnum.NONE.name().equals(order.getRefundStatus())) {
            return;
        }
        order.setRefundStatus(RefundStatusEnum.INIT.name());
        order.setRefundType(refundType.name());
        order.setRefundDate(new Date());
        payOrderService.singleUpdate(order);
    }

    /**
     * 组装返回参数
     * @param responseDTO
     * @param order
     */
    private void generateResponse(FrontendRefundResponseDTO responseDTO, PayOrder order) {
        responseDTO.setRefundStatus(RefundStatusEnum.getRefundStatusEnum(order.getRefundStatus()));
        responseDTO.setRefundType(RefundType.getRefundType(order.getRefundType()));
        responseDTO.setCustomerNumber(order.getCustomerNumber());
        responseDTO.setOrderNo(order.getOrderNo());
        responseDTO.setOrderType(OrderType.getOrderType(order.getOrderType()));
        responseDTO.setOutTradeNo(order.getOutTradeNo());
        responseDTO.setPlatformType(PlatformType.getPlatformType(order.getPlatformType()));
        responseDTO.setRequestId(order.getRequestId());
        responseDTO.setRequestSystem(order.getRequestSystem());
        responseDTO.setDealUniqueSerialNo(order.getDealUniqueSerialNo());
        responseDTO.setTotalAmount(order.getTotalAmount());
    }
}
