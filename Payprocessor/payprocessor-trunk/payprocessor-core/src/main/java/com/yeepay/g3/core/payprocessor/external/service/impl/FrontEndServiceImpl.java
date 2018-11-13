/**
 * 
 */
package com.yeepay.g3.core.payprocessor.external.service.impl;

import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import com.yeepay.g3.facade.payprocessor.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.external.service.FrontEndService;
import com.yeepay.g3.core.payprocessor.service.FeResultProccess;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentOrderRequestDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentOrderResponseDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentQueryRequestDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.facade.frontend.dto.NetOnlinePayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.NetOnlinePayResponseDTO;
import com.yeepay.g3.facade.frontend.dto.NetPayQueryRequestDTO;
import com.yeepay.g3.facade.frontend.dto.NetPayResultMessage;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.CflOrderType;
import com.yeepay.g3.facade.frontend.enumtype.NetOrderType;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.payprocessor.enumtype.PayCardType;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

import java.math.BigDecimal;

/**
 * @author chonos 对接FE接口,包括下单、查单等
 */
@Service("frontEndService")
public class FrontEndServiceImpl extends AbstractService implements FrontEndService {

	@Autowired
	private FeResultProccess feResultProccess;

	@Autowired
	private CombAbstractService combAbstractService;

	public static final Logger logger = PayLoggerFactory.getLogger(FrontEndServiceImpl.class);

	@Override
	public OpenPayResponseDTO openPay(OpenPayRequestDTO requestDTO, PayRecord payRecord) {
		try {
			PayRequestDTO payDTO = buildOpenPayRequestDTO(requestDTO, payRecord);
			PayResponseDTO responseDTO = frontendPayFacade.openPay(payDTO);
			return buildOpenPayResponseDTO(responseDTO, payRecord.getRecordNo());
		} catch (PayBizException e) {
			throw e;
		} catch (Throwable th) {
			logger.error("[请求FE下单失败]", th);
			throw new PayBizException(ErrorCode.P9003002);
		}
	}

	@Override
	public NetPayResponseDTO onlinePay(NetPayRequestDTO requestDTO, PayRecord payRecord) {
		try {
			NetOnlinePayRequestDTO payDTO = buildOnlinePayRequestDTO(requestDTO, payRecord);
			NetOnlinePayResponseDTO responseDTO = netPayFacade.onlinePay(payDTO);
			return buildOnlinePayResponse(responseDTO, payRecord.getRecordNo());
		} catch (PayBizException e) {
			throw e;
		}catch (Throwable th) {
			logger.error("请求FE下单失败", th);
			throw new PayBizException(ErrorCode.P9003003);
		}
	}

	@Override
	public FrontendQueryResponseDTO queryPaymentOrder(String recordNo, String platformType) {
		try {
			FrontendQueryRequestDTO queryRequestDTO = buildFeQueryRequest(recordNo, platformType);
			return frontendQueryFacade.queryOrderInfo(queryRequestDTO);
		} catch (Throwable th) {
			logger.error("请求FE查单失败", th);
			throw new PayBizException(ErrorCode.P9003006);
		}
	}

	@Override
	public OpenPrePayResponseDTO openPrePay(OpenPrePayRequestDTO requestDTO) {
		try {
			PrePayRequestDTO prePayRequestDTO = buildOpenPrePayRequestDTO(requestDTO);
			PrePayResponseDTO responseDTO = frontendPayFacade.prePayJsapi(prePayRequestDTO);
			return buildOpenPrePayResponse(responseDTO);
		} catch (PayBizException e) {
			throw e;
		} catch (Throwable th) {
			logger.error("请求FE预路由失败", th);
			throw new PayBizException(ErrorCode.P9003008);
		}
	}

	@Override
	public CflPayResponseDTO cflPay(CflPayRequestDTO requestDTO, PayRecord payRecord) {
		try {
			InstallmentOrderRequestDTO installmentRequestDTO = buildInstallmentRequestDTO(requestDTO, payRecord);
			InstallmentOrderResponseDTO responseDTO = installmentFacade.payWeb(installmentRequestDTO);
			return buildcflPayResponse(responseDTO, payRecord.getRecordNo());
		} catch (PayBizException e) {
			throw e;
		} catch (Throwable th) {
			logger.error("请求FE分期支付失败", th);
			throw new PayBizException(ErrorCode.P9003009);
		}
	}

