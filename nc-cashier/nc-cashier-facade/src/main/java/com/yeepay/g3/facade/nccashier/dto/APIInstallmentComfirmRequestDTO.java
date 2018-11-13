package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 银行卡分期确认支付请求入参
 * 
 * @author duangduang
 *
 */
public class APIInstallmentComfirmRequestDTO extends APIBasicRequestDTO {

	private static Logger logger = LoggerFactory.getLogger(APIInstallmentComfirmRequestDTO.class);

	private static final long serialVersionUID = 1L;

	private String recordId;

	private String verifyCode;

	public APIInstallmentComfirmRequestDTO() {

	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
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
		}
		if (recordIdL == null || recordIdL <= 0) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",recordId格式错误");
		}
		if (StringUtils.isBlank(verifyCode) || verifyCode.length()!=6) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",verifyCode格式错误");
		}
	}

}
