package com.yeepay.g3.app.nccashier.wap.service.impl;

import java.util.ArrayList;
import java.util.Set;

import javax.annotation.Resource;

import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.DirectPayType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.EBankPayService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.vo.EBankBaseVO;
import com.yeepay.g3.app.nccashier.wap.vo.EBankPayRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.EBankPayResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.EBankSupportBanksVO;
import com.yeepay.g3.app.nccashier.wap.vo.UrlInfoVO;
import com.yeepay.g3.facade.nccashier.enumtype.BankAccountTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * pc网银 支付相关服务提供类 
 * 
 * @author duangduang
 * @since 2016-11-08
 */
@Service("eBankPayService")
public class EBankPayServiceImpl implements EBankPayService {

	@Resource
	private NcCashierService ncCashierService;

	@Resource
	private NewWapPayService newWapPayService;

	@Override
	public EBankSupportBanksVO ebankIndexShow(String token) {
		RequestInfoDTO requestInfo = newWapPayService.validateRequestInfoDTO(token);
		return querySupportBankList(requestInfo);
	}

	@Override
	public EBankSupportBanksVO querySupportBankList(RequestInfoDTO requestInfo) {
		EBankSupportBanksRequestDTO param = buildSupportBanksRequestParam(requestInfo);
		EBankSupportBanksResponseDTO response = ncCashierService.ebankSupportBankList(param);
		filterSupportBanksWithDirectPay(response,requestInfo.getUrlParamInfo().getDirectPayType());
		return buildSupportBanksResponseVO(response);
	}

	/**
	 * 如果网银支付为直连，根据直连的卡类型，过滤b2b/b2c列表
	 * @param response
	 * @param directPayType
	 */
	private void filterSupportBanksWithDirectPay(EBankSupportBanksResponseDTO response, String directPayType) {
		BankAccountTypeEnum accountTypeEnum = DirectPayType.getEbankDirectAccountType(directPayType);
		if (accountTypeEnum == null) {
			return;
		}
		if (BankAccountTypeEnum.B2B.equals(accountTypeEnum)) {
			if (CollectionUtils.isEmpty(response.getB2bBanks())) {
				//直连b2b，且b2b银行列表为空，直接抛出异常"收银台模板支持的银行为空"
				throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
			}
			if (CollectionUtils.isNotEmpty(response.getB2cBanks())) {
				//直连b2b，且b2b和b2c银行列表非空，则清空b2c银行列表
				response.getB2cBanks().clear();
			}

		}
		if (BankAccountTypeEnum.B2C.equals(accountTypeEnum)) {
			if (CollectionUtils.isEmpty(response.getB2cBanks())) {
				throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
			}
			if (CollectionUtils.isNotEmpty(response.getB2bBanks())) {
				response.getB2bBanks().clear();
			}
		}

	}

	/**
	 * 创建请求支持的收银台模板参数
	 * 
	 * @param requestInfo
	 * @return
	 */
	private EBankSupportBanksRequestDTO buildSupportBanksRequestParam(RequestInfoDTO requestInfo) {
		EBankSupportBanksRequestDTO param = new EBankSupportBanksRequestDTO();
		param.setPaymentRequestId(requestInfo.getPaymentRequestId());
		return param;
	}

	/**
	 * 创建网银首页所需的支持的收银台模板的返回值
	 * 
	 * @param response
	 * @return
	 */
	private EBankSupportBanksVO buildSupportBanksResponseVO(EBankSupportBanksResponseDTO response) {
		EBankSupportBanksVO responseVO = new EBankSupportBanksVO();

		if (CollectionUtils.isNotEmpty(response.getB2bBanks())) {
			responseVO.setB2bBanks(new ArrayList<EBankBaseVO>());
			BeanUtils.copyListProperties(response.getB2bBanks(), responseVO.getB2bBanks(), EBankBaseVO.class);
			// 是否需要客户ID
			Set<String> pubBanksNeedClientId = CommonUtil.ebanksToPublicNeedClientId();
			if (CollectionUtils.isNotEmpty(pubBanksNeedClientId)) {
				for (String bankCodeNeedClient : pubBanksNeedClientId) {
					for (EBankBaseVO pubBank : responseVO.getB2bBanks()) {
						if (StringUtils.isNotBlank(pubBank.getBankCode()) && !pubBank.isNeedClient()
								&& pubBank.getBankCode().equals(bankCodeNeedClient)) {
							pubBank.setNeedClient(true);
							pubBank.setBankAccountType(BankAccountTypeEnum.B2B.name());
							break;
						}
					}
				}
			}
		}

		if (CollectionUtils.isNotEmpty(response.getB2cBanks())) {
			responseVO.setB2cBanks(new ArrayList<EBankBaseVO>());
			BeanUtils.copyListProperties(response.getB2cBanks(), responseVO.getB2cBanks(), EBankBaseVO.class);
		}
		responseVO.setB2bPayScene(response.getB2bPayScene());
		responseVO.setB2cPayScene(response.getB2cPayScene());
		return responseVO;
	}

