package com.yeepay.g3.app.nccashier.wap.vo.clfeasy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentAmountInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.ResponseVO;

public class CflEasyBankInfoVO extends ResponseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 可用银行列表
	 */
	private Map<String, String> usableBankList;

	/**
	 * key：银行编码 value：该银行对应的期数及每期手续费信息
	 */
	private Map<String, List<InstallmentAmountInfoVO>> periodList;

	/**
	 * 总的手续费
	 */
	private String totalFee;

	/**
	 * 总的订单金额
	 */
	private String totalAmount;

	private String orderAmount;

	public CflEasyBankInfoVO() {

	}

	public Map<String, String> getUsableBankList() {
		return usableBankList;
	}

	public void setUsableBankList(Map<String, String> usableBankList) {
		this.usableBankList = usableBankList;
	}

	public Map<String, List<InstallmentAmountInfoVO>> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(Map<String, List<InstallmentAmountInfoVO>> periodList) {
		this.periodList = periodList;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public void appendAmountInfo(BigDecimal orderAmount){
		setOrderAmount(orderAmount.toString());
		setTotalAmount(orderAmount.toString());
		setTotalFee("0.00");
	}
	
	public void appendBankInfo(String bankCode, String bankName) {
		if (MapUtils.isEmpty(usableBankList)) {
			usableBankList = new HashMap<String, String>();
		}
		usableBankList.put(bankCode, bankName);

	}

	public void appendPeriodInfo(String bankCode, InstallmentAmountInfoVO cflPeriodInfo) {
		if (MapUtils.isEmpty(periodList)) {
			periodList = new HashMap<String, List<InstallmentAmountInfoVO>>();
		}
		List<InstallmentAmountInfoVO> periodInfoOfBank = periodList.get(bankCode);
		if (periodInfoOfBank == null) {
			periodInfoOfBank = new ArrayList<InstallmentAmountInfoVO>();
		}
		periodInfoOfBank.add(cflPeriodInfo);
		periodList.put(bankCode, periodInfoOfBank);
	}

	public boolean hasNoUsableBank() {
		return MapUtils.isEmpty(usableBankList);
	}

	public static void main(String[] args) {
		CflEasyBankInfoVO reponseVO = new CflEasyBankInfoVO();
		reponseVO.setBizStatus("success");
		reponseVO.setToken("example");
		reponseVO.setTotalAmount("20000");
		reponseVO.setTotalFee("90");
		List<String> banks = new ArrayList<String>();
		banks.add("ICBC");
		banks.add("ABC");
		// reponseVO.setUsableBankList(banks);
		Map<String, List<InstallmentAmountInfoVO>> periods = new HashMap<String, List<InstallmentAmountInfoVO>>();
		List<InstallmentAmountInfoVO> icbc = new ArrayList<InstallmentAmountInfoVO>();
		InstallmentAmountInfoVO icbc3 = new InstallmentAmountInfoVO();
		icbc3.setFirstPayment("100");
		icbc3.setTerminalPayment("200");
		icbc3.setPeriod("3");
		icbc.add(icbc3);
		InstallmentAmountInfoVO icbc6 = new InstallmentAmountInfoVO();
		icbc6.setFirstPayment("50");
		icbc6.setTerminalPayment("70");
		icbc6.setPeriod("6");
		icbc.add(icbc6);
		periods.put("ICBC", icbc);
		List<InstallmentAmountInfoVO> abc = new ArrayList<InstallmentAmountInfoVO>();
		InstallmentAmountInfoVO abc3 = new InstallmentAmountInfoVO();
		abc3.setFirstPayment("100");
		abc3.setTerminalPayment("200");
		abc3.setPeriod("3");
		abc.add(abc3);
		periods.put("ABC", abc);
		reponseVO.setPeriodList(periods);
		System.out.print(JSON.toJSONString(reponseVO));
	}

}
