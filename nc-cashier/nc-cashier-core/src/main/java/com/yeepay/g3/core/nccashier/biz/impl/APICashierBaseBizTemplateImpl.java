package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APICashierBaseBizTemplate;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.core.nccashier.vo.VerifyProductOpenRequestParam;
import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.APICashierPayResultEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Service("apiCashierBaseBizTemplate")
public abstract class APICashierBaseBizTemplateImpl implements APICashierBaseBizTemplate {

	private static Logger logger = LoggerFactory.getLogger(APICashierBaseBizTemplateImpl.class);
	@Resource
	protected NewOrderHandleService newOrderHandleService;

	@Resource
	protected MerchantVerificationService merchantVerificationService;

	@Override
	public void handle(APIBasicRequestDTO basicRequestDTO, APIBasicResponseDTO responseDTO, ProductLevel productLevel) {
		// 参数校验
		paramValidate(basicRequestDTO);
		// 反查订单
		OrderDetailInfoModel orderInfo = queryOrder(basicRequestDTO);
		// 商户开通校验
		MerchantInNetConfigResult merchantConfigInfo = null;
		if (needVerifyProductOpen()) {
			VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
			requestParam.setMerchantNo(orderInfo.getMerchantAccountCode());
			requestParam.setProductLevel(productLevel);
			requestParam.setTransactionType(orderInfo.getTransactionType());
			merchantConfigInfo = merchantVerificationService.verifyMerchantAuthority(requestParam);
		}
		// 业务校验
		CombinedPaymentDTO combinedPaymentDTO = null;
		if (needBizValidate()) {
			combinedPaymentDTO = bizValidate(basicRequestDTO, orderInfo, merchantConfigInfo);
		}
		// 处理paymentRequest和paymentRecord
		combinedPaymentDTO = handleRequestAndRecord(combinedPaymentDTO, orderInfo, merchantConfigInfo, productLevel, basicRequestDTO, responseDTO);
		// 调用pp的服务
		Object result = callPPService(combinedPaymentDTO, basicRequestDTO, responseDTO);
		// 构造返回值
		buildResponse(responseDTO, basicRequestDTO, combinedPaymentDTO, result);
	}
	
	public abstract boolean needVerifyProductOpen();
	
	public abstract boolean needBizValidate();

	@Override
	public APIBasicResponseDTO errorResult(Throwable t, APIBasicRequestDTO basicRequestDTO,
			APIBasicResponseDTO responseDTO) {
		logger.warn("token=" + ((basicRequestDTO == null) ? null : basicRequestDTO.getToken()) + "订单异常，exception=", t);
		buildResponse(responseDTO, basicRequestDTO, null, null);
		errorResult(t, responseDTO);
		return responseDTO;
	}

	@Override
	public void errorResult(Throwable t, APIBasicResponseDTO responseDTO){
		boolean yeepayBizException = (t instanceof YeepayBizException);
		String errorCode = yeepayBizException ? ((YeepayBizException) t).getDefineCode()
				: Errors.SYSTEM_EXCEPTION.getCode();
		String errorMsg = yeepayBizException ? ((YeepayBizException) t).getMessage() : Errors.SYSTEM_EXCEPTION.getMsg();
		CashierBusinessException outerException = CommonUtil.handleApiCashierException(errorCode, errorMsg);
		responseDTO.setCode(outerException.getDefineCode());
		responseDTO.setMessage(outerException.getMessage());
	}

	/**
	 * 订单校验（反查订单/refer校验/判断订单非空及签名商编合法性）
	 * 
	 * @param basicRequestDTO
	 * @return
	 */
	protected OrderDetailInfoModel queryOrder(APIBasicRequestDTO basicRequestDTO) {
		// orderInfo的判空调用交易系统反查订单时已经校验了，空则抛异常
		OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(basicRequestDTO.getMerchantNo(), basicRequestDTO.getToken(),
				basicRequestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
		return orderInfo;
	}

	/**
	 * 入参校验，并将商编格式化
	 * 
	 * @param basicRequestDTO
	 */
	protected void paramValidate(APIBasicRequestDTO basicRequestDTO) {
		if (basicRequestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",入参不能为空");
		}
		// 由于yop请求进来的商编带有"OPR:"前缀，例如OPR：10040007800，因此需要将前缀去除
		basicRequestDTO.setMerchantNo(CommonUtil.formatMerchantNo(basicRequestDTO.getMerchantNo()));
		basicRequestDTO.validate();
	}

	/**
	 * 业务校验
	 * 
	 * @param basicRequestDTO
	 * @param orderInfo
	 * @return
	 */
	protected <T> T bizValidate(APIBasicRequestDTO basicRequestDTO, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantConfigInfo) {
		return null;
	}
	
	
	/**
	 * 处理paymentRequest和paymentRecord
	 * 
	 * @param otherInfoNeeded
	 *            处理所需的其他信息
	 * @param orderInfo
	 * @param merchantConfigInfo
	 * @param productLevel
	 * @param requestDTO
	 * @return
	 */
	protected abstract <T> CombinedPaymentDTO handleRequestAndRecord(T otherInfoNeeded, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantConfigInfo, ProductLevel productLevel, APIBasicRequestDTO requestDTO, APIBasicResponseDTO responseDTO);

	protected abstract <T> T callPPService(CombinedPaymentDTO combinedPaymentDTO, APIBasicRequestDTO requestDTO,
			APIBasicResponseDTO responseDTO);

	protected abstract void supplyOrderInfo(Object object, PaymentRecord paymentRecord);

	/**
	 * 补充返回值
	 * 
	 * @param basicResponseDTO
	 * @param basicRequestDTO
	 * @param combinedPaymentDTO
	 * @param t
	 */
	protected <T> void buildResponse(APIBasicResponseDTO basicResponseDTO, APIBasicRequestDTO basicRequestDTO,
			CombinedPaymentDTO combinedPaymentDTO, T t) {
		if (combinedPaymentDTO != null && combinedPaymentDTO.getPaymentRecord() != null) {
			basicResponseDTO.setRecordId(String.valueOf(combinedPaymentDTO.getPaymentRecord().getId()));
		}
		basicResponseDTO.setMerchantNo(basicRequestDTO.getMerchantNo());
		basicResponseDTO.setToken(basicRequestDTO.getToken());
		basicResponseDTO.setCode(APICashierPayResultEnum.SUCCESS.getCode());
		basicResponseDTO.setMessage(APICashierPayResultEnum.SUCCESS.getMessage());
	}

}
