package com.yeepay.g3.core.nccashier.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.yeepay.g3.component.open.dto.QueryAccountRequestDTO;
import com.yeepay.g3.component.open.dto.QueryAccountResponseDTO;
import com.yeepay.g3.core.nccashier.gateway.service.MemberService;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.member.enumtype.MemberStatusEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.PersonalMemberService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PersonalMemberSyncPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;

@Service("personalMemberService")
public class PersonalMemberServiceImpl extends NcCashierBaseService implements PersonalMemberService {

	@Resource
	private PayProcessorService payProcessorService;

	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private PaymentRequestService paymentRequestService;

	@Resource
	private MemberService memberService;

	// condition：同一个订单，是个人会员支付的订单，且非PAYING状态的，会员编号一致，不创建新的record？
	private RecordCondition buildMemberPayRecordCondition(String memberNo) {
		RecordCondition condition = new RecordCondition();
		condition.setPayTool(PayTool.GRHYZF.name()); // 要求payTool=个人会员支付
		String[] payTypes = { PayTypeEnum.GRHYZF.name() }; // 要求payType=个人会员支付
		condition.setRecordPayTypes(payTypes);
		condition.setMemberNo(memberNo); // 会员编号
		return condition;
	}

	private CombinedPaymentDTO getRequestAndRecord(PersonalMemberBanlancePayRequestInfo requestInfo,
			OrderDetailInfoModel orderInfo, MerchantInNetConfigResult merchantInNetConfig, ProductLevel productLevel) {
		// 构造会员信息（包括会员编号和会员类型）
		CashierUserInfo user = buildMemberUser(requestInfo.getMemberNo());
		// 获取paymentRequest，当不存在时创建paymentRequest
		PaymentRequestExtInfo extInfo = buildPaymentRequestExtInfo(requestInfo.getUserIp(),user);
		PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig,productLevel, extInfo);
		// 构建paymentRecord的查找条件
		RecordCondition compareCondition = buildMemberPayRecordCondition(requestInfo.getMemberNo());
		// 根据recordCondition查找paymentRecord，没有符合条件的paymentRecord时需要重新创建
		PaymentRecord paymentRecord = paymentProcessService.createRecordWhenUnexsit(paymentRequest, compareCondition,
				null, PayTypeEnum.GRHYZF.name(), null, null, null,requestInfo.getPayMerchantNo());
		CombinedPaymentDTO combinedPaymentDTO = new CombinedPaymentDTO(paymentRequest, paymentRecord);
		return combinedPaymentDTO;
	}

	@Override
	public void balancePay(PersonalMemberBanlancePayRequestInfo requestInfo, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfig, ProductLevel productLevel) {
		CombinedPaymentDTO combinedPaymentDTO = getRequestAndRecord(requestInfo, orderInfo, merchantInNetConfig,
				productLevel);
		paymentProcessService.avoidRepeatPayWithException(combinedPaymentDTO.getPaymentRecord(),
				new PayRecordStatusEnum[] { PayRecordStatusEnum.INIT });
		PersonalMemberSyncPayResponseDTO personalMemberSyncPayResponseDTO = null;
		try {
			personalMemberSyncPayResponseDTO = callPPBalancePay(
					requestInfo.getExtParam(), combinedPaymentDTO.getPaymentRequest(),
					combinedPaymentDTO.getPaymentRecord());
			PayRecordStatusEnum recordStatus = checkSyncTrxStatusOfPP(personalMemberSyncPayResponseDTO.getTrxStatus());
			paymentProcessService.updateRecordNo(combinedPaymentDTO.getPaymentRecord().getId(), "",
					personalMemberSyncPayResponseDTO.getRecordNo(), recordStatus);
		} catch (CashierBusinessException e) {
			// 先回滚状态，再抛异常
			if((!"P9002001".equals(e.getDefineCode()))&&(!"N900001".equals(e.getDefineCode()))){
				paymentProcessService.recoverRecordToObjStatus(combinedPaymentDTO.getPaymentRecord().getId(),
						PayRecordStatusEnum.INIT, PayRecordStatusEnum.PAYING);
			}
			throw e;
		}

		// 处理状态
		handleTrxStatusOfPPWithException(personalMemberSyncPayResponseDTO.getTrxStatus(), personalMemberSyncPayResponseDTO);
	}

	@Override
	public String queryValidMemberNo(String merchantNo, String merchantUserNo) {
		QueryAccountRequestDTO queryAccountRequestDTO = new QueryAccountRequestDTO();
		queryAccountRequestDTO.setPlatformId(merchantNo);
		queryAccountRequestDTO.setPlatformUserNo(merchantUserNo);
		QueryAccountResponseDTO queryAccountResponseDTO = memberService.queryMemberNo(queryAccountRequestDTO);
		MemberStatusEnum memberStatus = queryAccountResponseDTO.getMemberStatus();
		if(MemberStatusEnum.AVAILABLY.equals(memberStatus) ){
			return queryAccountResponseDTO.getMemberNo();
		}else {
			throw CommonUtil.handleException(Errors.MEMBER_INVALID_EXCEPTION);
		}
	}


	private CashierUserInfo buildMemberUser(String memberNo) {
		CashierUserInfo user = new CashierUserInfo();
		user.setType(MemberTypeEnum.YIBAO.name());
		user.setExternalUserId(memberNo);//此处入参的memberNo，为商户外部用户id和商编调用三代会员查询到的memberNo
		user.setUserType(MemberTypeEnum.YIBAO.name());
		return user;
	}

	/**
	 * 调用PP进行个人余额支付
	 * 
	 * @param paymentRequest
	 * @param paymentRecord
	 * @return
	 */
	private PersonalMemberSyncPayResponseDTO callPPBalancePay(Map<String, String> extParam,
			PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		PersonalMemberSyncPayRequestDTO personalMemberSyncPayRequestDTO = buildPersonalMemberSyncPayRequestDTO(extParam,
				paymentRequest, paymentRecord);
		PersonalMemberSyncPayResponseDTO responseDTO = payProcessorService
				.memberBalancePay(personalMemberSyncPayRequestDTO);
		return responseDTO;
	}

	/**
	 * 构造个人会员同步支付请求入参
	 * 
	 * @param paymentRequest
	 * @param paymentRecord
	 * @return
	 */
	private PersonalMemberSyncPayRequestDTO buildPersonalMemberSyncPayRequestDTO(Map<String, String> extParam,
			PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		PersonalMemberSyncPayRequestDTO requestDTO = new PersonalMemberSyncPayRequestDTO();
		buildBasicRequestDTO(paymentRequest, paymentRecord, requestDTO);
		requestDTO.setBasicProductCode(
				CommonUtil.getBasicProductCode(paymentRecord.getPayTool(), paymentRequest.getTradeSysNo()));
		requestDTO.setExtParam(extParam);
		 requestDTO.setMemberNo(paymentRecord.getMemberNo()); // 存储时注意
		ExtendInfoFromPayRequest extendInfo = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
		requestDTO.setRetailProductCode(extendInfo.getSaleProductCode());
		requestDTO.setPayOrderType(PayOrderType.MEMBER_PAY); // 会员支付
		return requestDTO;
	}
	private PaymentRequestExtInfo buildPaymentRequestExtInfo(String userIp, CashierUserInfo user) {
		PaymentRequestExtInfo extInfo = new PaymentRequestExtInfo();
		extInfo.setUserIp(userIp);
		extInfo.setCashierUser(user);
		return extInfo;
	}
}
