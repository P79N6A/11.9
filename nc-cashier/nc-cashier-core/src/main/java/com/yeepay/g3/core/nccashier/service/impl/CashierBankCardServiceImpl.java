/**
 * 
 */
package com.yeepay.g3.core.nccashier.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.StringPatternUtils;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantConfigCenterService;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.core.nccashier.gateway.service.RiskControlService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel.Product;
import com.yeepay.g3.facade.configcenter.dto.MerchantPayBankConfigDto;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.cwh.param.PayTmpCardDTO;
import com.yeepay.g3.facade.foundation.dto.MerchantLimitResponseDto;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.BusinessTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.ncconfig.enumtype.CardType;
import com.yeepay.g3.facade.ncconfig.param.ConfigCashierBankRuleParam;
import com.yeepay.g3.facade.ncconfig.result.ConfigCashierBankRuleDTO;
import com.yeepay.g3.facade.ncmember.dto.GetUsableRespDTO;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.riskcontrol.facade.util.DoorgodProductType;
import com.yeepay.riskcontrol.facade.v2.TradelimitDataQueryRequestDto;

/**
 * @author zhen.tan
 * @since：2016年5月25日 下午6:26:58
 *
 */
@Service("cashierBankCardService")
public class CashierBankCardServiceImpl extends NcCashierBaseService
		implements CashierBankCardService {
	private static final Logger logger =
			NcCashierLoggerFactory.getLogger(CashierBankCardServiceImpl.class);
	@Resource
	private CwhService cwhService;

	@Resource
	private NcConfigService ncConfigService;

	@Resource
	private RiskControlService riskControlService;

	@Resource
	private PaymentRequestService paymentRequestService;
	
	@Resource
	private BankCardLimitInfoService bankCardLimitInfoService;
	@Resource
	private CashierBindCardService cashierBindCardService;

	@Resource
	private MerchantConfigCenterService merchantConfigCenterService;

	@Resource
	private APICashierYJZFService apiCashierYJZFService;



	public void validateAndGetSupportBankList(long requestId, SupportBanksResponseDTO response, String cusType) {
		PaymentRequest paymentRequest = paymentRequestService.findPayRequestById(requestId);
		if (paymentRequest == null) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}

		List<BankSupportDTO> supportBankList = ncConfigService.getSupportBanks(paymentRequest,cusType);

		Iterator<BankSupportDTO> iterator = supportBankList.iterator();
		while (iterator.hasNext()) {
			BankSupportDTO bankSupportDTO = iterator.next();
			if ((StringUtils.isNotEmpty(paymentRequest.getCardType())
					&& !paymentRequest.getCardType().equals(bankSupportDTO.getBanktype().name()))
					|| (StringUtils.isNotEmpty(paymentRequest.getBankCode()) && !paymentRequest
							.getBankCode().equals(bankSupportDTO.getBankCode()))) {
				iterator.remove();
			}
		}
		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
		response.setSupportBankList(supportBankList);
	}

	@Override
	public void getCardValidates(CardValidateResponseDTO response,
			CardValidateRequestDTO requestDto) throws CashierBusinessException {
		NeedValidatesDTO nvDTO = null;
		PaymentRequest paymentRequest =
				paymentRequestService.findPaymentRequestByRequestId(requestDto.getRequestId());
		if(Constant.BNAK_RULE_CUSTYPE_PREAUTH.equals(requestDto.getCusType())){
			nvDTO = queryCardValidate4Preauth(requestDto.getCardno(), paymentRequest, requestDto.getCusType());
		}else{
			nvDTO = queryCardValidate(requestDto.getCardno(), paymentRequest, true, requestDto.getCusType());
		}
		response.setNeedValidatesDTO(nvDTO);
		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}

	@Override
	public boolean validateCardNeed(CardInfoDTO cardInfo, PaymentRequest payRequest,
			PaymentRecord record) throws CashierBusinessException {
		NeedValidatesDTO cardValidates = queryCardValidate(cardInfo.getCardno(), payRequest, false,Constant.BNAK_RULE_CUSTYPE_SALE);
		
		if(StringUtils.isEmpty(record.getBindId())){
			return true;
		}

		//手机号变更，需要重新下单
		if (StringUtils.isBlank(payRequest.getPhoneNo()) && StringUtils.isNotBlank(cardInfo.getPhone()) && StringUtils.isNotBlank(record.getPhoneNo()) && !cardInfo.getPhone().equals(record.getPhoneNo())){
			return true;
		}

		PassCardInfoDTO passCardInfoDTO = this.getPassCardInfo(payRequest);
		//卡号
		if (StringUtils.isNotBlank(record.getCardNo()) && StringUtils.isNotBlank(cardInfo.getCardno()) && !StringUtils.equals(cardInfo.getCardno(), record.getCardNo())) {
			logger.info("卡号不一致");
			if(passCardInfoDTO != null&&StringUtils.isBlank(passCardInfoDTO.getCardNo())){
				logger.info("透传信息不为空，无透传卡号");
				return true;
			}else if(passCardInfoDTO == null){
				logger.info("透传信息为空,卡号不一致");
				return true;
			}
		}

		//解除临时卡依赖后不保存临时卡ID,因此不用校验临时卡
		if ("-1".equals(record.getBindId())){
			return false;
		}
		PayTmpCardDTO payTmpCard =
				cwhService.getPayTmpCardByTempCardId(Long.parseLong(record.getBindId()));
		if (payTmpCard == null) {
			return true;
		}
		boolean cardInfoChange = this.judgeCardInfoChange(cardValidates,cardInfo, payTmpCard,payRequest);
		
		return cardInfoChange;
	}
	/**
	 * 判断首次支付卡信息是否变更
	 * @param cardInfo
	 * @param payTmpCard
	 * @param payRequest
	 * @return
	 */
	private boolean judgeCardInfoChange(NeedValidatesDTO cardValidates,CardInfoDTO cardInfo,
			PayTmpCardDTO payTmpCard,PaymentRequest payRequest) {
		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo(payRequest);
		PassCardInfoDTO passCardInfoDTO = this.getPassCardInfo(payRequest);
		logger.info("passCardInfoDTO:{},bindLimitInfoResDTO:{}",passCardInfoDTO,bindLimitInfoResDTO);
			//CVV
		if (cardValidates.isNeedCVV()&&!StringUtils.equals(cardInfo.getCvv2(), payTmpCard.getCardCvn2())) {
			logger.info("CVV不一致");
			return true;
			//密码
		} 
		if (cardValidates.isNeedPass()&&!StringUtils.equals(cardInfo.getPass(), payTmpCard.getCardPin())) {
			logger.info("密码不一致");
			return true;
			//有效期
		} 
		if (cardValidates.isNeedValid()&&!StringUtils.equals(cardInfo.getValid(), payTmpCard.getCardExpireDate())) {
			logger.info("有效期不一致");
			return true;
		}
		//卡账户限制信息不允许修改，不需要比较 （持卡人姓名、证件号、证件类型、手机号、卡号）
		//透传信息不允许修改，不需要比较（持卡人姓名、证件号、证件类型、手机号、卡号、卡类型、银行编码）
		//手机号
		if (cardValidates.isNeedMobile()&&!StringUtils.equals(cardInfo.getPhone(), payTmpCard.getPhoneNum())) {
			if(passCardInfoDTO != null&&StringUtils.isBlank(passCardInfoDTO.getPhone())){
				logger.info("手机号不一致");
				return true;
			}else if(passCardInfoDTO == null){
				logger.info("无透传，手机号不一致");
				return true;
			}
		}
		//姓名
		if (cardValidates.isNeedName()&&!StringUtils.equals(cardInfo.getName(), payTmpCard.getUserName())) {
			logger.info("姓名不一致");
			if(bindLimitInfoResDTO ==null&&passCardInfoDTO == null){
					logger.info("透传信息为空，卡限制信息为空");
					return true;
			}else if(bindLimitInfoResDTO !=null&&passCardInfoDTO == null){
				if(StringUtils.isBlank(bindLimitInfoResDTO.getUserNameLimit())){
					logger.info("透传信息为空，卡限制信息不为空时，无限制姓名");
					return true;
				}
			}else if(bindLimitInfoResDTO ==null&&passCardInfoDTO != null){
				if(StringUtils.isBlank(passCardInfoDTO.getOwner())){
					logger.info("透传信息不为空，卡限制信息为空时，无透传姓名");
					return true;
				}
			}else if(bindLimitInfoResDTO !=null&&passCardInfoDTO != null){
				if(StringUtils.isBlank(passCardInfoDTO.getOwner())&&StringUtils.isBlank(bindLimitInfoResDTO.getUserNameLimit())){
					logger.info("透传信息不为空，卡限制信息不为空时，无透传姓名或无限制姓名");
					return true;
				}
			}
		}
		//卡号
		if (!StringUtils.equals(cardInfo.getCardno(), payTmpCard.getCardNo())) {
			logger.info("卡号不一致");
			if(passCardInfoDTO != null&&StringUtils.isBlank(passCardInfoDTO.getCardNo())){
				logger.info("透传信息不为空，无透传卡号");
				return true;
			}else if(passCardInfoDTO == null){
				logger.info("透传信息为空,卡号不一致");
				return true;
			}
		}
		//证件号
		if (cardValidates.isNeedIdno()&&!StringUtils.equals(cardInfo.getIdno(), payTmpCard.getUserCardId())) {
			logger.info("证件号不一致");
			if(bindLimitInfoResDTO ==null&&passCardInfoDTO == null){
				logger.info("透传信息为空，卡限制信息为空");
					return true;
			}else if(bindLimitInfoResDTO !=null&&passCardInfoDTO == null){
				if(StringUtils.isBlank(bindLimitInfoResDTO.getIdentityNoLimit())){
					logger.info("透传信息为空，卡限制信息不为空，限制信息证件号为空");
					return true;
				}
			}else if(bindLimitInfoResDTO ==null&&passCardInfoDTO != null){
				if(StringUtils.isBlank(passCardInfoDTO.getIdNo())){
					logger.info("透传信息不为空，卡限制信息为空，透传信息证件号为空");
					return true;
				}
			}else if(bindLimitInfoResDTO !=null&&passCardInfoDTO != null){
				if(StringUtils.isBlank(passCardInfoDTO.getIdNo())&&StringUtils.isBlank(bindLimitInfoResDTO.getIdentityNoLimit())){
					logger.info("透传信息不为空，卡限制信息不为空，限制信息证件号为空或透传的证件号为空");
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * 获取业务方{key，name}
	 * 
	 * @param bizCode
	 * @return
	 */
	private String[] getBizInfo(String bizCode) {
		Map<String, String> bizInfoMap = CommonUtil.getBusinessConfig(bizCode);
		if (null == bizInfoMap) {
			throw CommonUtil.handleException(Errors.BUSINESS_NOT_SUPPORT);
		}
		String bizKey = "";
		String bizName = "";
		if (null != bizInfoMap) {
			bizKey = bizInfoMap.get(CommonUtil.BIZ_KEY);
			bizName = bizInfoMap.get(CommonUtil.BIZ_NAME);
		}

		return new String[] {bizKey, bizName};
	}
	
	/**
	 * 获取银行规则
	 * 
	 * @param cardNo
	 * @param paymentRequest
	 * @return
	 */
	private ConfigCashierBankRuleDTO getCashierBankRule(String cardNo,
			PaymentRequest paymentRequest, String cusType) {
		ConfigCashierBankRuleParam param = new ConfigCashierBankRuleParam();
		param.setSysId(Long.parseLong(StringUtils.isNotEmpty(paymentRequest.getOrderSysNo())
				? paymentRequest.getOrderSysNo() : paymentRequest.getTradeSysNo()));
		param.setGoodsCode(paymentRequest.getIndustryCatalog());
		param.setMerchantCode(paymentRequest.getMerchantNo());
		param.setToolCode(PayProductCode.NCCASHIER);
		param.setClientCode(paymentRequest.getBizModeCode());
		param.setCardNo(cardNo);
		//预授权交易，wap一定上送该字段；一键支付交易，该字段可能为空
		if(StringUtils.isNotBlank(cusType)){
			param.setCusType(cusType);
		}
		ConfigCashierBankRuleDTO bankRule = ncConfigService.getCashierBankRule(param);
		return bankRule;
	}
	/**
	 * 根据银行规则获取必填项信息、同人限制信息，供预授权使用
	 * @param cardNo
	 * @param paymentRequest
	 * @param cusType
	 * @return
	 */
	private NeedValidatesDTO queryCardValidate4Preauth(String cardNo, PaymentRequest paymentRequest, String cusType) {
		ncConfigService.getCardBinDTOByCardNo(cardNo);
		// 获取银行规则
		ConfigCashierBankRuleDTO bankRule = getCashierBankRule(cardNo, paymentRequest,cusType);
		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo(paymentRequest);
		NeedValidatesDTO nvDTO = makeNeedValidatesDTO(cardNo, bankRule, null, null,bindLimitInfoResDTO);
		return nvDTO;
	}
	/**
	 * 校验卡信息 - 包括银行规则校验、风控限额校验
	 * 
	 * @param cardNo
	 * @param paymentRequest
	 * @param needRiskLimit
	 * @return
	 */
	private NeedValidatesDTO queryCardValidate(String cardNo, PaymentRequest paymentRequest,
			boolean needRiskLimit, String cusType) {
		ncConfigService.getCardBinDTOByCardNo(cardNo);
		
		// 获取银行规则
		ConfigCashierBankRuleDTO bankRule = getCashierBankRule(cardNo, paymentRequest,cusType);
		// 比较商户下单透传卡类型和银行规则卡类型
		if (StringUtils.isNotEmpty(paymentRequest.getCardType())) {
			if (CardTypeEnum.CREDIT.name().equals(paymentRequest.getCardType())
					&& CardTypeEnum.DEBIT.name().equals(bankRule.getCardType().name())) {
				throw CommonUtil.handleException(Errors.PLZ_CREDIT_PAY);
			} else if (CardTypeEnum.DEBIT.name().equals(paymentRequest.getCardType())
					&& CardTypeEnum.CREDIT.name().equals(bankRule.getCardType().name())) {
				throw CommonUtil.handleException(Errors.PLZ_DEBIT_PAY);
			}
		}
		
		// 比较商户下单透传额银行编码和银行规则返回的银行编码
		if (StringUtils.isNotEmpty(paymentRequest.getBankCode())) {
			if (!paymentRequest.getBankCode().equals(bankRule.getBankCard().name())) {
				throw CommonUtil.handleException(Errors.SUPPORT_BANK_FAILED);
			}
		}
		
		// 获取风控限额
		QuataDTO quataDTO = null;
		if (needRiskLimit) {
			try {
				String bizCode = StringUtils.isNotEmpty(paymentRequest.getOrderSysNo())
						? paymentRequest.getOrderSysNo() : paymentRequest.getTradeSysNo();
				String bizInfo[] = getBizInfo(bizCode);

				quataDTO = getRiskLimit(paymentRequest.getQueryRiskInfo(),
						paymentRequest.getMerchantNo(), paymentRequest.getIndustryCatalog(),
						bankRule.getCardType().name(), bankRule.getBankCard().name(), bizInfo[1]);
			} catch (Exception e) {
				logger.info("获取风控限额异常", e);
			}

			if (null != quataDTO && quataDTO.getOrderQuotaDou() != -1) {
				if (paymentRequest.getOrderAmount()
						.compareTo(new BigDecimal(quataDTO.getOrderQuotaDou())) > 0) {
					throw CommonUtil.handleException(Errors.AMOUNT_OUT_RANGE);
				}
			}
		}
		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo(paymentRequest);
		PassCardInfoDTO passCardInfoDTO = this.getPassCardInfo(paymentRequest);
		compareCardInfo(bindLimitInfoResDTO,passCardInfoDTO);
		NeedValidatesDTO nvDTO = makeNeedValidatesDTO(cardNo, bankRule, quataDTO, paymentRequest,bindLimitInfoResDTO);
		return nvDTO;
	}
	
	@Override
	public void checkBindLimitInfo(Person person, BindLimitInfoResDTO bindLimitInfoResDTO) {
		if (person == null) {
			return;
		}
		if (bindLimitInfoResDTO == null
				|| Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())) {
			return;
		}
		if (StringUtils.isNotBlank(person.getRealName())
				&& StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())
				&& (!person.getRealName().equals(bindLimitInfoResDTO.getUserNameLimit()))) {
			throw CommonUtil.handleException(Errors.CARD_INFO_NOT_SAME);
		}
		if (StringUtils.isNotBlank(person.getIdCardNo())
				&& StringUtils.isNotBlank(bindLimitInfoResDTO.getIdentityNoLimit())
				&& (!person.getIdCardNo().equals(bindLimitInfoResDTO.getIdentityNoLimit()))) {
			throw CommonUtil.handleException(Errors.CARD_INFO_NOT_SAME);
		}
	}

	private void compareCardInfo(BindLimitInfoResDTO bindLimitInfoResDTO,
			PassCardInfoDTO passCardInfoDTO) {
		if (null != passCardInfoDTO && null != bindLimitInfoResDTO
				&&  !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())) {
			// 持卡人姓名
			if(StringUtils.isNotBlank(passCardInfoDTO.getOwner())&&StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())){
				String passOwner = passCardInfoDTO.getOwner();
				String cwhOwer = bindLimitInfoResDTO.getUserNameLimit();
				if(!passOwner.equals(cwhOwer)){
					throw CommonUtil.handleException(Errors.CARD_INFO_NOT_SAME);
				}
			}
	//		身份证号/证件号
			if(StringUtils.isNotBlank(passCardInfoDTO.getIdNo())&&StringUtils.isNotBlank(bindLimitInfoResDTO.getIdentityNoLimit())){
				String passIdNo = passCardInfoDTO.getIdNo();
				String cwhIdNo = bindLimitInfoResDTO.getIdentityNoLimit();
				if(!passIdNo.equalsIgnoreCase(cwhIdNo)){
					throw CommonUtil.handleException(Errors.CARD_INFO_NOT_SAME);
				}
			}
		}
	}

	

	/**
	 * 封装银行卡必填项
	 * 
	 * @param cardno
	 * @param ruleDTO
	 * @param quataDTO
	 * @param paymentRequest
	 * @return
	 */
	private NeedValidatesDTO makeNeedValidatesDTO(String cardno, ConfigCashierBankRuleDTO ruleDTO,
			QuataDTO quataDTO, PaymentRequest paymentRequest,BindLimitInfoResDTO bindLimitInfoResDTO) {
		NeedValidatesDTO nvDTO = new NeedValidatesDTO();

		NCPayParamMode needparam = ruleDTO.getFirstRequired();
		nvDTO.setNeedIdno(needparam.needIdCardNumber());
		nvDTO.setNeedCVV(needparam.needCvv());
		if (needparam.needBankMobilePhone() || needparam.needYeepayMobilePhone()) {
			nvDTO.setNeedMobile(true);
		}
		nvDTO.setNeedName(needparam.needUserName());
		nvDTO.setNeedPass(needparam.needBankPWD());
		nvDTO.setNeedValid(needparam.needAvlidDate());
		nvDTO.setNeedSms(true);

		nvDTO.setPreRouter(ruleDTO.isPreRouter());

		BankCardDTO bankcard = new BankCardDTO();
		bankcard.setBankQuata(quataDTO);
		bankcard.setCardno(cardno);

		bankcard.setBankCode(ruleDTO.getBankCard().name());
		bankcard.setBankName(ruleDTO.getBankCard().getName());
		bankcard.setCardtype(CardTypeEnum.valueOf(ruleDTO.getCardType().name()));
		if(null != paymentRequest){
			bankcard.setOwner(paymentRequest.getOwner());
			bankcard.setIdno(paymentRequest.getIdCard());
			bankcard.setPhoneNo(paymentRequest.getPhoneNo());
		}
		bankcard.setCardlater(cardno.substring(cardno.length() - 4));
		
		
		if(bindLimitInfoResDTO !=null){//卡账户返回的卡信息限制信息
			if(StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())){
				bankcard.setOwner(bindLimitInfoResDTO.getUserNameLimit());
			}
			if(StringUtils.isNotBlank(bindLimitInfoResDTO.getIdentityNoLimit())){
				bankcard.setIdno(bindLimitInfoResDTO.getIdentityNoLimit());
			}
			
			// 商户配置成同人 
			if(!Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())){
				nvDTO.setMerchantSamePersonConfig(true);
			}
		}
		
		
		nvDTO.setBankCard(bankcard);
		return nvDTO;
	}

	/**
	 * 获取风控限额
	 * @param queryRiskInfo
	 * 	交易系统透传
	 * @param merchantNo
	 * @param industryCatalog
	 * @param cardType
	 * @param bankCode
	 * @param bizName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private QuataDTO getRiskLimit(String queryRiskInfo, String merchantNo, String industryCatalog,
			String cardType, String bankCode, String bizName) {
		QuataDTO quataDTO = new QuataDTO();
		
		List<QueryRiskInfoDTO> queryRiskInfoList = null;
		if (StringUtils.isNotEmpty(queryRiskInfo)) {
			try {
				queryRiskInfoList = JSON.parseArray(queryRiskInfo, QueryRiskInfoDTO.class);
			} catch (JSONException e) {
				queryRiskInfoList =
						JSON.parseArray("[" + queryRiskInfo + "]", QueryRiskInfoDTO.class);
			} catch (Exception e) {
				queryRiskInfoList =
						JSON.parseArray("[" + queryRiskInfo + "]", QueryRiskInfoDTO.class);
			}
		}
		
		long orderQuotaDou = Long.MAX_VALUE;
		long dayQuotaDou = Long.MAX_VALUE;
		long monthQuotaDou = Long.MAX_VALUE;
		if (null != queryRiskInfoList && queryRiskInfoList.size() > 0) {
			for (QueryRiskInfoDTO queryRiskInfoDTO : queryRiskInfoList) {
				String interceptFactors = queryRiskInfoDTO.getInterceptKey();
				Map<String, String> map =
						(Map<String, String>) JSON.parse(queryRiskInfoDTO.getInterceptValue());
				TradelimitDataQueryRequestDto riskDTO = makeTradelimitDataQueryRequestDto(interceptFactors, map, cardType,
						bankCode, merchantNo, industryCatalog, bizName);
				QuataDTO riskquata = riskControlService.queryLimit(riskDTO);
				if (null != riskquata) {
					if (riskquata.getDayQuotaDou() >= 0
							&& riskquata.getDayQuotaDou() < dayQuotaDou) {
						dayQuotaDou = riskquata.getDayQuotaDou();
					}
					if (riskquata.getMonthQuotaDou() >= 0
							&& riskquata.getMonthQuotaDou() < monthQuotaDou) {
						monthQuotaDou = riskquata.getMonthQuotaDou();
					}
					if (riskquata.getOrderQuotaDou() >= 0
							&& riskquata.getOrderQuotaDou() < orderQuotaDou) {
						orderQuotaDou = riskquata.getOrderQuotaDou();
					}
				}
			}
		} else {
			// 下单没有风控信息时
			TradelimitDataQueryRequestDto riskDTO = makeTradelimitDataQueryRequestDto(null, null, cardType, bankCode, merchantNo, industryCatalog, bizName); 
			QuataDTO riskquata = riskControlService.queryLimit(riskDTO);
			if (null != riskquata) {
				dayQuotaDou = riskquata.getDayQuotaDou();
				monthQuotaDou = riskquata.getMonthQuotaDou();
				orderQuotaDou = riskquata.getOrderQuotaDou();
			}
		}

		if (orderQuotaDou == Long.MAX_VALUE) {
			orderQuotaDou = -1;
		}
		if (dayQuotaDou == Long.MAX_VALUE) {
			dayQuotaDou = -1;
		}
		if (monthQuotaDou == Long.MAX_VALUE) {
			monthQuotaDou = -1;
		}
		quataDTO.setDayQuotaDou(dayQuotaDou);
		quataDTO.setMonthQuotaDou(monthQuotaDou);
		quataDTO.setOrderQuotaDou(orderQuotaDou);
		return quataDTO;
	}
	
	/**
	 * 构造风控限额查询入参
	 * 
	 * @param interceptValueMap
	 * @param cardType
	 * @param bankCode
	 * @param bizName
	 * @return
	 */
	private TradelimitDataQueryRequestDto makeTradelimitDataQueryRequestDto(String interceptFactors, Map<String, String> interceptValueMap, String cardType, String bankCode,
			String merchantNo, String industryCatalog, String bizName) {
		TradelimitDataQueryRequestDto riskDTO = new TradelimitDataQueryRequestDto();
		// 老的接口 factors为空（相当于value为空），用外面的goodsCode、merchantNo、bankCardType、bankId
		// 老的接口 否则就使用map里的东西，不管外面的
		if(MapUtils.isEmpty(interceptValueMap)){
			interceptValueMap = new HashMap<String, String>();
			interceptValueMap.put(RiskControlQueryConstants.GOODS_CODE, industryCatalog);
			interceptValueMap.put(RiskControlQueryConstants.MERCHANT_NO, merchantNo);
			interceptValueMap.put(RiskControlQueryConstants.BANK_CARD_TYPE, cardType);
			interceptValueMap.put(RiskControlQueryConstants.BANK_ID, bankCode);
		}else{
			interceptValueMap.put(RiskControlQueryConstants.BANK_CARD_TYPE, cardType);
			interceptValueMap.put(RiskControlQueryConstants.BANK_CODE, bankCode);
		}
		riskDTO.setInterceptFactors(interceptFactors);
		riskDTO.setInterceptValueMap(interceptValueMap);
		riskDTO.setProductId(DoorgodProductType.NOCARDPAY.getValue());
		riskDTO.setProduction(bizName);
		return riskDTO;
	}


	@Override
	public void getPayType(PaymentRequest paymentRequest,
			BussinessTypeResponseDTO bussinessTypeResponseDTO,String cusType) {
		
		PassCardInfoDTO passCardInfoDTO = this.getPassCardInfo(paymentRequest);

		// 获取支持的绑卡信息
		List<BindCardDTO> bankCardList = getAndfilterBindCards(paymentRequest, passCardInfoDTO, null, cusType);
		//无可用的绑卡信息且不允许换卡支付(走首次支付)时抛异常
		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo(paymentRequest);
		
		boolean canFirstPay = bankCardLimitInfoService.getShowChangeCard(paymentRequest);
		if(bindLimitInfoResDTO !=null&& !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())&&!canFirstPay){
			if(!bindLimitInfoResDTO.isCanBindNewCard()&&CollectionUtils.isEmpty(bankCardList)){
				throw CommonUtil.handleException(Errors.OUT_BIND_CARD_LIMIT);
			}
			
		}
		
		// 获取支付的业务类型
		BusinessTypeEnum payType = judgePayType(bankCardList, passCardInfoDTO);
		bussinessTypeResponseDTO.setBusinessTypeEnum(payType);
		//首次支付时判断透传信息与卡账户限制信息是否一致
		if(payType ==BusinessTypeEnum.FIRSTPAY||payType ==BusinessTypeEnum.FIRSTPASS||payType==BusinessTypeEnum.FIRSTPASSCARDNO){
			this.compareCardInfo(bindLimitInfoResDTO, passCardInfoDTO);
		}
		bussinessTypeResponseDTO.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}

	/**
	 * 判断支付类型
	 * 
	 * @param bankCardList
	 * @param passCardInfoDTO
	 */
	private BusinessTypeEnum judgePayType(List<BindCardDTO> bankCardList,
			PassCardInfoDTO passCardInfoDTO) {

		if (null == passCardInfoDTO && CollectionUtils.isEmpty(bankCardList)) {
			return BusinessTypeEnum.FIRSTPAY;
		} else if (null == passCardInfoDTO && CollectionUtils.isNotEmpty(bankCardList)) {
			return BusinessTypeEnum.BINDPAY;
		} else if ((null != passCardInfoDTO && StringUtils.isNotBlank(passCardInfoDTO.getCardNo()))
				&& CollectionUtils.isEmpty(bankCardList)) {
			return BusinessTypeEnum.FIRSTPASSCARDNO;
		} else if ((null != passCardInfoDTO && StringUtils.isBlank(passCardInfoDTO.getCardNo()))
				&& CollectionUtils.isEmpty(bankCardList)) {
			return BusinessTypeEnum.FIRSTPASS;
		} else if ((null != passCardInfoDTO && StringUtils.isNotBlank(passCardInfoDTO.getCardNo()))
				&& CollectionUtils.isNotEmpty(bankCardList)) {
			return BusinessTypeEnum.BINDPASSCARDNO;
		} else if ((null != passCardInfoDTO && StringUtils.isBlank(passCardInfoDTO.getCardNo()))
				&& CollectionUtils.isNotEmpty(bankCardList)) {
			return BusinessTypeEnum.BINDPASS;
		} else {
			return BusinessTypeEnum.FIRSTPAY;
		}

	}

	/**
	 * 根据透传信息过滤绑卡信息
	 * 
	 * @param passCardInfoDTO
	 * @param supportbindCardlist
	 * @param unspopportedBankCards
	 */
	private void filterBindCardsByPassCardInfo(PassCardInfoDTO passCardInfoDTO, List<BindCardDTO> supportbindCardlist,
			List<BankCardDTO> unspopportedBankCards) {
		if (CollectionUtils.isEmpty(supportbindCardlist) || passCardInfoDTO == null) {
			return;
		}
		Iterator<BindCardDTO> iterator = supportbindCardlist.iterator();
		while (iterator.hasNext()) {
			BindCardDTO bankCardDTO = iterator.next();
			String cardType = null;
			if (null != bankCardDTO.getBankCardType()) {
				cardType = CommonUtil.cardTypeTransfer(bankCardDTO.getBankCardType()).name();
			}
			String unusableCause = null;
			if (StringUtils.isNotBlank(passCardInfoDTO.getCardNo()) && StringUtils.isNotBlank(bankCardDTO.getCardNo())
					&& !passCardInfoDTO.getCardNo().equals(bankCardDTO.getCardNo())) {
				unusableCause = "此卡的[卡号]与商户的传值不一致";
			} else if (StringUtils.isNotBlank(passCardInfoDTO.getBankCode())
					&& StringUtils.isNotBlank(bankCardDTO.getBankCode())
					&& !passCardInfoDTO.getBankCode().equals(bankCardDTO.getBankCode())) {
				unusableCause = "此卡的[银行编码]与商户的传值不一致";
			} else if (StringUtils.isNotEmpty(passCardInfoDTO.getCardType()) && StringUtils.isNotEmpty(cardType)
					&& !passCardInfoDTO.getCardType().equals(cardType)) {
				unusableCause = "此卡的[银行卡类型]与商户的传值不一致";
			} else if (StringUtils.isNotEmpty(passCardInfoDTO.getOwner()) && StringUtils.isNotEmpty(bankCardDTO.getOwner())
						&& !passCardInfoDTO.getOwner().equals(bankCardDTO.getOwner())) {
				unusableCause = "此卡的[姓名]与商户的传值不一致";
			} else if (StringUtils.isNotEmpty(passCardInfoDTO.getIdNo())
					&& StringUtils.isNotEmpty(bankCardDTO.getIdcard())
					&& !passCardInfoDTO.getIdNo().equalsIgnoreCase(bankCardDTO.getIdcard())) {
				unusableCause = "此卡的[证件号]与商户的传值不一致";
			} else if (StringUtils.isNotEmpty(passCardInfoDTO.getPhone())
					&& !((StringUtils.isEmpty(bankCardDTO.getYbMobile())
							&& StringUtils.isEmpty(bankCardDTO.getBankMobile()))
							|| (StringUtils.isNotEmpty(bankCardDTO.getYbMobile())
									&& passCardInfoDTO.getPhone().equals(bankCardDTO.getYbMobile()))
					|| (StringUtils.isNotEmpty(bankCardDTO.getBankMobile())
							&& passCardInfoDTO.getPhone().equals(bankCardDTO.getBankMobile())))) {
				unusableCause = "此卡的[手机号]与商户的传值不一致";
			}
			if (StringUtils.isNotBlank(unusableCause)) {
				if (unspopportedBankCards != null) {
					BankCardDTO unusableBankCard = makeBindCard(bankCardDTO);
					unusableBankCard.setUnusableCause(unusableCause);
					unspopportedBankCards.add(unusableBankCard);
				}
				iterator.remove();
			}
		}
	}
	
	

	/**
	 * 获取透传的卡信息
	 * 
	 * @param paymentRequest
	 * @return
	 */
	public PassCardInfoDTO getPassCardInfo(PaymentRequest paymentRequest) {
		PassCardInfoDTO passCardInfoDTO = null;
		boolean isPassCardInfo = this.judgePassCardInfo(paymentRequest);
		if (isPassCardInfo) {
			passCardInfoDTO = new PassCardInfoDTO();
			passCardInfoDTO.setCardNo(StringUtils.isNotBlank(paymentRequest.getCardNo())
					? paymentRequest.getCardNo() : null);
			passCardInfoDTO.setOwner(StringUtils.isNotBlank(paymentRequest.getOwner())
					? paymentRequest.getOwner() : null);
			passCardInfoDTO.setPhone(StringUtils.isNotBlank(paymentRequest.getPhoneNo())
					? paymentRequest.getPhoneNo() : null);
			passCardInfoDTO.setBankCode(StringUtils.isNotBlank(paymentRequest.getBankCode())
					? paymentRequest.getBankCode() : null);
			passCardInfoDTO.setCardType(StringUtils.isNotBlank(paymentRequest.getCardType())
					? paymentRequest.getCardType() : null);
			passCardInfoDTO.setIdNo(StringUtils.isNotBlank(paymentRequest.getIdCard())
					? paymentRequest.getIdCard() : null);
			passCardInfoDTO.setIdType(StringUtils.isNotBlank(paymentRequest.getIdCardType())
					? paymentRequest.getIdCardType() : null);
		}


		return passCardInfoDTO;
	}
	
	/**
	 * 过滤绑卡列表
	 * 
	 * @param paymentRequest
	 * @param passCardInfoDTO
	 * @param bankCardReponseDTO
	 */
	private List<BindCardDTO> getAndfilterBindCards(PaymentRequest paymentRequest, PassCardInfoDTO passCardInfoDTO,
			BankCardReponseDTO bankCardReponseDTO, String cusType) {
		// 1 从用户中心获取可用绑卡列表
		GetUsableRespDTO usableBindCards = cashierBindCardService.getUseableBindCardList(paymentRequest);
		if(usableBindCards == null){
			return null;
		}
		// 2 將可用綁卡銀行編碼轉為與統一配置一致
		transferBankCode(usableBindCards.getUsableBindCard());
		// 3 过滤非本人绑卡信息
		filterCardBelong2SameOne(usableBindCards.getUsableBindCard(), paymentRequest);
		// 4 过滤收银台模板不支持的绑卡，并記錄不支持的绑卡
		List<BindCardDTO> supportedBankCards = new ArrayList<BindCardDTO>();
		List<BankCardDTO> unspopportedBankCards = new ArrayList<BankCardDTO>();
		filterBindCardsByCashierTemplate(usableBindCards.getUsableBindCard(), paymentRequest, supportedBankCards, unspopportedBankCards, cusType);
		// 5 過濾與透傳信息不符合的綁卡
		filterBindCardsByPassCardInfo(passCardInfoDTO, supportedBankCards, unspopportedBankCards);
		// 6 根据限额剔除绑卡列表（可以搞个开关-影响性能）
//		filterBindCardsByMerchantLimit(paymentRequest, supportedBankCards, unspopportedBankCards);
		// 7 将不支持的卡加入到reponse中
		if(bankCardReponseDTO!=null){
			bankCardReponseDTO.setUnuseBankCardDTO(unspopportedBankCards);
			bankCardReponseDTO.setAuthorized(usableBindCards!=null?usableBindCards.isAuthorized():false);
		}
		return supportedBankCards;
	}
	
	/**
	 * 过滤超出限额的绑卡，并记录超出限额的绑卡
	 * 
	 * @param paymentRequest
	 */
	private void filterBindCardsByMerchantLimit(PaymentRequest paymentRequest, List<BindCardDTO> supportedBankCards,
			List<BankCardDTO> unspopportedBankCards) {
		if (CollectionUtils.isEmpty(supportedBankCards)) {
			return;
		}
		List<MerchantLimitResponseDto> merchantLimits = merchantConfigService.filterValidMerchantLimits(paymentRequest);
		//限额接口返回的是分，nccashier是元
		long amount = paymentRequest.getOrderAmount().multiply(new BigDecimal(100)).longValue();
		Iterator<BindCardDTO> iterator = supportedBankCards.iterator();
		for (;iterator.hasNext();) {
			BindCardDTO bindCard = iterator.next();
			for (MerchantLimitResponseDto merchantLimit : merchantLimits) {
				String cardType = CommonUtil.cardTypeTransferToStr(bindCard.getBankCardType());
				if (merchantLimit.getBankCode().equals(bindCard.getBankCode())
						&& merchantLimit.getDebit().equals(cardType)) {
					if ((merchantLimit.getLimitOfBill() != -1) && (amount > merchantLimit.getLimitOfBill()
							|| amount < merchantLimit.getLimitMinOfBill())) {
						// 超出限额
						BankCardDTO bankCard = makeBindCard(bindCard);
						bankCard.setUnusableCause("此卡超过单笔支付限额");
						unspopportedBankCards.add(bankCard);
						iterator.remove();
						break;
					}
				}
			}
		}

	}
	
	/**
	 * 將綁卡列表的銀行編碼轉為與統一配置配置的銀行編碼一致
	 * 
	 * @param oriBankCards
	 */
	private void transferBankCode(List<BindCardDTO> oriBankCards) {
		if (CollectionUtils.isEmpty(oriBankCards)) {
			return;
		}
		Map<String, String> standardBankCodes = CommonUtil.getConfig(Constant.STANDARD_BANKCODE);
		if (MapUtils.isNotEmpty(standardBankCodes)) {
			for (BindCardDTO BankCard : oriBankCards) {
				String bankCode = BankCard.getBankCode();
				if (standardBankCodes.containsKey(bankCode)) {
					BankCard.setBankCode(standardBankCodes.get(bankCode));
				}
			}
		}
	}
	
	/**
	 * 过滤收银台模板不支持的绑卡
	 * @param oriBankCards
	 * @param paymentRequest
	 * @param supportedBankCards
	 * @param unspopportedBankCards
	 */
	private void filterBindCardsByCashierTemplate(List<BindCardDTO> oriBankCards, PaymentRequest paymentRequest, List<BindCardDTO> supportedBankCards, List<BankCardDTO> unspopportedBankCards, String cusType){
		// 傳入的可用綁卡列表為空 直接返回
		if(CollectionUtils.isEmpty(oriBankCards)){
			return;
		}
		// 獲取配置的收銀台模板
		List<BankSupportDTO> supportCashierBanks = ncConfigService.getSupportBanks(paymentRequest, cusType);
		// 支持的收銀台模板為空，將可用綁卡列表全部置為不可用，并標記不可用原因
		if (CollectionUtils.isEmpty(supportCashierBanks)) {
			// 监控日志
			logger.error("[monitor],event:nccashier_filterBindCardsByCashierTemplate, requestId:{},收银台模板支持银行列表为空",
					paymentRequest.getId());
			// 這種情況下，首次也不可用 直接抛异常
			throw CommonUtil.handleException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}
		// 收銀台模板支持銀行不為空，篩選出模板支持的綁卡，并記錄不支持的綁卡
		for (BindCardDTO binkCard : oriBankCards) {
			boolean isSupported = false;
			for (BankSupportDTO supportCashierBank : supportCashierBanks) {
				boolean bankCode = supportCashierBank.getBankCode().equals(binkCard.getBankCode());
				boolean cardType = supportCashierBank.getBanktype()
						.equals(CommonUtil.cardTypeTransfer(binkCard.getBankCardType()));
				if (cardType && bankCode) {
					supportedBankCards.add(binkCard);
					isSupported = true;
					break;
				}
			}
			if (!isSupported) {
				BankCardDTO bankCardDTO = makeBindCard(binkCard);
				bankCardDTO.setUnusableCause("商户不支持此卡");
				unspopportedBankCards.add(bankCardDTO);
			}
		}
	}
	
	
	

	/**
	 * 若同人限制值存在的情况下，过滤本人的绑卡信息
	 * @param bindCardlist
	 * @param paymentRequest
	 */
	private void filterCardBelong2SameOne(List<BindCardDTO> bindCardlist,
			PaymentRequest paymentRequest) {
		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService
				.getLimitInfo4bind(paymentRequest);
		if(CollectionUtils.isNotEmpty(bindCardlist)&&null != bindLimitInfoResDTO&&!Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO
				.getBindCardLimitType())){
			//商户限制类型为同人，限制值不为空,绑卡属于不同人
			if (StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())
						&& StringUtils.isNotBlank(bindLimitInfoResDTO
								.getIdentityNoLimit())) {
					logger.info("商户为同人，且同人限制值不空，需移除非同人的绑卡信息");
					Person person = new Person();
					person.setIdCardNo(bindLimitInfoResDTO.getIdentityNoLimit());
					person.setRealName(bindLimitInfoResDTO.getUserNameLimit());
					// 移除非本人的绑卡信息
					cashierBindCardService
							.removeCardBelongtoOther(bindCardlist, person);
				}
			
		}
	}

	
	/**
	 * BindCardDTO转换为BankCardDTO
	 * 
	 * @param cwhbindcard
	 * @return
	 */
	private BankCardDTO makeBindCard(BindCardDTO cwhbindcard) {
		if (null == cwhbindcard) {
			return null;
		}
		BankCardDTO bindcard = new BankCardDTO();

		bindcard.setBankCode(cwhbindcard.getBankCode());
		bindcard.setBankName(cwhbindcard.getBankName());
		bindcard.setBindid(cwhbindcard.getBindId());
		String cardno = cwhbindcard.getCardNo();
		bindcard.setCardno(cardno);
		bindcard.setCardlater(cardno.substring(cardno.length() - 4, cardno.length()));
		CardTypeEnum cardType = CommonUtil.cardTypeTransfer(cwhbindcard.getBankCardType());
		if (null == cardType) {
			return null;
		}
		bindcard.setCardtype(cardType);
		bindcard.setIdno(cwhbindcard.getIdcard());
		bindcard.setOwner(cwhbindcard.getOwner());
		bindcard.setNeedCheck(true);
		bindcard.setPhoneNo(cwhbindcard.getBankMobile());
		bindcard.setYpMobile(cwhbindcard.getYbMobile());
		bindcard.setOtherCards(null);
		return bindcard;
	}
	
	@Override
	public InstallmentBanksResponseDTO filterSupportInstallmentBankList(String merchantNo, BigDecimal orderAmount) {
		List<InstallmentBankInfo> bankList = getSupportBankList(merchantNo);
		List<InstallmentBankInfoDTO> unusableBankList = new ArrayList<InstallmentBankInfoDTO>();
		List<InstallmentBankInfoDTO> usableBankList = new ArrayList<InstallmentBankInfoDTO>();
		for (InstallmentBankInfo bankInfo : bankList) {
			InstallmentBankInfoDTO info = bankInfo.transferToInstallmentBankInfoDTO();
			BigDecimal minLimit = new BigDecimal(bankInfo.getBankInfo().getMinLimit());
			if (minLimit.compareTo(orderAmount) > 0) {
				info.setStatus("UNUSABLE");
				info.setRemark("订单金额过低"); 
				unusableBankList.add(info);
			} else {
				usableBankList.add(info);
			}
		}
		InstallmentBanksResponseDTO responseDTO = new InstallmentBanksResponseDTO();
		responseDTO.setUnusableBankList(unusableBankList);
		responseDTO.setUsableBankList(usableBankList);
		return responseDTO;
	}
	
	/**
	 * 获取商户支持的银行卡分期银行及期数相关信息
	 * 
	 * @param merchantNo
	 * @return
	 */
	public List<InstallmentBankInfo> getSupportBankList(String merchantNo) {
		MerchantInNetConfigResult configResult = getInstallmentBankConfig(merchantNo);
		// 将配置中心返回的银行期数信息与统一配置的取交集
		List<InstallmentBankInfo> bankList = configResult.listInstallmentSupportBank();
		if (CollectionUtils.isEmpty(bankList)) {
			 throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}
		return bankList;
	}
	@Override
	public void getBankCardInfo4Preauth(long requestId, BankCardReponseDTO bankCardReponseDTO, String cusType) {
		PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestId);
		List<BindCardDTO> supportBindCards = getAndfilterBindCards(paymentRequest, null, bankCardReponseDTO,cusType);
		bankCardReponseDTO.setBankCardDTO(makeBankCardDtobyBindCards(supportBindCards));
		bankCardReponseDTO.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
		//获取是否展示换卡按钮
		bankCardReponseDTO.setShowChangeCard(showChangeCard(paymentRequest,null,null));
	}

	@Override
	public String validatePassBindId(long requestId) {
		PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestId);
		return validatePassBindId(paymentRequest.getBindId(),paymentRequest.getIdentityId(),paymentRequest.getIdentityType(),paymentRequest.getMerchantNo());
	}

	@Override
	public void getBankCardInfo(long requestId, BankCardReponseDTO bankCardReponseDTO, String cusType) {
		PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestId);
