package com.yeepay.g3.core.frontend.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.entity.PayRecord;
import com.yeepay.g3.core.frontend.entity.Promotion;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;
import com.yeepay.g3.core.frontend.service.*;
import com.yeepay.g3.core.frontend.util.ConstantUtils;
import com.yeepay.g3.core.frontend.util.FrontEndIdGenerator;
import com.yeepay.g3.core.frontend.util.RedisUtil;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.*;
import com.yeepay.g3.facade.frontend.enumtype.*;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import com.yeepay.g3.utils.common.log.Logger;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Biz层基类
 * @author TML
 */
public abstract class AbstractBiz {


	@Autowired
	protected PayOrderService payOrderService;

	@Autowired
	protected PayRecordService payRecordService;

	@Autowired
	protected BankPayService bankPayService;

	@Autowired
	protected BankRouterPayService bankRouterPayService;

	@Autowired
	protected SendMqService sendMqService;

	@Autowired
	protected ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	protected RiskControlService riskControlService;

	@Autowired
	protected RouterRefundCenterService routerRefundCenterService;

	private static final Logger logger = FeLoggerFactory.getLogger(AbstractBiz.class);



//	protected void checkOrderInfo(BasicRequestDTO requestDTO,PayOrder payOrderOld,OrderType orderType) {
//
//		if(payOrderOld != null){
//			if(PayStatusEnum.SUCCESS.name().equals(payOrderOld.getPayStatus())){
//				throw new FrontendBizException(ErrorCode.F0002001);
//			}
//			checkRecordInfo(payOrderOld);
//			if(StringUtils.isNotBlank(payOrderOld.getOrderType()) && !StringUtils.equals(payOrderOld.getOrderType(),orderType.name())){
//				throw new FrontendBizException(ErrorCode.F0001003);
//			}
//			if(StringUtils.isNotBlank(payOrderOld.getCustomerNumber()) && !StringUtils.equals(payOrderOld.getCustomerNumber(),requestDTO.getCustomerNumber())){
//				throw new FrontendBizException(ErrorCode.F0001003);
//			}
//
//			if(StringUtils.isNotBlank(payOrderOld.getOutTradeNo()) && !StringUtils.equals(payOrderOld.getOutTradeNo(),requestDTO.getOutTradeNo())){
//				throw new FrontendBizException(ErrorCode.F0001003);
//			}
//
//			if(StringUtils.isNotBlank(payOrderOld.getPlatformType()) && !StringUtils.equals(payOrderOld.getPlatformType(),requestDTO.getPlatformType().name())){
//				throw new FrontendBizException(ErrorCode.F0001003);
//			}
//
//			if(payOrderOld.getTotalAmount() != null && !payOrderOld.getTotalAmount().equals(requestDTO.getTotalAmount())){
//				throw new FrontendBizException(ErrorCode.F0001003);
//			}
//
//			if(payOrderOld.getGoodsDescription() != null && !payOrderOld.getGoodsDescription().equals(requestDTO.getGoodsDescription())){
//				throw new FrontendBizException(ErrorCode.F0001003);
//			}
//			if(requestDTO instanceof ActiveScanJsapiRequestDTO){
//				ActiveScanJsapiRequestDTO activeScanJsapiRequestDTO = (ActiveScanJsapiRequestDTO)requestDTO;
//				if(StringUtils.isNotBlank(payOrderOld.getOpenId()) && !StringUtils.equals(payOrderOld.getOpenId(),activeScanJsapiRequestDTO.getOpenId())){
//					throw new FrontendBizException(ErrorCode.F0001003);
//				}
//			}
//			if(requestDTO instanceof AppPayRequestDTO){
//				AppPayRequestDTO appPayRequestDTO = (AppPayRequestDTO)requestDTO;
//				if(payOrderOld.getPayLimitType() != null && !payOrderOld.getPayLimitType().equals(appPayRequestDTO.getPayLimitType()!=null?appPayRequestDTO.getPayLimitType().name():null)){
//					throw new FrontendBizException(ErrorCode.F0001003);
//				}
//			}
//
//		}
//
//	}

	/**
	 * 支付信息校验
	 * @param payOrder
	 */
	protected void checkRecordInfo(PayOrder payOrder){
		int pay_times = payRecordService.countRecordByOrderNo(payOrder.getOrderNo(), payOrder.getPlatformType());
		if (pay_times >= ConstantUtils.PAY_LIMIT_TIMES){
			throw new FrontendBizException(ErrorCode.F0001006);
		}
	}

