package com.yeepay.g3.app.nccashier.wap.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ContextPathFilter implements Filter {

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest paramServletRequest,
			ServletResponse paramServletResponse, FilterChain paramFilterChain)
			throws IOException, ServletException {
		ContxtPathRequestWraper requestWrapper = new ContxtPathRequestWraper((HttpServletRequest) paramServletRequest);       
		paramFilterChain.doFilter(requestWrapper, paramServletResponse);  

	}

	@Override
	public void destroy() {
	}

}
