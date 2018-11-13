package com.yeepay.g3.facade.nccashier.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.nccashier.enumtype.TimeTypeEnum;

/**
 * 账户支付校验接口返参
 * 
 * @author duangduang
 * @date 2017-06-01
 */
public class AccountPayValidateResponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 密码重试次数
	 */
	private Long retryTimes;

	/**
	 * 冻结时间
	 */
	private Long frozenTime;

	/**
	 * 冻结时间类型（一般是分钟）
	 */
	private TimeTypeEnum frozenTimeType;

	/**
	 * 付款方商编，由商户平台密码校验接口返回
	 */
	private String debitCustomerNo;

	public AccountPayValidateResponseDTO() {
		super();
	}

	public Long getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Long retryTimes) {
		this.retryTimes = retryTimes;
	}

	public TimeTypeEnum getFrozenTimeType() {
		return frozenTimeType;
	}

	public void setFrozenTimeType(TimeTypeEnum frozenTimeType) {
		this.frozenTimeType = frozenTimeType;
	}

	public String getDebitCustomerNo() {
		return debitCustomerNo;
	}

	public void setDebitCustomerNo(String debitCustomerNo) {
		this.debitCustomerNo = debitCustomerNo;
	}
	
	public Long getFrozenTime() {
		return frozenTime;
	}

	public void setFrozenTime(Long frozenTime) {
		this.frozenTime = frozenTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
