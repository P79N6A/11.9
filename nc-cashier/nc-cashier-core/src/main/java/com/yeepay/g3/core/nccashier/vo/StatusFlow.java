package com.yeepay.g3.core.nccashier.vo;

import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;

/**
 * 状态流
 * 
 * @author duangduang
 *
 */
public class StatusFlow {

	private static Map<OrderAction, PayRecordStatusEnum[]> statusFlows = new HashMap<OrderAction, PayRecordStatusEnum[]>();

	static {

		/** paying状态的不能再创建paymentRecord&下单、发短验证、确认支付 **/

		// 如果paymentRecord状态为初始化，允许重新下单
		PayRecordStatusEnum[] orderStatusEnums = { PayRecordStatusEnum.INIT };
		statusFlows.put(OrderAction.ORDER, orderStatusEnums);

		PayRecordStatusEnum[] smsSendStatusEnums = { PayRecordStatusEnum.ORDERED, PayRecordStatusEnum.SMS_SEND };
		statusFlows.put(OrderAction.SEND_SMS, smsSendStatusEnums);

		PayRecordStatusEnum[] confirmStatusEnums = { PayRecordStatusEnum.SMS_SEND };
		statusFlows.put(OrderAction.CONFIRM_PAY, confirmStatusEnums);
		
		PayRecordStatusEnum[] preauthOrderStatusEnums = { PayRecordStatusEnum.INIT };
		statusFlows.put(OrderAction.YSQ_ORDER, preauthOrderStatusEnums);

		PayRecordStatusEnum[] preauthSmsSendStatusEnums = { PayRecordStatusEnum.ORDERED, PayRecordStatusEnum.SMS_SEND };
		statusFlows.put(OrderAction.YSQ_SEND_SMS, preauthSmsSendStatusEnums);

		PayRecordStatusEnum[] preauthConfirmStatusEnums = { PayRecordStatusEnum.ORDERED, PayRecordStatusEnum.SMS_SEND };
		statusFlows.put(OrderAction.YSQ_CONFIRM_PAY, preauthConfirmStatusEnums);

		PayRecordStatusEnum[] APIYJZFSendSMSStatusEnums = { PayRecordStatusEnum.ORDERED, PayRecordStatusEnum.SMS_SEND };
		statusFlows.put(OrderAction.API_YJZF_SEND_SMS, APIYJZFSendSMSStatusEnums);

		PayRecordStatusEnum[] APIYJZFConfirmStatusEnums = { PayRecordStatusEnum.ORDERED, PayRecordStatusEnum.SMS_SEND };
		statusFlows.put(OrderAction.API_YJZF_CONFIRM_PAY, APIYJZFConfirmStatusEnums);


	}

	/**
	 * 校验执行动作=orderAction时，当前的status是否在允许的状态流里
	 * 
	 * @param status
	 * @param orderAction
	 * @return
	 */
	public static boolean legalStatus(PayRecordStatusEnum status, OrderAction orderAction) {
		// 没有状态要求
		if (orderAction == null || status == null) {
			return true;
		}
		PayRecordStatusEnum[] statusEnums = statusFlows.get(orderAction);
		// 没有状态要求
		if (statusEnums == null || statusEnums.length == 0) {
			return true;
		}
		for (PayRecordStatusEnum statusEnum : statusEnums) {
			if (status == statusEnum) {
				return true;
			}
		}
		return false;
	}

	public static Map<OrderAction, PayRecordStatusEnum[]> getStatusFlows() {
		return statusFlows;
	}

	public static void setStatusFlows(Map<OrderAction, PayRecordStatusEnum[]> statusFlows) {
		StatusFlow.statusFlows = statusFlows;
	}

}
