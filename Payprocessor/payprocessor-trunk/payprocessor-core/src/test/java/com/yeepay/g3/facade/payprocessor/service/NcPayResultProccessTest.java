/**
 * 
 */
package com.yeepay.g3.facade.payprocessor.service;

import com.yeepay.g3.core.payprocessor.dao.PayRecordDao;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.service.NcPayResultProccess;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;

/**
 * @author peile.fan
 *
 */
public class NcPayResultProccessTest extends BaseTest {
	@Autowired
	private NcPayResultProccess ncPayResultProccess;

	@Autowired
	private PayRecordDao payRecordDao;

	@Test
	public void testProcessForNcPayMsg() {
		PaymentResultMessage msg = new PaymentResultMessage();
//		msg.setAccountNo("11109316010711388103");
//		msg.setBankCode("CMBCHINA");
//		msg.setBankName("招商银行");
		msg.setBankOrderNo("031978");
		msg.setBizOrderNum("CFL1710121037375062095");
		msg.setCost(new Amount("5.88")) ;
		msg.setFrpCode("BJ_PSBC_KUAIPAY_J_Z001");
		msg.setPayCompleteDate(1479467389211L);
		msg.setTradeSerialNo("16111819004933022");
		msg.setPayStatus(PaymentOrderStatusEnum.SUCCESS);
//		msg.setErrorCode("411151");
//		msg.setErrorMsg("可用余额不足，请联系发卡银行");
		PayRecord payRecord = payRecordDao.selectByPrimaryKey("SALE1806211941188787981");
		ncPayResultProccess.processForNcPayMsgComb(msg, payRecord);
	}
}