	/**
	 * 构建支付记录
	 * @param payOrder
	 * @return
	 */
	protected PayRecord buildPayRecord(PayOrder payOrder){
		PayRecord payRecord = new PayRecord();
		// 创建支付记录流水号
		payRecord.setRecordNo(payRecordService.generateId(payOrder.getPlatformType()));
		payRecord.setOrderNo(payOrder.getOrderNo());
		payRecord.setRequestId(payOrder.getRequestId());
		payRecord.setRequestSystem(payOrder.getRequestSystem());
		payRecord.setCustomerNumber(payOrder.getCustomerNumber());
		payRecord.setOutTradeNo(payOrder.getOutTradeNo());
		payRecord.setPlatformType(payOrder.getPlatformType());
		payRecord.setTotalAmount(payOrder.getTotalAmount());
//		payRecord.setResponseCode();
//		payRecord.setResponseMsg();
//		payRecord.setNocardCode();
//		payRecord.setNocardMsg();
//		payRecord.setCreateTime();
//		payRecord.setFrontValue();
		return payRecord;
	}

//	/**
//	 * 保存订单数据 prepay
//	 * @param payOrder
//	 */
//	protected void createPayOrder(PayOrder payOrder){
//		if(payOrder.getId() == null){
//			Timestamp createTime = new Timestamp(System.currentTimeMillis());
//			payOrder.setCreateTime(createTime);
//			//获取银行子系统订单号
//			payOrder.setOrderNo(getPayOrderNo(payOrder));
//			payOrderService.createPayOrder(payOrder);
//		}
//	}

//	/**
//	 * 获取支付订单号
//	 * @return
//	 */
//	protected String getPayOrderNo(PayOrder payOrder){
//		return bankPayService.getPayOrderNo(payOrder);
//	}

	/**
	 * 通用创建支付记录
	 */
	protected void createPayRecord(PayRecord payRecord){
		PayRecord payRecordToCreate = new PayRecord();
		BeanUtils.copyProperties(payRecord, payRecordToCreate);
		if(payRecordToCreate != null){
			if(payRecordToCreate.getFrontValue() != null && payRecordToCreate.getFrontValue().length() > 512) {
				payRecordToCreate.setFrontValue(null);
			}
			payRecordService.createPayRecord(payRecordToCreate);
		}
	}

	/**
	 * 更新普通的返回值结果
	 * @param repsonseDTO
	 * @param payOrder
	 */
	protected void updateBasicResponse(YeepayBizException exception,BasicResponseDTO repsonseDTO,PayOrder payOrder){
		if(payOrder != null){
			repsonseDTO.setCustomerNumber(payOrder.getCustomerNumber());
			repsonseDTO.setOrderNo(payOrder.getOrderNo());
			repsonseDTO.setOrderType(OrderType.getOrderType(payOrder.getOrderType()));
			repsonseDTO.setOutTradeNo(payOrder.getOutTradeNo());
			repsonseDTO.setPlatformType(PlatformType.getPlatformType(payOrder.getPlatformType()));
			repsonseDTO.setRequestId(payOrder.getRequestId());
			repsonseDTO.setRequestSystem(payOrder.getRequestSystem());
			repsonseDTO.setDealUniqueSerialNo(payOrder.getDealUniqueSerialNo());
			repsonseDTO.setTotalAmount(payOrder.getTotalAmount());
		}

		if(exception != null){
			repsonseDTO.setResponseCode(exception.getDefineCode());
			repsonseDTO.setResponseMsg(exception.getMessage());
		}
	}

