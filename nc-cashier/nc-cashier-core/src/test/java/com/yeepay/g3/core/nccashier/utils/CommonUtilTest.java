package com.yeepay.g3.core.nccashier.utils;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

public class CommonUtilTest extends BaseTest{
	
	@Test
	public void handleExceptionTestSelf(){
		CashierBusinessException e = CommonUtil.handleException(Errors.AMOUNT_OUT_RANGE);
		System.out.println("defineCode:" + e.getDefineCode());
		System.out.println("errorMsg:" + e.getMessage());
	}
	
	@Test
	public void handleExceptionTestNcpay(){
		CashierBusinessException e = CommonUtil.handleException("NCPAY","411151","钱不够啊啊啊啊");
		System.out.println("defineCode:" + e.getDefineCode());
		System.out.println("errorMsg:" + e.getMessage());
	}
	
	@Test
	public void handleExceptionTestFE(){
		CashierBusinessException e = CommonUtil.handleException("FE","F0001002","随便错误码描述信息");
		System.out.println("defineCode:" + e.getDefineCode());
		System.out.println("errorMsg:" + e.getMessage());
	}

}
