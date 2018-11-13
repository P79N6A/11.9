package com.yeepay.g3.core.nccashier.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import org.springframework.stereotype.Component;

import com.yeepay.g3.core.nccashier.biz.APICashierBaseBizTemplate;
import com.yeepay.g3.core.nccashier.biz.APIInstallmentBiz;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.service.SignCardService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.APIInstallmentRequestInnerParam;
import com.yeepay.g3.core.nccashier.vo.InstallmentBankInfo;
import com.yeepay.g3.core.nccashier.vo.SignRelationInfo;
import com.yeepay.g3.core.nccashier.vo.UserSignRelationCollection;
import com.yeepay.g3.core.nccashier.vo.VerifyProductOpenRequestParam;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentComfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentComfirmResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentSmsRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentSmsResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentBankInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.APICashierPayResultEnum;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Component("apiInstallmentBiz")
public class APIInstallmentBizImpl implements APIInstallmentBiz {
	
	private static Logger logger = LoggerFactory.getLogger(APIInstallmentBizImpl.class);

	@Resource
	private SignCardService signCardService;

	@Resource
	private com.yeepay.g3.core.nccashier.service.MerchantSupportBankService MerchantSupportBankService;

	@Resource
	private MerchantVerificationService merchantVerificationService;

	@Resource(name = "apiInstallmentRequestBiz")
	private APICashierBaseBizTemplate apiInstallmentRequestBiz;

	@Resource(name = "apiInstallmentSmsSendBiz")
	private APICashierBaseBizTemplate apiInstallmentSmsSendBiz;

	@Resource(name = "apiInstallmentConfirmPayBiz")
	private APICashierBaseBizTemplate apiInstallmentConfirmPayBiz;

