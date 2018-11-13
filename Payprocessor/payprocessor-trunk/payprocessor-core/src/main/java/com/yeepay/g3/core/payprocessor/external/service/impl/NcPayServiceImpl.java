package com.yeepay.g3.core.payprocessor.external.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.facade.ncpay.dto.*;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.external.service.NcPayService;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.ncpay.enumtype.OrderTypeEnum;
import com.yeepay.g3.facade.ncpay.exception.PaymentException;
import com.yeepay.g3.utils.common.log.Logger;

@Service("ncPayService")
public class NcPayServiceImpl extends AbstractService implements NcPayService {
	protected static final Logger logger = PayLoggerFactory.getLogger(NcPayServiceImpl.class);

	@Override
	public PaymentResponseDTO requestPayment(NcPayOrderRequestDTO requestDTO, PayRecord record) {
		PaymentResponseDTO respDTO = null;
		try {
			respDTO = paymentManagerWrapperFacade.requestPayment(bulidRequestPaymentParam(requestDTO, record, OrderTypeEnum.SALE));
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003001);
		} catch (Exception e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (null == respDTO) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return respDTO;
	}

	/**
	 * 支付订单查询
	 */
	@Override
	public PayQueryResponseDTO queryPaymentOrder(String paymentRecordId) throws PayBizException {
		PayQueryRequestDTO queryDTO = new PayQueryRequestDTO();
		queryDTO.setPayOrderId(paymentRecordId);
		PayQueryResponseDTO payQueryResponseDTO = null;
		try {
			payQueryResponseDTO = paymentManageFacade.queryPaymentOrder(queryDTO);
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003001);
		} catch (Exception e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (payQueryResponseDTO == null) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return payQueryResponseDTO;
	}

	@Override
	public boolean updateTaskStatus(String paymentNo, String bizOrderNum) {
		PayResultTaskRequestDTO payResultTaskRequestDTO = new PayResultTaskRequestDTO();
		payResultTaskRequestDTO.setPayOrderNum(paymentNo);
		payResultTaskRequestDTO.setBizOrderNum(bizOrderNum);
		boolean isUpdateSuccess = false;
		try {
			isUpdateSuccess = ncPayResultTaskFacade.updateTaskStatus(payResultTaskRequestDTO);
		} catch (Exception e) {
			logger.error("mq确认回调ncpay失败", e);
		}
		return isUpdateSuccess;
	}

	@Override
	public SmsSendResponseDTO verifyAndSendSms(NcSmsRequestDTO requestDTO,String ncpayPaymentNo) {
		SmsSendResponseDTO smsSendResponseDTO = null;
		try {
			smsSendResponseDTO = paymentManageFacade.verifyAndSendSms(bulidSmsRequest(requestDTO, ncpayPaymentNo));
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003001);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (smsSendResponseDTO == null) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return smsSendResponseDTO;
	}

	@Override
	public PayConfirmResponseDTO confirmPay(NcPayConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
		PayConfirmResponseDTO payConfirmResponseDTO = null;
		try {
			// 调用ncpay异步接口，不更新支付订单状态
			payConfirmResponseDTO = paymentManageFacade.confirmPay(bulidConfirmPayDTO(requestDTO, ncpayPaymentNo));

		} catch (PaymentException e) {// PaymentException YeepayBizException
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003001);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return payConfirmResponseDTO;
	}

	@Override
	public PayQueryResponseDTO synConfirmPay(NcPayConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
		PayQueryResponseDTO payQueryResponseDTO = null;
		try {
			payQueryResponseDTO = paymentManageFacade.synConfirmPay(bulidConfirmPayDTO(requestDTO, ncpayPaymentNo));
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003001);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return payQueryResponseDTO;
	}

	@Override
	public long bindCardByOrderId(String payOrderId) {
		long bindCardId = 0;
		try {
			bindCardId = paymentManagerWrapperFacade.bindCardByOrderId(payOrderId);
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003001);
		} catch (Exception e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return bindCardId;
	}

