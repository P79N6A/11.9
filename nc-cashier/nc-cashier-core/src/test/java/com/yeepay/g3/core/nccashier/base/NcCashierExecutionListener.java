package com.yeepay.g3.core.nccashier.base;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class NcCashierExecutionListener extends AbstractTestExecutionListener {
	public void beforeTestMethod(TestContext testContext) throws Exception {
		long fStartTime = System.currentTimeMillis();
		testContext.setAttribute("fStartTime", fStartTime);
	}
	public void afterTestMethod(TestContext testContext) throws Exception {
		long fStartTime = (Long) testContext.getAttribute("fStartTime");
		long endTime = System.currentTimeMillis();
		
		System.out.println("JUnit test " +testContext.getTestClass().getSimpleName()+":" + testContext.getTestMethod().getName() + "time cost：" + (endTime - fStartTime)+"毫秒");

	}

}
