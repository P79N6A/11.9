package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantPlatformService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.AccountPayService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.mp.dto.TradePasswordValidateDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.CommonUtils;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountSyncPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 账户支付service服务实现类
 * 
 * @author duangduang
 * @date 2017-06-06
 */
@Service("accountPayService")
public class AccountPayServiceImpl extends NcCashierBaseService implements AccountPayService {

	@Autowired
	private MerchantPlatformService merchantPlatformService;

	@Autowired
	private PaymentProcessService paymentProcessService;

	@Resource
	private PayProcessorService payProcessorService;
	
	@Resource
	private PaymentRequestService paymentRequestService;

	@Override
	public void validateMerchantPermission(String merchantAccountNo) {
		boolean validateResult = merchantPlatformService.validatePermission(merchantAccountNo);
		if (!validateResult) {
			throw new CashierBusinessException(Errors.MERCHANT_PERMISSION_INVALID);
		}
	}

	@Override
	public void validateTradePassword(AccountPayValidateRequestDTO requestDTO,
			AccountPayValidateResponseDTO responseDTO) {
		TradePasswordValidateDTO validateRes = merchantPlatformService
				.validateTradePassword(requestDTO.getUserAccount(), requestDTO.getTradePassword());
		if (validateRes.getIsTradeValidate() == null || !validateRes.getIsTradeValidate()) { // 交易密码还未认证
			// 重试次数不为空时且大于等于0时设置值
			if (validateRes.getRetryTime() != null && validateRes.getRetryTime() >= 0) {
				responseDTO.setRetryTimes(validateRes.getRetryTime());
			}
			setFrozenTimeAndType(validateRes.getFrezonSecond(), responseDTO);
			throw new CashierBusinessException(Errors.MERCHANT_TRADE_PASSWORD_INVALID);
		} else { // 交易密码认证通过，将付款商编返回
			responseDTO.setDebitCustomerNo(validateRes.getMerchantNumber());
		}

	}

	/**
	 * 设置密码冻结时间
	 * 
	 * @param frozenSecond
	 * @param responseDTO
	 */
	private void setFrozenTimeAndType(Long frozenSecond, AccountPayValidateResponseDTO responseDTO) {
		// 冻结时间不会小于1min（现在是5min）
		if (frozenSecond != null && frozenSecond >= 0) {
			BigDecimal frozenSecondTime = new BigDecimal(frozenSecond);
			BigDecimal divisor = new BigDecimal(60);
			BigDecimal frozenMinuteTime = frozenSecondTime.divide(divisor, RoundingMode.UP);
			responseDTO.setFrozenTime(frozenMinuteTime.longValue());
			responseDTO.setFrozenTimeType(TimeTypeEnum.MINUTE);
		}
	}
	

	@Override
	public PaymentRecord needCreateNewRecord(CashierAccountPayRequestDTO requestDTO) {
		if (requestDTO.getRecordId() == null || requestDTO.getRecordId() <= 0) {
			// 当前用户首次支付，需要创建支付记录单
			return null;
		}
		PaymentRecord record = paymentProcessService
				.findRecordByPaymentRecordId(String.valueOf(requestDTO.getRecordId()));
		if (record == null || !PayTypeEnum.ZF_ZHZF.name().equals(record.getPayType())) {
			// 支付记录不存在或当前支付记录非账户支付的支付记录，都要重新创建
			return null;
		}
		
		return record;
	}

	@Override
	public PaymentRecord createRecordAndUpdateUserAccount(CashierAccountPayRequestDTO requestDTO,
			PaymentRequest paymentRequest) {
		PaymentRecord paymentRecord = buildPaymentRecord(requestDTO, paymentRequest);
		long recordId = paymentProcessService.savePaymentRecord(paymentRecord);
		userProceeService.getAndUpdatePaymentRecordId(requestDTO.getTokenId(), recordId + "");
		return paymentRecord;
	}