	/**
	 * 统一加工处理返回值结果
	 * @param exception
	 * @param repsonseDTO
	 * @param payOrder
	 * @param payRecord
	 */
	protected void updateResponse(YeepayBizException exception,BasicResponseDTO repsonseDTO,PayOrder payOrder,PayRecord payRecord){
		updateBasicResponse(exception,repsonseDTO, payOrder);
//		if(repsonseDTO instanceof ActiveScanResponseDTO){
//			ActiveScanResponseDTO activeScanResponseDTO = (ActiveScanResponseDTO)repsonseDTO;
//			if(payOrder != null){
//				activeScanResponseDTO.setPayStatus(PayStatusEnum.getPayStatusEnum(payOrder.getPayStatus()));
//			}else{
//				activeScanResponseDTO.setPayStatus(PayStatusEnum.INIT);
//			}
//			if(payRecord != null){
//				switch (activeScanResponseDTO.getOrderType()) {
//				case ACTIVESCAN:
//					activeScanResponseDTO.setCodeUrl(payRecord.getFrontValue());
//					break;
//				case JSAPI:
//					activeScanResponseDTO.setPrepayId(payRecord.getFrontValue());
//					break;
//				case ALIPAYSCAN:
//					activeScanResponseDTO.setCodeUrl(payRecord.getFrontValue());
//					break;
//				default:
//					break;
//				}
//			}
//		}
//		if(repsonseDTO instanceof AppPayResponseDTO){
//			AppPayResponseDTO appPayResponseDTO = (AppPayResponseDTO)repsonseDTO;
//			if(payRecord != null){
//				appPayResponseDTO.setCodeUrl(payRecord.getFrontValue());
//			}
//			if(payOrder != null){
//				appPayResponseDTO.setPayStatus(PayStatusEnum.getPayStatusEnum(payOrder.getPayStatus()));
//			}else{
//				appPayResponseDTO.setPayStatus(PayStatusEnum.FAILURE);
//			}
//		}
		if(repsonseDTO instanceof BankNotifyResponseDTO){
			BankNotifyResponseDTO bankNotifyResponseDTO = (BankNotifyResponseDTO) repsonseDTO;
			if(payOrder != null){
				bankNotifyResponseDTO.setNotifyStatus(NotifyStatusEnum.getNotifyStatusEnum(payOrder.getPayStatus()));
				bankNotifyResponseDTO.setPageCallBack(payOrder.getPageCallBack());
			} else {
				bankNotifyResponseDTO.setNotifyStatus(NotifyStatusEnum.INIT);
			}
		}
		if(repsonseDTO instanceof FrontendQueryResponseDTO){
			if(payOrder != null){
				FrontendQueryResponseDTO frontendQueryResponseDTO = (FrontendQueryResponseDTO) repsonseDTO;
				frontendQueryResponseDTO.setBankTotalCost(payOrder.getBankTotalCost()==null?null:payOrder.getBankTotalCost());
				frontendQueryResponseDTO.setOpenId(payOrder.getOpenId());
				frontendQueryResponseDTO.setPayBank(payOrder.getPayBank());
				frontendQueryResponseDTO.setPayBankcardType(PayBankcardType.getPayBankcardType(payOrder.getPayBankcardType()));
				frontendQueryResponseDTO.setPayStatus(PayStatusEnum.getPayStatusEnum(payOrder.getPayStatus()));
				frontendQueryResponseDTO.setTransactionId(payOrder.getTransactionId());
				frontendQueryResponseDTO.setBankTradeId(payOrder.getBankTradeId());
				frontendQueryResponseDTO.setBankSuccessTime(payOrder.getBankSuccessTime());
				frontendQueryResponseDTO.setPaySuccessTime(payOrder.getPaySuccessTime());
				frontendQueryResponseDTO.setRefundStatus(RefundStatusEnum.getRefundStatusEnum(payOrder.getRefundStatus()));
				frontendQueryResponseDTO.setRefundType(RefundType.getRefundType(payOrder.getRefundType()));
				frontendQueryResponseDTO.setPayInterface(payOrder.getPayInterface());
				frontendQueryResponseDTO.setPaymentProduct(payOrder.getPaymentProduct());
				//add by dongbo.jiao 20170621 start
				//查单同步返回时,增加零售产品码和基础产品码
				frontendQueryResponseDTO.setRetailProductCode(payOrder.getRetailProductCode());
				frontendQueryResponseDTO.setBasicProductCode(payOrder.getBasicProductCode());
				//add by dongbo.jiao 20170621 end
				// 增加扩展参数 added by zhijun.wang 2017-12-13
				if(StringUtils.isNotBlank(payOrder.getExtParam())) {
					try {
						String extParamString = payOrder.getExtParam();
						Map<String, String> extParam = (Map<String, String>) JSONObject.parse(extParamString);
						frontendQueryResponseDTO.setExtParam(extParam);
					} catch (Exception e) {
						logger.error("解析扩展参数异常，requestId=" + payOrder.getRequestId(), e);
					}
				}
			}
		}

	}



	/**
	 * 异常部分处理
	 * @param exception
	 * @param payOrder
	 * @param payRecord
	 */
	protected void handleException(Throwable exception,BasicResponseDTO repsonseDTO,PayOrder payOrder,PayRecord payRecord){
		if(exception != null){
			logger.error(" - [异常] ", exception);
			FrontendBizException FrontendBizException = null;
			if(exception instanceof FrontendBizException){
				FrontendBizException = (FrontendBizException) exception;
			}else{
				FrontendBizException = new FrontendBizException(ErrorCode.F0001000);
			}
			if (payRecord != null) {
				payRecord.updateToFail(FrontendBizException.getDefineCode(),FrontendBizException.getMessage());
			}
			updateResponse(FrontendBizException, repsonseDTO, payOrder, payRecord);
		}
	}

	/**
	 * 是否需要更新订单
	 * @param payOrder
	 * @return
	 */
	protected boolean isUpdatePayOrder(PayOrder payOrder){
		if(payOrder != null && PayStatusEnum.SUCCESS.name().equals(payOrder.getPayStatus())){
			return false;
		}
		return true;
	}

	/**
	 * 检查回调接口状态
	 * @param payOrder
	 */
	protected void checkNotifyPayOrder(PayOrder payOrder){
		if(payOrder == null){
			throw new FrontendBizException(ErrorCode.F0002004);
		}
		//成功接收到路由回调，清除redis缓存
		RedisUtil.delFromRedis("ORDER_CHECK", payOrder.getOrderNo()+payOrder.getPayInterface());
		logger.info("[FE下单缓存]FE接收到路由回调，清除下单缓存，payOrderNo="+payOrder.getPayOrderNo()+"，orderNo="+payOrder.getOrderNo());
		if(PayStatusEnum.SUCCESS.name().equals(payOrder.getPayStatus())){
			throw new FrontendBizException(ErrorCode.F0002001);
		}
	}


