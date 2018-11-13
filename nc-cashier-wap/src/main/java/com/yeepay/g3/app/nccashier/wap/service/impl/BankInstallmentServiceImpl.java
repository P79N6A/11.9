package com.yeepay.g3.app.nccashier.wap.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.BankInstallmentService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.vo.BankInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentAmountInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.SignRelationVO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentBankInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentBanksResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentPeriodAndRateInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;

@Service("bankInstallmentService")
public class BankInstallmentServiceImpl implements BankInstallmentService {

	private static final String INSTALLMENT_SIGN_RELATION_MODE = "OLD";

	private static final String INSTALLMENT_NON_SIGN_RELATION_MODE = "NEW";

	@Resource
	private NcCashierService ncCashierService;

	@Override
	public InstallmentBankResponseVO getUsableBankList(long paymentRequestId) {
		InstallmentBanksResponseDTO response = ncCashierService.getInstallmentBanks(paymentRequestId);
		InstallmentBankResponseVO bankResponse = transferToBankInfoVOList(response.getUsableBankList());
		if (bankResponse == null || CollectionUtils.isEmpty(bankResponse.getUsableBankList())) {
			throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}
		return bankResponse;
	}

	@Override
	public InstallmentBankResponseVO transferToBankInfoVOList(List<InstallmentBankInfoDTO> bankList) {
		List<BankInfoVO> bankListRes = new ArrayList<BankInfoVO>();
		Map<String, List<String>> periodListOfBank = new HashMap<String, List<String>>();
		for (InstallmentBankInfoDTO bankInfo : bankList) {
			BankInfoVO bank = new BankInfoVO();
			bank.setBankCode(bankInfo.getBank().getBankCode());
			bank.setBankName(bankInfo.getBank().getBankName());
			bank.setRemark(bankInfo.getRemark());
			if (periodListOfBank != null) {
				periodListOfBank.put(bank.getBankCode(), getPeriodListOfBank(bankInfo.getNumsAndRates()));
			}
			bankListRes.add(bank);
		}
		InstallmentBankResponseVO bankResponse = new InstallmentBankResponseVO();
		bankResponse.setPeriodListOfBank(periodListOfBank);
		bankResponse.setUsableBankList(bankListRes);
		return bankResponse;
	}

	private List<String> getPeriodListOfBank(List<InstallmentPeriodAndRateInfoDTO> periodAndRateList) {
		if (CollectionUtils.isEmpty(periodAndRateList)) {
			return null;
		}
		List<String> periodList = new ArrayList<String>();
		for (InstallmentPeriodAndRateInfoDTO periodAndRate : periodAndRateList) {
			periodList.add(periodAndRate.getPeriod());
		}
		return periodList;
	}

	@Override
	public InstallmentAmountInfoVO getRateInfo(RequestInfoDTO info, InstallmentBankRequestVO installmentBankRequestVO) {
		InstallmentFeeInfoRequestDTO requestDTO = buildInstallmentFeeInfoRequestDTO(installmentBankRequestVO,
				info.getPaymentRequestId());
		InstallmentFeeInfoResponseDTO response = ncCashierService.getInstallmentFeeInfo(requestDTO);
		InstallmentAmountInfoVO amountInfoVO = buildInstallmentAmountInfoVO(installmentBankRequestVO, response);
		return amountInfoVO;
	}

	private InstallmentFeeInfoRequestDTO buildInstallmentFeeInfoRequestDTO(
			InstallmentBankRequestVO installmentBankRequestVO, long requestId) {
		InstallmentFeeInfoRequestDTO requestDTO = new InstallmentFeeInfoRequestDTO();
		requestDTO.setBankCode(installmentBankRequestVO.getBankCode());
		requestDTO.setPeriod(installmentBankRequestVO.getPeriod());
		requestDTO.setRequestId(requestId);
		return requestDTO;
	}