	@Override
	public PassiveScanPayResponseDTO passiveScanPay(PassiveScanPayRequestDTO requestDTO, PayRecord payRecord) {
		try {
			PayRequestDTO payDTO = buildOpenPayRequestDTO(requestDTO, payRecord);
			PayResponseDTO responseDTO = frontendPayFacade.openPay(payDTO);
			return buildPassiveScanPayResponseDTO(responseDTO, payRecord.getRecordNo());
		} catch (PayBizException e) {
			throw e;
		} catch (Throwable th) {
			logger.error("[请求FE被扫支付失败]", th);
			throw new PayBizException(ErrorCode.P9003002);
		}
	}

	/**
	 * 组装开放支付请求参数
	 *
	 * @param orgDTO
	 * @return
	 */
	private PayRequestDTO buildOpenPayRequestDTO(BasicRequestDTO orgDTO, PayRecord payRecord) {
		
		PayRequestDTO payDTO = new PayRequestDTO();
		BeanCopier copier = null;
		if (orgDTO instanceof OpenPayRequestDTO) {
			copier = BeanCopier.create(OpenPayRequestDTO.class, PayRequestDTO.class, false);
			copier.copy(orgDTO, payDTO, null);
		} else if (orgDTO instanceof PassiveScanPayRequestDTO) {
			copier = BeanCopier.create(PassiveScanPayRequestDTO.class, PayRequestDTO.class, false);
			copier.copy(orgDTO, payDTO, null);
		}
		payDTO.setRequestId(payRecord.getRecordNo());

		// 组合支付，第一支付单的金额需要减去第二支付单金额
		if(payRecord.isCombinedPay()) {
			if(orgDTO.getUserFee() != null && orgDTO.getAmount() != null){
				payDTO.setTotalAmount(payRecord.getFirstPayAmount().add(orgDTO.getUserFee()));
			}else{
				payDTO.setTotalAmount(payRecord.getFirstPayAmount());
			}
		}else {
			if(orgDTO.getUserFee() != null && orgDTO.getAmount() != null){
				payDTO.setTotalAmount(orgDTO.getAmount().add(orgDTO.getUserFee()));
			}else{
				payDTO.setTotalAmount(orgDTO.getAmount());
			}
		}
		payDTO.setRequestSystem(ConstantUtils.SYS_NAME);
		payDTO.setGoodsDescription(orgDTO.getProductName());
		payDTO.setOrderType(OrderType.getOrderType(orgDTO.getPayOrderType().name()));
		payDTO.setPaymentProduct(orgDTO.getPayProduct());
		payDTO.setOrderSystem(orgDTO.getRiskProduction());

		return payDTO;
	}

