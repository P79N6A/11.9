package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.*;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * biz层是对service的组织，这里定义了所有的sevice
 * 
 * @author：peile.fan
 * @since：2016年5月19日 下午6:31:25
 * @version:
 */
public class NcCashierBaseBizImpl {
	protected static final Logger logger =
			NcCashierLoggerFactory.getLogger(NcCashierBaseBizImpl.class);
	@Resource
	protected PaymentProcessService paymentProcessService;

	@Resource
	protected PaymentRequestService paymentRequestService;
	@Resource
	protected UserProceeService userProceeService;
	@Resource
	protected NcPayService ncPayService;
	@Resource
	protected RiskControlService riskControlService;
	
	@Resource
	protected CashierBankCardService cashierBankCardService;
	@Resource
	protected CwhService cwhService;
	
	@Resource
	protected OrderProcessorService orderProcessorService;
	
	@Resource
	protected PayProcessorService payProcessorService;
	
	@Resource
	protected MerchantConfigCenterService merchantConfigCenterService;

	@Resource
	protected BankCardLimitInfoService bankCardLimitInfoService;

	@Resource
	protected NcCashierUserCenterService ncCashierUserCenterService;

	@Autowired
	protected UserRequestInfoService userRequestInfoService;

	@Resource
	protected OrderInfoAccessService orderInfoAccessAdapterService;
	
	@Resource
	protected MarketInfoManageService marketInfoManageService;
	
	protected void handleException(BasicResponseDTO response, Throwable e) {
		if (e instanceof CashierBusinessException) {
			String defineCode = ((CashierBusinessException) e).getDefineCode();
			String msg = ((CashierBusinessException) e).getMessage();
			logger.warn("[业务异常] - " + "errorCode:" + defineCode + ", errorMsg:" + msg, e);
			//考虑到各个DTO实体类中字段校验注释 被API接口和内部接口公用
			if(Errors.SYSTEM_INPUT_EXCEPTION.getCode().equals(defineCode)){
				msg = CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION).getMessage();
			}
			response.setProcessStatusEnum(ProcessStatusEnum.FAILED);
			response.setReturnCode(defineCode);
			response.setReturnMsg(msg);
			
		} else if (e instanceof IllegalArgumentException) {
			String defineCode = Errors.INPUT_PARAM_NULL.getCode();
			String errorMsg = CommonUtil.handleException(Errors.INPUT_PARAM_NULL).getMessage();
			response.setProcessStatusEnum(ProcessStatusEnum.FAILED);
			response.setReturnCode(defineCode);
			response.setReturnMsg(errorMsg);
			logger.error("[系统异常] - " + "errorCode:" + defineCode + ", errorMsg:" + errorMsg, e);
			
		} else {
			String defineCode = Errors.SYSTEM_EXCEPTION.getCode();
			String errorMsg = CommonUtil.handleException(Errors.SYSTEM_EXCEPTION).getMessage();
			response.setProcessStatusEnum(ProcessStatusEnum.FAILED);
			response.setReturnCode(defineCode);
			response.setReturnMsg(errorMsg);
			logger.error("[系统异常] - " + "errorCode:" + defineCode + ", errorMsg:" + errorMsg, e);
		}
	}

	protected PaymentRecord getPaymentRecord(Long recordId) {
		PaymentRecord payrecord =
				paymentProcessService.findRecordByPaymentRecordId(String.valueOf(recordId));
		return payrecord;
	}

	protected PaymentRequest findPaymentReqAndCheckValidateTime(Long paymentRequestId) {
		return paymentRequestService.findPaymentRequestByRequestId(paymentRequestId);
	}

	/**
	 * 校验订单是否支付成功
	 * @param paymentRequest
	 */
	protected void checkPaymentStatus(PaymentRequest paymentRequest) {
		if (paymentRequest == null) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
		List<PaymentRecord> recordList = paymentProcessService.findRecordListByOrderOrderId(
				paymentRequest.getOrderOrderId(), paymentRequest.getOrderSysNo());
		if(CollectionUtils.isNotEmpty(recordList)){
			for (PaymentRecord record : recordList) {
				if(PayRecordStatusEnum.SUCCESS==record.getState()){
					throw CommonUtil.handleException(Errors.THRANS_FINISHED);
				}
			}
		}
	}

	protected void buildBaicResponseDTOfromApi(APIBasicResponseDTO apiBasicResponseDTO, BasicResponseDTO responseDTO){
		responseDTO.setReturnCode(apiBasicResponseDTO.getCode());
		responseDTO.setReturnMsg(apiBasicResponseDTO.getMessage());
	}




}