	private InstallmentAmountInfoVO buildInstallmentAmountInfoVO(InstallmentBankRequestVO installmentBankRequestVO,
			InstallmentFeeInfoResponseDTO response) {
		InstallmentAmountInfoVO amountInfo = new InstallmentAmountInfoVO();
		amountInfo.setBankCode(installmentBankRequestVO.getBankCode());
		java.text.DecimalFormat myformat = new java.text.DecimalFormat("#.00");
		amountInfo.setFee(myformat.format(response.getFeeAmount())); // 手续费不会为0
		if (response.getFeeAmountAfterSubsidy() != null) { // 补贴后的手续费可能为0，因为需要做特殊处理
			if (response.getFeeAmountAfterSubsidy().compareTo(BigDecimal.ZERO) == 0) {
				amountInfo.setFeeAfterSubsidy("0.00");
			} else {
				amountInfo.setFeeAfterSubsidy(myformat.format(response.getFeeAmountAfterSubsidy()));
			}
		}
		amountInfo.setFirstPayment(myformat.format(response.getFirstPayment())); // 首期支付不会为0
		amountInfo.setOrderAmount(myformat.format(response.getOrderAmount())); // 订单金额不会为0
		amountInfo.setTerminalPayment(myformat.format(response.getTerminalPayment())); // 以后每期支付不会为0
		amountInfo.setPeriod(installmentBankRequestVO.getPeriod());
		return amountInfo;
	}

	@Override
	public InstallmentBankResponseVO routePayWay(long requestId) {
		InstallmentRouteResponseDTO response = ncCashierService.installmentRoutePayWay(requestId);
		if (CollectionUtils.isEmpty(response.getUsableBankList())) {
			throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}
		InstallmentBankResponseVO responseVO = null;
//		if (CollectionUtils.isEmpty(response.getUsableSignRelationList())
//				&& CollectionUtils.isEmpty(response.getUnusableSignRelationList())) {
		if (CollectionUtils.isEmpty(response.getUsableSignRelationList())) {
			responseVO = transferToBankInfoVOList(response.getUsableBankList());
			responseVO.setMode(INSTALLMENT_NON_SIGN_RELATION_MODE);
		} else {
			responseVO = new InstallmentBankResponseVO();
			Map<String, List<String>> periodListOfBank = new HashMap<String, List<String>>();
			List<SignRelationVO> usableSignRelationList = transferToSignRelationVOList(
					response.getUsableSignRelationList(), periodListOfBank);
			List<SignRelationVO> unusableSignRelationList = transferToSignRelationVOList(
					response.getUnusableSignRelationList(), null);
			responseVO.setMode(INSTALLMENT_SIGN_RELATION_MODE);
			responseVO.setUsableSignRelationList(usableSignRelationList);
			responseVO.setUnusableSignRelationList(unusableSignRelationList);
			responseVO.setPeriodListOfBank(periodListOfBank);
		}
		return responseVO;
	}

	private List<SignRelationVO> transferToSignRelationVOList(List<SignRelationDTO> signRelationList,
			Map<String, List<String>> periodListOfBank) {
		if (CollectionUtils.isEmpty(signRelationList)) {
			return null;
		}
		List<SignRelationVO> signRelationRes = new ArrayList<SignRelationVO>();
		for (SignRelationDTO signRelation : signRelationList) {
			SignRelationVO signRelationVO = new SignRelationVO();
			signRelationVO.setRemark(signRelation.getRemark());
			signRelationVO.setSignRelationId(signRelation.getSignRelationId());
			BankInfoVO bankInfoVO = new BankInfoVO();
			bankInfoVO.setBankCode(signRelation.getBankCode());
			bankInfoVO.setBankName(signRelation.getBankName());
			bankInfoVO.setRateWay(signRelation.getRateWay());
			signRelationVO.setBank(bankInfoVO);
			signRelationVO.setCardNo(signRelation.getCardNo().substring(signRelation.getCardNo().length() - 4, signRelation.getCardNo().length()));
			signRelationVO.setCardType(CardTypeEnum.CREDIT.name()); 
			if (periodListOfBank != null) {
				periodListOfBank.put(bankInfoVO.getBankCode(), getPeriodListOfBank(signRelation.getNumsAndRates()));
			}
			if (StringUtils.isNotBlank(signRelation.getRemark())) {
				signRelationVO.setStatus("unusable");
			} else {
				signRelationVO.setStatus("usable");
			}
			signRelationRes.add(signRelationVO);
		}
		return signRelationRes;
	}
}
