package com.yeepay.g3.core.frontend.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.entity.PayRecord;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.frontend.log.FrontEndTracer;
import com.yeepay.g3.core.frontend.service.BankRouterPayService;
import com.yeepay.g3.core.frontend.service.FanRouteService;
import com.yeepay.g3.core.frontend.util.ConstantUtils;
import com.yeepay.g3.core.frontend.util.ErrorCodeUtil;
import com.yeepay.g3.core.frontend.util.RedisUtil;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.bankchannel.dto.*;
import com.yeepay.g3.facade.frontend.dto.*;
import com.yeepay.g3.facade.frontend.enumtype.NotifyStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BankRouterPayServiceImpl extends AbstractService implements BankRouterPayService {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(BankRouterPayServiceImpl.class);
    @Autowired
    protected FanRouteService fanRouteService;

    @Override
    public void prePayJsapi(PrePayRequestDTO prePayRequestDTO, PrePayResponseDTO prePayResponseDTO) {
        OpenPayPrepareRouterRequestDTO openPayPrepareRouterRequestDTO = buildOpenPayPrepareRouterRequestDTO(prePayRequestDTO);
        //added by zengzhi.han 20181016 哆啦宝粉丝路由设置交易的商户号(优化代码)
        openPayPrepareRouterRequestDTO.setMaskMerchantNo(fanRouteService.getFanRouteTradeCustomerNum(prePayRequestDTO.getExtParam()));
        OpenPayPrepareRouterResponseDTO openPayPrepareRouterResponseDTO = openPayFacade.preRouter(openPayPrepareRouterRequestDTO);
        if (1 == openPayPrepareRouterResponseDTO.getDealStatus()) {   //"0000".equals(openPayPrepareRouterResponseDTO.getBizCode())
            prePayResponseDTO.setAppId(openPayPrepareRouterResponseDTO.getAppId());
            prePayResponseDTO.setAppSecret(openPayPrepareRouterResponseDTO.getAppSecret());
            prePayResponseDTO.setReportMerchantNo(openPayPrepareRouterResponseDTO.getReportMerchantNo());
            prePayResponseDTO.setPayInterface(openPayPrepareRouterResponseDTO.getChannelId());
            prePayResponseDTO.setBankTotalCost(new BigDecimal(openPayPrepareRouterResponseDTO.getTradeCost()).setScale(4, RoundingMode.HALF_UP));
            prePayResponseDTO.setDealStatus(openPayPrepareRouterResponseDTO.getDealStatus());// 调用预路由状态1：成功；0：失败 added by zhijun.wang 2017-05-23
            prePayResponseDTO.setSceneTypeExt(openPayPrepareRouterResponseDTO.getSceneTypeExt());// 场景类型扩展（jsapiH5：微信内部H5通道；normal：正常通道）added by zhijun.wang 2017-05-23
            //增加壳账户号字段 缓存到redis,下单时取出 20170918
            RedisUtil.pushMaskMerchantNo(openPayPrepareRouterResponseDTO.getMaskMerchantNo(), prePayRequestDTO.getDealUniqueSerialNo());
        } else {
            throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.BANKROUTER.getSysCode(), openPayPrepareRouterResponseDTO.getBizCode(),
                    openPayPrepareRouterResponseDTO.getBizMsg(), ErrorCode.F0003002);
        }
        // 通道traceId，返回便于业务方查询日志 added by zhijun.wang 20171214
        prePayResponseDTO.setTraceId(openPayPrepareRouterRequestDTO.getTraceId());
    }

    @Override
    public void openPay(PayOrder payOrder, PayRecord payRecord,
                        PayRequestDTO payRequestDTO, PayResponseDTO payResponseDTO) {
        OpenPayRequestDTO openPayRequestDTO = buildOpenPayRequestDTO(payOrder, payRequestDTO);
        //增加埋点日志
        if(ConstantUtils.enableSpanLog()){
            FrontEndTracer.frontendRequestRouterSpan(payRequestDTO.getCustomerNumber(), payRequestDTO.getOutTradeNo(),
                    payRequestDTO.getPlatformType().name(), payRequestDTO.getOrderType().name());
        }
        OpenPayResponseDTO openPayResponseDTO = openPayFacade.pay(openPayRequestDTO);
        if (openPayResponseDTO == null) {
            throw new FrontendBizException(ErrorCode.F0003002);
        }
        //扩展信息转成json格式字符串存入payOrder中 added by zhijun.wang 2017-12-12
        Map<String, String> map = openPayResponseDTO.getExternalResults();
        if (MapUtils.isEmpty(map)) {
            map = new HashMap();
        }
        //added by zengzhi.han 20181016 增加是否需要密码
        if(OrderType.PASSIVESCAN.equals(payRequestDTO.getOrderType())){
            if (openPayResponseDTO.getNeedPassword() != null) {
                map.put(ConstantUtils.FE_RETURN_PP_RESPONSE_IS_NEEDPASSWORD, String.valueOf(openPayResponseDTO.getNeedPassword()));
            } else {
                map.put(ConstantUtils.FE_RETURN_PP_RESPONSE_IS_NEEDPASSWORD, "");
            }
            payOrder.setExtParam(JSONObject.toJSONString(map));
        }
        if (openPayResponseDTO.getDealStatus() == ConstantUtils.BANKROUTER_RESULT_SUCCESS) {
            // 主扫公众号表示支付请求成功
            payOrder.setOrderNo(openPayResponseDTO.getBankOrderNo());
            payRecord.setOrderNo(openPayResponseDTO.getBankOrderNo());
            payRecord.setFrontValue(openPayResponseDTO.getPayUrl());
            if (!OrderType.JSAPI.equals(payRequestDTO.getOrderType())) {
                payOrder.setBankTotalCost(new BigDecimal(openPayResponseDTO.getSingleCost()).setScale(4, RoundingMode.HALF_UP));
                payOrder.setPayInterface(openPayResponseDTO.getChannelId());
                //被扫表示支付成功(免密)
                if (OrderType.PASSIVESCAN.equals(payRequestDTO.getOrderType())) {
                    payOrder.setPayStatus(PayStatusEnum.SUCCESS.name());
                    payOrder.setPayBank(openPayResponseDTO.getBankCode());
                    payOrder.setPayBankcardType(openPayResponseDTO.getCardType());
                    payOrder.setTransactionId(StringUtils.isBlank(openPayResponseDTO.getOpenTradeNo()) ?
                            openPayResponseDTO.getBankTradeNo() : openPayResponseDTO.getOpenTradeNo());
                    payOrder.setBankTradeId(openPayResponseDTO.getBankTradeNo());
                    payOrder.setPaySuccessTime(new Date());
                    payOrder.setBankSuccessTime(new Date());
                    payOrder.setNotifyStatus(NotifyStatusEnum.INIT.name());
                    payOrder.setOpenId(openPayResponseDTO.getOpenId());// added by zhijun.wang 2017-07-26

                    //被扫免密，直接获取支付成功的，增加埋点日志
                    if(ConstantUtils.enableSpanLog()){
                        FrontEndTracer.frontendOrderCompleteSpan(payRequestDTO.getCustomerNumber(), payRequestDTO.getOutTradeNo(),
                                payRequestDTO.getPlatformType() == null ? "" : payRequestDTO.getPlatformType().name(),
                                payRequestDTO.getOrderType() == null ? "" : payRequestDTO.getOrderType().name(),
                                PayStatusEnum.SUCCESS.name(),openPayResponseDTO.getReturnMsg(),openPayResponseDTO.getReturnCode());
                    }
                }
                //added by zengzhi.han 20181024 增加优惠券信息
                if (openPayResponseDTO.getCashFee()!=null){
                    payResponseDTO.setCashFee(new BigDecimal(openPayResponseDTO.getCashFee().toString()));
                }
                if (openPayResponseDTO.getSettlementFee()!=null){
                    payResponseDTO.setSettlementFee(new BigDecimal(openPayResponseDTO.getSettlementFee().toString()));
                }
                List<BankChannelPromotionInfoDTO> bankChannelPromotionInfoDTOS =  openPayResponseDTO.getBankChannelPromotionInfoDTOs();
                if (bankChannelPromotionInfoDTOS!=null&&!bankChannelPromotionInfoDTOS.isEmpty()){
                    PromotionInfoDTO promotionInfoDTO = null;
                    List<PromotionInfoDTO> promotionInfoDTOS = new LinkedList<PromotionInfoDTO>();
                    for (BankChannelPromotionInfoDTO temp:bankChannelPromotionInfoDTOS){
                        promotionInfoDTO = new PromotionInfoDTO();
                        BeanUtils.copyProperties(temp,promotionInfoDTO);
                        if (temp.getAmount()!=null){
                            promotionInfoDTO.setAmount(new BigDecimal(temp.getAmount().toString()));
                        }
                        if (temp.getAmountRefund()!=null){
                            promotionInfoDTO.setAmountRefund(new BigDecimal(temp.getAmountRefund().toString()));
                        }
                        promotionInfoDTOS.add(promotionInfoDTO);
                    }
                    payResponseDTO.setPromotionInfoDTOS(promotionInfoDTOS);
                }
            }
        } else if (openPayResponseDTO.getDealStatus() == ConstantUtils.BANKROUTER_RESULT_INIT
                || openPayResponseDTO.getDealStatus() == ConstantUtils.BANKROUTER_RESULT_PROCESS) {
            //支付初始化或者支付处理中
            payOrder.setOrderNo(openPayResponseDTO.getBankOrderNo());
            payOrder.setBankTotalCost(new BigDecimal(openPayResponseDTO.getSingleCost()).setScale(4, RoundingMode.HALF_UP));
            payOrder.setPayInterface(openPayResponseDTO.getChannelId());
            payRecord.setOrderNo(openPayResponseDTO.getBankOrderNo());
            payRecord.setNocardCode(openPayResponseDTO.getBizCode());
            payRecord.setNocardMsg(openPayResponseDTO.getBizMsg());
        } else {
            //支付请求失败
            payRecord.setNocardCode(openPayResponseDTO.getBizCode());
            payRecord.setNocardMsg(openPayResponseDTO.getBizMsg());
            //支付接口返回支付失败或未知的，增加埋点日志
            if(ConstantUtils.enableSpanLog()){
                FrontEndTracer.frontendOrderCompleteSpan(payRequestDTO.getCustomerNumber(), payRequestDTO.getOutTradeNo(),
                        payRequestDTO.getPlatformType() == null ? "" : payRequestDTO.getPlatformType().name(),
                        payRequestDTO.getOrderType() == null ? "" : payRequestDTO.getOrderType().name(),
                        PayStatusEnum.FAILURE.name(), openPayResponseDTO.getReturnMsg(), openPayResponseDTO.getReturnCode());
            }
            throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.BANKROUTER.getSysCode(), openPayResponseDTO.getBizCode(),
                    openPayResponseDTO.getBizMsg(), ErrorCode.F0003002);
        }
        // 通道traceId，返回便于业务方查询日志 added by zhijun.wang 20171214
        payResponseDTO.setTraceId(openPayRequestDTO.getTraceId());
    }



}
