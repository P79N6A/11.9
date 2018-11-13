/**
 * 
 */
package com.yeepay.g3.core.frontend.service;

import com.yeepay.g3.core.frontend.entity.PayOrder;

import java.util.Date;
import java.util.List;

/**
 * @author TML
 *
 */
public interface PayOrderService {
	
	/**
	 * 创建订单
	 * @param payOrder
	 */
	public void createPayOrder(PayOrder payOrder);
	
	/**
	 * 更新订单表
	 * @param payOrder
	 */
	public void updatePayOrder(PayOrder payOrder);
	
	/**
	 * 根据业务方订单号、业务方查询支付订单
	 * @param requestSystem
	 * @param requestId
	 * @return
	 */
//	public PayOrder queryBySystemAndRequestId(String requestSystem,String requestId);
	
	/**
	 * 根据业务方订单号、业务方查询支付订单
	 * @param requestSystem
	 * @param requestId
	 * @return
	 */
	public PayOrder queryBySystemAndRequestId(String requestSystem,String requestId, String platformType);

	/**
	 * 根据银行子系统订单号查询订单
	 * @param orderNo
	 * @return
     */
	PayOrder queryByOrderNo(String orderNo);
	
	/**
	 * 根据银行子系统订单号查询订单
	 * @param orderNo
	 * @return
     */
	PayOrder queryByOrderNo(String orderNo, String platformType);

	/**
	 * @Deprecated
	 * 根据订单ID查询订单
	 * 该方法必须废弃! 分表情况下通过id获取数据,但不指定路由条件 出大问题!!!
	 * @param
	 * @return
     */
//	PayOrder get(Long orderId);
	
	/**
	 * 单纯更新订单表,不做任何赋值处理
	 * @param order
	 */
	void singleUpdate(PayOrder order);
	
	/**
	 * 根据订单创建时间查询未支付成功订单
	 * @param start
	 * @param end
     * @return
     */
	List<PayOrder> queryUnSuccessByDate(Date start, Date end, String platformType);

	/**
	 * 根据订单修改时间查询未退款的订单
	 * @param start
	 * @param end
     * @return
     */
	List<PayOrder> queryUnRefundByDate(Date start, Date end, String platformType);

	/**
	 * 根据订单创建时间查询未通知的成功订单
	 * @param start
	 * @param end
	 * @return
	 */
	List<PayOrder> queryUnNotifyByDate(Date start, Date end, String platformType);
	/**
	 * 根据fe支付订单号查询
	 * @param payOrderNo
	 * @param platformType
	 * @return
	 */
	PayOrder queryByPayOrderNo(String payOrderNo, String platformType);
}