	@Override
	public EBankPayResponseVO ebankPay(EBankPayRequestVO param) {
		// 1 校验参数
		validateEbankPayParam(param);
		// 2 校验Token并获取订单基本信息
		RequestInfoDTO requestInfo = newWapPayService.validateRequestInfoDTO(param.getToken());
		// 3 调用core，进行下单
		EBankCreatePaymentRequestDTO request = buildEBankCreatePaymentRequestParam(param, requestInfo);
		EBankCreatePaymentResponseDTO response = ncCashierService.ebankCreatePayment(request);
		return buildEBankPayResponseVO(response);

	}

	/**
	 * 网银下单 用户输入校验
	 * 
	 * @param param
	 */
	private void validateEbankPayParam(EBankPayRequestVO param) {
		// 入参、银行编码、银行账户类型为空 
		if (param == null || StringUtils.isBlank(param.getBankCode())
				|| StringUtils.isBlank(param.getEbankAccountType())) {
			throw new CashierBusinessException(Errors.EBANK_DIRECT_PAY_INFO_NULL.getCode(), Errors.EBANK_DIRECT_PAY_INFO_NULL.getMsg());
		}
		// 对公银行 客户ID校验
		if (param.getEbankAccountType().contains(BankAccountTypeEnum.B2B.name())) {
			Set<String> pubBanksNeedClientId = CommonUtil.ebanksToPublicNeedClientId();
			if (CollectionUtils.isNotEmpty(pubBanksNeedClientId) && pubBanksNeedClientId.contains(param.getBankCode())
					&& StringUtils.isBlank(param.getClientId())) {
				throw new CashierBusinessException(Errors.EBANK_B2B_CLIENT_ID_NULL.getCode(), Errors.EBANK_B2B_CLIENT_ID_NULL.getMsg());
			}
		}
	}

	/**
	 * 创建确认支付下单入参
	 * 
	 * @param requestInfo
	 * @return
	 */
	private EBankCreatePaymentRequestDTO buildEBankCreatePaymentRequestParam(EBankPayRequestVO param,
			RequestInfoDTO requestInfo) {
		EBankCreatePaymentRequestDTO request = new EBankCreatePaymentRequestDTO();
		request.seteBankAccountType(param.getEbankAccountType());
		request.setBankId(param.getBankCode());
		request.setPaymentRequestId(requestInfo.getPaymentRequestId());
		request.setPaymentRecordId(requestInfo.getPaymentRecordId()==null?0:requestInfo.getPaymentRecordId().longValue());
		request.setToken(param.getToken());
		request.setClientId(param.getClientId());
		request.setDirectEbankPay(param.isDirectEbankPay());
		request.setPayScene(param.getPayScene());
		request.setNetPayerIp(param.getNetPayerIp());
		return request;
	}

	private EBankPayResponseVO buildEBankPayResponseVO(EBankCreatePaymentResponseDTO response) {
		EBankPayResponseVO responseVO = new EBankPayResponseVO();
		//TODO 等到此次上线之后，可以下掉
		responseVO.setBankPayUrl(response.getBankPayUrl());
		responseVO.setPayUrl(response.getPayUrl());
		responseVO.setRedirectType(response.getToBankPassThroughPccashier());
		responseVO.setMerchantOrderId(response.getMerchantOrderId());
		if(Constant.TO_BANK.equals(response.getToBankPassThroughPccashier())){
			UrlInfoVO urlInfoVO = new UrlInfoVO();
			BeanUtils.copyProperties(response.getUrlInfoDTO(), urlInfoVO);
			responseVO.setEbankUrlInfo(urlInfoVO);
		}
		
		return responseVO;
	}



	@Override
	public EBankSupportBanksVO getLoadSupportEBanks(String token, String type) {
		RequestInfoDTO requestInfo = newWapPayService.validateRequestInfoDTO(token);
		return queryLoadSupportBankList(requestInfo,token,type);
	}

	@Override
	public EBankSupportBanksVO queryLoadSupportBankList(RequestInfoDTO requestInfo,String token,String type) {
		EBankSupportBanksRequestDTO param = buildBacLoadQueryDto(requestInfo.getPaymentRequestId(),type,token,requestInfo.getMerchantNo());
		EBankSupportBanksResponseDTO response = ncCashierService.getBacLoadSuportBanks(param);
		return buildSupportBanksResponseVO(response);
	}


	EBankSupportBanksRequestDTO buildBacLoadQueryDto(long requestId,String type,String token,String merchantNum){
		EBankSupportBanksRequestDTO param = new EBankSupportBanksRequestDTO();
		param.setPaymentRequestId(requestId);
		param.setBacNetType(type);
		param.setToken(token);
		param.setMerchantNo(merchantNum);
		return param;
	}

}