	private RequestPaymentParam bulidRequestPaymentParam(NcPayOrderRequestDTO requestDTO, PayRecord record, OrderTypeEnum orderTypeEnum) {
		RequestPaymentParam param = new RequestPaymentParam();
		// NCPAY业务方
		param.setBizType(requestDTO.getBizType());
		// NCPAY业务订单号
		param.setBizOrderNum(record.getRecordNo());
		param.setOrderType(orderTypeEnum);
		param.setBizOrderDate(record.getCreateTime());
		param.setMerchantNo(requestDTO.getCustomerNumber());
		param.setMerchantName(requestDTO.getCustomerName());
		param.setProductName(requestDTO.getProductName());
		param.setRequestNo(requestDTO.getOutTradeNo());
		param.setMemberType(requestDTO.getMemberType());
		param.setMemberNO(requestDTO.getMemberNO());
		if(record.isCombinedPay()) {
			param.setOrderAmount(record.getFirstPayAmount());
		}else {
			param.setOrderAmount(requestDTO.getAmount());
		}
		param.setCardInfoType(requestDTO.getCardInfoType());
		param.setCardInfoId(requestDTO.getCardInfoId());
		param.setGoodsInfo(requestDTO.getGoodsInfo());
		param.setToolInfo(requestDTO.getToolsInfo());
		param.setIndustryCode(requestDTO.getIndustryCode());
		param.setTerminalId(requestDTO.getPayScene());
		param.setRequestSystem(ConstantUtils.NC_PAY_REQUEST_SYS);
		param.setPayToolId(requestDTO.getPayTool());
		param.setDealUniqueSerialNo(requestDTO.getDealUniqueSerialNo());
		if (requestDTO.getUserFee() != null) {
			param.setPayerFee(requestDTO.getUserFee());
		} else {
			param.setPayerFee(new BigDecimal(0));
		}
		param.setRetailProductCode(record.getRetailProductCode());
		param.setBasicProductCode(record.getBasicProductCode());
		// 添加卡信息
		if(requestDTO.getBankCardInfoDTO() != null) {
			param.setCardInfoDTO(composeCardInfo(requestDTO.getBankCardInfoDTO()));
		}
		param.setExtParam(requestDTO.getExtParam());
		param.setPayRedirectUrl(requestDTO.getPayRedirectUrl());
		param.setSignRedirectUrl(requestDTO.getSignRedirectUrl());
		return param;
	}

	private SmsSendRequestDTO bulidSmsRequest(NcSmsRequestDTO requestDTO, String ncpayPaymentNo) {
		SmsSendRequestDTO smsRequestDTO = new SmsSendRequestDTO();
		smsRequestDTO.setPayOrderId(ncpayPaymentNo);
		smsRequestDTO.setSmsSendType(requestDTO.getSmsSendType());
		smsRequestDTO.setTmpCardId(requestDTO.getTmpCardId());
		if(requestDTO.getBankCardInfoDTO() != null) {
			smsRequestDTO.setCardInfoDTO(composeCardInfo(requestDTO.getBankCardInfoDTO()));
		}
		return smsRequestDTO;
	}

	private PayConfirmRequestDTO bulidConfirmPayDTO(NcPayConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
		PayConfirmRequestDTO ncpayRequestDTO = new PayConfirmRequestDTO();
		ncpayRequestDTO.setPayOrderId(ncpayPaymentNo);
		ncpayRequestDTO.setSmsCode(requestDTO.getSmsCode());
		ncpayRequestDTO.setTmpCardId(requestDTO.getTmpCardId());
		ncpayRequestDTO.setExtParam(requestDTO.getExtParam());
		if(requestDTO.getBankCardInfoDTO() != null) {
			ncpayRequestDTO.setCardInfoDTO(composeCardInfo(requestDTO.getBankCardInfoDTO()));
		}
		return ncpayRequestDTO;
	}

	@Override
	public CflOrderResponseDTO clfOrderRequest(NcPayCflOrderRequestDTO requestDTO, PayRecord record) {// TODO 命名
		CflOrderResponseDTO responseDTO = null;
		try {
			responseDTO = ncpayCflFacade.createPayment(buildCflpaymentParam(requestDTO, record));
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003011);
		} catch (Exception e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (null == responseDTO) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return responseDTO;
	}

