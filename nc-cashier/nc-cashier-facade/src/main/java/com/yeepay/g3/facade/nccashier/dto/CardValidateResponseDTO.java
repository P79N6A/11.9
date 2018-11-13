package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 银行卡必填项结果
* @author zhen.tan
* @since：2016年5月27日 上午10:27:39
*/
public class CardValidateResponseDTO extends BasicResponseDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;

	/**
	 * 支付请求ID
	 */
	private long requestId;

	/**
	 * 
	 * 卡必填项标识
	 */
	private NeedValidatesDTO needValidatesDTO;

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public NeedValidatesDTO getNeedValidatesDTO() {
		return needValidatesDTO;
	}

	public void setNeedValidatesDTO(NeedValidatesDTO needValidatesDTO) {
		this.needValidatesDTO = needValidatesDTO;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CardValidateResponseDTO{");
		sb.append("requestId='").append(this.requestId).append('\'');
		sb.append(", needValidatesDTO='")
				.append(this.needValidatesDTO == null ? "" : this.needValidatesDTO.toString())
				.append('\'');
		sb.append("," + super.toString());
		sb.append('}');
		return sb.toString();
	}
}
