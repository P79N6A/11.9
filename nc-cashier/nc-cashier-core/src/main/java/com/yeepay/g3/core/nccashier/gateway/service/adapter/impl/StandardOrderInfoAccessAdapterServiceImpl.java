package com.yeepay.g3.core.nccashier.gateway.service.adapter.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.nccashier.gateway.service.adapter.OrderInfoAccessAdapter;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;
import com.yeepay.g3.core.nccashier.vo.TransparentCardInfo;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.OrderInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.OrderInfoQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CurrencyEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.service.NcCashierOrderInfoFacade;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

import org.springframework.stereotype.Service;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.service.impl
 * 标准化交易信息接入适配器实现
 * @author pengfei.chen
 * @since 17/4/12 11:45
 */
@Service
public class StandardOrderInfoAccessAdapterServiceImpl extends NcCashierBaseService implements OrderInfoAccessAdapter {
    private static final Logger logger = LoggerFactory.getLogger(StandardOrderInfoAccessAdapterServiceImpl.class);
    @Override
    public OrderDetailInfoModel getOrderDetailInfoModel(String orderNo, OrderSysConfigDTO orderSysConfigDTO) {
        OrderInfoQueryRequestDTO orderInfoQueryRequestDTO = new OrderInfoQueryRequestDTO();
        orderInfoQueryRequestDTO.setToken(orderNo);
        OrderInfoDTO orderInfoDTO;
        try {
            NcCashierOrderInfoFacade ncCashierOrderInfoFacade = RemoteFacadeProxyFactory.getService(orderSysConfigDTO.getOrderRequestUrl(),
                    RemotingProtocol.valueOf(orderSysConfigDTO.getRemoteServiceProtocol()),
                    NcCashierOrderInfoFacade.class,Constant.STANDARD_SYS_INTERFACE);
            orderInfoDTO = ncCashierOrderInfoFacade.queryOrderInfoByToken(orderInfoQueryRequestDTO);
        }catch (Throwable e){
            logger.error("反查订单信息异常,token:{},message:{}",orderNo,e);
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        handleException(orderInfoDTO,orderSysConfigDTO);
        return buildOrderDetailInfoModel(orderInfoDTO);
    }

    @Override
    public boolean isSupport(String accessCode){
        return Constant.STANDARD_SYS_INTERFACE.equals(accessCode);
    }

    private OrderDetailInfoModel buildOrderDetailInfoModel(OrderInfoDTO orderInfoDTO){
        OrderDetailInfoModel orderDetailInfoModel = new OrderDetailInfoModel();
        orderDetailInfoModel.setCallFeeItem(orderInfoDTO.getCallFeeItem());
        orderDetailInfoModel.setSaleProductCode(orderInfoDTO.getSaleProductCode());
        orderDetailInfoModel.setOrderExpDateType(orderInfoDTO.getOrderExpDateType());
        orderDetailInfoModel.setOrderExpDate(orderInfoDTO.getOrderExpDate());
        orderDetailInfoModel.setProductName(orderInfoDTO.getProductName());
        orderDetailInfoModel.setUniqueOrderNo(orderInfoDTO.getUniqueOrderNo());
        orderDetailInfoModel.setUserIp(orderInfoDTO.getUserIp());
        orderDetailInfoModel.setFrontCallBackUrl(orderInfoDTO.getFrontCallBackUrl());
        orderDetailInfoModel.setProductType(orderInfoDTO.getProductType());
        orderDetailInfoModel.setProductOrderCode(orderInfoDTO.getProductOrderCode());
        orderDetailInfoModel.setOrderType(orderInfoDTO.getOrderType());
        if (StringUtils.isNotBlank(orderInfoDTO.getIdentityId())) {
            if (MemberTypeEnum.YIBAO.name().equals(orderInfoDTO.getIdentityType())) {
                orderDetailInfoModel.setMemberNo(orderInfoDTO.getIdentityId());
                orderDetailInfoModel.setMemberType("YIBAO");
            } else {
                orderDetailInfoModel.setIdentityId(orderInfoDTO.getIdentityId());
                orderDetailInfoModel.setIdentityType(orderInfoDTO.getIdentityType());
            }
        }
        orderDetailInfoModel.setCurrency(CurrencyEnum.CNY);
        orderDetailInfoModel.setMerchantOrderId(orderInfoDTO.getMerchantOrderId());
        orderDetailInfoModel.setMerchantAccountCode(orderInfoDTO.getMerchantNo());
        orderDetailInfoModel.setParentMerchantNo(orderInfoDTO.getParentMerchantNo());
        orderDetailInfoModel.setOrderAmount(orderInfoDTO.getOrderAmount());
        orderDetailInfoModel.setMerchantName(orderInfoDTO.getMerchantName());
        orderDetailInfoModel.setOrderSysNo(orderInfoDTO.getOrderSysNo());
        orderDetailInfoModel.setRiskProduction(orderInfoDTO.getRiskProduction());
        if(StringUtils.isNotBlank(orderInfoDTO.getPaymentParamExt())){
            JSONObject jsonObject = JSON.parseObject(orderInfoDTO.getPaymentParamExt());
            TransparentCardInfo cardInfo = new TransparentCardInfo();
            cardInfo.setCardNo(jsonObject.getString("bankCardNo"));
            cardInfo.setIdcard(jsonObject.getString("idCardNo"));
            cardInfo.setOwner(jsonObject.getString("owner"));
            cardInfo.setBankCode(jsonObject.getString("bankCode"));
            cardInfo.setPhoneNo(jsonObject.getString("phone"));
            orderDetailInfoModel.setCardInfo(cardInfo);
            orderDetailInfoModel.setAppId(jsonObject.getString("appId"));
            orderDetailInfoModel.setAreaCode(jsonObject.getString("areaInfo"));
            // 账户支付所需的扣款商编（目前只有前置收银台取这个值）
            orderDetailInfoModel.setAccountPayMerchantNo(jsonObject.getString("accountPayMerchantNo"));
        }
        orderDetailInfoModel.setTradeRiskInfo(orderInfoDTO.getRiskParamExt());
        JSONObject jsonObject = CommonUtil.parseJson(orderInfoDTO.getRiskParamExt());
        orderDetailInfoModel.setReffer(jsonObject.getString("referer"));
        orderDetailInfoModel.setOrderTime(orderInfoDTO.getOrderTime());
        orderDetailInfoModel.setSignedMerchantAccountCode(orderInfoDTO.getMerchantNo());
        // 其他扩展信息（为了支持账户支付，目前增加了几个账户历史的字段）
        orderDetailInfoModel.buildOtherParamExt(orderInfoDTO.getOtherParamExt());
        orderDetailInfoModel.setTransactionType(TransactionTypeEnum.valueByName(orderInfoDTO.getTransactionType()));
        return orderDetailInfoModel;
    }
	
    private void handleException(OrderInfoDTO responseDTO,OrderSysConfigDTO orderSysConfigDTO) {
        if (responseDTO == null) {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        if (StringUtils.isBlank(responseDTO.getErrorCode())) {
            //接口未返回errorCode，直接返回
            return;
        }
        if (orderSysConfigDTO == null || StringUtils.isBlank(orderSysConfigDTO.getErrorCodeSysCode())) {
            //业务方未配置错误码系统编码
            throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
        }
        //业务方配置了错误码系统编码
        throw CommonUtil.handleException(orderSysConfigDTO.getErrorCodeSysCode(),responseDTO.getErrorCode(),responseDTO.getErrorMsg());
    }

}