	protected CflOrderRequestDTO buildCflpaymentParam(NcPayCflOrderRequestDTO requestDTO, PayRecord record) {
		CflOrderRequestDTO param = new CflOrderRequestDTO();
		param.setCflCount(record.getCflCount());
		param.setCflRate(record.getCflRate());
		param.setMerchantFeeSubsidy(record.getMerchantFeeSubsidy());
		param.setMerchantAmountSubsidy(record.getMerchantAmountSubsidy());
		param.setBizType(requestDTO.getBizType());
		param.setBizOrderNo(record.getRecordNo());
		param.setOrderType(OrderTypeEnum.BK_CFL);
		param.setBizOrderDate(record.getCreateTime());
		param.setMerchantNo(requestDTO.getCustomerNumber());
		param.setMerchantName(requestDTO.getCustomerName());
		param.setProductName(requestDTO.getProductName());
		param.setRequestNo(requestDTO.getOutTradeNo());
		param.setMemberType(requestDTO.getMemberType());
		param.setMemberNO(requestDTO.getMemberNO());
		param.setOrderAmount(requestDTO.getAmount());
		param.setSignCardIdType(requestDTO.getSignCardIdType());
		param.setSignCardId(requestDTO.getSignCardId());
		if (requestDTO.getUserFee() != null) {
			param.setPayerFee(requestDTO.getUserFee());
		} else {
			param.setPayerFee(new BigDecimal(0));
		}
		param.setCardNo(requestDTO.getCardNo());
		param.setGoodsInfo(requestDTO.getGoodsInfo());
		param.setIndustryCode(requestDTO.getIndustryCode());
		param.setRequestSystem(ConstantUtils.NC_PAY_REQUEST_SYS);
		param.setDealUniqueSerialNo(requestDTO.getDealUniqueSerialNo());
		param.setRetailProductCode(record.getRetailProductCode());
		param.setBasicProductCode(record.getBasicProductCode());
		return param;
	}

	@Override
	public CflSmsSendResponseDTO cflSendSms(String ncpayPaymentNo, NcPayCflSmsRequestDTO requestDTO) {
		CflSmsSendResponseDTO cflSmsSendResponseDTO = null;
		try {
			CflSmsSendRequestDTO cflSmsSendRequestDTO = new CflSmsSendRequestDTO();
			cflSmsSendRequestDTO.setPayOrderId(ncpayPaymentNo);
			cflSmsSendRequestDTO.setMobileNo(requestDTO.getMobileNo());
			cflSmsSendResponseDTO = ncpayCflFacade.sendMessage(cflSmsSendRequestDTO);
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003012);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (cflSmsSendResponseDTO == null) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return cflSmsSendResponseDTO;
	}


	@Override
	public CflOpenAndPayResponseDTO cflOpenAndPay(NcPayCflOpenRequestDTO requestDTO, PayRecord record) {
		CflOpenAndPayResponseDTO responseDTO = null;
		try {
			responseDTO = ncpayCflFacade.openAndPay(buildCflOpenAndPayParam(requestDTO, record));
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003010);
		} catch (Exception e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (null == responseDTO) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return responseDTO;
	}

	private CflOpenAndPayRequestDTO buildCflOpenAndPayParam(NcPayCflOpenRequestDTO requestDTO, PayRecord record) {
		CflOpenAndPayRequestDTO param = new CflOpenAndPayRequestDTO();
		param.setCflCount(record.getCflCount());
		param.setCflRate(record.getCflRate());
		param.setMerchantFeeSubsidy(record.getMerchantFeeSubsidy());
		param.setMerchantAmountSubsidy(record.getMerchantAmountSubsidy());
		param.setBizType(requestDTO.getBizType());
		param.setBizOrderNo(record.getRecordNo());
		param.setOrderType(OrderTypeEnum.BK_CFL);
		param.setBizOrderDate(record.getCreateTime());
		param.setMerchantNo(requestDTO.getCustomerNumber());
		param.setMerchantName(requestDTO.getCustomerName());
		param.setProductName(requestDTO.getProductName());
		param.setRequestNo(requestDTO.getOutTradeNo());
		param.setMemberType(requestDTO.getMemberType());
		param.setMemberNO(requestDTO.getMemberNO());
		param.setOrderAmount(requestDTO.getAmount());
		param.setSignCardIdType(requestDTO.getSignCardIdType());
		param.setSignCardId(requestDTO.getSignCardId());
		if (requestDTO.getUserFee() != null) {
			param.setPayerFee(requestDTO.getUserFee());
		} else {
			param.setPayerFee(new BigDecimal(0));
		}
		param.setCardNo(requestDTO.getCardNo());
		param.setGoodsInfo(requestDTO.getGoodsInfo());
		param.setIndustryCode(requestDTO.getIndustryCode());
		param.setRequestSystem(ConstantUtils.NC_PAY_REQUEST_SYS);
		param.setDealUniqueSerialNo(requestDTO.getDealUniqueSerialNo());
		param.setRetailProductCode(record.getRetailProductCode());
		param.setBasicProductCode(record.getBasicProductCode());
		param.setPageCallBack(requestDTO.getPageCallBack());
		param.setCardNo(requestDTO.getCardNo());
		return param;

	}

