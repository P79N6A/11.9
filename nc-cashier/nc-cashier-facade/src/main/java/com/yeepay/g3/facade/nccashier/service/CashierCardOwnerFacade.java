package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.CardOwnerConfirmResDTO;
import com.yeepay.g3.facade.nccashier.dto.Person;

/**
 * 持卡人信息服务
 * @since 2016-11-21
 * @author xueping.ni
 * 
 *
 */
public interface CashierCardOwnerFacade {

	/**
	 * 设置同人信息
	 * @param person
	 */
	public void setCardOwner(String bindId, String name, String idCardNo,long paymentRequestId);
	/**
	 * 解绑因隐形透传支付身份导致交易失败的绑卡
	 * @param paymentRequestId
	 * @param paymentOrderNo
	 */
	public void unbindCard(long paymentRequestId, String paymentOrderNo);
	/**
	 * 预留内部运营解绑卡接口
	 * @param bindId
	 */
	public void unbindCardForInner(long paymentRequestId, long bindId);
	/**
	 * 获取支付身份信息（新）
	 * @param paymentRequestId
	 * @param paymentRecordId
	 * @return
	 */
	public CardOwnerConfirmResDTO getCardOwners(long paymentRequestId,
			long paymentRecordId);
	/**
	 * 解绑卡（新）
	 * @param paymentRequestId
	 * @param paymentRecordId
	 */
	public void unbindCard(long paymentRequestId, long paymentRecordId);
	/**
	 * 定时自动设置同人限制值
	 */
	
	public  void autosetSamePeronInfo();

	/**
	 * 绑卡支付，在已绑卡列表，主动解绑选中的卡
	 * @param paymentRequestId
	 * @param bindId
	 */
	void unbindCardActive(String paymentRequestId, String bindId);
}
