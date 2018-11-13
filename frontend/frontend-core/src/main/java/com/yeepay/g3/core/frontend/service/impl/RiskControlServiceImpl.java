package com.yeepay.g3.core.frontend.service.impl;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.frontend.service.RiskControlService;
import com.yeepay.g3.core.frontend.util.CommonUtils;
import com.yeepay.g3.core.frontend.util.ConstantUtils;
import com.yeepay.g3.core.frontend.util.ErrorCodeUtil;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.BasicRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.riskcontrol.facade.DoorgodFacade;
import com.yeepay.riskcontrol.facade.RcAsyncRspDto;
import com.yeepay.riskcontrol.facade.RcSyncRspDto;
import com.yeepay.riskcontrol.facade.v2.RcAsyncEfPayReqDto;
import com.yeepay.riskcontrol.facade.v2.RcSyncEfPayReqDto;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * @author chronos.
 * @createDate 16/9/28.
 */
@Service("riskControlService")
public class RiskControlServiceImpl extends AbstractService implements RiskControlService {

    private FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(RiskControlServiceImpl.class);

    private DoorgodFacade doorgodFacade = CommonUtils.getRemoteFacade(DoorgodFacade.class, ConstantUtils.getDoorGodUrl(),ConstantUtils.getRiskServiceConnectTimeout(), ConstantUtils.getRiskServiceReadTimeout());
    private static final String RISK_INTERCEPT_CODE = "8000" ;