	protected void updatePayOrder(boolean needUpdate, PayOrder payOrder) {
		if (payOrder != null && needUpdate) {
			payOrderService.updatePayOrder(payOrder);
		}
	}

	/**
	 * 单纯更新订单表
	 * @param payOrder
	 */
	protected void singleUpdate(PayOrder payOrder){
		if (payOrder != null){
			payOrderService.singleUpdate(payOrder);
		}
	}


    /**
     * 构建通知mq消息
     *
     * @param payOrder
     * @return
     */
    protected PayResultMessage buildPayResultMessage(PayOrder payOrder, Map<String, String> extParam,List<PromotionInfoDTO> promotionInfoDTOS) {
        PayResultMessage msg = new PayResultMessage();
        msg.setRequestSystem(payOrder.getRequestSystem());
        msg.setRequestId(payOrder.getRequestId());
        msg.setCustomerNumber(payOrder.getCustomerNumber());
        msg.setOutTradeNo(payOrder.getOutTradeNo());
        msg.setOrderNo(payOrder.getOrderNo());
        msg.setOrderType(OrderType.getOrderType(payOrder.getOrderType()));
        msg.setPlatformType(PlatformType.getPlatformType(payOrder.getPlatformType()));
        msg.setOpenId(payOrder.getOpenId());
        msg.setPayBank(payOrder.getPayBank());
        msg.setPayBankcardType(PayBankcardType.getPayBankcardType(payOrder.getPayBankcardType()));
        msg.setTotalAmount(payOrder.getTotalAmount());
        msg.setTransactionId(payOrder.getTransactionId());
        msg.setPayStatus(PayStatusEnum.getPayStatusEnum(payOrder.getPayStatus()));
        msg.setBankTotalCost(payOrder.getBankTotalCost());
        msg.setCreateTime(payOrder.getCreateTime());
        msg.setExpireTime(payOrder.getExpireTime());
        msg.setBankSuccessTime(payOrder.getBankSuccessTime());
        msg.setPaySuccessTime(payOrder.getPaySuccessTime());
        msg.setPayInterface(payOrder.getPayInterface());
        msg.setOpenId(payOrder.getOpenId());
        if (MapUtils.isNotEmpty(extParam)) {
            msg.setExtParam(extParam);
        }
        msg.setBasicProductCode(payOrder.getBasicProductCode());
        msg.setRetailProductCode(payOrder.getRetailProductCode());
        msg.setPayOrderNo(payOrder.getPayOrderNo());
        //卡券
        msg.setCashFee(payOrder.getCashFee());
        msg.setSettlementFee(payOrder.getSettlementFee());
        msg.setPromotionInfoDTOS(promotionInfoDTOS);
        return msg;
    }

    /**
     * 判断是否需要重新下单
	 * 如果缓存里面有支付记录,则不重新下单
	 * 对于APP下单可能存在一个问题,下单ip改变了,会怎样,需要测试
     * @param payOrder
     * @return
     */
    protected PayRecord reOrderValidate(PayOrder payOrder){
        if (payOrder == null || StringUtils.isBlank(payOrder.getOrderNo())){
        	return null;
        }
        PayRecord record = RedisUtil.getRecord(payOrder);
        return record;
    }

	/**
	 * 同步风控检验
	 * @param requestDTO
	 */
	protected void syncRiskControl(BasicRequestDTO requestDTO, PayOrder payOrder){
		riskControlService.syncControl(requestDTO , payOrder);
	}

	/**
	 * 异步风控请求
	 * @param payOrder
	 */
	protected void asyncRiskControl(PayOrder payOrder){
		riskControlService.asyncControl(payOrder);
	}