	/**
	 * 组装开放支付返回结果
	 *
	 * @param feResponseDTO
	 * @return
	 */
	private OpenPayResponseDTO buildOpenPayResponseDTO(PayResponseDTO feResponseDTO, String recordNo) {
		if (StringUtils.isNotBlank(feResponseDTO.getResponseCode())) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.FRONTEND.getSysCode(), feResponseDTO.getResponseCode(),
					feResponseDTO.getResponseMsg(), ErrorCode.P9003002);
		}
		OpenPayResponseDTO responseDTO = new OpenPayResponseDTO();
		responseDTO.setPrepayCode(feResponseDTO.getPrepayCode());
		responseDTO.setRecordNo(recordNo);
		return responseDTO;
	}

	/**
	 * 组装网银下单请求参数
	 * 
	 * @param requestDTO
	 * @return
	 */
	private NetOnlinePayRequestDTO buildOnlinePayRequestDTO(NetPayRequestDTO requestDTO, PayRecord payRecord) {
		NetOnlinePayRequestDTO payDTO = new NetOnlinePayRequestDTO();
		BeanCopier copier = BeanCopier.create(NetPayRequestDTO.class, NetOnlinePayRequestDTO.class, false);
		copier.copy(requestDTO, payDTO, null);
		payDTO.setRequestSystem(ConstantUtils.SYS_NAME);
		payDTO.setRequestId(payRecord.getRecordNo());
		payDTO.setNetOrderType(NetOrderType.SALE);

		if(payRecord.isCombinedPay()) {
			if(requestDTO.getUserFee() != null && requestDTO.getAmount() != null){
				payDTO.setTotalAmount(payRecord.getFirstPayAmount().add(requestDTO.getUserFee()));
			}else{
				payDTO.setTotalAmount(payRecord.getFirstPayAmount());
			}
		}else {
			if(requestDTO.getUserFee() != null && requestDTO.getAmount() != null){
				payDTO.setTotalAmount(requestDTO.getAmount().add(requestDTO.getUserFee()));
			}else{
				payDTO.setTotalAmount(requestDTO.getAmount());
			}
		}

		payDTO.setGoodsDescription(requestDTO.getProductName());
		payDTO.setPaymentProduct(requestDTO.getPayProduct());
		payDTO.setOrderSystem(requestDTO.getRiskProduction());
		return payDTO;
	}

	/**
	 * 组装网银下单返回结果
	 * 
	 * @param feResponse
	 * @return
	 */
	private NetPayResponseDTO buildOnlinePayResponse(NetOnlinePayResponseDTO feResponse, String recordNo) {
		if (StringUtils.isNotBlank(feResponse.getResponseCode())) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.FRONTEND.getSysCode(), feResponse.getResponseCode(),
					feResponse.getResponseMsg(), ErrorCode.P9003003);
		}
		NetPayResponseDTO payResponse = new NetPayResponseDTO();
		payResponse.setParam(feResponse.getParam());
		payResponse.setPayUrl(feResponse.getPayUrl());
		payResponse.setCharset(feResponse.getCharset());
		payResponse.setMethod(feResponse.getMethod());
		payResponse.setRecordNo(recordNo);
		payResponse.setBankPayUrl(feResponse.getBankPayUrl());
		return payResponse;
	}

	/**
	 * 组装查询结果
	 * 
	 * @param recordNo
	 * @param platformType
	 * @return
	 */
	private FrontendQueryRequestDTO buildFeQueryRequest(String recordNo, String platformType) {
		FrontendQueryRequestDTO requestDTO = new FrontendQueryRequestDTO();
		requestDTO.setPlatformType(PlatformType.getPlatformType(platformType));
		requestDTO.setRequestId(recordNo);
		requestDTO.setRequestSystem(ConstantUtils.SYS_NAME);
		return requestDTO;
	}

	private PrePayRequestDTO buildOpenPrePayRequestDTO(OpenPrePayRequestDTO requestDTO) {
		PrePayRequestDTO prePayDTO = new PrePayRequestDTO();
		BeanCopier copier = BeanCopier.create(OpenPrePayRequestDTO.class, PrePayRequestDTO.class, false);
		copier.copy(requestDTO, prePayDTO, null);
		
		
		return prePayDTO;
	}

	private OpenPrePayResponseDTO buildOpenPrePayResponse(PrePayResponseDTO feResponseDTO) {
		if (StringUtils.isNotBlank(feResponseDTO.getResponseCode())) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.FRONTEND.getSysCode(), feResponseDTO.getResponseCode(),
					feResponseDTO.getResponseMsg(), ErrorCode.P9003008);
		}
		OpenPrePayResponseDTO responseDTO = new OpenPrePayResponseDTO();
		responseDTO.setAppId(feResponseDTO.getAppId());
		responseDTO.setCustomerNumber(feResponseDTO.getCustomerNumber());
		responseDTO.setTotalAmount(feResponseDTO.getTotalAmount());
		responseDTO.setDealUniqueSerialNo(feResponseDTO.getDealUniqueSerialNo());
		responseDTO.setAppSecret(feResponseDTO.getAppSecret());
		responseDTO.setPayInterface(feResponseDTO.getPayInterface());
		responseDTO.setBankTotalCost(feResponseDTO.getBankTotalCost());
		responseDTO.setReportMerchantNo(feResponseDTO.getReportMerchantNo());
		responseDTO.setDealStatus(feResponseDTO.getDealStatus());
		responseDTO.setSceneTypeExt(feResponseDTO.getSceneTypeExt());
		return responseDTO;
	}

	private InstallmentOrderRequestDTO buildInstallmentRequestDTO(CflPayRequestDTO requestDTO, PayRecord payRecord) {
		InstallmentOrderRequestDTO CflPay = new InstallmentOrderRequestDTO();
		BeanCopier copier = BeanCopier.create(CflPayRequestDTO.class, InstallmentOrderRequestDTO.class, false);
		copier.copy(requestDTO, CflPay, null);
		CflPay.setPaymentProduct(requestDTO.getPayProduct());
		CflPay.setRequestId(payRecord.getRecordNo());
		CflPay.setGoodsDescription(requestDTO.getProductName());

		// 组合支付，第一支付单不是订单金额
		if(payRecord.isCombinedPay()) {
			if(requestDTO.getAmount() != null && requestDTO.getUserFee() != null){
				CflPay.setTotalAmount(payRecord.getFirstPayAmount().add(requestDTO.getUserFee()));
			}else{
				CflPay.setTotalAmount(payRecord.getFirstPayAmount());
			}
		}else {
			if(requestDTO.getAmount() != null && requestDTO.getUserFee() != null){
				CflPay.setTotalAmount(requestDTO.getAmount().add(requestDTO.getUserFee()));
			}else{
				CflPay.setTotalAmount(requestDTO.getAmount());
			}
		}

		CflPay.setCflOrderType(CflOrderType.valueOf(requestDTO.getPayOrderType().name()));
		CflPay.setRequestSystem(ConstantUtils.SYS_NAME);
		CflPay.setOrderSystem(requestDTO.getRiskProduction());
		return CflPay;
	}

	private CflPayResponseDTO buildcflPayResponse(InstallmentOrderResponseDTO feCflResponse, String recordNo) {
		if (StringUtils.isNotBlank(feCflResponse.getResponseCode())) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.FRONTEND.getSysCode(), feCflResponse.getResponseCode(),
					feCflResponse.getResponseMsg(), ErrorCode.P9003009);
		}
		CflPayResponseDTO response = new CflPayResponseDTO();
		response.setPayUrl(feCflResponse.getPayUrl());
		response.setBankOrderNo(feCflResponse.getOrderNo());
		response.setRecordNo(recordNo);
		return response;
	}

	@Override
	public NetPayResultMessage queryNetPayPaymentOrder(String recordNo) {

		try {
			NetPayQueryRequestDTO requestDTO = new NetPayQueryRequestDTO();
			requestDTO.setRequestId(recordNo);
			requestDTO.setRequestSystem(ConstantUtils.SYS_NAME);
			return netPayQueryFacade.queryOrderInfo(requestDTO);
		} catch (Throwable th) {
			logger.error("请求FE NetPay查单失败", th);
			throw new PayBizException(ErrorCode.P9003006);
		}
	}

	@Override
	public InstallmentResultMessage queryCflPayPaymentOrder(String recordNo) {
		try {
			InstallmentQueryRequestDTO requestDTO = new InstallmentQueryRequestDTO();
			requestDTO.setRequestId(recordNo);
			requestDTO.setRequestSystem(ConstantUtils.SYS_NAME);
			return installmentQueryFacade.queryOrderInfo(requestDTO);
		} catch (Throwable th) {
			logger.error("请求FE NetPay查单失败", th);
			throw new PayBizException(ErrorCode.P9003006);
		}
	}

	/**
	 * @param feResponseDTO
	 * @param recordNo
	 * @return
	 */
	private PassiveScanPayResponseDTO buildPassiveScanPayResponseDTO(PayResponseDTO feResponseDTO, String recordNo) {
		PassiveScanPayResponseDTO responseDTO = new PassiveScanPayResponseDTO();
		responseDTO.setStatus(TrxStatusEnum.DOING.name());
		responseDTO.setRecordNo(recordNo);
		//added by zengzhi.han 20181016 接收fe返回的额外相应参数 比如是被扫支付是否需要输入密码
		responseDTO.setExtParam(feResponseDTO.getExtParam());
		if (feResponseDTO != null) {
			if (StringUtils.isNotBlank(feResponseDTO.getResponseCode())) {
				if (StringUtils.isNotBlank(feResponseDTO.getResponseCode())) {
					throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.FRONTEND.getSysCode(), feResponseDTO.getResponseCode(),
							feResponseDTO.getResponseMsg(), ErrorCode.P9003009);
				}
			}
			if (feResponseDTO.getPayStatus() == PayStatusEnum.SUCCESS) {
				PayRecord payRecord = payRecordDao.selectByPrimaryKey(recordNo);
				if(payRecord == null) {
					logger.error("支付订单不存在，recordNo=" + recordNo);
					throw new PayBizException(ErrorCode.P9002006);
				}
				// 组合支付
				if(payRecord.isCombinedPay()) {
					handleComResult(responseDTO, feResponseDTO, payRecord);
				}else {
					handleResult(responseDTO, feResponseDTO, payRecord);
				}
			}			
		}
		return responseDTO;
	}
	
	private void handleResult(PassiveScanPayResponseDTO responseDTO, PayResponseDTO feResponseDTO,
							  PayRecord payRecord) {
		// 备注：异步结果先于同步结果更新订单状态，同步会抛子单已成功的异常，这个时候需要捕捉异常，重新查询订单状态，如果成功，吃掉异常
		try{
			feResultProccess.processForFePayResponse(feResponseDTO, payRecord);
		}catch (PayBizException payBizException) {
			if(ErrorCode.P9002002.equals(payBizException.getDefineCode())) {
				payRecord = payRecordDao.selectByPrimaryKey(payRecord.getRecordNo());
				logger.info("支付子单已成功，再次查询支付记录：" + payRecord);
				// 只有成功的订单，才吃掉异常
				if(!TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
					throw payBizException;
				}
			}else {
				throw payBizException;
			}
		}	
		responseDTO.setBankId(payRecord.getBankId());
		responseDTO.setBankOrderNo(payRecord.getBankOrderNo());
		responseDTO.setBankTrxId(payRecord.getBankTrxId());
		responseDTO.setCardType(PayCardType.getPayCardType(payRecord.getCardType()));
		responseDTO.setChannelId(payRecord.getFrpCode());
		responseDTO.setCost(payRecord.getCost());
		responseDTO.setStatus(payRecord.getStatus());
		//added by zengzhi.han 20181024 增加优惠券信息
		responseDTO.setCashFee(feResponseDTO.getCashFee());
		responseDTO.setSettlementFee(feResponseDTO.getSettlementFee());
		responseDTO.setPromotionInfoDTOS(feResponseDTO.getPromotionInfoDTOS());
	}


	/**
	 * 组合支付的处理方法
	 * @param responseDTO
	 * @param feResponseDTO
	 * @param payRecord
	 */
	private void handleComResult(PassiveScanPayResponseDTO responseDTO, PayResponseDTO feResponseDTO,
								 PayRecord payRecord) {
		// 备注：异步结果先于同步结果更新订单状态，同步会抛子单已成功的异常，这个时候需要捕捉异常，重新查询订单状态，如果成功，吃掉异常
		try{
			feResultProccess.processForFePayResponseComb(feResponseDTO, payRecord);
		}catch (PayBizException payBizException) {
			if(ErrorCode.P9002002.equals(payBizException.getDefineCode())) {
				payRecord = payRecordDao.selectByPrimaryKey(payRecord.getRecordNo());
				logger.info("支付子单已成功，再次查询支付记录：" + payRecord);
				// 只有成功的订单，才吃掉异常
				if(!TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
					throw payBizException;
				}
			}else {
				throw payBizException;
			}
		}
		responseDTO.setBankId(payRecord.getBankId());
		responseDTO.setBankOrderNo(payRecord.getBankOrderNo());
		responseDTO.setBankTrxId(payRecord.getBankTrxId());
		responseDTO.setCardType(PayCardType.getPayCardType(payRecord.getCardType()));
		responseDTO.setChannelId(payRecord.getFrpCode());
		responseDTO.setCost(payRecord.getCost());
		responseDTO.setStatus(payRecord.getStatus());
		// 返回前查订单状态
		CombResponseDTO combResponseDTO = combAbstractService.bulidCombResponse(payRecord.getRecordNo());
		responseDTO.setCombResponseDTO(combResponseDTO);
		responseDTO.setFirstPayAmount(payRecord.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
	}

}
