package com.yeepay.g3.core.frontend.Exception;

import com.yeepay.g3.core.frontend.errorcode.ErrorCodeTranslator;
import com.yeepay.g3.core.frontend.errorcode.SystemErrorCodeTranslator;
import com.yeepay.g3.utils.common.exception.YeepayBizException;

/**
 * 系统业务异常.
 *
 * @author
 */
@Deprecated
public class FeBizException extends YeepayBizException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final ErrorCodeTranslator errorCodeTranslator = SystemErrorCodeTranslator.getInstance();


    public FeBizException(String defineCode) {
        super(defineCode);
        initErrorMsg();
    }

    public FeBizException(String defineCode, String msg) {
        super(defineCode);
        this.setMessage(msg);
    }

    private void  initErrorMsg() {
        String msg = errorCodeTranslator.getMessage(this.defineCode);
        this.setMessage(msg);
    }
}
