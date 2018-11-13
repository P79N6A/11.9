package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.dao.PaymentRecordDao;
import com.yeepay.g3.core.nccashier.entity.ParamShowInfo;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserAccount;
import com.yeepay.g3.core.nccashier.gateway.service.FrontEndService;
import com.yeepay.g3.core.nccashier.gateway.service.FrontedInstallmentService;
import com.yeepay.g3.core.nccashier.gateway.service.NcPayService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.CashierThemeService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.UserProceeService;
import com.yeepay.g3.core.nccashier.utils.AESUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.PersonHoldCard;
import com.yeepay.g3.core.nccashier.vo.RecordCondition;
import com.yeepay.g3.facade.frontend.dto.FrontendRefundResponseDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentReverseRequestDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentReverseResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.enumtype.RefundType;
import com.yeepay.g3.facade.frontend.enumtype.ReverseType;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.service.QueryPaymentResultFacade;
import com.yeepay.g3.facade.ncpay.enumtype.ReverseStatusEnum;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.log.utils.StringUtil;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xiewei on 15-10-20.
 */
@Service("paymentProcessService")
public class PaymentProcessServiceImpl extends NcCashierBaseService implements PaymentProcessService {

	private static Logger logger = NcCashierLoggerFactory.getLogger(PaymentProcessServiceImpl.class);
	// 本地缓存时间
	public static final int CACHE_LIMIT = 5 * 60 * 1000;
	public static final String PAYMENTREQUEST_CACHE_PREFIX = "REQUEST_";
	
	@Resource
	private PaymentRecordDao paymentRecordDao;
	@Resource
	private PaymentRequestService paymentRequestService;
	@Resource
	private CashierThemeService cashierThemeService;
	@Resource
	private NcPayService ncPayService;

	@Resource
	private FrontEndService frontEndService;
	
	@Resource
	private UserProceeService userProceeService;
	
	@Resource
	private FrontedInstallmentService frontedInstallmentService;

