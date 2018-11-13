package com.yeepay.g3.core.payprocessor.biz.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.external.service.*;
import com.yeepay.g3.core.payprocessor.service.*;
import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.enumtype.*;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.constants.PlatformTypeConstants;
import com.yeepay.g3.core.payprocessor.entity.ExtendedInfo;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.util.CommonUtils;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.facade.cwh.param.BankCardDetailDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import com.yeepay.g3.utils.common.CheckUtils;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 公共的方法，service的引用
 * 
 * @author peile.fan
 *
 */
public class BaseBizImpl {

	/** 引用所有的service **/

	@Autowired
	protected NcPayService ncPayService;

	@Autowired
	protected PaymentRequestService paymentRequestService;

	@Autowired
	protected PayCardInfoService payCardInfoService;

	@Autowired
	protected PayRecordService payRecordService;

	@Autowired
	protected ReverseRecordService reverseRecordService;

	@Autowired
	protected FrontEndService frontEndService;

	@Autowired
	protected BankCardService bankCardService;
	
	@Autowired
    protected AccountPayService accountPayService;

	@Autowired
	protected NcPayResultProccess ncPayResultProccess;

	@Autowired
	protected FeResultProccess feResultProccess;

	@Autowired
	protected CflPayResultProcess cflPayResultProcess;

	@Autowired
	protected NetPayResultProcess netPayResultProcess;
	
	@Autowired
    protected AccountPayResultProccess accountPayResultProccess;

	@Autowired
	protected NcPayResultPreAuthProccess ncPayResultPreAuthProccess;

	@Autowired
	protected PreAuthReverseRecordService preAuthReverseRecordService;
	
	@Autowired
    protected PersonalMemberPayService personalMemberPayService;
	
	@Autowired
	protected PersonalMemberPayResultProccess personalMemberPayResultProccess;

	@Autowired
    protected GuaranteeCflResultProccess guaranteeCflResultProccess;

	@Autowired
	protected CombPayRecordService combPayRecordService;

	@Autowired
	protected MktgService mktgService;

	@Autowired
	protected CombAbstractService combAbstractService;

	@Autowired
	protected HisPaymentRequestService hisPaymentRequestService;

	@Autowired
	protected HisPayRecordService hisPayRecordService;

	@Autowired
	protected NcPayCflEasyService ncPayCflEasyService;
	
	protected void setErrorInfo(ResponseStatusDTO respones, Throwable e) {
		respones.setProcessStatus(ProcessStatus.FAILED);
		String errorCode = "";
		String errorMsg = "";
		if (e instanceof PayBizException) {
			// 业务异常
			errorCode = ((PayBizException) e).getDefineCode();
			errorMsg = e.getMessage();
			respones.setResponseCode(errorCode);
			respones.setResponseMsg(errorMsg);
		} else {
			// 系统异常
			respones.setResponseCode(ErrorCode.P9001000);
			respones.setResponseMsg(ErrorCodeUtil.retrieveErrorCodeMsg(ErrorCode.P9001000));
		}
	}

	/**
	 * 创建订单，校验接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	protected PaymentRequest checkAndCreatePaymentRequest(BasicRequestDTO requestDTO) {
		PaymentRequest oldRequest = paymentRequestService.queryBySystemAndOrderNo(requestDTO.getOrderSystem(),
				requestDTO.getOrderNo());
		if (oldRequest != null) {
			// 检查订单信息--关键信息不能变更
			checkOrderInfo(requestDTO, oldRequest);
			checkPayTimes(oldRequest);
			return oldRequest;
		} else {
			PaymentRequest payment = buildPaymentRequest(requestDTO, null);
			try {
				paymentRequestService.createPaymentRequest(payment);
				return payment;
			} catch (DuplicateKeyException e) {
				// 获取刚保存的请求订单
				PaymentRequest justexistsRequest = paymentRequestService
						.queryBySystemAndOrderNo(requestDTO.getOrderSystem(), requestDTO.getOrderNo());
				// 校验信息是否被篡改。
				checkOrderInfo(requestDTO, justexistsRequest);
				return justexistsRequest;
			}
		}
	}

	/**
	 * 重复下单关键项校验
	 * 
	 * @param args
	 * @param payment
	 */
	private void checkOrderInfo(BasicRequestDTO args, PaymentRequest payment) {
		if (payment != null) {
			// 校验状态信息
			if (PaymentStatusEnum.SUCCESS.name().equals(payment.getPayStatus())) {
				throw new PayBizException(ErrorCode.P9002001);
			}
			if (PaymentStatusEnum.REVERSE.name().equals(payment.getPayStatus())) {
				throw new PayBizException(ErrorCode.P9002007);
			}
			// 校验信息是否被篡改
			if (!StringUtils.equals(payment.getCustomerNo(), args.getCustomerNumber())) {
				throw new PayBizException(ErrorCode.P9001003, "customerNumber exception");
			}
			if (!StringUtils.equals(payment.getOutTradeNo(), args.getOutTradeNo())) {
				throw new PayBizException(ErrorCode.P9001003, "outTradeNo exception");
			}
			if (payment.getAmount().compareTo(args.getAmount()) != 0) {
				throw new PayBizException(ErrorCode.P9001003, "amount exception");
			}
			if(StringUtils.isNotBlank(payment.getPayType())) {
				if(!StringUtils.equals(payment.getPayType(), args.getPayOrderType().name())) {
					throw new PayBizException(ErrorCode.P9001003, "payType exception");
				}
			}
		}
	}

