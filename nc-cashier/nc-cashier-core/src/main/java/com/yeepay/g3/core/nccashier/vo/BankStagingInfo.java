package com.yeepay.g3.core.nccashier.vo;

import java.util.List;
import java.util.Map;

import com.yeepay.g3.facade.nccashier.enumtype.PayTool;

/**
 * 支付工具对应的银行属性信息
 * 
 * @author duangduang
 *
 */
public class BankStagingInfo {
	
	/**
	 * 支付工具
	 */
	private PayTool payTool;
	
	/**
	 * 银行属性
	 */
	private Map<String, List<String>> bankStagings;

	public PayTool getPayTool() {
		return payTool;
	}

	public void setPayTool(PayTool payTool) {
		this.payTool = payTool;
	}

	public Map<String, List<String>> getBankStagings() {
		return bankStagings;
	}

	public void setBankStagings(Map<String, List<String>> bankStagings) {
		this.bankStagings = bankStagings;
	}

}
