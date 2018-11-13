package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.SystemEnum;
import com.yeepay.g3.core.nccashier.gateway.service.FrontEndService;
import com.yeepay.g3.core.nccashier.gateway.service.FrontedInstallmentService;
import com.yeepay.g3.core.nccashier.gateway.service.NcPayService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.gateway.service.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.nccashier.gateway.service.YOPService;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.QueryResultModel;
import com.yeepay.g3.core.nccashier.vo.SimpleRecodeInfoModel;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.service.QueryPaymentResultFacade;
import com.yeepay.g3.facade.nccashier.util.CommonUtils;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordQueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yeepay.g3.core.nccashier.utils.CommonUtil.PP_PAYRESULT_MQ_SWTICH_ON;

/**
 * 
 * @author zhen.tan
 * @since：2016年5月26日 上午12:27:39
 * 
 */
@Service("queryResultService")
public class QueryResultServiceImpl extends NcCashierBaseService implements QueryResultService {

	private static final Logger logger = LoggerFactory.getLogger(QueryResultService.class);

	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private PaymentRequestService paymentRequestService;

	@Resource
	private NcPayService ncPayService;

	@Resource
	private NcPayResultProccess ncPayResultProccess;

	@Resource
	private FrontEndService frontEndService;

	@Resource
	private FrontedInstallmentService frontedInstallmentService;

	@Resource
	private PayProcessorService payProcessorService;

	@Resource
	private CashierBindCardService cashierBindCardService;

	@Resource
	private YOPService yopService;

	@Override
	public CombinedPaymentDTO validateQueryBusinInfo(CashierQueryRequestDTO queryRequest) {

		PaymentRecord paymentRecord = paymentProcessService
				.findRecordByPaymentRecordId(queryRequest.getRecordId() + "");
		if (paymentRecord == null) {
			throw CommonUtil.handleException(Errors.PAY_RECORD_NULL);
		}
		PaymentRequest paymentRequest = paymentRequestService.findPayRequestById(queryRequest.getRequestId());

		if (paymentRequest == null) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}

		if (paymentRecord.getPaymentRequestId() != queryRequest.getRequestId()) {
			throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
		}

