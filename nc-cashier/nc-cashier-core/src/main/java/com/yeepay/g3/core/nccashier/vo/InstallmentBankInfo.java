package com.yeepay.g3.core.nccashier.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.nccashier.dto.BankInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentBankInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentPeriodAndRateInfoDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class InstallmentBankInfo {
	
	private static Logger logger = LoggerFactory.getLogger(InstallmentBankInfo.class);

	/**
	 * 当前银行信息
	 */
	private BankInfo bankInfo;

	/**
	 * 当前银行支持的所有期数和费率
	 */
	private List<InstallmentRateInfo> installmentRateInfos;
	
	private InstallmentRateInfo targetInstallmentRateInfo;

	/**
	 * 当前银行支持的分期手续费收取方式
	 */
	private String rateWay;

	/**
	 * 备注原因
	 */
	private String remark;
	
	public InstallmentBankInfo() {

	}

	public BankInfo getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(BankInfo bankInfo) {
		this.bankInfo = bankInfo;
	}

	public List<InstallmentRateInfo> getInstallmentRateInfos() {
		return installmentRateInfos;
	}

	public void setInstallmentRateInfos(List<InstallmentRateInfo> installmentRateInfos) {
		this.installmentRateInfos = installmentRateInfos;
	}

	public String getRateWay() {
		return rateWay;
	}

	public void setRateWay(String rateWay) {
		this.rateWay = rateWay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public InstallmentRateInfo getTargetInstallmentRateInfo() {
		return targetInstallmentRateInfo;
	}

	public void setTargetInstallmentRateInfo(InstallmentRateInfo targetInstallmentRateInfo) {
		this.targetInstallmentRateInfo = targetInstallmentRateInfo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 获取符合期数的费率信息
	 * 
	 * @param period
	 * @return
	 */
	public InstallmentRateInfo getInstallmentRateInfoByPeriod(String period) {
		List<InstallmentRateInfo> rateInfoList = getInstallmentRateInfos();
		for (InstallmentRateInfo rateInfo : rateInfoList) {
			if (period.equals(rateInfo.getPeriod() + "")) {
				return rateInfo;
			}
		}
		return null;
	}
	
	public void judgeTargetRateInfoByPeriod(String period) {
		InstallmentRateInfo info = getInstallmentRateInfoByPeriod(period);
		setTargetInstallmentRateInfo(info);
		if (info == null) {
			throw new CashierBusinessException(Errors.PERIOR_NOT_SUPPPORT);
		}
	}
	
	/**
	 * 
	 * @param numbers 开通的期数
	 * @return
	 */
	public List<InstallmentRateInfo> getSupportRateList(List<String> numbers) {
		if (CollectionUtils.isEmpty(installmentRateInfos)) {
			return null;
		}
		List<InstallmentRateInfo> objRates = new ArrayList<InstallmentRateInfo>();
		for (String number : numbers) {
			for (InstallmentRateInfo installmentRateInfo : installmentRateInfos) {
				if (number.equals(String.valueOf(installmentRateInfo.getPeriod()))) {
					objRates.add(installmentRateInfo);
					break;
				}
			}
		}
		return objRates;
	}

	public InstallmentBankInfoDTO transferToInstallmentBankInfoDTO() {
		if (bankInfo == null || CollectionUtils.isEmpty(installmentRateInfos)) {
			return null;
		}
		BankInfoDTO bankInfoDTO = bankInfo.transferToBankInfoDTO();
		List<InstallmentPeriodAndRateInfoDTO> rateInfoDTOList = transferRateListTORateDTOList();
		InstallmentBankInfoDTO infoDTO = new InstallmentBankInfoDTO();
		infoDTO.setBank(bankInfoDTO);
		infoDTO.setNumsAndRates(rateInfoDTOList);
		infoDTO.setRateWay(rateWay);
		return infoDTO;
	}

	public List<InstallmentPeriodAndRateInfoDTO> transferRateListTORateDTOList() {
		List<InstallmentPeriodAndRateInfoDTO> rateDTOList = new ArrayList<InstallmentPeriodAndRateInfoDTO>();
		for (InstallmentRateInfo info : installmentRateInfos) {
			InstallmentPeriodAndRateInfoDTO rateInfoDTO = info.transferToRateInfoDTO();
			rateDTOList.add(rateInfoDTO);
		}
		return rateDTOList;
	}

	public InstallmentBankInfo copy(InstallmentRateInfo objRate) {
		InstallmentBankInfo result = new InstallmentBankInfo();
		result.setBankInfo(bankInfo);
		List<InstallmentRateInfo> rates = new ArrayList<InstallmentRateInfo>();
		rates.add(objRate);
		result.setInstallmentRateInfos(rates);
		result.setRateWay(rateWay);
		return result;
	}
	
	public void standardBankInfo(){
		if(bankInfo!=null){
			bankInfo.standardBankCode();
		}
	}

}
