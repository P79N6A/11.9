package com.yeepay.g3.app.nccashier.wap.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.app.nccashier.wap.utils.*;
import com.yeepay.g3.app.nccashier.wap.vo.*;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.util.NullObject;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 新的WAP支付业务service Created by zhen.tan on 16-06-02
 */
@Service("newWapPayService")
public class NewWapPayServiceImpl implements NewWapPayService {

	private static Logger logger = LoggerFactory.getLogger(NewWapPayServiceImpl.class);

	@Resource
	private NcCashierService ncCashierService;


	@Override
	public TradeNoticeDTO queryResult(RequestInfoDTO info, String isRequery) {
		TradeNoticeDTO tradeNoticeDTO = null;

		CashierQueryRequestDTO requestDTO = new CashierQueryRequestDTO();
		requestDTO.setRecordId(info.getPaymentRecordId() == null ? 0 : info.getPaymentRecordId().longValue());
		requestDTO.setRequestId(info.getPaymentRequestId());
		requestDTO.setNeedQueryRedis(true);

		for (int i = 0; i < 5; i++) {
			if(i == 4){
				requestDTO.setNeedQueryRedis(false);
			}
			if ("requery".equals(isRequery)) {
				if (i < 2) {
					tradeNoticeDTO = ncCashierService.queryPayResult(requestDTO);
				} else {
					requestDTO.setRepeatQuery(true);
					tradeNoticeDTO = ncCashierService.queryPayResult(requestDTO);
				}
			} else {
				tradeNoticeDTO = ncCashierService.queryPayResult(requestDTO);
			}
			
			if (null != tradeNoticeDTO && (tradeNoticeDTO.getTradeState() == TradeStateEnum.SUCCESS
					|| tradeNoticeDTO.getTradeState() == TradeStateEnum.FAILED
					|| tradeNoticeDTO.getTradeState() == TradeStateEnum.CANCEL)) {
				return tradeNoticeDTO;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
			}
		}

		return tradeNoticeDTO;
	}