//		1、判断是否有透传信息
		PassCardInfoDTO passCardInfoDTO = this.getPassCardInfo(paymentRequest);
//		2、获取并根据规则过滤绑卡列表
		List<BindCardDTO> supportBindCards = getAndfilterBindCards(paymentRequest, passCardInfoDTO, bankCardReponseDTO, cusType);
//		3、判断支付类型
		BusinessTypeEnum payType = judgePayType(supportBindCards, passCardInfoDTO);
		if (payType == BusinessTypeEnum.BINDPASSCARDNO) {
			bankCardReponseDTO.setBankCardDTO(makeBindCard(supportBindCards.get(0)));
		} else if (payType == BusinessTypeEnum.BINDPASS || payType == BusinessTypeEnum.BINDPAY) {
			bankCardReponseDTO.setBankCardDTO(makeBankCardDtobyBindCards(supportBindCards));
		}
		
		// modify by meiling.zhuang 为某些商编定制{如果是首次非透传，首次透传非卡号，首次透传卡号，获取统一配置，判断是否需要提示自动绑卡}
		if(StringUtils.isBlank(paymentRequest.getIdentityId())||StringUtils.isBlank(paymentRequest.getIdentityType())){
			logger.info("IdentityId或IdentityType为空，不进行自动绑卡提示");
		}else{
			String autoBindText = CommonUtil.merchantNeedAutoBindTip(paymentRequest.getMerchantNo());
			if(StringUtils.isNotBlank(autoBindText)){
				bankCardReponseDTO.setAutoBindTipText(autoBindText);
			}
		}
		
		bankCardReponseDTO.setPassCardInfoDTO(passCardInfoDTO);
		bankCardReponseDTO.setBusinessTypeEnum(payType);
		bankCardReponseDTO.setShowChangeCard(showChangeCard(paymentRequest,payType,passCardInfoDTO));
		bankCardReponseDTO.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}


	private boolean showChangeCard(PaymentRequest paymentRequest,BusinessTypeEnum payType,PassCardInfoDTO passCardInfoDTO){
		if(StringUtils.isNotBlank(paymentRequest.getBindId())){	//能走到这一步,校验都没有问题,传了bindId,就不显示 更换按钮
			return false;
		}
		//4、获取是否显示换卡支付按钮
		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo(paymentRequest);
		boolean showChangeCard = bankCardLimitInfoService.getShowChangeCard(paymentRequest);
		if(payType == BusinessTypeEnum.BINDPASS){
			try{
				this.compareCardInfo(bindLimitInfoResDTO, passCardInfoDTO);
			}catch(CashierBusinessException e){
				logger.warn("透传信息与卡账户限制信息不一致，不允许新增卡");
				String defineCode = e.getDefineCode();
				if("3300006".equals(defineCode)){
					showChangeCard =false;
				}
			}

		}
		return showChangeCard;
	}

	/**
	 * 判断是否透传卡信息
	 * 
	 * @param paymentRequest
	 * @return
	 */
	private boolean judgePassCardInfo(PaymentRequest paymentRequest) {
		boolean isPassCardInfo = false;
		if (StringUtils.isNotBlank(paymentRequest.getCardNo())) {
			isPassCardInfo = true;
		} else if (StringUtils.isNotBlank(paymentRequest.getBankCode())) {
			isPassCardInfo = true;
		} else if (StringUtils.isNotBlank(paymentRequest.getOwner())) {
			isPassCardInfo = true;
		} else if (StringUtils.isNotBlank(paymentRequest.getIdCard())) {
			isPassCardInfo = true;
		} else if (StringUtils.isNotBlank(paymentRequest.getIdCardType())) {
			isPassCardInfo = true;
		} else if (StringUtils.isNotBlank(paymentRequest.getPhoneNo())) {
			isPassCardInfo = true;
		} else if (StringUtils.isNotBlank(paymentRequest.getCardType())) {
			isPassCardInfo = true;
		}

		if(!isPassCardInfo){
			String cardNo = validatePassBindId(paymentRequest.getBindId(),paymentRequest.getIdentityId(),paymentRequest.getIdentityType(),paymentRequest.getMerchantNo());
			if(StringUtils.isNotBlank(cardNo)){
				paymentRequest.setCardNo(cardNo);
				isPassCardInfo = true;
			}
		}
		return isPassCardInfo;
	}


	private String validatePassBindId(String bindId,String userNo,String userType,String merchantNo){
		if(StringUtils.isNotBlank(bindId)){
			if(StringUtils.isNotBlank(userNo) && StringUtils.isNotBlank(userType)){
				CashierUserInfo user = apiCashierYJZFService.buildMemberUser(userNo, userType, merchantNo);
				if(user != null && StringUtils.isNotBlank(user.getExternalUserId())){
					List<BankCardDTO> bankCardDTOs = cwhService.getBindCardListByBindId(bindId,user.getExternalUserId(),userType);
					if(CollectionUtils.isNotEmpty(bankCardDTOs) && bankCardDTOs.get(0)!=null && StringUtils.isNotBlank(bankCardDTOs.get(0).getCardno()))
						return bankCardDTOs.get(0).getCardno();
				}
			}
			throw new CashierBusinessException(Errors.PASS_BINDID_NOT_MATCH);
		}
		return "";
	}


	/**
	 * 绑卡列表转换为BankCardDTO
	 * 
	 * @param supportBindCards
	 * @return
	 */
	private BankCardDTO makeBankCardDtobyBindCards(List<BindCardDTO> supportBindCards) {

		if (null == supportBindCards || supportBindCards.size() == 0) {
			logger.info("支持的绑卡列表为空");
			return null;
		}
		BankCardDTO bankCardDTO = this.makeBindCard(supportBindCards.get(0)); // 可以根据交易时间选择最近交易成功的银行卡
		bankCardDTO.setOtherCards(makeBindCards(supportBindCards));

		return bankCardDTO;
	}

	/**
	 * bindCardList转换为BankCardDTOList
	 * 
	 * @param supportBindCards
	 * @return
	 */
	private List<BankCardDTO> makeBindCards(List<BindCardDTO> supportBindCards) {
		List<BankCardDTO> bankCards = new ArrayList<BankCardDTO>();
		for (BindCardDTO bindCard : supportBindCards) {
			BankCardDTO bankCardDTO = makeBindCard(bindCard);
			if (null != bankCardDTO) {
				bankCards.add(bankCardDTO);
			}
		}

		return bankCards;
	}
	
	
	@Override
	public CardBinDTO getCardBinInfo(String cardno) {
		return ncConfigService.getCardBinDTOByCardNo(cardno);
	}
	
	
	@Override
	public void shareBindCardResultFilter(List<BindCardDTO> list,PaymentRequest paymentRequest,ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirmResponseDTO, String cusType) {
		PassCardInfoDTO passCardInfoDTO = getPassCardInfo(paymentRequest);
		//將可用綁卡銀行編碼轉為與統一配置一致
		transferBankCode(list);
		List<BindCardDTO> supportedBankCards = new ArrayList<BindCardDTO>();
		List<BankCardDTO> unspopportedBankCards = new ArrayList<BankCardDTO>();
		filterBindCardsByCashierTemplate(list, paymentRequest, supportedBankCards, unspopportedBankCards, cusType);
		// 5 過濾與透傳信息不符合的綁卡
		filterBindCardsByPassCardInfo(passCardInfoDTO, supportedBankCards, unspopportedBankCards);
		// 6 根据限额剔除绑卡列表（可以搞个开关-影响性能）
		filterBindCardsByMerchantLimit(paymentRequest, supportedBankCards, unspopportedBankCards);
		BusinessTypeEnum businessTypeEnum = judgePayType(supportedBankCards,passCardInfoDTO);
		shareCardAuthoritySmsConfirmResponseDTO.setIsChangeCard(showChangeCard(paymentRequest, businessTypeEnum, passCardInfoDTO));
		//把可用的放在不可用的之前
		List<BankCardDTO> listAll = new ArrayList<BankCardDTO>();
		if(CollectionUtils.isNotEmpty(supportedBankCards)){
			for(BindCardDTO bindCardDTO : supportedBankCards){
				BankCardDTO bankCardDTO = makeBindCard(bindCardDTO);
				listAll.add(bankCardDTO);
			}
		}
		if(CollectionUtils.isNotEmpty(unspopportedBankCards)){
			listAll.addAll(unspopportedBankCards);
		}
		shareCardAuthoritySmsConfirmResponseDTO.setBankCardDTOList(listAll);

	}

	
    @Override
    public String changeBankCode(String bankCode) {
        Map<String, String> standardBankCodes = CommonUtil.getConfig(Constant.STANDARD_BANKCODE);
        if (MapUtils.isNotEmpty(standardBankCodes)) {
            if (standardBankCodes.containsKey(bankCode)) {
                return standardBankCodes.get(bankCode);
            }
        }
        return bankCode;
    }

	@Override
	public CardInfo getNonNullCardBin(String cardno) {
		CardInfo cardBin = getCardBin(cardno);
		if (cardBin == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "卡号不合法");
		}
		return cardBin;
	}

	@Override
	public CardInfo getCardBin(String cardno) {
		CardBinDTO cardBinDTO = ncConfigService.getCardBinDTOByCardNo(cardno);
		return buildCardInfo(cardBinDTO, cardno);
	}

	private CardInfo buildCardInfo(CardBinDTO cardBinDTO, String cardno){
		if(cardBinDTO==null || cardBinDTO.getBankEnum()==null || cardBinDTO.getCardType()==null){
			return null;
		}
		CardInfo card = new CardInfo();
		BankInfo bank = new BankInfo();
		bank.setBankCode(cardBinDTO.getBankEnum().name());
		bank.setBankName(cardBinDTO.getBankEnum().getName());
		bank.standardBankCode();
		card.setCardNo(cardno);
		card.setBank(bank);
		card.setCardType(transfer(cardBinDTO.getCardType()));
		card.setCardName(cardBinDTO.getCardName());
		return card;
	}
	
	private CardTypeEnum transfer(CardType cardType){
		if(cardType == null){
			return null;
		}
		if(CardType.DEBIT == cardType){
			return CardTypeEnum.DEBIT;
		}
		if(CardType.CREDIT == cardType){
			return CardTypeEnum.CREDIT;
		}
		
		return null;
	}

	@Override
	public List<InstallmentBankInfo> getNonNullInstallmentBankList(String merchantNo) {
		List<InstallmentBankInfo> bankList = getSupportBankList(merchantNo);
		if(CollectionUtils.isEmpty(bankList)){
			throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}
		return bankList;
	}
	

	@Override
	public MerchantInNetConfigResult getInstallmentBankConfig(String merchantNo){
		MerchantPayBankConfigDto payBankConfigList = getBankConfigOfBankInstallment(merchantNo);
		MerchantInNetConfigResult configResult = new MerchantInNetConfigResult();
		Product product = new Product();
		product.setPayTool(PayTool.YHKFQ_ZF);
		product.setBankStaging(payBankConfigList.getBankStaging());
		if (MapUtils.isNotEmpty(payBankConfigList.getBankStaging())) {
			List<BankStagingInfo> bankStagingsOfPayTool = new ArrayList<BankStagingInfo>();
			BankStagingInfo bankStaging = product.buildBankStagingInfo();
			bankStagingsOfPayTool.add(bankStaging);
			configResult.setBankStagingOfPayTool(bankStagingsOfPayTool);
		}
		return configResult;
	}
	
	/**
	 * 1，获取配置中心银行卡分期支持的银行及期数信息； 
	 * 2，如果支持的银行及期数信息为空，则抛异常；
	 * 
	 * @return
	 */
	private MerchantPayBankConfigDto getBankConfigOfBankInstallment(String merchantNo) {
		MerchantPayBankConfigDto payBankConfigList = null;
		String payBankConfigListJson = RedisTemplate
				.getTargetFromRedis(Constant.BANK_CARD_INSTALLMENT_SUPPORT_BANK_KEY + merchantNo, String.class);
		if (StringUtils.isNotBlank(payBankConfigListJson)) {
			if (Constant.BINDCARDNULL.equals(payBankConfigListJson)) {
				logger.info("从REDIS获取的银行列表为空");
			} else {
				payBankConfigList = JSON.parseObject(payBankConfigListJson, MerchantPayBankConfigDto.class);
			}
		} else {
			payBankConfigList = merchantConfigCenterService.getBankInfo(merchantNo);
			RedisTemplate.setCacheObjectSumValue(Constant.BANK_CARD_INSTALLMENT_SUPPORT_BANK_KEY + merchantNo,
					payBankConfigList == null ? Constant.BINDCARDNULL : JSON.toJSONString(payBankConfigList),
					Constant.NCCASHIER_REQUEST_REDIS_TIME_LIMIT);
		}
		if (payBankConfigList == null || MapUtils.isEmpty(payBankConfigList.getBankStaging())) {
			throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}

		return payBankConfigList;
	}

}