	@Override
	public CflConfirmPayResponseDTO cflConfirmPay(NcPayCflConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
		CflConfirmPayResponseDTO cflConfirmPayResponseDTO = null;
		try {
			// 调用ncpay异步接口，不更新支付订单状态
			cflConfirmPayResponseDTO = ncpayCflFacade.confirmPay(buildCflConfirmPayParam(requestDTO, ncpayPaymentNo));

		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003013);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return cflConfirmPayResponseDTO;
	}

	private CflConfirmPayRequestDTO buildCflConfirmPayParam(NcPayCflConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
		CflConfirmPayRequestDTO cflConfirmPayRequestDTO = new CflConfirmPayRequestDTO();
		cflConfirmPayRequestDTO.setPayOrderId(ncpayPaymentNo);
		cflConfirmPayRequestDTO.setSmsCode(requestDTO.getSmsCode());
		return cflConfirmPayRequestDTO;
	}


	/**
	 * 预授权下单
	 */
	@Override
	public PayQueryResponseDTO cflSynConfirmPay(NcPayCflSynConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
		PayQueryResponseDTO payQueryResponseDTO = null;
		try {
			payQueryResponseDTO = ncpayCflFacade.synConfirmPay(bulidSynConfirmPayDTO(requestDTO, ncpayPaymentNo));
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003014);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return payQueryResponseDTO;
	}

	private CflSynConfirmPayRequestDTO bulidSynConfirmPayDTO(NcPayCflSynConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
		CflSynConfirmPayRequestDTO cflSynConfirmPayRequestDTO = new CflSynConfirmPayRequestDTO();
		cflSynConfirmPayRequestDTO.setPayOrderId(ncpayPaymentNo);
		cflSynConfirmPayRequestDTO.setSmsCode(requestDTO.getSmsCode());
		return cflSynConfirmPayRequestDTO;
	}


	@Override
	public PayPreAuthRespDTO ncPreAuthRequest(NcPayOrderRequestDTO requestDTO, PayRecord record) {
		PayPreAuthRespDTO respDTO = null;
		try {
			respDTO = ncPayPreAuthFacade.createPreAuthPayment(bulidRequestPaymentParam(requestDTO, record, OrderTypeEnum.PREAUTH_RE));
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003021);
		} catch (IllegalArgumentException e) {
			handleIllegalException(e);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (null == respDTO) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return respDTO;
	}


	@Override
	public PayPreAuthConfirmResponseDTO ncPreAuthComfirm(NcPayConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
		PayPreAuthConfirmResponseDTO responseDTO = null;
		try {
			PayConfirmRequestDTO requestParam = new PayConfirmRequestDTO();
			requestParam.setPayOrderId(ncpayPaymentNo);
			requestParam.setTmpCardId(requestDTO.getTmpCardId());
			requestParam.setSmsCode(requestDTO.getSmsCode());
			requestParam.setExtParam(requestDTO.getExtParam());
			responseDTO = ncPayPreAuthFacade.preAuthConfirm(requestParam);
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003022);
		} catch (IllegalArgumentException e) {
			handleIllegalException(e);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (null == responseDTO) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return responseDTO;
	}

	@Override
	public PayPreAuthCancelResponseDTO ncPreAuthCancel(PreAuthCancelRequestDTO requestDTO, String preAuthPaymentNo,
													   PayRecord record) {
		PayPreAuthCancelResponseDTO responseDTO = null;
		try {
			PayPreAuthCancelRequestDTO requestParam = new PayPreAuthCancelRequestDTO();
			requestParam.setPreAuthPaymentNo(preAuthPaymentNo);
			requestParam.setBizType(requestDTO.getBizType());
			requestParam.setBizOrderNum(record.getRecordNo());
			requestParam.setBizOrderTime(record.getCreateTime());
			requestParam.setRequestNo(requestDTO.getOutTradeNo());
			requestParam.setCancelType(requestDTO.getCancelType());
			requestParam.setPayerCardNo(requestDTO.getPayerCardNo());
			responseDTO = ncPayPreAuthFacade.preAuthCancel(requestParam);
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003023);
		} catch (IllegalArgumentException e) {
			handleIllegalException(e);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (null == responseDTO) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return responseDTO;
	}

