package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.CashierCardOwnerBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.RealPersonChooseTimes;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.BankCardLimitInfoService;
import com.yeepay.g3.core.nccashier.service.CashierBindCardService;
import com.yeepay.g3.core.nccashier.service.RealPersonChooseTimesService;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.cwh.param.ExternalUserDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.CardOwnerConfirmResDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;
import com.yeepay.g3.facade.nccashier.dto.Person;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("cashierCardOwnerBiz")
public class CashierCardOwnerBizImpl extends NcCashierBaseBizImpl implements
		CashierCardOwnerBiz {

	@Resource
	private CashierBindCardService cashierBindCardService;
	@Resource
	private BankCardLimitInfoService bankCardLimitInfoService;
	@Resource
	private RealPersonChooseTimesService realPersonChooseTimesService;

	
	private RealPersonChooseTimes makeShowTimes(PaymentRequest payRequest) {
		RealPersonChooseTimes showTimes = new RealPersonChooseTimes();
		showTimes.setMerchantNo(payRequest.getMerchantNo());
		showTimes.setIdentityId(payRequest.getIdentityId());
		showTimes.setIdentityType(payRequest.getIdentityType());
		showTimes.setShowCount(1);
		showTimes.setStatus(Constant.VALIDE);
		showTimes.setCreateTime(new Date());
		showTimes.setUpdateTime(new Date());
		showTimes.setPayRequestId(payRequest.getId());
		return showTimes;
	}

	/**
	 * 设置同人限制值信息
	 */
	@Override
	public void setCardOwner(String bindId, String name, String idCardNo, long paymentRequestId) {
		NcCashierLoggerFactory.TAG_LOCAL.set("[设置同人限制值|setCardOwner],bindId="+bindId+",paymentRequestId="+paymentRequestId);
		PaymentRequest payRequest = paymentRequestService.findPayRequestById(paymentRequestId);
		if (null == payRequest) {
			return;
		}
		if (StringUtils.isBlank(bindId)
				&& (StringUtils.isBlank(idCardNo) || StringUtils.isBlank(name))) {
			return;
		}
		NcCashierLoggerFactory.TAG_LOCAL.set("[设置同人限制值|setCardOwner],支付请求ID="
				+ paymentRequestId);
		Person person = null;
		if (StringUtils.isNotBlank(idCardNo) && StringUtils.isNotBlank(name)) {
			logger.info("手动输入的支付身份信息");
			person = new Person();
			person.setIdCardNo(idCardNo);
			person.setRealName(name);
		} else if (StringUtils.isNotBlank(bindId)) {
			try {
				BindCardDTO bindCard = cwhService.getBindCardInfoByBindId(Long
						.parseLong(bindId));
				if (null != bindCard) {
					person = new Person();
					person.setIdCardNo(bindCard.getIdcard());
					person.setRealName(bindCard.getOwner());
				}
			} catch (Exception e) {
				logger.error("获取绑卡信息异常，绑卡ID=" + bindId, e);
			}
		}
		if (null != person && StringUtils.isNotBlank(person.getIdCardNo())
				&& StringUtils.isNotBlank(person.getRealName())) {
			RealPersonChooseTimes showTimes = realPersonChooseTimesService
					.getShowTimes(payRequest);
			this.setSamePersonInfoAndUnbind(showTimes, person, payRequest,
					"用户选择支付身份");
		}
	}

	private void updateShowTimes2Invalide(RealPersonChooseTimes showTimes) {
		if (null != showTimes) {
			showTimes.setStatus(Constant.INVALIDE);
			realPersonChooseTimesService.updateShowTimes(showTimes);
		}
	}

	@Deprecated
	@Override
	public void unbindCard(long paymentRequestId, String paymentOrderNo) {
		PaymentRequest payRequest = paymentRequestService
				.findPayRequestById(paymentRequestId);
		if (null == payRequest) {
			return;
		}
		PaymentRecord paymentRecord = null;
		List<PaymentRecord> recordList = paymentProcessService
				.findRecordListByOrderOrderId(payRequest.getOrderOrderId(),
						payRequest.getOrderSysNo());
		if (CollectionUtils.isNotEmpty(recordList)) {
			for (PaymentRecord record : recordList) {
				if (paymentOrderNo.equals(record.getPaymentOrderNo())) {
					paymentRecord = record;
					break;
				}
			}
		}
		if (null == paymentRecord) {
			return;
		}
		NcCashierLoggerFactory.TAG_LOCAL.set("[支付身份错误|unbindCard],支付请求ID="
				+ paymentRequestId + "支付订单号=" + paymentOrderNo);
		NeedSurportDTO needSupport = null;
		if (Constant.BIND.equals(paymentRecord.getPayType())) {
			try {
				needSupport = RedisTemplate.getTargetFromRedis(
						Constant.MISS_NEEDITEM_BIND + payRequest.getId(),
						NeedSurportDTO.class);
				logger.info(
						"从redis获取的隐形透传证件号和姓名的交易记录+needSupport:{},payRequest:{}",
						needSupport, payRequest);
			} catch (Exception e) {
				logger.error("获取有补充项的绑卡信息异常", e);
			}
			if (null != needSupport) {
				logger.info("交易失败为隐性发送补充项的订单，需要解绑该卡");
				BindCardDTO bindCard = cwhService.getBindCardInfoByBindId(Long
						.parseLong(paymentRecord.getBindId()));
				cashierBindCardService.unbindCard(bindCard, payRequest, "系统默认");
			}
		}

	}

	@Override
	public void unbindCard(long paymentRequestId, long paymentRecordId) {

		PaymentRequest payRequest = paymentRequestService
				.findPayRequestById(paymentRequestId);
		PaymentRecord paymentRecord = paymentProcessService
				.findRecordByPaymentRecordId(paymentRecordId + "");
		if (null == payRequest || null == paymentRecord) {
			return;
		}
		NcCashierLoggerFactory.TAG_LOCAL.set("[解绑卡|unbindCard],支付请求ID="
				+ paymentRequestId + "支付记录ID=" + paymentRecord.getId());

		NeedSurportDTO needSupport = null;
		if (Constant.BIND.equals(paymentRecord.getPayType())) {
			try {
				needSupport = RedisTemplate.getTargetFromRedis(
						Constant.MISS_NEEDITEM_BIND + payRequest.getId(),
						NeedSurportDTO.class);
				logger.info(
						"从redis获取的隐形透传证件号和姓名的交易记录+needSupport:{},payRequest:{}",
						needSupport, payRequest);
			} catch (Exception e) {
				logger.error("获取有补充项的绑卡信息异常", e);
			}
			if (null != needSupport) {
				logger.info("交易失败为隐性发送补充项的订单，需要解绑该卡");
				BindCardDTO bindCard = cwhService.getBindCardInfoByBindId(Long
						.parseLong(paymentRecord.getBindId()));
				cashierBindCardService.unbindCard(bindCard, payRequest, "系统默认");
			}
		}
	}

	@Override
	public CardOwnerConfirmResDTO getCardOwners(long paymentRequestId,
			long paymentRecordId) {
		PaymentRequest payRequest = paymentRequestService
				.findPayRequestById(paymentRequestId);
		PaymentRecord paymentRecord = paymentProcessService
				.findRecordByPaymentRecordId(paymentRecordId + "");
		if (null == payRequest || null == paymentRecord) {
			return null;
		}
		NcCashierLoggerFactory.TAG_LOCAL.set("[getCardOwners],支付请求ID="
				+ paymentRequestId);
		// 用户是否需要选择支付身份
		boolean needChoose = false;
		int showCount = 0;
		CardOwnerConfirmResDTO cardOwnerRes = new CardOwnerConfirmResDTO();
		cardOwnerRes.setPaymentRequestId(paymentRequestId);

		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService
				.getLimitInfo4bind(payRequest);
		// 一键支付的交易、绑卡属于不同人、商户设置为同人、同人限制值为空才需要选择支付身份
		if (null != paymentRecord
				&& (CardInfoTypeEnum.TEMP.name().equals(
						paymentRecord.getPayType()) || CardInfoTypeEnum.BIND
						.name().equals(paymentRecord.getPayType()))
				&& null != bindLimitInfoResDTO
				&& !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO
						.getBindCardLimitType())
				&& (StringUtils.isBlank(bindLimitInfoResDTO
						.getIdentityNoLimit()) || StringUtils
						.isBlank(bindLimitInfoResDTO.getUserNameLimit()))) {
			List<BindCardDTO> bindCards = cashierBindCardService
					.getBindCardListFromUserCenter(payRequest);
			if (CollectionUtils.isNotEmpty(bindCards)) {
				boolean canSetSamePersonLimit = cashierBindCardService
						.canSetSamePersonLimit(bindCards);
				List<Person> cardOwners = cashierBindCardService
						.getPersonFromBindList(bindCards);
				logger.info("绑卡列表中的持卡人信息,cardOwners：{}", cardOwners);
				if (canSetSamePersonLimit) {
					logger.info("绑卡为同一人，需要设置同人限制值");
					if (CollectionUtils.isNotEmpty(cardOwners)) {
						cashierBindCardService.setSamePersonLimit(
								cardOwners.get(0), payRequest,
								"绑卡均为同一人，系统自动设置同人身份");
					}
				} else {
					logger.info("绑卡属于不同人，需要用户选择支付");
					RealPersonChooseTimes showTimes = realPersonChooseTimesService
							.getShowTimes(payRequest);
					if (null == showTimes) {
						logger.info("首次选择支付身份，商户编号："
								+ payRequest.getMerchantNo() + "IdentityType:"
								+ payRequest.getIdentityType() + "IdentityID"
								+ HiddenCode.hiddenIdentityId(payRequest.getIdentityId()));
						needChoose = true;
						showCount = 1;
						showTimes = makeShowTimes(payRequest);
						realPersonChooseTimesService.createShowTimes(showTimes);
					} else if (showTimes.getShowCount() < 3
							&& Constant.VALIDE.equals(showTimes.getStatus())) {
						logger.info("支付身份选择页面展示次数小于3，商户编号："
								+ payRequest.getMerchantNo() + "IdentityType:"
								+ payRequest.getIdentityType() + "IdentityID"
								+ HiddenCode.hiddenIdentityId(payRequest.getIdentityId()));
						needChoose = true;
						showCount = showTimes.getShowCount() + 1;
					}
					cardOwnerRes.setNeedChooseCardOwner(needChoose);
					if (needChoose) {
						cardOwnerRes.setPersons(cardOwners);
						cardOwnerRes.setShowTimes(showCount);
					}

					if (payRequest.getId() != showTimes.getPayRequestId()) {
						showTimes.setPayRequestId(payRequest.getId());
						realPersonChooseTimesService.updateShowTimes(showTimes);
					}
				}
			}
		}

		logger.info("获取支付身份信息，返回的对象cardOwnerRes：{}", cardOwnerRes);
		return cardOwnerRes;
	}

	@Override
	public void unbindCardForInner(long paymentRequestId, long bindId) {

		if (bindId == 0 || paymentRequestId == 0) {
			return;
		}
		PaymentRequest payRequest = paymentRequestService
				.findPayRequestById(paymentRequestId);
		if (null == payRequest) {
			return;
		}
		NcCashierLoggerFactory.TAG_LOCAL.set("[解绑卡|unbindCard],支付请求ID="
				+ paymentRequestId + "绑卡ID=" + bindId);

		BindCardDTO bindCard = cwhService.getBindCardInfoByBindId(bindId);
		if (null == bindCard) {
			logger.info("绑卡记录不存在");
		}
		cashierBindCardService.unbindCard(bindCard, payRequest, "运营操作解绑");

	}

	@Override
	public void autosetSamePeronInfo() {
		try {
			List<RealPersonChooseTimes> showtimes = realPersonChooseTimesService
					.getUnChooseShowTimesInfo();
			logger.info("autosetSamePeronInfo商户未选择支付身份的列表为，showtimes:{}",
					showtimes);
			if (CollectionUtils.isNotEmpty(showtimes)) {
				for (RealPersonChooseTimes showTime : showtimes) {
					PaymentRequest payRequest = paymentRequestService
							.findPayRequestById(showTime.getPayRequestId());
					List<BindCardDTO> bindCards = cashierBindCardService
							.getBindCardListFromUserCenter(payRequest);
					if (CollectionUtils.isNotEmpty(bindCards)&&null != bindCards.get(0)
							&& StringUtils.isNotBlank(bindCards.get(0)
									.getIdcard())
							&& StringUtils.isNotBlank(bindCards.get(0)
									.getOwner())) {
						try {
								Person person = new Person();
								person.setIdCardNo(bindCards.get(0).getIdcard());
								person.setRealName(bindCards.get(0).getOwner());
								this.setSamePersonInfoAndUnbind(showTime,
										person, payRequest, "页面展示超过三次，系统自动设置");
						} catch (Throwable e) {
							logger.error(
									"系统自动设置同人限制值异常，支付请求ID为"
											+ payRequest.getId() + "绑卡ID为"
											+ bindCards.get(0).getBindId(), e);
						}
					}
				}

			}
			logger.info("需要定时设置同人限制值的用户数为空");
		} catch (Throwable e) {
			logger.error("autosetSamePeronInfo定时设置同人限制值出现异常", e);
		}
	}

	@Override
	public void unbindCardActive(String paymentRequestId, String bindId) {
		NcCashierLoggerFactory.TAG_LOCAL.set("[主动解绑卡|unbindCardActive]—[paymentRequestId=" + paymentRequestId + "]—[bindId="+bindId+"]");
		if(StringUtils.isBlank(paymentRequestId) || !StringUtils.isNumeric(paymentRequestId) ||
				StringUtils.isBlank(bindId) || !StringUtils.isNumeric(bindId)){
			logger.error("unbindCardActive(), param paymentRequestId = {}, bindId = {}, param is invalid", paymentRequestId, bindId);
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		PaymentRequest payRequest = paymentRequestService.findPayRequestById(Long.parseLong(paymentRequestId));
		if (null == payRequest) {
			logger.error("unbindCardActive(), param paymentRequestId = {}, bindId = {}, error = payRequest is null", paymentRequestId, bindId);
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		BindCardDTO bindCard = cwhService.getBindCardInfoByBindId(Long.parseLong(bindId));
		if (null == bindCard) {
			logger.error("unbindCardActive(), param paymentRequestId = {}, bindId = {}, error = 绑卡记录不存在", paymentRequestId, bindId);
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		if(!checkUnbindCardInfo(bindCard,payRequest)){
			logger.error("unbindCardActive(), param paymentRequestId = {}, bindId = {}, error = 解绑卡与请求用户身份不匹配", paymentRequestId, bindId);
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		cashierBindCardService.unbindCard(bindCard, payRequest, "用户主动解绑");
	}

	/**
	 * 用户主动解绑卡，检查要解绑的卡是否匹配用户身份
	 * @param bindCard
	 * @param payRequest
	 * @return
	 */
	private boolean checkUnbindCardInfo(BindCardDTO bindCard,PaymentRequest payRequest) {
		if (StringUtils.isBlank(bindCard.getUserId())) {
			return false;
		}
		ExternalUserDTO externalUser = cwhService.getExternalUserById(bindCard.getUserId());
		if(externalUser==null || StringUtils.isBlank(externalUser.getIdentityId()) || null == externalUser.getIdentityType()){
			return false;
		}
		String cashierUserId = "";
		String cashierUserType = "";
		if (Constant.YIBAO.equals(payRequest.getMemberType())) {
			cashierUserId = payRequest.getMemberNo();
			cashierUserType = Constant.YIBAO;
		} else {
			cashierUserId = payRequest.getIdentityId();
			cashierUserType = payRequest.getIdentityType();
		}
		if (StringUtils.isBlank(cashierUserId) || !externalUser.getIdentityId().equals(cashierUserId)) {
			//identityId 不符合
			return false;
		}
		if (StringUtils.isBlank(cashierUserType) || !externalUser.getIdentityType().name().equals(cashierUserType)) {
			//identityType 不符合
			return false;
		}
		return true;
	}

	private void setSamePersonInfoAndUnbind(RealPersonChooseTimes showTimes,
			Person person, PaymentRequest payRequest, String reason) {
		cashierBindCardService.setSamePersonLimit(person, payRequest, reason);
		if (null != showTimes) {
			showTimes.setStatus(Constant.INVALIDE);
			this.updateShowTimes2Invalide(showTimes);
		}
		List<BindCardDTO> bindCards = cashierBindCardService
				.getBindCardListFromUserCenter(payRequest);
		List<BindCardDTO> bindCard2Others = cashierBindCardService
				.filterCardBelongtoOther(bindCards, person);
		// 解除非本人绑卡
		cashierBindCardService.unbindCards(bindCard2Others, payRequest, reason);
	}

}
