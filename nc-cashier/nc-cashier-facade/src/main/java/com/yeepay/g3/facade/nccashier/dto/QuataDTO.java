package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * Created by xiewei on 15-10-19.
 */
public class QuataDTO implements Serializable{
	private static final long serialVersionUID = 7597853682229458911L;
	private long orderQuotaDou;
	private long dayQuotaDou;
	private long monthQuotaDou;

	public long getOrderQuotaDou() {
		return orderQuotaDou;
	}

	public void setOrderQuotaDou(long orderQuotaDou) {
		this.orderQuotaDou = orderQuotaDou;
	}

	public long getDayQuotaDou() {
		return dayQuotaDou;
	}

	public void setDayQuotaDou(long dayQuotaDou) {
		this.dayQuotaDou = dayQuotaDou;
	}

	public long getMonthQuotaDou() {
		return monthQuotaDou;
	}

	public void setMonthQuotaDou(long monthQuotaDou) {
		this.monthQuotaDou = monthQuotaDou;
	}
}