	@Override
	public PayPreAuthCompleteResponseDTO ncPreAuthComplete(PreAuthCompleteRequestDTO requestDTO, String preAuthPaymentNo, PayRecord record) {
		PayPreAuthCompleteResponseDTO responseDTO = null;
		try {
			PayPreAuthCompleteRequestDTO requestParam = new PayPreAuthCompleteRequestDTO();
			requestParam.setPreAuthPaymentNo(preAuthPaymentNo);
			requestParam.setAmount(requestDTO.getAmount());
			requestParam.setRequestNo(requestDTO.getOutTradeNo());
			requestParam.setBizType(requestDTO.getBizType());
			requestParam.setBizOrderNum(record.getRecordNo());
			requestParam.setBizOrderTime(new Date());
			responseDTO = ncPayPreAuthFacade.preAuthComplete(requestParam);
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003024);
		} catch (IllegalArgumentException e) {
			handleIllegalException(e);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (null == responseDTO) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return responseDTO;
	}

	/**
	 * pp在处理预授权冲正时，调用ncpay的撤销接口
	 * @return
	 */
	@Override
	public PayPreAuthCancelResponseDTO ncPreAuthReverseCancel(PayPreAuthCancelRequestDTO requestDTO) {
		PayPreAuthCancelResponseDTO responseDTO = null;
		try {
			responseDTO = ncPayPreAuthFacade.preAuthCancel(requestDTO);
		} catch (PaymentException e) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
					ErrorCode.P9003023);
		} catch (IllegalArgumentException e) {
			handleIllegalException(e);
		} catch (Throwable e) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		if (null == responseDTO) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		return responseDTO;
	}


	private void handleIllegalException(IllegalArgumentException e) {
		String errorCode = ErrorCode.P9001001;
		String errorMsg;
		if(StringUtils.isNotBlank(e.getMessage()) && e.getMessage().length() < 20) {
			errorMsg = e.getMessage();
		} else {
			errorMsg = "ncpay参数不合法";
		}
		throw new PayBizException(errorCode, errorMsg);
	}

	/**
	 * 担保分期预路由
	 */
    @Override
    public GuaranteeCflPrePayResponseDTO guaranteeCflPrePay(NcGuaranteeCflPrePayRequestDTO param) {
         
         GuaranteeCflPrePayResponseDTO response = null;
         try {
             response = guaranteeCflFacade.guaranteeCflPrePay(buildGuaranteeCflPrePayRequestDTO(param));
         } catch (PaymentException e) {
             throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
                     ErrorCode.P8001001);
         } catch (Exception e) {
             throw new PayBizException(ErrorCode.P9001000);
         }
         if (null == response) {
             throw new PayBizException(ErrorCode.P9001000);
         }
         return response;
    }
    
    private GuaranteeCflPrePayRequestDTO buildGuaranteeCflPrePayRequestDTO(NcGuaranteeCflPrePayRequestDTO param){
        GuaranteeCflPrePayRequestDTO request = new GuaranteeCflPrePayRequestDTO();
        request.setDealUniqueSerialNo(param.getDealUniqueSerialNo());    //唯一流水号
        request.setOrderSystem(param.getOrderSystem());                  //交易系统
        request.setOrderNo(param.getOrderNo());                          //交易系统订单号
        request.setRequestSystem(ConstantUtils.NC_PAY_REQUEST_SYS);      //请求系统
        request.setMerchantName(param.getCustomerName());                //商户名称
        request.setMerchantNo(param.getCustomerNumber());                //商户编号
        request.setOutTradeNo(param.getOutTradeNo());                    //商户订单号
        request.setProductName(param.getProductName());                  //商品名称
        request.setOrderType(OrderTypeEnum.SALE);                        //订单类型
        request.setCardNo(param.getCardNo());                  //卡号
        request.setCardType(param.getCardType());                        //卡类型
        request.setBankCode(param.getBankCode());                        //银行编码
        request.setAccountType(param.getAccountType());                  //银行的账户类型（对公、对私）
        request.setCflCount(param.getCflCount());                        //分期期数
        request.setAcceptPaySMS(param.getAcceptPaySMS().toString());     //是否接受签约短验
        request.setAcceptSignSMS(param.getAcceptSignSMS().toString());   //是否接受支付短验
        request.setOrderAmount(param.getAmount());                       //订单金额
        request.setPayerFee(param.getUserFee());                         //付款方手续费
        request.setSceneType(param.getSceneType());                      //场景类型（PC、MB（移动端）、API）
        request.setIndustryCode(param.getIndustryCode());                //商品类别码
        request.setBasicProductCode(param.getBasicProductCode());        //基础产品码
        request.setRetailProductCode(param.getRetailProductCode());      //零售产品码
//      ncPayRequest.setToolInfo(requestDTO.getToolsInfo());             //支付工具风控扩展信息
//      ncPayRequest.setGoodsInfo(requestDTO.getGoodsInfo());            //业务方风控扩展信息
        return request;
    }

    /**
     * 担保分期下单
     */
    @Override
    public GuaranteeCflPayResponseDTO guaranteeCflPay(NcGuaranteeCflPayRequestDTO param, PayRecord record) {
       
        
        GuaranteeCflPayResponseDTO response = null;
        try {
            response = guaranteeCflFacade.guaranteeCflPay(buildGuaranteeCflPayRequestDTO(param, record));
        } catch (PaymentException e) {
            throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
                    ErrorCode.P8001002);
        } catch (Exception e) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        if (null == response) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        return response;
    }
    
    private GuaranteeCflPayRequestDTO buildGuaranteeCflPayRequestDTO(NcGuaranteeCflPayRequestDTO param, PayRecord record){
        GuaranteeCflPayRequestDTO request = new GuaranteeCflPayRequestDTO();
        request.setPreRouteId(param.getPreRouteId());
        request.setBizType(param.getBizType());
        request.setBizOrderNum(record.getRecordNo());
        request.setBizOrderDate(record.getCreateTime().getTime());
      //request.setCflCount(param.getCflCount());
        request.setDealUniqueSerialNo(param.getDealUniqueSerialNo());
        request.setMerchantName(param.getCustomerName());
        request.setMerchantNo(param.getCustomerNumber());
        request.setOutTradeNo(param.getOutTradeNo());
        request.setRequestSystem(ConstantUtils.NC_PAY_REQUEST_SYS);
        request.setOrderSystem(param.getOrderSystem());
        request.setOrderNo(param.getOrderNo());
        request.setOrderAmount(param.getAmount());
        request.setPayerFee(param.getUserFee());
        request.setOrderType(OrderTypeEnum.GUAR_CFL);
        request.setMerchantFeeSubsidy(param.getMerchantFeeSubsidy());
        request.setCardNo(param.getCardNo());
        request.setIdNo(param.getIdNo());
        request.setIdType(param.getIdType());
        request.setMobileNo(param.getMobileNo());
        request.setAccountName(param.getAccountName());
        request.setCreditCardCvv(param.getCreditCardCvv());
        request.setCreditCardExpiryDate(param.getCreditCardExpiryDate());
        request.setBankPwd(param.getBankPwd());
        request.setCardType(param.getCardType());
        request.setAccountType(param.getAccountType());
        request.setBankCode(param.getBankCode());
        request.setProductName(param.getProductName());
        request.setSceneType(param.getSceneType());
        request.setIndustryCode(param.getIndustryCode());
        request.setToolInfo(param.getToolsInfo());
        request.setGoodsInfo(param.getGoodsInfo());
        request.setRiskProduction(param.getRiskProduction());
        request.setPageRedirectUrl(param.getPageRedirectUrl());
        request.setExtParamMap(param.getExtParamMap());
        request.setRetailProductCode(param.getRetailProductCode());
        request.setBasicProductCode(param.getBasicProductCode());
//      ncPayRequest.setAcceptPaySMS(param.);
//      ncPayRequest.setAcceptSignSMS(acceptSignSMS);
//      ncPayRequest.setRegionCode(regionCode);
        return request;
    }

    /**
     * 担保分期订单查询
     */
    @Override
    public GuaranteeCflQueryResponseDTO queryGuaranteeCflOrder(String paymentNo) {
        GuaranteeCflQueryResponseDTO respDTO = null;
        GuaranteeCflQueryRequestDTO request = new GuaranteeCflQueryRequestDTO();
        request.setPaymentNo(paymentNo);
        try {
            respDTO = guaranteeCflFacade.guaranteeCflOrderQuery(request);
        } catch (Exception e) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        if (null == respDTO) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        return respDTO;
    }

}
