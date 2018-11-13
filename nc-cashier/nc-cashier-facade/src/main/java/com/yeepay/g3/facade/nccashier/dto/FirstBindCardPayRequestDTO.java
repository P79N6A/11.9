package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

/**
 * 绑卡支付之首次支付请求DTO
 * 
 * @author duangduang
 *
 */
public class FirstBindCardPayRequestDTO extends BasicPayRequestDTO {

	private static final long serialVersionUID = 1L;

	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "bindId未传")
	private Long bindId;

	/**
	 * 补充项
	 */
	private NeedBankCardDTO needBankCardDTO;

	public NeedBankCardDTO getNeedBankCardDTO() {
		return needBankCardDTO;
	}

	public void setNeedBankCardDTO(NeedBankCardDTO needBankCardDTO) {
		this.needBankCardDTO = needBankCardDTO;
	}

	public Long getBindId() {
		return bindId;
	}

	public void setBindId(Long bindId) {
		this.bindId = bindId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("FirstBindCardPayRequestDTO{");
		sb.append("needBankCardDTO='").append(needBankCardDTO).append('\'');
		sb.append(", bindId='").append(bindId).append('\'');
		sb.append(", requestId='").append(this.getRequestId()).append('\'');
		sb.append(", recordId='").append(this.getRecordId()).append('\'');
		sb.append(", tokenId='").append(this.getTokenId());
		sb.append('}');
		return sb.toString();
	}

}