	/**
	 * 支付校验参数 prepay
	 * @param basicRequestDTO
	 * @param payOrderOld
	 */
	protected void checkOrderInfo(BasicRequestDTO basicRequestDTO,PayOrder payOrderOld){
		if(payOrderOld != null){
			if(PayStatusEnum.SUCCESS.name().equals(payOrderOld.getPayStatus())){
				throw new FrontendBizException(ErrorCode.F0002001);
			}
			int pay_times = payRecordService.countRecordByOrderNo(payOrderOld.getOrderNo(), payOrderOld.getPlatformType());
			if (pay_times >= ConstantUtils.PAY_LIMIT_TIMES){
				throw new FrontendBizException(ErrorCode.F0001006);
			}
			if(!StringUtils.equals(payOrderOld.getCustomerNumber(),basicRequestDTO.getCustomerNumber())){
				throw new FrontendBizException(ErrorCode.F0001003, "customerNumber exception");
			}
			if(!StringUtils.equals(payOrderOld.getOutTradeNo(),basicRequestDTO.getOutTradeNo())){
				throw new FrontendBizException(ErrorCode.F0001003, "outTradeNo exception");
			}
			if(!StringUtils.equals(payOrderOld.getPlatformType(),basicRequestDTO.getPlatformType().name())){
				throw new FrontendBizException(ErrorCode.F0001003, "platformType exception");
			}
			if(!payOrderOld.getTotalAmount().equals(basicRequestDTO.getTotalAmount())){
				throw new FrontendBizException(ErrorCode.F0001003, "totalAmount exception");
			}
			if(basicRequestDTO instanceof PayRequestDTO){
				PayRequestDTO payRequestDTO = (PayRequestDTO) basicRequestDTO;
				if(!StringUtils.equals(payOrderOld.getOrderType(),payRequestDTO.getOrderType().name())){
					throw new FrontendBizException(ErrorCode.F0001003, "orderType exception");
				}
				if(!StringUtils.equals(payOrderOld.getPayLimitType(), payRequestDTO.getPayBusinessType().name())){
					throw new FrontendBizException(ErrorCode.F0001003, "payBusinessType exception");
				}
				if(!StringUtils.equals(payOrderOld.getDealUniqueSerialNo(), payRequestDTO.getDealUniqueSerialNo())){
					throw new FrontendBizException(ErrorCode.F0001003, "dealUniqueSerialNo exception");
				}
			}
		}
	}

