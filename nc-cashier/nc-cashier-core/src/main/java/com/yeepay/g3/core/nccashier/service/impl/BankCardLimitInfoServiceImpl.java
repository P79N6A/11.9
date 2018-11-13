package com.yeepay.g3.core.nccashier.service.impl;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.BankCardLimitInfoService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoReqDTO;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


@Service("bankCardLimitInfoService")
public class BankCardLimitInfoServiceImpl extends NcCashierBaseService
		implements BankCardLimitInfoService {
	private static final Logger logger =
			NcCashierLoggerFactory.getLogger(BankCardLimitInfoServiceImpl.class);
	@Resource
	private CwhService cwhService;
	/**
	 * 获取卡账户限制信息
	 */
	@Override
	public BindLimitInfoResDTO getLimitInfo(PaymentRequest paymentRequest) {
		return getLimitInfo(paymentRequest,Constant.CARD_LIMIT_INFO+paymentRequest.getId());
	}
	
	/**
	 * 获取是否能换卡支付 ——针对商户设置为同人
	 */
	@Override
	public boolean getShowChangeCard(PaymentRequest paymentRequest) {
		
		BindLimitInfoResDTO bindLimitInfoResDTO = this.getLimitInfo(paymentRequest);
		// 不允许新增卡
		if (bindLimitInfoResDTO != null && !bindLimitInfoResDTO.isCanBindNewCard()
				&& !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())) {
			// 查询配置达到绑卡上限是否允许新增卡
			boolean g3AllowFirst = this.judge3GFirstPay(paymentRequest.getMerchantNo(),paymentRequest.getOrderSysNo());
			return g3AllowFirst;
			// 其他情况默认为允许新增卡
		} else {
			return true;
		}
	}
	/**
	 * 判断三代统一配置是否允许换卡支付
	 * @param merchantNo 
	 * @param orderSysNo 
	 * @return
	 */
	@Override
	public boolean judge3GFirstPay(String merchantNo, String orderSysNo) {
		boolean allowChangeCard = false;
		Map<String,String> allow3gFirstPayBizNo = CommonUtil.getSysConfigFrom3G(Constant.ALLOW_FIRST_PAY_BIZ+orderSysNo);
		if(allow3gFirstPayBizNo != null){
			String changeCard = allow3gFirstPayBizNo.get(Constant.CHANGE_CARD_SWITCH);
			String customerNo = allow3gFirstPayBizNo.get(merchantNo);
			logger.info("NCCONFIG配置的换卡支付开关"+changeCard+" "+merchantNo+"在商户白名单中"+customerNo);
			if(Constant.ON.equals(changeCard)){
					if(StringUtils.isBlank(customerNo)){
						allowChangeCard = true;
					}else{
					allowChangeCard = false;
					}
				
			}else if(Constant.OFF.equals(changeCard)){
					if(StringUtils.isNotBlank(customerNo)){
						allowChangeCard = true;
					}
				}
		}
		return allowChangeCard;
		
	}
	/**
	 * 组装获取同人同卡限制信息查询参数
	 * @param paymentRequest
	 * @return
	 */
	private BindLimitInfoReqDTO buildQueryLimitInfoEntity(PaymentRequest paymentRequest) {
		return buildQueryLimitInfoEntity(paymentRequest.getMemberType(),paymentRequest.getMerchantNo(),paymentRequest.getIdentityId(),paymentRequest.getIdentityType(),paymentRequest.getOrderSysNo());
	}

	/**
	 * 组装获取同人同卡限制信息查询参数
	 * @param memberType
	 * @param merchantNo
	 * @param identityId
	 * @param identityType
	 * @param orderSysNo
	 * @return
	 */
	private BindLimitInfoReqDTO buildQueryLimitInfoEntity(String memberType,String merchantNo, String identityId, String identityType, String orderSysNo) {
		//卡账户不支持三代会员的同人同卡限制
		if(Constant.YIBAO.equals(memberType)){
			return null;
		}
		if (StringUtils.isBlank(identityId)
				|| StringUtils.isBlank(identityType)) {
			return null;
		} 
		BindLimitInfoReqDTO reqDTO = new BindLimitInfoReqDTO();
		reqDTO.setMerchantNo(merchantNo);
		reqDTO.setCheckHistory(true);
		reqDTO.setIdentityId(identityId);
		reqDTO.setIdentityType(identityType);
		reqDTO.setBizType(this.getBizCode(orderSysNo));
		return reqDTO;
	}
	
	@Override
	public BindLimitInfoResDTO getSamePersonInfo(PaymentRequest paymentRequest) {
		return getLimitInfo(paymentRequest,Constant.MERCHANT_SAME_PERSON_SET+paymentRequest.getId());
	}
	
	@Override
	public BindLimitInfoResDTO getLimitInfo4bind(PaymentRequest paymentRequest) {
		return getLimitInfo(paymentRequest,Constant.CARD_BIND_LIMIT_INFO+paymentRequest.getId());
	}

	@Override
	public BindLimitInfoResDTO getLimitInfoNoCache(PaymentRequest paymentRequest) {
		BindLimitInfoReqDTO reqDTO = buildQueryLimitInfoEntity(paymentRequest);
		BindLimitInfoResDTO bindLimitInfoResDTO;
		if(reqDTO == null){
			return null;
		}else {
			bindLimitInfoResDTO = cwhService.getBankCardLimitInfo(reqDTO);
		}
		return bindLimitInfoResDTO;
	}

	@Override
	public BindLimitInfoResDTO getLimitInfoNoCache(String merchantNo, String identityId, String identityType, String orderSysNo) {
		BindLimitInfoReqDTO reqDTO = buildQueryLimitInfoEntity(null,merchantNo,identityId,identityType,orderSysNo);
		return cwhService.getBankCardLimitInfo(reqDTO);
	}

	private String getBizCode(String orderSysNo){
		String bizCode = null;
		Map<String,String>  switchSamePerson = CommonUtil.getSysConfigFrom3G(Constant.CARDINFO_LIMIT_BIZ+orderSysNo);
		if(MapUtils.isNotEmpty(switchSamePerson)){
			logger.info("同人开关配置为"+switchSamePerson);
			bizCode = switchSamePerson.get(orderSysNo);
			if(StringUtils.isBlank(bizCode)){
				logger.info("同人开关配置不全，请检查");
			}
		}
		return bizCode;
	}

	private BindLimitInfoResDTO getLimitInfo(PaymentRequest paymentRequest,String cacheKey) {
		BindLimitInfoReqDTO reqDTO = buildQueryLimitInfoEntity(paymentRequest);
		BindLimitInfoResDTO bindLimitInfoResDTO = null;
		if( null==reqDTO){
			return null;
		}
		try{
			//先从Redis获取
			bindLimitInfoResDTO = RedisTemplate.getTargetFromRedis(cacheKey, BindLimitInfoResDTO.class);
			if(bindLimitInfoResDTO != null){
				logger.info("从REDIS查询同人限制值bindLimitInfoResDTO:{}",bindLimitInfoResDTO);
				if(Constant.BINDCARDNULL.equals(bindLimitInfoResDTO.getMobileLimit())){
					return null;
				}else{
					return bindLimitInfoResDTO;
				}

			}
			//不存在去卡账户查
			bindLimitInfoResDTO = cwhService.getBankCardLimitInfo(reqDTO);
			if(bindLimitInfoResDTO !=null){
				RedisTemplate.setCacheObjectSumValue(cacheKey, bindLimitInfoResDTO, Constant.NCCASHIER_CARD_LIMIT_TIME);
			}else{
				BindLimitInfoResDTO resDTOReturn = new BindLimitInfoResDTO();
				resDTOReturn.setMobileLimit(Constant.BINDCARDNULL);
				RedisTemplate.setCacheObjectSumValue(cacheKey,resDTOReturn, Constant.NCCASHIER_CARD_LIMIT_TIME);
			}

		}catch(Throwable e){
			logger.error("[monitor],event:nccashier_getLimitInfo error",e);
		}
		return bindLimitInfoResDTO;
	}

}
