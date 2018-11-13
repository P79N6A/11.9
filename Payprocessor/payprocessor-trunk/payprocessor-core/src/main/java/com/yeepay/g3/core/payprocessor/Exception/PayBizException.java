package com.yeepay.g3.core.payprocessor.Exception;

import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.exception.YeepayBizException;

public class PayBizException extends YeepayBizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public PayBizException(String errorCode) {
		super(errorCode);
		super.setMessage(ErrorCodeUtil.retrieveErrorCodeMsg(errorCode));
	}
	
	public PayBizException(String errorCode, String errorMsg) {
		super(errorCode);
		if(StringUtils.isNotBlank(errorMsg)){
			super.setMessage(errorMsg);
		}
	}
	
}

