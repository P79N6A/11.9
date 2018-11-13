/**
 * 
 */
package com.yeepay.g3.core.nccashier.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.yeepay.g3.facade.cwh.enumtype.*;
import com.yeepay.g3.facade.cwh.param.*;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UnbindRecord;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.UserCenterService;
import com.yeepay.g3.core.nccashier.service.BankCardLimitInfoService;
import com.yeepay.g3.core.nccashier.service.CashierBindCardService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.UnbindCardService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.ExternalUserRequestDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.Person;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncmember.dto.GetUsableRespDTO;
import com.yeepay.g3.facade.ncmember.dto.MerchantUserDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * @author xueping.ni
 * @since：2016年11月21日 下午6:26:58
 *
 */
@Service("cashierBindCardService")
public class CashierBindCardServiceImpl extends NcCashierBaseService
		implements CashierBindCardService {
	private static final Logger logger =LoggerFactory.getLogger(CashierBindCardServiceImpl.class);
	@Resource
	private CwhService cwhService;
	@Resource
	private BankCardLimitInfoService bankCardLimitInfoService;
	@Resource
	private UnbindCardService unbindCardService;
	@Resource
	private PaymentProcessService paymentProcessService;
	@Resource
	private UserCenterService userCenterService;

	@Override
	public List<BindCardDTO> getBindCardList(String id, UserType userType) {
		List<BindCardDTO> bindCardList = null;
		bindCardList = cwhService.getBindCardsByExternalId(id, userType);
		if (CollectionUtils.isNotEmpty(bindCardList)) {
			orderByUpdateTime(bindCardList);// 按照updateTime排序
		}
		return bindCardList;
	}

	/**
	 * 按照绑卡的更新时间排序
	 * 
	 * @param bindCardList
	 */
	private void orderByUpdateTime(List<BindCardDTO> bindCardList) {
		Collections.sort(bindCardList, new Comparator<BindCardDTO>() {
			@Override
			public int compare(BindCardDTO o1, BindCardDTO o2) {
				return o2.getUpdateTime().compareTo(o1.getUpdateTime());
			}

		});
	}



	/**
	 * 剔除非本人的绑卡 
	 */
	@Override
	public List<BindCardDTO> filterCardBelongtoOne(List<BindCardDTO> bindCards, Person person) {
		List<BindCardDTO> belong2One = null;
		if(CollectionUtils.isNotEmpty(bindCards)){
			Iterator<BindCardDTO> iterator = bindCards.iterator();
			belong2One = new ArrayList<BindCardDTO>();
			while (iterator.hasNext()) {
				BindCardDTO bindCard = iterator.next();
//				移除非本人的绑卡
				if((StringUtils.isNotBlank(bindCard.getOwner())&&StringUtils.isNotBlank(bindCard.getIdcard()))){
					if(!person.getRealName().equals(bindCard.getOwner())||!person.getIdCardNo().equalsIgnoreCase(
							bindCard.getIdcard())){
						bindCards.remove(bindCard);
					}
//					本人绑卡返回
					if(person.getRealName().equals(bindCard.getOwner())&&person.getIdCardNo().equalsIgnoreCase(
							bindCard.getIdcard())){
						belong2One.add(bindCard);
					}
				}
			}
				
		}
		
		return belong2One;
	}

	/**
	 * 获取持卡人信息列表(证件号和姓名均不为空时)
	 */
	@Override
	public List<Person> getPersonFromBindList(List<BindCardDTO> bindCards,PaymentRequest paymentRequest) {
		logger.info("getPersonFromBindList绑卡列表为bindCards:{}",bindCards);
		String ownerInfo = null;
		List<Person> personInfo = new ArrayList<Person>();
		Map<String,String> tempPerson = new HashMap<String,String>();
		PaymentRecord paymentRecord = null;
		List<PaymentRecord> recordList = paymentProcessService
				.findRecordListByOrderOrderId(paymentRequest.getOrderOrderId(),
						paymentRequest.getOrderSysNo());
		if (CollectionUtils.isNotEmpty(recordList)
				&& StringUtils.isNotBlank(paymentRequest.getPaymentOrderNo())) {
			for (PaymentRecord record : recordList) {
				if (paymentRequest.getPaymentOrderNo().equals(
						record.getPaymentOrderNo())) {
					paymentRecord = record;
					break;
				}
			}
		}
		
		
		if (null != paymentRecord
				&& CardInfoTypeEnum.TEMP.name().equals(
						paymentRecord.getPayType())
				&& StringUtils.isNotBlank(paymentRecord.getBindId())) {
			logger.info("换卡或首次支付，持卡人信息第一个添加当前交易的持卡人信息");
			PayTmpCardDTO payTmpCardDTO = cwhService
					.getPayTmpCardByTempCardId(Long.parseLong(paymentRecord
							.getBindId()));
			if (null != payTmpCardDTO
					&& StringUtils.isNotBlank(payTmpCardDTO.getUserCardId())
					&& StringUtils.isNotBlank(payTmpCardDTO.getUserName())) {
				logger.info("临时卡信息中的证件号和姓名均不为空");
				ownerInfo = payTmpCardDTO.getUserCardId()+payTmpCardDTO.getUserName();
				tempPerson.put(ownerInfo, "1");
				Person p = new Person();
				p.setIdCardNo(payTmpCardDTO.getUserCardId());
				p.setBindId(payTmpCardDTO.getId());
				p.setRealName(payTmpCardDTO.getUserName());
				personInfo.add(p);
			}

		}
		if(CollectionUtils.isNotEmpty(bindCards)){
			logger.info("getPersonFromBindList绑卡列表大小"+bindCards.size());
			String idCardNo =null;
			String owner = null;
			for (BindCardDTO bindCard : bindCards) {
				if (StringUtils.isNotBlank(bindCard.getOwner())
						&& StringUtils.isNotBlank(bindCard.getIdcard())) {
						ownerInfo = bindCard.getIdcard()+bindCard.getOwner();
						if(null==owner&&null==idCardNo){
							owner = bindCard.getOwner();
							idCardNo	 = bindCard.getIdcard();
							if(null == tempPerson.get(ownerInfo)){
								tempPerson.put(ownerInfo, "1");
								Person temp = new Person();
								temp = new Person(); 
								temp.setIdCardNo(bindCard.getIdcard());
								temp.setRealName(bindCard.getOwner());
								temp.setBindId(bindCard.getBindId());
								personInfo.add(temp);
							}
						}
					
						if (!idCardNo.equalsIgnoreCase(bindCard.getIdcard())
								|| !owner.equals(bindCard.getOwner())) {
							
							if(null == tempPerson.get(ownerInfo)){
								tempPerson.put(ownerInfo, "1");
								Person temp = new Person();
								temp.setIdCardNo(bindCard.getIdcard());
								temp.setRealName(bindCard.getOwner());
								temp.setBindId(bindCard.getBindId());
								personInfo.add(temp);
							}
						}
					}
				}
			}
		logger.info("getPersonFromBindList返回的持卡人列表大小"+personInfo.size());
		return personInfo;
		}
		
	

	/**
	 * 获取非本人的绑卡（不包括证件和姓名为空的绑卡）
	 */
	@Override
	public List<BindCardDTO> filterCardBelongtoOther(
			List<BindCardDTO> bindCards, Person person) {
		if(CollectionUtils.isEmpty(bindCards)){
			return null;
		}
		
		List<BindCardDTO> bindCardList = new ArrayList<BindCardDTO>();
		for (BindCardDTO bindCard : bindCards) {
			//姓名、证件号均不为空，且与本人信息不一致的银行卡
			if(StringUtils.isBlank(bindCard.getOwner())||StringUtils.isBlank(bindCard.getIdcard())){
				continue;
			}
			if (!person.getRealName().equals(bindCard.getOwner()) || !person
							.getIdCardNo().equalsIgnoreCase(
									bindCard.getIdcard())) {
				bindCardList.add(bindCard);
			}
		}
		return bindCardList;
	}
	
	@Override
	public List<BindCardDTO> getBindCardList(PaymentRequest paymentRequest) {
	//	先从REDIS取绑卡列表，若存在则直接返回；若不存在则重新获取绑卡列表并将绑卡列表放入REDIS
		List<BindCardDTO> bindCardList = null;
		String bindCardLists = RedisTemplate.getTargetFromRedis(
				Constant.BINDCARDS + paymentRequest.getId(), String.class);
		if(StringUtils.isNotBlank(bindCardLists)){//redis中有值
			if (Constant.BINDCARDNULL.equals(bindCardLists)) {
				logger.info("从REDIS获取的绑卡列表为空");
				return null;
			} else {
				bindCardList = JSON.parseArray(bindCardLists, BindCardDTO.class);
				logger.info("从REDIS获取的绑卡列表大小为"+bindCardList.size());
				return bindCardList;
			}
		}
		ExternalUserRequestDTO externalUserRequestDTO = null;
		ExternalUserDTO externalUserDTO = null;
		if(Constant.YIBAO.equals(paymentRequest.getMemberType())){//易宝三代会员
			bindCardList = getBindCardList(paymentRequest.getMemberNo(), UserType.LOGIN);
		}else{//联名账户
			// 1、封装查询外部商户请求参数
			externalUserRequestDTO = makeExternalRequestDto(paymentRequest);
			// 2、获取外部商户信息
			if(null != externalUserRequestDTO){
				 externalUserDTO = cwhService.getExternalUser(externalUserRequestDTO);
			}
			// 3、获取绑卡列表
			if(null != externalUserDTO){
				bindCardList = getBindCardList(externalUserDTO.getId(), UserType.EXTERNAL);
				}
		}
		if(null == bindCardList){
				RedisTemplate.setCacheObjectSumValue(Constant.BINDCARDS + paymentRequest.getId(), Constant.BINDCARDNULL, Constant.NCCASHIER_REQUEST_REDIS_TIME_LIMIT);
		}else{
				RedisTemplate.setCacheObjectSumValue(Constant.BINDCARDS + paymentRequest.getId(), JSON.toJSONString(bindCardList), Constant.NCCASHIER_REQUEST_REDIS_TIME_LIMIT);
		}
		
		return bindCardList;
	}

	@Override
	public Map<String,Boolean> canSetSamePersonLimit(PaymentRequest paymentRequest) {
		Map<String,Boolean>   setSamePerson = new HashMap<String,Boolean>();
		boolean belongToOne =false;
		boolean checkHistoryBinds = true;
		List<BindCardDTO> bindCardBelongToOne = null;
		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService
				.getLimitInfo4bind(paymentRequest);
		//商户设置为同人时
		if(null != bindLimitInfoResDTO&&!Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO
				.getBindCardLimitType())){
			List<BindCardDTO> bindCards = getBindCardList(paymentRequest);
			
			PaymentRecord paymentRecord = null;
			List<PaymentRecord> recordList = paymentProcessService
					.findRecordListByOrderOrderId(paymentRequest.getOrderOrderId(),
							paymentRequest.getOrderSysNo());
			if (CollectionUtils.isNotEmpty(recordList)
					&& StringUtils.isNotBlank(paymentRequest.getPaymentOrderNo())) {
				for (PaymentRecord record : recordList) {
					if (paymentRequest.getPaymentOrderNo().equals(
							record.getPaymentOrderNo())) {
						paymentRecord = record;
						break;
					}
				}
			}
			
			if(CollectionUtils.isNotEmpty(bindCards)){//换卡
				  bindCardBelongToOne = judgeBindCardBelongToOne(bindCards);
				  if(CollectionUtils.isNotEmpty(bindCardBelongToOne)){
					  logger.info("绑卡属于同一人的换卡支付，可以设置同人限制值");
					  belongToOne = true;  
				  }
				  if (CardInfoTypeEnum.TEMP.name().equals(paymentRecord.getPayType())) {
					  logger.info("换卡支付，不校验历史绑卡");
					  checkHistoryBinds = false;
					}
			}else if(CardInfoTypeEnum.TEMP.name().equals(paymentRecord.getPayType())) {//首次
					logger.info("首次支付，可以设置同人限制值");
					belongToOne =true;
			}
		}
			logger.info("是否默认设置同人限制值"+belongToOne);
			logger.info("绑卡时是否校验历史绑卡信息"+checkHistoryBinds);
			
			setSamePerson.put("canSetSamePersonLimit", Boolean.valueOf(belongToOne));
			setSamePerson.put("checkHistoryBinds",Boolean.valueOf(checkHistoryBinds));
		
		return setSamePerson;
	}

	public List<BindCardDTO> judgeBindCardBelongToOne(List<BindCardDTO> bindCards) {
		String owner = null;
		String idCardNo = null;
		List<BindCardDTO> belong2One = new ArrayList<BindCardDTO>();
		for(BindCardDTO bindCard:bindCards){
			if (StringUtils.isNotBlank(bindCard.getOwner())
					&& StringUtils.isNotBlank(bindCard.getIdcard())) {
				if (null == owner&&null == idCardNo) {
					owner = bindCard.getOwner();
					idCardNo = bindCard.getIdcard();
					belong2One.add(bindCard);
				}
				if(!idCardNo.equalsIgnoreCase(bindCard.getIdcard())
						||!owner.equals(bindCard.getOwner())){
					belong2One = null;
					break;
				}
			}else{
				belong2One = null;
				break;
			}
		}
		return belong2One;
	}

	@Override
	public Person getDefautRealPerson(PaymentRequest payRequest) {
		Person person = null;
		PaymentRecord paymentRecord = null;
		List<PaymentRecord> recordList = paymentProcessService
				.findRecordListByOrderOrderId(payRequest.getOrderOrderId(),
						payRequest.getOrderSysNo());
		if (CollectionUtils.isNotEmpty(recordList)
				&& StringUtils.isNotBlank(payRequest.getPaymentOrderNo())) {
			for (PaymentRecord record : recordList) {
				if (payRequest.getPaymentOrderNo().equals(
						record.getPaymentOrderNo())) {
					paymentRecord = record;
					break;
				}
			}
		}
		if (null!=paymentRecord&&CardInfoTypeEnum.TEMP.name().equals(paymentRecord.getPayType())) {
			PayTmpCardDTO payTmpCardDTO = cwhService
					.getPayTmpCardByTempCardId(Long.parseLong(paymentRecord
							.getBindId()));
			if (null!=payTmpCardDTO&&StringUtils.isNotBlank(payTmpCardDTO.getUserCardId())
					&& StringUtils.isNotBlank(payTmpCardDTO.getUserName())) {
				person = new Person();
				person.setIdCardNo(payTmpCardDTO.getUserCardId());
				person.setRealName(payTmpCardDTO.getUserName());
				return person;
			}

		}

		if (null !=paymentRecord&&CardInfoTypeEnum.BIND.name().equals(paymentRecord.getPayType())) {
			BindCardDTO bindCard = cwhService.getBindCardInfoByBindId(Long
					.parseLong(paymentRecord.getBindId()));

			if (null!= bindCard&&StringUtils.isNotBlank(bindCard.getOwner())
					&& StringUtils.isNotBlank(bindCard.getIdcard())) {
				person = new Person();
				person.setIdCardNo(bindCard.getIdcard());
				person.setRealName(bindCard.getOwner());
				return person;
			}

		}

		List<BindCardDTO> bindCards = this.getBindCardList(payRequest);
		if (CollectionUtils.isEmpty(bindCards)) {
			return null;
		}
		for (BindCardDTO bindCard : bindCards) {
			if (StringUtils.isNotBlank(bindCard.getIdcard())
					&& StringUtils.isNotBlank(bindCard.getOwner())) {
				person = new Person();
				person.setIdCardNo(bindCard.getIdcard());
				person.setRealName(bindCard.getOwner());
				person.setBindId(bindCard.getBindId());
				break;
			}
		}
		return person;
	}
	
	@Override
	public List<BindCardDTO> getBindCardBelongsOthers(PaymentRequest paymentRequest) {
		List<BindCardDTO> bindCards = this.getBindCardList(paymentRequest);
		Person person = this.getRealPersonInfo(paymentRequest);
		if(CollectionUtils.isEmpty(bindCards)||null == person){
			return null;
		}
		return this.filterCardBelongtoOther(bindCards, person);
	}
	/**
	 * 获取卡账户设置的同人信息
	 */
	@Override
	public Person getRealPersonInfo(PaymentRequest paymentRequest) {
		Person samePerson = null;
		BindLimitInfoResDTO samePersonInfo = bankCardLimitInfoService.getLimitInfo4bind(paymentRequest);
		if(null ==samePersonInfo){
			return null;
		}
		if(null !=samePersonInfo&&!Constant.MERCHANT_LIMIT_TYPE.equals(samePersonInfo.getBindCardLimitType())){
			if(StringUtils.isBlank(samePersonInfo.getIdentityNoLimit())||StringUtils.isBlank(samePersonInfo.getUserNameLimit())){
				return null;
			}
			if(StringUtils.isNotBlank(samePersonInfo.getIdentityNoLimit())&&StringUtils.isNotBlank(samePersonInfo.getUserNameLimit())){
				samePerson = new Person();
				samePerson.setIdCardNo(samePersonInfo.getIdentityNoLimit());
				samePerson.setRealName(samePersonInfo.getUserNameLimit());
			}
		}
		return samePerson;
		
	}
	/**
	 * 解除绑卡
	 */
	@Override
	public void unbindCards(List<BindCardDTO> bindCards2Others,PaymentRequest paymentRequest,String cause) {
		//1、创建解绑记录 2、调用解绑接口3、更新解绑状态
		if(StringUtils.isNotBlank(paymentRequest.getIdentityId())&&StringUtils.isNotBlank(paymentRequest.getIdentityType())&&CollectionUtils.isNotEmpty(bindCards2Others)){
			for(BindCardDTO bindCard:bindCards2Others){
				this.unbindCard(bindCard, paymentRequest, cause);
			}
		}
		
	}
	/**
	 * 封装解绑请求参数
	 * @param bindId
	 * @param paymentRequest
	 * @param cause
	 * @return
	 */
	private UnbindParamRequestDTO makeUnbindRequest(Long bindId,
			PaymentRequest paymentRequest, String cause) {
		UnbindParamRequestDTO unbindRequest = new UnbindParamRequestDTO();
		unbindRequest.setBindId(bindId);
		unbindRequest.setMerchantAccount(paymentRequest.getMerchantNo());
		unbindRequest.setRemark(cause);
		unbindRequest.setOperatorType(OperatorType.SYSTEM);
		unbindRequest.setOperator("NCCASHIER");
		return unbindRequest;
	}

	/**
	 * 封装查询外部商户请求参数
	 * 
	 * @param paymentRequest
	 * @return
	 */
	private ExternalUserRequestDTO makeExternalRequestDto(PaymentRequest paymentRequest) {
		ExternalUserRequestDTO externalUserRequestDTO = new ExternalUserRequestDTO();

		if (StringUtils.isBlank(paymentRequest.getIdentityId())
				|| StringUtils.isBlank(paymentRequest.getIdentityType())) {
			logger.info("IdentityId或IdentityType为空不需要绑卡支付");
			return null;
		}
		externalUserRequestDTO.setIdentityId(paymentRequest.getIdentityId());
		externalUserRequestDTO
				.setIdentityType(IdentityType.valueOf(paymentRequest.getIdentityType()));
		externalUserRequestDTO.setMerchantAccount(paymentRequest.getMerchantNo());
		return externalUserRequestDTO;
	}

	@Override
	public void unbindCard(BindCardDTO bindCard, PaymentRequest payRequest,
			String cause) {
		if(null == bindCard){
			return;
		}
		UnbindRecord unbindRecord = unbindCardService.create(bindCard.getBindId(),payRequest,cause);
		if(!Constant.SUCCESS.equals(unbindRecord.getStatus())){
			UnbindParamRequestDTO bindParamRequestDTO = makeUnbindRequest(bindCard.getBindId(),payRequest,cause);
			BindParamResponseDTO bindParamResponseDTO = cwhService.unbindCard(bindParamRequestDTO);
			//判断解绑成功与否的逻辑需要再确认
			if(null == bindParamResponseDTO){
				unbindRecord.setStatus("FAILED");
				logger.error("[monitor],event:nccashier_unbindCard error,bindCard:{}",bindCard);
				logger.info("解绑卡失败，调用cwh返回为空，bindId"+bindParamRequestDTO.getBindId());
				unbindRecord.setErrorCode(Errors.SYSTEM_EXCEPTION.getCode());
				unbindRecord.setErrorMsg(Errors.SYSTEM_EXCEPTION.getMsg());
			} else if (StringUtils.isNotBlank(bindParamResponseDTO.getResponseCode()) || StringUtils.isNotBlank(bindParamResponseDTO.getResponseMsg())) {
				unbindRecord.setStatus("FAILED");
				logger.error("[monitor],event:nccashier_unbindCard error,bindCard:{}",bindCard);
				logger.info("解绑卡失败，调用cwh返回失败，bindId"+bindParamRequestDTO.getBindId());
				unbindRecord.setErrorCode(bindParamResponseDTO.getResponseCode());
				unbindRecord.setErrorMsg(bindParamResponseDTO.getResponseCode());
			} else if (null != bindParamResponseDTO.getBindId() && bindParamResponseDTO.getBindId() != 0l) {
				logger.info("该绑卡已解绑成功,绑卡ID"+unbindRecord.getBindId());
				unbindRecord.setStatus("SUCCESS");
				
			}
			unbindRecord.setMerchantNo(payRequest.getMerchantNo());
			unbindRecord.setUpdateTime(new Date());
			unbindCardService.update(unbindRecord);
		}
		logger.info("该绑卡已解绑成功,绑卡ID"+unbindRecord.getBindId());
	}

	@Override
	public void setSamePersonLimit(Person person, PaymentRequest payRequest,String reason) {
		if(null == person){
			logger.error("设置同人限制值时，入参person为null,支付请求ID号为"+payRequest.getId());
			return;
		}
		List<BindCardDTO> bindCards =this.getBindCardList(payRequest);
		if(CollectionUtils.isNotEmpty(bindCards)){
			ExternalUserUpdateRequestDTO externalUser = makeExternalUser(bindCards,person,reason);
			cwhService.setSamePersonLimit(externalUser);
		}
		
	}
	//添加更新原因
	private ExternalUserUpdateRequestDTO makeExternalUser(
			List<BindCardDTO> bindCards, Person person,String reason) {
		ExternalUserUpdateRequestDTO externalUser  = new ExternalUserUpdateRequestDTO();
		externalUser.setId(bindCards.get(0).getUserId().toString());
		externalUser.setUserIdentityNo(person.getIdCardNo());
		externalUser.setUserName(person.getRealName());
		OperationInfoDTO operationInfoDTO = new OperationInfoDTO();
		operationInfoDTO.setOperator(PayProductCode.NCCASHIER);
		operationInfoDTO.setOperatorType(OperatorType.SYSTEM);
		operationInfoDTO.setRemark(reason);
		externalUser.setOperationInfoDTO(operationInfoDTO);
		return externalUser;
	}

	@Override
	public void removeCardBelongtoOther(List<BindCardDTO> bindCardlist,
			Person person) {
		if(CollectionUtils.isNotEmpty(bindCardlist)){
			Iterator<BindCardDTO> iterator = bindCardlist.iterator();
			while (iterator.hasNext()) {
				BindCardDTO bindCard = iterator.next();
//				移除非本人的绑卡
				if((StringUtils.isNotBlank(bindCard.getOwner())&&StringUtils.isNotBlank(bindCard.getIdcard()))){
					if(!person.getRealName().equals(bindCard.getOwner())||!person.getIdCardNo().equalsIgnoreCase(
							bindCard.getIdcard())){
						iterator.remove();
					}
				}
			}
				
		}
		
	
	}
	
	/**
	 * 直接从用户中心查询绑卡列表
	 */
	@Override
	public List<BindCardDTO> getBindCardListFromUserCenter(PaymentRequest payRequest) {
		GetUsableRespDTO getUsableRespDTO = getUseableBindCardList(payRequest);
		return getUsableRespDTO == null ? null : getUsableRespDTO.getUsableBindCard();
	}
	
	/**
	 * 对接用户中心，获取可用绑卡列表
	 * @param payRequest
	 * @return
	 */
	public GetUsableRespDTO getUseableBindCardList(PaymentRequest payRequest){
		if (!validateUsableBindListQuery(payRequest)) {
			return null;
		}
		// TODO 缓存 
		MerchantUserDTO merchantUserDTO = new MerchantUserDTO();
		buildMerchantUser(merchantUserDTO, payRequest);
		try {
			return userCenterService.queryUseableBindBankList(merchantUserDTO);
		} catch (CashierBusinessException e) {
			logger.error("从用户中心获取可用绑卡列表异常", e);
		}
		return null;
	}

	@Override
	public CardInfoDTO getCardInfoDTO(String bindId) {
		//卡信息
		BindCardDTO bindCardDTO = cwhService.getBindCardInfoByBindId(Long.parseLong(bindId));
		if (null == bindCardDTO || bindCardDTO.getStatus() != BindCardStatus.VALID) {
			throw CommonUtil.handleException(Errors.SYSTEM_BINDID_NULL);
		}
		BankCardDetailDTO bankCardDetailDTO = cwhService.getBankCard(bindCardDTO.getCardId());
		if (bankCardDetailDTO == null) {
			throw CommonUtil.handleException(Errors.SYSTEM_BINDID_NULL);
		}
		BaseInfo baseInfo = bankCardDetailDTO.getBaseInfo();
		if (baseInfo == null) {
			throw CommonUtil.handleException(Errors.SYSTEM_BINDID_NULL);
		}
		CardInfoDTO cardInfoDTO = new CardInfoDTO();
		cardInfoDTO.setBankName(baseInfo.getBankName());
		cardInfoDTO.setBankCode(baseInfo.getBankCode());
		cardInfoDTO.setCardno(baseInfo.getCardNo());
		BankCardType bankCardType = baseInfo.getBankCardType();
		cardInfoDTO.setIdno(baseInfo.getIdcard());
		cardInfoDTO.setName(baseInfo.getOwner());
		cardInfoDTO.setPhone(baseInfo.getBankMobile());
		if (BankCardType.CREDITCARD == bankCardType) {
			cardInfoDTO.setCardType(CardTypeEnum.CREDIT.name());
		} else if (BankCardType.DEBITCARD == bankCardType) {
			cardInfoDTO.setCardType(CardTypeEnum.DEBIT.name());
		}
		return cardInfoDTO;
	}

	/**
	 * 校验用户中心可用绑卡列表查询所需入参
	 * 
	 * @param payRequest
	 * @return
	 */
	private boolean validateUsableBindListQuery(PaymentRequest payRequest) {
		if (Constant.YIBAO.equals(payRequest.getMemberType())) {
			if (StringUtils.isBlank(payRequest.getMemberNo())) {
				logger.info("查询三代会员可用绑卡列表memberNo不可为空");
				return false;
			}
		} else {
			if (StringUtils.isBlank(payRequest.getIdentityId()) || StringUtils.isBlank(payRequest.getIdentityType())) {
				logger.info("查询联名账户可用绑卡列表IdentityId或IdentityType不可为空");
				return false;
			}
		}
		return true;
	}

	/**
	 * 构建用户中心可用绑卡列表查询入参对象
	 * 
	 * @param merchantUserDTO
	 * @param payRequest
	 */
	private void buildMerchantUser(MerchantUserDTO merchantUserDTO, PaymentRequest payRequest) {
		if (Constant.YIBAO.equals(payRequest.getMemberType())) {
			merchantUserDTO.setIdentityId(payRequest.getMemberNo());
			merchantUserDTO.setIdentityType(payRequest.getMemberType());
		} else {
			merchantUserDTO.setIdentityId(payRequest.getIdentityId());
			merchantUserDTO.setIdentityType(payRequest.getIdentityType());
			merchantUserDTO.setMerchantNo(payRequest.getMerchantNo());
		}
	}

	@Override
	public boolean canSetSamePersonLimit(List<BindCardDTO> bindCards) {
		List<BindCardDTO> bindCardBelongToOne = judgeBindCardBelongToOne(bindCards);;
		return CollectionUtils.isNotEmpty(bindCardBelongToOne)?true:false;
	}

	@Override
	public List<Person> getPersonFromBindList(List<BindCardDTO> bindCards) {
		logger.info("获取持卡人信息，绑卡列表大小"+bindCards.size());
		String ownerInfo = null;
		String idCardNo =null;
		String owner = null;
		Map<String,String> tempPerson = new HashMap<String,String>();
		List<Person> personInfo = new ArrayList<Person>();
	
		for (BindCardDTO bindCard : bindCards) {
			if (StringUtils.isNotBlank(bindCard.getOwner())
					&& StringUtils.isNotBlank(bindCard.getIdcard())) {
				
				ownerInfo = bindCard.getIdcard()+bindCard.getOwner();
				
					if(null==owner&&null==idCardNo){
						owner = bindCard.getOwner();
						idCardNo	 = bindCard.getIdcard();
						if(null == tempPerson.get(ownerInfo)){
							tempPerson.put(ownerInfo, "1");
							Person temp = new Person();
							temp = new Person(); 
							temp.setIdCardNo(bindCard.getIdcard());
							temp.setRealName(bindCard.getOwner());
							temp.setBindId(bindCard.getBindId());
							personInfo.add(temp);
						}
					}
				
					if (!idCardNo.equalsIgnoreCase(bindCard.getIdcard())
							|| !owner.equals(bindCard.getOwner())) {
						
						if(null == tempPerson.get(ownerInfo)){
							tempPerson.put(ownerInfo, "1");
							Person temp = new Person();
							temp.setIdCardNo(bindCard.getIdcard());
							temp.setRealName(bindCard.getOwner());
							temp.setBindId(bindCard.getBindId());
							personInfo.add(temp);
						}
					}
				}
			}
		return personInfo;
		}


	@Override
	public String bindCard(PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		boolean canSetSamePersonLimit = true;
		if (StringUtils.isBlank(paymentRequest.getIdentityId()) || StringUtils.isBlank(paymentRequest.getIdentityType())) {
			logger.info("IdentityId/IdentityType不完整，不绑卡，requestId = {}",paymentRequest.getId());
			return null;
		}
		if(CardInfoTypeEnum.TEMP.name().equals(paymentRecord.getPayType())){
			List<BindCardDTO> bindCards = this.getBindCardListFromUserCenter(paymentRequest);
			if(CollectionUtils.isNotEmpty(bindCards)){
				canSetSamePersonLimit = this.canSetSamePersonLimit(bindCards);
			}
			String operator = this.getOperator(paymentRequest.getOrderSysNo());
			if(CashierVersionEnum.API.name().equals(paymentRequest.getCashierVersion())){
				logger.info("API首次支付开始组装绑卡请求参数,paymentRequestID="+paymentRequest.getId());
				BindCardInfoRequestDTO requestDto = new BindCardInfoRequestDTO();
				requestDto.setMerchantAccount(paymentRequest.getMerchantNo());
				requestDto.setIdentityId(paymentRequest.getIdentityId());
				BankCardParam bankCardParam = new BankCardParam();
				bankCardParam.setCardNo(paymentRecord.getCardNo());
				bankCardParam.setBusinSystem(BusinSystem.CLICK);//一键支付
				bankCardParam.setType(BankCardAccountType.PRIVATE);//对私
				requestDto.setBankCardParam(bankCardParam);
				requestDto.setIdentityType(IdentityType.valueOf(paymentRequest.getIdentityType()));
				requestDto.setUserType(UserType.EXTERNAL);
				requestDto.setCheckHistoryBindCard(true);
				requestDto.setOperator(operator);
				requestDto.setWriteLimit(!canSetSamePersonLimit);
				return cwhService.bindCard(requestDto);
			}else{
				logger.info("首次支付开始组装绑卡请求参数,paymentRequestID="+paymentRequest.getId());
				BindCardIdRequestDTO requestDto = new BindCardIdRequestDTO();
				requestDto.setMerchantAccount(paymentRequest.getMerchantNo());
				requestDto.setIdentityId(paymentRequest.getIdentityId());
				requestDto.setCardId(paymentRecord.getCardInfoId());
				requestDto.setIdentityType(IdentityType.valueOf(paymentRequest.getIdentityType()));
				requestDto.setUserType(UserType.EXTERNAL);
				requestDto.setOperatorType(OperatorType.SYSTEM);
				requestDto.setCheckHistoryBindCard(true);
				requestDto.setOperator(operator);
				requestDto.setWriteLimit(!canSetSamePersonLimit);
				cwhService.bindCard(requestDto);
			}
		}
		return null;
	}

	
	private String getOperator(String orderSysNo) {
		String bizCode =null;
		Map<String,String>  switchSamePerson = CommonUtil.getSysConfigFrom3G(Constant.CARDINFO_LIMIT_BIZ+orderSysNo);
		if(MapUtils.isNotEmpty(switchSamePerson)){
			bizCode = switchSamePerson.get(orderSysNo);
			if(StringUtils.isBlank(bizCode)){
				logger.info("绑卡时业务方编码未配置");
			}
		}
		return bizCode;
	}

}