		CombinedPaymentDTO combinedPaymentDto = new CombinedPaymentDTO();
		combinedPaymentDto.setPaymentRequest(paymentRequest);
		combinedPaymentDto.setPaymentRecord(paymentRecord);
		return combinedPaymentDto;
	}

	@Override
	public void supplyQureyResult(CombinedPaymentDTO combinedPaymentDto, CashierQueryRequestDTO queryRequest,
			CashierQueryResponseDTO response) {

		PaymentRecord paymentRecord = combinedPaymentDto.getPaymentRecord();
		PaymentRequest paymentRequest = combinedPaymentDto.getPaymentRequest();
		TradeNoticeDTO tradeNoticeDTO = null;
		if (!CommonUtils.isPayProcess(paymentRequest.getPaySysCode(), paymentRequest.getTradeSysNo())) {// 非订单处理器请求，查询内部状态
			String frontNotifyWay = "";
			String queryTradeResultInterfceUrl = "";

			if (!PayRequestStatusEnum.SUCCESS.getValue().equals(paymentRequest.getState())
					&& !PayRequestStatusEnum.FAILED.getValue().equals(paymentRequest.getState())
					&& !PayRequestStatusEnum.REVERSE.getValue().equals(paymentRequest.getState())
					&& queryRequest.isRepeatQuery()) {
				queryPaySystemAndUpdateState(paymentRecord, paymentRequest);
			}

			Map<String, String> businessInfoMap = CommonUtil
					.getSysConfigFrom3G(Constant.TRADE_CALLBACK_WAY + paymentRequest.getTradeSysNo());

			if (null != businessInfoMap) {
				frontNotifyWay = businessInfoMap.get(CommonUtil.FRONT_NOTIFY_WAP);
				queryTradeResultInterfceUrl = businessInfoMap.get(CommonUtil.QUERY_TRADE_RESULT_INTERFCE);
			} else {
				throw CommonUtil.handleException(Errors.BUSINESS_NOT_SUPPORT);
			}

			if (CommonUtil.PASSIVE.equals(frontNotifyWay)) {
				if (PayRecordStatusEnum.SUCCESS == paymentRecord.getState()) {
					tradeNoticeDTO = queryTradeResult(queryTradeResultInterfceUrl, paymentRecord.getTradeSysOrderId(),
							paymentRecord.getTradeSysNo());
					if (null != tradeNoticeDTO) {
						if (tradeNoticeDTO.getTradeState() == TradeStateEnum.FAILED) {
							tradeNoticeDTO.setErrorCode("3000020");
							tradeNoticeDTO.setErrorMsg("系统异常，请联系客服");
							tradeNoticeDTO.setCashierVersion(paymentRequest.getCashierVersion());
							tradeNoticeDTO.setPaymentRequestId(paymentRequest.getId());
							tradeNoticeDTO.setMerchantNo(paymentRequest.getMerchantNo());
						}
					} else {
						tradeNoticeDTO = new TradeNoticeDTO();
						tradeNoticeDTO.setPaymentAmount(paymentRequest.getOrderAmount());
						tradeNoticeDTO.setMerchantNo(paymentRequest.getMerchantNo());
						tradeNoticeDTO.setCashierVersion(paymentRequest.getCashierVersion());
						tradeNoticeDTO.setPaymentRequestId(paymentRequest.getId());
						tradeNoticeDTO.setMerchantOrderId(paymentRequest.getMerchantOrderId());
						tradeNoticeDTO.setTradeState(TradeStateEnum.PAYING);
					}
				} else {
					tradeNoticeDTO = makeWapTradeNoticeDTO(paymentRequest, paymentRecord);
				}
			} else if (CommonUtil.ACTIVE.equals(frontNotifyWay)) {
				tradeNoticeDTO = makeWapTradeNoticeDTO(paymentRequest, paymentRecord);
			} else {
				throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
		} else {// 订单处理器，查询支付处理器状态
			tradeNoticeDTO = new TradeNoticeDTO();
			tradeNoticeDTO.setPaymentAmount(paymentRequest.getOrderAmount());
			tradeNoticeDTO.setMerchantNo(paymentRequest.getMerchantNo());
			tradeNoticeDTO.setCashierVersion(paymentRequest.getCashierVersion());
			tradeNoticeDTO.setPaymentRequestId(paymentRequest.getId());
			tradeNoticeDTO.setMerchantOrderId(paymentRequest.getMerchantOrderId());
			tradeNoticeDTO.setOrderSysNo(paymentRequest.getOrderSysNo());
			// modify by meiling.zhuang：支持银行卡分期支付结果页定制，展示分期期数、每期还款额、首期还款额
			tradeNoticeDTO.setPayTool(paymentRecord.getPayTool());
			tradeNoticeDTO.setBankCode(paymentRecord.getBankCode());
			tradeNoticeDTO.setPeriod(paymentRecord.getPeriod() + "");
			// modify by ruiyang.du：支持担保分期支付结果页定制，增加展示分期服务费率
			String paymentExt = paymentRecord.getPaymentExt();
			JSONObject paymentExtJson = JSONObject.parseObject(paymentExt);
			if (null != paymentExtJson) {
				String serviceChargeRate = paymentExtJson.getString(Constant.GUARANTEE_INSTALLMENT_SERVICE_CHARGE_RATE);
				tradeNoticeDTO.setServiceChargeRate(serviceChargeRate);
			}
			//开关控制 使用pp查询接口还是直接使用record
			String isPpSendPayResultMQ = CommonUtil.getPpSendPayResultMQSwtich();
			if(isPpSendPayResultMQ!=null && isPpSendPayResultMQ.equals(PP_PAYRESULT_MQ_SWTICH_ON)){
				useMqResult(paymentRecord,paymentRequest,tradeNoticeDTO);
			}else{
				usePpQueryResult(paymentRecord,paymentRequest,queryRequest,tradeNoticeDTO);
			}
		}
		tradeNoticeDTO.setPaymentRequestId(paymentRequest.getId());
		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
		response.setTradeNoticeDTO(tradeNoticeDTO);
		bindCard(tradeNoticeDTO,paymentRequest,paymentRecord);
	}

	/**
	 * 使用查询pp得到的结果的处理
	 */
	private void usePpQueryResult(PaymentRecord paymentRecord,PaymentRequest paymentRequest,
								  CashierQueryRequestDTO queryRequest,TradeNoticeDTO tradeNoticeDTO){
		String queryReady = "ready";
		if (StringUtils.isNotEmpty(paymentRecord.getPaymentOrderNo()) && queryRequest.isNeedQueryRedis()) {
			queryReady = CommonUtil.getPpPayResultQueryState(paymentRecord.getPaymentOrderNo());
		}
		if (StringUtils.isNotEmpty(queryReady)) {
			if(StringUtils.isNotEmpty(paymentRecord.getPaymentOrderNo())){
				QueryResultModel result = queryPayProccesorOrderStatus(paymentRecord);
				if (result.getState() == TradeStateEnum.SUCCESS) {
					tradeNoticeDTO.setTradeState(TradeStateEnum.SUCCESS);
					tradeNoticeDTO.setFrontCallBackUrl(buildFrontCallbackUrl(paymentRequest));
					tradeNoticeDTO.setActualAmount(paymentRecord.getActualAmount());
				} else if (result.getState() == TradeStateEnum.FAILED) {
					tradeNoticeDTO.setTradeState(TradeStateEnum.FAILED);
					CashierBusinessException errorInfo = CommonUtil.handleException(result.getPaySystem(),
							result.getErrorCode(), result.getErrorMsg());
					tradeNoticeDTO.setErrorCode(errorInfo.getDefineCode());
					tradeNoticeDTO.setErrorMsg(errorInfo.getMessage());
				} else if (result.getState() == TradeStateEnum.TRADEREVERSE) {
					tradeNoticeDTO.setTradeState(TradeStateEnum.CANCEL);
				} else {
					tradeNoticeDTO.setTradeState(TradeStateEnum.PAYING);
				}
			}else{
				tradeNoticeDTO.setTradeState(TradeStateEnum.PAYING);
			}
		} else {
			tradeNoticeDTO.setTradeState(TradeStateEnum.PAYING);
		}
	}

	/**
	 * 使用ppMQ推送的消息更新的record的数据
	 */
	private void useMqResult(PaymentRecord paymentRecord,PaymentRequest paymentRequest,TradeNoticeDTO tradeNoticeDTO){
		if(StringUtils.isNotEmpty(paymentRecord.getPaymentOrderNo())){
			if (paymentRecord.getState() == PayRecordStatusEnum.SUCCESS) {
				tradeNoticeDTO.setTradeState(TradeStateEnum.SUCCESS);
				tradeNoticeDTO.setFrontCallBackUrl(buildFrontCallbackUrl(paymentRequest));
				tradeNoticeDTO.setActualAmount(paymentRecord.getActualAmount());
			} else if (paymentRecord.getState() == PayRecordStatusEnum.FAILED) {
				tradeNoticeDTO.setTradeState(TradeStateEnum.FAILED);
				tradeNoticeDTO.setErrorCode(paymentRecord.getErrorCode());
				tradeNoticeDTO.setErrorMsg(paymentRecord.getErrorMsg());
			} else if (paymentRecord.getState() == PayRecordStatusEnum.TRADEREVERSE) {
				tradeNoticeDTO.setTradeState(TradeStateEnum.CANCEL);
			} else {
				tradeNoticeDTO.setTradeState(TradeStateEnum.PAYING);
			}
		}else{
			tradeNoticeDTO.setTradeState(TradeStateEnum.PAYING);
		}
	}










	private void bindCard(TradeNoticeDTO tradeNoticeDTO, PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		logger.info("绑卡开始,tradeNoticeDTO:{}", tradeNoticeDTO);
		if (null != tradeNoticeDTO && TradeStateEnum.SUCCESS == tradeNoticeDTO.getTradeState()) {
			String bindid = RedisTemplate.getTargetFromRedis(Constant.BINDCARD_RECORD + paymentRequest.getId(),
					String.class);
			logger.info("从redis获取的bindid为" + bindid + "支付类型为" + paymentRecord.getPayType() + "支付卡ID为"
					+ paymentRecord.getBindId());
			if (StringUtils.isBlank(bindid) && CardInfoTypeEnum.TEMP.name().equals(paymentRecord.getPayType())
					&& StringUtils.isNotBlank(paymentRecord.getCardInfoId())) {
				logger.info("首次支付需要绑卡,卡ID=" + paymentRecord.getCardInfoId());
				cashierBindCardService.bindCard(paymentRequest, paymentRecord);
				RedisTemplate.setCacheObjectSumValue(Constant.BINDCARD_RECORD + paymentRequest.getId(),
						paymentRecord.getBindId(), Constant.BINDCARD_RECORD_TIMEOUT);
			}
		}
		logger.info("绑卡结束");
	}

	private QueryResultModel queryPayProccesorOrderStatus(PaymentRecord paymentRecord) {
		QueryResultModel result = RedisTemplate.getTargetFromRedis(
				Constant.FINAL_PAY_RESULT + paymentRecord.getPaymentOrderNo(), QueryResultModel.class);
		if (result != null) {
			if(paymentRecord.getActualAmount()==null){
				// 支付记录的实际支付金额为空（不会为0，因为默认是空，如果为0，说明已经写进去了）
				logger.info("token={}实际支付金额为空，需要查询实际支付金额", paymentRecord.getTokenId());
				PaymentRecord payRecordInDB = paymentProcessService
						.findRecordByPaymentRecordId(paymentRecord.getId()+"");
				if (payRecordInDB == null) {
					throw CommonUtil.handleException(Errors.PAY_RECORD_NULL);
				}
				paymentRecord.setActualAmount(payRecordInDB.getActualAmount());
			}
			
			return result;
		}
		result = new QueryResultModel();
		PayRecordQueryRequestDTO requestDTO = new PayRecordQueryRequestDTO();
		requestDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
		PayRecordResponseDTO responseDTO = payProcessorService.query(requestDTO);
		if (responseDTO.getOrderSystemStatus() == OrderSystemStatusEnum.SUCCESS) {
			result.setState(TradeStateEnum.SUCCESS);
			paymentRecord.setState(PayRecordStatusEnum.SUCCESS);
			paymentRecord.setCardInfoId(responseDTO.getCardId());
			paymentRecord.setActualAmount(responseDTO.getFirstPayAmount());
			paymentProcessService.updateRecord(paymentRecord);
		} else if (responseDTO.getOrderSystemStatus() == OrderSystemStatusEnum.REVERSE) {
			result.setState(TradeStateEnum.TRADEREVERSE);
		} else if (responseDTO.getTrxStatus() == TrxStatusEnum.FAILUER) {
			result.setState(TradeStateEnum.FAILED);
			CashierBusinessException error = CommonUtil.handleException(SysCodeEnum.PP.name(),
					responseDTO.getResponseCode(), responseDTO.getResponseMsg());
			result.setErrorCode(error.getDefineCode());
			result.setErrorMsg(error.getMessage());
		}

		if (result.getState() != null) {
			RedisTemplate.setCacheObjectSumValue(Constant.FINAL_PAY_RESULT + paymentRecord.getPaymentOrderNo(), result,
					Constant.FINAL_PAY_RESULT_TIMEOUT);
		}
		return result;
	}

	@Override
	public String buildFrontCallbackUrl(PaymentRequest paymentRequest) {
		StringBuffer url = new StringBuffer("");
		if (StringUtils.isNotEmpty(paymentRequest.getFrontCallBackUrl())) {
			// 如果是二代的交易，直接返回回调地址，无需封装
			if (TradeSysCodeEnum.G2NET.name().equals(paymentRequest.getTradeSysNo())) {
				return paymentRequest.getFrontCallBackUrl();
			}
			url.append(paymentRequest.getFrontCallBackUrl());
			if (paymentRequest.getFrontCallBackUrl().indexOf("?") > -1) {
				url.append("&merchantNo=").append(paymentRequest.getMerchantNo());
				url.append("&parentMerchantNo=").append(paymentRequest.getParentMerchantNo());
				url.append("&orderId=").append(paymentRequest.getMerchantOrderId());
			} else {
				url.append("?merchantNo=").append(paymentRequest.getMerchantNo());
				url.append("&parentMerchantNo=").append(paymentRequest.getParentMerchantNo());
				url.append("&orderId=").append(paymentRequest.getMerchantOrderId());
			}
			StringBuffer signContent = new StringBuffer("");
			signContent.append("merchantNo=").append(paymentRequest.getMerchantNo()).append("&parentMerchantNo=")
					.append(paymentRequest.getParentMerchantNo()).append("&orderId=")
					.append(paymentRequest.getMerchantOrderId());
			String sign = yopService.sign(signContent.toString());
			if (StringUtils.isEmpty(sign)) {
				throw CommonUtil.handleException(Errors.SIGN_ERROR);
			} else {
				url.append("&sign=" + sign);
			}
		}else {
			// 查询交易系统是否配置前端回调地址，获取RMI地址
			Map<String, String> businessInfoMap = CommonUtil
					.getSysConfigFrom3G(Constant.TRADE_CALLBACK_WAY + paymentRequest.getTradeSysNo());

			if (null != businessInfoMap) {
				String queryTradeResultInterfceUrl = businessInfoMap.get(CommonUtil.QUERY_TRADE_RESULT_INTERFCE);
				String frontCallBackUrl = queryFrontCallBackUrl(queryTradeResultInterfceUrl, paymentRequest.getTradeSysOrderId(), paymentRequest.getTradeSysNo());
				url.append(frontCallBackUrl);
			}
		}


		return url.toString();
	}

	@Override
	public OrderNoticeDTO supplyPaymentOrder(String tradeSysOrderId, String tradeSysNo) {

		PaymentRequest paymentRequest = paymentRequestService.findPayRequestByTradeSysOrderId(tradeSysOrderId,
				tradeSysNo);
		if (null != paymentRequest) {
			OrderNoticeDTO orderNoticeDTO = null;
			List<PaymentRecord> recordList = paymentProcessService.findRecordList(paymentRequest.getTradeSysOrderId(),
					paymentRequest.getTradeSysNo());
			if (CollectionUtils.isEmpty(recordList)) {
				throw CommonUtil.handleException(Errors.PAY_RECORD_NULL);
			}

			// 如果支付请求表是终态直接返回
			if (PayRequestStatusEnum.SUCCESS.getValue().equals(paymentRequest.getState())
					|| PayRequestStatusEnum.FAILED.getValue().equals(paymentRequest.getState())) {
				orderNoticeDTO = orderNoticeTransfer(paymentRequest, recordList);
			} else {
				List<PaymentRecord> paymentRecordList = paymentProcessService.findRecordList(tradeSysOrderId,
						tradeSysNo, PayRecordStatusEnum.PAYING.getValue());
				if (CollectionUtils.isNotEmpty(paymentRecordList)) {
					for (PaymentRecord paymentRecord : paymentRecordList) {
						queryPaySystemAndUpdateState(paymentRecord, paymentRequest);
					}

					for (PaymentRecord paymentRecord : recordList) {
						boolean exist = false;
						for (PaymentRecord updatePaymentRecord : paymentRecordList) {
							if (paymentRecord.getId().longValue() == updatePaymentRecord.getId().longValue()) {
								exist = true;
								break;
							}
						}
						if (!exist) {
							paymentRecordList.add(paymentRecord);
						}
					}
					orderNoticeDTO = orderNoticeTransfer(paymentRequest, paymentRecordList);
				} else {
					orderNoticeDTO = orderNoticeTransfer(paymentRequest, recordList);
				}

			}
			return orderNoticeDTO;
		} else {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
	}

	private void queryPaySystemAndUpdateState(PaymentRecord paymentRecord, PaymentRequest paymentRequest) {
		PayQueryResponseDTO payQueryResponseDTO = null;
		if (PayTypeEnum.WECHAT_ATIVE_SCAN.name().equals(paymentRecord.getPayType())
				|| PayTypeEnum.WECHAT_OPENID.name().equals(paymentRecord.getPayType())
				|| PayTypeEnum.WECHAT_H5_WAP.name().equals(paymentRecord.getPayType())
				|| PayTypeEnum.ALIPAY.name().equals(paymentRecord.getPayType())
				|| PayTypeEnum.WECHAT_H5_LOW.name().equals(paymentRecord.getPayType())
				|| PayTypeEnum.ALIPAY_H5_STANDARD.name().equals(paymentRecord.getPayType())) {
			FrontendQueryResponseDTO response = frontEndService.queryPaymentOrder(paymentRecord.getTradeSysOrderId(),
					paymentRecord.getPayType());
			payQueryResponseDTO = new PayQueryResponseDTO();
			payQueryResponseDTO.setBankCode(response.getPayBank());
			payQueryResponseDTO.setBankOrderNo(response.getOrderNo());
			payQueryResponseDTO.setErrorCode(response.getResponseCode());
			payQueryResponseDTO.setErrorMsg(response.getResponseMsg());
			payQueryResponseDTO.setPayCompleteDate(
					response.getPaySuccessTime() == null ? null : response.getPaySuccessTime().getTime());
			payQueryResponseDTO.setPayOrderId(response.getOrderNo());
			payQueryResponseDTO
					.setCost(response.getBankTotalCost() != null ? new Amount(response.getBankTotalCost()) : null);
			payQueryResponseDTO.setMerchantNo(response.getCustomerNumber());
			payQueryResponseDTO.setOrderAmount(new Amount(response.getTotalAmount()));
			payQueryResponseDTO.setFrpCode(response.getPayInterface());
			if (PayStatusEnum.FAILURE == response.getPayStatus() || PayStatusEnum.INIT == response.getPayStatus()) {
				payQueryResponseDTO.setStatus(PaymentOrderStatusEnum.DOING);
			} else if (PayStatusEnum.SUCCESS == response.getPayStatus()) {
				payQueryResponseDTO.setStatus(PaymentOrderStatusEnum.SUCCESS);
			}
			payQueryResponseDTO.setBasicProductCode(response.getBasicProductCode());
			payQueryResponseDTO.setTradeSerialNo(response.getTransactionId());
			paymentRecord.setCardType(response.getPayBankcardType() == null ? PayBankcardType.CFT.name()
					: response.getPayBankcardType().name());
		} else if (PayTypeEnum.CFL.name().equals(paymentRecord.getPayType())) {
			InstallmentResultMessage resultMessage = frontedInstallmentService.queryInstallmentOrder(paymentRecord);
			payQueryResponseDTO = new PayQueryResponseDTO();
			payQueryResponseDTO.setBankCode(resultMessage.getLoanTerm());// 分期期数放在银行编码中
			payQueryResponseDTO.setBankOrderNo(resultMessage.getOrderNo());// 银行子订单号
			payQueryResponseDTO.setCost(
					resultMessage.getBankTotalCost() == null ? null : new Amount(resultMessage.getBankTotalCost()));
			payQueryResponseDTO.setErrorCode(resultMessage.getResponseCode());
			payQueryResponseDTO.setErrorMsg(resultMessage.getResponseMsg());
			payQueryResponseDTO.setPayCompleteDate(
					resultMessage.getPaySuccessTime() == null ? null : resultMessage.getPaySuccessTime().getTime());
			payQueryResponseDTO.setPayOrderId(resultMessage.getOrderNo());
			payQueryResponseDTO.setFrpCode(resultMessage.getPayInterface());
			// payQueryResponseDTO.setTradeSerialNo(resultMessage.getTransactionId());//分期公司支付订单号
			if (PayStatusEnum.FAILURE == resultMessage.getPayStatus()
					|| PayStatusEnum.INIT == resultMessage.getPayStatus()) {
				payQueryResponseDTO.setStatus(PaymentOrderStatusEnum.DOING);
			} else if (PayStatusEnum.SUCCESS == resultMessage.getPayStatus()) {
				payQueryResponseDTO.setStatus(PaymentOrderStatusEnum.SUCCESS);
			}
			payQueryResponseDTO.setBasicProductCode(resultMessage.getBasicProductCode());

		} else {
			payQueryResponseDTO = ncPayService.queryPaymentOrder(paymentRecord.getPaymentOrderNo());
		}
		ncPayResultProccess.processForQuery(payQueryResponseDTO, paymentRecord, paymentRequest);
	}

	/**
	 * 调用nctrade查询交易结果
	 * 
	 * @param queryTradeResultUrl
	 * @param tradeSysOrderId
	 * @param tradeSysNo
	 * @return
	 */
	private TradeNoticeDTO queryTradeResult(String queryTradeResultUrl, String tradeSysOrderId, String tradeSysNo) {
		TradeNoticeDTO tradeNoticeDTO = null;
		QueryPaymentResultFacade queryPaymentResultFacade = null;
		try {
			queryPaymentResultFacade = RemoteFacadeProxyFactory.getService(queryTradeResultUrl,
					RemotingProtocol.HESSIAN, QueryPaymentResultFacade.class, SystemEnum.NCTRADE.name());
		} catch (Exception e) {
			// logger.info("获取QueryPaymentResultFacade接口服务异常:{}", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}

		try {
			tradeNoticeDTO = queryPaymentResultFacade.queryPayResult(tradeSysOrderId, tradeSysNo);
		} catch (Exception e) {
			// logger.info("调用查询交易结果接口QueryPaymentResultFacade.queryPayResult异常:{}",
			// e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}

		return tradeNoticeDTO;
	}


	/**
	 * 查询回调地址
	 * @param queryTradeResultUrl
	 * @param tradeSysOrderId
	 * @param tradeSysNo
	 * @return
	 */
	private String queryFrontCallBackUrl(String queryTradeResultUrl, String tradeSysOrderId, String tradeSysNo) {
		QueryPaymentResultFacade queryPaymentResultFacade = null;
		String url = "";
		try {
			queryPaymentResultFacade = RemoteFacadeProxyFactory.getService(queryTradeResultUrl,
					RemotingProtocol.HESSIAN, QueryPaymentResultFacade.class, tradeSysNo);
		} catch (Exception e) {
			logger.info("获取QueryPaymentResultFacade接口服务异常:{}", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}

		try {
			url = queryPaymentResultFacade.getFrontCallbackUrl(tradeSysOrderId, tradeSysNo);
		} catch (Exception e) {
			// 远程调用拦截器负责远程调用出参入参打印
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}

		return url;
	}

	@Override
	public void listenCanPayResultQuery(PayResultQuerySignListenRequestDTO request,
			PayResultQuerySignListenResponseDTO response) {
		PaymentRecord paymentRecord = null;
		// 获取paymentRecord
		if ((request.getPaymentRecordId() != null) && (request.getPaymentRecordId() > 0)) {
			paymentRecord = paymentProcessService.findRecordByPaymentRecordId(String.valueOf(request.getPaymentRecordId()));
		} else {
			paymentRecord = paymentProcessService.findRecordBytoken(request.getToken());
		}
		// 获取超时次数
		long tryTimes = CommonUtil.getListenerServerTimeout();
		String queryState = null;
		List<SimpleRecodeInfoModel> result = null;

		while (tryTimes > 0) {
			if (paymentRecord != null && StringUtils.isNotBlank(paymentRecord.getPaymentOrderNo())) {
				queryState = CommonUtil.getPpPayResultQueryState(paymentRecord.getPaymentOrderNo());
				if (StringUtils.isNotEmpty(queryState)) {
					response.setQueryState(true);
					return;
				}
			}
			if (result == null) {
				String resultJson = RedisTemplate.getTargetFromRedisToString(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + request.getToken());
				result = JSONObject.parseArray(resultJson, SimpleRecodeInfoModel.class);
				if(result == null){
					//读取不到缓存，直接置为空列表，避免多次读取缓存
					result = new ArrayList<SimpleRecodeInfoModel>();
				}
			}
			if(CollectionUtils.isNotEmpty(result)){
				for (SimpleRecodeInfoModel model : result) {
					queryState = CommonUtil.getPpPayResultQueryState(model.getPayOrderNo());
					if (StringUtils.isNotEmpty(queryState)) {
						response.setQueryState(true);
						userProceeService.getAndUpdatePaymentRecordId(request.getToken(), model.getRecordId() + "");
						return;
					}
				}
			}
			tryTimes--;
			try {
				//查询频率可定制 modified by yangmin.peng
				Thread.sleep(CommonUtil.getPPPayResultQueryIntervalTime());
			} catch (Throwable t) {
				logger.error("listenCanPayResultQuery e:{}", t);
			}
		}

	}

	@Override
	public void queryOrderResult(OrderDetailInfoModel orderInfo, APIResultQueryResponseDTO response) {
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), false);
		QueryRequestDTO queryRequestDTO = buildQueryRequestDTO(paymentRequest);
		QueryResponseDTO queryResponseDTO = payProcessorService.queryOrderResult(queryRequestDTO);
		response.setStatus(
				queryResponseDTO.getPaymentStatus() == null ? null : queryResponseDTO.getPaymentStatus().name());
		response.setOperationType(queryResponseDTO.getPayType());
		response.setPayOrderId(queryResponseDTO.getPaymentSysNo());
		response.setBankOrderNO(queryResponseDTO.getBankOrderId());
		response.setBankTrxId(queryResponseDTO.getBankTrxId());
		response.setCost(queryResponseDTO.getCost());
		response.setFrpCode(queryResponseDTO.getChannelId());
		response.setBankPaySuccDate(queryResponseDTO.getBankPaySuccDate());
	}

	private QueryRequestDTO buildQueryRequestDTO(PaymentRequest paymentRequest) {
		QueryRequestDTO queryRequest = new QueryRequestDTO();
		queryRequest.setOrderNo(paymentRequest.getTradeSysOrderId());
		queryRequest.setOrderSystem(paymentRequest.getTradeSysNo());
		return queryRequest;
	}



}
