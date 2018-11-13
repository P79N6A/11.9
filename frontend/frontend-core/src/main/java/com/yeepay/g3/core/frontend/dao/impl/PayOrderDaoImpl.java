/**
 * 
 */
package com.yeepay.g3.core.frontend.dao.impl;

import com.yeepay.g3.core.frontend.dao.PayOrderDao;
import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.facade.frontend.enumtype.NotifyStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.RefundStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.persistence.mybatis.GenericDaoDefault;
import com.yeepay.utils.jdbc.dal.analyzer.sql.OperatorTypeEnum;
import com.yeepay.utils.jdbc.dal.routing.DALCondition;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TML
 */
@Repository
public class PayOrderDaoImpl extends GenericDaoDefault<PayOrder> implements PayOrderDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PayOrder> queryBySystemAndRequestId(String requestSystem, String requestId, String platformType) {
		Map<String, String> params = new HashMap<String, String>();
        params.put("requestSystem", requestSystem);
        params.put("requestId", requestId);
        if(StringUtils.isNotBlank(platformType)){
        	DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
        }
		return (List<PayOrder>)this.query("queryBySystemAndRequestId", params);
	}

	@Override
	public void update(PayOrder payOrder) {
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, payOrder.getPlatformType());
		this.update("update", payOrder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PayOrder> queryUnRefundByDate(Date start, Date end, String platformType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("refundDateStart",start);
		params.put("refundDateEnd",end);
		params.put("refundStatus", RefundStatusEnum.INIT.name());
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
		return (List<PayOrder>)this.query("queryUnRefundByDate",params);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PayOrder> queryByOrderNo(String orderNo, String platformType) {
		if(StringUtils.isNotBlank(platformType)){
        	DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
        }
		return (List<PayOrder>)this.query("queryByOrderNo", orderNo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PayOrder> queryUnSuccessByDate(Date start, Date end, String platformType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("createTimeStart",start);
		params.put("createTimeEnd",end);
		params.put("payStatus", PayStatusEnum.INIT.name());
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
		return (List<PayOrder>)this.query("queryUnSuccessByDate",params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PayOrder> queryUnNotifyByDate(Date start, Date end, String platformType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("modifyTimeStart",start);
		params.put("modifyTimeEnd",end);
		params.put("payStatus", PayStatusEnum.SUCCESS.name());
		params.put("notifyStatus", NotifyStatusEnum.INIT.name());
		DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
		return (List<PayOrder>)this.query("queryUnNotifyByDate",params);
	}

	@Override
	public List<PayOrder> listByPayOrderNo(String payOrderNo, String platformType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("payOrderNo", payOrderNo);
		if(StringUtils.isNotBlank(platformType)){
			DALCondition.setCondition(OperatorTypeEnum.EQUALS, platformType);
		}
		return (List<PayOrder>)this.query("listByPayOrderNo", params);
	}
}