	/**
	 * 创建支付订单 prepay
	 * @param basicRequestDTO
	 * @param payOrderOld
	 * @return
	 */
	protected PayOrder buildPayOrder(BasicRequestDTO basicRequestDTO, PayOrder payOrderOld){
		if(payOrderOld != null){
			return payOrderOld;
		}else{
			PayOrder payOrder = new PayOrder();
			payOrder.setRefundStatus(RefundStatusEnum.NONE.name());
			payOrder.setRequestSystem(basicRequestDTO.getRequestSystem());
			payOrder.setRequestId(basicRequestDTO.getRequestId()); //改为系统唯一码
			payOrder.setPlatformType(basicRequestDTO.getPlatformType().name());
			payOrder.setCustomerNumber(basicRequestDTO.getCustomerNumber());
			payOrder.setOutTradeNo(basicRequestDTO.getOutTradeNo());
			payOrder.setTotalAmount(basicRequestDTO.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
			payOrder.setGoodsDescription(basicRequestDTO.getGoodsDescription());
			payOrder.setPayStatus(PayStatusEnum.INIT.name());
			payOrder.setPayerIp(basicRequestDTO.getPayerIp());
			payOrder.setOrderSystem(basicRequestDTO.getOrderSystem());
			if(basicRequestDTO instanceof PayRequestDTO){
				PayRequestDTO payRequestDTO = (PayRequestDTO) basicRequestDTO;
				payOrder.setPayOrderNo(generatePayOrderNo(payRequestDTO));
				payOrder.setOrderNo("");//注：orderNo字段非空
				payOrder.setOrderType(payRequestDTO.getOrderType().name());
				payOrder.setPageCallBack(payRequestDTO.getPageCallBack());
				payOrder.setPayLimitType(payRequestDTO.getPayBusinessType().name());
				payOrder.setPaymentProduct(payRequestDTO.getPaymentProduct());
				payOrder.setDealUniqueSerialNo(payRequestDTO.getDealUniqueSerialNo());
				payOrder.setBankTotalCost(payRequestDTO.getBankTotalCost());
				payOrder.setPayInterface(payRequestDTO.getPayInterface());
				payOrder.setOpenId(payRequestDTO.getOpenId());
				//add by dongbo.jiao 20170621 start
				//增加基础产品码和零售产品码
				payOrder.setRetailProductCode(payRequestDTO.getRetailProductCode());
				payOrder.setBasicProductCode(payRequestDTO.getBasicProductCode());
				//add by dongbo.jiao 20170621 end
				//哆啦宝粉丝路由功能新增字段 reportId
				payOrder.setReportId(payRequestDTO.getExtParam()!=null?payRequestDTO.getExtParam().get(ConstantUtils.WX_REPORT_ID):"");
			}
			Timestamp createTime = new Timestamp(System.currentTimeMillis());
			payOrder.setCreateTime(createTime);
			return payOrder;
		}
	}

	/**
	 * 支付请求返回参数 prepay
	 * @param exception
	 * @param basicResponseDTO
	 * @param payOrder
	 * @param payRecord
	 */
    protected void buildResponse(YeepayBizException exception, BasicResponseDTO basicResponseDTO,
    		PayOrder payOrder, PayRecord payRecord){
    	if(payOrder != null){
    		basicResponseDTO.setCustomerNumber(payOrder.getCustomerNumber());
    		basicResponseDTO.setOrderNo(payOrder.getOrderNo());
    		basicResponseDTO.setOrderType(OrderType.getOrderType(payOrder.getOrderType()));
    		basicResponseDTO.setOutTradeNo(payOrder.getOutTradeNo());
    		basicResponseDTO.setPlatformType(PlatformType.getPlatformType(payOrder.getPlatformType()));
    		basicResponseDTO.setRequestId(payOrder.getRequestId());
    		basicResponseDTO.setRequestSystem(payOrder.getRequestSystem());
    		basicResponseDTO.setTotalAmount(payOrder.getTotalAmount());
    		basicResponseDTO.setDealUniqueSerialNo(payOrder.getDealUniqueSerialNo());
			basicResponseDTO.setPayOrderNo(payOrder.getPayOrderNo());
		}
		if(exception != null){
			basicResponseDTO.setResponseCode(exception.getDefineCode());
			basicResponseDTO.setResponseMsg(exception.getMessage());
		}
		if(basicResponseDTO instanceof PayResponseDTO){
			PayResponseDTO payResponseDTO = (PayResponseDTO) basicResponseDTO;
			if(payRecord != null){
				payResponseDTO.setPrepayCode(payRecord.getFrontValue());
			}
			if(payOrder != null
					&& OrderType.PASSIVESCAN.name().equals(payOrder.getOrderType())){
				payResponseDTO.setBankTotalCost(payOrder.getBankTotalCost());
				payResponseDTO.setPayBank(payOrder.getPayBank());
				payResponseDTO.setPayBankcardType(PayBankcardType.getPayBankcardType(payOrder.getPayBankcardType()));
				payResponseDTO.setPayInterface(payOrder.getPayInterface());
				payResponseDTO.setPayStatus(PayStatusEnum.getPayStatusEnum(payOrder.getPayStatus()));
				payResponseDTO.setTransactionId(payOrder.getTransactionId());
				payResponseDTO.setOpenId(payOrder.getOpenId());// added by zhijun.wang 2017-07-26
				//added by zengzhi.han 20181016 被扫支付相应参数增加是否需要密码
				if (StringUtils.isNotBlank(payOrder.getExtParam())
						&&(Map<String, String>) JSONObject.parse(payOrder.getExtParam())!=null){
					Map<String,String> returnMap =null;
					if(payResponseDTO.getExtParam()!=null){
						returnMap = payResponseDTO.getExtParam();
					}else{
						returnMap= new HashMap<String, String>();
					}
					returnMap.put(ConstantUtils.FE_RETURN_PP_RESPONSE_IS_NEEDPASSWORD,
							((Map<String, String>)JSONObject.parse(payOrder.getExtParam())).get(ConstantUtils.FE_RETURN_PP_RESPONSE_IS_NEEDPASSWORD));
					payResponseDTO.setExtParam(returnMap);
				}
    		}
		}
	}

    /**
	 * 异常部分处理
	 * @param exception
	 * @param payOrder
	 * @param payRecord
	 */
	protected void processException(Throwable exception, BasicResponseDTO basicResponseDTO,
			PayOrder payOrder, PayRecord payRecord){
		if(exception != null){
			logger.error(" - [异常] ", exception);
			FrontendBizException frontendBizException = null;
			if(exception instanceof FrontendBizException){
				frontendBizException = (FrontendBizException) exception;
			}else{
				frontendBizException = new FrontendBizException(ErrorCode.F0001000, exception.getMessage());
			}
			if (payRecord != null) {
				payRecord.updateToFail(frontendBizException.getDefineCode(),frontendBizException.getMessage());
			}
			createPayRecord(payRecord);
			buildResponse(frontendBizException, basicResponseDTO, payOrder, payRecord);
		}
	}

    /**
     * 生成新订单
     *
     * @param payOrder
     */
    protected void createPayOrder(PayOrder payOrder) {
        if (payOrder == null) {
            return;
        }
        // added by zhijun.wang 2017-06-19
        // pageCallBack超过256不存储
        if (payOrder.getPageCallBack() != null && payOrder.getPageCallBack().length() > 256) {
            payOrder.setPageCallBack("");
        }
        payOrderService.createPayOrder(payOrder);
    }

    /**
     * 更新原订单
     *
     * @param payOrder
     */
    protected void updatePayOrder(PayOrder payOrder) {
        if (payOrder == null) {
            return;
        }
        // added by zhijun.wang 2017-06-19
        // pageCallBack超过256不存储
        if (payOrder.getPageCallBack() != null && payOrder.getPageCallBack().length() > 256) {
            payOrder.setPageCallBack("");
        }
        payOrderService.updatePayOrder(payOrder);
    }

    /**
     * 更新订单信息
     *
     * @param payOrder
     * @param bankNotifyRequestDTO
     */
    protected void updateNotifyOrder(PayOrder payOrder, BankNotifyRequestDTO bankNotifyRequestDTO, Map<String, String> extParam) {
        payOrder.setPayStatus(bankNotifyRequestDTO.getPayStatus().name());
        if (StringUtils.isNotBlank(bankNotifyRequestDTO.getPayBank())) {
            payOrder.setPayBank(bankNotifyRequestDTO.getPayBank());
        }
        String bankCardType = bankNotifyRequestDTO.getPayBankcardType() != null
                ? bankNotifyRequestDTO.getPayBankcardType().name() : null;
        payOrder.setPayBankcardType(bankCardType);
        payOrder.setTransactionId(bankNotifyRequestDTO.getTransactionId());
        payOrder.setPaySuccessTime(bankNotifyRequestDTO.getPaySuccessTime());
        payOrder.setBankSuccessTime(bankNotifyRequestDTO.getBankSuccessTime());
        payOrder.setBankTradeId(bankNotifyRequestDTO.getBankTradeId());
        payOrder.setBankTotalCost(payOrder.getBankTotalCost() == null ? (bankNotifyRequestDTO.getBankTotalCost() == null ?
                null : bankNotifyRequestDTO.getBankTotalCost().setScale(4, RoundingMode.HALF_UP)) : payOrder.getBankTotalCost());
        payOrder.setNotifyStatus(NotifyStatusEnum.INIT.name());
        payOrder.setPayInterface(bankNotifyRequestDTO.getPayInterface());
        payOrder.setOpenId(bankNotifyRequestDTO.getOpenId());// added by zhijun.wang 2017-07-25
        //added by zengzhi.han 20181016 增加易宝公众号下的商户openId
        payOrder.setYeepayOpenId(bankNotifyRequestDTO.getYeepayOpenId());
        payOrder.setOrderNo(bankNotifyRequestDTO.getOrderNo());
        //卡券 现金支付金额
        if (bankNotifyRequestDTO.getCashFee()!=null){
            payOrder.setCashFee(bankNotifyRequestDTO.getCashFee());
        }
        //卡券 应结算金额
        if (bankNotifyRequestDTO.getSettlementFee()!=null){
            payOrder.setSettlementFee(bankNotifyRequestDTO.getSettlementFee());
        }
        //扩展信息转成json格式字符串存入payOrder中 added by zhijun.wang 2017-12-12
        if (MapUtils.isNotEmpty(bankNotifyRequestDTO.getExtParam())) {
            try {
                payOrder.setExtParam(JSONObject.toJSONString(bankNotifyRequestDTO.getExtParam()));
            } catch (Exception e) {
                logger.error("更新扩展信息失败" + payOrder.getOrderNo(), e);
            }
            extParam.putAll(bankNotifyRequestDTO.getExtParam());
        }
        // 扩展信息 added by zhijun.wang 2017-05-24
        extParam.put("bankTradeId", bankNotifyRequestDTO.getBankTradeId());                    // 交易流水号(交易凭证号)
        extParam.put("reportMerchantNo", bankNotifyRequestDTO.getReportMerchantNo());        // 报备商户号(银行商户号)
        extParam.put("payerBankAccountNo", bankNotifyRequestDTO.getPayerBankAccountNo());    // 付款方账户号(卡号)
        extParam.put("couponInfo", bankNotifyRequestDTO.getCouponInfo());                    // 优惠信息

//        Map<String, String> promotionInfoMap = new HashMap<String, String>();
//        //added by zengzhi.han 20181019 卡券 现金支付金额
//        if (bankNotifyRequestDTO.getCashFee() != null) {
//            promotionInfoMap.put(ConstantUtils.PROMOTION_CASH_FEE, bankNotifyRequestDTO.getCashFee().toString());
//        }
//        //added by zengzhi.han 20181019 卡券 应结算金额
//        if (bankNotifyRequestDTO.getSettlementFee() != null) {
//            promotionInfoMap.put(ConstantUtils.PROMOTION_SETTLEMENT_FEE, bankNotifyRequestDTO.getSettlementFee().toString());
//        }
//        //added by zengzhi.han 20181019 卡券 优惠券信息
//        if (bankNotifyRequestDTO.getPromotionInfoDTOS() != null && !bankNotifyRequestDTO.getPromotionInfoDTOS().isEmpty()) {
//            promotionInfoMap.put(ConstantUtils.PROMOTION_INFO_DTOS, JSONObject.toJSONString(bankNotifyRequestDTO.getPromotionInfoDTOS()));
//        }
//        payOrder.setPromotionInfos(JSONObject.toJSONString(promotionInfoMap));
    }

    /**
     * 构建优惠券信息
     * @param bankNotifyRequestDTO
     * @param payOrder
     */
    protected List<Promotion> buildPromotion(BankNotifyRequestDTO bankNotifyRequestDTO,PayOrder payOrder){

        if (bankNotifyRequestDTO!=null&&bankNotifyRequestDTO.getPromotionInfoDTOS()!=null&&!bankNotifyRequestDTO.getPromotionInfoDTOS().isEmpty()){
            List<PromotionInfoDTO> promotionInfoDTOS = bankNotifyRequestDTO.getPromotionInfoDTOS();
            if (promotionInfoDTOS!=null&&!promotionInfoDTOS.isEmpty()){
                List<Promotion> promotions = new LinkedList<Promotion>();
                for (PromotionInfoDTO promotionInfoDTO:promotionInfoDTOS){
                    Promotion promotion = new Promotion();
                    BeanUtils.copyProperties(promotionInfoDTO, promotion);
                    promotion.setPayOrderNo(payOrder.getPayOrderNo());
                    promotions.add(promotion);
                }
                return promotions;
            }
        }
        return null;
    }




    /**
     * 异步处理
     * 通知业务方
     * 异步风控
     *
     * @param payOrder
     */
    protected void asyncProcess(final PayOrder payOrder, final Map<String, String> extParam, final List<PromotionInfoDTO> promotionInfoDTOS) {
        //判断是否需要通知
        if (!PayStatusEnum.SUCCESS.name().equals(payOrder.getPayStatus())
                || !NotifyStatusEnum.INIT.name().equals(payOrder.getNotifyStatus())) {
            return;
        }
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    if (TransactionSynchronizationAdapter.STATUS_COMMITTED == status) {
                        taskExecutor.execute(new AsyncProcess(payOrder, extParam,promotionInfoDTOS));
                    }
                }
            });
        } else {
            taskExecutor.execute(new AsyncProcess(payOrder, extParam,promotionInfoDTOS));
        }
    }

    private class AsyncProcess implements Runnable {
        private PayOrder payOrder;
        private Map<String, String> extParam = new HashMap<String, String>();
        private List<PromotionInfoDTO> promotionInfoDTOS;
        public AsyncProcess(PayOrder order, Map<String, String> extParam,List<PromotionInfoDTO> promotionInfoDTOS) {
            this.payOrder = order;
            this.extParam = extParam;
            this.promotionInfoDTOS = promotionInfoDTOS;
        }

        @Override
        public void run() {
            notifyAndUpdate(payOrder, extParam,promotionInfoDTOS);
            asyncRiskControl(payOrder);
        }
    }

    /**
     * 通知业务方,并更新通知状态
     *
     * @param payOrder
     */
    protected void notifyAndUpdate(PayOrder payOrder, Map<String, String> extParam,List<PromotionInfoDTO> promotionInfoDTOS) {
        sendMessageMq(payOrder, extParam,promotionInfoDTOS);
        if (NotifyStatusEnum.SUCCESS.name().equals(payOrder.getNotifyStatus())) {
            return;
        }
        payOrder.setNotifyStatus(NotifyStatusEnum.SUCCESS.name());
        singleUpdate(payOrder);
    }

    /**
     * 调用通知子系统通知消息
     */
    private void sendMessageMq(PayOrder payOrder, Map<String, String> extParam,List<PromotionInfoDTO> promotionInfoDTOS) {
        PayResultMessage payResultMessage = buildPayResultMessage(payOrder, extParam,promotionInfoDTOS);
        sendMqService.sendPayResultMessageMq(payResultMessage, payResultMessage.getRequestSystem().toUpperCase());
    }

	/**
	 * 根据请求系统和请求系统订单号查询订单,查不到抛异常
	 * @param requestSystem
	 * @param requestId
	 * @return
	 */
	protected PayOrder queryBySystemAndRequestId(String requestSystem, String requestId, String platformType) {
		PayOrder payOrder = payOrderService.queryBySystemAndRequestId(requestSystem, requestId, platformType);
		if (payOrder == null) {
			throw new FrontendBizException(ErrorCode.F0002004, "订单不存在");
		}
		return payOrder;
	}
	
