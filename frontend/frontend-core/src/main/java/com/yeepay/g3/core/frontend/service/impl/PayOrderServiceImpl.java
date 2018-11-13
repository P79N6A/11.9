/**
 * 
 */
package com.yeepay.g3.core.frontend.service.impl;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.service.PayOrderService;

import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author TML
 *
 */
@Service
public class PayOrderServiceImpl extends AbstractService implements PayOrderService{

	@Override
	public void createPayOrder(PayOrder payOrder) {
		if(payOrder != null){
			Timestamp createTime = new Timestamp(System.currentTimeMillis());
			payOrder.setModifyTime(createTime);
			payOrderDao.add(payOrder);
		}
	}

	@Override
	public void updatePayOrder(PayOrder payOrder) {
		if(payOrder != null){
			Timestamp modifyTime = new Timestamp(System.currentTimeMillis());
			payOrder.setModifyTime(modifyTime);
			payOrderDao.update(payOrder);
		}
	}

	
	@Override
	public PayOrder queryBySystemAndRequestId(String requestSystem, String requestId, String platformType) {
		List<PayOrder> list = payOrderDao.queryBySystemAndRequestId(requestSystem, requestId, platformType);
		if(list.isEmpty()){
			return null;
		}
		PayOrder payOrder = list.get(0);
		payOrder.setTotalAmount(payOrder.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
		return payOrder;
	}
	
	@Override
	public PayOrder queryByOrderNo(String orderNo) {
		List<PayOrder> list = payOrderDao.queryByOrderNo(orderNo, null);
		if(list.isEmpty()){
			return null;
		}
		PayOrder payOrder = list.get(0);
		payOrder.setTotalAmount(payOrder.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
		return payOrder;
	}
	
	@Override
	public PayOrder queryByOrderNo(String orderNo, String platformType) {
		List<PayOrder> list = payOrderDao.queryByOrderNo(orderNo, platformType);
		if(list.isEmpty()){
			return null;
		}
		PayOrder payOrder = list.get(0);
		payOrder.setTotalAmount(payOrder.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
		return payOrder;
	}

	@Override
	public List<PayOrder> queryUnRefundByDate(Date start, Date end, String platformType) {
		return payOrderDao.queryUnRefundByDate(start,end, platformType);
	}
	
	@Override
	public List<PayOrder> queryUnSuccessByDate(Date start, Date end, String platformType){
		return payOrderDao.queryUnSuccessByDate(start, end, platformType);
	}

	@Override
	public void singleUpdate(PayOrder order) {
		if (order!=null){
			payOrderDao.update(order);
		}
	}

	@Override
	public List<PayOrder> queryUnNotifyByDate(Date start, Date end, String platformType) {
		return payOrderDao.queryUnNotifyByDate(start, end, platformType);
	}

	@Override
	public PayOrder queryByPayOrderNo(String payOrderNo, String platformType) {
		List<PayOrder> list = payOrderDao.listByPayOrderNo(payOrderNo, platformType);
		if(list.isEmpty()){
			return null;
		}
		PayOrder payOrder = list.get(0);
		payOrder.setTotalAmount(payOrder.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
		return payOrder;
	}
}
