package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class APIInstallmentSmsRequestDTO extends APIBasicRequestDTO {

	private static Logger logger = LoggerFactory.getLogger(APIInstallmentSmsRequestDTO.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 支付记录ID
	 */
	private String recordId;

	public APIInstallmentSmsRequestDTO() {

	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Override
	public void validate() {
		super.validate();
		
		if(StringUtils.isBlank(getToken())){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",token不能为空");
		}
		
		Long recordIdL = null;
		try {
			recordIdL = Long.valueOf(recordId);
		} catch (Throwable t) {
			logger.warn("订单token=" + getToken() + "，转recordId为Long类型异常,e=", t);
			;
		}
		if (recordIdL == null || recordIdL <= 0) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",recordId格式错误");
		}
	}

}
