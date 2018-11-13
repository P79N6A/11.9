package com.yeepay.g3.facade.nccashier.dto;

import java.math.BigDecimal;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 预授权完成请求入参
 * 
 * @author duangduang
 *
 */
public class APIPreauthCompleteRequestDTO extends APIBasicRequestDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 支付处理器的支付子订单号
	 */
	private String payOrderId;

	/**
	 * 预授权完成要支付的金额
	 */
	private BigDecimal amount;

	public APIPreauthCompleteRequestDTO() {

	}

	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public void validate() {
		super.validate();
		if (StringUtils.isBlank(payOrderId)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",payOrderId不能为空");
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", amount格式不合法");
		}
	}

	@Override
	public String toString() {
		return "APIPreauthCompleteRequestDTO [payOrderId=" + payOrderId 
				+ ", amount=" + amount 
				+ ", bizType=" + getBizType()
				+ ", merchantNo=" + getMerchantNo()
				+ ", token=" + getToken()
				+ ", version=" + getVersion()
				+ "]";
	}
	
	

}
