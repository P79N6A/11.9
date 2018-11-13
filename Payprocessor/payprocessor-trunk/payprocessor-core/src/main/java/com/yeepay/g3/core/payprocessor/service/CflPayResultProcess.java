
package com.yeepay.g3.core.payprocessor.service;

import java.sql.Timestamp;
import java.util.Date;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;

/**
 * @author peile.fan
 *
 */
@Service
public class CflPayResultProcess extends AbstractService {

	@Autowired
	private ResultProcessService resultProcessService;
	
	@Autowired
    private NotifyService notifyService;

	@Autowired
	private CombAbstractService combAbstractService;
	
	/**
	 * @param resultMessage
	 */
	public void processForCflPayMsg(InstallmentResultMessage resultMessage, PayRecord payRecord) {
		// 查询支付子表
		checkPayRecord(payRecord);
		processPayRecord(payRecord, resultMessage);
		if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
			//更新信息，然后发起退款
			//判断是否发起过退款
			if(reverseRecordDao.queryByRecordNo(payRecord.getRecordNo())==null){
				payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
				createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正退款");
			}
		}else if(TrxStatusEnum.DOING.name().equals(payRecord.getStatus())){
			resultProcessService.updatePaymentToSuccess(payRecord);
			notifyService.notify(payRecord);
			setPayResultToRedis(resultMessage.getRequestId());
		}
	}

	private void processPayRecord(PayRecord payRecord, InstallmentResultMessage resultMessage) {
		payRecord.setPlatformType(resultMessage.getPlatformType());
		payRecord.setBankId(resultMessage.getPayBank());
		payRecord.setCardType(resultMessage.getPayBankcardType());
		payRecord.setCost(resultMessage.getBankTotalCost());
		payRecord.setPayTime(resultMessage.getPaySuccessTime());
		payRecord.setFrpCode(resultMessage.getPayInterface());
		payRecord.setBankOrderNo(resultMessage.getOrderNo());//消费金融编号
		payRecord.setBankTrxId(resultMessage.getTransactionId());//分期公司编号
		payRecord.setLoanCompany(resultMessage.getCompany());
		payRecord.setLoanTerm(resultMessage.getLoanTerm());
		payRecord.setUpdateTime(new Date());
		//如果下单的时候，没有传basicProductCode，底层支付系统会补充
		//将底层支付系统补充的，回写到支付处理器的数据库中
		if(StringUtils.isBlank(payRecord.getBasicProductCode())) {
			payRecord.setBasicProductCode(resultMessage.getBasicProductCode());
		}
	}


	/**
	 * 添加组合支付
	 */
	public void processForCflPayMsgComb(InstallmentResultMessage resultMessage, PayRecord payRecord) {
		// 查询支付子表
		CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
		checkPayRecordComb(payRecord, combPayRecord);
		processPayRecord(payRecord, resultMessage);
		combAbstractService.processCombResult(payRecord, combPayRecord);
	}
}