	/**
	 * 构建支付记录
	 * 
	 * @param requestDTO
	 * @param payRequest
	 * @return
	 */
	private PaymentRecord buildPaymentRecord(CashierAccountPayRequestDTO requestDTO, PaymentRequest payRequest) {
		PaymentRecord paymentRecord = new PaymentRecord();
		paymentRecord.setAreaInfo(payRequest.getAreaInfo());
		paymentRecord.setBizModeCode(payRequest.getBizModeCode());
		paymentRecord.setCreateTime(new Date());
		paymentRecord.setIdCardType(IdCardTypeEnum.IDENTITY.toString());
		paymentRecord.setMcc(payRequest.getIndustryCatalog());
		paymentRecord.setMerchantName(payRequest.getMerchantName());
		paymentRecord.setMerchantNo(payRequest.getMerchantNo());
		paymentRecord.setMerchantOrderId(payRequest.getMerchantOrderId());
		paymentRecord.setPaymentAmount(payRequest.getOrderAmount());
		paymentRecord.setPaymentRequestId(payRequest.getId());
		if (!CommonUtils.isPayProcess(payRequest.getPaySysCode(), payRequest.getTradeSysNo())) {
			paymentRecord.setPaymentSysNo(PaymentSysCode.FE);
		} else {
			paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
		}
		paymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
		paymentRecord.setProductName(payRequest.getProductName());
		paymentRecord.setState(PayRecordStatusEnum.INIT);
		paymentRecord.setTokenId(requestDTO.getTokenId());
		paymentRecord.setTradeSysNo(payRequest.getTradeSysNo());
		paymentRecord.setTradeSysOrderId(payRequest.getTradeSysOrderId());
		paymentRecord.setOrderOrderId(payRequest.getOrderOrderId());
		paymentRecord.setOrderSysNo(payRequest.getOrderSysNo());
		paymentRecord.setMemberNo("");
		paymentRecord.setMemberType(
				StringUtils.isNotBlank(payRequest.getMemberType()) ? payRequest.getMemberType() : Constant.JOINLY);
		paymentRecord.setUpdateTime(new Date());
		paymentRecord.setVersion(1); 

		paymentRecord.setPayType(PayTypeEnum.ZF_ZHZF.name());
		paymentRecord.setPayTool(PayTool.ZF_ZHZF.name());

		return paymentRecord;
	}
	
	private String buildGoodsInfo(MerchantAccountPayRequestInfo requestInfo, PaymentRequest paymentRequest){
		String riskInfo = null;
		if(StringUtils.isNotBlank(requestInfo.getTokenId())){
			riskInfo = buildTradeRiskInfoUseTokenAndRequest(requestInfo.getTokenId(), paymentRequest);
		}else{
			riskInfo = buildTradeRiskInfoByUseripAndRequest(requestInfo.getUserIp(), paymentRequest);
		}
		return riskInfo;
	}

	@Override
	public void accountPay(PaymentRequest paymentRequest, PaymentRecord paymentRecord,
			MerchantAccountPayRequestInfo requestInfo) {
		AccountPayRequestDTO accountPayRequestDTO = buildAccountPayRequestDTO(paymentRequest, paymentRecord, requestInfo);
		AccountPayResponseDTO responseDTO = payProcessorService.accountPay(accountPayRequestDTO);
		paymentProcessService.updateRecordNo(paymentRecord.getId(), "", responseDTO.getRecordNo(),
				PayRecordStatusEnum.PAYING); 
	}
	
