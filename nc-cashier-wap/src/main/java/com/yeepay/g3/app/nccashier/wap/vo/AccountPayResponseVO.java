package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 账户支付支付下单返回值
 * 
 * @author duangduang
 * @date 2017-06-01
 */
public class AccountPayResponseVO extends BasicResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 密码重试次数
	 */
	private Long retryTimes;

	/**
	 * 冻结时间（单位：分钟）
	 */
	private String frozenTime;

	public AccountPayResponseVO() {
		super();
	}

	public AccountPayResponseVO(String bizStatus) {
		super(bizStatus);
	}

	public Long getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Long retryTimes) {
		this.retryTimes = retryTimes;
	}

	public String getFrozenTime() {
		return frozenTime;
	}

	public void setFrozenTime(String frozenTime) {
		this.frozenTime = frozenTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