//	private Map<String, String> json2Map(String jsonStr) {
//		Map<String,String> extParamMap = null;
//		if(jsonStr != null && jsonStr.trim().length() > 0) {
//			extParamMap = JSONUtils.jsonToMap(jsonStr, String.class, String.class);
//		}else {
//			extParamMap = new HashMap<String, String>();
//		}
//		return extParamMap;
//	}
//	
//	private String map2Json(Map<String, String> extParamMap) {
//		String jsonStr = null;
//		if(extParamMap != null && extParamMap.size() > 0) {
//			jsonStr = JSONUtils.toJsonString(extParamMap);
//		}else {
//			jsonStr = "no extParam";
//		}
//		return jsonStr;
//	}
private String generatePayOrderNo(PayRequestDTO payRequestDTO) {
	PlatformType platformType = payRequestDTO.getPlatformType();
	OrderType orderType = payRequestDTO.getOrderType();
	String platformAndOrderTypeNum = platformType.getNum() + "" + orderType.getNum();
	String payOrderNo = FrontEndIdGenerator.getNextFormattedId(platformAndOrderTypeNum);
	if (StringUtils.isBlank(payOrderNo)) {
		throw new FrontendBizException(ErrorCode.F0001000, "FE生成订单号异常");
	}
	return payOrderNo;
}
}
