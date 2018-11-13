package com.yeepay.g3.core.nccashier.service;

import java.util.List;

import com.yeepay.g3.facade.nccashier.dto.Person;

public interface CashierCardOwnerService {
	/**
	 * 判断该笔交易是否需要选择证件号和姓名
	 * @param paymentRequestId
	 * @return
	 */
	public boolean needChooseCardOwner(String paymentRequestId);
	/**
	 * 获取绑卡的持卡人信息
	 * @param paymentRequestId
	 * @return
	 */
	public List<Person> getCardOwners(String paymentRequestId);

}
