package com.yeepay.g3.core.nccashier.gateway.service.adapter.impl;

import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.BibasicTradeOrderService;
import com.yeepay.g3.core.nccashier.gateway.service.adapter.OrderInfoAccessAdapter;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;
import com.yeepay.g3.core.nccashier.vo.TransparentCardInfo;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.pccashier.pay.dto.CashierQueryResponseDTO;
import com.yeepay.g3.utils.common.StringUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.service.impl
 * 二代交易信息接入适配器实现
 * @author pengfei.chen
 * @since 17/4/12 11:45
 */
@Service
public class G2NetOrderInfoAccessAdapterServiceImpl extends NcCashierBaseService implements OrderInfoAccessAdapter {
    @Resource
    private BibasicTradeOrderService bibasicTradeOrderService;
    @Override
    public OrderDetailInfoModel getOrderDetailInfoModel(String orderNo, OrderSysConfigDTO orderSysConfigDTO) {
        CashierQueryResponseDTO cashierQueryResponseDTO = bibasicTradeOrderService.queryOrderDetailFromBibasic(orderNo);
        return buildOrderDetailInfoModel(cashierQueryResponseDTO);
    }

    @Override
    public boolean isSupport(String accessCode) {
        return Constant.G2NET_SYS_INTERFACE.equals(accessCode);
    }

    private OrderDetailInfoModel buildOrderDetailInfoModel(CashierQueryResponseDTO dto) {
        OrderDetailInfoModel infoModel = new OrderDetailInfoModel();
        infoModel.setOrderSysNo(dto.getOrderSysNo());
        infoModel.setUniqueOrderNo(dto.getUniqueOrderNo());
        infoModel.setMerchantAccountCode(dto.getMerchantAccountCode());
        infoModel.setMerchantName(dto.getMerchantName());
        infoModel.setMerchantOrderId(dto.getMerchantOrderId());
        infoModel.setGoodsCategoryCode(dto.getGoodsCategoryCode());
        infoModel.setOrderAmount(dto.getOrderAmount());
        infoModel.setFrontCallBackUrl(dto.getFrontCallBackUrl());
        infoModel.setOrderExpDate(dto.getOrderExpDate());
        infoModel.setOrderTime(dto.getOrderTime());
        infoModel.setCurrency(CurrencyEnum.CNY);
        infoModel.setCashierVersion(CashierVersionEnum.valueOf(dto.getCashierVersion()));
        if (StringUtils.isNotBlank(dto.getIdentityId())) {
            if (MemberTypeEnum.YIBAO.name().equals(dto.getIdentityType())) {
                infoModel.setMemberNo(dto.getIdentityId());
                infoModel.setMemberType("YIBAO");
            } else {
                infoModel.setIdentityId(dto.getIdentityId());
                infoModel.setIdentityType(IdentityType.valueOf(dto.getIdentityType()));
            }
        }
        infoModel.setDirectPayType(DirectPayType.changeDirectTypeEnum(dto.getDirectPayType()));
        infoModel.setPayTool(StringUtils.isBlank(dto.getPayTool())?null: PayTool.valueOf(dto.getPayTool()));
        TransparentCardInfo cardInfo = new TransparentCardInfo();
        cardInfo.setBankCode(dto.getBankCode());
        cardInfo.setCardNo(dto.getBankCardNo());
        String  cardType = dto.getBankCardType();
        if(StringUtils.isNotBlank(cardType)){
            cardInfo.setCardType(CardTypeEnum.valueOf(cardType));
        }
        cardInfo.setIdcard(dto.getIdCardNo());
        cardInfo.setOwner(dto.getUserName());
        cardInfo.setPhoneNo(dto.getPhone());
        infoModel.setCardInfo(cardInfo);
        infoModel.setUserIp(dto.getUserIp());
        infoModel.setProductName(dto.getGoodsName());
        infoModel.setSaleProductCode(dto.getSaleProductCode());
        infoModel.setProductType(dto.getProductType());
        infoModel.setProductOrderCode(dto.getRiskProduction());
        infoModel.setCallFeeItem("ONLINE_ACQUIRING");
        infoModel.setSignedMerchantAccountCode(dto.getMerchantAccountCode());
        // 2代暂时不支持个人会员充值，这里默认写死成消费
        infoModel.setTransactionType(TransactionTypeEnum.SALE);
        return infoModel;
    }
}
