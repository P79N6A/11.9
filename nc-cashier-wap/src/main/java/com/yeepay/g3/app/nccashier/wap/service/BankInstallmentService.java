package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.vo.InstallmentAmountInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankResponseVO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentBankInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;

import java.util.List;

public interface BankInstallmentService {

	InstallmentBankResponseVO getUsableBankList(long paymentRequestId);

	InstallmentAmountInfoVO getRateInfo(RequestInfoDTO info, InstallmentBankRequestVO installmentBankRequestVO);

	InstallmentBankResponseVO routePayWay(long requestId);

	InstallmentBankResponseVO transferToBankInfoVOList(List<InstallmentBankInfoDTO> bankList);
}
