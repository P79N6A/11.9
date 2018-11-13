/**
 * 
 */
package com.yeepay.g3.core.payprocessor.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.yeepay.g3.core.payprocessor.dao.*;
import com.yeepay.g3.core.payprocessor.entity.*;
import com.yeepay.g3.core.payprocessor.service.SendMqService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.facade.ncpay.dto.CardInfoDTO;
import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import com.yeepay.g3.facade.ncpay.facade.*;
import com.yeepay.g3.facade.payprocessor.dto.BankCardInfoDTO;
import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.*;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.component.member.facade.G2MemberComponentFacade;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.util.RedisUtil;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.account.pay.facade.AccountPayFacade;
import com.yeepay.g3.facade.account.pay.facade.AccountPayQueryFacade;
import com.yeepay.g3.facade.cwh.facade.BankCardCwhFacade;
import com.yeepay.g3.facade.frontend.facade.FrontendPayFacade;
import com.yeepay.g3.facade.frontend.facade.FrontendQueryFacade;
import com.yeepay.g3.facade.frontend.installment.facade.InstallmentFacade;
import com.yeepay.g3.facade.frontend.installment.facade.InstallmentQueryFacade;
import com.yeepay.g3.facade.frontend.netpay.facade.NetPayFacade;
import com.yeepay.g3.facade.frontend.netpay.facade.NetPayQueryFacade;
import com.yeepay.g3.facade.opr.facade.PaymentFacade;
import com.yeepay.g3.facade.refund.RefundRequestFacade;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 抽象Service， 将Service公用的属性或方法进行封装
 *
 * @author TML
 */

public abstract class AbstractService {

	private static final Logger logger = PayLoggerFactory.getLogger(AbstractService.class);

	/** 定义本地Dao **/

	@Autowired
	protected PaymentRequestDao paymentRequestDao;

	@Autowired
	protected PayRecordDao payRecordDao;

	@Autowired
	protected ReverseRecordDao reverseRecordDao;

	@Autowired
	protected PreAuthReverseRecordDao preAuthReverseRecordDao;

	@Autowired
	private SendMqService sendMqService;

	@Autowired
	protected CombPayRecordDao combPayRecordDao;

	/** 定义外部facade **/

	protected PaymentManagerWrapperFacade paymentManagerWrapperFacade = RemoteFacadeProxyFactory
			.getService(PaymentManagerWrapperFacade.class, ExternalSystem.NCPAY);

	protected PaymentManageFacade paymentManageFacade = RemoteFacadeProxyFactory.getService(PaymentManageFacade.class,
			ExternalSystem.NCPAY);

	protected NcPayResultTaskFacade ncPayResultTaskFacade = RemoteFacadeProxyFactory
			.getService(NcPayResultTaskFacade.class, ExternalSystem.NCPAY);

	protected FrontendPayFacade frontendPayFacade = RemoteFacadeProxyFactory.getService(FrontendPayFacade.class,
			ExternalSystem.FRONTEND);

	protected FrontendQueryFacade frontendQueryFacade = RemoteFacadeProxyFactory.getService(FrontendQueryFacade.class,
			ExternalSystem.FRONTEND);

	protected PaymentFacade paymentFacade = RemoteFacadeProxyFactory.getService(PaymentFacade.class,
			ExternalSystem.OPR);

	protected BankCardCwhFacade bankCardCwhFacade = RemoteFacadeProxyFactory.getService(BankCardCwhFacade.class,
			ExternalSystem.NCCWH);

	protected RefundRequestFacade refundRequestFacade = RemoteFacadeProxyFactory.getService(RefundRequestFacade.class,
			ExternalSystem.REFUND);

	protected NetPayFacade netPayFacade = RemoteFacadeProxyFactory.getService(NetPayFacade.class, ExternalSystem.NETPAY);
	
	protected InstallmentFacade installmentFacade = RemoteFacadeProxyFactory.getService(InstallmentFacade.class, ExternalSystem.CFL);
	
	
	protected NetPayQueryFacade netPayQueryFacade = RemoteFacadeProxyFactory.getService(NetPayQueryFacade.class, ExternalSystem.NETPAY);
	
	protected InstallmentQueryFacade installmentQueryFacade = RemoteFacadeProxyFactory.getService(InstallmentQueryFacade.class, ExternalSystem.CFL);
	
	protected AccountPayFacade accountPayFacade = RemoteFacadeProxyFactory.getService(AccountPayFacade.class, ExternalSystem.ACCOUNTPAY);