	private static ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.YHKFQ_ZF,
			PayTypeEnum.YHKFQ_ZF);

	@Override
	public APIBasicResponseDTO request(APIInstallmentRequestDTO requestDTO) {
		APIBasicResponseDTO responseDTO = new APIInstallmentResponseDTO();
		APIInstallmentRequestInnerParam innerParam = new APIInstallmentRequestInnerParam();
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[银行卡分期API下单|request],订单token="+requestDTO.getToken());
			BeanUtils.copyProperties(requestDTO, innerParam);
			apiInstallmentRequestBiz.handle(innerParam, responseDTO, productLevel);
		} catch (Throwable t) {
			apiInstallmentRequestBiz.errorResult(t, innerParam, responseDTO);
		}
		return responseDTO;
	}

	@Override
	public APIBasicResponseDTO smsSend(APIInstallmentSmsRequestDTO requestDTO) {
		APIBasicResponseDTO responseDTO = new APIInstallmentSmsResponseDTO();
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[银行卡分期API发短验|smsSend],订单token="+requestDTO.getToken());
			apiInstallmentSmsSendBiz.handle(requestDTO, responseDTO, productLevel);
		} catch (Throwable t) {
			apiInstallmentSmsSendBiz.errorResult(t, requestDTO, responseDTO);
		}
		return responseDTO;
	}

	@Override
	public APIBasicResponseDTO confirmPay(APIInstallmentComfirmRequestDTO requestDTO) {
		APIBasicResponseDTO responseDTO = new APIInstallmentComfirmResponseDTO();
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[银行卡分期API确认支付|confirmPay],订单token="+requestDTO.getToken());
			apiInstallmentConfirmPayBiz.handle(requestDTO, responseDTO, productLevel);
		} catch (Throwable t) {
			apiInstallmentConfirmPayBiz.errorResult(t, requestDTO, responseDTO);
		}
		return responseDTO;
	}

	@Override
	public InstallmentInfoResponseDTO queryInstallmentRateInfos(InstallmentInfoRequestDTO rateInfoRequestDTO) {
		InstallmentInfoResponseDTO responseDTO = new InstallmentInfoResponseDTO();
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[银行卡分期API付查询费率|queryInstallmentRateInfos],订单token="+rateInfoRequestDTO.getToken());
			validateInstallmentInfoRequestDTO(rateInfoRequestDTO);
			responseDTO.setMerchantNo(rateInfoRequestDTO.getMerchantNo());
			// 获取配置中心分期支持银行列表及期数 这里的商编也必须暴露出去，必须是收单商编 TODO
			VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
			requestParam.setMerchantNo(rateInfoRequestDTO.getMerchantNo());
			requestParam.setProductLevel(productLevel);
			requestParam.setTransactionType(TransactionTypeEnum.SALE);
			MerchantInNetConfigResult merchantConfigInfo = merchantVerificationService.verifyMerchantAuthority(requestParam);
			List<InstallmentBankInfo> supportBanks = merchantConfigInfo.listInstallmentSupportBank();
			buildInstallmentInfoResponseDTO(responseDTO, supportBanks);
		} catch (Throwable t) {
			logger.warn("queryInstallmentRateInfos_查询分期期数及费率信息异常，param=" + rateInfoRequestDTO, t);
			apiInstallmentSmsSendBiz.errorResult(t, rateInfoRequestDTO, responseDTO);
		}
		return responseDTO;
	}

	private void buildInstallmentInfoResponseDTO(InstallmentInfoResponseDTO responseDTO,
			List<InstallmentBankInfo> supportBanks) {
		if (CollectionUtils.isEmpty(supportBanks)) {
			throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}
		List<InstallmentBankInfoDTO> installmentBankList = new ArrayList<InstallmentBankInfoDTO>();
		for (InstallmentBankInfo installmentBankInfo : supportBanks) {
			InstallmentBankInfoDTO installmentBankInfoDTO = installmentBankInfo.transferToInstallmentBankInfoDTO();
			installmentBankList.add(installmentBankInfoDTO);
		}
		responseDTO.setInstallmentBankList(installmentBankList);
	}

	/**
	 * 校验查询分期费率信息的
	 * 
	 * @param rateInfoRequestDTO
	 */
	private void validateInstallmentInfoRequestDTO(InstallmentInfoRequestDTO rateInfoRequestDTO) {
		if (rateInfoRequestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",查询入参不能为空");
		}
		// 格式化商编
		rateInfoRequestDTO.setMerchantNo(CommonUtil.formatMerchantNo(rateInfoRequestDTO.getMerchantNo()));
		rateInfoRequestDTO.validate();
	}

	/**
	 * 校验签约关系查询接口入参
	 * 
	 * @param queryDTO
	 */
	private void validateSignRelationQueryRequestDTO(SignRelationQueryRequestDTO queryDTO) {
		if (queryDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",查询入参不能为空");
		}
		// 格式化商编
		queryDTO.setMerchantNo(CommonUtil.formatMerchantNo(queryDTO.getMerchantNo()));
		queryDTO.validate();
	}

	@Override
	public SignRelationQueryResponseDTO querySignRelationList(SignRelationQueryRequestDTO queryDTO) {
		SignRelationQueryResponseDTO response = new SignRelationQueryResponseDTO();
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[银行卡分期API查询签约关系|querySignRelationList],userNo="+queryDTO.getUserNo()+",userType="+queryDTO.getUserType());
			validateSignRelationQueryRequestDTO(queryDTO);
			queryDTO.buildSignRelationQueryResponseDTO(response);
			// 获取该外部用户已签约关系记录
			List<SignRelationInfo> signRelationList = getSignCardList(queryDTO);
			UserSignRelationCollection userSignRelationCollection = null;
			if(CollectionUtils.isNotEmpty(signRelationList)){
				// 获取商户开通配置 要求这个商编必须是收单商编，因此这个商编需要暴露出去，其实这个是否要验签都无所谓 TODO
				VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
				requestParam.setMerchantNo(queryDTO.getMerchantNo());
				requestParam.setProductLevel(productLevel);
				requestParam.setTransactionType(TransactionTypeEnum.SALE);
				MerchantInNetConfigResult merchantConfigInfo = merchantVerificationService
						.verifyMerchantAuthorityWithoutNotOpenError(requestParam);
				userSignRelationCollection = MerchantSupportBankService
						.filterSignRelationList(signRelationList, merchantConfigInfo);
			}
			supplySignRelationQueryResponseDTO(userSignRelationCollection, response);
			return response;
		} catch (Throwable t) {
			apiInstallmentRequestBiz.errorResult(t, response);
		}
		return response;
	}

	private List<SignRelationInfo> getSignCardList(SignRelationQueryRequestDTO queryDTO) {
		CashierUserInfo externalUserInfo = buildCashierUserInfo(queryDTO);
		return signCardService.getSignCardList(externalUserInfo);
	}

	private void supplySignRelationQueryResponseDTO(UserSignRelationCollection userSignRelationCollection,
			SignRelationQueryResponseDTO response) {
		response.setCode(APICashierPayResultEnum.SUCCESS.getCode());
		response.setMessage(APICashierPayResultEnum.SUCCESS.getMessage());
		if(userSignRelationCollection==null){
			return;
		}
		List<SignRelationDTO> usableSignRelationDTOList = transferToSignRelationDTOList(
				userSignRelationCollection.getUsableSignRelationList());
		List<SignRelationDTO> unusableSignRelationDTOList = transferToSignRelationDTOList(
				userSignRelationCollection.getUnusableSignRelationList());
		response.setUnusableSignRelationList(unusableSignRelationDTOList);
		response.setUsableSignRelationList(usableSignRelationDTOList);
	}

	private List<SignRelationDTO> transferToSignRelationDTOList(List<SignRelationInfo> signRelationInfoList) {
		if (CollectionUtils.isEmpty(signRelationInfoList)) {
			return null;
		}
		List<SignRelationDTO> signRelationDTOList = new ArrayList<SignRelationDTO>();
		for (SignRelationInfo signRelationInfo : signRelationInfoList) {
			SignRelationDTO signRelationDTO = signRelationInfo.transferToSignRelationDTO();
			signRelationDTOList.add(signRelationDTO);
		}
		return signRelationDTOList;
	}


	/**
	 * 根据签约关系查询入参构造外部用户信息
	 *
	 * @param queryDTO
	 * @return
	 */
	private CashierUserInfo buildCashierUserInfo(SignRelationQueryRequestDTO queryDTO) {
		CashierUserInfo externalUserInfo = new CashierUserInfo();
		externalUserInfo.setUserNo(queryDTO.getUserNo());
		// 能走到这里 说明userType类型是合法的 --> 银行卡分期不支持三代会员
		externalUserInfo.setUserType(queryDTO.getUserType());
		externalUserInfo.setMerchantNo(queryDTO.getMerchantNo());
		externalUserInfo.setType(MemberTypeEnum.JOINLY.name());
		return externalUserInfo;
	}

}
