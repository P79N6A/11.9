package com.yeepay.g3.app.nccashier.wap.vo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class SupportBankResVO extends BasicResVO{

	private static final long serialVersionUID = 1L;
	
	private List<BankInfoVO> debitBankList;
	
	private List<BankInfoVO> creditBankList;
	
	public SupportBankResVO(){
		
	}

	public List<BankInfoVO> getDebitBankList() {
		return debitBankList;
	}

	public void setDebitBankList(List<BankInfoVO> debitBankList) {
		this.debitBankList = debitBankList;
	}

	public List<BankInfoVO> getCreditBankList() {
		return creditBankList;
	}

	public void setCreditBankList(List<BankInfoVO> creditBankList) {
		this.creditBankList = creditBankList;
	}

	@Override
	public String toString() {
		return "SupportBankResVO [debitBankList=" + debitBankList 
				+ ", creditBankList=" + creditBankList + "]";
	}
	
	public static void main(String[] args){
		SupportBankResVO response = new SupportBankResVO();
		response.setBizStatus("success");
		BankInfoVO bank1 = new BankInfoVO();
		bank1.setBankCode("ICBC");
		bank1.setBankName("工商银行");
		bank1.setCardType("DEBIT");
		List<BankInfoVO> debitBankList = new ArrayList<BankInfoVO>();
		debitBankList.add(bank1);
		BankInfoVO bank2 = new BankInfoVO();
		bank2.setBankCode("ABC");
		bank2.setBankName("农业银行");
		bank2.setCardType("DEBIT");
		debitBankList.add(bank2);
		response.setDebitBankList(debitBankList);
		
		List<BankInfoVO> creditBankList = new ArrayList<BankInfoVO>();
		BankInfoVO bank3 = new BankInfoVO();
		bank3.setBankCode("HX");
		bank3.setBankName("华夏银行");
		bank3.setCardType("CREDIT");
		creditBankList.add(bank3);
		BankInfoVO bank4 = new BankInfoVO();
		bank4.setBankCode("BOC");
		bank4.setBankName("中国银行");
		bank4.setCardType("CREDIT");
		creditBankList.add(bank4);
		response.setCreditBankList(creditBankList);
		System.out.println(JSON.toJSONString(response));
	}
	
}
