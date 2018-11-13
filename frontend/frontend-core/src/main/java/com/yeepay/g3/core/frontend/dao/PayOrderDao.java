/**
 * 
 */
package com.yeepay.g3.core.frontend.dao;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.utils.persistence.GenericDao;

import java.util.Date;
import java.util.List;

/**
 * 支付订单表实体
 * @author TML
 */
public interface PayOrderDao extends GenericDao<PayOrder> {

	
	/**
	 * 根据业务方订单号、业务方查询支付订单
	 * @param requestSystem
	 * @param requestId
	 * @return
	 */
	List<PayOrder> queryBySystemAndRequestId(String requestSystem,String requestId, String platformType);
	
	/**
	 * 修改查询支付订单
	 * @param payOrder
	 */
	void update(PayOrder payOrder);
	
	/**
	 * 查询订单根据订单号
	 * @param orderNo
	 * @return
	 */
	List<PayOrder> queryByOrderNo(String orderNo, String platformType);

	/**
	 * 根据支付订单修改时间查询未退款订单
	 * @param start
	 * @param end
     * @return
     */
	List<PayOrder> queryUnRefundByDate(Date start, Date end, String platformType);
	
	/**
	 * 根据订单创建时间查询未支付成功订单
	 * @param start
	 * @param end
     * @return
     */
	List<PayOrder> queryUnSuccessByDate(Date start, Date end, String platformType);

	/**
	 * 根据订单创建时间查询未通知的成功订单
	 * @param start
	 * @param end
	 * @return
	 */
	List<PayOrder> queryUnNotifyByDate(Date start, Date end, String platformType);

	/**
	 * 根据支付订单号查询支付订单
	 * @param payOrderNo
	 * @param platformType
	 * @return
	 */
	List<PayOrder> listByPayOrderNo(String payOrderNo, String platformType);
}