	/**
	 * 判断支付次数是否超过限制
	 * 
	 * @param payment
	 */
	private void checkPayTimes(PaymentRequest payment) {
		int times = payRecordService.queryPayTimes(payment.getId());
		if (times >= ConstantUtils.PAY_LIMIT_TIMES) {
			throw new PayBizException(ErrorCode.P9001009);
		}
	}

	protected PaymentRequest buildPaymentRequest(BasicRequestDTO requestDTO, String payType) {
		PaymentRequest payment = new PaymentRequest();
		payment.setOrderSystem(requestDTO.getOrderSystem());
		payment.setOrderSystemStatus(OrderSystemStatusEnum.DOING.name());
		payment.setAmount(requestDTO.getAmount());
		payment.setCustomerNo(requestDTO.getCustomerNumber());
		payment.setCustomerName(requestDTO.getCustomerName());
		payment.setDealUniqueSerialNo(requestDTO.getDealUniqueSerialNo());
		payment.setOrderNo(requestDTO.getOrderNo());
		payment.setPayStatus(PaymentStatusEnum.DOING.name());
		payment.setOutTradeNo(requestDTO.getOutTradeNo());
		if(StringUtils.isNotBlank(payType)) {
			payment.setPayType(payType);
		}
		return payment;

	}

	/**
	 * @param requestDTO
	 * @param payment
	 * @return
	 */
	protected PayRecord buildPayRecord(BasicRequestDTO requestDTO, PaymentRequest payment) {
		PayRecord payRecord = new PayRecord();
		payRecord.setAmount(payment.getAmount());
		payRecord.setRequestId(payment.getId());
		payRecord.setRequestSysId(requestDTO.getRequestSysId());
		payRecord.setRequestSystem(requestDTO.getRequestSystem());

		if (StringUtils.isNotBlank(requestDTO.getUserId()) || StringUtils.isNotBlank(requestDTO.getUserType())) {
			payRecord.setExtendedInfo(
					new ExtendedInfo().setUserId(requestDTO.getUserId()).setUserType(requestDTO.getUserType()));
		}
		if (StringUtils.isNotBlank(requestDTO.getCashierType())) {
			payRecord.setCashierVersion(requestDTO.getCashierType());
		} else if (requestDTO.getCashierVersion() != null) {
			payRecord.setCashierVersion(requestDTO.getCashierVersion().name());
		}
		payRecord.setPayProduct(requestDTO.getPayProduct());
		payRecord.setPayOrderType(requestDTO.getPayOrderType().name());
		payRecord.setPayerIp(requestDTO.getPayerIp());
		if (requestDTO.getUserFee() != null) {
			payRecord.setUserFee(requestDTO.getUserFee());
		} else {
			payRecord.setUserFee(new BigDecimal("0"));// 默认手续费为0
		}
		payRecord.setStatus(TrxStatusEnum.DOING.name());

		// 组合支付，需要添加标志
		if(requestDTO.getCombRequestDTO() != null && StringUtils.isNotBlank(requestDTO.getCombRequestDTO().getPayOrderType())) {
			if(CombPayOrderType.MKTG.name().equals(requestDTO.getCombRequestDTO().getPayOrderType())) {
				payRecord.setCombinedPay(true);
			}
		}
		if (requestDTO instanceof OpenPayRequestDTO) {
			OpenPayRequestDTO payRequest = (OpenPayRequestDTO) requestDTO;
			payRecord.setPlatformType(payRequest.getPlatformType().name());
			payRecord.setPayScene(payRequest.getPayScene());
			payRecord.setOpenId(payRequest.getOpenId());
			payRecord.setRetailProductCode(payRequest.getRetailProductCode());
			payRecord.setBasicProductCode(payRequest.getBasicProductCode());

			ExtendedInfo extendedInfo = new ExtendedInfo();
			if(StringUtils.isNotBlank(payRequest.getWalletLevel())){
				extendedInfo.setWalletLevel(payRequest.getWalletLevel());
			}
			if(MapUtils.isNotEmpty(payRequest.getExtParam())) {
				extendedInfo.setExtParam(payRequest.getExtParam());
			}
			payRecord.setExtendedInfo(extendedInfo);
		} else if (requestDTO instanceof PassiveScanPayRequestDTO) {
			PassiveScanPayRequestDTO payRequest = (PassiveScanPayRequestDTO) requestDTO;
			payRecord.setPlatformType(payRequest.getPlatformType().name());
			payRecord.setRetailProductCode(payRequest.getRetailProductCode());
			payRecord.setBasicProductCode(payRequest.getBasicProductCode());
			ExtendedInfo extendedInfo = new ExtendedInfo();
			if(MapUtils.isNotEmpty(payRequest.getExtParam())) {
				extendedInfo.setExtParam(payRequest.getExtParam());
			}
			payRecord.setExtendedInfo(extendedInfo);
		}else if (requestDTO instanceof NetPayRequestDTO) {
			NetPayRequestDTO payRequestDTO = (NetPayRequestDTO) requestDTO;
			payRecord.setBankId(payRequestDTO.getBankId());
			payRecord.setPlatformType(PlatformTypeConstants.NET);
			payRecord.setBankAccountType(payRequestDTO.getBankAccountType().name());
			payRecord.setPayScene(payRequestDTO.getPayScene());
			payRecord.setRetailProductCode(payRequestDTO.getRetailProductCode());
			payRecord.setBasicProductCode(payRequestDTO.getBasicProductCode());

		}else if (requestDTO instanceof NcPayOrderRequestDTO) {
			NcPayOrderRequestDTO ncPayRequest = (NcPayOrderRequestDTO) requestDTO;
			if (ncPayRequest.getCardInfoType() == CardInfoTypeEnum.BIND) {
				payRecord.setBindCardInfoId(String.valueOf(ncPayRequest.getCardInfoId()));
			}
			payRecord.setPlatformType(PlatformTypeConstants.NCPAY);
			payRecord.setPayScene(ncPayRequest.getPayScene());
			payRecord.setRetailProductCode(ncPayRequest.getRetailProductCode());
			if(StringUtils.isBlank(ncPayRequest.getBasicProductCode())) {
				//pp保证传给ncpay的是新的基础产品码
				payRecord.setBasicProductCode(ConstantUtils.getDefaultNcpayBasicProductCode());
			} else {
				payRecord.setBasicProductCode(ncPayRequest.getBasicProductCode());
			}
			// 将bizType存入扩展参数中
			if(PayOrderType.PREAUTH_RE == ncPayRequest.getPayOrderType()) {
				if(payRecord.getExtendedInfo() == null) {
					payRecord.setExtendedInfo(new ExtendedInfo());
				}
				Map<String, String> extParam = new HashMap<String, String>();
				String bizType = ncPayRequest.getBizType().toString();
				extParam.put("bizType", bizType);
				payRecord.getExtendedInfo().setExtParam(extParam);
			}
		}else if (requestDTO instanceof CflPayRequestDTO) {
			CflPayRequestDTO cflPayRequest = (CflPayRequestDTO) requestDTO;
			payRecord.setPlatformType(PlatformTypeConstants.CFL);
			payRecord.setRetailProductCode(cflPayRequest.getRetailProductCode());
			payRecord.setBasicProductCode(cflPayRequest.getBasicProductCode());
		}else if (requestDTO instanceof AccountPayRequestDTO) {
		    AccountPayRequestDTO accountPayRequestDTO = (AccountPayRequestDTO) requestDTO;
		    payRecord.setPlatformType(PlatformTypeConstants.ACCOUNT_PAY);
		    payRecord.setRetailProductCode(accountPayRequestDTO.getRetailProductCode());
		    payRecord.setBasicProductCode(accountPayRequestDTO.getBasicProductCode());
		    payRecord.setDebitAccountNo(accountPayRequestDTO.getDebitAccountNo());
		    payRecord.setDebitCustomerNo(accountPayRequestDTO.getDebitCustomerNo());
		    payRecord.setDebitUserName(accountPayRequestDTO.getDebitUserName());
			payRecord.setRetailProductCode(accountPayRequestDTO.getRetailProductCode());
			payRecord.setBasicProductCode(accountPayRequestDTO.getBasicProductCode());
		    if(payRecord.getExtendedInfo() == null) {
		        payRecord.setExtendedInfo(new ExtendedInfo());
		    }
		    payRecord.getExtendedInfo().setAccountPayExtInfo(accountPayRequestDTO.getExtInfo());
		}else if(requestDTO instanceof NcPayCflOrderRequestDTO) {
			// 银行卡分期 下单
			NcPayCflOrderRequestDTO ncPayCflOrderRequestDTO = (NcPayCflOrderRequestDTO) requestDTO;
			payRecord.setPlatformType(PlatformTypeConstants.NCPAY);
			payRecord.setCflCount(ncPayCflOrderRequestDTO.getCflCount());
			payRecord.setCflRate(ncPayCflOrderRequestDTO.getCflRate());
			payRecord.setMerchantFeeSubsidy(ncPayCflOrderRequestDTO.getMerchantFeeSubsidy());
			payRecord.setMerchantAmountSubsidy(ncPayCflOrderRequestDTO.getMerchantAmountSubsidy());
			payRecord.setRetailProductCode(ncPayCflOrderRequestDTO.getRetailProductCode());
			payRecord.setBasicProductCode(ncPayCflOrderRequestDTO.getBasicProductCode());
			payRecord.setCflCount(ncPayCflOrderRequestDTO.getCflCount());
			payRecord.setCflRate(ncPayCflOrderRequestDTO.getCflRate());
			payRecord.setMerchantFeeSubsidy(ncPayCflOrderRequestDTO.getMerchantFeeSubsidy());
			payRecord.setMerchantAmountSubsidy(ncPayCflOrderRequestDTO.getMerchantAmountSubsidy());

		}else if(requestDTO instanceof  NcPayCflOpenRequestDTO) {
			// 银行卡分期开通并支付
			NcPayCflOpenRequestDTO ncPayCflOpenRequestDTO = (NcPayCflOpenRequestDTO) requestDTO;
			payRecord.setPlatformType(PlatformTypeConstants.NCPAY);
			payRecord.setCflCount(ncPayCflOpenRequestDTO.getCflCount());
			payRecord.setCflRate(ncPayCflOpenRequestDTO.getCflRate());
			payRecord.setMerchantFeeSubsidy(ncPayCflOpenRequestDTO.getMerchantFeeSubsidy());
			payRecord.setMerchantAmountSubsidy(ncPayCflOpenRequestDTO.getMerchantAmountSubsidy());
			payRecord.setPageCallBack(ncPayCflOpenRequestDTO.getPageCallBack());
			payRecord.setRetailProductCode(ncPayCflOpenRequestDTO.getRetailProductCode());
			payRecord.setBasicProductCode(ncPayCflOpenRequestDTO.getBasicProductCode());
			payRecord.setCflCount(ncPayCflOpenRequestDTO.getCflCount());
			payRecord.setCflRate(ncPayCflOpenRequestDTO.getCflRate());
			payRecord.setMerchantFeeSubsidy(ncPayCflOpenRequestDTO.getMerchantFeeSubsidy());
			payRecord.setMerchantAmountSubsidy(ncPayCflOpenRequestDTO.getMerchantAmountSubsidy());
		}else if (requestDTO instanceof PersonalMemberSyncPayRequestDTO) {
			PersonalMemberSyncPayRequestDTO memberBalancePayRequestDTO = (PersonalMemberSyncPayRequestDTO) requestDTO;
		    payRecord.setPlatformType(PlatformTypeConstants.MEMBER_PAY);//数据库字段20长度
		    payRecord.setRetailProductCode(memberBalancePayRequestDTO.getRetailProductCode());
		    payRecord.setBasicProductCode(memberBalancePayRequestDTO.getBasicProductCode());
		    
		    /**DEBIT_ACCOUNT_NO 用来保存memberNo
		     */
		    payRecord.setDebitAccountNo(memberBalancePayRequestDTO.getMemberNo());
		    if(payRecord.getExtendedInfo() == null) {
		        payRecord.setExtendedInfo(new ExtendedInfo());
		    }
		    if(!CheckUtils.isEmpty(memberBalancePayRequestDTO.getExtParam())) {
		    	 payRecord.getExtendedInfo().setExtParam(memberBalancePayRequestDTO.getExtParam());
		    }
		}else if (requestDTO instanceof NcGuaranteeCflPayRequestDTO) {
			// 担保分期
            NcGuaranteeCflPayRequestDTO request = (NcGuaranteeCflPayRequestDTO)requestDTO;
            payRecord.setBankAccountType(request.getAccountType());
            payRecord.setBankId(request.getBankCode());
            payRecord.setBasicProductCode(request.getBasicProductCode());
            payRecord.setRetailProductCode(request.getRetailProductCode());
            payRecord.setCflCount(request.getCflCount());
            payRecord.setMerchantFeeSubsidy(request.getMerchantFeeSubsidy());
            payRecord.setPlatformType(PlatformTypeConstants.NCPAY);
            payRecord.setPayScene(request.getSceneType());
        }else if(requestDTO instanceof NcCflEasyRequestDTO) {
			// 分期易
			NcCflEasyRequestDTO ncPayRequest = (NcCflEasyRequestDTO) requestDTO;
			if (ncPayRequest.getCardInfoType() == CardInfoTypeEnum.BIND) {
				payRecord.setBindCardInfoId(String.valueOf(ncPayRequest.getCardInfoId()));
			}
			payRecord.setPlatformType(PlatformTypeConstants.NCPAY);
			payRecord.setPayScene(ncPayRequest.getPayScene());
			payRecord.setRetailProductCode(ncPayRequest.getRetailProductCode());
			if(StringUtils.isBlank(ncPayRequest.getBasicProductCode())) {
				//pp保证传给ncpay的是新的基础产品码
				payRecord.setBasicProductCode(ConstantUtils.getDefaultNcpayBasicProductCode());
			} else {
				payRecord.setBasicProductCode(ncPayRequest.getBasicProductCode());
			}
			payRecord.setCflCount(ncPayRequest.getCflCount());
		}


		return payRecord;
	}