	/**
	 * 构建支付处理器账户支付的请求入参
	 * 
	 * @param paymentRequest
	 * @param paymentRecord
	 * @param requestInfo
	 * @return
	 */
	private AccountPayRequestDTO buildAccountPayRequestDTO(PaymentRequest paymentRequest, PaymentRecord paymentRecord,
			MerchantAccountPayRequestInfo requestInfo) {
		AccountPayRequestDTO accountPayRequestDTO = new AccountPayRequestDTO();
		// 设置通用下单参数
		buildBasicRequestDTO(paymentRequest, accountPayRequestDTO);
		// 补充通用下单参数
		accountPayRequestDTO.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
		accountPayRequestDTO.setPayProduct(paymentRecord.getPayTool());

		String riskInfo = buildGoodsInfo(requestInfo, paymentRequest);
		accountPayRequestDTO.setGoodsInfo(riskInfo);

		accountPayRequestDTO.setBasicProductCode(
				CommonUtil.getBasicProductCode(paymentRecord.getPayTool(), paymentRequest.getTradeSysNo()));
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		accountPayRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));// 零售产品码

		// 设置账户支付特有入参
		buildAccountPayRequestDTO(accountPayRequestDTO, paymentRequest, requestInfo);
		return accountPayRequestDTO;
	}

	/**
	 * 构建下游系统账户支付特定参数
	 * 
	 * @param accountPayRequestDTO
	 * @param request
	 * @param requestInfo
	 */
	private void buildAccountPayRequestDTO(AccountPayRequestDTO accountPayRequestDTO, PaymentRequest request,
			MerchantAccountPayRequestInfo requestInfo) {
		accountPayRequestDTO.setPayOrderType(PayOrderType.ACCOUNT);
		accountPayRequestDTO.setDebitCustomerNo(requestInfo.getDebitCustomerNo());
		accountPayRequestDTO.setDebitUserName(requestInfo.getUserAccount());
		ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest
				.getFromJson(request.getExtendInfo());
		if (extendInfoFromPayRequest != null) {
			accountPayRequestDTO.setExtInfo(extendInfoFromPayRequest.getAccountPayExt());
		}
	}

	
	/**
	 * 构造匹配是否存在可以使用的record的比对条件
	 * 
	 * @return
	 */
	private RecordCondition buildAccountPayCompareCondition() {
		RecordCondition condition = new RecordCondition();
		condition.setPayTool(PayTool.ZF_ZHZF.name());
		String[] recordPayTypes = { PayTypeEnum.ZF_ZHZF.name() };
		condition.setRecordPayTypes(recordPayTypes);
		return condition;
	}
	
	/**
	 * 获取PaymentRequest和符合条件的paymentRecord，若不存在PaymentRequest时创建一条，
	 * 若不存在符合条件的PaymentRecord时创建一条
	 * 
	 * @param requestInfo
	 * @param orderInfo
	 * @param merchantInNetConfig
	 * @param productLevel
	 * @return
	 */
	private CombinedPaymentDTO getRequestAndRecord(MerchantAccountPayRequestInfo requestInfo,
			OrderDetailInfoModel orderInfo, MerchantInNetConfigResult merchantInNetConfig, ProductLevel productLevel) {
		// 获取paymentRequest，当不存在时创建paymentRequest
		PaymentRequestExtInfo extInfo = buildPaymentRequestExtInfo(requestInfo.getUserIp());
		PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig, productLevel, extInfo);
		// 构建paymentRecord的查找条件
		RecordCondition compareCondition = buildAccountPayCompareCondition();
		// 根据recordCondition查找paymentRecord，没有符合条件的paymentRecord时需要重新创建 
		PaymentRecord paymentRecord = paymentProcessService.createRecordWhenUnexsit(paymentRequest, compareCondition,
				null, PayTypeEnum.ZF_ZHZF.name(), null, null, null,null);
		CombinedPaymentDTO combinedPaymentDTO = new CombinedPaymentDTO(paymentRequest, paymentRecord);
		return combinedPaymentDTO;
	}
	
	@Override
	public void accountPay(MerchantAccountPayRequestInfo requestInfo, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfig, ProductLevel productLevel) {
		// 获取paymentRequest和paymentRecord
		CombinedPaymentDTO combinedPaymentDTO = getRequestAndRecord(requestInfo, orderInfo, merchantInNetConfig,
				productLevel);
		// 防重处理
		paymentProcessService.avoidRepeatPayWithException(combinedPaymentDTO.getPaymentRecord(),
				new PayRecordStatusEnum[] { PayRecordStatusEnum.INIT });
		// 调用支付处理器完成账户支付
		AccountSyncPayResponseDTO responseDTO = null;
		try {
			responseDTO = accountSyncPay(combinedPaymentDTO.getPaymentRequest(), combinedPaymentDTO.getPaymentRecord(),
					requestInfo);
		} catch (CashierBusinessException e) {
			// 先回滚状态，再抛异常
			paymentProcessService.recoverRecordToObjStatus(combinedPaymentDTO.getPaymentRecord().getId(),
					PayRecordStatusEnum.INIT, PayRecordStatusEnum.PAYING);
			throw e;
		}
		// 处理processStatus=SUCCESS，但是trxStatus不为成功的情况，这种情况无需回滚record的状态
		handleTrxStatusOfPPWithException(responseDTO.getTrxStatus(), responseDTO);
	}

	/**
	 * 调用支付处理器账户支付同步接口
	 * 
	 * @param paymentRequest
	 * @param paymentRecord
	 * @param requestInfo
	 */
	private AccountSyncPayResponseDTO accountSyncPay(PaymentRequest paymentRequest, PaymentRecord paymentRecord,
			MerchantAccountPayRequestInfo requestInfo) {
		AccountPayRequestDTO accountPayRequestDTO = buildAccountPayRequestDTO(paymentRequest, paymentRecord,
				requestInfo);
		AccountSyncPayResponseDTO responseDTO = payProcessorService.accountSyncPay(accountPayRequestDTO);
		PayRecordStatusEnum recordStatus = checkSyncTrxStatusOfPP(responseDTO.getTrxStatus());
		paymentProcessService.updateRecordNo(paymentRecord.getId(), "", responseDTO.getRecordNo(), recordStatus);
		return responseDTO;
	}
	private PaymentRequestExtInfo buildPaymentRequestExtInfo(String userIp) {
		PaymentRequestExtInfo extInfo = new PaymentRequestExtInfo();
		extInfo.setUserIp(userIp);
		return extInfo;
	}
}
