package com.yeepay.g3.core.frontend.Exception;

import com.yeepay.g3.core.frontend.util.ErrorCodeUtil;
import com.yeepay.g3.utils.common.exception.YeepayBizException;

public class FrontendBizException  extends YeepayBizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public FrontendBizException(String errorCode) {
		super(errorCode);
		super.setMessage(ErrorCodeUtil.retrieveErrorCodeMsg(errorCode));
	}
	
	public FrontendBizException(String errorCode, String errorMsg) {
		super(errorCode);
		super.setMessage(errorMsg);
	}
	
}

