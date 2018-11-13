package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.ExtendedInfo;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.dto.PayResultMessage;
import com.yeepay.g3.facade.frontend.dto.PromotionInfoDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理FE的消息回调
 * 
 * @author yp-tc-m-2804
 *
 */
@Service
public class FeResultProccess extends AbstractService {

	@Autowired
	private ResultProcessService resultProcessService;

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private CombAbstractService combAbstractService;

	public void processForFePayMsg(PayResultMessage resultMessage, PayRecord payRecord) {
		// 查询支付子表
		checkPayRecord(payRecord);
		processPayRecord(payRecord, resultMessage);
		if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
			// 更新信息，然后发起退款
			// 判断是否发起过退款
			if (reverseRecordDao.queryByRecordNo(payRecord.getRecordNo()) == null) {
				payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
				createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正退款");
			}
		} else if (TrxStatusEnum.DOING.name().equals(payRecord.getStatus())) {
			resultProcessService.updatePaymentToSuccess(payRecord);
			Map<String,Object> extMap = new HashMap<String, Object>();
			List<PromotionInfoDTO> promotionInfoDTOS = resultMessage.getPromotionInfoDTOS();
			extMap.put(ConstantUtils.PROMOTION_CASH_FEE,resultMessage.getCashFee());
			extMap.put(ConstantUtils.PROMOTION_SETTLEMENT_FEE,resultMessage.getSettlementFee());
			extMap.put(ConstantUtils.PROMOTION_INFO_DTOS,promotionInfoDTOS);
			notifyService.notify(payRecord, extMap);
			setPayResultToRedis(resultMessage.getRequestId());
		}
	}

	public PayRecord processForFePayResponse(PayResponseDTO payResponseDTO, PayRecord payRecord) {
		// 查询支付子表
		checkPayRecord(payRecord);
		PayResultMessage resultMessage = transformResultMessage(payResponseDTO);
		processPayRecord(payRecord, resultMessage);
		if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
			// 更新信息，然后发起退款
			// 判断是否发起过退款
			if (reverseRecordDao.queryByRecordNo(payRecord.getRecordNo()) == null) {
				payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
				createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正退款");
			}
		} else if (TrxStatusEnum.DOING.name().equals(payRecord.getStatus())) {
			resultProcessService.updatePaymentToSuccess(payRecord);
			Map<String,Object> extMap = new HashMap<String, Object>();
			List<PromotionInfoDTO> promotionInfoDTOS = resultMessage.getPromotionInfoDTOS();
			extMap.put(ConstantUtils.PROMOTION_CASH_FEE,resultMessage.getCashFee());
			extMap.put(ConstantUtils.PROMOTION_SETTLEMENT_FEE,resultMessage.getSettlementFee());
			extMap.put(ConstantUtils.PROMOTION_INFO_DTOS,promotionInfoDTOS);
			notifyService.notify(payRecord, extMap);
		}
		return payRecord;
	}

	/**
	 * @param payResponseDTO
	 * @return
	 */
	private PayResultMessage transformResultMessage(PayResponseDTO payResponseDTO) {
		PayResultMessage resultMessage = new PayResultMessage();

		resultMessage.setPayStatus(payResponseDTO.getPayStatus());
		resultMessage.setBankTotalCost(payResponseDTO.getBankTotalCost());
		resultMessage.setPayBank(payResponseDTO.getPayBank());
		resultMessage.setPayBankcardType(payResponseDTO.getPayBankcardType());
		resultMessage.setPayInterface(payResponseDTO.getPayInterface());
		resultMessage.setTransactionId(payResponseDTO.getTransactionId());
		resultMessage.setPlatformType(payResponseDTO.getPlatformType());
		resultMessage.setPaySuccessTime(new Date());
		resultMessage.setOrderNo(payResponseDTO.getOrderNo());
		resultMessage.setOpenId(payResponseDTO.getOpenId());
		resultMessage.setCashFee(payResponseDTO.getCashFee());
		resultMessage.setSettlementFee(payResponseDTO.getSettlementFee());
		resultMessage.setPromotionInfoDTOS(payResponseDTO.getPromotionInfoDTOS());

		return resultMessage;
	}

	private void processPayRecord(PayRecord payRecord, PayResultMessage resultMessage) {
		payRecord.setBankId(resultMessage.getPayBank());
		payRecord.setBankOrderNo(resultMessage.getOrderNo());
		payRecord.setBankTrxId(resultMessage.getTransactionId());
		payRecord.setCost(resultMessage.getBankTotalCost());
		payRecord.setPayTime(resultMessage.getPaySuccessTime());
		payRecord.setCardType(
				resultMessage.getPayBankcardType() == null ? null : resultMessage.getPayBankcardType().name());
		payRecord.setFrpCode(resultMessage.getPayInterface());
		payRecord.setUpdateTime(new Date());


/*		Map<String, String> promotionInfoMap = new HashMap<String, String>();
		//added by zengzhi.han 20181019 卡券 现金支付金额
		if (resultMessage.getCashFee() != null) {
			payRecord.setCashFee(resultMessage.getCashFee().toString());
		}
		//added by zengzhi.han 20181019 卡券 应结算金额
		if (resultMessage.getSettlementFee() != null) {
			payRecord.setSettlementFee(resultMessage.getSettlementFee().toString());
		}
		//added by zengzhi.han 20181019 卡券 优惠券信息
		//TODO: 新加优惠券表，批量写入
		if (resultMessage.getPromotionInfoDTOS() != null && !resultMessage.getPromotionInfoDTOS().isEmpty()) {
			promotionInfoMap.put(ConstantUtils.PROMOTION_INFO_DTOS, JSONObject.toJSONString(resultMessage.getPromotionInfoDTOS()));
		}*/



		//add by xueping.ni 增加银联主被扫消费凭证信息
		Map<String,String> extParam = resultMessage.getExtParam();
		if(MapUtils.isNotEmpty(extParam)){
			ExtendedInfo extendInfo = payRecord.getExtendedInfo();
			if(null==payRecord.getExtendedInfo()){
				extendInfo = new ExtendedInfo();
			}
			if(StringUtils.isNotBlank(extParam.get("bankTradeId"))){
				extendInfo.setBankTradeId(extParam.get("bankTradeId"));
				extParam.remove("bankTradeId");
			}
			if(StringUtils.isNotBlank(extParam.get("couponInfo"))){
				extendInfo.setCouponInfo(extParam.get("couponInfo"));
				extParam.remove("couponInfo");
			}
			if(StringUtils.isNotBlank(extParam.get("payerBankAccountNo"))){
				extendInfo.setPayerBankAccountNo(extParam.get("payerBankAccountNo"));
				extParam.remove("payerBankAccountNo");
			}
			if(StringUtils.isNotBlank(extParam.get("reportMerchantNo"))){
				extendInfo.setReportMerchantNo(extParam.get("reportMerchantNo"));
				extParam.remove("reportMerchantNo");
			}
			if(extendInfo.getExtParam() == null){
				extendInfo.setExtParam(extParam);
			}else {
				extendInfo.getExtParam().putAll(extParam);
			}

			payRecord.setExtendedInfo(extendInfo);
		}

		//如果下单的时候，没有传basicProductCode，底层支付系统会补充
		//将底层支付系统补充的，回写到支付处理器的数据库中
		if(StringUtils.isBlank(payRecord.getBasicProductCode())) {
			payRecord.setBasicProductCode(resultMessage.getBasicProductCode());
		}
		payRecord.setOpenId(resultMessage.getOpenId());
	}


	/**
	 * 回调处理逻辑
	 * @param resultMessage
	 * @param payRecord
	 */
	public void processForFePayMsgComb(PayResultMessage resultMessage, PayRecord payRecord) {
		CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
		checkPayRecordComb(payRecord, combPayRecord);
		processPayRecord(payRecord, resultMessage);
		combAbstractService.processCombResult(payRecord, combPayRecord);
	}

	public void processForFePayResponseComb(PayResponseDTO payResponseDTO, PayRecord payRecord) {
		CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
		checkPayRecordComb(payRecord, combPayRecord);
		PayResultMessage resultMessage = transformResultMessage(payResponseDTO);
		processPayRecord(payRecord, resultMessage);
		combAbstractService.processCombResult(payRecord, combPayRecord);
	}

}