	protected void createPayRecord(PayRecord record) {
		if (record.getRecordNo() == null) {
			record.setRecordNo(generateRecordNo(record.getPayOrderType()));
			payRecordService.createPayRecord(record);
		}
	}

	protected String generateRecordNo(String orderType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
		String dateStr = sdf.format(new Date());
		// 类型+日期+4位随机数
		String orderId = orderType + dateStr + CommonUtils.makeOrderNumber(4);
		return orderId;
	}

	/**
	 * 组装查单返回参数
	 * 
	 * @param payment
	 * @param record
	 * @param responseDTO
	 */
	protected void buildQueryResponseDTO(PaymentRequest payment, PayRecord record, QueryResponseDTO responseDTO) {
		responseDTO.setUniqueOrderNo(payment.getDealUniqueSerialNo());
		responseDTO.setPayAmount(payment.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
		responseDTO.setPaymentStatus(PaymentStatusEnum.getPayStatus(payment.getPayStatus()));
		responseDTO.setPaymentSysNo(payment.getRecordNo());
		responseDTO.setPaySuccDate(payment.getConfirmTime());
		responseDTO.setCustomerNumber(payment.getCustomerNo());
		responseDTO.setOutTradeNo(payment.getOutTradeNo());
		responseDTO.setOrderNo(payment.getOrderNo());
		responseDTO.setOrderSystem(payment.getOrderSystem());
		responseDTO.setPayOrderType(PayOrderType.getOrderType(payment.getPayType()));
		if (record != null) {
			responseDTO.setBankId(record.getBankId());
			responseDTO.setCost(record.getCost());
			responseDTO.setPayAmount(payment.getAmount().add(record.getUserFee()).setScale(2,BigDecimal.ROUND_HALF_UP));
			responseDTO.setUserFee(record.getUserFee());
			responseDTO.setBankPaySuccDate(record.getPayTime());
			responseDTO.setCashierType(record.getCashierVersion());
			responseDTO.setPayProduct(record.getPayProduct());
			responseDTO.setBankOrderId(record.getBankOrderNo());
			responseDTO.setBankTrxId(record.getBankTrxId());
			responseDTO.setChannelId(record.getFrpCode());
			responseDTO.setPaymentIp(record.getPayerIp());
			responseDTO.setUserFee(record.getUserFee().setScale(2,BigDecimal.ROUND_HALF_UP));
			responseDTO.setCardType(PayCardType.getPayCardType(record.getCardType()));
			responseDTO.setPlatformType(record.getPlatformType());
			if (StringUtils.isNotBlank(record.getCardId())) {
				BankCardDetailDTO detailDTO = bankCardService.getBankCardDetail(record.getCardId());
				if (detailDTO != null && detailDTO.getBaseInfo() != null) {
					String mobile = detailDTO.getBaseInfo().getBankMobile();
					if (StringUtils.isBlank(mobile)) {
						mobile = detailDTO.getBaseInfo().getYbMobile();
					}
					responseDTO.setIdCardNo(detailDTO.getBaseInfo().getIdcard());
					responseDTO.setMobilePhoneNo(mobile);
					responseDTO.setCardName(detailDTO.getBaseInfo().getOwner());
					responseDTO.setBankCardNo(detailDTO.getBaseInfo().getCardNo());
					responseDTO.setBankName(detailDTO.getBaseInfo().getBankName());
				}
			}
			responseDTO.setPayOrderType(PayOrderType.getOrderType(record.getPayOrderType()));
			responseDTO.setNetPayType(record.getBankAccountType());
			responseDTO.setLoanCompany(record.getLoanCompany());
			responseDTO.setLoanTerm(record.getLoanTerm());
			if(record.getExtendedInfo()!=null){
				responseDTO.setUserId(record.getExtendedInfo().getUserId());
				responseDTO.setUserType(record.getExtendedInfo().getUserType());
				responseDTO.setExtendMsg(ConstantUtils.transferExtendMsg(record.getExtendedInfo()));
				responseDTO.setWalletLevel(record.getExtendedInfo().getWalletLevel());
			}	
			
			responseDTO.setDebitAccountNo(record.getDebitAccountNo());
			responseDTO.setDebitCustomerNo(record.getDebitCustomerNo());
			responseDTO.setRetailProductCode(record.getRetailProductCode());
			responseDTO.setBasicProductCode(record.getBasicProductCode());
			responseDTO.setOpenId(record.getOpenId());
			// 银行卡分期订单
			if(PayOrderType.BK_CFL.name().equals(record.getPayOrderType())) {
				responseDTO.setCflCount(record.getCflCount());
				responseDTO.setCflRate(record.getCflRate());
				responseDTO.setMerchantFeeSubsidy(record.getMerchantFeeSubsidy());
				responseDTO.setMerchantAmountSubsidy(record.getMerchantAmountSubsidy());
			}
			// 预授权
			if(StringUtils.isNotBlank(payment.getPayType())) {
				responseDTO.setPayType(payment.getPayType());
				// 循环预授权需要返回错误码和错误信息
				responseDTO.setResponseCode(record.getErrorCode());
				responseDTO.setResponseMsg(record.getErrorMsg());
			}
			// 组合支付
			if(record.isCombinedPay()) {
				CombResponseDTO combResponseDTO = combAbstractService.bulidCombResponse(record.getRecordNo());
				responseDTO.setCombResponseDTO(combResponseDTO);
				responseDTO.setFirstPayAmount(record.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
			}


		}
	}

	protected void buildPayRecordResponse(PayRecord record, PaymentRequest payment, PayRecordResponseDTO responseDTO) {
		responseDTO.setOrderSystemStatus(OrderSystemStatusEnum.getStatus(payment.getOrderSystemStatus()));
		responseDTO.setTrxStatus(TrxStatusEnum.getTrxStatus(record.getStatus()));
		responseDTO.setFrpCode(record.getFrpCode());
		responseDTO.setBankOrderNO(record.getBankOrderNo());
		responseDTO.setCost(record.getCost());
		responseDTO.setCardId(record.getCardId());
		responseDTO.setBankTrxId(record.getBankTrxId());	
		if (StringUtils.isNotBlank(record.getSmsState())) {
			responseDTO.setSmsState(SmsCheckResultEnum.valueOf(record.getSmsState()));
		}
		// 查询支付状态，订单失败了，设置错误码和错误描述
		if (!(record.getStatus().equals(TrxStatusEnum.SUCCESS.name())
				|| record.getStatus().equals(TrxStatusEnum.REVERSE.name()))) {
			responseDTO.setResponseCode(record.getErrorCode());
			responseDTO.setResponseMsg(record.getErrorMsg());
		}
		// 银行卡分期订单
		if(PayOrderType.BK_CFL.name().equals(record.getPayOrderType())) {
			responseDTO.setCflCount(record.getCflCount());
			responseDTO.setCflRate(record.getCflRate());
			responseDTO.setMerchantFeeSubsidy(record.getMerchantFeeSubsidy());
			responseDTO.setMerchantAmountSubsidy(record.getMerchantAmountSubsidy());
		}
		responseDTO.setCustomerNo(payment.getCustomerNo());
		responseDTO.setOutTradeNo(payment.getOutTradeNo());
		responseDTO.setRecordNo(record.getRecordNo());
		responseDTO.setPayOrderType(record.getPayOrderType());
		// 组合支付
		if(record.isCombinedPay()) {
			CombResponseDTO combResponseDTO = combAbstractService.bulidCombResponse(record.getRecordNo());
			responseDTO.setCombResponseDTO(combResponseDTO);
			responseDTO.setFirstPayAmount(record.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
	}

	/**
	 * 基础返回参数设置
	 * 
	 * @param requestDTO
	 * @param responseDTO
	 */
	protected void setBasicResponseArgs(BasicRequestDTO requestDTO, BasicResponseDTO responseDTO) {
		responseDTO.setCustomerNumber(requestDTO.getCustomerNumber());
		responseDTO.setOrderNo(requestDTO.getOrderNo());
		responseDTO.setPayOrderType(requestDTO.getPayOrderType());
		responseDTO.setDealUniqueSerialNo(requestDTO.getDealUniqueSerialNo());
		responseDTO.setOutTradeNo(requestDTO.getOutTradeNo());
	}

	protected void updateErrorInfoToRecord(ResponseStatusDTO response, String recordNo) {
		if (StringUtils.isNotBlank(recordNo)) {
			try {
				payRecordService.updateRecordErrorInfo(recordNo, response.getResponseCode(), response.getResponseMsg());
			} catch (Exception e) {
				response.setResponseCode("P9001000");
				response.setResponseMsg("系统异常");
			}
		}
	}


	/**
	 * 预授权，创建订单
	 * 使用payType字段
	 */
	protected PaymentRequest checkAndCreatePaymentForPreAuth(BasicRequestDTO requestDTO) {
		PaymentRequest oldRequest = paymentRequestService.queryBySystemAndOrderNo(requestDTO.getOrderSystem(),
				requestDTO.getOrderNo());
		if (oldRequest != null) {
			// 检查订单信息--关键信息不能变更
			checkOrderInfo(requestDTO, oldRequest);
			checkPayTimes(oldRequest);
			
			return oldRequest;
		} else {
			PaymentRequest payment = buildPaymentRequest(requestDTO, PayOrderType.PREAUTH_RE.name());
			try {
				paymentRequestService.createPaymentRequest(payment);
				return payment;
			} catch (DuplicateKeyException e) {
				// 获取刚保存的请求订单
				PaymentRequest justexistsRequest = paymentRequestService
						.queryBySystemAndOrderNo(requestDTO.getOrderSystem(), requestDTO.getOrderNo());
				// 校验信息是否被篡改。
				checkOrderInfo(requestDTO, justexistsRequest);
				return justexistsRequest;
			}
		}
	}


	/**
	 * 创建组合支付的第二单
	 * @param requestDTO
	 * @return
	 */
	protected CombPayRecord buildCombPayRecord(BasicRequestDTO requestDTO, PayRecord payRecord) {
		if(payRecord.isCombinedPay()) {
			CombPayRecord combPayRecord = new CombPayRecord();
			CombRequestDTO combRequestDTO = requestDTO.getCombRequestDTO();

			combPayRecord.setRecordNo(payRecord.getRecordNo());
			combPayRecord.setPayOrderType(combRequestDTO.getPayOrderType());
			combPayRecord.setStatus(CombTrxStatusEnum.DOING.name());
			combPayRecord.setMarketingNo(combRequestDTO.getMarketingNo());
			combPayRecord.setCreateTime(new Date());
			combPayRecord.setPayProduct(ConstantUtils.MKTG_PAY_PRODUCT);
			combPayRecordService.add(combPayRecord);
			return combPayRecord;
		}
		return null;
	}


	/**
	 * 创建第二支付子单
	 * @param requestDTO
	 * @param payRecord
	 * @param paymentRequest
	 */
	protected void createCombPayRecord(BasicRequestDTO requestDTO, PayRecord payRecord, PaymentRequest paymentRequest) {
		// 创建第二支付子单
		CombPayRecord combPayRecord = buildCombPayRecord(requestDTO, payRecord);
		// 调用营销预冻结
		mktgService.deposit(requestDTO, payRecord, combPayRecord, paymentRequest);
	}


	/**
	 * ncpay同步支付结果处理
	 * ncpayBiz和ncpayCflEasyBiz
	 * 即无卡相关和分期易相关都需此方法，故放在baseBiz中
	 */
	protected void handlePayQueryResult(PayQueryResponseDTO payQueryResponseDTO, PayRecord record,
									  PayRecordResponseDTO response) {
		// 返回冲正
		if (payQueryResponseDTO.getStatus() == PaymentOrderStatusEnum.REVERSE) {
			payRecordService.updatePaymentStatus(record.getRecordNo(), TrxStatusEnum.REVERSE.name(),
					Arrays.asList(new String[] { TrxStatusEnum.DOING.name() }));
			// 若组合支付，更新第二单为失败
			combAbstractService.updateFailByRecordNo(record);
			response.setTrxStatus(TrxStatusEnum.REVERSE);
			response.setRecordNo(record.getRecordNo());
			response.setSmsState(payQueryResponseDTO.getSmsState());

		}
		// 返回失败
		if (payQueryResponseDTO.getStatus() == PaymentOrderStatusEnum.FAILURE) {
			String errorCode = null;
			String errormsg = null;
			if (StringUtils.isNotBlank(payQueryResponseDTO.getErrorCode())) {
				ErrorMeta meta = ErrorCodeUtil.translateCode(ErrorCodeSource.NCPAY.getSysCode(),
						payQueryResponseDTO.getErrorCode(), payQueryResponseDTO.getErrorMsg(), ErrorCode.P9001000);
				if (meta != null) {
					errorCode = meta.getErrorCode();
					errormsg = meta.getErrorMsg();
				} else {
					errorCode = "P" + payQueryResponseDTO.getErrorCode();
					errormsg = payQueryResponseDTO.getErrorMsg();
				}
			}
			payRecordService.updateNcPaymentToFail(record.getRecordNo(), errorCode, errormsg);
			// 若组合支付，更新第二单为失败
			combAbstractService.updateFailByRecordNo(record);
			response.setTrxStatus(TrxStatusEnum.FAILUER);
			response.setRecordNo(record.getRecordNo());
			response.setSmsState(payQueryResponseDTO.getSmsState());
			response.setResponseCode(errorCode);
			response.setResponseMsg(errormsg);
		}
		// 返回成功
		if (payQueryResponseDTO.getStatus() == PaymentOrderStatusEnum.SUCCESS) {
			if(record.isCombinedPay()) {
				handleComResult(response, payQueryResponseDTO, record);
			}else {
				handleResult(response, payQueryResponseDTO, record);
			}
		}
	}

	private void handleResult(PayRecordResponseDTO response, PayQueryResponseDTO payQueryResponseDTO,
							  PayRecord payRecord) {
		TrxStatusEnum recordStatus = ncPayResultProccess.processForNcPayQueryResponse(payQueryResponseDTO,
				payRecord);
		response.setTrxStatus(recordStatus);
		response.setBankOrderNO(payQueryResponseDTO.getBankOrderNo());
		response.setBankTrxId(payQueryResponseDTO.getTradeSerialNo());
		response.setCost(payQueryResponseDTO.getCost() == null ? null : payQueryResponseDTO.getCost().getValue());
		response.setFrpCode(payQueryResponseDTO.getFrpCode());
		response.setSmsState(payQueryResponseDTO.getSmsState());
		response.setRecordNo(payRecord.getRecordNo());
	}


	/**
	 * 组合支付的处理方法
	 */
	private void handleComResult(PayRecordResponseDTO response, PayQueryResponseDTO payQueryResponseDTO,
								 PayRecord payRecord) {
		TrxStatusEnum recordStatus = ncPayResultProccess.processForNcPayQueryResponseComb(payQueryResponseDTO,
				payRecord);
		response.setTrxStatus(recordStatus);
		response.setBankOrderNO(payQueryResponseDTO.getBankOrderNo());
		response.setBankTrxId(payQueryResponseDTO.getTradeSerialNo());
		response.setCost(payQueryResponseDTO.getCost() == null ? null : payQueryResponseDTO.getCost().getValue());
		response.setFrpCode(payQueryResponseDTO.getFrpCode());
		response.setSmsState(payQueryResponseDTO.getSmsState());
		response.setRecordNo(payRecord.getRecordNo());
		// 返回前查订单状态
		CombResponseDTO combResponseDTO = combAbstractService.bulidCombResponse(payRecord.getRecordNo());
		response.setCombResponseDTO(combResponseDTO);
		response.setFirstPayAmount(payRecord.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
	}

}