	@Override
	public void updateRecord(PaymentRecord paymentRecord) {
		try {
			// 加密敏感信息
			aesEncrypt(paymentRecord);
			int effortRow = paymentRecordDao.updateRecordState(paymentRecord);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		} catch (Exception e) {
			logger.error("更新支付订单失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		} finally {
			// 解密敏感信息
			aesDecrypt(paymentRecord);
		}
	}

	@Override
	public long savePaymentRecord(PaymentRecord paymentRecord) {
		long id = 0;
		try {
			aesEncrypt(paymentRecord);
			id = paymentRecordDao.savePaymentRecord(paymentRecord);
		} catch (Exception e) {
			logger.error("保存支付订单失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		} finally {
			aesDecrypt(paymentRecord);
		}
		return id;
	}

	@Override
	public List<PaymentRecord> findRecordList(String tradeSysOrderId, String tradeSysNo) {
		Map map = new HashMap();
		map.put("tradeSysOrderId", tradeSysOrderId);
		map.put("tradeSysNo", tradeSysNo);
		List<PaymentRecord> paymentRecords = paymentRecordDao.findRecordList(map);
		if (null != paymentRecords && paymentRecords.size() > 0) {
			for (PaymentRecord paymentRecord : paymentRecords) {
				aesDecrypt(paymentRecord);
			}
		}
		return paymentRecords;
	}

	@Override
	public List<PaymentRecord> findRecordListByOrderOrderId(String orderOrderId, String orderSysNo) {
		List<PaymentRecord> paymentRecords = paymentRecordDao.findRecordListByOrderOrderId(orderOrderId, orderSysNo);
		if (null != paymentRecords && paymentRecords.size() > 0) {
			for (PaymentRecord paymentRecord : paymentRecords) {
				aesDecrypt(paymentRecord);
			}
		}
		return paymentRecords;
	}

	@Override
	public List<PaymentRecord> findRecordsByMerchantOrderId(String merchantOrderId, String merchantNo) {
		List<PaymentRecord> paymentRecords = paymentRecordDao.findRecordsByMerchantOrderId(merchantOrderId, merchantNo);
		if (null != paymentRecords && paymentRecords.size() > 0) {
			for (PaymentRecord paymentRecord : paymentRecords) {
				aesDecrypt(paymentRecord);
			}
		}
		return paymentRecords;
	}

	@Override
	public PaymentRecord findRecordByMerchantOrderId(String merchantOrderId, String merchantNo, String paymentOrderNo) {
		PaymentRecord paymentRecord = null;
		List<PaymentRecord> recordList = findRecordsByMerchantOrderId(merchantOrderId,merchantNo);
		if (CollectionUtils.isNotEmpty(recordList)) {
			for (PaymentRecord record : recordList) {
				logger.info("数据库record:{},mq通知返回:{},是否相等:{}", record.getPaymentOrderNo(), paymentOrderNo,
						paymentOrderNo.equals(record.getPaymentOrderNo()));
				if (paymentOrderNo.equals(record.getPaymentOrderNo())) {
					paymentRecord = record;
					break;
				}
			}
		}
		if (null == paymentRecord) {
			throw CommonUtil.handleException(Errors.PAY_RECORD_NULL);
		}
		return paymentRecord;
	}

	@Override
	public void updateRecordNo(long orderId, String smstype, String payOrderId, PayRecordStatusEnum status) {
		PaymentRecord paymentRecord = new PaymentRecord();
		paymentRecord.setId(orderId);

		paymentRecord.setSmsVerifyType(smstype);
		paymentRecord.setPaymentOrderNo(payOrderId);
		paymentRecord.setState(status);
		try {
			// 加密敏感信息
			aesEncrypt(paymentRecord);
			int effortRow = paymentRecordDao.updateRecordNo(paymentRecord);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		} catch (Exception e) {
			logger.error("更新支付订单失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		} finally {
			// 解密敏感信息
			aesDecrypt(paymentRecord);
		}

	}

	@Override
	public List<PaymentRecord> findRecordList(String tradeSysOrderId, String tradeSysNo, String payState) {
		Map map = new HashMap();
		map.put("tradeSysOrderId", tradeSysOrderId);
		map.put("tradeSysNo", tradeSysNo);
		map.put("state", payState);
		List<PaymentRecord> paymentRecords = paymentRecordDao.findRecordList(map);
		if (null != paymentRecords && paymentRecords.size() > 0) {
			for (PaymentRecord paymentRecord : paymentRecords) {
				aesDecrypt(paymentRecord);
			}
		}
		return paymentRecords;
	}

	@Override
	public PaymentRecord findRecordByPayOrderNo(String payOrderNo, String paySysNo) {
		Map map = new HashMap();
		map.put("paymentOrderNo", payOrderNo);
		map.put("paymentSysNo", paySysNo);
		PaymentRecord paymentRecord = paymentRecordDao.findRecordByPayOrderNo(map);
		// 解密敏感信息
		aesDecrypt(paymentRecord);
		return paymentRecord;
	}

	@Override
	public PaymentRecord findRecordByPaymentRecordId(String paymentRecordId) {
		Map map = new HashMap();
		map.put("paymentRecordId", paymentRecordId);
		PaymentRecord paymentRecord = paymentRecordDao.findRecordByPaymentRecordId(map);
		// 解密敏感信息
		aesDecrypt(paymentRecord);
		return paymentRecord;
	}

	@Override
	public void verifyPayRequest(PaymentRequest paymentRequest) throws CashierBusinessException {
		if (paymentRequest == null) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
		if (paymentRequestService.isRequestExpired(paymentRequest)) {
			throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
		}
		if (PayRequestStatusEnum.SUCCESS.getValue().equals(paymentRequest.getState())
				|| PayRequestStatusEnum.FAILED.getValue().equals(paymentRequest.getState())) {
			throw CommonUtil.handleException(Errors.THRANS_FINISHED);
		}
	}

	

	public ParamShowInfo makeRequestInfoDTO(PaymentRequest payRequest) {
		ParamShowInfo rInfo = new ParamShowInfo();
		rInfo.setCompanyname(payRequest.getMerchantName());
		rInfo.setProductname(payRequest.getProductName());
		rInfo.setOrderid(payRequest.getMerchantOrderId());
		rInfo.setPaySysCode(payRequest.getPaymentSysNo());
		rInfo.setParentMerchantNo(payRequest.getParentMerchantNo());
//		rInfo.setAmount(payRequest.getOrderAmount().doubleValue());
		if(payRequest.getOrderAmount()!=null){
			rInfo.setAmount(payRequest.getOrderAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		if(payRequest.getFee()!=null && FeeTypeEnum.USER_FEE.name().equals(payRequest.getFeeType())){
			rInfo.setFee(payRequest.getFee().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		rInfo.setCreateTime(payRequest.getOrderTimeNotNull());
		rInfo.setIndustryCatalog(payRequest.getIndustryCatalog());
		JSONObject extendInfoJson = CommonUtil.parseJson(payRequest.getExtendInfo());
		//非银行卡支付增加以下四个参数
		rInfo.setGoodsDesc(extendInfoJson.getString("goodsDesc"));
		rInfo.setGoodsKind(extendInfoJson.getString("goodsKind"));
		rInfo.setGoodsExt(extendInfoJson.getString("goodsExt"));
		rInfo.setCallBackUrl(extendInfoJson.getString("callBackUrl"));
		ThemeSettingDTO themeDTO = new ThemeSettingDTO();
		themeDTO.setThemeCode(payRequest.getThemeCustomCode());
		ThemeResult theme = null;
		try {
			theme = cashierThemeService.themeSet(themeDTO);
		} catch (Exception e) {
			logger.error(payRequest.getId() + "", e);
		}
		rInfo.setTheme(theme);
		return rInfo;
	}

	@Override
	public TradeNoticeDTO queryTradeResult(String queryTradeResultUrl, String tradeSysOrderId, String tradeSysNo) {
		TradeNoticeDTO tradeNoticeDTO = null;
		QueryPaymentResultFacade queryPaymentResultFacade = null;
		try {
			queryPaymentResultFacade = RemoteServiceFactory.getService(queryTradeResultUrl, RemotingProtocol.HESSIAN,
					QueryPaymentResultFacade.class);
		} catch (Exception e) {
			logger.info("获取QueryPaymentResultFacade接口服务异常:{}", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (null != queryPaymentResultFacade) {
			try {
				tradeNoticeDTO = queryPaymentResultFacade.queryPayResult(tradeSysOrderId, tradeSysNo);
			} catch (Exception e) {
				logger.info("调用查询交易结果接口QueryPaymentResultFacade.queryPayResult异常:{}", e);
			}
		}
		return tradeNoticeDTO;
	}

	@Override
	public void reversePayOrder(ReverseRequestDTO reverseRequestDTO, ReverseResponseDTO reverseResponseDTO) {
		// 根据业务方订单号&业务方编码获取对应的支付请求记录
		PaymentRequest payRequest = paymentRequestService.findPayRequestByOrderOrderId(
				reverseRequestDTO.getBizOrderNum(), String.valueOf(reverseRequestDTO.getBizType()));
		if (null == payRequest) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}

		if (!PayRequestStatusEnum.REVERSE.name().equals(payRequest.getState())) {
			if (!PayRequestStatusEnum.SUCCESS.name().equals(payRequest.getState())) {
				logger.warn("只有成功状态的交易才能发起冲正,交易订单号:{}", reverseRequestDTO.getBizOrderNum());
				throw CommonUtil.handleException(Errors.CANT_REVERSE);
			}
		}

		// 如果冲正接口类型是NCPAY或者是空时候调用ncpay冲正接口，否则调用FE的冲正接口
		if ("NCPAY".equals(reverseRequestDTO.getReverseInterfaceType())
				|| StringUtils.isBlank(reverseRequestDTO.getReverseInterfaceType())) {
			com.yeepay.g3.facade.ncpay.dto.ReverseRequestDTO ncpayReverseRequestDTO = new com.yeepay.g3.facade.ncpay.dto.ReverseRequestDTO();
			try {
				BeanUtils.copyProperties(reverseRequestDTO, ncpayReverseRequestDTO);
			} catch (Exception e) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
			com.yeepay.g3.facade.ncpay.dto.ReverseResponseDTO ncpayReverseResponseDTO = ncPayService
					.reversePayOrder(ncpayReverseRequestDTO);
			makeReverseResponseDTO(ncpayReverseResponseDTO, reverseResponseDTO);
			if (ncpayReverseResponseDTO.getReverseStatus() == ReverseStatusEnum.SUCCESS) {
				payRequest.setState(PayRequestStatusEnum.REVERSE.name());
				payRequest.setUpdateTime(new Date());
				paymentRequestService.updatePayRequestState(payRequest);
			}
		} else{
			String errorCode = null;
			// 微信和支付宝的冲正
			if ("FE".equals(reverseRequestDTO.getReverseInterfaceType())) {
				PlatformType platformType = PlatformType.getPlatformType(reverseRequestDTO.getPlatformType());
				FrontendRefundResponseDTO response = frontEndService.callRefund(payRequest.getMerchantNo(),
						RefundType.TIMEOUT, payRequest.getTradeSysOrderId(), platformType);
				reverseResponseDTO.setBizOrderNum(reverseRequestDTO.getBizOrderNum());
				reverseResponseDTO.setErrorCode(response.getResponseCode());
				reverseResponseDTO.setErrorMsg(response.getResponseMsg());
				errorCode = response.getResponseCode();
			}
			// 分期支付的退款
			else if ("FE_CFL".equals(reverseRequestDTO.getReverseInterfaceType())) {
				InstallmentReverseRequestDTO request = buildInstallmentReverseRequestDTO(reverseRequestDTO, payRequest);
				InstallmentReverseResponseDTO response = frontedInstallmentService.callInstallmentRefund(request);
				reverseResponseDTO.setBizOrderNum(response.getRequestId());
				reverseResponseDTO.setErrorCode(response.getResponseCode());
				reverseResponseDTO.setErrorMsg(response.getResponseMsg());
				errorCode = response.getResponseCode();
			}
			
			reverseResponseDTO.setBizReverseNum(reverseRequestDTO.getBizReverseNum());
			reverseResponseDTO.setBizType(reverseRequestDTO.getBizType());
			reverseResponseDTO.setReverseCompleteDate(new Date());
			if (StringUtils.isEmpty(errorCode)) {
				reverseResponseDTO.setReverseStatus(com.yeepay.g3.facade.nccashier.enumtype.ReverseStatusEnum.SUCCESS);
				payRequest.setState(PayRequestStatusEnum.REVERSE.name());
				payRequest.setUpdateTime(new Date());
				paymentRequestService.updatePayRequestState(payRequest);
			} else {
				reverseResponseDTO.setReverseStatus(com.yeepay.g3.facade.nccashier.enumtype.ReverseStatusEnum.FAILURE);
			}
		}
	}
	
	/**
	 * 创建调用FE的分期子系统的冲正接口的入参
	 * 
	 * @param reverseRequestDTO
	 * @param payRequest
	 * @return
	 */
	private InstallmentReverseRequestDTO buildInstallmentReverseRequestDTO(ReverseRequestDTO reverseRequestDTO,
			PaymentRequest payRequest) {
		InstallmentReverseRequestDTO request = new InstallmentReverseRequestDTO();
		request.setCustomerNumber(reverseRequestDTO.getMerchantNo());
		request.setRequestId(String.valueOf(payRequest.getId()));
		request.setRequestSystem(PayProductCode.NCCASHIER);
		request.setReverseType(ReverseType.TIMEOUT);
		return request;
	}

	private ReverseResponseDTO makeReverseResponseDTO(
			com.yeepay.g3.facade.ncpay.dto.ReverseResponseDTO ncpayReverseResponseDTO,
			ReverseResponseDTO reverseResponseDTO) {
		reverseResponseDTO.setBizOrderNum(ncpayReverseResponseDTO.getBizOrderNum());
		reverseResponseDTO.setBizReverseNum(ncpayReverseResponseDTO.getBizReverseNum());
		reverseResponseDTO.setBizType(ncpayReverseResponseDTO.getBizType());
		reverseResponseDTO.setErrorCode(ncpayReverseResponseDTO.getErrorCode());
		reverseResponseDTO.setErrorMsg(ncpayReverseResponseDTO.getErrorMsg());
		reverseResponseDTO.setReverseCompleteDate(ncpayReverseResponseDTO.getReverseCompleteDate());
		reverseResponseDTO.setReverseStatus(com.yeepay.g3.facade.nccashier.enumtype.ReverseStatusEnum
				.valueOf(ncpayReverseResponseDTO.getReverseStatus().toString()));
		return reverseResponseDTO;
	}

	@Override
	@Transactional
	public boolean updateStateByNcPayCallBack(PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		// 支付成功的更新状态
		boolean isneedNotice = false;
		if (paymentRecord.getState() == PayRecordStatusEnum.SUCCESS) {
			updateRecord(paymentRecord);
			if (!paymentRequest.getState().equals(PayRequestStatusEnum.SUCCESS.getValue())) {
				paymentRequest.setState(PayRequestStatusEnum.SUCCESS.getValue());
				paymentRequest.setPaymentOrderNo(paymentRecord.getPaymentOrderNo());
				paymentRequestService.updateRequestBaseOnStatus(paymentRequest, Arrays.asList("INIT"));
				isneedNotice = true;
			} else if (PayRequestStatusEnum.SUCCESS.getValue().equals(paymentRequest.getState())
					&& !paymentRequest.getPaymentOrderNo().equals(paymentRecord.getPaymentOrderNo())) {
				// 重复退款逻辑
				isneedNotice = false;
				if (PayTypeEnum.WECHAT_ATIVE_SCAN.name().equals(paymentRecord.getPayType())
						|| PayTypeEnum.WECHAT_OPENID.name().equals(paymentRecord.getPayType())
						|| PayTypeEnum.WECHAT_H5_WAP.name().equals(paymentRecord.getPayType())
						|| PayTypeEnum.WECHAT_H5_LOW.name().equals(paymentRecord.getPayType())) {

					FrontendRefundResponseDTO response = frontEndService.callRefund(paymentRecord.getMerchantNo(),
							RefundType.REPEATPAY, paymentRecord.getTradeSysOrderId(), PlatformType.WECHAT);
					if (StringUtil.isEmpty(response.getResponseCode())) {
						updateRecordStateBaseOnOriginalStatus(paymentRecord.getId(), PayRecordStatusEnum.PAYREVERSE,
								Arrays.asList(new PayRecordStatusEnum[] { PayRecordStatusEnum.SUCCESS }));
					}
				} else if (PayTypeEnum.ALIPAY.name().equals(paymentRecord.getPayType()) 
						|| PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(paymentRecord.getPayType()) ) {
					FrontendRefundResponseDTO response = frontEndService.callRefund(paymentRecord.getMerchantNo(),
							RefundType.REPEATPAY, paymentRecord.getTradeSysOrderId(), PlatformType.ALIPAY);
					if (StringUtil.isEmpty(response.getResponseCode())) {
						updateRecordStateBaseOnOriginalStatus(paymentRecord.getId(), PayRecordStatusEnum.PAYREVERSE,
								Arrays.asList(new PayRecordStatusEnum[] { PayRecordStatusEnum.SUCCESS }));
					}
				} else if (PayTypeEnum.CFL.name().equals(paymentRecord.getPayType())) {
					InstallmentReverseRequestDTO InstallmentReverseRequestDTO = new InstallmentReverseRequestDTO();
					InstallmentReverseRequestDTO.setCustomerNumber(paymentRecord.getMerchantNo());
					InstallmentReverseRequestDTO.setRequestId(paymentRecord.getPaymentRequestId()+"");
					InstallmentReverseRequestDTO.setRequestSystem(SysCodeEnum.NCCASHIER.name());
					InstallmentReverseRequestDTO.setReverseType(ReverseType.REPEATPAY);
					
					InstallmentReverseResponseDTO response =frontedInstallmentService.callInstallmentRefund(InstallmentReverseRequestDTO);
					
					if(StringUtil.isEmpty(response.getResponseCode())){
						updateRecordStateBaseOnOriginalStatus(paymentRecord.getId(), PayRecordStatusEnum.PAYREVERSE,
								Arrays.asList(new PayRecordStatusEnum[] { PayRecordStatusEnum.SUCCESS }));
					}
					
				}else {
					com.yeepay.g3.facade.ncpay.dto.ReverseRequestDTO reverseRequestDTO = new com.yeepay.g3.facade.ncpay.dto.ReverseRequestDTO();
					reverseRequestDTO.setBizOrderNum(paymentRecord.getOrderOrderId());
					reverseRequestDTO.setBizReverseNum(UUID.randomUUID().toString());
					reverseRequestDTO.setBizType(Long.parseLong(paymentRecord.getOrderSysNo()));
					reverseRequestDTO.setMerchantNo(paymentRecord.getMerchantNo());
					reverseRequestDTO.setRemark("重复退款");

					com.yeepay.g3.facade.ncpay.dto.ReverseResponseDTO ncpayReverseResponseDTO = ncPayService
							.reversePayOrder(reverseRequestDTO);
					if (ncpayReverseResponseDTO.getReverseStatus() == ReverseStatusEnum.SUCCESS) {
						updateRecordStateBaseOnOriginalStatus(paymentRecord.getId(), PayRecordStatusEnum.PAYREVERSE,
								Arrays.asList(new PayRecordStatusEnum[] { PayRecordStatusEnum.SUCCESS }));
					}
				}
			} else {
				isneedNotice = true;
			}
		} else if (paymentRecord.getState() == PayRecordStatusEnum.FAILED) {
			updateRecord(paymentRecord);
			// 支付请求订单过了有效期处理,如果有存在支付中的支付订单，请求订单不置为失败
			if (paymentRequestService.isRequestExpired(paymentRequest)) {
				logger.info("支付订单过了有效期处理");
				List<PaymentRecord> recordList = findRecordList(paymentRequest.getTradeSysOrderId(),
						paymentRequest.getTradeSysNo());
				if (CollectionUtils.isNotEmpty(recordList)) {
					int failedCount = 0;
					int orderedCount = 0;
					for (PaymentRecord record : recordList) {
						if (PayRecordStatusEnum.FAILED == record.getState()) {
							failedCount++;
						} else if (PayRecordStatusEnum.ORDERED == record.getState()) {
							orderedCount++;
						}
					}
					// 如果有支付中的支付请求表状态不变
					if (failedCount == recordList.size() || (orderedCount > 0 && failedCount > 0
							&& orderedCount + failedCount == recordList.size())) {
						paymentRequest.setState(PayRequestStatusEnum.FAILED.getValue());
						paymentRequest.setPaymentOrderNo(paymentRecord.getPaymentOrderNo());
						paymentRequestService.updateRequestBaseOnStatus(paymentRequest, Arrays.asList("INIT"));
						// 失败过有效期需要发送给业务方消息
						isneedNotice = true;
					}
				}
			} else if (paymentRecord.getState() == PayRecordStatusEnum.PAYREVERSE) {
				logger.info("ncpay为差错退款处理");
				updateRecord(paymentRecord);
			}
		}
		return isneedNotice;
	}

	@Override
	public void updateRecordStateBaseOnOriginalStatus(Long id, PayRecordStatusEnum toState,
			List<PayRecordStatusEnum> statusList) {
		try {
			int effortRow = paymentRecordDao.updateRecordStateBaseOnOriginalStatus(id, toState, statusList);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		} catch (Exception e) {
			logger.error("更新支付订单状态失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}
	
	@Override
	public void updateRecordStatusBaseOnOriginalStatus(Long paymentRecordId, PayRecordStatusEnum objStatus,
			PayRecordStatusEnum preStatus) {
		try {
			logger.info("短信校验失败，开始恢复支付订单为SMS_SEND状态");
			updateRecordStateBaseOnOriginalStatus(paymentRecordId, objStatus,
					Arrays.asList(new PayRecordStatusEnum[] { preStatus }));
		} catch (CashierBusinessException e) {
			logger.error("更新支付订单为SMS_SEND状态失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}

	private void aesEncrypt(PaymentRecord paymentRecord) {
		if (null != paymentRecord) {
			if (StringUtils.isNotBlank(paymentRecord.getCardNo())) {
				paymentRecord.setCardNo(AESUtil.aesEncrypt(paymentRecord.getCardNo()));
			}
			if (StringUtils.isNotBlank(paymentRecord.getIdCard())) {
				paymentRecord.setIdCard(AESUtil.aesEncrypt(paymentRecord.getIdCard()));
			}
			if (StringUtils.isNotBlank(paymentRecord.getOwner())) {
				paymentRecord.setOwner(AESUtil.aesEncrypt(paymentRecord.getOwner()));
			}
			if (StringUtils.isNotBlank(paymentRecord.getPhoneNo())) {
				paymentRecord.setPhoneNo(AESUtil.aesEncrypt(paymentRecord.getPhoneNo()));
			}
		}
	}

	private void aesDecrypt(PaymentRecord paymentRecord) {
		if (null != paymentRecord) {
			if (StringUtils.isNotBlank(paymentRecord.getCardNo())) {
				paymentRecord.setCardNo(AESUtil.aesDecrypt(paymentRecord.getCardNo()));
			}
			if (StringUtils.isNotBlank(paymentRecord.getIdCard())) {
				paymentRecord.setIdCard(AESUtil.aesDecrypt(paymentRecord.getIdCard()));
			}
			if (StringUtils.isNotBlank(paymentRecord.getOwner())) {
				paymentRecord.setOwner(AESUtil.aesDecrypt(paymentRecord.getOwner()));
			}
			if (StringUtils.isNotBlank(paymentRecord.getPhoneNo())) {
				paymentRecord.setPhoneNo(AESUtil.aesDecrypt(paymentRecord.getPhoneNo()));
			}
		}
	}

	@Override
	public void updateRecordNo(Long orderId, String smstype, String payOrderId, PayRecordStatusEnum status,
			int needItem,String redirectType) {

		PaymentRecord paymentRecord = new PaymentRecord();
		paymentRecord.setId(orderId);
		paymentRecord.setNeedItem(needItem);
		paymentRecord.setSmsVerifyType(smstype);
		paymentRecord.setPaymentOrderNo(payOrderId);
		paymentRecord.setState(status);
		paymentRecord.setRedirectType(redirectType);
		try {
			// 加密敏感信息
			aesEncrypt(paymentRecord);
			int effortRow = paymentRecordDao.updateRecordNo(paymentRecord);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		} catch (Exception e) {
			logger.error("更新支付订单失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		} finally {
			// 解密敏感信息
			aesDecrypt(paymentRecord);
		}
	}

	@Override
	public void updateRecordStatusAndPaymentExt(Long id, String payOrderId, PayRecordStatusEnum ordered, String paymentExt) {
		PaymentRecord paymentRecord = new PaymentRecord();
		paymentRecord.setId(id);
		paymentRecord.setPaymentOrderNo(payOrderId);
		paymentRecord.setState(ordered);
		paymentRecord.setPaymentExt(paymentExt);
		try {
			int effortRow = paymentRecordDao.updateRecordStatusAndPaymentExt(paymentRecord);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		} catch (Exception e) {
			logger.error("更新支付订单失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}


	@Override
	public PaymentRecord findRecordBytoken(String token) {
		UserAccount userAccount = userProceeService.getUserAccountInfo(token);
		if(userAccount==null){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if(userAccount.getPaymentRecordId()!=null && Long.valueOf(userAccount.getPaymentRecordId())>0){
			return this.findRecordByPaymentRecordId(userAccount.getPaymentRecordId());
		}
		return null;
	}

	@Override
	public PaymentRecord createRecordWhenUnexsit(PaymentRequest paymentRequest, RecordCondition compareCondition,
			PersonHoldCard person, String recordPayType, String externalUserId, String token, String tmpCardId,String payMerchantNo) {
		PaymentRecord paymentRecord = filterPaymentRecordList(paymentRequest.getTradeSysOrderId(),
				paymentRequest.getTradeSysNo(), compareCondition);
		if (paymentRecord != null) {
			return paymentRecord;
		}
		paymentRecord = paymentRequest.buildPaymentRecord(externalUserId,tmpCardId);
		supplyPaymentRecord(paymentRecord, person, compareCondition, recordPayType, token,payMerchantNo);
		long paymentRecordId = savePaymentRecord(paymentRecord);
		paymentRecord.setId(paymentRecordId);
		if(StringUtils.isNotBlank(token)){
			userProceeService.getAndUpdatePaymentRecordId(token, paymentRecord.getId() + "");
		}
		return paymentRecord;
	}
	
	public PaymentRecord createPaymentRecord(PaymentRequest paymentRequest, RecordCondition compareCondition,
			PersonHoldCard person, String recordPayType, String externalUserId, String token, String tmpCardId,String payMerchantNo){
		PaymentRecord paymentRecord = paymentRequest.buildPaymentRecord(externalUserId,tmpCardId);
		supplyPaymentRecord(paymentRecord, person, compareCondition, recordPayType, token,payMerchantNo);
		long paymentRecordId = savePaymentRecord(paymentRecord);
		paymentRecord.setId(paymentRecordId);
		if(StringUtils.isNotBlank(token)){
			userProceeService.getAndUpdatePaymentRecordId(token, paymentRecord.getId() + "");
		}
		return paymentRecord;
	}

	@Override
	public PaymentRecord filterPaymentRecordList(String tradeSysOrderId, String tradeSysN0,
			RecordCondition compareCondition) {
		List<PaymentRecord> allPaymentRecordList = findRecordList(tradeSysOrderId, tradeSysN0);
		if (CollectionUtils.isEmpty(allPaymentRecordList)) {
			return null;
		}
		for (PaymentRecord record : allPaymentRecordList) {
			if (compareCondition.isValidateStatus()) {
				if (PayRecordStatusEnum.SUCCESS == record.getState()
						|| PayRecordStatusEnum.PAYREVERSE == record.getState()
						|| PayRecordStatusEnum.TRADEREVERSE == record.getState()) {
					// 下单流程，一笔交易订单对应的支付记录列表中，只要其中一笔已成功/冲正时，就抛异常
					throw CommonUtil.handleException(Errors.THRANS_FINISHED);
				}
				// 如果支付记录处于失败或者比较不符合比较条件，返回空
				if ((PayRecordStatusEnum.FAILED == record.getState())
						|| PayRecordStatusEnum.PAYING == record.getState()) {
					return null;
				}
			}
			if (compareCondition.compare(record)) {
				return record;
			}
		}
		return null;
	}

	@Override
	public PaymentRecord getNonNullPaymentRecord(String tradeSysOrderId, String tradeSysNo,
			RecordCondition compareCondition) {
		PaymentRecord paymentRecord = filterPaymentRecordList(tradeSysOrderId, tradeSysNo, compareCondition);
		if (paymentRecord == null) {
			throw new CashierBusinessException(Errors.PAY_RECORD_NULL);
		}
		return paymentRecord;
	}
	
	@Override
	public PaymentRecord getNonNullPaymentRecord(String recordId, RecordCondition compareCondition) {
		PaymentRecord paymentRecord = findRecordByPaymentRecordId(recordId);
		if (paymentRecord == null || !compareCondition.compare(paymentRecord)) {
			throw new CashierBusinessException(Errors.PAY_RECORD_NULL);
		}
		boolean legal = paymentRecord.checkStatusEnd(compareCondition.getOrderAction());
		if (legal) {
			return paymentRecord;
		}
		throw new CashierBusinessException(Errors.ORDER_STATUS_INVALID);
	}

	/**
	 * @title 新建paymentRecord记录时，对该记录的信息进行补充
	 * @description include bindId/signRelation/signId、cardNo、phoneNo etc.
	 * @param paymentRecord
	 * @param person
	 * @param condition
	 * @param recordPayType
	 */
	private void supplyPaymentRecord(PaymentRecord paymentRecord, PersonHoldCard person, RecordCondition condition,
			String recordPayType, String token,String payMerchantNo) {
		paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
		paymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
		paymentRecord.setIdCardType(IdCardTypeEnum.IDENTITY.toString());
		paymentRecord.setPayTool(condition.getPayTool());
		paymentRecord.setPayType(recordPayType);
		if(StringUtils.isNotBlank(token)){
			paymentRecord.setTokenId(token);
		}
		if(StringUtils.isNotBlank(condition.getBindId())){
			paymentRecord.setBindId(condition.getBindId());
		}
		if(StringUtils.isNotBlank(condition.getCardInfoId())){
			paymentRecord.setCardInfoId(condition.getCardInfoId());
		}
		if (StringUtils.isNotBlank(condition.getPeriod()) && StringUtils.isNumeric(condition.getPeriod())) {
			paymentRecord.setPeriod(Integer.parseInt(condition.getPeriod()));
		}
		if (person != null) {
			if (StringUtils.isBlank(paymentRecord.getCardType())
					&& person.getCard().getCardType()!=null) {
				paymentRecord.setCardType(person.getCard().getCardType().name());
			}
			if (StringUtils.isBlank(paymentRecord.getCardNo())) {
				paymentRecord.setCardNo(person.getCard().getCardNo());
			}
			if (StringUtils.isBlank(paymentRecord.getPhoneNo())) {
				paymentRecord.setPhoneNo(person.getPhoneN0());
			}
			if (StringUtils.isBlank(paymentRecord.getOwner())) {
				paymentRecord.setOwner(person.getOwner());
			}
			if (StringUtils.isBlank(paymentRecord.getIdCard())) {
				paymentRecord.setIdCard(person.getIdno());
			}
			if (StringUtils.isBlank(paymentRecord.getBankCode()) && person.bankNotNull()) {
				paymentRecord.setBankCode(person.getCard().getBank().getBankCode());
			}
		}
		if(StringUtils.isNotBlank(payMerchantNo)){
			paymentRecord.setPayMerchantNo(payMerchantNo);
		}
	}
	
	@Override
	public void recoverRecordToObjStatus(Long paymentRecordId, PayRecordStatusEnum objStatus,
			PayRecordStatusEnum preStatus) {
		try {
			logger.info("开始恢复支付订单为" + objStatus + "状态");
			updateRecordStateBaseOnOriginalStatus(paymentRecordId, objStatus,
					Arrays.asList(new PayRecordStatusEnum[] { preStatus }));
		} catch (CashierBusinessException e) {
			logger.error("更新支付订单为" + objStatus + "状态失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}
	
	@Override
	public boolean updateRecordToPaying(Long paymentRecordId, PayRecordStatusEnum[] preRecordStatusList) {
		boolean isRepeatePay = false;
		try {
			updateRecordStateBaseOnOriginalStatus(paymentRecordId, PayRecordStatusEnum.PAYING,
					Arrays.asList(preRecordStatusList));
		} catch (CashierBusinessException e) {
			logger.error("更新支付订单为paying状态失败", e);
			isRepeatePay = true;
		}
		return isRepeatePay;
	}

	@Override
	public void avoidRepeatPayWithException(PaymentRecord paymentRecord, PayRecordStatusEnum[] preRecordStatusList) {
		boolean isRepeatePay = updateRecordToPaying(paymentRecord.getId(), preRecordStatusList);
		if (isRepeatePay) {
			throw new CashierBusinessException(Errors.REPEAT_ORDER); // TODO 重新定义一个错误码，错误描述信息大概就是
		}
	}
	
}