	protected AccountPayQueryFacade accountPayQueryFacade = RemoteFacadeProxyFactory.getService(AccountPayQueryFacade.class, ExternalSystem.ACCOUNTPAY);

	protected NcpayCflFacade ncpayCflFacade = RemoteFacadeProxyFactory.getService(NcpayCflFacade.class, ExternalSystem.NCPAY);

	protected NcPayPreAuthFacade ncPayPreAuthFacade = RemoteFacadeProxyFactory.getService(NcPayPreAuthFacade.class, ExternalSystem.NCPAY);
	
	protected GuaranteeCflFacade guaranteeCflFacade = RemoteFacadeProxyFactory.getService(GuaranteeCflFacade.class, ExternalSystem.NCPAY);

	protected G2MemberComponentFacade g2MemberComponentFacade = RemoteFacadeProxyFactory.getService(G2MemberComponentFacade.class, ExternalSystem.PERSONALMEMBER);

	protected NcPayCflEasyFacade ncPayCflEasyFacade = RemoteFacadeProxyFactory.getService(NcPayCflEasyFacade.class, ExternalSystem.NCPAY);

	/**
	 * 接收MQ消息检查支付子表
	 *
	 * @param payRecord
	 */
	protected void checkPayRecord(PayRecord payRecord) {
		if (payRecord == null) {
			logger.error("payRecord not exist");
			throw new PayBizException(ErrorCode.P9002006);
		}
		if (TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())
				|| TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
			logger.error("payRecord already fail or success, " + payRecord.getRecordNo());
			throw new PayBizException(ErrorCode.P9002002);
		}

	}
	
	/**
     * 接收MQ消息为失败时，检查支付子表
     * 子表不存在，或为终态（成功、失败、冲正）均抛出异常
     *
     * @param payRecord
     */
	protected void checkPayRecordForFail(PayRecord payRecord) {
        checkPayRecord(payRecord);
        if(TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
            logger.error("payRecord already reverse, " + payRecord.getRecordNo());
            throw new PayBizException(ErrorCode.P9002005);
        }
        
    }


	/**
	 * 创建退款记录
	 *
	 * @param requestId
	 * @param recordNo
	 * @param remark
	 */
	protected void createRefundRecord(Long requestId, String recordNo, String remark) {
		ReverseRecord reverseRecord = new ReverseRecord();
		reverseRecord.setRecordNo(recordNo);
		reverseRecord.setRefundStatus(RefundStatusEnum.INIT.name());
		reverseRecord.setRequestId(requestId);
		reverseRecord.setRequestTime(new Date());
		reverseRecord.setModifyTime(new Date());
		reverseRecord.setRemark(remark);
		// 预授权订单不走清算，等二期，所有的冲正都不走清算
		PayRecord payRecord = payRecordDao.selectByPrimaryKey(recordNo);
		if(PayOrderType.PREAUTH_RE.name().equals(payRecord.getPayOrderType())) {
			reverseRecord.setRefundStatus(RefundStatusEnum.CSDONE.name());
		}
		reverseRecordDao.insert(reverseRecord);
	}


	/**
	 * 兼容redis和mq通知收银台
	 */
	protected void setPayResultToRedis(String paymentNo) {
		if("REDIS".equals(ConstantUtils.getMQorRedisSwitch())) {
			RedisUtil.pushObjectToRedis("PAYRESULT", paymentNo, "READY", 60);
		}else {
			sendMQMessage(paymentNo);
		}
	}

	/**
	 * 发送MQ消息
	 */
	private void sendMQMessage(String recordNo) {
		try {
			PayRecord payRecord = payRecordDao.selectByPrimaryKey(recordNo);
			PaymentRequest payment = paymentRequestDao.selectByPrimaryKey(payRecord.getRequestId());
			PayRecordResponseDTO responseDTO = buildPayRecordResponse(payRecord, payment);
			sendMqService.sendPayRecordResultMessageMq(responseDTO);
		} catch (Throwable th) {
			logger.error("发送MQ消息至收银台失败，recordNo = " + recordNo, th);
		}

	}

	protected PayRecordResponseDTO buildPayRecordResponse(PayRecord record, PaymentRequest payment) {
		PayRecordResponseDTO responseDTO = new PayRecordResponseDTO();
		responseDTO.setOrderSystemStatus(OrderSystemStatusEnum.getStatus(payment.getOrderSystemStatus()));
		responseDTO.setTrxStatus(TrxStatusEnum.getTrxStatus(record.getStatus()));
		responseDTO.setFrpCode(record.getFrpCode());
		responseDTO.setBankOrderNO(record.getBankOrderNo());
		responseDTO.setCost(record.getCost());
		responseDTO.setCardId(record.getCardId());
		responseDTO.setBankTrxId(record.getBankTrxId());
		if (StringUtils.isNotBlank(record.getSmsState())) {
			responseDTO.setSmsState(SmsCheckResultEnum.valueOf(record.getSmsState()));
		}
		// 查询支付状态，订单失败了，设置错误码和错误描述
		if (!(record.getStatus().equals(TrxStatusEnum.SUCCESS.name())
				|| record.getStatus().equals(TrxStatusEnum.REVERSE.name()))) {
			responseDTO.setResponseCode(record.getErrorCode());
			responseDTO.setResponseMsg(record.getErrorMsg());
		}
		// 分期相关
		responseDTO.setCflCount(record.getCflCount());
		responseDTO.setCflRate(record.getCflRate());
		responseDTO.setMerchantFeeSubsidy(record.getMerchantFeeSubsidy());
		responseDTO.setMerchantAmountSubsidy(record.getMerchantAmountSubsidy());

		responseDTO.setCustomerNo(payment.getCustomerNo());
		responseDTO.setOutTradeNo(payment.getOutTradeNo());
		responseDTO.setRecordNo(record.getRecordNo());
		responseDTO.setPayOrderType(record.getPayOrderType());
		// 如果组合支付
		if(record.isCombinedPay()) {
			CombResponseDTO combResponseDTO = new CombResponseDTO();
			CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(record.getRecordNo());
			if(combPayRecord != null) {
				combResponseDTO.setPayOrderType(combPayRecord.getPayOrderType());
				combResponseDTO.setPayOrderNo(combPayRecord.getPayOrderNo());
				combResponseDTO.setBankOrderNo(combPayRecord.getBankOrderNo());
				combResponseDTO.setAmount(combPayRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
				combResponseDTO.setPaySuccDate(combPayRecord.getPayTime());
				combResponseDTO.setChannelId(combPayRecord.getFrpCode());
				combResponseDTO.setPayProduct(combPayRecord.getPayProduct());
				combResponseDTO.setStatus(combPayRecord.getStatus());
			}
			responseDTO.setCombResponseDTO(combResponseDTO);
			responseDTO.setFirstPayAmount(record.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		return responseDTO;
	}


	/**
	 * 入参组装卡信息
	 */
	protected CardInfoDTO composeCardInfo(BankCardInfoDTO bankCardInfoDTO) {
		CardInfoDTO cardInfoDTO = new CardInfoDTO();
		cardInfoDTO.setBankCode(bankCardInfoDTO.getBankCode());
		cardInfoDTO.setBankName(bankCardInfoDTO.getBankName());
		cardInfoDTO.setCardNo(bankCardInfoDTO.getCardNo());
		cardInfoDTO.setCardType(bankCardInfoDTO.getCardType());
		cardInfoDTO.setExpireDate(bankCardInfoDTO.getExpireDate());
		cardInfoDTO.setCvv2(bankCardInfoDTO.getCvv2());
		cardInfoDTO.setPin(bankCardInfoDTO.getPin());
		cardInfoDTO.setOwner(bankCardInfoDTO.getOwner());
		cardInfoDTO.setBankMobile(bankCardInfoDTO.getBankMobile());
		cardInfoDTO.setIdCard(bankCardInfoDTO.getIdCard());
		cardInfoDTO.setIdCardType(bankCardInfoDTO.getIdCardType());
		return cardInfoDTO;
	}


	/**
	 * 接收MQ消息检查支付子表
	 * 组合支付
	 * @param payRecord
	 */
	protected void checkPayRecordComb(PayRecord payRecord, CombPayRecord combPayRecord) {
		if (payRecord == null) {
			logger.error("payRecord not exist");
			throw new PayBizException(ErrorCode.P9002006);
		}
		if(combPayRecord == null) {
			logger.error("combPayRecord not exist");
			throw new PayBizException(ErrorCode.P9003055);
		}
		if (TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())) {
			logger.error("payRecord already fail, " + payRecord.getRecordNo());
			throw new PayBizException(ErrorCode.P9002003);
		}
		if (TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
			if(CombTrxStatusEnum.DEPOSIT.name().equals(combPayRecord.getStatus())) {
				// 此处不处理，后续统一方法中调扣款接口
			}else if(CombTrxStatusEnum.SUCCESS.name().equals(combPayRecord.getStatus())) {
				logger.error("payRecord already fail, " + payRecord.getRecordNo());
				throw new PayBizException(ErrorCode.P9002002);
			}
		}

	}
}
