package com.yeepay.g3.core.payprocessor.service;

import java.sql.Timestamp;
import java.util.Date;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import com.yeepay.g3.facade.payprocessor.enumtype.CombTrxStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 个人会员支付通知业务方
 * 
 * @author：zhangxh
 * @since：2017年5月27日 下午6:05:08
 * @version:
 */
@Service
public class PersonalMemberPayResultProccess extends AbstractService {
	private static final Logger logger = PayLoggerFactory
			.getLogger(PersonalMemberPayResultProccess.class);
	@Autowired
	private ResultProcessService resultProcessService;

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private CombAbstractService combAbstractService;

	public TrxStatusEnum processForSyncPaySuccess(
			String recordNo,String paymentFlowId) {
		// 更新状态
		PayRecord dbRecord = payRecordDao.selectByPrimaryKey(recordNo);
		if (TrxStatusEnum.REVERSE.name().equals(dbRecord.getStatus())) {
			 // 更新信息，然后发起退款
            // 判断是否发起过退款
            if (reverseRecordDao.queryByRecordNo(dbRecord.getRecordNo()) == null) {
                payRecordDao.updatePaymentBankInfo(dbRecord, new Timestamp(System.currentTimeMillis()));
                createRefundRecord(dbRecord.getRequestId(), dbRecord.getRecordNo(), "冲正或差错退款");
            }
			return TrxStatusEnum.REVERSE;
		} else if (TrxStatusEnum.SUCCESS.name().equals(dbRecord.getStatus())) {
			try {
				// 通知订单方
				notifyService.notify(dbRecord);
			} catch (Exception e) {
				logger.info("PayBizException: ", e);
			}
		} else {
			try {
				processPayRecord(dbRecord, paymentFlowId);
				resultProcessService.updatePaymentToSuccess(dbRecord);
			} catch (Exception e) {
				logger.info("PayBizException: ", e);
				dbRecord = payRecordDao.selectByPrimaryKey(recordNo);
			}
			try {
				// 通知订单方
				notifyService.notify(dbRecord);
			} catch (Exception e) {
				logger.info("PayBizException: ", e);
			}
		}
		String updateRecordStatus = payRecordDao.queryStatus(recordNo);
		return TrxStatusEnum.valueOf(updateRecordStatus);

	}


	/**
	 * 添加组合支付
	 */
	public TrxStatusEnum processForSyncPaySuccessComb(String recordNo,String paymentFlowId) {
		// 更新状态
		PayRecord dbRecord = payRecordDao.selectByPrimaryKey(recordNo);
		CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(recordNo);
		// 已成功，重新发通知（因为补单接口也是用的这个逻辑）
		if (TrxStatusEnum.SUCCESS.name().equals(dbRecord.getStatus()) &&
				CombTrxStatusEnum.SUCCESS.name().equals(combPayRecord.getStatus())) {
			try {
				// 通知订单方
				notifyService.notify(dbRecord);
			} catch (Exception e) {
				logger.info("PayBizException: ", e);
			}
		}else {
			processPayRecord(dbRecord, paymentFlowId);
			combAbstractService.processCombResult(dbRecord, combPayRecord);
		}

		String updateRecordStatus = payRecordDao.queryStatus(recordNo);
		return TrxStatusEnum.valueOf(updateRecordStatus);

	}



	private void processPayRecord(PayRecord payRecord,
			String paymentFlowId) {
		payRecord.setPayTime(new Date());
		payRecord.setUpdateTime(new Date());
		payRecord.setBankOrderNo(paymentFlowId);
//		payRecord.setBankOrderNo(payRecord.getRecordNo());
		payRecord.setFrpCode(PayOrderType.MEMBER_PAY.name());//frp_code通道编码 60个长度
	}
}
