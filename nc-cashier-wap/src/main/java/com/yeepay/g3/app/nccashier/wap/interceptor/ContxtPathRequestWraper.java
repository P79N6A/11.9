package com.yeepay.g3.app.nccashier.wap.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ContxtPathRequestWraper extends HttpServletRequestWrapper {

	public ContxtPathRequestWraper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
    public String getContextPath() {
        if("cash.yeepay.com".equals(this.getServerName())) {
            return "";
        }else {
            return super.getContextPath();
        }

    }



}
