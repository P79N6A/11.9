package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.core.payprocessor.param.CancelRequestParam;
import com.yeepay.g3.core.payprocessor.param.CancelResponse;

/**
 * Description:
 * @author peile.fan
 * @since:2017年1月5日 下午3:21:59
 */
public interface CancelBiz {
	/**
	 * @param requestDTO
	 * @return
	 */
	CancelResponse cancelRequest(CancelRequestParam requestDTO);

}