	public void validateCardInfoAndTimeout(CardInfoDTO cardInfo, String token,String bindId) {
		if (StringUtils.isEmpty(token)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		validateCardInfo(bindId,cardInfo);
		this.judgeTimeOut(token);
	}

	@Override
	public ReqSmsSendTypeEnum createPaymentOrSendSMS(String token, String bindId, ReqSmsSendTypeEnum reqSmsSendTypeEnum,
			CardInfoDTO cardInfo) {
		RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
		Long recordId = info.getPaymentRecordId();
		Long requestId = info.getPaymentRequestId();

		ReqSmsSendTypeEnum reqSmsSendTypeEnum1 = null;

		CashierPaymentRequestDTO requestDTO = new CashierPaymentRequestDTO();
		requestDTO.setCardInfo(cardInfo);
		requestDTO.setOrderType(NCCashierOrderTypeEnum.FIRST);
		requestDTO.setRequestId(requestId);
		requestDTO.setRecordId(recordId == null ? 0 : recordId.longValue());
		requestDTO.setTokenId(token);
		logger.info("[monitor],event:nccashierwap_firstCreateOrder_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());
		CashierPaymentResponseDTO response = ncCashierService.createPayment(requestDTO);
		reqSmsSendTypeEnum1 = response.getReqSmsSendTypeEnum();

		info = ncCashierService.requestBaseInfo(token);
		recordId = info.getPaymentRecordId();
		CashierSmsSendRequestDTO smsRequestDTO = new CashierSmsSendRequestDTO();
		smsRequestDTO.setReqSmsSendTypeEnum(reqSmsSendTypeEnum1);
		smsRequestDTO.setRecordId(recordId == null ? 0 : recordId.longValue());
		smsRequestDTO.setTokenId(token);
		smsRequestDTO.setRequestId(requestId);
		logger.info("[monitor],event:nccashierwap_firstSendSms_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());
		ncCashierService.sendSMS(smsRequestDTO);
		return reqSmsSendTypeEnum1;
	}


	@Override
	public Map<String, Object> createPayment4RedirectUrl(String token, CardInfoDTO cardInfo) {
		RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
		Long recordId = info.getPaymentRecordId();
		Long requestId = info.getPaymentRequestId();

		Map<String, Object> result = new HashMap<String, Object>();

		CashierPaymentRequestDTO requestDTO = new CashierPaymentRequestDTO();
		requestDTO.setCardInfo(cardInfo);
		requestDTO.setOrderType(NCCashierOrderTypeEnum.FIRST);
		requestDTO.setRequestId(requestId);
		requestDTO.setRecordId(recordId == null ? 0 : recordId.longValue());
		requestDTO.setTokenId(token);

        requestDTO.setPayRedirectUrl(CommonUtil.getPayRedirectUrl(token));
		requestDTO.setSignRedirectUrl(CommonUtil.getSignRedirectUrl(token,NCCashierOrderTypeEnum.FIRST.name(),""));

		logger.info("[monitor],event:nccashierwap_firstCreateOrder_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());
		CashierPaymentResponseDTO response = ncCashierService.createPayment(requestDTO);

		//仅卡号
		Boolean onlyCardNo = StringUtils.isNotBlank(cardInfo.getCardno()) && StringUtils.isBlank(cardInfo.getName())
                && StringUtils.isBlank(cardInfo.getPhone()) && StringUtils.isBlank(cardInfo.getIdno())
                && StringUtils.isBlank(cardInfo.getCvv2()) && StringUtils.isBlank(cardInfo.getValid())
                && StringUtils.isBlank(cardInfo.getPass());

		if (null != response.getRedirectType() && RedirectTypeEnum.NONE.getValue().equalsIgnoreCase(response.getRedirectType()) && onlyCardNo){
            throw new CashierBusinessException(Errors.ONLYCAEDNO_NOT_SUPPORT.getCode(), Errors.ONLYCAEDNO_NOT_SUPPORT.getMsg());
        }

        setPageRedirectParam(result, response);

        if(null!= response.getReqSmsSendTypeEnum()){
			result.put("smsType", response.getReqSmsSendTypeEnum().name());
		}else{
			result.put("smsType", null);
		}

		return result;
	}

    private void setPageRedirectParam(Map<String, Object> result, CashierPaymentResponseDTO response) {
        if (response != null && null != response.getRedirectType() && !RedirectTypeEnum.NONE.getValue().equalsIgnoreCase(response.getRedirectType())){
            if (StringUtils.isBlank(response.getPageRedirectDTO().getRedirectUrl())){
                throw new CashierBusinessException(Errors.REDIRECT_URL_INVALID.getCode(), Errors.REDIRECT_URL_INVALID.getMsg());
            }
            result.put("pageRedirectParam",response.getPageRedirectDTO() !=null ? response.getPageRedirectDTO() : null);
        }else {
            result.put("pageRedirectParam",null);
        }
    }


    public Map<String, Object> getFirstPayModel(long requestId, String cardNo,
			BusinessTypeEnum businessType, BankCardDTO bindCardDTO) {

		Map<String, Object> result = new HashMap<String, Object>();

		CardValidateRequestDTO requestDTO = new CardValidateRequestDTO();
		requestDTO.setCardno(cardNo);
		requestDTO.setRequestId(requestId);
		NeedValidatesDTO validates = ncCashierService.getCardValidates(requestDTO);

		result.put("needidno", validates.isNeedIdno());
		result.put("needpass", validates.isNeedPass());
		result.put("needname", validates.isNeedName());
		result.put("needmobile", validates.isNeedMobile());
		result.put("needSmsCheck", validates.isNeedSms());
		result.put("needvalid", validates.isNeedValid());
		result.put("needcvv", validates.isNeedCVV());
		result.put("merchantSamePersonConf", validates.isMerchantSamePersonConfig());

		BankCardDTO bc = validates.getBankCard();
		result.put("bc", bc);

		boolean preRouter = validates.isPreRouter();

		//开关为ON,只走预路由
        boolean prerouterSwitch = CommonUtil.getPrerouterSwitch();
        if (prerouterSwitch){
            preRouter = true;
        }


        result.put("preRouter",preRouter);//是否预路由
		Boolean needsupply = validates.isNeedIdno()||validates.isNeedPass()||validates.isNeedName()||validates.isNeedMobile()||validates.isNeedValid()||validates.isNeedCVV();
		result.put("onlycardno",!needsupply);//必填项是否只是卡号
		result.put("haspay", preRouter && !needsupply);//是否直接支付

		if (bc != null) {
			result.put("quata", bc.getBankQuata());

			String owner = validates.getBankCard().getOwner();
			if (StringUtils.isNotBlank(owner)) {
				String name = HiddenCode.hiddenName(owner);
				validates.getBankCard().setOwner(name);
				result.put("owner", name);
			}
			String idno = validates.getBankCard().getIdno();
			if (StringUtils.isNotBlank(idno)) {
				String idNO = HiddenCode.hiddenIdentityCode(idno);
				result.put("idno", idNO);
				validates.getBankCard().setIdno(idNO);
			}
			String phoneNo = validates.getBankCard().getPhoneNo();
			if (StringUtils.isNotBlank(phoneNo)) {
				String pn = HiddenCode.hiddenMobile(phoneNo);
				validates.getBankCard().setPhoneNo(pn);
				result.put("phoneNo", pn);
			}
		}

		String otherCard = null;
		if (businessType == BusinessTypeEnum.FIRSTPAY || businessType == BusinessTypeEnum.FIRSTPASS) {
			List<BankCardDTO> list = new ArrayList<BankCardDTO>();
			list.add(bc);
			otherCard = JSON.toJSONString(list);
		} else if ( bindCardDTO !=null &&
				bindCardDTO.getOtherCards() != null) {
			List<BankCardDTO> otherCards = bindCardDTO.getOtherCards();
			List<BankCardDTO> list = new ArrayList<BankCardDTO>();
			list.add(bc);
			list.addAll(otherCards);
			otherCard = JSON.toJSONString(list);
		}
		result.put("otherCards", otherCard);

		return result;
	}

	/**
	 * 获取选定的绑卡记录
	 * @param bindId
	 * @param bindCardDTO
	 * @return
	 */
	private BankCardDTO getBindCard(String bindId, BankCardDTO bindCardDTO) {
		if (StringUtils.isNotEmpty(bindId)) {
			if(bindId.equals(bindCardDTO.getBindid()+"")){
				return bindCardDTO;
			}
			
			List<BankCardDTO> otherCards = bindCardDTO.getOtherCards();
			if (otherCards != null) {
				boolean ok = false;
				for (BankCardDTO bc : otherCards) {
					if (bindId.equals(bc.getBindid() + "")) {
						return bc;
					}
				}

				if (!ok) {
					throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
				}
			} else {
				throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
			}
		}
		return bindCardDTO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getBindPayModel(RequestInfoDTO info, String token, String bindId, BankCardDTO bindCardDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 调用支付下单，获取短验方式和卡信息补充项
		CashierPaymentResponseDTO response = null;
		BankCardDTO bindCard = null;
		Map<String, ResponseVO> errorInfo = null;
		
		// 用户在绑卡列表页面选择卡支付：下单失败，回到失败页
		if (StringUtils.isNotBlank(bindId) || CollectionUtils.isEmpty(bindCardDTO.getOtherCards())) {
			bindCard = getBindCard(bindId, bindCardDTO);
			CashierPaymentRequestDTO requestDTO = buildCashierPaymentRequestDTO(info, token, bindCard);
			logger.info("[monitor],event:nccashier_bindCreateOrder_request,token:{},merchantNo:{}", token,info.getMerchantNo());
			response = ncCashierService.createPayment(requestDTO);
		}
		// 选择默认的卡进行下单
		else {
			List<BankCardDTO> allBindCards = bindCardDTO.getOtherCards();
			CashierPaymentRequestDTO requestDTO = null;
			
			// 一旦有某张卡下单成功，则将该卡置为选中卡，并返回bind_index页面
			for (BankCardDTO item : allBindCards) {
				try {
					requestDTO = buildCashierPaymentRequestDTO(info, token, item);
					logger.info("[monitor],event:nccashier_bindCreateOrder_request,token:{},merchantNo:{}", token,info.getMerchantNo());

					response = ncCashierService.createPayment(requestDTO);

					//下单后通知跳转
					if (CollectionUtils.isNotEmpty(allBindCards) && allBindCards.size() > 1 && null  != response.getRedirectType() && !RedirectTypeEnum.NONE.name().equalsIgnoreCase(response.getRedirectType())){
					    continue;
                    }

					bindCard = item;
					break;
				} catch (Throwable t) {
					// 将失败的绑卡进行标记，前端页面需要将这些下单失败的卡置为不可下单状态
					logger.error("getBindPayModel e:{}", t);
					errorInfo = (errorInfo == null )? new HashMap<String, ResponseVO>() : errorInfo;
					CashierBusinessException ex = (t instanceof CashierBusinessException) ? (CashierBusinessException)t : new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
					errorInfo.put(String.valueOf(item.getBindid()), new ResponseVO(ex.getDefineCode(), ex.getMessage()));
					response = null;
				}
			}
			RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_CREATEPAYMENT_FAILUREMAP + token,
					errorInfo == null ? new HashMap<String, ResponseVO>() : errorInfo,
					Constant.NCCASHIER_CREATEPAYMENT_FAILUREMAP_REDIS_TIME_LIMIT);
		}

		//跳转参数
        setPageRedirectParam(result, response);


        // 不管什么情景，跳转到绑卡列表页面都需要展示所有卡表
		List<BankCardDTO> otherCards = bindCardDTO.getOtherCards();
		result.put("otherCards", CollectionUtils.isNotEmpty(otherCards) ? JSON.toJSONString(otherCards) : null);
		if(MapUtils.isEmpty(errorInfo)){
			errorInfo = RedisTemplate.getTargetFromRedis(
					Constant.NCCASHIER_CREATEPAYMENT_FAILUREMAP + token, HashMap.class);
		}
		result.put("createPaymentError", MapUtils.isNotEmpty(errorInfo) ? JSON.toJSONString(errorInfo) : null);
		
		// 所有卡都下单失败，回到bind_index.vm
		// 该页面所有卡的createPaymentRes都为false，将换卡支付置为选中态，页面显示卡号&下一步
		// 而不是回到失败页
		// 此时页面无需展示短信验证类型、卡信息必填项、此时bindCard肯定也是空的
		if (response == null) {
			return result;
		}

		// 当某一张卡下单成功，需要展示短信验证类型、卡信息必填项、当前选中的绑卡bindCard
		ReqSmsSendTypeEnum smsType = response.getReqSmsSendTypeEnum();
		if (smsType != null) {
			result.put("smsType", smsType.name());
		} else {
			result.put("smsType", null);
		}

		// 设置当前下单的绑卡信息
		result.put("bc", bindCard);
		// 处理卡必填项
		NeedBankCardDTO needBankCardDTO = response.getNeedBankCardDto();
		// 将敏感信息掩码
		needBankCardDTO = hiddenNeedBankCardDTO(needBankCardDTO);
		// 设置补充项信息
		result.put("needCardInfo", needBankCardDTO);

		return result;
	}
	
	
	/**
	 * 创建支付下单请求参数
	 * 
	 * @param info
	 * @param token
	 * @param bindCard
	 * @return
	 */
	private CashierPaymentRequestDTO buildCashierPaymentRequestDTO(RequestInfoDTO info, String token,
			BankCardDTO bindCard) {
		CashierPaymentRequestDTO requestDTO = new CashierPaymentRequestDTO();
		requestDTO.setOrderType(NCCashierOrderTypeEnum.BIND);
		requestDTO.setRequestId(info.getPaymentRequestId());
		if (info.getPaymentRecordId() != null) {
			requestDTO.setRecordId(info.getPaymentRecordId());
		}
		requestDTO.setTokenId(token);
		requestDTO.setBindId(bindCard.getBindid());
		requestDTO.setPayRedirectUrl(CommonUtil.getPayRedirectUrl(token));
		requestDTO.setSignRedirectUrl(CommonUtil.getSignRedirectUrl(token,NCCashierOrderTypeEnum.BIND.name(),String.valueOf(requestDTO.getBindId())));
		return requestDTO;
	}

	/**
	 * 对补充项中有值的项进行掩码处理
	 * @param needBankCardDTO
	 * @return
	 */

	public NeedBankCardDTO hiddenNeedBankCardDTO(NeedBankCardDTO needBankCardDTO){
		NeedBankCardDTO response = new NeedBankCardDTO();
		BeanUtils.copyProperties(needBankCardDTO, response);
		//卡号
		if(StringUtils.isNotBlank(response.getCardno())){
			response.setCardno(HiddenCode.hiddenBankCardNO(response.getCardno()));
		}
		//持卡人姓名
		if(StringUtils.isNotBlank(response.getOwner())){
			response.setOwner(HiddenCode.hiddenName(response.getOwner()));
		}
		//证件号
		if(StringUtils.isNotBlank(response.getIdno())){
			response.setIdno(HiddenCode.hiddenIdentityCode(response.getIdno()));
		}
		//手机号
		if(StringUtils.isNotBlank(response.getPhoneNo())){
			response.setPhoneNo(HiddenCode.hiddenMobile(response.getPhoneNo()));
		}
		return response;
	}
	
	@Override
	public CashierPayResponseDTO firstPay(RequestInfoDTO info, String token, CardInfoDTO cardInfo,
			String verifycode) {
		CashierFirstPayRequestDTO firstPayRequest = new CashierFirstPayRequestDTO();
		firstPayRequest.setCardInfo(cardInfo);
		firstPayRequest.setTokenId(token);
		firstPayRequest.setVerifycode(verifycode);
		firstPayRequest.setPaymentRecordId(info.getPaymentRecordId());
		firstPayRequest.setPaymentRequestId(info.getPaymentRequestId());
		logger.info("[monitor],event:nccashierwap_firstConfirmSms_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());
		return ncCashierService.firstPay(firstPayRequest);
	}


	@Override
	public CashierPayResponseDTO bindPay(RequestInfoDTO info, String token, String bindId,
			NeedBankCardDTO needBankCardDTO, String verifycode) {
		CashierBindPayRequestDTO bindPayRequest = new CashierBindPayRequestDTO();
		bindPayRequest.setBindId(bindId);
		//当手机号包含*时，调用needBankCardDTO.toString时，该方法调用HiddenCode.hideMobile时，会因为*是正则表达式的一个特殊字符而报错
		handleMobileOfNeedBankCardDTO(needBankCardDTO);
		bindPayRequest.setNeedBankCardDTO(needBankCardDTO);
		bindPayRequest.setTokenId(token);
		bindPayRequest.setVerifycode(verifycode);
		bindPayRequest.setPaymentRecordId(info.getPaymentRecordId());
		bindPayRequest.setPaymentRequestId(info.getPaymentRequestId());
		logger.info("[monitor],event:nccashierwap_bindConfirmSms_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());
		return ncCashierService.bindPay(bindPayRequest);
	}

	@Override
	public void validateCardInfo(String bindId,CardInfoDTO cardInfo) {
		if (cardInfo == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		
		if (StringUtils.isNotEmpty(cardInfo.getName()) && !cardInfo.getName().contains("*") 
				&& !DataUtil.isChinese(cardInfo.getName())) {
			throw new CashierBusinessException(Errors.INVALID_NAME.getCode(), Errors.INVALID_NAME.getMsg());
		}
		if (StringUtils.isNotBlank(cardInfo.getPhone()) && !cardInfo.getPhone().contains("*")
				&& !DataUtil.isMobile(cardInfo.getPhone())) {
			throw new CashierBusinessException(Errors.INVALID_PHONE.getCode(), Errors.INVALID_PHONE.getMsg());
		}
		try {
			if (StringUtils.isNotBlank(cardInfo.getIdno()) && !cardInfo.getIdno().contains("*")
					&& !"".equals(DataUtil.IDCardValidate(cardInfo.getIdno().toLowerCase()))) {
				throw new CashierBusinessException(Errors.INVALID_IDNO.getCode(), Errors.INVALID_IDNO.getMsg());
			}
		} catch (ParseException e) {
			throw new CashierBusinessException(Errors.INVALID_IDNO.getCode(), Errors.INVALID_IDNO.getMsg());
		}
	}
	
	
	private void handleMobileOfNeedBankCardDTO(NeedBankCardDTO needBankCardDTO){
		if(needBankCardDTO==null) return;
		if(StringUtils.isNotBlank(needBankCardDTO.getPhoneNo()) && needBankCardDTO.getPhoneNo().startsWith("*******")){
			needBankCardDTO.setPhoneNo("#######");
		}
	}


	@Override
	public void validateBindCardAndTimeout(
			NeedBankCardDTO needBankCardDTO, String token, String bindId) {
		if (StringUtils.isEmpty(token)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		if (StringUtils.isEmpty(bindId)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		
		this.judgeTimeOut(token);
	}


	@Override
	public void bindSendSMS(ReqSmsSendTypeEnum smsType,NeedBankCardDTO needBankCardDTO,
			String token, String bindId) {
		RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
		Long recordId = info.getPaymentRecordId();
		Long requestId = info.getPaymentRequestId();
		
		CashierSmsSendRequestDTO requestDTO = new CashierSmsSendRequestDTO();
		requestDTO.setReqSmsSendTypeEnum(smsType);
		requestDTO.setRecordId(recordId == null ? 0 : recordId.longValue());
		requestDTO.setTokenId(token);
		requestDTO.setRequestId(requestId);
		if(!NullObject.objectIsNull(needBankCardDTO)){
			requestDTO.setNeedBankCardDTO(needBankCardDTO);
		}
		logger.info("[monitor],event:nccashierwap_bindSendSms_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());

		ncCashierService.sendSMS(requestDTO);
	}
	
	/**
	 * 判读发短验是否超时
	 * @param token
	 */
	private void judgeTimeOut(String token){
		Long time = RedisTemplate.getTargetFromRedis(Constant.NCCASHIER_SMS_SEND_TIME_KEY + token,
				Long.class);
		if (null != time) {
			long proTime = 30 - (System.currentTimeMillis() - time) / 1000;
			if (proTime > 0 && proTime <= 30) {
				throw new CashierBusinessException(Errors.GET_SMS_FREQUENT.getCode(),
						"获取短验频繁，请" + proTime + "秒后再试");
			}
		}
	}

	public String directPay(String token,String payType) {
		RequestInfoDTO info = ncCashierService.requestBaseInfo(token);
		WeChatPayRequestDTO requestDTO = new WeChatPayRequestDTO();
		requestDTO.setTokenId(token);
		requestDTO.setRequestId(info.getPaymentRequestId());
		requestDTO.setRecordId(info.getPaymentRecordId()==null?0:info.getPaymentRecordId().longValue());
		requestDTO.setPayType(payType);
		String url = ncCashierService.WeChatPay(requestDTO);
		return url;
	}

	@Override
	public Map<String, Object> createPayment(String token, String bindId,
			RequestInfoDTO info,BankCardDTO bindCardDTO) {
		NeedBankCardDTO needBankCardDTO = null;
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isEmpty(token)){
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		//创建支付下单请求参数：订单类型，支付请求ID，支付记录ID，tokenId和绑卡ID
		CashierPaymentRequestDTO requestDTO = new CashierPaymentRequestDTO();
		requestDTO.setOrderType(NCCashierOrderTypeEnum.BIND);
		requestDTO.setRequestId(info.getPaymentRequestId());
		if (info.getPaymentRecordId() != null) {
			requestDTO.setRecordId(info.getPaymentRecordId());
		}
		requestDTO.setTokenId(token);
		if(bindCardDTO == null){
			requestDTO.setBindId(Long.parseLong(bindId));
		}else{
			//根据bindId获取绑卡记录
			BankCardDTO bindCard = getBindCard(bindId, bindCardDTO);
			requestDTO.setBindId(bindCard.getBindid());
		}

		requestDTO.setPayRedirectUrl(CommonUtil.getPayRedirectUrl(token));
		requestDTO.setSignRedirectUrl(CommonUtil.getSignRedirectUrl(token,NCCashierOrderTypeEnum.BIND.name(),String.valueOf(requestDTO.getBindId())));

		//调用支付下单，获取短验方式和卡信息补充项
		logger.info("[monitor],event:nccashierwap_bindCreateOrder_request,requestId:{},merchantNo:{}", info.getPaymentRequestId(),info.getMerchantNo());
		CashierPaymentResponseDTO response = ncCashierService.createPayment(requestDTO);
		 needBankCardDTO = response.getNeedBankCardDto();
		 if(null!=needBankCardDTO){
			 needBankCardDTO =  this.hiddenNeedBankCardDTO(needBankCardDTO);
		 }
		 if(null!= response.getReqSmsSendTypeEnum()){
			 result.put("smsType", response.getReqSmsSendTypeEnum().name());
		 }else{
			 result.put("smsType", null);
		 }
		 result.put("needBankCardDTO", needBankCardDTO);

		//处理跳转页面
        setPageRedirectParam(result, response);

        return result;
	}

	@Override
	public Map<String, Object> getCardValidates(Long requestId, String cardno) {
		Map<String, Object> result = new HashMap<String, Object>();

		CardValidateRequestDTO requestDTO = new CardValidateRequestDTO();
		requestDTO.setCardno(cardno);
		requestDTO.setRequestId(requestId);
		NeedValidatesDTO validates = ncCashierService.getCardValidates(requestDTO);
		if(null !=validates&&null!=validates.getBankCard()){
			result.put("status", "success");
		}else{
			result.put("status", "failed");
			result.put("error", "不支持此卡，请查看页面下方支持银行卡列表");
		}
		return result;
	}


	@Override
	public CarnivalVO queryQualification4Carnival(TradeNoticeDTO tradeNoticeDTO) {
		CarnivalVO carnivalVO = new CarnivalVO();
		boolean canJoin =false;
		String tradeSysNo = tradeNoticeDTO.getTradeSysNo();
		if(CarnivalUtil.systemMap.keySet().contains(tradeSysNo))	//系统筛选  一键\大算\2代
		{
			//金额判断
			String amount = CommonUtil.getJnhMinAmount();
			if(tradeNoticeDTO.getPaymentAmount().compareTo(new BigDecimal(amount))>=0){	//订单金额大于等于1000元能参加抽奖
				logger.info("交易金额大于等于1000元能参加嘉年华活动"+tradeNoticeDTO.getMerchantNo());
				//根据白名单筛选商户
				List<String> whiteCheckList = CommonUtil.getJnhWhiteCheckList();
				if(CollectionUtils.isNotEmpty(whiteCheckList) && whiteCheckList.contains(tradeNoticeDTO.getMerchantNo())){
					//时间判断
					canJoin = ncCashierService.queryQualification4Carnival(tradeNoticeDTO.getMerchantNo());
				}
			}
		}
		if(canJoin){
			carnivalVO.setCarnivalUrl("http://fun.yeepay.com?r="+tradeNoticeDTO.getMerchantOrderId()+
					"&s="+tradeNoticeDTO.getMerchantNo()+"&b="+CarnivalUtil.systemMap.get(tradeSysNo));
		}
		carnivalVO.setShowCarnival(canJoin);
		return carnivalVO;
	}


	@Override
	public SuccessActivateVO querySuccessActivate(TradeNoticeDTO tradeNoticeDTO) {
		SuccessActivateVO successVo = new SuccessActivateVO();
		boolean canJoin =false;
		String tradeSysNo = tradeNoticeDTO.getTradeSysNo();
		if(CarnivalUtil.systemMap.keySet().contains(tradeSysNo))	//系统筛选  一键\大算\2代
		{
			//根据白名单筛选商户
			List<String> merchantNoList = CommonUtil.getSuccessActivateMerchantNoList();
			if (CollectionUtils.isNotEmpty(merchantNoList) && merchantNoList.contains(tradeNoticeDTO.getMerchantNo())){
				//时间判断
				canJoin = CommonUtil.getSuccessActivitiesCanJoin();
			}
		}

		if(canJoin){
			String successActivitiesUrl = CommonUtil.getSuccessActivitiesUrl();
			//活动地址
			successVo.setSuccessActUrl(successActivitiesUrl);
		}
		successVo.setShowSuccessActivate(canJoin);
		return successVo;
	}

	@Override
	public void setCardOwner(String bindId, String name, String idCardNo,RequestInfoDTO info) {
			if(StringUtils.isNotBlank(bindId)){
				ncCashierService.setCardOwner(bindId,null,null,info.getPaymentRequestId());
			}
			if(StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(idCardNo)){
				ncCashierService.setCardOwner(null,name,idCardNo,info.getPaymentRequestId());
			}
	}


	@Override
	public Map<String, String> getPayFailErrors() {
		
		Map<String, String> businessInfoMap = null;
		try{
			businessInfoMap = CommonUtil.getSysConfigFrom3G(Constant.PAY_FAIL_OWNER_REASONS);
		}catch(Throwable e){
			logger.error("获取因支付身份导致交易失败的错误码时异常",e);
		}
		return businessInfoMap;
	}


	

	@Override
	public PayResultQueryStateListenVO listenCanPayResultQuery(String token) {
		// 1 校验token，并获取订单基本信息
		RequestInfoDTO requestInfo = validateRequestInfoDTO(token);
		// 2 构造支付结果可查标识监听接口的入参
		PayResultQuerySignListenRequestDTO request = new PayResultQuerySignListenRequestDTO();
		request.setPaymentRecordId(
				requestInfo.getPaymentRecordId() == null ? 0 : requestInfo.getPaymentRecordId().longValue());
		request.setToken(token);
		// 3 调用core的支付结果可查标识监听接口
		PayResultQuerySignListenResponseDTO response = ncCashierService.listenCanPayResultQuery(request);
		// 4 构造返回值
		PayResultQueryStateListenVO result = new PayResultQueryStateListenVO();
		result.setToken(token);
		result.setQueryState(response.isQueryState());
		return result;
	}


	@Override
	public TradeNoticeDTO queryPayResult(RequestInfoDTO info, String token) {
		CashierQueryRequestDTO requestDTO = new CashierQueryRequestDTO();
		requestDTO.setRecordId(
				info.getPaymentRecordId() == null ? 0 : info.getPaymentRecordId().longValue());
		requestDTO.setRequestId(info.getPaymentRequestId());
		return ncCashierService.queryPayResult(requestDTO);
	}
	
	
	/**
	 * 校验token 获取订单基本信息
	 * @param token
	 * @return
	 */
	@Override
	public RequestInfoDTO validateRequestInfoDTO(String token){
		if (StringUtils.isBlank(token)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		return ncCashierService.requestBaseInfo(token);
	}


	@Override
	public CardOwnerConfirmResDTO getOwnersInfo(RequestInfoDTO info) {
		logger.info("获取支付身份信息的订单信息info:{}", info);
		CardOwnerConfirmResDTO cardOwners = new CardOwnerConfirmResDTO();
		cardOwners.setNeedChooseCardOwner(false);
		List<Person> persons = new ArrayList<Person>();
		try {
			if (null != info.getPaymentRequestId()
					&& info.getPaymentRequestId() > 0
					&& null != info.getPaymentRecordId()
					&& info.getPaymentRecordId() > 0) {

				cardOwners.setPaymentRequestId(info.getPaymentRequestId());
				CardOwnerConfirmResDTO owners = ncCashierService.getOwnersInfo(
						info.getPaymentRequestId(), info.getPaymentRecordId());
				if (null != owners) {
					if (owners.isNeedChooseCardOwner()
							&& null != owners.getPersons()) {
						for (Person personInfo : owners.getPersons()) {
							Person person = new Person();
							String hiddenIdCard = HiddenCode
									.hiddenIdentityCode(personInfo
											.getIdCardNo());
							String hiddenName = HiddenCode
									.hiddenName(personInfo.getRealName());
							person.setIdCardNo(hiddenIdCard);
							person.setRealName(hiddenName);
							person.setBindId(personInfo.getBindId());
							persons.add(person);
						}
						cardOwners.setNeedChooseCardOwner(owners
								.isNeedChooseCardOwner());
						cardOwners.setShowTimes(owners.getShowTimes());
					}
				}
				cardOwners.setPersons(persons);

			}
		} catch (Throwable e) {
			logger.error("获取支付身份信息报错支付请求ID：" + info.getPaymentRequestId()
					+ "支付记录ID：" + info.getPaymentRecordId(), e);
		}
		logger.info("返回的支付身份信息，cardOwners:{}", cardOwners);
		return cardOwners;
	}


	@Override
	public void unbindCard(Long paymentRequestId, Long paymentRecordId) {
		if(null!=paymentRequestId&&paymentRequestId>0&&null!=paymentRecordId&&paymentRecordId>0){
			ncCashierService.unbindCard(paymentRequestId,paymentRecordId);
		}
	}

	@Override
	public void unbindCard(String paymentRequestId, String bindId) {
		if(StringUtils.isBlank(paymentRequestId) || !StringUtils.isNumeric(paymentRequestId) ||
				StringUtils.isBlank(bindId) || !StringUtils.isNumeric(bindId)){
			logger.error("unbindCard() param error : paymentRequestId={},bindId={}",paymentRequestId,bindId);
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
		}
		ncCashierService.unbindCardActive(paymentRequestId,bindId);
	}

	@Override
	public boolean checkAbleToUnbindCard(String merchantNo) {
		String customizedMerchant = CommonUtil.getSysConfigFrom3G(ConstantUtil.WEB_UNBIND_CARD_MERCHANT_NO);
		if(StringUtils.isNotBlank(customizedMerchant) && customizedMerchant.contains(merchantNo)){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 微信h5低配版的预路由逻辑
	 * @param extendInfo
	 * @param info
	 * @param token
	 * @return 微信授权地址(用于下一步在微信中打开)；返回null或抛出异常，则终止下单
	 */
	@Override
	public String wechatH5PreRouter(PayExtendInfo extendInfo, RequestInfoDTO info, String token, HttpServletRequest request) throws Exception {
		if (extendInfo == null || info == null || StringUtils.isBlank(token)) {
			return null;
		}
		JsapiRouteResponseDTO jsapiRouteResponseDTO = ncCashierService.getAppid(Long.toString(info.getPaymentRequestId()), PayTypeEnum.WECHAT_OPENID.name());
		if (jsapiRouteResponseDTO == null || !Constant.PRE_ROUTE_STATUS_SUCCESS.equals(jsapiRouteResponseDTO.getDealStatus())) {
			logger.error("wechatH5PreRouter() 微信h5预路由失败，requestId={},预路由结果={}",info.getPaymentRequestId(),jsapiRouteResponseDTO);
			return null;
		}
		if(Constant.PRE_ROUTE_SCENE_TYPE_EXT_JSAPIH5.equals(jsapiRouteResponseDTO.getSceneTypeExt())){
			//预路由返回jsapi通道，可以直接下单，无需openid
			String url = directPay(token, PayTypeEnum.WECHAT_H5_LOW.name());
			return url;
		}
		//以下为normal通道
		if (StringUtils.isBlank(jsapiRouteResponseDTO.getAppId())) {
			return null;
		}
		if (StringUtils.isNotBlank(jsapiRouteResponseDTO.getAppSecret()) ||
				StringUtils.isNotBlank(CommonUtil.getWechatH5LowMerchantRedirect(info.getMerchantNo()))) {
			//由于微信授权接口url过长，使用收银台的中转链接
			return CommonUtil.getPreUrl(info.getMerchantNo(),request) + "/newwap/jumpwx?token=" + token;
		} else {
			return null;
		}
	}

	@Override
	public String alipayLifeNoPreRouteIgnoreOriUserId(String paymentRequestId, String token) throws Exception {
		// 预授权
		JsapiRouteResponseDTO jsapiRouteResponseDTO = ncCashierService.getAppid(paymentRequestId,
				PayTypeEnum.ZFB_SHH.name());
		if (jsapiRouteResponseDTO == null
				|| !Constant.PRE_ROUTE_STATUS_SUCCESS.equals(jsapiRouteResponseDTO.getDealStatus())) {
			// 预路由失败 抛异常
			logger.error("预路由失败，无法使用支付宝标准版,token={}", token);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		// 解析支付宝授权地址
		String alipayAuth2Url = WaChatPayUtils.parseAlipayLifeNoAuth2Url(token, PayTypeEnum.ALIPAY_H5_STANDARD);
		if (StringUtils.isBlank(alipayAuth2Url)) {
			logger.error("无法使用支付宝标准版,token={}，支付宝授权地址不可为空", token);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		return alipayAuth2Url;
	}

	@Override
	public AlipayStandardJumpInfo alipayStandardPreHandleBeforeJumping(String paymentRequestId, String token,
			String merchantNo) throws Exception {
		String alipayAuth2Url = alipayLifeNoPreRouteIgnoreOriUserId(paymentRequestId, token);
		String authKey = CommonUtil.getAuthKeyOfAlipayTransferDoor();
		if (StringUtils.isBlank(authKey)) {
			logger.error("无法使用支付宝标准版,token={},商编={}，传送门key不可为空", token, merchantNo);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		// 传送标识
		WaChatPayUtils.recordEwalletH5PackingSiginal(token, PayTypeEnum.ALIPAY_H5_STANDARD.name());
		AlipayStandardJumpInfo jumpInfo = new AlipayStandardJumpInfo();
		jumpInfo.setAlipayAuth2Url(alipayAuth2Url);
		jumpInfo.setAuthKey(authKey);
		jumpInfo.setStatus("success");
		jumpInfo.setNeedRedirect(true);
		return jumpInfo;
	}
}
