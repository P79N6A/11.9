package com.yeepay.g3.core.frontend.service.impl;

import com.yeepay.app.httpinvoke.dto.MobilePayOrderPaymentDetailDTO;
import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.enumtype.ExternalSystem;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.service.RouterRefundCenterService;
import com.yeepay.g3.core.frontend.util.ConstantUtils;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.RefundStatusEnum;
import com.yeepay.g3.facade.refund.RefundRequestFacade;
import com.yeepay.g3.facade.refund.dto.InitiateOrderRefundParamDTO;
import com.yeepay.g3.facade.refund.dto.OrderPaymentRefundRequestDTO;
import com.yeepay.g3.facade.refund.enums.NotifyTypeEnum;
import com.yeepay.g3.facade.refund.enums.RefundClassifyEnum;
import com.yeepay.g3.utils.common.exception.YeepayRuntimeException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chronos.
 * @createDate 16/8/5.
 */
@Service("routerRefundCenterService")
public class RouterRefundCenterServiceImpl extends AbstractService implements RouterRefundCenterService {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(RouterRefundCenterServiceImpl.class);

    private RefundRequestFacade refundRequestFacade = RemoteFacadeProxyFactory.getService(
            RefundRequestFacade.class, ExternalSystem.REFUND);

    @Override
    public boolean submitRefundRequest(PayOrder order) {
        try {
            sendToRefundCenter(order);
            updateRefundStatus(order, RefundStatusEnum.SEND.name());
            return true;
        } catch (YeepayRuntimeException ye){
            logger.warn("["+order.getOrderNo()+"]发送到退款中心失败",ye);
            throw new FrontendBizException(ErrorCode.F0003003,"["+order.getOrderNo()+"]"+ye.getMessage());
        } catch (Throwable th){
            logger.warn("["+order.getOrderNo()+"]系统异常",th);
            throw new FrontendBizException(ErrorCode.F0001000,"["+order.getOrderNo()+"]" + th.getMessage());
        }
    }

    /**
     * 组装退款请求参数
     * 发送退款请求到退款中心
     * @param order
     */
    private void sendToRefundCenter(PayOrder order){
        OrderPaymentRefundRequestDTO requestDTO = new OrderPaymentRefundRequestDTO();
        requestDTO.setRefundClassify(RefundClassifyEnum.ORDER_REFUND);//退款类型 目前只有交易退款
        requestDTO.setNoCardSysFlowNo(order.getOrderNo());//银行子系统流水号

        InitiateOrderRefundParamDTO orderRefundParam = new InitiateOrderRefundParamDTO();
        orderRefundParam.setAgencyNo("YEEPAY");//固定
        orderRefundParam.setInitiator(ConstantUtils.SYS_NO);//约定配置为FE
        //产品类型
        orderRefundParam.setProductType(ConstantUtils.PRODUCT_TYPE_FE);
        orderRefundParam.setNotifyType(NotifyTypeEnum.HESSIAN);
        orderRefundParam.setResultNotifyUrl(ConstantUtils.getREFUND_CALLBAK_URL());// TODO: 16/8/5 退款通知Hessian地址
        orderRefundParam.setPaymentOrderNo(order.getOrderNo());//front pay的支付订单号
        orderRefundParam.setRefundAmount(new Amount(order.getTotalAmount()));
        orderRefundParam.setRefundPaymentId(order.getId());//退款的paymentid 设置为我们的订单id吧,最好是流水id
        orderRefundParam.setRequestNo(order.getOrderNo());//退款请求号,同样复用支付订单号
        orderRefundParam.setRefundRemark("FRONTEND退款请求");
        //增加零售产品码和基础产品码 added by dongbo.jiao 2017-06-21 start
        orderRefundParam.setSalesProductCode(order.getRetailProductCode());
        orderRefundParam.setBasicProductCode(order.getBasicProductCode());
        //增加零售产品码和基础产品码 added by dongbo.jiao 2017-06-21 end
        requestDTO.setOrderRefundParam(orderRefundParam);

        MobilePayOrderPaymentDetailDTO baseOrderPaymentDetailDTO =
                new MobilePayOrderPaymentDetailDTO();

        baseOrderPaymentDetailDTO.setBankOrderId(order.getOrderNo());//银行子系统流水
        baseOrderPaymentDetailDTO.setOrderAmount(order.getTotalAmount().doubleValue());
        baseOrderPaymentDetailDTO.setExternalId(order.getOrderNo());
        baseOrderPaymentDetailDTO.setRequestId(order.getOutTradeNo());//商户订单号?干嘛用的
        baseOrderPaymentDetailDTO.setCustomerNumber(order.getCustomerNumber());
        baseOrderPaymentDetailDTO.setConfirmOrCancel(order.getModifyTime());//支付完成时间是干嘛用的
        baseOrderPaymentDetailDTO.setPayAmount(order.getTotalAmount().doubleValue());
        baseOrderPaymentDetailDTO.setPaymentId(System.nanoTime());
        baseOrderPaymentDetailDTO.setBankTrxId(order.getTransactionId());//银行流水号和微信订单号是否一样?
        baseOrderPaymentDetailDTO.setG3PayInterfaceCode(order.getPayInterface()); //支付接口必填

        requestDTO.setPaymentOrder(baseOrderPaymentDetailDTO);

        List<Object> orderDTOList = new ArrayList<Object>();
        orderDTOList.add(baseOrderPaymentDetailDTO);
        requestDTO.setPaymentDetail(orderDTOList);

        logger.info("request to refund center ,request dto is " + ToStringBuilder.reflectionToString(requestDTO));
        refundRequestFacade.initOrderRefund(requestDTO);
    }

    private void updateRefundStatus(PayOrder order, String status){
        order.setRefundStatus(status);
        payOrderDao.update(order);
    }
}