    @Override
    public void syncControl(BasicRequestDTO requestDTO, PayOrder payOrder) {
        if(ConstantUtils.syncRiskControlWhiteList().contains(requestDTO.getCustomerNumber())){
            logger.info("syncControl() 商编'{}'在FE同步风控白名单，跳过风控校验",requestDTO.getCustomerNumber());
            return;
        }
        RcSyncRspDto rspDto;
        StringBuffer logBuffer = new StringBuffer("[同步风控请求参数] - ");
        long start = System.currentTimeMillis();
        try {
            RcSyncEfPayReqDto reqDto = buildRcSyncEfPayReqDto(requestDTO, payOrder);
            logBuffer.append(filterRcEfPayReqDto(reqDto));
            rspDto = doorgodFacade.intercept(reqDto);
            logBuffer.append(" --- [风控返回结果] - ").append(ToStringBuilder.reflectionToString(rspDto));
        } catch (Throwable th){
            logger.error("[风控同步处理] - [失败] - [忽略]",th);
            return;
        } finally {
            logBuffer.append(" --- [执行时间] - ").append(System.currentTimeMillis()-start).append("ms");
            logger.info(logBuffer.toString());
        }
        if (!rspDto.isDealResult())
            logger.warn("[风控同步处理] - [失败] - [忽略] " + rspDto.getErrorCode() +"," + rspDto.getErrorDescription());

        if( !RISK_INTERCEPT_CODE.equals(rspDto.getSupportOperateCode()))
            return;
        throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.DOORGOD.getSysCode(), rspDto.getRetCode(), rspDto.getRetCode(), ErrorCode.F0001008);
    }

    @Override
    public void asyncControl(PayOrder payOrder) {
        RcAsyncEfPayReqDto reqDto = bulidRcAsyncEfPayReqDto(payOrder);
        RcAsyncRspDto rspDto;
        try {
            logger.info("[风控异步请求参数] " + filterRcEfPayReqDto(reqDto));
            rspDto = doorgodFacade.summate(reqDto);
            logger.info("[风控异步返回结果] " + ToStringBuilder.reflectionToString(rspDto));
        } catch (Throwable th){
            logger.error("[风控异步处理] - [失败] - [忽略]",th);
        }
    }

    /**
     * 组装同步请求参数
     * @param requestDTO
     * @return
     */
    private RcSyncEfPayReqDto buildRcSyncEfPayReqDto(BasicRequestDTO requestDTO, PayOrder order){
        RcSyncEfPayReqDto reqDto = new RcSyncEfPayReqDto();
        reqDto.setOrderType(order.getOrderType());
//        reqDto.setBankChannelId(order.getPayInterface());
//        reqDto.setBizOrder(order.getOrderNo());
        reqDto.setRequestId(requestDTO.getOutTradeNo());
        reqDto.setPaymentId(requestDTO.getRequestId());
        reqDto.setUserIp(requestDTO.getPayerIp());
        reqDto.setUserType("MERCHANT");
        reqDto.setPayWay(requestDTO.getPlatformType().name());
        if (requestDTO instanceof PayRequestDTO){
            PayRequestDTO payRequestDTO = (PayRequestDTO) requestDTO;
            reqDto.setAppId(payRequestDTO.getAppId());
            reqDto.setUserId(payRequestDTO.getOpenId());
            //同步风控增加交易系统 added by dongbo.jiao 20180319 start
            reqDto.setPaySystem(requestDTO.getRequestSystem());
            //同步风控增加交易系统 added by dongbo.jiao 20180319 end
        }
        reqDto.setSequenceId(generateRiskSequenceId(order.getRequestId(), ConstantUtils.RISK_TYPE_SYNC));
//        reqDto.setEventType();
//        reqDto.setModuleId();
        reqDto.setAmount(requestDTO.getTotalAmount().toString());
        reqDto.setMerchantNo(requestDTO.getCustomerNumber());
        reqDto.setOccurTime(order.getCreateTime());
        reqDto.setProductId(ConstantUtils.SYS_NO);
        reqDto.setProduction(requestDTO.getOrderSystem());//订单方对应风控产品编码 + ConstantUtils.RISK_SUFFIX
        reqDto.setGoodsInfo(requestDTO.getGoodsInfo());
        reqDto.setToolsInfo(requestDTO.getToolsInfo());

        return reqDto;
    }

    /**
     * 组装异步请求参数
     * @param order
     * @return
     */
    private RcAsyncEfPayReqDto bulidRcAsyncEfPayReqDto(PayOrder order){
        RcAsyncEfPayReqDto reqDto = new RcAsyncEfPayReqDto();
        reqDto.setSequenceId(generateRiskSequenceId(order.getRequestId(),ConstantUtils.RISK_TYPE_ASYNC));
        reqDto.setRequestId(order.getOutTradeNo());
        reqDto.setPaymentId(order.getRequestId());
        reqDto.setBizOrder(order.getOrderNo());
        reqDto.setProductId(ConstantUtils.SYS_NO);
        reqDto.setProduction(order.getOrderSystem());  // + ConstantUtils.RISK_SUFFIX
//        reqDto.setAppId(order.ge);
        if (PayStatusEnum.SUCCESS.name().equals(order.getPayStatus())) {
            reqDto.setTradeResult("1");
        } else {
           reqDto.setTradeResult("0");
        }
        reqDto.setBankCardType(order.getPayBankcardType());
        reqDto.setBankChannelId(order.getPayInterface());
        reqDto.setBankId(order.getPayBank());
        reqDto.setBankTradeId(order.getBankTradeId());
        reqDto.setOrderType(order.getOrderType());
        reqDto.setUserId(order.getOpenId());
        reqDto.setUserType("MERCHANT");
        reqDto.setPayWay(order.getPlatformType());
        reqDto.setTransactionId(order.getTransactionId());
        reqDto.setCompleteTime(order.getModifyTime());
        reqDto.setOccurTime(order.getCreateTime());
        reqDto.setAmount(order.getTotalAmount().toString());
        reqDto.setMerchantNo(order.getCustomerNumber());
//        reqDto.setSynConsumeTime();
        //异步风控参数 added by dongbo.jiao 20180319 start
        //银行子系统订单号
        reqDto.setBankOrderNo(order.getOrderNo());
        //银行交易号
        reqDto.setBankTradeNo(order.getBankTradeId());
        //异步风控 added by dongbo.jiao 20180319 end
        return reqDto;
    }

    /**
     * 生成风控唯一标识
     * @param orderNo
     * @param riskType
     * @return
     */
    private String generateRiskSequenceId(String orderNo, String riskType){
        StringBuffer sb = new StringBuffer(ConstantUtils.RISK_SYS_NO);
        sb.append(riskType).append(ConstantUtils.RISK_SPLIT).append(RandomUtils.nextInt()).append(ConstantUtils.RISK_SPLIT).append(orderNo);
        return sb.toString();
    }

    /**
     * 转换风控请求参数,屏蔽goodsInfo字段
     * @param rcEfPayReqDto
     * @return
     */
    private String filterRcEfPayReqDto(Object rcEfPayReqDto){
        if(rcEfPayReqDto == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(rcEfPayReqDto.getClass().getSimpleName()).append("{");
        try{
            Field[] fields = rcEfPayReqDto.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(!field.getName().equals("goodsInfo")){
                    sb.append(field.getName()).append("=").append(field.get(rcEfPayReqDto)==null?"":field.get(rcEfPayReqDto).toString()).append(",");
                }
            }
        }catch(Exception e){
            logger.error("[打印风控请求参数] - [失败] - [忽略]",e);
        }
        finally {
            sb.append("}");
        }
        return sb.toString();
    }

}
